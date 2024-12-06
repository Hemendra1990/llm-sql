package com.hemendra.llmsql.repository.timesheet;

import com.hemendra.llmsql.entity.timesheet.TimesheetJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimesheetJobRepository extends JpaRepository<TimesheetJob, String> {
    List<TimesheetJob> findAllByStatusTrue();
}
