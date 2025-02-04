package com.culture.BAEUNDAY.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//test : USER 권한만 들어올 수 있는지 테스트옹 (무시해도됨)
@RestController
public class TestController {

    @GetMapping("/test")
    public String adminP() {
        return "test Controller";
    }
}
