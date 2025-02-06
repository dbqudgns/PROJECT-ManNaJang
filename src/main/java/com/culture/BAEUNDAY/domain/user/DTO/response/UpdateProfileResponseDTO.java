package com.culture.BAEUNDAY.domain.user.DTO.response;

import lombok.Builder;

@Builder
public record UpdateProfileResponseDTO(
        String message,
        String name,
        String field) {
}
