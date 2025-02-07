package com.culture.BAEUNDAY.domain.comment;

import com.culture.BAEUNDAY.domain.comment.DTO.request.CommentRequestDTO;
import com.culture.BAEUNDAY.domain.comment.DTO.request.UpdateCommentRequestDTO;
import com.culture.BAEUNDAY.domain.comment.DTO.response.CommentResponseDTO;
import com.culture.BAEUNDAY.domain.post.Post;
import com.culture.BAEUNDAY.domain.post.PostJPARepository;
import com.culture.BAEUNDAY.domain.reply.DTO.response.ReplyResponseDTO;
import com.culture.BAEUNDAY.domain.reply.Reply;
import com.culture.BAEUNDAY.domain.review.Review;
import com.culture.BAEUNDAY.domain.user.User;
import com.culture.BAEUNDAY.domain.user.UserRepository;
import com.culture.BAEUNDAY.domain.user.UserService;
import com.culture.BAEUNDAY.jwt.Custom.CustomUserDetails;
import com.culture.BAEUNDAY.utils.CursorRequest;
import com.culture.BAEUNDAY.utils.CursorResponse;
import com.culture.BAEUNDAY.utils.PageResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostJPARepository postRepository;
    private final UserService userService;

    //한 페이지에 보여줄 댓글 개수 5개에 추가 1개(다음 페이지 존재 여부 확인)
    private static final int PAGE_SIZE_PLUSE_ONE = 5 + 1;

    public PageResponse<LocalDateTime, List<CommentResponseDTO>> getCommentsAndReplies(
            Long postId,
            String cursor,
            Long cursorId,
            CustomUserDetails customUserDetails) {

        User user = userService.findUserByUsernameOrThrow(customUserDetails.getUsername());

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));

        //커서 요청 객체 생성 : 커서 타입은 댓글의 작성일(LocalDateTime)으로 설정
        CursorRequest<LocalDateTime> pageRequest = new CursorRequest<>(PAGE_SIZE_PLUSE_ONE, cursor, LocalDateTime.class, cursorId);

        List<Comment> comments = commentRepository.findCommentsWithRepliesByPostId(
                postId,
                pageRequest.cursor,
                pageRequest.cursorID,
                pageRequest.request //6개의 댓글을 반환함
        );


        //조회된 댓글 리스트를 커서 응답 객체로 변환하여 반환
        return createCursorPageResponse(Comment::getCreatedDate, comments, user);
    }

    private PageResponse<LocalDateTime, List<CommentResponseDTO>> createCursorPageResponse(
            Function<Comment, LocalDateTime> cursorExtractor,
            List<Comment> comments,
            User user
    ) {

        if (comments.isEmpty()) {
            return new PageResponse<>(new CursorResponse<>(false, 0, null, null), null);
        }

        int size = comments.size();
        boolean hasNext = false;

        //조회 결과가 PAGE_SIZE_PLUS_ONE 건이면, 실제 응답에 포함할 댓글은 5건이고, 다음 페이지가 존재함을 표시
        if (size == PAGE_SIZE_PLUSE_ONE) {
            comments.remove(size-1);
            size--;
            hasNext = true;
        }

       List<CommentResponseDTO> commentResponseDTOs = new ArrayList<>(); //전체 댓글 및 대댓글을 반환하는 리스트

        for (Comment comment : comments) {
            commentResponseDTOs.add(convertToCommentResponse(user, comment));
        }

        //마지막 댓글의 값을 기준으로 다음 페이지 요청 시 사용할 커서 정보 설정
        Comment lastComment = comments.get(size - 1);
        LocalDateTime nextCursor = cursorExtractor.apply(lastComment);
        Long nextCursorId = lastComment.getId();

        return new PageResponse<>(new CursorResponse<>(hasNext, size, nextCursor, nextCursorId), commentResponseDTOs);



    }

    @Transactional
    public CommentResponseDTO registerComment(Long postId, CommentRequestDTO commentRequestDTO, CustomUserDetails customUserDetails) {

        User user = userService.findUserByUsernameOrThrow(customUserDetails.getUsername());

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .field(commentRequestDTO.field())
                .createdDate(commentRequestDTO.createdDate())
                .build();

        commentRepository.save(comment);

        return CommentResponseDTO.builder()
                .comment_id(comment.getId())
                .name(user.getName())
                .profileImg(user.getProfileImg())
                .field(comment.getField())
                .createdDate(comment.getCreatedDate())
                .replies(List.of())
                .build();
    }

    @Transactional
    public CommentResponseDTO updateComment(Long commentId, UpdateCommentRequestDTO updateCommentRequestDTO, CustomUserDetails customUserDetails) throws AccessDeniedException {

        User user = userService.findUserByUsernameOrThrow(customUserDetails.getUsername());

        Comment updateComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다."));

        if(!Objects.equals(user.getName(), updateComment.getUser().getName())) {
            throw new AccessDeniedException("댓글을 수정할 수 있는 권한이 없습니다.");
        }

        updateComment.updateComment(updateCommentRequestDTO.field());

       return convertToCommentResponse(user, updateComment);
    }

    @Transactional
    public Map<String, Object> deleteComment(Long commentId, CustomUserDetails customUserDetails) throws AccessDeniedException {

        User user = userService.findUserByUsernameOrThrow(customUserDetails.getUsername());

        Comment deleteComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다."));

        if (!Objects.equals(deleteComment.getUser().getName(), user.getName())) {
            throw new AccessDeniedException("댓글을 삭제할 수 있는 권한이 없습니다.");
        }

        commentRepository.delete(deleteComment);

        return Map.of(
                "status", 200,
                "message", "정상적으로 삭제 됐습니다."
        );
    }

    private CommentResponseDTO convertToCommentResponse(User user, Comment comment) {

        List<ReplyResponseDTO> replyResponseDTOs = new ArrayList<>(); //하나의 댓글에 존재하는 대댓글을 저장하는 리스트

        for (Reply reply : comment.getReplies()) {

            replyResponseDTOs.add(convertToReplyResponseDTO(user, reply));

        }

        return CommentResponseDTO.builder()
                .comment_id(comment.getId())
                .name(comment.getUser().getName())
                .profileImg(user.getProfileImg())
                .field(comment.getField())
                .createdDate(comment.getCreatedDate())
                .replies(replyResponseDTOs)
                .build();


    }

    private ReplyResponseDTO convertToReplyResponseDTO(User user, Reply reply) {

       return ReplyResponseDTO.builder()
                .reply_id(reply.getId())
                .name(reply.getUser().getName())
                .profileImg(user.getProfileImg())
                .field(reply.getField())
                .createdDate(reply.getCreatedDate())
                .build();

    }


}
