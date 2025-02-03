package com.interviewmanagementsystem.services.department;

import com.ninja_in_pyjamas.dtos.departments.DepartmentCreateUpdateDTO;
import com.ninja_in_pyjamas.dtos.departments.DepartmentDTO;

import java.util.List;
import java.util.UUID;

public interface IDepartmentService {
    DepartmentDTO create(DepartmentCreateUpdateDTO departmentDTO);

    DepartmentDTO update(UUID id, DepartmentCreateUpdateDTO departmentDTO);

    DepartmentDTO findById(UUID id);

    List<DepartmentDTO> findAll();

    void delete(UUID id);

    DepartmentDTO findByName(String name);
}

