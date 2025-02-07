package com.culture.BAEUNDAY.domain.reply.DTO.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateReplyRequestDTO(
        @NotBlank(message = "필수 입력 항목입니다.")
        String field
) {
}
