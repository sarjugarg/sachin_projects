package com.ceir.BlackListProcess.repoimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceir.BlackListProcess.configuration.PropertiesReaders;
import com.ceir.BlackListProcess.model.BlackList;
import com.ceir.BlackListProcess.model.BlacklistDbHistory;
import com.ceir.BlackListProcess.model.FileDumpFilter;
import com.ceir.BlackListProcess.model.GreylistDb;
import com.ceir.BlackListProcess.model.GreylistDbHistory;
import com.ceir.BlackListProcess.model.SearchCriteria;
import com.ceir.BlackListProcess.model.constants.Datatype;
import com.ceir.BlackListProcess.model.constants.SearchOperation;
import com.ceir.BlackListProcess.repository.BlackListRepository;
import com.ceir.BlackListProcess.repository.BlackListTrackDetailsRepository;
import com.ceir.BlackListProcess.specificationbuilder.SpecificationBuilder;

@Service
public class BlackListRepoImpl {

	@Autowired
	BlackListRepository blackListRepo;

	@Autowired
	BlackListTrackDetailsRepository blackListHistoryRepo;
	
	@Autowired
	PropertiesReaders propertiesReader;

	public BlackList saveBlackList(BlackList blackList) {
		BlackList output=new BlackList();
		try {
			output=blackListRepo.save(blackList);
			return output;
		}
		catch(Exception e) {
			return null;
		}
	}

	public BlacklistDbHistory saveBlackListHistory(BlacklistDbHistory blackListHistory) {
		BlacklistDbHistory output=new BlacklistDbHistory();
		try {
			output=blackListHistoryRepo.save(blackListHistory);
			return output;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public List<BlackList> blackListDataByCreatedOn(FileDumpFilter fileDumpFilter){
	
		try {
			SpecificationBuilder<BlackList> fdsb = new SpecificationBuilder<BlackList>(propertiesReader.dialect);

			if(Objects.nonNull(fileDumpFilter.getStartDate()))
				fdsb.with(new SearchCriteria("createdOn",fileDumpFilter.getStartDate() , SearchOperation.GREATER_THAN, Datatype.DATE));

			if(Objects.nonNull(fileDumpFilter.getStartDate()))
				fdsb.with(new SearchCriteria("createdOn",fileDumpFilter.getEndDate(), SearchOperation.LESS_THAN, Datatype.DATE));

			return blackListRepo.findAll(fdsb.build());
		}
		catch(Exception e) {
			e.getMessage();
			List<BlackList> greyListData=new ArrayList<BlackList>();
			return greyListData; 

		}
	}


	public List<BlacklistDbHistory> blackListHistoryDataByCreatedOn(FileDumpFilter fileDumpFilter){

		try {
			SpecificationBuilder<BlacklistDbHistory> fdsb = new SpecificationBuilder<BlacklistDbHistory>(propertiesReader.dialect);

			if(Objects.nonNull(fileDumpFilter.getStartDate()))
				fdsb.with(new SearchCriteria("createdOn",fileDumpFilter.getStartDate() , SearchOperation.EQUALITY, Datatype.DATE));

			return blackListHistoryRepo.findAll(fdsb.build());
		}

		catch(Exception e) {
			e.getMessage();
			List<BlacklistDbHistory> greyListData=new ArrayList<BlacklistDbHistory>();
			return greyListData;
		}
	}


}
