package com.mindconnect.mindconnect.controllers;

import com.mindconnect.mindconnect.Models.User;
import com.mindconnect.mindconnect.dtos.*;
import com.mindconnect.mindconnect.payloads.ApiResponse;
import com.mindconnect.mindconnect.payloads.PostHistory;
import com.mindconnect.mindconnect.services.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;


    @PatchMapping("/password/change")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @RequestBody ChangePasswordDto changePasswordDto
    ) {
        return userService.changePassword(changePasswordDto);
    }


    @PatchMapping("/edit_profile")
    public ApiResponse<ProfileResponse> editProfile(
            @RequestBody ProfileDto profileDto){
        return userService.editProfile(profileDto);
    }

    @PostMapping("/news/feed")
    public ResponseEntity<ApiResponse<List<PostHistory>>> getPosts(@RequestBody NewsFeedDto newsFeedDto){
        return ResponseEntity.ok().body(userService.viewPosts(newsFeedDto));
    }
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> viewProfile() {
        return userService.viewProfile();
    }

    @PostMapping("/profile/upload")
    public ResponseEntity<ApiResponse<String>> handleFileUpload(
            @RequestParam("file")MultipartFile multipartFile
    ) throws IOException {
        return  userService.updateProfilePic(multipartFile);

    }

    @GetMapping(value = "/users")
    public ApiResponse<List<UserListResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
       return userService.getAllUsers(page, size);
    }

    @GetMapping(value="/search")
    public ApiResponse<List<UserListResponse>> searchUser(
         @NotNull @RequestParam("query") String query) {
        return userService.searchUsers(query);
    }
    @PostMapping("/users/block")
    public ResponseEntity<ApiResponse<String>> blockUser(@RequestParam UUID userIdToBlock) {
        return userService.blockUser(userIdToBlock);
    }

}
