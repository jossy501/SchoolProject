package com.model;

public class Address {
	
	 private String id;
	 private String address_1;
	 private String address_2;
	 private String city;
	 private String state;
	 private String zipCode;
	 private String postalCode;
	 private String country;
	 
	 public Address() {
		 
	 }
	 
	 public Address(String id, String address_1,String address_2,String city, String state,String zipCode,String postalCode,String country) {
		 this.id = id;
		 this.address_1 = address_1;
		 this.address_2 = address_2;
		 this.city = city;
		 this.state = state;
		 this.zipCode = zipCode;
		 this.postalCode = postalCode;
		 this.country = country;
		 
	 }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddress_1() {
		return address_1;
	}

	public void setAddress_1(String address_1) {
		this.address_1 = address_1;
	}

	public String getAddress_2() {
		return address_2;
	}

	public void setAddress_2(String address_2) {
		this.address_2 = address_2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", address_1=" + address_1 + ", address_2=" + address_2 + ", city=" + city
				+ ", state=" + state + ", zipCode=" + zipCode + ", postalCode=" + postalCode + ", country=" + country
				+ ", getId()=" + getId() + ", getAddress_1()=" + getAddress_1() + ", getAddress_2()=" + getAddress_2()
				+ ", getCity()=" + getCity() + ", getState()=" + getState() + ", getZipCode()=" + getZipCode()
				+ ", getPostalCode()=" + getPostalCode() + ", getCountry()=" + getCountry() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	 
	 
	 
}
