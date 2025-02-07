package com.culture.BAEUNDAY.domain.chatGPT;

import com.culture.BAEUNDAY.utils.PageResponse;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatCompletionCreateParams;
import com.openai.models.ChatModel;
import com.openai.models.FineTuningJob;
import com.openai.models.FineTuningJobCreateParams;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
@Tag(name = "GPT api", description = "기획안 생성 기능")
public class GptService {

    String openAiKey = "";
    private final OpenAIClient client = OpenAIOkHttpClient.builder()
            .apiKey(openAiKey)
                .build();

    private final String system =
            "사용자가 입력한 강의 정보를 바탕으로 체계적인 강의 기획안(커리큘럼)을 자동으로 생성해줘.다음과 같은 형식으로 작성해줘.\n" +
            "1. 강의명 : \n" +
            "2. 강의 목표 : 구체적인 강의 목표 ( 1~2개 ) \n" +
            "3. 강의 개요 : 구체적인 강의 개요\n" +
            "4. 강의 형태 : 구체적인 강의 진행 형태 \n" +
            "5. 강의 대상 : 구체적인 강의 대상 \n" +
            "5. 커리큘럼 : 강의 일정에 따라 적당한 주차별로 나누고, 각각의 주차별로 현실적이고 구체적인 활동을 적어줘. 이게 가장 중요한 부분이야.\n" +
            "6. 유의점 : 주제와 관련돼서 특별히 조심해야 하는 점 ( 유머스러운 것도 추가 ) " +
            "7. 홍보 자료 생성 : 강의 소개문, 강의 포스터 템플릿, SNS 홍보 게시글 초안을 자동 생성";
    private final String user =
            "강의명 : 홈베이킹 기초 클래스"+
            "강의 목표: 지역 특산물을 활용한 디저트 만들기"+
            "강의 개요: 기초 제빵 기술과 지역 특산물을 활용한 응용법을 배운다."+
            "강의 형태: 체험형"+
            "운영 방식: 오프라인"+
            "난이도: 초급"+
            "대상 연령대: 성인"+
            "강의 일정: 정기(월 4회)"+
            "강의 장소: 서울 강남구"+
            "강의료: 유료 (50,000원)"+
            "준비물: 앞치마, 필기도구" ;

    private String example = "## 꽃... 좋아하세요?\n" +
            "\n" +
            "### 1. 강의 개요\n" +
            "\n" +
            "꽃... 좋아하세요?는 꽃을 사랑하는 사람들을 위한 특별한 플라워 클래스입니다. 아름다운 꽃꽂이를 배우며 꽃을 활용한 감각적인 연출법을 익히고, 자연이 주는 힐링을 경험할 수 있습니다. 꽃집 운영 경험이 있는 강사가 실전 노하우를 직접 전수하며, 초보자도 쉽게 따라 할 수 있도록 구성된 체험형 강의입니다.\n" +
            "\n" +
            "### 2. 강의 목표\n" +
            "\n" +
            "- 꽃꽂이의 기본 원리와 스타일을 익힌다.\n" +
            "- 다양한 꽃과 식물을 조화롭게 활용하는 방법을 배운다.\n" +
            "- 꽃을 통한 감성 힐링과 인테리어 연출법을 익힌다.\n" +
            "\n" +
            "### 3. 강의 대상\n" +
            "\n" +
            "- 꽃과 플라워 디자인에 관심 있는 분\n" +
            "- 감각적인 인테리어 및 공간 연출을 하고 싶은 분\n" +
            "- 꽃을 활용한 힐링과 취미 활동을 찾는 분\n" +
            "\n" +
            "### 4. 강의 운영 방식\n" +
            "\n" +
            "- **진행 형태**: 오프라인 체험형 강의\n" +
            "- **일정**: 1일 단기 강의 (총 3시간)\n" +
            "- **장소**: 구미시 내 플라워 스튜디오\n" +
            "- **강의료**: 유료 (70,000원, 꽃 재료 포함)\n" +
            "- **준비물**: 가위, 앞치마 (강의실에서 대여 가능)\n" +
            "\n" +
            "### 5. 커리큘럼\n" +
            "\n" +
            "### **1주차: 꽃과 친해지기**\n" +
            "\n" +
            "- 꽃의 종류 및 계절별 특징\n" +
            "- 꽃 손질 및 관리법\n" +
            "- 꽃꽂이에 필요한 기본 도구 소개\n" +
            "\n" +
            "### **2주차: 감각적인 꽃꽂이 실습**\n" +
            "\n" +
            "- 기본 꽃꽂이 기법 익히기\n" +
            "- 색감과 형태를 고려한 디자인 연출\n" +
            "- 플라워 어레인지먼트 제작\n" +
            "\n" +
            "### **3주차: 나만의 꽃 작품 만들기**\n" +
            "\n" +
            "- 개성 있는 꽃다발 또는 센터피스 제작\n" +
            "- 완성 작품 피드백 및 사진 촬영\n" +
            "- 꽃 관리법 및 디스플레이 팁 공유\n" +
            "\n" +
            "### 6. 추가 안내 사항\n" +
            "\n" +
            "- 모든 꽃과 재료는 강의실에서 제공됩니다.\n" +
            "- 완성된 꽃꽂이는 가져갈 수 있습니다.\n" +
            "- 강의 신청은 선착순 마감되며, 환불 및 변경 규정은 별도 안내드립니다.\n" +
            "\n" +
            "### 7. 홍보 자료 생성\n" +
            "\n" +
            "- 꽃꽂이 기초 지식 및 실습 능력 습득\n" +
            "- 나만의 플라워 스타일 구축\n" +
            "- 꽃을 통한 힐링과 감성적인 여유 경험\n" ;

    public PageResponse<?, GptResponse.GptResponseDto> run(GptRequest.GptRequestDto requestDto) {

        HashMap<String,String> programInfo = new HashMap<>();
        programInfo.put("주제", requestDto.subject());
        String message = requestDto.province().toString() + "에서 진행할만한 " + requestDto.subject() + "주제에 대한 프로그램의 ";
        if ( requestDto.title() == null ) {
            programInfo.put("제목", message + "제목을 지어줘");
        } else {
            programInfo.put("제목", requestDto.title());
        }
        if ( requestDto.goal() == null ) {
            programInfo.put("목표", message + "목표를 설정해줘");
        } else {
            programInfo.put("목표", requestDto.goal());
        }
        if ( requestDto.syllabus() == null ) {
            programInfo.put("개요", message + "개요를 작성해줘");
        } else {
            programInfo.put("개요", requestDto.syllabus());
        }

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

        System.out.println(userM.toString());

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_4)
                .temperature(1)
                .topP(1)
                .n(1)
                .maxCompletionTokens(2048)
                .addSystemMessage(system)
                .addDeveloperMessage("예시를 줄게.\n" + example)
                .addUserMessage(userM.toString())
                .build();

        client.chat().completions().create(params).choices().stream()
                .flatMap(choice -> choice.message().content().stream())
                .forEach(System.out::println);

        String content = client.chat().completions().create(params).choices().stream()
                .map(choice -> choice.message().content().orElse(""))
                .collect(Collectors.joining("\n"));

        GptResponse.GptResponseDto responseDto = new GptResponse.GptResponseDto(content);

        return new PageResponse<>( null, responseDto);
    }


    public void run1(){
        System.out.println("----첫 번째----- user message만");
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_3_5_TURBO)
                .temperature(1)
                .topP(1)
                .n(1)
                .addUserMessage(user)
                .maxCompletionTokens(2048)
                .build();
        client.chat().completions().create(params).choices().stream().flatMap(choice -> choice.message().content().stream()).forEach(System.out::println);

    }
    public void run2(){
        System.out.println("----두 번째----- user message + system message");
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_3_5_TURBO)
                .temperature(1)
                .topP(1)
                .n(1)
                .maxCompletionTokens(2048)
                .addSystemMessage(system)
                .addUserMessage(user)
                .build();
        client.chat().completions().create(params).choices().stream().flatMap(choice -> choice.message().content().stream()).forEach(System.out::println);
    }
    public void run3(){
        System.out.println("----세 번째----- user message + system message + developer message");
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_3_5_TURBO)
                .temperature(1)
                .topP(1)
                .n(1)
                .maxCompletionTokens(2048)
                .addSystemMessage(system)
                .addDeveloperMessage("예시를 줄게. "+ example)
                .addUserMessage(user)
                .build();
        client.chat().completions().create(params).choices().stream().flatMap(choice -> choice.message().content().stream()).forEach(System.out::println);
    }

    public void tunning(){
//        FineTuningJobCreateParams params = FineTuningJobCreateParams.builder()
//                .model(String.valueOf(ChatModel.GPT_3_5_TURBO))
//                .trainingFile("train.jsonl")
//                .build();

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_3_5_TURBO)
                .temperature(1)
                .topP(1)
                .n(1)
                .maxCompletionTokens(2048)
                .addSystemMessage(system)
                .addUserMessage(user)
                .build();
        client.chat().completions().create(params).choices().stream()
                .flatMap(choice -> choice.message().content().stream())
                .forEach(System.out::println);

    }

    public void training() throws IOException {

//        UploadCreateParams createParams = UploadCreateParams.builder()
//                .bytes(file.length())
//                .filename(file.getName())
//                .mimeType("application/jsonl")
//                .purpose(FilePurpose.FINE_TUNE)
//                .build();
//
//        System.out.println(createParams.toString());
//        //Optional<FileObject> file = client.uploads().create(createParams).file();
//
//        Path filePath = Paths.get("C:\\Users\\82105\\Desktop\\BaeunDay_BE\\src\\main\\java\\com\\culture\\BAEUNDAY\\domain\\chatGPT\\train.jsonl");
//        byte[] fileBytes = Files.readAllBytes(filePath);
//
//        System.out.println("파일 크기: " + fileBytes.length + " bytes");  // 0이면 파일이 비어있음

//        FileObject file = FileObject.builder().id("1").bytes(123456).filename("train.jsonl").purpose(FileObject.Purpose.FINE_TUNE).build();

        FineTuningJob fineTuningJob = client.fineTuning().jobs().create(
                FineTuningJobCreateParams.builder().model("gpt-4o-mini")
                        .trainingFile("train.json")
                        .build()
        );
        System.out.println("Fine tuning started ..." + fineTuningJob.id());


        }


}
