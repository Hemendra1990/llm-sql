package com.hemendra.llmsql.dto.employee;

import com.hemendra.llmsql.dto.UserDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EmployeeInfoDto {
    private String id;

    private String employeeId;

    private String name;

    private String contact;

    private String emailId;

    private Boolean isActive;

    private List<ExperienceDto> experiences = new ArrayList<>();
    private UserDto user;
}
