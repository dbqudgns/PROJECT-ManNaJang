package com.culture.BAEUNDAY.domain.chatGPT.DTO;

import jakarta.validation.constraints.NotNull;

public class GptRequest {

    public record GptRequestDto(
            @NotNull
            String question1,
            @NotNull
            String question2,
            @NotNull
            String question3,
            @NotNull
            String question4,
            @NotNull
            String question5,
            @NotNull
            String question6,
            @NotNull
            String question7
    ){
    }
}
