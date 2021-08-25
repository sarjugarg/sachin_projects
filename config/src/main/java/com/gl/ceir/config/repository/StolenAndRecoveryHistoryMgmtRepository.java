package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gl.ceir.config.model.StolenAndRecoveryHistoryMgmt;

public interface StolenAndRecoveryHistoryMgmtRepository extends JpaRepository<StolenAndRecoveryHistoryMgmt, Long> {

}
