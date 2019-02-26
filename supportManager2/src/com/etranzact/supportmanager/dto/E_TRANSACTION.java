package com.etranzact.supportmanager.dto;

public class E_TRANSACTION 
{

	private boolean selected;
	private String bank_code;
	private String globalid;
	private String transid;
	private String card_num;
	private String trans_no;
	private String trans_date;
	private String trans_desc;
	private String trans_amount;
	private String trans_type;
	private String trans_code;
	private String merchat_code;
	private String closed;
	private String trans_ref;
	private String external_transid;
	private String unique_transid;
	private String rep_status;
	private String intstatus;
	private String sbatchid;
	private String recalc_bal;
	private String serviceid;
	private String channelid;
	private String process_status;
	private String fee;
	
	private String merchant_descr;
	private String transaction_count;
	private String merchant_split_type;
	private String total_amount;
	private String counter;
	private String merchant_cat_id;
	
	private String etranzactComm;
	private String otherComm;
	private String etzRatio;
	private String total_fee;
	private String commission_value;
	
	private String lastname;
	private String firstname;
	private String state;
	private String street;
	private String phone;
	private String tran_time;
	
	private String volume;
	private String card_count;
	
	private String debitAmt;
	private String creditAmt;
	
	private String settle_batch;
	private String batch_date;
	
	private String respondCode;
	
	private String errorDesc;
	private String day;
	private String month;
	private String year;
	
	private String appid;
	
	public E_TRANSACTION(){}

	public String getGlobalid() {
		return globalid;
	}

	public void setGlobalid(String globalid) {
		this.globalid = globalid;
	}

	public String getTransid() {
		return transid;
	}

	public void setTransid(String transid) {
		this.transid = transid;
	}

	public String getCard_num() {
		return card_num;
	}

	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}

	public String getTrans_no() {
		return trans_no;
	}

	public void setTrans_no(String trans_no) {
		this.trans_no = trans_no;
	}

	public String getTrans_date() {
		return trans_date;
	}

	public void setTrans_date(String trans_date) {
		this.trans_date = trans_date;
	}

	public String getTrans_desc() {
		return trans_desc;
	}

	public void setTrans_desc(String trans_desc) {
		this.trans_desc = trans_desc;
	}

	public String getTrans_amount() {
		return trans_amount;
	}

	public void setTrans_amount(String trans_amount) {
		this.trans_amount = trans_amount;
	}

	

	public String getTrans_type() {
		return trans_type;
	}

	public void setTrans_type(String trans_type) {
		this.trans_type = trans_type;
	}

	public String getTrans_code() {
		return trans_code;
	}

	public void setTrans_code(String trans_code) {
		this.trans_code = trans_code;
	}

	public String getMerchat_code() {
		return merchat_code;
	}

	public void setMerchat_code(String merchat_code) {
		this.merchat_code = merchat_code;
	}

	public String getClosed() {
		return closed;
	}

	public void setClosed(String closed) {
		this.closed = closed;
	}

	public String getTrans_ref() {
		return trans_ref;
	}

	public void setTrans_ref(String trans_ref) {
		this.trans_ref = trans_ref;
	}

	public String getExternal_transid() {
		return external_transid;
	}

	public void setExternal_transid(String external_transid) {
		this.external_transid = external_transid;
	}

	public String getUnique_transid() {
		return unique_transid;
	}

	public void setUnique_transid(String unique_transid) {
		this.unique_transid = unique_transid;
	}

	public String getRep_status() {
		return rep_status;
	}

	public void setRep_status(String rep_status) {
		this.rep_status = rep_status;
	}

	public String getIntstatus() {
		return intstatus;
	}

	public void setIntstatus(String intstatus) {
		this.intstatus = intstatus;
	}

	public String getSbatchid() {
		return sbatchid;
	}

	public void setSbatchid(String sbatchid) {
		this.sbatchid = sbatchid;
	}

	public String getRecalc_bal() {
		return recalc_bal;
	}

	public void setRecalc_bal(String recalc_bal) {
		this.recalc_bal = recalc_bal;
	}

	public String getServiceid() {
		return serviceid;
	}

	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public String getProcess_status() {
		return process_status;
	}

	public void setProcess_status(String process_status) {
		this.process_status = process_status;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getMerchant_descr() {
		return merchant_descr;
	}

	public void setMerchant_descr(String merchant_descr) {
		this.merchant_descr = merchant_descr;
	}

	public String getTransaction_count() {
		return transaction_count;
	}

	public void setTransaction_count(String transaction_count) {
		this.transaction_count = transaction_count;
	}

	public String getMerchant_split_type() {
		return merchant_split_type;
	}

	public void setMerchant_split_type(String merchant_split_type) {
		this.merchant_split_type = merchant_split_type;
	}

	public String getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}

	public String getCounter() {
		return counter;
	}

	public void setCounter(String counter) {
		this.counter = counter;
	}

	public String getEtranzactComm() {
		return etranzactComm;
	}

	public void setEtranzactComm(String etranzactComm) {
		this.etranzactComm = etranzactComm;
	}

	public String getOtherComm() {
		return otherComm;
	}

	public void setOtherComm(String otherComm) {
		this.otherComm = otherComm;
	}

	public String getMerchant_cat_id() {
		return merchant_cat_id;
	}

	public void setMerchant_cat_id(String merchant_cat_id) {
		this.merchant_cat_id = merchant_cat_id;
	}

	public String getEtzRatio() {
		return etzRatio;
	}

	public void setEtzRatio(String etzRatio) {
		this.etzRatio = etzRatio;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getCommission_value() {
		return commission_value;
	}

	public void setCommission_value(String commission_value) {
		this.commission_value = commission_value;
	}

	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTran_time() {
		return tran_time;
	}

	public void setTran_time(String tran_time) {
		this.tran_time = tran_time;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getCard_count() {
		return card_count;
	}

	public void setCard_count(String card_count) {
		this.card_count = card_count;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getDebitAmt() {
		return debitAmt;
	}

	public void setDebitAmt(String debitAmt) {
		this.debitAmt = debitAmt;
	}

	public String getCreditAmt() {
		return creditAmt;
	}

	public void setCreditAmt(String creditAmt) {
		this.creditAmt = creditAmt;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getSettle_batch() {
		return settle_batch;
	}

	public void setSettle_batch(String settle_batch) {
		this.settle_batch = settle_batch;
	}

	public String getBatch_date() {
		return batch_date;
	}

	public void setBatch_date(String batch_date) {
		this.batch_date = batch_date;
	}

	public String getRespondCode() {
		return respondCode;
	}

	public void setRespondCode(String respondCode) {
		this.respondCode = respondCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}
	
	
	
	
	
	
}
