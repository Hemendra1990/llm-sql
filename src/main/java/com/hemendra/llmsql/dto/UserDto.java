package com.hemendra.llmsql.dto;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Set;

@Data
public class UserDto {
    private String id;

    private String username;

    private String password;

    private String email;

    private String personalEmail;

    private String firstName;

    private String lastName;

    private String nickName;

    private String title;

    private String panNumber;

    private String aadhaarNumber;

    private String bloodGroup;

    private String emergencyContact;

    private String phoneNumber;

    private String gender;

    private String maritalStatus;

    private String employmentType;

    private String fax;

    private String mobileNumber;

    private String alternateNumber;

    private String company;

    private String division;

    private Boolean marketingUser;

    private Boolean offlineUser;

    private Boolean knowledgeUser;

    private Boolean flowUser;

    private Boolean serviceCloudUser;

    private Boolean wdcUser;

    private Boolean crmContentUser;

    private Boolean allowForecasting;

    private RoleDto role;

    private ProfileDto profile;

    private String timeZone;

    private String locale;

    private String federationId;

    private String language;

    private UserDto delegatedApproverId;

    private Set<UserDto> reportsTo;
    private Boolean isActive;

    private OffsetDateTime dateOfBirth;

    private OffsetDateTime dateOfJoin;

    private OffsetDateTime dateOfDeparture;

    private DepartmentDto department;

    private DesignationDto designation;

    private String employeeCode;
    private String kcUserId;

}
