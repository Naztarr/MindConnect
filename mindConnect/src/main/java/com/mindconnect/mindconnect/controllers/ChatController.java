package com.mindconnect.mindconnect.controllers;

import com.mindconnect.mindconnect.dtos.ChatDto;
import com.mindconnect.mindconnect.dtos.ChatResponse;
import com.mindconnect.mindconnect.dtos.RecentChatResponse;
import com.mindconnect.mindconnect.payloads.ApiResponse;
import com.mindconnect.mindconnect.payloads.ChatHistory;
import com.mindconnect.mindconnect.payloads.ChatHistoryResponse;
import com.mindconnect.mindconnect.services.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public ApiResponse<ChatHistory> processMessage(
            @Payload ChatDto chatDto
    ) {
        log.info("Message received: {}", chatDto.message());
        return chatMessageService.sendMessage(chatDto);
    }

    @GetMapping("/chat-history")
    public ResponseEntity<ApiResponse<List<ChatHistory>>> fetchChat(@RequestParam String otherUserEmail,
                                                                   @RequestParam Integer page,
                                                                   @RequestParam Integer pageSize){
        return ResponseEntity.ok().body(chatMessageService.readChat(otherUserEmail, page, pageSize));
    }

    @GetMapping("/chat/recent")
    public ApiResponse<List<RecentChatResponse>> getRecentChats(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return chatMessageService.findMostRecentMessagesWithAllUsers(page, size);
    }

}
