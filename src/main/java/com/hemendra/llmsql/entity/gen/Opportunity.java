package com.hemendra.llmsql.entity.gen;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "opportunity", schema = "kevit")
public class Opportunity {
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

    @Column(name = "amount")
    private Double amount;

    @Column(name = "amount_converted")
    private Double amountConverted;

    @Column(name = "amount_weighted_converted")
    private Double amountWeightedConverted;

    @Column(name = "close_date")
    private OffsetDateTime closeDate;

    @Column(name = "description")
    private String description;

    @Column(name = "lead_source")
    private String leadSource;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "next_step")
    private String nextStep;

    @Column(name = "probability")
    private Integer probability;

    @Column(name = "stage")
    private String stage;

    @Column(name = "type")
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}