package com.ceir.BlackListProcess.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ceir.BlackListProcess.model.FileDumpMgmt;

public interface FileDumpMgmtRepository extends JpaRepository<FileDumpMgmt, Long> {
	public List<FileDumpMgmt> getByServiceDump(String serviceDump);
    public FileDumpMgmt findTopByDumpTypeOrderByIdDesc(String dumpType);
    public FileDumpMgmt findTopByServiceDumpOrderByIdDesc(int  serviceDump);
    public FileDumpMgmt save(FileDumpMgmt fileDumpMgmt);
}