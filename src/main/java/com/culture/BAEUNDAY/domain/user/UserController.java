package com.culture.BAEUNDAY.domain.user;

import com.culture.BAEUNDAY.domain.user.DTO.request.*;
import com.culture.BAEUNDAY.domain.user.DTO.response.CheckNameResponseDTO;
import com.culture.BAEUNDAY.domain.user.DTO.response.CheckUsernameResponseDTO;
import com.culture.BAEUNDAY.jwt.Custom.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "유저 api", description = "유저 관련 기능")
public class UserController {

    private final UserService userService;
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final ImageService imageService;

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

    @PostMapping("/login-check-name")
    @Operation(summary = "로그인한 사용자의 닉네임 중복 확인")
    public ResponseEntity<?> checkNameLogin(@RequestBody @Valid CheckRequestDTO checkRequestDTO,
                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(userService.checkNameLogin(checkRequestDTO.checkname(), customUserDetails));
    }

    @PutMapping("/profile/name")
    @Operation(summary = "로그인한 사용자의 닉네임 중복 확인 후 닉네임 변경")
    public ResponseEntity<?> newName(@RequestBody @Valid UpdateNameRequestDTO updateNameRequestDTO,
                                     @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(userService.updateName(updateNameRequestDTO.name(), customUserDetails));
    }

    @PutMapping("/profile/field")
    @Operation(summary = "로그인한 사용자의 한 줄 소개 변경")
    public ResponseEntity<?> newName(@RequestBody @Valid UpdateFieldRequestDTO updateFieldRequestDTO,
                                     @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(userService.updateField(updateFieldRequestDTO.field(), customUserDetails));
    }

    @GetMapping("/profile/{user_id}")
    @Operation(summary = "특정 사용자의 프로필 조회")
    public ResponseEntity<?> seeProfile(@PathVariable("user_id") Long userId,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(userService.seeProfile(userId, customUserDetails));
    }

    @GetMapping("/profile/posts")
    @Operation(summary = "내가 작성한 글 조회")
    public ResponseEntity<?> getPosts(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(userService.getPosts(customUserDetails));
    }

    @PostMapping(value = "/profile/img/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "프로필 이미지 수정")
    public ResponseEntity<?> s3Upload(@RequestParam String deleteImage,
                                      @RequestPart(value = "image") MultipartFile image,
                                      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(imageService.uploadImg(customUserDetails, deleteImage, image));
    }

    @DeleteMapping("/profile/img/delete")
    @Operation(summary = "프로필 이미지 삭제")
    public ResponseEntity<?> s3Delete(@RequestParam String image,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(imageService.deleteImg(customUserDetails, image));
    }



}



