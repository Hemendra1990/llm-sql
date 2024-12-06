package com.hemendra.llmsql.repository;

import com.hemendra.llmsql.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    List<Role> findByRoleNameNot(String roleName);

    Optional<Role> findByRoleNameEqualsIgnoreCase(String roleName);
}
