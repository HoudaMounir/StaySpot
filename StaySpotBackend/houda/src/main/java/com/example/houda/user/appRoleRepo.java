package com.example.houda.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface appRoleRepo extends JpaRepository<appRole,Long> {
    appRole findByRoleName(String roleName);
}
