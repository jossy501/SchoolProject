/**
 * 
 */
package com.etranzact.payoutlet.dto;

/**
 * @author tony.ezeanya
 *
 */
public class CAgency {
	
	private String merchantCode;
	private String agencyId;
	private String agencyName;
	

	public CAgency(){}


	public String getMerchantCode() {
		return merchantCode;
	}


	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}


	public String getAgencyId() {
		return agencyId;
	}


	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}


	public String getAgencyName() {
		return agencyName;
	}


	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	
	
	
	
}
