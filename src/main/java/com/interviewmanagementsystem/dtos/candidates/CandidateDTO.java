package com.interviewmanagementsystem.dtos.candidates;

import com.ninja_in_pyjamas.dtos.BaseDTO;
import com.ninja_in_pyjamas.dtos.employees.response.EmployeeInformationDTO;
import com.ninja_in_pyjamas.dtos.skills.SkillDTO;
import com.ninja_in_pyjamas.enums.CandidateStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDTO extends BaseDTO {
    private UUID id;

    private String fullName;

    private String email;

    private boolean gender;

    private LocalDate dob;

    private String address;

    private String phoneNumber;

    private Integer yearOfExperience;

    private String highestLevel;

    private String CV;

    private String position;

    private String note;

    private CandidateStatus status;

    private EmployeeInformationDTO recruiter;

    private Set<SkillDTO> skills;

    private ZonedDateTime insertedAt;

    private ZonedDateTime updatedAt;

    private ZonedDateTime deletedAt;

    private boolean isActive;
}
