package com.culture.BAEUNDAY.dto.response;

import lombok.Builder;

@Builder
public record CheckProfileResponseDTO(
        String name,
        String profileImg,
        String field
) {
}
