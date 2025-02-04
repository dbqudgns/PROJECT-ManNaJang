package com.culture.BAEUNDAY.domain.user.DTO.response;

import lombok.Builder;

@Builder
public record ErrorResult(
        Integer status,
        String message,
        String errorCode
) {
}
