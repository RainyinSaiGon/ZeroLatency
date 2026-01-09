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

    public Optional<UserProfile> getProfileByUserId(Long userId) {
        return Optional.ofNullable(profileJpaRepo.findByUser_UserId(userId));
    }

    public Optional<UserWebsite> getWebsiteByUserId(Long userId) {
        return Optional.ofNullable(websiteJpaRepo.findByUser_UserId(userId));
    }

    public Optional<UserDestination> getDestinationByUserId(Long userId) {
        return Optional.ofNullable(destinationJpaRepo.findByUser_UserId(userId));
    }

    public Optional<FullInformationResponse> getFullInfoByUserId(Long userId) {
        Optional<Users> userOpt = Optional.ofNullable(usersRepo.findByUserId(userId));

        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Optional<UserProfile> profileOpt = getProfileByUserId(userId);
        Optional<UserWebsite> websiteOpt = getWebsiteByUserId(userId);
        Optional<UserDestination> destinationOpt = getDestinationByUserId(userId);

        if (profileOpt.isPresent() || websiteOpt.isPresent() || destinationOpt.isPresent() || userOpt.isPresent()) {
            FullInformationResponse fullInfo = new FullInformationResponse();
            UserResponse userResponse = new UserResponse(userOpt.get().getUserId(), userOpt.get().getUsername(), userOpt.get().getRole());
            DestinationResponse destinationResponse = destinationOpt.map(dest -> new DestinationResponse(
                dest.getKindergarten(),
                dest.getPrimarySchool(),
                dest.getMiddleSchool(),
                dest.getHighSchool(),
                dest.getCollegeUniversity()
            )).orElse(null);
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
            WebsiteResponse websiteResponse = websiteOpt.map(website -> new WebsiteResponse(
                website.getGithub(),
                website.getLinkedin(),
                website.getPortfolio(),
                website.getTwitter(),
                website.getFacebook(),
                website.getInstagram()
            )).orElse(null);
            
            fullInfo.setUser(userResponse);
            fullInfo.setProfile(profileResponse);
            fullInfo.setWebsite(websiteResponse);
            fullInfo.setDestination(destinationResponse);

            return Optional.of(fullInfo);
        }
        return Optional.empty();
    }

    public Optional<UserDestination> updateDestinationByUserId(Long userId, String kindergarten, String primarySchool, String middleSchool, String highSchool, String collegeUniversity) {
        int rowsUpdated = destinationJpaRepo.updateDestinationByUser_UserId
            (userId, kindergarten, primarySchool, middleSchool, highSchool, collegeUniversity);
        if (rowsUpdated > 0) {
            return getDestinationByUserId(userId);
        } else {
            return Optional.empty();
        }
    }

    public Optional<UserProfile> updateProfileByUserIdService(Long userId, String firstName, String lastName, String currentLocation, String hometown, String occupation, String bio, Date birthday, String gender) {
        int rowsUpdated = profileJpaRepo.updateProfileByUser_UserId
            (userId, firstName, lastName, currentLocation, hometown, occupation, bio, birthday, gender);
        if (rowsUpdated > 0) {
            return getProfileByUserId(userId);
        } else {
            return Optional.empty();
        }
    }

    public Optional<UserWebsite> updateWebsiteByUserIdService(Long userId, String github, String linkedin, String portfolio, String twitter, String facebook, String instagram){
        int rowsUpdated = websiteJpaRepo.updateWebsiteByUser_UserId
            (userId, github, linkedin, portfolio,  twitter, facebook, instagram);
        if (rowsUpdated > 0) {
            return getWebsiteByUserId(userId);
        } else {
            return Optional.empty();
        }
    }
}
