package com.etranzact.cms.dto;

public class SchemeRegistration {
	

	private String schemeId;
	private String schemeName;
	private String schemeNarration;
	private String settlementBank;
	
	
	public SchemeRegistration(){}
	
	
	public SchemeRegistration(String schemeId,String schemeName, String schemeNarration,String settlementBank)
	{
	
		this.schemeId = schemeId;
		this.schemeName = schemeName;
		this.schemeNarration = schemeNarration;
		this.settlementBank = settlementBank;
	}



	public String getSchemeId() {
		return schemeId;
	}


	public void setSchemeId(String schemeId) {
		this.schemeId = schemeId;
	}


	public String getSchemeName() {
		return schemeName;
	}


	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}


	public String getSchemeNarration() {
		return schemeNarration;
	}


	public void setSchemeNarration(String schemeNarration) {
		this.schemeNarration = schemeNarration;
	}


	public String getSettlementBank() {
		return settlementBank;
	}


	public void setSettlementBank(String settlementBank) {
		this.settlementBank = settlementBank;
	}


	
	
}

