package com.zerolatency.backend.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DestinationResponse {
    private String kindergarten;
    private String primarySchool;
    private String middleSchool;
    private String highSchool;
    private String collegeUniversity;
}
