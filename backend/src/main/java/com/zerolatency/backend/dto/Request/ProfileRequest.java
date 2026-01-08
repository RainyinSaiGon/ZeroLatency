package com.zerolatency.backend.dto.Request;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {
    private String bio;
    private String firstName;
    private String lastName;
    private String currentLocation;
    private String hometown;
    private String occupation;
    private Date birthday;
    private String gender;

}
