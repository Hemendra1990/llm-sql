package com.hemendra.llmsql.dto;

import lombok.Data;

import java.util.List;
@Data
public class RoleDto {
    private String id;

    private String label;

    private String roleName;

    private RoleDto parentRole;

    private List<RoleDto> subRoles;

    private String kcRoleId;
}
