package com.culture.BAEUNDAY.domain.post;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface PostCustomRepository {
    List<Post> findAllById(Long cursor, Long cursorId, Status status, FeeRange feeRange, Pageable pageable);
    List<Post> findAllByHeart(Integer cursor, Long cursorId, Status status, FeeRange feeRange, Pageable pageable);
    List<Post> findAllByDateTime(LocalDateTime cursor, Long cursorId, Status status, FeeRange feeRange, Pageable pageable);

}
