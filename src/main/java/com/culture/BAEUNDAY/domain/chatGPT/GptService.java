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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
@Tag(name = "GPT api", description = "기획안 생성 기능")
public class GptService {
//https://platform.openai.com/playground/chat?models=gpt-4o
    private final GptEx gptEx;
    private final String openAiKey = "";
    private final OpenAIClient client = OpenAIOkHttpClient.builder()
            .apiKey(openAiKey)
                .build();

    public PageResponse<?, GptResponse.GptResponseDto> run(GptRequest.GptRequestDto requestDto) {

        StringBuilder userM = makeUserMessage(requestDto);
        System.out.println(userM.toString());
        String content;
        try {
            ChatCompletionAssistantMessageParam chatCompletionMessage = ChatCompletionAssistantMessageParam.builder()
                    .content("참고할 양식을 두 개 줄게.\n" + gptEx.getEx1() + gptEx.getEx2())
                    .build();
            ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                    .model(ChatModel.GPT_4O_2024_11_20)
                    .temperature(0.8)
                    .maxCompletionTokens(3000)
                    .addSystemMessage( gptEx.getSystemMsg())
                    .addMessage(chatCompletionMessage)
                    .addUserMessage(userM.toString())
                    .build();
            content = client.chat().completions().create(params).choices().stream()
                    .map(choice -> choice.message().content().orElse(""))
                    .collect(Collectors.joining("\n"));
        }catch (OpenAIException e){
            throw new OpenAIException(e.getMessage());
        }
        System.out.println(content);
        GptResponse.GptResponseDto responseDto = new GptResponse.GptResponseDto(content);

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
        programInfo.put("내가 진행할 프로그램에 대한 기본적인 정보를 줄테니까 강의기획서를 작성해줘"," ");
        programInfo.put("특히 커리큘럼은 " + requestDto.numberOfProgram()+ "회차로 나눠서 각각의 세부 내용들을 적어줘."," ");
        programInfo.put("주제 : ", requestDto.subject());
        programInfo.put("목표 ", requestDto.goal());
        programInfo.put("개요", requestDto.syllabus());
        programInfo.put("강의 개요", requestDto.syllabus());
        programInfo.put("대상 연령대", requestDto.targetStudents().toString());
        programInfo.put("난이도", requestDto.level());
        programInfo.put("강의 형태", requestDto.method());
        programInfo.put("강의 비용", requestDto.fee().toString());
        programInfo.put("시작일", requestDto.startDate().toString());
        programInfo.put("강의 횟수", requestDto.numberOfProgram().toString());
        programInfo.put("지역", requestDto.province().toString() + requestDto.city()) ; //TODO : address 추가
        programInfo.put("최소 인원", requestDto.minP().toString());
        programInfo.put("최대 인원", requestDto.maxP().toString());
        programInfo.put("유의사항 또는 공지사항", requestDto.etc());

        StringBuilder userM = new StringBuilder();
        programInfo.forEach((key, value) -> {
            userM.append(key).append(" : ").append(value).append("\n");
        });
        return userM;
    }
}
