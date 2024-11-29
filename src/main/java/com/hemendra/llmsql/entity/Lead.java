package com.hemendra.llmsql.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "lead", schema = "client1")
@Data
public class Lead {

    @Id
    @Tsid
    private String id;

    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    /*@Column(name = "email", length = 150, nullable = false, unique = false)
    private String email;*/

    /*@Column(name = "phone", length = 40, nullable = true)
    private String phone;*/

   /* @Column(name = "address", length = 1000)
    private String address;*/

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "country", length = 100)
    private String country;

    /*@Column(name = "zip_code", length = 40)
    private String zipCode;*/

    /*@Column(name = "company_name", length = 150)
    private String company;*/

    /*@Column(name = "job_title", length = 100)
    private String jobTitle;*/

    @Column(name = "industry", length = 100)
    private String industry;

    @Column(name = "website", length = 200)
    private String website;

    /*@Column(name = "lead_source", length = 100)
    private String leadSource;*/

    @Column(name = "lead_status", length = 50)
    private String leadStatus;

    @Column(name = "rating", length = 10)
    private String rating;

    @Column(name = "annual_revenue")
    private Double annualRevenue;

    /*@Column(name = "no_of_employee")
    private Integer numberOfEmployees;*/

    /*@Column(name = "notes", columnDefinition = "TEXT")
    private String notes;*/

    @Column(name = "created_on", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_on")
    private LocalDateTime updatedDate;

    /*@Column(name = "opt_in", nullable = false)
    private Boolean optIn;*/

    /*@Column(name = "preferred_language", length = 50)
    private String preferredLanguage;*/

    /*@Column(name = "timezone", length = 50)
    private String timezone;*/

    /*@Column(name = "marketing_channel", length = 100)
    private String marketingChannel;*/

    /*@Column(name = "referral_source", length = 100)
    private String referralSource;*/

    /*@Column(name = "campaign_name", length = 150)
    private String campaignName;*/

    /*@Column(name = "priority", length = 50)
    private String priority;*/

    /*@Column(name = "assigned_to", length = 100)
    private String assignedTo;*/

    /*@Column(name = "last_contacted_date")
    private LocalDateTime lastContactedDate;*/


    @Column(name = "created_by", length = 50)
    private String createdBy;

    /*@Column(name = "created_on")
    private OffsetDateTime createdOn;*/

    /*@Column(name = "organisation_id", length = 50)
    private String organisationId;*/

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    /*@Column(name = "updated_on")
    private OffsetDateTime updatedOn;*/

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "company", nullable = false)
    private String company;

    @Column(name = "converted_at")
    private OffsetDateTime convertedAt;

    @Column(name = "description")
    private String description;

    @Column(name = "do_not_call")
    private Boolean doNotCall;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "fax")
    private String fax;

    @ColumnDefault("false")
    @Column(name = "is_converted")
    private Boolean isConverted;

    @Column(name = "lat_long")
    private String latLong;

    @Column(name = "mobile")
    private Long mobile;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "no_of_employee")
    private Integer noOfEmployee;

    @Column(name = "opportunity_amount")
    private Double opportunityAmount;

    @Column(name = "opportunity_amount_converted")
    private Double opportunityAmountConverted;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "salutation_name")
    private String salutationName;

    @Column(name = "source")
    private String source;

    @Column(name = "street")
    private String street;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public Lead() {

    }
}
