package com.hemendra.llmsql.dto.employee;

import com.hemendra.llmsql.entity.employee.EmployeeInfo;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ExperienceDto {
    private String id;
    private EmployeeInfo employeeInfo;
    private String companyName;
    private PositionDto position;
    private String description;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private Double sortOrder;
}
