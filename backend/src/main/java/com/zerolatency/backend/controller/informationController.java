package com.zerolatency.backend.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zerolatency.backend.dto.Request.DestinationRequest;
import com.zerolatency.backend.dto.Request.ProfileRequest;
import com.zerolatency.backend.dto.Request.WebsiteRequest;
import com.zerolatency.backend.dto.Response.DestinationResponse;
import com.zerolatency.backend.dto.Response.ProfileResponse;
import com.zerolatency.backend.dto.Response.ResponseFormat;
import com.zerolatency.backend.dto.Response.WebsiteResponse;
import com.zerolatency.backend.service.CloudinaryService;
import com.zerolatency.backend.service.informationService;

@RestController
@RequestMapping(
    "/api/information"
)
public class informationController {
    @Autowired informationService infoService;
    @Autowired CloudinaryService cloudinaryService;

    @GetMapping(
        "/get-profile/{userId}"
    )
    public ResponseEntity<ResponseFormat> getProfileByUserId(@PathVariable("userId") Long userId) {
        Optional<ProfileResponse> profileOpt = infoService.getProfileByUserId(userId);
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
        Optional<DestinationResponse> destinationOpt = infoService.getDestinationByUserId(userId);
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
        Optional<WebsiteResponse> websiteOpt = infoService.getWebsiteByUserId(userId);
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
        Optional<DestinationResponse> updatedDestinationOpt = infoService.updateDestinationByUserId(userId, destination.getKindergarten(), destination.getPrimarySchool(), destination.getMiddleSchool(), destination.getHighSchool(), destination.getCollegeUniversity());
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
        Optional<ProfileResponse> updatedProfileOpt = infoService.updateProfileByUserIdService(userId, profile.getFirstName(), profile.getLastName(), profile.getCurrentLocation(), profile.getHometown(), profile.getOccupation(), profile.getBio(), profile.getBirthday(), profile.getGender());
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
        Optional<WebsiteResponse> updatedWebsiteOpt = 
            infoService.updateWebsiteByUserIdService(userId, website.getGithub(), website.getLinkedin(), website.getPortfolio(), website.getTwitter(), website.getFacebook(), website.getInstagram());
        if (updatedWebsiteOpt.isPresent()) {
            ResponseFormat response = new ResponseFormat(200L, "Website updated successfully", updatedWebsiteOpt.get());
            return ResponseEntity.ok(response);
        } else {
            ResponseFormat response = new ResponseFormat(404L, "Failed to update website", null);
            return ResponseEntity.status(404).body(response);
        }
    }

    @PatchMapping(
        "/update-avatar/{userId}"
    )
    public ResponseEntity<ResponseFormat> updateAvatarByUserId(@PathVariable("userId") Long userId, @RequestPart MultipartFile avatarData) {
        // Upload avatar to Cloudinary
        Optional<String> avatarUrlOpt = Optional.ofNullable(cloudinaryService.uploadFile(avatarData));
        if (avatarUrlOpt.isEmpty()) {
            ResponseFormat response = new ResponseFormat(500L, "Failed to upload avatar", null);
            return ResponseEntity.status(500).body(response);
        }

        // Update avatar URL in profile
        Optional<String> updatedAvatarOpt = infoService.updateAvatarUrlByUserIdService(userId, avatarUrlOpt.get());
        if (updatedAvatarOpt.isPresent()) {
            ResponseFormat response = new ResponseFormat(200L, "Avatar updated successfully", updatedAvatarOpt.get());
            return ResponseEntity.ok(response);
        } else {
            ResponseFormat response = new ResponseFormat(404L, "Failed to update avatar", null);
            return ResponseEntity.status(404).body(response);
        }
    }
}
