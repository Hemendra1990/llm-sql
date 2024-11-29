package com.hemendra.llmsql.service;

import com.hemendra.llmsql.entity.User;
import com.hemendra.llmsql.entity.employee.EmployeeInfo;
import com.hemendra.llmsql.entity.employee.Position;
import com.hemendra.llmsql.repository.UserRepository;
import com.hemendra.llmsql.repository.employee.EmployeeInfoRepository;
import com.hemendra.llmsql.repository.employee.PositionRepository;
import com.hemendra.llmsql.util.EmployeeDataGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeInfoRepository employeeInfoRepository;
    private final PositionRepository positionRepository;
    private final UserRepository userRepository;
    private final EmployeeDataGenerator employeeDataGenerator;
    
    @Autowired
    private EntityManager entityManager;

    private static final int BATCH_SIZE = 1000;

    @Transactional
    public String setupReferenceData() {
        long start = System.currentTimeMillis();
        
        // Generate and save positions
        List<Position> positions = employeeDataGenerator.generatePredefinedPositions();
        for (Position position : positions) {
            position.setId(null); // Clear any pre-set ID
            positionRepository.save(position);
        }

        long end = System.currentTimeMillis();
        String summary = String.format("Reference data setup completed in %d ms: Positions: %d", 
            (end - start), positions.size());
        
        log.info(summary);
        return summary;
    }

    @Transactional
    public String generateAndSaveEmployees(Integer count) {
        long start = System.currentTimeMillis();

        // Check if reference data exists
        if (positionRepository.count() == 0) {
            setupReferenceData();
            log.info("Position reference data was missing and has been created");
        }

        // Get all positions
        List<Position> positions = positionRepository.findAll();

        // Get users who are not yet employees
        Set<String> existingEmployeeUserIds = employeeInfoRepository.findAll().stream()
            .map(emp -> emp.getUser().getId())
            .collect(Collectors.toSet());

        List<User> availableUsers = userRepository
                .findUsersNotEmployeesOrNoEmployees(PageRequest.of(0, count))
                .getContent();

        if (availableUsers.isEmpty()) {
            return "No available users found to create employees. Please create more users first.";
        }

        int employeesToCreate = Math.min(count, availableUsers.size());
        List<EmployeeInfo> employees = new ArrayList<>();

        // Generate employee data
        for (int i = 0; i < employeesToCreate; i++) {
            EmployeeInfo employeeInfo = employeeDataGenerator.generateEmployeeInfo(
                availableUsers.get(i),
                positions
            );
            employees.add(employeeInfo);
        }

        // Save employees in batches
        saveEmployeesInBatch(employees);

        long end = System.currentTimeMillis();
        String summary = String.format("""
            Employee generation completed:
            - Total employees created: %d
            - Processing time: %d ms
            """,
            employees.size(),
            (end - start)
        );

        log.info(summary);
        return summary;
    }

    @Transactional
    private void saveEmployeesInBatch(List<EmployeeInfo> employees) {
        int count = 0;
        for (EmployeeInfo employee : employees) {
            employee.setId(null); // Clear any pre-set ID
            entityManager.persist(employee);
            count++;

            if (count % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
                log.info("Batch of {} employees saved", BATCH_SIZE);
            }
        }

        entityManager.flush();
        entityManager.clear();
        log.info("Total {} employees saved", count);
    }
}