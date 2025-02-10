package com.culture.BAEUNDAY.domain.heart;


import com.culture.BAEUNDAY.domain.post.DTO.PostResponse;
import com.culture.BAEUNDAY.domain.post.Post;
import com.culture.BAEUNDAY.domain.post.PostService;
import com.culture.BAEUNDAY.domain.user.User;
import com.culture.BAEUNDAY.domain.user.UserService;
import com.culture.BAEUNDAY.utils.CursorRequest;
import com.culture.BAEUNDAY.utils.CursorResponse;
import com.culture.BAEUNDAY.utils.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HeartService {

    private final HeartJPARepository heartJPARepository;
    private final PostService postService;
    private final UserService userService;
    private static final int PAGE_SIZE_PLUS_ONE = 6+1;

    public String control(String username, HeartRequestDto requestDto) {
        User user = userService.findUserByUsernameOrThrow(username);
        Post post = postService.getPostById(requestDto.postId());

        Optional<Heart> heart = heartJPARepository.findByUserAndPost(user,post);

        if (heart.isPresent()) {
            heartJPARepository.delete(heart.get());
            post.removeHeart(heart.get());
            return ("찜 취소");
        }else {
            Heart newHeart = Heart.builder().user(user).post(post).createdDate(requestDto.createdDate()).build();
            heartJPARepository.save(newHeart);
            post.addHeart(newHeart);
            return ("찜 등록") ;
        }
    }

    public PageResponse<?, PostResponse.FindAllDTO> get(String username, String cursor, Long cursorId) {

        User user = userService.findUserByUsernameOrThrow(username);
        List<Heart> hearts;
        CursorRequest<LocalDateTime> page = new CursorRequest<>(PAGE_SIZE_PLUS_ONE, cursor, LocalDateTime.class, cursorId );
        hearts = heartJPARepository.findByUser(user, page.cursor, page.cursorID, page.request);
        if (hearts.isEmpty()) {
            return new PageResponse<>(new CursorResponse<>(false,0, null, null), null);
        }
        List<Long> postIds = hearts.stream()
                .map(heart -> heart.getPost().getId())
                .toList();

        List<Post> posts = new java.util.ArrayList<>(postIds.stream()
                .map(postService::getPostById)
                .toList());

        if (posts.isEmpty()) {
            return new PageResponse<>(new CursorResponse<>(false,0, null, null), null);
        }

        int size = posts.size();
        boolean hasNext = false;
        if (size == PAGE_SIZE_PLUS_ONE) {
            posts.remove(size - 1) ;
            size -= 1;
            hasNext = true;
        }
        PostResponse.FindAllDTO findAllDTO = PostResponse.FindAllDTO.of(posts);
        LocalDateTime nextCursor = hearts.get(size -1).getCreatedDate();
        Long nextCursorId = hearts.get(size - 1).getId();

        return new PageResponse<>(new CursorResponse<>(hasNext, size, nextCursor, nextCursorId), findAllDTO);
    }

}
