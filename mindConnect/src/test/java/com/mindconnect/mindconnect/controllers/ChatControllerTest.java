package com.mindconnect.mindconnect.controllers;

import com.mindconnect.mindconnect.payloads.ApiResponse;
import com.mindconnect.mindconnect.payloads.ChatHistory;
import com.mindconnect.mindconnect.payloads.ChatHistoryResponse;
import com.mindconnect.mindconnect.services.ChatMessageService;
import com.mindconnect.mindconnect.services.implementations.JwtImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ChatController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class ChatControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ChatMessageService chatMessageService;

    @MockBean
    JwtImplementation jwtImplementation;

    @MockBean
    UserDetailsService userDetailsService;



    @Test
    void fetchChat() throws Exception {
//        List<ChatHistory> chatHistoryList = new ArrayList<>();
//        chatHistoryList.add(new ChatHistory());
//        chatHistoryList.add(new ChatHistory());
//        chatHistoryList.add(new ChatHistory());
//
//        ApiResponse<ChatHistoryResponse> returnResponse = new ApiResponse(
//                new ChatHistoryResponse("Chinaza", "Herbert",
//                        "profilePicture.com", chatHistoryList),
//                "Chats fetched successfully");
//
//        when(chatMessageService.readChat(anyString(), anyInt(), anyInt())).thenReturn(returnResponse);
//
//        //Act and Assert
//        mockMvc.perform(get("/user/chat-history")
//                        .param("otherUserEmail", "Naz@gmail.com")
//                        .param("page", "0")
//                        .param("pageSize", "10")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.message").value("Chats fetched successfully"));
//
//        verify(chatMessageService).readChat(eq("Naz@gmail.com"), eq(0), eq(10));
    }
}