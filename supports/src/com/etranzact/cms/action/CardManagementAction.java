/**
 * 
 */
package com.etranzact.cms.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.io.FileOutputStream;
import java.io.OutputStream;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.etranzact.cms.model.MailDemo;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

import sun.text.SupplementaryCharacterData;

import com.etranzact.drs.controller.ExcelWriter;
import com.etranzact.institution.model.InstitutionModel;
import com.etranzact.cms.util.Sequencer;
import com.etranzact.cms.dto.CardHolder;
import com.etranzact.cms.dto.E_TRANSACTION;
import com.etranzact.cms.dto.FundGateInfo;
import com.etranzact.supportmanager.dto.LOTTO_LOG;
import com.etranzact.supportmanager.dto.MenuItem;
import com.etranzact.supportmanager.dto.User;
import com.etranzact.supportmanager.model.AdminModel;
import com.etranzact.supportmanager.model.ReportModel;
import com.etranzact.supportmanager.utility.HashNumber;
import com.etranzact.supportmanager.utility.HttpMessenger;
import com.etranzact.cms.model.CardManagementModel;
import com.etranzact.supportmanager.action.AuthenticationAction;
import com.etz.security.util.Cryptographer;
import com.etz.security.util.PBEncryptor;


/**
 * @author tony.ezeanya
 *
 */
@Restrict("#{authenticator.curUser.loggedIn}")
@Scope(ScopeType.SESSION)
@Name("cardManagementAction")
public class CardManagementAction implements Serializable
{

	@RequestParameter
	private String id;//used in passing values from a jsf to a jsf
	
	private String strParam;
	private Date start_date;
	private Date end_date;
	private String start_hr;
	private String end_hr;
	private String optionType;
	private String optionType2;
	private String mobileno;
	private String card_num;
	private String merchant_code;
	private String merchant_name;
	private String channel_id;
	private String trans_code;
	private String bank_code;
	private String branch_code;
	private String meterno;
	private String transSummary;
	private String line_type;
	private String tarrif_type;
	private String trans_count;
	private double total_amount = 0.0d;
	private int total_count = 0;
	private double total_etz_amount = 0.0d;
	private double total_other_amount = 0.0d;
	private String settlement_batch;
	private String edit_id;
	private String operation_id;
	private E_TRANSACTION selectedTran;  
    private Set<Integer> rowsToUpdate = new HashSet<Integer>();  
    private boolean selected;
    private String issueCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String street;
    private String cardScheme;
    private List schemeList = new ArrayList();
    private List schemeRegList = new ArrayList();
    private List schemeLists = new ArrayList();
    private List cardholdertranLog = new ArrayList();
    private List cardholderbreakdownlog  = new ArrayList();
    private List holderList = new ArrayList();
    private boolean checkHotList;
    private List updateLog = new ArrayList();
    private List mmoneyLog = new ArrayList();
    private List pendingAuthorization = new ArrayList();
    private List pendingAuth_Block = new ArrayList();
    private double minBal = 0.0d;
    private double boundWork = 0.0d;
    private String minBalance;
    private String boundworks;
    private String boundLimit;
    private String cardExp;
    
    private String cardType;
    private String controlId;
    private List CardTypeList = new ArrayList();
    private List allCardType =  new ArrayList();
    private List displayCardType = new ArrayList();
    private List displayControlName = new ArrayList();
    private List pendingAuth = new ArrayList();
    private List pendingPinAuth = new ArrayList();
    
    private List unBlockAuth = new ArrayList();
    private List fundsTransfer = new ArrayList();
    private boolean selecteds;
    private List bankList = new ArrayList();
    
    private List pingenlist = new ArrayList();
    
    private boolean disableButton;
    
    private String schemeId;
	private String schemeName;
	private String schemeNarration;
	private String settlementBank;
	private String resetBound;
	private String resetLimit;
	private String currentStatus;
	private String prevStatus;
	private String cardExpiration;
	private String disRegard;
	private String terminalID;
	private String transDate;
	private String transNo;
	private String transCode;
	private String channel;
	private String transDesc;
	private String merchantCode;
	private double debitAmt = 0.0d;
	private double creditAmt = 0.0d;
	private String onlinebalance ;
	private String withDrawLimit;
	private String userHotlist;
	private String userBlock;
	private String track2CardNumber;
	
	
	private boolean disable;
	
	private List cmsServerlist = new ArrayList();
	
	
		
	private List pendingResetBoundWork =  new ArrayList(); 
	private List pendingResetBoundLimit = new ArrayList();
	private List pendingCardExpiration = new ArrayList();
	private List fundgateSummary = new ArrayList();
	private List fundgateRecordList = new ArrayList();
	
	private List cardschemeStatusList = new ArrayList();
	
	private String terminalId;
	
	private String remoteIp;
	private String merchantName;
	private String masterKey;
	private String cardscheme;
	private String currency;
	private String acctType;
	private String createdDat;
	private String allowCredit;
	private String chargeCatId;
	private String vtuCom;
	private String cmsServer;
	
	private FundGateInfo curFundGateInfo;
	
	private List fundgateList = new ArrayList();
	
	
	@In
    FacesMessages facesMessages;
	
	@Logger
	private Log log;

	FacesContext context;
	

	public List getFundgateList() {
		getFundGatePendingDetails();
		return fundgateList;
	}

	public void setFundgateList(List fundgateList) {
		this.fundgateList = fundgateList;
	}

	public boolean isDisableButton() {
		return disableButton;
	}

	public void setDisableButton(boolean disableButton) {
		this.disableButton = disableButton;
	}

	public String getUserHotlist() {
		return userHotlist;
	}

	public void setUserHotlist(String userHotlist) {
		this.userHotlist = userHotlist;
	}

	public String getUserBlock() {
		return userBlock;
	}

	public void setUserBlock(String userBlock) {
		this.userBlock = userBlock;
	}

	public String getWithDrawLimit() {
		return withDrawLimit;
	}

	public void setWithDrawLimit(String withDrawLimit) {
		this.withDrawLimit = withDrawLimit;
	}
	

	public List getPingenlist() {
		
		System.out.println("Testing ######## ");
		return pingenlist;
	}

	public void setPingenlist(List pingenlist) {
		this.pingenlist = pingenlist;
	}

	public String getOnlinebalance() {
		return onlinebalance;
	}

	public void setOnlinebalance(String onlinebalance) {
		this.onlinebalance = onlinebalance;
	}

			public String getId() {
				return id;
			}
		
			public void setId(String id) {
				this.id = id;
			}
		
			public String getStrParam() {
				return strParam;
			}
		
			public void setStrParam(String strParam) {
				this.strParam = strParam;
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
		
			public String getOptionType() {
				return optionType;
			}
		
			public void setOptionType(String optionType) {
				this.optionType = optionType;
			}
		
			public String getOptionType2() {
				return optionType2;
			}
		
			public void setOptionType2(String optionType2) {
				this.optionType2 = optionType2;
			}
		
			public String getMobileno() {
				return mobileno;
			}
		
			public void setMobileno(String mobileno) {
				this.mobileno = mobileno;
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
		
			public String getBank_code() {
				return bank_code;
			}
		
			public void setBank_code(String bank_code) {
				this.bank_code = bank_code;
			}
		
			public String getBranch_code() {
				return branch_code;
			}
		
			public void setBranch_code(String branch_code) {
				this.branch_code = branch_code;
			}
		
			public String getMeterno() {
				return meterno;
			}
		
			public void setMeterno(String meterno) {
				this.meterno = meterno;
			}
		
			public String getTransSummary() {
				return transSummary;
			}
		
			public void setTransSummary(String transSummary) {
				this.transSummary = transSummary;
			}
		
			public String getLine_type() {
				return line_type;
			}
		
			public void setLine_type(String line_type) {
				this.line_type = line_type;
			}
		
			public String getTarrif_type() {
				return tarrif_type;
			}
		
			public void setTarrif_type(String tarrif_type) {
				this.tarrif_type = tarrif_type;
			}
		
			public String getTrans_count() {
				return trans_count;
			}
		
			public void setTrans_count(String trans_count) {
				this.trans_count = trans_count;
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
		
			public double getTotal_etz_amount() {
				return total_etz_amount;
			}
		
			public void setTotal_etz_amount(double total_etz_amount) {
				this.total_etz_amount = total_etz_amount;
			}
		
			public double getTotal_other_amount() {
				return total_other_amount;
			}
		
			public void setTotal_other_amount(double total_other_amount) {
				this.total_other_amount = total_other_amount;
			}
		
			public String getSettlement_batch() {
				return settlement_batch;
			}
		
			public void setSettlement_batch(String settlement_batch) {
				this.settlement_batch = settlement_batch;
			}
		
			public String getEdit_id() {
				return edit_id;
			}
		
			public void setEdit_id(String edit_id) {
				this.edit_id = edit_id;
			}
		
			public String getOperation_id() {
				return operation_id;
			}
		
			public void setOperation_id(String operation_id) {
				this.operation_id = operation_id;
			}
		
			public E_TRANSACTION getSelectedTran() {
				return selectedTran;
			}
		
			public void setSelectedTran(E_TRANSACTION selectedTran) {
				this.selectedTran = selectedTran;
			}
		
			public Set<Integer> getRowsToUpdate() {
				return rowsToUpdate;
			}
		
			public void setRowsToUpdate(Set<Integer> rowsToUpdate) {
				this.rowsToUpdate = rowsToUpdate;
			}
		
			public boolean isSelected() {
				return selected;
			}
		
			public void setSelected(boolean selected) {
				this.selected = selected;
			}
		
			public String getIssueCode() {
				return issueCode;
			}
		
			public void setIssueCode(String issueCode) {
				this.issueCode = issueCode;
			}
		
			public String getFirstName() {
				return firstName;
			}
		
			public void setFirstName(String firstName) {
				this.firstName = firstName;
			}
		
			public String getLastName() {
				return lastName;
			}
		
			public void setLastName(String lastName) {
				this.lastName = lastName;
			}
		
			public String getEmail() {
				return email;
			}
		
			public void setEmail(String email) {
				this.email = email;
			}
		
			public String getPhone() {
				return phone;
			}
		
			public void setPhone(String phone) {
				this.phone = phone;
			}
		
			
			public String getStreet() {
				return street;
			}

			public void setStreet(String street) {
				this.street = street;
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
		
			public String getCardScheme() {
				return cardScheme;
			}
		
			public void setCardScheme(String cardScheme) {
				this.cardScheme = cardScheme;
			}
		
			
			
			
			
			public List getSchemeList() {
				getCardSchemes();
				return schemeList;
			}
		
			public void setSchemeList(List schemeList) {
				this.schemeList = schemeList;
			}
			
		
			public List getCardholdertranLog() {
				return cardholdertranLog;
			}

			public void setCardholdertranLog(List cardholdertranLog) {
				this.cardholdertranLog = cardholdertranLog;
			}
			
			
			public List getCardholderbreakdownlog() {
				return cardholderbreakdownlog;
			}

			public void setCardholderbreakdownlog(List cardholderbreakdownlog) {
				this.cardholderbreakdownlog = cardholderbreakdownlog;
			}
			
			

			public List getUnBlockAuth() {
				getCardHolderDetails_Pending_UNBLOCKED();
				return unBlockAuth;
			}

			public void setUnBlockAuth(List unBlockAuth) {
				this.unBlockAuth = unBlockAuth;
			}

			public List getPendingAuth() {
				getCardHolderDetails_Pending_Dehotlist();
				return pendingAuth;
			}

			public void setPendingAuth(List pendingAuth) {
				this.pendingAuth = pendingAuth;
			}
			
			
			

			public List getPendingPinAuth() {
				
				//getCardHolderDetailsPendingPinReGeneration();
				System.out.println("Anything.......  @@@@@@");
				return pendingPinAuth;
			}

			public void setPendingPinAuth(List pendingPinAuth) {
			
				this.pendingPinAuth = pendingPinAuth;
			}

			public List getHolderList() {
				return holderList;
			}

			public void setHolderList(List holderList) {
				this.holderList = holderList;
			}

			
			public boolean isCheckHotList() {
				this.checkHotList = false;
				return checkHotList;
			}

			public void setCheckHotList(boolean checkHotList) {
				this.checkHotList = checkHotList;
			}

			
			public List getUpdateLog() {
				return updateLog;
			}

			public void setUpdateLog(List updateLog) {
				this.updateLog = updateLog;
			}
			

			public List getSchemeLists() {
				return schemeLists;
			}

			public void setSchemeLists(List schemeLists) {
				this.schemeLists = schemeLists;
			}

			public List getMmoneyLog() {
				return mmoneyLog;
			}

			public void setMmoneyLog(List mmoneyLog) {
				this.mmoneyLog = mmoneyLog;
			}
			
			

			public List getFundgateSummary() {
				return fundgateSummary;
			}

			public void setFundgateSummary(List fundgateSummary) {
				this.fundgateSummary = fundgateSummary;
			}

			public List getPendingAuthorization() {
			
				//getFundGatePendingDetails();
				return pendingAuthorization;
			}

			public void setPendingAuthorization(List pendingAuthorization) {
				this.pendingAuthorization = pendingAuthorization;
			}
			
			public void getFundGatePendingDetails()
			{
			
				CardManagementModel model = new CardManagementModel();
				fundgateList = model.getFundGateAuthorizedDetails();
				System.out.println(" fundgateList Size  "+fundgateList.size());
			
				
			}
			public double getMinBal() {
				return minBal;
			}

			public void setMinBal(double minBal) {
				this.minBal = minBal;
			}

			
			public String getMinBalance() {
				return minBalance;
			}

			public void setMinBalance(String minBalance) {
				this.minBalance = minBalance;
			}
			
			

			public double getBoundWork() {
				return boundWork;
			}

			public void setBoundWork(double boundWork) {
				this.boundWork = boundWork;
			}
			
			

			public String getCardType() {
				return cardType;
			}

			public void setCardType(String cardType) {
				this.cardType = cardType;
			}

			
			public String getControlId() {
				return controlId;
			}

			public void setControlId(String controlId) {
				this.controlId = controlId;
			}
			
			

			public List getCardTypeList() {
				getAllCardType();
				return CardTypeList;
			}

			public void setCardTypeList(List cardTypeList) {
				CardTypeList = cardTypeList;
			}
			
			
			


			public List getDisplayCardType() {
				getAllCardType();
				return displayCardType;
			}

			public void setDisplayCardType(List displayCardType) {
				this.displayCardType = displayCardType;
			}
			
			

			public List getDisplayControlName() {
				getAllControlName();
				return displayControlName;
			}

			public void setDisplayControlName(List displayControlName) {
				this.displayControlName = displayControlName;
			}

			
			public String getBoundworks() {
				return boundworks;
			}

			public void setBoundworks(String boundworks) {
				this.boundworks = boundworks;
			}

			
			public List getPendingAuth_Block() {
				getCardHolderDetails_Block_Pending();
				return pendingAuth_Block;
			}

			public void setPendingAuth_Block(List pendingAuth_Block) {
				this.pendingAuth_Block = pendingAuth_Block;
			}

			

			public List getFundsTransfer() {
				return fundsTransfer;
			}

			public void setFundsTransfer(List fundsTransfer) {
				this.fundsTransfer = fundsTransfer;
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
			
			
			
			public List getSchemeRegList() {
				getExistedSchemelist();
				return schemeRegList;
			}

			public void setSchemeRegList(List schemeRegList) {
				this.schemeRegList = schemeRegList;
			}

			public void getExistedSchemelist()
			{
				
				CardManagementModel cmmodel = new CardManagementModel();
				schemeRegList = cmmodel.getScheme_Registration();
				System.out.println("schemeList "+schemeList.size());
			}
			
			
			
			
			public String getBoundLimit() {
				return boundLimit;
			}

			public void setBoundLimit(String boundLimit) {
				this.boundLimit = boundLimit;
			}

			
			
			
			public List getPendingResetBoundWork() {
				
				getBoundWorkPending();
				return pendingResetBoundWork;
			}

			public void setPendingResetBoundWork(List pendingResetBoundWork) {
				this.pendingResetBoundWork = pendingResetBoundWork;
			}
			
			

			public String getResetBound() {
				return resetBound;
			}

			public void setResetBound(String resetBound) {
				this.resetBound = resetBound;
			}

			public String getResetLimit() {
				return resetLimit;
			}

			public void setResetLimit(String resetLimit) {
				this.resetLimit = resetLimit;
			}
			
			

			public String getCurrentStatus() {
				return currentStatus;
			}

			public void setCurrentStatus(String currentStatus) {
				this.currentStatus = currentStatus;
			}

			public String getPrevStatus() {
				return prevStatus;
			}

			public void setPrevStatus(String prevStatus) {
				this.prevStatus = prevStatus;
			}
			
			

			public String getDisRegard() {
				return disRegard;
			}

			public void setDisRegard(String disRegard) {
				this.disRegard = disRegard;
			}

			public String getCardExpiration() {
				return cardExpiration;
			}

			public void setCardExpiration(String cardExpiration) {
				this.cardExpiration = cardExpiration;
			}
			
			

			public String getTerminalID() {
				return terminalID;
			}

			public void setTerminalID(String terminalID) {
				this.terminalID = terminalID;
			}

			public List getPendingResetBoundLimit() {
				getPending_Rest_Bound_Limit();
				return pendingResetBoundLimit;
			}

			public void setPendingResetBoundLimit(List pendingResetBoundLimit) {
				this.pendingResetBoundLimit = pendingResetBoundLimit;
			}
					

			public List getPendingCardExpiration() {
				getPending_Extend_CardExpire();
				return pendingCardExpiration;
			}

			public void setPendingCardExpiration(List pendingCardExpiration) {
				this.pendingCardExpiration = pendingCardExpiration;
			}
			
			

			public boolean isSelecteds() {
				return selecteds;
			}

			public void setSelecteds(boolean selecteds) {
				this.selecteds = selecteds;
			}

			public String getTransDate() {
				return transDate;
			}

			public void setTransDate(String transDate) {
				this.transDate = transDate;
			}

			public String getTransNo() {
				return transNo;
			}

			public void setTransNo(String transNo) {
				this.transNo = transNo;
			}

			public String getTransCode() {
				return transCode;
			}

			public void setTransCode(String transCode) {
				this.transCode = transCode;
			}

			public String getChannel() {
				return channel;
			}

			public void setChannel(String channel) {
				this.channel = channel;
			}

			public String getTransDesc() {
				return transDesc;
			}

			public void setTransDesc(String transDesc) {
				this.transDesc = transDesc;
			}

			public String getMerchantCode() {
				return merchantCode;
			}

			public void setMerchantCode(String merchantCode) {
				this.merchantCode = merchantCode;
			}

		

			public double getDebitAmt() {
				return debitAmt;
			}

			public void setDebitAmt(double debitAmt) {
				this.debitAmt = debitAmt;
			}

			public double getCreditAmt() {
				return creditAmt;
			}

			public void setCreditAmt(double creditAmt) {
				this.creditAmt = creditAmt;
			}
			
			
			
			

			public String getTerminalId() {
				return terminalId;
			}

			public void setTerminalId(String terminalId) {
				this.terminalId = terminalId;
			}

			public String getRemoteIp() {
				return remoteIp;
			}

			public void setRemoteIp(String remoteIp) {
				this.remoteIp = remoteIp;
			}

			public String getMerchantName() {
				return merchantName;
			}

			public void setMerchantName(String merchantName) {
				this.merchantName = merchantName;
			}

			public String getMasterKey() {
				return masterKey;
			}

			public void setMasterKey(String masterKey) {
				this.masterKey = masterKey;
			}

			public String getCardscheme() {
				return cardscheme;
			}

			public void setCardscheme(String cardscheme) {
				this.cardscheme = cardscheme;
			}

			public String getCurrency() {
				return currency;
			}

			public void setCurrency(String currency) {
				this.currency = currency;
			}

			public String getAcctType() {
				return acctType;
			}

			public void setAcctType(String acctType) {
				this.acctType = acctType;
			}

			public String getCreatedDat() {
				return createdDat;
			}

			public void setCreatedDat(String createdDat) {
				this.createdDat = createdDat;
			}

			public String getAllowCredit() {
				return allowCredit;
			}

			public void setAllowCredit(String allowCredit) {
				this.allowCredit = allowCredit;
			}

			public String getChargeCatId() {
				return chargeCatId;
			}

			public void setChargeCatId(String chargeCatId) {
				this.chargeCatId = chargeCatId;
			}

			public String getVtuCom() {
				return vtuCom;
			}

			public void setVtuCom(String vtuCom) {
				this.vtuCom = vtuCom;
			}

			
			
			
			public FundGateInfo getCurFundGateInfo() {
				return curFundGateInfo;
			}

			public void setCurFundGateInfo(FundGateInfo curFundGateInfo) {
				this.curFundGateInfo = curFundGateInfo;
			}

			public String getCmsServer() {
				return cmsServer;
			}

			public void setCmsServer(String cmsServer) {
				this.cmsServer = cmsServer;
			}

			public void setAllCardType(List allCardType) {
				this.allCardType = allCardType;
			}
			
			

			public List getFundgateRecordList() {
				getFundGateExistedRecord();
				return fundgateRecordList;
			}

			public void setFundgateRecordList(List fundgateRecordList) {
				this.fundgateRecordList = fundgateRecordList;
			}

			public void getFundGateExistedRecord()
			{
				CardManagementModel model = new CardManagementModel();
				fundgateRecordList = model.getFungateRecords();
			}
			
			
			public boolean isDisable() {
				return disable;
			}

			public void setDisable(boolean disable) {
				this.disable = disable;
			}

	
			public List getCmsServerlist() {
				CardManagementModel cmodel = new CardManagementModel();
				cmsServerlist = cmodel.getCMSSERVER();
				return cmsServerlist;
			}

			public void setCmsServerlist(List cmsServerlist) {
				this.cmsServerlist = cmsServerlist;
			}

			
			
			public String getCardExp() {
				return cardExp;
			}

			public void setCardExp(String cardExp) {
				this.cardExp = cardExp;
			}

			
			
			
			
			
			
			
			public String getTrack2CardNumber() {
				return track2CardNumber;
			}

			public void setTrack2CardNumber(String track2CardNumber) {
				this.track2CardNumber = track2CardNumber;
			}

			public void createScheme()
			{
				
				CardManagementModel cmmodel = new CardManagementModel();
				
				try
				{
				
						String Scheme = getSchemeId();
					
						
						String shemenOwner = getSchemeName();
						String sttlementBank = getBank_code();
						String narration = getSchemeNarration();
						
						
						
						if(Scheme.trim().length() > 6)
						{
							facesMessages.add(Severity.INFO, "Scheme must not be more than 6 digit");
						}
						else
						{
					
								DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						
					
								Date d = df.parse(df.format(new Date())); 
								
							 
							    String beginDate = df.format(d);
								
						       // beginDate = beginDate + " " + getStart_hr() + ":00";
						       
						        //String endDate = df.format(getEnd_date());
						       //endDate = endDate + " " + getEnd_hr() + ":59";
						  
						        System.out.println("created date ---->>>> "+beginDate);
						 
						
							String mess = cmmodel.createE_Scheme(Scheme, shemenOwner, sttlementBank, narration,beginDate);
							
					 
							if(mess.equalsIgnoreCase("EXISTED"))
							{
								mess = "Records Already Exists";
							}
							else if(mess.equalsIgnoreCase("SUCCESS"))
							{
								mess = "Records successfully inserted";
								
								schemeRegList = cmmodel.getScheme_Registration();
								System.out.println("schemeList "+schemeList.size());
							}
							else
							{
								mess = "Records not inserted";
							}	
					    	
							facesMessages.add(Severity.INFO, mess);
						}
						
				
						
					
					
				}catch(Exception ex)
				{
						ex.printStackTrace();
				}
				
			}
			
			public void deleteFundgateRecord()
			{
			
				
				try
				{
						CardManagementModel model = new CardManagementModel();
			
						String payId = getId();
						String TeminalEdith = getEdit_id();
					
						System.out.println("CourseId  "+payId+"Course Edith "+TeminalEdith);
						
						String mess = model.deleteFundGateRecords(TeminalEdith);
						
						if(mess.equalsIgnoreCase("SUCCESS"))
						{
							mess = " Record Successfully Deleted ";
						
						}
						else
						{
							mess = " Record  Not  Deleted ! ";
						}
						
						
						facesMessages.add(Severity.INFO, mess);
					
					
				}catch(Exception ex)
				{
						ex.printStackTrace();
				}
				
			}
			
			public void deleteScheme()
			{
				
				CardManagementModel cmmodel = new CardManagementModel();
				
				try
				{
			
						String schemeId = getId();
						String shemeEdit = getEdit_id();
					
						System.out.println("schemeId  "+schemeId+"schemeEdit "+shemeEdit);
						
						String mess = cmmodel.deleteScheme(shemeEdit);
						if(mess.equalsIgnoreCase("SUCCESS"))
						{
							mess = "Records successfully deleted";
						
						}
						else
						{
							mess = "Records not deleted";
						}
						
						
						facesMessages.add(Severity.INFO, mess);
					
					
				}catch(Exception ex)
				{
						ex.printStackTrace();
				}
				
			}
			
			public List getBankList() {
				getBanks();
				return bankList;
			}
			  public void getBanks()
			    {
					try
					{
						CardManagementModel cardModel = new CardManagementModel();
						bankList =  cardModel.getBanks();
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}

			    }

			public void setBankList(List bankList) {
				this.bankList = bankList;
			}
			
			

			public List getCardschemeStatusList() {
				return cardschemeStatusList;
			}

			public void setCardschemeStatusList(List cardschemeStatusList) {
				this.cardschemeStatusList = cardschemeStatusList;
			}

			public void resetValues()
			{
				System.out.println("reset data cms");
			
				cardholdertranLog.clear();
				cardschemeStatusList.clear();
				pendingPinAuth.clear();
				pendingCardExpiration.clear();
				pendingAuth.clear();
				pendingResetBoundLimit.clear();
				pendingResetBoundWork.clear();
				pendingCardExpiration.clear();
				
				
			
				//setSelected(false);
				
				setMerchant_code(null);
				setMerchant_name(null);
				setOptionType("A");
				setOptionType2("SR");
				setStrParam(null);
				setChannel_id(null);
				setTrans_code(null);
				setLine_type(null);
				setCard_num(null);
				setMobileno(null);
			
				setChannel_id(null);
				setCard_num(null);
				setMerchant_code(null);
			
				setStart_date(null);
				setEnd_date(null);
				setStart_hr(null);
				setEnd_hr(null);
			
				setBank_code(null);
				setFirstName(null);
				setLastName(null);
				setPhone(null);
				setEmail(null);
				setCardScheme(null);
				setBoundworks(null);
				setBoundLimit(null);
				setCmsServer(null);
				setResetLimit(null);
				setCurrentStatus(null);
				setTrack2CardNumber(null);
			

				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				try
				{
					Date d = df.parse(df.format(new Date()));
					setStart_date(d);
					setEnd_date(d);
				}
				catch(Exception ex){ex.printStackTrace();
				}
				
				setStart_hr("00");
				setEnd_hr("23");
		 	}
		
			/*Funds Transfer*/
			public void viewFundsTransferLog()
			{
				try
				{
					fundsTransfer.clear();
					ReportModel reportModel = new ReportModel();
					CardManagementModel cardModel = new CardManagementModel();
					
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		            String beginDate = df.format(getStart_date());
		            beginDate = beginDate + " " + getStart_hr() + ":00";
		            
		            String endDate = df.format(getEnd_date());
		            endDate = endDate + " " + getEnd_hr() + ":59";
		            //mobile number
		            String card_num = getCard_num().trim();
		            String optn = getOptionType();
		            String amount = getMobileno().trim();
		            
		            String transCode = getTrans_code();
		            String transCount = getTrans_count();
		            String tranSummary = getTransSummary();
		            
		            
		            if(optn == null)
		            { 
		            	optn = "0";
		            }
		            
		            if(amount.length() <=0)
		            {
		            	amount = "0";
		            }
		            
					//System.out.println("optn " + optn);
					//System.out.println("amount " + amount);
		            
		            
		            FacesContext context = FacesContext.getCurrentInstance();
				    String schemes = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
				    
					
		            String scheme = "702";
		            
		            
		            fundsTransfer = cardModel.getFundsTransferTransaction(card_num, optn, Double.parseDouble(amount), beginDate ,
		            		endDate,transCode,transCount, tranSummary, scheme);
		            System.out.print("Funds Transfer Log   "+fundsTransfer.size());
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
			
			public void getCardSchemes()
			{
				
				CardManagementModel model = new CardManagementModel();
				schemeList = model.getAllCardScheme();
				System.out.println("getting all Scheme "+schemeList.size());
				
			}
		    /**
		     * This method is to get card details base on there scheme
		     */
			public void getCardHolderEnquires()
			{
				
				cardholdertranLog.clear();
				CardManagementModel model = new CardManagementModel();
				
				String message = "";
			
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		        
				String beginDate = df.format(getStart_date());
		        beginDate = beginDate + " " + getStart_hr() + ":00";
		        
		        String endDate = df.format(getEnd_date());
		        endDate = endDate + " " + getEnd_hr() + ":59";
		   
				String cardNumber = getCard_num();
				if(cardNumber==null)
				{
					cardNumber="";
				}
				
				
				String phone = getPhone();
				
				System.out.println("Phone values  "+phone);
				if(phone==null)
				{
					phone = "";
				}
				String email = getEmail();
				if(email==null)
				{
					email = "";
				}
		
				String databaseServer = getCmsServer();
				
				if(databaseServer==null)
				{
					databaseServer = "";
				}
			
				System.out.println("cms server  "+databaseServer);
				
				if(databaseServer.equalsIgnoreCase("VirtualConsole"))
				{
					databaseServer = "1";
					
				}
				else if(databaseServer.equalsIgnoreCase("PocketMoni"))
				{
					databaseServer = "2";
					
				}
				else if(databaseServer.equalsIgnoreCase("Central"))
				{
					
					databaseServer = "3";
					
				}
				
				

			    FacesContext context = FacesContext.getCurrentInstance();
			    String cardschemeNumber = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getCardSchemeNumbers();
				    
				System.out.println("Card Scheme @ session --> "+cardschemeNumber);
			
				
			
				if(!databaseServer.isEmpty())
				{
					if(cardNumber.length() > 0 || phone.length() > 0 )
					{	
						cardholdertranLog = model.getCardHolderEnquires(databaseServer,cardNumber,cardschemeNumber,phone,beginDate,endDate) ;
						System.out.println("cardholdertranLog  ---  >> > >"+cardholdertranLog.size());
					}
					else
					{
						facesMessages.add(Severity.INFO, " Card number or Mobile number is required ");
					}
				}
				else
				{
						facesMessages.add(Severity.INFO, " CMS Server is required  ");
				}
				
				/*else if(databaseServer.isEmpty())
				{
					
				}	*/
				
				
				
			}
			/*method to get track 2 card number details */
			
			public void getCardHolderEnquiresTrack2()
			{
				
				cardholdertranLog.clear();
				CardManagementModel model = new CardManagementModel();
				
				String message = "";
			
				String databaseServer = getCmsServer();
				
				if(databaseServer==null)
				{
					databaseServer = "";
				}
				
				String track2 = getTrack2CardNumber();
				if(track2==null)
				{
					
					track2 = "";
				}
				
				
				System.out.println("cms server  "+databaseServer);
				
				if(databaseServer.equalsIgnoreCase("VirtualConsole"))
				{
					databaseServer = "1";
					
				}
				else if(databaseServer.equalsIgnoreCase("PocketMoni"))
				{
					databaseServer = "2";
					
				}
				else if(databaseServer.equalsIgnoreCase("Central"))
				{
					
					databaseServer = "3";
					
				}
				

				if(!databaseServer.isEmpty())
				{
					if(track2.length() > 0 )
					{	
						cardholdertranLog = model.getCardHolderEnquiresTrack2(databaseServer,track2) ;
						System.out.println("cardholdertranLog  ---  >> > >"+cardholdertranLog.size());
					}
					else
					{
						facesMessages.add(Severity.INFO, " Track 2 card number is required ");
					}
				}
				else
				{
						facesMessages.add(Severity.INFO, " CMS Server is required  ");
				}
			
					
		
				
			}

			/*method to view hotlisted and blocked card */
			
			public void view_Hotlisted_Blocked_Card()
			{
				
				
				CardManagementModel model = new CardManagementModel();
				cardholdertranLog.clear();
				
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		        
				String beginDate = df.format(getStart_date());
		        beginDate = beginDate + " " + getStart_hr() + ":00";
		        
		        String endDate = df.format(getEnd_date());
		        endDate = endDate + " " + getEnd_hr() + ":59";
		        
				/*String scheme = getCardScheme();*/
				
				   String option = getChannel_id();
				   if(option==null)
				   {
					   option = "";
				   }
				
	
					FacesContext context = FacesContext.getCurrentInstance();
					String cardSchemeServ = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getCardScheme();
					
					String databaseServer[] = cardSchemeServ.split(":"); 
					
					cardSchemeServ = databaseServer[0];  // databaseServer[0] server 
					String cardscheme = databaseServer[1]; //  databaseServer[1] cardScheme  
					
					System.out.println("Server    "+cardSchemeServ+" Card scheme  "+cardscheme);
					System.out.println("Option Cases  - - -- -  >      "+option);
					
					
					cardholdertranLog = model.getHotlisted_Blocked_Card(cardSchemeServ,cardscheme, option,beginDate,endDate) ;
					System.out.println("cardholdertranLog  ---  >> > >"+cardholdertranLog.size());
			
				
			}
			
			
			/*Method for the card status report for scheme*/
			public void getCardStatusReportForScheme()
			{
				try
				{
			
					CardManagementModel cardModel = new CardManagementModel();
					cardschemeStatusList.clear();
					
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			        
					String beginDate = df.format(getStart_date());
			        beginDate = beginDate + " " + getStart_hr() + ":00";
			        
			        String endDate = df.format(getEnd_date());
			        endDate = endDate + " " + getEnd_hr() + ":59";
			        

			        FacesContext context = FacesContext.getCurrentInstance();
					String cardSchemeServ = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getCardScheme();
			        
					String databaseServer[] = cardSchemeServ.split(":"); 
					
					cardSchemeServ = databaseServer[0];  // databaseServer[0] server 
					String cardscheme = databaseServer[1]; //  databaseServer[1] cardScheme  
					
					
					cardschemeStatusList = cardModel.getCardStatusReportSummary(beginDate, endDate, cardscheme, cardSchemeServ);
					System.out.println("getCardStatusReportSummary()   ---     "+cardschemeStatusList.size());
				
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
			
			/*Method to get Transactions based on a card number*/
			public void getCardHolderTransactions()
			{
				try
				{
					CardManagementModel cardModel = new CardManagementModel();
					cardholdertranLog.clear();
					
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			        
					String beginDate = df.format(getStart_date());
			        beginDate = beginDate + " " + getStart_hr() + ":00";
			        
			        String endDate = df.format(getEnd_date());
			        endDate = endDate + " " + getEnd_hr() + ":59";
					
			        E_TRANSACTION tran = null;
			        double debit_total = 0.0;
			        double credit_total = 0.0;
			        
					String cardnum = getCard_num().trim();
					String id = getId();
					
					String firstname = getFirstName();
					String lastname = getLastName();
					String email = getEmail();
					String mobile = getPhone();
					
		
					
						if(cardnum.length() > 0 || mobile.length() > 0)
						{
							System.out.println("checking for Cardnum  "+cardnum);
				        	cardholdertranLog = cardModel.getCardHolderTransactions(cardnum, mobile,beginDate, endDate);
				        	System.out.println("size Return "+cardholdertranLog.size());
					        for(int i=0;i<cardholdertranLog.size();i++)
					        {
					        	tran = (E_TRANSACTION)cardholdertranLog.get(i);
					        	if(tran.getDebitAmt().trim().length()>0)
					        	{
					        		debit_total += Double.parseDouble(tran.getDebitAmt());
					        	}
					        	if(tran.getCreditAmt().trim().length()>0)
					        	{
					        		credit_total += Double.parseDouble(tran.getCreditAmt());
					        		
					        	}
					        	
					        }
					        setTotal_amount(debit_total);
					        setTotal_other_amount(credit_total);	
							
						}
						else
						{
							facesMessages.add(Severity.INFO, " Card Number or Mobile Number is required  ");
							
						}
		
								        
			        
					    
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
			
			public void sendTransactionReportViaEmail()
			{
				
				String message = "";
				
				String firstname = getFirstName();
				String lastname = getLastName();
				String email = getEmail();
				String transDate = getTransDate();
				String transNo = getTransNo();
				String transDesc = getTransDesc();
				String transCode = getTransCode();
				String channel = getChannel();
				String merchantCode = getMerchantCode();
				String cardNum = getEdit_id();
				double debitAmt = getDebitAmt();
				double creditAmt = getCreditAmt();
				double totalAmt = getTotal_amount();
				
				System.out.println("===========================================================================================================================================================================================================================================================================================");
				System.out.println("Firstname  "+firstname+"Lastname"+lastname+"Email"+email+"transDate"+transDate+"trans NO"+transNo+"transDesc"+transDesc+"Trans code"+transCode+"Channel"+channel+"Merchant Code"+merchantCode+"Card num"+cardNum+"debit Amount"+debitAmt+"credit Amount"+creditAmt+"total Amount"+totalAmt);
				System.out.println("===========================================================================================================================================================================================================================================================================================");
				
				//MailDemo demo  = new MailDemo();
				try
				{
					//demo.sendMail(firstname,lastname,email,transDate,transNo,transDesc,transCode,channel,merchantCode,cardNum,debitAmt,creditAmt,totalAmt);
					Events.instance().raiseEvent("transactionmailer");
					message = " Email has been sent successfully";
				}
				catch(Exception e)
				{
					e.printStackTrace();
					message = " Email not sent !  "+e.getMessage();
				}
				
				facesMessages.add(Severity.INFO,message );
				
			}
			/*
			 * THis method is to get Card holder details
			 */
			public void getCardHolderDetails()
			{
				CardManagementModel model = new CardManagementModel();
				cardholdertranLog.clear();
				String cardNumber = getCard_num();
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		        
				String beginDate = df.format(getStart_date());
		        beginDate = beginDate + " " + getStart_hr() + ":00";
		        
		        String endDate = df.format(getEnd_date());
		        endDate = endDate + " " + getEnd_hr() + ":59";
				
		    	
				FacesContext context = FacesContext.getCurrentInstance();
				String cardScheme = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
				//String[] databaseServer = cardScheme.split(":");

				cardholdertranLog = model.getCardHolders_Update_Details(cardNumber,cardScheme,beginDate,endDate) ;
				System.out.println("cardholdertranLog  ---  >> > >"+cardholdertranLog.size());
				
				
			}
			public void getSchemeReport()
			{
				
			
				CardManagementModel cmmodel = new CardManagementModel();
				schemeLists.clear();
				String cardNumber = getCard_num();
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		        
				String beginDate = df.format(getStart_date());
		        beginDate = beginDate + " " + getStart_hr() + ":00";
		        
		        String endDate = df.format(getEnd_date());
		        endDate = endDate + " " + getEnd_hr() + ":59";
				
				String Scheme = getSchemeId();
				
				try
				{
				
					schemeLists = cmmodel.getSchemeReport(Scheme,beginDate,endDate);
					System.out.print("scheme Report size "+schemeLists.size());
					
				}catch(Exception ex)
				{
						ex.printStackTrace();
				}
				
			}
			
			
			public void getAllCardType()
		    {
				try
				{
				
					CardManagementModel cardmodel = new CardManagementModel();
					
				
					displayCardType =  cardmodel.getCardType();
					System.out.println("Display ALL Card Type  "+displayCardType.size());
					
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}

		    }
			
			public void getAllControlName()
		    {
				try
				{
				
					CardManagementModel cardmodel = new CardManagementModel();
					
				
					displayControlName =  cardmodel.getCardControl();
					System.out.println("Display ALL Card Type  "+displayControlName.size());
					
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}

		    }
			/*
			 * This method is to get Card details before Authorisation
			 */
			public void getCardHolderDetails_Authorizaction()
			{

				CardManagementModel model = new CardManagementModel();
				cardholdertranLog.clear();
				String cardNum = getId();
				System.out.println("id "+cardNum);
				
				cardholdertranLog = model.getCardHolders(cardNum) ;
				System.out.println("cardholdertranLog  ---  >> > >"+cardholdertranLog.size());
				
			}
			
		/*	public void getCardHolderDetails_Auth()
			{

				CardManagementModel model = new CardManagementModel();

				pendingAuthorization = model.getCardHoldersDetail_Auth();
				System.out.println("pendingAuthorization  ---  >> > >"+pendingAuthorization.size());
				
			}*/
			
			public void getCardHolderDetails_Pending_Dehotlist()
			{
				//pendingAuth.clear();
				CardManagementModel model = new CardManagementModel();
			
				
				pendingAuth = model.getDehotlistedDetails();
				System.out.println("pendingAuthorization  ---  >> > >"+pendingAuth.size());
				
			}
			
			public void getCardHolderDetailsPendingPinReGeneration()
			{
				//pendingAuth.clear();
				CardManagementModel model = new CardManagementModel();
				pendingPinAuth = model.getPendingPinReGenerationDetails();
				System.out.println("pending pin Re Generation Authorization  ---  >> > >"+pendingPinAuth.size());
				
			}
			
			
			public void getCardHolderDetails_Pending_UNBLOCKED()
			{
				//unBlockAuth.clear();
				CardManagementModel model = new CardManagementModel();
				
				String unblockStatus = "0";
				//System.out.println("id "+cardNum);
				
				unBlockAuth = model.getCardHolderDetails_Block_Pending(unblockStatus);
				System.out.println("pendingUNBLOCKAuthorization  ---  >> > >"+unBlockAuth.size());
				
			}
		
			public void getCardHolderDetails_Block_Pending()
			{
				pendingAuth_Block.clear();

				CardManagementModel model = new CardManagementModel();
				
				String blockStatus = "1";
				//System.out.println("id "+cardNum);
				
				pendingAuth_Block = model.getCardHolderDetails_Block_Pending(blockStatus);
				System.out.println("pendingAuthorization  ---  >> > >"+pendingAuth_Block.size());
				
			}
			
		public void getBoundWorkPending()
		{
			
			try
			{
				CardManagementModel model = new CardManagementModel();
				
				pendingResetBoundWork = model.getBoundWorkPending();
				System.out.println("pendingResetBoundWork Size  "+pendingResetBoundWork.size());
				
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
		}
		
		
		public void getPending_Rest_Bound_Limit()
		{
			
			try
			{
				CardManagementModel model = new CardManagementModel();
					
				pendingResetBoundLimit = model.getReset_Bound_Limit_Pending();
				System.out.println("pendingResetBoundLimit Size  "+pendingResetBoundLimit.size());
				
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
		}
		
		
		public void getPending_Extend_CardExpire()
		{
			
			try
			{
				CardManagementModel model = new CardManagementModel();
					
				pendingCardExpiration = model.getReset_Extend_CardExpiration();
				System.out.println("pendingCardExpiration Size  "+pendingCardExpiration.size());
				
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
		}
		
		
			/*
			 * 
			 * This is method is to get Card Transaction Report 
			 */
			public void getCardTrasactionReport()
			{
				
				
				CardManagementModel model = new CardManagementModel();
				cardholdertranLog.clear();
				
				E_TRANSACTION eTrans = null;
		        double debit_total = 0.0;
		        double credit_total = 0.0;
		        
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		        
				String beginDate = df.format(getStart_date());
		        beginDate = beginDate + " " + getStart_hr() + ":00";
		        
		        String endDate = df.format(getEnd_date());
		        endDate = endDate + " " + getEnd_hr() + ":59";
		        
				String cardNumber = getCard_num();
				if(cardNumber==null)
				{
					cardNumber="";
				}

				//System.out.println("scheme "+scheme+"cardNumber"+cardNumber+"firstname"+firstName+"lastname"+lastName+"phone"+phone+"Email "+email);
				
				cardholdertranLog = model.getCardTransactionReport(cardNumber, beginDate, endDate) ;
				System.out.println("cardholdertranLog  ---  >> > >"+cardholdertranLog.size());
				
				
			}
			/*
			 * This is method is to authorised a card number for hot list
			 */
			public void Hotlist_Card()
			{
			
				
				 CardManagementModel model = new CardManagementModel();
				 
				 String message = "";
				 String myId = getId();
				// System.out.println("MY ID PARA     ----          "+myId);
				 
				try
				{
					
					FacesContext context = FacesContext.getCurrentInstance();
					String username = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
					String ipAddress = InetAddress.getLocalHost().getHostAddress();
					
					String cardnumd = getCard_num();
					String cardnum = getEdit_id();
					String cardnumid = getId();
					
					System.out.println("cardnum--->> "+cardnumd+"cardnumEdit--->E"+cardnum+"CardId"+cardnumid);
					String initUsername = username;
					String initIp = ipAddress;
					String authUser = initUsername;
					String authIp = initIp;
					String authStatus = "1";
			
					
						//message = model.Authorized_Hotlisted_Card(cardnum,initUsername,initIp,authStatus);
					
						message = model.Hotlisted_Card(cardnum,initUsername,initIp,authStatus);
					
						System.out.println("Message output....."+message);
						if(message.equalsIgnoreCase("SUCCESS"))
						{
							message = " Record Successfully Hotlisted ";
						
						}
						else
						{
							message = " unable to Hotlist ";
						}
						
					
				
					facesMessages.add(Severity.INFO, message);
					
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
				
					
				
			}
			//Authorized_Rest_Bound_work
			
			
			public void getAuthorized_Reset_Bound_work()
			{
				
				 CardManagementModel model = new CardManagementModel();
				 
				 String message = "";
				try
				{
					
					String cardnum = getEdit_id();
					System.out.println("getAuthorized_Reset_Bound_work card num here   - -- -   > "+cardnum);
					message = model.getAuthorized_Reset_Bound_work(cardnum);
					facesMessages.add(Severity.INFO, message);
					
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
				
					
				
			}
			
	//UNAuthorized_Rest_Bound_work 
			
			public void unAuthorized_Reset_Bound_work()
			{
				
				 CardManagementModel model = new CardManagementModel();
				 
				String message = "";			 
				try
				{
					
					String cardnumd = getCard_num();
					String cardnum = getEdit_id();
					String cardnums = getId();	
					
					message = model.unAuthorized_Reset_Bound_work(cardnum);
					
					if(message.equalsIgnoreCase("SUCCESS"))
					{
						message = " Awaiting [ Reset Bound Work ] has been Successfully DisRegarded ";
					}
					else
					{
						message = " Unable to DisRegard ";
						
					}
						
					facesMessages.add(Severity.INFO, message);
					
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
					
				
			}
			public void unAuthorized_Reset_Bound_Limit()
			{
				
				 CardManagementModel model = new CardManagementModel();
				 
				String message = "";			 
				try
				{
					
					String cardnumd = getCard_num();
					String cardnum = getEdit_id();
					String cardnums = getId();	
					
					message = model.unAuthorized_Reset_Bound_Limit(cardnum);
					
					if(message.equalsIgnoreCase("SUCCESS"))
					{
						message = " Awaiting [ Reset Bound Limit ] has been Successfully DisRegarded ";
					}
					else
					{
						message = " unable to DisRegard ";
						
					}
						
					facesMessages.add(Severity.INFO, message);
					
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
					
				
			}
			
			public void unAuthorized_DEHOTLIST_Card()
			{
				
				 CardManagementModel model = new CardManagementModel();
				 
				String message = "";			 
				try
				{
					
					String cardnum = getCard_num();
					String databaseServer = getEdit_id();
					String cardnums = getId();	
					
					
					
					if(databaseServer.equalsIgnoreCase("Virtual Server"))
					{
						databaseServer = "0";
						
					}else if(databaseServer.equalsIgnoreCase("Pocketmoni Server"))
					{
						databaseServer = "1";
						
					}else if(databaseServer.equalsIgnoreCase("Server on .35"))
					{
						
						databaseServer = "2";
						
					}
				

					message = model.unAuthorized_DEHOTLIST_Card(databaseServer,cardnum);
					
					if(message.equalsIgnoreCase("SUCCESS"))
					{
						message = " Record has been successfully disregarded ";
					}
					else
					{
						message = " Unable to disRegard  ";
						
					}
						
					facesMessages.add(Severity.INFO, message);
					
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
					
				
			}
			
			public void unAuthorized_UNBLOCK_BLOCK()
			{
				
				 CardManagementModel model = new CardManagementModel();
				 
				String message = "";			 
				try
				{
					
					String cardnumd = getCard_num();
					String cardnum = getEdit_id();
					String cardnums = getId();	
					
					message = model.unAuthorized_UNBLOCK_CARD(cardnum);
					
					if(message.equalsIgnoreCase("SUCCESS"))
					{
						message = " Awaiting [ UN-BLOCKED CARD] has been Successfully DisRegarded ";
					}
					else
					{
						message = " unable to DisRegard ";
						
					}
						
					facesMessages.add(Severity.INFO, message);
					
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
					
				
			}
			
			public void unAuthorized_Extend_CardExpiration()
			{
				
				 CardManagementModel model = new CardManagementModel();
				 
				String message = "";			 
				try
				{
					
					String cardnumd = getCard_num();
					String cardnum = getEdit_id();
					String cardnums = getId();	
					
					message = model.unAuthorized_Extend_CardExpiration(cardnum);
					
					if(message.equalsIgnoreCase("SUCCESS"))
					{
						message = " Awaiting [ Extend Card Expiration ] has been Successfully DisRegarded ";
					}
					else
					{
						message = " unable to DisRegard ";
						
					}
						
					facesMessages.add(Severity.INFO, message);
					
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
					
				
			}
			public void getAuthorized_Extend_CardExpiration()
			{
				
				 CardManagementModel model = new CardManagementModel();
				 
				 String message = "";
				 
				try
				{
					
					FacesContext context = FacesContext.getCurrentInstance();
					String username = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
					String ipAddress = InetAddress.getLocalHost().getHostAddress();
					
					String cardnumd = getCard_num();
					String cardnum = getEdit_id();
					String prevExpire = getPrevStatus();
					String cardnums = getId();	
					String initUsername = username;
					String initIp = ipAddress;
					String authUser = initUsername;
					String authIp = initIp;
					String authStatus = "1";
					
					
					String cardExpiration = getCardExpiration(); //01-2014
					System.out.print("from d interface  --  -- "+cardExpiration);
					
					String cardExpYear = cardExpiration.substring(3, 7); //2014 Year
					System.out.println("Card Year "+cardExpYear);
					
					String cardExpMonth = cardExpiration.substring(0,2); //01 Month
					System.out.println("cARD Month "+cardExpMonth);
					
					System.out.print("cardExpiration"+cardExpiration);
				
					int  year = Integer.parseInt(cardExpYear.trim()); // add 2 to the year

					System.out.print("Int year "+year);
					
					int  extendYear = year + 2 ;
					
					System.out.print("Int extendYear "+extendYear);
					
					String cardExtended = cardExpMonth+"-"+extendYear;
					
					System.out.print("CARD Extended Fianlly .."+cardExtended);
					
					String databaseServer = getCmsServer();
					System.out.println("Database serverss... "+databaseServer);
					/*
					if(databaseServer.equalsIgnoreCase("VirtualConsole"))
					{
						databaseServer = "1";
						
					}
					else if(databaseServer.equalsIgnoreCase("PocketMoni"))
					{
						databaseServer = "2";
						
					}
					else if(databaseServer.equalsIgnoreCase("Central"))
					{
						
						databaseServer = "3";
						
					}*/
					

						System.out.println("cardnumd "+cardnumd+"cardnum_Edith"+cardnum+"cardExpiration "+cardExpiration+"previous Card Expire"+prevExpire);
						message = model.Authorized_Extend_CardExpiration(databaseServer,cardnum,initUsername,initIp,authStatus,cardExtended,prevExpire);
						System.out.println("Message output....."+message);
						
						if(message.equalsIgnoreCase("SUCCESS"))
						{
							message = " Record has been successfully authorised ";
								
						}
						else
						{
							message = " Unable to authorised ";
							
						}
						
					
				
					facesMessages.add(Severity.INFO, message);
					
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
				
					
				
			}
			
			
			
			public void getAuthorizedResetBoundlimit()
			{
				
				 CardManagementModel model = new CardManagementModel();
				 

				try
				{
					
					String cardnum = getEdit_id();
					
					String dbServer = getCmsServer();
					FacesContext context = FacesContext.getCurrentInstance();
					String username = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
					String ipAddress = InetAddress.getLocalHost().getHostAddress();
					
					
					String initUsername = username;
					String initIp = ipAddress;
					String authUser = initUsername;
					String authIp = initIp;
					String authStatus = "1";
					
					String currentBoundLimit = getResetLimit();
					
					String privoustBoundLimit = getPrevStatus();
					
			
					String message = model.getAuthorizedResetBoundlimit(cardnum,dbServer,initUsername,initIp,authStatus,currentBoundLimit,privoustBoundLimit);
					
			
					facesMessages.add(Severity.INFO,message);
					
					
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
				
	
				
				
			
					
				
			}
			
			
			
			public void getAuthorized_Card_DeHotlist()
			{
				
				 CardManagementModel model = new CardManagementModel();
				 
				 String message = "";
				 String myId = getId();
				 
				try
				{
					
					FacesContext context = FacesContext.getCurrentInstance();
					String username = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
					String ipAddress = InetAddress.getLocalHost().getHostAddress();
					
					String cardnum = getCard_num();
					String databaseServer = getCmsServer();
					String cardnums = getId();
					
					System.out.println("cardnum--->> "+cardnum+"cardnumEdit--->E"+cardnums+"CardId"+cardnum);
					String initUsername = username;
					String initIp = ipAddress;
					String authUser = initUsername;
					String authIp = initIp;
					String authStatus = "1";
					
					
					System.out.println("Database server    -- - "+databaseServer);
					
					message = model.getAuthorized_DeHotlisted_Card(databaseServer,cardnum,initUsername,initIp,authStatus);
			
					facesMessages.add(Severity.INFO, message);
					
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
					
			}
			
			
			
			
			public void Block_Hotlist_Card()
			{
			
				
				 CardManagementModel model = new CardManagementModel();
				 
				 String message = "";
				 
				try
				{
					
					FacesContext context = FacesContext.getCurrentInstance();
					String username = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
					String ipAddress = InetAddress.getLocalHost().getHostAddress();
					
					String cardnumd = getCard_num();
					String cardnum = getEdit_id();
					String cardnums = getId();
					
					System.out.println("Cardnum --- >>>>>"+cardnum+" Cardnum2"+cardnumd+"cardnum Edith"+cardnum);
					String initUsername = username;
					String initIp = ipAddress;
					String authUser = initUsername;
					String authIp = initIp;
					String authStatus = "1";
					
					String databaseServer = getCmsServer();
					
					if(databaseServer.equalsIgnoreCase("VirtualConsole"))
					{
						databaseServer = "1";
						
					}
					else if(databaseServer.equalsIgnoreCase("PocketMoni"))
					{
						databaseServer = "2";
						
					}
					else if(databaseServer.equalsIgnoreCase("Central"))
					{
						
						databaseServer = "3";
						
					}

						message = model.Block_Hotlist_Card(databaseServer,cardnum,initUsername,initIp,authStatus);
						
						System.out.println("Message output....."+message);
						if(message.equalsIgnoreCase("SUCCESS"))
						{
							
							message = " Record has been Successfully Blocked and Hotlisted ";
						}
						else if(message.equalsIgnoreCase("exists"))
						{ 
						
							message = " Record has  Already been Blocked and Hotlisted  ";
						}
						else
						{
							message = " Unable to Blocked and  Hotlist this Card ";
						}
						
					
				
					facesMessages.add(Severity.INFO, message);
					
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
						
				
			}
			
			public void Authorized_UNBlock_CARD()
			{
			
				
				 CardManagementModel model = new CardManagementModel();
				 
				 String message = "";
				 
				try
				{
					
					FacesContext context = FacesContext.getCurrentInstance();
					String username = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
					String ipAddress = InetAddress.getLocalHost().getHostAddress();
					
					String cardnumd = getCard_num();
					String cardnum = getEdit_id();
					String cardnums = getId();
					
					System.out.println("CardnumE --- >>>>>"+cardnum+" Cardnum2"+cardnumd);
					String initUsername = username;
					String initIp = ipAddress;
					String authUser = initUsername;
					String authIp = initIp;
					String authStatus = "1";
			
					
						message = model.Authorized_UNBlOCK_CARD(cardnum,initUsername,initIp,authStatus);
						System.out.println("Message output....."+message);
						if(message.equalsIgnoreCase("SUCCESS"))
						{
							
							message = " Awaiting [ UNBLOCKED CARD ] has been Successfully Authorised ";
							//blockCard();	
						}
						else
						{
							message = " unable to Authorised ";
						}
						
					
				
					facesMessages.add(Severity.INFO, message);
					
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
				
					
				
			}
			
			/*
			 * 
			 * This is method is to get Card Transaction summary break down
			 */
			public void getCardBreakDownTrasactionSumary()
			{
				
				try
				{
				
					CardManagementModel model = new CardManagementModel();
					String cardId = getId();
					cardholderbreakdownlog = model.getCardBreakDownTrasactionSumary(cardId) ;
					if(cardholderbreakdownlog.size() > 0)
					{
						E_TRANSACTION trans = (E_TRANSACTION)cardholderbreakdownlog.get(0);
						setCard_num(trans.getCard_num().trim());
				
					}
					
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
				
			
				
			}
			
			public void setCardHolderEnquiryToEdit()
			{
				try
				{
					
					CardManagementModel model = new CardManagementModel();
					
					String cardnum = getId();
					String dbServer = getCmsServer();
					System.out.println("Cardnum with ID    "+cardnum+"CMS Server  - - - -"+dbServer);
					
					if(dbServer.equalsIgnoreCase("VirtualConsole"))
					{
						dbServer = "1";
						
					}
					else if(dbServer.equalsIgnoreCase("PocketMoni"))
					{
						dbServer = "2";
						
					}
					else if(dbServer.equalsIgnoreCase("Central"))
					{
						
						dbServer = "3";
						
					}
				
				    holderList = model.getCardHolderEnquires(dbServer,cardnum);
					if(holderList.size()>0)
					{
						
						CardHolder holder = (CardHolder)holderList.get(0);
						setCard_num(holder.getCard_num().trim());
						setFirstName(holder.getFirstname().trim());
						setLastName(holder.getLastname().trim());
						setStreet(holder.getStreet().trim());
						setPhone(holder.getPhone().trim());
						setEmail(holder.getEmail());
						setCardType(holder.getCardType());
					    setMinBalance(holder.getMinBalance());
					    setBoundworks(holder.getBound_work());
						String minBal = holder.getMinBalance();
						String boundWork = holder.getBound_work();
						
						if(boundWork == null)
						{
							boundWork = "0.0";
							setBoundWork(Double.valueOf(boundWork));
							
						}					
						if(minBal == null)
						{
							minBal = "0.0";
							setMinBal(Double.valueOf(minBal));
						}
						
						setMinBal(Double.valueOf(minBal));
						setBoundWork(Double.valueOf(boundWork));
						
						System.out.println("=====MinBal-================== "+minBal);
						//String boundWork = holder.getBound_work();
						
						/*if(boundWork == null)
						{
							boundWork = "0.0";
						}*/
						//setMinBal();
						//setBoundWork(Double.parseDouble(boundWork));
						setControlId(holder.getControlId());
						
					  
						setEmail(holder.getEmail());
					}
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
			
			public void CardHolderList()
			{
				
				CardManagementModel model = new CardManagementModel();
				String cardNum = getId();
				System.out.println("CARD WITH ID -- >   "+cardNum);
				try
				{
				
					updateLog = model.getCardHolderDetails(cardNum);
					System.out.println("size "+updateLog.size());
					
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
				
			}
			/*
			 * 
			 * This method is to update card details
			 * 
			 */
			public void updateCardHolder()
			{
				
			
				CardManagementModel model = new CardManagementModel();
				
				CardHolder cholder = null;
				String message = "";
				String cardNum = getCard_num();
				String mess = "SUCCESS";
				
				String firstName = getFirstName();
				if(firstName==null)
				{
					firstName="";
				}
				String lastName = getLastName();
				if(lastName==null)
				{
					lastName = "";
				}
				String phone =  getPhone();
				if(phone==null)
				{
					phone = "";
				}
				String street = getStreet();
				if(street==null)
				{
					street = "";
				}
				
				String email = getEmail();
				if(email == null)
				{
					email = "";
				}
				
				String cardType = getCardType();
				String controlId =  getControlId();
				String minbal = getMinBalance();
				String boundworks = getBoundworks();
				
				double minBal = getMinBal();
				double  boundWork  = getBoundWork();
		       
				String dbServer = getCmsServer();
				
				System.out.println("minbal + boundworks "+minbal+ " boundworks "+boundworks);
				
				System.out.println(" double -- >>minbal + boundworks "+minBal+ " boundworks "+boundWork);
				
				
				if(dbServer.equalsIgnoreCase("VirtualConsole"))
				{
					dbServer = "1";
					
				}
				else if(dbServer.equalsIgnoreCase("PocketMoni"))
				{
					dbServer = "2";
					
				}
				else if(dbServer.equalsIgnoreCase("Central"))
				{
					
					dbServer = "3";
					
				}
	
				updateLog = model.UpdateCardHolder(dbServer,cardNum,cardType,controlId, minBal,boundWork, firstName, lastName, phone, street, email);
			    if(updateLog.size() > 0)
			    {
			    	message ="Record Updated Successfully";
			    }else
			    {
			    	message ="Record not updated";
			    }
				
			        	    	
			     
			/*	
				System.out.println("message   ----"+message);
				if(message.equalsIgnoreCase("SUCCESS"))
				{
				
						message = "Record Successfully Updated ";
				}else
				{
						message = "Record Not Updated";
				}
				 
				/*for(int i=0;i<updateLog.size();i++)
			      {
					
			    	 
			      		System.out.println("card upcate....."+updateLog.get(i));
			      		cholder = (CardHolder)updateLog.get(i);
						  System.out.println("Card succcccxxxx "+cholder.getModified());
						if(cholder.getModified().equalsIgnoreCase("SUCCESS"))
						{
						
								message = "Record Successfully Updated ";
						}else
						{
								message = "Record Not Updated";
						}
						
			      }
			      */
					facesMessages.add(Severity.INFO, message);
				
				
			}
			
			public void hotlistCard()
			{
				try
				{
			
					CardManagementModel model = new CardManagementModel();
					String card_num = getEdit_id().trim();//card_num;
					String reason = "suspected";

					String message = model.createE_Exception(card_num, reason);
				
					System.out.println("message " + message);
					
					//facesMessages.add(Severity.WARN, message);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
			
			public void hotlistCard_Auth_Pending()
			{
				try
				{
			
					CardManagementModel model = new CardManagementModel();
					String card_num = getEdit_id().trim();//card_num;
					String hotlistStatus = "1";
					//String blockStatus = "1";

					String message = model.createPending_Hotlist(card_num, hotlistStatus);
				
					//System.out.println("message " + message);
					
					facesMessages.add(Severity.WARN, message);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
			
			public void logDeHotlistedDetails()
			{
				try
				{
			
					CardManagementModel model = new CardManagementModel();
					String card_num = getEdit_id().trim();//card_num;
					
					String fistname = getFirstName();
					String lastname = getLastName();
					String email = getEmail();
					String phone =getPhone();
					String street = getStreet();
					String cardExp = getCardExpiration();
					String onlinebalance = getOnlinebalance();
					String cashwithdraw = getWithDrawLimit();
					String boundwork = getBoundworks();
					String boundlimit= getBoundLimit();
					String userhotlist = getUserHotlist();
					String userblocked = getUserBlock();
					String deholistPending = "Queue";

					String databaseServer = getCmsServer();
					System.out.println("Database server    -- - "+databaseServer);
				
					
					if(databaseServer.equalsIgnoreCase("VirtualConsole"))
					{
						databaseServer = "1";
						
					}
					else if(databaseServer.equalsIgnoreCase("PocketMoni"))
					{
						databaseServer = "2";
						
					}
					else if(databaseServer.equalsIgnoreCase("Central"))
					{
						
						databaseServer = "3";
						
					}
					
					FacesContext context = FacesContext.getCurrentInstance();
					String initiator = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
					String ipAddress = InetAddress.getLocalHost().getHostAddress();
					
					
					String message = model.logDeHotlistedDetails(databaseServer,card_num,fistname,lastname,email,
							phone,street,cardExp,onlinebalance,cashwithdraw,boundwork,boundlimit,userhotlist,userblocked,deholistPending,initiator);

					facesMessages.add(Severity.WARN, message);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
			
			public void logResetBoudWorkDetails()
			{
				try
				{
					
					CardManagementModel model = new CardManagementModel();
					String card_num = getEdit_id().trim();//card_num
					String fistname = getFirstName();
					String lastname = getLastName();
					String email = getEmail();
					String phone =getPhone();
					String street = getStreet();
					String cardExp = getCardExpiration();
					String onlinebalance = getOnlinebalance();
					String cashwithdraw = getWithDrawLimit();
					String boundwork = getBoundworks();
					String boundlimit= getBoundLimit();
					String userhotlist = getUserHotlist();
					String userblocked = getUserBlock();
					
			
					String pendboundWork = "Queue";
					FacesContext context = FacesContext.getCurrentInstance();
					String initiator = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
					String ipAddress = InetAddress.getLocalHost().getHostAddress();

					String message = model.logResetBoudWorkDetails(card_num,fistname,lastname,email,
							phone,street,cardExp,onlinebalance,cashwithdraw,boundwork,boundlimit,userhotlist,userblocked,pendboundWork,initiator);
					
					facesMessages.add(Severity.WARN, message);
					
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
			
			
			public void logPinRegenerationDetails()
			{
				try
				{
					
					System.out.println("==========================================1");
					
					CardManagementModel model = new CardManagementModel();
					
					String card_num = getEdit_id().trim();//card_num
					String fistname = getFirstName();
					String lastname = getLastName();
					String email = getEmail();
					String phone =getPhone();
					String street = getStreet();
					String cardExp = getCardExpiration();
					String onlinebalance = getOnlinebalance();
					String cashwithdraw = getWithDrawLimit();
					String boundwork = getBoundworks();
					String boundlimit= getBoundLimit();
					String userhotlist = getUserHotlist();
					String userblocked = getUserBlock();
				
					String pinReset = "Queue";
					String status = "0";
					String createdBy = "";
					
					FacesContext context = FacesContext.getCurrentInstance();
					String authoriser = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
					String clientipAddress = InetAddress.getLocalHost().getHostAddress();
					
					String mess = model.logResetPinDetails(card_num,fistname,lastname,email,
							phone,street,cardExp,onlinebalance,cashwithdraw,boundwork,boundlimit,userhotlist,userblocked,pinReset,authoriser);
					
				
					facesMessages.add(Severity.WARN, mess);
					
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
			
		
			
			public void getAuthorizedPinRegeneration()
			{
				try
				{
					
					CardManagementModel model = new CardManagementModel();

					String cardnum = getCard_num();
					String phone = getPhone();
					
					String status = "0";
					String authorised = "1";
					String createdBy = "";
					String message = "";
					
					
					FacesContext context = FacesContext.getCurrentInstance();
					String authoriser = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
					String clientipAddress = InetAddress.getLocalHost().getHostAddress();
					String adminEmail = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getEmail();
					
					createdBy = model.getCardHolderIniatiorByName(cardnum);
					
					String sendPinOption = getCurrentStatus();
					
				
					System.out.println("Admin Email Address here ---   >  "+adminEmail+"SEND PIN OPTION "+sendPinOption+"Created By  - "+createdBy);
					if(sendPinOption==null)
					{
						message = "Send pin re-generation option is required  ";
					}
					else
					{
					 	message = model.logPinRegenerationDetails(cardnum,phone,status,createdBy,clientipAddress,authorised,authoriser,clientipAddress,adminEmail,sendPinOption);
					}
					
					facesMessages.add(Severity.WARN, message);
					
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
			
			public boolean isInteger( String boundLimit )  
			{  
			   try  
			   {  
			      Integer.parseInt(boundLimit);  
			      return true;  
			   }  
			   catch( Exception ex )  
			   {  
			      return false;  
			   }  
			}  
			public void logResetBoudLimitDetails()
			{
				try
				{
			
					CardManagementModel model = new CardManagementModel();
					String card_num = getEdit_id().trim();//card_num
					String mess = "";
					
					String fistname = getFirstName();
					String lastname = getLastName();
					String email = getEmail();
					String phone =getPhone();
					String street = getStreet();
					String cardExp = getCardExpiration();
					String onlinebalance = getOnlinebalance();
					String cashwithdraw = getWithDrawLimit();
					String boundwork = getBoundworks();
					
					String boundlimit= getBoundLimit();
					String userhotlist = getUserHotlist();
					String userblocked = getUserBlock();
					
					String currentResetLimit = getResetLimit();
					
					System.out.println("Bound work values "+boundwork+"Bound Limit Values "+boundlimit);
					
					System.out.println("new Value for ResetBount Limit  "+currentResetLimit);
					if(currentResetLimit==null)
					{
						currentResetLimit = "";
					}
					
					String pendingboundlimit = "Queue";
					
					
					String databaseServer = getCmsServer();
					System.out.println("Database server    -- - "+databaseServer);
				
					
					if(databaseServer.equalsIgnoreCase("VirtualConsole"))
					{
						databaseServer = "1";
						
					}
					else if(databaseServer.equalsIgnoreCase("PocketMoni"))
					{
						databaseServer = "2";
						
					}
					else if(databaseServer.equalsIgnoreCase("Central"))
					{
						
						databaseServer = "3";
						
					}
					
					
					FacesContext context = FacesContext.getCurrentInstance();
					String initiator = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
					String ipAddress = InetAddress.getLocalHost().getHostAddress();
					
					if(currentResetLimit.length() > 0 && isInteger(currentResetLimit))
					{

						String message = model.logResetBoudLimitDetails(databaseServer,card_num,fistname,lastname,email,
								phone,street,cardExp,onlinebalance,cashwithdraw,boundwork,boundlimit,userhotlist,userblocked,pendingboundlimit,initiator,currentResetLimit);
						
						System.out.println("logResetBoudLimitDetails() does it get here  "+message);
						facesMessages.add(Severity.WARN, message);
						
					}
					else if(isInteger(resetLimit)==false)
					{
						facesMessages.add(Severity.WARN, "Figure is required ");
					}
					else
					{
						facesMessages.add(Severity.WARN, "Enter a reset Bound Limit ");
					}
					
					
					

				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
			
			
			
			public void LogExtendCard_ExpirationDetails()
			{
				
				
				try
				{
			
					CardManagementModel model = new CardManagementModel();
					String card_num = getEdit_id().trim();//card_num
					
					System.out.println("Card Number  "+card_num);
					
					
										
					String fistname = getFirstName();
					String lastname = getLastName();
					String email = getEmail();
					String phone =getPhone();
					String street = getStreet();
					String cardExp = getCardExpiration();
					String onlinebalance = getOnlinebalance();
					String cashwithdraw = getWithDrawLimit();
					String boundwork = getBoundworks();
					String boundlimit= getBoundLimit();
					String userhotlist = getUserHotlist();
					String userblocked = getUserBlock();
					
					
					String extendExpPending = "Queue";
					
					System.out.println("Card Expiration  "+cardExp);
					
					
					String databaseServer = getCmsServer();
					System.out.println("Database server    -- - "+databaseServer);
				
					
					if(databaseServer.equalsIgnoreCase("VirtualConsole"))
					{
						databaseServer = "1";
						
					}
					else if(databaseServer.equalsIgnoreCase("PocketMoni"))
					{
						databaseServer = "2";
						
					}
					else if(databaseServer.equalsIgnoreCase("Central"))
					{
						
						databaseServer = "3";
						
					}
					
					
					FacesContext context = FacesContext.getCurrentInstance();
					String initiator = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
					String ipAddress = InetAddress.getLocalHost().getHostAddress();

		
					String message = model.LogExtendCard_ExpirationDetails(databaseServer,card_num,fistname,lastname,email,
							phone,street,cardExp,onlinebalance,cashwithdraw,boundwork,boundlimit,userhotlist,userblocked,extendExpPending,initiator);
				
				  facesMessages.add(Severity.WARN, message);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
			
			
			
			public void reSet_Bound_Limit_Pending()
			{
				try
				{
			
					CardManagementModel model = new CardManagementModel();
					String card_num = getEdit_id().trim();//card_num		
					
					String boundLimit = getBoundLimit();
				
					
					System.out.println("BOUND LIMIT VALUES  "+boundLimit);
					

					FacesContext context = FacesContext.getCurrentInstance();
					String initiator = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
					String ipAddress = InetAddress.getLocalHost().getHostAddress();
					
					if(boundLimit.length() < 0 || boundLimit.equals("") || boundLimit.equals("null"))
					{
						facesMessages.add(Severity.WARN, "Bound Limit Requried");
					}
					else
					{
					String message = model.createPending_Reset_Bound_Limit(card_num, boundLimit,initiator);
					
					facesMessages.add(Severity.WARN, message);
					}
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
			
			
			public void blockCard_Auth_Pending()
			{
				try
				{
			
					CardManagementModel model = new CardManagementModel();
					String card_num = getEdit_id().trim();//card_num;
					
					String block = "1";
					System.out.println("blockCard_Auth_Pending "+card_num);
					String message = model.createPending_BlockCard(card_num, block);
				
					//System.out.println("message " + message);
					
					facesMessages.add(Severity.WARN, message);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
			
			public void unBlockCard_Auth_Pending()
			{
				try
				{
			
					CardManagementModel model = new CardManagementModel();
					String card_num = getEdit_id().trim();//card_num;
					
					String blockStatus = "0";

					String message = model.createPending_UNBlockCard(card_num, blockStatus);
				
					//System.out.println("message " + message);
					
					facesMessages.add(Severity.WARN, message);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
			
			
			
			public void blockCard()
			{
				try
				{
			
					CardManagementModel model = new CardManagementModel();
					String cardNum = getEdit_id().trim();//card_num;
					
					String message = model.blockCard(cardNum);
					
					System.out.println("message " + message);
					facesMessages.add(Severity.WARN, message);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
			
			
			public void createFungateSetup()
			{
				
				try
				{
						CardManagementModel model = new CardManagementModel();
						Cryptographer cry = new Cryptographer();
						Sequencer seq = new Sequencer();
						
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			            String beginDate = df.format(getStart_date());
			            Calendar cal = Calendar.getInstance();
			         
			            String createdDate = df.format(cal.getTime());
			            
			       
			     	    System.out.println("INSTANCE DATE AND TIME  "+df.format(cal.getTime()));
			     	    
			     	   System.out.println(" DATE AND TIME  "+createdDate);
			            
	
			            
			           
			            String merchantName = getMerchantName();
			            String cardnum = getCard_num();
			            String expiration = getCardExpiration();
			            String currency = getCurrency();
			            String accountType = getAcctType();
			            String allowCredit = getAllowCredit();
			            String chargeCardId = getChargeCatId();
			            String vtuCom = getVtuCom();
			            
			       
			           
			            String key = Cryptographer.byte2hex(cry.getNewKey(Cryptographer.LENGTH_DES3_2KEY).getEncoded());
			            String  terminalId = seq.getNext("07", 11);
			            HttpServletRequest str = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			            String ipAddress = str.getRemoteAddr();
			            
			            System.out.println("=================================================================================================================================================================================================================================================================================================");
			            System.out.println("Terminal ID "+terminalId+"IPADDRESS"+ipAddress+"merchant Name"+merchantName+" masterKey"+key+"card number"+cardnum+"Expiration"+expiration+"Currency"+currency+"Account Type"+accountType+"Created Date"+createdDate+"allow Credit"+allowCredit+"charge Card Id"+chargeCardId+"vtu Com"+vtuCom);
			            System.out.println("=================================================================================================================================================================================================================================================================================================");
			          
			            String mess = model.createFungateSetup(terminalId, ipAddress, merchantName, key, cardnum, expiration, currency, accountType, createdDate, allowCredit, chargeCardId, vtuCom);
			            
			            if(mess.equalsIgnoreCase("Exists"))
			            {
			            	mess = " Record Already Exists ";
			            }
			            else if(mess.equalsIgnoreCase("SUCCESS"))
			            {
			            	mess = " Record Successfully Created !";
			            	fundgateRecordList = model.getFungateRecords();
							System.out.println("fundgateRecordList  list "+fundgateRecordList.size());
			            	
			            }
			            else
			            {
			            	mess = " Error Creating Record !";
			            	
			            }
			            
			            facesMessages.add(Severity.INFO, mess);
			            
			            setMerchantName(null);
			            setCard_num(null); 
			            setCardExpiration(null);
			            setCurrency(null);
			            setChargeCatId(null);
			            setVtuCom(null);
			            
			            
						
			      
				}catch(Exception ex)
				{
						ex.printStackTrace();
				}
				
			}
			
			
			public void logFungateSetupDetails()
			{
				
				try
				{
						CardManagementModel model = new CardManagementModel();
						
						
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			            String beginDate = df.format(getStart_date());
			            Calendar cal = Calendar.getInstance();
			         
			            String createdDate = df.format(cal.getTime());
		 
			           
			            String merchantName = getMerchantName();
			            String cardnum = getCard_num();
			            String expiration = getCardExpiration();
			            String currency = getCurrency();
			            String accountType = getAcctType();
			            String allowCredit = getAllowCredit();
			            String chargeCardId = getChargeCatId();
			            String vtuCom = getVtuCom();
			            String terminalId = getEdit_id();
			            
			            String operation ="update";
			            HttpServletRequest str = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			            String remotIp = str.getRemoteAddr();
			            

						FacesContext context = FacesContext.getCurrentInstance();
						String initiator = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
						String localIp = InetAddress.getLocalHost().getHostAddress();
			           
			            
			           /* System.out.println("=================================================================================================================================================================================================================================================================================================");
			            System.out.println("Terminal ID "+terminalId+"IPADDRESS"+ipAddress+"merchant Name"+merchantName+" masterKey"+key+"card number"+cardnum+"Expiration"+expiration+"Currency"+currency+"Account Type"+accountType+"Created Date"+createdDate+"allow Credit"+allowCredit+"charge Card Id"+chargeCardId+"vtu Com"+vtuCom);
			            System.out.println("=================================================================================================================================================================================================================================================================================================");
			          */
			            String mess = model.createFungateLogDetails(terminalId, localIp, merchantName, cardnum, expiration, currency, accountType, createdDate, allowCredit, chargeCardId, vtuCom,initiator,operation);
			            
			            if(mess.equalsIgnoreCase("Exists"))
			            {
			            	mess = " Record Already Exists ";
			            }
			            else if(mess.equalsIgnoreCase("SUCCESS"))
			            {
			            	mess = " Records successfully queued for autorization !";
			            	setControlId("create");
			            }
			            else
			            {
			            	mess = " Unable to queue for authorization !";
			            	
			            }
			            
			            facesMessages.add(Severity.INFO, mess);
			            
			            setMerchantName(null);
			            setCard_num(null); 
			            setCardExpiration(null);
			            setCurrency(null);
			            setChargeCatId(null);
			            setVtuCom(null);
			            
			            
						
			      
				}catch(Exception ex)
				{
						ex.printStackTrace();
				}
				
			}
			
			
			public void getFundGateReport()
			{
				
				
				try
				{
					CardManagementModel model = new CardManagementModel();
					
					String terminal = getTerminalID();
					
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			        
					String beginDate = df.format(getStart_date());
			        beginDate = beginDate + " " + getStart_hr() + ":00";
			        
			        String endDate = df.format(getEnd_date());
			        endDate = endDate + " " + getEnd_hr() + ":59";
			        
			        double t = 0.0;
			        E_TRANSACTION trans = null;
			        
			        fundgateSummary = model.getFundgateReport(terminal, beginDate, endDate);
			        System.out.println("  fundgateSummary size "+fundgateSummary.size());
			        for(int i=0;i<fundgateSummary.size(); i++)
			        {
			        	trans = (E_TRANSACTION)fundgateSummary.get(i);
			        	t +=Double.parseDouble(trans.getTrans_amount());
			        	
			        }
			        setTotal_amount(t);
			 		
					
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
				
				
				
			}
			
			public String getAuthorizeFundGateUpdateDetails()
			{
				String message= "";
				try
				{
					
					CardManagementModel model = new CardManagementModel();
					
					String merchantName = getMerchantName();
					String cardnum = getCard_num();
					String expiration = getCardExpiration();
					String creationDate = getCreatedDat();
					String alowCredit = getAllowCredit();
					String currency = getCurrency();
					String chargCatId = getChargeCatId();
					String vtucom = getVtuCom();
					String terminalId = getTerminalId();
				
					message = model.updateFungateSetup(terminalId,merchantName,cardnum,expiration,currency,creationDate,alowCredit,chargCatId,vtucom);
					if(message.equalsIgnoreCase("SUCCESS"))
					{
					
						message = "Records has been successfully authorize";
						setControlId("create");
					}
					else
					{
						message = " Unable to authorize Records ";
						
					}
					
					  facesMessages.add(Severity.INFO, message);
			            
			           setMerchantName(null);
			           setCard_num(null); 
			           setCardExpiration(null);
			           setCurrency(null);
			           setChargeCatId(null);
			           setVtuCom(null);
					
					
					
					
				}catch(Exception ex)
				{
					ex.printStackTrace();
					
				}
				
				return message;
				
			}
			
			public void setToEdit()
			{
				try
				{
					CardManagementModel model = new CardManagementModel();
					
					System.out.println("SET TO EDIT VALUES . . .. "+edit_id);
					ArrayList a  = model.getFungateRecords(edit_id);
					
					if(a.size()>0)
					{
						FundGateInfo u = (FundGateInfo)a.get(0);
						setMerchantName(u.getMerchantName());
						setCard_num(u.getCardnum().trim());
						setCardExpiration(u.getExpiration().trim());
					    setCurrency(u.getCurrency().trim());
						setAcctType(u.getAccountType().trim());
					    setCreatedDat(u.getCreateDat().trim());
						setAllowCredit(u.getAllowCredit().trim());
						setChargeCatId(u.getChargeCardId().trim());
						setVtuCom(u.getVtuCom().trim());
						setControlId("edit");
					}
					
					
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
			
			
			
			

}
