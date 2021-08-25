package com.ceir.BlackListFileProcess.repoServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceir.BlackListFileProcess.configuration.PropertiesReader;
import com.ceir.BlackListFileProcess.model.BlackList;
import com.ceir.BlackListFileProcess.model.BlacklistDbHistory;
import com.ceir.BlackListFileProcess.model.FileDumpFilter;
import com.ceir.BlackListFileProcess.model.SearchCriteria;
import com.ceir.BlackListFileProcess.model.constants.Datatype;
import com.ceir.BlackListFileProcess.model.constants.SearchOperation;
import com.ceir.BlackListFileProcess.repo.BlackListRepository;
import com.ceir.BlackListFileProcess.repo.BlackListTrackDetailsRepository;
import com.ceir.BlackListFileProcess.specificationbuilder.SpecificationBuilder;

@Service
public class BlackListRepoImpl {

	@Autowired
	BlackListRepository blackListRepo;

	@Autowired
	BlackListTrackDetailsRepository blackListHistoryRepo;
	
	
	@Autowired
	PropertiesReader propertiesReader;
	
	@Autowired
	BlackListRepository blackRepo;
	
	@Autowired
	BlackListTrackDetailsRepository blackListHistory;
	

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
	
	public List<BlackList> blackListDataByexpiryDate(FileDumpFilter fileDumpFilter){

		try {
			SpecificationBuilder<BlackList> fdsb = new SpecificationBuilder<BlackList>(propertiesReader.dialect);

//			if(Objects.nonNull(fileDumpFilter.getStartDate()))
//				fdsb.with(new SearchCriteria("expiryDate",fileDumpFilter.getStartDate() , SearchOperation.GREATER_THAN, Datatype.DATE));

			if(Objects.nonNull(fileDumpFilter.getStartDate()))
				fdsb.with(new SearchCriteria("expiryDate",fileDumpFilter.getEndDate(), SearchOperation.LESS_THAN, Datatype.DATE));

			return blackRepo.findAll(fdsb.build());
		}
		catch(Exception e) {
			e.getMessage();
			return new ArrayList<BlackList>(); 

		}
	}


	public List<BlacklistDbHistory> blackListHistoryDataByExpiryDate(FileDumpFilter fileDumpFilter){

		try {
			SpecificationBuilder<BlacklistDbHistory> fdsb = new SpecificationBuilder<BlacklistDbHistory>(propertiesReader.dialect);

			if(Objects.nonNull(fileDumpFilter.getStartDate()))
				if(Objects.nonNull(fileDumpFilter.getStartDate()))
					fdsb.with(new SearchCriteria("expiryDate",fileDumpFilter.getStartDate() , SearchOperation.GREATER_THAN, Datatype.DATE));

				if(Objects.nonNull(fileDumpFilter.getStartDate()))
					fdsb.with(new SearchCriteria("expiryDate",fileDumpFilter.getEndDate(), SearchOperation.LESS_THAN, Datatype.DATE));

			return blackListHistory.findAll(fdsb.build());
		}

		catch(Exception e) {
			e.getMessage();
			return new ArrayList<BlacklistDbHistory>();
		}
	}
	

}
