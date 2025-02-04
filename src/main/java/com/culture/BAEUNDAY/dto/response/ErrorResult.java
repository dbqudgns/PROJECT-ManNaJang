package com.culture.BAEUNDAY.dto.response;

import lombok.Builder;

@Builder
public record ErrorResult(
        Integer status,
        String message,
        String errorCode
) {
}
