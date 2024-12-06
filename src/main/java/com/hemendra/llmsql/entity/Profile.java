package com.hemendra.llmsql.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "profile")
@Data
public class Profile {
    @Id
    @Tsid
    @Column(name = "id", length = 50)
    private String profileId;

    @Column(name="name")
    private String profileName;

    private String description;
    private Boolean isAdministrator;

    @ManyToOne
    @JoinColumn(name = "organisation_id", referencedColumnName = "id")
    private Organisation organisation;
}
