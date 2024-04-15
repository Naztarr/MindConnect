package com.mindconnect.mindconnect.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListResponse {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String profilePicUrl;
}
