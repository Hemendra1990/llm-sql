package com.hemendra.llmsql.service;

import com.hemendra.llmsql.entity.timesheet.JobCategory;
import com.hemendra.llmsql.entity.timesheet.Project;
import com.hemendra.llmsql.entity.timesheet.ProjectCategory;
import com.hemendra.llmsql.util.TimesheetConfigGenerator;
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
public class TimesheetConfigService {
    private final TimesheetConfigGenerator configGenerator;
    
    @Autowired
    private EntityManager entityManager;
    
    private static final int BATCH_SIZE = 1000;

    public void setupConfig(int count) {
        long start = System.currentTimeMillis();
        
        // Generate and save project categories
        List<ProjectCategory> projectCategories = configGenerator.generateProjectCategories(count);
        saveBatch(projectCategories);
        
        // Generate and save job categories with jobs
        List<JobCategory> jobCategories = configGenerator.generateJobCategories(count);
        saveBatch(jobCategories);
        
        // Generate and save projects
        List<Project> projects = configGenerator.generateProjects(count, projectCategories);
        saveBatch(projects);
        
        log.info("Config setup completed in {} ms", System.currentTimeMillis() - start);
    }

    private <T> void saveBatch(List<T> entities) {
        int count = 0;
        for (T entity : entities) {
            entityManager.persist(entity);
            count++;

            if (count % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
        log.info("Saved {} entities of type {}", count, entities.get(0).getClass().getSimpleName());
    }
}