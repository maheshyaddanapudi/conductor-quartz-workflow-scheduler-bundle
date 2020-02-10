package com.maheshyaddanapudi.conductorquartzworkflowscheduler.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maheshyaddanapudi.conductorquartzworkflowscheduler.db.entities.ConductorQuartzHistory;
import com.maheshyaddanapudi.conductorquartzworkflowscheduler.db.entities.ConductorQuartzMapping;
import com.maheshyaddanapudi.conductorquartzworkflowscheduler.db.entities.SchedulerPortfolioStats;

public interface ConductorQuartzHistoryRepository extends JpaRepository<ConductorQuartzHistory, Long> {
	
	List<ConductorQuartzHistory> findByConductorQuartzMapping(ConductorQuartzMapping conductorQuartzHistory);

	@Query(value="SELECT new com.maheshyaddanapudi.conductorquartzworkflowscheduler.db.entities.SchedulerPortfolioStats(count(*), (SELECT count(*) FROM ConductorQuartzHistory WHERE quartzExecutionStatus = false ), (SELECT count(*) FROM ConductorQuartzHistory WHERE quartzExecutionStatus = true)) FROM ConductorQuartzHistory")
	SchedulerPortfolioStats getSchedulerPortfolioStats();
}
