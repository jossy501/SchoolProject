/**
 * 
 */
package com.etz.loanCalculator.dto;

/**
 * @author tony.ezeanya
 *
 */
public class MenuItemToRole 
{
	private String counter;
	private String id;
	private String menuItemToMenu_id;
	private String userType_id;
	private String createdBy;
	private String created_dt;
	
	private String menuItem_nm;
	private String menu_nm;
	private String userType_nm;
	
	
	
	public MenuItemToRole(){}


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


	


	public String getUserType_id() {
		return userType_id;
	}


	public void setUserType_id(String userType_id) {
		this.userType_id = userType_id;
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


	public String getUserType_nm() {
		return userType_nm;
	}


	public void setUserType_nm(String userType_nm) {
		this.userType_nm = userType_nm;
	}


	public String getMenuItem_nm() {
		return menuItem_nm;
	}


	public void setMenuItem_nm(String menuItem_nm) {
		this.menuItem_nm = menuItem_nm;
	}


	public String getMenu_nm() {
		return menu_nm;
	}


	public void setMenu_nm(String menu_nm) {
		this.menu_nm = menu_nm;
	}


	public String getMenuItemToMenu_id() {
		return menuItemToMenu_id;
	}


	public void setMenuItemToMenu_id(String menuItemToMenu_id) {
		this.menuItemToMenu_id = menuItemToMenu_id;
	}
	
	
	
	
	
}
