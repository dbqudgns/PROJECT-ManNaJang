package com.culture.BAEUNDAY.domain.comment.DTO.response;

import com.culture.BAEUNDAY.domain.reply.DTO.response.ReplyResponseDTO;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CommentResponseDTO(
        Long comment_id,
        String name,
        String profileImg,
        String field,
        LocalDateTime createdDate,
        List<ReplyResponseDTO> replies
) {
}
