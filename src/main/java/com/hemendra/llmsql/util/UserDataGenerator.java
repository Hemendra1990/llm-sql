package com.hemendra.llmsql.util;

import com.github.javafaker.Faker;
import com.hemendra.llmsql.entity.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Component
public class UserDataGenerator {
    private final List<Role> predefinedRoles;
    private final List<Profile> predefinedProfiles;
    private final List<Department> predefinedDepartments;
    private final List<Designation> predefinedDesignations;
    private final Faker faker;
    private final Random random;

    public UserDataGenerator() {
        this.faker = new Faker();
        this.random = new Random();
        this.predefinedRoles = generatePredefinedRoles();
        this.predefinedProfiles = generatePredefinedProfiles();
        this.predefinedDepartments = generatePredefinedDepartments();
        this.predefinedDesignations = generatePredefinedDesignations();
    }

    private List<Role> generatePredefinedRoles() {
        List<Role> roles = new ArrayList<>();
        // Create admin role as parent
        Role adminRole = new Role();
        adminRole.setId(UUID.randomUUID().toString());
        adminRole.setLabel("Administrator");
        adminRole.setRoleName("ADMIN");
        adminRole.setKcRoleId("KC_ADMIN");
        roles.add(adminRole);

        // Create other roles with admin as parent
        String[] roleNames = {"Manager", "Team Lead", "Developer", "HR", "Finance", "Sales", "Support", "Marketing"};
        for (String roleName : roleNames) {
            Role role = new Role();
            //role.setId(UUID.randomUUID().toString());
            role.setLabel(roleName);
            role.setRoleName(roleName.toUpperCase().replace(" ", "_"));
            role.setKcRoleId("KC_" + roleName.toUpperCase().replace(" ", "_"));
            role.setParentRole(adminRole);
            roles.add(role);
        }

        return roles;
    }

    private List<Profile> generatePredefinedProfiles() {
        List<Profile> profiles = new ArrayList<>();
        String[] profileNames = {
            "System Administrator", "Manager Profile", "Developer Profile", 
            "HR Profile", "Sales Profile", "Support Profile", "Guest Profile",
            "Analyst Profile", "Executive Profile", "Standard User"
        };

        for (String profileName : profileNames) {
            Profile profile = new Profile();
            //profile.setProfileId(UUID.randomUUID().toString());
            profile.setProfileName(profileName);
            profile.setDescription("Profile for " + profileName);
            profile.setIsAdministrator(profileName.contains("Administrator"));
            profiles.add(profile);
        }
        return profiles;
    }

    private List<Department> generatePredefinedDepartments() {
        List<Department> departments = new ArrayList<>();
        String[] departmentNames = {
            "Engineering", "Human Resources", "Finance", "Sales", 
            "Marketing", "Support", "Operations", "Research", 
            "Product Management", "Legal"
        };

        for (String deptName : departmentNames) {
            Department department = new Department();
            //department.setId(UUID.randomUUID().toString());
            department.setDepartmentName(deptName.toUpperCase().replace(" ", "_"));
            department.setDisplayName(deptName);
            department.setActive(true);
            departments.add(department);
        }
        return departments;
    }

    private List<Designation> generatePredefinedDesignations() {
        List<Designation> designations = new ArrayList<>();
        String[] designationNames = {
            "Senior Software Engineer", "Project Manager", "HR Manager",
            "Sales Executive", "Support Engineer", "Team Lead",
            "Product Owner", "Business Analyst", "Technical Architect",
            "Department Head"
        };

        for (String desigName : designationNames) {
            Designation designation = new Designation();
            //designation.setId(UUID.randomUUID().toString());
            designation.setDesignationName(desigName.toUpperCase().replace(" ", "_"));
            designation.setDisplayName(desigName);
            designation.setActive(true);
            designations.add(designation);
        }
        return designations;
    }

    public List<User> generateUsers(int count) {
        List<User> users = new ArrayList<>();
        Map<String, User> userMap = new HashMap<>();  // For handling reporting relationships

        for (int i = 0; i < count; i++) {
            User user = new User();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();

            //user.setId(UUID.randomUUID().toString());
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUsername(firstName.toLowerCase() + "." + lastName.toLowerCase());
            user.setEmail(user.getUsername() + "@" + faker.internet().domainName());
            user.setPersonalEmail(user.getUsername() + "@gmail.com");
            user.setNickName(firstName);
            user.setPassword(faker.internet().password(8, 20, true, true));
            
            // Personal details
            user.setTitle(faker.name().prefix());
            user.setPanNumber("PAN" + faker.numerify("########"));
            user.setAadhaarNumber(faker.numerify("############"));
            user.setBloodGroup(faker.options().option("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"));
            user.setEmergencyContact(faker.phoneNumber().cellPhone());
            user.setPhoneNumber(faker.phoneNumber().phoneNumber());
            user.setGender(faker.options().option("Male", "Female", "Other"));
            user.setMaritalStatus(faker.options().option("Single", "Married", "Divorced", "Widowed"));
            
            // Professional details
            user.setEmploymentType(faker.options().option("Full-time", "Part-time", "Contract", "Intern"));
            user.setMobileNumber(faker.phoneNumber().cellPhone());
            user.setAlternateNumber(faker.phoneNumber().cellPhone());
            user.setCompany(faker.company().name());
            user.setDivision(faker.commerce().department());
            
            // System access flags
            user.setMarketingUser(random.nextBoolean());
            user.setOfflineUser(random.nextBoolean());
            user.setKnowledgeUser(random.nextBoolean());
            user.setFlowUser(random.nextBoolean());
            user.setServiceCloudUser(random.nextBoolean());
            user.setWdcUser(random.nextBoolean());
            user.setCrmContentUser(random.nextBoolean());
            user.setAllowForecasting(random.nextBoolean());
            
            // Assignments from predefined data
            user.setRole(predefinedRoles.get(random.nextInt(predefinedRoles.size())));
            user.setProfile(predefinedProfiles.get(random.nextInt(predefinedProfiles.size())));
            user.setDepartment(predefinedDepartments.get(random.nextInt(predefinedDepartments.size())));
            user.setDesignation(predefinedDesignations.get(random.nextInt(predefinedDesignations.size())));
            
            // Location and preferences
            user.setTimeZone(faker.options().option("UTC", "IST", "PST", "EST", "GMT"));
            user.setLocale(faker.options().option("en_US", "en_UK", "en_IN", "fr_FR", "de_DE"));
            user.setLanguage(faker.options().option("English", "Hindi", "French", "German", "Spanish"));
            
            // Dates
            LocalDateTime now = LocalDateTime.now();
            user.setDateOfBirth(OffsetDateTime.of(
                now.minusYears(random.nextInt(20) + 20), 
                ZoneOffset.UTC
            ));
            user.setDateOfJoin(OffsetDateTime.of(
                now.minusYears(random.nextInt(5)), 
                ZoneOffset.UTC
            ));
            
            // Additional details
            user.setEmployeeCode("EMP" + faker.numerify("######"));
            user.setKcUserId("KC_" + UUID.randomUUID().toString());
            user.setIsActive(true);
            
            users.add(user);
            userMap.put(user.getId(), user);
        }
        
        // Set up reporting relationships
        for (User user : users) {
            if (!user.getRole().getLabel().equals("Administrator")) {
                int numberOfReports = random.nextInt(3); // 0-2 reporting relationships
                Set<User> reportsTo = new HashSet<>();
                for (int i = 0; i < numberOfReports; i++) {
                    User randomManager = users.get(random.nextInt(users.size()));
                    if (!randomManager.equals(user)) {
                        reportsTo.add(randomManager);
                    }
                }
                user.setReportsTo(reportsTo);
            }
        }

        return users;
    }

    // Getter methods for predefined data
    public List<Role> getPredefinedRoles() {
        return predefinedRoles;
    }

    public List<Profile> getPredefinedProfiles() {
        return predefinedProfiles;
    }

    public List<Department> getPredefinedDepartments() {
        return predefinedDepartments;
    }

    public List<Designation> getPredefinedDesignations() {
        return predefinedDesignations;
    }
}