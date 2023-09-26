package com.sch.crane.cranewebbackend_v2.Data.DTO.User;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class LoginDto {

    private String userEmail;

    private String userPassword;

    @Builder
    public LoginDto(String userEmail,String userPassword){
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }
}
