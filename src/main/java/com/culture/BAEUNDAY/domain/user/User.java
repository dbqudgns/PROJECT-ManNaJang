package com.culture.BAEUNDAY.domain.user;

import com.culture.BAEUNDAY.domain.comment.Comment;
import com.culture.BAEUNDAY.domain.heart.Heart;
import com.culture.BAEUNDAY.domain.reply.Reply;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_tb")
@Data
@DynamicInsert
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private Double manner = 36.5;

    private String field;

    private String profileImg;

    @OneToMany(mappedBy = "user")
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Reply> replies = new ArrayList<>();

    @Builder
    public User(String name, String username, String password, Role role, String field, String profileImg) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
        this.field = field;
        this.profileImg = profileImg;
    }

    public void profileUpdate(String name, String field) {
        this.name = name;
        this.field = field;
    }

    public User() {

    }

}
