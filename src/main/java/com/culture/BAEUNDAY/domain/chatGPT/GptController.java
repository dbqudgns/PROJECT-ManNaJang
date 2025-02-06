package com.culture.BAEUNDAY.domain.chatGPT;


import com.culture.BAEUNDAY.utils.ApiUtils;
import com.culture.BAEUNDAY.utils.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/gpt")
@RequiredArgsConstructor
public class GptController {

    private final GptService gptService;

    @PostMapping()
    public ResponseEntity<?> test1(@RequestBody @Valid GptRequest.GptRequestDto requestDto) {
        PageResponse<?, GptResponse.GptResponseDto> response = gptService.run(requestDto);
        return ResponseEntity.ok(ApiUtils.success(response));
    }
    @GetMapping()
    public ResponseEntity<?> test() throws IOException {
        gptService.run1();
        gptService.run2();
        gptService.run3();
        return ResponseEntity.ok(ApiUtils.success("gpt-test"));
    }
}
