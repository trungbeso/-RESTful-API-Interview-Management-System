package com.interviewmanagementsystem.services.auth;

import com.ninja_in_pyjamas.dtos.auth.PasswordResetRequestDTO;
import com.ninja_in_pyjamas.dtos.auth.RegisterRequestDTO;
import com.ninja_in_pyjamas.dtos.employees.response.EmployeeInformationDTO;
import com.ninja_in_pyjamas.services.employee.ChangePasswordRequestDTO;

import java.util.UUID;

public interface IAuthService {
	boolean requestResetPassword(PasswordResetRequestDTO requestDTO);

	boolean changePassword(ChangePasswordRequestDTO requestDTO);

	UUID register(RegisterRequestDTO registerRequestDTO);

	EmployeeInformationDTO getEmployeeInformationDTO(String username);

	boolean existsByUsername(String username);
}
