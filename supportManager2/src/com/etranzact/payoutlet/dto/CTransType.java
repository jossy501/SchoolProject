/**
 * 
 */
package com.etranzact.payoutlet.dto;

/**
 * @author tony.ezeanya
 *
 */
public class CTransType 
{

	private String merchantCode;
	private String transId;
	private String typeName;
	private Double typeAmount = 0.0d;
	private String typeSymbol;
	private Double typeQuantity = 0.0d;
	private Double addFee = 0.0d;
	private String isMust;
	private String agencyId;
	private String altMerchantCode;
	
	
	public CTransType(){}


	public String getMerchantCode() {
		return merchantCode;
	}


	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}


	public String getTransId() {
		return transId;
	}


	public void setTransId(String transId) {
		this.transId = transId;
	}


	public String getTypeName() {
		return typeName;
	}


	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}


	public Double getTypeAmount() {
		return typeAmount;
	}


	public void setTypeAmount(Double typeAmount) {
		this.typeAmount = typeAmount;
	}


	public String getTypeSymbol() {
		return typeSymbol;
	}


	public void setTypeSymbol(String typeSymbol) {
		this.typeSymbol = typeSymbol;
	}


	public Double getTypeQuantity() {
		return typeQuantity;
	}


	public void setTypeQuantity(Double typeQuantity) {
		this.typeQuantity = typeQuantity;
	}


	public Double getAddFee() {
		return addFee;
	}


	public void setAddFee(Double addFee) {
		this.addFee = addFee;
	}


	public String getIsMust() {
		return isMust;
	}


	public void setIsMust(String isMust) {
		this.isMust = isMust;
	}


	public String getAgencyId() {
		return agencyId;
	}


	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}


	public String getAltMerchantCode() {
		return altMerchantCode;
	}


	public void setAltMerchantCode(String altMerchantCode) {
		this.altMerchantCode = altMerchantCode;
	}


	
	
	
}
