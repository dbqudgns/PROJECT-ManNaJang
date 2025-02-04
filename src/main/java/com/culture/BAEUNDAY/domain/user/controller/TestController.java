package com.culture.BAEUNDAY.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//test : USER 권한만 들어올 수 있는지 테스트옹 (무시해도됨)
@RestController
public class TestController {

    @GetMapping("/test")
    @Operation(summary = "테스트용")
    public String adminP() {
        return "test Controller";
    }
}
