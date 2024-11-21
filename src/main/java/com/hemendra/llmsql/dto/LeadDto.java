package com.hemendra.llmsql.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LeadDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String address;

    private String city;

    private String state;

    private String country;

    private String zipCode;

    private String companyName;

    private String jobTitle;

    private String industry;

    private String website;

    private String leadSource;

    private String leadStatus;

    private String rating;

    private Double annualRevenue;

    private Integer numberOfEmployees;

    private String notes;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private Boolean optIn;

    private String preferredLanguage;

    private String timezone;

    private String marketingChannel;

    private String referralSource;

    private String campaignName;

    private String priority;

    private String assignedTo;

    private LocalDateTime lastContactedDate;

    private String customField1;

    private String customField2;
}
