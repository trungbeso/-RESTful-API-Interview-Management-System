package com.interviewmanagementsystem.repositories;



import com.interviewmanagementsystem.entities.InterviewSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;


@Repository
public interface IInterviewRepository extends JpaRepository<InterviewSchedule, UUID>, JpaSpecificationExecutor<InterviewSchedule> {
   List<InterviewSchedule> findAllByStartTimeBetween(ZonedDateTime start, ZonedDateTime end);
}
