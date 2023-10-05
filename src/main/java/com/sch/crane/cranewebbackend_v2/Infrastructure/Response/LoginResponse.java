package com.sch.crane.cranewebbackend_v2.Infrastructure.Response;

import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.ResponseMessage;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.StatusCode;
import lombok.Builder;
import lombok.Data;

@Data
public class LoginResponse {
    private int code;
    private String message;
    private Long expireTimeMs;

    public LoginResponse() {
        this.code = code = StatusCode.BAD_REQUEST;
        this.message = ResponseMessage.LOGIN_FAILED;
        this.expireTimeMs = null;
    }

    @Builder
    public LoginResponse(int code, String message, Long expireTimeMs) {
        this.code = code;
        this.message = message;
        this.expireTimeMs = expireTimeMs;
    }
}
