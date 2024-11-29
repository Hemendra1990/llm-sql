package com.hemendra.llmsql.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "designation", schema = "client1")
@Data
public class Designation {
    @Id
    @Tsid
    @Column(name = "id", length = 50)
    private String id;

    @Column(name="name")
    private String designationName;

    private String displayName;

    private boolean active;
}
