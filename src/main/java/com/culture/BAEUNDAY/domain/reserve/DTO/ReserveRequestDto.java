package com.culture.BAEUNDAY.domain.reserve.DTO;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReserveRequestDto(
        @NotNull
        Long postId,
        @NotNull
        LocalDateTime reservationDate
) {
}
