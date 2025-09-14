package com.culture.BAEUNDAY.domain.review;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r join fetch r.reviewer u where r.reviewee.id = :revieweeId and r.id < :cursor order by r.id desc ")
    List<Review> findByRevieweeIdWithCursor(@Param("revieweeId") Long id, @Param("cursor") Long cursor, Pageable request);

    // 1. 첫 페이지 조회용 (WHERE 절이 매우 단순함. id 조건 없음)
    @Query( "SELECT r " +
            "FROM Review r JOIN FETCH r.reviewer " +
            "WHERE r.reviewee.id = :revieweeId " +
            "ORDER BY r.id DESC")
    List<Review> findFirstPageByRevieweeId(@Param("revieweeId") Long id, Pageable request);

    // 2. 다음 페이지 조회용 (id < cursor 조건 있음)
    @Query( "SELECT r " +
            "FROM Review r JOIN FETCH r.reviewer " +
            "WHERE r.reviewee.id = :revieweeId AND r.id < :cursor " +
            "ORDER BY r.id DESC")
    List<Review> findNextPageByRevieweeIdWithCursor(@Param("revieweeId") Long id, @Param("cursor") Long cursor, Pageable request);
}