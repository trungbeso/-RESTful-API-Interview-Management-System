package com.interviewmanagementsystem.dtos.candidates;


import com.interviewmanagementsystem.enums.CandidateStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateCreateUpdateDTO {
	@NotBlank(message = "Candidate full Name is required")
	@Length(min = 2, max = 100, message = "Candidate Full Name must be between 2 and 100 characters")
	private String fullName;

	@NotBlank(message = "Candidate email is required")
	@Length(min = 6, max = 50, message = "Candidate Email must be between 6 and 50 characters")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
	private String email;

	@NotNull(message = "Gender is required")
	private boolean gender;

	@Past(message = "Date of Birth must be in the past")
	private LocalDate dob;

	@NotBlank(message = "Address is required")
	@Length(min = 2, max = 255, message = "Address must be between 2 and 255 characters")
	private String address;

	@NotBlank(message = "Phone Number is required")
	@Length(min = 10, max = 20, message = "Phone Number must be between 10 and 20 characters")
	private String phoneNumber;

	@Range(min = 0, max = 100, message = "Year Of Experience must be between 0 and 100")
	private Integer yearOfExperience;

	@NotNull(message = "Highest Level is required")
	@Length(min = 2, max = 50, message = "Highest Level must be between 2 and 50 characters")
	private String highestLevel;

	private String CV;

	@NotBlank(message = "Position is required")
	@Length(min = 2, max = 50, message = "Position must be between 2 and 50 characters")
	private String position;

	@Length(max = 500, message = "Notes must not exceed 500 characters")
	private String note;

	@NotNull(message = "Status is required")
	private CandidateStatus status;

	@NotNull(message = "Recruiter Id is required")
	private UUID recruiterId;

	private Set<UUID> skillIds;
}
