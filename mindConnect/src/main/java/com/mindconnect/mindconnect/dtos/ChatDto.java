package com.mindconnect.mindconnect.dtos;

import com.mindconnect.mindconnect.Models.User;

import java.util.UUID;

public record ChatDto(
        String recipientEmail,
        String senderEmail,
        String message
) {
}
