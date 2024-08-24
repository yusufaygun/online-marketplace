package com.example.online_marketplace.service;

import com.example.online_marketplace.model.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {

    Role saveRole(Role role);

    List<Role> findAllRoles();

    Role findRoleByName(String name);

    void deleteRole(Long id);

}
