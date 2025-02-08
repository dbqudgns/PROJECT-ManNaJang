package com.culture.BAEUNDAY.domain.post;

import com.culture.BAEUNDAY.domain.post.DTO.PostRequest;
import com.culture.BAEUNDAY.domain.post.DTO.PostResponse;
import com.culture.BAEUNDAY.domain.user.User;
import com.culture.BAEUNDAY.domain.user.UserRepository;
import com.culture.BAEUNDAY.utils.CursorRequest;
import com.culture.BAEUNDAY.utils.CursorResponse;
import com.culture.BAEUNDAY.utils.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostJPARepository postJPARepository;
    private final UserRepository userRepository;
    private static final int PAGE_SIZE_PLUS_ONE = 5 + 1;

    @Transactional
    public PageResponse<? extends Comparable<?> , PostResponse.FindAllDTO> findAll(String sort, Status status, Fee fee, String cursor, Long cursorId) {
        List<Post> posts;

        switch( sort ) {
            case "id" -> {
                CursorRequest<Long> page = new CursorRequest<>(PAGE_SIZE_PLUS_ONE, cursor, Long.class, cursorId );
                if (status != null && fee != null ){
                    posts = postJPARepository.findAllByIdAndStatusAndFeeLessThanCursor(page.cursor, status, fee, page.request);
                } else if ( status == null && fee != null ) {
                    posts = postJPARepository.findAllByIdAndFeeLessThanCursor(page.cursor, fee, page.request);
                } else if (fee == null && status != null) {
                    posts = postJPARepository.findAllByIdAndStatusLessThanCursor(page.cursor, status, page.request);
                } else {
                    posts = postJPARepository.findAllByIdLessThanCursor(page.cursor, page.request);
                }
                return createCursorPageResponse(Post::getId, posts);
            }
            case "heart" -> {
                CursorRequest<Integer> page = new CursorRequest<>(PAGE_SIZE_PLUS_ONE, cursor, Integer.class, cursorId );
                if (status != null && fee != null ){
                    posts = postJPARepository.findAllByHeartAndStatusAndFeeLessThanCursor(page.cursor, page.cursorID, status, fee, page.request);
                } else if ( status == null && fee != null ) {
                    posts = postJPARepository.findAllByHeartAndFeeLessThanCursor(page.cursor, page.cursorID,fee, page.request);
                } else if (fee == null && status != null) {
                    posts = postJPARepository.findAllByHeartAndStatusLessThanCursor(page.cursor, page.cursorID,status, page.request);
                } else {
                    posts = postJPARepository.findAllByHeartLessThanCursor(page.cursor, page.cursorID, page.request);
                }
                return createCursorPageResponse(Post::getNumsOfHeart, posts);
            }
            case "recent" -> {
                CursorRequest<LocalDateTime> page = new CursorRequest<>(PAGE_SIZE_PLUS_ONE, cursor, LocalDateTime.class, cursorId );
                if (status != null && fee != null ){
                    posts = postJPARepository.findAllByDateAndStatusAndFeeLessThanCursor(page.cursor, page.cursorID, status, fee, page.request);
                } else if ( status == null && fee != null ) {
                    posts = postJPARepository.findAllByDateAndFeeLessThanCursor(page.cursor, page.cursorID,fee, page.request);
                } else if (fee == null && status != null) {
                    posts = postJPARepository.findAllByDateAndStatusLessThanCursor(page.cursor, page.cursorID,status, page.request);
                } else {
                    posts = postJPARepository.findAllByDateLessThanCursor( page.cursor, page.cursorID, page.request);
                }
                return createCursorPageResponse(Post::getCreatedDate, posts);
            }
            default -> throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void create(String username, PostRequest.PostRequestDto request) {
        User user = getUserByName(username);
        Fee feeRange = getFeeRange(request.fee());

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
                .feeRange(feeRange)
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
                .numsOfHeart(0)
                .user(user) // TODO : User 매핑
                .build();
        postJPARepository.save(post);
    }

    @Transactional
    public void update(String username, Long postId, PostRequest.PostRequestDto request){
        User user = getUserByName(username);
        Post post = getPostById(postId);

        if (!Objects.equals(user.getName(), post.getUser().getName())) {
            throw new IllegalArgumentException("해당 사용자가 작성한 게시글이 아닙니다.");
        }
        Fee feeRange = getFeeRange(request.fee());
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
                feeRange,
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
                request.deadline()
        );
    }

    @Transactional
    public void delete(String username, Long postId) {
        User user = getUserByName(username);
        Post post = getPostById(postId);

        if (!Objects.equals(user.getName(), post.getUser().getName())) {
            throw new IllegalArgumentException("해당 사용자가 작성한 게시글이 아닙니다.");
        }
        postJPARepository.delete(post);
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

    @Transactional
    public PageResponse<?, PostResponse.FindByIdDTO> findById(Long postId) {
        Post post = getPostById(postId);
        PostResponse.FindByIdDTO findByIdDTO = new PostResponse.FindByIdDTO(post);
        return new PageResponse<>( null, findByIdDTO);
    }


    public Post getPostById(Long postId) {
            return  postJPARepository.findById(postId).orElseThrow(
                    () -> new IllegalArgumentException("해당 포스트를 찾을 수 없습니다.")
            );
    }

    private User getUserByName(String userName) {
        User user = userRepository.findByUsername(userName);
        if (user == null) {
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
        } else {
            return user;
        }
    }

    private Fee getFeeRange(int fee) {
        Fee feeRange;
        if (fee == 0) { feeRange = Fee.FREE ;}
        else if ( fee < 30000 ) { feeRange = Fee.UNDER_3 ;}
        else if ( fee < 50000 ) { feeRange = Fee.BETWEEN3_5; }
        else if ( fee < 100000 ) { feeRange = Fee.BETWEEN5_10 ; }
        else { feeRange = Fee.OVER_10 ;  }
        return feeRange;
    }


}
