package com.interviewmanagementsystem.repositories;


import com.interviewmanagementsystem.entities.Offer;
import com.interviewmanagementsystem.enums.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IOfferRepository extends JpaRepository<Offer, UUID>, JpaSpecificationExecutor<Offer> {
    List<Offer> findByDepartmentIdAndStatus(UUID departmentId, OfferStatus status);
}
