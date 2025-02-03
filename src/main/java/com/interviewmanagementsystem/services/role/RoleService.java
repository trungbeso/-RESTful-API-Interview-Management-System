package com.interviewmanagementsystem.services.role;

import com.interviewmanagementsystem.dtos.role.RoleMasterDTO;
import com.interviewmanagementsystem.entities.Role;
import com.interviewmanagementsystem.mapper.RoleMapper;
import com.interviewmanagementsystem.repositories.IRoleRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleService implements IRoleService{

	private final IRoleRepository roleRepository;
	private final RoleMapper roleMapper;

	public RoleService(IRoleRepository roleRepository, RoleMapper roleMapper) {
		this.roleRepository = roleRepository;
		this.roleMapper = roleMapper;
	}

	@Override
	public List<RoleMasterDTO> findByName(String name) {
		Specification<Role> spec = (root, query, cb) -> {
			if (name == null) {
				return null;
			}
			return cb.like(root.get("name"), "%" + name  + "%");
		};
		var roles = roleRepository.findAll(spec);

		// Convert entities to DTOs
		return roles.stream().map(roleMapper::toMasterDTO).toList();
	}

	@Override
	public List<RoleMasterDTO> getAll() {
		return roleRepository.findAll().stream().map(roleMapper::toMasterDTO).toList();
	}

	@Override
	public RoleMasterDTO getById(UUID id) {
		var role = roleRepository.findById(id).orElseThrow( () -> new RuntimeException("Role not found"));
		return roleMapper.toMasterDTO(role);
	}

}
