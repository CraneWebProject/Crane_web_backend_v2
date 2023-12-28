package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sch.crane.cranewebbackend_v2.Data.DTO.User.LoginDto;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.ResponseMessage;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.StatusCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("로그인 성공 테스트")
    public void SucceedLoginTest() throws Exception{
        //Create test email and pw
        LoginDto loginDto = LoginDto.builder()
                .userEmail("test@sch.ac.kr")
                .userPassword("testpw")
                .build();

        String json = objectMapper.writeValueAsString(loginDto);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.code").value(StatusCode.OK))
                .andExpect(jsonPath("$.message").value(ResponseMessage.LOGIN_SUCCESS))
                .andReturn();
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void FailedLoginTest() throws Exception {
        LoginDto loginDto = LoginDto.builder()
                .userEmail("test@test.com")
                .userPassword("testtesttest")
                .build();

        String json = objectMapper.writeValueAsString(loginDto);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.code").value(StatusCode.UNAUTHORIZED))
                .andExpect(jsonPath("$.message").value(ResponseMessage.LOGIN_FAILED))
                .andReturn();
    }
}
