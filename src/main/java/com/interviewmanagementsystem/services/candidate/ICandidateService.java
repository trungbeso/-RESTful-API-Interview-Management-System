package com.interviewmanagementsystem.services.candidate;

import com.ninja_in_pyjamas.dtos.candidates.CandidateCreateUpdateDTO;
import com.ninja_in_pyjamas.dtos.candidates.CandidateDTO;
import com.ninja_in_pyjamas.enums.CandidateStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ICandidateService {
    List<CandidateDTO> getAll();

    CandidateDTO findById(UUID id);

    CandidateDTO create(CandidateCreateUpdateDTO dto);

    CandidateDTO update(UUID id, CandidateCreateUpdateDTO dto);

    boolean delete(UUID id);

    Page<CandidateDTO> search(String fullName,CandidateStatus status, UUID recruiterId, Pageable pageable);

    List<CandidateDTO> searchByFullName(String fullName);

    CandidateDTO updateStatus(UUID id, CandidateStatus status);
}