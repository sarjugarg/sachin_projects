package com.gl.ceir.config.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.Action;
import com.gl.ceir.config.model.ActionParameters;

@Repository
public interface ActionParametersRepository extends JpaRepository<ActionParameters, Long> {

	// @Query("SELECT ap FROM ActionParameters ap WHERE ap.action_id = :")
	public List<ActionParameters> findByAction(Action action);
}
