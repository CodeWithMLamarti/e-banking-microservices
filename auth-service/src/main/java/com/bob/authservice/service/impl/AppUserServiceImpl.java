package com.bob.authservice.service.impl;

import com.bob.authservice.model.AppUser;
import com.bob.authservice.model.Role;
import com.bob.authservice.repository.AppUserRepo;
import com.bob.authservice.repository.RoleRepo;
import com.bob.authservice.service.AppUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepo appUserRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    public AppUserServiceImpl(AppUserRepo appUserRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.appUserRepo = appUserRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser addUser(String username, String email, String password, String confirmPassword) {
        if(password.equals(confirmPassword)) {
            AppUser appUser = new AppUser(null, username, email, passwordEncoder.encode(password), new ArrayList<>());
            return appUserRepo.save(appUser);
        }else {
            throw new IllegalArgumentException("Passwords do not match");
        }
    }

    public AppUser addUser(String username, String email, String password) {
        AppUser appUser = new AppUser(null, username, email, passwordEncoder.encode(password), new ArrayList<>());
        return appUserRepo.save(appUser);
    }

    @Override
    public List<AppUser> getAllAppUsers() {
        return appUserRepo.findAll();
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        AppUser appUser = appUserRepo.findByUsername(username);
        Role role = roleRepo.findByRoleName(roleName);

        appUser.getRoles().add(role);
//        appUserRepo.save(appUser);  //note: there is no need to save the modifications since we already annotated the class as @Transactional
    }

    @Override
    public void removeRoleFromAppUser(String username, String roleName) {
        AppUser appUser = appUserRepo.findByUsername(username);
        Role role = roleRepo.findByRoleName(roleName);

        appUser.getRoles().remove(role);
    }

    @Override
    public AppUser loadUserByUsername(String username) { // I have no idea why we implemented this here instead of creating a custom UserDetailsService class that implement the interface UserDetailsService
        return appUserRepo.findByUsername(username);
    }
}
