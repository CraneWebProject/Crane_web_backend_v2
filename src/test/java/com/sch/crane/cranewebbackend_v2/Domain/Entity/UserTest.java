package com.sch.crane.cranewebbackend_v2.Domain.Entity;

import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserRole;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserSession;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;



class UserTest {

    @Test
    @DisplayName("User Creation Test")
    void createUser(){

        User user = User.builder()
                .userEmail("test@test.com")
                .userPassword("1234")
                .userBirth(LocalDate.now())
                .userDept("IoT")
                .userName("testName")
                .userPhNum("01000001234")
                .userRole(UserRole.MEMBER)
                .userSession(UserSession.BASS)
                .userStdId("20180000")
                .build();

        Assertions.assertThat(user.getUserEmail()).isEqualTo("test@test.com");
        Assertions.assertThat(user.getUserPassword()).isEqualTo("1234");
        Assertions.assertThat(user.getUserBirth()).isEqualTo(LocalDate.now());
        Assertions.assertThat(user.getUserDept()).isEqualTo("IoT");
        Assertions.assertThat(user.getUsername()).isEqualTo("testName");
        Assertions.assertThat(user.getUserPhNum()).isEqualTo("01000001234");
        Assertions.assertThat(user.getUserRole()).isEqualTo(UserRole.MEMBER);
        Assertions.assertThat(user.getUserSession()).isEqualTo(UserSession.BASS);
        Assertions.assertThat(user.getUserStdId()).isEqualTo("20180000");
    }

    @Test
    @DisplayName("User Repository Test")
    void UserRepositoryTest(){

    }
}