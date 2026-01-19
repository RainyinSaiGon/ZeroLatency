package com.zerolatency.backend.repo;

import org.springframework.stereotype.Repository;

import com.zerolatency.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    public User findByUsername(String username);

    public User findByEmail(String email);

    public Boolean existsByUsername(String username);

    public Boolean existsByEmail(String email);
}
