package com.interviewmanagementsystem.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class Users extends BaseEntity {

	@Column(nullable = false)
	private String fullName;

	private String username;

	private String password;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false, unique = true)
	private String phoneNumber;

	private LocalDate dob;

	private String address;

	@Column(nullable = false)
	private boolean gender;
}
