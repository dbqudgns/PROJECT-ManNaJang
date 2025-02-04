package com.culture.BAEUNDAY.utils;


public record PageResponse<T,U> (
        CursorResponse<T> cursor,
        U body
)
{}
