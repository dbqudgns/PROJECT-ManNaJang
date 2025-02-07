package com.culture.BAEUNDAY.domain.comment;

import com.culture.BAEUNDAY.domain.post.Post;
import com.culture.BAEUNDAY.domain.reply.Reply;
import com.culture.BAEUNDAY.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment")
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String field;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replies =  new ArrayList<>();

    @Builder
    public Comment(User user, Post post, String field, LocalDateTime createdDate) {
        this.user = user;
        this.post = post;
        this.field = field;
        this.createdDate = createdDate;
    }

    public void updateComment(String field) {
        this.field = field;
    }

    public Comment() {

    }
}
