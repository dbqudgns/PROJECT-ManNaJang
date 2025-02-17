package com.culture.BAEUNDAY.domain.reply;

import com.culture.BAEUNDAY.domain.reply.DTO.request.ReplyRequestDTO;
import com.culture.BAEUNDAY.domain.reply.DTO.request.UpdateReplyRequestDTO;
import com.culture.BAEUNDAY.jwt.Custom.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor
@Tag(name = "대댓글 api", description = "대댓글 CRUD")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/{comment_id}")
    @Operation(summary = "대댓글 작성")
    public ResponseEntity<?> registerReply(@PathVariable("comment_id") Long id, @RequestBody @Valid ReplyRequestDTO replyRequestDTO,
                                           @AuthenticationPrincipal CustomUserDetails customUserDetails) throws AccessDeniedException {
        return ResponseEntity.ok(replyService.registerReply(id, replyRequestDTO, customUserDetails));
    }

    @PutMapping("/{reply_id}")
    @Operation(summary = "대댓글 수정")
    public ResponseEntity<?> updateReply(@PathVariable("reply_id") Long id, @RequestBody @Valid UpdateReplyRequestDTO updateReplyRequestDTO,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) throws AccessDeniedException {
        return ResponseEntity.ok(replyService.updateReply(id, updateReplyRequestDTO, customUserDetails));
    }

    @DeleteMapping("/{reply_id}")
    @Operation(summary = "대댓글 삭제")
    public ResponseEntity<?> deleteReply(@PathVariable("reply_id") Long id, @AuthenticationPrincipal CustomUserDetails customUserDetails) throws AccessDeniedException {
        return ResponseEntity.ok(replyService.deleteReply(id, customUserDetails));
    }

}
