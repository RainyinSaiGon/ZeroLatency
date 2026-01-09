package com.zerolatency.backend.repo;

import org.springframework.stereotype.Repository;

import com.zerolatency.backend.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface usersRepo extends JpaRepository<Users, Long> {
    public Users findByUserId(Long userId);
    public Users findByUsername(String username);
}
