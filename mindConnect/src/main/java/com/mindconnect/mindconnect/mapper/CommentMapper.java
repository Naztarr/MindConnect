package com.mindconnect.mindconnect.mapper;

import com.mindconnect.mindconnect.Models.Comment;
import com.mindconnect.mindconnect.payloads.CommentHistory;
import com.mindconnect.mindconnect.utils.TimeFormatter;

public class CommentMapper {
    public static CommentHistory mapToCommentHistory(Comment comment, Long commentLikeCount ){
        return new CommentHistory(
                comment.getId(),
                comment.getContent(),
                commentLikeCount,
                comment.getUser().getFirstName(),
                comment.getUser().getLastName(),
                comment.getUser().getProfilePictureUrl(),
                TimeFormatter.formatRelativeTime(comment.getCreatedAt())
        );
    }
}
