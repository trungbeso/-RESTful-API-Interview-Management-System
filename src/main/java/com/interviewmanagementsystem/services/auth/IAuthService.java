package com.interviewmanagementsystem.services.auth;

import com.interviewmanagementsystem.dtos.auth.PasswordResetRequestDTO;
import com.interviewmanagementsystem.dtos.auth.RegisterRequestDTO;
import com.interviewmanagementsystem.dtos.employees.response.EmployeeInformationDTO;
import com.interviewmanagementsystem.services.employee.ChangePasswordRequestDTO;

import java.util.UUID;

public interface IAuthService {
	boolean requestResetPassword(PasswordResetRequestDTO requestDTO);

	boolean changePassword(ChangePasswordRequestDTO requestDTO);

	UUID register(RegisterRequestDTO registerRequestDTO);

	EmployeeInformationDTO getEmployeeInformationDTO(String username);

	boolean existsByUsername(String username);
}
