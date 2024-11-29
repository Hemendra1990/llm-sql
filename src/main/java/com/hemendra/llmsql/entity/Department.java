package com.hemendra.llmsql.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "department", schema = "client1")
@Data
public class Department {
    @Id
    @Tsid
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "name")
    private String departmentName;

    private String displayName;

    private boolean active;
}
