package com.hemendra.llmsql.repository.employee;

import com.hemendra.llmsql.entity.employee.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperienceRepository extends JpaRepository<Experience, String> {
}
