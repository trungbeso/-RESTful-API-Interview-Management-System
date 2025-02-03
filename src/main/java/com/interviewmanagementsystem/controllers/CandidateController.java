package com.interviewmanagementsystem.controllers;

import com.interviewmanagementsystem.dtos.candidates.CandidateCreateUpdateDTO;
import com.interviewmanagementsystem.dtos.candidates.CandidateDTO;
import com.interviewmanagementsystem.enums.CandidateStatus;
import com.interviewmanagementsystem.mapper.CustomPagedResponse;
import com.interviewmanagementsystem.services.candidate.ICandidateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/candidates")
@RequiredArgsConstructor
@Tag(name = "Candidates", description = "APIs for managing candidates")
public class CandidateController {
    private final ICandidateService candidateService;
    private final PagedResourcesAssembler<CandidateDTO> pagedResourcesAssembler;


    @Operation(summary = "Get all candidates")
    @ApiResponse(responseCode = "200", description = "Return all candidates")
    @GetMapping
    public ResponseEntity<List<CandidateDTO>> getAll() {
        List<CandidateDTO> candidateDTOS = candidateService.getAll();
        return ResponseEntity.ok().body(candidateDTOS);
    }

    @Operation(summary = "Get candidate by id")
    @ApiResponse(responseCode = "200", description = "Return candidate that match the id")
    @GetMapping("/{id}")
    public ResponseEntity<CandidateDTO> getById(@PathVariable UUID id) {
        CandidateDTO candidateDTO = candidateService.findById(id);
        return ResponseEntity.ok().body(candidateDTO);
    }

    @Operation(summary = "Create candidate")
    @ApiResponse(responseCode = "200", description = "Return created candidate")
    @ApiResponse(responseCode = "400", description = "Return error message if failed")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid CandidateCreateUpdateDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        CandidateDTO createdCandidate  = candidateService.create(dto);

        if (createdCandidate == null) {
            return ResponseEntity.badRequest().body("Candidate creation failed");
        }
        return ResponseEntity.ok().body(createdCandidate);
    }

    @Operation(summary = "Update candidate by id")
    @ApiResponse(responseCode = "200", description = "Return updated candidate")
    @ApiResponse(responseCode = "400", description = "Return error message if failed")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id,@RequestBody @Valid CandidateCreateUpdateDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        CandidateDTO updatedCandidate = candidateService.update(id, dto);

        if (updatedCandidate == null) {
            return ResponseEntity.badRequest().body("Candidate creation failed");
        }
        return ResponseEntity.ok().body(updatedCandidate);
    }


    @Operation(summary = "Delete candidate by id")
    @ApiResponse(responseCode = "200", description = "Return true if delete successfully")
    @ApiResponse(responseCode = "400", description = "Return error message if delete failed")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable UUID id) {
        boolean result = candidateService.delete(id);
        if (!result) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "Search candidates")
    @ApiResponse(responseCode = "200", description = "Return candidates that match the keyword")
    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(name = "fullName", defaultValue = "") String fullName,
            @RequestParam(name = "status", defaultValue = "") CandidateStatus status,
            @RequestParam(name = "recruiterId", defaultValue = "") UUID recruiterId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size, 
            @RequestParam(name = "sortBy",  defaultValue = "fullName") String sortBy,
            @RequestParam(name = "order", defaultValue = "asc") String order) {
        Pageable pageable = null;
        
        if (order.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }

        Page<CandidateDTO> result = candidateService.search(fullName, status, recruiterId, pageable);
        
        PagedModel<EntityModel<CandidateDTO>> pagedModel = pagedResourcesAssembler.toModel(result);
        
        // Get data, page, and links from pagedModel
        Collection<EntityModel<CandidateDTO>> data = pagedModel.getContent();
        
        Links links = pagedModel.getLinks();
        
        var response = new CustomPagedResponse<EntityModel<CandidateDTO>>(data, pagedModel.getMetadata(), links);
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search candidates by full name")
    @ApiResponse(responseCode = "200", description = "Return candidates that match the keyword")
    @GetMapping("/searchByFullName")
    public ResponseEntity<List<CandidateDTO>> searchByFullName(@RequestParam(name = "keyword", defaultValue = "") String keyword) {
        List<CandidateDTO> candidates = candidateService.searchByFullName(keyword);
        return ResponseEntity.ok().body(candidates);
    }

    @Operation(summary = "Update candidate status by id")
    @ApiResponse(responseCode = "200", description = "Return updated candidate")
    @ApiResponse(responseCode = "400", description = "Return error message if failed")
    @PatchMapping("/{id}/status")
    public ResponseEntity<CandidateDTO> updateStatus(@PathVariable UUID id,@RequestParam CandidateStatus status) {
        CandidateDTO candidateDTO = candidateService.updateStatus(id, status);
        return ResponseEntity.ok().body(candidateDTO);
    }
}
