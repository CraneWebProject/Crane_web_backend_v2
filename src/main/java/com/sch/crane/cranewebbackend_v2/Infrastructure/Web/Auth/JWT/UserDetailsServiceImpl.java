package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.JWT;

import com.sch.crane.cranewebbackend_v2.Data.Repository.User.UserRepository;
import com.sch.crane.cranewebbackend_v2.Service.Exception.UserNameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String userEmail) throws UserNameNotFoundException {
        return userRepository.findWithAuthoritiesByUserEmail(userEmail)
                .map(user -> createUser(userEmail, user))
                .orElseThrow(() -> new UsernameNotFoundException(userEmail + " DB에 존재하지 않는 유저"));
    }
//    @Override
//    @Transactional(readOnly = true)
//    public UserDetails loadUserByUsername(final String userEmail) throws UserNameNotFoundException {
//        return userRepository.findWithAuthoritiesByUserEmail(userEmail)
//                .map(UserDetailsImpl::from)
//                .orElseThrow(() -> new UsernameNotFoundException(userEmail + " DB에 존재하지 않는 유저"));
//    }

    private User createUser(String userEmail, com.sch.crane.cranewebbackend_v2.Domain.Entity.User user){

        List<GrantedAuthority> authorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

        return new User(user.getUserEmail(),
                user.getUserPassword(),
                authorities);
    }


}
