package com.culture.BAEUNDAY.domain.user;

import com.culture.BAEUNDAY.domain.user.DTO.response.*;
import com.culture.BAEUNDAY.domain.user.DTO.request.RegisterRequestDTO;
import com.culture.BAEUNDAY.domain.user.DTO.request.UpdateProfileRequestDTO;
import com.culture.BAEUNDAY.jwt.Custom.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public CheckNameResponseDTO checkName(String name) {

        if (userRepository.existsByName(name)) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }

        return CheckNameResponseDTO.builder()
                .name(name)
                .successName(1)
                .message("사용 가능한 닉네임입니다.")
                .build();
    }

    @Transactional
    public CheckUsernameResponseDTO checkUsername(String username) {

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }

        return CheckUsernameResponseDTO.builder()
                .username(username)
                .successUserName(1)
                .message("사용 가능한 아이디입니다.")
                .build();

    }

    @Transactional
    public RegisterResponseDTO registerUser(RegisterRequestDTO registerRequestDTO) {

        // S3 기본 이미지 URL 설정
        String defaultProfileImageUrl = "추후 추가 예정.png";

        User user = User.builder()
                .name(registerRequestDTO.name())
                .username(registerRequestDTO.username())
                .password(bCryptPasswordEncoder.encode(registerRequestDTO.password()))
                .role(Role.USER)
                .profileImg(defaultProfileImageUrl)
                .field(registerRequestDTO.field())
                .build();

        userRepository.save(user);

        return RegisterResponseDTO.builder()
                .message("회원가입 성공")
                .name(user.getName())
                .username(user.getUsername())
                .profileImg(defaultProfileImageUrl)
                .manner(user.getManner())
                .build();

    }

    public CheckProfileResponseDTO checkProfile(CustomUserDetails customUserDetails) {

        User user = userRepository.findByUsername(customUserDetails.getUsername());

        if (user == null) {
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }

        return CheckProfileResponseDTO.builder()
                .name(user.getName())
                .profileImg(user.getProfileImg())
                .field(user.getField())
                .build();

    }

    @Transactional
    public UpdateProfileResponseDTO updateProfile(CustomUserDetails customUserDetails, UpdateProfileRequestDTO updateProfileRequestDTO) {

        User user = userRepository.findByUsername(customUserDetails.getUsername());

        if (user == null) {
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }

        user.profileUpdate(updateProfileRequestDTO.name(), updateProfileRequestDTO.field());

        return UpdateProfileResponseDTO.builder()
                .message("프로필 수정 완료")
                .name(user.getName())
                .field(user.getField())
                .build();

    }

    public CheckProfileResponseDTO seeProfile(Long userId, CustomUserDetails customUserDetails) {

        User user = userRepository.findByUsername(customUserDetails.getUsername());

        if (user == null) {
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }

        User seeUser = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."));

        return CheckProfileResponseDTO.builder()
                .name(seeUser.getName())
                .profileImg(seeUser.getProfileImg())
                .field(seeUser.getField())
                .build();

    }
}
