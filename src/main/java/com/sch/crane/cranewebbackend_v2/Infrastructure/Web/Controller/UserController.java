package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller;

import co.elastic.clients.elasticsearch.nodes.Http;
import com.sch.crane.cranewebbackend_v2.Data.DTO.User.JoinDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.User.LoginDto;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Response.JoinResponse;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Response.LoginResponse;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.JWT.UserDetailsImpl;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.Redis.RedisUtil;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.Security.TokenProvider;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.ResponseMessage;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.StatusCode;
import com.sch.crane.cranewebbackend_v2.Service.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;
    private final TokenProvider tokenProvider;
    private final RedisUtil redisUtil;
    private final UserDetailsImpl userDetails;
    private final Long expireTimeMs = 300000l;
    private final Long RefreshExpireTimeMs = 1000 * 60 * 60 * 60L;


    @PostMapping("/signup")
    public ResponseEntity<JoinResponse> join(@RequestBody JoinDto joinDto) {
        User user = userService.join(joinDto);
        JoinResponse response;
        response = JoinResponse.builder()
                .code(StatusCode.OK)
                .message(ResponseMessage.SIGNIN_SUCCESS)
                .data(user)
                .build();
        return ResponseEntity.ok(response);
    }


}
