package com.culture.BAEUNDAY.domain.review.DTO.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record updateReviewResponseDTO(
        String name,
        String field,
        Integer star
){
}
