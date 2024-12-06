package com.hemendra.llmsql.repository.timesheet;

import com.hemendra.llmsql.entity.timesheet.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, String> {
    List<Project> findAllByProjectStatus(String projectStatus);
}
