package com.mindconnect.mindconnect.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecentChatResponse {
    private UUID chatMessageId;
    private String message;
    private String timeCreated;
    private String userEmail;
    private String senderEmail;
    private String recipientEmail;
    private String otherUserFirstName;
    private String otherUserLastName;
    private String otherUserProfilePictureUrl;
}
