package com.hemendra.llmsql.repository.employee;

import com.hemendra.llmsql.entity.employee.EmployeeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeInfoRepository extends JpaRepository<EmployeeInfo, String> {
}
