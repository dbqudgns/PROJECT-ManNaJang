package com.culture.BAEUNDAY.utils;

import java.time.LocalDateTime;

public class CursorUtils {
    public static Double convertNullCursorToMinDoubleValue(String cursor) {
        return cursor != null ? Double.parseDouble(cursor) : Double.MIN_VALUE;
    }
    public static Integer convertNullCursorToMaxIntegerValue(String cursor) {
        return cursor != null ? Integer.parseInt(cursor) : Integer.MAX_VALUE;
    }
    public static Long convertNullCursorToMaxLongValue(String cursor) {
        return cursor != null ? Long.parseLong(cursor) : Long.MAX_VALUE;
    }
    public static LocalDateTime convertNullCursorToMaxDateValue(String cursor) {
        return cursor != null ? LocalDateTime.parse(cursor) : LocalDateTime.now().plusYears(1000);
    }
    public static Long convertNullCursorToMaxValue(Long cursor) {
        return cursor != null ? cursor : Long.MAX_VALUE;
    }
}
