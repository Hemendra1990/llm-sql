package com.hemendra.llmsql.repository.timesheet;

import com.hemendra.llmsql.entity.timesheet.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobCategoryRepository extends JpaRepository<JobCategory, String> {
    List<JobCategory> findAllByStatus(boolean status);
}
