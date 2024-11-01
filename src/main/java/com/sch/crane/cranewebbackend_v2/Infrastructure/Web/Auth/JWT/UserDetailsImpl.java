package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.JWT;

import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserRole;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class UserDetailsImpl implements UserDetails {

    private final User user;

    private final String userEmail;

    @Builder
    public UserDetailsImpl(User user){
        this.user = user;
        this.userEmail =user.getUserEmail();
    }


    public static UserDetailsImpl from (User user) {
        return UserDetailsImpl.builder()
                .user(user)
                .build();
    }


    @Override
    public String getUsername() {
        return userEmail;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRole userRole = user.getUserRole();
        SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority(userRole.getAuthority());
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(adminAuthority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getUserPassword();
    }


    @Override
    public boolean isAccountNonExpired() {
        return false;
    }


    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
