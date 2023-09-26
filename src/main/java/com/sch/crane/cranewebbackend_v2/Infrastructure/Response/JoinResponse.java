package com.sch.crane.cranewebbackend_v2.Infrastructure.Response;

import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.ResponseMessage;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.StatusCode;
import lombok.Builder;
import lombok.Data;

@Data
public class JoinResponse {
    private int code;
    private String message;
    private Object data;

    public JoinResponse(){
        this.code = StatusCode.DATA_CONFLICT;
        this.message = ResponseMessage.EMAIL_EXISTED;
        this.data = null;
    }

    @Builder
    public JoinResponse(int code, String message, Object data){
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
