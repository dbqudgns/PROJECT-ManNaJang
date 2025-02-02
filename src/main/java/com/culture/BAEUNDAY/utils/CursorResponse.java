package com.culture.BAEUNDAY.utils;

public record CursorResponse<T>(
        boolean hasNext,
        int size,
        T nextCursor,
        Long nextId
)
{ }
