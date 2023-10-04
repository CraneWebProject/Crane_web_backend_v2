package com.sch.crane.cranewebbackend_v2.Data.DTO.User;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserSession;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class JoinDto {

    private String userEmail;

    private String userPassword;

    private String userName;

    private String userDept;

    private String userStdId;

    private String userPhNum;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/seoul")
    private LocalDate userBirth;

    private UserSession userSession;


    @Builder
    public JoinDto(String userEmail,String userPassword,String userName,
                   String userDept, String userStdId,String userPhNum,LocalDate userBirth,UserSession userSession){

        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userDept = userDept;
        this.userStdId = userStdId;
        this.userPhNum = userPhNum;
        this.userBirth = userBirth;
        this.userSession = userSession;
    }
}
