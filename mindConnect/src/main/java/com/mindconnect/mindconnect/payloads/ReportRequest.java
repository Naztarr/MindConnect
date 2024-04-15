package com.mindconnect.mindconnect.payloads;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReportRequest {
    private UUID reportedUserId;
    private String reason;
}

