package com.hemendra.llmsql.repository;

import com.hemendra.llmsql.entity.Role;
import com.hemendra.llmsql.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, String> {
    Page<User> findByIdNotIn(Set<String> userIds, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.id NOT IN (SELECT e.user.id FROM EmployeeInfo e) ORDER BY u.id")
    Page<User> findUsersNotEmployees(Pageable pageable);

    @Query("SELECT u FROM User u WHERE (NOT EXISTS (SELECT e FROM EmployeeInfo e)) OR u.id NOT IN (SELECT e.user.id FROM EmployeeInfo e) ORDER BY u.id")
    Page<User> findUsersNotEmployeesOrNoEmployees(Pageable pageable);

    List<User> findAllByIsActiveTrue();

    Optional<User> findFirstByRole_RoleName(String roleName);

    List<User> findByRole_RoleNameNot(String roleName);

    long countByRole(Role role);
}
