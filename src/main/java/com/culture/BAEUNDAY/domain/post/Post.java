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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; //

    @Column(nullable = false)
    private String title; //

    @Column
    private String imgURL; //

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDateTime; //

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDateTime; //

    @Column(nullable = false)
    private Integer fee; //

    @Column(name = "fee_range", nullable = false)
    @Enumerated(EnumType.STRING)
    private FeeRange feeRange;

    @Column(nullable = false)
    private LocalDateTime deadline; //

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Province province; //

    @Column(nullable = false)
    private String city; //

    @Column(nullable = false)
    private String address; //

    @Column(nullable = false)
    private int minP; //

    @Column(nullable = false)
    private int maxP; //

    @Column(name = "nums_of_participant", nullable = false)
    private Integer numsOfParticipant;

    @Column(name = "nums_of_heart", nullable = false)
    private Integer numsOfHeart;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status; //

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate; //

    @Column(nullable = false)
    private String content; //

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Heart> hearts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Reserve> reserves = new ArrayList<>();

    public void update(String title, String imgURL, LocalDateTime startDateTime, LocalDateTime endDateTime,
                       Integer fee, FeeRange feeRange, LocalDateTime deadline, Province province,
                       String city, String address, int minP, int maxP, Status status, String content) {

        this.title = title;
        this.imgURL = imgURL;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.fee = fee;
        this.feeRange = feeRange;
        this.deadline = deadline;
        this.province = province;
        this.city = city;
        this.address = address;
        this.minP = minP;
        this.maxP = maxP;
        this.status = status;
        this.content = content;
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
