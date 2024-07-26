package com.E1i3.NoExit.domain.mail.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.E1i3.NoExit.domain.mail.domain.MailVerify;

public interface MailVerifyRepository extends JpaRepository<MailVerify, Long> {
}
