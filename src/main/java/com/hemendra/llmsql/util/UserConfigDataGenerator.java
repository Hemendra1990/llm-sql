package com.hemendra.llmsql.util;

import com.github.javafaker.Faker;
import com.hemendra.llmsql.entity.*;
import com.hemendra.llmsql.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserConfigDataGenerator {
    private final Faker faker = new Faker();
    private final RoleRepository roleRepository;
    private final ProfileRepository profileRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;

    @Value("${spring.datasource.schema}")
    private String schemaName;

    // Generate predefined roles
    public List<Role> generatePredefinedRoles() {

        List<Role> roles = new ArrayList<>();

        // Fetch existing Super Admin role
        Role superAdminRole = roleRepository.findByRoleNameEqualsIgnoreCase("Super Admin")
                .orElseThrow(() -> new IllegalStateException("Super Admin role not found"));

        // Create admin role with Super Admin as parent
        if (roleRepository.findByRoleNameEqualsIgnoreCase("Admin").isEmpty()) {
            // Create Admin role
            Role adminRole = new Role();
            adminRole.setLabel("Administrator");
            adminRole.setRoleName("Admin");
            adminRole.setParentRole(superAdminRole);
            adminRole.setSubRoles(new ArrayList<>());
            roles.add(adminRole);

            // Create Manager role with Admin as parent
            if (roleRepository.findByRoleNameEqualsIgnoreCase("Manager").isEmpty()) {
                Role managerRole = new Role();
                managerRole.setLabel("Manager");
                managerRole.setRoleName("Manager");
                managerRole.setParentRole(adminRole);
                managerRole.setSubRoles(new ArrayList<>());
                roles.add(managerRole);
                adminRole.getSubRoles().add(managerRole);

                // Create other roles with Manager as parent
                String[] roleNames = {"Team Lead", "Developer", "HR", "Finance", "Sales", "Support", "Marketing"};
                for (String roleName : roleNames) {
                    String formattedRoleName = roleName.toUpperCase().replace(" ", "_");
                    if (roleRepository.findByRoleNameEqualsIgnoreCase(formattedRoleName).isEmpty()) {
                        Role role = new Role();
                        role.setLabel(roleName);
                        role.setRoleName(formattedRoleName);
                        role.setParentRole(managerRole);
                        role.setSubRoles(new ArrayList<>());
                        roles.add(role);

                        // Add to manager's sub-roles
                        managerRole.getSubRoles().add(role);
                    } else {
                        log.info("Role {} already exists, skipping creation", formattedRoleName);
                    }
                }
            } else {
                log.info("Manager role already exists, skipping creation of manager and its sub-roles");
            }

            // Update Super Admin's sub-roles
            if (superAdminRole.getSubRoles() == null) {
                superAdminRole.setSubRoles(new ArrayList<>());
            }
            superAdminRole.getSubRoles().add(adminRole);
        } else {
            log.info("Admin role already exists, skipping role creation");
        }

        return roles;
    }

    // Generate predefined profiles
    public List<Profile> generatePredefinedProfiles() {

        List<Profile> profiles = new ArrayList<>();
        String[] profileNames = {
            "Manager Profile", "Developer Profile",
            "HR Profile", "Sales Profile", "Support Profile", "Guest Profile",
            "Analyst Profile", "Executive Profile", "Standard User"
        };

        for (String profileName : profileNames) {
            if(!profileRepository.existsByProfileNameEqualsIgnoreCase(profileName)) {
                Profile profile = new Profile();
                profile.setProfileName(profileName);
                profile.setDescription("Profile for " + profileName);
                profile.setIsAdministrator(false);
                profiles.add(profile);
            }
        }
        return profiles;
    }

    // Generate predefined departments
    public List<Department> generatePredefinedDepartments() {
        List<Department> departments = new ArrayList<>();
        String[] departmentNames = {
            "Engineering", "Human Resources", "Finance", "Sales", 
            "Marketing", "Support", "Operations", "Research", 
            "Product Management", "Legal"
        };

        for (String deptName : departmentNames) {
            if(!departmentRepository.existsByDepartmentNameEqualsIgnoreCase(deptName)) {
                Department department = new Department();
                department.setDepartmentName(deptName.toUpperCase().replace(" ", "_"));
                department.setDisplayName(deptName);
                department.setActive(true);
                departments.add(department);
            }
        }
        return departments;
    }

    // Generate predefined designations
    public List<Designation> generatePredefinedDesignations() {
        List<Designation> designations = new ArrayList<>();
        String[] designationNames = {
            "Senior Software Engineer", "Project Manager", "HR Manager",
            "Sales Executive", "Support Engineer", "Team Lead",
            "Product Owner", "Business Analyst", "Technical Architect",
            "Department Head"
        };

        for (String desigName : designationNames) {
            if(!designationRepository.existsByDesignationNameEqualsIgnoreCase(desigName)) {
                Designation designation = new Designation();
                designation.setDesignationName(desigName.toUpperCase().replace(" ", "_"));
                designation.setDisplayName(desigName);
                designation.setActive(true);
                designations.add(designation);
            }
        }
        return designations;
    }
}