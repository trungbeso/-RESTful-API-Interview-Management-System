package com.interviewmanagementsystem.mapper;

import com.ninja_in_pyjamas.dtos.departments.DepartmentCreateUpdateDTO;
import com.ninja_in_pyjamas.dtos.departments.DepartmentDTO;
import com.ninja_in_pyjamas.entities.Department;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepartmentMapper {
    public DepartmentDTO toDTO(Department department);

    public Department toEntity(DepartmentCreateUpdateDTO departmentDTO);

    void updateEntity(DepartmentDTO dto, @MappingTarget Department department);
}
