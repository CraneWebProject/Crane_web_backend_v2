package com.sch.crane.cranewebbackend_v2.Service.Service;

import com.sch.crane.cranewebbackend_v2.Data.DTO.User.EditMemberDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.User.JoinDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.User.UserResponseDto;
import com.sch.crane.cranewebbackend_v2.Data.Repository.User.UserRepository;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.Authority;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserRole;
import com.sch.crane.cranewebbackend_v2.Service.Exception.UserNameNotFoundException;
import com.sch.crane.cranewebbackend_v2.Util.RandomProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
//    private final RedisUtil redisUtil;
//    private final RedisTemplate redisTemplate;
    private final PasswordEncoder passwordEncoder;
    private final RandomProvider randomProvider; // 랜덤 스트링 생성


    @Transactional
    public UserResponseDto join(JoinDto joinDto){

        Authority authority = Authority.builder()
                .authorityName(UserRole.ROLE_STAN_BY.getAuthority())
                .build();

        User user = User.builder()
                .userEmail(joinDto.getUserEmail())
                .userPassword(passwordEncoder.encode(joinDto.getUserPassword()))
                .userName(joinDto.getUserName())
                .userDept(joinDto.getUserDept())
                .userStdId(joinDto.getUserStdId())
                .userPhNum(joinDto.getUserPhNum())
                .userBirth(joinDto.getUserBirth())
                .userTh(joinDto.getUserTh())
                .userSession(joinDto.getUserSession())
                .userRole(UserRole.ROLE_STAN_BY)
                .authorities(Collections.singleton(authority))
                .build();

        Long uid = userRepository.save(user).getUid();

        return UserResponseDto.builder()
                .uid(uid)
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .session(user.getUserSession())
                .build();
    }

    @Transactional
    public Optional<User> findUserByEmail(String email){
        return userRepository.findByUserEmail(email);
    }

    @Transactional
    public boolean isEmailExist(String email){
        if(findUserByEmail(email).isPresent())
            return true;
        else
            return false;
    }

    @Transactional
    public Long updateUserInfo(String userEmail, EditMemberDto editMemberDto) throws UserNameNotFoundException {
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(
                () -> new UserNameNotFoundException());

        user.updateUserInfo(editMemberDto.getUserDept(), editMemberDto.getUserPhNum());

        return userRepository.save(user).getUid();
    }

    @Transactional
    public Long updateUserRole(EditMemberDto editMemberDto) throws UserNameNotFoundException {
        User user = userRepository.findByUserEmail(editMemberDto.getUserEmail()).orElseThrow(UserNameNotFoundException::new);

        user.updateUserRole(editMemberDto.getUserRole());

        return userRepository.save(user).getUid();
    }

    @Transactional
    public Long editPassword(String userEmail, EditMemberDto editMemberDto) throws UserNameNotFoundException, BadCredentialsException{
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(
                () -> new UserNameNotFoundException());

        //기존 비밀번호 일치 확인
        if(!passwordEncoder.matches(editMemberDto.getUserPastPassword(), user.getUserPassword()) ){
            //틀린경우
            throw new BadCredentialsException("잘못된 비밀번호");
        }

        user.updateUserPassword(editMemberDto.getUserNewPassword());

        return userRepository.save(user).getUid();

    }

    @Transactional
    public void delUser(String userEmail, EditMemberDto editMemberDto) {

        User user = userRepository.findByUserEmail(userEmail).orElseThrow(UserNameNotFoundException::new);

        //비밀번호 확인
        if(!passwordEncoder.matches(editMemberDto.getUserPastPassword(), user.getUserPassword())){
            throw new BadCredentialsException("잘못된 비밀번호");
        }
        String randEmail = randomProvider.RandomNumCharStringProvider(10) + "@sch.ac.kr";
        String randRawPassword = randomProvider.RandomNumCharStringProvider(15);
        String randPassword = passwordEncoder.encode(randRawPassword);

        user.deleteUser(randEmail, randPassword);
    }
}
