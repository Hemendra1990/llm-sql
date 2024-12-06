package com.hemendra.llmsql.service;

import com.hemendra.llmsql.entity.*;
import com.hemendra.llmsql.repository.*;
import com.hemendra.llmsql.util.UserConfigDataGenerator;
import com.hemendra.llmsql.util.UserDataGenerator;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    private EntityManager entityManager;

    private static final int BATCH_SIZE = 1000;

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

        // Save users
        saveUsersInBatch(users);

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

    @Transactional
    public String setupReferenceData() {
        long start = System.currentTimeMillis();

        // Generate configuration data using the config generator
        List<Role> roles = configDataGenerator.generatePredefinedRoles();
        List<Profile> profiles = configDataGenerator.generatePredefinedProfiles();
        List<Department> departments = configDataGenerator.generatePredefinedDepartments();
        List<Designation> designations = configDataGenerator.generatePredefinedDesignations();

        // Save roles with hierarchy
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
    private void saveRolesWithHierarchy(List<Role> roles) {
        // First, save the admin role (parent)
        Role adminRole = roles.get(0);
        adminRole.setId(null); // Clear any pre-set ID
        adminRole = roleRepository.save(adminRole);
        log.info("Admin role saved with ID: {}", adminRole.getId());

        // Then save other roles with the saved admin role as parent
        for (int i = 1; i < roles.size(); i++) {
            Role role = roles.get(i);
            role.setId(null); // Clear any pre-set ID
            role.setParentRole(adminRole);
            Role savedRole = roleRepository.save(role);
            log.debug("Role saved: {} with ID: {}", savedRole.getLabel(), savedRole.getId());
        }

        log.info("All roles saved successfully with proper hierarchy");
    }

    @Transactional
    private <T> void saveEntitiesInBatch(List<T> entities, String entityType) {
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
    private void saveUsersInBatch(List<User> users) {
        int count = 0;
        for (User user : users) {
            user.setId(null); // Clear any pre-set ID
            entityManager.persist(user);
            count++;

            if (count % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
                log.info("Batch of {} users saved", BATCH_SIZE);
            }
        }

        // Flush remaining entities
        entityManager.flush();
        entityManager.clear();
        log.info("Total {} users saved", count);
    }
}