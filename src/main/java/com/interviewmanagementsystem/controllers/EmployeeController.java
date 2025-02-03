package com.interviewmanagementsystem.controllers;

import com.ninja_in_pyjamas.dtos.employees.request.ChangePasswordDTO;
import com.ninja_in_pyjamas.dtos.employees.request.EmployeeCreationRequest;
import com.ninja_in_pyjamas.dtos.employees.request.EmployeeUpdateRequest;
import com.ninja_in_pyjamas.dtos.employees.response.EmployeeMasterDTO;
import com.ninja_in_pyjamas.mapper.CustomPagedResponse;
import com.ninja_in_pyjamas.services.employee.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employees", description = "APIs for managing employees")
public class EmployeeController {
	private final EmployeeService employeeService;
	private final PagedResourcesAssembler<EmployeeMasterDTO> pagedResourcesAssembler;

	public EmployeeController(EmployeeService employeeService, PagedResourcesAssembler<EmployeeMasterDTO> pagedResourcesAssembler) {
		this.employeeService = employeeService;
		this.pagedResourcesAssembler = pagedResourcesAssembler;
	}


	@Operation(summary = "Get all employees")
	@ApiResponse(responseCode = "200", description = "Return all users")
	@GetMapping
	public ResponseEntity<List<EmployeeMasterDTO>> getAll() {
		var employees = employeeService.getAll();
		return ResponseEntity.ok().body(employees);
	}

	@GetMapping("/searchByKeyword")
	@Operation(summary = "Search employee list by keyword")
	@ApiResponse(responseCode = "200", description = "Return employee list that match the key word")
	public ResponseEntity<List<EmployeeMasterDTO>> findByName(@RequestParam(required = false) String keyword) {
		var employees = employeeService.findByKeyword(keyword);
		return ResponseEntity.ok().body(employees);
	}

	@GetMapping("/search")
	@Operation(summary = "Search employee list with pagination")
	@ApiResponse(responseCode = "200", description = "Return employee list that match the keyword with pagination")
	public ResponseEntity<?> searchPaginated(
		  @RequestParam(required = false) String keyword,
		  @RequestParam(defaultValue = "username") String sortBy,
		  @RequestParam(defaultValue = "asc") String order,
		  @RequestParam(defaultValue = "0") int page,
		  @RequestParam(defaultValue = "10") int size) {
		Pageable pageable = null;

		if (order.equals("asc")) {
			pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
		} else {
			pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
		}

		//from pageModel, get data, page, link
		var employees = employeeService.findByPaginated(keyword, pageable);

		var pagedModel = pagedResourcesAssembler.toModel(employees);

		Collection<EntityModel<EmployeeMasterDTO>> entityModels = pagedModel.getContent();

		var links = pagedModel.getLinks();

		var response = new CustomPagedResponse<>(entityModels, pagedModel.getMetadata(), links);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get employee by id")
	@ApiResponse(responseCode = "200", description = "Return employee that match the id")
	public ResponseEntity<EmployeeMasterDTO> getById(@RequestParam UUID id) {
		var employee = employeeService.findById(id);
		return ResponseEntity.ok().body(employee);
	}

	@PostMapping()
	@Operation(summary = "Create new employee")
	@ApiResponse(responseCode = "200", description = "Return created employee")
	@ApiResponse(responseCode = "400", description = "Return error message if failed")
	public ResponseEntity<?> create(@Valid @RequestBody EmployeeCreationRequest request,
	                                BindingResult bindingResult) {
		System.out.println(request);
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
		}

		var newEmployee = employeeService.create(request);

		if (newEmployee == null) {
			return ResponseEntity.badRequest().body("Employee creation failed");
		}

		return ResponseEntity.ok().body(newEmployee);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update employee by id")
	@ApiResponse(responseCode = "200", description = "Return updated employee")
	@ApiResponse(responseCode = "400", description = "Return error message if failed")
	public ResponseEntity<?> update(@Valid @RequestBody EmployeeUpdateRequest request,
	                                @PathVariable UUID id,
	                                BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
		}

		var updatedEmployee = employeeService.update(id, request);

		if (updatedEmployee == null) {
			return ResponseEntity.badRequest().body("Employee creation failed");
		}

		return ResponseEntity.ok().body(updatedEmployee);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete employee by id")
	@ApiResponse(responseCode = "200", description = "Return true if delete successfully")
	@ApiResponse(responseCode = "400", description = "Return error message if delete failed")
	public ResponseEntity<?> delete(@PathVariable UUID id) {
		var result = employeeService.delete(id);

		if (!result) {
			return ResponseEntity.badRequest().body("Employee deletion failed");
		}
		return ResponseEntity.ok().build();
	}

	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO request, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
		}

		var result = employeeService.changePassword(request);
		if (!result) {
			return ResponseEntity.badRequest().body("Employee change password failed");
		}
		return ResponseEntity.ok().body("Password changed successfully");
	}
}
