package com.culture.BAEUNDAY.dto.response;

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
