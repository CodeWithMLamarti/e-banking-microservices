package com.bob.authservice.service;

import com.bob.authservice.model.AppUser;

import java.util.List;

public interface AppUserService {
    AppUser addUser(String username, String email, String password, String confirmPassword);
    AppUser addUser(String username, String email, String password);
//    AppUser updateAppUser(AppUser appUser);
    List<AppUser> getAllAppUsers();
    void addRoleToUser(String username, String roleName);
    void removeRoleFromAppUser(String username, String roleName);
    AppUser loadUserByUsername(String username);
}
