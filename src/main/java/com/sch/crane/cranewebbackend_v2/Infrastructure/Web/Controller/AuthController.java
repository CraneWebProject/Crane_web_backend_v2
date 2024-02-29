package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller;

import com.sch.crane.cranewebbackend_v2.Data.DTO.User.LoginDto;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserRole;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Response.LoginResponse;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.Security.TokenProvider;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.ResponseMessage;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.StatusCode;
import com.sch.crane.cranewebbackend_v2.Service.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpHead;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.beans.Encoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final UserService userservice;
//    private final RedisUtil redisUtil;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    LoginResponse loginResponse = new LoginResponse();


    //TODO: service 에서 처리 가능한 코드는 service 에서 처리 후 예외로 처리 할 것.
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDto loginDto, HttpServletResponse response){

        Optional<User> optionalUser = userservice.findUserByEmail(loginDto.getUserEmail());

        //email이 존재하지 않는경우
        if(!optionalUser.isPresent()){
            loginResponse = LoginResponse.builder()
                    .code(StatusCode.UNAUTHORIZED)
                    .message(ResponseMessage.LOGIN_FAILED)
                    .build();
            return new ResponseEntity<>(loginResponse, HttpStatus.BAD_REQUEST);
        }
        User user = optionalUser.get();
        //TODO:password 틀리면 알려주고, 특정 횟수 이상 틀리면 비밀번호 초기화로 구현 할 것.
        // password 틀린 횟수를 저장하는 필드를 어떤 엔티티에 둘 것인지 결정 필요.
        //password 틀린경우
        if(!passwordEncoder.matches(loginDto.getUserPassword(), user.getUserPassword())){
            loginResponse = LoginResponse.builder()
                    .code(StatusCode.UNAUTHORIZED)
                    .message(ResponseMessage.LOGIN_FAILED)
                    .build();
            return new ResponseEntity<>(loginResponse, HttpStatus.BAD_REQUEST);
        }

        List<String> userRoleList = new ArrayList<>();

        if(user.getUserRole() == null){
            userRoleList.add(UserRole.ROLE_STAN_BY.toString());
        }else {
            userRoleList.add(user.getUserRole().toString());//userRole이 List<String>으로 요구되어 해당 방식으로 변환

        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUserEmail(), loginDto.getUserPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        String accessToken = tokenProvider.createToken(authentication);
//        String refreshToken = tokenProvider.createToken(user.getUserEmail(), userRoleList, REFRESH_TOKEN_TIME);
//        redisUtil.setValues(refreshToken, user.getUserEmail());


//        Cookie responseCookie = new Cookie("refreshToken", refreshToken);
//        responseCookie.setMaxAge(7*24*60*60);
//        responseCookie.setHttpOnly(true);
//        responseCookie.setSecure(true);
//        responseCookie.setPath("/");
//        response.addCookie(responseCookie);

        Cookie accessCookie = new Cookie("accessToken", accessToken);
        accessCookie.setMaxAge(5*60);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);
        accessCookie.setPath("/");
        response.addCookie(accessCookie);


        loginResponse = LoginResponse.builder()
                .code(StatusCode.OK)
                .message(ResponseMessage.LOGIN_SUCCESS)
                .build();

        return ResponseEntity.ok(loginResponse);


    }
}
