package com.interviewmanagementsystem.repositories;

import com.ninja_in_pyjamas.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IDepartmentRepository extends JpaRepository<Department, UUID>, JpaSpecificationExecutor<Department> {
	Department findByName(String name);
}
