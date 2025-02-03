package com.interviewmanagementsystem.configuration;

import com.interviewmanagementsystem.services.auth.token.ITokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class JWTFilter extends GenericFilterBean {

	private final ITokenService tokenService;

	public JWTFilter(ITokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Override
	public void doFilter(
		  ServletRequest servletRequest,
		  ServletResponse servletResponse,
		  FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		String bearerToken = httpServletRequest.getHeader("Authorization");
		String jwtToken = null;

		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			jwtToken = bearerToken.substring(7);
		}

		Authentication authentication = tokenService.getAuthentication(jwtToken);

		if (authentication != null) {
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}
}
