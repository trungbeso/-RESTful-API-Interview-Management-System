package com.interviewmanagementsystem.repositories;


import com.interviewmanagementsystem.entities.Job;
import com.interviewmanagementsystem.enums.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface IJobRepository extends JpaRepository<Job, UUID>, JpaSpecificationExecutor<Job> {
    Job findByTitle(String title);

    Page<Job> findByStatus(JobStatus jobStatus, Pageable pageable);
}