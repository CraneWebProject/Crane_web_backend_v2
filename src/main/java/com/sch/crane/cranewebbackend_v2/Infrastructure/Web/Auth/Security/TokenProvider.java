package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
@AllArgsConstructor
public class TokenProvider implements InitializingBean {
//    private  final long ACCESS_EXPIRATION_TIME; // 1일

//    public static final String AUTHORIZATION_HEADER = "Authorization";
    @Value("${spring.jwt.secret}")
    public String secretKey;

    @Value("${spring.jwt.access-expiration-time}")
    public Long ACCESS_EXPIRATION_TIME;

    public static final String AUTHORIZATION_KEY = "auth";
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

    //1. Bean 생성 후 주입 받은 후 할당
//    public TokenProvider(){
//
//    }

    @Override
    public void afterPropertiesSet() throws Exception   {
//    secret값을 Base64 Decode해서 key 변수에 할당하기 위해 사용
        byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(secretKeyBytes);
    }

//    @PostConstruct
//    protected void init() {
////        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
//
//    }

    public String createToken(Authentication authentication /*, Long TOKEN_TIME*/) {
        //Refresh Token 생성 시 위해 TokenTime 인자 생성

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        System.out.println("권한: " + authorities);

        long now = (new Date()).getTime();
        Date validity = new Date(now + ACCESS_EXPIRATION_TIME);

        return Jwts.builder()
                .claim(AUTHORIZATION_KEY, authorities)
                .setSubject(authentication.getName())
                .setIssuedAt(new Date(now))
                .setExpiration(validity)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    //Token으로 Claim을 만들고 이를 이용해 유저 객체 만들어 authentication 객체 리턴
    public Authentication getAuthentication(String token){
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORIZATION_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new).toList();

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }


    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }

        return false;
    }

    public boolean isTokenExpired(String token){
        try{
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return claims.getExpiration().before(new Date());
        }catch (ExpiredJwtException e){
            return true;
        }
    }


//    public String resolveToken(HttpServletRequest request) {
//
////        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
////
////        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
////            return bearerToken.substring(8);
////        }
//        return request.getHeader(AUTHORIZATION_HEADER);
//    }

//    public String resolveACToken(HttpServletRequest request){
//        return TokenCookieUtil.extractTokenFromCookie(request, "accessToken");
//    }

//    public String resolveRFToken(HttpServletRequest request){
//        return TokenCookieUtil.extractTokenFromCookie(request, "refreshToken");
//    }









//    public TokenResponse refreshToken(String refreshToken) {
//        Long tokenValidTime = 1000 * 60 * 60l;
//        Long RefreshExpireTimeMs = 1000 * 60 * 60 * 60L;
//        // Check if the refresh token exists in Redis
//        if (redisUtil.existData(refreshToken)) {
//            // Get the email associated with the refresh token from Redis
//            String userEmail = redisUtil.getData(refreshToken);
//
//            // Get the user details from the userDetailsService
//            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
//
//            // Generate a new access token
//            String newAccessToken = createToken(userDetails.getUsername(), getRolesFromUserDetails(userDetails), tokenValidTime);
//            String newRefreshToken = createToken(userDetails.getUsername(), getRolesFromUserDetails(userDetails), RefreshExpireTimeMs);
//
//            TokenResponse tokenResponse = TokenResponse.builder()
//                    .RefreshToken(newRefreshToken)
//                    .AccessToken(newAccessToken)
//                    .build();
//            redisUtil.setValues(newRefreshToken, userEmail);
//            redisUtil.deleteData(refreshToken);
//            return tokenResponse;
//        } else {
//            throw new IllegalStateException("Invalid refresh token");
//        }
//    }



//    public TokenResponse logoutResfreshToken(String refreshToken) {
//        Long tvd = 1000l;
//        Long rfd = 1000l;
//        // Check if the refresh token exists in Redis
//        if (redisUtil.existData(refreshToken)) {
//            // Get the email associated with the refresh token from Redis
//            String userEmail = redisUtil.getData(refreshToken);
//
//            // Get the user details from the userDetailsService
//            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
//
//            // Generate a new access token
//            String newAccessToken = createToken(userDetails.getUsername(), getRolesFromUserDetails(userDetails), tvd);
//            String newRefreshToken = createToken(userDetails.getUsername(), getRolesFromUserDetails(userDetails), rfd);
//
//            TokenResponse tokenResponse = TokenResponse.builder()
//                    .RefreshToken(newRefreshToken)
//                    .AccessToken(newAccessToken)
//                    .build();
//            redisUtil.deleteData(refreshToken);
//            return tokenResponse;
//        } else {
//            throw new IllegalStateException("Invalid refresh token");
//        }
//    }
//    public Claims getUserInfoFromToken(String token) {
//        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
//    }



    private List<String> getRolesFromUserDetails(UserDetails userDetails) {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

//    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
//    private static final String AUTHORITIES_KEY = "auth";
//    private final String secret;
//
//    private final long tokenValidityInMilliseconds;
//    private Key key;
//
//    public TokenProvider(
//            @Value("${spring.jwt.secret}") String secret,
//            @Value("${spring.jwt.token-validity-in-seconds}") long tokenValidityMilliseconds){
//        this.secret = secret;
//        this.tokenValidityInMilliseconds = tokenValidityMilliseconds * 1000;
//    }
//
//    @Override
//    public void afterPropertiesSet(){
//        byte[] keyBytes = Decoders.BASE64.decode(secret);
//        this.key = Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    //Authentication 객체의 권한 정보를 이용해서 토큰을 생성
//    public String createToken(Authentication authentication){
//        //authorities 설정
//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
//
//        //토큰 만료시간 설정
//        long now = (new Date()).getTime();
//        Date validity = new Date(now + this.tokenValidityInMilliseconds);
//
//        return Jwts.builder()
//                .setSubject(authentication.getName())
//                .claim(AUTHORITIES_KEY, authorities)
//                .signWith(key, SignatureAlgorithm.HS512)
//                .setExpiration(validity)
//                .compact();
//    }
//
//    //토큰에 담겨있는 정보를 이용해 Authentication 객체 리턴
//    public Authentication getAuthentication(String token) {
//        //토큰을 이용햐여 claim 생성
//        Claims claims = Jwts
//                .parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        //claim을 이용하여 authorities 생성
//        Collection<? extends GrantedAuthority> authorities =
//                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());
//
//        //claim과 authorites 이용하여 User 객체 생성
//        User principal = new User(claims.getSubject(), "", authorities);
//        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
//    }
//
//
//    //토큰의 유효성 검증 수행
//    public boolean validateToken(String token){
//        //토큰 파싱 후 발생하는 예외 캐치하여 false, 정상이면 true 리턴
//        try{
//            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//            return true;
//        }catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
//            logger.info("잘못된 JWT 서명입니다.");
//        }catch(ExpiredJwtException e){
//            logger.info("만료된 JWT 토큰입니다.");
//        }catch(UnsupportedJwtException e){
//            logger.info("지원되지 않는 토큰입니다.");
//        }catch(IllegalArgumentException e){
//            logger.info("JWT 토큰이 잘못되었습니다.");
//        }
//        return false;
//    }
//


}
