package com.interviewmanagementsystem.services.role;

import com.interviewmanagementsystem.dtos.role.RoleMasterDTO;

import java.util.List;
import java.util.UUID;

public interface IRoleService {
	List<RoleMasterDTO> findByName(String name);

	List<RoleMasterDTO> getAll();

	RoleMasterDTO getById(UUID id);
}
