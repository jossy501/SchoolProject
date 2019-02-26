package com.etranzact.cms.dto;

public class CardControl {
	
	private String cardControlId;
	private String controlName;
	
	public CardControl(){}
	
	
	public CardControl(String cardControlId,String controlName)
	{
	
		this.cardControlId = cardControlId;
		this.controlName = controlName;
	}


	public String getCardControlId() {
		return cardControlId;
	}


	public void setCardControlId(String cardControlId) {
		this.cardControlId = cardControlId;
	}


	public String getControlName() {
		return controlName;
	}


	public void setControlName(String controlName) {
		this.controlName = controlName;
	}

	
	
}

