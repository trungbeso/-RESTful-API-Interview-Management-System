package com.interviewmanagementsystem.repositories;

import com.interviewmanagementsystem.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITokenRepository extends JpaRepository<PasswordResetToken, Long> {
}
