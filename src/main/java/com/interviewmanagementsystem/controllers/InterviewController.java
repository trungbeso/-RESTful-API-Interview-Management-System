package com.interviewmanagementsystem.controllers;

import com.ninja_in_pyjamas.dtos.interviews.InterviewCreateUpdateDTO;
import com.ninja_in_pyjamas.dtos.interviews.InterviewDTO;
import com.ninja_in_pyjamas.enums.InterviewResult;
import com.ninja_in_pyjamas.enums.InterviewStatus;
import com.ninja_in_pyjamas.services.interview.IInterviewService;
import com.ninja_in_pyjamas.utils.CustomPagedResponse;
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
@RequestMapping("api/interviews")
@RequiredArgsConstructor
@Tag(name = "Interviews", description = "APIs for managing interviews")
public class InterviewController {
    private final IInterviewService interviewService;
    private final PagedResourcesAssembler<InterviewDTO> pagedResourcesAssembler;

    @Operation(summary = "Get all interviews")
    @ApiResponse(responseCode = "200", description = "Return all interviews")
    @GetMapping
    public ResponseEntity<List<InterviewDTO>> getAll() {
        var interviews = interviewService.getAll();
        return ResponseEntity.ok().body(interviews);
    }

    @Operation(summary = "Get interview by id")
    @ApiResponse(responseCode = "200", description = "Return interview that match the id")
    @GetMapping("/{id}")
    public ResponseEntity<InterviewDTO> getById(@PathVariable UUID id) {
        var interview = interviewService.findById(id);
        return ResponseEntity.ok().body(interview);
    }


    @Operation(summary = "Create interview")
    @ApiResponse(responseCode = "200", description = "Return created interview")
    @ApiResponse(responseCode = "400", description = "Return error message if failed")
    @PostMapping()
    public ResponseEntity<?> create(@RequestBody @Valid InterviewCreateUpdateDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        InterviewDTO createdInterview = interviewService.create(dto);

        if (createdInterview == null) {
            return ResponseEntity.badRequest().body("Interview creation failed");
        }
        return ResponseEntity.ok().body(createdInterview);
    }


    @Operation(summary = "Update interview by id")
    @ApiResponse(responseCode = "200", description = "Return updated interview")
    @ApiResponse(responseCode = "400", description = "Return error message if failed")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id,@RequestBody @Valid InterviewCreateUpdateDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        InterviewDTO updatedInterview = interviewService.update(id, dto);

        if (updatedInterview == null) {
            return ResponseEntity.badRequest().body("Interview creation failed");
        }
        return ResponseEntity.ok().body(updatedInterview);
    }

    @Operation(summary = "Delete interview by id")
    @ApiResponse(responseCode = "200", description = "Return true if delete successfully")
    @ApiResponse(responseCode = "400", description = "Return error message if delete failed")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable UUID id) {
        var result = interviewService.delete(id);
        if (!result) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "Search interviews with pagination")
    @ApiResponse(responseCode = "200", description = "Return interviews that match the search criteria")
    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(name = "title", defaultValue = "") String title,
            @RequestParam(name = "status", defaultValue = "") InterviewStatus status,
            @RequestParam(name = "interviewerId", defaultValue = "") UUID interviewerId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy",  defaultValue = "title") String sortBy,
            @RequestParam(name = "order", defaultValue = "asc") String order) {
        Pageable pageable = null;
        
        if (order.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }

        Page<InterviewDTO> result = interviewService.search(title, status, interviewerId, pageable);
        
        PagedModel<EntityModel<InterviewDTO>> pagedModel = pagedResourcesAssembler.toModel(result);
        
        // Get data, page, and links from pagedModel
        Collection<EntityModel<InterviewDTO>> data = pagedModel.getContent();
        
        Links links = pagedModel.getLinks();
        
        var response = new CustomPagedResponse<EntityModel<InterviewDTO>>(data, pagedModel.getMetadata(), links);
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update interview status by id")
    @ApiResponse(responseCode = "200", description = "Return updated interview with new status")
    @ApiResponse(responseCode = "400", description = "Return error message if update failed")
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable UUID id, @RequestParam InterviewStatus status) {
        InterviewDTO updatedInterview = interviewService.updateStatus(id, status);
        return ResponseEntity.ok(updatedInterview);
    }

    @Operation(summary = "Update interview result and note by id")
    @ApiResponse(responseCode = "200", description = "Return updated interview with new result and note")
    @ApiResponse(responseCode = "400", description = "Return error message if update failed")
    @PatchMapping("/{id}/result")
    public ResponseEntity<?> updateResultAndNote(
            @PathVariable UUID id,
            @RequestParam InterviewResult result,
            @RequestParam String note) {
        InterviewDTO updatedInterview = interviewService.updateResultAndNote(id, result, note);
        return ResponseEntity.ok(updatedInterview);
    }

    @Operation(summary = "Send reminder email to interviewers")
    @ApiResponse(responseCode = "200", description = "Reminder email sent successfully")
    @ApiResponse(responseCode = "404", description = "Interview not found")
    @PostMapping("/{id}/send-reminder")
    public ResponseEntity<Void> sendReminder(@PathVariable UUID id) {
        interviewService.sendReminderEmail(id);
        return ResponseEntity.ok().build();
    }

}
