package com.hemendra.llmsql.entity.employee;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "em_position", schema = "client1")
public class Position {
    @Id
    @Tsid
    @Column(length = 50)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

}
