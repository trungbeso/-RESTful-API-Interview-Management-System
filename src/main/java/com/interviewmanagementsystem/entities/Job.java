package com.interviewmanagementsystem.entities;

import com.interviewmanagementsystem.enums.JobStatus;
import com.interviewmanagementsystem.enums.Level;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "jobs")
public class Job extends BaseEntity {
	@Column(nullable = false)
	private String title;

	@ManyToMany
	@JoinTable(
		  name = "job_skills",
		  joinColumns = @JoinColumn(name = "job_id"),
		  inverseJoinColumns = @JoinColumn(name = "skill_id")
	)
	private Set<Skill> skills;

	@Column(nullable = false)
	private LocalDateTime startDate;

	@Column(nullable = false)
	private LocalDateTime endDate;

	@Column(nullable = false)
	private Level level;

	private JobStatus status;

	private String workingAddress;

	private String description;

	@ManyToMany
	@JoinTable(
		  name = "job_benefits",
		  joinColumns = @JoinColumn(name = "job_id"),
		  inverseJoinColumns = @JoinColumn(name = "benefit_id")
	)
	private Set<Benefit> benefits;

	@Column(precision = 10, scale = 2)
	private BigDecimal salaryFrom;

	@Column(precision = 10, scale = 2)
	private BigDecimal salaryTo;

	@OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
	private Set<InterviewSchedule> interviews;
}