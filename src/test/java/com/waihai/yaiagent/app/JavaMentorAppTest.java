package com.waihai.yaiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JavaMentorAppTest {

    @Resource
    private JavaMentorApp javaMentorApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是刚入门的Java小白，叫waihai";
        String answer = javaMentorApp.doChat(message, chatId);
//        // 第二轮
//        message = "我想学习Web开发方向";
//        answer = javaMentorApp.doChat(message, chatId);
//        Assertions.assertNotNull(answer);
//        // 第三轮
//        message = "我叫什么？刚跟你说过，帮我回忆一下";
//        answer = javaMentorApp.doChat(message, chatId);
//        Assertions.assertNotNull(answer);
    }
}