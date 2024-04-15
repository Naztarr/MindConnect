package com.mindconnect.mindconnect.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindconnect.mindconnect.dtos.ChangePasswordDto;
import com.mindconnect.mindconnect.dtos.NewsFeedDto;
import com.mindconnect.mindconnect.payloads.ApiResponse;
import com.mindconnect.mindconnect.payloads.PostHistory;
import com.mindconnect.mindconnect.services.UserService;
import com.mindconnect.mindconnect.services.implementations.JwtImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    JwtImplementation jwtImplementation;

    @MockBean
    UserDetailsService userDetailsService;


    /**
     * This test method verifies the behavior of the changePassword endpoint in the UserController.
     * It sends a PATCH request with a valid ChangePasswordDto
     * and expects a successful response with the appropriate message.
     */
    @Test
    void testChangePassword() throws Exception {
        // Arrange
        ChangePasswordDto changePasswordDto = new ChangePasswordDto("naza", "naztar", "naztar");

        ResponseEntity<ApiResponse<String>> responseExpected = new ResponseEntity<>(
                new ApiResponse<>("New password", "Password changed successfully"),
                HttpStatus.OK);

        when(userService.changePassword(any(ChangePasswordDto.class))).thenReturn(responseExpected);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.patch("/user/password/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(changePasswordDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").value("New password"))
                .andExpect(jsonPath("$.message").value("Password changed successfully"));
    }

    /**
     * This test method verifies the behavior of the getPosts endpoint in the UserController.
     * It sends a GET request with a user id
     * and expects a list of posts and a successful response with the appropriate message.
     */
    @Test
    void getPosts_ReturnsPostHistoryListOfAUser() throws Exception {
        //Arrange
        List<PostHistory> postHistoryList = new ArrayList<>();
        postHistoryList.add(new PostHistory());
        postHistoryList.add(new PostHistory());
        postHistoryList.add(new PostHistory());

        NewsFeedDto newsFeedDto = new NewsFeedDto(true, null, null, null);

        ApiResponse<List<PostHistory>> returnResponse = new ApiResponse(postHistoryList, "User's posts fetched successfully");
        UUID uuid = UUID.randomUUID();

        when(userService.viewPosts(any(NewsFeedDto.class))).thenReturn(returnResponse);

        //Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/user/news/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newsFeedDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("User's posts fetched successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(postHistoryList.size()));


        //verifying that userService.viewPosts is called with the correct arguments
        verify(userService).viewPosts(eq(newsFeedDto));
    }

    /**
     * This test method verifies the behavior of the getPosts endpoint in the UserController.
     * It sends a GET request with a group id
     * and expects a list of posts and a successful response with the appropriate message.
     */
    @Test
    void getPosts_ReturnsPostHistoryListOfAGroup() throws Exception {
        //Arrange
        UUID groupId = UUID.randomUUID();
        List<PostHistory> postHistoryList = new ArrayList<>();
        postHistoryList.add(new PostHistory());
        postHistoryList.add(new PostHistory());
        postHistoryList.add(new PostHistory());

        NewsFeedDto newsFeedDto = new NewsFeedDto(false, groupId, null, null);

        ApiResponse<List<PostHistory>> returnResponse = new ApiResponse(postHistoryList, "Group posts fetched successfully");
        UUID uuid = UUID.randomUUID();

        when(userService.viewPosts(any(NewsFeedDto.class))).thenReturn(returnResponse);

        //Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/user/news/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newsFeedDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Group posts fetched successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(postHistoryList.size()));

        //verifying that userService.viewPosts is called with the correct arguments
        verify(userService).viewPosts(eq(newsFeedDto));
    }

    /**
     * This test method verifies the behavior of the getPosts endpoint in the UserController.
     * It sends a GET request without indicating any user or group id
     * and expects a list of posts and a successful response with the appropriate message.
     */
    @Test
    void getPosts_ReturnsPostHistoryList() throws Exception {
        //Arrange
        List<PostHistory> postHistoryList = new ArrayList<>();
        postHistoryList.add(new PostHistory());
        postHistoryList.add(new PostHistory());
        postHistoryList.add(new PostHistory());
        postHistoryList.add(new PostHistory());
        postHistoryList.add(new PostHistory());
        postHistoryList.add(new PostHistory());
        postHistoryList.add(new PostHistory());
        postHistoryList.add(new PostHistory());

        NewsFeedDto newsFeedDto = new NewsFeedDto(false, null, null, null);

        ApiResponse<List<PostHistory>> returnResponse = new ApiResponse(postHistoryList, "Public posts fetched successfully");

        when(userService.viewPosts(any(NewsFeedDto.class))).thenReturn(returnResponse);

        //Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/user/news/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newsFeedDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Public posts fetched successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(postHistoryList.size()));

        //verifying that userService.viewPosts is called with the correct arguments
        verify(userService).viewPosts(eq(newsFeedDto));
    }
}