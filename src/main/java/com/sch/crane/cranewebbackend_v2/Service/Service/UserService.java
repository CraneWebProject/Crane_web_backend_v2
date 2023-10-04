package com.sch.crane.cranewebbackend_v2.Service.Service;

import com.sch.crane.cranewebbackend_v2.Data.DTO.User.EditMemberDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.User.JoinDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.User.LoginDto;
import com.sch.crane.cranewebbackend_v2.Data.Repository.User.UserRepository;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.Redis.RedisUtil;
import com.sch.crane.cranewebbackend_v2.Service.Exception.UserNameNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.config.types.Password;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RedisUtil redisUtil;
    private final RedisTemplate redisTemplate;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User join(JoinDto joinDto){

        User user = User.builder()
                .userEmail(joinDto.getUserEmail())
                .userPassword(passwordEncoder.encode(joinDto.getUserPassword()))
                .userName(joinDto.getUserName())
                .userDept(joinDto.getUserDept())
                .userStdId(joinDto.getUserStdId())
                .userPhNum(joinDto.getUserPhNum())
                .userBirth(joinDto.getUserBirth())
                .userSession(joinDto.getUserSession())
                .build();
        return userRepository.save(user);
    }

//    @Transactional
//    public User login(LoginDto loginDto){
//        User user = userRepository.findByUserEmail(loginDto.getUserEmail()).orElseThrow(
//                () -> new IllegalArgumentException("아이디가 일치 하지 않습니다"));
//        if(!passwordEncoder.matches(loginDto.getUserPassword(),user.getPassword())){
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
//        }
//        return user;
//
//    }

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
    public Long editUser(String userEmail, EditMemberDto editMemberDto){
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(
                () -> new UserNameNotFoundException());

        user.updateUser(editMemberDto.getUserDept(), editMemberDto.getUserPhNum());

        return userRepository.save(user).getUid();
    }

    @Transactional
    public Long editPassword(String userEmail, EditMemberDto editMemberDto){
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(
                () -> new UserNameNotFoundException());
        user.updateUserPassword(editMemberDto.getUserPassword());

        return userRepository.save(user).getUid();

    }

    @Transactional
    public void delUser(Long uId)
    {
        Optional<User> optionalUser = userRepository.findById(uId);
        if(optionalUser.isEmpty())
        {
            throw new NoSuchElementException("유저가 존재하지 않습니다.");
        }
        userRepository.deleteById(uId);
    }
}
