package com.mindconnect.mindconnect.repositories;

import com.mindconnect.mindconnect.Models.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    @Query("SELECT COUNT(c) FROM comments c WHERE c.post.id = :postId")
    Long getCommentCountByPostId(UUID postId);

    List<Comment> findCommentsByPostId(UUID postId, Pageable pageable);
}
