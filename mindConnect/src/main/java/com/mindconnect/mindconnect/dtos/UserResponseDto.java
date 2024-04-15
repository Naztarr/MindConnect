package com.mindconnect.mindconnect.dtos;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String firstName,
        String lastName,
        String username,
        String gender,
        String country,
        String state,
        String mentalCondition
) {}

