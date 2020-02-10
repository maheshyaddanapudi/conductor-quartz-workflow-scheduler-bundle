package com.maheshyaddanapudi.conductorquartzworkflowscheduler.db.entities;

import java.text.DecimalFormat;

import org.springframework.lang.Nullable;

public class SchedulerPortfolioStats {

	private long total;
	private long failed;
	private long success;
	
	@Nullable
	private double percentage;
	
	public SchedulerPortfolioStats() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SchedulerPortfolioStats(long total, long failed, long success) {
		super();
		this.total = total;
		this.failed = failed;
		this.success = success;
		
		if(total>0)
		{
			this.percentage = success / total * 100;
			
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			
			this.percentage = Double.parseDouble(df.format(this.percentage));
		}
		else
			this.percentage = 100;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getFailed() {
		return failed;
	}

	public void setFailed(long failed) {
		this.failed = failed;
	}

	public long getSuccess() {
		return success;
	}

	public void setSuccess(long success) {
		this.success = success;
	}

	@Override
	public String toString() {
		return "SchedulerPortfolioStats [total=" + total + ", failed=" + failed + ", success=" + success
				+ ", percentage=" + percentage + "]";
	}
}
