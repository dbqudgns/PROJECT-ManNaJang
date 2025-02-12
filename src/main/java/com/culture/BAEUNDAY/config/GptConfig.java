package com.culture.BAEUNDAY.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GptConfig {

    @Value("${openai.key}")
    private String secretKey;

    @Bean
    public  String getSecretKey() {
        return secretKey;
    }


}

