package com.mindconnect.mindconnect.services.implementations;


import com.mindconnect.mindconnect.Models.Post;
import com.mindconnect.mindconnect.Models.User;
import com.mindconnect.mindconnect.dtos.*;
import com.mindconnect.mindconnect.exceptions.MindConnectException;
import com.mindconnect.mindconnect.mapper.PostMapper;
import com.mindconnect.mindconnect.payloads.ApiResponse;
import com.mindconnect.mindconnect.payloads.PostHistory;
import com.mindconnect.mindconnect.repositories.*;
import com.mindconnect.mindconnect.services.UserService;
import com.mindconnect.mindconnect.utils.TimeFormatter;
import com.mindconnect.mindconnect.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryServiceImpl cloudinaryService;

    @Override
    public ResponseEntity<ApiResponse<String>> changePassword(ChangePasswordDto changePasswordDto) {
            String email = UserUtil.getLoggedInUser();
            User user = userRepository.findByEmailAddress(email)
                    .orElseThrow(() -> new MindConnectException("User not found"));

            if (passwordEncoder.matches(changePasswordDto.oldPassword(), user.getPassword())) {
                if(!changePasswordDto.newPassword().equals(changePasswordDto.oldPassword())){
                    if (changePasswordDto.newPassword().equals(changePasswordDto.confirmPassword())) {
                        user.setPassword(passwordEncoder.encode(changePasswordDto.newPassword()));
                        userRepository.save(user);

                        ApiResponse<String> response = new ApiResponse<>(
                                "New password",
                                "Password changed successfully"
                        );
                        return new ResponseEntity<>(response, response.getStatus());
                    } else {
                        throw new MindConnectException("New passwords do not match");
                    }
                } else {
                    throw new MindConnectException("New and old passwords are the same");
                }
            } else {
                throw new MindConnectException("Incorrect old password");
            }
    }

    @Override
    public ApiResponse<ProfileResponse> editProfile(ProfileDto profileDto) {
        String email = UserUtil.getLoggedInUser();
        User user = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new MindConnectException("User not found"));
        user.setUserNames(profileDto.userNames());
        user.setFirstName(profileDto.firstName());
        user.setLastName(profileDto.lastName());
        user.setGender(profileDto.gender());
        user.setCountry(profileDto.country());
        user.setState(profileDto.state());
        user.setMentalCondition(profileDto.mentalCondition());
        User updatedUser = userRepository.save(user);

        return new ApiResponse<>(new ProfileResponse(
                updatedUser.getFirstName(),
                updatedUser.getLastName(),
                updatedUser.getCountry(),
                updatedUser.getState(),
                updatedUser.getEmailAddress(),
                updatedUser.getProfilePictureUrl(),
                updatedUser.getGender(),
                updatedUser.getMentalCondition()
        ),
                "Profile updated successfully",
                HttpStatus.OK
        );
    }

    /**
     * @param newsFeedDto - NewsFeedDto Object
     * @returnAn ApiResponse containing the paginated list of PostHistory objects.
     */
    @Override
    public ApiResponse<List<PostHistory>> viewPosts(NewsFeedDto newsFeedDto) {
        Integer page = newsFeedDto.page();
        Integer size = newsFeedDto.size();
        page = page != null && page >= 0 ? page : 0;
        size = size != null && size > 0 ? size : DEFAULT_PAGE_SIZE;

        Pageable pageable = PageRequest.of(page, size);

        if(newsFeedDto.isUser()){
            User loggedInUser = userRepository.findByEmailAddress(UserUtil.getLoggedInUser())
                    .orElseThrow(() -> new MindConnectException("User doesn't exist"));
            UUID userId = loggedInUser.getId();
            Page<Post> userPosts = postRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
            return new ApiResponse<>(mapPostsToPostHistory(userPosts), "User's posts fetched successfully");

        } else if (newsFeedDto.groupId() != null) {
            Page<Post> groupPosts = postRepository.findByGroupIdOrderByCreatedAtDesc(newsFeedDto.groupId(), pageable);
            return new ApiResponse<>(mapPostsToPostHistory(groupPosts), "Group posts fetched successfully");

        } else {
            Page<Post> publicPosts = postRepository.findAllByHiddenFalseAndGroupIsNullOrderByCreatedAtDesc(pageable);
            return new ApiResponse<>(mapPostsToPostHistory(publicPosts), "Public posts fetched successfully");
        }

    }

    @Override
    public ResponseEntity<ApiResponse<String>> updateProfilePic(MultipartFile file) throws IOException {

        String email = UserUtil.getLoggedInUser();
        User user = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new MindConnectException("User not found"));

        String uri = cloudinaryService.uploadFile(file);
        user.setProfilePictureUrl(uri);
        userRepository.save(user);
        ApiResponse<String> response = new ApiResponse<>(
                uri,
                "Profile pic updated successfully",
                HttpStatus.OK
        );
        return new ResponseEntity<>(response, response.getStatus());
    }

    @Override
    public ApiResponse<List<UserListResponse>> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<UserListResponse> users =  userRepository.findAll(pageable).stream().map(user -> {
            return new UserListResponse(
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmailAddress(),
                    user.getProfilePictureUrl()
            );
        }).collect(Collectors.toList());

       return new ApiResponse<>(
               users,
               "success"
       );
    }

    @Override
    public ApiResponse<List<UserListResponse>> searchUsers(String query) {
        UserListResponse response = new UserListResponse();
        List<UserListResponse> searchResult = userRepository.searchUsers(query, UserUtil.getLoggedInUser()).stream().map(user -> {
            if (!Objects.equals(user.getEmailAddress(), UserUtil.getLoggedInUser())) {
                    response.setFirstName(user.getFirstName());
                    response.setLastName(user.getLastName());
                    response.setEmailAddress(user.getEmailAddress());
                    response.setProfilePicUrl(user.getProfilePictureUrl());
        }
            return  response;
        }).collect(Collectors.toList());
        return new ApiResponse<>(searchResult, "success");
    }

    /**
     * Maps a Page of Post entities to a List of PostHistory objects.
     *
     * @param posts The Page object containing Post entities.
     * @return A list of PostHistory objects.
     */
    private List<PostHistory> mapPostsToPostHistory(Page<Post> posts){
       return posts.stream()
               .map(post -> PostMapper
                       .mapToPostHistory(post,
                               new PostHistory(),
                               likeRepository.getLikeCountByPostId(post.getId()),
                               commentRepository.getCommentCountByPostId(post.getId()),
                               TimeFormatter.formatRelativeTime(post.getCreatedAt())))
               .collect(Collectors.toList());
    }
    public ResponseEntity<ApiResponse<ProfileResponse>> viewProfile() {
        var user = userRepository.findByEmailAddress(UserUtil.getLoggedInUser()).orElseThrow(()->
                new MindConnectException("User not found"));
            ProfileResponse profileResponse = new ProfileResponse();
            profileResponse.setFirstName(user.getFirstName());
            profileResponse.setLastName(user.getLastName());
            profileResponse.setState(user.getState());
            profileResponse.setCountry(user.getCountry());
            profileResponse.setEmailAddress(user.getEmailAddress());
            profileResponse.setMentalCondition(user.getMentalCondition());
            profileResponse.setGender(user.getGender());
            profileResponse.setProfilePicture(user.getProfilePictureUrl());
            ApiResponse response = new ApiResponse<>(profileResponse, "Request Processed Successfully");

            return new ResponseEntity<>(response, response.getStatus());
        }

    @Override
    public ResponseEntity<ApiResponse<String>> blockUser(UUID blockedUserId) {
        String email = UserUtil.getLoggedInUser();
        var user = userRepository.findByEmailAddress(email).orElseThrow(
                () -> new MindConnectException("User not found")
        );

        User userToBlock = userRepository.findById(blockedUserId);
        if (userToBlock == null) {
            throw new MindConnectException("User to be blocked not found");
        }

        if (user.getBlockedUsers().contains(userToBlock)) {
            throw new MindConnectException("You have already blocked this user");
        }

        user.getBlockedUsers().add(userToBlock);
        userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse<>("User blocked successfully", HttpStatus.OK));
    }

    }
