package com.mindconnect.mindconnect.services;

import com.mindconnect.mindconnect.Models.ChatMessage;
import com.mindconnect.mindconnect.dtos.ChatDto;
import com.mindconnect.mindconnect.dtos.ChatResponse;
import com.mindconnect.mindconnect.dtos.RecentChatResponse;
import com.mindconnect.mindconnect.payloads.ApiResponse;
import com.mindconnect.mindconnect.payloads.ChatHistory;
import com.mindconnect.mindconnect.payloads.ChatHistoryResponse;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface ChatMessageService {
    ApiResponse<ChatHistory> sendMessage(ChatDto chatDto);
    ApiResponse<List<ChatHistory>> readChat(String otherUserEmail, Integer page, Integer pageSize);

    ApiResponse<List<RecentChatResponse>> findMostRecentMessagesWithAllUsers(int page, int size);
}
