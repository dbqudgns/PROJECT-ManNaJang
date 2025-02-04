package com.culture.BAEUNDAY.jwt.Custom;

import com.culture.BAEUNDAY.dto.request.LoginDTO;
import com.culture.BAEUNDAY.jwt.CookieUtil;
import com.culture.BAEUNDAY.jwt.JWTUtil;
import com.culture.BAEUNDAY.service.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


//커스텀 로그인 필터
@RequiredArgsConstructor
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    //로그인한 사용자의 username, password 검출 후 AuthenticationManager에게 전달
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        LoginDTO loginDTO = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

           loginDTO = objectMapper.readValue(messageBody, LoginDTO.class);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String username = loginDTO.username();
        String password = loginDTO.password();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authToken);

    }

    //로그인 성공 시 실행 : JWT 토큰 발급 및 반환
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        String username = authentication.getName();

        String role = authentication.getAuthorities().iterator().next().getAuthority();

        String access = jwtUtil.createJWT("access", username, role, 60 * 10 * 1000L); //Access 토큰 : 10분으로 설정

        Integer expireS = 24 * 60 * 60;
        String refresh = jwtUtil.createJWT("refresh", username, role, expireS * 1000L); //Refresh 토큰 : 24시간으로 설정
        refreshTokenService.saveRefresh(username, refresh, expireS);

        response.setHeader("Authorization", "Bearer " + access);
        response.addCookie(CookieUtil.createCookie("refresh", refresh, expireS));

        // JSON 변환 및 응답 출력
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JSON 응답 데이터 생성
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("status", 200);
        responseData.put("message", "로그인 성공");

        new ObjectMapper().writeValue(response.getWriter(), responseData);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("status", 401);
        responseData.put("message", "아이디 또는 비밀번호가 올바르지 않습니다.");
        responseData.put("errorCode", "AUTH_401");

        new ObjectMapper().writeValue(response.getWriter(), responseData);

    }
}
