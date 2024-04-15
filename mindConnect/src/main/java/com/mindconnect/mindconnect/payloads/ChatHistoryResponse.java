package com.mindconnect.mindconnect.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class ChatHistoryResponse {
    private String otherUserEmail;
    private String otherUserFirstname;
    private String otherUserLastname;
    private String profilePictureUrl;
    private List<ChatHistory> chats;

}
