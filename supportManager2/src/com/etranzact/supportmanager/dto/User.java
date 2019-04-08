/**
 * 
 */
package com.etranzact.supportmanager.dto;

import org.hibernate.validator.Email;

/**
 * @author Etrantact
 *
 */
public class User 
{
	
	private String user_id;
	@Email
	private String email;
	private String password;
	private String firstname;
	private String lastname;
	private String fullname;
	private String username;
	private String type_id;
	private boolean loggedIn;
	private String mobile;
	private String type_nm;
	private String status_id;
	private String status_nm;
	private String user_code;
	private String first_logon;
	
	private String old_password;
	private String confirm_password;
	private String account_id;
	
	private String user_code_nm;
	private String dealer_nm;
	private String service_id;
	private String esa_code;
	private String esa_code_email;
	private String esa_type;
	private String esa_question;
	private String esa_answer;
	
	public User(){}


	public String getUser_id() {
		return user_id;
	}


	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getFirstname() {
		return firstname;
	}


	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	public String getLastname() {
		return lastname;
	}


	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	public String getFullname() {
		return fullname;
	}


	public void setFullname(String fullname) {
		this.fullname = fullname;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}




	public boolean isLoggedIn() {
		return loggedIn;
	}


	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}


	public String getType_id() {
		return type_id;
	}


	public void setType_id(String type_id) {
		this.type_id = type_id;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getType_nm() {
		return type_nm;
	}


	public void setType_nm(String type_nm) {
		this.type_nm = type_nm;
	}


	public String getStatus_id() {
		return status_id;
	}


	public void setStatus_id(String status_id) {
		this.status_id = status_id;
	}


	public String getStatus_nm() {
		return status_nm;
	}


	public void setStatus_nm(String status_nm) {
		this.status_nm = status_nm;
	}


	public String getUser_code() {
		return user_code;
	}


	public void setUser_code(String user_code) {
		this.user_code = user_code;
	}


	public String getOld_password() {
		return old_password;
	}


	public void setOld_password(String old_password) {
		this.old_password = old_password;
	}


	public String getConfirm_password() {
		return confirm_password;
	}


	public void setConfirm_password(String confirm_password) {
		this.confirm_password = confirm_password;
	}


	public String getFirst_logon() {
		return first_logon;
	}


	public void setFirst_logon(String first_logon) {
		this.first_logon = first_logon;
	}


	public String getAccount_id() {
		return account_id;
	}


	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}


	public String getUser_code_nm() {
		return user_code_nm;
	}


	public void setUser_code_nm(String user_code_nm) {
		this.user_code_nm = user_code_nm;
	}


	public String getDealer_nm() {
		return dealer_nm;
	}


	public void setDealer_nm(String dealer_nm) {
		this.dealer_nm = dealer_nm;
	}


	public String getService_id() {
		return service_id;
	}


	public void setService_id(String service_id) {
		this.service_id = service_id;
	}


	public String getEsa_code() {
		return esa_code;
	}


	public void setEsa_code(String esa_code) {
		this.esa_code = esa_code;
	}


	public String getEsa_type() {
		return esa_type;
	}


	public void setEsa_type(String esa_type) {
		this.esa_type = esa_type;
	}


	public String getEsa_question() {
		return esa_question;
	}


	public void setEsa_question(String esa_question) {
		this.esa_question = esa_question;
	}


	public String getEsa_code_email() {
		return esa_code_email;
	}


	public void setEsa_code_email(String esa_code_email) {
		this.esa_code_email = esa_code_email;
	}


	public String getEsa_answer() {
		return esa_answer;
	}


	public void setEsa_answer(String esa_answer) {
		this.esa_answer = esa_answer;
	}
	
	
	
}
