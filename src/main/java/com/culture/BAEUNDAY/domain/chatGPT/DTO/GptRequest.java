package com.culture.BAEUNDAY.domain.chatGPT.DTO;

import com.culture.BAEUNDAY.domain.post.Province;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class GptRequest {

    public record GptRequestDto(

            @NotBlank
            String subject,
            String goal,
            String syllabus,
            @NotNull
            List<String> targetStudents,
            @NotBlank
            String level,
            @NotBlank
            String method,
            @NotNull
            Integer fee,
            @NotNull
            LocalDate startDate,
            @NotNull
            Integer numberOfProgram,
            @NotNull
            Province province,
            @NotBlank
            String city,
            String address,
            @NotNull
            Integer minP,
            @NotNull
            Integer maxP,
            @NotBlank
            String etc

    ){

    }
}
