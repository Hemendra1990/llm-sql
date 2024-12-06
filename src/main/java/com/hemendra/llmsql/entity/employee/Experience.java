package com.hemendra.llmsql.entity.employee;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Table(name = "em_experience")
public class Experience {

    @Id
    @Tsid
    @Column(length = 50)
    private String id;

    @ManyToOne
    @JoinColumn(name = "employee_info_id", nullable = false)
    private EmployeeInfo employeeInfo;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "start_date", nullable = false)
    private OffsetDateTime startDate;

    @Column(name = "end_date")
    private OffsetDateTime endDate;

    private Double sortOrder;
}
