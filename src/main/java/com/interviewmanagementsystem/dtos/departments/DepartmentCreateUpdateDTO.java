package com.interviewmanagementsystem.dtos.departments;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentCreateUpdateDTO {

    @NotBlank(message = "Department name is required")
    @Length(min = 2, max = 100, message = "Department name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Length(min = 2, max = 100, message = "Description must be between 2 and 100 characters")
    private String description;
}
