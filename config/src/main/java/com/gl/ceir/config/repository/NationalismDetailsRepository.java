package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gl.ceir.config.model.NationlismDetails;

public interface NationalismDetailsRepository extends JpaRepository<NationlismDetails, Long> {



	public NationlismDetails  save(NationlismDetails nationlismDetails);

	public NationlismDetails findByPassportNumberOrVisaNumber(String passportNumber ,String visaNumber);

	public NationlismDetails getByPassportNumber(String passportNumber);

	
	
	
}
