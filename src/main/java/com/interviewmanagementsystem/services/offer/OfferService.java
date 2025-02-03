package com.interviewmanagementsystem.services.offer;

import com.interviewmanagementsystem.dtos.offers.OfferCreateUpdateDTO;
import com.interviewmanagementsystem.dtos.offers.OfferDTO;
import com.interviewmanagementsystem.entities.Candidate;
import com.interviewmanagementsystem.entities.Department;
import com.interviewmanagementsystem.entities.Employee;
import com.interviewmanagementsystem.entities.Offer;
import com.interviewmanagementsystem.enums.CandidateStatus;
import com.interviewmanagementsystem.enums.OfferStatus;
import com.interviewmanagementsystem.exceptions.ResourceNotFoundException;
import com.interviewmanagementsystem.mapper.OfferMapper;
import com.interviewmanagementsystem.repositories.ICandidateRepository;
import com.interviewmanagementsystem.repositories.IDepartmentRepository;
import com.interviewmanagementsystem.repositories.IEmployeeRepository;
import com.interviewmanagementsystem.repositories.IOfferRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OfferService implements IOfferService {

    private final IOfferRepository offerRepository;
    private final IEmployeeRepository employeeRepository;
    private final ICandidateRepository candidateRepository;
    private final IDepartmentRepository departmentRepository;
    private final OfferMapper offerMapper;

    @Override
    public OfferDTO create(OfferCreateUpdateDTO offerDTO) {
        if (offerDTO == null) {
            throw new IllegalArgumentException("Offer cannot be null");
        }

        Employee recruiter = employeeRepository.findById(offerDTO.getRecruiterId())
                            .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
        Candidate candidate = candidateRepository.findById(offerDTO.getCandidateId())
                            .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
        Department department = departmentRepository.findById(offerDTO.getDepartmentId())
                            .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        Employee approver = employeeRepository.findById(offerDTO.getApproverId()) 
                            .orElseThrow(() -> new ResourceNotFoundException("Approver not found"));                    

        if (candidate.getStatus() == CandidateStatus.BANNED) {
            throw new ResourceNotFoundException("Candidate is banned");
        }

        Offer offer = offerMapper.toEntity(offerDTO);
        offer.setApprover(approver);
        offer.setCandidate(candidate);
        offer.setDepartment(department);
        offer.setRecruiter(recruiter);
        offer.setStatus(OfferStatus.WAITING_FOR_APPROVAL);

        offerRepository.save(offer);
        return offerMapper.toDTO(offer);
    }

    @Override
    public OfferDTO update(UUID id, OfferCreateUpdateDTO offerDTO) {
        if (offerDTO == null) {
            throw new IllegalArgumentException("Offer cannot be null");
        }

        Offer existingOffer = offerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Offer not found"));

        Employee recruiter = employeeRepository.findById(offerDTO.getRecruiterId()).orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
        Candidate candidate = candidateRepository.findById(offerDTO.getCandidateId()).orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
        Department department = departmentRepository.findById(offerDTO.getDepartmentId())
                            .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        Employee approver = employeeRepository.findById(offerDTO.getApproverId()).orElseThrow(() -> new ResourceNotFoundException("Approver not found"));

        offerMapper.updateEntity(offerDTO, existingOffer);
        existingOffer.setRecruiter(recruiter);
        existingOffer.setCandidate(candidate);
        existingOffer.setDepartment(department);
        existingOffer.setApprover(approver);
    
        offerRepository.save(existingOffer);

        return offerMapper.toDTO(existingOffer);
    }

    @Override
    public OfferDTO findById(UUID id) {
        Offer offer = offerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Offer not found"));
        return offerMapper.toDTO(offer);
    }

    @Override
    public List<OfferDTO> findAll() {
        return offerRepository.findAll().stream().map(offerMapper::toDTO).toList();
    }

    @Override
    public Page<OfferDTO> searchAll(String candidateName, OfferStatus status,  UUID departmentId, Pageable pageable) {
        Specification<Offer> spec = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            // Filter by status

            if (candidateName != null && !candidateName.isEmpty()) {
                Join<Object, Object> candidates = root.join("candidate", JoinType.INNER);
                predicate = cb.and(predicate, cb.like(candidates.get("name"), "%" + candidateName + "%"));
            }
            
            if (status != null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), status));
            }

            if (departmentId != null) {
                Join<Object, Object> departments = root.join("department", JoinType.INNER);
                predicate = cb.and(predicate, cb.equal(departments.get("id"), departmentId));
            }

            return predicate;
        };

        return offerRepository.findAll(spec, pageable).map(offerMapper::toDTO);
    }


    // UC27: Approve Offer  
    @Override
    public void approveOffer(UUID id) {  
        Offer offer = offerRepository.findById(id)  
                .orElseThrow(() -> new EntityNotFoundException("Offer not found"));  

        offer.setStatus(OfferStatus.APPROVED);  
        offerRepository.save(offer);  
    }  

    // UC27: Reject Offer  
    @Override
    public void rejectOffer(UUID id) {  
        Offer offer = offerRepository.findById(id)  
                .orElseThrow(() -> new EntityNotFoundException("Offer not found"));  
        offer.setStatus(OfferStatus.REJECTED);  
        offerRepository.save(offer);  
    }  

    @Override
    public OfferDTO markAsSentToCandidate(UUID id) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Offer not found"));
        if (offer.getStatus() != OfferStatus.APPROVED) {
            throw new IllegalStateException("Offer must be approved before being sent to the candidate.");
        }
        offer.setStatus(OfferStatus.WAITING_FOR_RESPONSE);
        return offerMapper.toDTO(offerRepository.save(offer));
    }

    // UC28: Update Offer Status  
    @Override
    public void updateOfferStatus(UUID id, OfferStatus status) {  
        Offer offer = offerRepository.findById(id)  
                .orElseThrow(() -> new EntityNotFoundException("Offer not found"));  
        
        // Assume that status is a valid OfferStatus enum value  
        offer.setStatus(status);  
        offerRepository.save(offer);  
    }

    @Override
    public void delete(UUID id) {
        offerRepository.deleteById(id);
    }

    @Override
    public byte[] exportOffers(UUID departmentId, OfferStatus status) {
        List<Offer> offers = offerRepository.findByDepartmentIdAndStatus(departmentId, status);
        return generateCsvFile(offers);
    }

    private byte[] generateCsvFile(List<Offer> offers) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(out)) {

            // Header row
            writer.printf("Offer ID,Candidate Name,Position,Department,Status,Approver,BasicSalary%n");

            // Data rows
            for (Offer offer : offers) {
                writer.printf("%s,%s,%s,%s,%s,%s,%s%n",
                        offer.getId(),
                        offer.getCandidate().getFullName(),
                        offer.getPosition(),
                        offer.getDepartment() != null ? offer.getDepartment().getName() : "", 
                        offer.getStatus(),
                        offer.getApprover() != null ? offer.getApprover().getFullName() : "",
                        offer.getBasicSalary());
            }

            writer.flush();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating CSV file", e);
        }
    }
}

