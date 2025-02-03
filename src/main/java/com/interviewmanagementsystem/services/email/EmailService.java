package com.interviewmanagementsystem.services.email;

import com.ninja_in_pyjamas.dtos.email.EmailRequestDTO;
import com.ninja_in_pyjamas.exceptions.AppException;
import com.ninja_in_pyjamas.exceptions.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EmailService implements IEmailService{

	JavaMailSender mailSender;
	TemplateEngine templateEngine;

	@NonFinal
	@Value("${spring.mail.from")
	private String from;


	@Async
	@Override
	public void sendEmailAsync(EmailRequestDTO request) {
		if (request.getTo() == null || request.getTo().isEmpty()) {
			throw new AppException(ErrorCode.MAILTO_REQUIRED);
		}

		if (request.getSubject() == null || request.getSubject().isEmpty()) {
			throw new AppException(ErrorCode.MAIL_SUBJECT_REQUIRED);
		}

		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message,true);
			helper.setFrom(from);
			helper.setTo(request.getTo());
			helper.setSubject(request.getSubject());
			helper.setText(generateEmailBody(request.getTemplateName(), request), true);

			if (request.getCc() != null && !request.getCc().isEmpty()) {
				helper.setCc(request.getCc());
			}

			if (request.getBcc() != null && !request.getBcc().isEmpty()) {
				helper.setBcc(request.getBcc());
			}

			mailSender.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException("Failed to send email",e);
		}
	}

	private String generateEmailBody(String templateName, EmailRequestDTO request) {
		Context context = new Context();

		context.setVariables(request.getVariables());

		return templateEngine.process(templateName, context);
	}
}
