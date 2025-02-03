package com.interviewmanagementsystem.services.interview;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.interviewmanagementsystem.dtos.email.EmailRequestDTO;
import com.interviewmanagementsystem.dtos.interviews.InterviewCreateUpdateDTO;
import com.interviewmanagementsystem.dtos.interviews.InterviewDTO;
import com.interviewmanagementsystem.entities.Candidate;
import com.interviewmanagementsystem.entities.Employee;
import com.interviewmanagementsystem.entities.InterviewSchedule;
import com.interviewmanagementsystem.entities.Job;
import com.interviewmanagementsystem.enums.CandidateStatus;
import com.interviewmanagementsystem.enums.InterviewResult;
import com.interviewmanagementsystem.enums.InterviewStatus;
import com.interviewmanagementsystem.exceptions.ResourceNotFoundException;

import com.interviewmanagementsystem.mapper.InterviewMapper;
import com.interviewmanagementsystem.repositories.ICandidateRepository;
import com.interviewmanagementsystem.repositories.IEmployeeRepository;
import com.interviewmanagementsystem.repositories.IInterviewRepository;
import com.interviewmanagementsystem.repositories.IJobRepository;
import com.interviewmanagementsystem.services.email.IEmailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;



import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewService implements IInterviewService {
    private final IInterviewRepository interviewRepository;
    private final IEmployeeRepository employeeRepository;
    private final ICandidateRepository candidateRepository;
    private final IJobRepository jobRepository;
    private final InterviewMapper interviewMapper;
    private final IEmailService emailService;

	@Override
    public List<InterviewDTO> getAll() {
        return interviewRepository.findAll().stream()
             .map(interviewMapper::toDTO)
             .toList();
    }

    @Override
    public InterviewDTO findById(UUID id) {
        InterviewSchedule interview = interviewRepository.findById(id)
             .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));

        return interviewMapper.toDTO(interview);
    }

    @Override
    public InterviewDTO create(InterviewCreateUpdateDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Interview cannot be null");
        }

        Set<Employee> interviewers = dto.getInterviewerIds().stream()
             .map(id -> employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Interviewer not found")))
             .collect(Collectors.toSet());

        if (interviewers.isEmpty()) {
            throw new ResourceNotFoundException("Interviewer not found");
        }

        Employee recruiter = employeeRepository.findById(dto.getRecruiterId()).orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));

        Candidate candidate = candidateRepository.findById(dto.getCandidateId()).orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        if (candidate.getStatus() == CandidateStatus.BANNED) {
            throw new ResourceNotFoundException("Candidate is banned");
        }

        Job job = jobRepository.findById(dto.getJobId()).orElseThrow(() -> new ResourceNotFoundException("Job not found"));


        //convert InterviewDTO to Interview
        InterviewSchedule interview = interviewMapper.toEntity(dto);
        interview.setInterviewers(interviewers);
        interview.setRecruiter(recruiter);
        interview.setCandidate(candidate);
        interview.setJob(job);
        // set interview status and result
        interview.setStatus(InterviewStatus.NEW);
        interview.setResult(InterviewResult.NA);

        //save to database
        InterviewSchedule savedInterview = interviewRepository.save(interview);

        // update candidate status and save to database
        candidate.setStatus(CandidateStatus.WAITING_FOR_INTERVIEW);
        candidateRepository.save(candidate);

        return interviewMapper.toDTO(savedInterview);
    }

    @Override
    public InterviewDTO update(UUID id, InterviewCreateUpdateDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Interview cannot be null");
        }

        InterviewSchedule existingInterview = interviewRepository.findById(id)
             .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));

        Set<Employee> interviewers = dto.getInterviewerIds().stream()
             .map(interviewerId -> employeeRepository.findById(interviewerId).orElseThrow(() -> new ResourceNotFoundException("Interviewer not found")))
             .collect(Collectors.toSet());

        if (interviewers.isEmpty()) {
            throw new ResourceNotFoundException("Interviewer not found");
        }

        Employee recruiter = employeeRepository.findById(dto.getRecruiterId()).orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));

        Candidate candidate = candidateRepository.findById(dto.getCandidateId()).orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        Job job = jobRepository.findById(dto.getJobId()).orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        //convert InterviewCreateUpdateDTO to Interview
        interviewMapper.updateEntity(dto, existingInterview);
        existingInterview.setInterviewers(interviewers);
        existingInterview.setRecruiter(recruiter);
        existingInterview.setCandidate(candidate);
        existingInterview.setJob(job);

        //save to database
        InterviewSchedule savedInterview = interviewRepository.save(existingInterview);

        return interviewMapper.toDTO(savedInterview);
    }

    @Override
    public boolean delete(UUID id) {
        InterviewSchedule interview = interviewRepository.findById(id)
             .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));
        interviewRepository.delete(interview);
        return !interviewRepository.existsById(id);
    }

    @Override
    public Page<InterviewDTO> search(String title, InterviewStatus status, UUID interviewerId, Pageable pageable) {
        Specification<InterviewSchedule> spec = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            // Filter by title
            if (title != null && !title.isEmpty()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }

            // Filter by status
            if (status != null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), status));
            }

            // Filter by interviewerId if provided
            if (interviewerId != null) {
                Join<Object, Object> interviewers = root.join("interviewers", JoinType.INNER);
                predicate = cb.and(predicate, cb.equal(interviewers.get("id"), interviewerId));
            }

            query.distinct(true);

            return predicate;
        };

        return interviewRepository.findAll(spec, pageable).map(interviewMapper::toDTO);
    }

    @Override
    public InterviewDTO updateStatus(UUID id, InterviewStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        InterviewSchedule interview = interviewRepository.findById(id)
             .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));

        interview.setStatus(status);
        InterviewSchedule updatedInterview = interviewRepository.save(interview);

        // update candidate status
        if (interview.getStatus() == InterviewStatus.CANCELLED) {
            Candidate candidate = candidateRepository.findById(interview.getCandidate().getId()).orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
            candidate.setStatus(CandidateStatus.CANCELLED_BY_INTERVIEW);
            candidateRepository.save(candidate);
        }

        // Convert to DTO and return
        return interviewMapper.toDTO(updatedInterview);
    }

    @Override
    public InterviewDTO updateResultAndNote(UUID id, InterviewResult result, String note) {
        if (result == null) {
            throw new IllegalArgumentException("Result cannot be null");
        }

        InterviewSchedule interview = interviewRepository.findById(id)
             .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));

        interview.setResult(result);
        interview.setNote(note);
        InterviewSchedule updatedInterview = interviewRepository.save(interview);

        // Update candidate status
        if (interview.getResult() == InterviewResult.FAILED) {
            Candidate candidate = candidateRepository.findById(interview.getCandidate().getId()).orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
            candidate.setStatus(CandidateStatus.FAILED_BY_INTERVIEW);
            candidateRepository.save(candidate);
        }

        if (interview.getResult() == InterviewResult.PASSED) {
            Candidate candidate = candidateRepository.findById(interview.getCandidate().getId()).orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
            candidate.setStatus(CandidateStatus.PASSED_BY_INTERVIEW);
            candidateRepository.save(candidate);
        }

        return interviewMapper.toDTO(updatedInterview);
    }

    @Scheduled(cron = "0 0 8 * * ?") // Executes at 8:00 AM every day
    public void sendDailyReminderEmails() {
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        ZonedDateTime startOfDay = today.atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime endOfDay = today.plusDays(1).atStartOfDay(ZoneId.systemDefault());

        List<InterviewSchedule> interviewsToday = interviewRepository.findAllByStartTimeBetween(startOfDay, endOfDay);

        interviewsToday.forEach(interview -> sendReminderEmail(interview.getId()));
    }


    @Override
    public void sendReminderEmail(UUID interviewId) {
        InterviewSchedule interview = interviewRepository.findById(interviewId)
             .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a z").withZone(ZoneId.systemDefault());

        // Format the start time
        String formattedStartTime = formatter.format(interview.getStartTime());


        // Compose the email for each interviewer
        interview.getInterviewers().forEach(interviewer -> {
            EmailRequestDTO emailRequest = new EmailRequestDTO();
            emailRequest.setTo(interviewer.getEmail());
            emailRequest.setSubject("Reminder: Interview Scheduled");
            emailRequest.setTemplateName("interview-reminder");
            emailRequest.setVariables(Map.of(
                 "interviewerName", interviewer.getFullName(),
                 "candidateName", interview.getCandidate().getFullName(),
                 "jobTitle", interview.getJob().getTitle(),
                 "schedule", formattedStartTime
            ));
            emailService.sendEmailAsync(emailRequest);
        });

        // Update the interview status to INVITED
        interview.setStatus(InterviewStatus.INVITED);
        interviewRepository.save(interview);
    }

}
