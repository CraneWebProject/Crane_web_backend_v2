package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller;

import com.sch.crane.cranewebbackend_v2.Data.DTO.User.EditMemberDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.User.JoinDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.User.UserResponseDto;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Response.GeneralResponse;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Response.JoinResponse;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.JWT.JwtUtil;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.JWT.UserDetailsImpl;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.Security.TokenProvider;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.ResponseMessage;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.StatusCode;
import com.sch.crane.cranewebbackend_v2.Service.Exception.ErrorResponse;
import com.sch.crane.cranewebbackend_v2.Service.Exception.UserNameNotFoundException;
import com.sch.crane.cranewebbackend_v2.Service.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "https://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final JwtUtil jwtUtil;
//    private final RedisUtil redisUtil;
    private final Long expireTimeMs = 300000l;
    private final Long RefreshExpireTimeMs = 1000 * 60 * 60 * 60L;


    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<JoinResponse> join(@RequestBody JoinDto joinDto) {
        JoinResponse response;
        //Email 중복시 실패 반환
        if(userService.isEmailExist(joinDto.getUserEmail())){
            HttpHeaders headers= new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            response = JoinResponse.builder()
                    .code(StatusCode.DATA_CONFLICT)
                    .message(ResponseMessage.EMAIL_EXISTED)
                    .data(false)
                    .build();
            return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
        }else { //미 중복시 정상 가입 성공
            UserResponseDto userResponseDto = userService.join(joinDto);
            response = JoinResponse.builder()
                    .code(StatusCode.OK)
                    .message(ResponseMessage.SIGNIN_SUCCESS)
                    .data(userResponseDto)
                    .build();
        }
        return ResponseEntity.ok(response);
    }

    //이메일 중복체크
    @GetMapping("/emailcheck")
    public ResponseEntity<JoinResponse> emailCheck(@RequestBody JoinDto joinDto){
        JoinResponse response;
        if(userService.isEmailExist(joinDto.getUserEmail())){ //동일한 이메일이 존재하면
            response = JoinResponse.builder()
                    .code(StatusCode.DATA_CONFLICT)
                    .message(ResponseMessage.EMAIL_EXISTED)
                    .data(false)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        response = JoinResponse.builder()
                .code(StatusCode.OK)
                .message(ResponseMessage.EMAIL_OK)
                .data(true)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //로그인 후 유저 정보 반환
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @GetMapping("/userinfo")
    public ResponseEntity<?> userInfo(@CookieValue(value = "accessToken") Cookie ACToken){
//        System.out.println(ACToken.getValue());
        UserResponseDto userResponseDto;
        String userEmail = jwtUtil.extractUseremail(ACToken.getValue());
//        System.out.println(userEmail);

        Optional<User> optionalUser =  userService.findUserByEmail(userEmail);
        if(optionalUser.isEmpty()){
            ErrorResponse response = ErrorResponse.builder()
                    .status(StatusCode.NOT_FOUND)
                    .message(ResponseMessage.NOT_FOUND_USER)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        User user = optionalUser.get();
        userResponseDto = UserResponseDto.builder()
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .session(user.getUserSession())
                .userRole(user.getUserRole())
                .uid(user.getUid())
                .build();

        GeneralResponse response = GeneralResponse.builder()
                .code(StatusCode.OK)
                .message(ResponseMessage.CHECK_OK)
                .data(userResponseDto)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //유저 정보 수정
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @PatchMapping("/updateUserInfo")
    public ResponseEntity<?> updateUserInfo(@RequestBody EditMemberDto editMemberDto){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) principal;
        String userEmail = userDetails.getUserEmail();

        try{ //업데이트 시도
            userService.updateUserInfo(userEmail, editMemberDto);
        } catch (UserNameNotFoundException e){ //존재하지 않는 사용자의 경우 예외 처리
            ErrorResponse response = ErrorResponse.builder()
                    .status(StatusCode.NOT_FOUND)
                    .message(ResponseMessage.NOT_FOUND_USER)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        GeneralResponse response = GeneralResponse.builder()
                .code(StatusCode.OK)
                .message(ResponseMessage.UPDATE_OK)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //유저 권한 수정
    //사이트 관리자 및 임원만 가능
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PatchMapping("/updateUserRole")
    public ResponseEntity<?> updateUserRole(@RequestBody EditMemberDto editMemberDto){
        try{
            userService.updateUserRole(editMemberDto);
        }catch(UserNameNotFoundException e){
            ErrorResponse response = ErrorResponse.builder()
                    .status(StatusCode.NOT_FOUND)
                    .message(ResponseMessage.NOT_FOUND_USER)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        GeneralResponse response = GeneralResponse.builder()
                .code(StatusCode.OK)
                .message(ResponseMessage.UPDATE_OK)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //유저 비밀번호 수정
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @PatchMapping("/udateuserpassword")
    public ResponseEntity<?> updateUserPassword(@RequestBody EditMemberDto editMemberDto){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) principal;
        String userEmail = userDetails.getUserEmail();

        try{
            userService.editPassword(userEmail, editMemberDto);
        }catch (UserNameNotFoundException e){
            ErrorResponse response = ErrorResponse.builder()
                    .status(StatusCode.NOT_FOUND)
                    .message(ResponseMessage.NOT_FOUND_USER)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }catch (BadCredentialsException e){
            ErrorResponse response = ErrorResponse.builder()
                    .status(StatusCode.FORBIDDEN)
                    .message(ResponseMessage.PASSWORD_ERROR)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        GeneralResponse response =  GeneralResponse.builder()
                .code(StatusCode.OK)
                .message(ResponseMessage.PASSWORD_CHANGE_OK)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    //회원 탈퇴
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @DeleteMapping("/deleteuser")
    public ResponseEntity<?> deleteUser(@RequestBody EditMemberDto editMemberDto){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) principal;
        String userEmail = userDetails.getUserEmail();

        try{
            userService.delUser(userEmail, editMemberDto);
        }catch (Exception e){
            ErrorResponse response = ErrorResponse.builder()
                    .status(StatusCode.BAD_REQUEST)
                    .message(ResponseMessage.INVALID_REQUEST)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        GeneralResponse response = GeneralResponse.builder()
                .code(StatusCode.OK)
                .message(ResponseMessage.UPDATE_OK)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK );
    }


}
