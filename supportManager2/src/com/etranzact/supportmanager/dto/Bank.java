/**
 * 
 */
package com.etranzact.supportmanager.dto;

/**
 * @author tony.ezeanya
 *
 */
public class Bank 
{

	private String bank_code;
	private String bank_nm;
	private String acronym;
	private String branchName;
	private String branchCode;
	
	public Bank(){}

	
	
	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}

	public String getBank_nm() {
		return bank_nm;
	}

	public void setBank_nm(String bank_nm) {
		this.bank_nm = bank_nm;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}



	public String getBranchName() {
		return branchName;
	}



	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}



	public String getBranchCode() {
		return branchCode;
	}



	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	
	
}
