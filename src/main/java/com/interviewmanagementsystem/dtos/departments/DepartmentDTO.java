package com.interviewmanagementsystem.dtos.departments;


import com.interviewmanagementsystem.dtos.MasterDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO extends MasterDTO {
	private UUID id;

	private String name;

	private String description;
}

