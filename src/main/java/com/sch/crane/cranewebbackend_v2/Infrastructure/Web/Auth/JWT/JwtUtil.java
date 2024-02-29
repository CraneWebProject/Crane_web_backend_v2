package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;

public class JwtUtil {

    @Value("${spring.jwt.secret}")
    public String secretKey;


    public String extractUseremail(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

}
