package com.mindconnect.mindconnect.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private String firstName;
    private String lastName;
    private String country;
    private String state;
    private String emailAddress;
    private String profilePicture;
    private String gender;
    private String mentalCondition;
}
