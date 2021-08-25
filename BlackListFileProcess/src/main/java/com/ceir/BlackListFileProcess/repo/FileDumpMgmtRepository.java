package com.ceir.BlackListFileProcess.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ceir.BlackListFileProcess.model.FileDumpMgmt;


@Repository
public interface FileDumpMgmtRepository extends JpaRepository<FileDumpMgmt, Long> {

	public List<FileDumpMgmt> getByServiceDump(String serviceDump);
    public FileDumpMgmt findTopByDumpTypeAndServiceDumpOrderByIdDesc(String dumpType,int serviceDump);
    public FileDumpMgmt save(FileDumpMgmt fileDumpMgmt);
}