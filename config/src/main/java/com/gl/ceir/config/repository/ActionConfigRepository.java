package com.gl.ceir.config.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.ActionsConfigDb;

@Repository
public interface ActionConfigRepository extends JpaRepository<ActionsConfigDb, Long>, JpaSpecificationExecutor<ActionsConfigDb> {

	public List<ActionsConfigDb> getByStateId(long stateId);

}
