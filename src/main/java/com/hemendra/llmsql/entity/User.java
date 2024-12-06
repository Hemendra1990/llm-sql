package com.hemendra.llmsql.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "crm_user", schema = "client1")
@Getter
@Setter
@ToString(exclude = {"reportsTo", "delegatedApproverId"})
@EqualsAndHashCode(exclude = {"reportsTo", "delegatedApproverId"})
public class User {
    @Id
    @Tsid
    @Column(length = 50)
    private String id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = true)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "personal_email")
    private String personalEmail;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @Column(name = "title")
    private String title;

    @Column(name = "pan_number")
    private String panNumber;

    @Column(name = "aadhaar_number")
    private String aadhaarNumber;

    @Column(name = "blood_group")
    private String bloodGroup;

    @Column(name = "emergency_contact")
    private String emergencyContact;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "gender")
    private String gender;

    @Column(name = "marital_Status")
    private String maritalStatus;

    @Column(name = "employment_type")
    private String employmentType;

    @Column(name = "fax")
    private String fax;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "alternate_number")
    private String alternateNumber;

    @Column(name = "company")
    private String company;

    @Column(name = "division")
    private String division;

    @Column(name = "marketing_user")
    private Boolean marketingUser;

    @Column(name = "offline_user")
    private Boolean offlineUser;

    @Column(name = "knowledge_user")
    private Boolean knowledgeUser;

    @Column(name = "flow_user")
    private Boolean flowUser;

    @Column(name = "service_cloud_user")
    private Boolean serviceCloudUser;

    @Column(name = "wdc_user")
    private Boolean wdcUser;

    @Column(name = "crm_content_user")
    private Boolean crmContentUser;

    @Column(name = "allow_forecasting")
    private Boolean allowForecasting;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    @Column(name = "time_zone")
    private String timeZone;

    @Column(name = "locale")
    private String locale;

    @Column(name = "federation_iD")
    private String federationId;

    @Column(name = "language")
    private String language;

    @OneToOne
    @JoinColumn(name = "delegated_approver_id", referencedColumnName = "id", nullable = true)
    private User delegatedApproverId;

    @ManyToMany
    @JoinTable(name = "user_reporting",
            joinColumns = @JoinColumn(name = "reporter_id"),
            inverseJoinColumns = @JoinColumn(name = "reported_to_id"))
    private Set<User> reportsTo;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "date_of_birth")
    private OffsetDateTime dateOfBirth;

    @Column(name = "date_of_joining")
    private OffsetDateTime dateOfJoin;

    @Column(name = "date_of_departure")
    private OffsetDateTime dateOfDeparture;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "designation_id", referencedColumnName = "id")
    private Designation designation;

    private String employeeCode;
    private String kcUserId;

}
