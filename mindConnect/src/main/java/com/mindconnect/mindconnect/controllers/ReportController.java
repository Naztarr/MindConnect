package com.mindconnect.mindconnect.controllers;

import com.mindconnect.mindconnect.payloads.ApiResponse;
import com.mindconnect.mindconnect.payloads.ReportRequest;
import com.mindconnect.mindconnect.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/report")
    public ResponseEntity<ApiResponse<?>> reportUser(@RequestBody ReportRequest request) {
        ApiResponse<String> response = reportService.reportUser(request);
        return new ResponseEntity<>(response, response.getStatus());
    }

}

