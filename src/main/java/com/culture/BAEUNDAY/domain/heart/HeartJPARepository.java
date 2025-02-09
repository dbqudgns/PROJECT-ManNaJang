package com.culture.BAEUNDAY.domain.heart;

import com.culture.BAEUNDAY.domain.post.Post;
import com.culture.BAEUNDAY.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HeartJPARepository  extends JpaRepository<Heart, Long> {

    @Override
    Optional<Heart> findById(Long aLong);

    Optional<Heart> findByUserAndPost(User user, Post post);

    @Query("select h from Heart h where( h.user = :user and h.createdDate < :cursor ) or (h.user = :user and h.createdDate = :cursor and h.id < :cursorId ) order by h.createdDate desc , h.id desc ")
    List<Heart> findByUser(User user, @Param("cursor") LocalDateTime cursor, @Param("cursorId") Long cursorId, Pageable pageable);
}
