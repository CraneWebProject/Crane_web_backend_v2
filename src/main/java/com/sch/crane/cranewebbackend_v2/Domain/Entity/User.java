package com.sch.crane.cranewebbackend_v2.Domain.Entity;

import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserRole;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserSession;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

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

    private UserRole userRole;


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

}
