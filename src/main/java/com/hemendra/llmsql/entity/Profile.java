package com.hemendra.llmsql.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "profile", schema = "client1")
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
}
