package com.culture.BAEUNDAY.domain.user;

import com.culture.BAEUNDAY.domain.user.DTO.request.CheckRequestDTO;
import com.culture.BAEUNDAY.domain.user.DTO.request.LoginDTO;
import com.culture.BAEUNDAY.domain.user.DTO.request.RegisterRequestDTO;
import com.culture.BAEUNDAY.domain.user.DTO.request.UpdateProfileRequestDTO;
import com.culture.BAEUNDAY.domain.user.DTO.response.CheckNameResponseDTO;
import com.culture.BAEUNDAY.domain.user.DTO.response.CheckUsernameResponseDTO;
import com.culture.BAEUNDAY.jwt.Custom.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "유저 api", description = "유저 관련 기능")
public class UserController {

    private final UserService userService;
    private final LoginService loginService;
    private final LogoutService logoutService;

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
        return loginService.loginUser(loginDTO, request, response);
    }

    @PostMapping("/check-name")
    @Operation(summary = "닉네임 중복 확인")
    public ResponseEntity<?> checkName(@RequestBody @Valid CheckRequestDTO checkRequestDTO) {
        return ResponseEntity.ok(userService.checkName(checkRequestDTO.checkname()));
    }

    @PostMapping("/check-username")
    @Operation(summary = "아이디 중복 확인")
    public ResponseEntity<?> checkUsername(@RequestBody @Valid CheckRequestDTO checkRequestDTO) {
        return ResponseEntity.ok(userService.checkUsername(checkRequestDTO.checkname()));
    }

    @PostMapping("/register")
    @Operation(summary = "회원가입")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterRequestDTO registerDTO) {
        return ResponseEntity.ok(userService.registerUser(registerDTO));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public Map<String, Object> logout(HttpServletRequest request, HttpServletResponse response) {
        return logoutService.logout(request, response);
    }

    @GetMapping("/profile")
    @Operation(summary = "내 프로필 정보 조회")
    public ResponseEntity<?> checkProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(userService.checkProfile(customUserDetails));
    }

    @PutMapping("/profile")
    @Operation(summary = "내 프로필 정보 수정")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                           @RequestBody @Valid UpdateProfileRequestDTO updateProfileRequestDTO) {
        return ResponseEntity.ok(userService.updateProfile(customUserDetails, updateProfileRequestDTO));
    }

    @GetMapping("/profile/{user_id}")
    @Operation(summary = "특정 사용자의 프로필 조회")
    public ResponseEntity<?> seeProfile(@PathVariable("user_id") Long userId,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(userService.seeProfile(userId, customUserDetails));
    }

    //사용자 이미지 수정 : 추후 작성


    //사용자 이미지 삭제(S3의 기본 이미지 경로명 반환해야 함) : 추후 작성

}



