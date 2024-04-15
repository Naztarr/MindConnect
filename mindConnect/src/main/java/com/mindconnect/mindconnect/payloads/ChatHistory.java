package com.mindconnect.mindconnect.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatHistory {
    private UUID messageId;
    private String content;
    private LocalDateTime createdAt;
    private String SenderFirstName;
    private String senderLastName;
    private String senderEmailAddress;
}
