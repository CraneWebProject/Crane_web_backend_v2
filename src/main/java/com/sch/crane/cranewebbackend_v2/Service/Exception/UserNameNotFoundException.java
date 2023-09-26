package com.sch.crane.cranewebbackend_v2.Service.Exception;

import lombok.Getter;


@Getter
public class UserNameNotFoundException extends RuntimeException{
    private static final String MESSAGE = "아이디 또는 비밀번호를 확인 해주세요";

    public UserNameNotFoundException(){
        super(MESSAGE);
    }
}
