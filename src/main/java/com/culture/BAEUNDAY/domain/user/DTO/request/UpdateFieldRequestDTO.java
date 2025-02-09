package com.culture.BAEUNDAY.domain.user.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record UpdateFieldRequestDTO(
        @NotBlank(message = "필수 입력 항목입니다.")
        String field
) {
}
