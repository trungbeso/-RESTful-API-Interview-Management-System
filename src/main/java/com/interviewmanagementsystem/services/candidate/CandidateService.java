package com.interviewmanagementsystem.services.candidate;

import com.interviewmanagementsystem.dtos.candidates.CandidateCreateUpdateDTO;
import com.interviewmanagementsystem.dtos.candidates.CandidateDTO;
import com.interviewmanagementsystem.entities.Candidate;
import com.interviewmanagementsystem.entities.Employee;
import com.interviewmanagementsystem.enums.CandidateStatus;
import com.interviewmanagementsystem.exceptions.ResourceNotFoundException;
import com.interviewmanagementsystem.mapper.CandidateMapper;
import com.interviewmanagementsystem.repositories.ICandidateRepository;
import com.interviewmanagementsystem.repositories.IEmployeeRepository;
import com.interviewmanagementsystem.repositories.ISkillRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class CandidateService implements ICandidateService{
    private final ICandidateRepository candidateRepository;
    private final IEmployeeRepository employeeRepository;
    private final CandidateMapper candidateMapper;
    private final ISkillRepository skillRepository;

    @Override
    public List<CandidateDTO> getAll() {
        return candidateRepository.findAll().stream().map(candidateMapper::toDTO).toList();
    }

    @Override
    public CandidateDTO findById(UUID id) {
        Candidate candidate = candidateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
        return candidateMapper.toDTO(candidate);
    }

    @Override
    public CandidateDTO create(CandidateCreateUpdateDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Candidate cannot be null");
        }
        Candidate existingCandidate = candidateRepository.findByEmailOrPhoneNumber(dto.getEmail(), dto.getPhoneNumber());

        if (existingCandidate != null) {
            if (existingCandidate.getEmail().equals(dto.getEmail())) {
                throw new IllegalArgumentException("Candidate with email " + dto.getEmail() + " already exists");
            }

            if (existingCandidate.getPhoneNumber().equals(dto.getPhoneNumber())) {
                throw new IllegalArgumentException("Candidate with phone number " + dto.getPhoneNumber() + " already exists");
            }
        }

        Employee recruiter = employeeRepository.findById(dto.getRecruiterId()).orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));

        if (recruiter == null) {
            throw new ResourceNotFoundException("Recruiter not found");
        }

        // convert to entity
        Candidate candidate = candidateMapper.toEntity(dto);
        candidate.setRecruiter(recruiter);
        candidate.setSkills(dto.getSkillIds().stream().map(id -> skillRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Skill not found"))).collect(Collectors.toSet()));
        candidate.setStatus(CandidateStatus.OPEN);

        // save to database
        Candidate savedCandidate = candidateRepository.save(candidate);

        // convert to dto and return
        return candidateMapper.toDTO(savedCandidate);
    }

    @Override
    public CandidateDTO update(UUID id, CandidateCreateUpdateDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Candidate cannot be null");
        }

        // find existing
        Candidate existingCandidate = candidateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));


        // check email and phone
        validateUniqueEmailAndPhone(dto.getEmail(), dto.getPhoneNumber(), id);

        // check recruiter
        Employee recruiter = employeeRepository.findById(dto.getRecruiterId()).orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));

        // convert to entity
        candidateMapper.update(existingCandidate, dto);
        existingCandidate.setRecruiter(recruiter);
        existingCandidate.setSkills(dto.getSkillIds().stream().map(skillId -> skillRepository.findById(skillId).orElseThrow(() -> new ResourceNotFoundException("Skill not found"))).collect(Collectors.toSet()));
        Candidate updatedCandidate = candidateRepository.save(existingCandidate);

        return candidateMapper.toDTO(updatedCandidate);
    }

    private void validateUniqueEmailAndPhone(String email, String phoneNumber, UUID id) {
        Candidate candidateWithSameEmailOrPhone = candidateRepository.findByEmailOrPhoneNumber(email, phoneNumber);

        if (candidateWithSameEmailOrPhone != null && !candidateWithSameEmailOrPhone.getId().equals(id)) {
            if (candidateWithSameEmailOrPhone.getEmail().equals(email)) {
                throw new IllegalArgumentException("Candidate with email " + email + " already exists");
            }
            if (candidateWithSameEmailOrPhone.getPhoneNumber().equals(phoneNumber)) {
                throw new IllegalArgumentException("Candidate with phone number " + phoneNumber + " already exists");
            }
        }
    }

    @Override
    public boolean delete(UUID id) {
        Candidate candidate = candidateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
        if (!candidate.getStatus().equals(CandidateStatus.OPEN)) {
            throw new IllegalArgumentException("Candidate status must be OPEN to be deleted");
        }
        candidateRepository.delete(candidate);
        return !candidateRepository.existsById(id);
    }

    @Override
    public Page<CandidateDTO> search(String fullName,CandidateStatus status, UUID recruiterId, Pageable pageable) {
        Specification<Candidate> spec = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            // Filter by fullName
            if (fullName != null && !fullName.isEmpty()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("fullName")), "%" + fullName.toLowerCase() + "%"));
            }

            // Filter by status
            if (status != null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), status));
            }

            // Filter by recruiterId
            if (recruiterId != null) {
                predicate = cb.and(predicate, cb.equal(root.join("recruiter").get("id"), recruiterId));
            }

            return predicate;
        };
        
        return candidateRepository.findAll(spec, pageable).map(candidateMapper::toDTO);
    }

    @Override
    public List<CandidateDTO> searchByFullName(String fullName) {
        List<Candidate> candidates = fullName == null || fullName.isBlank()
            ? candidateRepository.findAll()
            : candidateRepository.findByFullNameContainingIgnoreCase(fullName);

        return candidates.stream().map(candidateMapper::toDTO).toList();
    }

    @Override
    public CandidateDTO updateStatus(UUID id, CandidateStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        Candidate candidate = candidateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        candidate.setStatus(status);
        Candidate updatedCandidate = candidateRepository.save(candidate);

        return candidateMapper.toDTO(updatedCandidate);
    }
}
