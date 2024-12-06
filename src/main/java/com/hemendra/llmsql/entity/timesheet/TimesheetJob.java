package com.hemendra.llmsql.entity.timesheet;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hemendra.llmsql.entity.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ts_job")
@Getter
@Setter
public class TimesheetJob extends BaseEntity {

    @Id
    @Tsid
    @Column(length = 50)
    private String id;
    private String name;
    private String displayName;
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private JobCategory jobCategory;
    private Boolean isSystemJob;

}
