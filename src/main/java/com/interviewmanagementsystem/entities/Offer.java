package com.interviewmanagementsystem.entities;

import com.interviewmanagementsystem.enums.Level;
import com.interviewmanagementsystem.enums.OfferStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "offers")
public class Offer extends BaseEntity {
	private String contractType;

	private String position;

	private Level level;

	private LocalDateTime contractFrom;

	private LocalDateTime contractTo;

	private LocalDateTime dueDate;

	@Column(precision = 10, scale = 2)
	private BigDecimal basicSalary;

	private OfferStatus status;

	private String note;

	@ManyToOne
	@JoinColumn(name = "candidate_id", referencedColumnName = "id")
	private Candidate candidate;

	@ManyToOne
	@JoinColumn(name = "approver_id", referencedColumnName = "id")
	private Employee approver;

	@ManyToOne
	@JoinColumn(name = "department_id", referencedColumnName = "id")
	private Department department;

	@ManyToOne
	@JoinColumn(name = "recruiter_id", referencedColumnName = "id")
	private Employee recruiter;
}
