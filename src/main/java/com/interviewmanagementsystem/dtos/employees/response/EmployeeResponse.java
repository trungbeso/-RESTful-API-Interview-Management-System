package com.interviewmanagementsystem.dtos.employees.response;

import com.ninja_in_pyjamas.dtos.MasterDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeResponse extends MasterDTO {

	String fullName;

	String username;

	String password;

	LocalDateTime dob;
}
