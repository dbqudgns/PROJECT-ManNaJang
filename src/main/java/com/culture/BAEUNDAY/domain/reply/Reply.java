package com.culture.BAEUNDAY.domain.reply;

import com.culture.BAEUNDAY.domain.comment.Comment;
import com.culture.BAEUNDAY.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reply")
@Getter
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @Column(nullable = false)
    private String field;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Builder
    public Reply(User user, Comment comment, String field, LocalDateTime createdDate) {
        this.user = user;
        this.comment = comment;
        this.field = field;
        this.createdDate = createdDate;
    }

    public Reply() {

    }

    public void updateReply(String field) {
        this.field = field;
    }

}
