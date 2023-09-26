package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.JWT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.Security.TokenProvider;
import com.sch.crane.cranewebbackend_v2.Service.Exception.ErrorResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = tokenProvider.resolveToken(request);

        if(token != null){
            if(!tokenProvider.validateToken(token)){  // JWT 토큰이 올바르지 않으면 예외를 처리를한다
                jwtExceptionHandler(response, "Token Error", HttpStatus.UNAUTHORIZED.value());
                return;
            }
            Claims info = tokenProvider.getUserInfoFromToken(token); //JWT 토큰으로부터 추출한 사용자 정보를 Claims 객체로 반환한다
            setAuthentication(info.getSubject());// Security Context와 Authentication 객체를 생성한다
        }
        filterChain.doFilter(request,response);// 필터 체인의 다음 필터를 실행한다
    }
    public void setAuthentication(String userEmail) { // Context와 Authentication 객체를 생성하는 메소드
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = tokenProvider.createAuthentication(userEmail);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }
    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) { // 예외 발생시 처리하는 메서드
        response.setStatus(statusCode);
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(ErrorResponse.of(statusCode,msg));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    // 실제 필터링 로직 작성
    // doFilter : 토큰의 인증 정보를 SecurityContext에 저장
//    @Override
//    public void doFilter(ServletRequest servletRequest,
//                         ServletResponse servletResponse,
//                         FilterChain filterChain) throws IOException, ServletException {
//
//        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
//        String jwt = resolveToken(httpServletRequest);
//        String requestURI = httpServletRequest.getRequestURI();
//
//        if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)){
//            Authentication authentication = tokenProvider.getAuthentication(jwt);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            logger.debug("SecurityContext에 '{}'인증정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
//        } else {
//            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
//        }
//        // 생성한 필터 실행
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//
//    // Request Header에서 토큰 정보를 꺼내오기
//    private String resolveToken(HttpServletRequest request){
//        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
//
//        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
}
