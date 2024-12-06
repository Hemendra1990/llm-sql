package com.hemendra.llmsql.util;

import com.github.javafaker.Faker;
import com.hemendra.llmsql.entity.*;
import com.hemendra.llmsql.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
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

    private static final int MAX_ADMIN_USERS = 3;

    public List<User> generateUsers(int count) {
        // Fetch all configuration data from repositories
        List<Role> allRoles = roleRepository.findByRoleNameNot("Super Admin");
        List<Profile> allProfiles = profileRepository.findAll();
        List<Department> departments = departmentRepository.findAll();
        List<Designation> designations = designationRepository.findAll();

        // Validate configuration data
        validateConfigData(allRoles, allProfiles, departments, designations);

        // Get count of existing admin users
        Role adminRole = allRoles.stream()
                .filter(role -> "ADMIN".equals(role.getRoleName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Admin role not found"));

        long existingAdminCount = userRepository.countByRole(adminRole);
        int remainingAdminSlots = MAX_ADMIN_USERS - (int)existingAdminCount;

        // Separate admin role and system admin profile
        Profile systemAdminProfile = allProfiles.stream()
                .filter(profile -> "System Administrator".equals(profile.getProfileName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("System Administrator profile not found"));

        List<Profile> nonAdminProfiles = allProfiles.stream()
                .filter(profile -> !"System Administrator".equals(profile.getProfileName()))
                .toList();

        List<Role> nonAdminRoles = allRoles.stream()
                .filter(role -> !"ADMIN".equals(role.getRoleName()))
                .toList();

        List<User> users = new ArrayList<>();
        Map<String, User> userMap = new HashMap<>();

        // First create admin users if slots are available
        if (remainingAdminSlots > 0) {
            int adminUsersToCreate = Math.min(remainingAdminSlots, count);
            for (int i = 0; i < adminUsersToCreate; i++) {
                User adminUser = generateUser(adminRole, systemAdminProfile, departments, designations);
                users.add(adminUser);
                userMap.put(adminUser.getId(), adminUser);
                count--; // Reduce the remaining count
            }
            log.info("Created {} admin users out of available {} slots", adminUsersToCreate, remainingAdminSlots);
        } else {
            log.info("Maximum admin users ({}) already exist, skipping admin user creation", MAX_ADMIN_USERS);
        }

        // Create remaining non-admin users
        for (int i = 0; i < count; i++) {
            User user = generateUser(
                    nonAdminRoles.get(random.nextInt(nonAdminRoles.size())),
                    nonAdminProfiles.get(random.nextInt(nonAdminProfiles.size())),
                    departments,
                    designations
            );
            users.add(user);
            userMap.put(user.getId(), user);
        }

        // Setup reporting relationships
        setupReportingRelationships(users);

        return users;
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
                              List<Department> departments, List<Designation> designations) {
        User user = new User();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        // Basic information
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(generateUsername(firstName, lastName));
        user.setEmail(generateEmail(user.getUsername()));
        user.setPersonalEmail(user.getUsername() + "@gmail.com");
        user.setNickName(firstName);
        user.setPassword("test123");

        // Personal details
        populatePersonalDetails(user);

        // Professional details
        populateProfessionalDetails(user);

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

        // ... rest of the user generation code ...
        return user;
    }

    private void populatePersonalDetails(User user) {
        user.setTitle(faker.name().prefix());
        user.setPanNumber("PAN" + faker.numerify("########"));
        user.setAadhaarNumber(faker.numerify("############"));
        user.setBloodGroup(faker.options().option("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"));
        user.setEmergencyContact(faker.phoneNumber().cellPhone());
        user.setPhoneNumber(faker.phoneNumber().phoneNumber());
        user.setGender(faker.options().option("Male", "Female", "Other"));
        user.setMaritalStatus(faker.options().option("Single", "Married", "Divorced", "Widowed"));
    }

    private void populateProfessionalDetails(User user) {
        user.setEmploymentType(faker.options().option("Full-time", "Part-time", "Contract", "Intern"));
        user.setMobileNumber(faker.phoneNumber().cellPhone());
        user.setAlternateNumber(faker.phoneNumber().cellPhone());
        user.setCompany(faker.company().name());
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
        user.setTimeZone(faker.options().option("UTC", "IST", "PST", "EST", "GMT"));
        user.setLocale(faker.options().option("en_US", "en_UK", "en_IN", "fr_FR", "de_DE"));
        user.setLanguage(faker.options().option("English", "Hindi", "French", "German", "Spanish"));
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
        user.setKcUserId("KC_" + UUID.randomUUID().toString());
        user.setIsActive(true);
    }

    private void setupReportingRelationships(List<User> users) {
        // Get all admin and manager users
        List<User> adminUsers = users.stream()
                .filter(user -> "ADMIN".equals(user.getRole().getRoleName()))
                .toList();

        List<User> managerUsers = users.stream()
                .filter(user -> "MANAGER".equals(user.getRole().getRoleName()))
                .toList();

        // First, set up managers reporting to admins
        if (!adminUsers.isEmpty()) {
            for (User manager : managerUsers) {
                // Each manager reports to a random admin
                User adminUser = adminUsers.get(random.nextInt(adminUsers.size()));
                Set<User> reportsTo = new HashSet<>();
                reportsTo.add(adminUser);
                manager.setReportsTo(reportsTo);
            }
        }

        // Then, set up other users reporting to managers
        List<User> otherUsers = users.stream()
                .filter(user -> !("ADMIN".equals(user.getRole().getRoleName()) ||
                        "MANAGER".equals(user.getRole().getRoleName())))
                .toList();

        if (!managerUsers.isEmpty()) {
            for (User user : otherUsers) {
                // Each user reports to a random manager
                User manager = managerUsers.get(random.nextInt(managerUsers.size()));
                Set<User> reportsTo = new HashSet<>();
                reportsTo.add(manager);
                user.setReportsTo(reportsTo);
            }
        }

        log.info("Set up reporting relationships: {} managers reporting to admins, {} users reporting to managers",
                managerUsers.size(), otherUsers.size());
    }

    private String generateUsername(String firstName, String lastName) {
        return (firstName.toLowerCase() + "." + lastName.toLowerCase())
                .replaceAll("[^a-zA-Z0-9.]", "");
    }

    private String generateEmail(String username) {
        return username + "@" + faker.internet().domainName();
    }
}