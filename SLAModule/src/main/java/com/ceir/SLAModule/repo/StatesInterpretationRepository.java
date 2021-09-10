package com.ceir.SLAModule.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ceir.SLAModule.entity.StatesInterpretationDb;

@Repository
public interface StatesInterpretationRepository extends JpaRepository<StatesInterpretationDb, Long>, JpaSpecificationExecutor<StatesInterpretationDb> {

	public List<StatesInterpretationDb> findByFeatureId(Integer featureId);
	
	public StatesInterpretationDb findByFeatureIdAndState(Integer featureId, int state);

}
