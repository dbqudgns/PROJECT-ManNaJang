package com.culture.BAEUNDAY.domain.user.DTO.response;

import lombok.Builder;

@Builder
public record CheckProfileResponseDTO(
        String name,
        String profileImg,
        String field
) {
}
