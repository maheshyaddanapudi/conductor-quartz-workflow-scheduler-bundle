package com.maheshyaddanapudi.conductorquartzworkflowscheduler.dtos.responses;

public class SchedulerResponse {

	private String scheduleId;
	private String message;
	
	public SchedulerResponse(String scheduleId, String message) {
		super();
		this.scheduleId = scheduleId;
		this.message = message;
	}

	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ScheduleConductorWorkflowExecutionResponse [scheduleId=" + scheduleId + ", message=" + message + "]";
	}
}
