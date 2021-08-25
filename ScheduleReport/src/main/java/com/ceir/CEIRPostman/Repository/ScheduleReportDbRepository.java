package com.ceir.CEIRPostman.Repository;
import com.ceir.CEIRPostman.model.ScheduleReportDb;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ceir.CEIRPostman.model.SystemConfigurationDb;
import java.util.List;

public interface ScheduleReportDbRepository extends JpaRepository<ScheduleReportDb, Long> {

	public List<ScheduleReportDb> getByFlag(String flag);
	public ScheduleReportDb getById(Long id);

}
