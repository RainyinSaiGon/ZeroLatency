package com.zerolatency.backend.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerolatency.backend.dto.Response.fullInfoResponse;
import com.zerolatency.backend.model.UserDestination;
import com.zerolatency.backend.model.UserProfile;
import com.zerolatency.backend.model.UserWebsite;
import com.zerolatency.backend.repo.destinationJpaRepo;
import com.zerolatency.backend.repo.profileJpaRepo;
import com.zerolatency.backend.repo.websiteJpaRepo;

@Service
public class informationService {
    @Autowired profileJpaRepo profileJpaRepo;
    @Autowired websiteJpaRepo websiteJpaRepo;
    @Autowired destinationJpaRepo destinationJpaRepo;

    public Optional<UserProfile> getProfileByUserId(Long userId) {
        return Optional.ofNullable(profileJpaRepo.findByUser_UserId(userId));
    }

    public Optional<UserWebsite> getWebsiteByUserId(Long userId) {
        return Optional.ofNullable(websiteJpaRepo.findByUser_UserId(userId));
    }

    public Optional<UserDestination> getDestinationByUserId(Long userId) {
        return Optional.ofNullable(destinationJpaRepo.findByUser_UserId(userId));
    }

    public Optional<fullInfoResponse> getFullInfoByUserId(Long userId) {
        Optional<UserProfile> profileOpt = getProfileByUserId(userId);
        Optional<UserWebsite> websiteOpt = getWebsiteByUserId(userId);
        Optional<UserDestination> destinationOpt = getDestinationByUserId(userId);

        if (profileOpt.isPresent() || websiteOpt.isPresent() || destinationOpt.isPresent()) {
            fullInfoResponse fullInfo = new fullInfoResponse();
            profileOpt.ifPresent(fullInfo::setProfile);
            websiteOpt.ifPresent(fullInfo::setWebsite);
            destinationOpt.ifPresent(fullInfo::setDestination);
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
