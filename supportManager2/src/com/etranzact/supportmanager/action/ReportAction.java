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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

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
import com.etranzact.supportmanager.dto.Bill_Of_Sale;
import com.etranzact.supportmanager.dto.C_TRANSACTION;
import com.etranzact.supportmanager.dto.Car_Inventory;
import com.etranzact.supportmanager.dto.CardHolder;
import com.etranzact.supportmanager.dto.Channel;
import com.etranzact.supportmanager.dto.E_CARDSERVICE;
import com.etranzact.supportmanager.dto.E_EXCEPTION;
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
import com.etranzact.supportmanager.dto.LOTTO_LOG;
import com.etranzact.supportmanager.dto.PAYTRANS;
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
import com.etz.security.util.Cryptographer;
import com.etz.security.util.PBEncryptor;

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
	//settled option
	private ArrayList settle_batch = new ArrayList();
	
	private String subscriber_id ;
	
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


	//merchant search
	private ArrayList<E_MERCHANT> merchantSearchLog = new ArrayList<E_MERCHANT>();
	
	
	//revenue head report
	private ArrayList<E_TRANSACTION> revenueReportLog = new ArrayList<E_TRANSACTION>();
	
	//all option
	private ArrayList<E_TRANSACTION> allMerchantBankReportLog = new ArrayList<E_TRANSACTION>();
	private ArrayList<E_TRANSACTION> allMerchantTransReportLog = new ArrayList<E_TRANSACTION>();
	private ArrayList<E_TRANSACTION> allMerchantChannelReportLog = new ArrayList<E_TRANSACTION>();
	private ArrayList<E_TRANSACTION> allMerchantReportLog = new ArrayList<E_TRANSACTION>();
	
	
	//get standard split formula
	private ArrayList<E_STANDARD_SPLIT> standardSplitFormula = new ArrayList<E_STANDARD_SPLIT>();
	private ArrayList<E_MERCHANT_SPLIT> merchantSplitFormula = new ArrayList<E_MERCHANT_SPLIT>();
	private ArrayList<Summary> successfulSummaryLog = new ArrayList();
	private ArrayList mmoneyLog = new ArrayList();
	private ArrayList mmoneySummaryLog = new ArrayList();
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
	
	private List bankList = new ArrayList();
	private List appList = new ArrayList();
	private List districtList = new ArrayList();
	
	private T_SMS_RECEIVE sms_receive;
	private User curUser;
	private PAYTRANS payTrans;

	private String from_source;
	private String to_dest;
	private String status_id;
	private String terminal_id = "";
	
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
    
    private Bill_Of_Sale billOfSale;
	private ArrayList<Bill_Of_Sale> billOfSaleLists = new ArrayList();
	private List billOfSaleList = new ArrayList();
	
	private Customer customer;
	private List customerList = new ArrayList();
	
	private Car_Road_Assistance carRoadAssistance;
	private List carRoadAssistanceList = new ArrayList();
	
	private Car_body_repair_service carBodyRepairService;
	private List carBodyRepairServiceList = new ArrayList();
	
	
	@In
    FacesMessages facesMessages;
	
	@Logger
	private Log log;

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
	
	
	
	
	public void createBillOfSale()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			
			ArrayList arr = new ArrayList();
		
			String amountOfSale = getBillOfSale().getAmount_sale().trim();
			String methodOfPayment =getBillOfSale().getMethod_of_payment().trim();
			String buyersFullName = getBillOfSale().getBuyers_fullname().trim();
			String descriptioOfItemSold = getBillOfSale().getDescription_of_item_sold();
			String histor_ownerShip = getBillOfSale().getHistory_owner_ship();
			String taxes = getBillOfSale().getTaxes();
			
			System.out.println(amountOfSale+"Amount of sales");
		
			
			String message = reportModel.createBillOfSale(amountOfSale,methodOfPayment,buyersFullName,descriptioOfItemSold,histor_ownerShip,taxes);
			
			if(message.equals("Records successfully inserted"))
			{
				getBillOfSale().setAmount_sale(null);
				getBillOfSale().setBuyers_fullname(null);
				getBillOfSale().setDescription_of_item_sold(null);
				getBillOfSale().setHistory_owner_ship(null);
				getBillOfSale().setMethod_of_payment(null);
				getBillOfSale().setTaxes(null);
				
			}
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void updateBillOfSale()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			
			ArrayList arr = new ArrayList();
		
			
			String amountOfSale = getBillOfSale().getAmount_sale().trim();
			String methodOfPayment =getBillOfSale().getMethod_of_payment().trim();
			String buyersFullName = getBillOfSale().getBuyers_fullname().trim();
			String descriptioOfItemSold = getBillOfSale().getDescription_of_item_sold();
			String histor_ownerShip = getBillOfSale().getHistory_owner_ship();
			String taxes = getBillOfSale().getTaxes();
			
			System.out.println(amountOfSale+"Amount of sales");
		
			String message = reportModel.updateBillOfSale(edit_id,amountOfSale,methodOfPayment,buyersFullName,descriptioOfItemSold,histor_ownerShip,taxes);
			
			if(message.equals("Records successfully inserted"))
			{
				getBillOfSale().setAmount_sale(null);
				getBillOfSale().setBuyers_fullname(null);
				getBillOfSale().setDescription_of_item_sold(null);
				getBillOfSale().setHistory_owner_ship(null);
				getBillOfSale().setTaxes(null);
				
			}
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public List getBillOfSaleLists() 
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			billOfSaleList = reportModel.getBillOfSaleLists();
		
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return billOfSaleList;
	}
	
	/*set the car inventory to editing option*/
	public void setBillOfSaleToEdit()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			
			
			String sales_id = getEdit_id();
			System.out.println("menuitem_id " + sales_id);
			
			ArrayList billOfSaleList  = reportModel.getBillOfSaleBySaleId(sales_id);
			if(billOfSaleList.size()>0)
			{
				Bill_Of_Sale billOfSale = (Bill_Of_Sale)billOfSaleList.get(0);
				getBillOfSale().setAmount_sale(billOfSale.getAmount_sale().trim());
				getBillOfSale().setMethod_of_payment(billOfSale.getMethod_of_payment().trim());
				getBillOfSale().setBuyers_fullname(billOfSale.getBuyers_fullname().trim());
				getBillOfSale().setDescription_of_item_sold(billOfSale.getDescription_of_item_sold().trim());
				getBillOfSale().setHistory_owner_ship(billOfSale.getHistory_owner_ship().trim());
				getBillOfSale().setTaxes(billOfSale.getTaxes().trim());
				
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*Method to delete created menuitem*/
	public void deleteBillOfSale()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			String sales_id = getOperation_id().trim();
			//System.out.println("menuitem_id " + menuitem_id);
			String message  = reportModel.deleteBillOfSale(sales_id);
			setEdit_id(null);
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}

	public void createCustomer()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			
			ArrayList arr = new ArrayList();
			
			String firstName = getCustomer().getFirstname().trim();
			String lastName = getCustomer().getLastname().trim();
			String interested_in_what_car = getCustomer().getInterested_in_what_cars().trim();
			String email = getCustomer().getEmail().trim();
			String phone = getCustomer().getPhone_number().trim();
			String customerAddress = getCustomer().getCustomer_address().trim();
			String salesPerson = getCustomer().getSales_person().trim();
			System.out.println(amountOfSale+"Amount of sales");
			String message = reportModel.createCustomer(firstname,lastname,interested_in_what_car,email,phone,customerAddress,salesPerson);
				
			
			if(message.equals("Records successfully inserted"))
			{
				getCustomer.setFirstname(null);
				getCustomer.setLastname(null);
				getCustomer.setDate_of_transaction(null);
				getCustomer.setInterested_in_what_cars(null);
				getCustomer.setEmail(null);
				getCustomer.setPhone_number(null);
				getCustomer.setCustomer_address(null);
				getCustomer.setSales_person(null);
			}
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void updateCustomer()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			
			ArrayList arr = new ArrayList();
		
			
			String firstName = getCustomer().getFirstname().trim();
			String lastName = getCustomer().getLastname().trim();
			String interested_in_what_car = getCustomer().getInterested_in_what_cars().trim();
			String email = getCustomer().getEmail().trim();
			String phone = getCustomer().getPhone_number().trim();
			String customerAddress = getCustomer().getCustomer_address().trim();
			String salesPerson = getCustomer().getSales_person().trim();
			
			System.out.println(amountOfSale+"Amount of sales");
			String message = reportModel.updateCustomer(edit_id, firstName, lastName, interested_in_what_car, email, phone, customer_address, salesPerson);
			
			if(message.equals("Records successfully inserted"))
			{
				getCustomer.setFirstname(null);
				getCustomer.setLastname(null);
				getCustomer.setDate_of_transaction(null);
				getCustomer.setInterested_in_what_cars(null);
				getCustomer.setEmail(null);
				getCustomer.setPhone_number(null);
				getCustomer.setCustomer_address(null);
				getCustomer.setSales_person(null);
				
			}
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public List getCustomerLists() 
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			customerList = reportModel.getCustomerLists();
		
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return customerList;
	}
	
	public void setCustomerToEdit()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			
			
			String customerId = getEdit_id();
			System.out.println("menuitem_id " + sales_id);
			
			ArrayList customerList  = reportModel.getCustomerByCustomerId(customerId);
			if(customerList.size()>0)
			{
				Customer customer = (Customer)customerList.get(0);
				getCustomer().setFirstname(customer.getFirstname().trim());
				getCustomer().setLastname(customer.getLastname().trim());
				getCustomer().setInterested_in_what_cars(customer.getInterested_in_what_cars().trim());
				getCustomer().setEmail(customer.getEmail().trim());
				getCustomer().setPhone_number(customer.getPhone_number().trim());
				getCustomer().setCustomer_address(customer.getCustomer_address().trim());
				getCustomer().setSales_person(customer.getSales_person().trim());
		
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void deleteCustomer()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			String customer_Id = getOperation_id().trim();
			//System.out.println("menuitem_id " + menuitem_id);
			String message  = reportModel.deleteCustomer(customer_Id);
			setEdit_id(null);
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	public void createCarRoadAssistance()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			
			ArrayList arr = new ArrayList();
			
			String road_assistance_service = getCarRoadAssistance().getRoad_assistance__service().trim();
			String price = getCarRoadAssistance().getPrice().trim();
			String insurance_company = getCarRoadAssistance().getInsurance_company().trim();
			String phone = getCarRoadAssistance().getPhone_number().trim();
			String email = getCarRoadAssistance().getEmail().trim();
			String coverage = getCarRoadAssistance().getCustomer_address().trim();
			System.out.println(coverage+"Coverage");
		
			String message = reportModel.createCarRoadAssistance(road_assistance_service,price, insurance_company,phone, email, coverage);
		
			if(message.equals("Records successfully inserted"))
			{
				getCarRoadAssistance.setRoad_assistance__service(null);
				getCarRoadAssistance.setPrice(null);
				getCarRoadAssistance.setInsurance_company(null);
				getCarRoadAssistance.setPhone_number(null);
				getCarRoadAssistance.setEmail(null);
				getCarRoadAssistance.setCoverage(null);

			}
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public void updateCarRoadAssistance()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			
			ArrayList arr = new ArrayList();
		
			String road_assistance_service = getCarRoadAssistance().getRoad_assistance__service().trim();
			String price = getCarRoadAssistance().getPrice().trim();
			String insurance_company = getCarRoadAssistance().getInsurance_company().trim();
			String phone = getCarRoadAssistance().getPhone_number().trim();
			String email = getCarRoadAssistance().getEmail().trim();
			String coverage = getCarRoadAssistance().getCustomer_address().trim();
			System.out.println(coverage+"Coverage");
			
			System.out.println(amountOfSale+"Amount of sales");
			
			String message = reportModel.updateCarRoadAssistance(edit_id, road_assistance_service,price, insurance_company,phone, email, coverage);
			
			if(message.equals("Records successfully updated"))
			{
				getCarRoadAssistance.setRoad_assistance__service(null);
				getCarRoadAssistance.setPrice(null);
				getCarRoadAssistance.setInsurance_company(null);
				getCarRoadAssistance.setPhone_number(null);
				getCarRoadAssistance.setEmail(null);
				getCarRoadAssistance.setCoverage(null);
				
			}
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public List getCarRoadAssistanceList() 
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			carRoadAssistanceList = reportModel.getCarRoadAssistanceList();
		
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return customerList;
	}
	
	public void setCarRoadAssistanceToEdit()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			
			
			String rank_id = getEdit_id();
			System.out.println("menuitem_id " + sales_id);
			
			ArrayList carRoadAssistanceList  = reportModel.getCarRoadAssistanceByRankId(rank_id);
			if(carRoadAssistanceList.size()>0)
			{
				Car_Road_Assistance carRoadAssistance = (Car_Road_Assistance)carRoadAssistanceList.get(0);
				getCarRoadAssistance().setRoad_assistance__service(carRoadAssistance.getCarRoadAssistance().trim());
				getCarRoadAssistance().setPrice(carRoadAssistance.getPrice().trim());
				getCarRoadAssistance().setDate_of_transaction(carRoadAssistance.getDate_of_transaction().trim());
				getCarRoadAssistance().setInsurance_company(carRoadAssistance.getInsurance_company().trim());
				getCarRoadAssistance().setEmail(carRoadAssistance.getEmail().trim());
				getCarRoadAssistance().setPhone_number(carRoadAssistance.getPhone_number().trim());
				getCarRoadAssistance().setCoverage(carRoadAssistance.getCoverage().trim());

			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void deleteCarRoadAssistance()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			String rank_id = getOperation_id().trim();
			//System.out.println("menuitem_id " + menuitem_id);
			String message  = reportModel.deleteCarRoadAssistance(rank_id);
			setEdit_id(null);
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	public void createCarBodyRepairService()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			
			ArrayList arr = new ArrayList();
			String vehicle_make = getCarBodyRepairService().getVehicle_make().trim();
			String vehicle_model = getCarBodyRepairService().getVehicle_model.trim();
			String vehicle_year = getCarBodyRepairService().getVehicle_year().trim();
			String vehicle_color = getCarBodyRepairService().getVehicle_color().trim();
			String repair_description = getCarBodyRepairService().getRepair_description().trim();
			String vehicle_damage = getCarBodyRepairService().getVehicle_demage().trim();
			String insurance_company_paying_for_repair = getCarBodyRepairService().getInsurance_company_paying_for_repair().trim();
			
			System.out.println(amountOfSale+"Amount of sales");
		
			
			String message = reportModel.createCarBodyRepairService(vehicle_make, vehicle_model, vehicle_year, vehicle_color, repair_description, vehicle_damage,insurance_company_paying_for_repair);
			
			if(message.equals("Records successfully inserted"))
			{
				getCarBodyRepairService().setVehicle_make(null);
				getCarBodyRepairService().setVehicle_model(null);
				getCarBodyRepairService().setVehicle_year(null);
				getCarBodyRepairService().setVehicle_color(null);
				getCarBodyRepairService().setRepair_description(null);
				getCarBodyRepairService().setVehicle_demage(null);
				getCarBodyRepairService().setInsurance_company_paying_for_repair(null);
			}
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void updateCarBodyRepairService()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			
			ArrayList arr = new ArrayList();
		
			String vehicle_make = getCarBodyRepairService().getVehicle_make().trim();
			String vehicle_model = getCarBodyRepairService().getVehicle_model.trim();
			String vehicle_year = getCarBodyRepairService().getVehicle_year().trim();
			String vehicle_color = getCarBodyRepairService().getVehicle_color().trim();
			String repair_description = getCarBodyRepairService().getRepair_description().trim();
			String vehicle_damage = getCarBodyRepairService().getVehicle_demage().trim();
			
			System.out.println(vehicle_damage+"vehicle_damage");
			
			String message = reportModel.updateCarBodyRepairService(edit_id,vehicle_make,vehicle_model, vehicle_year,vehicle_color, repair_description, vehicle_damage,insurance_company_paying_for_repair);
			
			if(message.equals("Records successfully updated"))
			{
				getCarBodyRepairService().setVehicle_make(null);
				getCarBodyRepairService().setVehicle_model(null);
				getCarBodyRepairService().setVehicle_year(null);
				getCarBodyRepairService().setVehicle_color(null);
				getCarBodyRepairService().setRepair_description(null);
				getCarBodyRepairService().setVehicle_demage(null);
				getCarBodyRepairService().setInsurance_company_paying_for_repair(null);
				
			}
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public List getCarBodyRepairServiceList() 
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			carBodyRepairServiceList = reportModel.getCarBodyRepairServiceList();
		
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return customerList;
	}
	
	public void setCarBodyRepairServiceToEdit()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			
			
			String repairId = getEdit_id();
			System.out.println("menuitem_id " + sales_id);
			
			ArrayList carBodyRepairServiceList  = reportModel.getCarBodyRepairServiceByRepairId(repairId);
			if(carBodyRepairServiceList.size()>0)
			{
				Car_body_repair_service carBodyRepairService = (Car_body_repair_service)carBodyRepairServiceList.get(0);
				getCarBodyRepairService().setRepair_id(carBodyRepairService.getCarRoadAssistance().trim());
				getCarBodyRepairService().setVehicle_make(carBodyRepairService.getVehicle_make().trim());
				getCarBodyRepairService().setVehicle_model(carBodyRepairService.getVehicle_model().trim());
				getCarBodyRepairService().setVehicle_year(carBodyRepairService.getVehicle_year().trim());
				getCarBodyRepairService().setVehicle_color(carBodyRepairService.getVehicle_color().trim());
				getCarBodyRepairService().setRepair_description(carBodyRepairService.getRepair_description().trim());
				getCarBodyRepairService().setVehicle_demage(carBodyRepairService.getVehicle_demage().trim());
				getCarBodyRepairService().setInsurance_company_paying_for_repair(carBodyRepairService.getInsurance_company_paying_for_repair().trim());
				
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void deleteCarBodyRepairService()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			String repair_id = getOperation_id().trim();
			//System.out.println("menuitem_id " + menuitem_id);
			String message  = reportModel.deleteCarBodyRepairService(rank_id);
			setEdit_id(null);
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
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
					
					supportLog = reportModel.getSupportLog(getChannel_id(), getCard_num(), beginDate, endDate,
							rowcount, getStatus_id(), getMerchant_code(), user_code, getTerminal_id());

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
								rowcount, getStatus_id(), getMerchant_code(), user_code, getTerminal_id());
						
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
	
	
	
	/*Mobile Subscription Details*/
	public void viewMobileSubscriptionDetials()
	{
		try
		{
			mobileSubscriberLog.clear();
			mobileSubscriberCardLog.clear();
			ReportModel reportModel = new ReportModel();
			
			System.out.println("mobile " + getFrom_source().trim());
			
			if(getLine_type().equals("Version I"))
			{
				mobileSubscriberLog = reportModel.getE_MOBILE_SUBSCRIBER(getFrom_source().trim(), getLine_type(),"");
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
			
			smsReceiveLog = reportModel.getT_SMS_RECEIVE(id, mobile, user_code);
			smsLog = reportModel.getE_SMS_LOG("02"+id, mobile, user_code);
			failedFundsLog = reportModel.getE_REQUEST_LOG("02"+id, mobile, user_code);
			smsSendLog = reportModel.getT_SMS_SEND(id, mobile, user_code);
			vtuLog = reportModel.getVTUByTransID("02"+id, mobile, user_code);
			tranLog = reportModel.getE_TRANSACTION("02"+id, user_code);	
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
	/*public void block_unblockCardVersionII()
	{		
		try
		{
			
			System.out.println("about to call webservice");
			
			Cryptographer crypt = new Cryptographer();       
            
			HttpServletRequest str = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String ipAddress = str.getRemoteAddr();
			
			File g = new File("");
		    Properties props = new Properties();
		    InputStream in = (InputStream)(new FileInputStream(new File(g.getAbsolutePath() + "\\" + "xportal-config.properties")));
		    props.load(in);
		    String setup_excel_folder = props.getProperty("xportal_webservice");
		     
			
			String setup_excel_folder = "c:/XPortalWebServiceFile/XPortalWS.xml";
			
          //  xportalserviceclient.XPortalWSService service = new xportalserviceclient.XPortalWSService(new URL("file:"+ setup_excel_folder +""));
           // xportalserviceclient.XPortalWS port = service.getXPortalWSPort();
             
            String type = getOperation_id();
            String cardnum = getEdit_id();
            String mac = type + cardnum + ipAddress;
            mac = crypt.doMd5Hash(mac);
            
			java.lang.String type = getOperation_id();
            java.lang.String cardnum = getEdit_id();
            java.lang.String ipAddress = "127.0.0.1";
            java.lang.String mac = "C9E6529E3A0A65E0EDBC593F54A4CE0D";
			
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
	*/
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
            
            if(!optionType.equals("A") && !optionType.equals("B"))
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
			//all and detailed report
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
							if(est.getOwner_card().equals("0690019914") || est.getOwner_card().equals("0690000000") || est.getOwner_card().equals("0570019947") || est.getOwner_card().endsWith("0000000") || est.getOwner_card().equals("0570019902") || est.getOwner_card().equals("0443241197"))//etranzact commission owner cards
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
            						
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197"))//etranzact commission split cards
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
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197"))//etranzact commission split cards
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
							if(est.getOwner_card().equals("0690019914") || est.getOwner_card().equals("0690000000") || est.getOwner_card().equals("0570019947") || est.getOwner_card().endsWith("0000000") || est.getOwner_card().equals("0570019902") || est.getOwner_card().equals("0443241197"))//etranzact commission owner cards
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
            						
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197"))//etranzact commission split cards
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
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197"))//etranzact commission split cards
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
							if(est.getOwner_card().equals("0690019914") || est.getOwner_card().equals("0690000000") || est.getOwner_card().equals("0570019947") || est.getOwner_card().endsWith("0000000") || est.getOwner_card().equals("0570019902") || est.getOwner_card().equals("0443241197"))//etranzact commission owner cards
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
            						
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197"))//etranzact commission split cards
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
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197"))//etranzact commission split cards
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
							if(est.getOwner_card().equals("0690019914") || est.getOwner_card().equals("0690000000") || est.getOwner_card().equals("0570019947") || est.getOwner_card().endsWith("0000000") || est.getOwner_card().equals("0570019902") || est.getOwner_card().equals("0443241197"))//etranzact commission owner cards
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
            						
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197"))//etranzact commission split cards
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
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197"))//etranzact commission split cards
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
							if(est.getOwner_card().equals("0690019914") || est.getOwner_card().equals("0690000000") || est.getOwner_card().equals("0570019947") || est.getOwner_card().endsWith("0000000") || est.getOwner_card().equals("0570019902") || est.getOwner_card().equals("0443241197"))//etranzact commission owner cards
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
            						
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197"))//etranzact commission split cards
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
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197"))//etranzact commission split cards
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
							if(est.getOwner_card().equals("0690019914") || est.getOwner_card().equals("0690000000") || est.getOwner_card().equals("0570019947") || est.getOwner_card().endsWith("0000000") || est.getOwner_card().equals("0570019902")  || est.getOwner_card().equals("0443241197"))//etranzact commission owner cards
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
            						
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197"))//etranzact commission split cards
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
            						if(em.getSplit_card().equals("0690019914") || em.getSplit_card().equals("0690000000") || em.getSplit_card().equals("0570019947") || em.getSplit_card().endsWith("0000000") || em.getSplit_card().equals("0570019902") || em.getSplit_card().equals("0443241197"))//etranzact commission split cards
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
	        //System.out.println("switchReportLog " + switchReportLog.size());
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
			
	        E_TRANSACTION tran = null;
	        double debit_total = 0.0;
	        double credit_total = 0.0;
	        
			String card_number_mobile = getCard_num().trim();
			
			if(card_number_mobile.trim().length()>0)
			{
				if(card_number_mobile.startsWith("234"))
				{
					card_number_mobile = "0" + card_number_mobile.substring(3, card_number_mobile.length());
				}
				else;
			}
			
			System.out.println("card_number_mobile " + card_number_mobile);
	        
	        //get the card number
			String card_number = reportModel.getCardNumber(card_number_mobile).trim();
	        if(card_number.length()<=0)
	        {
	        	facesMessages.add(Severity.WARN, "Card number doesnt exist.");
	        }
	        else
	        {
	        	mmoneyLog = reportModel.getCardHolders(card_number);
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
    
	
	/*This method is used to get various details from c_transaction for phcn*/
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
	        String district = getLine_type();
	        String merchant_code = "1234567890";
	        
	        if(district.equals("ALL"))
	        {
	        	district = "";
	        }
	        
	        double b = 0.0;
	        
	        billLog = reportModel.getPHCNReport(beginDate, endDate, optionType, district, merchant_code);
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
	    	    
	    	    /*writer.append(et.getSubscriber_id().trim().replaceAll(",", " "));
	    	    writer.append(';');
	    	    writer.append(et.getT_address().trim().replaceAll(",", " "));
	    	    writer.append(';');
	    	    writer.append(et.getIssuer_code().trim().replaceAll(",", " "));
	    	    writer.append(';');
	    	    writer.append(et.getCard_subname().trim().replaceAll(",", " "));
	    	    writer.append(';');
	    	    writer.append(et.getSub_code().trim().replaceAll(",", " "));
	    	    writer.append(';');
	    	    writer.append(et.getTrans_note().trim().replaceAll(",", " "));
	    	    writer.append(';');
	    	    writer.append(et.getStatus_desc().trim().replaceAll(",", " "));
	    	    writer.append(';');
	    	    writer.append('\n');*/
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

    
	

	public void resetValues()
	{
		System.out.println("reset data");
		
		edit_id = null;
		billOfSale = new Bill_Of_Sale();
		setBillOfSale(null);
		billOfSaleList.clear();
	
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
		
		merchantSplitFormula.clear();
		
		mmoneyLog.clear();
		mmoneySummaryLog.clear();
		switchReportLog.clear();
		cardserviceLog.clear();
		cardholdertranLog.clear();
		cardholdertranByMerchantCodeLog.clear();
		failedFundsLog.clear();
		fundsTransfer.clear();
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
	        
	        System.out.println("channelid : "+channelid);
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
	    	
			if(tarrifType.equals("PHCN: TARRIFF") || tarrifType.equals("ANY"))
			{
				FacesContext context = FacesContext.getCurrentInstance();
				String disco = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();
		    	
				merchantCode += reportModel.getDistinctPostpaidPHCNMerchantCode(disco, "mobile_merchant_code");
				merchantCode += reportModel.getDistinctPostpaidPHCNMerchantCode(disco, "pos_merchant_code");
				merchantCode += reportModel.getDistinctPostpaidPHCNMerchantCode(disco, "payoutlet_merchant_code");
				//merchantCode += reportModel.getDistinctPHCNMerchantCode(disco, "web_merchant_code");
			}
			else
			{
				merchantCode += "7006020013:7006020014:7006020015";
			}
			
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


	public String getTransSummary() {
		return transSummary;
	}


	public void setTransSummary(String transSummary) {
		this.transSummary = transSummary;
	}


	public Bill_Of_Sale getBillOfSale() {
		if(billOfSale ==  null) {
			billOfSale = new Bill_Of_Sale();
		}
		return billOfSale;
	}


	public void setBillOfSale(Bill_Of_Sale billOfSale) {
		this.billOfSale = billOfSale;
	}


	public List getBillOfSaleList() {
		return billOfSaleList;
	}


	public void setBillOfSaleList(List billOfSaleList) {
		this.billOfSaleList = billOfSaleList;
	}


	public void setBillOfSaleLists(ArrayList<Bill_Of_Sale> billOfSaleLists) {
		this.billOfSaleLists = billOfSaleLists;
	}


	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		if(customer == null) {
			customer = new Customer();
		}
		return customer;
	}


	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}


	/**
	 * @return the customerList
	 */
	public List getCustomerList() {
		return customerList;
	}


	/**
	 * @param customerList the customerList to set
	 */
	public void setCustomerList(List customerList) {
		this.customerList = customerList;
	}


	/**
	 * @return the carRoadAssistance
	 */
	public Car_Road_Assistance getCarRoadAssistance() {
		if(carRoadAssistance == null) {
			carRoadAssistance = new Car_Road_Assistance();
		}
		return carRoadAssistance;
	}


	/**
	 * @param carRoadAssistance the carRoadAssistance to set
	 */
	public void setCarRoadAssistance(Car_Road_Assistance carRoadAssistance) {
		this.carRoadAssistance = carRoadAssistance;
	}


	/**
	 * @return the carRoadAssistanceList
	 */
	public List getCarRoadAssistanceList() {
		return carRoadAssistanceList;
	}


	/**
	 * @param carRoadAssistanceList the carRoadAssistanceList to set
	 */
	public void setCarRoadAssistanceList(List carRoadAssistanceList) {
		this.carRoadAssistanceList = carRoadAssistanceList;
	}


	/**
	 * @return the carBodyRepairService
	 */
	public Car_body_repair_service getCarBodyRepairService() {
		if(carBodyRepairService ==  null) {
			carBodyRepairService = new Car_body_repair_service();
		}
		return carBodyRepairService;
	}


	/**
	 * @param carBodyRepairService the carBodyRepairService to set
	 */
	public void setCarBodyRepairService(Car_body_repair_service carBodyRepairService) {
		this.carBodyRepairService = carBodyRepairService;
	}


	/**
	 * @return the carBodyRepairServiceList
	 */
	public List getCarBodyRepairServiceList() {
		return carBodyRepairServiceList;
	}


	/**
	 * @param carBodyRepairServiceList the carBodyRepairServiceList to set
	 */
	public void setCarBodyRepairServiceList(List carBodyRepairServiceList) {
		this.carBodyRepairServiceList = carBodyRepairServiceList;
	}
	
	

	
	
	
	
	
}
