package com.ceir.BlackListProcess.repoimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceir.BlackListProcess.model.GreylistDb;
import com.ceir.BlackListProcess.model.GreylistDbHistory;
import com.ceir.BlackListProcess.repository.GreyListRepository;
import com.ceir.BlackListProcess.repository.GreyListTrackRepository;
import org.apache.log4j.Logger;

@Service
public class NationalislmServiceImpl {

    private final Logger log = Logger.getLogger(getClass());
    @Autowired
    GreyListRepository greyListRepository;

    @Autowired
    GreyListTrackRepository greyListTrackRepository;

    public GreylistDbHistory saveGreyListHistory(GreylistDbHistory greyListData) {

        GreylistDbHistory greyList = new GreylistDbHistory();
        try {
            greyList = greyListTrackRepository.save(greyListData);
            return greyList;
        } catch (Exception e) {
            return null;
        }
    }

    public int deleteGreyListById(long id) {

        try {
            greyListRepository.deleteById(id);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public List<GreylistDb> findAllGreyListData() {
        List<GreylistDb> greyListData = new ArrayList<GreylistDb>();
        try {
            greyListData = greyListRepository.findAll();
            log.info(this);
            return greyListData;
        } catch (Exception e) {
            log.warn(e);
            return new ArrayList<GreylistDb>();
        }
    }

}

/*
	 * @Autowired PropertiesReader propertiesReader;
	 * 
	 * public List<GreylistDb> greyListDataByCreatedOn(FileDumpFilter
	 * fileDumpFilter){
	 * 
	 * try { SpecificationBuilder<GreylistDb> fdsb = new
	 * SpecificationBuilder<GreylistDb>(propertiesReader.dialect);
	 * 
	 * if(Objects.nonNull(fileDumpFilter.getStartDate())) fdsb.with(new
	 * SearchCriteria("createdOn",fileDumpFilter.getStartDate() ,
	 * SearchOperation.GREATER_THAN, Datatype.DATE));
	 * 
	 * if(Objects.nonNull(fileDumpFilter.getStartDate())) fdsb.with(new
	 * SearchCriteria("createdOn",fileDumpFilter.getEndDate(),
	 * SearchOperation.LESS_THAN, Datatype.DATE));
	 * 
	 * return greyListRepository.findAll(fdsb.build()); } catch(Exception e) {
	 * e.getMessage(); List<GreylistDb> greyListData=new ArrayList<GreylistDb>();
	 * return greyListData;
	 * 
	 * } }
	 * 
	 * 
	 * public List<GreylistDbHistory> greyListHistoryDataByCreatedOn(FileDumpFilter
	 * fileDumpFilter){
	 * 
	 * try { SpecificationBuilder<GreylistDbHistory> fdsb = new
	 * SpecificationBuilder<GreylistDbHistory>(propertiesReader.dialect);
	 * 
	 * if(Objects.nonNull(fileDumpFilter.getStartDate())) fdsb.with(new
	 * SearchCriteria("createdOn",fileDumpFilter.getStartDate() ,
	 * SearchOperation.EQUALITY, Datatype.DATE));
	 * 
	 * return greyListTrackRepository.findAll(fdsb.build()); }
	 * 
	 * catch(Exception e) { e.getMessage(); List<GreylistDbHistory> greyListData=new
	 * ArrayList<GreylistDbHistory>(); return greyListData; } }
 */
