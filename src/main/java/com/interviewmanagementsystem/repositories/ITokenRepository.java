package com.interviewmanagementsystem.repositories;

import com.ninja_in_pyjamas.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITokenRepository extends JpaRepository<PasswordResetToken, Long> {
}
