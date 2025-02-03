package com.interviewmanagementsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.TimeZoneColumn;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	boolean isActive;

	@CreatedDate
	@TimeZoneColumn
	@Column(nullable = false, columnDefinition = "DATETIMEOFFSET")
	ZonedDateTime insertedAt;

	@LastModifiedDate
	@TimeZoneColumn
	@Column(columnDefinition = "DATETIMEOFFSET")
	ZonedDateTime updatedAt;

	@LastModifiedDate
	@TimeZoneColumn
	@Column(columnDefinition = "DATETIMEOFFSET")
	ZonedDateTime deletedAt;

	@PrePersist
	public void onInsert() {
		this.insertedAt = ZonedDateTime.now();
		this.isActive = true;
	}

	@PreUpdate
	public void onUpdate() {
		this.updatedAt = ZonedDateTime.now();
	}
}
