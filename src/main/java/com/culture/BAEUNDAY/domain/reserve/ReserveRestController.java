package com.culture.BAEUNDAY.domain.reserve;


import com.culture.BAEUNDAY.domain.reserve.DTO.ReserveRequestDto;
import com.culture.BAEUNDAY.domain.reserve.DTO.ReserveResponseDto;
import com.culture.BAEUNDAY.jwt.Custom.CustomUserDetails;
import com.culture.BAEUNDAY.utils.ApiUtils;
import com.culture.BAEUNDAY.utils.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reserve")
@RequiredArgsConstructor
@Tag(name = "신청 api", description = "신청 관련 기능")
public class ReserveRestController {

    private final ReserveService reserveService;

    @Operation(summary = "프로그램 신청 및 취소")
    @PostMapping
    public ResponseEntity<?> reserve (@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                      @RequestBody @Valid ReserveRequestDto requestDTO){
        String message = reserveService.reserve(customUserDetails.getUsername(), requestDTO);
        return ResponseEntity.ok(ApiUtils.success(message));

    }

    @Operation(summary = "내가 신청한 프로그램 조회, filter = CONFIRMED/PAYMENT")
    @GetMapping
    public ResponseEntity<?> get(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                 @RequestParam(value = "filter", required = false) String filter,
                                 @RequestParam(value = "cursor", required = false) String cursor,
                                 @RequestParam(value = "cursorId",required = false) Long cursorId
                                 ){
        PageResponse<?, List<ReserveResponseDto>> response = reserveService.get(customUserDetails.getUsername(), filter, cursor, cursorId);
        return ResponseEntity.ok(response);
    }


}
