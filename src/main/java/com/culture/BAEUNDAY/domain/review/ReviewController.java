package com.culture.BAEUNDAY.domain.review;

import com.culture.BAEUNDAY.domain.review.DTO.request.ReviewRequestDTO;
import com.culture.BAEUNDAY.domain.review.DTO.request.updateReviewRequestDTO;
import com.culture.BAEUNDAY.domain.review.DTO.response.ReviewResponseDTO;
import com.culture.BAEUNDAY.jwt.Custom.CustomUserDetails;
import com.culture.BAEUNDAY.utils.ApiUtils;
import com.culture.BAEUNDAY.utils.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
@Tag(name = "별점 api", description = "별점 CRUD")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/me")
    @Operation(summary = "나의 별점 조회")
    public ResponseEntity<?> getMyReviews(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                          @RequestParam(value = "cursor", required = false) String cursor) {
        PageResponse<Long, List<ReviewResponseDTO>> responseDto = reviewService.getMyReviews(customUserDetails,cursor);
        return ResponseEntity.ok(ApiUtils.success(responseDto));
    }

    @GetMapping("/other/{user_id}")
    @Operation(summary = "특정 사용자의 별점 조회")
    public ResponseEntity<?> getOtherReviews(@PathVariable("user_id") Long id,
                                            @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                             @RequestParam(value = "cursor", required = false) String cursor) {
        PageResponse<Long, List<ReviewResponseDTO>> responseDto = reviewService.getOtherReviews(id, customUserDetails,cursor);
        return ResponseEntity.ok(ApiUtils.success(responseDto));
    }

    @PostMapping("/{post_id}")
    @Operation(summary = "별점 등록")
    public ResponseEntity<?> registerReview(@PathVariable("post_id") Long id, @RequestBody @Valid ReviewRequestDTO reviewRequest,
                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(reviewService.registerReview(id, reviewRequest, customUserDetails));
    }

    @PutMapping("/{review_id}")
    @Operation(summary = "별점 수정")
    public ResponseEntity<?> updateReview(@PathVariable("review_id") Long id,
                                          @RequestBody @Valid updateReviewRequestDTO updateReviewRequest,
                                          @AuthenticationPrincipal CustomUserDetails customUserDetails) throws AccessDeniedException {
        return ResponseEntity.ok(reviewService.updateReview(id, updateReviewRequest, customUserDetails));
    }

    @DeleteMapping("/{review_id}")
    @Operation(summary = "별점 삭제")
    public ResponseEntity<?> deleteReview(@PathVariable("review_id") Long id,
                                          @AuthenticationPrincipal CustomUserDetails customUserDetails) throws AccessDeniedException {
        return ResponseEntity.ok(reviewService.deleteReview(id, customUserDetails));
    }
}
