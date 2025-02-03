package com.interviewmanagementsystem.services.auth.token;

import org.springframework.security.core.Authentication;

public interface ITokenService {
	String generateToken(Authentication authentication);

	Authentication getAuthentication(String token);
}