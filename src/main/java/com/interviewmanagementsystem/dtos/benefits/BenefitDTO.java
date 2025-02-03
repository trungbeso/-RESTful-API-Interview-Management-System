package com.interviewmanagementsystem.dtos.benefits;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BenefitDTO {

    private UUID id;
    private String name;

}
