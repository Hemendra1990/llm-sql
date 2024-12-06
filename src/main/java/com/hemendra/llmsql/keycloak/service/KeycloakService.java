package com.hemendra.llmsql.keycloak.service;


import com.hemendra.llmsql.entity.Role;
import com.hemendra.llmsql.entity.User;
import com.hemendra.llmsql.keycloak.mapper.KcServiceFailedMapper;
import com.hemendra.llmsql.keycloak.model.KcServiceFailed;
import com.hemendra.llmsql.keycloak.model.OprationType;
import com.hemendra.llmsql.keycloak.model.dto.KcServiceFailedDTO;
import com.hemendra.llmsql.keycloak.repository.KcServiceFailedRepository;
import com.hemendra.llmsql.repository.RoleRepository;
import com.hemendra.llmsql.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class KeycloakService {
    private final Keycloak keycloak;
    private final UserRepository userRepository;
    private final KcServiceFailedRepository kcServiceFailedRepository;
    private final KcServiceFailedMapper kcServiceFailedMapper;
    private final RoleRepository roleRepository;

    public void createKcUser(User user, String schemaName) {
        try {
            RealmResource realmResource = keycloak.realm(schemaName);
            UsersResource usersResource = realmResource.users();

            UserRepresentation kcUser = new UserRepresentation();
            setBasicInfoInKcUser(user, kcUser);

            setPasswordInKcUser(user, kcUser);
            kcUser.setGroups(List.of(schemaName));
            usersResource.create(kcUser);

            // Get the user id
            String kcUserId = usersResource.search(user.getUsername()).getFirst().getId();

            // Get role representations for the client roles
            List<RoleRepresentation> roleRepresentations = realmResource.roles().list();
            // Assign client roles to the user
            usersResource.get(kcUserId).roles().realmLevel()
                    .add(roleRepresentations.stream().filter(rr -> rr.getName().equals(user.getRole().getRoleName())).collect(Collectors.toList()));

            // Save User with Keycloak user id
            user.setKcUserId(kcUserId);
            userRepository.updateKcUserId(user.getId(), kcUserId);
        } catch (Exception e) {
            KcServiceFailedDTO kcServiceFailedDTO = new KcServiceFailedDTO();
            kcServiceFailedDTO.setUserId(user.getId());
            kcServiceFailedDTO.setSchemaName(schemaName);
            kcServiceFailedDTO.setReasonOfFailure(e.getMessage());
            kcServiceFailedDTO.setOperationType(OprationType.CREATE.toString());
            //Save failed data
            saveFailedServiceData(kcServiceFailedDTO);
            log.error("Failed to create Keycloak user. Email: {}, Reason: {}", user.getEmail(), e.getMessage());
        }
    }

    private static void setBasicInfoInKcUser(User user, UserRepresentation kcUser) {
        kcUser.setUsername(user.getUsername());
        kcUser.setFirstName(user.getFirstName());
        kcUser.setLastName(user.getLastName());
        kcUser.setEmail(user.getEmail());
        kcUser.setEmailVerified(true);
        kcUser.setEnabled(user.getIsActive());
        kcUser.setCreatedTimestamp(
                Optional.ofNullable(user.getCreatedOn()).map(co -> co.toInstant().toEpochMilli())
                        .orElse(OffsetDateTime.now().toInstant().toEpochMilli()));
    }

    private static void setPasswordInKcUser(User user, UserRepresentation kcUser) {
        CredentialRepresentation kcPassword = getCredentialRepresentation(user);
        kcUser.setCredentials(List.of(kcPassword));
    }

    @NotNull
    private static CredentialRepresentation getCredentialRepresentation(User user) {
        CredentialRepresentation kcPassword = new CredentialRepresentation();
        kcPassword.setCreatedDate(Optional.ofNullable(user.getCreatedOn()).map(co -> co.toInstant().toEpochMilli())
                .orElse(OffsetDateTime.now().toInstant().toEpochMilli()));

        kcPassword.setType(CredentialRepresentation.PASSWORD);
        kcPassword.setTemporary(false);

        kcPassword.setCredentialData("""
                {"hashIterations":-1,"algorithm":"bcrypt","additionalParameters":{}}
                """);
        kcPassword.setSecretData("""
                {"value":"%s","salt":"","additionalParameters":{}}
                """.formatted(user.getPassword()));
        return kcPassword;
    }

    public void saveFailedServiceData(KcServiceFailedDTO kcServiceFailedDTO) {
        KcServiceFailed entity = kcServiceFailedMapper.toEntity(kcServiceFailedDTO);
        kcServiceFailedRepository.save(entity);
    }

    public void createKcRealmRole(Role role, String schemaName) {
        try {
            RealmResource realmResource = keycloak.realm(schemaName);
            GroupsResource groupsResource = realmResource.groups();
            RoleRepresentation kcClientRole = new RoleRepresentation();
            kcClientRole.setName(role.getRoleName());
            kcClientRole.setDescription(role.getLabel());
            // Set parent_role_id attributes only if parentRoleId is not null
            if (role.getParentRole() != null) {
                kcClientRole.setAttributes(Map.of("parent_role_id", List.of(String.valueOf(role.getParentRole().getId()))));
            }

            // Set sub_roles attribute only if subRoles is not null
            if (role.getSubRoles() != null) {
                List<String> subRoleIds = role.getSubRoles().stream().map(subRole -> String.valueOf(subRole.getId())).collect(Collectors.toList());
                kcClientRole.setAttributes(Map.of("sub_roles", subRoleIds));
            }
            //create client role
            RolesResource roles = realmResource.roles();
            roles.create(kcClientRole);

            //Get the client Role id
            RoleRepresentation representation = roles.get(kcClientRole.getName()).toRepresentation();
            String kcRoleId = representation.getId();
            role.setKcRoleId(kcRoleId);
            roleRepository.save(role);

            // Assign roles to the group
            GroupRepresentation kcGroup = realmResource.groups().groups().stream()
                    .filter(gr -> StringUtils.equals(gr.getName(), schemaName)).findFirst().orElse(null);

            if (Objects.nonNull(kcGroup)) {
                GroupResource groupResource = groupsResource.group(kcGroup.getId());
                groupResource.roles().realmLevel().add(List.of(representation));
            }
        } catch (Exception e) {
            KcServiceFailedDTO kcServiceFailedDTO = new KcServiceFailedDTO();
            kcServiceFailedDTO.setSchemaName(schemaName);
            kcServiceFailedDTO.setRoleId(role.getId());
            kcServiceFailedDTO.setReasonOfFailure(e.getMessage());
            kcServiceFailedDTO.setOperationType(OprationType.CREATE.toString());
            //Save failed data
            saveFailedServiceData(kcServiceFailedDTO);
            log.error("Failed to create Keycloak client role . Role Name: {}, Reason: {}", role.getRoleName(), e.getMessage());
        }
    }
}
