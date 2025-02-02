package com.culture.BAEUNDAY.domain.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostJPARepository extends JpaRepository<Post, Long> {


    @Query("select p from Post p where p.id < :cursor order by p.id desc ")
    List<Post> findAllByIdLessThanCursor(@Param("cursor") Long cursor, Pageable pageable);

    @Query("select p from Post p where (p.numsOfHeart < :cursor) or (p.numsOfHeart = :cursor and p.id < :cursorId) order by p.numsOfHeart desc ")
    List<Post> findAllByHeartLessThanCursor(@Param("cursor") Long cursor, @Param("cursorId") Long cursorId, Pageable pageable);

    @Query("select p from Post p where (p.createdDate < :cursor) or (p.createdDate = :cursor and p.id < :cursorId) order by p.createdDate desc ")
    List<Post> findAllByDateLessThanCursor(@Param("cursor") LocalDateTime cursor,  @Param("cursorId") Long cursorId, Pageable pageable);
}
