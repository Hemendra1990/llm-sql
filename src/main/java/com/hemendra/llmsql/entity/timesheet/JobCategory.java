package com.hemendra.llmsql.entity.timesheet;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hemendra.llmsql.entity.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ts_job_category")
@Getter
@Setter
public class JobCategory extends BaseEntity {
    @Id
    @Tsid
    @Column(length = 50)
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String displayName;
    private Boolean status;

    @OneToMany(mappedBy = "jobCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<TimesheetJob> jobList = new ArrayList<>();

}
