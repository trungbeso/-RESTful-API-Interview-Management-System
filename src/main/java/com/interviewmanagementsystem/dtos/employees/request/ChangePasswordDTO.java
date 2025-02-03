package com.interviewmanagementsystem.dtos.employees.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordDTO {
	@NotBlank(message = "Current password is required")
	@Length(min = 8, max = 20, message = "Current password must be between 8 and 20 characters")
	String oldPassword;

	@NotBlank(message = "New password is required")
	@Length(min = 8, max = 20, message = "New password must be between 8 and 20 characters")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@$!%*?&-])[A-Za-z\\\\d@$!%*?&-]{8,20}$",
		  message = "New password must contain at least one uppercase letter, one lowercase letter, one number and one special character")
	String newPassword;

	@NotBlank(message = "Confirm password is required")
	@Length(min = 8, max = 20, message = "Confirm password must be between 8 and 20 characters")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@$!%*?&-])[A-Za-z\\\\d@$!%*?&-]{8,20}$",
		  message = "Confirm password must contain at least one uppercase letter, one lowercase letter, one number and one special character")
	String confirmNewPassword;
}
