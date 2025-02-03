package com.interviewmanagementsystem.services.employee;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.Normalizer;

@Service
public class EmployeeHelpers implements IEmployeeHelpers {

	@Override
	public String generateUsername(String fullName) {
		if (fullName == null || fullName.trim().isEmpty()) {
			throw new IllegalArgumentException("Name cannot be null or empty");
		}

		// Split the full name into parts by spaces
		String[] parts = fullName.trim().toLowerCase().split("\\s+");

		// Ensure the name has at least two parts
		if (parts.length < 2) {
			throw new IllegalArgumentException("Name must have at least two parts");
		}

		// Extract the last name, first name initial, and middle name initial
		String lastName = parts[parts.length - 1];
		String firstNameInitial = String.valueOf(parts[0].charAt(0));
		StringBuilder middleNameInitial = new StringBuilder();

		for (int i = 1; i < parts.length - 1; i++) {
			middleNameInitial.append(parts[i].charAt(0));
		}

		// Combine the initials and the last name
		return removeDiacritics(lastName + firstNameInitial + middleNameInitial);
	}

	@Override
	public String removeDiacritics(String input) {
		if (input == null || input.trim().isEmpty()) {
			return "";
		}
		// Bỏ dấu tất cả ký tự
		String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

		// Kết hợp từ cuối với các chữ cái đầu
		return normalized.replaceAll("\\p{M}", "")
			  .replaceAll("[đĐ]", "d")
			  .replaceAll("[Ôô]", "o")
			  .replaceAll("[Ơơ]", "o")
			  .replaceAll("[Ưư]", "u")
			  .replaceAll("[Êê]", "e")
			  .replaceAll("[Ăă]", "a")
			  .replaceAll("[Ââ]", "a");
	}

	public static UserDetails getCurrentUser() {
		return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
