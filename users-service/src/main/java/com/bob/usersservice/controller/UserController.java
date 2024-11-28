package com.bob.usersservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createUser(){
        return "Creating a new user";
    }


}
