package com.culture.BAEUNDAY.domain.chatGPT;


import com.culture.BAEUNDAY.domain.chatGPT.DTO.GptRequest;
import com.culture.BAEUNDAY.domain.chatGPT.DTO.GptResponse;
import com.culture.BAEUNDAY.utils.ApiUtils;
import com.culture.BAEUNDAY.utils.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/gpt")
@RequiredArgsConstructor
public class GptController {

    private final GptService gptService;

    @PostMapping()
    public ResponseEntity<?> get(@RequestBody @Valid GptRequest.GptRequestDto requestDto) {
        PageResponse<?, GptResponse.GptResponseDto> response = gptService.run(requestDto);
        return ResponseEntity.ok(ApiUtils.success(response));
    }
    @PostMapping("/test")
    public ResponseEntity<?> test(@RequestBody @Valid GptRequest.GptRequestDto requestDto) throws IOException {
        gptService.zero_few(requestDto);

        return ResponseEntity.ok(ApiUtils.success("gpt-test"));
    }
}
