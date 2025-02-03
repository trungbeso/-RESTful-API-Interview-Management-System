package com.interviewmanagementsystem.mapper;


import com.interviewmanagementsystem.dtos.jobs.JobDTO;
import com.interviewmanagementsystem.entities.Job;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface JobMapper {
  @Mapping(target = "skills", ignore = true)
  @Mapping(target = "benefits", ignore = true)
  JobDTO toDTO(Job job);

  @Mapping(target = "skills", ignore = true)
  @Mapping(target = "benefits", ignore = true)
  @Mapping(target = "interviews", ignore = true)
  Job toEntity(JobDTO jobDTO);
}
