package com.etranzact.supportmanager.dto;

public class SMS_LOG 
{

	private String unique_transid;
	private String mobile;
	private String message_date;
	private String source_card;
	private String trans_type;
	private String response_message;
	private String response_code;
	private String response_time;
	private String issuer;
	private String response_time_in_secs;
	
	
	
	public SMS_LOG(){}
	
	public SMS_LOG(String unique_transid, String mobile, String message_date,
			String source_card, String trans_type, String response_message,
			String response_code, String response_time, String issuer) {
		this.unique_transid = unique_transid;
		this.mobile = mobile;
		this.message_date = message_date;
		this.source_card = source_card;
		this.trans_type = trans_type;
		this.response_message = response_message;
		this.response_code = response_code;
		this.response_time = response_time;
		this.issuer = issuer;
	}


	/**
	 * @return the unique_transid
	 */
	public String getUnique_transid() {
		return unique_transid;
	}


	/**
	 * @param unique_transid the unique_transid to set
	 */
	public void setUnique_transid(String unique_transid) {
		this.unique_transid = unique_transid;
	}


	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}


	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	/**
	 * @return the message_date
	 */
	public String getMessage_date() {
		return message_date;
	}


	/**
	 * @param message_date the message_date to set
	 */
	public void setMessage_date(String message_date) {
		this.message_date = message_date;
	}


	/**
	 * @return the source_card
	 */
	public String getSource_card() {
		return source_card;
	}


	/**
	 * @param source_card the source_card to set
	 */
	public void setSource_card(String source_card) {
		this.source_card = source_card;
	}


	/**
	 * @return the trans_type
	 */
	public String getTrans_type() {
		return trans_type;
	}


	/**
	 * @param trans_type the trans_type to set
	 */
	public void setTrans_type(String trans_type) {
		this.trans_type = trans_type;
	}


	/**
	 * @return the response_message
	 */
	public String getResponse_message() {
		return response_message;
	}


	/**
	 * @param response_message the response_message to set
	 */
	public void setResponse_message(String response_message) {
		this.response_message = response_message;
	}


	/**
	 * @return the response_code
	 */
	public String getResponse_code() {
		return response_code;
	}


	/**
	 * @param response_code the response_code to set
	 */
	public void setResponse_code(String response_code) {
		this.response_code = response_code;
	}


	/**
	 * @return the response_time
	 */
	public String getResponse_time() {
		return response_time;
	}


	/**
	 * @param response_time the response_time to set
	 */
	public void setResponse_time(String response_time) {
		this.response_time = response_time;
	}


	/**
	 * @return the issuer
	 */
	public String getIssuer() {
		return issuer;
	}


	/**
	 * @param issuer the issuer to set
	 */
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getResponse_time_in_secs() {
		return response_time_in_secs;
	}

	public void setResponse_time_in_secs(String response_time_in_secs) {
		this.response_time_in_secs = response_time_in_secs;
	}
	
	
	
	
}
