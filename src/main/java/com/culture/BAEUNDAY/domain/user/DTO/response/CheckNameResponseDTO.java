package com.culture.BAEUNDAY.domain.user.DTO.response;

import lombok.Builder;

@Builder
public record CheckNameResponseDTO(String name, Integer successName, String message) {
}
