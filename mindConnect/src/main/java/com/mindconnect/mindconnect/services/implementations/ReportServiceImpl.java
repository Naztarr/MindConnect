package com.mindconnect.mindconnect.services.implementations;

import com.mindconnect.mindconnect.Models.Report;
import com.mindconnect.mindconnect.Models.User;
import com.mindconnect.mindconnect.exceptions.MindConnectException;
import com.mindconnect.mindconnect.payloads.ApiResponse;
import com.mindconnect.mindconnect.payloads.ReportRequest;
import com.mindconnect.mindconnect.repositories.ReportRepository;
import com.mindconnect.mindconnect.repositories.UserRepository;
import com.mindconnect.mindconnect.services.ReportService;
import com.mindconnect.mindconnect.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    @Override
    public ApiResponse<String> reportUser(ReportRequest request) {
        String email = UserUtil.getLoggedInUser();
        User reporter = userRepository.findByEmailAddress(email).orElseThrow(
                () -> new MindConnectException("User not found")
        );

        User reportedUser = userRepository.findById(request.getReportedUserId());
        if (reportedUser == null) {
            throw new MindConnectException("Reported user not found");
        }

        boolean alreadyReported = reportRepository.existsByReportedByAndReportedUser(reporter, reportedUser);
        if (alreadyReported) {
            return new ApiResponse<>("User already reported", HttpStatus.BAD_REQUEST);
        }

        Report report = new Report();
        report.setReportedBy(reporter);
        report.setReportedUser(reportedUser);
        report.setReason(request.getReason());

        reportRepository.save(report);

        return new ApiResponse<>("User reported successfully", HttpStatus.OK);
    }


}

