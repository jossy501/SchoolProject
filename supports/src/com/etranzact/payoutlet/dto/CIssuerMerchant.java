/**
 * 
 */
package com.etranzact.payoutlet.dto;

/**
 * @author tony.ezeanya
 *
 */
public class CIssuerMerchant {

	private String merchantCode;
	private String issuerCode;
	private String description;
	private String created;
	private String specialMerchant;
	private String merchantPage;
	private String allowMulitple;
	private String disabled;
	private String onlineFlag;
	
	public CIssuerMerchant(){}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getIssuerCode() {
		return issuerCode;
	}

	public void setIssuerCode(String issuerCode) {
		this.issuerCode = issuerCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getSpecialMerchant() {
		return specialMerchant;
	}

	public void setSpecialMerchant(String specialMerchant) {
		this.specialMerchant = specialMerchant;
	}

	public String getMerchantPage() {
		return merchantPage;
	}

	public void setMerchantPage(String merchantPage) {
		this.merchantPage = merchantPage;
	}

	public String getAllowMulitple() {
		return allowMulitple;
	}

	public void setAllowMulitple(String allowMulitple) {
		this.allowMulitple = allowMulitple;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public String getOnlineFlag() {
		return onlineFlag;
	}

	public void setOnlineFlag(String onlineFlag) {
		this.onlineFlag = onlineFlag;
	}
	
	
}
