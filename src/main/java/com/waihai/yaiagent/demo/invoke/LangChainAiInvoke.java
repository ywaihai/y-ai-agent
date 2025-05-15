package com.waihai.yaiagent.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.stereotype.Component;

@Component
public class LangChainAiInvoke {

    public static void main(String[] args) {
        ChatLanguageModel qwenModel = QwenChatModel.builder()
                .apiKey("")
                .modelName("qwen-max")
                .build();
        String answer = qwenModel.chat("你好你好你好鸭");
        System.out.println(answer);
    }
}
