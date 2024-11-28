package com.hemendra.llmsql.entity.gen;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "opportunity_product", schema = "kevit")
public class OpportunityProduct {
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

    @Column(name = "date")
    private OffsetDateTime date;

    @Column(name = "description")
    private String description;

    @Column(name = "list_price")
    private String listPrice;

    @Column(name = "name")
    private String name;

    @Column(name = "pricebook_entries_id", length = 50)
    private String pricebookEntriesId;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "product_id", length = 50)
    private String productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "sale_price")
    private Double salePrice;

    @Column(name = "total_price")
    private Double totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opportunity_id")
    private Opportunity opportunity;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}