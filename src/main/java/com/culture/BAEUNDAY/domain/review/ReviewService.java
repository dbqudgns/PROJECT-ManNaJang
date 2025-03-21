package com.culture.BAEUNDAY.domain.review;

import com.culture.BAEUNDAY.domain.post.Post;
import com.culture.BAEUNDAY.domain.post.PostJPARepository;
import com.culture.BAEUNDAY.domain.reserve.Reserve;
import com.culture.BAEUNDAY.domain.reserve.ReserveJPARepository;
import com.culture.BAEUNDAY.domain.review.DTO.request.ReviewRequestDTO;
import com.culture.BAEUNDAY.domain.review.DTO.request.updateReviewRequestDTO;
import com.culture.BAEUNDAY.domain.review.DTO.response.ReviewResponseDTO;
import com.culture.BAEUNDAY.domain.review.DTO.response.updateReviewResponseDTO;
import com.culture.BAEUNDAY.domain.user.User;
import com.culture.BAEUNDAY.domain.user.UserRepository;
import com.culture.BAEUNDAY.domain.user.UserService;
import com.culture.BAEUNDAY.jwt.Custom.CustomUserDetails;
import com.culture.BAEUNDAY.utils.CursorRequest;
import com.culture.BAEUNDAY.utils.CursorResponse;
import com.culture.BAEUNDAY.utils.PageResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PostJPARepository postJPARepository;
    private final ReserveJPARepository reserveJPARepository;

    private static final int PAGE_SIZE_PLUS_ONE = 5 + 1;

    public PageResponse<Long,  List<ReviewResponseDTO>> getMyReviews(CustomUserDetails customUserDetails, String cursor) {

        User user = userService.findUserByUsernameOrThrow(customUserDetails.getUsername());

        CursorRequest<Long> page = new CursorRequest<>(PAGE_SIZE_PLUS_ONE, cursor, Long.class, 0L) ;
        List<Review> reviews = reviewRepository.findByRevieweeIdWithCursor(user.getId(), page.cursor, page.request);

        return createCursorPageResponse(Review::getId, reviews);

    }

    public  PageResponse<Long,  List<ReviewResponseDTO>> getOtherReviews(Long otherId, CustomUserDetails customUserDetails,String cursor) {

        User user = userService.findUserByUsernameOrThrow(customUserDetails.getUsername());

        User findUser = userRepository.findById(otherId)
                .orElseThrow(() -> new EntityNotFoundException("해당 대상자를 찾을 수 없습니다."));

        CursorRequest<Long> page = new CursorRequest<>(PAGE_SIZE_PLUS_ONE, cursor, Long.class, 0L) ;
        List<Review> reviews = reviewRepository.findByRevieweeIdWithCursor(otherId, page.cursor, page.request);

        return createCursorPageResponse(Review::getId, reviews);
    }

    @Transactional
    public ReviewResponseDTO registerReview(Long postId, ReviewRequestDTO reviewRequest, CustomUserDetails customUserDetails) {

        User user = userService.findUserByUsernameOrThrow(customUserDetails.getUsername());

        Post post = postJPARepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));

        User reviewee = post.getUser();

        if (user.equals(reviewee)) {
            throw new IllegalArgumentException("게시글 작성자는 리뷰를 등록할 수 없습니다.");
        }

      /* Optional<Reserve> reserve = reserveJPARepository.findByUserAndPost(user, post);
        if (reserve.isEmpty()){
            throw new IllegalArgumentException("신청한 내역이 없습니다.");
        }*/

        Review review = Review.builder()
                .reviewee(reviewee)
                .reviewer(user)
                .post(post)
                .field(reviewRequest.field())
                .star(reviewRequest.star())
                .createdDate(reviewRequest.createdDate())
                .build();

        //reserve.get().changeMyStatus();
        reviewRepository.save(review);

        return ReviewResponseDTO.builder()
                .review_id(review.getId())
                .name(user.getName())
                .field(review.getField())
                .star(review.getStar())
                .title(post.getTitle())
                .createdDate(review.getCreatedDate())
                .build();

    }

    @Transactional
    public updateReviewResponseDTO updateReview(Long reviewId, updateReviewRequestDTO updateReviewRequest, CustomUserDetails customUserDetails) throws AccessDeniedException {

        User user = userService.findUserByUsernameOrThrow(customUserDetails.getUsername());

        Review updateReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("해당 리뷰를 찾을 수 없습니다."));

        if (!Objects.equals(updateReview.getReviewer().getName(), user.getName())) {
            throw new AccessDeniedException("리뷰를 수정할 수 있는 권한이 없습니다.");
        }

        updateReview.updateReview(updateReviewRequest.field(), updateReviewRequest.star());

        return updateReviewResponseDTO.builder()
                .name(user.getName())
                .field(updateReview.getField())
                .star(updateReview.getStar())
                .build();

    }

    @Transactional
    public Map<String, Object> deleteReview(Long reviewId, CustomUserDetails customUserDetails) throws AccessDeniedException {

        User user = userService.findUserByUsernameOrThrow(customUserDetails.getUsername());

        Review deleteReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("해당 리뷰를 찾을 수 없습니다."));

        if (!Objects.equals(deleteReview.getReviewer().getName(), user.getName())) {
            throw new AccessDeniedException("리뷰를 삭제할 수 있는 권한이 없습니다.");
        }

        reviewRepository.deleteById(reviewId);

        return Map.of(
                "status", 200,
                "message", "정상적으로 삭제 됐습니다."
        );

    }

    private PageResponse<Long, List<ReviewResponseDTO>> createCursorPageResponse(
            Function<Review, Long> cursorExtractor,
            List<Review> reviews
    ){
        if (reviews.isEmpty()) {
            return new PageResponse<>(new CursorResponse<>(false, 0,null,null ),null);
        }
        int size = reviews.size();
        boolean hasNext = false;
        if ( size == PAGE_SIZE_PLUS_ONE ){
            reviews.remove( size - 1) ;
            size -= 1;
            hasNext = true;
        }
        List<ReviewResponseDTO> reviewList = new ArrayList<>();

        for (Review review : reviews) {
            reviewList.add(ReviewResponseDTO.builder()
                    .review_id(review.getId())
                    .name(review.getReviewer().getName())
                    .field(review.getField())
                    .star(review.getStar())
                    .createdDate(review.getCreatedDate())
                    .build());
        }
        Review lastReview = reviews.get(size - 1) ;
        Long nextCursor =  lastReview.getId();
        return new PageResponse<>(new CursorResponse<>(hasNext, size, nextCursor, nextCursor), reviewList);

    }
}
