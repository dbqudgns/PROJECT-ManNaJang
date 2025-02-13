package com.culture.BAEUNDAY.domain.chatGPT;

import com.culture.BAEUNDAY.domain.chatGPT.DTO.GptRequest;
import com.culture.BAEUNDAY.domain.chatGPT.DTO.GptResponse;
import com.culture.BAEUNDAY.utils.PageResponse;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.errors.OpenAIException;
import com.openai.models.ChatCompletionAssistantMessageParam;
import com.openai.models.ChatCompletionCreateParams;
import com.openai.models.ChatModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Slf4j
@Tag(name = "GPT api", description = "기획안 생성 기능")
@Service
public class GptService {
//https://platform.openai.com/playground/chat?models=gpt-4o
    private final GptEx gptEx;

    private final String openAiKey;
    private OpenAIClient client;

    @PostConstruct
    public void init() {
        this.client = OpenAIOkHttpClient.builder()
                .apiKey(openAiKey)
                .build();
    }

    public PageResponse<?, GptResponse.GptResponseDto> run(GptRequest.GptRequestDto requestDto) {

        StringBuilder userM = makeUserMessage(requestDto);
        System.out.println(userM.toString());
        String content;
        long beforeTime = System.currentTimeMillis();
        try {

            ChatCompletionAssistantMessageParam chatCompletionMessage1 = ChatCompletionAssistantMessageParam.builder()
                    .content(gptEx.assisMsg1)
                    .build();
            ChatCompletionAssistantMessageParam chatCompletionMessage2 = ChatCompletionAssistantMessageParam.builder()
                    .content(gptEx.assisMsg2)
                    .build();
            ChatCompletionAssistantMessageParam chatCompletionMessage3 = ChatCompletionAssistantMessageParam.builder()
                    .content(gptEx.assisMsg3)
                    .build();

            ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                    .model(ChatModel.GPT_4O_2024_11_20)
                    .temperature(0.5)
                    .maxCompletionTokens(3000)
                    .addSystemMessage( gptEx.getMainMsg())
                    .addUserMessage(gptEx.userMsg1)
                    .addMessage(chatCompletionMessage1)
                    .addSystemMessage(gptEx.getMainMsg())
                    .addUserMessage(gptEx.userMsg2)
                    .addMessage(chatCompletionMessage2)
                    .addSystemMessage(gptEx.getMainMsg())
                    .addUserMessage(gptEx.userMsg3)
                    .addMessage(chatCompletionMessage3)
                    .addUserMessage(userM.toString())
                    .build();

            content = client.chat().completions().create(params)
                    .choices().stream()
                    .map(choice -> choice.message().content().orElse(""))
                    .collect(Collectors.joining("\n"));

        }catch (OpenAIException e){
            throw new OpenAIException(e.getMessage());
        }
        System.out.println(content);
        GptResponse.GptResponseDto responseDto = new GptResponse.GptResponseDto(content);
        long afterTime = System.currentTimeMillis();
        long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
        System.out.println("시간차이(m) : "+secDiffTime);
        return new PageResponse<>( null, responseDto);
    }

    public void zero_few(GptRequest.GptRequestDto requestDto) {

        StringBuilder userM = makeUserMessage(requestDto);
        System.out.println(userM.toString());
        String content;
        try {
            //zero-shot
            System.out.println("**************zero-shot****************");
            ChatCompletionCreateParams params1 = ChatCompletionCreateParams.builder()
                    .model(ChatModel.GPT_4O_2024_11_20)
                    .temperature(0.8)
                    .maxCompletionTokens(3000)
                    .addUserMessage(userM.toString())
                    .build();
            content = client.chat().completions().create(params1).choices().stream()
                    .map(choice -> choice.message().content().orElse(""))
                    .collect(Collectors.joining("\n"));
            System.out.println("1\n"+content);

            //few-shot
            System.out.println("**************few-shot****************");
            ChatCompletionCreateParams params2 = ChatCompletionCreateParams.builder()
                    .model(ChatModel.GPT_4O_2024_11_20)
                    .temperature(0.8)
                    .maxCompletionTokens(3000)
                    .addSystemMessage("기획안 예시를 2개 줄게. \n" + gptEx.getEx1() + gptEx.getEx2())
                    .addUserMessage(userM.toString())
                    .build();
            content = client.chat().completions().create(params2).choices().stream()
                    .map(choice -> choice.message().content().orElse(""))
                    .collect(Collectors.joining("\n"));
            System.out.println("2\n"+content);
        }catch (OpenAIException e){
            throw new OpenAIException(e.getMessage());
        }
    }


    private StringBuilder makeUserMessage(GptRequest.GptRequestDto requestDto) {
        HashMap<String,String> programInfo = new LinkedHashMap<>();

        programInfo.put("지금의 나를 만든 가장 의미 있는 순간은 무엇인가요? ", requestDto.question1());
        programInfo.put("내가 남들과 다른 시각을 가지게 된 계기는? ", requestDto.question2());
        programInfo.put("이걸 몰라서 과거의 내가 고생했던 순간이 있나요?", requestDto.question3());
        programInfo.put("나는 왜 이 주제에 대해 열정을 느끼나요?", requestDto.question4());
        programInfo.put("내 강의를 들은 사람들이 반드시 깨달았으면 하는 것은?", requestDto.question5());
        programInfo.put("이 강의를 듣지 않는다면, 수강생들은 어떤 기회를 놓치게 될까요?", requestDto.question6());
        programInfo.put("내 이야기를 들은 사람들이 ‘와, 이거다!’라고 느끼게 하고 싶은 부분은?", requestDto.question7());

        StringBuilder userM = new StringBuilder();
        programInfo.forEach((key, value) -> {
            userM.append(key).append(" : ").append(value).append("\n");
        });
        return userM;
    }
}
