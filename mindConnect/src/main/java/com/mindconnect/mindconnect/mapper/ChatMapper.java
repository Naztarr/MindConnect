package com.mindconnect.mindconnect.mapper;

import com.mindconnect.mindconnect.Models.ChatMessage;
import com.mindconnect.mindconnect.payloads.ChatHistory;

public class ChatMapper {

    public static ChatHistory mapToChatHistory(ChatMessage chatMessage, ChatHistory chatHistory){
        chatHistory.setMessageId(chatMessage.getId());
        chatHistory.setContent(chatMessage.getMessage());
        chatHistory.setCreatedAt(chatMessage.getCreatedAt());
        chatHistory.setSenderFirstName(chatMessage.getSender().getFirstName());
        chatHistory.setSenderLastName(chatMessage.getSender().getLastName());
        chatHistory.setSenderEmailAddress(chatMessage.getSender().getEmailAddress());
        return chatHistory;
    }
}
