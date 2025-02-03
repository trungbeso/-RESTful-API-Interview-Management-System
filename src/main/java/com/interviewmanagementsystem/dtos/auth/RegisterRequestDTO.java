package com.interviewmanagementsystem.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequestDTO {

	@NotBlank(message = "Full Name is required")
	@Length(min = 2, max = 100, message = "Full Name must be between 2 and 100 characters")
	String fullName;

	@NotBlank(message = "Email is required")
	@Length(min = 6, max = 50, message = "Email must be between 6 and 50 characters")
	String email;

	@NotBlank(message = "Phone Number is required")
	@Length(min = 10, max = 20, message = "Phone Number must be between 10 and 20 characters")
	String phoneNumber;

	boolean gender;
}