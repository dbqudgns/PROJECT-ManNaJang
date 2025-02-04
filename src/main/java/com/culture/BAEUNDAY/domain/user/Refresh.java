package com.culture.BAEUNDAY.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
public class Refresh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; //Refresh 토큰이 발급된 유저명
    private String refresh;  //Refresh 토큰
    private String expiration; //Refresh 토큰 유효 기간

    @Builder
    public Refresh(String username, String refresh, String expiration) {
        this.username = username;
        this.refresh = refresh;
        this.expiration = expiration;
    }

    public Refresh() {

    }
}
