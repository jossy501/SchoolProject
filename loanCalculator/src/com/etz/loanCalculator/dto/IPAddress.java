/**
 * 
 */
package com.etz.loanCalculator.dto;

/**
 * @author tony.ezeanya
 *
 */
public class IPAddress 
{

	private String counter;
	private String id;
	private String ip_address;
	private String createdBy;
	private String created_dt;
	
	
	public IPAddress(){}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getIp_address() {
		return ip_address;
	}


	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public String getCreated_dt() {
		return created_dt;
	}


	public void setCreated_dt(String created_dt) {
		this.created_dt = created_dt;
	}


	public String getCounter() {
		return counter;
	}


	public void setCounter(String counter) {
		this.counter = counter;
	}
	
	

}
