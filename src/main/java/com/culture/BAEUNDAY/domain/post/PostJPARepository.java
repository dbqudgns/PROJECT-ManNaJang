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


    //find by id with cursor
    @Query("select p from Post p where p.id < :cursor order by p.id desc ")
    List<Post> findAllByIdLessThanCursor(@Param("cursor") Long cursor, Pageable pageable);

    @Query("select p from Post p where p.id < :cursor and p.status = :status and p.feeRange = :fee order by p.id desc ")
    List<Post> findAllByIdAndStatusAndFeeLessThanCursor(@Param("cursor")Long cursor, @Param("status")Status status, @Param("fee")Fee fee, Pageable request);

    @Query("select p from Post p where p.id < :cursor and p.feeRange = :fee order by p.id desc ")
    List<Post> findAllByIdAndFeeLessThanCursor(@Param("cursor")Long cursor,@Param("fee")Fee fee, Pageable request);

    @Query("select p from Post p where p.id < :cursor and p.status = :status order by p.id desc ")
    List<Post> findAllByIdAndStatusLessThanCursor(@Param("cursor")Long cursor, @Param("status")Status status, Pageable request);


    //find by heart with cursor
    @Query("select p from Post p where (p.numsOfHeart < :cursor and p.status = :status and p.feeRange = :fee ) or (p.numsOfHeart = :cursor and p.id < :cursorId and p.status = :status and p.feeRange = :fee )  order by p.numsOfHeart desc ")
    List<Post> findAllByHeartAndStatusAndFeeLessThanCursor(@Param("cursor")Integer cursor,  @Param("cursorId") Long cursorId, @Param("status")Status status, @Param("fee")Fee fee, Pageable request);
    @Query("select p from Post p where (p.numsOfHeart < :cursorand and p.feeRange = :fee ) or (p.numsOfHeart = :cursor and p.id < :cursorId and p.feeRange = :fee ) order by p.numsOfHeart desc ")
    List<Post> findAllByHeartAndFeeLessThanCursor(@Param("cursor")Integer cursor,  @Param("cursorId") Long cursorId, @Param("fee")Fee fee, Pageable request);
    @Query("select p from Post p where (p.numsOfHeart < :cursor and p.status = :status) or (p.numsOfHeart = :cursor and p.id < :cursorId and p.status = :status)  order by p.numsOfHeart desc ")
    List<Post> findAllByHeartAndStatusLessThanCursor(@Param("cursor")Integer cursor, @Param("cursorId") Long cursorId, @Param("status")Status status, Pageable request);
    @Query("select p from Post p where (p.numsOfHeart < :cursor) or (p.numsOfHeart = :cursor and p.id < :cursorId)  order by p.numsOfHeart desc ")
    List<Post> findAllByHeartLessThanCursor(@Param("cursor") Integer cursor, @Param("cursorId") Long cursorId, Pageable pageable);

    //find by date with cursor

    @Query("select p from Post p where (p.createdDate < :cursor ) or (p.createdDate = :cursor and p.id < :cursorId ) order by p.createdDate desc ")
    List<Post> findAllByDateLessThanCursor(@Param("cursor") LocalDateTime cursor,  @Param("cursorId") Long cursorId, Pageable pageable);

    @Query("select p from Post p where (p.createdDate < :cursor and p.status = :status and p.feeRange = :fee) or (p.createdDate = :cursor and p.id < :cursorId and p.status = :status and p.feeRange = :fee) order by p.createdDate desc ")
    List<Post> findAllByDateAndStatusAndFeeLessThanCursor(@Param("cursor")LocalDateTime cursor, @Param("cursorId")Long cursorID, @Param("status")Status status, @Param("fee")Fee fee, Pageable request);
    @Query("select p from Post p where (p.createdDate < :cursor and p.feeRange = :fee ) or (p.createdDate = :cursor and p.id < :cursorId and p.feeRange = :fee) order by p.createdDate desc ")
    List<Post> findAllByDateAndFeeLessThanCursor(@Param("cursor") LocalDateTime cursor, @Param("cursorId")Long cursorID, @Param("fee")Fee fee, Pageable request);
    @Query("select p from Post p where (p.createdDate < :cursor and p.status = :status ) or (p.createdDate = :cursor and p.id < :cursorId and p.status = :status) order by p.createdDate desc ")
    List<Post> findAllByDateAndStatusLessThanCursor(@Param("cursor")LocalDateTime cursor, @Param("cursorId")Long cursorID, @Param("status")Status status, Pageable request);
}
