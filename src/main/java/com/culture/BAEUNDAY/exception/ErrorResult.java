package com.culture.BAEUNDAY.exception;

import lombok.Builder;

@Builder
public record ErrorResult(
        Integer status,
        String message,
        String errorCode
) {
}
