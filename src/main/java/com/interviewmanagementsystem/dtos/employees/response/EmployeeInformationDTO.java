package com.interviewmanagementsystem.dtos.employees.response;



import com.interviewmanagementsystem.dtos.BaseDTO;
import com.interviewmanagementsystem.enums.RoleName;
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
