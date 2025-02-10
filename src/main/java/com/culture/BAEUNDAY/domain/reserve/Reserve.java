package com.culture.BAEUNDAY.domain.reserve;

import com.culture.BAEUNDAY.domain.post.Post;
import com.culture.BAEUNDAY.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "reserve_tb")
public class Reserve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Long postUserId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime reservationDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MyStatus myStatus;


    @Builder
    public Reserve(Post post, User user, Long postUserId, Status status, LocalDateTime reservationDate, MyStatus myStatus){
        this.post = post;
        this.user = user;
        this.postUserId = postUserId;
        this.status = status;
        this.reservationDate = reservationDate;
        this.myStatus = myStatus;
    }

}