package com.interviewmanagementsystem.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequestDTO {

	@NotBlank(message = "Email is required")
	@Length(min = 5, max = 255, message = "Email must be between 5 and 255 characters")
	String email;

}
