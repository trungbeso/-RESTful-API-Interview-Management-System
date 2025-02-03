package com.interviewmanagementsystem.mapper;

import com.ninja_in_pyjamas.dtos.skills.SkillDTO;
import com.ninja_in_pyjamas.entities.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SkillMapper {
     // Chuyển từ Skill sang SkillDTO
    SkillDTO toDTO(Skill skill);

    // Chuyển từ SkillDTO sang Skill
    Skill toEntity(SkillDTO skillDTO);

    // Chuyển danh sách Skill sang danh sách SkillDTO
    List<SkillDTO> toDTOList(List<Skill> skills);

    // Chuyển danh sách SkillDTO sang danh sách Skill
    List<Skill> toEntityList(List<SkillDTO> skillDTOs);
}
