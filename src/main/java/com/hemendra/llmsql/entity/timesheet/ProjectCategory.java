package com.hemendra.llmsql.entity.timesheet;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ts_project_category")
@Getter
@Setter
public class ProjectCategory {

    @Id
    @Tsid
    private String id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String displayName;
    private Boolean status;

    @Column(columnDefinition = "text")
    private String description;
}
