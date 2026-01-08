package com.zerolatency.backend.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WebsiteRequest {
    private String github;
    private String linkedin;
    private String portfolio;
    private String twitter;
    private String facebook;
    private String instagram;
}
