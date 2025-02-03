package com.interviewmanagementsystem.dtos.employees.request;

import com.ninja_in_pyjamas.dtos.MasterCreateUpdateDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeCreationRequest extends MasterCreateUpdateDTO {

	@NotBlank(message = "Full Name is required")
	@Length(min = 2, max = 100, message = "Full Name must be between 2 and 100 characters")
	String fullName;

	@NotBlank(message = "Email is required")
	@Length(min = 2, max = 50, message = "Email must be between 2 and 50 characters")
	String email;

	@NotBlank(message = "Phone Number is required")
	@Length(min = 10, max = 20, message = "Phone Number must be between 10 and 20 characters")
	String phoneNumber;

	@NotNull(message = "Gender is required")
	boolean gender;

	UUID departmentId;

	Set<UUID> roleIds;

	LocalDate dateOfBirth;

	String address;


}
