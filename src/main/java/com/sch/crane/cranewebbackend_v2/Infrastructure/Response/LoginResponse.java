package com.sch.crane.cranewebbackend_v2.Infrastructure.Response;

import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.ResponseMessage;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.StatusCode;
import lombok.Builder;
import lombok.Data;

@Data
public class LoginResponse {
    private int code;
    private String message;
    private Object token;
    private Long expireTimeMs;

    public LoginResponse() {
        this.code = code = StatusCode.BAD_REQUEST;
        this.message = ResponseMessage.LOGIN_FAILED;
        this.token = null;
        this.expireTimeMs = null;
    }

    @Builder
    public LoginResponse(int code, String message, Object token, Long expireTimeMs) {
        this.code = code;
        this.message = message;
        this.token = token;
        this.expireTimeMs = expireTimeMs;
    }
}
