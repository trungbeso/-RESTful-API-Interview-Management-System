package com.interviewmanagementsystem.dtos.auth;

import com.ninja_in_pyjamas.dtos.employees.response.EmployeeInformationDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponseDTO {
	String token;

	EmployeeInformationDTO user;
}

