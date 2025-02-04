package com.culture.BAEUNDAY.domain.post.DTO;

import com.culture.BAEUNDAY.domain.post.Province;
import com.culture.BAEUNDAY.domain.post.Status;
import jakarta.validation.constraints.NotBlank;

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
            @NotBlank
            Integer fee,

            @NotBlank
            LocalDateTime startDate,
            @NotBlank
            LocalDateTime endDate,
            @NotBlank
            Province province,
            @NotBlank
            String city,
            @NotBlank
            String address,
            @NotBlank
            Integer minP,
            @NotBlank
            Integer maxP,
            @NotBlank
            String content,
            @NotBlank
            Status status,
            @NotBlank
            LocalDateTime createdDate,
            @NotBlank
            LocalDateTime deadline,
            Long numsOfHeart

    ){}


}
