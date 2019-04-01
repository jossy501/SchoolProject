package com.model;


public class Customer {
	
	private String guld;
	private String firstname;
	private String lastname;
	private String midInit;
	private String dateOfBirth;
	private String gender;
	
	public Customer() {
		
	}
	
	public Customer(String guld,String firstname, String lastname, String midInit,String dateOfBirth, String gender ) {
		this.guld = guld;
		this.firstname = firstname;
		this.lastname = lastname;
		this.midInit = midInit;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		
	}

	public String getGuld() {
		return guld;
	}

	public void setGuld(String guld) {
		this.guld = guld;
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

	public String getMidInit() {
		return midInit;
	}

	public void setMidInit(String midInit) {
		this.midInit = midInit;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "customer [guld=" + guld + ", firstname=" + firstname + ", lastname=" + lastname + ", midInit=" + midInit
				+ ", dateOfBirth=" + dateOfBirth + ", gender=" + gender + ", getGuld()=" + getGuld()
				+ ", getFirstname()=" + getFirstname() + ", getLastname()=" + getLastname() + ", getMidInit()="
				+ getMidInit() + ", getDateOfBirth()=" + getDateOfBirth() + ", getGender()=" + getGender()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}
	
	
	
	
	
	

}
