package com.hemendra.llmsql.entity.gen;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "campaign", schema = "kevit")
public class Campaign {
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

    @Column(name = "actual_cost")
    private Long actualCost;

    @Column(name = "budgeted_cost")
    private Long budgetedCost;

    @Column(name = "description")
    private String description;

    @Column(name = "end_date")
    private OffsetDateTime endDate;

    @Column(name = "expected_response")
    private Long expectedResponse;

    @Column(name = "expected_revenue")
    private Long expectedRevenue;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private OffsetDateTime startDate;

    @Column(name = "status")
    private String status;

    @Column(name = "type")
    private String type;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}