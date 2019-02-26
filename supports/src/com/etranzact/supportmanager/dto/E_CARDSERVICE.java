/**
 * 
 */
package com.etranzact.supportmanager.dto;

/**
 * @author tony.ezeanya
 *
 */
public class E_CARDSERVICE 
{

	private String counter;
	private String card_num;
	private String service_id;
	private String status;
	private String created_dt;
	private String modified;
	private String param0;
	
	public E_CARDSERVICE(){}
	
	public String getCard_num() {
		return card_num;
	}
	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}
	public String getService_id() {
		return service_id;
	}
	public void setService_id(String service_id) {
		this.service_id = service_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreated_dt() {
		return created_dt;
	}
	public void setCreated_dt(String created_dt) {
		this.created_dt = created_dt;
	}
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getCounter() {
		return counter;
	}

	public void setCounter(String counter) {
		this.counter = counter;
	}

	public String getParam0() {
		return param0;
	}

	public void setParam0(String param0) {
		this.param0 = param0;
	}
	
	
	
	
}
