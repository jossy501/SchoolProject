/**
 * 
 */
package com.etranzact.supportmanager.action;

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

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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

//import sun.text.SupplementaryCharacterData;

import com.etranzact.cms.dto.FundGateInfo;
import com.etranzact.cms.model.CardManagementModel;
import com.etranzact.drs.controller.ExcelWriter;
import com.etranzact.institution.model.InstitutionModel;
import com.etranzact.institution.util.Sequencer;
import com.etranzact.supportmanager.dto.AccountInfo;
import com.etranzact.supportmanager.dto.COP_FUNDGATE_LOG;
import com.etranzact.supportmanager.dto.C_MTNRequestLogger;
import com.etranzact.supportmanager.dto.C_TRANSACTION;
import com.etranzact.supportmanager.dto.CardHolder;
import com.etranzact.supportmanager.dto.Channel;
import com.etranzact.supportmanager.dto.Company;
import com.etranzact.supportmanager.dto.E_CARDSERVICE;
import com.etranzact.supportmanager.dto.E_EXCEPTION;
import com.etranzact.supportmanager.dto.E_IPAYMENTTRAN;
import com.etranzact.supportmanager.dto.E_MERCHANT;
import com.etranzact.supportmanager.dto.E_MERCHANT_SPLIT;
import com.etranzact.supportmanager.dto.E_MOBILE_SUBSCRIBER;
import com.etranzact.supportmanager.dto.E_MOBILE_SUBSCRIBER_CARD;
import com.etranzact.supportmanager.dto.E_SETTLEMENTDOWNLOAD_BK;
import com.etranzact.supportmanager.dto.E_STANDARD_SPLIT;
import com.etranzact.supportmanager.dto.E_TMCEVENT;
import com.etranzact.supportmanager.dto.E_TMCNODE;
import com.etranzact.supportmanager.dto.E_TMCREQUEST;
import com.etranzact.supportmanager.dto.E_TRANSACTION;
import com.etranzact.supportmanager.dto.E_TRANSCODE;
import com.etranzact.supportmanager.dto.FundPocketMoniLog;
import com.etranzact.supportmanager.dto.LOTTO_LOG;
import com.etranzact.supportmanager.dto.PAYTRANS;
import com.etranzact.supportmanager.dto.PoolAccount;
import com.etranzact.supportmanager.dto.ProviderLog;
import com.etranzact.supportmanager.dto.REQUEST_LOG;
import com.etranzact.supportmanager.dto.SMS_LOG;
import com.etranzact.supportmanager.dto.Summary;
import com.etranzact.supportmanager.dto.T_SMS_RECEIVE;
import com.etranzact.supportmanager.dto.T_SMS_SEND;
import com.etranzact.supportmanager.dto.User;
import com.etranzact.supportmanager.dto.VTU_LOG;
import com.etranzact.supportmanager.model.AdminModel;
import com.etranzact.supportmanager.model.ReportModel;
import com.etranzact.supportmanager.utility.HashNumber;
import com.etranzact.supportmanager.utility.HttpMessenger;
import com.etz.http.etc.Card;
import com.etz.http.etc.HttpHost;
import com.etz.http.etc.TransCode;
import com.etz.http.etc.XProcessor;
import com.etz.http.etc.XRequest;
import com.etz.http.etc.XResponse;
import com.etz.mobile.security.CardAudit;
import com.etz.security.util.Cryptographer;
import com.etz.security.util.PBEncryptor;

//import org.richfaces.event.FileUploadEvent;
//import org.richfaces.model.UploadedFile;





/**
 * @author Joshua.Aruno
 *
 */
@Restrict("#{authenticator.curUser.loggedIn}")
@Scope(ScopeType.SESSION)
@Name("reportAction")
public class ReportAction implements Serializable
{
	//commission breakdown
	private ArrayList<E_TRANSACTION> merchantSummaryLog = new ArrayList<E_TRANSACTION>();
	private ArrayList merchantSplitLog = new ArrayList();
	
	//general merchant report
	private ArrayList merchantReportLog = new ArrayList();
	private ArrayList merchantSummaryReportLog = new ArrayList();
	private List branchList = new ArrayList();
	
	private List marchantlsit = new ArrayList();
	
	//settled option
	private ArrayList settle_batch = new ArrayList();
	private ArrayList schemeLists = new ArrayList();
	
	private ArrayList companyList = new ArrayList();
	
	private ArrayList poolaccountList = new ArrayList();
	
	private ArrayList accountInfoList = new ArrayList();
	
	private ArrayList transSummaryList = new ArrayList();
	private ArrayList tranSummaryList = new ArrayList();
	
	private ArrayList transPoolList = new ArrayList();
	private ArrayList transPool = new ArrayList();
	private ArrayList bankAccountList = new ArrayList();
	private ArrayList masterBankList = new ArrayList();
	private ArrayList customerBankList = new ArrayList();
	
	private ArrayList reversalList = new ArrayList();
	
	private ArrayList<File> files = new ArrayList<File>();
	
    private ArrayList uploadList = new ArrayList();
    
    private ArrayList ipaymentMerchant = new ArrayList();
    
  
	
	
	private String subscriber_id ;
	private String branchCode;
	private String accountName;
	
    private String schemeId;
	private String schemeName;
	private String schemeNarration;
	private String settlementBank;
	private String companyId;
	private String pin;
	private String companyname;
	private String transDate;
	private String esa;
	private String transAmount;
	private String vsmFullName;
	private String month;
	private String year;
	private String companyBankAccount;
	private String companyBank;
    private double onlineBalance;
    
    private String title;
    private String description;
    private String size;
    private String author;
    private String filePath;
    private String fileName;
    private String firstname;
    private String lastname;
    private String staffNo;
    
    
    
    

	public List getMarchantlsit() {
		return marchantlsit;
	}


	public void setMarchantlsit(List marchantlsit) {
		this.marchantlsit = marchantlsit;
	}


	
	public ArrayList getIpaymentMerchant() {
		getIPaymentMerchantName();
		return ipaymentMerchant;
	}


	public void setIpaymentMerchant(ArrayList ipaymentMerchant) {
		this.ipaymentMerchant = ipaymentMerchant;
	}


	public String getAccountName() {
		return accountName;
	}


	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}


	public String getBranchCode() {
		return branchCode;
	}


	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}


	public List getBranchList() 
	{
		return branchList;
	}


	public void setBranchList(List branchList) {
		this.branchList = branchList;
	}


	public String getSubscriber_id() {
		return subscriber_id;
	}


	public void setSubscriber_id(String subscriber_id) {
		this.subscriber_id = subscriber_id;
	}
	

	public ArrayList getSchemeLists() {
		return schemeLists;
	}


	public void setSchemeLists(ArrayList schemeLists) {
		this.schemeLists = schemeLists;
	}

	

	public String getPin() {
		return pin;
	}


	public void setPin(String pin) {
		this.pin = pin;
	}

	


	public String getCompanyname() {
		return companyname;
	}


	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}




	public String getTransDate() {
		return transDate;
	}


	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}



	public String getEsa() {
		return esa;
	}


	public void setEsa(String esa) {
		this.esa = esa;
	}



	public String getTransAmount() {
		return transAmount;
	}


	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}



	public String getVsmFullName() {
		return vsmFullName;
	}


	public void setVsmFullName(String vsmFullName) {
		this.vsmFullName = vsmFullName;
	}




	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getSize() {
		return size;
	}


	public void setSize(String size) {
		this.size = size;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}

	
    



	public String getFilePath() {
		return filePath;
	}


	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	



	public String getFirstname() {
		return firstname;
	}


	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	public String getLastname() {
		return lastname;
	}


	public void setLastname(String lastname) {
		this.lastname = lastname;
	}






	//merchant search
	private ArrayList<E_MERCHANT> merchantSearchLog = new ArrayList<E_MERCHANT>();
	
	
	//revenue head report
	private ArrayList<E_TRANSACTION> revenueReportLog = new ArrayList<E_TRANSACTION>();
	
	//all option
	private ArrayList<E_TRANSACTION> allMerchantBankReportLog = new ArrayList<E_TRANSACTION>();
	private ArrayList<E_TRANSACTION> allMerchantTransReportLog = new ArrayList<E_TRANSACTION>();
	private ArrayList<E_TRANSACTION> allMerchantChannelReportLog = new ArrayList<E_TRANSACTION>();
	private ArrayList<E_TRANSACTION> allMerchantReportLog = new ArrayList<E_TRANSACTION>();
	private ArrayList<E_TRANSACTION> allMerchantAccountReportLog = new ArrayList<E_TRANSACTION>();
	
	//get standard split formula
	private ArrayList<E_STANDARD_SPLIT> standardSplitFormula = new ArrayList<E_STANDARD_SPLIT>();
	private ArrayList<E_MERCHANT_SPLIT> merchantSplitFormula = new ArrayList<E_MERCHANT_SPLIT>();
	private ArrayList<Summary> successfulSummaryLog = new ArrayList();
	private ArrayList mmoneyLog = new ArrayList();
	private ArrayList mmoneySummaryLog = new ArrayList();
	
	private ArrayList districtByZone = new ArrayList();
	private ArrayList switchReportLog = new ArrayList();
	
	
	//newly added
	
	private ArrayList supportLog = new ArrayList();
	private ArrayList<T_SMS_RECEIVE> smsReceiveLog = new ArrayList();
	private ArrayList<SMS_LOG> smsLog = new ArrayList();
	private ArrayList<T_SMS_SEND> smsSendLog = new ArrayList();
	
	
	private ArrayList<VTU_LOG> vtuLog = new ArrayList();
	private ArrayList<PAYTRANS> billLog = new ArrayList();
	private ArrayList<PAYTRANS> billLog1 = new ArrayList();
	public ArrayList<PAYTRANS> getBillLog1() {
		return billLog1;
	}


	public void setBillLog1(ArrayList<PAYTRANS> billLog1) {
		this.billLog1 = billLog1;
	}


	private ArrayList<REQUEST_LOG> failedFundsLog = new ArrayList();
	private ArrayList<E_TRANSACTION> tranLog = new ArrayList();
	private ArrayList<E_MOBILE_SUBSCRIBER> mobileSubscriberLog = new ArrayList();
	private ArrayList<E_MOBILE_SUBSCRIBER_CARD> mobileSubscriberCardLog = new ArrayList();
	
	private ArrayList<Summary> summaryLog = new ArrayList();
	private ArrayList<Summary> failedSummaryLog = new ArrayList();
	private ArrayList failedSummaryDrillDownLog = new ArrayList();
	
	private ArrayList<Channel> channelList = new ArrayList();
	private ArrayList<E_TRANSCODE> transcodeList = new ArrayList<E_TRANSCODE>();
	
	
	private ArrayList<E_TMCNODE> tmcNodeList = new ArrayList<E_TMCNODE>();
	private ArrayList<E_TMCEVENT> tmcEventLog = new ArrayList<E_TMCEVENT>();
	private ArrayList<E_TMCREQUEST> tmcRequestLog = new ArrayList<E_TMCREQUEST>();
	
	private ArrayList<C_TRANSACTION> pinLog = new ArrayList<C_TRANSACTION>();
	
	
	//e_exception
	private ArrayList<E_EXCEPTION> exceptionLog = new ArrayList<E_EXCEPTION>();
	
	//e_cardservice
	private ArrayList<E_CARDSERVICE> cardserviceLog = new ArrayList<E_CARDSERVICE>();
	
	//card holder transaction by card num
	private ArrayList cardholdertranLog = new ArrayList();
	
	//card holder transaction by merchant_code
	private ArrayList cardholdertranByMerchantCodeLog = new ArrayList();
	
	private ArrayList<E_TRANSACTION> fundsTransfer = new ArrayList();
	private ArrayList pendingCardDeletionLog = new ArrayList();
	private ArrayList merchantExtSplitLog = new ArrayList();
	
	private List bankList = new ArrayList();
	private List appList = new ArrayList();
	private List districtList = new ArrayList();
	private List bizDevList = new ArrayList();
	private List walletLog = new ArrayList();
	private List schemeRegList = new ArrayList();	
	private T_SMS_RECEIVE sms_receive;
	private User curUser;
	private PAYTRANS payTrans;
	private Company companyOb;//company object
	private PoolAccount poolAccountObj; // poolaccount object
	private AccountInfo accountInfo; // accountInfo object

	private String from_source;
	private String to_dest;
	private String status_id;
	private String terminal_id = "";
	private String depotId;	
	@RequestParameter
	private String id;//used in passing values from a jsf to a jsf
	
	
	@RequestParameter
	private String id1;//used in passing values from a jsf to a jsf
	
	
	@RequestParameter
	private String id2;//used in passing values from a jsf to a jsf
	
	@RequestParameter
	private String id3;//used in passing values from a jsf to a jsf
	
	@RequestParameter
	private String id4;//used in passing values from a jsf to a jsf
	
	@RequestParameter
	private String id5;//used in passing values from a jsf to a jsf
	
	
	
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
	
	private String accountNo;
	private String customerId;
	private String customerMisdn;
	private String transactionId;
	private String partNo;
	private String chequeBank;
	private String chequeDate;
	
	
	
	public ArrayList getDistrictByZone() {
		getPHCNALLDISTRICTS();
		return districtByZone;
	}


	public void setDistrictByZone(ArrayList districtByZone) {
		
		this.districtByZone = districtByZone;
	}


	public String getTrans_count() {
		return trans_count;
	}


	public void setTrans_count(String trans_count) {
		this.trans_count = trans_count;
	}


	public ArrayList<Summary> getSuccessfulSummaryLog() {
		return successfulSummaryLog;
	}


	public void setSuccessfulSummaryLog(ArrayList<Summary> successfulSummaryLog) {
		this.successfulSummaryLog = successfulSummaryLog;
	}

	
	
	public ArrayList getTransSummaryList() {
		return transSummaryList;
	}


	public void setTransSummaryList(ArrayList transSummaryList) {
		this.transSummaryList = transSummaryList;
	}

	
	
	public ArrayList getTransPoolList() {
		
		 return transPoolList;
	}


	public void setTransPoolList(ArrayList transPoolList) {
		this.transPoolList = transPoolList;
	}


	public ArrayList getTransPool() {
		return transPool;
	}


	public void setTransPool(ArrayList transPool) {
		this.transPool = transPool;
	}


	public List getSchemeRegList() {
		getExistedSchemelist();
		return schemeRegList;
	}


	public void setSchemeRegList(List schemeRegList) {
		this.schemeRegList = schemeRegList;
	}

	
	

	public ArrayList getCompanyList() 
	{
		ReportModel reportModel = new ReportModel();
		companyList = reportModel.getCompanyName();
		//getCompany();
		return companyList;
	}


	public void setCompanyList(ArrayList companyList) {
		this.companyList = companyList;
	}

	
	

	public ArrayList getPoolaccountList() {
		
		ReportModel reportModel = new ReportModel();
		poolaccountList = reportModel.getPoolAccount();
		return poolaccountList;
	}


	public void setPoolaccountList(ArrayList poolaccountList) {
		this.poolaccountList = poolaccountList;
	}


	public List getWalletLog() {
		return walletLog;
	}


	public void setWalletLog(List walletLog) {
		this.walletLog = walletLog;
	}
    

	public String getCompanyId() {
		return companyId;
	}


	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	

	
	public String getDepotId() {
		return depotId;
	}


	public void setDepotId(String depotId) {
		this.depotId = depotId;
	}


	

	
	
	
	
	

	public String getAccountNo() {
		return accountNo;
	}


	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}


	public String getCustomerId() {
		return customerId;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public String getCustomerMisdn() {
		return customerMisdn;
	}


	public void setCustomerMisdn(String customerMisdn) {
		this.customerMisdn = customerMisdn;
	}


	public String getTransactionId() {
		return transactionId;
	}


	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}


	public String getPartNo() {
		return partNo;
	}


	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}


	public String getChequeBank() {
		return chequeBank;
	}


	public void setChequeBank(String chequeBank) {
		this.chequeBank = chequeBank;
	}


	public String getChequeDate() {
		return chequeDate;
	}


	public void setChequeDate(String chequeDate) {
		this.chequeDate = chequeDate;
	}


	public FileUploadBean getCardfileUploadBean() {
		return cardfileUploadBean;
	}


	public void setCardfileUploadBean(FileUploadBean cardfileUploadBean) {
		this.cardfileUploadBean = cardfileUploadBean;
	}


	public ArrayList getReversalList() {
		getQueuedReversal();
		return reversalList;
	}


	public void setReversalList(ArrayList reversalList) {
		this.reversalList = reversalList;
	}


	public void getQueuedReversal()
	{
		ReportModel model = new ReportModel();
		reversalList =  model.getQueuedReveralDetails();
		System.out.println("size "+reversalList.size());
		
	}
	

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
    private String mtiNumber;
    
    private int totalTranCount = 0;
    private double totalTranSum = 0.0d;
    private int totalSettleCount = 0;
    private double totalSettleSum = 0.0d;
    private int totalFeeCount = 0;
    private double totalFeeSum = 0.0d;
	
	@In
    FacesMessages facesMessages;
	
	@Logger
	private Log log;

	@In(create = true)
	FileUploadBean cardfileUploadBean;
	
	FacesContext context;
	
	
	public String getTarrif_type() {
		return tarrif_type;
	}


	public void setTarrif_type(String tarrif_type) {
		this.tarrif_type = tarrif_type;
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


	public String getId1() {
		return id1;
	}


	public void setId1(String id1) {
		this.id1 = id1;
	}

	
	

	public String getId2() {
		return id2;
	}


	public void setId2(String id2) {
		this.id2 = id2;
	}

	

	public String getId3() {
		return id3;
	}


	public void setId3(String id3) {
		this.id3 = id3;
	}

	

	public String getId4() {
		return id4;
	}


	public void setId4(String id4) {
		this.id4 = id4;
	}


	public String getId5() {
		return id5;
	}


	public void setId5(String id5) {
		this.id5 = id5;
	}


	public ArrayList getTranSummaryList() {
		return tranSummaryList;
	}


	public void setTranSummaryList(ArrayList tranSummaryList) {
		this.tranSummaryList = tranSummaryList;
	}

	

	public ArrayList getUploadList() {
		
		ReportModel model = new ReportModel();
		uploadList = model.getCreatedFileUploaderList();
		return uploadList;
	}


	public void setUploadList(ArrayList uploadList) {
		this.uploadList = uploadList;
	}


	public ArrayList getMasterBankList() {
		
		 ReportModel model = new ReportModel();
		
		 FacesContext context = FacesContext.getCurrentInstance();
		 String bankcode = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
		 masterBankList = model.geteMasterBankRecords(bankcode);
		
		return masterBankList;
	}


	public void setMasterBankList(ArrayList masterBankList) {
		this.masterBankList = masterBankList;
	}
	
	


	public ArrayList getCustomerBankList() {
		
		ReportModel model = new ReportModel();
		
		 FacesContext context = FacesContext.getCurrentInstance();
		 String bankcode = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
		 
		 customerBankList = model.geteCustomerBankStaff(bankcode);
		 return customerBankList;
	}


	public void setCustomerBankList(ArrayList customerBankList) {
		this.customerBankList = customerBankList;
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

	
	public String getStaffNo() {
		
	/*    ReportModel model = new ReportModel();
	    String fname = getFirstname();
	    System.out.println("Firstname Records - --   > "+fname);
	    staffNo = model.getMasterBankRecordByFirstName("Fumi");
	    setStaffNo(staffNo);*/
	  
		return staffNo;
	}


	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}


	/*Created Card Service*/
	public void createCardService()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			
			
			String cno = getCard_num().trim();
			String mno = getMobileno().trim();
			
			String message = reportModel.createE_CardService(cno, mno);
			if(message.equals("Records successfully inserted"))
			{
				setCard_num(null);
				setMobileno(null);
			}
			System.out.println(message + " message");
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void createCompanySetup()
	{
		
		try
		{
			ReportModel model = new ReportModel();
			
			String companyName = companyOb.getCompanyName();
			String compCode = companyOb.getCompanyCode();
			String bankToCredit = companyOb.getBank();
			String bankAcct = companyOb.getBankAcct();
			
			String mess = model.createCompanySetup(companyName,compCode, bankAcct, bankToCredit);
		
			if(mess.equalsIgnoreCase("Records successfully inserted"))
			{
				setCompanyOb(null);
			}
				
			facesMessages.add(Severity.INFO, mess);
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	
	/*This method is to setup pool account for vsm validation check*/
	public void createPoolAccount()
	{
		
		try
		{
			ReportModel model = new ReportModel();
			
			String companyId = poolAccountObj.getCompanyId();
			String mobile  = poolAccountObj.getMobile();
			String status = poolAccountObj.getActiveStatus();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");

            String beginDate = df.format(getStart_date());
            Calendar cal = Calendar.getInstance();
         
            String createdDate = df.format(cal.getTime());

			String mess = model.createPoolAccountSetup(companyId, mobile, status, createdDate);
		
			if(mess.equalsIgnoreCase("Records successfully inserted"))
			{
				
				setPoolAccountObj(null);
			}
				
			facesMessages.add(Severity.INFO, mess);
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	
	
	/*This method is to setup  account Info */
	public void createAccountInfo()
	{
		
		try
		{
			ReportModel model = new ReportModel();
			
			
			String companyId = accountInfo.getCompanyId();
			String bankCode  = accountInfo.getBankCode();
			String accountNo = accountInfo.getAccountNo();
			String accountDesc = accountInfo.getAccountDesc();
			

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");

            String beginDate = df.format(getStart_date());
            Calendar cal = Calendar.getInstance();
         
            String createdDate = df.format(cal.getTime());

			String mess = model.createAccountInfoSetup(companyId, bankCode, accountNo,accountDesc, createdDate);
		
			if(mess.equalsIgnoreCase("Records successfully inserted"))
			{
				
				setAccountInfo(null);
			}
				
			facesMessages.add(Severity.INFO, mess);
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	
	
	
	/*public void createScheme()
	{
		
		ReportModel cmmodel = new ReportModel();
		
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
						
					 
						String beginDate = df.format(getStart_date());
						
				        beginDate = beginDate + " " + getStart_hr() + ":00";
				       
				        String endDate = df.format(getEnd_date());
				        endDate = endDate + " " + getEnd_hr() + ":59";
				  
				        System.out.println("created date  "+beginDate);
				 
				
					String mess = cmmodel.createE_Scheme(Scheme, shemenOwner, sttlementBank, narration,beginDate);
			 
					if(mess.equalsIgnoreCase("EXISTED"))
					{
						mess = "Records Already Exists";
					}
					else if(mess.equalsIgnoreCase("SUCCESS"))
					{
						mess = "Records successfully inserted";
						
						schemeRegList = cmmodel.getScheme_Registration();
						System.out.println("schemeList "+schemeRegList.size());
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
	/*Created new commission split*/
	
	public void createMerchantExtSplit()
	{
		try
		{
			ReportModel reportModel = new ReportModel();

			String merchantCode = getMerchant_code().trim();
			String value = getStrParam().trim();
			try
			{
				double d = Double.parseDouble(value);
			}catch(Exception ex)
			{
				facesMessages.add(Severity.ERROR, "Commission Amount must be numeric");
        		return;
			}
			
			
			String message = reportModel.createMerchantExtSplit(merchantCode, value);
			if(message.equals("Records successfully inserted"))
			{
				setMerchant_code(null);
				setStrParam(null);
			}
			System.out.println(message + " message");
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public String getMtiNumber() {
		return mtiNumber;
	}


	public void setMtiNumber(String mtiNumber) {
		this.mtiNumber = mtiNumber;
	}
	/*successful Summary*/
	public void successfulSummary()
	{
		
		ReportModel reportModel = new ReportModel();
		String channel = getId().trim();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String beginDate = df.format(getStart_date());
        beginDate = beginDate + " " + getStart_hr() + ":00";
        
        String endDate = df.format(getEnd_date());
        endDate = endDate + " " + getEnd_hr() + ":59";
    
        setChannel_id(channel);
        System.out.println(" Channel ID  >>>>> ---- "+channel+" ID" +getId().trim());
        successfulSummaryLog = reportModel.getSuccessfulSummary(getId(), beginDate, endDate);
        
	}
	

	/*
	 * Method to get VAS Transaction done on the ATM
	 */
	
	public void getvasTransactionAtm()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			mmoneyLog.clear();

			//String card_number = getCard_num().trim();
			// Mti default value =  "0220"
			String mtinumber = getMtiNumber().trim();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
	        FacesContext context = FacesContext.getCurrentInstance();
			String scheme_card = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
	//		System.out.println("Mti no : "+mtinumber+"begin date "+beginDate+"  end date "+endDate);
			if(mtinumber.trim().length() <= 0)
	        {
	        	facesMessages.add(Severity.WARN, "MTI number doesnt exist.");	
	        }else{
	        	mmoneyLog = reportModel.getVasTransactionAtmDB(mtinumber,beginDate,endDate);
				System.out.println("getVasTransactionAtmDB get Vas Transaction on Atm  Log"+mmoneyLog.size());
				
	        }
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*This is the method to view card service*/
	public void viewCardService()
	{
		try
		{
			cardserviceLog.clear();
			ReportModel reportModel = new ReportModel();			
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
			
            cardserviceLog = reportModel.getE_CardService(getMerchant_code(), beginDate, endDate);
			System.out.println("supportLog " + supportLog.size());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public void getAbiaLottoTransactions()
	{
		try
		{
			LOTTO_LOG tran = null;
	        double d = 0.0;
			ReportModel reportModel = new ReportModel();
			cardholdertranLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
			
	        String lotto_no = getCard_num().trim();
	        String phone_no = getMerchant_code().trim();
	        String terminal_id = getTerminal_id().trim();
	        
	        
	        
			
        	cardholdertranLog = reportModel.getAbiaLottoTransactions(lotto_no, phone_no, terminal_id, beginDate, endDate);
	        for(int i=0;i<cardholdertranLog.size();i++)
	        {
	        	tran = (LOTTO_LOG)cardholdertranLog.get(i);
	        	d += Double.parseDouble(tran.getTrans_amount());	        	
	        }
	        setTotal_amount(d);
	        
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*This is the method for the investigation searches*/
	public void viewSupportLog()
	{
		try
		{
			supportLog.clear();
			ReportModel reportModel = new ReportModel();
			
			if(getLine_type().equals("Version I"))
			{
				if(getStatus_id().equals("ONLY SMS LOG") && getCard_num().trim().length()<=0)
				{
					facesMessages.add(Severity.WARN, "Mobile number requested");
				}
				else
				{
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		            String beginDate = df.format(getStart_date());
		            beginDate = beginDate + " " + getStart_hr() + ":00";
		            
		            String endDate = df.format(getEnd_date());
		            endDate = endDate + " " + getEnd_hr() + ":59";
					String rowcount = "0";
					
					
					FacesContext context = FacesContext.getCurrentInstance();
					String user_code = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
					
					String merchantId = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
					if(merchantId==null)
					{
						merchantId = "";
					}
					
					supportLog = reportModel.getSupportLog(getChannel_id(), getCard_num(), beginDate, endDate,
							rowcount, getStatus_id(), getMerchant_code(), user_code, getTerminal_id(),merchantId);

				}
			}
			else if(getLine_type().equals("Version II"))
			{
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	            String beginDate = df.format(getStart_date());
	            beginDate = beginDate + " " + getStart_hr() + ":00";
	            
	            String endDate = df.format(getEnd_date());
	            endDate = endDate + " " + getEnd_hr() + ":59";
				
				supportLog = reportModel.getPocketMoniIncomingLog(getCard_num(), beginDate, endDate);
			}
			
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*
	 * Summary Log for successful Transaction
	 */
	public void viewSuccessfulSummaryReport()
	{
		
		try
		{
			
			summaryLog.clear();
			Summary summary = null;
			double fmtamount =0.0;
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
			
            summaryLog = reportModel.getLogSuccessfulSummary(beginDate , endDate);
        	for(int i=0;i<summaryLog.size();i++)
			{
        		summary = (Summary)summaryLog.get(i);
        		fmtamount += Double.parseDouble(summary.getSummaryamount());	        	
			}
			setTotal_amount(fmtamount);
			//summary.setSummaryamount(Double.parseDouble(fmtamount));
			
            
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public void viewDailyTransactionReport()
	{
		try
		{
			supportLog.clear();
			ReportModel reportModel = new ReportModel();
			
		
			
			
					
					String card_num = getCard_num();
					if(card_num.trim().length() > 0)
					{

						facesMessages.add(Severity.WARN, "Card number is require");
					}
					else{
						
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						
			            String beginDate = df.format(getStart_date());
			            beginDate = beginDate + " " + getStart_hr() + ":00";
			            
			            String endDate = df.format(getEnd_date());
			            endDate = endDate + " " + getEnd_hr() + ":59";
						String rowcount = "0";
						String status = getStatus_id();
						System.out.println("Status >>>>>>> "+status + " Card number  >>"+ card_num);
						
						
						FacesContext context = FacesContext.getCurrentInstance();
						String user_code = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
						
						supportLog = reportModel.getSupportLog(getChannel_id(), getCard_num(), beginDate, endDate,
								rowcount, getStatus_id(), getMerchant_code(), user_code, getTerminal_id(),"");
						
					}
			
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*This is used for the new card scheme implementation*/
	public void viewSupportSchemeLog()
	{
		try
		{
			supportLog.clear();
			ReportModel reportModel = new ReportModel();
			
			if(getLine_type().equals("Version I"))
			{
				if(getStatus_id().equals("ONLY SMS LOG") && getCard_num().trim().length()<=0)
				{
					facesMessages.add(Severity.WARN, "Mobile number requested");
				}
				else
				{
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		            String beginDate = df.format(getStart_date());
		            beginDate = beginDate + " " + getStart_hr() + ":00";
		            
		            String endDate = df.format(getEnd_date());
		            endDate = endDate + " " + getEnd_hr() + ":59";
					String rowcount = "0";
					
					
					FacesContext context = FacesContext.getCurrentInstance();
					String user_code = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
					String card_scheme = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
					
					String databaseServer[] = card_scheme.split(":");
					card_scheme = databaseServer[0];
					
					supportLog = reportModel.getSupportSchemeLog(getChannel_id(), getCard_num(), beginDate, endDate,
							rowcount, getStatus_id(), getMerchant_code(), user_code, getTerminal_id(),
							card_scheme, databaseServer[1]);

					System.out.println("log " + supportLog.size());
					
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*This is the method for the pos report, pos bridge searches*/
	public void viewPosReportLog()
	{
		try
		{
			supportLog.clear();
			ReportModel reportModel = new ReportModel();
			
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
			String rowcount = "0";
			
			FacesContext context = FacesContext.getCurrentInstance();
			String user_code = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
			
			supportLog = reportModel.getPosReportLog(getChannel_id(), getCard_num(), beginDate, endDate,
					rowcount, getStatus_id(), getMerchant_code(), user_code, getTerminal_id());	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*This is the method for the POS Transaction searches*/
	public void getPOSTransactions()
	{
		try
		{
			supportLog.clear();
			ReportModel reportModel = new ReportModel();			
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
			
			supportLog = reportModel.getPOSTransactions(getChannel_id(), beginDate, endDate);
			//System.out.println("supportLog " + supportLog.size());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*VTU Investigation*/
	public void viewVTULog()
	{
		try
		{
			vtuLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
            
            String status = getStatus_id().trim();
            
			String table_location = getOptionType().trim();
            
			String orig_msisdn = getTerminal_id().trim();
			String uniquetransid = getTrans_code().trim();
			String version = getLine_type().trim();
			
			
			vtuLog = reportModel.getVtuLog(getFrom_source(), getTo_dest(), beginDate , endDate, status,
					table_location, orig_msisdn, version, uniquetransid);

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*Bill Investigation*/
	public void viewBillLog()
	{
		try
		{
			//String strParam = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("firstParameter");
			
			billLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
            
            String table_location = getOptionType().trim();
            
            
			
            billLog = reportModel.getPayTrans(getFrom_source(),beginDate , endDate,
            		getMerchant_code().trim(), table_location);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
/* drill down bank branch transaction Report */
	
	public void viewDrillDownBranchTransactionReportByBank()
	{
		try
		{
			cardholdertranLog.clear();
			ReportModel reportModel = new ReportModel();
			
			String subCode = getId();
			String issuerCode = getId1();
	
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			
			 String beginDate = df.format(getStart_date());
	         beginDate = beginDate + " " + getStart_hr() + ":00";
	            
	         String endDate = df.format(getEnd_date());
	         endDate = endDate + " " + getEnd_hr() + ":59";
  
            System.out.println("Sub code "+subCode+"ISSUER CODE "+issuerCode+"Start Date "+beginDate+"End Date "+endDate);
            
            
            System.out.println("my Begin Date  ---  "+beginDate+"my END DATE  "+endDate);
			
            double t = 0.0;
            C_TRANSACTION ctrans = null;
      
            cardholdertranLog = reportModel.getDrillDownBranchTransactionReportByBank(subCode,issuerCode,beginDate, endDate);
        	for(int i=0;i<cardholdertranLog.size();i++)
         	{
        		ctrans = (C_TRANSACTION)cardholdertranLog.get(i);
         		t += Double.parseDouble(ctrans.getTrans_amount());
         	}
         	setTotal_amount(t);
        	System.out.println("failedFundsLog " + cardholdertranLog.size());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	

	public void viewDrillDownCollectingBankTransactionReportByBranch()
	{
		try
		{
			cardholdertranLog.clear();
			ReportModel reportModel = new ReportModel();
			
			String subCode = getId();
			//String issuerCode = getId1();
	
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			
			 String beginDate = df.format(getStart_date());
	         beginDate = beginDate + " " + getStart_hr() + ":00";
	            
	         String endDate = df.format(getEnd_date());
	         endDate = endDate + " " + getEnd_hr() + ":59";
  
          
            
            System.out.println("my Begin Date  ---  "+beginDate+"my END dATE  "+endDate);
			
            double t = 0.0;
            C_TRANSACTION ctrans = null;
            
            FacesContext context = FacesContext.getCurrentInstance();
        	String issuerCode = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
        	       
        	System.out.println("Sub code "+subCode+"ISSUER CODE "+issuerCode+"Start Date "+beginDate+"End Date "+endDate);
              
            cardholdertranLog = reportModel.getDrillDownCollectingBankTransactionReportByBranch(subCode,issuerCode,beginDate, endDate);
        	for(int i=0;i<cardholdertranLog.size();i++)
         	{
        		ctrans = (C_TRANSACTION)cardholdertranLog.get(i);
         		t += Double.parseDouble(ctrans.getTrans_amount());
         	}
         	setTotal_amount(t);
        	System.out.println("failedFundsLog " + cardholdertranLog.size());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*Bill Investigation scheme*/
	public void viewBillSchemeLog()
	{
		try
		{
			//String strParam = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("firstParameter");
			
			billLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
            
            FacesContext context = FacesContext.getCurrentInstance();
			String scheme_card = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
		   
            
            billLog = reportModel.getPayTranScheme(getFrom_source(),beginDate , endDate,getMerchant_code().trim(), scheme_card);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	/*
	 * view ALL Payment Transaction
	 */
	public void viewPaymentTransaction()
	{
		try
		{
			fundsTransfer.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
            
            //String card_num = getCard_num().trim();
            String optn = getOptionType();
            
            String amount = getMobileno().trim();
            
            String transCode = getTrans_code();
            String transCount = getTrans_count();
            
            //String tranSummary = getTransSummary();
            
            
            if(optn == null)
            { 
            	optn = "0";
            }
            
            
            if(amount.length() <=0)
            {
            	amount = "0";
            	facesMessages.add(Severity.WARN, "Amount is required");
            }
            
			//System.out.println("  Amount  " + amount);
			//System.out.println("amount " + amount);
            
          
            	 fundsTransfer = reportModel.getPaymentTransaction(optn, Double.parseDouble(amount),transCode,transCount, beginDate,endDate);
                 System.out.println("getPaymentTransaction"+fundsTransfer.size());
            	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void viewPaymentTransaction_Wallet()
	{
		try
		{
			walletLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
            
            //String card_num = getCard_num().trim();
            String optn = getOptionType();
            
            String amount = getMobileno().trim();
            
            String transCode = getTrans_code();
            String transCount = getTrans_count();
            
            //String tranSummary = getTransSummary();


            if(optn == null)
            { 
            	optn = "0";
            }
           
            
            if(amount.length() <=0)
            {
            	amount = "0";
            	facesMessages.add(Severity.WARN, "Amount is required");
            }
            
            	walletLog = reportModel.getPaymentTransaction_Wallet(optn, Double.parseDouble(amount),transCode,transCount, beginDate,endDate);
                 System.out.println("getPaymentTransaction   --- > "+walletLog.size());
            	
           
           
           
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*Friendship center report from t_paytrans*/
	public void viewFriendshipCenterTranDetials()
	{
		try
		{			
			billLog.clear();
			
			ReportModel reportModel = new ReportModel();
			
		
			String type = getFrom_source();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
            
            PAYTRANS tran = null;
	        double d = 0.0;
	        String merchantcode = "";
	        
	        System.out.println(type + " type");
	        
	        if(type == null || type.equals("ALL"))//no type selected
	        {
	        	FacesContext context = FacesContext.getCurrentInstance();
				merchantcode = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
	        }
	        else
	        {
	        	merchantcode = type;
	        }
	        
	        billLog = reportModel.viewFriendshipCenterTranDetials(beginDate, endDate, merchantcode);
			for(int i=0;i<billLog.size();i++)
			{
				tran = (PAYTRANS)billLog.get(i);
			 	d += Double.parseDouble(tran.getTrans_amount());	        	
			}
			setTotal_amount(d);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	
	
	
	/*
	 * this method is to invistage subcriber
	 * 
	 */
	public void viewBillPaymentSubscriberDetials()
	{
		try
		{
			//String strParam = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("firstParameter");
			
			billLog.clear();
			ReportModel reportModel = new ReportModel();
			
		
			String subscriberId = getFrom_source();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
            
            PAYTRANS tran = null;
	        double d = 0.0;
            
            FacesContext context = FacesContext.getCurrentInstance();
			String merchantcode = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
		    //String merchantcode = "2140010004:2140010003:7363738727";
	    	billLog = reportModel.getBillPaymentSubscriberDetails(subscriberId, beginDate, endDate, merchantcode);
			for(int i=0;i<billLog.size();i++)
			{
				tran = (PAYTRANS)billLog.get(i);
			 	d += Double.parseDouble(tran.getTrans_amount());	        	
			}
			setTotal_amount(d);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	
	public void getGTBillPaymentSubscriberSummary()
	{
		try
		{			
			billLog.clear();
			ReportModel reportModel = new ReportModel();
			
		
			String subscriberId = getFrom_source();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
            
            PAYTRANS tran = null;
	        double d = 0.0;
            
            FacesContext context = FacesContext.getCurrentInstance();            
			String merchantcode = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
		   
      		billLog = reportModel.getGTBillPaymentSubscriberSummary(subscriberId, beginDate, endDate, merchantcode);
			for(int i=0;i<billLog.size();i++)
			{
				tran = (PAYTRANS)billLog.get(i);
			 	d += Double.parseDouble(tran.getTrans_amount());	        	
			}
			setTotal_amount(d);
            
          
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	public void getGTBillPaymentSubscriberDetails()
	{
		try
		{			
			supportLog.clear();
			ReportModel reportModel = new ReportModel();
			String subscriberId = getId();   
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
			
            FacesContext context = FacesContext.getCurrentInstance();
            String merchantcode = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
            
            supportLog = reportModel.getGTBillPaymentSubscriberDetails(subscriberId, merchantcode, beginDate, endDate);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	

	
	/*Failed Funds Transfer*/
	public void viewFailedFundsTransferLog()
	{
		try
		{
			
			fundsTransfer.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
			
            String op = getOptionType();
           

            if(op.equals("A"))//select failed transfer and group by error codes
            {
            	fundsTransfer = reportModel.getFailedFundsTransferGroupByErrorCode(beginDate , endDate);
            	System.out.println("fundsTransfer.size in action " + fundsTransfer.size());
            }
            else if(op.equals("B"))//select failed transfer and group by banks
            {
            	fundsTransfer = reportModel.getFailedFundsTransferGroupedByBankCode(beginDate , endDate);
            }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	
	
	
	/**
	 * Method to view the Failed Funds Transfer by bank code*/
	public void viewFailedFundsTransferByBankCode()
	{
		try
		{			
			failedSummaryLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";

            String bankCode = getId();
            
            failedSummaryLog = reportModel.getFailedFundsTransferByBankCodeGroupByErrorCode(bankCode, beginDate , endDate);
            
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*Failed Funds Transfer by error code*/
	public void viewFailedFundsTransferByErrorCode()
	{
		try
		{
			
			failedFundsLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";

            String errorCode = getId();
            
            failedFundsLog = reportModel.getFailedFundsTransferByErrorCode(errorCode , beginDate , endDate);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	

	/*Failed Funds Transfer by bank code and error code*/
	public void viewFailedFundsTransferByBankCodeAndErrorCode()
	{
		try
		{
			
			failedFundsLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";

            String s = getId();
            String errorCode = s.substring(0,s.indexOf(":"));
            String bankCode = s.substring(s.lastIndexOf(":")+1);
            
            failedFundsLog = reportModel.getFailedFundsTransferByBankCodeAndErrorCode(bankCode, errorCode , beginDate , endDate);

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	
	/*Funds Transfer*/
	public void viewFundsTransferLog()
	{
		try
		{
			fundsTransfer.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
            
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
            
            fundsTransfer = reportModel.getFundsTransferTransaction(card_num, optn, Double.parseDouble(amount), beginDate ,
            		endDate,transCode,transCount, tranSummary );
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void activateStatus()
	{
		
		
		try
		{
			
			
		}catch(Exception ex)
		{
					
					}
		
	}
	
	/*Mobile Subscription Details*/
	public void viewMobileSubscriptionDetials()
	{
		try
		{
			mobileSubscriberLog.clear();
			mobileSubscriberCardLog.clear();
			ReportModel reportModel = new ReportModel();
			E_MOBILE_SUBSCRIBER em = null;
			setSelected(false);
			
			
			FacesContext context = FacesContext.getCurrentInstance();
	        String esaType = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getEsa_type();
	        String bankApp = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getBankApp();
			
			if(getLine_type().equals("Version I"))
			{
				mobileSubscriberLog = reportModel.getE_MOBILE_SUBSCRIBER(getFrom_source().trim(), getLine_type(),"");
				
				//if(esaType.equals("0"))//0 means the user must login with esa
				//{
				
					for(int i=0;i<mobileSubscriberLog.size();i++)
					{
						em = (E_MOBILE_SUBSCRIBER)mobileSubscriberLog.get(i);
						if(em.getAppid().equals(bankApp))
						{
							setSelected(true);
							break;
						}
					}
				//}
	            mobileSubscriberCardLog = reportModel.getE_MOBILE_SUBSCRIBER_CARD(getFrom_source().trim(), getLine_type());
	            setStatus_id("Version I");
			}
			else if(getLine_type().equals("Version II"))
			{
				if(getBank_code() == null || getBank_code().length()<=0)
				{
					facesMessages.add(Severity.WARN, "Apps is required");
					return;
				}
				else
				{
					String subscriber_id = "";
					String lname = "";
					String fname = "";
					String email = "";
					
					
				/*	FacesContext context = FacesContext.getCurrentInstance();
					
					lname = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getLastname();
					fname = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getFirstname();
					email = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getEmail();
				*/	
					
					
					mobileSubscriberLog = reportModel.getE_MOBILE_SUBSCRIBER(getFrom_source().trim(), getLine_type(), getBank_code());
					for(int i=0;i<mobileSubscriberLog.size();i++)
					{
						E_MOBILE_SUBSCRIBER m = (E_MOBILE_SUBSCRIBER)mobileSubscriberLog.get(i);
						subscriber_id = m.getId();
					}
					System.out.println("subscriber_id " + subscriber_id);
		            mobileSubscriberCardLog = reportModel.getE_MOBILE_SUBSCRIBER_CARD(subscriber_id, getLine_type());
		            setStatus_id("Version II");
				}
			}  
			System.out.println("mobileSubscriberLog " + mobileSubscriberLog.size());
			System.out.println("mobileSubscriberCardLog " + mobileSubscriberCardLog.size());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*Mobile Subscription Details for scheme*/
	public void viewMobileSubscriptionDetails_scheme()
	{
		try
		{
			mobileSubscriberLog.clear();
			mobileSubscriberCardLog.clear();
			ReportModel reportModel = new ReportModel();
			
			String mobile_no = getFrom_source();
			String app_id = getBank_code();
			String subscriber_id = "";
			
			FacesContext context = FacesContext.getCurrentInstance();
			String scheme_card = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
		    
			String databaseServer[] = scheme_card.split(":");
			scheme_card = databaseServer[0];
			
			if(app_id == null || app_id.length()<=0)
			{
				facesMessages.add(Severity.WARN, "Apps is required");
				return;
			}
			else
			{
				mobileSubscriberLog = reportModel.getE_MOBILE_SUBSCRIBER(mobile_no, "Version II", app_id);
				for(int i=0;i<mobileSubscriberLog.size();i++)
				{
					E_MOBILE_SUBSCRIBER m = (E_MOBILE_SUBSCRIBER)mobileSubscriberLog.get(i);
					subscriber_id = m.getId();
				}
	            mobileSubscriberCardLog = reportModel.getE_MOBILE_SUBSCRIBER_CARDScheme(subscriber_id, mobile_no, databaseServer[1]);
	            setStatus_id("Version II");
			}
			
			System.out.println("mobileSubscriberLog " + mobileSubscriberLog.size());
			System.out.println("mobileSubscriberCardLog " + mobileSubscriberCardLog.size());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*Send Card Number*/
	public void sendCardNumberDetails()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			String message = "";
			try
			{
				String lname = "";
				String fname = "";
				String email = "";
				
				
				FacesContext context = FacesContext.getCurrentInstance();
				lname = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getLastname();
				fname = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getFirstname();
				email = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getEmail();
				
				getCurUser().setLastname(lname);
				getCurUser().setFirstname(fname);
				getCurUser().setEmail(email);
				
				//logic to check if the card number is PCI encrypted
				String s = getEdit_id();
				char c[] = s.toCharArray();
				for(int i=0;i<c.length;i++)
				{
			      if(Character.isLetter(c[i]))
			      {
			        //i can see letters in the card number, so i decrypt to the original format
			        s = reportModel.cryptPan(getEdit_id(), 2);
			        break;
			      }
				}
				
				setEdit_id(s);
				
				Events.instance().raiseEvent("cardnumbermailer");
				message = "Email has been sent successfully";
			}
			catch(Exception e)
			{
				e.printStackTrace();
				message = "Email not sent " + e.getMessage();
			}
			
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/* this method is to active user */
	public void userActivate()
	{
		try
		{
			AdminModel adminModel = new AdminModel();
			ReportModel reportModel = new ReportModel();
			

			FacesContext context = FacesContext.getCurrentInstance();
			String username = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
			String userCode= ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
			String ip_address = InetAddress.getLocalHost().getHostAddress();
			
			//String auth_status = "1";//1 means pending
			String request_by = username;
			String request_byip = ip_address;
			String version_type = getStatus_id();
			String card_num = getEdit_id();
						
			String  str = "";
			String mobile = getFrom_source();
			//String unionCode = "032";
			
			System.out.println("Version  "+version_type+"Requested By "+request_by+"Request_byIp"+request_byip+" Mobile number "+mobile);
			
			String message = adminModel.setMappedMobileForActivation(version_type, request_by, request_byip, mobile,userCode);
			facesMessages.add(Severity.INFO, message);
			//str = reportModel.getActivateUsers(mobile);
			
			//System.out.println("getActivateUsers  ====  "+str);
			
			/*str = reportModel.getActivateUsers(mobile);
			if(str.equalsIgnoreCase("Success"))
			{
			
				facesMessages.add(Severity.INFO, "U have Successfully Activated Your Mobile Number");
				
			}else
			{
				facesMessages.add(Severity.INFO, "Unable to Activate Your Mobile Number");
			}*/
	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void userDeActivate()
	{
		try
		{
			AdminModel adminModel = new AdminModel();
			ReportModel reportModel = new ReportModel();
			
			FacesContext context = FacesContext.getCurrentInstance();
			String username = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
			String userCode = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
			String ip_address = InetAddress.getLocalHost().getHostAddress();
			
			//String auth_status = "1";//1 means pending
			String request_by = username;
			String request_byip = ip_address;
			String version_type = getStatus_id();
			String card_num = getEdit_id();
						
			String  str = "";
			String mobile = getFrom_source();
			//String unserCode = "032";
			

			String message = adminModel.setMappedMobileForDeativation(version_type, request_by, request_byip, mobile,userCode);
			facesMessages.add(Severity.INFO, message);
			//str = reportModel.getDeActivateUsers(mobile);
			/*if(str.equalsIgnoreCase("Success"))
			{
				facesMessages.add(Severity.INFO, "U have Successfully DeActivate Your Mobile Number");
			}else
			{
				facesMessages.add(Severity.INFO, "Unable to  DeActivate Your Mobile Number");
			}*/
	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}



	
	/*Summary Log*/
	public void viewSummaryReport()
	{
		try
		{
			summaryLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
			
            summaryLog = reportModel.getLogSummary(beginDate , endDate);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}


	/*Failed Summary*/
	public void failedSummary()
	{
		ReportModel reportModel = new ReportModel();
		String channel = getId().trim();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String beginDate = df.format(getStart_date());
        beginDate = beginDate + " " + getStart_hr() + ":00";
        
        String endDate = df.format(getEnd_date());
        endDate = endDate + " " + getEnd_hr() + ":59";
        
        setChannel_id(channel);

        failedSummaryLog = reportModel.getFailedSummary(getId(), beginDate, endDate);
        
        if(channel.equals("VTU - VERSION II"))
        {
        	supportLog = reportModel.getVTUForReprocessing(beginDate, endDate);
        }
	}
	
	
	/*Failed Summary Drill Down*/
	public void failedSummaryDrillDown()
	{
		ReportModel reportModel = new ReportModel();
		String respond_code = getId().trim();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String beginDate = df.format(getStart_date());
        beginDate = beginDate + " " + getStart_hr() + ":00";
        
        String endDate = df.format(getEnd_date());
        endDate = endDate + " " + getEnd_hr() + ":59";

        String channel = getChannel_id();
        if(channel.equalsIgnoreCase("WEB"))
			channel = "1";
	
        else if(channel.equalsIgnoreCase("MOBILE"))
			channel = "2";
		
        else if(channel.equalsIgnoreCase("POS"))
			channel = "3";
		
        else if(channel.equalsIgnoreCase("ATM"))
			channel = "4";

        else if(channel.equalsIgnoreCase("VTU"))
			channel = "5";
		
        else if(channel.equalsIgnoreCase("MOBILE - VERSION II"))
			channel = "6";
		
        else if(channel.equalsIgnoreCase("VTU - VERSION II"))
			channel = "7";
		
		setChannel_id(channel);
		
		failedSummaryDrillDownLog = reportModel.getFailedTransactions(channel, beginDate, endDate, respond_code);
		System.out.println("failedSummaryDrillDownLog size " + failedSummaryDrillDownLog.size());
		System.out.println("channel " + getChannel_id());
	}
	
	
	/*Process Failed VTU Transaction*/
	public void processFailedVTUTransaction()
	{
		try
		{
			ReportModel reportModel = new ReportModel();

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	        String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
	        String respondcode = getEdit_id();
	        String channel = getChannel_id();
			
			String message = reportModel.processFailedVTUTransaction(beginDate, endDate, respondcode);
			if(channel.equals("VTU - VERSION II"))
	        {
				supportLog.clear();
	        	supportLog = reportModel.getVTUForReprocessing(beginDate, endDate);
	        }
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			
		}
	}
	
	
	//this is used to drill down on the sms key when searching in mobile investigation
	public void drillDown_Log()
	{
		ReportModel reportModel = new ReportModel();
		
		try
		{
			String id = getId().substring(0, getId().indexOf(":"));
			String mobile = getId().substring(getId().indexOf(":")+1);
			FacesContext context = FacesContext.getCurrentInstance();
			String user_code = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
			
			/*smsReceiveLog = reportModel.getT_SMS_RECEIVE(id, mobile, user_code);
			smsLog = reportModel.getE_SMS_LOG("02"+id, mobile, user_code);
			failedFundsLog = reportModel.getE_REQUEST_LOG("02"+id, mobile, user_code);
			smsSendLog = reportModel.getT_SMS_SEND(id, mobile, user_code);
			vtuLog = reportModel.getVTUByTransID("02"+id, mobile, user_code);
			tranLog = reportModel.getE_TRANSACTION("02"+id, user_code);	*/
			
			smsReceiveLog = reportModel.getT_SMS_RECEIVE(id, mobile, user_code);
			smsLog = reportModel.getE_SMS_LOG(id, mobile, user_code);
			failedFundsLog = reportModel.getE_REQUEST_LOG(id, mobile, user_code);
			smsSendLog = reportModel.getT_SMS_SEND(id, mobile, user_code);
			vtuLog = reportModel.getVTUByTransID(id, mobile, user_code);
			tranLog = reportModel.getE_TRANSACTION(id, user_code);	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
				
	}
	/**
	 * This is to view the list of Bill payment Transaction 
	 */
	
	//this is used to drill down on the tran id when searching in web investigation
	public void drillDown()
	{
		ReportModel reportModel = new ReportModel();
		
		try
		{
			String id = getId().substring(0, getId().indexOf(":"));
			String mobile = getId().substring(getId().indexOf(":")+1);
			mobile = "00000";
			
			FacesContext context = FacesContext.getCurrentInstance();
			String user_code = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
			
			failedFundsLog = reportModel.getE_REQUEST_LOG(id, mobile, user_code);
			tranLog =  reportModel.getE_TRANSACTION(id, user_code);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	//this is used to drill down on the tran id when searching in pocket moni investigation
	public void drillDownPocketMoni()
	{
		ReportModel reportModel = new ReportModel();
		
		try
		{
			System.out.println("drill down pocket moni");
			String unique_trans_id = getId().substring(0, getId().indexOf(":"));
			String message_id = getId().substring(getId().indexOf(":")+1);

			FacesContext context = FacesContext.getCurrentInstance();
			String user_code = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
		
			smsLog = reportModel.getPocketMoniOutgoingLogByMessageID(message_id);
			tranLog =  reportModel.getE_TRANSACTION(unique_trans_id, user_code);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	//this is used to delete mapped subscriber
	public void deleteMappedSubscriber()
	{
		AdminModel adminModel = new AdminModel();
		
		try
		{
			
			FacesContext context = FacesContext.getCurrentInstance();
			String userType = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getType_id();
			if(userType.equals("1") || userType.equals("5"))//admin/ bank admin
			{
				System.out.println(getStatus_id() + " status");
				System.out.println(getOperation_id() + " mobile");
				System.out.println(getTo_dest() + " id");
				
				String id = getTo_dest();
				String mobile = getOperation_id();
				String version_type = getStatus_id();
				
				String message = adminModel.deleteSubscriber(id, mobile, version_type);
				facesMessages.add(Severity.INFO, message);
			}
			else
			{
				facesMessages.add(Severity.INFO, "Only administrators can delete subscriber information");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	//this is used to delete mapped subscriber card
	public void deleteMappedSubscriberCard()
	{
		AdminModel adminModel = new AdminModel();
		
		try
		{
			
			FacesContext context = FacesContext.getCurrentInstance();
			String username = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
			String ip_address = InetAddress.getLocalHost().getHostAddress();
			
			String auth_status = "2";//2 means delete
			
			String auth_by = username;
			String auth_byip = ip_address;
			String card_num = getEdit_id();
			
			
			String message = adminModel.deleteSubscriberCard(card_num, auth_status, auth_by, auth_byip);
			if(message.equals("Records successfully deleted"))
			{
				mobileSubscriberCardLog.clear();
				mobileSubscriberLog.clear();
			}
			facesMessages.add(Severity.INFO, message);
			
			/*if(userType.equals("1") || userType.equals("5"))//admin/ bank admin
			{
				String cardnum = getEdit_id();
				String version_type = getStatus_id();
				
				String message = adminModel.deleteSubscriberCard(cardnum, version_type);
				facesMessages.add(Severity.INFO, message);
			}
			else
			{
				facesMessages.add(Severity.INFO, "Only administrators can delete subscriber information");
			}*/

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*This method is used to delete the mapping of the merchant ext mapping*/
	public void deleteMerchantCommissionExt()
	{
		ReportModel reportModel = new ReportModel();
		
		try
		{
			String mcode = getEdit_id();
			String message = reportModel.deleteMerchantCommissionExt(mcode);
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	//this is used to set a card for deleting
	public void setMappedCardForDeleting()
	{
		AdminModel adminModel = new AdminModel();
		ReportModel reportModel = new ReportModel();
		
		try
		{
			
			FacesContext context = FacesContext.getCurrentInstance();
			String username = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
			String ip_address = InetAddress.getLocalHost().getHostAddress();
			
			String auth_status = "1";//1 means pending
			String request_by = username;
			String request_byip = ip_address;
			String auth_by = "";
			String auth_byip = "";
			String card_num = getEdit_id();
			String alias = getOperation_id().trim();
			String version_type = getStatus_id();
			
			/*System.out.println("mobile "  + getFrom_source().trim());
			System.out.println("mobile "  + getLine_type().trim());
			System.out.println("mobile "  + getBank_code().trim());*/
			
			
			String subscriber_id = "";
			mobileSubscriberLog = reportModel.getE_MOBILE_SUBSCRIBER(getFrom_source().trim(), getLine_type(), getBank_code());
			for(int i=0;i<mobileSubscriberLog.size();i++)
			{
				E_MOBILE_SUBSCRIBER m = (E_MOBILE_SUBSCRIBER)mobileSubscriberLog.get(i);
				subscriber_id = m.getId();
			}
			//System.out.println("subscriber_id " + subscriber_id);
            mobileSubscriberCardLog = reportModel.getE_MOBILE_SUBSCRIBER_CARD(subscriber_id, getLine_type());
            System.out.println("no of cards "  + mobileSubscriberCardLog.size() + " " + alias);
            
			if(mobileSubscriberCardLog.size() > 1 && alias.equals("Default"))
			{
				facesMessages.add(Severity.INFO, "You cannot unmap the Default card until all other cards have been successfully un-mapped and authorised");
			}
			else
			{
				String message = adminModel.setMappedCardForDeleting(version_type, auth_status, request_by, request_byip, card_num);
				facesMessages.add(Severity.INFO, message);
			}
			/*String message = adminModel.setMappedCardForDeleting(version_type, auth_status, request_by, request_byip, card_num);
			facesMessages.add(Severity.INFO, message);
			*/

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	//this is used to send sms back to the initiator
	public void resendPin()
	{
		ReportModel reportModel = new ReportModel();
		
		try
		{
			String id = getMobileno().substring(0, getMobileno().indexOf(":"));
			String mobile = getMobileno().substring(getMobileno().indexOf(":")+1);
			String resp = "";

			resp = reportModel.getE_SMS_LOGResendPIN(id).trim();
			
			resp = resp.replaceAll(" ", "%20");
			resp = resp.replaceAll("\n", "%0d%0a");//URL Encode for new line
			
			
			//System.out.println(resp + " resp");
			
			HttpMessenger ht = new HttpMessenger();
			ht.setServer("http://172.16.10.134");
			ht.setWebApp("etzsmsapi");
			ht.setQueryString("sender.jsp?dest=" + mobile.trim() + "&msg="+ resp.trim());
			
			 
			try
			{
				ht.submit();
				facesMessages.add(Severity.WARN, "PIN sent successfully");
				
			}catch(Exception e)
			{
				e.printStackTrace();
				facesMessages.add(Severity.WARN, "Error sending PIN");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	
	//this is used to send sms back to the initiator for outgoing mess
	public void resendPinVersionII()
	{
		ReportModel reportModel = new ReportModel();
		
		try
		{
			String id = getMobileno().substring(0, getMobileno().indexOf(":"));
			String mobile = getMobileno().substring(getMobileno().indexOf(":")+1);
			String resp = "";

			resp = reportModel.getOutGoingResendPIN(id).trim();
			
			resp = resp.replaceAll(" ", "%20");
			resp = resp.replaceAll("\n", "%0d%0a");//URL Encode for new line
			
			
			//System.out.println(resp + " resp");
			
			HttpMessenger ht = new HttpMessenger();
			ht.setServer("http://172.16.10.134");
			ht.setWebApp("etzsmsapi");
			ht.setQueryString("sender.jsp?dest=" + mobile.trim() + "&msg="+ resp.trim());
			 
			try
			{
				ht.submit();
				facesMessages.add(Severity.WARN, "PIN sent successfully");
				
			}catch(Exception e)
			{
				e.printStackTrace();
				facesMessages.add(Severity.WARN, "Error sending PIN");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	
	
	//this is used to re-send bill payment
	public void resendBillPayment()
	{
		ReportModel reportModel = new ReportModel();
		
		try
		{
			String unique_transid = getEdit_id();
            String message = reportModel.resendBillPayment(unique_transid);
            facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	
	//this is used to reset PIN from pocketMoni DB
	public void resetPINVersionII()
	{
		ReportModel reportModel = new ReportModel();
		
		try
		{
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	 
	
	//this is used to block and unblock a card from pocketMoni DB
	public void block_unblockCardVersionII()
	{		
		try
		{
			
			System.out.println("about to call webservice");
			
			Cryptographer crypt = new Cryptographer();       
            
			HttpServletRequest str = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String ipAddress = str.getRemoteAddr();
			
			/*File g = new File("");
		    Properties props = new Properties();
		    InputStream in = (InputStream)(new FileInputStream(new File(g.getAbsolutePath() + "\\" + "xportal-config.properties")));
		    props.load(in);
		    String setup_excel_folder = props.getProperty("xportal_webservice");
		    */ 
			
			String setup_excel_folder = "c:/XPortalWebServiceFile/XPortalWS.xml";
			
            xportalserviceclient.XPortalWSService service = new xportalserviceclient.XPortalWSService(new URL("file:"+ setup_excel_folder +""));
            xportalserviceclient.XPortalWS port = service.getXPortalWSPort();
             
            String type = getOperation_id();
            String cardnum = getEdit_id();
            String mac = type + cardnum + ipAddress;
            mac = crypt.doMd5Hash(mac);
            
			/*java.lang.String type = getOperation_id();
            java.lang.String cardnum = getEdit_id();
            java.lang.String ipAddress = "127.0.0.1";
            java.lang.String mac = "C9E6529E3A0A65E0EDBC593F54A4CE0D";*/
			
            System.out.println("type " + type);
			System.out.println("card_num " + cardnum);
			System.out.println("ipAddress " + ipAddress);
            System.out.println("mac " + mac);
            
            String message = port.cardBlockUnBlockCardnum(type, cardnum, ipAddress, mac);
            if(message.startsWith("0:successful"))
            {
            	if(type.equals("0"))
            		facesMessages.add(Severity.WARN, "Card successfully unblocked");
            	else
            		facesMessages.add(Severity.WARN, "Card successfully blocked");
            }
            else
            {
            	facesMessages.add(Severity.WARN, "Could not block/unblock card");
            }
            System.out.println("unblock/block card message " + message);
            
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			facesMessages.add(Severity.WARN, "Error occured while trying to block/unblock card");
		}
		
	}
	
	//this is used to edit bill payment
	public void editBillPayment()
	{
		ReportModel reportModel = new ReportModel();
		
		try
		{
			String unique_transid = getEdit_id();
            String message = reportModel.resendBillPayment(unique_transid);
            facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	
	
	/*This is used to check that the pin to be resent has a response code of 0*/
	public boolean checkValue(String value)
	{
		try
		{
			ReportModel rp = new ReportModel();
			SMS_LOG s = rp.getE_SMS_LOG_2("02"+value);
			if(s != null)
			{
				if(s.getResponse_code().equals("Transaction Successfull"))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		catch(Exception ex)
		{
			return false;
		}
	}
	
	
	//cardNumberUpdateUpload
    public void cardNumberUpdateUpload()
    {
    	try
    	{    		
    		FacesContext context = FacesContext.getCurrentInstance();
    		String createdby = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getEmail();
    		String cardScheme = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
			
    		cardScheme = cardScheme.substring(0, cardScheme.lastIndexOf(":"));
    		
    		if(cardfileUploadBean.getByteupload() == null || cardfileUploadBean.getByteupload().equals("") ||
    				cardfileUploadBean.getByteupload().equals("null"))
			{
				facesMessages.add(Severity.INFO, "Card number list is required");
				return;
			}
			
			String message = "Records successfully inserted";
			if(message.equals("Records successfully inserted"))
			{
				String uploadedFile = "in" + "#" + cardScheme + "#" + createdby + ".xls";
				convertToFile(cardfileUploadBean.getByteupload(), uploadedFile);
				message = "Card number list successfully queued for upload, the confirmation details will be sent to the logged on user";
			}
			else
			{
				cardfileUploadBean.setByteupload(null);
			}
			facesMessages.add(Severity.INFO, message);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }
	
	//This method is used for etmc event report
	public void viewETMCEventLog()
	{
		try
		{
			tmcEventLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
			
            tmcEventLog = reportModel.getE_TMCEVENT(getStatus_id(),beginDate , endDate);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	//This method is used to get pin log investigation
	public void viewPinLog()
	{
		try
		{
			pinLog.clear();
			ReportModel reportModel = new ReportModel();
			pinLog = reportModel.getPinLog(getCard_num().trim());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	//This method is used for bank event report
	public void viewETMCRequestLog()
	{
		try
		{
			tmcRequestLog.clear();
			ReportModel reportModel = new ReportModel();
            tmcRequestLog = reportModel.getE_TMCREQUESTFORBANK(getStatus_id());
            //System.out.println("tmcRequestLog " + tmcRequestLog.size());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	
	
	/*Merchant Commission Report*/
	public void viewMerchantSummaryReport()
	{
		try
		{
			merchantSummaryLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
			
            merchantSummaryLog = reportModel.getE_TRANSACTIONSummaryByDate(beginDate, endDate);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*Merchant Report for other users apart from business development user type*/
	public void viewMerchantReport()
	{
		try
		{
			merchantReportLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
            
            String channel = getChannel_id();
            String transcode = getTrans_code();   
            String mcode = getMerchant_code();
            
            
            
            if(channel_id == null)channel = "";
            if(transcode == null)transcode = "";
            
            if(!optionType.equals("A") && !optionType.equals("B") && !optionType.equals("C"))
            {
            	if(mcode == null || mcode.trim().length()<=0)
            	{
            		facesMessages.add(Severity.ERROR, "Merchant Code is required");
            		return;
            	}
            }
            
            
            //if((mcode == null || mcode.equals("") && !optionType.equals("A")) || (mcode == null || mcode.equals("") && !optionType.equals("B")))
            //if(!optionType.equals("A") || !optionType.equals("B"))
            //{
           // 	facesMessages.add(Severity.ERROR, "Merchant Code is required");
           // }
          //  else
           // {
            	//this is used to check that the batch id has not been clicked
            	if(optionType != null && optionType.trim().equals("S"))
            	{
            		setStrParam(null);
            		settle_batch = reportModel.getE_SETTLE_BATCH(beginDate, endDate, mcode,
            				channel, transcode);
            		allMerchantTransReportLog.clear();
            		allMerchantBankReportLog.clear();
            		merchantSummaryReportLog.clear();
            	}
            	//unsettled and summarized report
                else if(optionType != null && optionType.trim().equals("U"))
                {
                	merchantSummaryReportLog = reportModel.getE_TRANSACTIONSummaryByDateAndMerchant(beginDate, endDate,
                			mcode, channel, transcode);
                	setStrParam("");
                	settle_batch.clear();
                	allMerchantTransReportLog.clear();
                	allMerchantBankReportLog.clear();
                	optionType2 = "SR";
                }
            	//all and summarized report at trans_code stage
                else if(optionType != null && optionType.trim().equals("A") && optionType2.equals("SR"))
                {
                	E_TRANSACTION e = null;
                	E_TRANSACTION er = null;
                	double t = 0.0;
                	int c = 0;
                	double etzAmt = 0.0d;
                	double etz = 0.0;
                	
                	allMerchantTransReportLog = reportModel.getE_TRANSACTIONGroupByTransCode(beginDate, endDate);
                	for(int i=0;i<allMerchantTransReportLog.size();i++)
                	{
                		etz = 0.0;
                		e = (E_TRANSACTION)allMerchantTransReportLog.get(i);
                		t += Double.parseDouble(e.getTotal_amount());
                		c += Integer.parseInt(e.getTransaction_count());
                		
                		//start of addition
                		
                		//This is used to get calculation grouping by channel
                		/*allMerchantChannelReportLog = reportModel.getE_TRANSACTIONByTransCodeGroupByChannel(e.getTrans_code(), beginDate, endDate);
            			for(int tr=0;tr<allMerchantChannelReportLog.size();tr++)
                    	{
            				
                    		er = (E_TRANSACTION)allMerchantChannelReportLog.get(tr);

                    		//This is used to get the commission
                    		etz += viewCommissionTotalByTransCodeAndChannel(beginDate, endDate, e.getTrans_code(), er.getChannelid());
                    	}
            			e.setCommission_value(""+etz);
            			etzAmt += etz;*/
                    	
            			//end of addition
                	}
                	setTotal_amount(t);
                	setTotal_count(c);
                	setTotal_etz_amount(etzAmt);
                	
                	settle_batch.clear();
                	merchantSummaryReportLog.clear();
                	allMerchantBankReportLog.clear();
                	supportLog.clear();
                }
            	//all and summarized report at bank stage
                else if(optionType != null && optionType.trim().equals("B") && optionType2.equals("SR"))
                {
                	E_TRANSACTION e = null;
                	double t = 0.0;
                	int c = 0;
                	allMerchantBankReportLog = reportModel.getE_TRANSACTIONGroupByBankCode(beginDate, endDate);
                	for(int i=0;i<allMerchantBankReportLog.size();i++)
                	{
                		e = (E_TRANSACTION)allMerchantBankReportLog.get(i);
                		t += Double.parseDouble(e.getTotal_amount());
                		c += Integer.parseInt(e.getTransaction_count());
                		
                	}
                	setTotal_amount(t);
                	setTotal_count(c);
                	settle_batch.clear();
                	merchantSummaryReportLog.clear();
                	allMerchantTransReportLog.clear();
                	supportLog.clear();
                }
            	//all and summarized report at group heads of bizness
                else if(optionType != null && optionType.trim().equals("C") && optionType2.equals("SR"))
                {
                	String[] bizHeads = new String[8];
                	double t = 0.0;
                	int c = 0;
                	supportLog = reportModel.getBizHeadAccountId(beginDate, endDate);
                	for(int i=0;i<supportLog.size();i++)
                	{
                		bizHeads = (String[])supportLog.get(i);
                		c += Integer.parseInt(bizHeads[5]);
                		t += Double.parseDouble(bizHeads[6]);

                		bizHeads[7] = reportModel.getMerchantCodeCommissionCountByAccountId("", "", beginDate, endDate, bizHeads[0]);
                	}
                	setTotal_amount(t);
                	setTotal_count(c);
                	
            		
                	settle_batch.clear();
                	merchantSummaryReportLog.clear();
                	allMerchantTransReportLog.clear();
                	allMerchantBankReportLog.clear();
                }
            //}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*Merchant Report for business development user type*/
	public void viewBizDevMerchantReport()
	{
		try
		{
			merchantReportLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
            
            String channel = getChannel_id();
            String transcode = getTrans_code();   
            String mcode = getMerchant_code();
            
            if(channel_id == null)channel = "";
            if(transcode == null)transcode = "";
            
        	/*all and summarized report at trans_code stage*/
            if(optionType != null && optionType.trim().equals("A") && optionType2.equals("SR"))
            {
            	E_TRANSACTION e = null;
            	E_TRANSACTION er = null;
            	double t = 0.0;
            	int c = 0;
            	double etzAmt = 0.0d;
            	double etz = 0.0;
            	
            	context = FacesContext.getCurrentInstance();
            	String account_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getAccount_id();
            	
            	allMerchantTransReportLog = reportModel.getE_TRANSACTIONByAccountIDGroupByTransCode(beginDate, endDate, account_id);
            	for(int i=0;i<allMerchantTransReportLog.size();i++)
            	{
            		etz = 0.0;
            		e = (E_TRANSACTION)allMerchantTransReportLog.get(i);
            		t += Double.parseDouble(e.getTotal_amount());
            		c += Integer.parseInt(e.getTransaction_count());
            		
            		//start of addition
            		
            		//This is used to get calculation grouping by channel
            		/*allMerchantChannelReportLog = reportModel.getE_TRANSACTIONByTransCodeAndAccountIDGroupByChannel(e.getTrans_code(), beginDate, endDate, account_id);
        			for(int tr=0;tr<allMerchantChannelReportLog.size();tr++)
                	{
                		er = (E_TRANSACTION)allMerchantChannelReportLog.get(tr);

                		//This is used to get the calculation grouping by channel
                		etz += viewCommissionTotalByTransCodeAndChannelAndAccountID(beginDate, endDate, e.getTrans_code(), er.getChannelid(), account_id);
                	}
        			e.setCommission_value(""+etz);
        			etzAmt += etz;*/
                	
        			//end of addition
            	}
            	setTotal_amount(t);
            	setTotal_count(c);
            	setTotal_etz_amount(etzAmt);
            	
            	allMerchantBankReportLog.clear();
            }
        	//all and summarized report at bank stage
            else if(optionType != null && optionType.trim().equals("B") && optionType2.equals("SR"))
            {
            	E_TRANSACTION e = null;
            	double t = 0.0;
            	int c = 0;
            	allMerchantBankReportLog = reportModel.getE_TRANSACTIONGroupByBankCode(beginDate, endDate);
            	for(int i=0;i<allMerchantBankReportLog.size();i++)
            	{
            		e = (E_TRANSACTION)allMerchantBankReportLog.get(i);
            		t += Double.parseDouble(e.getTotal_amount());
            		c += Integer.parseInt(e.getTransaction_count());
            		
            	}
            	setTotal_amount(t);
            	setTotal_count(c);
            	settle_batch.clear();
            	merchantSummaryReportLog.clear();
            	allMerchantTransReportLog.clear();
            }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	//corporate pay commission
	/*public void getCorporatePayCommission()
	{
		try
		{
			supportLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";

        	//context = FacesContext.getCurrentInstance();
        	//String account_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getAccount_id();
        	//String type = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getType_id();
        	
        	supportLog = reportModel.getCorporatePayCommission(beginDate, endDate);
        	//setTotal_amount(t);
        	//setTotal_count(c);
        	//setTotal_etz_amount(etzAmt);
            	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}*/
	
	
    public void getCorporatePayCommission()
    {
          try
          {
                System.out.println("cpay commission");
                supportLog.clear();
                ReportModel reportModel = new ReportModel();
                
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

          String beginDate = df.format(getStart_date());
          beginDate = beginDate + " " + getStart_hr() + ":00";
          
          String endDate = df.format(getEnd_date());
          endDate = endDate + " " + getEnd_hr() + ":59";

          double t = 0.0d;
          
          supportLog = reportModel.getCorporatePayCommission(beginDate, endDate);
          for(int i=0;i<supportLog.size();i++)
          {
                COP_FUNDGATE_LOG cp = (COP_FUNDGATE_LOG)supportLog.get(i);
                t += Double.parseDouble(cp.getTRANS_AMOUNT());
          }
          setTotal_amount(t);
          }
          catch(Exception ex)
          {
                ex.printStackTrace();
          }
    }


	
	
	//---------------------------------------------------------
	
	/*This is for the viewDetailedMerchantReport, this is used when a merchant is clicked from the commission report*/
	public void viewDetailedMerchantReport()
	{
		try
		{
			merchantReportLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	        String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
	        String channel = getChannel_id();
	        String transcode = getTrans_code();
	        String bank_code = getBank_code();
	        
	        
	        if(channel_id == null)channel = "";
	        if(transcode == null)transcode = "";
			total_amount = 0.0;
			
			if(optionType != null && optionType.trim().equals("U"))
    		{
		    	merchantReportLog = reportModel.getE_TRANSACTIONDetailByDateAndMerchant(beginDate, endDate,
		    			merchant_code, channel, transcode, true);
		    	if(merchantReportLog.size()>0)
		    	{
		    		for(int i=0;i<merchantReportLog.size();i++)
		    		{
		    			E_TRANSACTION et = (E_TRANSACTION)merchantReportLog.get(i);
		            	total_amount += Double.parseDouble(et.getTrans_amount());
		    		}
		    	}
		    	setTotal_amount(total_amount);
		    	setStrParam("");
		    	settle_batch.clear();
		    	optionType2 = "DR";
    		}
			//settled and detailed report
			else if(optionType != null && optionType.trim().equals("S"))
            {
            	total_amount = 0.0;
            	merchantReportLog = reportModel.getE_SETTLEMENTDOWNLOADBKDetailByDateAndMerchant(getStrParam(),
            			merchant_code, channel, transcode);
            	if(merchantReportLog.size()>0)
            	{
            		for(int i=0;i<merchantReportLog.size();i++)
            		{
            			E_SETTLEMENTDOWNLOAD_BK et = (E_SETTLEMENTDOWNLOAD_BK)merchantReportLog.get(i);
                    	total_amount += Double.parseDouble(et.getTrans_amount());
            		}
            	}
            	setTotal_amount(total_amount);
            	optionType2 = "DR";
            }
			//all and detailed report, A is the normal commision report
			else if(optionType != null && optionType.trim().equals("A"))
            {
				merchantReportLog = reportModel.getE_TRANSACTIONDetailByDateAndMerchant(beginDate, endDate,
		    			merchant_code, channel, transcode, false);
		    	if(merchantReportLog.size()>0)
		    	{
		    		for(int i=0;i<merchantReportLog.size();i++)
		    		{
		    			E_TRANSACTION et = (E_TRANSACTION)merchantReportLog.get(i);
		            	total_amount += Double.parseDouble(et.getTrans_amount());
		    		}
		    	}
		    	setTotal_amount(total_amount);
		    	setStrParam("");
		    	settle_batch.clear();
		    	optionType2 = "DR";
            }
			//all by bank and detailed report
			else if(optionType != null && optionType.trim().equals("B"))
            {
				merchantReportLog = reportModel.getE_TRANSACTIONDetailByDateAndMerchant_Bank(beginDate, endDate,
		    			merchant_code, channel, transcode, false, bank_code);
		    	if(merchantReportLog.size()>0)
		    	{
		    		for(int i=0;i<merchantReportLog.size();i++)
		    		{
		    			E_TRANSACTION et = (E_TRANSACTION)merchantReportLog.get(i);
		            	total_amount += Double.parseDouble(et.getTrans_amount());
		    		}
		    	}
		    	setTotal_amount(total_amount);
		    	setStrParam("");
		    	settle_batch.clear();
		    	optionType2 = "DR";
            }
			//all and detailed report, C is the grouping by BizHeads
			else if(optionType != null &&  optionType.trim().equals("C"))
            {
				merchantReportLog = reportModel.getE_TRANSACTIONDetailByDateAndMerchant(beginDate, endDate,
		    			merchant_code, channel, transcode, false);
		    	if(merchantReportLog.size()>0)
		    	{
		    		for(int i=0;i<merchantReportLog.size();i++)
		    		{
		    			E_TRANSACTION et = (E_TRANSACTION)merchantReportLog.get(i);
		            	total_amount += Double.parseDouble(et.getTrans_amount());
		    		}
		    	}
		    	setTotal_amount(total_amount);
		    	settle_batch.clear();
		    	optionType2 = "DR";
            }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*This method is used to display merchant data by date and bank code*/
	public void viewAllReportByBankCode()
	{
		try
		{
			//System.out.println("viewAllReportByBankCode " + getStrParam());
			
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	        String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        E_TRANSACTION e = null;
	        E_TRANSACTION er = null;
	        
	        double t = 0.0;
	        int c = 0;
	        double etz = 0.0;
	        double etzAmt = 0.0;
	        
			allMerchantTransReportLog = reportModel.getE_TRANSACTIONByBankCodeGroupByTransCode(getStrParam(), beginDate, endDate);
			for(int i=0;i<allMerchantTransReportLog.size();i++)
        	{
				etz = 0.0;
        		e = (E_TRANSACTION)allMerchantTransReportLog.get(i);
        		t += Double.parseDouble(e.getTotal_amount());
        		c += Integer.parseInt(e.getTransaction_count());
        		
        		
        		//start of addition
        		
        		//This is used to get calculation grouping by channel
        		/*allMerchantChannelReportLog = reportModel.getE_TRANSACTIONByBankCodeAndTransCodeGroupByChannel(getStrParam(), e.getTrans_code(), beginDate, endDate);
    			for(int tr=0;tr<allMerchantChannelReportLog.size();tr++)
            	{
    				
            		er = (E_TRANSACTION)allMerchantChannelReportLog.get(tr);

            		//This is used to get the commission
            		etz += viewCommissionTotalByBankAndTransCodeAndChannel(beginDate, endDate, er.getChannelid(), e.getTrans_code(), getStrParam());
            	}
    			e.setCommission_value(""+etz);
    			etzAmt += etz;*/
            	
    			//end of addition
        		
        	}
        	setTotal_amount(t);
        	setTotal_count(c);
        	setTotal_etz_amount(etzAmt);
        	
	    	settle_batch.clear();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*This method is used to display merchant data by date,trans_code*/
	public void viewAllReportByTransCode()
	{
		try
		{			
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	        String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        E_TRANSACTION e = null;
	        
	        double t = 0.0;
	        double etz = 0.0;
	        double etzAmt = 0.0;
	        int c = 0;
	        
	        String tran_code = getStrParam();

			allMerchantChannelReportLog = reportModel.getE_TRANSACTIONByTransCodeGroupByChannel(tran_code,beginDate, endDate);
			for(int i=0;i<allMerchantChannelReportLog.size();i++)
        	{
				etz = 0.0;
        		e = (E_TRANSACTION)allMerchantChannelReportLog.get(i);
        		
        		t += Double.parseDouble(e.getTotal_amount());
        		c += Integer.parseInt(e.getTransaction_count());
        		
        		etz = viewCommissionTotalByTransCodeAndChannel(beginDate, endDate, tran_code, e.getChannelid());
        		
        		//this is used to get the merchant count
        		e.setBatch_date(reportModel.getMerchantCodeCommissionCount(tran_code, e.getChannelid(), beginDate, endDate));
        		
        		e.setCommission_value(""+etz);
        		etzAmt += etz; 
        	}
        	setTotal_amount(t);
        	setTotal_count(c);
        	setTotal_etz_amount(etzAmt);
        	
	    	settle_batch.clear();
	    	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*This method is used to display merchant data by date and account code*/
	public void viewReportByAccountCodeGroupByTransCode()
	{
		try
		{
			
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	        String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        E_TRANSACTION e = null;
	        E_TRANSACTION er = null;
	        
	        double t = 0.0;
	        int c = 0;
	        double etz = 0.0;
	        double etzAmt = 0.0;
	        
			allMerchantTransReportLog = reportModel.getE_TRANSACTIONByAccountIDGroupByTransCode(beginDate, endDate, getStrParam());
			for(int i=0;i<allMerchantTransReportLog.size();i++)
        	{
				etz = 0.0;
        		e = (E_TRANSACTION)allMerchantTransReportLog.get(i);
        		t += Double.parseDouble(e.getTotal_amount());
        		c += Integer.parseInt(e.getTransaction_count());
        	}
			setStrParam(getStrParam());
        	setTotal_amount(t);
        	setTotal_count(c);
        	setTotal_etz_amount(etzAmt);
        	
	    	settle_batch.clear();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*This method is used to display merchant data by date, bank and trans_code*/
	public void viewAllReportByBankAndTransCode()
	{
		try
		{
			//System.out.println("viewAllReport_Bank_TransCodes " + getStrParam());
			
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	        String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        E_TRANSACTION e = null;
	        
	        
	        double t = 0.0;
	        double etzAmt = 0.0;
	        double etz = 0.0;
	        int c = 0;
	        
	        String tran_code = getStrParam().substring(0,getStrParam().indexOf(":"));
	        String bank_code = getStrParam().substring(getStrParam().indexOf(":")+1);
	        
	        
			allMerchantChannelReportLog = reportModel.getE_TRANSACTIONByBankCodeAndTransCodeGroupByChannel(bank_code, tran_code,beginDate, endDate);
			for(int i=0;i<allMerchantChannelReportLog.size();i++)
        	{
				etz = 0.0;
        		e = (E_TRANSACTION)allMerchantChannelReportLog.get(i);
        		t += Double.parseDouble(e.getTotal_amount());
        		c += Integer.parseInt(e.getTransaction_count());
        		
        		etz = viewCommissionTotalByBankAndTransCodeAndChannel(beginDate, endDate, e.getChannelid(), tran_code, bank_code);
        		e.setCommission_value(""+etz);
        		
        		etzAmt += etz;
        	}
        	setTotal_amount(t);
        	setTotal_count(c);
        	setTotal_etz_amount(etzAmt);
        	
	    	settle_batch.clear();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	
	/*This method is used to display merchant data by date, trans_code and account_id*/
	public void viewAllReportByTransCodeAndAccountID()
	{
		try
		{			
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	        String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        E_TRANSACTION e = null;
	        
	        double t = 0.0;
	        double etz = 0.0;
	        double etzAmt = 0.0;
	        int c = 0;
	        
	        String tran_code = getStrParam();

	        context = FacesContext.getCurrentInstance();
        	String account_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getAccount_id();
        	
        	
        	boolean s = isSelected();
        	if(s)//this is used to determine if the breakdown should involve the channels
        	{
        		allMerchantChannelReportLog = reportModel.getE_TRANSACTIONByTransCodeAndAccountIDGroupByChannel(tran_code, beginDate, endDate,
				account_id);
				for(int i=0;i<allMerchantChannelReportLog.size();i++)
		    	{
					etz = 0.0;
		    		e = (E_TRANSACTION)allMerchantChannelReportLog.get(i);
		    		
		    		t += Double.parseDouble(e.getTotal_amount());
		    		c += Integer.parseInt(e.getTransaction_count());
		    		
		    		etz = viewCommissionTotalByTransCodeAndChannelAndAccountID(beginDate, endDate, tran_code, e.getChannelid(), account_id);
		    		
	        		//this is used to get the merchant count
	        		e.setBatch_date(reportModel.getMerchantCodeCommissionCountByAccountId(tran_code, e.getChannelid(),
	        				beginDate, endDate, account_id));
	        		
		    		e.setCommission_value(""+etz);
		    		etzAmt += etz;
		    	}
		    	setTotal_amount(t);
		    	setTotal_count(c);
		    	setTotal_etz_amount(etzAmt);
		    	setSelected(false);
        	}
        	else
        	{
        		allMerchantChannelReportLog = reportModel.getE_TRANSACTIONByTransCodeAndAccountIDNoChannelGrouping(tran_code, beginDate, endDate,account_id);
            	for(int i=0;i<allMerchantChannelReportLog.size();i++)
            	{
    				etz = 0.0;
            		e = (E_TRANSACTION)allMerchantChannelReportLog.get(i);
            		
            		t += Double.parseDouble(e.getTotal_amount());
            		c += Integer.parseInt(e.getTransaction_count());
            		
            		e.setCommission_value("0.0");
            	}
            	
            	etz = viewCommissionTotalByTransCodeAndChannelAndAccountID(beginDate, endDate, tran_code, "", account_id);
	    		etzAmt += etz;
            	
            	setTotal_amount(t);
            	setTotal_count(c);
            	setTotal_etz_amount(etzAmt);
            	setSelected(true);
        	}
        	
        	
        	
			
        	
	    	settle_batch.clear();
	    	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*This method is used to display merchant data by date, accountcode and trans_code*/
	public void viewAllReportByAccontCodeAndTransCode()
	{
		try
		{
			//System.out.println("viewAllReport_Bank_TransCodes " + getStrParam());
			
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	        String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        E_TRANSACTION e = null;
	        
	        
	        double t = 0.0;
	        double etzAmt = 0.0;
	        double etz = 0.0;
	        int c = 0;
	        
	        /*String tran_code = getStrParam().substring(0,getStrParam().indexOf(":"));
	        String account_code = getStrParam().substring(getStrParam().indexOf(":")+1);*/
	        String tran_code = getTrans_code();
	        String account_code = getStrParam();

        	
        	allMerchantChannelReportLog = reportModel.getE_TRANSACTIONByTransCodeAndAccountIDGroupByChannel(tran_code, beginDate, endDate,
        			account_code);
			for(int i=0;i<allMerchantChannelReportLog.size();i++)
	    	{
				etz = 0.0;
	    		e = (E_TRANSACTION)allMerchantChannelReportLog.get(i);
	    		
	    		t += Double.parseDouble(e.getTotal_amount());
	    		c += Integer.parseInt(e.getTransaction_count());
	    		
	    		etz = viewCommissionTotalByTransCodeAndChannelAndAccountID(beginDate, endDate, tran_code, e.getChannelid(), account_code);
	    		e.setCommission_value(""+etz);
	    		etzAmt += etz;
	    		
	    		//this is used to get the merchant count
        		e.setBatch_date(reportModel.getMerchantCodeCommissionCountByAccountId(tran_code, e.getChannelid(), beginDate, endDate, account_code));
	    	}
			setStrParam(account_code);
	    	setTotal_amount(t);
	    	setTotal_count(c);
	    	setTotal_etz_amount(etzAmt);
        	
        	
	    	settle_batch.clear();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	
	/*This method is used to display merchant data by date, trans_code and channel*/
	public void viewAllReportByChannelAndTransCode()
	{
		//System.out.println("viewAllReport_Channel_TransCode ");
		
		ReportModel reportModel = new ReportModel();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String beginDate = df.format(getStart_date());
        beginDate = beginDate + " " + getStart_hr() + ":00";
        
        String endDate = df.format(getEnd_date());
        endDate = endDate + " " + getEnd_hr() + ":59";
        
        String channel = getChannel_id();
        String transcode = getTrans_code(); 
       
        
        if(channel_id == null)channel = "";
        if(transcode == null)transcode = "";
		
    	E_TRANSACTION et;
    	String merchant_code = "";
    	String tran_count = "";
    	String total_amount = "";
    	String merchant_catid = "";
    	String merchant_split_type = "";
    	String total_fee = "";
    	double others = 0.0;
    	double etzcomm = 0.0;
    	double grand_etz_total = 0.0;
    	double grand_others_total = 0.0;
    	double grand_total = 0.0;
    	double etzratio = 0.0;
    	int grand_count = 0;
    	double e_catscale_commision_value = 0.0;
    	E_MERCHANT_SPLIT em = null;
    	E_STANDARD_SPLIT est = null;

    	try
    	{
    		allMerchantReportLog = reportModel.getE_TRANSACTIONByDateAndTransCodeAndChannel (beginDate, endDate, channel, transcode);
    		System.out.println("starting of the calculations :  begin date " + beginDate + " end date " + endDate + " transcode " +  transcode  + " channel "  + channel);
        	for(int i=0;i<allMerchantReportLog.size();i++)
        	{
        		//System.out.println("size of array " + allMerchantReportLog.size() + " count " +  i);
        		merchant_code = "";
        		et = (E_TRANSACTION)allMerchantReportLog.get(i);
        		
        		tran_count = et.getTransaction_count();
        		total_amount = et.getTotal_amount();
        		total_fee = et.getTotal_fee();
        		merchant_code = et.getMerchat_code();
        		merchant_catid = et.getMerchant_cat_id();
        		merchant_split_type = et.getMerchant_split_type();//1: special split , 0 commission split
        		
        		em = null;
        		est = null;
				others = 0.0;
				etzcomm = 0.0;
				e_catscale_commision_value = 0.0;
				etzratio = 0.0;
        		
				//System.out.println("merchant_split_type " + merchant_split_type);
				
        		if(!transcode.equalsIgnoreCase("P"))//other transaction codes
        		{
					if(total_fee == null || total_fee.equals("")|| total_fee.equals("0.00") ||total_fee.equals("null"))
					{
						total_fee = "0";
					}
					else
					{
						
						merchantSplitLog = reportModel.getE_STANDARD_SPLIT(channel, transcode, total_fee, merchant_code, beginDate, endDate); 

	        			est = null;
	        			others = 0.0;
						etzcomm = 0.0;
						etzratio = 0.0;
						
						
						for(int m=0;m<merchantSplitLog.size();m++)
						{
							est = (E_STANDARD_SPLIT)merchantSplitLog.get(m);						
							if(est.getOwner_card().equals("0690019914") || est.getOwner_card().equals("0690000000") || est.getOwner_card().equals("0570019947") || est.getOwner_card().endsWith("0000000") || est.getOwner_card().equals("0570019902") || est.getOwner_card().equals("0443241197") || est.getOwner_card().equals("7850010003"))//etranzact commission owner cards
							{
								etzratio += Double.parseDouble(est.getEtz_ratio());
								etzcomm += Double.parseDouble(est.getPart_fee());
								grand_etz_total += Double.parseDouble(est.getPart_fee());
							}
							else
							{
								others += Double.parseDouble(""+est.getPart_fee());
								grand_others_total += Double.parseDouble(""+est.getPart_fee());
							}
						}
					}
					et.setMerchant_split_type("STANDARD SPLIT");
        		}
    			else//payment trans code
    			{
    				if(merchant_split_type == null || merchant_catid == null || merchant_split_type.trim().equals("") || merchant_catid.trim().equals("")|| merchant_split_type.trim().equals("null") || merchant_catid.trim().equals("null"));
            		else
            		{   
            			switch(Integer.parseInt(merchant_split_type))
            			{
            				case 0:
            					
            					em = null;
            					others = 0.0;
            					etzcomm = 0.0;
            					e_catscale_commision_value = 0.0;
            					etzratio = 0.0;
            					
            					merchantSplitLog = reportModel.getE_MERCHANT_COMMISSION_SPLIT(merchant_code, merchant_catid , tran_count, total_amount, beginDate, endDate, channel, transcode);
            					for(int m=0;m<merchantSplitLog.size();m++)
            					{
            						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);
            						e_catscale_commision_value = Double.parseDouble(em.getCommission_value());
            						
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197")|| em.getSplit_card().equals("7850010003"))//etranzact commission split cards
            						{
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
            						}
            						else
            						{
            							others += Double.parseDouble(""+em.getSvalue());
            							grand_others_total += Double.parseDouble(""+em.getSvalue());
            						}
            					}
            					et.setMerchant_split_type("COMMISSION SPLIT");
            					if(merchantSplitLog.size()>0);
            					else//try the merchant_ext_split table
            					{
            						merchantSplitLog = reportModel.getE_MERCHANT_EXT_SPLIT(merchant_code, tran_count, total_amount);
                					
                					for(int m=0;m<merchantSplitLog.size();m++)
                					{
                						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);        						
                						
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
                						
                					}
                					et.setMerchant_split_type("COMMISSION EXT SPLIT");
            					}
            					
            					break;
            				
            				case 1:
            					
            					em = null;
            					others = 0.0;
            					etzcomm = 0.0;
            					etzratio = 0.0;
            					
            					merchantSplitLog = reportModel.getE_MERCHANT_SPECIAL_SPLIT(merchant_code, tran_count, total_amount);
            					
            					for(int m=0;m<merchantSplitLog.size();m++)
            					{
            						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);        						
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197") || em.getSplit_card().equals("7850010003"))//etranzact commission split cards
            						{
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
            						}
            						else
            						{
            							others += Double.parseDouble(""+em.getSvalue());
            							grand_others_total += Double.parseDouble(""+em.getSvalue());
            						}
            					}
            					et.setMerchant_split_type("SPECIAL SPLIT");
            					
            					if(merchantSplitLog.size()>0);
            					else//try the merchant_ext_split table
            					{
            						merchantSplitLog = reportModel.getE_MERCHANT_EXT_SPLIT(merchant_code, tran_count, total_amount);
                					
                					for(int m=0;m<merchantSplitLog.size();m++)
                					{
                						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);        						
                						
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
                						
                					}
                					et.setMerchant_split_type("SPECIAL EXT SPLIT");
            					}
            					break;
            				
            				default:
            					
            				break;
            			}
            		}
    			}
        		
        		//checking if there is no etz_comm for the standard split
        		if(etzcomm == 0.0 && !transcode.equalsIgnoreCase("P"))
        		{
        			et.setEtzRatio("Balance To eTranzact");
        			etzcomm = Double.parseDouble(total_fee) - (others);
        			//others = others * Double.parseDouble(tran_count);
        			grand_etz_total +=  etzcomm; 
        		}
        		
        		//checking if there is no etz_comm for the payment merchant commission split
        		if(etzcomm == 0.0 && merchant_split_type.equalsIgnoreCase("0"))
        		{
        			et.setEtzRatio("Balance To eTranzact");
        			etzcomm = e_catscale_commision_value - others;
        			grand_etz_total +=  etzcomm; 
        		}
        		
        		
        		if(!transcode.equalsIgnoreCase("P"))
        		{
        			et.setEtzRatio(""+etzratio);
        			et.setEtranzactComm(""+etzcomm);
        			et.setOtherComm(""+ others);
            		et.setCommission_value(total_fee);
            		
            		grand_count += Integer.parseInt(tran_count);
            		grand_total += Double.parseDouble(total_amount);
        		}
        		else
        		{
	        		if(merchant_split_type.equals("0"))//commission split
	        		{
	        			et.setEtzRatio(""+etzratio);
	        			et.setEtranzactComm(""+etzcomm);
	        			et.setOtherComm(""+ others);
	            		et.setCommission_value(""+e_catscale_commision_value);
	            		
	            		grand_count += Integer.parseInt(tran_count);
	            		grand_total += Double.parseDouble(total_amount);
	        		}
	        		else if(merchant_split_type.equals("1"))//special split
	        		{
	        			et.setEtzRatio(""+etzratio);
	        			et.setEtranzactComm(""+etzcomm);
	            		et.setOtherComm(""+others);
	            		et.setCommission_value("Determined By Transaction Count");
	            		
	            		grand_count += Integer.parseInt(tran_count);
	            		grand_total += Double.parseDouble(total_amount);
	        		}
	        		else
	        		{
	        			grand_count += Integer.parseInt(tran_count);
	            		grand_total += Double.parseDouble(total_amount);
	        		}
        		}
        	}
        	
        	System.out.println("ending of the calculations :  ..................................");
        	settle_batch.clear();
        	setTotal_amount(grand_total);
        	setTotal_count(grand_count);
        	setTotal_etz_amount(grand_etz_total);
        	setTotal_other_amount(grand_others_total);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
	}
	

	
	
	
	
	
	/*This method is used to display merchant data by date, bank, trans_code and channel*/
	public void viewAllReportByBankAndTransCodeAndChannel()
	{
		//System.out.println("viewAllReport_Channel_TransCode ");
		
		ReportModel reportModel = new ReportModel();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String beginDate = df.format(getStart_date());
        beginDate = beginDate + " " + getStart_hr() + ":00";
        
        String endDate = df.format(getEnd_date());
        endDate = endDate + " " + getEnd_hr() + ":59";
        
        String channel = getChannel_id();
        String transcode = getTrans_code(); 
        String bank_code = getBank_code();
        
        
        if(channel_id == null)channel = "";
        if(transcode == null)transcode = "";
		
    	E_TRANSACTION et;
    	String merchant_code = "";
    	String tran_count = "";
    	String total_amount = "";
    	String merchant_catid = "";
    	String merchant_split_type = "";
    	String total_fee = "";
    	double others = 0.0;
    	double etzcomm = 0.0;
    	double grand_etz_total = 0.0;
    	double grand_others_total = 0.0;
    	double grand_total = 0.0;
    	double etzratio = 0.0;
    	int grand_count = 0;
    	double e_catscale_commision_value = 0.0;
    	E_MERCHANT_SPLIT em = null;
    	E_STANDARD_SPLIT est = null;

    	try
    	{
    		allMerchantReportLog = reportModel.getE_TRANSACTIONByDateAndBankCodeAndTransCodeAndChannel (beginDate, endDate, channel, transcode, bank_code);
    		System.out.println("starting of the calculations :  begin date " + beginDate + " end date " + endDate + " transcode " +  transcode  + " channel "  + channel + " bank " + bank_code);
        	for(int i=0;i<allMerchantReportLog.size();i++)
        	{
        		//System.out.println("size of array " + allMerchantReportLog.size() + " count " +  i);
        		merchant_code = "";
        		et = (E_TRANSACTION)allMerchantReportLog.get(i);
        		
        		tran_count = et.getTransaction_count();
        		total_amount = et.getTotal_amount();
        		total_fee = et.getTotal_fee();
        		merchant_code = et.getMerchat_code();
        		merchant_catid = et.getMerchant_cat_id();
        		merchant_split_type = et.getMerchant_split_type();//1: special split , 0 commission split
        		
        		
        		em = null;
        		est = null;
				others = 0.0;
				etzcomm = 0.0;
				e_catscale_commision_value = 0.0;
				etzratio = 0.0;
        		
        		if(!transcode.equalsIgnoreCase("P"))//other trans code
        		{
					if(total_fee == null || total_fee.equals("")|| total_fee.equals("0.00") ||total_fee.equals("null"))
					{
						total_fee = "0";
					}
					else
					{
						merchantSplitLog = reportModel.getE_STANDARD_SPLIT_Bank(channel, transcode, total_fee, merchant_code, beginDate, endDate, bank_code); 
	        			
	        			est = null;
	        			others = 0.0;
						etzcomm = 0.0;
						etzratio = 0.0;
						
						
						for(int m=0;m<merchantSplitLog.size();m++)
						{
							est = (E_STANDARD_SPLIT)merchantSplitLog.get(m);	
							if(est.getOwner_card().equals("0690019914") || est.getOwner_card().equals("0690000000") || est.getOwner_card().equals("0570019947") || est.getOwner_card().endsWith("0000000") || est.getOwner_card().equals("0570019902") || est.getOwner_card().equals("0443241197") || est.getOwner_card().equals("7850010003") )//etranzact commission owner cards
							{
								//et.setEtzRatio(est.getEtz_ratio());
								etzratio += Double.parseDouble(est.getEtz_ratio());
								etzcomm += Double.parseDouble(est.getPart_fee());
								grand_etz_total += Double.parseDouble(est.getPart_fee());
							}
							else
							{
								others += Double.parseDouble(""+est.getPart_fee());
								grand_others_total += Double.parseDouble(""+est.getPart_fee());
							}
						}
					}
					et.setMerchant_split_type("STANDARD SPLIT");
        		}
    			else//p trans code
    			{
    				if(merchant_split_type == null || merchant_catid == null || merchant_split_type.trim().equals("") || merchant_catid.trim().equals("")|| merchant_split_type.trim().equals("null") || merchant_catid.trim().equals("null"));
            		else
            		{   
            			switch(Integer.parseInt(merchant_split_type))
            			{
            				case 0:
            					
            					em = null;
            					others = 0.0;
            					etzcomm = 0.0;
            					e_catscale_commision_value = 0.0;
            					etzratio = 0.0;
            					
            					merchantSplitLog = reportModel.getE_MERCHANT_COMMISSION_SPLIT_Bank(merchant_code, merchant_catid , tran_count, total_amount, beginDate, endDate, channel, transcode, bank_code);
            					for(int m=0;m<merchantSplitLog.size();m++)
            					{
            						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);
            						e_catscale_commision_value = Double.parseDouble(em.getCommission_value());
            						
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197") || em.getSplit_card().equals("7850010003"))//etranzact commission split cards
            						{
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
            						}
            						else
            						{
            							others += Double.parseDouble(""+em.getSvalue());
            							grand_others_total += Double.parseDouble(""+em.getSvalue());
            						}
            					}
            					et.setMerchant_split_type("COMMISSION SPLIT");
            					break;
            				
            				case 1:
            					
            					em = null;
            					others = 0.0;
            					etzcomm = 0.0;
            					etzratio = 0.0;
            					
            					merchantSplitLog = reportModel.getE_MERCHANT_SPECIAL_SPLIT(merchant_code, tran_count, total_amount);
            					
            					for(int m=0;m<merchantSplitLog.size();m++)
            					{
            						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);   
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197") || em.getSplit_card().equals("7850010003") )//etranzact commission split cards
            						{
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
            						}
            						else
            						{
            							others += Double.parseDouble(""+em.getSvalue());
            							grand_others_total += Double.parseDouble(""+em.getSvalue());
            						}
            					}
            					et.setMerchant_split_type("SPECIAL SPLIT");
            					break;
            				
            				default:
            					
            				break;
            			}
            		}
    			}
        		
        		//checking if there is not etz_comm for the standard split
        		if(etzcomm == 0.0 && !transcode.equalsIgnoreCase("P"))
        		{
        			et.setEtzRatio("Balance To eTranzact");
        			etzcomm = Double.parseDouble(total_fee) - (others);
        			//others = others * Double.parseDouble(tran_count);
        			grand_etz_total +=  etzcomm; 
        		}
        		
        		//checking if there is not etz_comm for the payment merchant commission split
        		if(etzcomm == 0.0 && merchant_split_type.equalsIgnoreCase("0"))
        		{
        			et.setEtzRatio("Balance To eTranzact");
        			etzcomm = e_catscale_commision_value - others;
        			grand_etz_total +=  etzcomm; 
        		}
        		
        		
        		if(!transcode.equalsIgnoreCase("P"))
        		{
        			et.setEtzRatio(""+etzratio);
        			et.setEtranzactComm(""+etzcomm);
        			et.setOtherComm(""+ others);
            		et.setCommission_value(total_fee);
            		
            		grand_count += Integer.parseInt(tran_count);
            		grand_total += Double.parseDouble(total_amount);
        		}
        		else
        		{
	        		if(merchant_split_type.equals("0"))
	        		{
	        			et.setEtzRatio(""+etzratio);
	        			et.setEtranzactComm(""+etzcomm);
	        			et.setOtherComm(""+ others);
	            		et.setCommission_value(""+e_catscale_commision_value);
	            		
	            		grand_count += Integer.parseInt(tran_count);
	            		grand_total += Double.parseDouble(total_amount);
	        		}
	        		else if(merchant_split_type.equals("1"))
	        		{
	        			et.setEtzRatio(""+etzratio);
	        			et.setEtranzactComm(""+etzcomm);
	            		et.setOtherComm(""+others);
	            		et.setCommission_value("Determined By Transaction Count");
	            		
	            		grand_count += Integer.parseInt(tran_count);
	            		grand_total += Double.parseDouble(total_amount);
	        		}
	        		else
	        		{
	        			grand_count += Integer.parseInt(tran_count);
	            		grand_total += Double.parseDouble(total_amount);
	        		}
        		}
        	}
        	System.out.println("ending of the calculations :  ..................................");
        	settle_batch.clear();
        	setTotal_amount(grand_total);
        	setTotal_count(grand_count);
        	setTotal_etz_amount(grand_etz_total);
        	setTotal_other_amount(grand_others_total);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
	}
	
	
	
	/*This method is used to display merchant data by date, trans_code and channel and account_id*/
	public void viewAllReportByChannelAndTransCodeAndAccountID()
	{
		//System.out.println("viewAllReport_Channel_TransCode ");
		
		ReportModel reportModel = new ReportModel();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String beginDate = df.format(getStart_date());
        beginDate = beginDate + " " + getStart_hr() + ":00";
        
        String endDate = df.format(getEnd_date());
        endDate = endDate + " " + getEnd_hr() + ":59";
        
        String channel = getChannel_id();
        String transcode = getTrans_code(); 
       
        
        if(channel_id == null)channel = "";
        if(transcode == null)transcode = "";
		
    	E_TRANSACTION et;
    	String merchant_code = "";
    	String tran_count = "";
    	String total_amount = "";
    	String merchant_catid = "";
    	String merchant_split_type = "";
    	String total_fee = "";
    	double others = 0.0;
    	double etzcomm = 0.0;
    	double grand_etz_total = 0.0;
    	double grand_others_total = 0.0;
    	double grand_total = 0.0;
    	double etzratio = 0.0;
    	int grand_count = 0;
    	double e_catscale_commision_value = 0.0;
    	E_MERCHANT_SPLIT em = null;
    	E_STANDARD_SPLIT est = null;

    	try
    	{
    		
    		context = FacesContext.getCurrentInstance();
        	String account_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getAccount_id();
        	
    		
    		allMerchantReportLog = reportModel.getE_TRANSACTIONByDateAndTransCodeAndChannelAndAccountID (beginDate, endDate, channel, transcode, account_id);
    		System.out.println("starting of the calculations :  begin date " + beginDate + " end date " + endDate + " transcode " +  transcode  + " channel "  + channel);
        	for(int i=0;i<allMerchantReportLog.size();i++)
        	{
        		//System.out.println("size of array " + allMerchantReportLog.size() + " count " +  i);
        		merchant_code = "";
        		et = (E_TRANSACTION)allMerchantReportLog.get(i);
        		
        		tran_count = et.getTransaction_count();
        		total_amount = et.getTotal_amount();
        		total_fee = et.getTotal_fee();
        		merchant_code = et.getMerchat_code();
        		merchant_catid = et.getMerchant_cat_id();
        		merchant_split_type = et.getMerchant_split_type();//1: special split , 0 commission split
        		
        		em = null;
        		est = null;
				others = 0.0;
				etzcomm = 0.0;
				e_catscale_commision_value = 0.0;
				etzratio = 0.0;
        		
				//System.out.println("merchant_split_type " + merchant_split_type);
				
        		if(!transcode.equalsIgnoreCase("P"))//other transaction codes
        		{
					if(total_fee == null || total_fee.equals("")|| total_fee.equals("0.00") ||total_fee.equals("null"))
					{
						total_fee = "0";
					}
					else
					{
						
						merchantSplitLog = reportModel.getE_STANDARD_SPLIT(channel, transcode, total_fee, merchant_code, beginDate, endDate); 

	        			est = null;
	        			others = 0.0;
						etzcomm = 0.0;
						etzratio = 0.0;
						
						
						for(int m=0;m<merchantSplitLog.size();m++)
						{
							est = (E_STANDARD_SPLIT)merchantSplitLog.get(m);						
							if(est.getOwner_card().equals("0690019914") || est.getOwner_card().equals("0690000000") || est.getOwner_card().equals("0570019947") || est.getOwner_card().endsWith("0000000") || est.getOwner_card().equals("0570019902") || est.getOwner_card().equals("0443241197") || est.getOwner_card().equals("7850010003") )//etranzact commission owner cards
							{
								etzratio += Double.parseDouble(est.getEtz_ratio());
								etzcomm += Double.parseDouble(est.getPart_fee());
								grand_etz_total += Double.parseDouble(est.getPart_fee());
							}
							else
							{
								others += Double.parseDouble(""+est.getPart_fee());
								grand_others_total += Double.parseDouble(""+est.getPart_fee());
							}
						}
					}
					et.setMerchant_split_type("STANDARD SPLIT");
        		}
    			else//payment trans code
    			{
    				if(merchant_split_type == null || merchant_catid == null || merchant_split_type.trim().equals("") || merchant_catid.trim().equals("")|| merchant_split_type.trim().equals("null") || merchant_catid.trim().equals("null"));
            		else
            		{   
            			switch(Integer.parseInt(merchant_split_type))
            			{
            				case 0:
            					
            					em = null;
            					others = 0.0;
            					etzcomm = 0.0;
            					e_catscale_commision_value = 0.0;
            					etzratio = 0.0;
            					
            					merchantSplitLog = reportModel.getE_MERCHANT_COMMISSION_SPLIT(merchant_code, merchant_catid , tran_count, total_amount, beginDate, endDate, channel, transcode);
            					for(int m=0;m<merchantSplitLog.size();m++)
            					{
            						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);
            						e_catscale_commision_value = Double.parseDouble(em.getCommission_value());
            						
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197") || em.getSplit_card().equals("7850010003") )//etranzact commission split cards
            						{
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
            						}
            						else
            						{
            							others += Double.parseDouble(""+em.getSvalue());
            							grand_others_total += Double.parseDouble(""+em.getSvalue());
            						}
            					}
            					et.setMerchant_split_type("COMMISSION SPLIT");
            					
            					if(merchantSplitLog.size()>0);
            					else//try the merchant_ext_split table
            					{
            						merchantSplitLog = reportModel.getE_MERCHANT_EXT_SPLIT(merchant_code, tran_count, total_amount);
                					
                					for(int m=0;m<merchantSplitLog.size();m++)
                					{
                						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);        						
                						
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
                						
                					}
                					et.setMerchant_split_type("COMMISSION EXT SPLIT");
            					}
            					break;
            				
            				case 1:
            					
            					em = null;
            					others = 0.0;
            					etzcomm = 0.0;
            					etzratio = 0.0;
            					
            					merchantSplitLog = reportModel.getE_MERCHANT_SPECIAL_SPLIT(merchant_code, tran_count, total_amount);
            					
            					for(int m=0;m<merchantSplitLog.size();m++)
            					{
            						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);        						
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197") || em.getSplit_card().equals("7850010003") )//etranzact commission split cards
            						{
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
            						}
            						else
            						{
            							others += Double.parseDouble(""+em.getSvalue());
            							grand_others_total += Double.parseDouble(""+em.getSvalue());
            						}
            					}
            					et.setMerchant_split_type("SPECIAL SPLIT");
            					
            					if(merchantSplitLog.size()>0);
            					else//try the merchant_ext_split table
            					{
            						merchantSplitLog = reportModel.getE_MERCHANT_EXT_SPLIT(merchant_code, tran_count, total_amount);
                					
                					for(int m=0;m<merchantSplitLog.size();m++)
                					{
                						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);        						
                						
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
                						
                					}
                					et.setMerchant_split_type("SPECIAL EXT SPLIT");
            					}
            					
            					break;
            				
            				default:
            					
            				break;
            			}
            		}
    			}
        		
        		//checking if there is no etz_comm for the standard split
        		if(etzcomm == 0.0 && !transcode.equalsIgnoreCase("P"))
        		{
        			et.setEtzRatio("Balance To eTranzact");
        			etzcomm = Double.parseDouble(total_fee) - (others);
        			//others = others * Double.parseDouble(tran_count);
        			grand_etz_total +=  etzcomm; 
        		}
        		
        		//checking if there is no etz_comm for the payment merchant commission split
        		if(etzcomm == 0.0 && merchant_split_type.equalsIgnoreCase("0"))
        		{
        			et.setEtzRatio("Balance To eTranzact");
        			etzcomm = e_catscale_commision_value - others;
        			grand_etz_total +=  etzcomm; 
        		}
        		
        		
        		if(!transcode.equalsIgnoreCase("P"))
        		{
        			et.setEtzRatio(""+etzratio);
        			et.setEtranzactComm(""+etzcomm);
        			et.setOtherComm(""+ others);
            		et.setCommission_value(total_fee);
            		
            		grand_count += Integer.parseInt(tran_count);
            		grand_total += Double.parseDouble(total_amount);
        		}
        		else
        		{
	        		if(merchant_split_type.equals("0"))//commission split
	        		{
	        			et.setEtzRatio(""+etzratio);
	        			et.setEtranzactComm(""+etzcomm);
	        			et.setOtherComm(""+ others);
	            		et.setCommission_value(""+e_catscale_commision_value);
	            		
	            		grand_count += Integer.parseInt(tran_count);
	            		grand_total += Double.parseDouble(total_amount);
	        		}
	        		else if(merchant_split_type.equals("1"))//special split
	        		{
	        			et.setEtzRatio(""+etzratio);
	        			et.setEtranzactComm(""+etzcomm);
	            		et.setOtherComm(""+others);
	            		et.setCommission_value("Determined By Transaction Count");
	            		
	            		grand_count += Integer.parseInt(tran_count);
	            		grand_total += Double.parseDouble(total_amount);
	        		}
	        		else
	        		{
	        			grand_count += Integer.parseInt(tran_count);
	            		grand_total += Double.parseDouble(total_amount);
	        		}
        		}
        	}
        	
        	System.out.println("ending of the calculations :  ..................................");
        	settle_batch.clear();
        	setTotal_amount(grand_total);
        	setTotal_count(grand_count);
        	setTotal_etz_amount(grand_etz_total);
        	setTotal_other_amount(grand_others_total);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
	}
	

	/*This method is used to display merchant data by date, bank, trans_code and channel*/
	public void viewAllReportByAccountCodeAndTransCodeAndChannel()
	{
		//System.out.println("viewAllReport_Channel_TransCode ");
		
		ReportModel reportModel = new ReportModel();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String beginDate = df.format(getStart_date());
        beginDate = beginDate + " " + getStart_hr() + ":00";
        
        String endDate = df.format(getEnd_date());
        endDate = endDate + " " + getEnd_hr() + ":59";
        
        String channel = getChannel_id();
        String transcode = getTrans_code(); 
        String account_code = getStrParam();
        
        
        if(channel_id == null)channel = "";
        if(transcode == null)transcode = "";
		
    	E_TRANSACTION et;
    	String merchant_code = "";
    	String tran_count = "";
    	String total_amount = "";
    	String merchant_catid = "";
    	String merchant_split_type = "";
    	String total_fee = "";
    	double others = 0.0;
    	double etzcomm = 0.0;
    	double grand_etz_total = 0.0;
    	double grand_others_total = 0.0;
    	double grand_total = 0.0;
    	double etzratio = 0.0;
    	int grand_count = 0;
    	double e_catscale_commision_value = 0.0;
    	E_MERCHANT_SPLIT em = null;
    	E_STANDARD_SPLIT est = null;

    	try
    	{
    		
    		//context = FacesContext.getCurrentInstance();
        	//String account_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getAccount_id();
        	
    		
    		allMerchantReportLog = reportModel.getE_TRANSACTIONByDateAndTransCodeAndChannelAndAccountID (beginDate, endDate, channel, transcode, account_code);
    		System.out.println("starting of the calculations :  begin date " + beginDate + " end date " + endDate + " transcode " +  transcode  + " channel "  + channel);
        	for(int i=0;i<allMerchantReportLog.size();i++)
        	{
        		//System.out.println("size of array " + allMerchantReportLog.size() + " count " +  i);
        		merchant_code = "";
        		et = (E_TRANSACTION)allMerchantReportLog.get(i);
        		
        		tran_count = et.getTransaction_count();
        		total_amount = et.getTotal_amount();
        		total_fee = et.getTotal_fee();
        		merchant_code = et.getMerchat_code();
        		merchant_catid = et.getMerchant_cat_id();
        		merchant_split_type = et.getMerchant_split_type();//1: special split , 0 commission split
        		
        		em = null;
        		est = null;
				others = 0.0;
				etzcomm = 0.0;
				e_catscale_commision_value = 0.0;
				etzratio = 0.0;
        		
				//System.out.println("merchant_split_type " + merchant_split_type);
				
        		if(!transcode.equalsIgnoreCase("P"))//other transaction codes
        		{
					if(total_fee == null || total_fee.equals("")|| total_fee.equals("0.00") ||total_fee.equals("null"))
					{
						total_fee = "0";
					}
					else
					{
						
						merchantSplitLog = reportModel.getE_STANDARD_SPLIT(channel, transcode, total_fee, merchant_code, beginDate, endDate); 

	        			est = null;
	        			others = 0.0;
						etzcomm = 0.0;
						etzratio = 0.0;
						
						
						for(int m=0;m<merchantSplitLog.size();m++)
						{
							est = (E_STANDARD_SPLIT)merchantSplitLog.get(m);						
							if(est.getOwner_card().equals("0690019914") || est.getOwner_card().equals("0690000000") || est.getOwner_card().equals("0570019947") || est.getOwner_card().endsWith("0000000") || est.getOwner_card().equals("0570019902") || est.getOwner_card().equals("0443241197") || est.getOwner_card().equals("7850010003") )//etranzact commission owner cards
							{
								etzratio += Double.parseDouble(est.getEtz_ratio());
								etzcomm += Double.parseDouble(est.getPart_fee());
								grand_etz_total += Double.parseDouble(est.getPart_fee());
							}
							else
							{
								others += Double.parseDouble(""+est.getPart_fee());
								grand_others_total += Double.parseDouble(""+est.getPart_fee());
							}
						}
					}
					et.setMerchant_split_type("STANDARD SPLIT");
        		}
    			else//payment trans code
    			{
    				if(merchant_split_type == null || merchant_catid == null || merchant_split_type.trim().equals("") || merchant_catid.trim().equals("")|| merchant_split_type.trim().equals("null") || merchant_catid.trim().equals("null"));
            		else
            		{   
            			switch(Integer.parseInt(merchant_split_type))
            			{
            				case 0:
            					
            					em = null;
            					others = 0.0;
            					etzcomm = 0.0;
            					e_catscale_commision_value = 0.0;
            					etzratio = 0.0;
            					
            					merchantSplitLog = reportModel.getE_MERCHANT_COMMISSION_SPLIT(merchant_code, merchant_catid , tran_count, total_amount, beginDate, endDate, channel, transcode);
            					for(int m=0;m<merchantSplitLog.size();m++)
            					{
            						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);
            						e_catscale_commision_value = Double.parseDouble(em.getCommission_value());
            						
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197") || em.getSplit_card().equals("7850010003") )//etranzact commission split cards
            						{
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
            						}
            						else
            						{
            							others += Double.parseDouble(""+em.getSvalue());
            							grand_others_total += Double.parseDouble(""+em.getSvalue());
            						}
            					}
            					et.setMerchant_split_type("COMMISSION SPLIT");
            					
            					if(merchantSplitLog.size()>0);
            					else//try the merchant_ext_split table
            					{
            						merchantSplitLog = reportModel.getE_MERCHANT_EXT_SPLIT(merchant_code, tran_count, total_amount);
                					
                					for(int m=0;m<merchantSplitLog.size();m++)
                					{
                						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);        						
                						
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
                						
                					}
                					et.setMerchant_split_type("COMMISSION EXT SPLIT");
            					}
            					
            					break;
            				
            				case 1:
            					
            					em = null;
            					others = 0.0;
            					etzcomm = 0.0;
            					etzratio = 0.0;
            					
            					merchantSplitLog = reportModel.getE_MERCHANT_SPECIAL_SPLIT(merchant_code, tran_count, total_amount);
            					
            					for(int m=0;m<merchantSplitLog.size();m++)
            					{
            						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);        						
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197") || em.getSplit_card().equals("7850010003") )//etranzact commission split cards
            						{
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
            						}
            						else
            						{
            							others += Double.parseDouble(""+em.getSvalue());
            							grand_others_total += Double.parseDouble(""+em.getSvalue());
            						}
            					}
            					et.setMerchant_split_type("SPECIAL SPLIT");
            					
            					if(merchantSplitLog.size()>0);
            					else//try the merchant_ext_split table
            					{
            						merchantSplitLog = reportModel.getE_MERCHANT_EXT_SPLIT(merchant_code, tran_count, total_amount);
                					
                					for(int m=0;m<merchantSplitLog.size();m++)
                					{
                						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);        						
                						
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
                						
                					}
                					et.setMerchant_split_type("SPECIAL EXT SPLIT");
            					}
            					
            					break;
            				
            				default:
            					
            				break;
            			}
            		}
    			}
        		
        		//checking if there is no etz_comm for the standard split
        		if(etzcomm == 0.0 && !transcode.equalsIgnoreCase("P"))
        		{
        			et.setEtzRatio("Balance To eTranzact");
        			etzcomm = Double.parseDouble(total_fee) - (others);
        			//others = others * Double.parseDouble(tran_count);
        			grand_etz_total +=  etzcomm; 
        		}
        		
        		//checking if there is no etz_comm for the payment merchant commission split
        		if(etzcomm == 0.0 && merchant_split_type.equalsIgnoreCase("0"))
        		{
        			et.setEtzRatio("Balance To eTranzact");
        			etzcomm = e_catscale_commision_value - others;
        			grand_etz_total +=  etzcomm; 
        		}
        		
        		
        		if(!transcode.equalsIgnoreCase("P"))
        		{
        			et.setEtzRatio(""+etzratio);
        			et.setEtranzactComm(""+etzcomm);
        			et.setOtherComm(""+ others);
            		et.setCommission_value(total_fee);
            		
            		grand_count += Integer.parseInt(tran_count);
            		grand_total += Double.parseDouble(total_amount);
        		}
        		else
        		{
	        		if(merchant_split_type.equals("0"))//commission split
	        		{
	        			et.setEtzRatio(""+etzratio);
	        			et.setEtranzactComm(""+etzcomm);
	        			et.setOtherComm(""+ others);
	            		et.setCommission_value(""+e_catscale_commision_value);
	            		
	            		grand_count += Integer.parseInt(tran_count);
	            		grand_total += Double.parseDouble(total_amount);
	        		}
	        		else if(merchant_split_type.equals("1"))//special split
	        		{
	        			et.setEtzRatio(""+etzratio);
	        			et.setEtranzactComm(""+etzcomm);
	            		et.setOtherComm(""+others);
	            		et.setCommission_value("Determined By Transaction Count");
	            		
	            		grand_count += Integer.parseInt(tran_count);
	            		grand_total += Double.parseDouble(total_amount);
	        		}
	        		else
	        		{
	        			grand_count += Integer.parseInt(tran_count);
	            		grand_total += Double.parseDouble(total_amount);
	        		}
        		}
        	}
        	
        	System.out.println("ending of the calculations :  ..................................");
        	settle_batch.clear();
        	setTotal_amount(grand_total);
        	setTotal_count(grand_count);
        	setTotal_etz_amount(grand_etz_total);
        	setTotal_other_amount(grand_others_total);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
	}
	
	
	/*This method is used to display merchant data by date, trans_code and channel, getting the commission for the display without clicking on channel*/
	public double viewCommissionTotalByTransCodeAndChannel(String beginDate, String endDate, String transcode, String channel)
	{
		
		ReportModel reportModel = new ReportModel();
		
		
    	E_TRANSACTION et;
    	String merchant_code = "";
    	String tran_count = "";
    	String total_amount = "";
    	String merchant_catid = "";
    	String merchant_split_type = "";
    	String total_fee = "";
    	double others = 0.0;
    	double etzcomm = 0.0;
    	double grand_etz_total = 0.0;
    	double grand_others_total = 0.0;
    	double grand_total = 0.0;
    	double etzratio = 0.0;
    	int grand_count = 0;
    	double e_catscale_commision_value = 0.0;
    	E_MERCHANT_SPLIT em = null;
    	E_STANDARD_SPLIT est = null;
    	double message = 0.0;

    	try
    	{
    		allMerchantReportLog = reportModel.getE_TRANSACTIONByDateAndTransCodeAndChannel(beginDate, endDate, channel, transcode);
    		System.out.println("starting of the calculations :  begin date " + beginDate + " end date " + endDate + " transcode " +  transcode + " channel " + channel );
        	for(int i=0;i<allMerchantReportLog.size();i++)
        	{
        		//System.out.println("size of array " + allMerchantReportLog.size() + " count " +  i);
        		merchant_code = "";
        		et = (E_TRANSACTION)allMerchantReportLog.get(i);
        		
        		tran_count = et.getTransaction_count();
        		total_amount = et.getTotal_amount();
        		total_fee = et.getTotal_fee();
        		merchant_code = et.getMerchat_code();
        		merchant_catid = et.getMerchant_cat_id();
        		merchant_split_type = et.getMerchant_split_type();//1: special split , 0 commission split
        		
        		em = null;
        		est = null;
				others = 0.0;
				etzcomm = 0.0;
				e_catscale_commision_value = 0.0;
				etzratio = 0.0;
        		
				//System.out.println("merchant_split_type " + merchant_split_type);
				
        		if(!transcode.equalsIgnoreCase("P"))//other transaction codes
        		{
					if(total_fee == null || total_fee.equals("")|| total_fee.equals("0.00") ||total_fee.equals("null"))
					{
						total_fee = "0";
					}
					else
					{
						
						merchantSplitLog = reportModel.getE_STANDARD_SPLIT(channel, transcode, total_fee, merchant_code, beginDate, endDate); 

	        			est = null;
	        			others = 0.0;
						etzcomm = 0.0;
						etzratio = 0.0;
						
						
						for(int m=0;m<merchantSplitLog.size();m++)
						{
							est = (E_STANDARD_SPLIT)merchantSplitLog.get(m);						
							if(est.getOwner_card().equals("0690019914") || est.getOwner_card().equals("0690000000") || est.getOwner_card().equals("0570019947") || est.getOwner_card().endsWith("0000000") || est.getOwner_card().equals("0570019902") || est.getOwner_card().equals("0443241197") || est.getOwner_card().equals("7850010003"))//etranzact commission owner cards
							{
								etzratio += Double.parseDouble(est.getEtz_ratio());
								etzcomm += Double.parseDouble(est.getPart_fee());
								grand_etz_total += Double.parseDouble(est.getPart_fee());
							}
							else
							{
								others += Double.parseDouble(""+est.getPart_fee());
								grand_others_total += Double.parseDouble(""+est.getPart_fee());
							}
						}
					}
					et.setMerchant_split_type("STANDARD SPLIT");
        		}
    			else//payment trans code
    			{
    				if(merchant_split_type == null || merchant_catid == null || merchant_split_type.trim().equals("") || merchant_catid.trim().equals("")|| merchant_split_type.trim().equals("null") || merchant_catid.trim().equals("null"));
            		else
            		{   
            			switch(Integer.parseInt(merchant_split_type))
            			{
            				case 0:
            					
            					em = null;
            					others = 0.0;
            					etzcomm = 0.0;
            					e_catscale_commision_value = 0.0;
            					etzratio = 0.0;
            					
            					merchantSplitLog = reportModel.getE_MERCHANT_COMMISSION_SPLIT(merchant_code, merchant_catid ,
            							tran_count, total_amount, beginDate, endDate, channel, transcode);
            					for(int m=0;m<merchantSplitLog.size();m++)
            					{
            						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);
            						e_catscale_commision_value = Double.parseDouble(em.getCommission_value());
            						
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197") || em.getSplit_card().equals("7850010003"))//etranzact commission split cards
            						{
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
            						}
            						else
            						{
            							others += Double.parseDouble(""+em.getSvalue());
            							grand_others_total += Double.parseDouble(""+em.getSvalue());
            						}
            					}
            					et.setMerchant_split_type("COMMISSION SPLIT");
            					
            					if(merchantSplitLog.size()>0);
            					else//try the merchant_ext_split table
            					{
            						merchantSplitLog = reportModel.getE_MERCHANT_EXT_SPLIT(merchant_code, tran_count, total_amount);
                					
                					for(int m=0;m<merchantSplitLog.size();m++)
                					{
                						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);        						
                						
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
                						
                					}
                					et.setMerchant_split_type("COMMISSION EXT SPLIT");
            					}
            					
            					break;
            				
            				case 1:
            					
            					em = null;
            					others = 0.0;
            					etzcomm = 0.0;
            					etzratio = 0.0;
            					
            					merchantSplitLog = reportModel.getE_MERCHANT_SPECIAL_SPLIT(merchant_code, tran_count, total_amount);
            					
            					for(int m=0;m<merchantSplitLog.size();m++)
            					{
            						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);        						
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197") || em.getSplit_card().equals("7850010003"))//etranzact commission split cards
            						{
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
            						}
            						else
            						{
            							others += Double.parseDouble(""+em.getSvalue());
            							grand_others_total += Double.parseDouble(""+em.getSvalue());
            						}
            					}
            					et.setMerchant_split_type("SPECIAL SPLIT");
            					
            					if(merchantSplitLog.size()>0);
            					else//try the merchant_ext_split table
            					{
            						merchantSplitLog = reportModel.getE_MERCHANT_EXT_SPLIT(merchant_code, tran_count, total_amount);
                					
                					for(int m=0;m<merchantSplitLog.size();m++)
                					{
                						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);        						
                						
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
                						
                					}
                					et.setMerchant_split_type("SPECIAL EXT SPLIT");
            					}
            					
            					break;
            				
            				default:
            					
            				break;
            			}
            		}
    			}
        		
        		//checking if there is no etz_comm for the standard split
        		if(etzcomm == 0.0 && !transcode.equalsIgnoreCase("P"))
        		{
        			etzcomm = Double.parseDouble(total_fee) - (others);
        			grand_etz_total +=  etzcomm; 
        		}
        		
        		//checking if there is no etz_comm for the payment merchant commission split
        		if(etzcomm == 0.0 && merchant_split_type.equalsIgnoreCase("0"))
        		{
        			etzcomm = e_catscale_commision_value - others;
        			grand_etz_total +=  etzcomm; 
        		}
        		
        		
        		if(!transcode.equalsIgnoreCase("P"))
        		{
            		grand_count += Integer.parseInt(tran_count);
        		}
        		else
        		{
	        		if(merchant_split_type.equals("0"))//commission split
	        		{
	            		grand_count += Integer.parseInt(tran_count);
	        		}
	        		else if(merchant_split_type.equals("1"))//special split
	        		{
	            		grand_count += Integer.parseInt(tran_count);
	        		}
	        		else
	        		{
	        			grand_count += Integer.parseInt(tran_count);
	        		}
        		}
        	}
        	
        	System.out.println("ending of the calculations :  ..................................");
        	settle_batch.clear();
        	//System.out.println("grand_count " + grand_count);
        	//System.out.println("grand_etz_total " + grand_etz_total);
        	
        	message = grand_etz_total;
        	
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	
    	return message;
	}
	
	
	
	
	/*This method is used to display merchant data by date, trans_code and channel, bank, getting the commission for the display without clicking on channel*/
	public double viewCommissionTotalByBankAndTransCodeAndChannel(String beginDate, String endDate, String channel, String transcode, String bank_code)
	{
		//System.out.println("viewAllReport_Channel_TransCode ");
		
		ReportModel reportModel = new ReportModel();
		
    	E_TRANSACTION et;
    	String merchant_code = "";
    	String tran_count = "";
    	String total_amount = "";
    	String merchant_catid = "";
    	String merchant_split_type = "";
    	String total_fee = "";
    	double others = 0.0;
    	double etzcomm = 0.0;
    	double grand_etz_total = 0.0;
    	double grand_others_total = 0.0;
    	double grand_total = 0.0;
    	double etzratio = 0.0;
    	int grand_count = 0;
    	double e_catscale_commision_value = 0.0;
    	E_MERCHANT_SPLIT em = null;
    	E_STANDARD_SPLIT est = null;
    	double message = 0.0;

    	try
    	{
    		allMerchantReportLog = reportModel.getE_TRANSACTIONByDateAndBankCodeAndTransCodeAndChannel (beginDate, endDate, channel, transcode, bank_code);
    		System.out.println("starting of the calculations :  begin date " + beginDate + " end date " + endDate + " transcode " +  transcode  + " channel "  + channel + " bank " + bank_code);
        	for(int i=0;i<allMerchantReportLog.size();i++)
        	{
        		//System.out.println("size of array " + allMerchantReportLog.size() + " count " +  i);
        		merchant_code = "";
        		et = (E_TRANSACTION)allMerchantReportLog.get(i);
        		
        		tran_count = et.getTransaction_count();
        		total_amount = et.getTotal_amount();
        		total_fee = et.getTotal_fee();
        		merchant_code = et.getMerchat_code();
        		merchant_catid = et.getMerchant_cat_id();
        		merchant_split_type = et.getMerchant_split_type();//1: special split , 0 commission split
        		
        		
        		em = null;
        		est = null;
				others = 0.0;
				etzcomm = 0.0;
				e_catscale_commision_value = 0.0;
				etzratio = 0.0;
        		
        		if(!transcode.equalsIgnoreCase("P"))//other trans code
        		{
					if(total_fee == null || total_fee.equals("")|| total_fee.equals("0.00") ||total_fee.equals("null"))
					{
						total_fee = "0";
					}
					else
					{
						merchantSplitLog = reportModel.getE_STANDARD_SPLIT_Bank(channel, transcode, total_fee, merchant_code, beginDate, endDate, bank_code); 
	        			
	        			est = null;
	        			others = 0.0;
						etzcomm = 0.0;
						etzratio = 0.0;
						
						
						for(int m=0;m<merchantSplitLog.size();m++)
						{
							est = (E_STANDARD_SPLIT)merchantSplitLog.get(m);	
							if(est.getOwner_card().equals("0690019914") || est.getOwner_card().equals("0690000000") || est.getOwner_card().equals("0570019947") || est.getOwner_card().endsWith("0000000") || est.getOwner_card().equals("0570019902") || est.getOwner_card().equals("0443241197") || est.getOwner_card().equals("7850010003"))//etranzact commission owner cards
							{
								//et.setEtzRatio(est.getEtz_ratio());
								etzratio += Double.parseDouble(est.getEtz_ratio());
								etzcomm += Double.parseDouble(est.getPart_fee());
								grand_etz_total += Double.parseDouble(est.getPart_fee());
							}
							else
							{
								others += Double.parseDouble(""+est.getPart_fee());
								grand_others_total += Double.parseDouble(""+est.getPart_fee());
							}
						}
					}
					et.setMerchant_split_type("STANDARD SPLIT");
        		}
    			else//p trans code
    			{
    				if(merchant_split_type == null || merchant_catid == null || merchant_split_type.trim().equals("") || merchant_catid.trim().equals("")|| merchant_split_type.trim().equals("null") || merchant_catid.trim().equals("null"));
            		else
            		{   
            			switch(Integer.parseInt(merchant_split_type))
            			{
            				case 0:
            					
            					em = null;
            					others = 0.0;
            					etzcomm = 0.0;
            					e_catscale_commision_value = 0.0;
            					etzratio = 0.0;
            					
            					merchantSplitLog = reportModel.getE_MERCHANT_COMMISSION_SPLIT_Bank(merchant_code, merchant_catid , tran_count, total_amount, beginDate, endDate, channel, transcode, bank_code);
            					for(int m=0;m<merchantSplitLog.size();m++)
            					{
            						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);
            						e_catscale_commision_value = Double.parseDouble(em.getCommission_value());
            						
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197") || em.getSplit_card().equals("7850010003"))//etranzact commission split cards
            						{
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
            						}
            						else
            						{
            							others += Double.parseDouble(""+em.getSvalue());
            							grand_others_total += Double.parseDouble(""+em.getSvalue());
            						}
            					}
            					et.setMerchant_split_type("COMMISSION SPLIT");
            					break;
            				
            				case 1:
            					
            					em = null;
            					others = 0.0;
            					etzcomm = 0.0;
            					etzratio = 0.0;
            					
            					merchantSplitLog = reportModel.getE_MERCHANT_SPECIAL_SPLIT(merchant_code, tran_count, total_amount);
            					
            					for(int m=0;m<merchantSplitLog.size();m++)
            					{
            						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);   
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197") || em.getSplit_card().equals("7850010003"))//etranzact commission split cards
            						{
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
            						}
            						else
            						{
            							others += Double.parseDouble(""+em.getSvalue());
            							grand_others_total += Double.parseDouble(""+em.getSvalue());
            						}
            					}
            					et.setMerchant_split_type("SPECIAL SPLIT");
            					break;
            				
            				default:
            					
            				break;
            			}
            		}
    			}
        		
        		//checking if there is not etz_comm for the standard split
        		if(etzcomm == 0.0 && !transcode.equalsIgnoreCase("P"))
        		{
        			etzcomm = Double.parseDouble(total_fee) - (others);
        			grand_etz_total +=  etzcomm; 
        		}
        		
        		//checking if there is not etz_comm for the payment merchant commission split
        		if(etzcomm == 0.0 && merchant_split_type.equalsIgnoreCase("0"))
        		{
        			etzcomm = e_catscale_commision_value - others;
        			grand_etz_total +=  etzcomm; 
        		}
        		
        		
        		if(!transcode.equalsIgnoreCase("P"))
        		{
            		grand_count += Integer.parseInt(tran_count);
            		grand_total += Double.parseDouble(total_amount);
        		}
        		else
        		{
	        		if(merchant_split_type.equals("0"))
	        		{
	            		grand_count += Integer.parseInt(tran_count);
	        		}
	        		else if(merchant_split_type.equals("1"))
	        		{	            		
	            		grand_count += Integer.parseInt(tran_count);
	        		}
	        		else
	        		{
	        			grand_count += Integer.parseInt(tran_count);
	        		}
        		}
        	}
        	System.out.println("ending of the calculations :  ..................................");
        	settle_batch.clear();
        	message = grand_etz_total;
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	
    	return message;
	}
	
	
	
	/*This method is used to display merchant data by date, trans_code and channel, account id getting the commission for the display without clicking on channel*/
	public double viewCommissionTotalByTransCodeAndChannelAndAccountID(String beginDate, String endDate, String transcode, String channel, String account_id)
	{
		
		ReportModel reportModel = new ReportModel();
		
		
    	E_TRANSACTION et;
    	String merchant_code = "";
    	String tran_count = "";
    	String total_amount = "";
    	String merchant_catid = "";
    	String merchant_split_type = "";
    	String total_fee = "";
    	double others = 0.0;
    	double etzcomm = 0.0;
    	double grand_etz_total = 0.0;
    	double grand_others_total = 0.0;
    	double grand_total = 0.0;
    	double etzratio = 0.0;
    	int grand_count = 0;
    	double e_catscale_commision_value = 0.0;
    	E_MERCHANT_SPLIT em = null;
    	E_STANDARD_SPLIT est = null;
    	double message = 0.0;

    	try
    	{
    		allMerchantReportLog = reportModel.getE_TRANSACTIONByDateAndTransCodeAndChannelAndAccountID(beginDate, endDate, channel, transcode, account_id);
    		System.out.println("starting of the calculations :  begin date " + beginDate + " end date " + endDate + " transcode " +  transcode + " channel " + channel );
        	for(int i=0;i<allMerchantReportLog.size();i++)
        	{
        		//System.out.println("size of array " + allMerchantReportLog.size() + " count " +  i);
        		merchant_code = "";
        		et = (E_TRANSACTION)allMerchantReportLog.get(i);
        		
        		tran_count = et.getTransaction_count();
        		total_amount = et.getTotal_amount();
        		total_fee = et.getTotal_fee();
        		merchant_code = et.getMerchat_code();
        		merchant_catid = et.getMerchant_cat_id();
        		merchant_split_type = et.getMerchant_split_type();//1: special split , 0 commission split
        		
        		em = null;
        		est = null;
				others = 0.0;
				etzcomm = 0.0;
				e_catscale_commision_value = 0.0;
				etzratio = 0.0;
        		
				//System.out.println("merchant_split_type " + merchant_split_type);
				
        		if(!transcode.equalsIgnoreCase("P"))//other transaction codes
        		{
					if(total_fee == null || total_fee.equals("")|| total_fee.equals("0.00") ||total_fee.equals("null"))
					{
						total_fee = "0";
					}
					else
					{
						
						merchantSplitLog = reportModel.getE_STANDARD_SPLIT(channel, transcode, total_fee, merchant_code, beginDate, endDate); 

	        			est = null;
	        			others = 0.0;
						etzcomm = 0.0;
						etzratio = 0.0;
						
						
						for(int m=0;m<merchantSplitLog.size();m++)
						{
							est = (E_STANDARD_SPLIT)merchantSplitLog.get(m);						
							if(est.getOwner_card().equals("0690019914") || est.getOwner_card().equals("0690000000") || est.getOwner_card().equals("0570019947") || est.getOwner_card().endsWith("0000000") || est.getOwner_card().equals("0570019902")  || est.getOwner_card().equals("0443241197") || est.getOwner_card().equals("7850010003"))//etranzact commission owner cards
							{
								etzratio += Double.parseDouble(est.getEtz_ratio());
								etzcomm += Double.parseDouble(est.getPart_fee());
								grand_etz_total += Double.parseDouble(est.getPart_fee());
							}
							else
							{
								others += Double.parseDouble(""+est.getPart_fee());
								grand_others_total += Double.parseDouble(""+est.getPart_fee());
							}
						}
					}
					et.setMerchant_split_type("STANDARD SPLIT");
        		}
    			else//payment trans code
    			{
    				if(merchant_split_type == null || merchant_catid == null || merchant_split_type.trim().equals("") || merchant_catid.trim().equals("")|| merchant_split_type.trim().equals("null") || merchant_catid.trim().equals("null"));
            		else
            		{   
            			switch(Integer.parseInt(merchant_split_type))
            			{
            				case 0:
            					
            					em = null;
            					others = 0.0;
            					etzcomm = 0.0;
            					e_catscale_commision_value = 0.0;
            					etzratio = 0.0;
            					
            					merchantSplitLog = reportModel.getE_MERCHANT_COMMISSION_SPLIT(merchant_code, merchant_catid ,
            							tran_count, total_amount, beginDate, endDate, channel, transcode);
            					for(int m=0;m<merchantSplitLog.size();m++)
            					{
            						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);
            						e_catscale_commision_value = Double.parseDouble(em.getCommission_value());
            						
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197") || em.getSplit_card().equals("7850010003"))//etranzact commission split cards
            						{
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
            						}
            						else
            						{
            							others += Double.parseDouble(""+em.getSvalue());
            							grand_others_total += Double.parseDouble(""+em.getSvalue());
            						}
            					}
            					et.setMerchant_split_type("COMMISSION SPLIT");
            					if(merchantSplitLog.size()>0);
            					else//try the merchant_ext_split table
            					{
            						merchantSplitLog = reportModel.getE_MERCHANT_EXT_SPLIT(merchant_code, tran_count, total_amount);
                					
                					for(int m=0;m<merchantSplitLog.size();m++)
                					{
                						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);        						
                						
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
                						
                					}
                					et.setMerchant_split_type("COMMISSION EXT SPLIT");
            					}
            					
            					break;
            				
            				case 1:
            					
            					em = null;
            					others = 0.0;
            					etzcomm = 0.0;
            					etzratio = 0.0;
            					
            					merchantSplitLog = reportModel.getE_MERCHANT_SPECIAL_SPLIT(merchant_code, tran_count, total_amount);
            					
            					for(int m=0;m<merchantSplitLog.size();m++)
            					{
            						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);        						
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197") || em.getSplit_card().equals("7850010003"))//etranzact commission split cards
            						{
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
            						}
            						else
            						{
            							others += Double.parseDouble(""+em.getSvalue());
            							grand_others_total += Double.parseDouble(""+em.getSvalue());
            						}
            					}
            					et.setMerchant_split_type("SPECIAL SPLIT");
            					if(merchantSplitLog.size()>0);
            					else//try the merchant_ext_split table
            					{
            						merchantSplitLog = reportModel.getE_MERCHANT_EXT_SPLIT(merchant_code, tran_count, total_amount);
                					
                					for(int m=0;m<merchantSplitLog.size();m++)
                					{
                						em = (E_MERCHANT_SPLIT)merchantSplitLog.get(m);        						
                						
            							etzratio += Double.parseDouble(em.getEtzRatio());
            							etzcomm += Double.parseDouble(em.getSvalue());
            							grand_etz_total += Double.parseDouble(em.getSvalue()); 
                						
                					}
                					et.setMerchant_split_type("SPECIAL EXT SPLIT");
            					}
            					
            					break;
            				
            				default:
            					
            				break;
            			}
            		}
    			}
        		
        		//checking if there is no etz_comm for the standard split
        		if(etzcomm == 0.0 && !transcode.equalsIgnoreCase("P"))
        		{
        			etzcomm = Double.parseDouble(total_fee) - (others);
        			grand_etz_total +=  etzcomm; 
        		}
        		
        		//checking if there is no etz_comm for the payment merchant commission split
        		if(etzcomm == 0.0 && merchant_split_type.equalsIgnoreCase("0"))
        		{
        			etzcomm = e_catscale_commision_value - others;
        			grand_etz_total +=  etzcomm; 
        		}
        		
        		
        		if(!transcode.equalsIgnoreCase("P"))
        		{
            		grand_count += Integer.parseInt(tran_count);
        		}
        		else
        		{
	        		if(merchant_split_type.equals("0"))//commission split
	        		{
	            		grand_count += Integer.parseInt(tran_count);
	        		}
	        		else if(merchant_split_type.equals("1"))//special split
	        		{
	            		grand_count += Integer.parseInt(tran_count);
	        		}
	        		else
	        		{
	        			grand_count += Integer.parseInt(tran_count);
	        		}
        		}
        	}
        	
        	System.out.println("ending of the calculations :  ..................................");
        	settle_batch.clear();
        	//System.out.println("grand_count " + grand_count);
        	//System.out.println("grand_etz_total " + grand_etz_total);
        	
        	message = grand_etz_total;
        	
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	
    	return message;
	}
	
	
	
	/*Revenue Report*/
	public void viewRevenueReport()
	{
		try
		{
			revenueReportLog.clear();
			ReportModel reportModel = new ReportModel();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
	        revenueReportLog = reportModel.getRevenueE_TRANSACTION(beginDate, endDate);
	        
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*Method used to search for merchants, user only has to supply the merchant name*/
	public void viewMerchant()
	{
		try
		{
			merchantSearchLog.clear();
			ReportModel reportModel = new ReportModel();
			merchantSearchLog = reportModel.getE_MERCHANTByCode(getMerchant_name().trim());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*Method used to search for merchants, user only has to supply the merchant name*/
	public void viewSingleMerchantReport()
	{
		try
		{
			tranLog.clear();
			ReportModel reportModel = new ReportModel();
			tranLog =  reportModel.getE_TRANSACTIONByMerchantCode(getMerchant_code().trim());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	

	/*Merchant Report for viewing settlement report details of merchant code like 058*/
	public void viewSettlementReport()
	{
		try
		{
			settle_batch.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
            
            String card_num = "058";
            String merchant_code = "058";
            String trans_code = "T";
            
            
            settle_batch = reportModel.getE_SETTLEMENTDOWNLOADBKBy058(card_num, merchant_code, trans_code, beginDate, endDate);
            

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*Merchant Report for viewing settlement report details of merchant code like 058*/
	public void viewSettlementReportDetails()
	{
		try
		{
			merchantReportLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			E_SETTLEMENTDOWNLOAD_BK e;

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
            

            String batch_id = getStrParam().trim();
            String card_num = "058";
            String merchant_code = "058";
            String trans_code = "T";
            double c = 0;
            
            
            merchantReportLog = reportModel.getE_SETTLEMENTDOWNLOADBKBy058Details(batch_id, trans_code, card_num, merchant_code);
            for(int i=0;i<merchantReportLog.size();i++)
        	{
        		e = (E_SETTLEMENTDOWNLOAD_BK)merchantReportLog.get(i);
        		c += Double.parseDouble(e.getTrans_amount());
        	}
        	setTotal_amount(c);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*Merchant Report for viewing mto transactions*/
	public void viewMTOTransaction()
	{
		try
		{
			merchantReportLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
            
            String trans_code = getTrans_code().trim();
            String status = getStatus_id().trim();
            String card_num = getCard_num().trim();
            
            double t = 0.0;
        	int c = 0;
        	E_TRANSACTION e;
           
            merchantReportLog = reportModel.getMTOTransactionsGroupByChannelAndMerchantCode(beginDate, endDate, status, card_num, trans_code);
        	System.out.println("merchantReportLog " + merchantReportLog.size());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*Merchant Report for viewing mto transactions details*/
	public void viewMTODetails()
	{
		ReportModel reportModel = new ReportModel();
		
		try
		{
			String card_num = getId();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
			fundsTransfer = reportModel.getMTOTransactionsDetails(card_num, beginDate, endDate);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	
	
	/*This is the method for viewing e request log details by card number*/
	public void viewErequestLog()
	{
		try
		{
			supportLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";			
			
			FacesContext context = FacesContext.getCurrentInstance();
			String cardnum = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
			
			supportLog = reportModel.getErequestLogDetailsByCardnum(cardnum, beginDate, endDate, getStatus_id());

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	
	/*Merchant Report for viewing mto summary reports*/
	public void getMTOTransactionSummary()
	{
		try
		{
			merchantReportLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
            
            String status = getStatus_id().trim();
            
            double t = 0.0;
        	int c = 0;
        	E_TRANSACTION e;
           
            merchantReportLog = reportModel.getMTOTransactionSummary(status, beginDate, endDate);
        	System.out.println("merchantReportLog " + merchantReportLog.size());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	//this method is used to set the clicked merchant to the merchant code field after doing a merchant search
	public void merchantDrill()
	{
		try
		{
			setMerchant_code(getStrParam().trim());
			merchantSearchLog.clear();
			setMerchant_name(null);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	//This method is used to drill down on the commission split
	public void drillDownCommision_Log()
	{
		
		ReportModel reportModel = new ReportModel();
		
		merchantSplitLog.clear();
		String merchant_code = getStrParam().substring(0,getStrParam().indexOf(":"));
		String tran_count = getStrParam().substring(getStrParam().indexOf(":")+1, getStrParam().lastIndexOf(":"));
		String total_amount = getStrParam().substring(getStrParam().lastIndexOf(":")+1);
		
		//1: special split , 0 commission split
		String[] split_type = reportModel.getE_MERCHANT_CATID_SPLIT_TYPE(merchant_code);
		
		if(split_type == null || split_type[0] == null || split_type[1] == null);
		else
		{
			switch(Integer.parseInt(split_type[1]))
			{
				case 0:
					merchantSplitLog = reportModel.getE_MERCHANT_COMMISSION_SPLIT2(merchant_code, split_type[0] , tran_count, total_amount);
					break;
				
				case 1:
					merchantSplitLog = reportModel.getE_MERCHANT_SPECIAL_SPLIT(merchant_code, tran_count, total_amount);
					break;
				
				default:
					
				break;
			}
		}
	}
	
	
	/*Method for the switch report*/
	public void getSwitchReport()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			switchReportLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
	        switchReportLog = reportModel.getSwitchReportByDateAndType(beginDate, endDate, getTrans_code());
	        System.out.println("switchReportLog " + switchReportLog.size());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*Method for the activation debit card  report*/
	public void getActivationUBADebitCardReport()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			switchReportLog.clear();
			transSummaryList.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
	        String reportType = getTrans_code();
	        
	        switchReportLog = reportModel.getActivationUBADebitCardReport(beginDate, endDate);
	        System.out.print("SIZE "+switchReportLog.size());

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	/*
	 * 
	 * This method is to get the list of Bank Branch name
	 */
	public void getBranch()
    {
		try
		{
			ReportModel reportModel = new ReportModel();
			branchList =  reportModel.getBankBranch(getBank_code());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
    }
	
	public void getMerchantAcount()
    {
		try
		{
			
			String serviceId = "";
			ReportModel reportModel = new ReportModel();
			
			FacesContext context = FacesContext.getCurrentInstance();
			
			serviceId = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
			marchantlsit =  reportModel.getMerchantAccount(serviceId);
			System.out.println(" Merchant code    -- - "+marchantlsit);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

    }
	

	public void getPHCNALLDISTRICTS()
    {
		try
		{
			ReportModel reportModel = new ReportModel();
			
			FacesContext context = FacesContext.getCurrentInstance();
			String zone_district = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
			String[] zone = zone_district.split(":");
			districtByZone =  reportModel.getPHCNALLDISTRCT(zone[0]);
			System.out.println("Display ALL distirct from PHCN_POSTPARD  "+districtByZone.size());
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

    }
    
	
	//this method is used to get the branch name
	public String getBranchName(String branchCode)
    {
		String message = "";
		try
		{
			ReportModel reportModel = new ReportModel();
			message =  reportModel.getBranchName(branchCode);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return message;
    }
	
	/* Failed Transaction reports for bank_code 057 */
	public void getFailedFundTransactionReportByBank()
	{
		try
		{
			failedFundsLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
          
        
            FacesContext context = FacesContext.getCurrentInstance();
			String userCode = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();

            double t = 0.0;
            REQUEST_LOG log;
 
            	failedFundsLog = reportModel.getFailedFundTransactionReportByBank(userCode,beginDate, endDate);
           	 	for(int i=0;i<failedFundsLog.size();i++)
            	{
           	 		log = (REQUEST_LOG)failedFundsLog.get(i);
            		t += Double.parseDouble(log.getTrans_amount());
            	}
           	 		setTotal_amount(t);
           	 		System.out.println("failedFundsLog " + failedFundsLog.size());
           

        	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/* Failed Transaction reports for selected bank_code */
	public void getFailedFundTransactionReportBySelectedBank()
	{
		try
		{
			failedFundsLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
            
            String bankCode =  getBank_code();
             
            double t = 0.0;
            REQUEST_LOG log;
        	

        	failedFundsLog = reportModel.getFailedFundTransactionReportByBank(bankCode,beginDate, endDate);
        	 for(int i=0;i<failedFundsLog.size();i++)
         	{
        		 log = (REQUEST_LOG)failedFundsLog.get(i);
         		t += Double.parseDouble(log.getTrans_amount());
         	}
         	setTotal_amount(t);
        	System.out.println("failedFundsLog " + failedFundsLog.size());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	
	/*Method for the switch report*/
	public void getSwitchReportForBank()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			switchReportLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        

	        FacesContext context = FacesContext.getCurrentInstance();
			String bank_code = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
	        
	        switchReportLog = reportModel.getSwitchReportByDateAndTypeAndBank(beginDate, endDate, getTrans_code(), bank_code);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*Method for the switch report for scheme*/
	public void getSwitchReportForScheme()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			switchReportLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        

	        FacesContext context = FacesContext.getCurrentInstance();
			String scheme_card = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
	        
			String databaseServer[] = scheme_card.split(":");
			scheme_card = databaseServer[0];
	        switchReportLog = reportModel.getSwitchReportByDateAndTypeAndScheme(beginDate, endDate, getTrans_code(), scheme_card, databaseServer[1]);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	//--------------------Mobile Money Reports---------------
	
	/*Method for the mobile money reports*/
	public void getMMoneyTransactions()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			mmoneyLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
	        double b = 0.0;
	        
	        mmoneyLog = reportModel.getMobileMoneyTransactionsByDateAndType(beginDate, endDate, getChannel_id(), getCard_num().trim());
	        if(mmoneyLog.size()>0)
	        {
		        for(int i=0;i<mmoneyLog.size();i++)
		        {
		        	E_TRANSACTION et = (E_TRANSACTION)mmoneyLog.get(i);
		        	b += Double.parseDouble(et.getTrans_amount());
		        }
		        setTotal_amount(b);
	        }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*Method for the mobile money summary reports*/
	public void getMMoneySummaryTransactions()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			mmoneyLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
	        double b = 0.0;
	        
	        mmoneyLog = reportModel.getMobileMoneySummaryByDateAndType(beginDate, endDate, getChannel_id(), getCard_num().trim());
	        if(mmoneyLog.size()>0)
	        {
		        for(int i=0;i<mmoneyLog.size();i++)
		        {
		        	E_TRANSACTION et = (E_TRANSACTION)mmoneyLog.get(i);
		        	b += Double.parseDouble(et.getTrans_amount());
		        }
		        setTotal_amount(b);
	        }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*Method for the mobile money card holders*/
	public void getMMoneyCardHolders()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			mmoneyLog.clear();
	        mmoneyLog = reportModel.getMobileMoneyCardHolders(getCard_num().trim(), getMobileno().trim());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*Method for the mobile money card holders, this report doesn't connect to get the mobile subscriber card info*/
	public void getMMoneyCardHolders2()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			mmoneyLog.clear();
	        mmoneyLog = reportModel.getMobileMoneyCardHolders2(getCard_num().trim(), getMobileno().trim(), getCurUser().getLastname().trim());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*Method for the mobile money card balance*/
	public void getMMoneyCardBalance()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			mmoneyLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
			String endDate = beginDate + "23:59";
	        beginDate = beginDate  + "00:00";
	        
	        String cnum = getCard_num().trim();
	        double total_sum = 0.0;
	        CardHolder cholder = null;
	        
	        //System.out.println("cnum " + cnum);
	        
	        if(cnum.trim().length()>0)
	        {
	        	total_sum = 0.0;
	        	mmoneySummaryLog.clear();
	        	
	        	mmoneyLog = reportModel.getMobileMoneyCardBalance(beginDate, endDate ,cnum);
	        	for(int i=0;i<mmoneyLog.size();i++)
	        	{
	        		cholder = (CardHolder)mmoneyLog.get(i);
	        		total_sum += Double.parseDouble(cholder.getOnline_balance().trim());
	        	}
	        	setTotal_amount(total_sum);
	        }
	        else
	        {
	        	total_sum = 0.0;
	        	mmoneyLog.clear();
	        	
	        	mmoneySummaryLog = reportModel.getMobileMoneyCardBalanceSummary(beginDate, endDate, cnum);
	        	for(int i=0;i<mmoneySummaryLog.size();i++)
	        	{
	        		cholder = (CardHolder)mmoneySummaryLog.get(i);
	        		total_sum += Double.parseDouble(cholder.getOnline_balance().trim());
	        	}
	        	setTotal_amount(total_sum);
	        }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	
	
	/*Method for the mobile money new card subscriber*/
	public void getMMoneyCardSubscribers()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			mmoneyLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
	         
			
	        mmoneyLog = reportModel.getMobileMoneyCardSubscribers(beginDate, endDate);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*Method for getting ecarddb.e_exception*/
	public void getE_EXCEPTION()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			exceptionLog.clear();
			exceptionLog = reportModel.getE_EXCEPTIONByPan(getCard_num().trim());
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
			ReportModel reportModel = new ReportModel();
			cardholdertranLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
	        String version = getLine_type();
			
	        E_TRANSACTION tran = null;
	        double debit_total = 0.0;
	        double credit_total = 0.0;
	        
			String card_number_mobile = getCard_num().trim();
			
			
			if(version.equals("Version I"))
			{
				if(card_number_mobile.trim().length()>0)
				{
					if(card_number_mobile.startsWith("234"))
					{
						card_number_mobile = "0" + card_number_mobile.substring(3, card_number_mobile.length());
					}
					else;
				}
			}
			
			
			System.out.println("card_number_mobile " + card_number_mobile);
	        
	        //get the card number
			String card_number = reportModel.getCardNumber(card_number_mobile, version).trim();
	        if(card_number.length()<=0)
	        {
	        	facesMessages.add(Severity.WARN, "Card number doesnt exist.");
	        }
	        else
	        {
	        	mmoneyLog = reportModel.getCardHolders(card_number, version);
		        if(mmoneyLog.size()>0)
		        {
		        	cardholdertranLog = reportModel.getCardHolderTransactions(card_number, beginDate, endDate);
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
		        	cardholdertranLog.clear();
		        	cardholdertranByMerchantCodeLog.clear();
		        }
	        }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public void getMerchantServiceTransactions()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			cardholdertranLog.clear();
			
			String channel = getChannel_id();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
	        FacesContext context = FacesContext.getCurrentInstance();
	        String merchantcode = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
       
	        //String merchantcode = "2140010004:2140010003:7363738727:0560019902:0840010094:0690010913:0760000000";
	        
	        E_TRANSACTION tran = null;
	        double d = 0.0;
	        if(channel.equals(""))
	        {
	        	facesMessages.add(Severity.WARN, " Select a Channel ");
	        	
	        }else{
			
	        	cardholdertranLog = reportModel.getMerchantServiceTransactions(getCard_num(), beginDate,endDate,merchantcode,channel);
		        System.out.println(" getMerchantServiceTransactions ---->    "+cardholdertranLog.size());
	        	for(int i=0;i<cardholdertranLog.size();i++)
		        {
		        	tran = (E_TRANSACTION)cardholdertranLog.get(i);
		        	d += Double.parseDouble(tran.getTrans_amount());	        	
		        }
		        setTotal_amount(d);
	        
	        }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/* Method to get Transaction Summary reports by Bank */
	public void getTransactionSummaryReportByBank()
	{
		try
		{
			failedFundsLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
            
            String bankOption = getChannel_id();
            
  
            FacesContext context = FacesContext.getCurrentInstance();
        	String bankCode = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
        	        
        	 if(bankOption.equals("0"))
        	 { 
        		 facesMessages.add(Severity.INFO, " Bank option is required ");	
        	 }
        	 else if(bankOption.equals("01"))
        	 {
        		 failedFundsLog.clear();
        		 failedFundsLog = reportModel.getLeadBankTransactionSummaryReport(bankCode,beginDate, endDate);
        	 }
        	 else if(bankOption.equals("02"))
        	 {
        		 failedFundsLog.clear();
        		 failedFundsLog = reportModel.getCollectingBankTransactionSummaryReport(bankCode, beginDate, endDate);
        		 
        	 }
        		

        	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	
	/* Method to get VTU Transaction Summary reports  */
	public void getVTUTransactionSummaryReport()
	{
		try
		{
			failedFundsLog.clear();
			cardholdertranLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
            
            String bankOption = getChannel_id();
            
      
        	 if(bankOption.equals("0"))
        	 { 
        		 facesMessages.add(Severity.INFO, " Report option is required ");	
        		 
        	 }
        	 else if(bankOption.equals("01"))
        	 {
        		 failedFundsLog.clear();
        		 failedFundsLog = reportModel.getVTUTransactionSummaryReport(beginDate, endDate);
        		 System.out.println("Size "+failedFundsLog.size());
        	 }
        	 else if(bankOption.equals("02"))
        	 {
        		 cardholdertranLog.clear();
        		 cardholdertranLog = reportModel.getPINTransactionSummaryReport(beginDate, endDate);
        		 System.out.println("Size "+cardholdertranLog.size());
        		 
        	 }
        			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	
	public void getMtnCollectionParameters()
	{
		String url = "";
		try
		{
			
			
			String accountNo = getAccountNo();
			String custMsisdn = getCustomerMisdn();
			String custId =  getCustomerId();
			String custName = getCompanyname();
			String dispositorName = getVsmFullName();
			String depositorMsisdn = getAccountName();
			String transID = getTransactionId();
			String transDate = getTransDate();
			String partno = getPartNo();
			String chequeNo = getTo_dest();
			String paymentDesc = getStatus_id();
			String errorCode =getEdit_id();
			String errorMessage = getOperation_id();
			String channel = getChannel_id();
			String amount =  getStrParam();
			String CHEQUECLEARDATE = getChequeDate();
			String userId = getSubscriber_id();  // userId
			String email = getEsa(); // customer email
			String paymentMode = getPin(); // payment mode
			String machantRef = getMtiNumber(); // MerchantReferenceID
			String pricelistId = getCompanyId(); //PriceListID
			String paymentType = getLine_type(); // PaymentTYpe 
			String chequeBank = getCompanyBank(); // chequeBank
			String currency = getTarrif_type(); // currency 
			String region = getDepotId();  // Region
			
			String oPtion1 = getOptionType(); // option 1
			
			String oPtion2 = getOptionType2(); // option 2
			
			String option3 = getSchemeId(); // option 3
			
			String option4 = getSchemeName(); // opption 4
			
			String tellerNo = getMeterno(); // teller no
			
			String TransCodeId = getTrans_count(); // TransactionCodeId
			
			String TransStatus = getMonth(); // TransactionStatus 
			
			String chequeStatus = getYear(); // ChequeStatus 
			
			String paymentTypeDetails = getBranch_code(); // PaymentType Detaisl
			
			String partNo = getPartNo(); // Part No
			
			String ServicesType = getFrom_source(); // ServiceType
			
			
			
			
			
			
			//http://172.16.10.35:8080/MTNService/loggerRetry.jsp 
				
	
				

		    url="http://172.16.10.35:8080/MTNService/loggerRetry.jsp?&Accountno="+accountNo+"&CustomerMsisdn="+custMsisdn+"&CustomerID="+custId+"&CustomerName="+custName+"&DepositorName="+dispositorName+"&DepositorMsisdn="+depositorMsisdn+"&TransactionID="+transID+"&transDate="+transDate+""+ 
		    	"&partno="+partno+"&ChequeNo="+chequeNo+"&PaymentDescription="+paymentDesc+"&errorCode="+errorCode+"&errorMessage="+errorMessage+"&channel="+channel+"&Amount="+amount+"&Userid="+userId+"&CustomerEmail="+email+"&PaymentMode="+paymentMode+"&MerchantReferenceID="+machantRef+"" +
				"&PriceListId="+pricelistId+"&PaymentType="+paymentType+"&chequeBank="+chequeBank+"&Currency="+currency+"&Channel="+channel+"&Region="+region+"&Option1="+oPtion1+"&Option2="+oPtion2+"&Option3="+option3+"&Option4="+option4+"&TellerNo="+tellerNo+"&TransactionCodeId="+TransCodeId+""+
				"&TransactionStatus="+TransStatus+"&ChequeStatus="+chequeStatus+"&PayTypeDetail="+paymentTypeDetails+"&PartNo="+partNo+"&ServiceType="+ServicesType+"";
				
		    FacesContext.getCurrentInstance().getExternalContext().redirect(url);
			System.out.println("URL ---! > "+url);
			
			/*url = "/support/mtnCollectionReport.xhtml?&accountno="+accountNo+"&custMsisdn="+custMsisdn+"&custId="+custId+"&custName="+custName+"&dispositorName="+dispositorName+"&depositorMsisdn="+depositorMsisdn+"&transID="+transID+"&transDate="+transDate+"" +
					"&partno="+partno+"&chequeNo="+chequeNo+"&paymentDesc="+paymentDesc+"&errorCode="+errorCode+"&errorMessage="+errorMessage+"&channel="+channel+"&amount="+amount+" " ;*/
			
			
			
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		//return url;
				
	}
	
	public void getMTNTransactionReport()
	{
	
		try
		{
			cardholdertranLog.clear();
			
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            beginDate = beginDate + " " + getStart_hr() + ":00";
            
            String endDate = df.format(getEnd_date());
            endDate = endDate + " " + getEnd_hr() + ":59";
			
            
        	String transType = getTrans_code();
        	C_TRANSACTION tran = null;
        	double d = 0.0d;
        	
        	cardholdertranLog = reportModel.getMTNTransactionReport(transType, beginDate, endDate);
        	for(int i=0;i<cardholdertranLog.size();i++)
	        {
	        	tran = (C_TRANSACTION)cardholdertranLog.get(i);
	        	d += Double.parseDouble(tran.getTrans_amount());	        	
	        }
	        setTotal_amount(d);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			
		}
		
		
	}
	
	
	
	/*Method to get Transactions based on a mobile number for the scheme merchants*/
	/*public void getCardHolderTransactions_Scheme()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			cardholdertranLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
			
	        E_TRANSACTION tran = null;
	        double debit_total = 0.0;
	        double credit_total = 0.0;
	        
			String card_number = getCard_num().trim();
			
			FacesContext context = FacesContext.getCurrentInstance();
			String scheme_card = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
		    
			if(card_number.length()<=0)
	        {
	        	facesMessages.add(Severity.WARN, "Card number doesnt exist.");
	        }
			else
			{
				mmoneyLog = reportModel.getCardHolders_SchemeFromPocketMoniDB(card_number, scheme_card);
		        if(mmoneyLog.size()>0)
		        {
		        	cardholdertranLog = reportModel.getCardHolderTransactions_scheme(card_number, beginDate, endDate, scheme_card);
					System.out.println("cardholdertranLog " + cardholdertranLog.size());
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
		        	cardholdertranLog.clear();
		        	cardholdertranByMerchantCodeLog.clear();
		        }
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	*/
	
	/*Method to get Transactions based on a mobile number for the scheme merchants*/
	public void getCardHolderTransactions_Scheme()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			cardholdertranLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
			
	        E_TRANSACTION tran = null;
	        double debit_total = 0.0;
	        double credit_total = 0.0;
	        
			String card_number = getCard_num().trim();
			String channelid = getChannel_id().trim();
			
			FacesContext context = FacesContext.getCurrentInstance();
			String scheme_card = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
			
			String databaseServer[] = scheme_card.split(":");
			scheme_card = databaseServer[0];
			cardholdertranLog = reportModel.getCardHolderTransactions_scheme(card_number, beginDate, endDate, scheme_card,
					channelid, databaseServer[1]);
			System.out.println("cardholdertranLog " + cardholdertranLog.size());
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
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	
	public void getCompany()
	{
		
		try
		{
			ReportModel reportModel = new ReportModel();
			companyList = reportModel.getCompanyName();
			System.out.println("Displaying Company Size   "+companyList.size());
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	public String getCompanyByCompanyId(String companyId)
	{
		String message = "";
		try
		{
			
			
			ReportModel reportModel = new ReportModel();
			 message = reportModel.getCompanyNameByComapanyId(companyId);
			
		
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return message;
		
	}
	
	
	/*Method to get cardholder basic information for a scheme and for version II*/
	public void getCardHolderEnquiry_Scheme()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			mmoneyLog.clear();

			String card_number = getCard_num().trim();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
			
			FacesContext context = FacesContext.getCurrentInstance();
			String scheme_card = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
		    
			String databaseServer[] = scheme_card.split(":");
			scheme_card = databaseServer[0];
			
			//mmoneyLog = reportModel.getCardHolders_SchemeFromPocketMoniDB(card_number, scheme_card);
			mmoneyLog = reportModel.getCardHolders_SchemeFromPocketMoniDB(card_number,beginDate,endDate,
					scheme_card, databaseServer[1]);
			System.out.println("getCardHolders_SchemeFromPocketMoniDB Log"+mmoneyLog.size());
		
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*Method to get cardholder basic information for a scheme and for version II*/
	/*public void getCardHolderEnquiry_Scheme()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			mmoneyLog.clear();

			String card_number = getCard_num().trim();
			
			FacesContext context = FacesContext.getCurrentInstance();
			String scheme_card = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
		    
			if(card_number.length()<=0)
	        {
	        	facesMessages.add(Severity.WARN, "Card number doesnt exist.");
	        }
			else
			{
				mmoneyLog = reportModel.getCardHolders_SchemeFromPocketMoniDB(card_number, scheme_card);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	*/
	
	/*Method to get Transactions based on an international merchant*/
	public void getMerchantTransactions()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			cardholdertranLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
			
	        E_TRANSACTION tran = null;
	        double debit_total = 0.0;
	        double credit_total = 0.0;
	        String service_id = "";
			
	        FacesContext context = FacesContext.getCurrentInstance();
			service_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();

	       
			String value = getStatus_id().trim();
			String type = value.substring(0,value.indexOf(":"));
			service_id = value.substring(value.indexOf(":")+1);
			
        	cardholdertranLog = reportModel.getMerchantTransactions(type, service_id, beginDate, endDate);
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
	        setAccountName(reportModel.getMerchantOnlineBalance(type, service_id));//dis place holder stands as the online balance
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*Method to get Transactions based on an international merchant*/
	public void getMerchantTransactionsMinistry()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			cardholdertranLog.clear();
			switchReportLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
			
	        E_TRANSACTION tran = null;
	        E_IPAYMENTTRAN ipayment = null;
	        double debit_total = 0.0;
	        double credit_total = 0.0;
	        String service_id = "";
			
	        FacesContext context = FacesContext.getCurrentInstance();
			service_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();

	       
			String value = getStatus_id().trim();
			String type = value.substring(0,value.indexOf(":"));
			service_id = value.substring(value.indexOf(":")+1);
			
			String currencyType = getTitle(); //  currencyCode here 
			if(currencyType==null)
			{
				currencyType = "";
			}
			
			//String status = getOptionType(); // status 
			
			
			System.out.println("Status: "+value);
			System.out.println("Type: "+type);
			System.out.println("Service Id: "+service_id);
	
			if(type.equals("1"))
			{
				  switchReportLog.clear();
				  switchReportLog = reportModel.getMerchantTransactions2(type, service_id,currencyType,beginDate, endDate);
				  System.out.println("switchReportLog"+switchReportLog.size());	
				  for(int i=0;i<switchReportLog.size();i++)
			        {
			        	tran = (E_TRANSACTION)switchReportLog.get(i);
			        	
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
			else if(type.equals("2"))
			{
			
				
				cardholdertranLog = reportModel.getMerchantTransactions2(type, service_id,currencyType,beginDate, endDate);
				 System.out.println(" Size ------------------------------------------------- "+cardholdertranLog.size());
		        for(int i=0;i<cardholdertranLog.size();i++)
		        {
		        	ipayment = (E_IPAYMENTTRAN)cardholdertranLog.get(i);
		        	
		        	if(ipayment.getCurrencyCode().trim().equals("USD"))
		        	{
		        		debit_total +=Double.parseDouble(ipayment.getConvertedAmount());
		        	}
		        	if(ipayment.getCurrencyCode().trim().equals("GBP"))
		        	{
		        		credit_total +=Double.parseDouble(ipayment.getConvertedAmount());
		        	}
		        	
		    	    	      	
		        }
		        setTotal_amount(debit_total);
		        setTotal_other_amount(credit_total);
		        

	        	 System.out.println("USD------------------------------------------------- "+getTotal_amount());
			        System.out.println("GBP-------------------------------------------------- "+getTotal_other_amount());
		       // setAccountName(reportModel.getMerchantOnlineBalance(type, service_id));//dis place holder stands as the online balance
			}
        	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	
	
	/*Method to get Transactions based on a merchant*/
	public void getOnlyMerchantTransactions()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			cardholdertranLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
			
	        E_TRANSACTION tran = null;
	        double d = 0.0;
			
        	cardholdertranLog = reportModel.getOnlyMerchantTransactions(getCard_num(), beginDate, endDate);
	        for(int i=0;i<cardholdertranLog.size();i++)
	        {
	        	tran = (E_TRANSACTION)cardholdertranLog.get(i);
	        	d += Double.parseDouble(tran.getTrans_amount());	        	
	        }
	        setTotal_amount(d);
	        
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*Method to get Transactions based on trip mart merchant*/
	public void getMerchantTransactionsTripMart()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			cardholdertranLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
	        
			
	        FacesContext context = FacesContext.getCurrentInstance();
			String merchantcode = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();

	       
			
	        E_TRANSACTION tran = null;
	        double d = 0.0;
			
        	cardholdertranLog = reportModel.getMerchantTransactionsTripMart(merchantcode, beginDate, endDate);
	        for(int i=0;i<cardholdertranLog.size();i++)
	        {
	        	tran = (E_TRANSACTION)cardholdertranLog.get(i);
	        	d += Double.parseDouble(tran.getTrans_amount());	        	
	        }
	        setTotal_amount(d);
	        
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*Method to get Covenant House Parish  Transactions based on the merchant*/
	public void getMerchantTransaction()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			cardholdertranLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
			
	        E_TRANSACTION tran = null;
	        double debit_total = 0.0;
	        double credit_total = 0.0;
	        String service_id = "";

	        //String merchantcode = "";
	        FacesContext context = FacesContext.getCurrentInstance();
	        //merchantcode = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
	        String merchantcode = "7006020020:057PS10019:7006020021";
	        	
	       
			/*String value = getStatus_id().trim();
			String type = value.substring(0,value.indexOf(":"));
			service_id = value.substring(value.indexOf(":")+1);*/
			
        	cardholdertranLog = reportModel.getMerchantTransaction(merchantcode, beginDate, endDate);
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
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*Method to get lotto transactions*/
	public void getLottoTransactions()
	{
		try
		{
			LOTTO_LOG tran = null;
	        double d = 0.0;
			ReportModel reportModel = new ReportModel();
			cardholdertranLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
			
	        String lotto_no = getCard_num().trim();
	        String phone_no = getMerchant_code().trim();
	        String terminal_id = getTerminal_id().trim();
	        
			
        	cardholdertranLog = reportModel.getLottoTransactions(lotto_no, phone_no, terminal_id, beginDate, endDate);
	        for(int i=0;i<cardholdertranLog.size();i++)
	        {
	        	tran = (LOTTO_LOG)cardholdertranLog.get(i);
	        	d += Double.parseDouble(tran.getTrans_amount());	        	
	        }
	        setTotal_amount(d);
	        
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*This method is used to log for reversal*/
	public void logForReversal()
	{
		String message = "";
		System.out.println("logForReversal");
		
		try
		{
			System.out.println("trans id " + getStatus_id());
		}
		catch(Exception ex)
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
	
	public void deleteCompany()
	{
		
		try
		{
			ReportModel model = new ReportModel();
			String companyId =getEdit_id();
			
			String message = model.deleteCompany(companyId);
			if(message.equalsIgnoreCase("SUCCESS"))
			{
				message = "Record Successfully Deleted ";
			}
			else
			{
				message = " Unable to Delete Record ";
			}
					
			facesMessages.add(Severity.INFO, message);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		
	}
	
	public void deletePoolAccount()
	{
		
		try
		{
			ReportModel model = new ReportModel();
			String companyId =getEdit_id();
			
			String message = model.deletePoolAccount(companyId);
			if(message.equalsIgnoreCase("SUCCESS"))
			{
				message = "Record Successfully Deleted ";
			}
			else
			{
				message = " Unable to Delete Record ";
			}
					
			facesMessages.add(Severity.INFO, message);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		
	}
	
	
	public void deleteAccountInfo()
	{
		
		try
		{
			ReportModel model = new ReportModel();
			String companyId =getEdit_id();
			
			String message = model.deleteAccountInfo(companyId);
			if(message.equalsIgnoreCase("SUCCESS"))
			{
				message = "Record Successfully Deleted ";
			}
			else
			{
				message = " Unable to Delete Record ";
			}
					
			facesMessages.add(Severity.INFO, message);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		
	}
	
	
	public void disablePoolAccount()
	{
		
		try
		{
			ReportModel model = new ReportModel();
			String companyId =getEdit_id();
			
			String message = model.disablePoolAccount(companyId);
			if(message.equalsIgnoreCase("SUCCESS"))
			{
				message = "Record Successfully Disabled ";
			}
			else
			{
				message = " Unable to Disable Record ";
			}
					
			facesMessages.add(Severity.INFO, message);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		
	}
	
	public void deleteException()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			exceptionLog.clear();
			
			
			String pan = getOperation_id().trim();
			String tran_date = getEdit_id().trim();
			
			System.out.println(pan + " pan");
			System.out.println(tran_date + " tran_date");
			
			String message = reportModel.deleteE_EXCEPTION(pan, tran_date);
			facesMessages.add(Severity.INFO, message);
			
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	public String rowClickAction() 
	{  
        System.out.println("Row clicked...");  
        selectedTran.setSelected(!selectedTran.isSelected());  
        //rowsToUpdate.clear();  
        rowsToUpdate.add(fundsTransfer.indexOf(selectedTran));  
        System.out.println("Total Row added... " + rowsToUpdate.size());
        for(int i=0;i<rowsToUpdate.size();i++)
        {
        	
        	System.out.println("Total Row added... " + rowsToUpdate.size());
        }
        
        return null;  
    } 
	
	public void hotlistCard()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			String card_num = getEdit_id().trim();//card_num;
			String reason = "suspected";
			
			String message = reportModel.createE_Exception(card_num, reason);
			System.out.println("message " + message);
			facesMessages.add(Severity.WARN, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void getLogData()
	{
		ReportModel reportModel = new ReportModel();
		
		try
		{
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
			failedFundsLog = reportModel.getRequestLogData(beginDate, endDate);
			tranLog = reportModel.getETransactionLogData(beginDate, endDate);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
    
    public void viewSettlementReportGTB()
	{
		try
		{
			settle_batch.clear();
			ReportModel reportModel = new ReportModel();
	
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	        String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	
	        String card_num = "058";
	        String merchant_code = "058";
	        String trans_code = "T";
	
	
	        settle_batch = reportModel.getE_SETTLEMENTDOWNLOADBKBy058GTB(card_num, merchant_code, trans_code, beginDate, endDate,getIssueCode());
	
	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
    
    public void viewSettlementReportDetailsGTB()
	{
		try
		{
			merchantReportLog.clear();
			ReportModel reportModel = new ReportModel();
	
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			E_SETTLEMENTDOWNLOAD_BK e;
	
	        String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	
	
	        String batch_id = getStrParam().trim();
	        String card_num = "058";
	        String merchant_code = "058";
	        String trans_code = "T";
	        double c = 0;
	
	        
	        merchantReportLog = reportModel.getE_SETTLEMENTDOWNLOADBKBy058DetailsGTB(batch_id, trans_code, card_num, merchant_code, getIssueCode());
			//        for(int i=0;i<merchantReportLog.size();i++)
			//    	{
			//    		e = (E_SETTLEMENTDOWNLOAD_BK)merchantReportLog.get(i);
			//    		c += Double.parseDouble(e.getTrans_amount());
			//    	}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
    
    /*This is used to get settlement report for the in-house settlement team*/
    public void getSettlementTransactions()
  	{
  		try
  		{
  			merchantReportLog.clear();
  			failedFundsLog.clear();
  			ReportModel reportModel = new ReportModel();
  	
  			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
  	
  	        String beginDate = df.format(getStart_date());
  	        beginDate = beginDate + " " + getStart_hr() + ":00";
  	
  	        String endDate = df.format(getEnd_date());
  	        endDate = endDate + " " + getEnd_hr() + ":59";
  	
  	
  	        String seloption = getOptionType().trim();
  	        String trans_type = getChannel_id().trim();
  	        String bank = getBank_code().trim();
  	        String status = getStatus_id().trim();
  	        
  	        if(seloption.equals("1") && trans_type.equals("1"))
  	        {
  	        	merchantReportLog = reportModel.getAllCorporatePayTransactions(bank, beginDate, endDate, seloption, status);
  	        }
  	        else if(seloption.equals("2") && trans_type.equals("1"))
	        {
	        	merchantReportLog = reportModel.getAllCorporatePayTransactions(bank, beginDate, endDate, seloption, status);
	        }
  	        else if(seloption.equals("1") && trans_type.equals("2"))
	        {
  	        	failedFundsLog = reportModel.getAllFundsTransferTransactions_Settlement(bank, beginDate, endDate, seloption, status);
	        }
  	        else if(seloption.equals("2") && trans_type.equals("2"))
	        {
  	        	failedFundsLog = reportModel.getAllFundsTransferTransactions_Settlement(bank, beginDate, endDate, seloption, status);
	        }
  	        
  	        System.out.println("merchantReportLog " + merchantReportLog.size());
  	        System.out.println("failedFundsLog " + failedFundsLog.size());
  	
  		}
  		catch(Exception ex)
  		{
  			ex.printStackTrace();
  		}
  	}
    
/*	public void getSchemeReport()
	{
		
	
		ReportModel cmmodel = new ReportModel();
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
*/
	
    public void getOnlyHolderTransactions()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			cardholdertranLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
			
	        E_TRANSACTION tran = null;
	        double d = 0.0;
			
        	cardholdertranLog = reportModel.getOnlyHolderTransactions(getCard_num(), beginDate, endDate);
        	System.out.println("cardholdertranLog ====  "+cardholdertranLog.size());
	        for(int i=0;i<cardholdertranLog.size();i++)
	        {
	        	tran = (E_TRANSACTION)cardholdertranLog.get(i);
	        	d += Double.parseDouble(tran.getTrans_amount());	        	
	        }
	        setTotal_amount(d);
	        
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
    
    
    public void getMtnCollectionReport()
   	{
   		try
   		{
   			ReportModel reportModel = new ReportModel();
   			cardholdertranLog.clear();
   			
   			
   			String custId = getCustomerId();
   			
   			System.out.println("Customer ID "+custId);
   			if(custId==null)
   			{
   				custId="";
   			}
   			String transId = getTransactionId();
   			if(transId==null)
   			{
   				transId="";
   			}
   			String accountNo = getAccountNo();
   			if(accountNo==null)
   			{
   				accountNo = "";
   			}
   			String custMsisdn = getCustomerMisdn();
   			if(custMsisdn==null)
   			{
   				custMsisdn = "";
   			}
   			String partNo = getPartNo();
   			if(partNo==null)
   			{
   				partNo = "";
   			}

   			String transOption = getTrans_code();
   			
   			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
   	        
   			String beginDate = df.format(getStart_date());
   	        beginDate = beginDate + " " + getStart_hr() + ":00";
   	        
   	        String endDate = df.format(getEnd_date());
   	        endDate = endDate + " " + getEnd_hr() + ":59";
   			
   	        C_MTNRequestLogger tran = null;
   	        double d = 0.0;
   	        
   	        cardholdertranLog = reportModel.getMtnCollectionReport(custId,transId,accountNo,custMsisdn,partNo,transOption, beginDate, endDate);
        	System.out.println("cardholdertranLog ====  "+cardholdertranLog.size());
	        for(int i=0;i<cardholdertranLog.size();i++)
	        {
	        	tran = (C_MTNRequestLogger)cardholdertranLog.get(i);
	        	d += Double.parseDouble(tran.getAmount());	        	
	        }
	        setTotal_amount(d);
/*
   	        if(custId.length() > 0 || transId.length() > 0 || accountNo.length() > 0 || custMsisdn.length() > 0 || partNo.length() > 0 )
   	        {
   	        	
	   	     	
   	        	
   	        }
   	        else
   	        {
   	        	
   	        	facesMessages.add(Severity.INFO, " Enter any of the field to view report ");
   	        }
   	        
   	        */
   	        
           
   	        
   		}
   		catch(Exception ex)
   		{
   			ex.printStackTrace();
   		}
   	}
    
    public void getSettlementReportByBank()
   	{
   		try
   		{
   			ReportModel reportModel = new ReportModel();
   			cardholdertranLog.clear();
   			
   			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
   	        
   			String beginDate = df.format(getStart_date());
   	        beginDate = beginDate + " " + getStart_hr() + ":00";
   	        
   	        String endDate = df.format(getEnd_date());
   	        endDate = endDate + " " + getEnd_hr() + ":59";
   	        
   	        String service_id = "";
   			
   	        C_TRANSACTION tran = null;
   	        double d = 0.0;
   	        	FacesContext context = FacesContext.getCurrentInstance();
   	  			service_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();

           	cardholdertranLog = reportModel.getSettlementReportByBank(service_id, beginDate, endDate);
           	
           	System.out.println("cardholdertranLog ====  "+cardholdertranLog.size());
   	        for(int i=0;i<cardholdertranLog.size();i++)
   	        {
   	        	tran = (C_TRANSACTION)cardholdertranLog.get(i);
   	        	d += Double.parseDouble(tran.getTrans_amount());	        	
   	        }
   	        setTotal_amount(d);
   	        
   		}
   		catch(Exception ex)
   		{
   			ex.printStackTrace();
   		}
   	}
    
    public void getSettlementReportByRevenue()
   	{
   		try
   		{
   			ReportModel reportModel = new ReportModel();
   			cardholdertranLog.clear();
   			
   			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
   	        
   			String beginDate = df.format(getStart_date());
   	        beginDate = beginDate + " " + getStart_hr() + ":00";
   	        
   	        String endDate = df.format(getEnd_date());
   	        endDate = endDate + " " + getEnd_hr() + ":59";
   	        
   	        String service_id = "";
   			
   	        C_TRANSACTION tran = null;
   	        double d = 0.0;
   	        	FacesContext context = FacesContext.getCurrentInstance();
   	  			service_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();

           	cardholdertranLog = reportModel.getSettlementReportByRevenue(service_id, beginDate, endDate);
           	
           	System.out.println("cardholdertranLog ====  "+cardholdertranLog.size());
   	        for(int i=0;i<cardholdertranLog.size();i++)
   	        {
   	        	tran = (C_TRANSACTION)cardholdertranLog.get(i);
   	        	d += Double.parseDouble(tran.getTrans_amount());	        	
   	        }
   	        setTotal_amount(d);
   	        
   		}
   		catch(Exception ex)
   		{
   			ex.printStackTrace();
   		}
   	}
    /**
     * 
     * This method is to get Merchant Statment
     */
    public void getMerchantStatement()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			cardholdertranLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
			String service_id = "";
			E_SETTLEMENTDOWNLOAD_BK settleDownload = null;
	        double d = 0.0;
	        double d1 = 0.0;
	        
	        FacesContext context = FacesContext.getCurrentInstance();
			service_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();

			
        	cardholdertranLog = reportModel.getMerchantStatement(service_id, beginDate, endDate);
        	System.out.println("cardholdertranLog ====  "+cardholdertranLog.size());
	        for(int i=0;i<cardholdertranLog.size();i++)
	        {
	        	settleDownload = (E_SETTLEMENTDOWNLOAD_BK)cardholdertranLog.get(i);
	        	d -= Double.parseDouble(settleDownload.getTrans_amount());
	        	d1 += Double.parseDouble(settleDownload.getTransaction_count());	
	        }
	        setTotal_amount(d);
	        setTotal_etz_amount(d1);
	        
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
    public void getReportByPaymentType()
  	{
  		try
  		{
  			ReportModel reportModel = new ReportModel();
  			cardholdertranLog.clear();
  			
  			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
  	        
  			String beginDate = df.format(getStart_date());
  	        beginDate = beginDate + " " + getStart_hr() + ":00";
  	        
  	        String endDate = df.format(getEnd_date());
  	        endDate = endDate + " " + getEnd_hr() + ":59";
  			String service_id = "";
  			C_TRANSACTION ctran = null;
  	        double d = 0.0;
  	        double d1 = 0.0;
  	        
  	        FacesContext context = FacesContext.getCurrentInstance();
  			service_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();

  			
          	cardholdertranLog = reportModel.getReportByPaymentType(service_id, beginDate, endDate);
          	System.out.println("cardholdertranLog ====  "+cardholdertranLog.size());
  	        for(int i=0;i<cardholdertranLog.size();i++)
  	        {
  	        	ctran = (C_TRANSACTION)cardholdertranLog.get(i);
  	        	d = Double.parseDouble(ctran.getTrans_amount());
  	       
  	        }
  	        setTotal_amount(d);
  	       
  	        
  		}
  		catch(Exception ex)
  		{
  			ex.printStackTrace();
  		}
  	}
    
    /*
     * 
     *  method to get Report by Faulty 
     */
    
    public void getReportByFaulty()
  	{
  		try
  		{
  			ReportModel reportModel = new ReportModel();
  			cardholdertranLog.clear();
  			
  			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
  	        
  			String beginDate = df.format(getStart_date());
  	        beginDate = beginDate + " " + getStart_hr() + ":00";
  	        
  	        String endDate = df.format(getEnd_date());
  	        endDate = endDate + " " + getEnd_hr() + ":59";
  			String service_id = "";
  			E_SETTLEMENTDOWNLOAD_BK settleDownload = null;
  	        double d = 0.0;
  	        double d1 = 0.0;
  	        
  	        FacesContext context = FacesContext.getCurrentInstance();
  			service_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();

  			
          	cardholdertranLog = reportModel.getReportByFaculty(service_id, beginDate, endDate);
          	System.out.println("cardholdertranLog ====  "+cardholdertranLog.size());
  	        for(int i=0;i<cardholdertranLog.size();i++)
  	        {
  	        	settleDownload = (E_SETTLEMENTDOWNLOAD_BK)cardholdertranLog.get(i);
  	        	d -= Double.parseDouble(settleDownload.getTrans_amount());
  	        	d1 += Double.parseDouble(settleDownload.getTransaction_count());	
  	        }
  	        setTotal_amount(d);
  	        setTotal_etz_amount(d1);
  	        
  		}
  		catch(Exception ex)
  		{
  			ex.printStackTrace();
  		}
  	}
    
    public void getReportByProgramm()
  	{
  		try
  		{
  			ReportModel reportModel = new ReportModel();
  			cardholdertranLog.clear();
  			
  			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
  	        
  			String beginDate = df.format(getStart_date());
  	        beginDate = beginDate + " " + getStart_hr() + ":00";
  	        
  	        String endDate = df.format(getEnd_date());
  	        endDate = endDate + " " + getEnd_hr() + ":59";
  			String service_id = "";
  			C_TRANSACTION ctran = null;
  	        double d = 0.0;
  	    
  	        FacesContext context = FacesContext.getCurrentInstance();
  			service_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();

  			
          	cardholdertranLog = reportModel.getReportByProgramm(service_id, beginDate, endDate);
          	System.out.println("cardholdertranLog ====  "+cardholdertranLog.size());
  	        for(int i=0;i<cardholdertranLog.size();i++)
  	        {
  	        	ctran = (C_TRANSACTION)cardholdertranLog.get(i);
  	        	d = Double.parseDouble(ctran.getTrans_amount());
  	     
  	        }
  	        setTotal_amount(d);
  	        
  		}
  		catch(Exception ex)
  		{
  			ex.printStackTrace();
  		}
  	}
    
/*Method to get Marchant Summary Report */
	
	public void getMerchantSummary()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			cardholdertranLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        C_TRANSACTION cTran = null;
	        double transAmount = 0.0;

	        String service_id = "";
	        
	       FacesContext context = FacesContext.getCurrentInstance();
			service_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();

			System.out.println("service_idservice_idservice_idservice_id==== "+service_id );
        	
			cardholdertranLog = reportModel.getMarchantSummary(service_id,beginDate, endDate);
			
            System.out.println("cardholdertranLog Size: : : : :  ::   "+cardholdertranLog.size());
           
	        for(int i=0;i<cardholdertranLog.size();i++)
	        {
	        	cTran = (C_TRANSACTION)cardholdertranLog.get(i);
	        	transAmount += Double.parseDouble(cTran.getTrans_amount());
	             
	        }
	        setTotal_amount(transAmount); 
	        
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void getMerchantSettlementReport()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			cardholdertranLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        E_SETTLEMENTDOWNLOAD_BK settlement = null;
	        double transAmount = 0.0;

	        String service_id = "";
	        
	       FacesContext context = FacesContext.getCurrentInstance();
			service_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();

			System.out.println("service_idservice_idservice_idservice_id==== "+service_id );
        	
			cardholdertranLog = reportModel.getMarchantSettlementReport(service_id,beginDate, endDate);
			
            System.out.println("cardholdertranLog Size: : : : :  ::   "+cardholdertranLog.size());
           
	        for(int i=0;i<cardholdertranLog.size();i++)
	        {
	        	settlement = (E_SETTLEMENTDOWNLOAD_BK)cardholdertranLog.get(i);
	        	transAmount += Double.parseDouble(settlement.getTrans_amount());
	             
	        }
	        setTotal_amount(transAmount); 
	        
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
    
/*Method to get Marchant Summary Report */
	
	public void getMerchantPayment()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			cardholdertranLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        E_MERCHANT marchant  = null;
	        double transAmount = 0.0;

	        String service_id = "";
	        
	       FacesContext context = FacesContext.getCurrentInstance();
			service_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();

			System.out.println("service_idservice_idservice_idservice_id==== "+service_id );
        	
			cardholdertranLog = reportModel.getMarchantPayment(service_id,beginDate, endDate);
			
            System.out.println("cardholdertranLog Size: : : : :  ::   "+cardholdertranLog.size());
           
	        for(int i=0;i<cardholdertranLog.size();i++)
	        {
	        	marchant = (E_MERCHANT)cardholdertranLog.get(i);
	        	transAmount += Double.parseDouble(marchant.getTransAmount());
	             
	        }
	        setTotal_amount(transAmount); 
	        
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
/*Method to get Marchant Summary Report */
	
	public void getMerchantSummaryByBank()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			cardholdertranLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        C_TRANSACTION cTran = null;
	        double transAmount = 0.0;

	        String service_id = "";
	        String issuer = getBank_code();
	       FacesContext context = FacesContext.getCurrentInstance();
			service_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();

			System.out.println("service_idservice_idservice_idservice_id==== "+service_id );
        	 
			cardholdertranLog = reportModel.getMarchantSummaryByBank(service_id,issuer, beginDate, endDate);
			
            System.out.println("cardholdertranLog Size: : : : :  ::   "+cardholdertranLog.size());
           
	        for(int i=0;i<cardholdertranLog.size();i++)
	        {
	        	cTran = (C_TRANSACTION)cardholdertranLog.get(i);
	        	transAmount += Double.parseDouble(cTran.getTrans_amount());
	             
	        }
	        setTotal_amount(transAmount); 
	        
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
/*Method to get Marchant Summary Report */
	
	public void getMerchantSummaryReport()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			cardholdertranLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        C_TRANSACTION cTran = null;
	        double transAmount = 0.0;

	        String service_id = "";
	        
	        String issuerCode = getBank_code();
	        if(issuerCode==null)
	        {
	        	issuerCode="";
	        }
	        String subCode = getBranchCode();
	        if(subCode==null)
	        {
	        	subCode = "";
	        }
	        
	        System.out.print("issure code  "+issuerCode + "sub Code  " +subCode);
			
	        FacesContext context = FacesContext.getCurrentInstance();
			service_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();

			System.out.println("service_idservice_idservice_idservice_id==== "+service_id );
        	
			cardholdertranLog = reportModel.getMarchantSummaryReport(service_id, issuerCode, subCode, beginDate, endDate);
           System.out.println("cardholdertranLog Size: : : : :  ::   "+cardholdertranLog.size());
	        for(int i=0;i<cardholdertranLog.size();i++)
	        {
	        	cTran = (C_TRANSACTION)cardholdertranLog.get(i);
	        	transAmount += Double.parseDouble(cTran.getTrans_amount());
	             
	        }
	        setTotal_amount(transAmount); 
	        
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
    
    
    /*getATM_POS_Settlement*/
    public void getATM_POS_Settlement()
  	{
  		try
  		{
  			merchantReportLog.clear();
  			ReportModel reportModel = new ReportModel();
  	
  			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
  	
  	        String beginDate = df.format(getStart_date());
  	        beginDate = beginDate + " " + getStart_hr() + ":00";
  	
  	        String endDate = df.format(getEnd_date());
  	        endDate = endDate + " " + getEnd_hr() + ":59";
  	        String ch_id = getChannel_id();
  	        String pan = getCard_num().trim();
  	        String t_id = getTerminal_id().trim();
  	        
  	        merchantReportLog = reportModel.getATM_POS_Settlement(pan, t_id, beginDate, endDate, ch_id);
  	        
  	        System.out.println("merchantReportLog " + merchantReportLog.size());  	
  		}
  		catch(Exception ex)
  		{
  			ex.printStackTrace();
  		}
  	}
    
    
    /*getChammsSettlement*/
    public void getChammsSettlement()
  	{
  		try
  		{
  			merchantReportLog.clear();
  			ReportModel reportModel = new ReportModel();
  	
  			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
  	
  	        String beginDate = df.format(getStart_date());
  	        beginDate = beginDate + " " + getStart_hr() + ":00";
  	
  	        String endDate = df.format(getEnd_date());
  	        endDate = endDate + " " + getEnd_hr() + ":59";
  	        
  	        merchantReportLog = reportModel.getChammsSettlement(beginDate, endDate);
  	        System.out.println("merchantReportLog " + merchantReportLog.size());
  	
  		}
  		catch(Exception ex)
  		{
  			ex.printStackTrace();
  		}
  	}
    
    
    public void getReversalTransaction()
  	{
  		try
  		{
  			ReportModel reportModel = new ReportModel();
  			cardholdertranLog.clear();
  			
  			
  			String cardnum = getCard_num();
  			
  			String merchantCode = getMerchant_code();
  			
  			String uniqueTransId = getSubscriber_id();
  			
  			
  			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
  	        
  			String beginDate = df.format(getStart_date());
  	        beginDate = beginDate + " " + getStart_hr() + ":00";
  	        
  	        String endDate = df.format(getEnd_date());
  	        endDate = endDate + " " + getEnd_hr() + ":59";
  			
  	        E_TRANSACTION tran = null;
  	        double d = 0.0;
  			
          	cardholdertranLog = reportModel.getReversalTransactions(cardnum,merchantCode,uniqueTransId,beginDate, endDate);
          	System.out.println("cardholdertranLog ====  "+cardholdertranLog.size());
  	        for(int i=0;i<cardholdertranLog.size();i++)
  	        {
  	        	tran = (E_TRANSACTION)cardholdertranLog.get(i);
  	        	d += Double.parseDouble(tran.getTrans_amount());	        	
  	        }
  	        setTotal_amount(d);
  	        
  		}
  		catch(Exception ex)
  		{
  			ex.printStackTrace();
  		}
  	}

    public void getPOSMerchantReport()
   	{
   		try
   		{
   			merchantReportLog.clear();
   			ReportModel reportModel = new ReportModel();
   	
   			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
   	
   	        String beginDate = df.format(getStart_date());
   	        beginDate = beginDate + " " + getStart_hr() + ":00";
   	
   	        String endDate = df.format(getEnd_date());
   	        endDate = endDate + " " + getEnd_hr() + ":59";
   	        
   	        String etz_pos_merchant = "0560019910";
   	
   	        merchantReportLog = reportModel.getPOSMerchantReport(etz_pos_merchant, beginDate, endDate);
 	        
   		}
   		catch(Exception ex)
   		{
   			ex.printStackTrace();
   		}
   	}
    
    
    public void getAppsByMobile()
   	{
   		try
   		{
   			appList.clear();
   			mobileSubscriberLog.clear();
   			mobileSubscriberCardLog.clear();
   			
   			setBank_code(null);
   			ReportModel reportModel = new ReportModel();   	
   			String m = getFrom_source();
   			
   			System.out.println("mobile " + getFrom_source().trim());
   			
			if(m!= null && m.length()>0)
   			{
				if(getLine_type().equals("Version II"))
	   			{
					appList = reportModel.getAppsByMobileNo(m);
	   			}
				else
	   			{
	   				appList.clear();
	   				facesMessages.add(Severity.WARN, "Version I Has No Multiple Apps Configuration.");
	   			}
   			}
   			else
   			{
   				facesMessages.add(Severity.WARN, "Mobile Number is required.");
   			}
   		}
   		catch(Exception ex)
   		{
   			ex.printStackTrace();
   		}
   	}
    
    
    
    /*getRechargePinLog*/
    public void getRechargePinLog()
   	{
   		try
   		{
   			pinLog.clear();
   			ReportModel reportModel = new ReportModel();   	
   			
   			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
   		   	
   	        String beginDate = df.format(getStart_date());
   	        beginDate = beginDate + " " + getStart_hr() + ":00";
   	
   	        String endDate = df.format(getEnd_date());
   	        endDate = endDate + " " + getEnd_hr() + ":59";
   			
   	        FacesContext context = FacesContext.getCurrentInstance();
			String merchant_code = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();

   	        
   			System.out.println("m " + merchant_code);
   			String a = merchant_code;
   			String b = getTerminal_id();
   			pinLog = reportModel.getRechargePinLog(a, b, beginDate, endDate);
	   			
   		}
   		catch(Exception ex)
   		{
   			ex.printStackTrace();
   		}
   	}
    
    
    
    /*Method for the pocket moni total number of users*/
	public void getPocketMoniTotalNumberOfUsers()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			mmoneyLog.clear();
	        mmoneyLog = reportModel.getPocketMoniTotalNumberOfUsers();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
    
     
    /*Method for the pocket moni summary reports*/
	public void getPocketMoniSummaryTransactions()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			mmoneyLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
	        double b = 0.0;
	        
	        mmoneyLog = reportModel.getPocketMoniSummaryByDateAndType(beginDate, endDate, getChannel_id(), getCard_num().trim());
	        if(mmoneyLog.size()>0)
	        {
		        for(int i=0;i<mmoneyLog.size();i++)
		        {
		        	E_TRANSACTION et = (E_TRANSACTION)mmoneyLog.get(i);
		        	b += Double.parseDouble(et.getTrans_amount());
		        }
		        setTotal_amount(b);
	        }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
    
	
	/*This method is used to get payment reports for the phcn district officers*/
	public void getPHCNReport()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			billLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
	        String optionType = getOptionType();
	        String tarrifType = getTarrif_type();
	        String issuercode = getBank_code();
	        String channelid = getChannel_id();
	        String subcode = getBranch_code(); 
	        String meterno = getMeterno();
	 
	        if(subcode == null)
	        {
	        	subcode = "";
	        }
	        if(meterno==null)
	        {
	        	meterno = "";
	        }
	        
	        if(channelid == null)
	        {
	        	channelid = "";
	        }
	        
	        if(issuercode == null)
	        {
	        	issuercode = "";
	        } 

	       
	        double b = 0.0;
	        double c = 0.0;
	        double e = 0.0;
	    	String merchantCode = "";
	    	String[] d = null;
	    	
			if(tarrifType.equals("PHCN: TARRIFF"))
			{
				FacesContext context = FacesContext.getCurrentInstance();
				String disco = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
				d = disco.split(":");//first param is the zone, second param the district
		    	
				merchantCode += reportModel.getDistinctPostpaidPHCNMerchantCode(d[0], d[1]);
			}
			else if(tarrifType.equals("PHCN: PREPAID"))
			{
				FacesContext context = FacesContext.getCurrentInstance();
				String disco = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
				d = disco.split(":");
				if(d[0].equalsIgnoreCase("EKO"))
		    	{
		    		//merchantCode += "7006020013:7006020014:7006020015";
		    		merchantCode += reportModel.getDistinctPostpaidPHCNMerchantCode(d[0], d[1]);
		    		//merchantCode += "7006020013:7006020014:7006020015";
		    	}
			}
			else
			{
				FacesContext context = FacesContext.getCurrentInstance();
				String disco = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
				d = disco.split(":");
				
				merchantCode += reportModel.getDistinctPostpaidPHCNMerchantCode(d[0], d[1]);
				if(d[0].equalsIgnoreCase("EKO"))
		    	{
		    		//merchantCode += "7006020013:7006020014:7006020015";
					merchantCode += reportModel.getDistinctPostpaidPHCNMerchantCode(d[0], d[1]);
					//merchantCode += "7006020013:7006020014:7006020015";
		    	}
					//7006020007 7006020001 2140010004
			}
			
		
	        billLog = reportModel.getPHCNAdminReport(beginDate, endDate, optionType,
	        		merchantCode, tarrifType, issuercode, subcode, channelid, meterno,d[0],d[1]);
	        
	        System.out.println("Bill Log     "+billLog.size());
	        
	        if(billLog.size()>0)
	        {
		        for(int i=0;i<billLog.size();i++)
		        {
		        	PAYTRANS et = (PAYTRANS)billLog.get(i);
		        	b += Double.parseDouble(et.getTrans_amount());
		        	c += Double.parseDouble(et.getEtzCommissionAmt());
		        	e += Double.parseDouble(et.getNetAmt());
		        }
		        setTotal_amount(b);
		        setTotal_etz_amount(c);
		        setTotal_other_amount(e);
	        }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*This method is used to get the ctransation process to monitor URLMessenger*/
	public void getCTransactionProcess()
	{
		try
		{
			supportLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
	        supportLog = reportModel.getCTransactionProcess(beginDate, endDate);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*Bizdev Settlement Summary Report*/
	public void getBizdevSettlementSummary()
	{
		try
		{
			supportLog.clear();
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        

	        FacesContext context = FacesContext.getCurrentInstance();
			String type = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getType_id();
			String serviceId = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
			if(type.equals("1") || type.equals("2"))//support/admin
			{
				String bizDev = getStatus_id();
				supportLog = reportModel.getBizdevSettlementSummary(beginDate, endDate, bizDev);
			}
			else
			{
				supportLog = reportModel.getBizdevSettlementSummary(beginDate, endDate, serviceId);
			}
	        
	        
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*Settlement Summary Report*/
	public void getSettlementSummary()
	{
		try
		{
			supportLog.clear();
			setTotalTranSum(0);
			setTotalTranCount(0);
			setTotalSettleCount(0);
			setTotalSettleSum(0);
			setTotalFeeSum(0);
			setTotalFeeCount(0);
			
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        

	        
	        supportLog = reportModel.getSettlementSummary(beginDate, endDate);
	        for(int i=0;i<supportLog.size();i++)
	        {
	        	String[] s = (String[])supportLog.get(i);
	        	totalTranCount += Integer.parseInt(s[1]);
	        	totalTranSum += Double.parseDouble(s[2]);
	        	totalSettleCount += Integer.parseInt(s[3]);
	        	totalSettleSum += Double.parseDouble(s[4]);
	        	totalFeeCount += Integer.parseInt(s[5]);
	        	totalFeeSum += Double.parseDouble(s[6]);
	        }
	        
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*Settlement Details Report*/
	public void getSettlementDetails()
	{
		try
		{
			merchantReportLog.clear();
			setTotalTranSum(0);
			setTotalTranCount(0);
			setTotalSettleCount(0);
			setTotalSettleSum(0);
			setTotalFeeSum(0);
			setTotalFeeCount(0);
			
			ReportModel reportModel = new ReportModel();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
	        String transDesc = getId();
	        
	        //System.out.println("transDesc " + transDesc);
	        merchantReportLog = reportModel.getSettlementDetails(beginDate, endDate, transDesc);
	        for(int i=0;i<merchantReportLog.size();i++)
	        {
	        	String[] s = (String[])merchantReportLog.get(i);
	        	totalTranCount += Integer.parseInt(s[2]);
	        	totalTranSum += Double.parseDouble(s[3]);
	        	totalSettleCount += Integer.parseInt(s[4]);
	        	totalSettleSum += Double.parseDouble(s[5]);
	        	totalFeeCount += Integer.parseInt(s[6]);
	        	totalFeeSum += Double.parseDouble(s[7]);
	        }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*This method is used to process Payment Advice*/
	public void processPaymentAdvice()
	{
		try
		{
			String message = "";
			String mcode = "";
			String lname = "";
			String fname = "";
			String email = "";
			
			ReportModel reportModel = new ReportModel();
			
			
			try
			{
				FacesContext context = FacesContext.getCurrentInstance();
				mcode = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
				lname = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getLastname();
				fname = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getFirstname();
				email = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getEmail();
				
				getCurUser().setLastname(lname);
				getCurUser().setFirstname(fname);
				getCurUser().setEmail(email);
				setMerchant_code(mcode);
				setCard_num(getCard_num());
				
				
				Events.instance().raiseEvent("paymentadvicemailer");
				message = "Payment advice has been sent successfully";
			}
			catch(Exception e)
			{
				e.printStackTrace();
				message = "Email not sent " + e.getMessage();
			}
			
			facesMessages.add(Severity.INFO, message);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
    
	/*This is used for the exporting*/
	public void exportPHCN(String param) 
	{
        try 
        {
        	double totalAmt = 0.0;
        	int cnt = 0;
        	String blank = " ";
        	String longblank = "                       ";
        	context = FacesContext.getCurrentInstance();
            HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();            
        	
            String folder = "c:/phcnCSV";
            String filenm = folder + "/phcn." + param;
        	
        	File f = new File(folder);
        	if(f.exists());
        	else
        	{
        		f.mkdir();
        	}
        	
        	
        	
        	FileWriter writer = new FileWriter(filenm);
        	if(param.equalsIgnoreCase("txt"))//having a different if for txt becos of the format of download
        	{
        		String txtDate = "";
        		PAYTRANS et = null;
        		
        		
        		for(int i=0;i<billLog.size();i++)
    	        {
        			et = (PAYTRANS)billLog.get(i);
        			txtDate = et.getTrans_date();

        			txtDate = txtDate.substring(0, txtDate.indexOf(" "));
                    String[] data = txtDate.split("-");
                    String yr = data[0];
                    String mm = data[1];
                    String dy = data[2];
                    txtDate = dy + "/" + mm + "/" + yr;
    	   	        
                    totalAmt += Double.parseDouble(et.getTrans_amount());
                    System.out.println("single payments " + et.getTrans_amount());
    	        }
        		
        		String onlyAmt = ""+totalAmt;
                cnt = onlyAmt.substring(onlyAmt.indexOf(".")+1).length();
                System.out.println("cnt " + cnt);
        		if(cnt == 1)
        		{
        			onlyAmt = onlyAmt + "0";
        		}
        		else if(cnt > 2)
                {
                    onlyAmt = onlyAmt.substring(0, onlyAmt.indexOf(".")+3);
                }
        		
        		System.out.println("onlyAmt " + onlyAmt);

        		String amt = onlyAmt;
        		String vAmt = "";
        		int standardLength = 11; 
        		int remLength = 0;
        		String paddedZeros = "";
        		if(amt.length() != 11)
        		{
        			int a = amt.length();
        			remLength = standardLength - a;
        			for(int i=0;i<remLength;i++)
        			{
        				paddedZeros += "0";
        			}
        			amt = paddedZeros +  onlyAmt;
        		}
        		
	        	writer.append(txtDate);//date
	    	    writer.append("7777");//etranzact batch number
	    	    writer.append(amt);//batch total
	    	    writer.append("Batch");//Batch
	    	    writer.append(blank);//Blank Space
	    	    writer.append("7777");//unique batch number
	    	    writer.append(blank);//Blank Space
	    	    writer.append("e-Payments");//e-Payments
	    	    writer.append(blank);//Blank Space
	    	    writer.append("-");//Hyphen
	    	    writer.append(blank);//Blank Space
	    	    writer.append("ETZ");//ETZ
	    	    writer.append(longblank);//Blank Space
	    	    writer.append("001001");//File identifier code
	    	    writer.append("\n");
        		
        		for(int i=0;i<billLog.size();i++)
    	        {
    	        	et = (PAYTRANS)billLog.get(i);
    	        	
    	        	writer.append(et.getSubscriber_id().trim().replaceAll(",", " "));//account no
    	    	    
    	        	
    	        	vAmt = et.getTrans_amount().trim();
            		remLength = 0;
            		paddedZeros = "";
            		if(vAmt.length() != 11)
            		{
            			int a = vAmt.length();
            			remLength = standardLength - a;
            			for(int m=0;m<remLength;m++)
            			{
            				paddedZeros += "0";
            			}
            			vAmt = paddedZeros +  vAmt;
            		}
            		
    	        	writer.append(vAmt);
    	    	    writer.append("1");//Constant 1
    	    	    writer.append(blank);//Blank space
    	    	    if(et.getUnique_trans_id().length() > 16)
    	    	    {
    	    	    	writer.append(et.getUnique_trans_id().substring(et.getUnique_trans_id().length()-16));//receipt no
    	    	    }
    	    	    else if(et.getUnique_trans_id().length() < 16)
    	    	    {
    	    	    	int a = et.getUnique_trans_id().length();
    	    	    	remLength = 0;
    	    	    	remLength = 16 - a;
    	    	    	paddedZeros = "";
            			for(int m=0;m<remLength;m++)
            			{
            				paddedZeros += "0";
            			}
            			writer.append(et.getUnique_trans_id() + paddedZeros);//receipt no
    	    	    }
    	    	    else
    	    	    {
    	    	    	writer.append(et.getUnique_trans_id());//receipt no
    	    	    }
    	    	    writer.append("\n");
    	        }
        	}
        	else
        	{
        		for(int i=0;i<billLog.size();i++)
    	        {
    	        	PAYTRANS et = (PAYTRANS)billLog.get(i);
    	        	
    	        	writer.append("ETZ");//unique identifier
    	    	    writer.append(';');
    	    	    writer.append(getChannelName(et.getTrans_channel().trim()).replaceAll(",", "	"));//channel
    	    	    writer.append(';');
    	    	    writer.append("	");//location of tran
    	    	    writer.append(';');
    	    	    
    	    	    if(et.getStatus_desc().equals("0|0|0|0"))
    	    	    {
    	    	    	writer.append("POSTPAID");//customer type
    		    	    writer.append(';');
    	    	    }
    	    	    else
    	    	    {
    	    	    	writer.append("PREPAID");//customer type
    		    	    writer.append(';');
    	    	    }
    	    	    writer.append(et.getUnique_trans_id().trim().replaceAll(",", "	"));//trans_ref
    	    	    writer.append(';');
    	    	    writer.append("	");//receipt no
    	    	    writer.append(';');
    	    	    writer.append(et.getSubscriber_id().trim().replaceAll(",", "	"));//account number/meter_no
    	    	    writer.append(';');
    	    	    writer.append(et.getCard_fullname().trim().replaceAll(",", "	"));//customer name
    	    	    writer.append(';');
    	    	    writer.append(et.getTrans_amount().trim().replaceAll(",", "	"));//amount
    	    	    writer.append(';');
    	        	writer.append(et.getTrans_date().trim().replaceAll(",", "	"));//date
    	    	    writer.append(';');
    	    	    writer.append("	");//disco
    	    	    writer.append(';');
    	    	    writer.append("	");//district
    	    	    writer.append(';');
    	    	    writer.append("	");//phone
    	    	    writer.append(';');
    	    	    writer.append('\n');
    	    	    
    	        }
        	}

    	    //generate whatever data you want
     
    	    writer.flush();
    	    writer.close();
        	
    	    
    	    java.io.File f_ = new File("c:/phcnCSV/phcn." + param);
        	
            InputStream is = new FileInputStream(f_);
            long length = f_.length();
            byte[] bytes = new byte[(int) length];
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file " + f_.getName());
            }
            is.close();

            context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.setHeader("Content-disposition",
                    "attachment; filename= " + f_.getName());
            response.setContentLength(bytes.length);
            response.setContentType("application/vnd.ms-excel");
            if(param.indexOf("pdf") > - 1)
            {
            	   response.setContentType("application/pdf");
            	   exportPHCNPdf();
            }
            try {

                response.getOutputStream().write(bytes);
                response.getOutputStream().flush();
                response.getOutputStream().close();
                context.responseComplete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception dd) {
            dd.printStackTrace();
        }
    }
    /**
     * 
     * This is method is to export report to pdf
     */
	public void exportPHCNPdf()
	{
		
		 try 
		 {
	            OutputStream file = new FileOutputStream(new File("C:\\PhcnReport.pdf"));
	 
	            Document document = new Document();
	            PdfWriter.getInstance(document, file);
	            document.open();
	            document.add(new Paragraph("Checking..."));
	            document.add(new Paragraph(new Date().toString()));
	 
	            document.close();
	            file.close();
	 
	     } 
		 catch (Exception e) 
		 {
	 
	            e.printStackTrace();
	     }
	  
	}
	
    /*Send Recharge PIN*/
	public void sendRechargePIN()
	{
		try
		{
			String message = "";
			try
			{
				String lname = "";
				String fname = "";
				String email = "";
				
				
				FacesContext context = FacesContext.getCurrentInstance();
				lname = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getLastname();
				fname = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getFirstname();
				email = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getEmail();
				
				getCurUser().setLastname(lname);
				getCurUser().setFirstname(fname);
				getCurUser().setEmail(email);
				
				
				
				String pin = getMobileno().substring(0, getMobileno().indexOf(":"));
				String serial_no = getMobileno().substring(getMobileno().indexOf(":")+1);
				
				System.out.println("pin " + pin);
				System.out.println("serial_no " + serial_no);
				
				PBEncryptor pb = new PBEncryptor();
				String real_pin = pb.PBDecrypt(pin, serial_no);
				setEdit_id(real_pin);
				
				System.out.println("real_pin " + real_pin);
				
				
				Events.instance().raiseEvent("rechargepinmailer");
				message = "Email has been sent successfully";
			}
			catch(Exception e)
			{
				e.printStackTrace();
				message = "Email not sent " + e.getMessage();
			}
			
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*Airtime Sales Report*/
	public void airTimeSales()
	{
		try
		{
			String message = "";
			try
			{
				supportLog.clear();
	   			ReportModel reportModel = new ReportModel();   	
	   			
	   			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	   		   	
	   	        String beginDate = df.format(getStart_date());
	   	        beginDate = beginDate + " " + getStart_hr() + ":00";
	   	
	   	        String endDate = df.format(getEnd_date());
	   	        endDate = endDate + " " + getEnd_hr() + ":59";

	   	        supportLog = reportModel.airTimeSales(beginDate, endDate);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*Airtime Sales Breakdown Report*/
	public void airTimeSalesBreakDown()
	{
		try
		{
			String message = "";
			try
			{
				mmoneyLog.clear();
	   			ReportModel reportModel = new ReportModel();   	
	   			
	   			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	   		   	
	   	        String beginDate = df.format(getStart_date());
	   	        beginDate = beginDate + " " + getStart_hr() + ":00";
	   	
	   	        String endDate = df.format(getEnd_date());
	   	        endDate = endDate + " " + getEnd_hr() + ":59";
	   	        
	   	        String merchant_code = getId().trim();

	   	        mmoneyLog = reportModel.airTimeSalesBreakDown(merchant_code, beginDate, endDate);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*Airtime Sales Report*/
	public void airTimeSalesGroupBy()
	{
		try
		{
			String message = "";
			try
			{
				supportLog.clear();
	   			ReportModel reportModel = new ReportModel();   	
	   			
	   			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	   		   	
	   	        String beginDate = df.format(getStart_date());
	   	        beginDate = beginDate + " " + getStart_hr() + ":00";
	   	
	   	        String endDate = df.format(getEnd_date());
	   	        endDate = endDate + " " + getEnd_hr() + ":59";

	   	        supportLog = reportModel.airTimeSalesGroupBy(beginDate, endDate);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	public void getPHCNZoneSettlement()
	  {
	    try
	    {
	      ReportModel reportModel = new ReportModel();
	      this.supportLog.clear();

	      DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	      String beginDate = df.format(getStart_date());
	      beginDate = beginDate + " " + getStart_hr() + ":00";

	      String endDate = df.format(getEnd_date());
	      endDate = endDate + " " + getEnd_hr() + ":59";

	      FacesContext context = FacesContext.getCurrentInstance();
	      String zone = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();

	      String district = getLine_type();

	      double b = 0.0D;

	      this.supportLog = reportModel.getPHCNSettlement(zone, district, beginDate, endDate);
	      if (this.supportLog.size() > 0)
	      {
	        for (int i = 0; i < this.supportLog.size(); i++)
	        {
	          String[] et = (String[])this.supportLog.get(i);
	          b += Double.parseDouble(et[4]);
	        }
	        setTotal_amount(b);
	      }
	    }
	    catch (Exception ex)
	    {
	      ex.printStackTrace();
	    }
	  }

	
	
	
	
  public void getPHCNDistrictSettlement()
  {
    try
    {
      ReportModel reportModel = new ReportModel();
      this.supportLog.clear();

      DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

      String beginDate = df.format(getStart_date());
      beginDate = beginDate + " " + getStart_hr() + ":00";

      String endDate = df.format(getEnd_date());
      endDate = endDate + " " + getEnd_hr() + ":59";

      double b = 0.0D;
      String[] d = (String[])null;

      FacesContext context = FacesContext.getCurrentInstance();
      String disco = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
      d = disco.split(":");

      this.supportLog = reportModel.getPHCNSettlement(d[0], d[1], beginDate, endDate);
      if (this.supportLog.size() > 0)
      {
        for (int i = 0; i < this.supportLog.size(); i++)
        {
          String[] et = (String[])this.supportLog.get(i);
          b += Double.parseDouble(et[4]);
        }
        setTotal_amount(b);
      }

    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
	
  /*Glo Registration Report*/
	public void getGloRegistrationReport()
	{
		try
		{
			String message = "";
			try
			{
				mmoneyLog.clear();
	   			ReportModel reportModel = new ReportModel();   	
	   			
	   			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	   		   	
	   	        String beginDate = df.format(getStart_date());
	   	        beginDate = beginDate + " " + getStart_hr() + ":00";
	   	
	   	        String endDate = df.format(getEnd_date());
	   	        endDate = endDate + " " + getEnd_hr() + ":59";
	   	        
	   	        mmoneyLog = reportModel.getGloRegistrationReport(beginDate, endDate);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
  
  
	/*Glo Transaction Report*/
	public void getGloTransactionReport()
	{
		try
		{
			String message = "";
			try
			{
				mmoneyLog.clear();
	   			ReportModel reportModel = new ReportModel();   	
	   			
	   			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	   		   	
	   	        String beginDate = df.format(getStart_date());
	   	        beginDate = beginDate + " " + getStart_hr() + ":00";
	   	
	   	        String endDate = df.format(getEnd_date());
	   	        endDate = endDate + " " + getEnd_hr() + ":59";
	   	        
	   	        double b = 0.0d;
	   	        
	   	        mmoneyLog = reportModel.getGloTransactionReport(beginDate, endDate);
	   	        if(mmoneyLog.size() > 0)
	   	        {
	   	        	for(int i=0;i<mmoneyLog.size();i++)
			        {
			        	String[] et = (String[])mmoneyLog.get(i);
			        	b += Double.parseDouble(et[6]);
			        }
	   	        	setTotal_amount(b);
	   	        }
	   	        
		       
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
  
	public void convertToFile(byte[] b ,String name)
	{
		try 
		{			
			File g = new File("");
            Properties props = new Properties();
            InputStream in = (InputStream)(new FileInputStream(new File(g.getAbsolutePath() + "\\" + "xportaldb-config.properties")));
            props.load(in);
            String setup_excel_folder = props.getProperty("setup_excel");
	        File f = new File(setup_excel_folder);
			if(f.exists())
			{
				java.io.FileOutputStream fout = new java.io.FileOutputStream(setup_excel_folder + "\\" + name);
				fout.write(b);
				fout.close();
			}
			else
			{
				f.mkdirs();
				java.io.FileOutputStream fout = new java.io.FileOutputStream(setup_excel_folder + "\\" + name);
				fout.write(b);
				fout.close();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
    
     public void export()
     {
         java.io.File f_ = new ExcelWriter().write2(this.merchantReportLog);
	     try 
	     {
	         InputStream is = new FileInputStream(f_);
	         long length = f_.length();
	         byte[] bytes = new byte[(int) length];
	         int offset = 0;
	         int numRead = 0;
	         while (offset < bytes.length
	                 && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
	             offset += numRead;
	         }
	         if (offset < bytes.length) {
	             throw new IOException("Could not completely read file " + f_.getName());
	         }
	         is.close();
	
	         FacesContext context = FacesContext.getCurrentInstance();
	         HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	         response.setHeader("Content-disposition",
	                 "attachment; filename= " + f_.getName());
	         response.setContentLength(bytes.length);
	         response.setContentType("application/vnd.ms-excel");
	         try 
	         {
	
	             response.getOutputStream().write(bytes);
	             response.getOutputStream().flush();
	             response.getOutputStream().close();
	             context.responseComplete();
	         } catch (IOException e) {
	             e.printStackTrace();
	         }
	     } catch (Exception dd) {
	         dd.printStackTrace();
	     }
     }

    
	
     FundPocketMoniLog fundPocketMoniLog;
     private ArrayList<FundPocketMoniLog> loadPmLog = new ArrayList();
	private XProcessor processor;
     private static String pmServerIP = "172.16.10.134";
     private static int XProcessorserverPortPM = 9999;

     public ArrayList<FundPocketMoniLog> getLoadPmLog() {
         return loadPmLog;
     }

     public void setLoadPmLog(ArrayList<FundPocketMoniLog> loadPmLog) {
         this.loadPmLog = loadPmLog;
     }

     public FundPocketMoniLog getFundPocketMoniLog() {
         if (fundPocketMoniLog == null) {
             fundPocketMoniLog = new FundPocketMoniLog();
         }
         return fundPocketMoniLog;
     }

     public void setFundPocketMoniLog(FundPocketMoniLog fundPocketMoniLog) {
         this.fundPocketMoniLog = fundPocketMoniLog;
     }

     public void viewLoadPMReport() 
     {
         try 
         {
             loadPmLog.clear();
             ReportModel reportModel = new ReportModel();

             DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

             String beginDate = df.format(getStart_date());
             beginDate = beginDate + " " + getStart_hr() + ":00";

             String endDate = df.format(getEnd_date());
             endDate = endDate + " " + getEnd_hr() + ":59";

             loadPmLog = reportModel.getFundPmSummary(this.getFundPocketMoniLog().getSearchDate1(), this.getFundPocketMoniLog().getSearchDate2(), this.getFundPocketMoniLog().getSearchTranRef(), this.getFundPocketMoniLog().getSearchAction());
         } 
         catch (Exception ex) {
             ex.printStackTrace();
         }
     }

     public void reProcessPmFunding() 
     {
    	ReportModel rm = new ReportModel();
	    for(int t=0;t<loadPmLog.size();t++)
	    {
	         com.etranzact.supportmanager.dto.FundPocketMoniLog fundPocketMoniLog = (com.etranzact.supportmanager.dto.FundPocketMoniLog)loadPmLog.get(t);
	         XRequest request = new XRequest();
	         XProcessor processor = new XProcessor();
	         HttpHost host = new HttpHost();
	         host.setPort(XProcessorserverPortPM);
	         host.setServerAddress(pmServerIP);
	         XResponse response = null;
	         String ft900Notification = "<eft_notify>"
	                 + "<MobileNumber>" + fundPocketMoniLog.getPhonenumber() + "</MobileNumber>"
	                 + "<Amount>" + fundPocketMoniLog.getAmount() + "</Amount>"
	                 + "<Authvalue></Authvalue>"
	                 + "<eft_notify>";
	         String crd = this.doCardQuery(fundPocketMoniLog.getPhonenumber());
	         String mCsourceCard = crd.substring(0, 3) + "0000000000000";
	         String mCdestnationCard = crd;
	         try 
	         {
	             Card card = new Card();
	             card.setCardExpiration("072015");
	             card.setCardNumber(mCsourceCard);//0910010705222055
	             card.setCardPin("0000");//2009
	             card.setNewPin("0000");
	             request.setTransAmount(Double.parseDouble(fundPocketMoniLog.getAmount()));
	             request.setCard(card);
	             request.setReference(fundPocketMoniLog.getTransactionRef());
	             request.setTransCode(TransCode.NOTIFICATION);
	             request.setChannelId("01");
	             request.setDescription(fundPocketMoniLog.getPhonenumber() + " Account Funding");
	             request.setFee(0);
	             request.setXmlString(ft900Notification);
	             request.setMerchantCode(crd);
	             request.setOtherReference("t");
	             response = processor.process(host, request);
	             if (response == null || response.getResponse() != 0) 
	             {
	                 response = new XResponse();
	                 response.setResponse(-1);
	             }
	
	             System.out.println(response.getMessage() + "   " + response.getResponse());
	
	             if (response.getResponse() != 0) {}
	
	         } catch (Exception s) {
	             s.printStackTrace();
	             response = new XResponse();
	             response.setResponse(-1);
	         }
	
	         rm.updateFundPMLog(fundPocketMoniLog.getTransactionRef(), fundPocketMoniLog.getPhonenumber(), response.getResponse());
	
	     }
     }

    
     private String doCardQuery(String phoneNo) {
         com.etz.http.etc.XProcessor processor = new com.etz.http.etc.XProcessor();
         com.etz.http.etc.HttpHost host = new com.etz.http.etc.HttpHost();
         com.etz.http.etc.TransCode tc = new com.etz.http.etc.TransCode();
         host.setServerAddress(pmServerIP);
         host.setPort(XProcessorserverPortPM);
         String key = "123456";
         host.setSecureKey(key);
         com.etz.http.etc.VCardRequest request2 = new com.etz.http.etc.VCardRequest();
         request2.setMobileNumber(phoneNo);
         request2.setOtherReference("1ESA" + new com.etranzact.drs.utility.Utility().generateRandomNumber(10));
         request2.setRequestType(tc.CARDQUERY);
         request2.setSchemeCode(this.getFundPocketMoniLog().getOperatorType());
         try {
             com.etz.http.etc.VCardRequest response2 = processor.process(host, request2);
             System.out.println("Response code:" + response2.getResponse());
             if (response2.getResponse() == 0) {

                 
                 System.out.println("Response Message:" + response2.getCardNumber());
                 System.out.println("Response Message:" + response2.getFirstName());
                 System.out.println("Response Message:" + response2.getLastName());
                 System.out.println("Response Message:" + response2.getAccountNumber());
                 System.out.println("Response Message:" + response2.getCardBalance());
                 System.out.println("Response Message:" + response2.getStatus());
                 return (response2.getCardNumber());
             } else {
                 return "";
             }
         } catch (Exception ss) {
             ss.printStackTrace();
         }
         return "";
     } 
     
     
     
     
     

	public void resetValues()
	{
		System.out.println("reset data");
		
		supportLog.clear();
		supportLog = new ArrayList();
		vtuLog.clear();
		billLog.clear();
		failedFundsLog.clear();
		mobileSubscriberLog.clear();
		mobileSubscriberCardLog.clear();
		failedSummaryLog.clear();
		failedSummaryDrillDownLog.clear();
		summaryLog.clear();
		tmcEventLog.clear();
		pinLog.clear();
		tmcRequestLog.clear();
		tranLog.clear();
		
		merchantSummaryLog.clear();
		merchantSplitLog.clear();
		
		merchantReportLog.clear();
		merchantSummaryReportLog.clear();
		merchantSearchLog.clear();
		settle_batch.clear();
		revenueReportLog.clear();
		
		allMerchantBankReportLog.clear();
		allMerchantTransReportLog.clear();
		allMerchantChannelReportLog.clear();
		allMerchantReportLog.clear();
		uploadList.clear();
		
		merchantSplitFormula.clear();
		
		mmoneyLog.clear();
		mmoneySummaryLog.clear();
		switchReportLog.clear();
		cardserviceLog.clear();
		cardholdertranLog.clear();
		cardholdertranByMerchantCodeLog.clear();
		failedFundsLog.clear();
		fundsTransfer.clear();
		tranSummaryList.clear();
		transPool.clear();
		transPoolList.clear();
		appList.clear();
		setSelected(false);
		
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
		setFrom_source(null);
		setChannel_id(null);
		setCard_num(null);
		setMerchant_code(null);
		setStatus_id(null);
		setStart_date(null);
		setEnd_date(null);
		setStart_hr(null);
		setEnd_hr(null);
		setTerminal_id(null);
		setBank_code(null);
		setVsmFullName(null);
		setFirstname(null);
		setLastname(null);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
			Date d = df.parse(df.format(new Date()));
			setStart_date(d);
			setEnd_date(d);
		}
		catch(Exception ex){ex.printStackTrace();}
		
		setStart_hr("00");
		setEnd_hr("23");
 	}
	
	
	public void mobile_resetValues()
	{
		
		supportLog.clear();
		supportLog = new ArrayList();
		vtuLog.clear();
		billLog.clear();
		failedFundsLog.clear();
		mobileSubscriberLog.clear();
		mobileSubscriberCardLog.clear();
		failedSummaryLog.clear();
		failedSummaryDrillDownLog.clear();
		summaryLog.clear();
		tmcEventLog.clear();
		pinLog.clear();
		tmcRequestLog.clear();
		
		
		setFrom_source(null);
		setChannel_id(null);
		setCard_num("234");
		setMerchant_code(null);
		setStatus_id(null);
		setStart_date(null);
		setEnd_date(null);
		setStart_hr(null);
		setEnd_hr(null);
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
			Date d = df.parse(df.format(new Date()));
			setStart_date(d);
			setEnd_date(d);
		}
		catch(Exception ex){ex.printStackTrace();}
		
		setStart_hr("00");
		setEnd_hr("23");
        
 	}
	
	
	public void cardholders_resetValues()
	{
		merchantSummaryLog.clear();
		merchantSplitLog.clear();
		
		merchantReportLog.clear();
		merchantSummaryReportLog.clear();
		merchantSearchLog.clear();
		settle_batch.clear();
		revenueReportLog.clear();
		
		allMerchantBankReportLog.clear();
		allMerchantTransReportLog.clear();
		allMerchantChannelReportLog.clear();
		allMerchantReportLog.clear();
		
		merchantSplitFormula.clear();
		
		mmoneyLog.clear();
		mmoneySummaryLog.clear();
		switchReportLog.clear();
		
		
		setMerchant_code(null);
		setMerchant_name(null);
		setOptionType("U");
		setOptionType2("SR");
		setStrParam(null);
		setChannel_id(null);
		setTrans_code(null);
		setLine_type(null);
		setCard_num(null);
		setMobileno(null);
		
		setStart_date(null);
		setEnd_date(null);
		setStart_hr(null);
		setEnd_hr(null);
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
			Date d = df.parse(df.format(new Date()));
			setStart_date(d);
			setEnd_date(d);
		}
		catch(Exception ex){ex.printStackTrace();}
		
		setStart_hr("00");
		setEnd_hr("23");
 	}
	
	
	public void checkChannel()
	{
		setCard_num(null);
		setMerchant_code(null);
	}
	
	public T_SMS_RECEIVE getSms_receive() 
	{
		if(sms_receive == null)
		{
			sms_receive = new T_SMS_RECEIVE();
		}
		
		return sms_receive;
	}

	public void setSms_receive(T_SMS_RECEIVE sms_receive) {
		this.sms_receive = sms_receive;
	}

	

	public String getFrom_source() {
		return from_source;
	}


	public void setFrom_source(String from_source) {
		this.from_source = from_source;
	}


	public String getTo_dest() {
		return to_dest;
	}


	public void setTo_dest(String to_dest) {
		this.to_dest = to_dest;
	}

	public ArrayList<REQUEST_LOG> getFailedFundsLog() 
	{
		return failedFundsLog;
	}


	public void setFailedFundsLog(ArrayList<REQUEST_LOG> failedFundsLog) {
		this.failedFundsLog = failedFundsLog;
	}


	public ArrayList<VTU_LOG> getVtuLog() {
		return vtuLog;
	}


	public void setVtuLog(ArrayList<VTU_LOG> vtuLog) {
		this.vtuLog = vtuLog;
	}


	public ArrayList<PAYTRANS> getBillLog() {
		return billLog;
	}


	public void setBillLog(ArrayList<PAYTRANS> billLog) {
		this.billLog = billLog;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public ArrayList<E_TRANSACTION> getTranLog() 
	{
		return tranLog;
	}


	public void setTranLog(ArrayList<E_TRANSACTION> tranLog) {
		this.tranLog = tranLog;
	}


	public ArrayList<Summary> getSummaryLog() {
		return summaryLog;
	}


	public void setSummaryLog(ArrayList<Summary> summaryLog) {
		this.summaryLog = summaryLog;
	}
	

	public ArrayList<Channel> getChannelList() 
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			channelList = reportModel.getChannel();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return channelList;
	}


	public void setChannelList(ArrayList<Channel> channelList) {
		this.channelList = channelList;
	}



	public String getStatus_id() 
	{
		return status_id;
	}


	public void setStatus_id(String status_id) {
		this.status_id = status_id;
	}


	public User getCurUser() {
		if(curUser == null)
		{
			curUser = new User();
		}
		
		return curUser;
	}


	public void setCurUser(User curUser) {
		this.curUser = curUser;
	}


	public ArrayList<E_MOBILE_SUBSCRIBER> getMobileSubscriberLog() {
		return mobileSubscriberLog;
	}


	public void setMobileSubscriberLog(
			ArrayList<E_MOBILE_SUBSCRIBER> mobileSubscriberLog) {
		this.mobileSubscriberLog = mobileSubscriberLog;
	}


	public ArrayList<E_MOBILE_SUBSCRIBER_CARD> getMobileSubscriberCardLog() {
		return mobileSubscriberCardLog;
	}


	public void setMobileSubscriberCardLog(
			ArrayList<E_MOBILE_SUBSCRIBER_CARD> mobileSubscriberCardLog) {
		this.mobileSubscriberCardLog = mobileSubscriberCardLog;
	}


	public ArrayList<Summary> getFailedSummaryLog() {
		return failedSummaryLog;
	}


	public void setFailedSummaryLog(ArrayList<Summary> failedSummaryLog) {
		this.failedSummaryLog = failedSummaryLog;
	}


	public ArrayList getFailedSummaryDrillDownLog() {
		return failedSummaryDrillDownLog;
	}


	public void setFailedSummaryDrillDownLog(ArrayList failedSummaryDrillDownLog) {
		this.failedSummaryDrillDownLog = failedSummaryDrillDownLog;
	}


	public ArrayList getSupportLog() {
		return supportLog;
	}


	public void setSupportLog(ArrayList supportLog) {
		this.supportLog = supportLog;
	}


	public ArrayList<SMS_LOG> getSmsLog() {
		return smsLog;
	}


	public void setSmsLog(ArrayList<SMS_LOG> smsLog) {
		this.smsLog = smsLog;
	}


	public ArrayList<T_SMS_RECEIVE> getSmsReceiveLog() {
		return smsReceiveLog;
	}


	public void setSmsReceiveLog(ArrayList<T_SMS_RECEIVE> smsReceiveLog) {
		this.smsReceiveLog = smsReceiveLog;
	}


	public ArrayList<T_SMS_SEND> getSmsSendLog() {
		return smsSendLog;
	}


	public void setSmsSendLog(ArrayList<T_SMS_SEND> smsSendLog) {
		this.smsSendLog = smsSendLog;
	}


	public ArrayList<E_TRANSCODE> getTranscodeList() 
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			transcodeList = reportModel.getTransCode();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return transcodeList;
	}


	public void setTranscodeList(ArrayList<E_TRANSCODE> transcodeList) {
		this.transcodeList = transcodeList;
	}


	

	public ArrayList<E_TMCNODE> getTmcNodeList() 
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			tmcNodeList = reportModel.getTMCNodes();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return tmcNodeList;
	}


	public void setTmcNodeList(ArrayList<E_TMCNODE> tmcNodeList) {
		this.tmcNodeList = tmcNodeList;
	}


	public ArrayList<E_TMCEVENT> getTmcEventLog() {
		return tmcEventLog;
	}


	public void setTmcEventLog(ArrayList<E_TMCEVENT> tmcEventLog) {
		this.tmcEventLog = tmcEventLog;
	}


	public String getTerminal_id() {
		return terminal_id;
	}


	public void setTerminal_id(String terminal_id) {
		this.terminal_id = terminal_id;
	}



	public ArrayList<C_TRANSACTION> getPinLog() {
		return pinLog;
	}


	public void setPinLog(ArrayList<C_TRANSACTION> pinLog) {
		this.pinLog = pinLog;
	}


	public ArrayList<E_TMCREQUEST> getTmcRequestLog() {
		return tmcRequestLog;
	}


	public void setTmcRequestLog(ArrayList<E_TMCREQUEST> tmcRequestLog) {
		this.tmcRequestLog = tmcRequestLog;
	}
	

	public double makeDouble(String value)
	{
		double resp = 0.00;
		try
		{
			if(value == null || value.equals("") || value.equals("null"))
			{
				value= "0.00";
			}
			else
			{
				resp = Double.parseDouble(value);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return resp;
	}
	
	public String getChannelName(String channel_id)
	{
		ReportModel reportModel = new ReportModel();
		return reportModel.getChannelName(channel_id);
	}
	
	
	public String getTransName(String trans_code)
	{
		ReportModel reportModel = new ReportModel();
		return reportModel.getTransName(trans_code);
	}
	
	
	public String getBankName(String bank_code)
	{
		ReportModel reportModel = new ReportModel();
		return reportModel.getBankName(bank_code);
	}
	
	public void getMerchantSplitFormuala()
	{
		ReportModel reportModel = new ReportModel();
		merchantSplitFormula = reportModel.getE_MERCHANT_SPLIT_FORMULA(merchant_code);
	}
	
	
	
	public String getCardHolderLastName(String card_num)
	{
		ReportModel reportModel = new ReportModel();
		return reportModel.getCardHolderLastName(card_num);
	}
	
	public String getCardHolderFirstName(String card_num)
	{
		ReportModel reportModel = new ReportModel();
		return reportModel.getCardHolderFirstName(card_num);
	}
	
	public String getCardHolderStreet(String card_num)
	{
		ReportModel reportModel = new ReportModel();
		return reportModel.getCardHolderStreet(card_num);
	}
	
	public String getCardHolderState(String card_num)
	{
		ReportModel reportModel = new ReportModel();
		return reportModel.getCardHolderState(card_num);
	}
	
	public String getCardHolderPhone(String card_num)
	{
		ReportModel reportModel = new ReportModel();
		return reportModel.getCardHolderPhone(card_num);
	}
	
	public String hashData(String card_num)
	{
		HashNumber hn = new HashNumber();
		return hn.hashStringValue(card_num, 9, 4);
	}

	public String hashSpecifiedLastValue(String card_num)
	{
		HashNumber hn = new HashNumber();
		return hn.hashSpecifiedLastValue(card_num.trim(), 4);
	}
	
	
	
	
	public String getStrParam() {
		return strParam;
	}


	public void setStrParam(String strParam) {
		this.strParam = strParam;
	}


	public ArrayList<E_TRANSACTION> getMerchantSummaryLog() {
		return merchantSummaryLog;
	}


	public void setMerchantSummaryLog(ArrayList<E_TRANSACTION> merchantSummaryLog) {
		this.merchantSummaryLog = merchantSummaryLog;
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

	public ArrayList getMerchantSplitLog() {
		return merchantSplitLog;
	}

	public void setMerchantSplitLog(ArrayList merchantSplitLog) {
		this.merchantSplitLog = merchantSplitLog;
	}

	public String getOptionType() {
		return optionType;
	}



	public void setOptionType(String optionType) {
		this.optionType = optionType;
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

	public String getOptionType2() {
		return optionType2;
	}


	public void setOptionType2(String optionType2) {
		this.optionType2 = optionType2;
	}

	public ArrayList<E_MERCHANT> getMerchantSearchLog() {
		return merchantSearchLog;
	}


	public void setMerchantSearchLog(ArrayList<E_MERCHANT> merchantSearchLog) {
		this.merchantSearchLog = merchantSearchLog;
	}


	public String getChannel_id() 
	{
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


	public String getSettlement_batch() {
		return settlement_batch;
	}


	public void setSettlement_batch(String settlement_batch) {
		this.settlement_batch = settlement_batch;
	}




	public ArrayList getSettle_batch() {
		return settle_batch;
	}


	public void setSettle_batch(ArrayList settle_batch) {
		this.settle_batch = settle_batch;
	}

	

	public String getLine_type() {
		return line_type;
	}

	public void setLine_type(String line_type) {
		this.line_type = line_type;
	}

	public ArrayList<E_TRANSACTION> getRevenueReportLog() {
		return revenueReportLog;
	}

	public void setRevenueReportLog(ArrayList<E_TRANSACTION> revenueReportLog) {
		this.revenueReportLog = revenueReportLog;
	}

	public ArrayList<E_TRANSACTION> getAllMerchantTransReportLog() {
		return allMerchantTransReportLog;
	}

	public void setAllMerchantTransReportLog(
			ArrayList<E_TRANSACTION> allMerchantTransReportLog) {
		this.allMerchantTransReportLog = allMerchantTransReportLog;
	}

	public ArrayList<E_TRANSACTION> getAllMerchantChannelReportLog() {
		return allMerchantChannelReportLog;
	}

	public void setAllMerchantChannelReportLog(
			ArrayList<E_TRANSACTION> allMerchantChannelReportLog) {
		this.allMerchantChannelReportLog = allMerchantChannelReportLog;
	}

	public ArrayList<E_TRANSACTION> getAllMerchantReportLog() {
		return allMerchantReportLog;
	}

	public void setAllMerchantReportLog(
			ArrayList<E_TRANSACTION> allMerchantReportLog) {
		this.allMerchantReportLog = allMerchantReportLog;
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

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	public ArrayList<E_STANDARD_SPLIT> getStandardSplitFormula() 
	{
		ReportModel reportModel = new ReportModel();
		standardSplitFormula = reportModel.getE_STANDARD_SPLIT_FORMULA();
		return standardSplitFormula;
	}

	public void setStandardSplitFormula(
			ArrayList<E_STANDARD_SPLIT> standardSplitFormula) {
		this.standardSplitFormula = standardSplitFormula;
	}

	public ArrayList<E_MERCHANT_SPLIT> getMerchantSplitFormula() 
	{
		return merchantSplitFormula;
	}

	public void setMerchantSplitFormula(
			ArrayList<E_MERCHANT_SPLIT> merchantSplitFormula) {
		this.merchantSplitFormula = merchantSplitFormula;
	}

	public ArrayList<E_TRANSACTION> getAllMerchantBankReportLog() {
		return allMerchantBankReportLog;
	}

	public void setAllMerchantBankReportLog(
			ArrayList<E_TRANSACTION> allMerchantBankReportLog) {
		this.allMerchantBankReportLog = allMerchantBankReportLog;
	}

	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}

	

	public String getCard_num() {
		return card_num;
	}

	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}


	public ArrayList getSwitchReportLog() {
		return switchReportLog;
	}

	public void setSwitchReportLog(ArrayList switchReportLog) {
		this.switchReportLog = switchReportLog;
	}

	public ArrayList getMmoneySummaryLog() {
		return mmoneySummaryLog;
	}

	public void setMmoneySummaryLog(ArrayList mmoneySummaryLog) {
		this.mmoneySummaryLog = mmoneySummaryLog;
	}

	public ArrayList getMerchantReportLog() {
		return merchantReportLog;
	}

	public void setMerchantReportLog(ArrayList merchantReportLog) {
		this.merchantReportLog = merchantReportLog;
	}

	public ArrayList getMerchantSummaryReportLog() {
		return merchantSummaryReportLog;
	}

	public void setMerchantSummaryReportLog(ArrayList merchantSummaryReportLog) {
		this.merchantSummaryReportLog = merchantSummaryReportLog;
	}

	public ArrayList getMmoneyLog() {
		return mmoneyLog;
	}

	public void setMmoneyLog(ArrayList mmoneyLog) {
		this.mmoneyLog = mmoneyLog;
	}


	public ArrayList<E_EXCEPTION> getExceptionLog() {
		return exceptionLog;
	}


	public void setExceptionLog(ArrayList<E_EXCEPTION> exceptionLog) {
		this.exceptionLog = exceptionLog;
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


	public ArrayList<E_CARDSERVICE> getCardserviceLog() {
		return cardserviceLog;
	}


	public void setCardserviceLog(ArrayList<E_CARDSERVICE> cardserviceLog) {
		this.cardserviceLog = cardserviceLog;
	}


	public ArrayList getCardholdertranLog() {
		return cardholdertranLog;
	}


	public void setCardholdertranLog(ArrayList cardholdertranLog) {
		this.cardholdertranLog = cardholdertranLog;
	}


	public ArrayList getCardholdertranByMerchantCodeLog() {
		return cardholdertranByMerchantCodeLog;
	}


	public void setCardholdertranByMerchantCodeLog(
			ArrayList cardholdertranByMerchantCodeLog) {
		this.cardholdertranByMerchantCodeLog = cardholdertranByMerchantCodeLog;
	}


	public ArrayList<E_TRANSACTION> getFundsTransfer() {
		return fundsTransfer;
	}


	public void setFundsTransfer(ArrayList<E_TRANSACTION> fundsTransfer) {
		this.fundsTransfer = fundsTransfer;
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

	
	public void getExistedSchemelist()
	{
		
		ReportModel cmmodel = new ReportModel();
		schemeRegList = cmmodel.getScheme_Registration();
		System.out.println("schemeList "+schemeRegList.size());
	}
	

	public boolean isSelected() {
		return selected;
	}


	public void setSelected(boolean selected) {
		this.selected = selected;
	}


	public PAYTRANS getPayTrans() 
	{
		if(payTrans == null)
		{
			payTrans = new PAYTRANS();
		}
		return payTrans;
	}


	public void setPayTrans(PAYTRANS payTrans) {
		this.payTrans = payTrans;
	}

	
	public List getBankList() 
	{
	    getBanks();
		return bankList;
	}

	public void setBankList(List bankList) {
		this.bankList = bankList;
	}
    public void getBanks()
    {
		try
		{
			ReportModel reportModel = new ReportModel();
			bankList =  reportModel.getBanks();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

    }


	public String getIssueCode() {
		return issueCode;
	}


	public void setIssueCode(String issueCode) {
		this.issueCode = issueCode;
	}

	public List getAppList() {
		return appList;
	}


	public void setAppList(List appList) {
		this.appList = appList;
	}


	public ArrayList getPendingCardDeletionLog() 
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			
			FacesContext context = FacesContext.getCurrentInstance();
			String user_code = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
			String userType = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getType_id();

			pendingCardDeletionLog =  reportModel.getPendingCardDeletionLog(user_code, userType);
			
		}
		catch(Exception ex){}
		
		return pendingCardDeletionLog;
	}


	public void setPendingCardDeletionLog(ArrayList pendingCardDeletionLog) {
		this.pendingCardDeletionLog = pendingCardDeletionLog;
	}


	public List getDistrictList() 
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			String channel = getChannel_id();
			
			districtList =  reportModel.getDistricts();
					
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return districtList;
	}


	public void setDistrictList(List districtList) {
		this.districtList = districtList;
	}
	
	public void getPHCNAdminReport()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			billLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
	        String optionType = getOptionType();
	        String district = getLine_type();
	        //System.out.println(":::Distirct from PHCN :: :: :: : : : :: : :>>>>>  "+district);
	       // String merchant_code = "1234567890";
	        String tarrifType = getTarrif_type();
	        String issuercode = getBank_code();
	        String channelid = getChannel_id();
	        String subcode = getBranch_code(); 
	        String meterno = getMeterno();
	 
	        if(subcode == null)
	        {
	        	subcode = "";
	        }
	        if(meterno==null)
	        {
	        	meterno = "";
	        }
	        
	        if(channelid == null)
	        {
	        	channelid = "";
	        }
	        
	        //System.out.println("channelid : "+channelid);
	        if(issuercode == null)
	        {
	        	issuercode = "";
	        } 
	        if(district.equals("ALL"))
	        {
	        	district = "";
	        }
	       
	        double b = 0.0;
	        double c = 0.0;
	        double d = 0.0;
	    	String merchantCode = "";
	    	
	    	
			if(tarrifType.equals("PHCN: TARRIFF"))
			{
				FacesContext context = FacesContext.getCurrentInstance();
				String disco = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
				merchantCode += reportModel.getDistinctPostpaidPHCNMerchantCode(disco, district);
			}
			else if(tarrifType.equals("PHCN: PREPAID"))
			{
				FacesContext context = FacesContext.getCurrentInstance();
				String disco = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
				if(disco.equalsIgnoreCase("EKO"))
		    	{
		    		//merchantCode += "7006020013:7006020014:7006020015";
		    		merchantCode += reportModel.getDistinctPostpaidPHCNMerchantCode(disco, district);
		    		//merchantCode += "7006020013:7006020014:7006020015";
		    	}
				
			}
			else
			{
				FacesContext context = FacesContext.getCurrentInstance();
				String disco = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
				merchantCode += reportModel.getDistinctPostpaidPHCNMerchantCode(disco, district);
				if(disco.equalsIgnoreCase("EKO"))
		    	{
		    		//merchantCode += "7006020013:7006020014:7006020015";
		    		merchantCode += reportModel.getDistinctPostpaidPHCNMerchantCode(disco, district);
		    		//merchantCode += "7006020013:7006020014:7006020015";
		    	}
			}
			
			FacesContext context = FacesContext.getCurrentInstance();
			String disco = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
			
	        billLog = reportModel.getPHCNAdminReport(beginDate, endDate, optionType,
	        		merchantCode, tarrifType, issuercode, subcode, channelid, meterno,disco,district);
	        
	        System.out.println("Bill Log     "+billLog.size());
	        
	        if(billLog.size()>0)
	        {
		        for(int i=0;i<billLog.size();i++)
		        {
		        	PAYTRANS et = (PAYTRANS)billLog.get(i);
		        	b += Double.parseDouble(et.getTrans_amount());
		        	c += Double.parseDouble(et.getEtzCommissionAmt());
		        	d += Double.parseDouble(et.getNetAmt());
		        }
		        setTotal_amount(b);
		        setTotal_etz_amount(c);
		        setTotal_other_amount(d);
	        }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*This is for the phcn officers*/
	/*public void getPHCNOfficerReport()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			billLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
	        String optionType = getOptionType();
	        String district = getLine_type();
	        String tarrifType = getTarrif_type();
	        String issuercode = getBank_code();
	        String channelid = getChannel_id();
	        String subcode = getBranch_code(); 
	        String meterno = getMeterno();
	 
	        if(subcode == null)
	        {
	        	subcode = "";
	        }
	        if(meterno==null)
	        {
	        	meterno = "";
	        }
	        
	        if(channelid == null)
	        {
	        	channelid = "";
	        }
	        
	        if(issuercode == null)
	        {
	        	issuercode = "";
	        } 
	        if(district.equals("ALL"))
	        {
	        	district = "";
	        }
	       
	        
	        double b = 0.0;
	    	String merchantCode = "";
	    	
   	        FacesContext context = FacesContext.getCurrentInstance();
			String disco = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();

			merchantCode += reportModel.getDistinctPHCNMerchantCode(disco, "mobile_merchant_code");
			merchantCode += reportModel.getDistinctPHCNMerchantCode(disco, "pos_merchant_code");
			merchantCode += reportModel.getDistinctPHCNMerchantCode(disco, "payoutlet_merchant_code");
			
	        billLog = reportModel.getPHCNAdminReport(beginDate, endDate, optionType, district,
	        		merchantCode, tarrifType, issuercode, subcode, channelid, meterno);
	        System.out.println("Bill Log     "+billLog.size());
	        if(billLog.size()>0)
	        {
		        for(int i=0;i<billLog.size();i++)
		        {
		        	PAYTRANS et = (PAYTRANS)billLog.get(i);
		        	b += Double.parseDouble(et.getTrans_amount());
		        }
		        setTotal_amount(b);
	        }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}*/
	
	
	public void getPHCNPaymentSummaryReport()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			billLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
	        String optionType = getOptionType();
	        String district = getLine_type();
	        String merchant_code = "1234567890";
	        String tarrifType = getTarrif_type();
	        String issuercode = getBank_code();
	        String channelid = getChannel_id();

	        
	        System.out.println("channelid : "+channelid);
	        if(issuercode == null)
	        {
	        	issuercode = "";
	        } 
	        if(channelid == null)
	        {
	        	channelid = "";
	        }
	        
	        double b = 0.0;
	        
	        billLog = reportModel.getPHCNPaymentByDistrictRport(beginDate, endDate, optionType,
	        		merchant_code, issuercode, channelid);
	        System.out.println("Bill Log     "+billLog.size());
	        if(billLog.size()>0)
	        {
		        for(int i=0;i<billLog.size();i++)
		        {
		        	PAYTRANS et = (PAYTRANS)billLog.get(i);
		        	b += Double.parseDouble(et.getTrans_amount());
		        }
		        setTotal_amount(b);
	        }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public void getPHCNPaymentByBankSummaryReport()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			billLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
	        String optionType = getOptionType();
	        String district = getLine_type();
	        String merchant_code = "1234567890";
	        String tarrifType = getTarrif_type();
	        String issuercode = getBank_code();
	        String channelid = getChannel_id();

	        
	        System.out.println("channelid : "+channelid);
	        if(issuercode == null)
	        {
	        	issuercode = "";
	        } 
	        if(channelid == null)
	        {
	        	channelid = "";
	        }
	        
	        double b = 0.0;
	        
	        billLog = reportModel.getPHCNPaymentByBankSummaryReport(beginDate, endDate, optionType,
	        		merchant_code, issuercode, channelid);
	        System.out.println("Bill Log     "+billLog.size());
	        if(billLog.size()>0)
	        {
		        for(int i=0;i<billLog.size();i++)
		        {
		        	PAYTRANS et = (PAYTRANS)billLog.get(i);
		        	b += Double.parseDouble(et.getTrans_amount());
		        }
		        setTotal_amount(b);
	        }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	
	public void getPHCNBankBranchSummaryReport()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			billLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
	        String optionType = getOptionType();
	        String district = getLine_type();
	        String merchant_code = "1234567890";
	        String tarrifType = getTarrif_type();
	        String issuercode = getBank_code();
	        System.out.println("Bank code --- "+issuercode);
	        String channelid = getChannel_id();

	        
	        System.out.println("channelid : "+channelid);
	        if(issuercode == null)
	        {
	        	issuercode = "";
	        } 
	        if(channelid == null)
	        {
	        	channelid = "";
	        }
	        
	        double b = 0.0;
	        
	        billLog = reportModel.getPHCNBranchBankSummaryReport(beginDate, endDate, optionType,
	        		merchant_code, issuercode, channelid);
	        System.out.println("Bill Log     "+billLog.size());
	        if(billLog.size()>0)
	        {
		        for(int i=0;i<billLog.size();i++)
		        {
		        	PAYTRANS et = (PAYTRANS)billLog.get(i);
		        	b += Double.parseDouble(et.getTrans_amount());
		        }
		        setTotal_amount(b);
		        
	        }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	
	public void getVSMTransactionSummary()
	{
		
			try
			{
			
				ReportModel reportModel = new ReportModel();
				AdminModel adminModel = new AdminModel();
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		        
				String beginDate = df.format(getStart_date());
		        beginDate = beginDate + " " + getStart_hr() + ":00";
		        
		        String endDate = df.format(getEnd_date());
		        endDate = endDate + " " + getEnd_hr() + ":59";
		        String cardnum = "";
		        String transDat = "";
		        String transAmt = "";
		        String mobile = getMobileno();
		        
		        String salesMan = adminModel.getUserNameByMobile(mobile);
		        String vsmFullname = reportModel.getVSMFullName(mobile);
		        System.out.println("Mobile number ---- >>> "+mobile);
		        double b = 0.0;
		        tranSummaryList = reportModel.getVSMTransactionSummary(mobile, salesMan, beginDate, endDate);
		        System.out.println("Size "+tranSummaryList.size());
		        if(tranSummaryList.size()>0)
		        {
			        for(int i=0;i<tranSummaryList.size();i++)	
			        {
			        	E_TRANSACTION  trans = (E_TRANSACTION)tranSummaryList.get(i);
			        	b += Double.parseDouble(trans.getTrans_amount());
			        }
			        //setTransAmount(transAmt);
			        setTotal_amount(b);
			        setVsmFullName(vsmFullname); //fullname of the VSM
		        }
		        
	
			}
			catch(Exception ex)
			{
					ex.printStackTrace();
				
			}
		
		
	}
	
	
	public void getDepotPoolAccountTransactionSummary()
	{
		
			try
			{
			
				ReportModel reportModel = new ReportModel();
				
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		        
				String beginDate = df.format(getStart_date());
		        beginDate = beginDate + " " + getStart_hr() + ":00";
		        
		        String endDate = df.format(getEnd_date());
		        endDate = endDate + " " + getEnd_hr() + ":59";
		        String cardnum = "";
		        String transDat = "";
		     
		   
		        double b = 0.0;
		   
				
		        transPoolList = reportModel.getDepotPoolAccountTransactionSummary(beginDate, endDate);
		        System.out.println("Size "+transPoolList.size());
		        if(transPoolList.size()>0)
		        {
			        for(int i=0;i<transPoolList.size();i++)	
			        {
			        	E_TRANSACTION  trans = (E_TRANSACTION)transPoolList.get(i);
			        	b += Double.parseDouble(trans.getTrans_amount());
			        	cardnum = trans.getCard_num();
			        }

			        setTotal_amount(b);
			      //  setAccountName(salesMan);
			        setCard_num(cardnum);
			        //setId3(salesMan);
			      
	         
		        }
		        
	
			}
			catch(Exception ex)
			{
					ex.printStackTrace();
				
			}
		
		
	}
	
	public void getDepotPoolTransactionReport()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
		
			//String vsmCard = getId();
			String cardnum = getId1();
			String day = getId2();
			String month = getId3();
			String year = getId4();
			
			//String transDate = year+"-"+month+"-"+day;
		
			System.out.println("Day of transaction "+day+"Card number "+cardnum+"Trans Date "+transDate);
			double b = 0.0;
			double onlinebalance = 0.0; 
			//transPool =  reportModel.getDepotPoolTransactionReport(day,cardnum);
			transPool =  reportModel.getDepotPoolTransactionReport(day,month,year,cardnum);
	        System.out.println("Size "+transPool.size());
	        if(transPool.size() > 0)
	        {
		        for(int i=0;i<transPool.size();i++)
		        {
		        	E_TRANSACTION  trans = (E_TRANSACTION)transPool.get(i);
		        	//onlinebalance = Double.parseDouble(trans.getRecalc_bal());
		        	onlinebalance = Double.parseDouble(trans.getVolume());
		        	System.out.println("Transaction Amount  -- - "+trans.getTrans_amount());
		        	
		        	
		        }
		        
		        setTotalFeeSum(onlinebalance);
	        }

		}
		catch(Exception ex)
		{
				ex.printStackTrace();
			
		}
	}
	
	public void getVasTranSummary()
	{
		
			try
			{
				transPoolList.clear();
				ReportModel reportModel = new ReportModel();
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		        
				String beginDate = df.format(getStart_date());
		        beginDate = beginDate + " " + getStart_hr() + ":00";
		        
		        String endDate = df.format(getEnd_date());
		        endDate = endDate + " " + getEnd_hr() + ":59";
		        String cardnum = "";
		        String transDat = "";
		        String mobileNo = "";
		        
		        FacesContext context = FacesContext.getCurrentInstance();
				mobileNo = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getMobile();

		        double b = 0.0;
		   
		       /* FacesContext context = FacesContext.getCurrentInstance();
				String walletNo = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();*/


				
		        transPoolList = reportModel.getVasTransSummary(mobileNo,beginDate, endDate);
		        System.out.println("Size "+transPoolList.size());
		        if(transPoolList.size()>0)
		        {
			        for(int i=0;i<transPoolList.size();i++)	
			        {
			        	E_TRANSACTION  trans = (E_TRANSACTION)transPoolList.get(i);
			        	b += Double.parseDouble(trans.getTrans_amount());
			        	cardnum = trans.getCard_num();
			        	
			       
			        }

			        setTotal_amount(b);
			      //  setAccountName(salesMan);
			        setCard_num(cardnum);
			        //setId3(salesMan);
			      
	         
		        }
		        
	
			}
			catch(Exception ex)
			{
					ex.printStackTrace();
				
			}
		
		
	}
	
	public void setToEditDay()
	{
		
		try
		{
			
			AdminModel adminModel = new AdminModel();
			ReportModel reportModel = new ReportModel();
			
			String perday = getId3(); //day to move
			String month = getId4();  // month to move
			String year = getId5(); // year to move
			String cardToDebit = getId1();//vsm card to debit
			String transamount = getId(); //amount to debit
			String mobile = getId2();
			
			System.out.println("Day "+perday+"Month "+month+"Year "+year);

			FacesContext context = FacesContext.getCurrentInstance();// card to credit
			String cardToCredit = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id().substring(4);
			String companyCode = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id().substring(0,3);
			//String mobile = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getMobile();
			String depotId = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_id();
			
			String companyId = reportModel.getCompanyIDByComapanyCode(companyCode);
			String transdate = reportModel.getTransDate(perday,month,year,cardToDebit);
			setTransDate(transdate); // transdate
			setDepotId(depotId);
			setCompanyId(companyId); // company id
			setMobileno(mobile); // mobile
			setCard_num(cardToDebit);//card to debit
			setTransAmount(transamount);
			setMerchant_code(cardToCredit);//card to credit
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	public void setToEditDepot()
	{
		try
		{
			
			AdminModel adminModel = new AdminModel();
			ReportModel reportModel = new ReportModel();
			
			String transamount = getId();
			String transDate = getId2();
			System.out.println("Move trans Date  "+transDate);
			String cardnum = getId1();
			
			
			FacesContext context = FacesContext.getCurrentInstance();
			String cardToCredit = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id().substring(4);
			String mobile = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getMobile();
			String companyCode = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id().substring(0,3);
			String companyId = reportModel.getCompanyIDByComapanyCode(companyCode);
			String companyAccountNo = reportModel.getCompanyAccountNumberByCompanyCode(companyCode);
			String companyBank = reportModel.getCompanyBankByCompanyCode(companyCode);
			
			/*ArrayList day = reportModel.getTransactionDateByDayDates(perday,cardnum);
			System.out.println("SIZE  "+day.size());
			if(day.size() > 0)
			{
				E_TRANSACTION tran = (E_TRANSACTION)day.get(0);
				setTransDate(tran.getTrans_date().trim());
			}
			*/
			setTransDate(transDate);
			setTransAmount(transamount);
			setMobileno(mobile);
			setMerchant_code(cardToCredit);
			setCompanyId(companyId);
			setCard_num(cardnum);
			setCompanyBankAccount(companyAccountNo);
			setCompanyBank(companyBank);
					
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	

	public void getVSMTransactionReportByDay()
	{
		
			try
			{
				ReportModel reportModel = new ReportModel();
				
				String day1 = getId();
				String day = getId2();
				String cardnum = getId1();
				
				System.out.println("Day of transaction "+day+"Card number "+cardnum);
				 double b = 0.0;
				transSummaryList =  reportModel.getVSMTransactionReporByDay(day,cardnum);
				
		        System.out.println("Size "+transSummaryList.size());
		        if(transSummaryList.size()>0)
		        {
			        for(int i=0;i<transSummaryList.size();i++)
			        {
			        	E_TRANSACTION  trans = (E_TRANSACTION)transSummaryList.get(i);
			        	b += Double.parseDouble(trans.getTrans_amount());
			        }
			        setTotal_amount(b);
		        }
	
			}
			catch(Exception ex)
			{
					ex.printStackTrace();
				
			}
		
		
	}
	

	public static HttpHost getHttpHost() 
	{
		HttpHost httpHost = null;
		try
		{
			String hostIp = "172.16.10.134";//live params
			//String hostIp = "10.0.0.199"; //staging server
	        httpHost = new HttpHost();
	        httpHost.setServerAddress(hostIp);
	        httpHost.setPort(9999);//live
	       // httpHost.setPort(8085);//staging
	        String key = "123456";
	        httpHost.setSecureKey(key);
	        System.out.println("connected");
		}
		catch(Exception ex)
		{
			System.out.println("could not connected " + ex.getMessage());
			ex.printStackTrace();
		}
        
        return httpHost;
	}
	
	/*Move vsm amount to depot card */
	public String moveToDepotCard()
	{
		String ret = "";
		try
        { 
			FacesContext context = FacesContext.getCurrentInstance();
			String username = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
			String sessionid = "";
			ReportModel reportModel = new ReportModel();
			
			// Call Web Service Operation
			HttpServletRequest str = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			System.out.println("about to call webservice");
			
			com.etz.webserviceClient.XPortalWSService services = new com.etz.webserviceClient.XPortalWSService(new URL("file:c:/Tony/XP.xml"));
			com.etz.webserviceClient.XPortalWS port = services.getXPortalWSPort();
			
			String amount = getTransAmount();
			String mobile = getMobileno();//vsm phone number
			String companyId = getCompanyId();//company id
			String depotId = getDepotId();//depot admin user_id

			System.out.println("Amount   "+ amount);
			System.out.println("Mobile  no  "+ mobile);
			System.out.println("CompanyId  "+ companyId);
			System.out.println("depoted id "+ depotId);
			
			
			String message = moveToCard();
			if(message.equalsIgnoreCase("Transaction Successfully Moved !"))
			{
				String a = port.processDirectDebit(amount, mobile, companyId, depotId);				
				if(a.equalsIgnoreCase("0:Successful"))
				{
					ret = "/support/bcmTransactionSummaryReport.etz";
				}
				else
				{
					message = "Unable to Move Transaction !";
					ret = "/support/bcmTransactionSummaryReport.etz";
				}
			}
			else if(message.equalsIgnoreCase("EXISTED"))
			{
				message = "Transaction Already  Moved !";
				ret = "/support/bcmTransactionSummaryReport.etz";
			}
			else
			{
				message = "Unable to Move Transaction !";
				ret = "/support/bcmTransactionSummaryReport.etz";
			}
			
			facesMessages.add(Severity.INFO, message);	
        }
		catch (Exception ex) 
        {
            ex.printStackTrace();
        }
        return ret;
	}
	
	
	//this method is used to move the money from the VSM card to the company card
	public String moveToCard()
	{
		String locatePage = "";
		String message= "";
		
		try
		{
			ReportModel model = new ReportModel();
			String cardnum = getMerchant_code();//cardnumber to credit
		
			String vsmCard = getEdit_id(); // vsm card number 
			
			String totalAmount = getTransAmount();
			String transDate = getStart_hr();
			
			String companyId = getCompanyId();
			String depotId = getDepotId();

			String status = "Transfered";
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String beginDate = df.format(getStart_date());
            Calendar cal = Calendar.getInstance();
            String moveDate = df.format(cal.getTime());
			
		   
			System.out.println("Transaction Date   - -- - "+transDate);
		
			message = model.moveToCard(cardnum,status, totalAmount, transDate,companyId,depotId,vsmCard,moveDate);
			if(message.equalsIgnoreCase("SUCCESS"))
			{
				message = "Transaction Successfully Moved !";
				//locatePage = "/support/bcmTransactionSummaryReport.etz";
			}
			else if(message.equalsIgnoreCase("EXISTED"))
			{
				message = "Transaction Already  Moved !";
				//locatePage = "/support/bcmTransactionSummaryReport.etz";
			}
			else
			{
				message = "Unable to Move Transaction !";
				//locatePage = "/support/bcmTransactionSummaryReport.etz";
			}

			facesMessages.add(Severity.INFO, message);	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	
		return message;	
	}
	
	//this method is used to move the money from the VSM card to the company card
		public String moveToCreditAccount()
		{
			String locatePage = "";
			String message= "";
			
			try
			{
				ReportModel model = new ReportModel();
				
				// String file = "C:/Users/joshua.aruno/Desktop/e_Cheque.xls";
					
			        String file = getFilePath();  //  file absolute path
			        
					Workbook workbook = WorkbookFactory.create(new FileInputStream(file)); // or sample.xls
		            System.out.println("Number Of Sheets" + workbook.getNumberOfSheets());
		            Sheet sheet = workbook.getSheetAt(0);
		            System.out.println("Number Of Rows:" + sheet.getLastRowNum());

		            Row row = sheet.getRow(0);
		        
		            String cardName = row.getCell(0).getStringCellValue();
		            String cardToDebit = row.getCell(1).getStringCellValue(); // Cardnumber to debit
		            String cardToDebitMobile = row.getCell(2).getStringCellValue();
		            double amounts = row.getCell(3).getNumericCellValue();
		            String offerings = row.getCell(4).getStringCellValue();
		            
		            System.out.println("CardName : "+cardName);
		            System.out.println("CardNum : "+cardToDebit);
		            System.out.println("Mobile : "+cardToDebitMobile);
		            System.out.println("Amount : "+amounts);
		            System.out.println("Offerengs : "+offerings);
		            
		           // String cardToCredit = "0112301006250010";    // cardnumber to credit
		            String Status = "0";
		            String companyId = "0";
		            String deportId = "0";
		            String dateUploaded = getEdit_id();  // date as at when the file was uploaded
		            String filename = getCard_num(); // filename 
		            String pendingStatus = getTo_dest(); // pending status from the list
		            
		        
		            
		            System.out.println("File Path    - - -  > "+file);
		            
		            FacesContext context = FacesContext.getCurrentInstance();
					String authoirsedBy = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
		            
					String cardToCredit = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
		            
					
		            message =  model.moveToCreditAccount(cardToCredit,Status,amounts,dateUploaded,companyId,deportId,cardToDebit,cardToDebitMobile,filename,pendingStatus,authoirsedBy);
		            if(message.equalsIgnoreCase("SUCCESS"))
					{
		            	
		            	message = "Transaction Successfully Authorized !";
						
					}
					else if(message.equalsIgnoreCase("EXISTED"))
					{
						message = "Transaction Already  Authorized !";
						//locatePage = "/support/bcmTransactionSummaryReport.etz";
					}
					else
					{
						message = "Unable to Authoirzied Transaction !";
						//locatePage = "/support/bcmTransactionSummaryReport.etz";
					}
		           

				facesMessages.add(Severity.INFO, message);	
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		
			return message;	
		}
		
	

	
	
	//this method is used to move the funds from the Depot Wallet to the Bank Account
	public String moveToDepotBank()
	{
		
		String nevigateLocation =  "";
		try
		{
			String message = "";
			ReportModel model = new ReportModel();
			String cardtoDebit = getMerchant_code();
			String track2 = getMerchant_code();
			String companyId = getCompanyId();
			String totalAmount = getTransAmount(); // totall amount move to depot bank account
			String transDate = getTransDate();
			String pin = getPin();
			String month = getMonth();
			String year = getYear();
			String accountNo = getCompanyBankAccount();
			String bankCode = getCompanyBank();
			String cardExpiry = month+""+year;
			
			//String cardnum = getCard_num(); // vsm cardnumber
			
			String bankAccunt = getAccountName(); // account number from the drop down list
			
			if(cardtoDebit.length() > 16)
			{
				CardAudit cardAudit = new CardAudit();
				cardtoDebit = cardAudit.deduceCardFromTrack2(cardtoDebit);
			}
			
			
			
			System.out.println("Card to Debit "+ cardtoDebit);
			System.out.println("Bank  " + bankCode);
			System.out.println("Bank Account " + accountNo);
			System.out.println("Expirtaion "+ cardExpiry);
			System.out.println("Bank Account from the drop down list  "+ bankAccunt);
			System.out.println("Total Amount  ->  "+ totalAmount);
			
			String status = "Move To Bank";
			
			Card card = new Card();
            card.setCardExpiration(cardExpiry);
            card.setCardNumber(cardtoDebit);
            card.setCardPin(pin);
            card.setAccountType("CA");

            XRequest request = new XRequest();
            request.setCard(card);
            request.setTransCode(TransCode.EFTA);
            request.setReference("01"+com.etz.mobileutilities.MobileUtilities.generateUniqueId(bankCode + accountNo));
            request.setTransAmount(Double.parseDouble(totalAmount));
            request.setMerchantCode(bankCode + accountNo);//append the bank code to the card number
            FacesContext context = FacesContext.getCurrentInstance();
			String username = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
			

			XResponse xResponse = null;
			request.setFee(0);
			request.setDescription(username + " Remittance");
			request.setChannelId("01");
			
            processor = new XProcessor();
            xResponse = processor.process(getHttpHost(), request);
            System.out.println("XProcessor message " + xResponse.getMessage());
            System.out.println("response code " + xResponse.getResponse());
            if(xResponse == null) 
            {
            	message = "Server error unable to move transaction to bank !"; //issue occured
            }
            else if(xResponse.getResponse() != 0)//0 means success , so if the response is not 0
            {
            	message = "Server Error : " + xResponse.getResponse();
            }
            else if(xResponse.getResponse() == 0)//this means success, soo i persist in the table
            {
            	try 
				{
					message = model.moveToDepotBank(track2, status, totalAmount,transDate);
					if(message.equalsIgnoreCase("SUCCESS"))
					{
						message = "Transaction Successfully Moved To Bank !";
						nevigateLocation = "/support/depotPoolAccountTransactionSummaryReport.etz";
					}
					else if(message.equalsIgnoreCase("EXISTED"))
					{
						message = "Transaction Already  Moved To Bank !";
						nevigateLocation = "/support/depotPoolAccountTransactionSummaryReport.etz";
					}
					else
					{
						message = "Funds have been successfully moved to bank but could not save in the XPortal reporting application !";
						nevigateLocation = "/support/depotPoolAccountTransactionSummaryReport.etz";
					}
		        } 
				catch (Exception e) 
				{
		            e.printStackTrace();
		        }
            }
			facesMessages.add(Severity.INFO, message);
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
			facesMessages.add(Severity.INFO, "Internal server error occured");
		}
		
		return nevigateLocation;
	}
	
	
	/*Method for compare two values */
	public boolean doesMobileExistInSession(String mobile, String[] session_data)
	{

		boolean status = false;

		for(int i=0;i<session_data.length;i++){
			if(mobile.equals(session_data[i])){
				status = true;
				break;
			}
		}

		return status;
	}
	
	public boolean SingleMobileExistInSession(String mobile, String session_data){

		boolean status = false;
		
		if(mobile.equals(session_data))
		{
			status = true;
		}

		return status;
	}
		
	/*Method to get Transactions based on a card number*/
	public void getSupervisor_VAS_CardHolderTransactions()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			cardholdertranLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	    	String apostrophe = "'";
	    
	        E_TRANSACTION tran = null;
	        double debit_total = 0.0;
	        double credit_total = 0.0;

	        FacesContext context = FacesContext.getCurrentInstance();
			String serviceId = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
	        
			String vsmasMobile_depotCardnumber[] = serviceId.split("#");
			
			String vsmMobile = vsmasMobile_depotCardnumber[0];
			System.out.println("Vsm No - --  > "+vsmMobile);
			String depotCardNumber = vsmasMobile_depotCardnumber[1];
			
			String mobileno = getCard_num();
			String singleVSMMobile = "";
			
			String m[] = null;
			
			if(vsmMobile.indexOf(":")>0)
			{
			    m = vsmMobile.split(":");
				vsmMobile = "";
				for(int i=0;i<m.length;i++)
				{
					vsmMobile +=" "+ m[i] ;
				}
			}
			else
			{
				singleVSMMobile = vsmMobile ;
			}
			
			System.out.println("Session VsmMobile Number---- >  "+vsmMobile+"Session Mobile "+m);
			

			System.out.println("Session Single VsmMobile Number---- >  "+singleVSMMobile);
		
			boolean chk = false;
			if(m != null){
				chk = doesMobileExistInSession(mobileno,m);
				System.out.println("my if......"+chk);
			}
			else
			{
				chk = SingleMobileExistInSession(mobileno,singleVSMMobile);
				System.out.println("doest int get there  "+chk);
				
			}
			
			
			if(chk)
			{
				cardholdertranLog = reportModel.getSupervisor_VAS_CardHolderTransactions(mobileno, beginDate, endDate);
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
			  	facesMessages.add(Severity.INFO, " You dont have the Priviledges to view Report for this VSM Wallet No ["  +mobileno+  "] ");
		    	return;
			}
			
			
		        	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}	
		
	/*Method to get Transactions based on a card number*/
	public void getSupervisor_DEPOT_CardHolderTransactions()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			cardholdertranLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
	    
	        E_TRANSACTION tran = null;
	        double debit_total = 0.0;
	        double credit_total = 0.0;
	        
			//String card_number_mobile = getCard_num().trim();
	        
	        FacesContext context = FacesContext.getCurrentInstance();
			String serviceIdSplite = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
	        
			String vsmasMobile_depotCardnumber[] = serviceIdSplite.split("#");
			
			//String vsmMobile = vsmasMobile_depotCardnumber[0];
			String depotCardNumber = vsmasMobile_depotCardnumber[1];
			

			
		        	cardholdertranLog = reportModel.getSupervisor_DEPOT_CardHolderTransactions(depotCardNumber, beginDate, endDate);
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
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	public String getTransSummary() {
		return transSummary;
	}


	public void setTransSummary(String transSummary) {
		this.transSummary = transSummary;
	}
	
	public ArrayList<E_TRANSACTION> getAllMerchantAccountReportLog() {
		return allMerchantAccountReportLog;
	}


	public void setAllMerchantAccountReportLog(
			ArrayList<E_TRANSACTION> allMerchantAccountReportLog) {
		this.allMerchantAccountReportLog = allMerchantAccountReportLog;
	}


	public ArrayList getMerchantExtSplitLog() 
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			merchantExtSplitLog = reportModel.getAllMerchantExtSplit();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return merchantExtSplitLog;
	}


	public void setMerchantExtSplitLog(ArrayList merchantExtSplitLog) {
		this.merchantExtSplitLog = merchantExtSplitLog;
	}


	public List getBizDevList() 
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			bizDevList = reportModel.getOnlyBizHeadAccountId();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		return bizDevList;
	}


	public void setBizDevList(List bizDevList) {
		this.bizDevList = bizDevList;
	}


	public int getTotalTranCount() {
		return totalTranCount;
	}


	public void setTotalTranCount(int totalTranCount) {
		this.totalTranCount = totalTranCount;
	}


	public double getTotalTranSum() {
		return totalTranSum;
	}


	public void setTotalTranSum(double totalTranSum) {
		this.totalTranSum = totalTranSum;
	}


	public int getTotalSettleCount() {
		return totalSettleCount;
	}


	public void setTotalSettleCount(int totalSettleCount) {
		this.totalSettleCount = totalSettleCount;
	}


	public double getTotalSettleSum() {
		return totalSettleSum;
	}


	public void setTotalSettleSum(double totalSettleSum) {
		this.totalSettleSum = totalSettleSum;
	}


	public int getTotalFeeCount() {
		return totalFeeCount;
	}


	public void setTotalFeeCount(int totalFeeCount) {
		this.totalFeeCount = totalFeeCount;
	}


	public double getTotalFeeSum() {
		return totalFeeSum;
	}


	public void setTotalFeeSum(double totalFeeSum) {
		this.totalFeeSum = totalFeeSum;
	}

	

	public ArrayList getAccountInfoList() {
		
		ReportModel reportModel = new ReportModel();
		accountInfoList = reportModel.getAccountInfo();
		
		return accountInfoList;
	}


	public void setAccountInfoList(ArrayList accountInfoList) {
		this.accountInfoList = accountInfoList;
	}


	public Company getCompanyOb() 
	{
		if(companyOb == null)
		{
			companyOb = new Company();
		}		
		return companyOb;
	}


	public void setCompanyOb(Company companyOb) {
		this.companyOb = companyOb;
	}


	public PoolAccount getPoolAccountObj() {
		if(poolAccountObj ==  null)
		{
			poolAccountObj = new PoolAccount();
		}
		return poolAccountObj;
	}


	public void setPoolAccountObj(PoolAccount poolAccountObj) {
		this.poolAccountObj = poolAccountObj;
	}


	public AccountInfo getAccountInfo() {
		if(accountInfo == null)
		{
			accountInfo = new AccountInfo();
		}
		return accountInfo;
	}


	public void setAccountInfo(AccountInfo accountInfo) {
		this.accountInfo = accountInfo;
	}


	public String getCompanyBankAccount() {
		return companyBankAccount;
	}


	public void setCompanyBankAccount(String companyBankAccount) {
		this.companyBankAccount = companyBankAccount;
	}


	public String getCompanyBank() {
		return companyBank;
	}


	public void setCompanyBank(String companyBank) {
		this.companyBank = companyBank;
	}


	public ArrayList getBankAccountList() 
	{
		ReportModel reportModel = new ReportModel();
		
		FacesContext context = FacesContext.getCurrentInstance();
		String companyCode = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id().substring(0,3);
		System.out.println("Company Code from Service Id   - -- "+companyCode);
		bankAccountList = reportModel.getCompanyBankAccountByCompanyCode(companyCode);
		return bankAccountList;
	}


	public void setBankAccountList(ArrayList bankAccountList) {
		this.bankAccountList = bankAccountList;
	}
	

	public void getFileUploderReport()
	{
		
		try
		{
			
			ReportModel repmodel = new ReportModel();
			cardholdertranLog.clear();
			
			String cardNumber = getCard_num();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
			
			String title = getTitle();
			String author = getAuthor();

			cardholdertranLog = repmodel.getFileUploderReport(title,author,beginDate,endDate);
			System.out.print("Report size "+cardholdertranLog.size());
			
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	public void createMasterBankSetup()
	{
		
		try
		{
				ReportModel model = new ReportModel();
	       
		        FacesContext context = FacesContext.getCurrentInstance();
				String bankcode = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
		        
				String mobile  = getMobileno();
				String fname = getFirstname();
				String lname = getLastname();
				

	            String mess = model.createMasterBankSetup(bankcode, mobile, fname, lname);
	            
	            if(mess.equalsIgnoreCase("Exists"))
	            {
	            	mess = " Record Already Exists ";
	            }
	            else if(mess.equalsIgnoreCase("SUCCESS"))
	            {
	            	mess = " Record Successfully Created !";
	            	setSchemeId("create");
	            	//tranSummaryList = model.geteMasterBankRecords();
					//System.out.println("switchReportLog  list "+switchReportLog.size());
	            	
	            }
	            else
	            {
	            	mess = " Error Creating Record !";
	            	
	            }
	            
	            facesMessages.add(Severity.INFO, mess);
	            
	            setMobileno(null);
	            setFirstname(null);
	            setLastname(null);
	            	
	      
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	
	public void createCustomerBankStaffSetup()
	{
		
		try
		{
			
				ReportModel model = new ReportModel();
				
				String mess  = "";
				
				String bankStaffMobile  = getMobileno();  // bank staff mobile no
				String userNo = getLastname();  // User Mobile Number 
				//String staffNo   = getCustomerId(); // Staff Mobile Number
				
				FacesContext context = FacesContext.getCurrentInstance();
				String bankcode = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
	
			
				String existedBankcode = model.getStaffBankCode(bankStaffMobile);
				if(bankcode.equalsIgnoreCase(existedBankcode))
				{
					mess  = model.createCustomerBankStaffSetup(bankcode,bankStaffMobile,userNo);
			            
				    if(mess.equalsIgnoreCase("User Number Exists"))
					{
								mess = "User mobile number already exists";
					}
					else if(mess.equalsIgnoreCase("SUCCESS"))
					{
					           mess = " Record Successfully Created !";
					           setSchemeId("create");   	
					}
					else
					{
					         mess = " Error Creating Record !";
					            	
				    }	
							
				}
				else
				{
							mess = "Invalid Bank Staff Mobile No !";
				}
			
					
				
				
	            facesMessages.add(Severity.INFO, mess);
	            
	            setMobileno(null);
	            setFirstname(null);
	            setLastname(null);
	            setCustomerId(null);
	            	
	      
		}
		catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	
	/*Method to delete Master Bank */
	public void deleteMasterBankRecord()
	{
		try
		{
				ReportModel model = new ReportModel();
	
				String mobile = getEdit_id();
				if(mobile==null)
				{
					mobile = "";
				}
			
				System.out.println("Mobile to delete "+mobile);
				
				String mess = model.deletMasterBankRecords(mobile);
				
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
	
	public void deleteCustomerBankStaffRecord()
	{
		try
		{
				ReportModel model = new ReportModel();
	
				String mobile = getEdit_id();
				if(mobile==null)
				{
					mobile = "";
				}
			
				System.out.println("Mobile to delete "+mobile);
				
				String mess = model.deleteCustomerBankStaffRecord(mobile);
				
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
	public void setToEditMasterBankRecord()
	{
		try
		{
			ReportModel model = new ReportModel();
			
			System.out.println("SET TO EDIT VALUES . . .. "+edit_id);
			ArrayList a  = model.geteMasterBankRecords(edit_id);
			
			if(a.size()>0)
			{
				CardHolder u = (CardHolder)a.get(0);
				setMobileno(u.getPhone());
				setFirstname(u.getFirstname());
				setLastname(u.getLastname());
				setSchemeId("edit");
				
			}
			
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void updateMasterBankRecords()
	{
		
	
		try
		{
			
			ReportModel model = new ReportModel();
			
			
			String mobile  = getMobileno();
			String fname = getFirstname();
			String lname = getLastname();
			
			 FacesContext context = FacesContext.getCurrentInstance();
			 String bankcode = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
			
			String message = model.updateMasterBankRecords(mobile, fname, lname,bankcode);
			if(message.equalsIgnoreCase("SUCCESS"))
			{
				message = "Record successfully updated  ";
				setSchemeId("create");
			}
			else
			{
				message = "Unable to update record successfully updated  ";
			}
			
			facesMessages.add(Severity.INFO, message);
			setMobileno(null);
			setFirstname(null);
			setLastname(null);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	public String getMasterBankRecordByFirstName()
	{
		
		String staffNo = "";
		
		try
		{
			ReportModel model = new ReportModel();
			
			String mobile = getMobileno();
			
			staffNo = model.getMasterBankRecordByMobile(mobile);
			
			setCustomerId(staffNo);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return staffNo;
		
	}
	public void getTripMartMarchartAdminSummaryReport()
	{
		
		try
		{
			ReportModel repmodel = new ReportModel();
			cardholdertranLog.clear();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String beginDate = df.format(getStart_date());
	        beginDate = beginDate + " " + getStart_hr() + ":00";
	        
	        String endDate = df.format(getEnd_date());
	        endDate = endDate + " " + getEnd_hr() + ":59";
	        
	        FacesContext context = FacesContext.getCurrentInstance();
			//String merchantcode = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();

			String merchantcode =  "7006020116:7006020117:7006020118:7006020119";
			
			
			cardholdertranLog = repmodel.getTripMartMarchartAdminSummaryReport(merchantcode, beginDate, endDate);
			
			System.out.println("Size-- - > "+cardholdertranLog.size());
			
		}
		catch(Exception ex)
		{
			
			ex.printStackTrace();
			
			
		}
	}
	public void getSettledFailedVtuTransactionReport()
	{
			
				try
				{
					ReportModel repmodel = new ReportModel();
					cardholdertranLog.clear();
					
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		            String beginDate = df.format(getStart_date());
		            beginDate = beginDate + " " + getStart_hr() + ":00";
		            
		            String endDate = df.format(getEnd_date());
		            endDate = endDate + " " + getEnd_hr() + ":59";
			        String merchantcode = getMerchant_code();
			        
			        ProviderLog tran = null;
			        double transAmountTotal = 0.0;
			        double settleAmountTotal = 0.0;
			        
			        cardholdertranLog = repmodel.getSettledFailedVtuTransactionReport(merchantcode, beginDate, endDate);
			        System.out.print("size "+cardholdertranLog.size());
			       /* for(int i=0;i<cardholdertranLog.size();i++)
			        {
			        	tran = (ProviderLog)cardholdertranLog.get(i);
			        	if(tran.getAmount().trim().length()>0)
			        	{
			        		transAmountTotal += Double.parseDouble(tran.getAmount());
			        	}
			        	if(tran.getDestBalance().trim().length()>0)
			        	{
			        		settleAmountTotal += Double.parseDouble(tran.getDestBalance());
			        	}	
			        }
			        setTotal_amount(transAmountTotal);
			        setTotal_other_amount(settleAmountTotal);  
					
					*/
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
				
				
	}
		
	
	public void getAirTimePinReport()
	{
			
				try
				{
					ReportModel repmodel = new ReportModel();
					cardholdertranLog.clear();
					
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		            String beginDate = df.format(getStart_date());
		            beginDate = beginDate + " " + getStart_hr() + ":00";
		            
		            String endDate = df.format(getEnd_date());
		            endDate = endDate + " " + getEnd_hr() + ":59";
		            
		      
		            cardholdertranLog = repmodel.getAirTimePinReport(beginDate, endDate);
					System.out.print("Pin  size "+cardholdertranLog.size());
		
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
		
	}
	
	public void getAirTimePinlessReport()
	{
			
				try
				{
					ReportModel repmodel = new ReportModel();
					cardholdertranLog.clear();
					
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		            String beginDate = df.format(getStart_date());
		            beginDate = beginDate + " " + getStart_hr() + ":00";
		            
		            String endDate = df.format(getEnd_date());
		            endDate = endDate + " " + getEnd_hr() + ":59";

		           
		            cardholdertranLog = repmodel.getAirTimePinlessReport(beginDate,endDate);
		            System.out.print("Pineless size "+cardholdertranLog.size());

		
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
		
	}
	
	
	public void getDebitCardActivationReport()
	{
			
				try
				{
					ReportModel repmodel = new ReportModel();
					switchReportLog.clear();
					
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		            String beginDate = df.format(getStart_date());
		            beginDate = beginDate + " " + getStart_hr() + ":00";
		            
		            String endDate = df.format(getEnd_date());
		            endDate = endDate + " " + getEnd_hr() + ":59";
		            
		            String reportOption = getTrans_code();
		            if(reportOption.equalsIgnoreCase("01"))
		            {
		            	switchReportLog.clear();
		            	switchReportLog = repmodel.getFailedDebitCardActivationReport(beginDate,endDate);
			            System.out.print("failed debit card activatione "+switchReportLog.size());
		            }
		            else if(reportOption.equalsIgnoreCase("02"))
		            {
		            	switchReportLog.clear();
		            	switchReportLog = repmodel.getSuccessfulDebitCardActivationReport(beginDate, endDate);
		            	System.out.print("successful debit card activatione "+switchReportLog.size());
		            }


		
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
		
	}
	
	
	public void getSwitchLogReport()
	{
			
				try
				{
					ReportModel repmodel = new ReportModel();
					switchReportLog.clear();
					
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		            String beginDate = df.format(getStart_date());
		            beginDate = beginDate + " " + getStart_hr() + ":00";
		            
		            String endDate = df.format(getEnd_date());
		            endDate = endDate + " " + getEnd_hr() + ":59";
		            
		            String merchantCode = getMerchant_code();  // merchant code
 		            
		            String paymentRef = getSubscriber_id();  // payment Ref
		            
		            switchReportLog = repmodel.getSwitchLogReport(merchantCode,paymentRef,beginDate,endDate);
			        System.out.print("getSwitchLogReport "+switchReportLog.size());
		           

		
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
		
	}
	/*Method to get Interswitch Report by merchant code*/
	public void getInterSwitchReportByMerchant()
	{
			
				try
				{
					ReportModel repmodel = new ReportModel();
					switchReportLog.clear();
					
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		            String beginDate = df.format(getStart_date());
		            beginDate = beginDate + " " + getStart_hr() + ":00";
		            
		            String endDate = df.format(getEnd_date());
		            endDate = endDate + " " + getEnd_hr() + ":59";
		                     
		            FacesContext context = FacesContext.getCurrentInstance();
					String merchantCode = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
 		            
		            String paymentRef = getSubscriber_id();  // payment Ref
		            
		            switchReportLog = repmodel.getSwitchLogReport(merchantCode,paymentRef,beginDate,endDate);
			        System.out.print("getSwitchLogReport "+switchReportLog.size());
		           

		
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
		
	}
	
	
	/*Method to get IPayment Report by merchant code,Terminal ID, Date*/ 
	public void getIPaymentTransactionReportByMTD()
	{
			
				try
				{
					ReportModel repmodel = new ReportModel();
					switchReportLog.clear();
					
					String merchantId =  getMerchant_code(); // merchant Code
					
					String paymentRef = getDescription();  // payment Ref
					
					String status =  getOptionType(); // status 
					
					System.out.println("Status -- - >  "+status);
									
					
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		            String beginDate = df.format(getStart_date());
		            beginDate = beginDate + " " + getStart_hr() + ":00";
		            
		            String endDate = df.format(getEnd_date());
		            endDate = endDate + " " + getEnd_hr() + ":59";
		                     
		            FacesContext context = FacesContext.getCurrentInstance();
					String terminalId = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
 		            
		          		            
		            switchReportLog = repmodel.getIPaymentTransactionReport(merchantId,terminalId,status,paymentRef,beginDate,endDate);
			        System.out.print("getIPaymentTransactionReportByMTD "+switchReportLog.size());
		           

		
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
		
	}
	
	/*Method to get IPayment Report by merchant code */ 
	public void getIPaymentTransactionReportByMerchantCode()
	{
			
				try
				{
					ReportModel repmodel = new ReportModel();
					switchReportLog.clear();
					
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		            String beginDate = df.format(getStart_date());
		            beginDate = beginDate + " " + getStart_hr() + ":00";
		            
		            String endDate = df.format(getEnd_date());
		            endDate = endDate + " " + getEnd_hr() + ":59";
		                     
		            FacesContext context = FacesContext.getCurrentInstance();
					String merchantCode = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
 		            
		            switchReportLog = repmodel.getIPaymentTransactionReportByMerchantCode(merchantCode,beginDate,endDate);
		            
			        System.out.print("getIPaymentTransactionReportByMerchantCode "+switchReportLog.size());
		           

		
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
		
	}
	
	
	/*Method to get IPayment Report by merchant code */ 
	public void getIPaymentTransactionReportByUser()
	{
			
				try
				{
					ReportModel repmodel = new ReportModel();
					switchReportLog.clear();
					
					String emailaddress = getEdit_id(); // email addresses 
					String mobileNo = getMobileno(); // phone no
					String country = getOptionType();  // country
					if(country==null)
					{
						country = "";
					}
					System.out.println("Country -- >"+country);
					String merchantName = getLine_type(); // merchant desc
					if(merchantName==null)
					{
						merchantName = "";
					}
			
			       switchReportLog = repmodel.getIPaymentTransactionReportByUser(emailaddress,mobileNo,country,merchantName);
			            
				   System.out.print("getIPaymentTransactionReportByMerchantCode "+switchReportLog.size());
			           
				/*	}
					else
					{
						facesMessages.add(Severity.INFO, "Any of the field is required  ");	
						
					}*/
				
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
		
	}
	
	/*Method to get IPayment Report by email address */ 
	public void getIPaymentTransactionReportByEmail()
	{
			
				try
				{
					ReportModel repmodel = new ReportModel();
					switchReportLog.clear();
					cardholdertranLog.clear();
					
					String emailAddress = getId(); // email address
			
		            switchReportLog = repmodel.getIPaymentTransactionReportByEmail(emailAddress);
		            cardholdertranLog = repmodel.getIPaymentTransactionReportByEmailDrillDown(emailAddress);
		            
		            
			        System.out.print("getIPaymentTransactionReportByEmail "+switchReportLog.size());
			        System.out.print("getIPaymentTransactionReportByEmail "+cardholdertranLog.size());
		   
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
		
	}
	
	public void getIPaymentMerchantName()
	{
		
		try
		{
			ReportModel model = new ReportModel();
			ipaymentMerchant = model.getIPaymentMerchantName();
			System.out.println("size "+ipaymentMerchant.size());
			
		}
		catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	
	
	
	/*Method to log into a pending table */
	public void getReversaLog()
	{
				
				try
				{
					ReportModel repmodel = new ReportModel();
			
					String uniqueTransId = getEdit_id(); // unique transId
					String transNo = getSchemeId(); // trans No
					String transDesc = getSchemeName(); // trans Description
					String cardnum = getCard_num(); // cardnum
					String merchantCode = getAccountName(); // merchantcode
					String transAmount = getTransAmount(); // transamount
					String transDate = getTransDate(); // transDate
					String channelId = getChannel_id(); // channel Id
					if(channelId==null){channelId="";}
					String transRef = getMobileno(); // transRef 
					String reversaStatus = "Queued";
					String closed = getPartNo();  // closed 
					
					
					
					
					System.out.println("unique Trans id "+uniqueTransId+"Trans No"+transNo+"Trans Desc"+transDesc+"Card Num"+cardnum+"Merchant Code"+merchantCode+"Trans Amount"+transAmount+"Trans Date"+transDate+"ChannelId"+channelId+"Trans Ref"+transRef+"Reversal Status"+reversaStatus);
				    FacesContext context = FacesContext.getCurrentInstance();
					String userInitiator = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
	 		            
					
					String message  = repmodel.getReversalLog(uniqueTransId,transNo,transDesc,cardnum,merchantCode,transAmount,transDate,reversaStatus,userInitiator,channelId,transRef,closed);
					
					System.out.println("Responses Message -- > "+message);
					facesMessages.add(Severity.WARN, message);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
		
	}
	
	public void getAuthorizedReversal()
	{
				String message = "";
				try
				{
					ReportModel repmodel = new ReportModel();
					//switchReportLog.clear();
					
					String cardnum = getCard_num();
					String uniqueTransId = getEdit_id();
					//String uniqueTransId =  "02NJ22714EE8099
					String transAmount = getTransAmount(); // amount
					String merchantcode = getDescription();  // merchant code
					String channelId = getChannel_id();  // channel Id
					String transDesc = getTrans_code();  // Trans Description
					//String transNo = "000000";
					String cardExpiration = "0000000";
					String transNo = getMeterno();  // Transaction No 
					String transClosed = getTo_dest(); // closed 
					System.out.println("Closed Values s "+transClosed);
					if(transClosed==null){transClosed="";}
			
	
					System.out.println("cardnum :"+cardnum+"unique TransId:"+uniqueTransId+"trans Amount:"+transAmount+"merchant Code:"+merchantcode+"Channel Id:"+channelId+"Trans Desc: "+transDesc+"Transaction Number:"+transNo+"Closed : "+transClosed);
					
					
					Card card = new Card();
		            card.setCardNumber(cardnum);
		            card.setCardExpiration(cardExpiration);
		        
		            XRequest request = new XRequest();
		            request.setCard(card);
		            request.setTransCode(TransCode.REVERSAL);
		            request.setReference(uniqueTransId);
		            request.setTransAmount(Double.parseDouble(transAmount));
		            request.setMerchantCode(merchantcode);//
		            request.setDescription(transDesc);
		            request.setChannelId(channelId);
		            request.setTransno(transNo);
		      
		            FacesContext context = FacesContext.getCurrentInstance();
					String authUsers = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
	 		            
		            //request.setFee(0);
		          
		            XResponse xResponse = null;

		            processor = new XProcessor();
		            xResponse = processor.process(getHttpHost(), request);
		            System.out.println("XProcessor message " + xResponse.getMessage());
		            System.out.println("response code " + xResponse.getResponse());
		           
		            
		            boolean check = repmodel.getCheckUserAuthoriser(authUsers);
		            if(check==false)
		            {
		            	
		            	if(transClosed.trim().equals("3"))
						{
						
							message = repmodel.getReversal_Auth(cardnum);
			            	if(message.equalsIgnoreCase("SUCCESS"))
			            	{
			            	
			            		message = "Transaction has already been reversed ";
			            	}
						}
						else
						{
							   if(xResponse == null) 
					            {
					            	message = "Server error unable to Reverse transaction  !"; //issue occured
					            }
					            else if(xResponse.getResponse() != 0)//0 means success , so if the response is not 0
					            {
					            	message = "Server Error : " + xResponse.getResponse();
					            	//message = "Server Error : " + xResponse.getMessage();
					            }
					            else if(xResponse.getResponse() == 0) //this means success,  so i reverse transaction
					            {
					            	
					            	message = repmodel.getReversal_Auth(cardnum);
					            	if(message.equalsIgnoreCase("SUCCESS"))
					            	{
					            		message = "Reverse Transaction has been Successfully Authorized ";
					            	}
					            	else
					            	{
					            		message = " Unable to Authorize  Reversal ";
					            	}
					            	
					            }
								
						}  	
		            }
		            else
		            {
		            			message = "The user on this Profile is an initiator and does not have the privilege to authorised this reversal";
		            	
		            }
		            
		            facesMessages.add(Severity.WARN, message);
					
				
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
		
	}
	
	
	
	public void resendInterSwitchTran(String url, String webapp)
	 {
	  ReportModel reportModel = new ReportModel();
	  
	  try
	  {
	   String ref = getMobileno().substring(0, getMobileno().indexOf(":"));
	   String amt = getMobileno().substring(getMobileno().indexOf(":")+1);
	   
	   ref = ref.replaceAll(" ", "%20");
	   ref = ref.replaceAll("\n", "%0d%0a");//URL Encode for new line
	   
	   //http://demo.etranzact.com:8080/WebConnectPlus/ixs.jsp?txnref=9098859b-3d6c-41&apprAmt=323.0&para=1
	   
	   HttpMessenger ht = new HttpMessenger();
	   ht.setServer(url);//http://172.16.10.134
	   ht.setWebApp(webapp);// etzsmsapi
	   ht.setQueryString("ixs.jsp?txnref=" + ref.trim() + "&apprAmt="+ amt.trim() + "&para=1");
	   
	   try
	   {
	    ht.submit();
	    getSwitchLogReport();
	    facesMessages.add(Severity.WARN, "Transaction successfully resent");
	    
	   }catch(Exception e)
	   {
	    e.printStackTrace();
	    facesMessages.add(Severity.WARN, "Error sending transaction");
	   }
	  }
	  catch(Exception ex)
	  {
	   ex.printStackTrace();
	  }
	  
	 }
	
    


	
	
	


}
