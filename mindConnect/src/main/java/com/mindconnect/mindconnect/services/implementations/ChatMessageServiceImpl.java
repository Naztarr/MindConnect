package com.mindconnect.mindconnect.services.implementations;

import com.mindconnect.mindconnect.Models.ChatMessage;
import com.mindconnect.mindconnect.Models.User;
import com.mindconnect.mindconnect.dtos.ChatDto;
import com.mindconnect.mindconnect.dtos.ChatResponse;
import com.mindconnect.mindconnect.dtos.RecentChatDto;
import com.mindconnect.mindconnect.dtos.RecentChatResponse;
import com.mindconnect.mindconnect.exceptions.MindConnectException;
import com.mindconnect.mindconnect.mapper.ChatMapper;
import com.mindconnect.mindconnect.payloads.ApiResponse;
import com.mindconnect.mindconnect.payloads.ChatHistory;
import com.mindconnect.mindconnect.repositories.ChatMessageRepository;
import com.mindconnect.mindconnect.repositories.UserRepository;
import com.mindconnect.mindconnect.services.ChatMessageService;
import com.mindconnect.mindconnect.utils.TimeFormatter;
import com.mindconnect.mindconnect.utils.UserUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @Override
    @Transactional
    public ApiResponse<ChatHistory> sendMessage(ChatDto chatDto) {
        var sender = userRepository.findByEmailAddress(chatDto.senderEmail()).orElseThrow(() -> new MindConnectException("User not found"));
        var recipient = userRepository.findByEmailAddress(chatDto.recipientEmail()).orElseThrow(() -> new MindConnectException("User not found"));

        ChatMessage message = new ChatMessage();
        message.setMessage(chatDto.message());
        message.setReceiver(recipient);
        message.setSender(sender);
        chatMessageRepository.save(message);

        ChatHistory response = new ChatHistory(
                message.getId(),
                message.getMessage(),
                message.getCreatedAt(),
                message.getSender().getFirstName(),
                message.getSender().getLastName(),
                message.getSender().getEmailAddress()
               );


        simpMessagingTemplate.convertAndSendToUser(
                chatDto.recipientEmail(),
                "/private/chats",
                response
        );

        simpMessagingTemplate.convertAndSendToUser(
                chatDto.senderEmail(),
                "/private/chats",
                response);

        System.out.println("message received in here!");

        return new ApiResponse<>(
                response,
                "success"
        );
    }


    /**
     * @param otherUserEmail
     * @param page
     * @param pageSize
     * @return ApiResponse containing the paginated list of PostHistory objects
     */
    @Override
    public ApiResponse<List<ChatHistory>> readChat(String otherUserEmail, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<ChatMessage> chats = chatMessageRepository
                .findBySenderEmailAddressAndReceiverEmailAddressOrReceiverEmailAddressAndSenderEmailAddressOrderByCreatedAtDesc(
                        UserUtil.getLoggedInUser(), otherUserEmail, UserUtil.getLoggedInUser(), otherUserEmail, pageable);

        List<ChatHistory> chatList = chats.stream()
                .map(chat -> ChatMapper.mapToChatHistory(chat, new ChatHistory()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Collections.reverse(list);
                    return list;
                }));

        return new ApiResponse<>( chatList,
                        "chats fetched successfully");
    }


    @Override
    public ApiResponse<List<RecentChatResponse>> findMostRecentMessagesWithAllUsers(int page, int size) {
        var loggedInUserEmail = UserUtil.getLoggedInUser();
        var currentUser = userRepository.findByEmailAddress(loggedInUserEmail).orElseThrow(() -> new MindConnectException("User not found"));
        List<RecentChatDto> result = chatMessageRepository.findMostRecentMessagesWithAllUsers(currentUser.getId(), page, size);
        List<RecentChatResponse> response = result.stream().map(message -> new RecentChatResponse(
                    message.getId(),
                    message.getMessage(),
                    message.getCreatedAt().format(TimeFormatter.formatter()),
                    loggedInUserEmail,
                    getUser(message.getSenderId()).getEmailAddress(),
                    getUser(message.getReceiverId()).getEmailAddress(),
                    Objects.equals(loggedInUserEmail, getUser(message.getSenderId()).getEmailAddress()) ? getUser(message.getReceiverId()).getFirstName() : getUser(message.getSenderId()).getFirstName(),
                    Objects.equals(loggedInUserEmail, getUser(message.getSenderId()).getEmailAddress()) ? getUser(message.getReceiverId()).getLastName() : getUser(message.getSenderId()).getLastName(),
                    Objects.equals(loggedInUserEmail, getUser(message.getSenderId()).getEmailAddress()) ? getUser(message.getReceiverId()).getProfilePictureUrl() : getUser(message.getSenderId()).getProfilePictureUrl()
            )).collect(Collectors.toList());
        return new ApiResponse<>(response, "success");
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId);
    }
}
