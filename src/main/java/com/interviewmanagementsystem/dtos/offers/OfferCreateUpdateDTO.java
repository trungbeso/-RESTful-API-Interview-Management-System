package com.interviewmanagementsystem.dtos.offers;

import com.ninja_in_pyjamas.enums.Level;
import com.ninja_in_pyjamas.enums.OfferStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferCreateUpdateDTO {
    
    @NotBlank(message = "Contract type is required")
    @Length(min = 2, max = 255, message = "Contract type must be between 2 and 255 characters")
    private String contractType;

    @NotBlank(message = "Position is required")
    @Length(min = 2, max = 255, message = "Position must be between 2 and 255 characters")
    private String position;

    @NotNull(message = "Level is required")
    private Level level;

    @NotNull(message = "Contract from is required")
    @FutureOrPresent(message = "Contract from must be in the present or future")
    private LocalDateTime contractFrom;

    @NotNull(message = "Contract to is required")
    @Future(message = "Contract to must be in the future")
    private LocalDateTime contractTo;

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    private LocalDateTime dueDate;

    @NotNull(message = "Basic salary is required")
    @Positive(message = "Basic salary must be positive")
    private BigDecimal basicSalary;

    private OfferStatus status;

    @Length(max = 500, message = "Notes must not exceed 500 characters")
    private String note;
    
    private UUID recruiterId;

    private UUID departmentId;

    private UUID candidateId;

    private UUID approverId;
}
