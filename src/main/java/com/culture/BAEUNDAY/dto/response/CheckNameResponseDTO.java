package com.culture.BAEUNDAY.dto.response;

import lombok.Builder;

@Builder
public record CheckNameResponseDTO(String name, Integer successName, String message) {
}
