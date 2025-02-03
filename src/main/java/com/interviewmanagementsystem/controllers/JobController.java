package com.interviewmanagementsystem.controllers;

import com.interviewmanagementsystem.dtos.jobs.JobCreateUpdateDTO;
import com.interviewmanagementsystem.dtos.jobs.JobDTO;
import com.interviewmanagementsystem.enums.JobStatus;
import com.interviewmanagementsystem.mapper.CustomPagedResponse;
import com.interviewmanagementsystem.services.job.IJobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/jobs")
@Tag(name = "Jobs")
public class JobController {

    private final IJobService jobService;
    private final PagedResourcesAssembler<JobDTO> pagedResourcesAssembler;


    public JobController (IJobService jobService, PagedResourcesAssembler<JobDTO> pagedResourcesAssembler){
        this.jobService=jobService;
        this.pagedResourcesAssembler=pagedResourcesAssembler;
    }

   
    // Get All
    @GetMapping
    @Operation(summary="Get all jobs")
    @ApiResponse(responseCode = "200", description = "Get all amenities successfully")
    public ResponseEntity<List<JobDTO>> getAll() {
        var result = jobService.findAll();
        return ResponseEntity.ok(result);
    }

    // Get By Id
    @GetMapping("/{id}")
    @Operation(summary = "Get job by id")
    @ApiResponse(responseCode = "200", description = "Get job by id successfully")
    @ApiResponse(responseCode = "400", description = "Job not found")
    public ResponseEntity<JobDTO> getById(
            @PathVariable("id") UUID id) {
        var result = jobService.getById(id);

        if (result == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

    // Search
    @GetMapping("/search")
    @Operation(summary = "Search jobs")
    @ApiResponse(responseCode = "200", description = "Search amenities successfully")
    public ResponseEntity<?> search(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sortBy", required = false, defaultValue = "title") String sortBy,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String order) {
        Pageable pageable = null;

        if (order.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }

        Page<JobDTO> result = jobService.searchAll(keyword, pageable);
        var pagedModel = this.pagedResourcesAssembler.toModel(result);
        Collection<EntityModel<JobDTO>> data = pagedModel.getContent();
        var links = pagedModel.getLinks();
        var response = new CustomPagedResponse<EntityModel<JobDTO>>(data, pagedModel.getMetadata(), links);
        return ResponseEntity.ok(response);
    }


    // Create
    @PostMapping
    @Operation(summary = "Create job")
    @ApiResponse(responseCode = "200", description = "Create job successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    public ResponseEntity<JobDTO> create(@RequestBody @Valid JobCreateUpdateDTO jobCreateUpdateDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        var result = jobService.create(jobCreateUpdateDTO);

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(result);
    }

     // Update
    @PutMapping("/{id}")
    @Operation(summary = "Update job")
    @ApiResponse(responseCode = "200", description = "Update job successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    public ResponseEntity<JobDTO> update(
            @PathVariable("id") UUID id,
            @RequestBody @Valid JobCreateUpdateDTO jobCreateUpdateDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        var result = jobService.update(id, jobCreateUpdateDTO);

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(result);
    }

     // Delete
    // Get By Id
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete job by id")
    @ApiResponse(responseCode = "200", description = "Delete job by id successfully")
    @ApiResponse(responseCode = "404", description = "Job not found")
    public ResponseEntity<Boolean> delete(@PathVariable("id") UUID id) {
        var result = jobService.delete(id);

        if (!result) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/status")
    @Operation(summary = "Search jobs by status with pagination")
    @ApiResponse(responseCode = "200", description = "Search jobs by status successfully")
    public ResponseEntity<?> searchByStatus(
            @RequestParam(name = "status") String status,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sortBy", required = false, defaultValue = "title") String sortBy,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String order) {

        Pageable pageable = null;

        if (order.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }

        Page<JobDTO> result = jobService.searchByStatus(status, pageable);
        var pagedModel = this.pagedResourcesAssembler.toModel(result);
        Collection<EntityModel<JobDTO>> data = pagedModel.getContent();
        var links = pagedModel.getLinks();
        var response = new CustomPagedResponse<EntityModel<JobDTO>>(data, pagedModel.getMetadata(), links);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statuses")
    public ResponseEntity<List<String>> getJobStatuses() {
        List<String> statuses = Arrays.stream(JobStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(statuses);
    }
    

}