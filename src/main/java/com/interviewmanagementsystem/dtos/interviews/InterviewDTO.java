package com.interviewmanagementsystem.dtos.interviews;



import com.interviewmanagementsystem.dtos.candidates.CandidateDTO;
import com.interviewmanagementsystem.dtos.employees.response.EmployeeInformationDTO;
import com.interviewmanagementsystem.dtos.jobs.JobDTO;
import com.interviewmanagementsystem.enums.InterviewStatus;
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
