package com.hemendra.llmsql.service;

import com.hemendra.llmsql.entity.*;
import com.hemendra.llmsql.keycloak.service.KeycloakService;
import com.hemendra.llmsql.repository.*;
import com.hemendra.llmsql.util.UserConfigDataGenerator;
import com.hemendra.llmsql.util.UserDataGenerator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProfileRepository profileRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final UserDataGenerator userDataGenerator;
    private final UserConfigDataGenerator configDataGenerator;
    private final KeycloakService keycloakService;
    private final OrganisationRepository organisationRepository;

    @Value("${spring.datasource.schema}")
    private String schemaName;

    @Autowired
    private EntityManager entityManager;

    private static final int BATCH_SIZE = 1000;

    private Organisation organisation;
    private User superAdmin;

    @PostConstruct
    public void init() {
        this.organisation = organisationRepository.findByNameIgnoreCase(schemaName)
                .orElseThrow(() -> new IllegalStateException("Organisation not found"));
        this.superAdmin = userRepository.findFirstByRole_RoleName("Super Admin")
                .orElseThrow(() -> new IllegalStateException("Super Admin user not found"));
        log.info("Initialized UserService with organisation: {} and superAdmin: {}",
                organisation.getName(), superAdmin.getUsername());
    }

    public String generateAndSaveUsers(Integer count) {
        long start = System.currentTimeMillis();

        // Check if reference data exists
        if (!isReferenceDataExists()) {
            setupReferenceData();
            log.info("Reference data was missing and has been created");
        }

        // Generate users
        List<User> users = userDataGenerator.generateUsers(count);
        long generationEnd = System.currentTimeMillis();
        log.info("Generated {} users in {} ms", users.size(), generationEnd - start);

        // Filter out users with existing usernames
        List<User> uniqueUsers = filterUniqueUsers(users);
        log.info("{} unique users after filtering existing usernames", uniqueUsers.size());

        // Save users
        saveUsersWithKeycloak(uniqueUsers);

        long end = System.currentTimeMillis();
        String summary = String.format("""
            User generation completed:
            - Total users created: %d
            - Generation time: %d ms
            - Total processing time: %d ms
            """,
                users.size(),
                (generationEnd - start),
                (end - start)
        );

        log.info(summary);
        return summary;
    }

    private List<User> filterUniqueUsers(List<User> users) {
        Set<String> existingUsernames = new HashSet<>(
                userRepository.findAllUsernames()
        );

        return users.stream()
                .filter(user -> !existingUsernames.contains(user.getUsername()))
                .collect(Collectors.toList());
    }

    @Transactional
    public String setupReferenceData() {
        long start = System.currentTimeMillis();

        // Generate configuration data using the config generator
        List<Role> roles = configDataGenerator.generatePredefinedRoles();
        List<Profile> profiles = configDataGenerator.generatePredefinedProfiles();
        List<Department> departments = configDataGenerator.generatePredefinedDepartments();
        List<Designation> designations = configDataGenerator.generatePredefinedDesignations();

        // Save roles with hierarchy and create in Keycloak
        saveRolesWithHierarchy(roles);

        // Save profiles
        saveEntitiesInBatch(profiles, "Profile");

        // Save departments
        saveEntitiesInBatch(departments, "Department");

        // Save designations
        saveEntitiesInBatch(designations, "Designation");

        long end = System.currentTimeMillis();

        String summary = String.format("""
            Reference data setup completed in %d ms:
            - Roles: %d
            - Profiles: %d
            - Departments: %d
            - Designations: %d
            """,
                (end - start),
                roles.size(),
                profiles.size(),
                departments.size(),
                designations.size()
        );

        log.info(summary);
        return summary;
    }

    private boolean isReferenceDataExists() {
        return roleRepository.count() > 0 &&
                profileRepository.count() > 0 &&
                departmentRepository.count() > 0 &&
                designationRepository.count() > 0;
    }

    @Transactional
    public void saveRolesWithHierarchy(List<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            log.info("No roles to save, all roles might already exist");
            return;
        }

        try {
            for (Role role : roles) {
                populateCommonFields(role);
                Role savedRole = roleRepository.save(role);
                // Create in Keycloak
                keycloakService.createKcRealmRole(savedRole, schemaName);
                log.debug("Role saved: {} with ID: {}, Parent Role: {}",
                        savedRole.getLabel(),
                        savedRole.getId(),
                        savedRole.getParentRole() != null ? savedRole.getParentRole().getRoleName() : "None");
            }

            log.info("All {} roles saved successfully with proper hierarchy", roles.size());
        } catch (Exception e) {
            log.error("Error while saving roles: {}", e.getMessage());
            throw e;
        }
    }

    @Transactional
    public  <T> void saveEntitiesInBatch(List<T> entities, String entityType) {
        int count = 0;
        for (T entity : entities) {
            // Clear any pre-set ID using reflection
            try {
                java.lang.reflect.Method setIdMethod = entity.getClass().getMethod("setId", String.class);
                setIdMethod.invoke(entity, (String)null);
            } catch (Exception e) {
                try {
                    java.lang.reflect.Method setIdMethod = entity.getClass().getMethod("setProfileId", String.class);
                    setIdMethod.invoke(entity, (String)null);
                } catch (Exception ex) {
                    log.warn("Could not clear ID for entity type: {}", entityType);
                }
            }

            // Populate common fields
            populateCommonFields(entity);

            entityManager.persist(entity);
            count++;

            if (count % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
                log.debug("Saved batch of {} {}", BATCH_SIZE, entityType);
            }
        }

        entityManager.flush();
        entityManager.clear();
        log.info("{} {} saved", count, entityType);
    }

    @Transactional
    private void saveUsersWithKeycloak(List<User> users) {
        int count = 0;
        for (User user : users) {
            try {
                user.setId(null); // Clear any pre-set ID
                populateCommonFields(user);
                entityManager.persist(user);
                count++;

                // Create user in Keycloak
                keycloakService.createKcUser(user, schemaName);

                if (count % BATCH_SIZE == 0) {
                    entityManager.flush();
                    entityManager.clear();
                    log.info("Batch of {} users saved", BATCH_SIZE);
                }
            } catch (Exception e) {
                log.error("Failed to create user {}: {}", user.getUsername(), e.getMessage());
            }
        }

        entityManager.flush();
        entityManager.clear();
        log.info("Total {} users saved", count);
    }

    /**
     * Helper method to populate audit and organization fields for any entity
     */
    private <T> void populateCommonFields(T entity) {
        try {
            Class<?> clazz = entity.getClass();

            // Set organization for all entities
            try {
                Field orgField = clazz.getDeclaredField("organisation");
                orgField.setAccessible(true);
                orgField.set(entity, organisation);
            } catch (NoSuchFieldException e) {
                log.warn("Organisation field not found in {}", clazz.getSimpleName());
            }

            // For entities other than Role and Profile, set additional audit fields
            if (!clazz.equals(Role.class) && !clazz.equals(Profile.class)) {
                // Set created by
                try {
                    Field createdByField = clazz.getDeclaredField("createdBy");
                    createdByField.setAccessible(true);
                    createdByField.set(entity, superAdmin.getId());
                } catch (NoSuchFieldException e) {
                    log.warn("CreatedBy field not found in {}", clazz.getSimpleName());
                }

                // Set updated by
                try {
                    Field updatedByField = clazz.getDeclaredField("updatedBy");
                    updatedByField.setAccessible(true);
                    updatedByField.set(entity, superAdmin.getId());
                } catch (NoSuchFieldException e) {
                    log.warn("UpdatedBy field not found in {}", clazz.getSimpleName());
                }

                // Set created on
                try {
                    Field createdOnField = clazz.getDeclaredField("createdOn");
                    createdOnField.setAccessible(true);
                    createdOnField.set(entity, OffsetDateTime.now());
                } catch (NoSuchFieldException e) {
                    log.warn("CreatedOn field not found in {}", clazz.getSimpleName());
                }

                // Set updated on
                try {
                    Field updatedOnField = clazz.getDeclaredField("updatedOn");
                    updatedOnField.setAccessible(true);
                    updatedOnField.set(entity, OffsetDateTime.now());
                } catch (NoSuchFieldException e) {
                    log.warn("UpdatedOn field not found in {}", clazz.getSimpleName());
                }
            }

        } catch (IllegalAccessException e) {
            log.error("Error setting common fields: {}", e.getMessage());
            throw new RuntimeException("Failed to set common fields", e);
        }
    }
}