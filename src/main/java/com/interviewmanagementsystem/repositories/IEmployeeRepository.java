package com.interviewmanagementsystem.repositories;

import com.ninja_in_pyjamas.entities.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, UUID>, JpaSpecificationExecutor<Employee> {

	Employee findByPhoneNumber(String phoneNumber);

	Employee findByUsername(String username);

	boolean existsByUsername(String username);

	Employee findByEmailOrPhoneNumber(String email, String phoneNumber);

	Employee findByEmail(String email);

	Employee findByFullName(String fullName);

	@Transactional
	@Modifying
	@Query("update Employee e set e.password =?2 where e.email=?1")
	void updatePassword(String email, String password);
}
