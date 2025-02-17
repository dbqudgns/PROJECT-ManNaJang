package com.culture.BAEUNDAY.domain.reply.DTO.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReplyResponseDTO(

        Long reply_id,
        String name,
        String profileImg,
        String field,
        LocalDateTime createdDate

) {
}
