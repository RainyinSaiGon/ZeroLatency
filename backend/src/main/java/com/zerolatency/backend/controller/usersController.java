package com.zerolatency.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zerolatency.backend.dto.usernameRequest;
import com.zerolatency.backend.model.users;
import com.zerolatency.backend.service.usersService;

@RestController
@RequestMapping("/api/users")
public class usersController {
    @Autowired
    private usersService userService;

    @GetMapping
    public users findByUsername(@RequestParam String username) {
        return userService.findByUsername(username);
    }

    @PostMapping
    public users findByUsernameFromBody(@RequestBody usernameRequest user) {
        return userService.findByUsername(user.getUsername());
    }
}
