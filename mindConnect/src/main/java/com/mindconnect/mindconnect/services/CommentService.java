package com.mindconnect.mindconnect.services;


import com.mindconnect.mindconnect.dtos.CommentDTO;
import com.mindconnect.mindconnect.exceptions.MindConnectException;
import com.mindconnect.mindconnect.payloads.ApiResponse;
import com.mindconnect.mindconnect.payloads.CommentHistory;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    ResponseEntity<ApiResponse<String>> createComment(CommentDTO commentDto, String postId) throws MindConnectException;

    ApiResponse<List<CommentHistory>> getCommentsByPostId(UUID postId, int page, int size);
}
