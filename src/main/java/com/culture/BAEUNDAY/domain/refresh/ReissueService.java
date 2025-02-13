package com.culture.BAEUNDAY.domain.refresh;

import com.culture.BAEUNDAY.jwt.CookieUtil;
import com.culture.BAEUNDAY.jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

//Refresh 토큰을 인증하여 Access 토큰 재발급 하는 서비스 또한 Refresh Rotate(화이트리스트)
@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final RefreshTokenService refreshTokenService;

    public ResponseEntity<Map<String, Object>> reissue(HttpServletRequest request, HttpServletResponse response) {

        String refresh = null;
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {

            if(cookie.getName().equals("refresh")){
                refresh = cookie.getValue();
            }
        }

        if(refresh == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createResponse(401, "Refresh 토큰이 없습니다.", "AUTH_401"));
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createResponse(401, "자동 로그아웃 되었습니다. 다시 로그인하세요.", "AUTH_401"));
        }

        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createResponse(401, "Refresh 토큰이 아닙니다.", "AUTH_401"));
        }

        Boolean isExist = refreshRepository.existsByRefresh(refresh);

        if(!isExist) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createResponse(401, "저장소에 해당 Refresh 토큰이 없습니다.", "AUTH_401"));
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        //새로운 Access, Refresh 토큰 발급 (Refresh Rotate)
        Integer expiredS = 60 * 60 * 24;
        String newAccess = jwtUtil.createJWT("access", username, role, expiredS * 1000L);
        //Integer expiredS = 60 * 60 * 24;
        String newRefresh = jwtUtil.createJWT("refresh", username, role, expiredS * 1000L);

        // 기존 Refresh 토큰 DB에서 삭제 후 새로운 Refresh 토큰 저장 (Refresh Rotate)
        refreshRepository.deleteByRefresh(refresh);
        refreshTokenService.saveRefresh(username, newRefresh, expiredS);

        response.setHeader("Authorization", "Bearer " + newAccess);
        response.addCookie(CookieUtil.createCookie("refresh", newRefresh, expiredS));

        return ResponseEntity.ok(createResponseOK(200, "Access 토큰, Refresh 토큰 재발급 완료"));


    }

    private Map<String, Object> createResponse(int status, String message, String errorCode) {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("status", status);
        responseData.put("message", message);
        responseData.put("errorCode", errorCode);
        return responseData;
    }

    private Map<String, Object> createResponseOK(int status, String message) {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("status", status);
        responseData.put("message", message);
        return responseData;
    }
}
