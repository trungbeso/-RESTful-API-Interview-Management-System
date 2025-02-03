package com.interviewmanagementsystem.services.offer;

import com.interviewmanagementsystem.dtos.offers.OfferCreateUpdateDTO;
import com.interviewmanagementsystem.dtos.offers.OfferDTO;
import com.interviewmanagementsystem.enums.OfferStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IOfferService {
    OfferDTO create(OfferCreateUpdateDTO offerDTO);

    OfferDTO update(UUID id, OfferCreateUpdateDTO offerDTO);

    OfferDTO findById(UUID id);

    List<OfferDTO> findAll();

    Page<OfferDTO> searchAll(String candidateName, OfferStatus status, UUID departmentId, Pageable pageable);

    void approveOffer(UUID id);

    void rejectOffer(UUID id);

    OfferDTO markAsSentToCandidate(UUID id);

    void updateOfferStatus(UUID id, OfferStatus status);

    void delete(UUID id);

    byte[] exportOffers(UUID departmentId, OfferStatus status);
}

