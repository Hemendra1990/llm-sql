package com.hemendra.llmsql.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "role", schema = "client1")
@Getter
@Setter
@ToString(exclude = {"parentRole", "subRoles"})
@EqualsAndHashCode(exclude = {"parentRole", "subRoles"})
public class Role {
    @Id
    @Tsid
    @Column(length = 50)
    private String id;

    @Column(name = "label")
    private String label;

    @Column(name = "name")
    private String roleName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_role_id")
    private Role parentRole;

    @OneToMany(mappedBy = "parentRole", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Role> subRoles;

    private String kcRoleId;
}
