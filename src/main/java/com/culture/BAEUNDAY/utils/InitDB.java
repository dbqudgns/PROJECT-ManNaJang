package com.culture.BAEUNDAY.utils;

import com.culture.BAEUNDAY.domain.post.Post;
import com.culture.BAEUNDAY.domain.post.PostJPARepository;
import com.culture.BAEUNDAY.domain.review.Review;
import com.culture.BAEUNDAY.domain.review.ReviewRepository;
import com.culture.BAEUNDAY.domain.user.User;
import com.culture.BAEUNDAY.domain.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
      // initService.dbInit_review();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final UserRepository userRepository;
        private final PostJPARepository postJPARepository;
        private final ReviewRepository reviewRepository;

        public void dbInit_review() {

            // 1. 리뷰를 받을 사용자(reviewee) 목록을 준비
            User reviewee1 = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("Error: User with ID 1 not found."));
            User reviewee2 = userRepository.findById(2L).orElseThrow(() -> new RuntimeException("Error: User with ID 2 not found."));
            User reviewee3 = userRepository.findById(3L).orElseThrow(() -> new RuntimeException("Error: User with ID 3 not found."));
            List<User> reviewees = List.of(reviewee1, reviewee2, reviewee3);

            // 2. 각 reviewee에 해당하는 게시물을 준비
            Post postA = postJPARepository.findById(1L).orElseThrow(() -> new RuntimeException("Error: Post with ID 1 not found.")); // reviewee1의 게시글
            Post postB = postJPARepository.findById(2L).orElseThrow(() -> new RuntimeException("Error: Post with ID 2 not found.")); // reviewee2의 게시글
            Post postC = postJPARepository.findById(3L).orElseThrow(() -> new RuntimeException("Error: Post with ID 3 not found.")); // reviewee3의 게시글
            List<Post> posts = List.of(postA, postB, postC);

            // 3. 리뷰를 작성할 사용자(reviewer)를 준비
            User reviewerA = userRepository.findById(4L).orElseThrow(() -> new RuntimeException("Error: User with ID 4 not found."));
            User reviewerB = userRepository.findById(5L).orElseThrow(() -> new RuntimeException("Error: User with ID 5 not found."));

            int batchSize = 1000; // 한 번에 저장할 엔티티 수
            List<Review> reviewBatch = new ArrayList<>();
            LocalDateTime reviewDate = LocalDateTime.of(2025, 9, 16, 12, 0, 0);

            for (int i = 0; i < 500000; i++) {
                // 4. reviewee와 post를 순환하며 선택
                // i % 3 의 결과(0, 1, 2)를 인덱스로 사용하여 reviewee와 post를 매칭
                int currentIndex = i % reviewees.size();
                User currentReviewee = reviewees.get(currentIndex);
                Post currentPost = posts.get(currentIndex);

                // 리뷰어는 기존 로직대로 번갈아 가며 선택
                User currentReviewer = (i % 2 == 0) ? reviewerA : reviewerB;
                int star = (i % 5) + 1;
                LocalDateTime createdDate = reviewDate.plusSeconds(i);

                Review review = createReview(currentReviewee, currentReviewer, currentPost, "별점 " + star + "개", star, createdDate);
                reviewBatch.add(review);

                if ((i + 1) % batchSize == 0) {
                    reviewRepository.saveAll(reviewBatch);
                    reviewBatch.clear();
                    System.out.println((i + 1) + "개의 데이터 생성");
                }
            }

            if (!reviewBatch.isEmpty()) {
                reviewRepository.saveAll(reviewBatch);
            }
            System.out.println(500000 + "개의 리뷰 데이터 생성 완료");
        }

        public static Review createReview(User reviewee, User currentReviewer, Post post,
                                          String field, int star, LocalDateTime createdDate) {
            // Review 엔티티 생성 (Builder 패턴을 사용한다고 가정)
            Review newReview = Review.builder()
                    .reviewee(reviewee)
                    .reviewer(currentReviewer)
                    .post(post)
                    .star(star)
                    .field(field)
                    .createdDate(createdDate)
                    .build();

            return newReview;
        }
    }
}
