/**
 * 
 */
package com.etranzact.supportmanager.dto;

/**
 * @author tony.ezeanya
 *
 */
public class E_SETTLE_BATCH 
{

	private String batch_id;
	private String batch_date;
	private String closed;
	private String start_date;
	private String end_date;
	private String counter;
	private String amount;
	
	private String total_count;
	private String total_amount;
	
	public E_SETTLE_BATCH(){}

	public String getBatch_id() {
		return batch_id;
	}

	public void setBatch_id(String batch_id) {
		this.batch_id = batch_id;
	}

	public String getBatch_date() {
		return batch_date;
	}

	public void setBatch_date(String batch_date) {
		this.batch_date = batch_date;
	}

	public String getClosed() {
		return closed;
	}

	public void setClosed(String closed) {
		this.closed = closed;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getCounter() {
		return counter;
	}

	public void setCounter(String counter) {
		this.counter = counter;
	}

	public String getTotal_count() {
		return total_count;
	}

	public void setTotal_count(String total_count) {
		this.total_count = total_count;
	}

	public String getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	
	
}
