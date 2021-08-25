package com.ceir.GreyListProcess.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceir.GreyListProcess.model.FileDumpMgmt;


public interface FileDumpMgmtRepository extends JpaRepository<FileDumpMgmt, Long> {

	public List<FileDumpMgmt> getByServiceDump(String serviceDump);
    public FileDumpMgmt findTopByDumpTypeOrderByIdDesc(String dumpType);
    public FileDumpMgmt findTopByDumpTypeAndServiceDumpOrderByIdDesc(String dumpType,int serviceDump);
    public FileDumpMgmt save(FileDumpMgmt fileDumpMgmt);
}