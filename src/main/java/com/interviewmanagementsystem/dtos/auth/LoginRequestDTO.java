package com.interviewmanagementsystem.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequestDTO {
	@NotBlank(message = "Username is required")
	@Length(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
	String username;

	@NotBlank(message = "Password is required")
	@Length(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
	String password;
}
