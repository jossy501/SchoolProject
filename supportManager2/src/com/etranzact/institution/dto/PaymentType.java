package com.etranzact.institution.dto;

public class PaymentType {
	
	private String paymentTypeId;
	private String description;
	private String paymentName;
	private String departmentId;
	private String payAmt;
	
	
	
	public PaymentType()
	{
		
	}



	public String getPaymentTypeId() {
		return paymentTypeId;
	}



	public void setPaymentTypeId(String paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getPaymentName() {
		return paymentName;
	}



	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}



	public String getDepartmentId() {
		return departmentId;
	}



	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}



	public String getPayAmt() {
		return payAmt;
	}



	public void setPayAmt(String payAmt) {
		this.payAmt = payAmt;
	}

   

	
	
	
}
