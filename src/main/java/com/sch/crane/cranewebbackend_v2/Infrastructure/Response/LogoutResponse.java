package com.sch.crane.cranewebbackend_v2.Infrastructure.Response;

import lombok.Builder;
import lombok.Data;

@Data
public class LogoutResponse {
    private int code;
    private String message;

    @Builder
    public LogoutResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
