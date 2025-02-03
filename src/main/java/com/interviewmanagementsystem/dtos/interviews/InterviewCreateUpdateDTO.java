package com.interviewmanagementsystem.dtos.interviews;



import com.interviewmanagementsystem.enums.InterviewResult;
import com.interviewmanagementsystem.enums.InterviewStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewCreateUpdateDTO {
    @NotBlank(message = "Interview title cannot be blank")
    @Length(min = 5, max = 255, message = "Title must be between 5 and 255 characters")
    private String title;

    @NotNull(message = "Interviewer IDs cannot be null")
    private Set<UUID> interviewerIds;

    @NotNull(message = "Candidate ID cannot be null")
    private UUID candidateId;

    @NotNull(message = "Recruiter ID cannot be null")
    private UUID recruiterId;

    @NotNull(message = "Start time cannot be null")
    @FutureOrPresent(message = "Start time must be in the present or future")
    private ZonedDateTime startTime;

    @NotNull(message = "End time cannot be null")
    @Future(message = "End time must be in the future")
    private ZonedDateTime endTime;

    @NotBlank(message = "Location cannot be blank")
    @Length(max = 255, message = "Location must not exceed 255 characters")
    private String location;

    private InterviewStatus status;

    private InterviewResult result;

    @Length(max = 500, message = "Notes must not exceed 500 characters")
    private String note;
    
    @NotNull(message = "Job ID cannot be null")
    private UUID jobId;

    private String meetingID;
}
