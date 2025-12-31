package com.zerolatency.backend.repo;

import org.springframework.stereotype.Repository;

import com.zerolatency.backend.model.users;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface usersRepo extends JpaRepository<users, Long> {
    public users findByUsername(String username);
}
