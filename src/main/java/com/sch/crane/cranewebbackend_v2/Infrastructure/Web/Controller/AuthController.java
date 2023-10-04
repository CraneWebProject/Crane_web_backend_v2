package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller;

import com.sch.crane.cranewebbackend_v2.Data.DTO.User.LoginDto;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserRole;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Response.LoginResponse;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.Redis.RedisUtil;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.Security.TokenProvider;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.ResponseMessage;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.StatusCode;
import com.sch.crane.cranewebbackend_v2.Service.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.beans.Encoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userservice;
    private final RedisUtil redisUtil;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private static final Long expireTimeMs = 60L;
    private static final Long RefreshExporeTimeMs = 1000 * 60 * 60 * 60L;

    LoginResponse loginResponse = new LoginResponse();


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDto loginDto){

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
        //password 틀린경우 //보안 강화를 위해 틀린 것이 Email인지 PW인지 알려주지 않음.
        if(!passwordEncoder.matches(loginDto.getUserPassword(), user.getPassword())){
            System.out.println(loginDto.getUserPassword());
            System.out.println(user.getPassword());
            loginResponse = LoginResponse.builder()
                    .code(StatusCode.UNAUTHORIZED)
                    .message(ResponseMessage.LOGIN_FAILED)
                    .build();
            return new ResponseEntity<>(loginResponse, HttpStatus.BAD_REQUEST);
        }

        long currentTimeMillis = System.currentTimeMillis();
        Long expireTimeEND = expireTimeMs + currentTimeMillis;
        List<String> userRoleList = new ArrayList<>();

        if(user.getUserRole() == null){
            userRoleList.add(UserRole.STAN_BY.toString());
        }else {
            userRoleList.add(user.getUserRole().toString());//userRole이 List<String>으로 요구되어 해당 방식으로 변환

        }
        String token = tokenProvider.createToken(user.getUserEmail(), userRoleList, expireTimeMs);
        String refreshToken = tokenProvider.createToken(user.getUserEmail(), userRoleList, RefreshExporeTimeMs);
        System.out.println(token);
        System.out.println(refreshToken);
        redisUtil.setValues(refreshToken, user.getUserEmail());

        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken)
                .domain("crane.sch.ac.kr")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(3600000)
                .build();

        loginResponse = LoginResponse.builder()
                .code(StatusCode.OK)
                .message(ResponseMessage.LOGIN_SUCCESS)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .header("accessToken", token)
                .header("expireTime", String.valueOf(expireTimeEND))
                .body(loginResponse);
    }
}
