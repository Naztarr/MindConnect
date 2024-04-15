package com.mindconnect.mindconnect.dtos;

import java.util.UUID;

public record NewsFeedDto(Boolean isUser, UUID groupId, Integer page, Integer size) {
}
