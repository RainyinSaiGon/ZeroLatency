package com.zerolatency.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zerolatency.backend.model.UserDestination;
import com.zerolatency.backend.model.Users;

import jakarta.transaction.Transactional;


@Repository
public interface destinationJpaRepo extends JpaRepository<UserDestination, Long> {
    UserDestination findByUser(Users user);
    UserDestination findByUser_UserId(Long userId);

    @Modifying
    @Transactional
    @Query("""
        UPDATE UserDestination ud 
        SET ud.kindergarten = ?2, 
            ud.primarySchool = ?3, 
            ud.middleSchool = ?4, 
            ud.highSchool = ?5, 
            ud.collegeUniversity = ?6 
        WHERE ud.user.userId = ?1
    """)
    int updateDestinationByUser_UserId
    (Long userId, String kindergarten, String primarySchool, String middleSchool, String highSchool, String collegeUniversity);
}
