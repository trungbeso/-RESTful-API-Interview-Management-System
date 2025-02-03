package com.interviewmanagementsystem.controllers;

import com.interviewmanagementsystem.services.role.IRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Roles")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class RoleController {

	IRoleService roleService;

	@GetMapping
	@Operation(summary="Get all roles")
	@ApiResponse(responseCode = "200", description = "Get all roles successfully")
	@ApiResponse(responseCode = "400", description = "Failed to get all roles")
	public ResponseEntity<?> getAll() {
		return ResponseEntity.ok(roleService.getAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get a role by Id")
	@ApiResponse(responseCode = "200", description = "Get a role by enter Id")
	@ApiResponse(responseCode = "400", description = "Role with this Id not found")
	public ResponseEntity<?> getById(@PathVariable UUID id) throws RoleNotFoundException {
		var role = roleService.getById(id);
		if (role == null) {
			throw new RoleNotFoundException("Role with this Id not found");
		}
		return ResponseEntity.ok(role);
	}

	@GetMapping("/searchByKeyword")
	@Operation(summary = "Search employee list by keyword")
	@ApiResponse(responseCode = "200", description = "Return role list that match the key word")
	public ResponseEntity<?> searchByKeyword(@RequestParam(required = false) String keyword) {
		var roleList = roleService.findByName(keyword);
		if (roleList == null) {
			throw new IllegalArgumentException("role not exist");
		}
		return ResponseEntity.ok().body(roleList);
	}
}
