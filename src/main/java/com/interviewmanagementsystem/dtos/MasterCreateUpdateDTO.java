package com.interviewmanagementsystem.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MasterCreateUpdateDTO {
	@NotNull(message = "Active status is required")
	private boolean isActive;
}
