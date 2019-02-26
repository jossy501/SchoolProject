/**
 * 
 */
package com.etz.loanCalculator.dto;

/**
 * @author tony.ezeanya
 *
 */
public class MenuItem 
{

	private String counter;
	private String menuitem_id;
	private String menuitem_nm;
	private String menuitem_link;
	private String menuitem_comments;
	private String menu_id;
	private String createdBy;
	private String created_dt;
	private String extraParam;
	private String ip_address;
	
	
	public MenuItem(){}
	
	
	public String getMenuitem_id() {
		return menuitem_id;
	}
	public void setMenuitem_id(String menuitem_id) {
		this.menuitem_id = menuitem_id;
	}
	public String getMenuitem_nm() {
		return menuitem_nm;
	}
	public void setMenuitem_nm(String menuitem_nm) {
		this.menuitem_nm = menuitem_nm;
	}
	public String getMenuitem_link() {
		return menuitem_link;
	}
	public void setMenuitem_link(String menuitem_link) {
		this.menuitem_link = menuitem_link;
	}
	public String getMenuitem_comments() {
		return menuitem_comments;
	}
	public void setMenuitem_comments(String menuitem_comments) {
		this.menuitem_comments = menuitem_comments;
	}
	public String getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(String menu_id) {
		this.menu_id = menu_id;
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


	public String getExtraParam() {
		return extraParam;
	}


	public void setExtraParam(String extraParam) {
		this.extraParam = extraParam;
	}


	public String getIp_address() {
		return ip_address;
	}


	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}


	
	
	
	
	
	
}
