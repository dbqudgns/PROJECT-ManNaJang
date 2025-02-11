package com.culture.BAEUNDAY.domain.user;

import com.culture.BAEUNDAY.domain.post.DTO.PostResponse;
import com.culture.BAEUNDAY.domain.post.Post;
import com.culture.BAEUNDAY.domain.post.PostJPARepository;
import com.culture.BAEUNDAY.domain.review.DTO.response.ReviewResponseDTO;
import com.culture.BAEUNDAY.domain.review.Review;
import com.culture.BAEUNDAY.domain.user.DTO.response.*;
import com.culture.BAEUNDAY.domain.user.DTO.request.RegisterRequestDTO;
import com.culture.BAEUNDAY.jwt.Custom.CustomUserDetails;
import com.culture.BAEUNDAY.utils.CursorRequest;
import com.culture.BAEUNDAY.utils.CursorResponse;
import com.culture.BAEUNDAY.utils.PageResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final int PAGE_SIZE_PLUS_ONE = 5 + 1;


    private static final String DEFAULT_IMAGE_URL = "https://rootimpact11-user.s3.ap-northeast-2.amazonaws.com/defaultImage.png";


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
    public CheckNameResponseDTO checkNameLogin(String name, CustomUserDetails customUserDetails) {

        User user = findUserByUsernameOrThrow(customUserDetails.getUsername());

        //닉네임을 수정할 의향이 없을 시 기존 닉네임 입력
        if (name.equals(user.getName())) {
            return CheckNameResponseDTO.builder()
                    .name(name)
                    .successName(1)
                    .message("사용 가능한 닉네임입니다.")
                    .build();
        }

        //바꾸고자 하는 닉네임이 존재할 경우
        if (userRepository.existsByName(name)) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }

        //바꾸고자 하는 닉네임이 존재하지 않을 경우
        return CheckNameResponseDTO.builder()
                .name(name)
                .successName(1)
                .message("사용 가능한 닉네임입니다.")
                .build();
    }

    @Transactional
    public UpdateProfileResponseDTO updateName(String name, CustomUserDetails customUserDetails) {

        User user = findUserByUsernameOrThrow(customUserDetails.getUsername());

        user.profileName(name);

        return UpdateProfileResponseDTO.builder()
                .message("닉네임 수정 완료")
                .name(user.getName())
                .field(user.getField())
                .build();
    }

    @Transactional
    public UpdateProfileResponseDTO updateField(String field, CustomUserDetails customUserDetails) {

        User user = findUserByUsernameOrThrow(customUserDetails.getUsername());

        user.profileField(field);

        return UpdateProfileResponseDTO.builder()
                .message("한 줄 소개 수정 완료")
                .name(user.getName())
                .field(user.getField())
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
        String defaultProfileImageUrl = DEFAULT_IMAGE_URL;

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

        User user = findUserByUsernameOrThrow(customUserDetails.getUsername());

        return CheckProfileResponseDTO.builder()
                .name(user.getName())
                .profileImg(user.getProfileImg())
                .field(user.getField())
                .build();

    }

    public CheckProfileResponseDTO seeProfile(Long userId, CustomUserDetails customUserDetails) {

        User user = findUserByUsernameOrThrow(customUserDetails.getUsername());

        User seeUser = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."));

        return CheckProfileResponseDTO.builder()
                .name(seeUser.getName())
                .profileImg(seeUser.getProfileImg())
                .field(seeUser.getField())
                .build();

    }

    public PageResponse<Long,  List<PostResponse.PostDTO>> getPosts(CustomUserDetails customUserDetails, String cursor) {

        User user = findUserByUsernameOrThrow(customUserDetails.getUsername());

        CursorRequest<Long> page = new CursorRequest<>(PAGE_SIZE_PLUS_ONE, cursor, Long.class, 0L);
        List<Post> posts = userRepository.findByUserIdWithCursor(user.getId(), page.cursor, page.request);

        return createCursorPageResponse(Post::getId, posts);
    }

    public User findUserByUsernameOrThrow(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }

        return user;
    }

    private PageResponse<Long, List<PostResponse.PostDTO>> createCursorPageResponse(
            Function<Post, Long> cursorExtractor,
            List<Post> posts
    ){
        if (posts.isEmpty()) {
            return new PageResponse<>(new CursorResponse<>(false, 0,null,null ),null);
        }

        int size = posts.size();
        boolean hasNext = false;
        if ( size == PAGE_SIZE_PLUS_ONE ){
            posts.remove( size - 1) ;
            size -= 1;
            hasNext = true;
        }
        List<PostResponse.PostDTO> postList = new ArrayList<>();

        for (Post post : posts) {
            postList.add(new PostResponse.PostDTO(post));
        }

        Post lastPost = posts.get(size - 1) ;
        Long nextCursor =  lastPost.getId();
        return new PageResponse<>(new CursorResponse<>(hasNext, size, nextCursor, nextCursor), postList);

    }

}
