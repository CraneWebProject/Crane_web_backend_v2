package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sch.crane.cranewebbackend_v2.Data.DTO.User.LoginDto;
import com.sch.crane.cranewebbackend_v2.Data.Repository.User.UserRepository;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.ResponseMessage;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.StatusCode;
import org.json.JSONObject;
import com.sch.crane.cranewebbackend_v2.Data.DTO.User.JoinDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.User.UserResponseDto;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserSession;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Response.JoinResponse;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.Security.TokenProvider;
import com.sch.crane.cranewebbackend_v2.Service.Service.UserService;
import com.sch.crane.cranewebbackend_v2.Util.JsonProvider;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.hibernate.mapping.Join;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//Given: 테스트를 진행할 행위를 위한 사전 준비
//when: 테스트를 진행할 행위
//then: 테스트를 진행한 행위에 대한 결과 검증

//Test주체 선언
//@WebMvcTest(UserController.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {
    //MockMvc는 실제 서블릿 컨테이너가 아닌 테스트로 컨트롤러를 사용할 수 있게 해줌

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

////    Test협력자
//    @MockBean
//    UserService userService;
//    @MockBean
//    TokenProvider tokenProvider;
//    @MockBean
//    RedisUtil redisUtil;
//
//    @MockBean
//    JsonProvider jsonProvider;


//    @Test
//    @DisplayName("회원가입 테스트")
//    void signUpTest(){
//        //given
//        JoinDto joinDto = JoinDto.builder()
//                .userEmail("test@test.com")
//                .userPassword("testPassword")
//                .userName("testName")
//                .userPhNum("01012341234")
//                .userBirth(LocalDate.now())
//                .userDept("IoT")
//                .userSession(UserSession.BASS)
//                .userStdId("20180000")
//                .build();
//
//        UserResponseDto userResponseDto = UserResponseDto.builder()
//                .userEmail(joinDto.getUserEmail())
//                .userName(joinDto.getUserName())
//                .session(joinDto.getUserSession())
//                .build();
//
//        //when then
//        try{
//            String requestJson =  jsonProvider.dtoToJson(joinDto);
//            String responseJson = jsonProvider.dtoToJson(userResponseDto);
//
//
//            mvc.perform(MockMvcRequestBuilders.get("/api/users/join")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(requestJson))
//                    .andExpect(status().isOk())
//                    .andExpect(MockMvcResultMatchers.content().string(responseJson))
//                    .andDo(MockMvcResultHandlers.print());
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }


    @Test
    @DisplayName("회원가입 테스트")
    public void testJoin() throws Exception {
        //Create a test user
        JoinDto joinDto = JoinDto.builder()
                .userEmail("jointest@test.com")
                .userPassword("testPassword")
                .userName("testName")
                .userPhNum("01012341234")
                .userBirth(LocalDate.now())
                .userDept("IoT")
                .userSession(UserSession.BASS)
                .userStdId("20180000")
                .build();

        String json = objectMapper.writeValueAsString(joinDto);


        //Make a request to join with the test user
        MvcResult result = mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value(ResponseMessage.SIGNIN_SUCCESS))
                .andReturn();


        //Check the result
        JSONObject response = new JSONObject(result.getResponse().getContentAsString());
//        assertEquals(200, response.get("code"));
//        assertEquals(ResponseMessage.SIGNIN_SUCCESS, response.get("message"));

    }

    @Test
    @DisplayName("이메일 중복체크 성공 테스트")
    public void testEmailCheck() throws Exception {
        //Create test email
        JoinDto joinDto_NotExist = JoinDto.builder()
                .userEmail("test1@sch.ac.kr")
                .build();


        String jsonN = objectMapper.writeValueAsString(joinDto_NotExist);

        MvcResult result = mockMvc.perform(get("/api/users/emailcheck")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonN))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true))
                .andExpect(jsonPath("$.message").value(ResponseMessage.EMAIL_OK))
                .andReturn();
    }

    @Test
    @DisplayName("이메일 중복체크 실패 테스트")
    public void testEmailCheckFail() throws Exception {
        //Create test email
        JoinDto joinDto_Exist = JoinDto.builder()
                .userEmail("test@test.com")
                .build();

        String jsonE = objectMapper.writeValueAsString(joinDto_Exist);

        MvcResult resultE = mockMvc.perform(get("/api/users/emailcheck")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonE))
                .andExpect(jsonPath("$.code").value(StatusCode.DATA_CONFLICT))
                .andExpect(jsonPath("$.data").value(false))
                .andExpect(jsonPath("$.message").value(ResponseMessage.EMAIL_EXISTED))
                .andReturn();
    }



}