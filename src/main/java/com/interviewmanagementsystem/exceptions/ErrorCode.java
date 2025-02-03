package com.interviewmanagementsystem.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	INVALID_KEY(222, "Invalid message key"),
	INVALID_PASSWORD(333, "Password must have at least eight characters"),
	PASSWORD_MISMATCH(999, "Password does not match"),
	USERNAME_INVALID(444, "User name must have at least three characters"),
	UNCATEGORIZED_EXCEPTION(555, "Uncategorized Exception"),
	USER_EXISTED(666, "User already existed"),
	USER_NOT_EXISTED(777, "User not existed"),
	UNAUTHENTICATED(888, "Unauthenticated"),
	MAILTO_REQUIRED(101,"Mailto is required"),
	MAIL_SUBJECT_REQUIRED(102,"Mail subject is required"),
	INVALID_TOKEN(103, "Token is invalid"),
	EXPIRED_TOKEN(104, "Token is expired"),
	;

	private int code;
	private String message;


}
