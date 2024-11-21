package com.hemendra.llmsql.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "lead")
@Data
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @Column(name = "email", length = 150, nullable = false, unique = false)
    private String email;

    @Column(name = "phone", length = 40, nullable = true)
    private String phone;

    @Column(name = "address", length = 1000)
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "zip_code", length = 40)
    private String zipCode;

    @Column(name = "company_name", length = 150)
    private String companyName;

    @Column(name = "job_title", length = 100)
    private String jobTitle;

    @Column(name = "industry", length = 100)
    private String industry;

    @Column(name = "website", length = 200)
    private String website;

    @Column(name = "lead_source", length = 100)
    private String leadSource;

    @Column(name = "lead_status", length = 50)
    private String leadStatus;

    @Column(name = "rating", length = 10)
    private String rating;

    @Column(name = "annual_revenue")
    private Double annualRevenue;

    @Column(name = "number_of_employees")
    private Integer numberOfEmployees;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "opt_in", nullable = false)
    private Boolean optIn;

    @Column(name = "preferred_language", length = 50)
    private String preferredLanguage;

    @Column(name = "timezone", length = 50)
    private String timezone;

    @Column(name = "marketing_channel", length = 100)
    private String marketingChannel;

    @Column(name = "referral_source", length = 100)
    private String referralSource;

    @Column(name = "campaign_name", length = 150)
    private String campaignName;

    @Column(name = "priority", length = 50)
    private String priority;

    @Column(name = "assigned_to", length = 100)
    private String assignedTo;

    @Column(name = "last_contacted_date")
    private LocalDateTime lastContactedDate;

    @Column(name = "custom_field_1", length = 255)
    private String customField1;

    @Column(name = "custom_field_2", length = 255)
    private String customField2;

    public Lead() {

    }
}
