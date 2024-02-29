package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.Security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class TokenCookieUtil {

    public static void setTokenCookie(HttpServletResponse response, String cookieName, String token) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true);
//        cookie.setMaxAge(-1); // 브라우저가 종료될 때 쿠키 만료
        cookie.setPath("/"); // 모든 경로에서 접근 가능하도록 설정
        response.addCookie(cookie);
    }

    public static String extractTokenFromCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
