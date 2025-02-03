package com.interviewmanagementsystem.entities;

import com.ninja_in_pyjamas.enums.InterviewResult;
import com.ninja_in_pyjamas.enums.InterviewStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "interviews")
public class InterviewSchedule extends BaseEntity {
	private String title;

	@ManyToOne
	@JoinColumn(name = "job_id", referencedColumnName = "id")
	private Job job;

	@ManyToOne
	@JoinColumn(name = "candidate_id", referencedColumnName = "id")
	private Candidate candidate;

	private LocalDateTime interviewDate;

	private ZonedDateTime startTime;

	private ZonedDateTime endTime;

	private String location;

	@ManyToMany
	@JoinTable(
		  name = "employee_interviews",
		  inverseJoinColumns = @JoinColumn(name = "employee_id"),
		  joinColumns = @JoinColumn(name = "interview_id")
	)
	private Set<Employee> interviewers;

	@ManyToOne
	@JoinColumn(name = "recruiter_id", referencedColumnName = "id")
	private Employee recruiter;

	private String note;

	private String meetingID;

	private InterviewResult result;

	private InterviewStatus status;
}
