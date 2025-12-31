package com.zerolatency.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerolatency.backend.model.users;
import com.zerolatency.backend.repo.usersRepo;

@Service
public class usersService {
    @Autowired
    private usersRepo userRepo;

    public users findByUsername(String username) {
        return userRepo.findByUsername(username);
    }
}
