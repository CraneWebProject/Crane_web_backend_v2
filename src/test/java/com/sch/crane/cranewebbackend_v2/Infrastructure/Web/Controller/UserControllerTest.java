package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sch.crane.cranewebbackend_v2.Data.DTO.User.JoinDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.User.UserResponseDto;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserSession;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Response.JoinResponse;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.Redis.RedisUtil;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.Security.TokenProvider;
import com.sch.crane.cranewebbackend_v2.Service.Service.UserService;
import com.sch.crane.cranewebbackend_v2.Util.JsonProvider;
import org.assertj.core.api.Assertions;
import org.hibernate.mapping.Join;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

//Given: 테스트를 진행할 행위를 위한 사전 준비
//when: 테스트를 진행할 행위
//then: 테스트를 진행한 행위에 대한 결과 검증

//Test주체 선언
@WebMvcTest(UserController.class)
class UserControllerTest {
    //MockMvc는 실제 서블릿 컨테이너가 아닌 테스트로 컨트롤러를 사용할 수 있게 해줌

    @Autowired
    MockMvc mvc;

//    Test협력자
    @MockBean
    UserService userService;
    @MockBean
    TokenProvider tokenProvider;
    @MockBean
    RedisUtil redisUtil;

    @MockBean
    JsonProvider jsonProvider;


    @Test
    @DisplayName("회원가입 테스트")
    void signUpTest(){
        //given
        JoinDto joinDto = JoinDto.builder()
                .userEmail("test@test.com")
                .userPassword("testPassword")
                .userName("testName")
                .userPhNum("01012341234")
                .userBirth(LocalDate.now())
                .userDept("IoT")
                .userSession(UserSession.BASS)
                .userStdId("20180000")
                .build();

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .userEmail(joinDto.getUserEmail())
                .userName(joinDto.getUserName())
                .session(joinDto.getUserSession())
                .build();

        //when then
        try{
            String requestJson =  jsonProvider.dtoToJson(joinDto);
            String responseJson = jsonProvider.dtoToJson(userResponseDto);


            mvc.perform(MockMvcRequestBuilders.get("/api/users/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(responseJson))
                    .andDo(MockMvcResultHandlers.print());

        }catch(Exception e){
            e.printStackTrace();
        }





    }
}