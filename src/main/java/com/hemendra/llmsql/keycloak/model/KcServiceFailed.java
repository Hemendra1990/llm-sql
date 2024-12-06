package com.hemendra.llmsql.keycloak.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "kc_service_failed")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KcServiceFailed {

    @Id
    @Tsid
    @Column(length = 50)
    private String id;

    @Column(name="role_id")
    private String roleId;

    @Column(name = "organisation_id")
    private String organisationId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "reason_of_failure")
    private String reasonOfFailure;

    @Column(name = "schema_name")
    private String schemaName;

    @Column(name = "opration_type")
    private String operationType;
}
