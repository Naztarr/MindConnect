package com.mindconnect.mindconnect.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {
    private UUID messageId;
    private String content;
    private LocalDateTime createdAt;
    private String SenderFirstName;
    private String senderLastName;
    private String senderEmailAddress;
    private boolean sentByCurrentUser;
}
