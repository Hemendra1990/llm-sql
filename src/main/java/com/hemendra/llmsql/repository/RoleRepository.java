package com.hemendra.llmsql.repository;

import com.hemendra.llmsql.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, String> {
    List<Role> findByRoleNameNot(String roleName);
}
