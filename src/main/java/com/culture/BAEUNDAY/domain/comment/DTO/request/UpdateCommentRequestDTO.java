package com.culture.BAEUNDAY.domain.comment.DTO.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateCommentRequestDTO(
        @NotBlank(message = "필수 입력 항목입니다.")
        String field
) {
}
