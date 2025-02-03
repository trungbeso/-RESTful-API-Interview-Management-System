package com.interviewmanagementsystem.mapper;


import com.ninja_in_pyjamas.dtos.role.RoleCreateUpdateDTO;
import com.ninja_in_pyjamas.dtos.role.RoleDTO;
import com.ninja_in_pyjamas.dtos.role.RoleMasterDTO;
import com.ninja_in_pyjamas.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {
    RoleDTO toDTO(Role entity);

    @Mapping(target = "insertedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    RoleMasterDTO toMasterDTO(Role entity);

    Role toEntity(RoleCreateUpdateDTO dto);
}
