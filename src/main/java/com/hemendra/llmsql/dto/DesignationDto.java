package com.hemendra.llmsql.dto;

import lombok.Data;

@Data
public class DesignationDto {
    private String id;
    private String designationName;
    private String displayName;
    private boolean active;
}
