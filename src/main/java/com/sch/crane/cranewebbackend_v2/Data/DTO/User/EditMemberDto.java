package com.sch.crane.cranewebbackend_v2.Data.DTO.User;

import lombok.Builder;
import lombok.Data;

@Data
public class EditMemberDto {

    private String userDept;
    private String userPhNum;
    private String userPassword;

    @Builder
    public EditMemberDto(String userDept, String userPhNum,String userPassword){

        this.userDept = userDept;
        this.userPhNum = userPhNum;
        this.userPassword = userPassword;

    }


}
