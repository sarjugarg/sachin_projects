package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gl.ceir.config.model.ImmegreationFileDetails;

public interface ImmegreationFileDertailsRepository extends JpaRepository<ImmegreationFileDetails, Long> {


	public 	ImmegreationFileDetails save(ImmegreationFileDetails immegreationFileDetails);

}
