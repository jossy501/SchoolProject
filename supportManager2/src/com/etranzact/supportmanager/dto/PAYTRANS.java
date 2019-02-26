/**
 * 
 */
package com.etranzact.supportmanager.dto;

/**
 * @author Etrantact
 *
 */
public class PAYTRANS 
{

	private String counter;
	private String subscriber_id;
	private String trans_date;
	private String process_status;
	private String status_desc;
	private String unique_trans_id;
	private String mobile_no;
	private String successful_tran;
	private String transid; 
	private String trans_channel;
	private String trans_amount;
	private String trans_status;
	private String payment_type;
	private String t_fullname;
	private String t_address;
	private String t_quantity;
	private String payment_code;
	private String cheque_no;
	private String cheque_bank;
	private String aut_username;
	private String issuer_code;
	private String card_subname;
	private String sub_code;
	private String card_fullname;
	private String trans_note;
	private String transaction_count;
	private String merchant_id;
	private String merchant_code;
	private String etzCommissionAmt;
	private String netAmt;
	
	
	public String getTransaction_count() {
		return transaction_count;
	}

	public void setTransaction_count(String transaction_count) {
		this.transaction_count = transaction_count;
	}

	public PAYTRANS(){}
	
	public String getSubscriber_id() {
		return subscriber_id;
	}
	public void setSubscriber_id(String subscriber_id) {
		this.subscriber_id = subscriber_id;
	}
	public String getTrans_date() {
		return trans_date;
	}
	public void setTrans_date(String trans_date) {
		this.trans_date = trans_date;
	}
	public String getProcess_status() {
		return process_status;
	}
	public void setProcess_status(String process_status) {
		this.process_status = process_status;
	}
	public String getStatus_desc() {
		return status_desc;
	}
	public void setStatus_desc(String status_desc) {
		this.status_desc = status_desc;
	}
	public String getUnique_trans_id() {
		return unique_trans_id;
	}
	public void setUnique_trans_id(String unique_trans_id) {
		this.unique_trans_id = unique_trans_id;
	}

	public String getMobile_no() {
		return mobile_no;
	}

	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}

	public String getCounter() {
		return counter;
	}

	public void setCounter(String counter) {
		this.counter = counter;
	}

	public String getSuccessful_tran() {
		return successful_tran;
	}

	public void setSuccessful_tran(String successful_tran) {
		this.successful_tran = successful_tran;
	}

	public String getTransid() {
		return transid;
	}

	public void setTransid(String transid) {
		this.transid = transid;
	}

	public String getTrans_channel() {
		return trans_channel;
	}

	public void setTrans_channel(String trans_channel) {
		this.trans_channel = trans_channel;
	}

	public String getTrans_amount() {
		return trans_amount;
	}

	public void setTrans_amount(String trans_amount) {
		this.trans_amount = trans_amount;
	}

	public String getTrans_status() {
		return trans_status;
	}

	public void setTrans_status(String trans_status) {
		this.trans_status = trans_status;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public String getT_fullname() {
		return t_fullname;
	}

	public void setT_fullname(String t_fullname) {
		this.t_fullname = t_fullname;
	}

	public String getT_address() {
		return t_address;
	}

	public void setT_address(String t_address) {
		this.t_address = t_address;
	}

	public String getT_quantity() {
		return t_quantity;
	}

	public void setT_quantity(String t_quantity) {
		this.t_quantity = t_quantity;
	}

	public String getPayment_code() {
		return payment_code;
	}

	public void setPayment_code(String payment_code) {
		this.payment_code = payment_code;
	}

	public String getCheque_no() {
		return cheque_no;
	}

	public void setCheque_no(String cheque_no) {
		this.cheque_no = cheque_no;
	}

	public String getCheque_bank() {
		return cheque_bank;
	}

	public void setCheque_bank(String cheque_bank) {
		this.cheque_bank = cheque_bank;
	}

	public String getAut_username() {
		return aut_username;
	}

	public void setAut_username(String aut_username) {
		this.aut_username = aut_username;
	}

	public String getIssuer_code() {
		return issuer_code;
	}

	public void setIssuer_code(String issuer_code) {
		this.issuer_code = issuer_code;
	}

	public String getCard_subname() {
		return card_subname;
	}

	public void setCard_subname(String card_subname) {
		this.card_subname = card_subname;
	}

	public String getSub_code() {
		return sub_code;
	}

	public void setSub_code(String sub_code) {
		this.sub_code = sub_code;
	}

	public String getCard_fullname() {
		return card_fullname;
	}

	public void setCard_fullname(String card_fullname) {
		this.card_fullname = card_fullname;
	}

	public String getTrans_note() {
		return trans_note;
	}

	public void setTrans_note(String trans_note) {
		this.trans_note = trans_note;
	}

	public String getMerchant_id() {
		return merchant_id;
	}

	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}

	public String getMerchant_code() {
		return merchant_code;
	}

	public void setMerchant_code(String merchant_code) {
		this.merchant_code = merchant_code;
	}

	public String getEtzCommissionAmt() {
		return etzCommissionAmt;
	}

	public void setEtzCommissionAmt(String etzCommissionAmt) {
		this.etzCommissionAmt = etzCommissionAmt;
	}

	public String getNetAmt() {
		return netAmt;
	}

	public void setNetAmt(String netAmt) {
		this.netAmt = netAmt;
	}
	
}
