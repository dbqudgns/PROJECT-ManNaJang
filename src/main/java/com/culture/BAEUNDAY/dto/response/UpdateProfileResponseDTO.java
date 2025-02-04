package com.culture.BAEUNDAY.dto.response;

import lombok.Builder;

@Builder
public record UpdateProfileResponseDTO(
        String message,
        String name,
        String field
) {



}
