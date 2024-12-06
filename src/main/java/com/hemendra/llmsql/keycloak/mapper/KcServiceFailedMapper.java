package com.hemendra.llmsql.keycloak.mapper;

import com.hemendra.llmsql.keycloak.model.dto.KcServiceFailedDTO;
import com.hemendra.llmsql.keycloak.model.KcServiceFailed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KcServiceFailedMapper implements BaseMapper<KcServiceFailedDTO, KcServiceFailed> {
    @Override
    public KcServiceFailedDTO toDto(KcServiceFailed kcServiceFailed) {
        KcServiceFailedDTO kcServiceFailedDTO = new KcServiceFailedDTO();
        kcServiceFailedDTO.setId(kcServiceFailed.getId());
        kcServiceFailedDTO.setRoleId(kcServiceFailed.getRoleId());
        kcServiceFailedDTO.setReasonOfFailure(kcServiceFailed.getReasonOfFailure());
        kcServiceFailedDTO.setUserId(kcServiceFailed.getUserId());
        kcServiceFailedDTO.setOrganisationId(kcServiceFailed.getOrganisationId());
        kcServiceFailedDTO.setSchemaName(kcServiceFailed.getSchemaName());
        kcServiceFailedDTO.setOperationType(kcServiceFailed.getOperationType());
        return kcServiceFailedDTO;
    }

    @Override
    public KcServiceFailed toEntity(KcServiceFailedDTO kcServiceFailedDTO) {
        KcServiceFailed kcServiceFailed = new KcServiceFailed();
        kcServiceFailed.setUserId(kcServiceFailedDTO.getUserId());
        kcServiceFailed.setReasonOfFailure(kcServiceFailedDTO.getReasonOfFailure());
        kcServiceFailed.setRoleId(kcServiceFailedDTO.getRoleId());
        kcServiceFailed.setOrganisationId(kcServiceFailedDTO.getOrganisationId());
        kcServiceFailed.setId(kcServiceFailedDTO.getId());
        kcServiceFailed.setSchemaName(kcServiceFailedDTO.getSchemaName());
        kcServiceFailed.setOperationType(kcServiceFailedDTO.getOperationType());
        return kcServiceFailed;
    }
}
