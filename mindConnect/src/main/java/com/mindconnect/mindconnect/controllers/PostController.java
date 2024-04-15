package com.mindconnect.mindconnect.controllers;

import com.mindconnect.mindconnect.dtos.PostDTO;
import com.mindconnect.mindconnect.exceptions.MindConnectException;
import com.mindconnect.mindconnect.payloads.ApiResponse;
import com.mindconnect.mindconnect.payloads.CommentHistory;
import com.mindconnect.mindconnect.services.CommentService;
import com.mindconnect.mindconnect.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createPost(@RequestBody PostDTO postDto) {
        ApiResponse<String> apiResponse = postService.createPost(postDto).getBody();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/{postId}/report")
    public ResponseEntity<ApiResponse<String>> reportPost(@PathVariable UUID postId, @RequestParam String reportReason) throws MindConnectException {
        ApiResponse<String> apiResponse = postService.reportPost(postId, reportReason).getBody();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{postId}/update")
    public ResponseEntity<ApiResponse<String>> updatePost(@PathVariable UUID postId, @RequestBody PostDTO postDTO) {
        ApiResponse<String> updatedPost = postService.updatePost(postId, postDTO).getBody();
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{postId}/delete")
    public ResponseEntity<ApiResponse<String>> deletePost(@PathVariable UUID postId) {
        ApiResponse<String> deletedPost = postService.deletePost(postId).getBody();
        return ResponseEntity.ok(deletedPost);
    }


    @GetMapping("/{postId}/comments")
    public ApiResponse<List<CommentHistory>> getCommentsOnPost(
            @PathVariable UUID postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return commentService.getCommentsByPostId(postId, page, size);
    }

    @PostMapping("/{postId}/toggleVisibility")
    public ResponseEntity<ApiResponse<String>> togglePostVisibility(@PathVariable UUID postId) throws MindConnectException {
        postService.togglePostVisibility(postId);
        return ResponseEntity.ok(new ApiResponse<>("Post visibility toggled successfully", HttpStatus.OK));
    }

}

