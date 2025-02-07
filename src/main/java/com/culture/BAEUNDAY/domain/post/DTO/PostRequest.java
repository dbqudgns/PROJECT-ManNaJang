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
            String imgURL,
            @NotBlank
            String subject,
            @NotBlank

            String goal,
            @NotBlank
            String outline,
            @NotBlank
            String targetStudent,
            @NotBlank
            String level,
            @NotBlank
            String contactMethod,
            @NotNull
            Integer fee,

            @NotNull
            LocalDateTime startDate,
            @NotNull
            LocalDateTime endDate,
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
            LocalDateTime createdDate,
            @NotNull
            LocalDateTime deadline

    ){}


}
