package com.etranzact.supportmanager.dto;

public class Bill_Of_Sale {

	private String counter;
	private String sales_id;
	private String amount_sale;
	private String method_of_payment;
	private String date_of_transaction;
	private String buyers_fullname;
	private String description_of_item_sold;
	private String history_owner_ship;
	private String taxes;
	
	public Bill_Of_Sale() { 
	
	}

	public String getSales_id() {
		return sales_id;
	}

	public void setSales_id(String sales_id) {
		this.sales_id = sales_id;
	}

	public String getAmount_sale() {
		return amount_sale;
	}

	public void setAmount_sale(String amount_sale) {
		this.amount_sale = amount_sale;
	}

	public String getMethod_of_payment() {
		return method_of_payment;
	}

	public void setMethod_of_payment(String method_of_payment) {
		this.method_of_payment = method_of_payment;
	}

	public String getDate_of_transaction() {
		return date_of_transaction;
	}

	public void setDate_of_transaction(String date_of_transaction) {
		this.date_of_transaction = date_of_transaction;
	}

	public String getBuyers_fullname() {
		return buyers_fullname;
	}

	public void setBuyers_fullname(String buyers_fullname) {
		this.buyers_fullname = buyers_fullname;
	}

	public String getDescription_of_item_sold() {
		return description_of_item_sold;
	}

	public void setDescription_of_item_sold(String description_of_item_sold) {
		this.description_of_item_sold = description_of_item_sold;
	}

	public String getHistory_owner_ship() {
		return history_owner_ship;
	}

	public void setHistory_owner_ship(String history_owner_ship) {
		this.history_owner_ship = history_owner_ship;
	}

	public String getTaxes() {
		return taxes;
	}

	public void setTaxes(String taxes) {
		this.taxes = taxes;
	}

	public String getCounter() {
		return counter;
	}

	public void setCounter(String counter) {
		this.counter = counter;
	}
	
}
	