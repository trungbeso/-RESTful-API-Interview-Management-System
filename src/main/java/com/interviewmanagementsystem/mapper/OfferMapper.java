package com.interviewmanagementsystem.mapper;


import com.interviewmanagementsystem.dtos.offers.OfferCreateUpdateDTO;
import com.interviewmanagementsystem.dtos.offers.OfferDTO;
import com.interviewmanagementsystem.entities.Offer;
import com.interviewmanagementsystem.entities.Role;
import com.interviewmanagementsystem.enums.RoleName;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OfferMapper {
   OfferDTO toDTO (Offer entity);

   Offer toEntity (OfferCreateUpdateDTO dto);

   @Mapping(target = "insertedAt", ignore = true)
   @Mapping(target = "updatedAt", ignore = true)
   @Mapping(target = "deletedAt", ignore = true)
   void updateEntity (OfferCreateUpdateDTO dto, @MappingTarget Offer entity);

   default Set<RoleName> mapRoles(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}

