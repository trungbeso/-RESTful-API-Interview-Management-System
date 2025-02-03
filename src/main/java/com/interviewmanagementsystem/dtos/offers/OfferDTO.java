package com.interviewmanagementsystem.dtos.offers;

import com.ninja_in_pyjamas.dtos.candidates.CandidateDTO;
import com.ninja_in_pyjamas.dtos.departments.DepartmentDTO;
import com.ninja_in_pyjamas.dtos.employees.response.EmployeeInformationDTO;
import com.ninja_in_pyjamas.enums.Level;
import com.ninja_in_pyjamas.enums.OfferStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferDTO {
    private UUID id;

    private CandidateDTO candidate;

    private String contractType;

    private String position;

    private Level level;

    private EmployeeInformationDTO approver;

    private LocalDateTime contractFrom;

    private LocalDateTime contractTo;

    private LocalDateTime dueDate;

    private BigDecimal basicSalary;

    private OfferStatus status;

    private String note;

    private EmployeeInformationDTO recruiter;

    private DepartmentDTO department;
}
