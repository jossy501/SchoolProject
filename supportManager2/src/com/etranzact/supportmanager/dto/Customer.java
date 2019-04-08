package com.etranzact.supportmanager.dto;

public class Customer {
	
	private String customer_id;
	private String firstname;
	private String lastname;
	private String date_of_transaction;
	private String interested_in_what_cars;
	private String email;
	private String phone_number;
	private String customer_address;
	private String sales_person;
	
	public Customer() {
		
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
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

	public String getDate_of_transaction() {
		return date_of_transaction;
	}

	public void setDate_of_transaction(String date_of_transaction) {
		this.date_of_transaction = date_of_transaction;
	}

	public String getInterested_in_what_cars() {
		return interested_in_what_cars;
	}

	public void setInterested_in_what_cars(String interested_in_what_cars) {
		this.interested_in_what_cars = interested_in_what_cars;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getCustomer_address() {
		return customer_address;
	}

	public void setCustomer_address(String customer_address) {
		this.customer_address = customer_address;
	}

	public String getSales_person() {
		return sales_person;
	}

	public void setSales_person(String sales_person) {
		this.sales_person = sales_person;
	}
	
	

}
