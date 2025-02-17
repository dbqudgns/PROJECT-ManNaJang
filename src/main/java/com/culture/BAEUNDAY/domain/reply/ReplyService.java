package com.culture.BAEUNDAY.domain.reply;

import com.culture.BAEUNDAY.domain.comment.Comment;
import com.culture.BAEUNDAY.domain.comment.CommentRepository;
import com.culture.BAEUNDAY.domain.reply.DTO.request.ReplyRequestDTO;
import com.culture.BAEUNDAY.domain.reply.DTO.request.UpdateReplyRequestDTO;
import com.culture.BAEUNDAY.domain.reply.DTO.response.ReplyResponseDTO;
import com.culture.BAEUNDAY.domain.user.User;
import com.culture.BAEUNDAY.domain.user.UserService;
import com.culture.BAEUNDAY.jwt.Custom.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Transactional
    public ReplyResponseDTO registerReply(Long commentId, ReplyRequestDTO replyRequestDTO, CustomUserDetails customUserDetails) throws AccessDeniedException {

        User user = userService.findUserByUsernameOrThrow(customUserDetails.getUsername());

        Comment ParentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 대댓글을 찾을 수 없습니다."));

        //게시글을 작성한 사람만 대댓글을 작성 가능
        if (!Objects.equals(ParentComment.getPost().getUser().getName(), user.getName())) {
            throw new AccessDeniedException("게시글 작성자만 대댓글을 작성할 수 있습니다.");

        }

        Reply reply = Reply.builder()
                .user(user)
                .comment(ParentComment)
                .field(replyRequestDTO.field())
                .createdDate(replyRequestDTO.createdDate())
                .build();

        replyRepository.save(reply);

        return ReplyResponseDTO.builder()
                .reply_id(reply.getId())
                .name(reply.getUser().getName())
                .profileImg(user.getProfileImg())
                .field(reply.getField())
                .createdDate(reply.getCreatedDate())
                .build();
    }

    @Transactional
    public ReplyResponseDTO updateReply(Long replyId, UpdateReplyRequestDTO replyRequestDTO, CustomUserDetails customUserDetails) throws AccessDeniedException {

        User user = userService.findUserByUsernameOrThrow(customUserDetails.getUsername());

        Reply updateReply = replyRepository.findById(replyId)
                .orElseThrow(() -> new EntityNotFoundException("해당 대댓글을 찾을 수 없습니다."));

        if (!Objects.equals(user.getName(), updateReply.getUser().getName())) {
            throw new AccessDeniedException("대댓글을 수정할 수 있는 권한이 없습니다.");
        }

        updateReply.updateReply(replyRequestDTO.field());

        return ReplyResponseDTO.builder()
                .reply_id(updateReply.getId())
                .name(updateReply.getUser().getName())
                .profileImg(user.getProfileImg())
                .field(updateReply.getField())
                .createdDate(updateReply.getCreatedDate())
                .build();
    }

    @Transactional
    public Map<String, Object> deleteReply(Long replyId, CustomUserDetails customUserDetails) throws AccessDeniedException {

        User user = userService.findUserByUsernameOrThrow(customUserDetails.getUsername());

        Reply deleteReply = replyRepository.findById(replyId)
                .orElseThrow(() -> new EntityNotFoundException("해당 대댓글을 찾을 수 없습니다."));

        if (!Objects.equals(user.getName(), deleteReply.getUser().getName())) {
            throw new AccessDeniedException("대댓글을 삭제할 수 있는 권한이 없습니다.");
        }

        replyRepository.delete(deleteReply);

        return Map.of(
                "status", 200,
                "message", "정상적으로 삭제 됐습니다."
        );

    }




}
