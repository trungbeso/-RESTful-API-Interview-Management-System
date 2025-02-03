package com.interviewmanagementsystem.dtos.role;


import com.interviewmanagementsystem.dtos.MasterCreateUpdateDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
public class RoleCreateUpdateDTO extends MasterCreateUpdateDTO {
    @NotBlank(message = "Name is required")
    @Length(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    private String name;
    
    @Length(max = 500, message = "Name must be less than 500 characters")
    private String description;
}