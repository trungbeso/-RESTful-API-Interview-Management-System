package com.interviewmanagementsystem.dtos.jobs;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;


import com.interviewmanagementsystem.enums.JobStatus;
import com.interviewmanagementsystem.enums.Level;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobCreateUpdateDTO {

    @NotBlank(message = "Title is required")
    @Length(min = 2, max = 255, message = "Title must be between 2 and 255 characters")
    private String title;

    @NotNull(message = "Skill is required")
    private Set<UUID> skillIds;

    @NotNull(message = "Start date is required")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
@JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
@JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime endDate;

    @NotNull(message = "Level is required")
    private Level level;

    private JobStatus status;

    @NotBlank(message = "Working address is required")
    private String workingAddress;

    private String description;

    @NotEmpty(message = "Benefits are required")
    private Set<UUID> benefitIds;

    @DecimalMin(value = "0.0", inclusive = false, message = "Salary from must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Salary from format is invalid")
    private BigDecimal salaryFrom;

    @DecimalMin(value = "0.0", inclusive = false, message = "Salary to must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Salary to format is invalid")
    private BigDecimal salaryTo;

}
