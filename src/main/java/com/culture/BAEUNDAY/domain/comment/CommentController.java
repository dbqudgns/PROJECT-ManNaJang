package com.culture.BAEUNDAY.domain.comment;

import com.culture.BAEUNDAY.domain.comment.DTO.request.CommentRequestDTO;
import com.culture.BAEUNDAY.domain.comment.DTO.request.UpdateCommentRequestDTO;
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
@RequestMapping("/comments")
@RequiredArgsConstructor
@Tag(name = "댓글 api", description = "댓글 CRUD")
public class CommentController {

    private final CommentService commentService;


    @GetMapping("/{post_id}")
    @Operation(summary = "댓글과 대댓글 전체 조회")
    public ResponseEntity<?> getCommentsAndReplies(
            @PathVariable("post_id") Long postId,
            @RequestParam(required = false) String cursor,
            @RequestParam(required = false) Long cursorId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.ok(
                commentService.getCommentsAndReplies(postId, cursor, cursorId, customUserDetails)
        );
    }


    @PostMapping("/{post_id}")
    @Operation(summary = "댓글 등록")
    public ResponseEntity<?> registerComment(@PathVariable("post_id") Long id, @RequestBody @Valid CommentRequestDTO commentRequestDTO,
                                             @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(commentService.registerComment(id, commentRequestDTO, customUserDetails));
    }

    @PutMapping("/{comment_id}")
    @Operation(summary = "댓글 수정")
    public ResponseEntity<?> updateComment(@PathVariable("comment_id") Long id, @RequestBody @Valid UpdateCommentRequestDTO updateCommentRequestDTO,
                                           @AuthenticationPrincipal CustomUserDetails customUserDetails) throws AccessDeniedException {
        return ResponseEntity.ok(commentService.updateComment(id, updateCommentRequestDTO, customUserDetails));
    }

    @DeleteMapping("/{comment_id}")
    @Operation(summary = "댓글 삭제")
    public ResponseEntity<?> deleteComment(@PathVariable("comment_id") Long id, @AuthenticationPrincipal CustomUserDetails customUserDetails) throws AccessDeniedException {
        return ResponseEntity.ok(commentService.deleteComment(id, customUserDetails));
    }
}
