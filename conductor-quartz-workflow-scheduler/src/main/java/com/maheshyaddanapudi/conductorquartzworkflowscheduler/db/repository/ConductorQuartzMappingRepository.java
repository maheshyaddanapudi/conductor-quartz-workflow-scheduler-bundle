package com.maheshyaddanapudi.conductorquartzworkflowscheduler.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maheshyaddanapudi.conductorquartzworkflowscheduler.db.entities.ConductorQuartzMapping;

public interface ConductorQuartzMappingRepository extends JpaRepository<ConductorQuartzMapping, Long> {
	
	ConductorQuartzMapping findByQuartzSchedulerId(String quartzSchedulerId);

}
