package com.culture.BAEUNDAY.domain.review.DTO.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReviewResponseDTO(
        Long review_id,
        String name,
        String field,
        Integer star,
        LocalDateTime createdDate
){
}
