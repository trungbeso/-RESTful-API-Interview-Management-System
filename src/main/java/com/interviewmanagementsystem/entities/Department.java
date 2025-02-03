package com.interviewmanagementsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "departments")
public class Department {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private String name;

	private String description;

	@OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
	private Set<Employee> employees;

	@OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
	private Set<Offer> offers;
}
