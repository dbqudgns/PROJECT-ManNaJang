package com.culture.BAEUNDAY.jwt;

import jakarta.servlet.http.Cookie;

public class CookieUtil {

    public static Cookie createCookie(String key, String value, Integer expiredS) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        cookie.setMaxAge(expiredS);

        return cookie;
    }

}
