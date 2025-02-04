package com.culture.BAEUNDAY.domain.user.DTO.response;

import lombok.Builder;

@Builder
public record CheckUsernameResponseDTO(String username, Integer successUserName, String message) {
}
