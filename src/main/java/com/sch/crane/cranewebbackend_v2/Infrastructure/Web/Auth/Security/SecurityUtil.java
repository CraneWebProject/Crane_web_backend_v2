package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.Security;

import com.sch.crane.cranewebbackend_v2.Data.Repository.User.UserRepository;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.JWT.UserDetailsImpl;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityUtil {
    private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);
    private final UserRepository userRepository;

    public SecurityUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    public static Optional<String> getCurrentLoginId(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if(authentication == null){
            logger.debug("Security Context에 인증 정보가 없습니다.");
            return Optional.empty();
        }

        String loginId = null;
        if(authentication.getPrincipal() instanceof UserDetails springSecurityUser){
            loginId = springSecurityUser.getUsername();
        }else if(authentication.getPrincipal() instanceof String){
            loginId = (String) authentication.getPrincipal();
        }
        return Optional.ofNullable(loginId);
    }


//    public String getCurrentLoggedInUserEmail(){
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        UserDetailsImpl userDetails = (UserDetailsImpl) principal;
//        String userEmail = userDetails.getUsername();
//
//        return userRepository.findByUserEmail(userEmail).orElseThrow(EntityNotFoundException::new).getUserEmail();
//
//    }

    public String getCurrentLoggedInUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof org.springframework.security.core.userdetails.User springSecurityUser) {
            return springSecurityUser.getUsername();
        } else {
            throw new EntityNotFoundException("인증된 사용자 정보를 찾을 수 없습니다.");
        }
    }
}
