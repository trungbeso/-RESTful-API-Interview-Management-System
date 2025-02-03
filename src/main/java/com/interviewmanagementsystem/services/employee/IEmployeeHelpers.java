package com.interviewmanagementsystem.services.employee;

public interface IEmployeeHelpers {
	String generateUsername(String fullName);

	String removeDiacritics(String input);
}
