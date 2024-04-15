package com.mindconnect.mindconnect.services.implementations;

import com.mindconnect.mindconnect.Models.ChatMessage;
import com.mindconnect.mindconnect.Models.User;
import com.mindconnect.mindconnect.payloads.ApiResponse;
import com.mindconnect.mindconnect.payloads.ChatHistory;
import com.mindconnect.mindconnect.payloads.ChatHistoryResponse;
import com.mindconnect.mindconnect.repositories.ChatMessageRepository;
import com.mindconnect.mindconnect.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ChatMessageServiceImplTest {
    @InjectMocks
    ChatMessageServiceImpl chatMessageServiceImpl;
    @Mock
    ChatMessageRepository chatMessageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    /**
     * Setup method executed before each test case.
     * Initializes the mocks and sets up a mock security context.
     */
    @BeforeEach
    void setUp() {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken("Naz@gmail.com", "naztarr$");
        securityContext.setAuthentication(authentication);
    }

    /**
     * Method to create a mock User instance.
     * @return A mock User instance.
     */
    private User createUser() {
        User user = new User();
        user.setEmailAddress("Naz@gmail.com");
        user.setPassword(passwordEncoder.encode("naztarr$"));
        return user;
    }

    @Test
    void readChat() {
        Pageable pageable = PageRequest.of(0, 10);
        List<ChatMessage> chats = new ArrayList<>();
        chats.add(new ChatMessage(null, new User("firstname", "lastname"), new User("firstname2", "lastname2")));
        chats.add(new ChatMessage(null, new User("firstname", "lastname"), new User("firstname2", "lastname2")));

        System.out.println( ":::::::"+chats);
        Page<ChatMessage> page = new PageImpl<>(chats);

        User mockUser = createUser();

        when(chatMessageRepository
                .findBySenderEmailAddressAndReceiverEmailAddressOrReceiverEmailAddressAndSenderEmailAddressOrderByCreatedAtDesc(
                        anyString(), anyString(), anyString(), anyString(), eq(pageable)
                )).thenReturn(page);
        when(userRepository.findByEmailAddress(anyString())).thenReturn(Optional.of(mockUser));

        ApiResponse<List<ChatHistory>> response = chatMessageServiceImpl
                .readChat("Don@gmail.com", 0, 10);

        assertNotNull(response);
        assertEquals("chats fetched successfully", response.getMessage());
    }
}