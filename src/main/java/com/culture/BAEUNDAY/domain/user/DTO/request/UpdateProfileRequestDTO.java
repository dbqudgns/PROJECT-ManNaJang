package com.culture.BAEUNDAY.domain.user.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record UpdateProfileRequestDTO(

        @NotBlank(message = "이름은 필수 입력 항목입니다.")
        String name,

        @NotBlank(message = "한 줄 소개는 필수 입력 항목입니다.")
        String field,

        @NotNull(message = "닉네임 중복 검사는 필수입니다.")
        @Range(min = 1, max = 1, message = "닉네임 중복 검사는 필수입니다.")
        Integer successName
) {

}
