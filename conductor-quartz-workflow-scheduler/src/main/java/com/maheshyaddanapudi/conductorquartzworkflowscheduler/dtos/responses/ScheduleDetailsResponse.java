package com.maheshyaddanapudi.conductorquartzworkflowscheduler.dtos.responses;

public class ScheduleDetailsResponse {

	private Object jobDetail;
	private Object jobTrigger;
	
	public ScheduleDetailsResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ScheduleDetailsResponse(Object jobDetail, Object jobTrigger) {
		super();
		this.jobDetail = jobDetail;
		this.jobTrigger = jobTrigger;
	}

	public Object getJobDetail() {
		return jobDetail;
	}

	public void setJobDetail(Object jobDetail) {
		this.jobDetail = jobDetail;
	}

	public Object getJobTrigger() {
		return jobTrigger;
	}

	public void setJobTrigger(Object jobTrigger) {
		this.jobTrigger = jobTrigger;
	}

	@Override
	public String toString() {
		return "ScheduleDetailsResponse [jobDetail=" + jobDetail + ", jobTrigger=" + jobTrigger + "]";
	}
	
}
