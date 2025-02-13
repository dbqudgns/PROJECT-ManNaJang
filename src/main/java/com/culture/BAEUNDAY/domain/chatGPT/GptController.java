package com.culture.BAEUNDAY.domain.chatGPT;


import com.culture.BAEUNDAY.domain.chatGPT.DTO.GptRequest;
import com.culture.BAEUNDAY.domain.chatGPT.DTO.GptResponse;
import com.culture.BAEUNDAY.utils.ApiUtils;
import com.culture.BAEUNDAY.utils.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gpt")
@RequiredArgsConstructor
public class GptController {

    private final GptService gptService;

    @PostMapping()
    public ResponseEntity<?> get(@RequestBody @Valid GptRequest.GptRequestDto requestDto) {
        PageResponse<?, GptResponse.GptResponseDto> response = gptService.run(requestDto);
        return ResponseEntity.ok(ApiUtils.success(response));
    }
}
