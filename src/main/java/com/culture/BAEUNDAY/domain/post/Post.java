package com.culture.BAEUNDAY.domain.post;

import com.culture.BAEUNDAY.domain.heart.Heart;
import com.culture.BAEUNDAY.domain.reserve.Reserve;
import com.culture.BAEUNDAY.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String imgURL;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String goal;

    @Column(nullable = false)
    private String outline;

    @Column(name = "target_student", nullable = false)
    private String targetStudent;

    @Column(nullable = false)
    private String level;

    @Column(name = "contact_method", nullable = false)
    private String contactMethod;

    @Column(nullable = false)
    private Integer fee;

    @Column(name = "fee_range", nullable = false)
    @Enumerated(EnumType.STRING)
    private FeeRange feeRange;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate ;

    @Column(name = "end_date", nullable = false)
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

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column(name = "nums_of_heart", nullable = false)
    private Integer numsOfHeart;

    @Column(name = "nums_of_participant", nullable = false)
    private Integer numsOfParticipant;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Reserve> reserves = new ArrayList<>();

    public void update(String title, String imgURL, String subject, String goal, String outline,
                       String targetStudent, String level, String contactMethod, Integer fee, FeeRange feeRange,
                       LocalDateTime startDate, LocalDateTime endDate, Province province, String city,
                       String address, int minP, int maxP, String content, Status status,
                       LocalDateTime createdDate, LocalDateTime deadline) {
        this.title = title;
        this.imgURL = imgURL;
        this.subject = subject;
        this.goal = goal;
        this.outline = outline;
        this.targetStudent = targetStudent;
        this.level = level;
        this.contactMethod = contactMethod;
        this.fee = fee;
        this.feeRange = feeRange;
        this.startDate = startDate;
        this.endDate = endDate;
        this.province = province;
        this.city = city;
        this.address = address;
        this.minP = minP;
        this.maxP = maxP;
        this.content = content;
        this.status = status;
        this.createdDate = createdDate;
        this.deadline = deadline;
    }

    public void addHeart(Heart heart){
        this.hearts.add(heart);
        this.numsOfHeart += 1;
    }
    public void removeHeart(Heart heart){
        this.hearts.remove(heart);
        this.numsOfHeart -= 1;
    }
    public void addParticipant(Reserve reserve){
        this.reserves.add(reserve);
        this.numsOfParticipant += 1;
        if (this.numsOfParticipant == this.maxP && this.status == Status.AVAILABLE) {
            this.status = Status.ING;
        }
    }
    public void removeParticipant(Reserve reserve){
        this.reserves.remove(reserve);
        this.numsOfParticipant -= 1;
        if (this.numsOfParticipant < this.maxP && this.status == Status.ING) {
            this.status = Status.AVAILABLE;
        }
    }


}
