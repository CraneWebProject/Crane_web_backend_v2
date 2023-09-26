package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.JWT;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponse {
    private String AccessToken;
    private String RefreshToken;
}
