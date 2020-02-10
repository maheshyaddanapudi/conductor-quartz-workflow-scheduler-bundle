package com.maheshyaddanapudi.conductorquartzworkflowscheduler.db.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name="CONDUCTOR_QUARTZ_HISTORY")
public class ConductorQuartzHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "history_id", updatable = false, nullable = false)
	private Long historyId;
	
	@OneToOne
    @JoinColumn(name = "mappingId")
	@Fetch(FetchMode.SELECT)
	private ConductorQuartzMapping conductorQuartzMapping;
	
	@Column(name = "conductor_correlation_id", length = 150, nullable = true, unique=true)
	private String conductorCorrelationId;
	
	@Column(name = "quartz_execution_status")
	private boolean quartzExecutionStatus;
	
	@Column(name = "quartz_execution_log", length = 500, nullable = true, unique=false)
	private String quartzExecutionLog;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "insert_timestamp", nullable = false)
	private Date insertTimestamp;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_timestamp", nullable = false)
	private Date updateTimestamp;

	@PrePersist
	protected void onCreate() {
		updateTimestamp = insertTimestamp = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		updateTimestamp = new Date();
	}

	public ConductorQuartzHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ConductorQuartzHistory(Long historyId, ConductorQuartzMapping conductorQuartzMapping,
			String conductorCorrelationId, boolean quartzExecutionStatus, String quartzExecutionLog,
			Date insertTimestamp, Date updateTimestamp) {
		super();
		this.historyId = historyId;
		this.conductorQuartzMapping = conductorQuartzMapping;
		this.conductorCorrelationId = conductorCorrelationId;
		this.quartzExecutionStatus = quartzExecutionStatus;
		this.quartzExecutionLog = quartzExecutionLog;
		this.insertTimestamp = insertTimestamp;
		this.updateTimestamp = updateTimestamp;
	}

	public Long getHistoryId() {
		return historyId;
	}

	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
	}

	public ConductorQuartzMapping getConductorQuartzMapping() {
		return conductorQuartzMapping;
	}

	public void setConductorQuartzMapping(ConductorQuartzMapping conductorQuartzMapping) {
		this.conductorQuartzMapping = conductorQuartzMapping;
	}

	public String getConductorCorrelationId() {
		return conductorCorrelationId;
	}

	public void setConductorCorrelationId(String conductorCorrelationId) {
		this.conductorCorrelationId = conductorCorrelationId;
	}

	public boolean isQuartzExecutionStatus() {
		return quartzExecutionStatus;
	}

	public void setQuartzExecutionStatus(boolean quartzExecutionStatus) {
		this.quartzExecutionStatus = quartzExecutionStatus;
	}

	public String getQuartzExecutionLog() {
		return quartzExecutionLog;
	}

	public void setQuartzExecutionLog(String quartzExecutionLog) {
		this.quartzExecutionLog = quartzExecutionLog;
	}

	public Date getInsertTimestamp() {
		return insertTimestamp;
	}

	public void setInsertTimestamp(Date insertTimestamp) {
		this.insertTimestamp = insertTimestamp;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	@Override
	public String toString() {
		return "ConductorQuartzHistory [historyId=" + historyId + ", conductorQuartzMapping=" + conductorQuartzMapping
				+ ", conductorCorrelationId=" + conductorCorrelationId + ", quartzExecutionStatus="
				+ quartzExecutionStatus + ", quartzExecutionLog=" + quartzExecutionLog + ", insertTimestamp="
				+ insertTimestamp + ", updateTimestamp=" + updateTimestamp + "]";
	}
	
}
