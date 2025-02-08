package com.culture.BAEUNDAY.domain.heart;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record HeartRequestDto (
        @NotNull
        Long postId,
        @NotNull
        LocalDateTime createdDate
){
}
