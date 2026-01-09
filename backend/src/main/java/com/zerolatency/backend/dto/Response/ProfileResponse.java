package com.zerolatency.backend.dto.Response;


import java.util.Date;

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
    private String bio;
    private String currentLocation;
    private String hometown;
    private String occupation;
    private Date birthday;
    private String gender;
    private String avatarUrl;
}
