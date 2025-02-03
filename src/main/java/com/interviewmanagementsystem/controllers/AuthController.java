package com.interviewmanagementsystem.controllers;

import com.interviewmanagementsystem.dtos.auth.LoginRequestDTO;
import com.interviewmanagementsystem.dtos.auth.LoginResponseDTO;
import com.interviewmanagementsystem.dtos.auth.RegisterRequestDTO;
import com.interviewmanagementsystem.services.auth.AuthService;
import com.interviewmanagementsystem.services.auth.token.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Auth", description = "Authentication APIs")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthController {
	AuthService authService;
	AuthenticationManagerBuilder authenticationManagerBuilder;
	TokenService tokenService;

	// Login Api
	@PostMapping("/login")
	@Operation(summary = "Log-in", description = "Login Api")
	public ResponseEntity<?> Login(@Valid @RequestBody LoginRequestDTO loginRequestDTO,
	                               BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
		}

		//create authentication token with username + pass
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
			  loginRequestDTO.getUsername(), loginRequestDTO.getPassword());

		//authenticate user
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

		//set authentication to security context
		SecurityContextHolder.getContext().setAuthentication(authentication);

		//create jwt token
		String accessToken = tokenService.generateToken(authentication);

		var employeeInformation = authService.getEmployeeInformationDTO(loginRequestDTO.getUsername());

		//create response
		LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
		loginResponseDTO.setToken(accessToken);
		loginResponseDTO.setUser(employeeInformation);

		return ResponseEntity.ok(loginResponseDTO);
	}

	// Register Api
	@PostMapping("/register")
	@Operation(summary = "Register", description = "Register Api")
	public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO,
	                                  BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
		}
		var result = authService.register(registerRequestDTO);

		return ResponseEntity.ok(result);
	}

}
