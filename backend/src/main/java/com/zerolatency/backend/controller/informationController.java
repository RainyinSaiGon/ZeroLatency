package com.zerolatency.backend.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerolatency.backend.dto.Request.DestinationRequest;
import com.zerolatency.backend.dto.Request.ProfileRequest;
import com.zerolatency.backend.dto.Request.WebsiteRequest;
import com.zerolatency.backend.dto.Response.ResponseFormat;
import com.zerolatency.backend.model.UserDestination;
import com.zerolatency.backend.model.UserProfile;
import com.zerolatency.backend.model.UserWebsite;
import com.zerolatency.backend.service.informationService;

@RestController
@RequestMapping(
    "/api/information"
)
public class informationController {
    @Autowired informationService infoService;

    @GetMapping(
        "/get-profile/{userId}"
    )
    public ResponseEntity<ResponseFormat> getProfileByUserId(@PathVariable("userId") Long userId) {
        Optional<UserProfile> profileOpt = infoService.getProfileByUserId(userId);
        if (profileOpt.isPresent()) {
            ResponseFormat response = new ResponseFormat(200L, "Success", profileOpt.get());
            return ResponseEntity.ok(response);
        } else {
            ResponseFormat response = new ResponseFormat(404L, "Profile not found", null);
            return ResponseEntity.status(404).body(response);
        }
    }

    @GetMapping(
        "/get-destination/{userId}"
    )
    public ResponseEntity<ResponseFormat> getDestinationByUserId(@PathVariable("userId") Long userId) {
        Optional<UserDestination> destinationOpt = infoService.getDestinationByUserId(userId);
        if (destinationOpt.isPresent()) {
            ResponseFormat response = new ResponseFormat(200L, "Success", destinationOpt.get());
            return ResponseEntity.ok(response);
        } else {
            ResponseFormat response = new ResponseFormat(404L, "Destination not found", null);
            return ResponseEntity.status(404).body(response);
        }
    }

    @GetMapping(
        "/get-website/{userId}"
    )
    public ResponseEntity<ResponseFormat> getWebsiteByUserId(@PathVariable("userId") Long userId) {
        Optional<UserWebsite> websiteOpt = infoService.getWebsiteByUserId(userId);
        if (websiteOpt.isPresent()) {
            ResponseFormat response = new ResponseFormat(200L, "Success", websiteOpt.get());
            return ResponseEntity.ok(response);
        } else {
            ResponseFormat response = new ResponseFormat(404L, "Website not found", null);
            return ResponseEntity.status(404).body(response);
        }
    }

    @GetMapping(
        "/get-full-info/{userId}"
    )
    public ResponseEntity<ResponseFormat> getFullInfoByUserId(@PathVariable("userId") Long userId) {
        Optional<?> fullInfoOpt = infoService.getFullInfoByUserId(userId);
        if (fullInfoOpt.isPresent()) {
            ResponseFormat response = new ResponseFormat(200L, "Success", fullInfoOpt.get());
            return ResponseEntity.ok(response);
        } else {
            ResponseFormat response = new ResponseFormat(404L, "No information found", null);
            return ResponseEntity.status(404).body(response);
        }
    }

    @PatchMapping(
        "/update-destination/{userId}"
    )
    public ResponseEntity<ResponseFormat> updateDestinationByUserId(@PathVariable("userId") Long userId, @RequestBody DestinationRequest destination) {
        Optional<UserDestination> updatedDestinationOpt = infoService.updateDestinationByUserId(userId, destination.getKindergarten(), destination.getPrimarySchool(), destination.getMiddleSchool(), destination.getHighSchool(), destination.getCollegeUniversity());
        if (updatedDestinationOpt.isPresent()) {
            ResponseFormat response = new ResponseFormat(200L, "Destination updated successfully", updatedDestinationOpt.get());
            return ResponseEntity.ok(response);
        } else {
            ResponseFormat response = new ResponseFormat(404L, "Failed to update destination", null);
            return ResponseEntity.status(404).body(response);
        }
    }

    @PatchMapping(
        "/update-profile/{userId}"
    )
    public ResponseEntity<ResponseFormat> updateProfileByUserId(@PathVariable("userId") Long userId, @RequestBody ProfileRequest profile) {
        Optional<UserProfile> updatedProfileOpt = infoService.updateProfileByUserIdService(userId, profile.getFirstName(), profile.getLastName(), profile.getCurrentLocation(), profile.getHometown(), profile.getOccupation(), profile.getBio(), profile.getBirthday(), profile.getGender());
        if (updatedProfileOpt.isPresent()) {
            ResponseFormat response = new ResponseFormat(200L, "Profile updated successfully", updatedProfileOpt.get());
            return ResponseEntity.ok(response);
        } else {
            ResponseFormat response = new ResponseFormat(404L, "Failed to update profile", null);
            return ResponseEntity.status(404).body(response);
        }
    }

    @PatchMapping(
        "/update-website/{userId}"
    )
    public ResponseEntity<ResponseFormat> updateWebsiteByUserId(@PathVariable("userId") Long userId, @RequestBody WebsiteRequest website) {
        Optional<UserWebsite> updatedWebsiteOpt = 
            infoService.updateWebsiteByUserIdService(userId, website.getGithub(), website.getLinkedin(), website.getPortfolio(), website.getTwitter(), website.getFacebook(), website.getInstagram());
        if (updatedWebsiteOpt.isPresent()) {
            ResponseFormat response = new ResponseFormat(200L, "Website updated successfully", updatedWebsiteOpt.get());
            return ResponseEntity.ok(response);
        } else {
            ResponseFormat response = new ResponseFormat(404L, "Failed to update website", null);
            return ResponseEntity.status(404).body(response);
        }
    }
}
