package com.interviewmanagementsystem.dtos.employees.request;

import com.ninja_in_pyjamas.dtos.MasterCreateUpdateDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeUpdateRequest extends MasterCreateUpdateDTO {

	String fullName;

	String username;

	String password;

	LocalDate dateOfBirth;

	String phoneNumber;

	Set<UUID> roleIds;

	boolean isActive;

	String email;

	String address;

	boolean gender;

	UUID departmentId;

	String note;
}
