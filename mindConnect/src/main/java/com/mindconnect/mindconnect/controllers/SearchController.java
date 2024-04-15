package com.mindconnect.mindconnect.controllers;

import com.mindconnect.mindconnect.dtos.GroupSearchResponseDTO;
import com.mindconnect.mindconnect.dtos.PostResponseDto;
import com.mindconnect.mindconnect.dtos.UserResponseDto;
import com.mindconnect.mindconnect.payloads.ApiResponse;
import com.mindconnect.mindconnect.services.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam String query,
            @RequestParam(required = false) String isPostSearch,
            @RequestParam(required = false) String isGroupSearch) {

        ApiResponse<?> response = searchService.search(query, isPostSearch, isGroupSearch);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
