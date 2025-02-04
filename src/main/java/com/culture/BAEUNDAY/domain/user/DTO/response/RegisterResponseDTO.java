package com.culture.BAEUNDAY.domain.user.DTO.response;

import lombok.Builder;

@Builder
public record RegisterResponseDTO(
        String message,
        String name,
        String username,
        String profileImg,
        Double manner
) {



}
