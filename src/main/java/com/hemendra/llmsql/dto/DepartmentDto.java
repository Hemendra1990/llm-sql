package com.hemendra.llmsql.dto;

import lombok.Data;

@Data
public class DepartmentDto {
    private String id;
    private String departmentName;
    private String displayName;
    private boolean active;
}
