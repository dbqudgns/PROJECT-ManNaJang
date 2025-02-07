package com.culture.BAEUNDAY.domain.review.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;
import java.time.LocalDateTime;

public record ReviewRequestDTO(

        @NotBlank(message = "필수 입력 항목입니다.")
        String field,

        @NotNull(message = "별점을 채워주세요")
        @Range(min = 0, max = 5, message = "별점은 0개부터 5개까지 채울 수 있습니다.")
        Integer star,

        @NotNull(message = "작성한 시간을 보내주세요.")
        LocalDateTime createdDate

) {

}
