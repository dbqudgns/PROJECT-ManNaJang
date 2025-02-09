package com.culture.BAEUNDAY.utils.s3;

import lombok.Builder;

@Builder
public record ImageResponseDTO(
        String name,
        String profileImg
) {
}
