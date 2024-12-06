package com.hemendra.llmsql.keycloak.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KcServiceFailedDTO implements Serializable {

    private String id;
    private String roleId;
    private String organisationId;
    private String userId;
    private String reasonOfFailure;
    private String schemaName;
    private String operationType;
}
