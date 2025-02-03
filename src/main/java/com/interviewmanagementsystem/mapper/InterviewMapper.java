package com.interviewmanagementsystem.mapper;

import com.ninja_in_pyjamas.dtos.interviews.InterviewCreateUpdateDTO;
import com.ninja_in_pyjamas.dtos.interviews.InterviewDTO;
import com.ninja_in_pyjamas.entities.Benefit;
import com.ninja_in_pyjamas.entities.InterviewSchedule;
import com.ninja_in_pyjamas.entities.Role;
import com.ninja_in_pyjamas.entities.Skill;
import com.ninja_in_pyjamas.enums.RoleName;
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
