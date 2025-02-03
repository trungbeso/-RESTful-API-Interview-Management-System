package com.interviewmanagementsystem.services.employee;

import com.ninja_in_pyjamas.dtos.employees.request.ChangePasswordDTO;
import com.ninja_in_pyjamas.dtos.employees.request.EmployeeCreationRequest;
import com.ninja_in_pyjamas.dtos.employees.request.EmployeeUpdateRequest;
import com.ninja_in_pyjamas.dtos.employees.response.EmployeeMasterDTO;
import com.ninja_in_pyjamas.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IEmployeeService {
	List<EmployeeMasterDTO> getAll();

	EmployeeMasterDTO findById(UUID id);

	Employee findByEmail(String email);

	void save(Employee employee);

	List<EmployeeMasterDTO> findByKeyword(String keyword);

	Page<EmployeeMasterDTO> findByPaginated(String keyword, Pageable pageable);

	EmployeeMasterDTO create(EmployeeCreationRequest request);

	EmployeeMasterDTO update(UUID id, EmployeeUpdateRequest request);

	boolean delete(UUID id);

	boolean changePassword(ChangePasswordDTO request);
}
