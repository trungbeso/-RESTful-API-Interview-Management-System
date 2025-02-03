package com.interviewmanagementsystem.mapper;


import com.interviewmanagementsystem.dtos.candidates.CandidateCreateUpdateDTO;
import com.interviewmanagementsystem.dtos.candidates.CandidateDTO;
import com.interviewmanagementsystem.entities.Candidate;
import com.interviewmanagementsystem.entities.Role;
import com.interviewmanagementsystem.enums.RoleName;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CandidateMapper {

    CandidateDTO toDTO(Candidate candidate);

    Candidate toEntity(CandidateCreateUpdateDTO masterDTO);

    CandidateCreateUpdateDTO toMasterDTO(Candidate candidate);

    @Mapping(target = "insertedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Candidate update(@MappingTarget Candidate candidate, CandidateCreateUpdateDTO dto);

    default Set<RoleName> mapRoles(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}

