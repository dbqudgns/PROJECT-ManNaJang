package com.culture.BAEUNDAY.domain.post;

import com.culture.BAEUNDAY.domain.heart.Heart;
import com.culture.BAEUNDAY.domain.heart.HeartJPARepository;
import com.culture.BAEUNDAY.domain.post.DTO.PostRequest;
import com.culture.BAEUNDAY.domain.post.DTO.PostResponse;
import com.culture.BAEUNDAY.domain.reserve.Reserve;
import com.culture.BAEUNDAY.domain.reserve.ReserveJPARepository;
import com.culture.BAEUNDAY.domain.user.User;
import com.culture.BAEUNDAY.domain.user.UserRepository;
import com.culture.BAEUNDAY.utils.CursorRequest;
import com.culture.BAEUNDAY.utils.CursorResponse;
import com.culture.BAEUNDAY.utils.PageResponse;
import com.culture.BAEUNDAY.utils.s3.ForImageResponseDTO;
import com.culture.BAEUNDAY.utils.s3.PostImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
@Slf4j
public class PostService {

    private final PostJPARepository postJPARepository;
    private final UserRepository userRepository;
    private final PostImageService postImageService;
    private final HeartJPARepository heartJPARepository;
    private final PostCustomRepositoryImpl postCustomRepository;
    private final ReserveJPARepository reserveJPARepository;

    private static final int PAGE_SIZE_PLUS_ONE = 5 + 1;

    @Transactional
    public PageResponse<? extends Comparable<?> , PostResponse.FindAllDTO> findAll(String sort, Status status, FeeRange feeRange, Province province, String city, String cursor, Long cursorId) {
        List<Post> posts;
        switch( sort ) {
            case "recent" -> {
                CursorRequest<Long> page = new CursorRequest<>(PAGE_SIZE_PLUS_ONE, cursor, Long.class, cursorId );
                posts = postCustomRepository.findAllById(page.cursor, page.cursorID, status, feeRange, province, city, page.request);
                return createCursorPageResponse(Post::getId, posts);
            }
            case "heart" -> {
                CursorRequest<Integer> page = new CursorRequest<>(PAGE_SIZE_PLUS_ONE, cursor, Integer.class, cursorId );
                posts = postCustomRepository.findAllByHeart(page.cursor, page.cursorID, status, feeRange, province, city, page.request);
                return createCursorPageResponse(Post::getNumsOfHeart, posts);
            }
            case "deadline" -> {
                CursorRequest<LocalDateTime> page = new CursorRequest<>(PAGE_SIZE_PLUS_ONE, cursor, LocalDateTime.class, cursorId );
                posts = postCustomRepository.findAllByDateTime(page.cursor, page.cursorID, status, feeRange, province, city, page.request);
                return createCursorPageResponse(Post::getDeadline, posts);
            }
            default -> throw new IllegalArgumentException();
        }
    }

    @Transactional
    public ForImageResponseDTO create(String username, MultipartFile image, PostRequest.PostRequestDto request) {
        User user = getUserByName(username);
        FeeRange feeRange = getFeeRange(request.fee());

        ForImageResponseDTO forImageResponseDTO = postImageService.uploadImg(image);

        Post post = Post.builder()
                .user(user)
                .title(request.title())
                .imgURL(forImageResponseDTO.postImg())
                .startDateTime(request.startDateTime())
                .endDateTime(request.endDateTime())
                .fee(request.fee())
                .feeRange(feeRange)
                .deadline(request.deadline())
                .province(request.province())
                .city(request.city())
                .address(request.address())
                .minP(request.minP())
                .maxP(request.maxP())
                .numsOfParticipant(0)
                .numsOfHeart(0)
                .status(request.status())
                .createdDate(request.createdDate())
                .content(request.content())
                .build();
        postJPARepository.save(post);

        return forImageResponseDTO;
    }

    @Transactional
    public ForImageResponseDTO update(String username, Long postId, String beforeImageAddress, MultipartFile image, PostRequest.PostRequestDto request){
        User user = getUserByName(username);
        Post post = getPostById(postId);

        if (!Objects.equals(user.getName(), post.getUser().getName())) {
            throw new IllegalArgumentException("해당 사용자가 작성한 게시글이 아닙니다.");
        }
        FeeRange feeRange = getFeeRange(request.fee());

        ForImageResponseDTO forImageResponseDTO = postImageService.updateImg(post, beforeImageAddress, image);

        post.update(
                request.title(),
                forImageResponseDTO.postImg(),
                request.startDateTime(),
                request.endDateTime(),
                request.fee(),
                feeRange,
                request.deadline(),
                request.province(),
                request.city(),
                request.address(),
                request.minP(),
                request.maxP(),
                request.status(),
                request.content()
        );
        return forImageResponseDTO;
    }

    @Transactional
    public void delete(String username, Long postId) {
        User user = getUserByName(username);
        Post post = getPostById(postId);

        if (!Objects.equals(user.getName(), post.getUser().getName())) {
            throw new IllegalArgumentException("해당 사용자가 작성한 게시글이 아닙니다.");
        }
        postImageService.deleteImg(post.getImgURL()); //s3에 저장된 게시글 이미지 부터 삭제
        postJPARepository.delete(post);
    }

    @Transactional
    public PageResponse<?, PostResponse.FindByIdDTO> findById(Long postId, String user) {
        Post post = getPostById(postId);
        User visitor = getUserByName(user);
        User writer = getUserByName(post.getUser().getName());
        boolean isMine = false;
        boolean isHearted = false;
        boolean isReserved = false;
        Optional<Reserve> reserve = reserveJPARepository.findByUserAndPost(visitor, post);
        if ( reserve.isPresent()) { isReserved = true; }
        if (visitor.equals(writer)) {isMine = true;} // 본인 글 체크
        Optional<Heart> heart = heartJPARepository.findByUserAndPost(visitor, post);
        if(heart.isPresent()) { isHearted = true ;}
        PostResponse.FindByIdDTO findByIdDTO = new PostResponse.FindByIdDTO(post, writer, isMine, isHearted, isReserved);
        return new PageResponse<>( null, findByIdDTO);
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


    public Post getPostById(Long postId) {
            return  postJPARepository.findById(postId).orElseThrow(
                    () -> new IllegalArgumentException("해당 포스트를 찾을 수 없습니다.")
            );
    }

    private User getUserByName(String userName) {
        Optional<User> user = userRepository.findByName(userName);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
        } else {
            System.out.println(user.get().getId()+user.get().getName());
            log.info(user.get().getId()+user.get().getName());
            return user.get();
        }
    }

    private FeeRange getFeeRange(int fee) {
        FeeRange feeRange;
        if (fee == 0) { feeRange = FeeRange.FREE ;}
        else if ( fee < 30000 ) { feeRange = FeeRange.UNDER_3 ;}
        else if ( fee < 50000 ) { feeRange = FeeRange.BETWEEN3_5; }
        else if ( fee < 100000 ) { feeRange = FeeRange.BETWEEN5_10 ; }
        else { feeRange = FeeRange.OVER_10 ;  }
        return feeRange;
    }


}
