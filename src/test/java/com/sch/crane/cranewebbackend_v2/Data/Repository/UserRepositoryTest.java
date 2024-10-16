package com.sch.crane.cranewebbackend_v2.Data.Repository;

import com.sch.crane.cranewebbackend_v2.Data.Repository.User.UserRepository;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserRole;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("User를 이메일로 조회한다.")
    public void testFindByUserEmail() {
        // Given
        User user = User.builder()
                .userEmail("test@test.com")
                .userName("testUser")
                .userSession(UserSession.BASS)
                .userStdId("20181537")
                .userPhNum("01025819869")
                .userDept("사물인터넷학과")
                .userBirth(LocalDate.now())
                .userPassword("asdfasdf")
                .userRole(UserRole.ROLE_MEMBER)
                .userTh(37)
                .build();
        userRepository.save(user);

        // When
        Optional<User> foundUser = userRepository.findByUserEmail("test@test.com");

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUserEmail()).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 조회할 경우 빈 Optional을 반환한다.")
    public void testFindByNonExistentUserEmail() {
        // When
        Optional<User> foundUser = userRepository.findByUserEmail("nonexistent@test.com");

        // Then
        assertThat(foundUser).isNotPresent();
    }


    @Test
    @DisplayName("User를 이메일로 조회하고 권한 정보를 함께 로드한다.")
    public void testFindWithAuthoritiesByUserEmail() {
        // Given
        User user = User.builder()
                .userEmail("test@test.com")
                .userName("testUser")
                .userSession(UserSession.BASS)
                .userStdId("20181537")
                .userPhNum("01025819869")
                .userDept("사물인터넷학과")
                .userBirth(LocalDate.now())
                .userPassword("asdfasdf")
                .userRole(UserRole.ROLE_MEMBER)
                .userTh(37)
                .build();
        userRepository.save(user);
        // When
        Optional<User> foundUser = userRepository.findWithAuthoritiesByUserEmail("test@test.com");

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUserEmail()).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("User를 이름으로 조회한다.")
    public void testFindOneWithAuthoritiesByUserName() {
        // Given
        User user = User.builder()
                .userEmail("test@test.com")
                .userName("userName")
                .userSession(UserSession.BASS)
                .userStdId("20181537")
                .userPhNum("01025819869")
                .userDept("사물인터넷학과")
                .userBirth(LocalDate.now())
                .userPassword("asdfasdf")
                .userRole(UserRole.ROLE_MEMBER)
                .userTh(37)
                .build();
        userRepository.save(user);

        // When
        User foundUser = userRepository.findOneWithAuthoritiesByUserName("userName");

        // Then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUserName()).isEqualTo("userName");
    }
}