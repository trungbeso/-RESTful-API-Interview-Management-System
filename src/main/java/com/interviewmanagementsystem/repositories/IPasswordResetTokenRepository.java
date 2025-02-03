package com.interviewmanagementsystem.repositories;

import com.ninja_in_pyjamas.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IPasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
	Optional<PasswordResetToken> findByHashedTokenId(String hashedTokenId);

	Optional<PasswordResetToken> findByEmail(String email);

	void deleteByHashedTokenId(String hashedTokenId);
}
