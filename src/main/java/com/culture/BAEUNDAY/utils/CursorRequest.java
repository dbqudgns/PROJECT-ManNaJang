package com.culture.BAEUNDAY.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public class CursorRequest<T> {
    public Pageable request;
    public T cursor; // 정렬 기준 ( sorting )
    public Long cursorID; // 중복 방지

    public CursorRequest(int size, String cursor, Class<T> clazz, Long cursorId) {
        this.request = PageRequest.ofSize(size);
        this.cursor = convertNullCursorToMaxValue(cursor,clazz);
        this.cursorID = CursorUtils.convertNullCursorToMaxValue(cursorId);
    }

    private static <T> T convertNullCursorToMaxValue(String cursor, Class<T> clazz) {
        if (clazz == Double.class) {
            return (T) CursorUtils.convertNullCursorToMinDoubleValue((cursor));
        } else if (clazz == Integer.class) {
            return (T) CursorUtils.convertNullCursorToMaxIntegerValue(((cursor)) );
        } else if (clazz == Long.class) {
            return (T) CursorUtils.convertNullCursorToMaxLongValue(((cursor)) );
        } else if (clazz == LocalDateTime.class) {
            return (T) CursorUtils.convertNullCursorToMaxDateValue(((cursor)));
        }
        return null;
    }
}
