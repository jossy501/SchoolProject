/**
 * 
 */
package com.etz.loanCalculator.dto;

/**
 * @author tony.ezeanya
 *
 */
public class Transaction 
{

	private String lendingHouseId;
	private String lendingHouseName;
	private String totalAmountBorrowed;
	private String totalAmountPaid;
	private String dateBorrowed;
	private String borrowerId;
	private String datePaid;
	private String amountPaid;
	private String duration;
	private String monthlyPayBack;
	
	public Transaction(){}


	public String getLendingHouseId() {
		return lendingHouseId;
	}


	public void setLendingHouseId(String lendingHouseId) {
		this.lendingHouseId = lendingHouseId;
	}


	public String getTotalAmountBorrowed() {
		return totalAmountBorrowed;
	}


	public void setTotalAmountBorrowed(String totalAmountBorrowed) {
		this.totalAmountBorrowed = totalAmountBorrowed;
	}


	public String getTotalAmountPaid() {
		return totalAmountPaid;
	}


	public void setTotalAmountPaid(String totalAmountPaid) {
		this.totalAmountPaid = totalAmountPaid;
	}


	public String getDateBorrowed() {
		return dateBorrowed;
	}


	public void setDateBorrowed(String dateBorrowed) {
		this.dateBorrowed = dateBorrowed;
	}


	public String getLendingHouseName() {
		return lendingHouseName;
	}


	public void setLendingHouseName(String lendingHouseName) {
		this.lendingHouseName = lendingHouseName;
	}


	public String getBorrowerId() {
		return borrowerId;
	}


	public void setBorrowerId(String borrowerId) {
		this.borrowerId = borrowerId;
	}


	public String getDatePaid() {
		return datePaid;
	}


	public void setDatePaid(String datePaid) {
		this.datePaid = datePaid;
	}


	public String getAmountPaid() {
		return amountPaid;
	}


	public void setAmountPaid(String amountPaid) {
		this.amountPaid = amountPaid;
	}


	public String getDuration() {
		return duration;
	}


	public void setDuration(String duration) {
		this.duration = duration;
	}


	public String getMonthlyPayBack() {
		return monthlyPayBack;
	}


	public void setMonthlyPayBack(String monthlyPayBack) {
		this.monthlyPayBack = monthlyPayBack;
	}
	
	
	
}
