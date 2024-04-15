package com.mindconnect.mindconnect.services;

import com.mindconnect.mindconnect.Models.User;
import com.mindconnect.mindconnect.dtos.*;
import com.mindconnect.mindconnect.payloads.ApiResponse;
import com.mindconnect.mindconnect.payloads.PostHistory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserService {

    ResponseEntity<ApiResponse<String>> changePassword(ChangePasswordDto changePasswordDto);

   ApiResponse<ProfileResponse> editProfile(ProfileDto profileDto);
    public ResponseEntity<ApiResponse<ProfileResponse>> viewProfile();

    ApiResponse<List<PostHistory>> viewPosts(NewsFeedDto newsFeedDto);

    ResponseEntity<ApiResponse<String>> updateProfilePic(MultipartFile file) throws IOException;

    ApiResponse<List<UserListResponse>> getAllUsers(int page, int size);

    ApiResponse<List<UserListResponse>> searchUsers(String query);

    ResponseEntity<ApiResponse<String>> blockUser(UUID blockedUserId);
}
