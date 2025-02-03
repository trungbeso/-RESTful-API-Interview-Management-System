package com.interviewmanagementsystem.controllers;

import com.ninja_in_pyjamas.dtos.auth.PasswordResetRequestDTO;
import com.ninja_in_pyjamas.dtos.auth.ValidateResetPasswordTokenRequestDTO;
import com.ninja_in_pyjamas.services.auth.AuthService;
import com.ninja_in_pyjamas.services.auth.PasswordService;
import com.ninja_in_pyjamas.services.employee.ChangePasswordRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/forgotPassword")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ForgotPasswordController {

	AuthService authService;
	private final PasswordService passwordService;

	@PostMapping("/request-reset-password")
	@Operation(summary = "Request reset password", description = "Request reset password API")
	public ResponseEntity<?> requestResetPassword(@Valid @RequestBody PasswordResetRequestDTO requestDTO
		  , BindingResult bindingResult) {
		//validate request
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(bindingResult.getAllErrors().toString());
		}
		// request reset password
		var result = authService.requestResetPassword(requestDTO);
		return ResponseEntity.ok().body("password reset successful" + result);
	}

	@PostMapping("/validate-reset-password-token")
	@Operation(summary = "Validate reset password token", description = "Validate reset password token API")
	public ResponseEntity<?> validateResetPasswordToken(@Valid @RequestBody ValidateResetPasswordTokenRequestDTO requestDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(bindingResult.getAllErrors().toString());
		}

		try {
			boolean result = passwordService.validatePasswordResetToken(requestDTO.getToken());
			return ResponseEntity.ok().body(result);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}

	@PostMapping("/change-password")
	@Operation(summary = "Change password", description = "Change password API")
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequestDTO requestDTO, BindingResult bindingResult) {
		//validate request
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(bindingResult.getAllErrors().toString());
		}
		//change password
		var result = authService.changePassword(requestDTO);
		return ResponseEntity.ok().body(result);
	}
}
