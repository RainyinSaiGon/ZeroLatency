package com.zerolatency.backend.dto.Response;

import com.zerolatency.backend.model.UserDestination;
import com.zerolatency.backend.model.UserProfile;
import com.zerolatency.backend.model.UserWebsite;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class fullInfoResponse {
    private UserProfile profile;
    private UserWebsite website;
    private UserDestination destination;
}
