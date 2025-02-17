package com.culture.BAEUNDAY.domain.user;

import com.culture.BAEUNDAY.jwt.JWTUtil;
import com.culture.BAEUNDAY.domain.refresh.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Map;

@Service
@RequiredArgsConstructor
public class LogoutService {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public Map<String, Object> logout(HttpServletRequest request, HttpServletResponse response) {

        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            return Map.of(
                    "status", 401,
                    "message", "Refresh 토큰이 없습니다.",
                    "errorCode", "AUTH_401"
            );
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            return Map.of(
                    "status", 401,
                    "message", "자동 로그아웃 되었습니다. 다시 로그인하세요.",
                    "errorCode", "AUTH_401"
            );

        }

        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            return Map.of(
                    "status", 401,
                    "message", "Refresh 토큰이 아닙니다.",
                    "errorCode", "AUTH_401"
            );
        }

        Boolean isExist = refreshRepository.existsByRefresh(refresh);

        if (!isExist) {
            return Map.of(
                    "status", 401,
                    "message", "저장소에 해당 Refresh 토큰이 없습니다.",
                    "errorCode", "AUTH_401"
            );
        }

        //로그아웃 진행
        refreshRepository.deleteByRefresh(refresh);

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);

        return Map.of(
                "status", 200,
                "message", "로그아웃 성공"
        );


    }

}
