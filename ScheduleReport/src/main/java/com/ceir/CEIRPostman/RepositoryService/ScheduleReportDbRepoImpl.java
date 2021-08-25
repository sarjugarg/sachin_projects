package com.ceir.CEIRPostman.RepositoryService;

import com.ceir.CEIRPostman.Repository.ScheduleReportDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ceir.CEIRPostman.Repository.SystemConfigurationDbRepository;
import com.ceir.CEIRPostman.model.ScheduleReportDb;
import com.ceir.CEIRPostman.model.SystemConfigurationDb;
import java.util.List;

@Service
public class ScheduleReportDbRepoImpl {

    @Autowired
    ScheduleReportDbRepository scheduleReportDbRepository;

    public List<ScheduleReportDb> getDataByFlag(String flag) {
        try {
//		SystemConfigurationDb systemConfigDb=new SystemConfigurationDb();
            List<ScheduleReportDb> reportData = scheduleReportDbRepository.getByFlag(flag);
            return reportData;
        } catch (Exception e) {
            return null;
        }
    }
}
