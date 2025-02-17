package com.culture.BAEUNDAY.domain.user.DTO.request;

import jakarta.validation.constraints.NotBlank;

public record CheckRequestDTO(
        @NotBlank(message = "필수 입력 항목입니다.")
        String checkname
) {
}
