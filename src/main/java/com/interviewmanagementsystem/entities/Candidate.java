package com.interviewmanagementsystem.entities;

import com.ninja_in_pyjamas.enums.CandidateStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "candidates")
public class Candidate extends Users {
	@ManyToOne
	@JoinColumn(name = "recruiter_id", referencedColumnName = "id", nullable = false)
	private Employee recruiter;

	@Column(nullable = false)
	private CandidateStatus status;

	// Use FireBase to store CV
	private String CV;

	@Column(nullable = false)
	private String highestLevel;

	@Column(nullable = false)
	private String position;

	private String note;

	private Integer yearOfExperience;

	@ManyToMany
	@JoinTable(
		  name = "candidate_skills",
		  joinColumns = @JoinColumn(name = "candidate_id"),
		  inverseJoinColumns = @JoinColumn(name = "skill_id")
	)
	private Set<Skill> skills;


	@OneToMany(mappedBy = "candidate",
		  fetch = FetchType.LAZY)
	private Set<InterviewSchedule> interviews;
}

