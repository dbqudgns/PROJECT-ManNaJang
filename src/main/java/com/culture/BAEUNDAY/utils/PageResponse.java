package com.culture.BAEUNDAY.utils;


import java.util.List;

public record PageResponse<T,U> (
        CursorResponse<T> cursor,
        List<U> body
)
{}
