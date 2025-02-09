package com.culture.BAEUNDAY.domain.user.DTO.response;

import lombok.Builder;

@Builder
public record ImageResponseDTO(
        String name,
        String profileImg
) {
}
