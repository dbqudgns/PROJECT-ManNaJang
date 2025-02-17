package com.culture.BAEUNDAY.domain.heart;

import com.culture.BAEUNDAY.domain.post.DTO.PostResponse;
import com.culture.BAEUNDAY.jwt.Custom.CustomUserDetails;
import com.culture.BAEUNDAY.utils.ApiUtils;
import com.culture.BAEUNDAY.utils.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hearts")
@RequiredArgsConstructor
@Tag(name = "찜 api", description = "찜 관련 기능")
public class HeartRestController {

    private final HeartService heartService;

    @PostMapping("")
    public ResponseEntity<?> control(@AuthenticationPrincipal CustomUserDetails userPrincipal,
                                     @RequestBody HeartRequestDto requestDto){
        String message = heartService.control(userPrincipal.getUsername(), requestDto);
        return ResponseEntity.ok().body(ApiUtils.success(message));
    }

    @GetMapping("")
    public ResponseEntity<?> get(@AuthenticationPrincipal CustomUserDetails userPrincipal,
                                 @RequestParam(value = "cursor", required = false) String cursor,
                                 @RequestParam(value = "cursorId", required = false) Long cursorId){
        PageResponse<?, PostResponse.FindAllDTO> responseDTO = heartService.get(userPrincipal.getUsername(), cursor, cursorId);
        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

}
