package com.sch.crane.cranewebbackend_v2.Infrastructure.Response;

import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.ResponseMessage;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.StatusCode;
import lombok.Builder;

public class GeneralResponse {
    private int code;
    private String message;
    private Object data;

    public GeneralResponse(){
        this.code = StatusCode.BAD_REQUEST;
        this.message = ResponseMessage.INVALID_REQUEST;
        this.data = null;
    }

    @Builder
    public GeneralResponse(int code, String message, Object data){
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
