package com.zerolatency.backend.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebsiteResponse {
    private String github;
    private String linkedin;
    private String portfolio;
    private String twitter;
    private String facebook;
    private String instagram;
}
