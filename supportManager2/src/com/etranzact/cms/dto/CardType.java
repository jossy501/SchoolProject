package com.etranzact.cms.dto;

public class CardType {
	
	private String cardName;
	private String cardType;
	
	
	public CardType()
	{
		
	}
	
	public CardType(String cardName,String cardType)
	{
		this.cardName = cardName;
		this.cardType = cardType;
		
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	
	

}
