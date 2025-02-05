package com.culture.BAEUNDAY.domain.review;

import com.culture.BAEUNDAY.domain.review.DTO.request.ReviewRequestDTO;
import com.culture.BAEUNDAY.domain.review.DTO.request.updateReviewRequestDTO;
import com.culture.BAEUNDAY.domain.review.DTO.response.ReviewResponseDTO;
import com.culture.BAEUNDAY.domain.user.User;
import com.culture.BAEUNDAY.domain.user.UserRepository;
import com.culture.BAEUNDAY.jwt.Custom.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public List<ReviewResponseDTO> getMyReviews(CustomUserDetails customUserDetails) {

        User user = userRepository.findByUsername(customUserDetails.getUsername());

        if (user == null) {
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }

        List<ReviewResponseDTO> reviewList = new ArrayList<>();

        List<Review> reviews = reviewRepository.findByRevieweeId(user.getId());

        for (Review review : reviews) {

            reviewList.add(ReviewResponseDTO.builder()
                    .review_id(review.getId())
                    .name(review.getReviewer().getName())
                    .field(review.getField())
                    .star(review.getStar())
                    .createdDate(review.getCreatedDate())
                    .build());

        }

        return reviewList;
    }

    public List<ReviewResponseDTO> getOtherReviews(Long otherId, CustomUserDetails customUserDetails) {

        User user = userRepository.findByUsername(customUserDetails.getUsername());

        if (user == null) {
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }

        User findUser = userRepository.findById(otherId)
                .orElseThrow(() -> new EntityNotFoundException("해당 대상자를 찾을 수 없습니다."));

        List<ReviewResponseDTO> reviewList = new ArrayList<>();

        List<Review> reviews = reviewRepository.findByRevieweeId(otherId);

        for (Review review : reviews) {

            reviewList.add(ReviewResponseDTO.builder()
                    .review_id(review.getId())
                    .name(review.getReviewer().getName())
                    .field(review.getField())
                    .star(review.getStar())
                    .createdDate(review.getCreatedDate())
                    .build());

        }

        return reviewList;
    }

    @Transactional
    public ReviewResponseDTO registerReview(Long otherId, ReviewRequestDTO reviewRequest, CustomUserDetails customUserDetails) {

        User user = userRepository.findByUsername(customUserDetails.getUsername());

        if (user == null) {
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }

        User reviewee = userRepository.findById(otherId)
                .orElseThrow(() -> new EntityNotFoundException("별점 대상자를 찾을 수 없습니다."));

        Review review = Review.builder()
                .reviewer(user)
                .reviewee(reviewee)
                .field(reviewRequest.field())
                .star(reviewRequest.star())
                .createdDate(reviewRequest.createdDate())
                .build();

        reviewRepository.save(review);

        return ReviewResponseDTO.builder()
                .review_id(review.getId())
                .name(user.getName())
                .field(review.getField())
                .star(review.getStar())
                .createdDate(review.getCreatedDate())
                .build();

    }

    @Transactional
    public ReviewResponseDTO updateReview(Long reviewId, updateReviewRequestDTO updateReviewRequest, CustomUserDetails customUserDetails) throws AccessDeniedException {

        User user = userRepository.findByUsername(customUserDetails.getUsername());

        if (user == null) {
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }

        Review updateReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("해당 리뷰를 찾을 수 없습니다."));

        if (!Objects.equals(updateReview.getReviewer().getName(), user.getName())) {
            throw new AccessDeniedException("리뷰를 수정할 수 있는 권한이 없습니다.");
        }

        updateReview.updateReview(updateReviewRequest.field(), updateReviewRequest.star());

        return ReviewResponseDTO.builder()
                .review_id(updateReview.getId())
                .name(user.getName())
                .field(updateReview.getField())
                .star(updateReview.getStar())
                .createdDate(updateReview.getCreatedDate())
                .build();

    }

    @Transactional
    public Map<String, Object> deleteReview(Long reviewId, CustomUserDetails customUserDetails) throws AccessDeniedException {

        User user = userRepository.findByUsername(customUserDetails.getUsername());

        if (user == null) {
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }

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


}
