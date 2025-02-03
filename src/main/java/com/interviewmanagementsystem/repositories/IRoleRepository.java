package com.interviewmanagementsystem.repositories;

import com.ninja_in_pyjamas.entities.Role;
import com.ninja_in_pyjamas.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface IRoleRepository extends JpaRepository<Role, UUID>, JpaSpecificationExecutor<Role> {
	Role findByName(RoleName name);
}
