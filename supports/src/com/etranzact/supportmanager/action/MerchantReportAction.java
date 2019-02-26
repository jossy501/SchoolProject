package com.etranzact.supportmanager.action;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javassist.bytecode.Descriptor.Iterator;

import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

import com.etranzact.supportmanager.dto.E_FEE_DETAIL_BK;
import com.etranzact.supportmanager.dto.E_MERCHANT;
import com.etranzact.supportmanager.dto.E_SETTLEMENTDOWNLOAD_BK;
import com.etranzact.supportmanager.model.MerchantReportModel;
import com.etranzact.supportmanager.model.ReportModel;

@Restrict("#{authenticator.curUser.loggedIn}")
@Scope(ScopeType.SESSION)
@Name("merchantReportAction")
public class MerchantReportAction {

	@In
	FacesMessages facesMessages;

	@Logger
	private Log log;

	FacesContext context;

	// merchant bank commission
	private ArrayList<E_FEE_DETAIL_BK> merchantBankComm = new ArrayList<E_FEE_DETAIL_BK>();

	private ArrayList<E_FEE_DETAIL_BK> merchantDetails = new ArrayList<E_FEE_DETAIL_BK>();
	
	private ArrayList<E_SETTLEMENTDOWNLOAD_BK> settle_download_list = new ArrayList<E_SETTLEMENTDOWNLOAD_BK>();
	
	private ArrayList<E_SETTLEMENTDOWNLOAD_BK> merchant_settlement_details = new ArrayList<E_SETTLEMENTDOWNLOAD_BK>();

	private Date start_date;
	private Date end_date;
	private String start_hr;
	private String end_hr;

	private String card_num;
	private String merchant_code;
	private String settlement_batch;
	private String merchant_name;
	private String channel_id;
	private String trans_code;

	private double total_amount = 0.0d;
	private int total_count = 0;
	

	@RequestParameter
	private String id;//used in passing values from a jsf to a jsf

	/* View Bank Commission */
	public void viewBankCommByMerchant() {
		try {
			merchantBankComm.clear();
			MerchantReportModel reportModel = new MerchantReportModel();

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

			String beginDate = df.format(getStart_date());
			beginDate = beginDate + " " + getStart_hr() + ":00";

			String endDate = df.format(getEnd_date());
			endDate = endDate + " " + getEnd_hr() + ":59";
			FacesContext context = FacesContext.getCurrentInstance();
			String user_code = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
			String b_merchant_code = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
			String mcode = user_code + b_merchant_code;
			String channel_value = getChannel_id();
			
			merchantBankComm = reportModel.getBankCommissionSummaryByDateAndMerchant(beginDate,endDate, mcode, channel_value, user_code);
			double b_com = 0.0d;

			for(int i = 0; i < merchantBankComm.size(); i++) 
			{
				E_FEE_DETAIL_BK bk = (E_FEE_DETAIL_BK) merchantBankComm.get(i);
				b_com += Double.parseDouble(bk.getTotal_amount());
			}
			setTotal_amount(b_com);

		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
	}
	
	
	
	/* View Bank - Merchant Commission */
	public void viewMerchantCommission() 
	{
		
		try 
		{
			merchantBankComm.clear();
			MerchantReportModel reportModel = new MerchantReportModel();

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

			String beginDate = df.format(getStart_date());
			beginDate = beginDate + " " + getStart_hr() + ":00";

			String endDate = df.format(getEnd_date());
			endDate = endDate + " " + getEnd_hr() + ":59";
			
			FacesContext context = FacesContext.getCurrentInstance();
			String user_code = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
			String b_merchant_code = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
			String mcode = user_code + b_merchant_code;
			String channel_value = getChannel_id();
			
			merchantBankComm = reportModel.getMerchantCommissionSummary(beginDate,endDate, mcode, channel_value, user_code);
			double b_com = 0.0d;

			for(int i = 0; i < merchantBankComm.size(); i++) 
			{
				E_FEE_DETAIL_BK bk = (E_FEE_DETAIL_BK) merchantBankComm.get(i);
				b_com += Double.parseDouble(bk.getTotal_amount());
			}
			setTotal_amount(b_com);

		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
	}
	
	
	//method used to view merchant summary report
	public void viewMerchantSettlementReport() {
		try
		{
			settle_download_list.clear();
			MerchantReportModel reportModel = new MerchantReportModel();

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

			String beginDate = df.format(getStart_date());
			beginDate = beginDate + " " + getStart_hr() + ":00";

			String endDate = df.format(getEnd_date());
			endDate = endDate + " " + getEnd_hr() + ":59";
			FacesContext context = FacesContext.getCurrentInstance();
			//String pan = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
			String pan = "'0700000045'";
			System.out.println("pan <<<< " + pan);		
			double trans_amount = 0.0d;
			settle_download_list = reportModel.getMerchantSettlementSummary(getSettlement_batch(), getMerchant_code(), pan, beginDate, endDate);
			for(int i=0; i<settle_download_list.size();i++) {
					E_SETTLEMENTDOWNLOAD_BK settle_bk = (E_SETTLEMENTDOWNLOAD_BK)settle_download_list.get(i);
					trans_amount += Double.parseDouble(settle_bk.getTrans_amount());
			}
			setTotal_amount(trans_amount);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	// this is used to drill down on the tran id when searching in web
	// investigation
	public void drillDownMerchant() {
		merchantDetails.clear();
		MerchantReportModel mModel = new MerchantReportModel();

		try {
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

			String beginDate = df.format(getStart_date());
			beginDate = beginDate + " " + getStart_hr() + ":00";

			String endDate = df.format(getEnd_date());
			endDate = endDate + " " + getEnd_hr() + ":59";

			System.out.println("startdate >>>" + beginDate);
			System.out.println("enddate >>>" + endDate);
			
			FacesContext context = FacesContext.getCurrentInstance();
			String user_code = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
			String b_merchant_code = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
			String id = getId();
			String channel_value = getChannel_id();
			String mcode = user_code + b_merchant_code;
			double sum_total =0.0d;
			
			System.out.println("id>>> " + id);
			
			merchantDetails = mModel.getBankMerchantComDetail(beginDate,endDate, mcode, channel_value, id);
			for(int i=0; i<merchantDetails.size(); i++) {
					E_FEE_DETAIL_BK b =(E_FEE_DETAIL_BK)merchantDetails.get(i);
					sum_total +=Double.parseDouble(b.getTrans_amount());
			}
			
			System.out.println("size of array is >>>>"+ merchantDetails.size());
			setTotal_amount(sum_total);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	//Used to drill down merchant settlement report
	
	public void merchantSettlementDetail() {
		merchant_settlement_details.clear();
		MerchantReportModel mModel = new MerchantReportModel();

		try {
			
			/*DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

			String beginDate = df.format(getStart_date());
			beginDate = beginDate + " " + getStart_hr() + ":00";

			String endDate = df.format(getEnd_date());
			endDate = endDate + " " + getEnd_hr() + ":59";*/

			/*FacesContext context = FacesContext.getCurrentInstance();
			String user_code = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();*/
			
			String parameterValues[] = id.split(":");
			String bank = parameterValues[0];
			String settle_batch = parameterValues[1];
			String merchant_code = parameterValues[2];
			String trans_code = parameterValues[3];
			String bank_type = parameterValues[4];
			double trans_amount = 0.0d;
			merchant_settlement_details = mModel.getMerchantSettlementDetail(merchant_code, settle_batch, trans_code, bank_type); 
			for(int j=0; j<merchant_settlement_details.size(); j++) {
				E_SETTLEMENTDOWNLOAD_BK bk =(E_SETTLEMENTDOWNLOAD_BK)merchant_settlement_details.get(j);
				trans_amount += Double.parseDouble(bk.getTrans_amount());
			}
			setTotal_amount(trans_amount);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// Used to retrieve merchant code.
	public String getMerchantName(String mcode) {
		String merchantname = "";
		try {
			MerchantReportModel mModel = new MerchantReportModel();
			merchantname = mModel.getMerchantName(mcode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return merchantname;
	}
	
	// Used to retrieve merchant code.
	public String getBranchName(String sub_code) {
		String branchname = "";
		try {
			
			FacesContext context = FacesContext.getCurrentInstance();
			String user_code = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
			MerchantReportModel mModel = new MerchantReportModel();
			branchname = mModel.getBranchName(user_code, sub_code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return branchname;
	}

	public ArrayList<E_SETTLEMENTDOWNLOAD_BK> getSettle_download_list() {
		return settle_download_list;
	}


	public void setSettle_download_list(
			ArrayList<E_SETTLEMENTDOWNLOAD_BK> settle_download_list) {
		this.settle_download_list = settle_download_list;
	}


	public void resetValues() {
		System.out.println("reset data");

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d = df.parse(df.format(new Date()));
			setStart_date(d);
			setEnd_date(d);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		setMerchant_code(null);
		setSettlement_batch(null);
		setStart_hr("00");
		setEnd_hr("23");
		merchantBankComm.clear();

	}

	public double makeDouble(String value) {
		double resp = 0.00;
		try {
			if (value == null || value.equals("") || value.equals("null")) {
				value = "0.00";
			} else {
				resp = Double.parseDouble(value);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resp;
	}

	public FacesMessages getFacesMessages() {
		return facesMessages;
	}

	public void setFacesMessages(FacesMessages facesMessages) {
		this.facesMessages = facesMessages;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public FacesContext getContext() {
		return context;
	}

	public void setContext(FacesContext context) {
		this.context = context;
	}

	public ArrayList<E_FEE_DETAIL_BK> getMerchantBankComm() {
		return merchantBankComm;
	}

	public void setMerchantBankComm(ArrayList<E_FEE_DETAIL_BK> merchantBankComm) {
		this.merchantBankComm = merchantBankComm;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public String getStart_hr() {
		return start_hr;
	}

	public void setStart_hr(String start_hr) {
		this.start_hr = start_hr;
	}

	public String getEnd_hr() {
		return end_hr;
	}

	public void setEnd_hr(String end_hr) {
		this.end_hr = end_hr;
	}

	public String getCard_num() {
		return card_num;
	}

	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}

	public String getMerchant_code() {
		return merchant_code;
	}

	public void setMerchant_code(String merchant_code) {
		this.merchant_code = merchant_code;
	}

	public String getMerchant_name() {
		return merchant_name;
	}

	public void setMerchant_name(String merchant_name) {
		this.merchant_name = merchant_name;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

	public String getTrans_code() {
		return trans_code;
	}

	public void setTrans_code(String trans_code) {
		this.trans_code = trans_code;
	}

	public double getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(double total_amount) {
		this.total_amount = total_amount;
	}

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	public ArrayList<E_FEE_DETAIL_BK> getMerchantDetails() {
		return merchantDetails;
	}

	public void setMerchantDetails(ArrayList<E_FEE_DETAIL_BK> merchantDetails) {
		this.merchantDetails = merchantDetails;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getSettlement_batch() {
		return settlement_batch;
	}
	public void setSettlement_batch(String settlement_batch) {
		this.settlement_batch = settlement_batch;
	}	

}
