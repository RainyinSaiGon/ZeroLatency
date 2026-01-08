package com.zerolatency.backend.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DestinationRequest {
    private String kindergarten;
    private String primarySchool;
    private String middleSchool;
    private String highSchool;
    private String collegeUniversity;
}
