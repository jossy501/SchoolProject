/**
 * 
 */
package com.etz.loanCalculator.dto;

/**
 * @author tony.ezeanya
 *
 */
public class LoanHouse 
{

	private String id;
	private String loanName;
	private String loanHouseLogo;
	private String createdBy;
	private String code;
	private String exposure;//this is the limit set by the lending house

	
	public LoanHouse(){}


	public String getLoanName() {
		return loanName;
	}


	public void setLoanName(String loanName) {
		this.loanName = loanName;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getLoanHouseLogo() {
		return loanHouseLogo;
	}


	public void setLoanHouseLogo(String loanHouseLogo) {
		this.loanHouseLogo = loanHouseLogo;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getExposure() {
		return exposure;
	}


	public void setExposure(String exposure) {
		this.exposure = exposure;
	}
	
	
	
	
}
