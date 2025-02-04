package com.culture.BAEUNDAY.dto.response;

import lombok.Builder;

@Builder
public record CheckUsernameResponseDTO(String username, Integer successUserName, String message) {
}
