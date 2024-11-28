package com.bob.authservice.service.impl;

import com.bob.authservice.model.AppUser;
import com.bob.authservice.model.Role;
import com.bob.authservice.service.AppUserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AppUserService appUserService;

    public CustomUserDetailsService(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserService.loadUserByUsername(username);
        if (appUser == null) {
            throw new UsernameNotFoundException(username);
        }
        String[] roles = appUser.getRoles().stream().map(Role::getRoleName).toArray(String[]::new);
        UserDetails principal = User.withUsername(username).password(appUser.getPassword()).roles(roles).build();
        System.out.printf("principal: %s ", principal);
        return principal;
    }
}
