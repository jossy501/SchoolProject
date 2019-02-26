package com.etranzact.supportmanager.dto;

public class Summary 
{

	private String summary_count;
	private String summary_name;
	private String summary_success_status;
	private String summary_failed_status;
	private String summary_pending_status;
	private String summaryamount;
	private String summarydate;
	
	
	public Summary(){}


	public String getSummary_count() {
		return summary_count;
	}


	public void setSummary_count(String summary_count) {
		this.summary_count = summary_count;
	}


	public String getSummary_name() {
		return summary_name;
	}


	public void setSummary_name(String summary_name) {
		this.summary_name = summary_name;
	}


	public String getSummary_success_status() {
		return summary_success_status;
	}


	public void setSummary_success_status(String summary_success_status) {
		this.summary_success_status = summary_success_status;
	}


	public String getSummary_failed_status() {
		return summary_failed_status;
	}


	public void setSummary_failed_status(String summary_failed_status) {
		this.summary_failed_status = summary_failed_status;
	}


	public String getSummary_pending_status() {
		return summary_pending_status;
	}


	public void setSummary_pending_status(String summary_pending_status) {
		this.summary_pending_status = summary_pending_status;
	}


	public String getSummaryamount() {
		return summaryamount;
	}


	public void setSummaryamount(String summaryamount) {
		this.summaryamount = summaryamount;
	}


	public String getSummarydate() {
		return summarydate;
	}


	public void setSummarydate(String summarydate) {
		this.summarydate = summarydate;
	}


	
	
	
	
}
