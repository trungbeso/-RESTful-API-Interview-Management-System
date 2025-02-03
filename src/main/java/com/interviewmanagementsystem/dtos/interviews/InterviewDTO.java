package com.interviewmanagementsystem.dtos.interviews;

import com.ninja_in_pyjamas.dtos.candidates.CandidateDTO;
import com.ninja_in_pyjamas.dtos.employees.response.EmployeeInformationDTO;
import com.ninja_in_pyjamas.dtos.jobs.JobDTO;
import com.ninja_in_pyjamas.enums.InterviewStatus;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewDTO {
    private UUID id;

    private String title;

    private ZonedDateTime startTime;

    private ZonedDateTime endTime;

    private String result;

    private InterviewStatus status;

    private String location;

    private String meetingID;

    private String note;

    private JobDTO job;

    private CandidateDTO candidate;
    
    private EmployeeInformationDTO recruiter;

    private Set<EmployeeInformationDTO> interviewers;

    private ZonedDateTime insertedAt;

    private ZonedDateTime updatedAt;

    private ZonedDateTime deletedAt;

    private boolean isActive;
}
