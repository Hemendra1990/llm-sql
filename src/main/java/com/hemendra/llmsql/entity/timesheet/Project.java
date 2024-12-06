package com.hemendra.llmsql.entity.timesheet;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hemendra.llmsql.entity.BaseEntity;
import com.hemendra.llmsql.entity.User;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "ts_project", schema = "client1")
@Getter
@Setter
@NoArgsConstructor
public class Project extends BaseEntity {

    public Project(String id) {
        this.id = id;
    }

    @Id
    @Tsid
    @Column(length = 50)
    private String id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String displayName;

    @Column(columnDefinition = "text")
    private String description;

    private OffsetDateTime startDate;
    private OffsetDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "manager_id") // Foreign key to represent the manager relationship
    private User manager;

    @JsonManagedReference
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "ts_project_user_xref",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> teamMembers;

    @JsonManagedReference
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "ts_project_job_xref",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "job_id")
    )
    private List<TimesheetJob> jobList;
    private String projectStatus;// Active, InActive, OnHold
    private String customerName;
    @Column(columnDefinition = "text")
    private String customerLocation;
    private String module;
    private String projectType;// Billable, NonBillable, Internal, Administration
    @ManyToOne
    private ProjectCategory projectCategory;
    private Boolean isSystemProject;

}
