package com.interviewmanagementsystem.mapper;


import com.interviewmanagementsystem.dtos.interviews.InterviewCreateUpdateDTO;
import com.interviewmanagementsystem.dtos.interviews.InterviewDTO;
import com.interviewmanagementsystem.entities.Benefit;
import com.interviewmanagementsystem.entities.InterviewSchedule;
import com.interviewmanagementsystem.entities.Role;
import com.interviewmanagementsystem.entities.Skill;
import com.interviewmanagementsystem.enums.RoleName;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InterviewMapper {
    InterviewDTO toDTO(InterviewSchedule entity);

    InterviewSchedule toEntity(InterviewCreateUpdateDTO dto);

    @Mapping(target = "insertedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(InterviewCreateUpdateDTO dto, @MappingTarget InterviewSchedule entity);
    
    default Set<RoleName> mapRoles(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    default Set<String> mapSkills(Set<Skill> skills) {
        return skills.stream()
                .map(Skill::getName)
                .collect(Collectors.toSet());
    }

    default Set<String> mapBenefits(Set<Benefit> skills) {
        return skills.stream()
                .map(Benefit::getName)
                .collect(Collectors.toSet());
    }
}
