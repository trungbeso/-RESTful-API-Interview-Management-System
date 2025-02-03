package com.interviewmanagementsystem.dtos.employees.response;

import com.ninja_in_pyjamas.dtos.BaseDTO;
import com.ninja_in_pyjamas.enums.RoleName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeInformationDTO extends BaseDTO {
	String username;

	String fullName;

	String email;

	String phoneNumber;

	Set<RoleName> roles;

}
