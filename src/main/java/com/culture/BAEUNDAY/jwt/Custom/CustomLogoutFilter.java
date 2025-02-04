package com.culture.BAEUNDAY.jwt.Custom;

import com.culture.BAEUNDAY.jwt.JWTUtil;
import com.culture.BAEUNDAY.repository.RefreshRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//POST, /logout으로 요청이 올 시 수행되는 클래스
@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilter {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            // HTTP 상태 코드 401 설정
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // JSON 응답 데이터 생성
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("status", 401);
            responseData.put("message", "Refresh 토큰이 없습니다.");
            responseData.put("errorCode", "AUTH_401");

            // JSON 변환 및 응답 출력
            new ObjectMapper().writeValue(response.getWriter(), responseData);

            return;
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            // HTTP 상태 코드 401 설정
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // JSON 응답 데이터 생성
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("status", 401);
            responseData.put("message", "자동 로그아웃 되었습니다. 다시 로그인하세요.");
            responseData.put("errorCode", "AUTH_401");

            // JSON 변환 및 응답 출력
            new ObjectMapper().writeValue(response.getWriter(), responseData);

            return;

        }

        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            // HTTP 상태 코드 401 설정
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // JSON 응답 데이터 생성
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("status", 401);
            responseData.put("message", "Refresh 토큰이 아닙니다.");
            responseData.put("errorCode", "AUTH_401");

            // JSON 변환 및 응답 출력
            new ObjectMapper().writeValue(response.getWriter(), responseData);

            return;
        }

        Boolean isExist = refreshRepository.existsByRefresh(refresh);

        if(!isExist) {
            // HTTP 상태 코드 401 설정
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // JSON 응답 데이터 생성
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("status", 401);
            responseData.put("message", "저장소에 해당 Refresh 토큰이 없습니다.");
            responseData.put("errorCode", "AUTH_401");

            // JSON 변환 및 응답 출력
            new ObjectMapper().writeValue(response.getWriter(), responseData);

            return;
        }

        //로그아웃 진행
        refreshRepository.deleteByRefresh(refresh);

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);

        // HTTP 상태 코드 400 설정
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JSON 응답 데이터 생성
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("status", 200);
        responseData.put("message", "로그아웃 성공");

        new ObjectMapper().writeValue(response.getWriter(), responseData);



    }


}
