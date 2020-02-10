package com.maheshyaddanapudi.conductorquartzworkflowscheduler.dtos.requests;

import javax.validation.constraints.NotEmpty;

import org.springframework.lang.Nullable;

public class ScheduleConductorWorkflowExecutionRequest {

	@NotEmpty
    private String workflowName;
    
    private int workflowVersion;
    
    @NotEmpty
    private String scheduleName;
    
    @Nullable
    private String scheduleDesc;
	
    @NotEmpty
    private String json;
    
    @NotEmpty
    private String cronExpression;
    
    @Nullable
    private String startDate;
    
    @NotEmpty
    private String endDate;

	public ScheduleConductorWorkflowExecutionRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ScheduleConductorWorkflowExecutionRequest(@NotEmpty String workflowName, int workflowVersion,
			@NotEmpty String scheduleName, @NotEmpty String scheduleDesc, @NotEmpty String json,
			@NotEmpty String cronExpression, String startDate, @NotEmpty String endDate) {
		super();
		this.workflowName = workflowName;
		this.workflowVersion = workflowVersion;
		this.scheduleName = scheduleName;
		this.scheduleDesc = scheduleDesc;
		this.json = json;
		this.cronExpression = cronExpression;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public int getWorkflowVersion() {
		return workflowVersion;
	}

	public void setWorkflowVersion(int workflowVersion) {
		this.workflowVersion = workflowVersion;
	}

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public String getScheduleDesc() {
		return scheduleDesc;
	}

	public void setScheduleDesc(String scheduleDesc) {
		this.scheduleDesc = scheduleDesc;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "ScheduleConductorWorkflowExecutionRequest [workflowName=" + workflowName + ", workflowVersion="
				+ workflowVersion + ", scheduleName=" + scheduleName + ", scheduleDesc=" + scheduleDesc + ", json="
				+ json + ", cronExpression=" + cronExpression + ", startDate=" + startDate + ", endDate=" + endDate
				+ "]";
	}
}