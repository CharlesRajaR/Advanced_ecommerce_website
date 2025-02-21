package com.rcr.repository;

import com.rcr.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    public VerificationCode findByEmail(String email);
}
