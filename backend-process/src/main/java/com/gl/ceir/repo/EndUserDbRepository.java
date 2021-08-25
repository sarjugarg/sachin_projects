package com.gl.ceir.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.entity.EndUserDB;

@Repository
public interface EndUserDbRepository extends RevisionRepository<EndUserDB, Long, Long>, 
JpaRepository<EndUserDB, Long>, JpaSpecificationExecutor<EndUserDB> {

	public EndUserDB getByTxnId(String txnId);

	public EndUserDB getByNid(String nid);
	
	public void deleteByNid(String nid);
	
}
