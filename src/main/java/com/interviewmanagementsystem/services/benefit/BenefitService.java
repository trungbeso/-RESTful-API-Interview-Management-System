package com.interviewmanagementsystem.services.benefit;

import com.ninja_in_pyjamas.dtos.benefits.BenefitDTO;
import com.ninja_in_pyjamas.entities.Benefit;
import com.ninja_in_pyjamas.mapper.BenefitMapper;
import com.ninja_in_pyjamas.repositories.IBenefitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BenefitService {
    private final IBenefitRepository benefitRepository;

    private final BenefitMapper benefitMapper;

     public List<BenefitDTO> getAllBenefits() {
        List<Benefit> benefits = benefitRepository.findAll();
        return benefitMapper.toDTOList(benefits); // Chuyển từ entity sang DTO
    }

    public BenefitDTO saveBenefit(BenefitDTO benefitDTO) {
        Benefit benefit = benefitMapper.toEntity(benefitDTO); // Chuyển từ DTO sang entity
        Benefit savedBenefit = benefitRepository.save(benefit);
        return benefitMapper.toDTO(savedBenefit); // Chuyển entity đã lưu sang DTO
    }
}
