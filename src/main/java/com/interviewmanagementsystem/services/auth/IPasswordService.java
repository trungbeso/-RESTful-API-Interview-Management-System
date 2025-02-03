package com.interviewmanagementsystem.services.auth;

public interface IPasswordService {


	String generatePassword(int length);


	String hashPassword(String rawPassword);

	boolean verifyPassword(String rawPassword, String encodedPassword);

	String generatePasswordResetToken(String email);

	boolean validatePasswordResetToken(String token);

	String getEmailFromPasswordResetToken(String token);

	boolean deletePasswordResetToken(String token);

	boolean matchHashedPassword(String oldPassword, String hashedPassword);
}
