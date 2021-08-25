package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gl.ceir.config.model.SmsAccount;

public interface SmsAccountRepository extends JpaRepository<SmsAccount, Long> {

}
