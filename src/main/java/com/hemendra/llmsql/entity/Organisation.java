package com.hemendra.llmsql.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "organisation")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Organisation implements Serializable {
    @Id
    //@Tsid
    @Column(length = 50)
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "schema_name", nullable = false, unique = true, updatable = false, columnDefinition = "VARCHAR(255) DEFAULT ' '")
    private String schemaName;

    @Column(name = "logo", columnDefinition = "TEXT")
    private String logo;

    @Column(name = "time_zone")
    private String timeZone;

    @Column(name = "kc_client_id")
    private String kcClientId;

    private String dateFormat;
    private String timeFormat;

}
