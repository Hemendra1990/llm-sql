package com.hemendra.llmsql.entity.gen;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "contact")
public class Contact {
    @Id
    @Tsid
    private String id;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "created_on")
    private OffsetDateTime createdOn;

    @Column(name = "organisation_id", length = 50)
    private String organisationId;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "updated_on")
    private OffsetDateTime updatedOn;

    @Column(name = "account_is_inactive")
    private Boolean accountIsInactive;

    @Column(name = "account_role")
    private String accountRole;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "state")
    private String state;

    @Column(name = "street")
    private String street;

    @Column(name = "assistant")
    private String assistant;

    @Column(name = "asst_phone")
    private Long asstPhone;

    @Column(name = "birth_date")
    private OffsetDateTime birthDate;

    @Column(name = "department")
    private String department;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "do_not_call")
    private Boolean doNotCall;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "fax")
    private String fax;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "lead_source")
    private String leadSource;

    @Column(name = "mobile")
    private Long mobile;

    @Column(name = "name")
    private String name;

    @Column(name = "opportunity_role")
    private String opportunityRole;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "salutation_name")
    private String salutationName;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}