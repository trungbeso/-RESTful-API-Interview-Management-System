package com.interviewmanagementsystem.services.employee;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequestDTO {
	String token;
	String oldPassword;
	String newPassword;
	String confirmPassword;
}
