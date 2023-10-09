package com.sch.crane.cranewebbackend_v2.Domain.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserRole;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserSession;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(unique = true, nullable = false)
    private String userEmail;

    @Column(nullable = false)
    @JsonIgnore
    private String userPassword;

    @Column(nullable = false)
    private String userName;

    private String userDept;

    private String userStdId;

    private String userPhNum;

    private LocalDate userBirth;

    @Enumerated(EnumType.STRING)
    private UserSession userSession;

    @Enumerated(EnumType.STRING)
    private  UserRole userRole;


    public void updateUserInfo(String userDept, String userPhNum){
        this.userDept = userDept;
        this.userPhNum = userPhNum;
    }

    public void updateUserRole(UserRole userRole){
        this.userRole = userRole;
    }

    public void updateUserPassword(String userPassword){
        this.userPassword = userPassword;
    }

    public void deleteUser(String randEmail, String randPassword ){
        this.userEmail = randEmail;
        this.userPassword = randPassword;
        this.userName = "탈퇴한 사용자";
        this.userPhNum = "01000000000";
        this.userRole = UserRole.STAN_BY;
    }

    @Builder
    public User(Long uid,String userEmail,String userPassword,String userName,String userDept,String userStdId,
                String userPhNum,LocalDate userBirth,UserSession userSession, UserRole userRole) {
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
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        return this.userName;
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
