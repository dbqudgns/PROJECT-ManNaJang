package com.culture.BAEUNDAY.s3;

import lombok.Builder;

@Builder
public record ImageResponseDTO(
        String name,
        String profileImg
) {
}
