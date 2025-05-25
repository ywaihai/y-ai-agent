package com.waihai.chatmemory;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

import java.util.List;

public class dataSourceChatMemory implements ChatMemory {


    public dataSourceChatMemory() {
    }

    public void add(String conversationId, List<Message> messages) {

    }

    public List<Message> get(String conversationId, int lastN) {

        return null;
    }

    public void clear(String conversationId) {

    }
}