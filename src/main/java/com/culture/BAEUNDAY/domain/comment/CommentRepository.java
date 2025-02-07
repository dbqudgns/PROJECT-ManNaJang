package com.culture.BAEUNDAY.domain.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT DISTINCT c FROM Comment c " +
            "LEFT JOIN FETCH c.replies r " +
            "WHERE c.post.id = :postId " +
            "AND (c.createdDate < :cursor OR (c.createdDate = :cursor AND c.id < :cursorId)) " +
            "ORDER BY c.createdDate DESC, c.id DESC, r.createdDate DESC, r.id DESC")
    List<Comment> findCommentsWithRepliesByPostId(
            @Param("postId") Long postId,
            @Param("cursor") LocalDateTime cursor,
            @Param("cursorId") Long cursorId,
            Pageable pageable);



}
