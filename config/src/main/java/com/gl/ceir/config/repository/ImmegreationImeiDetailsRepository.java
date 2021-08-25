package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gl.ceir.config.model.SingleImeiDetails;

public interface ImmegreationImeiDetailsRepository extends JpaRepository<SingleImeiDetails, Long> {

	public SingleImeiDetails save(SingleImeiDetails immegreationImeiDetails);


}
