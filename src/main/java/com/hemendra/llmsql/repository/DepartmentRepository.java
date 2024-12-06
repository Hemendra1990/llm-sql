package com.hemendra.llmsql.repository;

import com.hemendra.llmsql.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, String> {
    boolean existsByDepartmentNameEqualsIgnoreCase(String departmentName);
}
