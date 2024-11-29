package com.hemendra.llmsql.repository;

import com.hemendra.llmsql.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
