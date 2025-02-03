package com.interviewmanagementsystem.services.department;


import com.interviewmanagementsystem.dtos.departments.DepartmentCreateUpdateDTO;
import com.interviewmanagementsystem.dtos.departments.DepartmentDTO;

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

