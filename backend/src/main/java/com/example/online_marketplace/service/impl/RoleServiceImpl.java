package com.example.online_marketplace.service.impl;

import com.example.online_marketplace.exception.EntityNotFoundException;
import com.example.online_marketplace.model.Role;
import com.example.online_marketplace.repository.RoleRepository;
import com.example.online_marketplace.service.RoleService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @PostConstruct
    public void initRoles() {
        List<String> roleNames = Arrays.asList("user", "admin");
        for (String roleName : roleNames) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        }
    }

    public Role findRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Role not found."));
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll().stream().toList();
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
