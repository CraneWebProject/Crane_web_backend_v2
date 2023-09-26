package com.sch.crane.cranewebbackend_v2.Service.Exception;

import lombok.Getter;

@Getter
public enum ExceptionEnum {
    WRONG_EMAIL(400,"Invalid email"),
    WORNG_PASSWORD(400,"Invalid password"),
    DUPLICATE_EMAIL(400, "Duplicate email exists."),
    DUPLICATE_MEMBER(400, "Duplicate member exists."),
    DUPLICATE_PHONE_NUMBER(400, "Duplicate phone number exists."),
    DUPLICATE_PASSWORD(400, "Duplicate password exists."),
    NOT_EXIST_MEMBER(404,"That account(member) does not exist."),
    INVALID_PERMISSION_TO_MODIFY(403,"You are not authorized to edit this post."),
    REQUIRED_ADMIN_POSITION(403, "ADMIN permission is required."),
    IMMULATABLE_TO_ADMIN(403,"You cannot change your position to an ADMIN account.");





    private final int code;
    private final String msg;

    ExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
