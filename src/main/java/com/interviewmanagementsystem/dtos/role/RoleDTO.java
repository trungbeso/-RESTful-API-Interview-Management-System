package com.interviewmanagementsystem.dtos.role;


import com.interviewmanagementsystem.dtos.BaseDTO;
import com.interviewmanagementsystem.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO extends BaseDTO {
    private RoleName name;

    private String description;
}
