package com.zerolatency.backend.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FullInformationResponse {
    private UserResponse user;
    private ProfileResponse profile;
    private WebsiteResponse website;
    private DestinationResponse destination;
}
