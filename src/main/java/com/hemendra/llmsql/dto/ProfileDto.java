package com.hemendra.llmsql.dto;

import lombok.Data;

@Data
public class ProfileDto {
    private String profileId;
    private String profileName;
    private String description;
    private Boolean isAdministrator;
}
