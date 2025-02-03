package com.interviewmanagementsystem.mapper;


import com.interviewmanagementsystem.dtos.benefits.BenefitDTO;
import com.interviewmanagementsystem.entities.Benefit;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BenefitMapper {
     // Chuyển từ Benefit sang BenefitDTO
    BenefitDTO toDTO(Benefit skill);

    // Chuyển từ BenefitDTO sang Benefit
    Benefit toEntity(BenefitDTO skillDTO);

    // Chuyển danh sách Benefit sang danh sách BenefitDTO
    List<BenefitDTO> toDTOList(List<Benefit> skills);

    // Chuyển danh sách BenefitDTO sang danh sách Benefit
    List<Benefit> toEntityList(List<BenefitDTO> skillDTOs);

}
