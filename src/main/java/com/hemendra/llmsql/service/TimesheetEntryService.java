package com.hemendra.llmsql.service;

import com.hemendra.llmsql.entity.User;
import com.hemendra.llmsql.entity.timesheet.Timesheet;
import com.hemendra.llmsql.repository.UserRepository;
import com.hemendra.llmsql.util.TimesheetEntryGenerator;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TimesheetEntryService {
    private final TimesheetEntryGenerator entryGenerator;
    private final UserRepository userRepository;
    
    @Autowired
    private EntityManager entityManager;
    
    private static final int BATCH_SIZE = 1000;

    public void generateWeeklyTimesheets(int weeksBack) {
        long start = System.currentTimeMillis();
        
        // Get all active users
        List<User> users = userRepository.findByRole_RoleNameNot("Super Admin");
        
        // Generate timesheets for each user
        List<Timesheet> timesheets = entryGenerator.generateWeeklyTimesheets(users, weeksBack);
        saveTimesheetsInBatch(timesheets);
        
        log.info("Generated weekly timesheets for {} users in {} ms", 
                users.size(), System.currentTimeMillis() - start);
    }

    private void saveTimesheetsInBatch(List<Timesheet> timesheets) {
        int count = 0;
        for (Timesheet timesheet : timesheets) {
            entityManager.persist(timesheet);
            count++;

            if (count % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
        log.info("Saved {} timesheets", count);
    }
}