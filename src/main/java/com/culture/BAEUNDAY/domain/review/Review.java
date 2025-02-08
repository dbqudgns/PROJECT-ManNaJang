package com.culture.BAEUNDAY.domain.review;

import com.culture.BAEUNDAY.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "review_tb")
@Getter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewee_id", nullable = false)
    private User reviewee; //리뷰를 받는 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer; //리뷰를 작성한 사람

    @Column(nullable = false)
    private String field;

    private Integer star;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Builder
    public Review(User reviewee, User reviewer, String field, Integer star, LocalDateTime createdDate) {
        this.reviewee = reviewee;
        this.reviewer = reviewer;
        this.field = field;
        this.star = star;
        this.createdDate = createdDate;
    }

    public Review() {

    }

    public void updateReview(String field, Integer star) {
        this.field = field;
        this.star = star;
    }

}
