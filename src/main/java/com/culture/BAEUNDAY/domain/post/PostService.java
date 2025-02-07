package com.culture.BAEUNDAY.domain.post;

import com.culture.BAEUNDAY.domain.post.DTO.PostRequest;
import com.culture.BAEUNDAY.domain.post.DTO.PostResponse;
import com.culture.BAEUNDAY.utils.CursorRequest;
import com.culture.BAEUNDAY.utils.CursorResponse;
import com.culture.BAEUNDAY.utils.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostJPARepository postJPARepository;
    private static final int PAGE_SIZE_PLUS_ONE = 5 + 1;

    @Transactional
    public PageResponse<? extends Comparable<?> , PostResponse.FindAllDTO> findAll(String sort, String status, String fee, String cursor, Long cursorId) {
        List<Post> posts;

        switch( sort ) {
            case "id" -> {
                CursorRequest<Long> page = new CursorRequest<>(PAGE_SIZE_PLUS_ONE, cursor, Long.class, cursorId );
                posts = postJPARepository.findAllByIdLessThanCursor(page.cursor, page.request);
                return createCursorPageResponse(Post::getId, posts);
            }
            case "heart" -> {
                CursorRequest<Long> page = new CursorRequest<>(PAGE_SIZE_PLUS_ONE, cursor, Long.class, cursorId );
                posts = postJPARepository.findAllByHeartLessThanCursor(page.cursor, page.cursorID, page.request);
                return createCursorPageResponse(Post::getNumsOfHeart, posts);
            }
            case "recent" -> {
                CursorRequest<LocalDateTime> page = new CursorRequest<>(PAGE_SIZE_PLUS_ONE, cursor, LocalDateTime.class, cursorId );
                posts = postJPARepository.findAllByDateLessThanCursor( page.cursor, page.cursorID, page.request);
                return createCursorPageResponse(Post::getCreatedDate, posts);
            }
            default -> throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void create(PostRequest.PostRequestDto request) {
//        User user = userService.find()
        Long user_id = 1L;

        Post post = Post.builder()
                .title(request.title())
                .imgURL(request.imgURL())
                .subject(request.subject())
                .goal(request.goal())
                .outline(request.outline())
                .targetStudent(request.targetStudent())
                .level(request.level())
                .contactMethod(request.contactMethod())
                .fee(request.fee())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .province(request.province())
                .city(request.city())
                .address(request.address())
                .minP(request.minP())
                .maxP(request.maxP())
                .content(request.content())
                .status(request.status())
                .createdDate(request.createdDate())
                .deadline(request.deadline())

                .build();
        postJPARepository.save(post);
    }

    @Transactional
    public void update(Long postId, PostRequest.PostRequestDto request){
        Post post = getPostById(postId);
        //TODO : User
        post.update(
                request.title(),
                request.imgURL(),
                request.subject(),
                request.goal(),
                request.outline(),
                request.targetStudent(),
                request.level(),
                request.contactMethod(),
                request.fee(),
                request.startDate(),
                request.endDate(),
                request.province(),
                request.city(),
                request.address(),
                request.minP(),
                request.maxP(),
                request.content(),
                request.status(),
                request.createdDate(),
                request.deadline(),
                request.numsOfHeart()
        );


    }

    @Transactional
    public void delete(Long postId) {
        Post post = getPostById(postId);
        postJPARepository.delete(post); //TODO: user ?
    }

    private <T extends Comparable<T>> PageResponse<T, PostResponse.FindAllDTO> createCursorPageResponse(Function<Post, T> cursorExtractor, List<Post> posts){

        //포스트 정보가 없을 경우
        if (posts.isEmpty()) {
            return new PageResponse<>(new CursorResponse<>(false,0, null, null), null);
        }

        int size = posts.size();
        boolean hasNext = false;
        if (size == PAGE_SIZE_PLUS_ONE) { //6개라면, 5개만 전송하고 hasNext를 true로 설정
            posts.remove(size - 1) ;
            size -= 1;
            hasNext = true;
        }

        PostResponse.FindAllDTO findAllDTO = PostResponse.FindAllDTO.of(posts);
        Post lastPost = posts.get(size - 1) ;
        T nextCursor = cursorExtractor.apply(lastPost);
        Long nextCursorId = lastPost.getId();

        return new PageResponse<>(new CursorResponse<>(hasNext, size, nextCursor, nextCursorId), findAllDTO);
    }

    public PageResponse<?, PostResponse.FindByIdDTO> findById(Long postId) {
        Post post = getPostById(postId);
        PostResponse.FindByIdDTO findByIdDTO = new PostResponse.FindByIdDTO(post);
        return new PageResponse<>( null, findByIdDTO);
    }


    public Post getPostById(Long postId) {
        return postJPARepository.findById(postId).orElseThrow(IllegalArgumentException::new);
    }


}
