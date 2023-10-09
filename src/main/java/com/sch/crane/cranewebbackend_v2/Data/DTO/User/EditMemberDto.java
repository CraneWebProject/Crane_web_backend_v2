package com.sch.crane.cranewebbackend_v2.Data.DTO.User;

import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
public class EditMemberDto {

    private String userDept;
    private String userPhNum;
    private String userPastPassword;
    private String userNewPassword;

    //관리자가 유저 권한 업데이트시 사용
    private String userEmail;
    private UserRole userRole;

    @Builder
    public EditMemberDto(String userDept, String userPhNum, String userPastPassword,String userNewPassword, String userEmail, UserRole userRole){

        this.userDept = userDept;
        this.userPhNum = userPhNum;
        this.userPastPassword = userPastPassword;
        this.userNewPassword = userNewPassword;
        this.userEmail = userEmail;
        this.userRole = userRole;

    }


}
