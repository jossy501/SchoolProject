/**
 * 
 */
package com.etz.loanCalculator.dto;

/**
 * @author tony.ezeanya
 *
 */
public class MenuToRole 
{

	private String counter;
	private String id;
	private String menu_id;
	private String userType_id;
	
	private String menu_nm;
	private String userType_nm;
	
	private String createdBy;
	private String created_dt;
	
	public MenuToRole(){}

	public String getCounter() {
		return counter;
	}

	public void setCounter(String counter) {
		this.counter = counter;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(String menu_id) {
		this.menu_id = menu_id;
	}

	public String getUserType_id() {
		return userType_id;
	}

	public void setUserType_id(String userType_id) {
		this.userType_id = userType_id;
	}

	public String getMenu_nm() {
		return menu_nm;
	}

	public void setMenu_nm(String menu_nm) {
		this.menu_nm = menu_nm;
	}

	public String getUserType_nm() {
		return userType_nm;
	}

	public void setUserType_nm(String userType_nm) {
		this.userType_nm = userType_nm;
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
	
	
	
	
	
	
}
