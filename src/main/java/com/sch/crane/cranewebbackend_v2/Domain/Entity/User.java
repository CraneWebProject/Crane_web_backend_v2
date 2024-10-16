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
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private Integer userTh;

    private String userProfilePic;

    @Enumerated(EnumType.STRING)
    private UserSession userSession;

    @Enumerated(EnumType.STRING)
    private  UserRole userRole;

    @ManyToMany
    @JoinTable(
        name = "user_authority",
        joinColumns = {@JoinColumn(name = "uid", referencedColumnName = "uid")},
        inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")}
    )
    private Collection<Authority> authorities;


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
        this.userRole = UserRole.ROLE_STAN_BY;
        this.userProfilePic = ""; //TODO: 탈퇴시 프로필 사진 지우도록 수정
    }

    @Builder
    public User(Long uid,String userEmail,String userPassword,String userName,String userDept,String userStdId,
                String userPhNum,LocalDate userBirth, Integer userTh, String userProfilePic, UserSession userSession, UserRole userRole, Collection<Authority> authorities ) {
        this.uid = uid;

        this.userEmail = userEmail;

        this.userPassword = userPassword;

        this.userName = userName;

        this.userDept = userDept;

        this.userStdId = userStdId;

        this.userPhNum = userPhNum;

        this.userBirth = userBirth;

        this.userTh = userTh;

        this.userProfilePic = userProfilePic;

        this.userSession = userSession;

        this.userRole = userRole;

        this.authorities = authorities;

    }

}
