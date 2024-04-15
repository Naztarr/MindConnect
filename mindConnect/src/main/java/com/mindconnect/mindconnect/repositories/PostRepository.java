package com.mindconnect.mindconnect.repositories;

import com.mindconnect.mindconnect.Models.Post;
import com.mindconnect.mindconnect.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    Page<Post> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);
    Page<Post> findByGroupIdOrderByCreatedAtDesc(UUID groupId, Pageable pageable);
    Page<Post> findAllByHiddenFalseAndGroupIsNullOrderByCreatedAtDesc(Pageable pageable);
    List<Post> findByContentContainingIgnoreCase(String content);

}
