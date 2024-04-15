package com.mindconnect.mindconnect.mapper;

import com.mindconnect.mindconnect.Models.Comment;
import com.mindconnect.mindconnect.Models.Post;
import com.mindconnect.mindconnect.payloads.PostHistory;

import java.util.List;

public class PostMapper {
    public static PostHistory mapToPostHistory(Post post, PostHistory postHistory, Long likeCount, Long commentCount, String timeCreated){
        postHistory.setPostId(post.getId());
        postHistory.setContent(post.getContent());
        postHistory.setLikeCount(likeCount);
        postHistory.setCommentCount(commentCount);
        postHistory.setPosterFirstName(post.getUser().getFirstName());
        postHistory.setPosterLastName(post.getUser().getLastName());
        postHistory.setProfilePictureUrl(post.getUser().getProfilePictureUrl());
        postHistory.setTimeCreated(timeCreated);
        return postHistory;
    }
}
