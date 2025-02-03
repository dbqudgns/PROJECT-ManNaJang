package com.culture.BAEUNDAY.domain.post;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post_tb")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;

    @Column(nullable = false)
    private Long user_id;

    @Column
    private String imgURL;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String goal;

    @Column(nullable = false)
    private String outline;

    @Column(nullable = false)
    private String targetStudent;

    @Column(nullable = false)
    private String level;

    @Column(nullable = false)
    private String contactMethod;

    @Column(nullable = false)
    private Integer fee;

    @Column(nullable = false)
    private LocalDateTime startDate ;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Province province;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private int minP;

    @Column(nullable = false)
    private int maxP;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime createdDate ;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column()
    private Long numsOfHeart;
    public void update(){

    }

    public void delete(){

    }
}
