package com.culture.BAEUNDAY.domain.post.DTO;

import com.culture.BAEUNDAY.domain.post.Province;
import com.culture.BAEUNDAY.domain.post.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class PostRequest {

    public record PostRequestDto(

            @NotBlank(message = "제목은 필수 입력입니다")
            String title,
            @NotNull
            LocalDateTime startDateTime,
            @NotNull
            LocalDateTime endDateTime,
            @NotNull
            LocalDateTime deadline,
            @NotNull
            Integer fee,
            @NotNull
            Province province,
            @NotBlank
            String city,
            @NotBlank
            String address,
            @NotNull
            Integer minP,
            @NotNull
            Integer maxP,
            @NotBlank
            String content,
            @NotNull
            Status status,
            @NotNull
            LocalDateTime createdDate
    ){}
}
