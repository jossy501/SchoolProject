package com.etranzact.supportmanager.dto;

public class T_SMS_RECEIVE 
{

	private String trans_dt;
	private String sms_dest;
	private String sms_message;
	private String sms_originator;
	private String sms_status;
	private String sms_key;
	private String sms_id;
	private String sms_duplicate;
	private String counter;
	private String transType;
	
	public T_SMS_RECEIVE(){}
	
	public String getTrans_dt() {
		return trans_dt;
	}
	public void setTrans_dt(String trans_dt) {
		this.trans_dt = trans_dt;
	}
	public String getSms_dest() {
		return sms_dest;
	}
	public void setSms_dest(String sms_dest) {
		this.sms_dest = sms_dest;
	}
	public String getSms_message() {
		return sms_message;
	}
	public void setSms_message(String sms_message) {
		this.sms_message = sms_message;
	}
	public String getSms_originator() {
		return sms_originator;
	}
	public void setSms_originator(String sms_originator) {
		this.sms_originator = sms_originator;
	}
	public String getSms_status() {
		return sms_status;
	}
	public void setSms_status(String sms_status) {
		this.sms_status = sms_status;
	}
	public String getSms_key() {
		return sms_key;
	}
	public void setSms_key(String sms_key) {
		this.sms_key = sms_key;
	}
	public String getSms_id() {
		return sms_id;
	}
	public void setSms_id(String sms_id) {
		this.sms_id = sms_id;
	}
	public String getSms_duplicate() {
		return sms_duplicate;
	}
	public void setSms_duplicate(String sms_duplicate) {
		this.sms_duplicate = sms_duplicate;
	}

	public String getCounter() {
		return counter;
	}

	public void setCounter(String counter) {
		this.counter = counter;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}
	
	
	
}
