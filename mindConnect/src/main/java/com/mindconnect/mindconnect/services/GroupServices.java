package com.mindconnect.mindconnect.services;

import com.mindconnect.mindconnect.Models.ChatMessage;
import com.mindconnect.mindconnect.dtos.*;
import com.mindconnect.mindconnect.payloads.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GroupServices {
    ResponseEntity<ApiResponse<GroupResponseDto>> createGroup(GroupRequestDto requestDto);
    ResponseEntity<List<GroupListDto>> getGroupsByPopularity(Integer page, Integer size);
    ResponseEntity<ApiResponse<String>> exitGroup(ExitGroupDto exitGroupDto);
    ResponseEntity<ApiResponse<String>> joinGroup(JoinGroupDto joinGroupDto);
    }
