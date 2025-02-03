package com.interviewmanagementsystem.services.job;

import com.interviewmanagementsystem.dtos.jobs.JobCreateUpdateDTO;
import com.interviewmanagementsystem.dtos.jobs.JobDTO;
import com.interviewmanagementsystem.entities.Job;
import com.interviewmanagementsystem.enums.JobStatus;
import com.interviewmanagementsystem.repositories.IBenefitRepository;
import com.interviewmanagementsystem.repositories.IJobRepository;
import com.interviewmanagementsystem.repositories.ISkillRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.interviewmanagementsystem.entities.Skill;
import com.interviewmanagementsystem.entities.Benefit;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class JobService implements IJobService {
    private final IJobRepository jobRepository;
    private final ISkillRepository skillRepository;
    private final IBenefitRepository benefitRepository;

    public JobService(IJobRepository jobRepository, ISkillRepository skillRepository,
            IBenefitRepository benefitRepository) {

        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.benefitRepository = benefitRepository;
    }

    @Scheduled(cron = "0 */2 * * * ?") 
    public void updateJobStatuses() {
        LocalDate today = LocalDate.now();
        List<Job> jobs = jobRepository.findAll();
        for (Job job : jobs) {
            LocalDate startDate = job.getStartDate() != null ? job.getStartDate().toLocalDate() : null;
            LocalDate endDate = job.getEndDate() != null ? job.getEndDate().toLocalDate() : null;
            var status = job.getStatus();
            var checkDate = today.isBefore(startDate);
            if (status.name().equals("DRAFT") && startDate != null && !checkDate) {
                job.setStatus(JobStatus.OPEN);
            }
            var checkEndDate =today.isAfter(endDate);
            if (status.name().equals("OPEN") && endDate != null && checkEndDate) {
                job.setStatus(JobStatus.CLOSED); 
            }

            jobRepository.save(job);
        }
    }

    @Override
    public List<JobDTO> findAll() {
        // Get all job entities
        var jobs = jobRepository.findAll();

        // Convert to DTO
        var jobDTOs = jobs.stream().map(item -> {
            var jobDTO = new JobDTO();
            jobDTO.setId(item.getId());
            jobDTO.setTitle(item.getTitle());
            jobDTO.setStartDate(item.getStartDate());
            jobDTO.setEndDate(item.getEndDate());
            jobDTO.setLevel(item.getLevel());
            jobDTO.setStatus(item.getStatus());
            jobDTO.setWorkingAddress(item.getWorkingAddress());
            jobDTO.setDescription(item.getDescription());
            jobDTO.setSalaryFrom(item.getSalaryFrom());
            jobDTO.setSalaryTo(item.getSalaryTo());
            var skillNames = item.getSkills().stream()
                    .map(Skill::getName) 
                    .collect(Collectors.toSet());
            jobDTO.setSkills(skillNames);

            var benefitNames = item.getBenefits().stream()
                    .map(Benefit::getName) 
                    .collect(Collectors.toSet());
            jobDTO.setBenefits(benefitNames);
            return jobDTO;
        }).toList();
        // Return data
        return jobDTOs;
    }

    @Override
    public JobDTO getById(UUID id) {
        var job = jobRepository.findById(id).orElse(null);

        if (job == null) {
            throw new IllegalArgumentException("Job is null");
        }

        // Convert to DTO
        var jobDTO = new JobDTO();
        jobDTO.setId(job.getId());
        jobDTO.setTitle(job.getTitle());
        jobDTO.setStartDate(job.getStartDate());
        jobDTO.setEndDate(job.getEndDate());
        jobDTO.setLevel(job.getLevel());
        jobDTO.setStatus(job.getStatus());
        jobDTO.setWorkingAddress(job.getWorkingAddress());
        jobDTO.setDescription(job.getDescription());
        jobDTO.setSalaryFrom(job.getSalaryFrom());
        jobDTO.setSalaryTo(job.getSalaryTo());

        var skillNames = job.getSkills().stream()
                .map(Skill::getName) 
                .collect(Collectors.toSet());
        jobDTO.setSkills(skillNames);

        var benefitNames = job.getBenefits().stream()
                .map(Benefit::getName) 
                .collect(Collectors.toSet());
        jobDTO.setBenefits(benefitNames);

        // Return dto
        return jobDTO;
    }

    @Override
    public Page<JobDTO> searchAll(String keyword, Pageable pageable) {
        Specification<Job> spec = (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isBlank()) {
                return null;
            }

            // name LIKE %keyword%
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    "%" + keyword.toLowerCase() + "%");
        };

        // Find by filter and paging, sorting
        var jobs = jobRepository.findAll(spec, pageable);

        // Convert to DTO
        var jobDTOs = jobs.map(item -> {
            var jobDTO = new JobDTO();
            jobDTO.setId(item.getId());
            jobDTO.setTitle(item.getTitle());
            jobDTO.setStartDate(item.getStartDate());
            jobDTO.setEndDate(item.getEndDate());
            jobDTO.setLevel(item.getLevel());
            jobDTO.setStatus(item.getStatus());
            jobDTO.setWorkingAddress(item.getWorkingAddress());
            jobDTO.setDescription(item.getDescription());
            jobDTO.setSalaryFrom(item.getSalaryFrom());
            jobDTO.setSalaryTo(item.getSalaryTo());

            var skillNames = item.getSkills().stream()
                    .map(Skill::getName) 
                    .collect(Collectors.toSet());
            jobDTO.setSkills(skillNames);

            var benefitNames = item.getBenefits().stream()
                    .map(Benefit::getName) 
                    .collect(Collectors.toSet());
            jobDTO.setBenefits(benefitNames);

            return jobDTO;
        });

        // Return data
        return jobDTOs;
    }

    @Override
    public JobDTO create(JobCreateUpdateDTO jobCreateUpdateDTO) {
        // Check null object
        if (jobCreateUpdateDTO == null) {
            throw new IllegalArgumentException("Job is null");
        }

        // Check unique name
        var existingJob = jobRepository.findByTitle(jobCreateUpdateDTO.getTitle());

        if (existingJob != null) {
            throw new IllegalArgumentException("Job Title is existed");
        }
        var skills = skillRepository.findAllById(jobCreateUpdateDTO.getSkillIds());
        if (skills.size() != jobCreateUpdateDTO.getSkillIds().size()) {
            throw new IllegalArgumentException("Some skills are invalid or not found");
        }

        var benefits = benefitRepository.findAllById(jobCreateUpdateDTO.getBenefitIds());
        if (benefits.size() != jobCreateUpdateDTO.getBenefitIds().size()) {
            throw new IllegalArgumentException("Some benefits are invalid or not found");
        }

        // Convert to job
        var job = new Job();
        job.setTitle(jobCreateUpdateDTO.getTitle());
        job.setSkills(skills.stream().collect(Collectors.toSet()));
        job.setStartDate(jobCreateUpdateDTO.getStartDate());
        job.setEndDate(jobCreateUpdateDTO.getEndDate());
        job.setLevel(jobCreateUpdateDTO.getLevel());
        job.setStatus(JobStatus.DRAFT);
        job.setWorkingAddress(jobCreateUpdateDTO.getWorkingAddress());
        job.setDescription(jobCreateUpdateDTO.getDescription());
        job.setBenefits(benefits.stream().collect(Collectors.toSet()));
        job.setSalaryFrom(jobCreateUpdateDTO.getSalaryFrom());
        job.setSalaryTo(jobCreateUpdateDTO.getSalaryTo());
        job.setInsertedAt(ZonedDateTime.now());

        // Save to database
        var newJob = jobRepository.save(job);

        // Convert to DTO
        var jobDTO = new JobDTO();
        jobDTO.setId(newJob.getId());
        jobDTO.setTitle(newJob.getTitle());
        jobDTO.setStartDate(newJob.getStartDate());
        jobDTO.setEndDate(newJob.getEndDate());
        jobDTO.setLevel(newJob.getLevel());
        jobDTO.setStatus(newJob.getStatus());
        jobDTO.setWorkingAddress(newJob.getWorkingAddress());
        jobDTO.setDescription(newJob.getDescription());
        jobDTO.setSalaryFrom(newJob.getSalaryFrom());
        jobDTO.setSalaryTo(newJob.getSalaryTo());

        var skillNames = newJob.getSkills().stream()
                .map(Skill::getName) 
                .collect(Collectors.toSet());
        jobDTO.setSkills(skillNames);

        var benefitNames = newJob.getBenefits().stream()
                .map(Benefit::getName) 
                .collect(Collectors.toSet());
        jobDTO.setBenefits(benefitNames);

        // Return dto
        return jobDTO;
    }

    @Override
    public JobDTO update(UUID id, JobCreateUpdateDTO jobCreateUpdateDTO) {
        // Check null object
        if (jobCreateUpdateDTO == null) {
            throw new IllegalArgumentException("Job is null");
        }

        // Check if job not exsiting
        var existingJob = jobRepository.findById(id).orElse(null);

        if (existingJob == null) {
            throw new IllegalArgumentException("Job is not existed");
        }

        // Check unique name if not the same id
        var existingJobSameName = jobRepository.findByTitle(jobCreateUpdateDTO.getTitle());

        if (existingJobSameName != null && !existingJobSameName.getId().equals(id)) {
            throw new IllegalArgumentException("Job name is existed");
        }

        // Check startDate and endDate consistency
        if (jobCreateUpdateDTO.getStartDate() != null
                && jobCreateUpdateDTO.getEndDate() != null
                && jobCreateUpdateDTO.getStartDate().isAfter(jobCreateUpdateDTO.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        var skills = skillRepository.findAllById(jobCreateUpdateDTO.getSkillIds());
        if (skills.size() != jobCreateUpdateDTO.getSkillIds().size()) {
            throw new IllegalArgumentException("Some skills are invalid or not found");
        }

        var benefits = benefitRepository.findAllById(jobCreateUpdateDTO.getBenefitIds());
        if (benefits.size() != jobCreateUpdateDTO.getBenefitIds().size()) {
            throw new IllegalArgumentException("Some benefits are invalid or not found");
        }

        // Convert to job to update
        existingJob.setTitle(jobCreateUpdateDTO.getTitle());
        existingJob.setSkills(skills.stream().collect(Collectors.toSet()));;
        existingJob.setStartDate(jobCreateUpdateDTO.getStartDate());
        existingJob.setEndDate(jobCreateUpdateDTO.getEndDate());
        existingJob.setLevel(jobCreateUpdateDTO.getLevel());
        existingJob.setWorkingAddress(jobCreateUpdateDTO.getWorkingAddress());
        existingJob.setDescription(jobCreateUpdateDTO.getDescription());
        existingJob.setBenefits(benefits.stream().collect(Collectors.toSet()));;
        existingJob.setSalaryFrom(jobCreateUpdateDTO.getSalaryFrom());
        existingJob.setSalaryTo(jobCreateUpdateDTO.getSalaryTo());
        existingJob.setUpdatedAt(ZonedDateTime.now());

        // Save to database
        var updatedJob = jobRepository.save(existingJob);
        // Return true if success, otherwise false

        // Convert to DTO
        var jobDTO = new JobDTO();
        jobDTO.setId(updatedJob.getId());
        jobDTO.setTitle(updatedJob.getTitle());
        jobDTO.setStartDate(updatedJob.getStartDate());
        jobDTO.setEndDate(updatedJob.getEndDate());
        jobDTO.setLevel(updatedJob.getLevel());
        jobDTO.setStatus(updatedJob.getStatus());
        jobDTO.setWorkingAddress(updatedJob.getWorkingAddress());
        jobDTO.setDescription(updatedJob.getDescription());
        jobDTO.setSalaryFrom(updatedJob.getSalaryFrom());
        jobDTO.setSalaryTo(updatedJob.getSalaryTo());

        var skillNames = updatedJob.getSkills().stream()
                .map(Skill::getName) 
                .collect(Collectors.toSet());
        jobDTO.setSkills(skillNames);

        var benefitNames = updatedJob.getBenefits().stream()
                .map(Benefit::getName) 
                .collect(Collectors.toSet());
        jobDTO.setBenefits(benefitNames);
        // Return dto
        return jobDTO;
    }

    @Override
    public boolean delete(UUID id) {
        var existingJob = jobRepository.findById(id).orElse(null);
        if (existingJob == null) {
            throw new IllegalArgumentException("Job is not existed");
        }

        jobRepository.delete(existingJob);

        var result = jobRepository.existsById(id);

        return !result;
    }

    @Override
    public Page<JobDTO> searchByStatus(String status, Pageable pageable) {
        Specification<Job> spec = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
    
            if (status != null && !status.isBlank()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(
                        root.get("status"),
                        JobStatus.valueOf(status.toUpperCase()) // Enum JobStatus
                ));
            }
    
            return predicate;
        };
    
        var jobs = jobRepository.findAll(spec, pageable);
    
        var jobDTOs = jobs.map(item -> {
            var jobDTO = new JobDTO();
            jobDTO.setId(item.getId());
            jobDTO.setTitle(item.getTitle());
            jobDTO.setStartDate(item.getStartDate());
            jobDTO.setEndDate(item.getEndDate());
            jobDTO.setLevel(item.getLevel());
            jobDTO.setStatus(item.getStatus());
            jobDTO.setWorkingAddress(item.getWorkingAddress());
            jobDTO.setDescription(item.getDescription());
            jobDTO.setSalaryFrom(item.getSalaryFrom());
            jobDTO.setSalaryTo(item.getSalaryTo());
    
            var skillNames = item.getSkills().stream()
                    .map(Skill::getName)
                    .collect(Collectors.toSet());
            jobDTO.setSkills(skillNames);
    
            var benefitNames = item.getBenefits().stream()
                    .map(Benefit::getName)
                    .collect(Collectors.toSet());
            jobDTO.setBenefits(benefitNames);
    
            return jobDTO;
        });
    
        return jobDTOs;
    }

    

}