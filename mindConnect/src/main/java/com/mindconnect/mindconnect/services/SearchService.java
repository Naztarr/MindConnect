package com.mindconnect.mindconnect.services;

import com.mindconnect.mindconnect.dtos.GroupSearchResponseDTO;
import com.mindconnect.mindconnect.payloads.ApiResponse;
import com.mindconnect.mindconnect.dtos.PostResponseDto;
import com.mindconnect.mindconnect.dtos.UserResponseDto;

import java.util.List;


public interface SearchService {
    ApiResponse<List<UserResponseDto>> searchUsers(String query, String isPostSearch, String isGroupSearch);
    ApiResponse<List<GroupSearchResponseDTO>> searchGroups(String query, String isPostSearch, String isGroupSearch);
    ApiResponse<List<PostResponseDto>> searchPosts(String query, String isPostSearch, String isGroupSearch);
    ApiResponse<?> search(String query, String isPostSearch, String isGroupSearch);

}

