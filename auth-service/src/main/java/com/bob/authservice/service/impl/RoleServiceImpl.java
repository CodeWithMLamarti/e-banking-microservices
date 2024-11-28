package com.bob.authservice.service.impl;

import com.bob.authservice.model.Role;
import com.bob.authservice.repository.RoleRepo;
import com.bob.authservice.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepo roleRepo;

    public RoleServiceImpl(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    public Role addRole(String roleName) {
        Role role = new Role(null, roleName);
        return roleRepo.save(role);
    }
}
