package com.mindconnect.mindconnect.services.implementations;

import com.mindconnect.mindconnect.Models.Group;
import com.mindconnect.mindconnect.Models.Post;
import com.mindconnect.mindconnect.Models.User;
import com.mindconnect.mindconnect.payloads.ApiResponse;
import com.mindconnect.mindconnect.dtos.GroupSearchResponseDTO;
import com.mindconnect.mindconnect.dtos.PostResponseDto;
import com.mindconnect.mindconnect.dtos.UserResponseDto;
import com.mindconnect.mindconnect.repositories.GroupRepository;
import com.mindconnect.mindconnect.repositories.PostRepository;
import com.mindconnect.mindconnect.repositories.UserRepository;
import com.mindconnect.mindconnect.services.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final GroupRepository groupRepository;

    @Override
    public ApiResponse<List<UserResponseDto>> searchUsers(String query, String isPostSearch, String isGroupSearch) {
        boolean isPostSearchBoolean = Boolean.parseBoolean(isPostSearch);
        boolean isGroupSearchBoolean = Boolean.parseBoolean(isGroupSearch);

        if (isPostSearchBoolean || isGroupSearchBoolean) {
            return new ApiResponse<>("Invalid search type for searching users", HttpStatus.BAD_REQUEST);
        }

        List<User> users = searchUsers(query);

        if (users.isEmpty()) {
            return new ApiResponse<>("No users found for the query", HttpStatus.NOT_FOUND);
        }

        List<UserResponseDto> userDtos = users.stream()
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getUsername(),
                        user.getGender(),
                        user.getCountry(),
                        user.getState(),
                        user.getMentalCondition()))
                .collect(Collectors.toList());

        return new ApiResponse<>(userDtos, "Users retrieved successfully", HttpStatus.OK);
    }

    @Override
    public ApiResponse<List<GroupSearchResponseDTO>> searchGroups(String query, String isPostSearch, String isGroupSearch) {
        boolean isPostSearchBoolean = Boolean.parseBoolean(isPostSearch);
        boolean isGroupSearchBoolean = Boolean.parseBoolean(isGroupSearch);

        if (!isGroupSearchBoolean) {
            return new ApiResponse<>("Invalid search type for searching groups", HttpStatus.BAD_REQUEST);
        }

        List<Group> groups = groupRepository.findByNameContainingIgnoreCase(query);

        if (groups.isEmpty()) {
            return new ApiResponse<>("No groups found for the query", HttpStatus.NOT_FOUND);
        }

        List<GroupSearchResponseDTO> groupDtos = groups.stream()
                .map(group -> new GroupSearchResponseDTO(group.getId(), group.getName()))
                .collect(Collectors.toList());

        return new ApiResponse<>(groupDtos, "Groups retrieved successfully", HttpStatus.OK);
    }

    @Override
    public ApiResponse<List<PostResponseDto>> searchPosts(String query, String isPostSearch, String isGroupSearch) {
        boolean isPostSearchBoolean = Boolean.parseBoolean(isPostSearch);
        boolean isGroupSearchBoolean = Boolean.parseBoolean(isGroupSearch);

        if (!isPostSearchBoolean) {
            return new ApiResponse<>("Invalid search type for searching posts", HttpStatus.BAD_REQUEST);
        }

        List<Post> posts = postRepository.findByContentContainingIgnoreCase(query);

        if (posts.isEmpty()) {
            return new ApiResponse<>("No posts found for the query", HttpStatus.NOT_FOUND);
        }

        List<PostResponseDto> postDtos = posts.stream()
                .map(post -> new PostResponseDto(post.getId(), post.getContent()))
                .collect(Collectors.toList());

        return new ApiResponse<>(postDtos, "Posts retrieved successfully", HttpStatus.OK);
    }

    private List<User> searchUsers(String query) {
        if (!StringUtils.hasText(query)) {
            return Collections.emptyList();
        }

        String[] parts = query.trim().split("\\s+");
        List<User> users;

        if (parts.length == 1) {
            users = userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(parts[0], parts[0]);
        } else if (parts.length == 2) {
            users = userRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(parts[0], parts[1]);
            if (users.isEmpty()) {
                users = userRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(parts[1], parts[0]);
            }
        } else {
            return Collections.emptyList();
        }

        return users;
    }

    public ApiResponse<?> search(String query, String isPostSearch, String isGroupSearch) {
        if ("true".equalsIgnoreCase(isPostSearch)) {
            return searchPosts(query, isPostSearch, isGroupSearch);
        } else if ("true".equalsIgnoreCase(isGroupSearch)) {
            return searchGroups(query, isPostSearch, isGroupSearch);
        } else {
            return searchUsers(query, isPostSearch, isGroupSearch);
        }
    }
}
