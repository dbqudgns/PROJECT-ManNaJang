package com.culture.BAEUNDAY.domain.reply.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReplyRequestDTO(

        @NotBlank(message = "필수 입력 항목입니다.")
        String field,

        @NotNull(message = "작성한 시간을 보내주세요.")
        LocalDateTime createdDate
        ) {
}
