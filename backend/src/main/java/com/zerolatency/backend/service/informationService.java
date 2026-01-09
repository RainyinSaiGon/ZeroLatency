package com.zerolatency.backend.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerolatency.backend.dto.Response.DestinationResponse;
import com.zerolatency.backend.dto.Response.FullInformationResponse;
import com.zerolatency.backend.dto.Response.ProfileResponse;
import com.zerolatency.backend.dto.Response.UserResponse;
import com.zerolatency.backend.dto.Response.WebsiteResponse;
import com.zerolatency.backend.model.UserDestination;
import com.zerolatency.backend.model.UserProfile;
import com.zerolatency.backend.model.UserWebsite;
import com.zerolatency.backend.model.Users;
import com.zerolatency.backend.repo.destinationJpaRepo;
import com.zerolatency.backend.repo.profileJpaRepo;
import com.zerolatency.backend.repo.usersRepo;
import com.zerolatency.backend.repo.websiteJpaRepo;

@Service
public class informationService {
    @Autowired profileJpaRepo profileJpaRepo;
    @Autowired websiteJpaRepo websiteJpaRepo;
    @Autowired destinationJpaRepo destinationJpaRepo;
    @Autowired usersRepo usersRepo;

    public Optional<ProfileResponse> getProfileByUserId(Long userId) {
        Optional<UserProfile> profileOpt = Optional.ofNullable(profileJpaRepo.findByUser_UserId(userId));
        ProfileResponse profileResponse = profileOpt.map(profile -> new ProfileResponse(
            profile.getFirstName(),
            profile.getLastName(),
            profile.getBio(),
            profile.getCurrentLocation(),
            profile.getHometown(),
            profile.getOccupation(),
            profile.getBirthday(),
            profile.getGender(),
            profile.getAvatarUrl()
        )).orElse(null);
        return Optional.ofNullable(profileResponse);
        
    }

    public Optional<WebsiteResponse> getWebsiteByUserId(Long userId) {
        Optional<UserWebsite> websiteOpt = Optional.ofNullable(websiteJpaRepo.findByUser_UserId(userId));
        WebsiteResponse websiteResponse = websiteOpt.map(website -> new WebsiteResponse(
            website.getGithub(),
            website.getLinkedin(),
            website.getPortfolio(),
            website.getTwitter(),
            website.getFacebook(),
            website.getInstagram()
        )).orElse(null);
        return Optional.ofNullable(websiteResponse);
    }

    public Optional<DestinationResponse> getDestinationByUserId(Long userId) {
        Optional<UserDestination> destinationOpt = Optional.ofNullable(destinationJpaRepo.findByUser_UserId(userId));
        DestinationResponse destinationResponse = destinationOpt.map(dest -> new DestinationResponse(
            dest.getKindergarten(),
            dest.getPrimarySchool(),
            dest.getMiddleSchool(),
            dest.getHighSchool(),
            dest.getCollegeUniversity()
        )).orElse(null);
        return Optional.ofNullable(destinationResponse);
    }

    public Optional<FullInformationResponse> getFullInfoByUserId(Long userId) {
        Optional<Users> userOpt = Optional.ofNullable(usersRepo.findByUserId(userId));

        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Optional<ProfileResponse> profileOpt = getProfileByUserId(userId);
        Optional<WebsiteResponse> websiteOpt = getWebsiteByUserId(userId);
        Optional<DestinationResponse> destinationOpt = getDestinationByUserId(userId);

        if (profileOpt.isPresent() || websiteOpt.isPresent() || destinationOpt.isPresent() || userOpt.isPresent()) {
            FullInformationResponse fullInfo = new FullInformationResponse();
            UserResponse userResponse = new UserResponse(userOpt.get().getUserId(), userOpt.get().getUsername(), userOpt.get().getRole());
            ProfileResponse profileResponse = profileOpt.orElse(null);
            WebsiteResponse websiteResponse = websiteOpt.orElse(null);
            DestinationResponse destinationResponse = destinationOpt.orElse(null);

            fullInfo.setUser(userResponse);
            fullInfo.setProfile(profileResponse);
            fullInfo.setWebsite(websiteResponse);
            fullInfo.setDestination(destinationResponse);

            return Optional.of(fullInfo);
        }
        return Optional.empty();
    }

    public Optional<DestinationResponse> updateDestinationByUserId(Long userId, String kindergarten, String primarySchool, String middleSchool, String highSchool, String collegeUniversity) {
        int rowsUpdated = destinationJpaRepo.updateDestinationByUser_UserId
            (userId, kindergarten, primarySchool, middleSchool, highSchool, collegeUniversity);
        if (rowsUpdated > 0) {
            return getDestinationByUserId(userId);
        } else {
            return Optional.empty();
        }
    }

    public Optional<ProfileResponse> updateProfileByUserIdService(Long userId, String firstName, String lastName, String currentLocation, String hometown, String occupation, String bio, Date birthday, String gender) {
        int rowsUpdated = profileJpaRepo.updateProfileByUser_UserId
            (userId, firstName, lastName, currentLocation, hometown, occupation, bio, birthday, gender);
        if (rowsUpdated > 0) {
            return getProfileByUserId(userId);
        } else {
            return Optional.empty();
        }
    }

    public Optional<WebsiteResponse> updateWebsiteByUserIdService(Long userId, String github, String linkedin, String portfolio, String twitter, String facebook, String instagram){
        int rowsUpdated = websiteJpaRepo.updateWebsiteByUser_UserId
            (userId, github, linkedin, portfolio,  twitter, facebook, instagram);
        if (rowsUpdated > 0) {
            return getWebsiteByUserId(userId);
        } else {
            return Optional.empty();
        }
    }

    public Optional<String> updateAvatarUrlByUserIdService(Long userId, String avatarUrl){
        int rowsUpdated = profileJpaRepo.updateAvatarUrlByUser_UserId(userId, avatarUrl);
        if (rowsUpdated > 0) {
            return Optional.of(avatarUrl);
        } else {
            return Optional.empty();
        }
    }
}
