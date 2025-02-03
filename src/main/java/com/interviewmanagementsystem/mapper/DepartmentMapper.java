package com.interviewmanagementsystem.mapper;


import com.interviewmanagementsystem.dtos.departments.DepartmentCreateUpdateDTO;
import com.interviewmanagementsystem.dtos.departments.DepartmentDTO;
import com.interviewmanagementsystem.entities.Department;
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
