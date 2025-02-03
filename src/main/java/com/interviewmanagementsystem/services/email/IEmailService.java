package com.interviewmanagementsystem.services.email;

import com.interviewmanagementsystem.dtos.email.EmailRequestDTO;
import org.springframework.scheduling.annotation.Async;

public interface IEmailService {
	@Async
	void sendEmailAsync(EmailRequestDTO request);
}
