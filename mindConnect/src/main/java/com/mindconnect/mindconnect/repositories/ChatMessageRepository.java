package com.mindconnect.mindconnect.repositories;

import com.mindconnect.mindconnect.Models.ChatMessage;
import com.mindconnect.mindconnect.Models.User;
import com.mindconnect.mindconnect.dtos.RecentChatDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
    Page<ChatMessage> findBySenderEmailAddressAndReceiverEmailAddressOrReceiverEmailAddressAndSenderEmailAddressOrderByCreatedAtDesc(
            String senderAddress, String receiverAddress, String sender2Address, String receiver2Address, Pageable pageable);


    @Query(
            name = "find_most_recent_messages_with_all_users",
            nativeQuery = true)
    List<RecentChatDto> findMostRecentMessagesWithAllUsers(
            @Param("currentUser") UUID currentUserId,
            @Param("offset") int page,
            @Param("limit") int size
    );

}
