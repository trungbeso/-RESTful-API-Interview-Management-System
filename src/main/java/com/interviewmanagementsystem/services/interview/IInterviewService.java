package com.interviewmanagementsystem.services.interview;

import com.ninja_in_pyjamas.dtos.interviews.InterviewCreateUpdateDTO;
import com.ninja_in_pyjamas.dtos.interviews.InterviewDTO;
import com.ninja_in_pyjamas.enums.InterviewResult;
import com.ninja_in_pyjamas.enums.InterviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IInterviewService {
    List<InterviewDTO> getAll();

    InterviewDTO findById(UUID id);

    InterviewDTO create(InterviewCreateUpdateDTO dto);

    InterviewDTO update(UUID id, InterviewCreateUpdateDTO dto);

    boolean delete(UUID id);

    Page<InterviewDTO> search(String title, InterviewStatus status, UUID interviewerId, Pageable pageable);

    InterviewDTO updateStatus(UUID id, InterviewStatus status);

    InterviewDTO updateResultAndNote(UUID id, InterviewResult result, String note);

    void sendReminderEmail(UUID interviewId);
}
