package com.waihai.yaiagent.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class LocalConfig {

    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;
}
