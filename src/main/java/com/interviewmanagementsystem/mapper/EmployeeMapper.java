package com.interviewmanagementsystem.mapper;

import com.ninja_in_pyjamas.dtos.employees.response.EmployeeInformationDTO;
import com.ninja_in_pyjamas.entities.Employee;
import com.ninja_in_pyjamas.entities.Role;
import com.ninja_in_pyjamas.enums.RoleName;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {
  @Mapping(target = "roles", source = "roles")
  EmployeeInformationDTO toDTO(Employee entity);

  Employee toEntity(EmployeeInformationDTO dto);

  default Set<RoleName> mapRoles(Set<Role> roles) {
      return roles.stream()
              .map(Role::getName)
              .collect(Collectors.toSet());
  }
}
