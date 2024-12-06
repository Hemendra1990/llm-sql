package com.hemendra.llmsql.util;

import com.github.javafaker.Faker;
import com.hemendra.llmsql.entity.Department;
import com.hemendra.llmsql.entity.Designation;
import com.hemendra.llmsql.entity.Profile;
import com.hemendra.llmsql.entity.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserConfigDataGenerator {
    private final Faker faker = new Faker();

    // Generate predefined roles
    public List<Role> generatePredefinedRoles() {
        List<Role> roles = new ArrayList<>();
        
        // Create admin role as parent
        Role adminRole = new Role();
        adminRole.setLabel("Administrator");
        adminRole.setRoleName("ADMIN");
        adminRole.setKcRoleId("KC_ADMIN");
        roles.add(adminRole);

        // Create other roles with admin as parent
        String[] roleNames = {"Manager", "Team Lead", "Developer", "HR", "Finance", "Sales", "Support", "Marketing"};
        for (String roleName : roleNames) {
            Role role = new Role();
            role.setLabel(roleName);
            role.setRoleName(roleName.toUpperCase().replace(" ", "_"));
            role.setKcRoleId("KC_" + roleName.toUpperCase().replace(" ", "_"));
            role.setParentRole(adminRole);
            roles.add(role);
        }

        return roles;
    }

    // Generate predefined profiles
    public List<Profile> generatePredefinedProfiles() {
        List<Profile> profiles = new ArrayList<>();
        String[] profileNames = {
            "System Administrator", "Manager Profile", "Developer Profile", 
            "HR Profile", "Sales Profile", "Support Profile", "Guest Profile",
            "Analyst Profile", "Executive Profile", "Standard User"
        };

        for (String profileName : profileNames) {
            Profile profile = new Profile();
            profile.setProfileName(profileName);
            profile.setDescription("Profile for " + profileName);
            profile.setIsAdministrator(profileName.contains("Administrator"));
            profiles.add(profile);
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
            Department department = new Department();
            department.setDepartmentName(deptName.toUpperCase().replace(" ", "_"));
            department.setDisplayName(deptName);
            department.setActive(true);
            departments.add(department);
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
            Designation designation = new Designation();
            designation.setDesignationName(desigName.toUpperCase().replace(" ", "_"));
            designation.setDisplayName(desigName);
            designation.setActive(true);
            designations.add(designation);
        }
        return designations;
    }
}