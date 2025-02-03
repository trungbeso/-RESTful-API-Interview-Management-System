package com.interviewmanagementsystem.services.job;

import com.interviewmanagementsystem.dtos.jobs.JobCreateUpdateDTO;
import com.interviewmanagementsystem.dtos.jobs.JobDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IJobService  {

    List<JobDTO> findAll();

    JobDTO getById(UUID id);

    Page<JobDTO> searchAll(String keyword, Pageable pageable);

    JobDTO create(JobCreateUpdateDTO jobCreateUpdateDTO);

    JobDTO update(UUID id, JobCreateUpdateDTO jobCreateUpdateDTO);

    boolean delete(UUID id);

    Page<JobDTO> searchByStatus(String status, Pageable pageable);


   
}