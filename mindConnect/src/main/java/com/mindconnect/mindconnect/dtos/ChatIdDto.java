package com.mindconnect.mindconnect.dtos;

public record ChatIdDto (
        String senderId,
        String recipientId,

        String otherEmail
){
}
