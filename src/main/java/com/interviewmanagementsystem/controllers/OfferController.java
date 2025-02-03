package com.interviewmanagementsystem.controllers;

import com.interviewmanagementsystem.dtos.offers.OfferCreateUpdateDTO;
import com.interviewmanagementsystem.dtos.offers.OfferDTO;
import com.interviewmanagementsystem.enums.OfferStatus;
import com.interviewmanagementsystem.mapper.CustomPagedResponse;
import com.interviewmanagementsystem.services.offer.IOfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/offers")
@Tag(name = "Offers")
public class OfferController {
        private final IOfferService offerService;
    private final PagedResourcesAssembler<OfferDTO> pagedResourcesAssembler;

    public OfferController(IOfferService offerService, PagedResourcesAssembler<OfferDTO> pagedResourcesAssembler) {
        this.offerService = offerService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    // Get All
    @GetMapping
    @Operation(summary = "Get all offers")
    @ApiResponse(responseCode = "200", description = "Get all offers successfully")
    public ResponseEntity<List<OfferDTO>> getAll() {
        var result = offerService.findAll();
        return ResponseEntity.ok(result);
    }

    // Get By Id
    @GetMapping("/{id}")
    @Operation(summary = "Get offer by id")
    @ApiResponse(responseCode = "200", description = "Get offer by id successfully")
    @ApiResponse(responseCode = "404", description = "Offer not found")
    public ResponseEntity<OfferDTO> getById(
            @PathVariable("id") UUID id) {
        var result = offerService.findById(id);

        if (result == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

    // Search
    @GetMapping("/search")
    @Operation(summary = "Search offers")
    @ApiResponse(responseCode = "200", description = "Search offers successfully")
    public ResponseEntity<?> search(
            @RequestParam(name = "candidateName", required = false, defaultValue = "") String candidateName,
            @RequestParam(name = "status", required = false, defaultValue = "") OfferStatus status,
            @RequestParam(name = "departmentId", required = false, defaultValue = "") UUID departmentId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String order) {
        Pageable pageable = null;

        if (order.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }

        Page<OfferDTO> result = offerService.searchAll(candidateName, status, departmentId, pageable);

        var pagedModel = pagedResourcesAssembler.toModel(result);

        // Get data, page, and links from pagedModel
        Collection<EntityModel<OfferDTO>> data = pagedModel.getContent();

        var links = pagedModel.getLinks();

        var response = new CustomPagedResponse<EntityModel<OfferDTO>>(data, pagedModel.getMetadata(), links);

        return ResponseEntity.ok(response);
    }

    // Create
    @PostMapping()
    @Operation(summary = "Create offer")
    @ApiResponse(responseCode = "200", description = "Create offer successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    public ResponseEntity<OfferDTO> create(@RequestBody @Valid OfferCreateUpdateDTO offerCreateUpdateDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        var result = offerService.create(offerCreateUpdateDTO);

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(result);
    }

    // Update
    @PutMapping("/{id}")
    @Operation(summary = "Update offer")
    @ApiResponse(responseCode = "200", description = "Update offer successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    public ResponseEntity<OfferDTO> update(
            @PathVariable("id") UUID id,
            @RequestBody @Valid OfferCreateUpdateDTO offerCreateUpdateDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        var result = offerService.update(id, offerCreateUpdateDTO);

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(result);
    }

    // UC27: Approve/ Reject offer  
    @PostMapping("/{id}/approve")  
    public ResponseEntity<Void> approveOffer(@PathVariable UUID id) {  
        offerService.approveOffer(id);  
        return ResponseEntity.noContent().build();  
    }  

    @PostMapping("/{id}/reject")  
    public ResponseEntity<Void> rejectOffer(@PathVariable UUID id) {  
        offerService.rejectOffer(id);  
        return ResponseEntity.noContent().build();  
    }  

    @PatchMapping("/{id}/mark-sent")
    public OfferDTO markAsSentToCandidate(@PathVariable UUID id) {
        return offerService.markAsSentToCandidate(id);
    }

    // UC28: Update offer status from Candidate  
    @PostMapping("/{id}/status")  
    public ResponseEntity<Void> updateOfferStatus(@PathVariable UUID id, @RequestParam OfferStatus status) {  
        offerService.updateOfferStatus(id, status);  
        return ResponseEntity.noContent().build();  
    }

    // Delete
    // Get By Id
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete offer by id")
    @ApiResponses(value = {  
        @ApiResponse(responseCode = "200", description = "Successfully deleted the offer"),  
        @ApiResponse(responseCode = "404", description = "Offer not found with the given ID")
    })
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        offerService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportOffers(@RequestParam UUID departmentId,
                                               @RequestParam OfferStatus status) {
        byte[] data = offerService.exportOffers(departmentId, status);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=offers.csv");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }
}

