package com.zerolatency.backend.repo;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zerolatency.backend.model.UserProfile;

import jakarta.transaction.Transactional;

@Repository
public interface profileJpaRepo extends JpaRepository<UserProfile, Long>{
    UserProfile findByUser_UserId(Long userId);

    @Modifying
    @Transactional
    @Query("""
        UPDATE UserProfile up 
        SET up.firstName = ?2,
            up.lastName = ?3,
            up.currentLocation = ?4,
            up.hometown = ?5,
            up.occupation = ?6,
            up.bio = ?7,
            up.birthday = ?8,
            up.gender = ?9
        WHERE up.user.userId = ?1
            """)
    int updateProfileByUser_UserId
        (Long userId, String firstName, String lastName, String currentLocation, String hometown, String occupation, String bio, Date birthday, String gender);
        
    @Modifying
    @Transactional
    @Query("""
        UPDATE UserProfile up 
        SET up.avatarUrl = ?2
        WHERE up.user.userId = ?1
            """)
    int updateAvatarUrlByUser_UserId(Long userId, String avatarUrl);
}
