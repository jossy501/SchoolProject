/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.etz.loanCalculator.dto;

import java.util.Date;

import org.hibernate.validator.Digits;
import org.hibernate.validator.Email;

/**
 *
 * @author etranzact
 */
public class Borrower {
String lastname;
String firstname;
String mobile;
@Email
String email;
private Date  dob;
String address;
String nationalID;
String staffID;
String companyName;
private String monthlyIncome;
private String duration;
private String amountToBorrow;
private String id;


	public Borrower(){}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

   

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

	public String getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(String monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getAmountToBorrow() {
		return amountToBorrow;
	}

	public void setAmountToBorrow(String amountToBorrow) {
		this.amountToBorrow = amountToBorrow;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

        
}
