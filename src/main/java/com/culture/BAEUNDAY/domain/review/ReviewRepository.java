package com.culture.BAEUNDAY.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r where r.reviewee.id = :userId")
    List<Review> findByRevieweeId(@Param("userId") Long id);

}