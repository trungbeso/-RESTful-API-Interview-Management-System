package com.interviewmanagementsystem.dtos.role;


import com.interviewmanagementsystem.dtos.MasterDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleMasterDTO extends MasterDTO {
	private String name;

	private String description;
}
