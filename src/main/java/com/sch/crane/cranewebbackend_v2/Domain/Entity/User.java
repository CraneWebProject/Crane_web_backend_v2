package com.sch.crane.cranewebbackend_v2.Domain.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserRole;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserSession;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    private String userEmail;

    private String userPassword;

    private String userName;

    private String userDept;

    private String userStdId;

    private String userPhNum;

    private LocalDateTime userBirth;

    private UserSession userSession;

    @Enumerated(EnumType.STRING)
    private  UserRole userRole;


    public void updateUser(String userDept, String userPhNum){
        this.userDept = userDept;
        this.userPhNum = userPhNum;
    }

    public void updateUserPassword(String userPassword){
        this.userPassword = userPassword;
    }

    @Builder
    public User(Long uid,String userEmail,String userPassword,String userName,String userDept,String userStdId,
                String userPhNum,LocalDateTime userBirth,UserSession userSession, UserRole userRole) {
        this.uid = uid;

        this.userEmail = userEmail;

        this.userPassword = userPassword;

        this.userName = userName;

        this.userDept = userDept;

        this.userStdId = userStdId;

        this.userPhNum = userPhNum;

        this.userBirth = userBirth;

        this.userSession = userSession;

        this.userRole = userRole;


    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
