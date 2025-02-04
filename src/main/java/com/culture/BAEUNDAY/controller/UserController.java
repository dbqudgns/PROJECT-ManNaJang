package com.culture.BAEUNDAY.controller;

import com.culture.BAEUNDAY.dto.request.CheckRequestDTO;
import com.culture.BAEUNDAY.dto.request.RegisterRequestDTO;
import com.culture.BAEUNDAY.dto.request.UpdateProfileRequestDTO;
import com.culture.BAEUNDAY.dto.response.CheckNameResponseDTO;
import com.culture.BAEUNDAY.dto.response.CheckUsernameResponseDTO;
import com.culture.BAEUNDAY.jwt.Custom.CustomUserDetails;
import com.culture.BAEUNDAY.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //닉네임 중복 확인
    @PostMapping("/check-name")
    public ResponseEntity<CheckNameResponseDTO> checkName(@RequestBody @Valid CheckRequestDTO checkRequestDTO) {
        return ResponseEntity.ok(userService.checkName(checkRequestDTO.checkname()));
    }

    //아이디 중복 확인
    @PostMapping("/check-username")
    public ResponseEntity<CheckUsernameResponseDTO> checkUsername(@RequestBody @Valid CheckRequestDTO checkRequestDTO) {
        return ResponseEntity.ok(userService.checkUsername(checkRequestDTO.checkname()));
    }

    //회원가입
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterRequestDTO registerDTO) {
        return ResponseEntity.ok(userService.registerUser(registerDTO));
    }

    //내 프로필 정보 조회
    @GetMapping("/profile")
    public ResponseEntity<?> checkProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(userService.checkProfile(customUserDetails));
    }

    //내 프로필 정보 수정
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                           @RequestBody @Valid UpdateProfileRequestDTO updateProfileRequestDTO) {
        return ResponseEntity.ok(userService.updateProfile(customUserDetails, updateProfileRequestDTO));
    }

    //특정 사용자의 프로필 조회
    @GetMapping("/profile/{user_id}")
    public ResponseEntity<?> seeProfile(@PathVariable("user_id") Long userId,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(userService.seeProfile(userId, customUserDetails));
    }

    //사용자 이미지 수정 : 추후 작성


    //사용자 이미지 삭제(S3의 기본 이미지 경로명 반환해야 함) : 추후 작성


    //등록한 강의 불러오기(커서 기반 페이지네이션 적용해야 함) : 추후 작성
}



