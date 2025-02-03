package com.interviewmanagementsystem.controllers;



import com.interviewmanagementsystem.dtos.benefits.BenefitDTO;
import com.interviewmanagementsystem.services.benefit.BenefitService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/benefits")
public class BenefitController {

     private final BenefitService benefitService;

    public BenefitController(BenefitService benefitService) {
        this.benefitService = benefitService;
    }

    @GetMapping
    public List<BenefitDTO> getAllBenefits() {
        return benefitService.getAllBenefits();
    }

    @PostMapping
    public BenefitDTO createBenefit(@RequestBody BenefitDTO benefitDTO) {
        return benefitService.saveBenefit(benefitDTO);
    }

}

