package com.culture.BAEUNDAY.domain.review.DTO.response;

import com.culture.BAEUNDAY.domain.review.Review;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReviewResponseDTO(
        Long review_id,
        String name,
        String field,
        Integer star,
        String title,
        LocalDateTime createdDate
){
}
