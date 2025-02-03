package com.interviewmanagementsystem.services.email;

import com.ninja_in_pyjamas.dtos.email.EmailRequestDTO;
import org.springframework.scheduling.annotation.Async;

public interface IEmailService {


	//String sendEmailResetPassword(EmployeeResetPasswordRequest requestDTO);

	@Async
	void sendEmailAsync(EmailRequestDTO request);
}
