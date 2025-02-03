package com.interviewmanagementsystem.dtos.offers;



import com.interviewmanagementsystem.dtos.candidates.CandidateDTO;
import com.interviewmanagementsystem.dtos.departments.DepartmentDTO;
import com.interviewmanagementsystem.dtos.employees.response.EmployeeInformationDTO;
import com.interviewmanagementsystem.enums.Level;
import com.interviewmanagementsystem.enums.OfferStatus;
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
