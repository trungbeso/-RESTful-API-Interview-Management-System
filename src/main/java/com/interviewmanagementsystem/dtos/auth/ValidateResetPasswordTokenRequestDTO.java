package com.interviewmanagementsystem.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateResetPasswordTokenRequestDTO {
	@NotBlank(message = "Token is required")
	@Length(min = 5, max = 255, message = "Token must be between 5 and 255 characters")
	private String token;
}
