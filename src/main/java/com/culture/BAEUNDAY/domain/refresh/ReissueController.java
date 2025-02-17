package com.culture.BAEUNDAY.domain.refresh;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

//Refresh 토큰을 통해 Access 토큰을 재발급 및 Refresh Rotate(화이트리스트) 컨트롤러
@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final ReissueService reissueService;

    @PostMapping("/api/reissue")
    @Operation(summary = "Access 토큰 재발급 및 Refresh 토큰 Rotate")
    public ResponseEntity<Map<String, Object>> reissue(HttpServletRequest request, HttpServletResponse response) {

        return reissueService.reissue(request, response);

    }

}
