package com.culture.BAEUNDAY.domain.user;

import com.culture.BAEUNDAY.domain.refresh.RefreshTokenService;
import com.culture.BAEUNDAY.domain.user.DTO.request.LoginDTO;
import com.culture.BAEUNDAY.jwt.CookieUtil;
import com.culture.BAEUNDAY.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public ResponseEntity<?> loginUser(LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password()));

            String username = authentication.getName();
            String role = authentication.getAuthorities().iterator().next().getAuthority();

            String access = jwtUtil.createJWT("access", username, role, 60 * 10 * 1000L); //Access 토큰 : 10분으로 설정

            Integer expireS = 24 * 60 * 60;
            String refresh = jwtUtil.createJWT("refresh", username, role, expireS * 1000L); //Refresh 토큰 : 24시간으로 설정
            refreshTokenService.saveRefresh(username, refresh, expireS);

            response.setHeader("Authorization", "Bearer " + access);
            response.addCookie(CookieUtil.createCookie("refresh", refresh, expireS));

            return ResponseEntity.ok(Map.of("status", 200, "message", "로그인 성공"));


        } catch (AuthenticationException e) {
            //로그인 실패 시
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", 401);
            errorResponse.put("message", "아이디 또는 비밀번호가 올바르지 않습니다.");
            errorResponse.put("errorCode", "AUTH_401");

            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(errorResponse);
        }
    }

}
