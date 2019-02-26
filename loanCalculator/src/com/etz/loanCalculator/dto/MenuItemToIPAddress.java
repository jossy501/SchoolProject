/**
 * 
 */
package com.etz.loanCalculator.dto;

/**
 * @author tony.ezeanya
 *
 */
public class MenuItemToIPAddress 
{

	private String counter;
	private String id;
	private String menuitem_id;
	private String ipaddress_id;
	private String menuitem_nm;
	private String ipaddress_nm;
	private String createdBy;
	private String created_dt;
	
	public MenuItemToIPAddress(){}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMenuitem_id() {
		return menuitem_id;
	}

	public void setMenuitem_id(String menuitem_id) {
		this.menuitem_id = menuitem_id;
	}

	public String getIpaddress_id() {
		return ipaddress_id;
	}

	public void setIpaddress_id(String ipaddress_id) {
		this.ipaddress_id = ipaddress_id;
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

	public String getMenuitem_nm() {
		return menuitem_nm;
	}

	public void setMenuitem_nm(String menuitem_nm) {
		this.menuitem_nm = menuitem_nm;
	}

	public String getIpaddress_nm() {
		return ipaddress_nm;
	}

	public void setIpaddress_nm(String ipaddress_nm) {
		this.ipaddress_nm = ipaddress_nm;
	}
	
	
	
	
	
	
	
}
