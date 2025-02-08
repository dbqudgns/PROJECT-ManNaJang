package com.culture.BAEUNDAY.domain.review;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r where r.reviewee.id = :userId and r.id < :cursor order by r.id desc ")
    List<Review> findByRevieweeIdWithCursor(@Param("userId") Long id, @Param("cursor") Long cursor, Pageable request);

}