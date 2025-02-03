package com.interviewmanagementsystem.mapper;

import com.ninja_in_pyjamas.dtos.offers.OfferCreateUpdateDTO;
import com.ninja_in_pyjamas.dtos.offers.OfferDTO;
import com.ninja_in_pyjamas.entities.Offer;
import com.ninja_in_pyjamas.entities.Role;
import com.ninja_in_pyjamas.enums.RoleName;
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

