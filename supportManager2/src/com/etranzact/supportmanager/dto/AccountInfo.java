package com.etranzact.supportmanager.dto;

public class AccountInfo {
	
	
	private String companyId;
	private String bankCode;
	private String accountNo;
	private String accountDesc;
	private String createDat;
	
	public AccountInfo()
	{
		
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountDesc() {
		return accountDesc;
	}

	public void setAccountDesc(String accountDesc) {
		this.accountDesc = accountDesc;
	}

	public String getCreateDat() {
		return createDat;
	}

	public void setCreateDat(String createDat) {
		this.createDat = createDat;
	}
	
	
	

}
