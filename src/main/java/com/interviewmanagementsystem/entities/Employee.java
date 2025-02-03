package com.interviewmanagementsystem.entities;

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
@Table(name = "employees")
public class Employee extends Users{

	@ManyToOne
	@JoinColumn(name = "department_id", referencedColumnName = "id")
	private Department department;

	@ManyToMany
	@JoinTable(
		  name = "employee_roles",
		  joinColumns = @JoinColumn(name = "employee_id"),
		  inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles;

	@OneToMany(mappedBy = "recruiter", fetch = FetchType.LAZY)
	private Set<Candidate> candidates;


	@OneToMany(mappedBy = "approver")
	private Set<Offer> offers;

	@ManyToMany(mappedBy = "interviewers")
	private Set<InterviewSchedule> interviews;
}
