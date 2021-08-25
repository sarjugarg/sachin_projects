package com.ceir.GreyListProcess.repositoryImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceir.GreyListProcess.configuration.PropertiesReader;
import com.ceir.GreyListProcess.exceptions.ResourceServicesException;
import com.ceir.GreyListProcess.model.FileDumpFilter;
import com.ceir.GreyListProcess.model.GreylistDb;
import com.ceir.GreyListProcess.model.GreylistDbHistory;
import com.ceir.GreyListProcess.model.SearchCriteria;
import com.ceir.GreyListProcess.model.constants.Datatype;
import com.ceir.GreyListProcess.model.constants.SearchOperation;
import com.ceir.GreyListProcess.repository.GreyListRepository;
import com.ceir.GreyListProcess.repository.GreyListTrackRepository;
import com.ceir.GreyListProcess.specificationsbuilder.SpecificationBuilder;

@Service
public class NationalislmServiceImpl {



	@Autowired
	GreyListRepository greyListRepository;

	@Autowired
	GreyListTrackRepository greyListTrackRepository;

	@Autowired
	PropertiesReader propertiesReader;







	public List<GreylistDb> greyListDataByCreatedOn(FileDumpFilter fileDumpFilter){

		try {
			SpecificationBuilder<GreylistDb> fdsb = new SpecificationBuilder<GreylistDb>(propertiesReader.dialect);

//			if(Objects.nonNull(fileDumpFilter.getStartDate()))
//				fdsb.with(new SearchCriteria("createdOn",fileDumpFilter.getStartDate() , SearchOperation.GREATER_THAN, Datatype.DATE));

			if(Objects.nonNull(fileDumpFilter.getStartDate()))
				fdsb.with(new SearchCriteria("createdOn",fileDumpFilter.getEndDate(), SearchOperation.LESS_THAN, Datatype.DATE));

			return greyListRepository.findAll(fdsb.build());
		}
		catch(Exception e) {
			e.getMessage();
			List<GreylistDb> greyListData=new ArrayList<GreylistDb>();
			return greyListData; 

		}
	}


	public List<GreylistDbHistory> greyListHistoryDataByCreatedOn(FileDumpFilter fileDumpFilter){

		try {
			SpecificationBuilder<GreylistDbHistory> fdsb = new SpecificationBuilder<GreylistDbHistory>(propertiesReader.dialect);

			if(Objects.nonNull(fileDumpFilter.getStartDate()))
				fdsb.with(new SearchCriteria("createdOn",fileDumpFilter.getStartDate() , SearchOperation.EQUALITY, Datatype.DATE));

			return greyListTrackRepository.findAll(fdsb.build());
		}

		catch(Exception e) {
			e.getMessage();
			List<GreylistDbHistory> greyListData=new ArrayList<GreylistDbHistory>();
			return greyListData;
		}
	}
}