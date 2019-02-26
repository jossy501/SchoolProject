/**
 * 
 */
package com.etranzact.supportmanager.dto;

/**
 * @author tony.ezeanya
 *
 */
public class E_MERCHANT 
{
	private String counter;
	private String merchant_code;
	private String merchant_acct;
	private String merchant_pin;
	private String merchant_name;
	private String account_id;
	private String transAmount;
	
	public E_MERCHANT(){}

	

	public String getTransAmount() {
		return transAmount;
	}




	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}




	public String getMerchant_code() {
		return merchant_code;
	}

	public void setMerchant_code(String merchant_code) {
		this.merchant_code = merchant_code;
	}

	public String getMerchant_acct() {
		return merchant_acct;
	}

	public void setMerchant_acct(String merchant_acct) {
		this.merchant_acct = merchant_acct;
	}

	public String getMerchant_pin() {
		return merchant_pin;
	}

	public void setMerchant_pin(String merchant_pin) {
		this.merchant_pin = merchant_pin;
	}

	public String getMerchant_name() {
		return merchant_name;
	}

	public void setMerchant_name(String merchant_name) {
		this.merchant_name = merchant_name;
	}

	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}

	public String getCounter() {
		return counter;
	}

	public void setCounter(String counter) {
		this.counter = counter;
	}
	
	
	
}
