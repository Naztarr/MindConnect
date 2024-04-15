package com.mindconnect.mindconnect.payloads;

import com.mindconnect.mindconnect.Models.Comment;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Data
public class PostHistory {
    private UUID postId;
    private String content;
    private Long likeCount;
    private Long commentCount;
    private String posterFirstName;
    private String posterLastName;
    private String profilePictureUrl;
    private String timeCreated;

}
