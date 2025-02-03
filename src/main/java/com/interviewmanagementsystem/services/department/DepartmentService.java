package com.interviewmanagementsystem.services.department;

import com.interviewmanagementsystem.dtos.departments.DepartmentCreateUpdateDTO;
import com.interviewmanagementsystem.dtos.departments.DepartmentDTO;
import com.interviewmanagementsystem.entities.Department;
import com.interviewmanagementsystem.mapper.DepartmentMapper;
import com.interviewmanagementsystem.repositories.IDepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentService implements IDepartmentService {
    private final IDepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentDTO create(DepartmentCreateUpdateDTO departmentDTO) {
        Department department = departmentMapper.toEntity(departmentDTO);
        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toDTO(savedDepartment);
    }

    @Override
    public DepartmentDTO update(UUID id, DepartmentCreateUpdateDTO departmentDTO) {
        Department department = departmentMapper.toEntity(departmentDTO);
        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toDTO(savedDepartment);
    }

    @Override
    public DepartmentDTO findById(UUID id) {
        return departmentRepository.findById(id)
                .map(departmentMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }

    @Override
    public List<DepartmentDTO> findAll() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        departmentRepository.deleteById(id);
    }

    @Override
    public DepartmentDTO findByName(String name) {
        return departmentMapper.toDTO(departmentRepository.findByName(name));
    }

}

