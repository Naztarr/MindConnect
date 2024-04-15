package com.mindconnect.mindconnect.services.implementations;

import com.mindconnect.mindconnect.Models.Comment;
import com.mindconnect.mindconnect.Models.Post;
import com.mindconnect.mindconnect.dtos.CommentDTO;
import com.mindconnect.mindconnect.exceptions.CommentCreationFailedException;
import com.mindconnect.mindconnect.exceptions.MindConnectException;
import com.mindconnect.mindconnect.mapper.CommentMapper;
import com.mindconnect.mindconnect.payloads.ApiResponse;
import com.mindconnect.mindconnect.payloads.CommentHistory;
import com.mindconnect.mindconnect.repositories.CommentRepository;
import com.mindconnect.mindconnect.repositories.LikeRepository;
import com.mindconnect.mindconnect.repositories.PostRepository;
import com.mindconnect.mindconnect.repositories.UserRepository;
import com.mindconnect.mindconnect.services.CommentService;
import com.mindconnect.mindconnect.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    @Override
    public ResponseEntity<ApiResponse<String>> createComment(CommentDTO commentDto, String postId) throws MindConnectException {
        String email = UserUtil.getLoggedInUser();
        var user = userRepository.findByEmailAddress(email).orElseThrow(
                () -> new MindConnectException("User not found")
        );
        Post post = postRepository.findById(UUID.fromString(postId)).orElseThrow(
                () -> new MindConnectException("Post not found")
        );

        Comment comment = new Comment();
        comment.setContent(commentDto.content());
        comment.setUser(user);
        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);

        if (savedComment == null) {
            throw new CommentCreationFailedException("Failed to create comment");
        }

        return ResponseEntity.ok(new ApiResponse<>("Comment created successfully", HttpStatus.OK));
    }

    @Override
    public ApiResponse<List<CommentHistory>> getCommentsByPostId(UUID postId, int page, int size) {
        org.springframework.data.domain.Pageable pageable = PageRequest.of(page, size);
               var postComments =  commentRepository.findCommentsByPostId(postId, pageable);
               return new ApiResponse<>(postComments
                       .stream().map(comment -> CommentMapper.mapToCommentHistory(
                               comment,
                               likeRepository.getLikeCountByCommentId(comment.getId()
                               )))
                       .collect(Collectors.toList()), "Successful");
    }

}
