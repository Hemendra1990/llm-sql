package com.hemendra.llmsql.util;

import com.github.javafaker.Faker;
import com.hemendra.llmsql.entity.*;
import com.hemendra.llmsql.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDataGenerator {
    private final Faker faker = new Faker();
    private final Random random = new Random();

    private final RoleRepository roleRepository;
    private final ProfileRepository profileRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final UserRepository userRepository;
    private final OrganisationRepository organisationRepository;

    private static final int MAX_ADMIN_USERS = 3;

    private static final String DEFAULT_PASSWORD = "test";

    @Value("${spring.datasource.schema}")
    private String schemaName;

    public List<User> generateUsers(int count) {
        // Fetch all configuration data from repositories
        List<Role> allRoles = roleRepository.findByRoleNameNot("Super Admin");
        List<Profile> allProfiles = profileRepository.findAll();
        List<Department> departments = departmentRepository.findAll();
        List<Designation> designations = designationRepository.findAll();

        Organisation organisation = organisationRepository.findByNameIgnoreCase(schemaName)
                .orElseThrow(() -> new IllegalArgumentException("No organisation matching found for " + schemaName));

        // Validate configuration data
        validateConfigData(allRoles, allProfiles, departments, designations);

        // Get specific profiles based on names
        Profile adminProfile = allProfiles.stream()
                .filter(profile -> "Admin Profile".equalsIgnoreCase(profile.getProfileName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Admin Profile not found"));

        Profile managerProfile = allProfiles.stream()
                .filter(profile -> "Manager Profile".equalsIgnoreCase(profile.getProfileName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Manager Profile not found"));

        List<Profile> otherProfiles = allProfiles.stream()
                .filter(profile -> !profile.getProfileName().equalsIgnoreCase("Admin Profile")
                        && !profile.getProfileName().equalsIgnoreCase("Manager Profile"))
                .toList();

        // Get roles
        Role adminRole = allRoles.stream()
                .filter(role -> "ADMIN".equalsIgnoreCase(role.getRoleName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Admin role not found"));

        Role managerRole = allRoles.stream()
                .filter(role -> "MANAGER".equalsIgnoreCase(role.getRoleName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Manager role not found"));

        // Get existing user counts
        long existingUserCount = userRepository.count();
        long existingAdminCount = userRepository.countByRole(adminRole);
        long existingManagerCount = userRepository.countByRole(managerRole);
        long existingOtherCount = existingUserCount - (existingAdminCount + existingManagerCount);

        // Calculate total target count including existing users
        int totalTargetCount = (int) (existingUserCount + count);

        // Calculate target numbers based on total
        int targetAdminCount = (int) Math.round(totalTargetCount * random.nextDouble(0.01, 0.05)); // 1-5%
        int targetManagerCount = (int) Math.round(totalTargetCount * random.nextDouble(0.10, 0.20)); // 10-20%

        // Ensure admin count doesn't exceed maximum
        targetAdminCount = Math.min(targetAdminCount, 50);

        // Calculate how many new users of each type to create
        int newAdminCount = Math.max(0, Math.min(targetAdminCount - (int)existingAdminCount, count));
        int newManagerCount = Math.max(0, Math.min(targetManagerCount - (int)existingManagerCount, count - newAdminCount));
        int newOtherCount = count - (newAdminCount + newManagerCount);

        log.info("Current user distribution - Admin: {}, Manager: {}, Other: {}",
                existingAdminCount, existingManagerCount, existingOtherCount);
        log.info("New users to create - Admin: {}, Manager: {}, Other: {}",
                newAdminCount, newManagerCount, newOtherCount);

        List<User> newUsers = new ArrayList<>();

        // Create new admin users
        for (int i = 0; i < newAdminCount; i++) {
            User adminUser = generateUser(adminRole, adminProfile, departments, designations, organisation);
            newUsers.add(adminUser);
        }

        // Create new manager users
        for (int i = 0; i < newManagerCount; i++) {
            User managerUser = generateUser(managerRole,
                    managerProfile,
                    departments, designations, organisation);
            newUsers.add(managerUser);
        }

        // Create new other users
        Role defaultRole = allRoles.stream()
                .filter(role -> !"ADMIN".equalsIgnoreCase(role.getRoleName()) && !"MANAGER".equalsIgnoreCase(role.getRoleName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No default role found"));

        for (int i = 0; i < newOtherCount; i++) {
            User user = generateUser(defaultRole,
                    otherProfiles.get(random.nextInt(otherProfiles.size())),
                    departments, designations, organisation);
            newUsers.add(user);
        }

        // Setup reporting relationships
        setupReportingRelationships(newUsers);

        log.info("Final user distribution - Admin: {}, Manager: {}, Other: {}",
                existingAdminCount + newAdminCount,
                existingManagerCount + newManagerCount,
                existingOtherCount + newOtherCount);

        return newUsers;
    }

    private void validateConfigData(List<Role> roles, List<Profile> profiles,
                                    List<Department> departments, List<Designation> designations) {
        if (roles.isEmpty()) {
            throw new IllegalStateException("No roles found in database");
        }
        if (profiles.isEmpty()) {
            throw new IllegalStateException("No profiles found in database");
        }
        if (departments.isEmpty()) {
            throw new IllegalStateException("No active departments found in database");
        }
        if (designations.isEmpty()) {
            throw new IllegalStateException("No active designations found in database");
        }
    }

    private User generateUser(Role role, Profile profile,
                              List<Department> departments, List<Designation> designations, Organisation organisation) {
        User user = new User();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        // Basic information
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(generateUsername(firstName, lastName)+"@"+organisation.getName()+".com");
        user.setEmail(generateUsername(firstName, lastName)+"@"+organisation.getName()+".com");
        user.setPersonalEmail(user.getUsername() + "@gmail.com");
        user.setNickName(firstName);
        //user.setPassword(DEFAULT_PASSWORD);
        user.setPassword(BCrypt.hashpw(DEFAULT_PASSWORD, BCrypt.gensalt()));

        // Personal details
        populatePersonalDetails(user);

        // Professional details
        populateProfessionalDetails(user, organisation);

        // System access flags
        populateSystemAccessFlags(user);

        // Location and preferences
        populateLocationPreferences(user);

        // Dates and additional details
        populateDatesAndDetails(user);

        // Set the provided role and profile
        user.setRole(role);
        user.setProfile(profile);
        user.setDepartment(departments.get(random.nextInt(departments.size())));
        user.setDesignation(designations.get(random.nextInt(designations.size())));

        return user;
    }

    private void populatePersonalDetails(User user) {
        user.setTitle(faker.name().prefix());
        user.setPanNumber("PAN" + faker.numerify("########"));
        user.setAadhaarNumber(faker.numerify("############"));
        user.setBloodGroup(faker.options().option("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"));
        user.setEmergencyContact(faker.phoneNumber().cellPhone());
        user.setPhoneNumber(faker.phoneNumber().phoneNumber());
        user.setGender(faker.options().option("male", "female", "other"));
        user.setMaritalStatus(faker.options().option("single", "married", "divorced", "widowed"));
    }

    private void populateProfessionalDetails(User user, Organisation organisation) {
        user.setEmploymentType(faker.options().option("permanent", "outsourcing", "contractual"));
        user.setMobileNumber(faker.phoneNumber().cellPhone());
        user.setAlternateNumber(faker.phoneNumber().cellPhone());
        user.setCompany(organisation.getName());
        user.setDivision(faker.commerce().department());
    }

    private void populateSystemAccessFlags(User user) {
        user.setMarketingUser(random.nextBoolean());
        user.setOfflineUser(random.nextBoolean());
        user.setKnowledgeUser(random.nextBoolean());
        user.setFlowUser(random.nextBoolean());
        user.setServiceCloudUser(random.nextBoolean());
        user.setWdcUser(random.nextBoolean());
        user.setCrmContentUser(random.nextBoolean());
        user.setAllowForecasting(random.nextBoolean());
    }

    private void populateLocationPreferences(User user) {
        user.setTimeZone(faker.options().option("IST"));
        user.setLocale(faker.options().option("en_US"));
        user.setLanguage(faker.options().option("English"));
    }

    private void populateDatesAndDetails(User user) {
        LocalDateTime now = LocalDateTime.now();
        user.setDateOfBirth(OffsetDateTime.of(
                now.minusYears(random.nextInt(20) + 20),
                ZoneOffset.UTC
        ));
        user.setDateOfJoin(OffsetDateTime.of(
                now.minusYears(random.nextInt(5)),
                ZoneOffset.UTC
        ));

        user.setEmployeeCode("EMP" + faker.numerify("######"));
        //user.setKcUserId("KC_" + UUID.randomUUID().toString());
        user.setIsActive(true);
    }

    private void setupReportingRelationships(List<User> newUsers) {
        // First, find the Super Admin
        User superAdmin = userRepository.findFirstByRole_RoleName("Super Admin")
                .orElseThrow(() -> new IllegalStateException("Super Admin not found"));

        // Get all existing admin and manager users from database
        List<User> existingAdmins = userRepository.findFirstByRole_RoleNameEqualsIgnoreCase("ADMIN");
        List<User> existingManagers = userRepository.findFirstByRole_RoleNameEqualsIgnoreCase("MANAGER");

        // Separate new users by role
        List<User> newAdminUsers = newUsers.stream()
                .filter(user -> "ADMIN".equalsIgnoreCase(user.getRole().getRoleName()))
                .toList();

        List<User> newManagerUsers = newUsers.stream()
                .filter(user -> "MANAGER".equalsIgnoreCase(user.getRole().getRoleName()))
                .toList();

        List<User> otherUsers = newUsers.stream()
                .filter(user -> !("ADMIN".equalsIgnoreCase(user.getRole().getRoleName()) ||
                        "MANAGER".equalsIgnoreCase(user.getRole().getRoleName())))
                .toList();

        // Combine existing and new users
        List<User> allAdmins = new ArrayList<>(existingAdmins);
        allAdmins.addAll(newAdminUsers);

        List<User> allManagers = new ArrayList<>(existingManagers);
        allManagers.addAll(newManagerUsers);

        // Set up all admin users reporting to Super Admin
        for (User admin : allAdmins) {
            Set<User> reportsTo = new HashSet<>();
            reportsTo.add(superAdmin);
            admin.setReportsTo(reportsTo);
            log.debug("Admin user {} reports to Super Admin", admin.getUsername());
        }

        // Set up all manager users reporting to admins
        if (!allAdmins.isEmpty()) {
            for (User manager : allManagers) {
                // Distribute managers evenly among admins
                User adminUser = allAdmins.get(allManagers.indexOf(manager) % allAdmins.size());
                Set<User> reportsTo = new HashSet<>();
                reportsTo.add(adminUser);
                manager.setReportsTo(reportsTo);
                log.debug("Manager {} reports to Admin {}", manager.getUsername(), adminUser.getUsername());
            }
        } else {
            log.warn("No admin users found to assign as managers' reporting managers");
        }

        // Set up other users reporting to managers
        if (!allManagers.isEmpty()) {
            for (User user : otherUsers) {
                // Distribute users evenly among managers
                User manager = allManagers.get(otherUsers.indexOf(user) % allManagers.size());
                Set<User> reportsTo = new HashSet<>();
                reportsTo.add(manager);
                user.setReportsTo(reportsTo);
                log.debug("User {} reports to Manager {}", user.getUsername(), manager.getUsername());
            }
        } else {
            log.warn("No manager users found to assign as users' reporting managers");
        }

        log.info("Set up reporting relationships: {} total admins reporting to Super Admin, {} total managers reporting to admins, {} new users reporting to managers",
                allAdmins.size(), allManagers.size(), otherUsers.size());
    }

    private String generateUsername(String firstName, String lastName) {
        return (firstName.toLowerCase() + "." + lastName.toLowerCase())
                .replaceAll("[^a-zA-Z0-9.]", "");
    }
}