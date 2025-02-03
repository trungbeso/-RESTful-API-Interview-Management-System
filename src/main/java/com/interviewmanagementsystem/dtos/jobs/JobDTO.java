package com.interviewmanagementsystem.dtos.jobs;

import com.ninja_in_pyjamas.dtos.MasterDTO;
import com.ninja_in_pyjamas.enums.JobStatus;
import com.ninja_in_pyjamas.enums.Level;
import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobDTO extends MasterDTO {
    private String title;

     @ElementCollection
    private Set<String> skills;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Level level;
 
    private JobStatus status;

    private String workingAddress;

    private String description;

    @ElementCollection
    private Set<String> benefits;

    private BigDecimal salaryFrom;

    private BigDecimal salaryTo;

}
