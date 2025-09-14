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

            // 1. 리뷰를 받을 사용자(reviewee) 목록을 준비합니다.
            User reviewee1 = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("Error: User with ID 1 not found."));
            User reviewee2 = userRepository.findById(2L).orElseThrow(() -> new RuntimeException("Error: User with ID 2 not found."));
            User reviewee3 = userRepository.findById(3L).orElseThrow(() -> new RuntimeException("Error: User with ID 3 not found."));
            List<User> reviewees = List.of(reviewee1, reviewee2, reviewee3);

            // 2. 각 reviewee에 해당하는 게시물을 준비합니다.
            Post postA = postJPARepository.findById(1L).orElseThrow(() -> new RuntimeException("Error: Post with ID 1 not found.")); // reviewee1의 게시글
            Post postB = postJPARepository.findById(2L).orElseThrow(() -> new RuntimeException("Error: Post with ID 2 not found.")); // reviewee2의 게시글
            Post postC = postJPARepository.findById(3L).orElseThrow(() -> new RuntimeException("Error: Post with ID 3 not found.")); // reviewee3의 게시글
            List<Post> posts = List.of(postA, postB, postC);

            // 3. 리뷰를 작성할 사용자(reviewer) 50명을 새로 생성
            List<User> reviewers = new ArrayList<>();
            for (int i = 1; i <= 50; i++) {
                // 실제 User 엔티티의 필드에 맞게 수정해주세요.
                User reviewer = User.builder()
                        .username("reviewer" + i)
                        .name("리뷰작성자" + i)
                        .password("password123")
                        .build();
                reviewers.add(reviewer);
            }
            userRepository.saveAll(reviewers); // 생성된 50명의 유저를 DB에 저장
            System.out.println("리뷰어 50명 생성 완료");


            // 4. 리뷰 생성을 위한 준비
            int batchSize = 1000; // 한 번에 저장할 엔티티 수
            List<Review> reviewBatch = new ArrayList<>();
            LocalDateTime reviewDate = LocalDateTime.of(2025, 9, 16, 12, 0, 0);
            int reviewCount = 0; // 생성된 리뷰 개수 카운터

            // 5. 50명의 리뷰어가 3명의 리뷰이에게 각각 리뷰를 작성하는 로직
            for (User reviewer : reviewers) { // 50명의 리뷰어를 순회
                for (int i = 0; i < reviewees.size(); i++) { // 3명의 리뷰이를 순회
                    User currentReviewee = reviewees.get(i);
                    Post currentPost = posts.get(i);

                    int star = (reviewCount % 5) + 1; // 별점은 1~5 순환
                    LocalDateTime createdDate = reviewDate.plusSeconds(reviewCount);

                    Review review = createReview(currentReviewee, reviewer, currentPost, "별점 " + star + "개", star, createdDate);
                    reviewBatch.add(review);
                    reviewCount++;

                    // 배치 사이즈에 도달하면 DB에 저장
                    if (reviewCount % batchSize == 0) {
                        reviewRepository.saveAll(reviewBatch);
                        reviewBatch.clear();
                        System.out.println(reviewCount + "개의 데이터 생성");
                    }
                }
            }


            // 6. 마지막으로 남은 배치 처리
            if (!reviewBatch.isEmpty()) {
                reviewRepository.saveAll(reviewBatch);
            }
            System.out.println("총 " + reviewCount + "개의 리뷰 데이터 생성 완료");
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
