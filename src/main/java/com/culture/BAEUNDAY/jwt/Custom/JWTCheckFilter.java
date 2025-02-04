package com.culture.BAEUNDAY.jwt.Custom;

import com.culture.BAEUNDAY.domain.user.User;
import com.culture.BAEUNDAY.domain.user.Role;
import com.culture.BAEUNDAY.jwt.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//로그인한 사용자의 JWT 토큰을 검증하는 필터
@RequiredArgsConstructor
@Slf4j
public class JWTCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {

            log.info("토큰이 없거나 Bearer 로 시작하지 않음");
            filterChain.doFilter(request, response);

            return;
        }

        String accessToken = token.split(" ")[1];

        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e){

            // HTTP 상태 코드 401 설정
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // JSON 응답 데이터 생성
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("status", 401);
            responseData.put("message", "Access 토큰 만료");
            responseData.put("errorCode", "AUTH_401");

            // JSON 변환 및 응답 출력
            new ObjectMapper().writeValue(response.getWriter(), responseData);

            return ;
        }

        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {

            // HTTP 상태 코드 401 설정
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // JSON 응답 데이터 생성
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("status", 401);
            responseData.put("message", "Access 토큰이 아닙니다.");
            responseData.put("errorCode", "AUTH_401");

            // JSON 변환 및 응답 출력
            new ObjectMapper().writeValue(response.getWriter(), responseData);

            return ;

        }

        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);
        Role userRole = Role.valueOf(role.replace("ROLE_", ""));

        User user = User.builder()
                .username(username)
                .role(userRole)
                .password("temp_pw")
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }
}
