package com.zerolatency.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zerolatency.backend.model.UserWebsite;

import jakarta.transaction.Transactional;

@Repository
public interface websiteJpaRepo extends JpaRepository<UserWebsite, Long> {
    UserWebsite findByUser_UserId(Long userId);   

    @Modifying
    @Transactional
    @Query("""
        UPDATE UserWebsite uw 
        SET uw.github = ?2, 
            uw.linkedin = ?3, 
            uw.portfolio = ?4, 
            uw.twitter = ?5, 
            uw.facebook = ?6, 
            uw.instagram = ?7 
        WHERE uw.user.userId = ?1
    """)
    int updateWebsiteByUser_UserId
        (Long userId, String github, String linkedin, String portfolio, String twitter, String facebook, String instagram);
}
