package com.mindconnect.mindconnect.services;

import com.mindconnect.mindconnect.payloads.ApiResponse;
import com.mindconnect.mindconnect.payloads.ReportRequest;


public interface ReportService {
    ApiResponse<String> reportUser(ReportRequest request);
}

