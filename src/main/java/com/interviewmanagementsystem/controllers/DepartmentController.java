package com.interviewmanagementsystem.controllers;

import com.interviewmanagementsystem.dtos.departments.DepartmentCreateUpdateDTO;
import com.interviewmanagementsystem.dtos.departments.DepartmentDTO;
import com.interviewmanagementsystem.services.department.IDepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/departments")
@Tag(name = "Departments")
public class DepartmentController {
    private final IDepartmentService departmentService;

    private final PagedResourcesAssembler<DepartmentDTO> pagedResourcesAssembler;

    public DepartmentController(IDepartmentService departmentService, PagedResourcesAssembler<DepartmentDTO> pagedResourcesAssembler) {
        this.departmentService = departmentService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    // Get All
    @GetMapping
    @Operation(summary = "Get all departments")
    @ApiResponse(responseCode = "200", description = "Get all departments successfully")
    public ResponseEntity<List<DepartmentDTO>> getAll() {
        var result = departmentService.findAll();
        return ResponseEntity.ok(result);
    }

    // Get By Id
    @GetMapping("/{id}")
    @Operation(summary = "Get department by id")
    @ApiResponse(responseCode = "200", description = "Get department by id successfully")
    @ApiResponse(responseCode = "404", description = "Department not found")
    public ResponseEntity<DepartmentDTO> getById(@PathVariable UUID id) {
        var result = departmentService.findById(id);
        return ResponseEntity.ok(result);
    }

    // Get By Name
    @GetMapping("/name")
    @Operation(summary = "Get department by name")
    @ApiResponse(responseCode = "200", description = "Get department by name successfully")
    public ResponseEntity<DepartmentDTO> getByName(String name) {
        var result = departmentService.findByName(name);
        return ResponseEntity.ok(result);
    }

    // Create
    @PostMapping
    @Operation(summary = "Create department")
    @ApiResponse(responseCode = "201", description = "Create department successfully")
    public ResponseEntity<?> create(@RequestBody @Valid DepartmentCreateUpdateDTO departmentDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        
        var result = departmentService.create(departmentDTO);

        if (result == null) {
            return ResponseEntity.badRequest().body("creation failed");
        }

        return ResponseEntity.ok(result);
    }

    // Update
    @PutMapping("/{id}")
    @Operation(summary = "Update department")
    @ApiResponse(responseCode = "200", description = "Update department successfully")
    @ApiResponse(responseCode = "404", description = "Department not found")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody @Valid DepartmentCreateUpdateDTO departmentDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        
        var result = departmentService.update(id, departmentDTO);

        if (result == null) {
            return ResponseEntity.badRequest().body("update failed");
        }

        return ResponseEntity.ok(result);
    }

    // Delete
    @PostMapping("/{id}/delete")
    @Operation(summary = "Delete department")
    @ApiResponse(responseCode = "200", description = "Delete department successfully")
    @ApiResponse(responseCode = "404", description = "Department not found")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        departmentService.delete(id);
        return ResponseEntity.ok().build();
    }
}

