package com.culture.BAEUNDAY.domain.reserve;

import com.culture.BAEUNDAY.domain.post.Post;
import com.culture.BAEUNDAY.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReserveJPARepository extends JpaRepository<Reserve,Long> {
    Optional<Reserve> findByUserAndPost(User user, Post post);

    @Query("select r from Reserve r where r.user = :user and r.id < :cursor order by r.id desc ")
    List<Reserve> findByUserWithCursor(@Param("user")User user, @Param("cursor")Long cursor, Pageable request);
    @Query("select r from Reserve r where r.user = :user and r.id < :cursor and r.status = :filter order by r.id desc")
    List<Reserve> findByUserAndFilterWithCursor(@Param("user")User user, @Param("filter")Status filter, @Param("cursor")Long cursor, Pageable request);
}
