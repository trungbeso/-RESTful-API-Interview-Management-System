package com.interviewmanagementsystem.dtos.employees.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmployeeResetPasswordRequest {
	private String email;
}
