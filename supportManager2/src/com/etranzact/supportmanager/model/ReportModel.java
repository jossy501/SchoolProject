/**
 * 
 */
package com.etranzact.supportmanager.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.etranzact.psm.dto.TPsmDealer;
import com.etranzact.supportmanager.dto.Bank;
import com.etranzact.supportmanager.dto.Bill_Of_Sale;
import com.etranzact.supportmanager.dto.COP_FUNDGATE_LOG;
import com.etranzact.supportmanager.dto.C_TRANSACTION;
import com.etranzact.supportmanager.dto.Car_Inventory;
import com.etranzact.supportmanager.dto.CardHolder;
import com.etranzact.supportmanager.dto.Channel;
import com.etranzact.supportmanager.dto.District;
import com.etranzact.supportmanager.dto.E_CARDSERVICE;
import com.etranzact.supportmanager.dto.E_CATSCALE;
import com.etranzact.supportmanager.dto.E_EXCEPTION;
import com.etranzact.supportmanager.dto.E_MERCHANT;
import com.etranzact.supportmanager.dto.E_MERCHANT_SPLIT;
import com.etranzact.supportmanager.dto.E_MOBILE_SUBSCRIBER;
import com.etranzact.supportmanager.dto.E_MOBILE_SUBSCRIBER_CARD;
import com.etranzact.supportmanager.dto.E_POSREQUEST;
import com.etranzact.supportmanager.dto.E_SETTLEMENTDOWNLOAD_BK;
import com.etranzact.supportmanager.dto.E_SETTLE_BATCH;
import com.etranzact.supportmanager.dto.E_STANDARD_SPLIT;
import com.etranzact.supportmanager.dto.E_TMCEVENT;
import com.etranzact.supportmanager.dto.E_TMCNODE;
import com.etranzact.supportmanager.dto.E_TMCREQUEST;
import com.etranzact.supportmanager.dto.E_TRANSACTION;
import com.etranzact.supportmanager.dto.E_TRANSCODE;
import com.etranzact.supportmanager.dto.LOTTO_LOG;
import com.etranzact.supportmanager.dto.M_Incoming_Messages;
import com.etranzact.supportmanager.dto.M_Outgoing_Messages;
import com.etranzact.supportmanager.dto.MenuItem;
import com.etranzact.supportmanager.dto.PAYTRANS;
import com.etranzact.supportmanager.dto.ProviderLog;
import com.etranzact.supportmanager.dto.REQUEST_LOG;
import com.etranzact.supportmanager.dto.REVERSAL;
import com.etranzact.supportmanager.dto.R_pins_bought;
import com.etranzact.supportmanager.dto.SMS_LOG;
import com.etranzact.supportmanager.dto.Summary;
import com.etranzact.supportmanager.dto.T_SMS_RECEIVE;
import com.etranzact.supportmanager.dto.T_SMS_SEND;
import com.etranzact.supportmanager.dto.VTU_LOG;
import com.etranzact.supportmanager.utility.Env;
import com.etranzact.supportmanager.utility.HashNumber;
import com.etz.security.util.Cryptographer;


/**
 * @author Joshua.Aruno
 *
 */
public class ReportModel 
{

	public ReportModel(){}

	
	
	/*Method to create the ip address restriction*/
	public String createBillOfSale(String amountOfSale, String methodOfPayment, String buyersFullName, String descriptioOfItemSold, String histor_ownerShip, String taxes )
	{
		int output = -1;
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			con = connectToSupportLog();
			stat = con.createStatement();

	
			String query = "insert into telcodb.dbo.bill_of_sales(amount_of_sale, method_of_payment,buyers,description_of_item_sold,history_owner_ship,taxes,date_of_transaction)" +
					" values('"+amountOfSale+"','"+methodOfPayment+"','"+buyersFullName+"','"+descriptioOfItemSold+"','"+histor_ownerShip+"','"+taxes+"',getDate())";
			//System.out.println("query " + query);
			output = stat.executeUpdate(query);
			
			if(output > 0)
			{
				con.commit();
				message = "Records successfully inserted";
			}
			else
			{
				con.rollback();
				message = "Records not inserted";
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			try
			{
				con.rollback();
				message = "Error occured while creating bill of sale";
			}
			catch(Exception e){}
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return message;
	}
	
	/*Method to create the ip address restriction*/
	public String updateBillOfSale(String sales_id, String amountOfSale, String methodOfPayment, String buyersFullName, String descriptioOfItemSold, String histor_ownerShip, String taxes )
	{
		int output = -1;
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			con = connectToSupportLog();
			stat = con.createStatement();
			
			output = stat.executeUpdate("update telcodb.dbo.bill_of_sales set amount_of_sale= '"+amountOfSale+"', method_of_payment = '"+methodOfPayment+"', buyers ='"+buyersFullName+"'"
					+ ", description_of_item_sold = '"+descriptioOfItemSold+"', history_owner_ship = '"+histor_ownerShip+"', taxes = '"+taxes+"' where sales_id = '"+sales_id+"'");
			//System.out.println("query " + query);
			//output = stat.executeUpdate(query);
			
			if(output > 0)
			{
				con.commit();
				message = "Records successfully updated";
			}
			else
			{
				con.rollback();
				message = "Records not updated";
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			try
			{
				con.rollback();
				message = "Error occured while updating bill of sale";
			}
			catch(Exception e){}
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return message;
	}
	
	public ArrayList getBillOfSaleLists()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Bill_Of_Sale billOfSale = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;

		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			query = "select sales_id, "
					+ "amount_of_sale, "
					+ "method_of_payment, "
					+ "date_of_transaction,"
					+ " buyers,"
					+ " description_of_item_sold,"
					+ " history_owner_ship, taxes from telcodb.dbo.bill_of_sales";
			
			System.out.println("getMenuItems " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
	
				billOfSale = new Bill_Of_Sale();
				billOfSale.setCounter(""+counter);
				billOfSale.setSales_id(""+result.getObject(1));
				billOfSale.setAmount_sale(""+result.getObject(2));
				billOfSale.setMethod_of_payment(""+result.getObject(3));
				billOfSale.setDate_of_transaction(""+result.getObject(4));
				billOfSale.setBuyers_fullname(""+result.getObject(5));
				billOfSale.setDescription_of_item_sold(""+result.getObject(6));
				billOfSale.setHistory_owner_ship(""+result.getObject(7));
				billOfSale.setTaxes(""+result.getObject(8));
				arr.add(billOfSale);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return arr;
	}
	
	public ArrayList getBillOfSaleBySaleId(String sale_id)
	{
		ArrayList arr = new ArrayList();
		Car_Inventory carInventory;
		Bill_Of_Sale billOfSale;
		String empty = "";
		String query = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			query = "select sales_id, amount_of_sale, method_of_payment, date_of_transaction, buyers, description_of_item_sold, history_owner_ship,taxes from telcodb.dbo.bill_of_sales where sales_id ='"+sale_id+"'";
			System.out.println("getMenuItems " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				billOfSale = new Bill_Of_Sale();
				billOfSale.setSales_id(""+result.getObject(1));
				billOfSale.setAmount_sale(""+result.getObject(2));
				billOfSale.setMethod_of_payment(""+result.getObject(3));
				billOfSale.setDate_of_transaction(""+result.getObject(4));
				billOfSale.setBuyers_fullname(""+result.getObject(5));
				billOfSale.setDescription_of_item_sold(""+result.getObject(6));
				billOfSale.setHistory_owner_ship(""+result.getObject(7));
				billOfSale.setTaxes(""+result.getObject(8));
				arr.add(billOfSale);
			}
		}
		catch(SQLException sq)
		{
			System.out.println("error " + sq.getMessage());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return arr;
	}
	
	
	/*This method is used to delete the menuitem created*/
	public String deleteBillOfSale(String sales_id)
	{
		int output = -1;
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			con = connectToSupportLog();
			stat = con.createStatement();
			
			String query = "delete from telcodb.dbo.bill_of_sales where sales_id = "+sales_id+" ";

			output = stat.executeUpdate(query);
			
			if(output > 0){
				con.commit();
				message = "Records successfully deleted";
			}
			else{
				con.rollback();
				message = "Records not deleted";
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			try
			{
				con.rollback();
				message = "Error occured while deleting menuitem";
			}
			catch(Exception e){}
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return message;
	}
	
	
	/*Method to create the ip address restriction*/
	public String createCustomer(String firstname, String lastname, String interest_in_what_car, String email, String phone_number, String customer_address, String sales_person)
	{
		int output = -1;
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			con = connectToSupportLog();
			stat = con.createStatement();
	
			String query = "insert into telcodb.dbo.customer(firstname, lastname,interested_in_what_car,email,phone_number,customer_address,Sales_person,date_of_transaction)" +
					" values('"+firstname+"','"+lastname+"','"+interest_in_what_car+"','"+email+"','"+phone_number+"','"+customer_address+"','"+sales_person+"',getDate())";
			System.out.println("query " + query);
			output = stat.executeUpdate(query);
			
			if(output > 0)
			{
				con.commit();
				message = "Records successfully inserted";
			}
			else
			{
				con.rollback();
				message = "Records not inserted";
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			try
			{
				con.rollback();
				message = "Error occured while creating bill of sale";
			}
			catch(Exception e){}
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return message;
	}
	
	/*Method to create the ip address restriction*/
	public String updateCustomer(String customer_id, String firstname, String lastname, String buyers, String interest_in_what_car, String email, String phone_number, String customer_address, String sales_person)
	{
		int output = -1;
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			con = connectToSupportLog();
			stat = con.createStatement();
			
			output = stat.executeUpdate("update telcodb.dbo.customer set firstname= '"+firstname+"', lastname = '"+lastname+"', buyers ='"+buyers+"'"
					+ ", interested_in_what_car = '"+interest_in_what_car+"', email = '"+email+"', phone_number = '"+phone_number+"', customer_address = '"+customer_address+"', Sales_person = '"+sales_person+"' where customer_id = '"+customer_id+"'");
			System.out.println("query " + query);
			//output = stat.executeUpdate(query);
			
			if(output > 0)
			{
				con.commit();
				message = "Records successfully updated";
			}
			else
			{
				con.rollback();
				message = "Records not updated";
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			try
			{
				con.rollback();
				message = "Error occured while updating bill of sale";
			}
			catch(Exception e){}
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return message;
	}
	
	public ArrayList getCustomerLists()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Bill_Of_Sale billOfSale = null;
		Customer customer = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;

		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			query = "select customer_id, "
					+ "firstname, "
					+ "lastname, "
					+ "date_of_transaction,"
					+ " interested_in_what_car,"
					+ "email,"
					+ "phone_number,"
					+ "customer_address,"
					+ " Sales_person from telcodb.dbo.customer";
			
			System.out.println("getMenuItems " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				customer = new Customer();
				customer.setCounter(""+counter);
				customer.setFirstname(""+result.getObject(1));
				customer.setLastname(""+result.getObject(2));
				customer.setDate_of_transaction(""+result.getObject(3));
				customer.setInterested_in_what_cars(""+result.getObject(4));
				customer.setEmail(""+result.getObject(5));
				customer.setPhone_number(""+result.getObject(6));
				customer.setCustomer_address(""+result.getObject(7));
				customer.setSales_person(""+result.getObject(8));
				arr.add(customer);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return arr;
	}
	
	public ArrayList getCustomerByCustomerId(String customerId)
	{
		ArrayList arr = new ArrayList();
		Car_Inventory carInventory;
		Bill_Of_Sale billOfSale;
		String empty = "";
		String query = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			query = "select customer_id, "
					+ "firstname, "
					+ "lastname, "
					+ "date_of_transaction,"
					+ " interested_in_what_car,"
					+ "email,"
					+ "phone_number,"
					+ "customer_address,"
					+ " Sales_person from telcodb.dbo.customer where customer_id ='"+customerId+"' ";
			
			System.out.println("getMenuItems " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				customer = new Customer();
				customer.setFirstname(""+result.getObject(1));
				customer.setLastname(""+result.getObject(2));
				customer.setDate_of_transaction(""+result.getObject(3));
				customer.setInterested_in_what_cars(""+result.getObject(4));
				customer.setEmail(""+result.getObject(5));
				customer.setPhone_number(""+result.getObject(6));
				customer.setCustomer_address(""+result.getObject(7));
				customer.setSales_person(""+result.getObject(8));
				arr.add(customer);
			}
		}
		catch(SQLException sq)
		{
			System.out.println("error " + sq.getMessage());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return arr;
	}
	
	public String deleteCustomer(String customerId)
	{
		int output = -1;
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			con = connectToSupportLog();
			stat = con.createStatement();
			
			String query = "delete from telcodb.dbo.customer where customer_id ='"+customerId+"' ";

			output = stat.executeUpdate(query);
			
			if(output > 0){
				con.commit();
				message = "Records successfully deleted";
			}
			else{
				con.rollback();
				message = "Records not deleted";
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			try
			{
				con.rollback();
				message = "Error occured while deleting menuitem";
			}
			catch(Exception e){}
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return message;
	}
	
	/*Method to create the ip address restriction*/
	public String createCarRoadAssistance(String road_assistance_service, String price, String insurance_company,String phone_number, String email, String coverage)
	{
		int output = -1;
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			con = connectToSupportLog();
			stat = con.createStatement();
	
			String query = "insert into telcodb.dbo.car_road_assistance(road_assistance_service, price,insurance_company,phone_number,email,coverage,date_of_transaction)" +
					" values('"+road_assistance_service+"','"+price+"','"+insurance_company+"','"+phone_number+"','"+email+"','"+coverage+"',getDate())";
			System.out.println("query " + query);
			output = stat.executeUpdate(query);
			
			if(output > 0)
			{
				con.commit();
				message = "Records successfully inserted";
			}
			else
			{
				con.rollback();
				message = "Records not inserted";
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			try
			{
				con.rollback();
				message = "Error occured while creating bill of sale";
			}
			catch(Exception e){}
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return message;
	}
	
	public String updateCarRoadAssistance(String rank_id, String road_assistance_service, String price, String insurance_company,String phone_number, String email, String coverage)
	{
		int output = -1;
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			con = connectToSupportLog();
			stat = con.createStatement();
			
			output = stat.executeUpdate("update telcodb.dbo.car_road_assistance set road_assistance_service= '"+road_assistance_service+"', price = '"+price+"', insurance_company ='"+insurance_company+"'"
					+ ", phone_number = '"+phone_number+"', email = '"+email+"', coverage = '"+coverage+"' where rank_id = '"+rank_id+"'");
			System.out.println("query " + query);
			//output = stat.executeUpdate(query);
			
			if(output > 0)
			{
				con.commit();
				message = "Records successfully updated";
			}
			else
			{
				con.rollback();
				message = "Records not updated";
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			try
			{
				con.rollback();
				message = "Error occured while updating bill of sale";
			}
			catch(Exception e){}
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return message;
	}
	
	public ArrayList getCarRoadAssistanceList()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Car_Road_Assistance carRoadAssistance = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;

		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			query = "select rank_id, "
					+ "road_assistance_service, "
					+ "price, "
					+ "date_of_transaction,"
					+ " insurance_company,"
					+ "phone_number,"
					+ "email,"
					+ " coverage from telcodb.dbo.car_road_assistance";
			System.out.println("getMenuItems " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				carRoadAssistance = new Car_Road_Assistance();
				carRoadAssistance.setCounter(""+counter);
				carRoadAssistance.setRank_id(""+result.getObject(1));
				carRoadAssistance.setRoad_assistance_id(""+result.getObject(2));
				carRoadAssistance.setPrice(""+result.getObject(3));
				carRoadAssistance.setDate_of_transaction(""+result.getObject(4));
				carRoadAssistance.setInsurance_company(""+result.getObject(5));
				carRoadAssistance.setPhone_number(""+result.getObject(6));
				carRoadAssistance.setEmail(""+result.getObject(7));
				carRoadAssistance.setCoverage(""+result.getObject(8));
				arr.add(carRoadAssistance);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return arr;
	}
	
	public ArrayList getCarRoadAssistanceByRankId(String rankId)
	{
		ArrayList arr = new ArrayList();
		Car_Road_Assistance carRoadAssistance;
		String empty = "";
		String query = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
		
			query = "select rank_id, "
					+ "road_assistance_service, "
					+ "price, "
					+ "date_of_transaction,"
					+ " insurance_company,"
					+ "phone_number,"
					+ "email,"
					+ " coverage from telcodb.dbo.car_road_assistance where rank_id = '"+rankId+"' ";
			
			System.out.println("getMenuItems " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				carRoadAssistance = new Car_Road_Assistance();
				carRoadAssistance.setRank_id(""+result.getObject(1));
				carRoadAssistance.setRoad_assistance_id(""+result.getObject(2));
				carRoadAssistance.setPrice(""+result.getObject(3));
				carRoadAssistance.setDate_of_transaction(""+result.getObject(4));
				carRoadAssistance.setInsurance_company(""+result.getObject(5));
				carRoadAssistance.setPhone_number(""+result.getObject(6));
				carRoadAssistance.setEmail(""+result.getObject(7));
				carRoadAssistance.setCoverage(""+result.getObject(8));
				arr.add(carRoadAssistance);
			}
		}
		catch(SQLException sq)
		{
			System.out.println("error " + sq.getMessage());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return arr;
	}
	
	public String deleteCarRoadAssistance(String rankId)
	{
		int output = -1;
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			con = connectToSupportLog();
			stat = con.createStatement();
			
			String query = "delete from telcodb.dbo.car_road_assistance where rank_id = '"+rankId+"' ";

			output = stat.executeUpdate(query);
			
			if(output > 0){
				con.commit();
				message = "Records successfully deleted";
			}
			else{
				con.rollback();
				message = "Records not deleted";
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			try
			{
				con.rollback();
				message = "Error occured while deleting menuitem";
			}
			catch(Exception e){}
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return message;
	}
	
	public String createCarBodyRepairService(String vehicle_make, String vehicle_model, String vehicle_year,String vehicle_color, String repair_description, String vehicle_damage,String insurance_company_paying_for_repair)
	{
		int output = -1;
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			con = connectToSupportLog();
			stat = con.createStatement();
	
			String query = "insert into telcodb.dbo.car_body_repair_services(vehicle_make, vehicle_model,vehicle_year,vehicle_color,repair_description,vehicle_damage,insurance_company_paying_for_repair,date_of_transaction)" +
					" values('"+vehicle_make+"','"+vehicle_model+"','"+vehicle_year+"','"+vehicle_color+"','"+repair_description+"','"+vehicle_damage+"','"+insurance_company_paying_for_repair+"',getDate())";
			System.out.println("query " + query);
			output = stat.executeUpdate(query);
			
			if(output > 0)
			{
				con.commit();
				message = "Records successfully inserted";
			}
			else
			{
				con.rollback();
				message = "Records not inserted";
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			try
			{
				con.rollback();
				message = "Error occured while creating bill of sale";
			}
			catch(Exception e){}
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return message;
	}
	public String updateCarBodyRepairService(String repair_id,String vehicle_make, String vehicle_model, String vehicle_year,String vehicle_color, String repair_description, String vehicle_damage,String insurance_company_paying_for_repair)
	{
		int output = -1;
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			con = connectToSupportLog();
			stat = con.createStatement();
			
			output = stat.executeUpdate("update telcodb.dbo.car_body_repair_services set vehicle_make= '"+vehicle_make+"', vehicle_model = '"+vehicle_model+"', vehicle_year ='"+vehicle_year+"'"
					+ ", vehicle_color = '"+vehicle_color+"', repair_description = '"+repair_description+"', vehicle_damage = '"+vehicle_damage+"', insurance_company_paying_for_repair='"+insurance_company_paying_for_repair+"' where repair_id = '"+repair_id+"'");
			System.out.println("query " + query);
			//output = stat.executeUpdate(query);
			
			if(output > 0)
			{
				con.commit();
				message = "Records successfully updated";
			}
			else
			{
				con.rollback();
				message = "Records not updated";
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			try
			{
				con.rollback();
				message = "Error occured while updating bill of sale";
			}
			catch(Exception e){}
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return message;
	}
	
	public ArrayList getCarBodyRepairServiceList()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Car_body_repair_service carBodyRepairService = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;

		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			query = "select repair_id, "
					+ "vehicle_make, "
					+ "vehicle_model, "
					+ "vehicle_year,"
					+ "vehicle_color,"
					+ "repair_description,"
					+ "vehicle_damage,"
					+ "created_date,"
					+ " insurance_company_paying_for_repair from telcodb.dbo.car_body_repair_services";
			System.out.println("getMenuItems " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				carBodyRepairService = new Car_body_repair_service();
				carBodyRepairService.setCounter(""+counter);
				carBodyRepairService.setRepair_id(""+result.getObject(1));
				carBodyRepairService.setVehicle_make(""+result.getObject(2));
				carBodyRepairService.setVehicle_model(""+result.getObject(3));
				carBodyRepairService.setVehicle_year(""+result.getObject(4));
				carBodyRepairService.setVehicle_color(""+result.getObject(5));
				carBodyRepairService.setRepair_description(""+result.getObject(6));
				carBodyRepairService.setVehicle_demage(""+result.getObject(7));
				carBodyRepairService.setCreated_date(""+result.getObject(8));
				carBodyRepairService.setInsurance_company_paying_for_repair(""+result.getObject(9));
				arr.add(carBodyRepairService);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return arr;
	}
	
	public ArrayList getCarBodyRepairServiceByRepairId(String repairId)
	{
		ArrayList arr = new ArrayList();
		Car_body_repair_service carBodyRepairService;
		String empty = "";
		String query = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			query = "select repair_id, "
					+ "vehicle_make, "
					+ "vehicle_model, "
					+ "vehicle_year,"
					+ "vehicle_color,"
					+ "repair_description,"
					+ "vehicle_damage,"
					+ "created_date,"
					+ " insurance_company_paying_for_repair from telcodb.dbo.car_body_repair_services where repair_id = '"+repairId+"'";
			
			
			System.out.println("getMenuItems " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				carBodyRepairService = new Car_body_repair_service();
				carBodyRepairService.setCounter(""+counter);
				carBodyRepairService.setRepair_id(""+result.getObject(1));
				carBodyRepairService.setVehicle_make(""+result.getObject(2));
				carBodyRepairService.setVehicle_model(""+result.getObject(3));
				carBodyRepairService.setVehicle_year(""+result.getObject(4));
				carBodyRepairService.setVehicle_color(""+result.getObject(5));
				carBodyRepairService.setRepair_description(""+result.getObject(6));
				carBodyRepairService.setVehicle_demage(""+result.getObject(7));
				carBodyRepairService.setCreated_date(""+result.getObject(8));
				carBodyRepairService.setInsurance_company_paying_for_repair(""+result.getObject(9));
				arr.add(carBodyRepairService);
			}
		}
		catch(SQLException sq)
		{
			System.out.println("error " + sq.getMessage());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return arr;
	}
	
	public String deleteCarBodyRepairService(String repair_id)
	{
		int output = -1;
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			con = connectToSupportLog();
			stat = con.createStatement();
			
			String query = "delete from telcodb.dbo.car_body_repair_services where repair_id = '"+repair_id+"' ";

			output = stat.executeUpdate(query);
			
			if(output > 0){
				con.commit();
				message = "Records successfully deleted";
			}
			else{
				con.rollback();
				message = "Records not deleted";
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			try
			{
				con.rollback();
				message = "Error occured while deleting menuitem";
			}
			catch(Exception e){}
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return message;
	}
	/*Method to get air time sales*/
	public ArrayList airTimeSales(String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION etran = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		//" merchant_code in ('0560019910','0690019910','0690069901','0112430001')";
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select merchant_code, count(*), sum(trans_amount) from e_transaction" +
					" where char_length(card_num) > 10  and" +
					" trans_code in ('P') and char_length(merchant_code) = 10 and" +
					" trans_date between '"+start_dt+"' and '"+end_dt+"' and" +
					" merchant_code in ('0112430001','0333460001','0570010442','0560029913'," +
					" '0140010010','7930010001','7920150004','7850010002','0560019910','0690019910','0690069901') group by merchant_code";
		
			System.out.println("airTimeSales query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				etran = new E_TRANSACTION();
				etran.setMerchat_code(""+result.getObject(1));
				etran.setCard_count(""+result.getObject(2));
				etran.setTotal_amount(""+result.getObject(3));
				

				arr.add(etran);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Method to get air time sales breakdown*/
	public ArrayList airTimeSalesBreakDown(String merchant_code, String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION etran = null;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			

			query = "select unique_transid, card_num, merchant_code, trans_descr, trans_amount," +
					" trans_date, channelid  from e_transaction" +
					" where char_length(card_num) > 10  and" +
					" trans_code in ('P') and char_length(merchant_code) = 10 and" +
					" trans_date between '"+start_dt+"' and '"+end_dt+"' and" +
					" merchant_code = '"+merchant_code+"' order by trans_date desc ";
			
			System.out.println("airTimeSalesBreakDown query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				etran = new E_TRANSACTION();
				etran.setUnique_transid(""+result.getObject(1));
				etran.setCard_num(""+result.getObject(2));
				etran.setMerchat_code(""+result.getObject(3));
				etran.setTrans_desc(""+result.getObject(4));
				etran.setTrans_amount(""+result.getObject(5));
				etran.setTrans_date(""+result.getObject(6));
				etran.setChannelid(""+result.getObject(7));

				arr.add(etran);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	public ArrayList DialyTransactionReportLog(String card_num, String start_dt, String end_dt, String status)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION etran = null;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
		    if(status.equals("SUCCESSFUL"))
		    {
		    	query = "select unique_transid, card_num, merchant_code, trans_descr, trans_amount," +
						" trans_date, channelid  from e_transaction" +
						" where card_num like '"+card_num+"%' and" +
						" trans_date between '"+start_dt+"' and '"+end_dt+"' and" +
						" order by trans_date desc ";   	
		    }
		    if(status.equals("FAIL"))
		    {
		    	
		    			
		    	
		    }
		    

			
			
			System.out.println("airTimeSalesBreakDown query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				etran = new E_TRANSACTION();
				etran.setUnique_transid(""+result.getObject(1));
				etran.setCard_num(""+result.getObject(2));
				etran.setMerchat_code(""+result.getObject(3));
				etran.setTrans_desc(""+result.getObject(4));
				etran.setTrans_amount(""+result.getObject(5));
				etran.setTrans_date(""+result.getObject(6));
				etran.setChannelid(""+result.getObject(7));

				arr.add(etran);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	/**
	 * This method is used to generate summary report
	 * */
	public ArrayList getLogSuccessfulSummary(String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Summary summary = new Summary();
		String str = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			//mobile channel
			
			query = "select count(*), sum(trans_amount) from ecarddb..e_transaction where trans_date between('"+ start_dt +"') and ('"+ end_dt +"') and channelid ='02'";
			//query = "select count(*) from ecarddb.dbo.E_SMSLOG where message_date between('"+ start_dt +"') and ('"+ end_dt +"')";
			result = stat.executeQuery(query);
			while(result.next())
			{
				summary = new Summary();
				summary.setSummary_count(""+result.getObject(1));
				summary.setSummaryamount(""+result.getObject(2));
				summary.setSummary_name("Mobile");
				//arr.add(summary);
			}
			
			
			query = "select count(*) from ecarddb.dbo.E_SMSLOG where message_date between('"+ start_dt +"') and ('"+ end_dt +"') and response_code = 0";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			//Summary s = (Summary)arr.get(0);
			summary.setSummary_success_status(str);
			
			
			query = "select count(*) from ecarddb.dbo.E_SMSLOG where message_date between('"+ start_dt +"') and ('"+ end_dt +"') and response_code <> 0";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			
			//summary = (Summary)arr.get(0);
			summary.setSummary_failed_status(str);
			arr.add(summary);
			
			
			//web
			//query = "select count(*) from ecarddb.dbo.e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"') and substring(transid,1,2) = '01'";//01 is for web
			query = "select count(*), sum(trans_amount) from ecarddb..e_transaction where trans_date between('"+ start_dt +"') and ('"+ end_dt +"') and channelid ='01'";
			result = stat.executeQuery(query);
			while(result.next())
			{
				summary = new Summary();
				summary.setSummary_count(""+result.getObject(1));
				summary.setSummaryamount(""+result.getObject(2));
				summary.setSummary_name("WEB");
				//arr.add(summary);
			}
			
			query = "select count(*) from ecarddb.dbo.e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"') and substring(transid,1,2) = '01' and response_code = '0'";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			//summary = (Summary)arr.get(1);
			summary.setSummary_success_status(str);
			
			
			query = "select count(*) from ecarddb.dbo.e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"') and substring(transid,1,2) = '01' and response_code <> '0'";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			//s = (Summary)arr.get(1);
			summary.setSummary_failed_status(str);
			arr.add(summary);
			
			
			closeConnectionECard(con, result, result);
			
			
			con = connectToETMC();
			stat = con.createStatement();
			
			//pos
			//query = "select count(*),sum(amount) from ecarddb.dbo.e_tmcrequest where merchant_type = '6012' and trans_date between('"+start_dt+"') and ('"+end_dt+"')";
			query = "select count(*), sum(trans_amount) from ecarddb..e_transaction where trans_date between('"+ start_dt +"') and ('"+ end_dt +"') and channelid ='03'";
			result = stat.executeQuery(query);
			while(result.next())
			{
				summary = new Summary();
				summary.setSummary_count(""+result.getObject(1));
				summary.setSummaryamount(""+result.getObject(2));
				summary.setSummary_name("POS");
				
				//arr.add(summary);
			}
			
			query = "select count(*) from ecarddb.dbo.e_tmcrequest where  merchant_type = '6012' and trans_date between('"+start_dt+"') and ('"+end_dt+"') and response_code in('00')";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			//s = (Summary)arr.get(2);
			summary.setSummary_success_status(str);
			
			query = "select count(*) from ecarddb.dbo.e_tmcrequest where merchant_type = '6012' and trans_date between('"+start_dt+"') and ('"+end_dt+"') and (response_code not in ('00') or response_code = null)";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			//s = (Summary)arr.get(2);
			summary.setSummary_failed_status(str);
			arr.add(summary);
			
			//atm
			//query = "select count(*),sum(amount) from ecarddb.dbo.e_tmcrequest where merchant_type = '6011' and trans_date between('"+start_dt+"') and ('"+end_dt+"')";
			query = "select count(*), sum(trans_amount) from ecarddb..e_transaction where trans_date between('"+ start_dt +"') and ('"+ end_dt +"') and channelid ='04'";
			result = stat.executeQuery(query);
			while(result.next())
			{
				summary = new Summary();
				summary.setSummary_count(""+result.getObject(1));
				summary.setSummaryamount(""+result.getObject(2));
				summary.setSummary_name("ATM");
				//arr.add(summary);
				
			}
			System.out.print("query 4 Atm  "+query);
			
			query = "select count(*) from ecarddb.dbo.e_tmcrequest where merchant_type = '6011' and trans_date between('"+start_dt+"') and ('"+end_dt+"') and response_code in('00')";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			//s = (Summary)arr.get(3);
			summary.setSummary_success_status(str);
			
			query = "select count(*) from ecarddb.dbo.e_tmcrequest where merchant_type = '6011' and trans_date between('"+start_dt+"') and ('"+end_dt+"') and (response_code not in ('00') or response_code = null)";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			//s = (Summary)arr.get(3);
			summary.setSummary_failed_status(str);
			arr.add(summary);
			
			closeConnectionETMC(con, result);
			
			
		/*	con = connectToECard();
			stat = con.createStatement();
			//VTU
			query = "select count(*),sum(convert(decimal(18,3),amount)) from ecarddb.dbo.vtu_log where txn_date_time between('"+start_dt+"') and ('"+end_dt+"')";
			result = stat.executeQuery(query);
			while(result.next())
			{
				summary = new Summary();
				summary.setSummary_count(""+result.getObject(1));
				summary.setSummary_name("VTU");
				summary.setSummaryamount(""+result.getObject(2));
			}
			
			query = "select count(*) from ecarddb.dbo.vtu_log where txn_date_time between('"+start_dt+"') and ('"+end_dt+"') and status_id in('0')";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			summary.setSummary_success_status(str);
			
			query = "select count(*) from ecarddb.dbo.vtu_log where txn_date_time between('"+start_dt+"') and ('"+end_dt+"') and (status_id not in('0') or status_id = null)";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			summary.setSummary_failed_status(str);
			arr.add(summary);
			
			closeConnectionECard(con, result, result);
			
			
			
			con = connectMobileDB();
			stat = con.createStatement();
			
			//Version II
			query = "select count(*) from m_outgoing_messages where created between('"+start_dt+"') and ('"+end_dt+"') ";
			result = stat.executeQuery(query);
			while(result.next())
			{
				summary = new Summary();
				summary.setSummary_count(""+result.getObject(1));
				summary.setSummary_name("MOBILE - VERSION II");
			}
			
			query = "select count(*) from m_outgoing_messages where response_code in (0,1000,1001,1002,1003,1004,1005) and created between('"+start_dt+"') and ('"+end_dt+"') ";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			summary.setSummary_success_status(str);
			
			query = "select count(*) from m_outgoing_messages where response_code not in (0,1000,1001,1002,1003,1004,1005) and created between('"+start_dt+"') and ('"+end_dt+"') ";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			summary.setSummary_failed_status(str);
			arr.add(summary);
			
			closeConnectionMobileDB(con, result);
			
			/*con = connectToTelco();
			stat = con.createStatement();
			//Bill Payment
			query = "select count(*) from ecarddb.dbo.vtu_log where txn_date_time between('"+start_dt+"') and ('"+end_dt+"')";
			query = "select count(*) from telcodb..t_paytrans where trans_date between('"+start_dt+"') and ('"+end_dt+"') and merchant_code in ('0690010012','2140010003','0326000002','0320010088','0582280012','0582280016')";
			result = stat.executeQuery(query);
			while(result.next())
			{
				summary = new Summary();
				summary.setSummary_count(""+result.getObject(1));
				summary.setSummary_name("Bill Payment");
			}
			
			query = "select count(*) from telcodb..t_paytrans where trans_date between('"+start_dt+"') and ('"+end_dt+"') and merchant_code in ('0690010012','2140010003','0326000002','0320010088','0582280012','0582280016')";
			
			query = "select count(*) from ecarddb.dbo.vtu_log where txn_date_time between('"+start_dt+"') and ('"+end_dt+"') and status_id in('0')";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			summary.setSummary_success_status(str);
			
			query = "select count(*) from ecarddb.dbo.vtu_log where txn_date_time between('"+start_dt+"') and ('"+end_dt+"') and (status_id not in('0') or status_id = null)";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			summary.setSummary_failed_status(str);
			arr.add(summary);
			
			closeConnectionTelco(con, result);*/
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
			closeConnectionETMC(con, result);
			closeConnectionMobileDB(con, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
			closeConnectionETMC(con, result);
			closeConnectionMobileDB(con, result);
		}
		return arr;
	}
	
	
	/*Method to do the select for the support log*/
	public ArrayList getSupportLog(String channel,String card_num, String start_dt , String end_dt,
			String row_count, String status, String merchant_code, String bank_code, String terminal_id)
	{
		ArrayList arr = new ArrayList();
		T_SMS_RECEIVE sms_receive;
		REQUEST_LOG request_log;
		E_TMCREQUEST tmc_request;
		HashNumber hn = new HashNumber();
		String query = "";
		String start_pan = "";
		String end_pan = "";
		String pan = "";
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			int ch = Integer.parseInt(channel);
			switch(ch)
			{
				case 1://web, the substring 01 means the transaction is for web
					
					
					con = connectToECard();
					stat = con.createStatement();
					
					if(bank_code.equalsIgnoreCase("000") || bank_code.equalsIgnoreCase("001"))
					{
						if(status.equals("ALL"))
						{
							query = "select TRANSID,CARD_NUM,TRANS_DATE,TRANS_AMOUNT,TRANS_CODE," +
							"(select merchant_name from ecarddb.dbo.e_merchant where MERCHANT_CODE = ecarddb.dbo.e_requestlog.MERCHANT_CODE)," +
							"(select error_desc from ecarddb..e_autoswitch_error where error_code = ecarddb.dbo.e_requestlog.response_code),RESPONSE_TIME,TRANS_DESCR,client_id," +
							"REQUEST_ID,FEE,CURRENCY,datediff(ss,response_time,trans_date)" +
							" from ecarddb.dbo.e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"') and substring(transid,1,2) = '01' and card_num like '"+card_num +"%'  and merchant_code like '"+merchant_code+"%'  order by trans_date desc ";
						}
						else if(status.equals("SUCCESSFUL"))
						{
							query = "select TRANSID,CARD_NUM,TRANS_DATE,TRANS_AMOUNT,TRANS_CODE," +
							"(select merchant_name from ecarddb.dbo.e_merchant where MERCHANT_CODE = ecarddb.dbo.e_requestlog.MERCHANT_CODE)," +
							"(select error_desc from ecarddb..e_autoswitch_error where error_code = ecarddb.dbo.e_requestlog.response_code),RESPONSE_TIME,TRANS_DESCR,client_id," +
							"REQUEST_ID,FEE,CURRENCY,datediff(ss,response_time,trans_date)" +
							" from ecarddb.dbo.e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"') and substring(transid,1,2) = '01' and card_num like '"+card_num +"%'  and merchant_code like '"+merchant_code+"%' and response_code in('0') order by trans_date desc ";
						}
						else if(status.equals("FAILED"))
						{
							query = "select TRANSID,CARD_NUM,TRANS_DATE,TRANS_AMOUNT,TRANS_CODE," +
							"(select merchant_name from ecarddb.dbo.e_merchant where MERCHANT_CODE = ecarddb.dbo.e_requestlog.MERCHANT_CODE)," +
							"(select error_desc from ecarddb..e_autoswitch_error where error_code = ecarddb.dbo.e_requestlog.response_code),RESPONSE_TIME,TRANS_DESCR,client_id," +
							"REQUEST_ID,FEE,CURRENCY,datediff(ss,response_time,trans_date)" +
							" from ecarddb.dbo.e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"') and substring(transid,1,2) = '01' and card_num like '"+card_num +"%'  and merchant_code like '"+merchant_code+"%' and response_code not in('0') order by trans_date desc ";
						}
					}//bank codes
					else
					{
						if(status.equals("ALL"))
						{
							query = "select TRANSID,CARD_NUM,TRANS_DATE,TRANS_AMOUNT,TRANS_CODE," +
							"(select merchant_name from ecarddb.dbo.e_merchant where MERCHANT_CODE = ecarddb.dbo.e_requestlog.MERCHANT_CODE)," +
							"(select error_desc from ecarddb..e_autoswitch_error where error_code = ecarddb.dbo.e_requestlog.response_code),RESPONSE_TIME,TRANS_DESCR,client_id," +
							"REQUEST_ID,FEE,CURRENCY,datediff(ss,response_time,trans_date)" +
							" from ecarddb.dbo.e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"') and substring(transid,1,2) = '01' and card_num like '"+card_num +"%'  and merchant_code like '"+merchant_code+"%' and (substring(card_num,1,3) = '"+bank_code+"' or substring(merchant_code,1,3) = '"+bank_code+"') order by trans_date desc ";
						}
						else if(status.equals("SUCCESSFUL"))
						{
							query = "select TRANSID,CARD_NUM,TRANS_DATE,TRANS_AMOUNT,TRANS_CODE," +
							"(select merchant_name from ecarddb.dbo.e_merchant where MERCHANT_CODE = ecarddb.dbo.e_requestlog.MERCHANT_CODE)," +
							"(select error_desc from ecarddb..e_autoswitch_error where error_code = ecarddb.dbo.e_requestlog.response_code),RESPONSE_TIME,TRANS_DESCR,client_id," +
							"REQUEST_ID,FEE,CURRENCY,datediff(ss,response_time,trans_date)" +
							" from ecarddb.dbo.e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"') and substring(transid,1,2) = '01' and card_num like '"+card_num +"%'  and merchant_code like '"+merchant_code+"%' and response_code in('0') and (substring(card_num,1,3) = '"+bank_code+"' or substring(merchant_code,1,3) = '"+bank_code+"') order by trans_date desc";
						}
						else if(status.equals("FAILED"))
						{
							query = "select TRANSID,CARD_NUM,TRANS_DATE,TRANS_AMOUNT,TRANS_CODE," +
							"(select merchant_name from ecarddb.dbo.e_merchant where MERCHANT_CODE = ecarddb.dbo.e_requestlog.MERCHANT_CODE)," +
							"(select error_desc from ecarddb..e_autoswitch_error where error_code = ecarddb.dbo.e_requestlog.response_code),RESPONSE_TIME,TRANS_DESCR,client_id," +
							"REQUEST_ID,FEE,CURRENCY,datediff(ss,response_time,trans_date)" +
							" from ecarddb.dbo.e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"') and substring(transid,1,2) = '01' and card_num like '"+card_num +"%'  and merchant_code like '"+merchant_code+"%' and response_code not in('0') and (substring(card_num,1,3) = '"+bank_code+"' or substring(merchant_code,1,3) = '"+bank_code+"') order by trans_date desc ";
						}
					}
					System.out.println("query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						counter++;
						
						request_log = new REQUEST_LOG();
						
						request_log.setCounter(""+counter);
						request_log.setTransid(""+result.getObject(1));
						request_log.setCard_num(""+result.getObject(2));
						request_log.setTrans_date(""+result.getObject(3));
						request_log.setTrans_amount(""+result.getObject(4));
						request_log.setTrans_code(""+result.getObject(5));
						
						String s = ""+result.getObject(6);
						if(s.equals("")||s.equals("null"))
							s = "";
						request_log.setMerchant_desc(s);
						
						request_log.setResponse_code(""+result.getObject(7));
						request_log.setResponse_time(""+result.getObject(8));
						request_log.setTrans_descr(""+result.getObject(9));
						request_log.setClient_id(""+result.getObject(10));
						request_log.setRequest_id(""+result.getObject(11));
						request_log.setFee(""+result.getObject(12));
						request_log.setCurrency(""+result.getObject(13));
						
						s = ""+result.getObject(14);
						if(s.equals("")||s.equals("null"))
							s = "";
						
						request_log.setResponse_time_in_secs(s);
						
						arr.add(request_log);
					}
					
					closeConnectionECard(con, result,result);
				
					break;
					
				case 2://mobile
					
					con = connectToTelco();
					stat = con.createStatement();
					
					
					String str_mobile = card_num.trim();
					/*if(str_mobile.trim().length()>0)
					{
						if(str_mobile.startsWith("234"));
						else
						{
							str_mobile = "234" + str_mobile.substring(1, str_mobile.length());
						}
					}*/
				
					
					String key = ""; 
					String sms_originator = "";
					query = "select * from telcodb..T_SMS_RECEIVE where trans_date between('"+start_dt+"') and ('"+end_dt+"') and sms_originator like '"+ str_mobile +"%' order by trans_date desc ";
					System.out.println("query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						counter++;
						key = "";
						sms_receive = new T_SMS_RECEIVE();
						
						sms_receive.setCounter(""+counter);
						sms_receive.setTrans_dt(""+result.getObject(1));
						sms_receive.setSms_dest(""+result.getObject(2));
						sms_receive.setSms_message(""+result.getObject(3));
						sms_originator = ""+result.getObject(4);
						
						if(bank_code.equals("000") || bank_code.equalsIgnoreCase("001"))
						{
							sms_receive.setSms_originator(sms_originator);
						}
						else
						{
							if(card_num.trim().length()<=0 || card_num.trim().equals("234"))
								sms_receive.setSms_originator(hn.hashStringValue2(sms_originator, 4));
							else
								sms_receive.setSms_originator(sms_originator);
						}
						
						sms_receive.setSms_status(""+result.getObject(5));
						key = ""+result.getObject(6);
						
						sms_receive.setSms_key(key.trim());
						sms_receive.setSms_id(""+result.getObject(7));
						sms_receive.setSms_duplicate(""+result.getObject(8));
						
						if(sms_receive.getSms_message().startsWith("?V ") || sms_receive.getSms_message().startsWith("?=V ") || sms_receive.getSms_message().startsWith("?#V ") || sms_receive.getSms_message().startsWith("V ") || sms_receive.getSms_message().startsWith("?==V "))
						{
							sms_receive.setTransType("V");
						}
						
						arr.add(sms_receive);
					}
					
					if(status.equals("ONLY SMS LOG") && arr.size()>0)
					{
						ArrayList arr_2 = new ArrayList();
						for(int i=0;i<arr.size();i++)
						{
							sms_receive = (T_SMS_RECEIVE)arr.get(i);
							arr_2.add(getE_SMS_LOG_2("02"+sms_receive.getSms_key()));
						}
						arr.clear();
						arr.trimToSize();
						arr = arr_2;
					}
					
					
					closeConnectionTelco(con, result);
					break;
				
				case 3://pos
					
					con = connectToETMC();
					stat = con.createStatement();
					
					if(status.equals("ALL"))
					{
						query = "select mti,pan,(select pro_code_desc from ecarddb.dbo.e_tmccode where ecarddb.dbo.e_tmccode.pro_code =  substring(ecarddb.dbo.e_tmcrequest.pro_code,1,2)), card_acc_name, terminal_id, (select aquirer_name from ecarddb.dbo.e_tmcaquirer where ecarddb.dbo.e_tmcaquirer.aqid =  ecarddb.dbo.e_tmcrequest.aqid), trans_date, datediff(ss,trans_date,response_time), amount,(select ecarddb.dbo.e_isoerror.error_descr from ecarddb.dbo.e_isoerror where error_code = ecarddb.dbo.e_tmcrequest.response_code),trans_seq,target from ecarddb.dbo.e_tmcrequest where trans_date between('"+start_dt+"') and ('"+end_dt+"') and merchant_type='6012' and pan like '"+card_num+"%' and terminal_id like '"+ terminal_id +"%' order by trans_date desc ";
					}
					else if(status.equals("SUCCESSFUL"))
					{
						query = "select mti,pan,(select pro_code_desc from ecarddb.dbo.e_tmccode where ecarddb.dbo.e_tmccode.pro_code =  substring(ecarddb.dbo.e_tmcrequest.pro_code,1,2)), card_acc_name, terminal_id, (select aquirer_name from ecarddb.dbo.e_tmcaquirer where ecarddb.dbo.e_tmcaquirer.aqid =  ecarddb.dbo.e_tmcrequest.aqid), trans_date, datediff(ss,trans_date,response_time), amount,(select ecarddb.dbo.e_isoerror.error_descr from ecarddb.dbo.e_isoerror where error_code = ecarddb.dbo.e_tmcrequest.response_code),trans_seq,target from ecarddb.dbo.e_tmcrequest where trans_date between('"+start_dt+"') and ('"+end_dt+"') and response_code in('00') and merchant_type='6012' and pan like '"+card_num+"%' and terminal_id like '"+ terminal_id +"%' order by trans_date desc ";
					}
					else if(status.equals("FAILED"))
					{
						query = "select mti,pan,(select pro_code_desc from ecarddb.dbo.e_tmccode where ecarddb.dbo.e_tmccode.pro_code =  substring(ecarddb.dbo.e_tmcrequest.pro_code,1,2)), card_acc_name, terminal_id, (select aquirer_name from ecarddb.dbo.e_tmcaquirer where ecarddb.dbo.e_tmcaquirer.aqid =  ecarddb.dbo.e_tmcrequest.aqid), trans_date, datediff(ss,trans_date,response_time), amount,(select ecarddb.dbo.e_isoerror.error_descr from ecarddb.dbo.e_isoerror where error_code = ecarddb.dbo.e_tmcrequest.response_code),trans_seq,target from ecarddb.dbo.e_tmcrequest where trans_date between('"+start_dt+"') and ('"+end_dt+"') and (response_code not in ('00') or response_code = null) and merchant_type='6012' and pan like '"+card_num+"%' and terminal_id like '"+ terminal_id +"%' order by trans_date desc ";
					}
					
					result = stat.executeQuery(query);
					while(result.next())
					{
						counter++;
						tmc_request = new E_TMCREQUEST();
						
						tmc_request.setCounter(""+counter);
						tmc_request.setMti(""+result.getObject(1));
						
						try
						{
							pan = ""+result.getObject(2);
							start_pan = pan.substring(0,9);
							end_pan = pan.substring(pan.length()-4);
						}
						catch(Exception e){e.printStackTrace();}
						
						tmc_request.setPan(start_pan + "#########" + end_pan);
						tmc_request.setPro_code(""+result.getObject(3));
						tmc_request.setCard_acc_name(""+result.getObject(4));
						tmc_request.setTerminal_id(""+result.getObject(5));
						
						String s = ""+result.getObject(6);
						if(s.equals("")||s.equals("null"))
							s = "";
						tmc_request.setAqid(s);
						
						tmc_request.setTrans_date(""+result.getObject(7));
						
						s = ""+result.getObject(8);
						if(s.equals("")||s.equals("null"))
							s = "";
							
						tmc_request.setResponse_time_in_secs(s);
						tmc_request.setAmount(""+result.getObject(9));
						
						s = ""+result.getObject(10);
						if(s.equals("")||s.equals("null"))
							s = "";
						
						tmc_request.setResponse_code(s);
						tmc_request.setTrans_seq(""+result.getObject(11));
						tmc_request.setTarget(""+result.getObject(12));
						
						arr.add(tmc_request);
					}
					
					closeConnectionETMC(con, result);
					
					
					break;
					
				case 4://atm
					
					con = connectToETMC();
					stat = con.createStatement();
					
					if(status.equals("ALL"))
					{
						query = "select mti,pan,(select distinct pro_code_desc from ecarddb.dbo.e_tmccode where ecarddb.dbo.e_tmccode.pro_code =  substring(ecarddb.dbo.e_tmcrequest.pro_code,1,2)), card_acc_name, terminal_id, (select distinct aquirer_name from ecarddb.dbo.e_tmcaquirer where ecarddb.dbo.e_tmcaquirer.aqid =  ecarddb.dbo.e_tmcrequest.aqid), trans_date, datediff(ss,trans_date,response_time),amount, (select distinct ecarddb.dbo.e_isoerror.error_descr from ecarddb.dbo.e_isoerror where error_code = ecarddb.dbo.e_tmcrequest.response_code),trans_seq,target from ecarddb.dbo.e_tmcrequest where trans_date between('"+start_dt+"') and ('"+end_dt+"') and merchant_type='6011'  and pan like '"+card_num+"%' and terminal_id like '"+ terminal_id +"%' order by trans_date desc";
						
					}
					else if(status.equals("SUCCESSFUL"))
					{
						query = "select mti,pan,(select pro_code_desc from ecarddb.dbo.e_tmccode where ecarddb.dbo.e_tmccode.pro_code =  substring(ecarddb.dbo.e_tmcrequest.pro_code,1,2)), card_acc_name, terminal_id, (select aquirer_name from ecarddb.dbo.e_tmcaquirer where ecarddb.dbo.e_tmcaquirer.aqid =  ecarddb.dbo.e_tmcrequest.aqid), trans_date, datediff(ss,trans_date,response_time),amount, (select ecarddb.dbo.e_isoerror.error_descr from ecarddb.dbo.e_isoerror where error_code = ecarddb.dbo.e_tmcrequest.response_code),trans_seq,target from ecarddb.dbo.e_tmcrequest where trans_date between('"+start_dt+"') and ('"+end_dt+"') and response_code in('00') and merchant_type='6011' and pan like '"+card_num+"%' and terminal_id like '"+ terminal_id +"%' order by trans_date desc";
					}
					else if(status.equals("FAILED"))
					{
						query = "select mti,pan,(select pro_code_desc from ecarddb.dbo.e_tmccode where ecarddb.dbo.e_tmccode.pro_code =  substring(ecarddb.dbo.e_tmcrequest.pro_code,1,2)), card_acc_name, terminal_id, (select aquirer_name from ecarddb.dbo.e_tmcaquirer where ecarddb.dbo.e_tmcaquirer.aqid =  ecarddb.dbo.e_tmcrequest.aqid), trans_date, datediff(ss,trans_date,response_time),amount, (select ecarddb.dbo.e_isoerror.error_descr from ecarddb.dbo.e_isoerror where error_code = ecarddb.dbo.e_tmcrequest.response_code),trans_seq,target from ecarddb.dbo.e_tmcrequest where trans_date between('"+start_dt+"') and ('"+end_dt+"') and (response_code not in ('00') or response_code = null) and merchant_type='6011' and pan like '"+card_num+"%' and terminal_id like '"+ terminal_id +"%' order by trans_date desc";
					}
					
					result = stat.executeQuery(query);
					while(result.next())
					{
						counter++;
						tmc_request = new E_TMCREQUEST();
						
						tmc_request.setCounter(""+counter);
						tmc_request.setMti(""+result.getObject(1));
						
						try
						{
							pan = ""+result.getObject(2);
							start_pan = pan.substring(0,9);
							end_pan = pan.substring(pan.length()-4);
						}
						catch(Exception e){}
						
						tmc_request.setPan(start_pan + "#########" + end_pan);
						tmc_request.setPro_code(""+result.getObject(3));
						tmc_request.setCard_acc_name(""+result.getObject(4));
						tmc_request.setTerminal_id(""+result.getObject(5));

						String s = ""+result.getObject(6);
						if(s.equals("")||s.equals("null"))
							s = "";
						tmc_request.setAqid(s);
						
						tmc_request.setTrans_date(""+result.getObject(7));
						
						s = ""+result.getObject(8);
						if(s.equals("")||s.equals("null")||s == null)
							s = "";
						
						tmc_request.setResponse_time_in_secs(s);
						tmc_request.setAmount(""+result.getObject(9));
						
						s = ""+result.getObject(10);
						if(s.equals("")||s.equals("null"))
							s = "";
						
						tmc_request.setResponse_code(s);
						tmc_request.setTrans_seq(""+result.getObject(11));
						tmc_request.setTarget(""+result.getObject(12));
						
						arr.add(tmc_request);
					}
					
					closeConnectionETMC(con , result);
				
					break;
					
					
				case 5://atm and pos and any other
					
					con = connectToETMC();
					stat = con.createStatement();
					
					if(status.equals("ALL"))
					{
						query = "select mti,pan,(select distinct pro_code_desc from ecarddb.dbo.e_tmccode where ecarddb.dbo.e_tmccode.pro_code =  substring(ecarddb.dbo.e_tmcrequest.pro_code,1,2)), card_acc_name, terminal_id, (select distinct aquirer_name from ecarddb.dbo.e_tmcaquirer where ecarddb.dbo.e_tmcaquirer.aqid =  ecarddb.dbo.e_tmcrequest.aqid), trans_date, datediff(ss,trans_date,response_time),amount, (select distinct ecarddb.dbo.e_isoerror.error_descr from ecarddb.dbo.e_isoerror where error_code = ecarddb.dbo.e_tmcrequest.response_code),trans_seq,target from ecarddb.dbo.e_tmcrequest where trans_date between('"+start_dt+"') and ('"+end_dt+"') and pan like '"+card_num+"%' and terminal_id like '"+ terminal_id +"%' order by trans_date desc";
						
					}
					else if(status.equals("SUCCESSFUL"))
					{
						query = "select mti,pan,(select pro_code_desc from ecarddb.dbo.e_tmccode where ecarddb.dbo.e_tmccode.pro_code =  substring(ecarddb.dbo.e_tmcrequest.pro_code,1,2)), card_acc_name, terminal_id, (select aquirer_name from ecarddb.dbo.e_tmcaquirer where ecarddb.dbo.e_tmcaquirer.aqid =  ecarddb.dbo.e_tmcrequest.aqid), trans_date, datediff(ss,trans_date,response_time),amount, (select ecarddb.dbo.e_isoerror.error_descr from ecarddb.dbo.e_isoerror where error_code = ecarddb.dbo.e_tmcrequest.response_code),trans_seq,target from ecarddb.dbo.e_tmcrequest where trans_date between('"+start_dt+"') and ('"+end_dt+"') and response_code in('00') and pan like '"+card_num+"%' and terminal_id like '"+ terminal_id +"%' order by trans_date desc";
					}
					else if(status.equals("FAILED"))
					{
						query = "select mti,pan,(select pro_code_desc from ecarddb.dbo.e_tmccode where ecarddb.dbo.e_tmccode.pro_code =  substring(ecarddb.dbo.e_tmcrequest.pro_code,1,2)), card_acc_name, terminal_id, (select aquirer_name from ecarddb.dbo.e_tmcaquirer where ecarddb.dbo.e_tmcaquirer.aqid =  ecarddb.dbo.e_tmcrequest.aqid), trans_date, datediff(ss,trans_date,response_time),amount, (select ecarddb.dbo.e_isoerror.error_descr from ecarddb.dbo.e_isoerror where error_code = ecarddb.dbo.e_tmcrequest.response_code),trans_seq,target from ecarddb.dbo.e_tmcrequest where trans_date between('"+start_dt+"') and ('"+end_dt+"') and (response_code not in ('00') or response_code = null) and pan like '"+card_num+"%' and terminal_id like '"+ terminal_id +"%' order by trans_date desc";
					}
					
					result = stat.executeQuery(query);
					while(result.next())
					{
						counter++;
						tmc_request = new E_TMCREQUEST();
						
						tmc_request.setCounter(""+counter);
						tmc_request.setMti(""+result.getObject(1));
						
						try
						{
							pan = ""+result.getObject(2);
							start_pan = pan.substring(0,9);
							end_pan = pan.substring(pan.length()-4);
						}
						catch(Exception e){}
						
						tmc_request.setPan(start_pan + "#########" + end_pan);
						tmc_request.setPro_code(""+result.getObject(3));
						tmc_request.setCard_acc_name(""+result.getObject(4));
						tmc_request.setTerminal_id(""+result.getObject(5));

						String s = ""+result.getObject(6);
						if(s.equals("")||s.equals("null"))
							s = "";
						tmc_request.setAqid(s);
						
						tmc_request.setTrans_date(""+result.getObject(7));
						
						s = ""+result.getObject(8);
						if(s.equals("")||s.equals("null")||s == null)
							s = "";
						
						tmc_request.setResponse_time_in_secs(s);
						tmc_request.setAmount(""+result.getObject(9));
						
						s = ""+result.getObject(10);
						if(s.equals("")||s.equals("null"))
							s = "";
						
						tmc_request.setResponse_code(s);
						tmc_request.setTrans_seq(""+result.getObject(11));
						tmc_request.setTarget(""+result.getObject(12));
						
						arr.add(tmc_request);
					}
					
					closeConnectionETMC(con , result);
				
					break;
					
				case 6://mobile tmc
					
					con = connectToETMC();
					stat = con.createStatement();
					
					if(status.equals("ALL"))
					{
						query = "select mti,pan,(select distinct pro_code_desc from ecarddb.dbo.e_tmccode where ecarddb.dbo.e_tmccode.pro_code =  substring(ecarddb.dbo.e_tmcrequest.pro_code,1,2)), card_acc_name, terminal_id, (select distinct aquirer_name from ecarddb.dbo.e_tmcaquirer where ecarddb.dbo.e_tmcaquirer.aqid =  ecarddb.dbo.e_tmcrequest.aqid), trans_date, datediff(ss,trans_date,response_time),amount, (select distinct ecarddb.dbo.e_isoerror.error_descr from ecarddb.dbo.e_isoerror where error_code = ecarddb.dbo.e_tmcrequest.response_code),trans_seq,target from ecarddb.dbo.e_tmcrequest where trans_date between('"+start_dt+"') and ('"+end_dt+"') and merchant_type='6013'  and pan like '"+card_num+"%' and terminal_id like '"+ terminal_id +"%' order by trans_date desc";
						
					}
					else if(status.equals("SUCCESSFUL"))
					{
						query = "select mti,pan,(select pro_code_desc from ecarddb.dbo.e_tmccode where ecarddb.dbo.e_tmccode.pro_code =  substring(ecarddb.dbo.e_tmcrequest.pro_code,1,2)), card_acc_name, terminal_id, (select aquirer_name from ecarddb.dbo.e_tmcaquirer where ecarddb.dbo.e_tmcaquirer.aqid =  ecarddb.dbo.e_tmcrequest.aqid), trans_date, datediff(ss,trans_date,response_time),amount, (select ecarddb.dbo.e_isoerror.error_descr from ecarddb.dbo.e_isoerror where error_code = ecarddb.dbo.e_tmcrequest.response_code),trans_seq,target from ecarddb.dbo.e_tmcrequest where trans_date between('"+start_dt+"') and ('"+end_dt+"') and response_code in('00') and merchant_type='6013' and pan like '"+card_num+"%' and terminal_id like '"+ terminal_id +"%' order by trans_date desc";
					}
					else if(status.equals("FAILED"))
					{
						query = "select mti,pan,(select pro_code_desc from ecarddb.dbo.e_tmccode where ecarddb.dbo.e_tmccode.pro_code =  substring(ecarddb.dbo.e_tmcrequest.pro_code,1,2)), card_acc_name, terminal_id, (select aquirer_name from ecarddb.dbo.e_tmcaquirer where ecarddb.dbo.e_tmcaquirer.aqid =  ecarddb.dbo.e_tmcrequest.aqid), trans_date, datediff(ss,trans_date,response_time),amount, (select ecarddb.dbo.e_isoerror.error_descr from ecarddb.dbo.e_isoerror where error_code = ecarddb.dbo.e_tmcrequest.response_code),trans_seq,target from ecarddb.dbo.e_tmcrequest where trans_date between('"+start_dt+"') and ('"+end_dt+"') and (response_code not in ('00') or response_code = null) and merchant_type='6013' and pan like '"+card_num+"%' and terminal_id like '"+ terminal_id +"%' order by trans_date desc";
					}
					
					result = stat.executeQuery(query);
					while(result.next())
					{
						counter++;
						tmc_request = new E_TMCREQUEST();
						
						tmc_request.setCounter(""+counter);
						tmc_request.setMti(""+result.getObject(1));
						
						try
						{
							pan = ""+result.getObject(2);
							start_pan = pan.substring(0,9);
							end_pan = pan.substring(pan.length()-4);
						}
						catch(Exception e){}
						
						tmc_request.setPan(start_pan + "#########" + end_pan);
						tmc_request.setPro_code(""+result.getObject(3));
						tmc_request.setCard_acc_name(""+result.getObject(4));
						tmc_request.setTerminal_id(""+result.getObject(5));

						String s = ""+result.getObject(6);
						if(s.equals("")||s.equals("null"))
							s = "";
						tmc_request.setAqid(s);
						
						tmc_request.setTrans_date(""+result.getObject(7));
						
						s = ""+result.getObject(8);
						if(s.equals("")||s.equals("null")||s == null)
							s = "";
						
						tmc_request.setResponse_time_in_secs(s);
						tmc_request.setAmount(""+result.getObject(9));
						
						s = ""+result.getObject(10);
						if(s.equals("")||s.equals("null"))
							s = "";
						
						tmc_request.setResponse_code(s);
						tmc_request.setTrans_seq(""+result.getObject(11));
						tmc_request.setTarget(""+result.getObject(12));
						
						arr.add(tmc_request);
					}
					
					closeConnectionETMC(con , result);
				
					break;
					
				default:
					
					break;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
			closeConnectionECard(con, result, result);
			closeConnectionETMC(con, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
			closeConnectionECard(con, result, result);
			closeConnectionETMC(con, result);
		}
		return arr;
	}
	
	/*This method does the search on successful transactions*/
	public ArrayList getSuccessfulSummary(String channel, String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Summary summary;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			if(channel.equals("WEB"))//web
			{
				con = connectToECard();
				stat = con.createStatement();
				
				
				query =  "select count(*) trans_count_splite ,trans_amount,trans_date from ecarddb..e_transaction where channelid = '01' and trans_date between('"+start_dt+"') and ('"+end_dt+"') group by channelid,trans_amount";
				//query = "select count(*)as total_count,response_code,(select error_desc from ecarddb.dbo.e_autoswitch_error where ecarddb.dbo.e_autoswitch_error.error_code = ecarddb.dbo.e_requestlog.response_code) as response_desc from ecarddb.dbo.e_requestlog where RESPONSE_CODE = '0' and substring(transid,1,2) = '01' and trans_date between('"+start_dt+"') and ('"+end_dt+"') group by RESPONSE_CODE having count(*) > 0";
				result = stat.executeQuery(query);
				while(result.next())
				{
					summary = new Summary();
					summary.setSummary_name("WEB");
					summary.setSummary_count(""+result.getObject(1));
					summary.setSummaryamount(""+result.getObject(2));
					summary.setSummarydate(""+result.getObject(3));
					
					
					//summary.setSummary_success_status(""+result.getObject(2));
					//summary.setSummary_failed_status(""+result.getObject(2));
					//summary.setSummary_pending_status(""+result.getObject(3));
					arr.add(summary);
				}
				closeConnectionTelco(con, result);	
			}
			else if(channel.equals("Mobile"))//mobile
			{
				con = connectToECard();
				stat = con.createStatement();
				query =  "select count(*) trans_count_splite ,trans_amount,trans_date from ecarddb..e_transaction where channelid = '02' and trans_date between('"+start_dt+"') and ('"+end_dt+"') group by channelid,trans_amount";
				//query = "select count(*)as total_count,response_code,(select error_desc from ecarddb.dbo.e_autoswitch_error where ecarddb.dbo.e_autoswitch_error.error_code = convert(varchar(2),ecarddb.dbo.e_smslog.response_code)) as response_desc from ecarddb.dbo.e_smslog where RESPONSE_CODE = 0 and message_date between('"+start_dt+"') and ('"+end_dt+"') group by RESPONSE_CODE having count(*) > 0";
				result = stat.executeQuery(query);
				while(result.next())
				{
					summary = new Summary();
					summary.setSummary_name("MOBILE");
					summary.setSummary_count(""+result.getObject(1));
					summary.setSummaryamount(""+result.getObject(2));
					summary.setSummarydate(""+result.getObject(3));
					arr.add(summary);
					/*summary.setSummary_name("MOBILE");
					summary.setSummary_count(""+result.getObject(1));
					summary.setSummary_success_status(""+result.getObject(2));
					//summary.setSummary_failed_status(""+result.getObject(2));
					summary.setSummary_pending_status(""+result.getObject(3));
					arr.add(summary);*/
				}
				closeConnectionTelco(con, result);	
			}
			else if(channel.equals("POS"))//pos
			{
				con = connectToETMC();
				stat = con.createStatement();
				
				//query = "select count(*) as total_count,response_code,(select error_descr from ecarddb.dbo.e_isoerror where ecarddb.dbo.e_isoerror.error_code = ecarddb.dbo.e_tmcrequest.response_code) as response_desc from ecarddb.dbo.e_tmcrequest where (response_code not in ('00') or response_code = null) and merchant_type = '6012' and trans_date between('"+start_dt+"') and ('"+end_dt+"') group by RESPONSE_CODE having count(*) > 0";
				query =  "select count(*) trans_count_splite ,trans_amount,trans_date from ecarddb..e_transaction where channelid = '03' and trans_date between('"+start_dt+"') and ('"+end_dt+"') group by channelid,trans_amount";
				result = stat.executeQuery(query);
				while(result.next())
				{
					summary = new Summary();
					summary.setSummary_name("POS");
					summary.setSummary_count(""+result.getObject(1));
					summary.setSummaryamount(""+result.getObject(2));
					summary.setSummarydate(""+result.getObject(3));
					arr.add(summary);
					/*summary.setSummary_count(""+result.getObject(1));
					summary.setSummary_success_status(""+result.getObject(2));
					//summary.setSummary_failed_status(""+result.getObject(2));
					summary.setSummary_pending_status(""+result.getObject(3));
					arr.add(summary);*/
				}
				closeConnectionETMC(con, result);	
			}
			else if(channel.equals("ATM"))//atm
			{
				con = connectToETMC();
				stat = con.createStatement();
				
				query = "select count(*) trans_count_splite ,trans_amount,trans_date from ecarddb..e_transaction where channelid = '04' and trans_date between('"+start_dt+"') and ('"+end_dt+"') group by channelid,trans_amount";
				//query = "select count(*) as total_count,response_code,(select error_descr from ecarddb.dbo.e_isoerror where ecarddb.dbo.e_isoerror.error_code = ecarddb.dbo.e_tmcrequest.response_code) as response_desc from ecarddb.dbo.e_tmcrequest where (response_code  in ('00') or response_code = null) and merchant_type = '6011' and trans_date between('"+start_dt+"') and ('"+end_dt+"') group by RESPONSE_CODE having count(*) > 0";
				result = stat.executeQuery(query);
				while(result.next())
				{
					summary = new Summary();
					summary.setSummary_name("ATM");
					summary.setSummary_count(""+result.getObject(1));
					summary.setSummaryamount(""+result.getObject(2));
					summary.setSummarydate(""+result.getObject(3));
					arr.add(summary);
					/*summary.setSummary_count(""+result.getObject(1));
					summary.setSummary_success_status(""+result.getObject(2));
					//summary.setSummary_failed_status(""+result.getObject(2));
					summary.setSummary_pending_status(""+result.getObject(3));
					arr.add(summary);*/
				}
				//System.out.println("ATM QURY PORTION  --- >>>> "+query);
				closeConnectionETMC(con, result);	
			}
			else if(channel.equals("VTU"))//vtu
			{
				/*summary = new Summary();
				summary.setSummary_name("NA");
				summary.setSummary_count("0");
				summary.setSummary_failed_status("0");
				summary.setSummary_pending_status("0");
				arr.add(summary);*/
				
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select count(*)as total_count,status_id,(select error_desc from e_autoswitch_error where e_autoswitch_error.error_code = vtu_log.status_id) as response_desc from vtu_log where status_id <> '0' and txn_date_time between('"+start_dt+"') and ('"+end_dt+"') group by status_id having count(*) > 0";
				System.out.println("query vtu log " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					summary = new Summary();
					summary.setSummary_name("VTU");
					summary.setSummary_count(""+result.getObject(1));
					summary.setSummary_success_status(""+result.getObject(2));
					//summary.setSummary_failed_status(""+result.getObject(2));
					summary.setSummary_pending_status(""+result.getObject(3));
					arr.add(summary);
				}
				closeConnectionETMC(con, result);
			}
			else if(channel.equals("MOBILE - VERSION II"))//version II
			{
				con = connectMobileDB();
				stat = con.createStatement();
				
				query = "select count(*)as total_count, response_code, (select ecarddb..e_autoswitch_error.error_desc from ecarddb..e_autoswitch_error where ecarddb..e_autoswitch_error.error_code =  convert(varchar(5), mobiledb..m_outgoing_messages.response_code)) from m_outgoing_messages where response_code in (0,1000,1001,1002,1003,1004,1005) and created between('"+start_dt+"') and ('"+end_dt+"') group by response_code having count(*) > 0  ";
				System.out.println("query version II log " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					summary = new Summary();
					summary.setSummary_name("MOBILE - VERSION II");
					summary.setSummary_count(""+result.getObject(1));
					summary.setSummary_success_status(""+result.getObject(2));
					//summary.setSummary_failed_status(""+result.getObject(2));
					summary.setSummary_pending_status(""+result.getObject(3));
					arr.add(summary);
				}
				System.out.println("MOBILE - VERSION II --- >> > "+query);
				closeConnectionETMC(con, result);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
			closeConnectionETMC(con, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
			closeConnectionETMC(con, result);
		}
		return arr;
	}
	
	
	/*Method to do the select for the support scheme log*/
	public ArrayList getSupportSchemeLog(String channel,String card_num, String start_dt , String end_dt,
			String row_count, String status, String merchant_code, String bank_code,
			String terminal_id, String scheme_card, String dbServer)
	{
		ArrayList arr = new ArrayList();
		REQUEST_LOG request_log;
		E_TMCREQUEST tmc_request;
		String query = "";
		String start_pan = "";
		String end_pan = "";
		String pan = "";
		int counter = 0;
		Connection con = null, con1 = null;
		Statement stat = null, stat1 = null;
		ResultSet result = null, result1 = null;
		Cryptographer crypt = new Cryptographer();
		int scheme_position = scheme_card.length();
		String actual_cardnum = "";
		
		try
		{
			
			
			/*if(card_num.length() > 0)
			{
				con1 = connectPocketMoniEcardDB();
				stat1 = con1.createStatement();
				
				query = "select card_num from e_cardholder where track2 = '"+card_num+"' ";
				System.out.println("query " + query);
				result = stat1.executeQuery(query);
				if(result.next())
				{
					card_num = ""+result.getObject(1);
				}
			}*/
			
			
			if(card_num.trim().length() > 16)//the card num here is actually the track 2
			{
				if(dbServer.equals("1"))//pocketmoni server
				{
					con1 = connectPocketMoniEcardDB();
					stat1 = con1.createStatement();
				}
				else if(dbServer.equals("2"))//.57 server
				{
					con1 = connectToTestECard();
					stat1 = con1.createStatement();
				}
				
				query = "select card_num from e_cardholder where track2 = '"+card_num+"' ";
				result1 = stat1.executeQuery(query);
				if(result1.next())
				{
					actual_cardnum = ""+result1.getObject(1);
				}
			}
			else
			{
				actual_cardnum = card_num;
			}
			

			
			int ch = Integer.parseInt(channel);
			switch(ch)
			{
				case 1://web, the substring 01 means the transaction is for web
					
					
					con = connectToECard();
					stat = con.createStatement();
					

					if(status.equals("ALL"))
					{
						query = "select TRANSID,CARD_NUM,TRANS_DATE,TRANS_AMOUNT,TRANS_CODE," +
						"(select merchant_name from ecarddb.dbo.e_merchant where MERCHANT_CODE = ecarddb.dbo.e_requestlog.MERCHANT_CODE)," +
						"(select error_desc from ecarddb..e_autoswitch_error where error_code = ecarddb.dbo.e_requestlog.response_code),RESPONSE_TIME,TRANS_DESCR,client_id," +
						"REQUEST_ID,FEE,CURRENCY,datediff(ss,response_time,trans_date)" +
						" from ecarddb.dbo.e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
						" and substring(transid,1,2) = '01' and card_num like '"+actual_cardnum+"%' and substring(card_num,7,"+scheme_position+") like '"+ scheme_card +"%'  and merchant_code like '"+merchant_code+"%'  order by trans_date desc ";
					}
					else if(status.equals("SUCCESSFUL"))
					{
						query = "select TRANSID,CARD_NUM,TRANS_DATE,TRANS_AMOUNT,TRANS_CODE," +
						"(select merchant_name from ecarddb.dbo.e_merchant where MERCHANT_CODE = ecarddb.dbo.e_requestlog.MERCHANT_CODE)," +
						"(select error_desc from ecarddb..e_autoswitch_error where error_code = ecarddb.dbo.e_requestlog.response_code),RESPONSE_TIME,TRANS_DESCR,client_id," +
						"REQUEST_ID,FEE,CURRENCY,datediff(ss,response_time,trans_date)" +
						" from ecarddb.dbo.e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
						" and substring(transid,1,2) = '01' and card_num like '"+actual_cardnum+"%' and substring(card_num,7,"+scheme_position+") like '"+scheme_card +"%'  and merchant_code like '"+merchant_code+"%' and response_code in('0') order by trans_date desc ";
					}
					else if(status.equals("FAILED"))
					{
						query = "select TRANSID,CARD_NUM,TRANS_DATE,TRANS_AMOUNT,TRANS_CODE," +
						"(select merchant_name from ecarddb.dbo.e_merchant where MERCHANT_CODE = ecarddb.dbo.e_requestlog.MERCHANT_CODE)," +
						"(select error_desc from ecarddb..e_autoswitch_error where error_code = ecarddb.dbo.e_requestlog.response_code),RESPONSE_TIME,TRANS_DESCR,client_id," +
						"REQUEST_ID,FEE,CURRENCY,datediff(ss,response_time,trans_date)" +
						" from ecarddb.dbo.e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
						" and substring(transid,1,2) = '01' and card_num like '"+actual_cardnum+"%' and substring(card_num,7,"+scheme_position+") like '"+ scheme_card +"%'  and merchant_code like '"+merchant_code+"%' and response_code not in('0') order by trans_date desc ";
					}
										
					System.out.println("query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						counter++;
						
						request_log = new REQUEST_LOG();
						
						request_log.setCounter(""+counter);
						request_log.setTransid(""+result.getObject(1));
						request_log.setCard_num(""+result.getObject(2));
						request_log.setTrans_date(""+result.getObject(3));
						request_log.setTrans_amount(""+result.getObject(4));
						request_log.setTrans_code(""+result.getObject(5));
						
						String s = ""+result.getObject(6);
						if(s.equals("")||s.equals("null"))
							s = "";
						request_log.setMerchant_desc(s);
						
						request_log.setResponse_code(""+result.getObject(7));
						request_log.setResponse_time(""+result.getObject(8));
						request_log.setTrans_descr(""+result.getObject(9));
						request_log.setClient_id(""+result.getObject(10));
						request_log.setRequest_id(""+result.getObject(11));
						request_log.setFee(""+result.getObject(12));
						request_log.setCurrency(""+result.getObject(13));
						
						s = ""+result.getObject(14);
						if(s.equals("")||s.equals("null"))
							s = "";
						
						request_log.setResponse_time_in_secs(s);
						
						arr.add(request_log);
					}
					
					closeConnectionECard(con, result,result);
				
					break;
					
				
				case 3://pos
					
					con = connectToETMC();
					stat = con.createStatement();
					
					if(status.equals("ALL"))
					{
						
						
						query = "select mti,pan," +
								"(select pro_code_desc from ecarddb.dbo.e_tmccode where ecarddb.dbo.e_tmccode.pro_code =  substring(ecarddb.dbo.e_tmcrequest.pro_code,1,2))," +
								" card_acc_name, terminal_id," +
								" (select aquirer_name from ecarddb.dbo.e_tmcaquirer where ecarddb.dbo.e_tmcaquirer.aqid =  ecarddb.dbo.e_tmcrequest.aqid)," +
								" trans_date, datediff(ss,trans_date,response_time), amount," +
								"(select ecarddb.dbo.e_isoerror.error_descr from ecarddb.dbo.e_isoerror where error_code = ecarddb.dbo.e_tmcrequest.response_code)," +
								"trans_seq,target from ecarddb.dbo.e_tmcrequest where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
								" and merchant_type='6012' and pmv like '"+ crypt.doMd5Hash(actual_cardnum) +"%' and" +
								" terminal_id like '"+ terminal_id +"%' order by trans_date desc ";
					}
					
					
					result = stat.executeQuery(query);
					while(result.next())
					{
						counter++;
						tmc_request = new E_TMCREQUEST();
						
						tmc_request.setCounter(""+counter);
						tmc_request.setMti(""+result.getObject(1));
						
						try
						{
							pan = ""+result.getObject(2);
							start_pan = pan.substring(0,9);
							end_pan = pan.substring(pan.length()-4);
						}
						catch(Exception e){e.printStackTrace();}
						
						tmc_request.setPan(start_pan + "#########" + end_pan);
						tmc_request.setPro_code(""+result.getObject(3));
						tmc_request.setCard_acc_name(""+result.getObject(4));
						tmc_request.setTerminal_id(""+result.getObject(5));
						
						String s = ""+result.getObject(6);
						if(s.equals("")||s.equals("null"))
							s = "";
						tmc_request.setAqid(s);
						
						tmc_request.setTrans_date(""+result.getObject(7));
						
						s = ""+result.getObject(8);
						if(s.equals("")||s.equals("null"))
							s = "";
							
						tmc_request.setResponse_time_in_secs(s);
						tmc_request.setAmount(""+result.getObject(9));
						
						s = ""+result.getObject(10);
						if(s.equals("")||s.equals("null"))
							s = "";
						
						tmc_request.setResponse_code(s);
						tmc_request.setTrans_seq(""+result.getObject(11));
						tmc_request.setTarget(""+result.getObject(12));
						
						arr.add(tmc_request);
					}
					
					closeConnectionETMC(con, result);
					
					
					break;
					
				case 4://atm
					
					con = connectToETMC();
					stat = con.createStatement();
					
					if(status.equals("ALL"))
					{
						query = "select mti,pan," +
								"(select distinct pro_code_desc from ecarddb.dbo.e_tmccode where ecarddb.dbo.e_tmccode.pro_code =  substring(ecarddb.dbo.e_tmcrequest.pro_code,1,2))," +
								" card_acc_name, terminal_id," +
								" (select distinct aquirer_name from ecarddb.dbo.e_tmcaquirer where ecarddb.dbo.e_tmcaquirer.aqid =  ecarddb.dbo.e_tmcrequest.aqid)," +
								" trans_date, datediff(ss,trans_date,response_time),amount," +
								" (select distinct ecarddb.dbo.e_isoerror.error_descr from ecarddb.dbo.e_isoerror where error_code = ecarddb.dbo.e_tmcrequest.response_code)," +
								" trans_seq,target from ecarddb.dbo.e_tmcrequest where trans_date" +
								" between('"+start_dt+"') and ('"+end_dt+"') and merchant_type='6011'  and" +
								" pan like '"+crypt.doMd5Hash(actual_cardnum)+"%' and" +
								" terminal_id like '"+ terminal_id +"%' order by trans_date desc";
						
					}
					
					
					result = stat.executeQuery(query);
					while(result.next())
					{
						counter++;
						tmc_request = new E_TMCREQUEST();
						
						tmc_request.setCounter(""+counter);
						tmc_request.setMti(""+result.getObject(1));
						
						try
						{
							pan = ""+result.getObject(2);
							start_pan = pan.substring(0,9);
							end_pan = pan.substring(pan.length()-4);
						}
						catch(Exception e){}
						
						tmc_request.setPan(start_pan + "#########" + end_pan);
						tmc_request.setPro_code(""+result.getObject(3));
						tmc_request.setCard_acc_name(""+result.getObject(4));
						tmc_request.setTerminal_id(""+result.getObject(5));

						String s = ""+result.getObject(6);
						if(s.equals("")||s.equals("null"))
							s = "";
						tmc_request.setAqid(s);
						
						tmc_request.setTrans_date(""+result.getObject(7));
						
						s = ""+result.getObject(8);
						if(s.equals("")||s.equals("null")||s == null)
							s = "";
						
						tmc_request.setResponse_time_in_secs(s);
						tmc_request.setAmount(""+result.getObject(9));
						
						s = ""+result.getObject(10);
						if(s.equals("")||s.equals("null"))
							s = "";
						
						tmc_request.setResponse_code(s);
						tmc_request.setTrans_seq(""+result.getObject(11));
						tmc_request.setTarget(""+result.getObject(12));
						
						arr.add(tmc_request);
					}
					
					closeConnectionETMC(con , result);
				
					break;
					
				default:
					
					break;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
			closeConnectionECard(con, result, result);
			closeConnectionETMC(con, result);
			closeConnectionTestECard(con1, result, result1);
		}
		finally
		{
			closeConnectionTelco(con, result);
			closeConnectionECard(con, result, result);
			closeConnectionETMC(con, result);
			closeConnectionTestECard(con1, result, result1);
		}
		return arr;
	}
	
	
	/*Method to do the select for the pocket moni log*/
	public ArrayList getPocketMoniIncomingLog(String mobile_no, String start_dt , String end_dt)
	{
		ArrayList arr = new ArrayList();
		M_Incoming_Messages incoming_messages;
		String query = "";
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectMobileDB();
			stat = con.createStatement();
			
			String str_mobile = mobile_no.trim();
			/*if(str_mobile.trim().length()>0)
			{
				if(str_mobile.startsWith("234"));
				else
				{
					str_mobile = "234" + str_mobile.substring(1, str_mobile.length());
				}
			}*/

			query = "select id , mobile_no, destination_no, channel, message, ticket_id, unique_transid, checkvalue, msg_date " +
					"from m_incoming_messages where msg_date between('"+start_dt+"') and ('"+end_dt+"') and" +
					" mobile_no like '"+ str_mobile +"%' order by msg_date desc ";
		
			System.out.println("query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				
				incoming_messages = new M_Incoming_Messages();
				
				incoming_messages.setId(""+result.getObject(1));
				incoming_messages.setMobile_no(""+result.getObject(2));
				incoming_messages.setDestination_no(""+result.getObject(3));
				incoming_messages.setChannel(""+result.getObject(4));
				incoming_messages.setMessage(""+result.getObject(5));
				incoming_messages.setTicket_id(""+result.getObject(6));
				incoming_messages.setUnique_transid(""+result.getObject(7));
				incoming_messages.setCheckvalue(""+result.getObject(8));
				incoming_messages.setMsg_date(""+result.getObject(9));
				
				if(incoming_messages.getMessage().startsWith("?V*") || incoming_messages.getMessage().startsWith("?=V*") ||
						incoming_messages.getMessage().startsWith("?==V*"))
				{
					incoming_messages.setTransType("V");
				}
				
				arr.add(incoming_messages);
					
			}	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionMobileDB(con, result);
		}
		finally
		{
			closeConnectionMobileDB(con, result);
		}
		return arr;
	}
	
	
	/*Method to do the select for the outgoing pocket moni log*/
	public ArrayList getPocketMoniOutgoingLogByMessageID(String message_id)
	{
		ArrayList arr = new ArrayList();
		M_Outgoing_Messages outgoing_messages;
		String query = "";
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		String start_message = "";
		String hide_message = "";
		String end_message = "";
		
		try
		{
			con = connectMobileDB();
			stat = con.createStatement();
			
			query = "select id , message_id, appid, response_code, response_message, mobile_no, sender_id, token, created " +
					"from m_outgoing_messages where message_id = "+Integer.parseInt(message_id)+" ";
		
			System.out.println("query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				
				outgoing_messages = new M_Outgoing_Messages();
				
				outgoing_messages.setId(""+result.getObject(1));
				outgoing_messages.setMessage_id(""+result.getObject(2));
				outgoing_messages.setAppid(""+result.getObject(3));
				outgoing_messages.setResponse_code(""+result.getObject(4));
				//outgoing_messages.setResponse_message(""+result.getObject(5));
				
				String response_message = ""+result.getObject(5);
				
				try
				{
					start_message = response_message.substring(0,response_message.indexOf("PIN:"));
					end_message = response_message.substring(response_message.indexOf("For"));
					
					response_message = start_message + "PIN: ############### " + end_message;
				}
				catch(Exception e)
				{
					//System.out.println("unable to hash pin");
				}
				outgoing_messages.setResponse_message(response_message);
				
				
				outgoing_messages.setMobile_no(""+result.getObject(6));
				outgoing_messages.setSender_id(""+result.getObject(7));
				outgoing_messages.setToken(""+result.getObject(8));
				outgoing_messages.setCreated(""+result.getObject(9));
				
				arr.add(outgoing_messages);
					
			}	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionMobileDB(con, result);
		}
		finally
		{
			closeConnectionMobileDB(con, result);
		}
		return arr;
	}
	
	
	/*Method to do the select for the pos report log, from the bridge*/
	public ArrayList getPosReportLog(String channel,String card_num, String start_dt , String end_dt,
			String row_count, String status, String merchant_code, String bank_code, String terminal_id)
	{
		ArrayList arr = new ArrayList();
		T_SMS_RECEIVE sms_receive;
		REQUEST_LOG request_log;
		E_POSREQUEST tmc_posrequest;
		HashNumber hn = new HashNumber();
		String query = "";
		String start_pan = "";
		String end_pan = "";
		String pan = "";
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			con = connectToETMC();
			stat = con.createStatement();
			
			if(status.equals("ALL"))
			{
				query = "select mti, pan2,(select pro_code_desc from ecarddb.dbo.e_tmccode where ecarddb.dbo.e_tmccode.pro_code =  substring(ecarddb.dbo.e_posrequest.pro_code3,1,2)), card_acc_name43, terminal_id, (select aquirer_name from ecarddb.dbo.e_tmcaquirer where ecarddb.dbo.e_tmcaquirer.aqid =  ecarddb.dbo.e_posrequest.aqid), created, amount,(select ecarddb.dbo.e_isoerror.error_descr from ecarddb.dbo.e_isoerror where error_code = ecarddb.dbo.e_posrequest.response_code) from ecarddb.dbo.E_POSREQUEST where created between('"+start_dt+"') and ('"+end_dt+"') and pan2 like '%"+card_num+"%' and terminal_id like '%"+ terminal_id +"%' order by created desc ";
			}
			
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				tmc_posrequest = new E_POSREQUEST();
				
				tmc_posrequest.setCounter(""+counter);
				tmc_posrequest.setMti(""+result.getObject(1));
				
				try
				{
					pan = ""+result.getObject(2);
					start_pan = pan.substring(0,9);
					end_pan = pan.substring(pan.length()-4);
				}
				catch(Exception e){e.printStackTrace();}
				
				tmc_posrequest.setPan(start_pan + "#########" + end_pan);
				tmc_posrequest.setPro_code(""+result.getObject(3));
				tmc_posrequest.setCard_acc_name(""+result.getObject(4));
				tmc_posrequest.setTerminal_id(""+result.getObject(5));
				
				String s = ""+result.getObject(6);
				if(s.equals("")||s.equals("null"))
					s = "";
				tmc_posrequest.setAqid(s);
				
				tmc_posrequest.setTrans_date(""+result.getObject(7));
				
				/*s = ""+result.getObject(8);
				if(s.equals("")||s.equals("null"))
					s = "";
					
				tmc_posrequest.setResponse_time_in_secs(s);*/
				tmc_posrequest.setAmount(""+result.getObject(8));
				
				s = ""+result.getObject(9);
				if(s.equals("")||s.equals("null"))
					s = "";
				
				tmc_posrequest.setResponse_code(s);
				
				
				arr.add(tmc_posrequest);
			}
			
			closeConnectionETMC(con, result);
			
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionETMC(con, result);
		}
		finally
		{
			closeConnectionETMC(con, result);
		}
		return arr;
	}
	
	
	
	
	/*Method to do the select for the pos transactions*/
	public ArrayList getPOSTransactions(String searchType, String start_dt , String end_dt)
	{
		ArrayList arr = new ArrayList();
		E_TMCREQUEST tmc_request;
		E_SETTLE_BATCH e_settle;
		HashNumber hn = new HashNumber();
		String query = "";
		int counter = 0;
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			
			if(searchType.equals("ACTIVE POS"))//type of trans attempts on active POS  on server voucher_svr (.70)
			{
				con = connectToETMC();
				stat = con.createStatement();
				
				query = "select CARD_ACC_ID,TERMINAL_ID,CARD_ACC_NAME, count(*) tran_count from e_tmcrequest" +
						" where trans_date between '"+start_dt+"' and '"+end_dt+"'" +
						" and MERCHANT_TYPE = '6012' group by CARD_ACC_ID,TERMINAL_ID,CARD_ACC_NAME order by CARD_ACC_ID";
				
				System.out.println("query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					counter++;
					tmc_request = new E_TMCREQUEST();
					
					tmc_request.setCounter(""+counter);
					tmc_request.setCard_acc_id(""+result.getObject(1));
					tmc_request.setTerminal_id(""+result.getObject(2));
					tmc_request.setCard_acc_name(""+result.getObject(3));
					tmc_request.setTran_count(""+result.getObject(4));

					arr.add(tmc_request);
				}
				closeConnectionTelco(con, result);
			}
			else if(searchType.equals("TRANSACTION ATTEMPTS"))//type of trans successfully done on active POS  on server voucher_svr (.70)
			{
				con = connectToETMC();
				stat = con.createStatement();
				
				query = "select substring(e_tmcrequest.PRO_CODE,1,2), e_tmccode.pro_code_desc, count(*) trans_count, sum(AMOUNT) trans_sum" +
						" from e_tmcrequest left outer join e_tmccode on substring(e_tmcrequest.PRO_CODE,1,2) = e_tmccode.pro_code" +
						" where trans_date between '"+start_dt+"' and '"+end_dt+"'" +
						" and MERCHANT_TYPE = '6012' group by substring(e_tmcrequest.PRO_CODE,1,2), e_tmccode.pro_code_desc";
		
				System.out.println("query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					counter++;
					tmc_request = new E_TMCREQUEST();
					
					tmc_request.setCounter(""+counter);
					tmc_request.setPro_code(""+result.getObject(1));
					tmc_request.setPro_code_desc(""+result.getObject(2));
					tmc_request.setTran_count(""+result.getObject(3));
					tmc_request.setAmount(""+result.getObject(4));
	
					arr.add(tmc_request);
				}
				
				closeConnectionETMC(con, result);
			}
			else if(searchType.equals("SUCCESSFUL TRANSACTIONS"))//type of trans successfully done on active POS  on server voucher_svr (.70)
			{
				con = connectToETMC();
				stat = con.createStatement();
				
				query = "select substring(e_tmcrequest.PRO_CODE,1,2), e_tmccode.pro_code_desc, count(*) trans_count, sum(AMOUNT) trans_sum" +
						" from e_tmcrequest left outer join e_tmccode on substring(e_tmcrequest.PRO_CODE,1,2) = e_tmccode.pro_code" +
						" where trans_date between '"+start_dt+"' and '"+end_dt+"' and MERCHANT_TYPE = '6012' and response_code = '00'" +
						" group by substring(e_tmcrequest.PRO_CODE,1,2), e_tmccode.pro_code_desc";
		
				System.out.println("query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					counter++;
					tmc_request = new E_TMCREQUEST();
					
					tmc_request.setCounter(""+counter);
					tmc_request.setPro_code(""+result.getObject(1));
					tmc_request.setPro_code_desc(""+result.getObject(2));
					tmc_request.setTran_count(""+result.getObject(3));
					tmc_request.setAmount(""+result.getObject(4));
	
					arr.add(tmc_request);
				}
				
				closeConnectionETMC(con, result);
			}
			else if(searchType.equals("FAILED TRANSACTIONS"))//type of trans not successful on active POS  on server voucher_svr (.70)
			{
				con = connectToETMC();
				stat = con.createStatement();
				
				query = "select substring(e_tmcrequest.PRO_CODE,1,2), e_tmccode.pro_code_desc, count(*) trans_count, sum(AMOUNT) trans_sum" +
						" from e_tmcrequest left outer join e_tmccode on substring(e_tmcrequest.PRO_CODE,1,2) = e_tmccode.pro_code" +
						" where trans_date between '"+start_dt+"' and '"+end_dt+"' and MERCHANT_TYPE = '6012' and response_code <> '00'" +
						" group by substring(e_tmcrequest.PRO_CODE,1,2), e_tmccode.pro_code_desc";
		
				System.out.println("query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					counter++;
					tmc_request = new E_TMCREQUEST();
					
					tmc_request.setCounter(""+counter);
					tmc_request.setPro_code(""+result.getObject(1));
					tmc_request.setPro_code_desc(""+result.getObject(2));
					tmc_request.setTran_count(""+result.getObject(3));
					tmc_request.setAmount(""+result.getObject(4));
	
					arr.add(tmc_request);
				}
				
				closeConnectionETMC(con, result);
			}
			else if(searchType.equals("DAILY POS COMMISSION"))//Commission earned per day on POS  on server ecard_svr (.105)
			{
				con = connectToSettleECard();
				stat = con.createStatement();
				
				query = "select settle_batch, batch_date, count(*), sum(trans_amount)" +
						" from e_fee_detail_bk left outer join e_settle_batch on e_fee_detail_bk.settle_batch = e_settle_batch.batch_id" +
						" where settle_batch = '11GYA04000910' and channelid = '03' and merchant_code = '0690019914'" +
						" group by settle_batch, batch_date";
		
				System.out.println("query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					counter++;
					e_settle = new E_SETTLE_BATCH();
					
					e_settle.setCounter(""+counter);
					e_settle.setBatch_id(""+result.getObject(1));
					e_settle.setBatch_date(""+result.getObject(2));
					e_settle.setTotal_count(""+result.getObject(3));
					e_settle.setTotal_amount(""+result.getObject(4));
	
					arr.add(e_settle);
				}
				
				closeConnectionSettleEcard(con, result);
			}
				
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
			closeConnectionETMC(con, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeConnectionETMC(con, result);
		}
		return arr;
	}
	
	/*Method to get Transactions based on a merchant_code*/
	public ArrayList getMerchantServiceTransactions(String card_num, String start_dt, String end_dt,String merchantcode,String channelId)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION tran = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		double d = 0.0;
		String str = "";
		String custname = "";
		String toll = "";
		String afterprefix = "";
		
		String apostrophe = "'";

		try
		{
			
			if(merchantcode.indexOf(":")>0)
			{
				String m[] = merchantcode.split(":");
				merchantcode = "";
				for(int i=0;i<m.length;i++)
				{
					merchantcode += apostrophe + m[i] + apostrophe + ",";
				}
				
				merchantcode = merchantcode.substring(0, merchantcode.lastIndexOf(","));
			}
			else
			{
				merchantcode = apostrophe + merchantcode + apostrophe;
			}
			
			System.out.println("merchantcode " + merchantcode);
			
			con = connectToECard();
			stat = con.createStatement();
			
			
			if(channelId.equals("01") || channelId.equals("02") || channelId.equals("03") || channelId.equals("04") || channelId.equals("05"))
			{
			
				query = "select trans_no, card_num, merchant_code, trans_descr," +
						" trans_date, trans_amount, channelid " +
						" from ecarddb..e_transaction  " +
						" where merchant_code in ("+merchantcode+") " +
						" and channelid like '"+channelId+"%'  " +
						" and trans_date between('"+start_dt+"') and ('"+end_dt+"') order by trans_date";
				
				System.out.println("getMerchantServiceTransactions query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					tran = new E_TRANSACTION();
					tran.setTrans_no(""+result.getObject(1));
					tran.setCard_num(""+result.getObject(2));
					tran.setMerchat_code(""+result.getObject(3));
					tran.setTrans_desc(""+result.getObject(4));
					tran.setTrans_date(""+result.getObject(5));
					tran.setTrans_amount(""+result.getObject(6));
					tran.setChannelid(""+result.getObject(7));
					
					custname = tran.getTrans_desc();
					
					
					
					if(custname.startsWith("PAYMENT TO:"));
					else if(custname.startsWith("PAYMENT:"))
					{
						afterprefix = custname.substring(custname.indexOf(":")+1);
						tran.setFirstname(afterprefix.substring(0, afterprefix.indexOf(" ")));
						tran.setLastname(afterprefix.substring(afterprefix.indexOf(" "), afterprefix.indexOf(":")));
					}
					else
					{
						System.out.println("didnt start with " + custname);
					}
					
					
					
					arr.add(tran);
				}
			}
			else
			{
				
				query = "select trans_no, card_num, merchant_code, trans_descr," +
						" trans_date, trans_amount, channelid " +
						" from ecarddb..e_transaction  " +
						" where merchant_code in ("+merchantcode+") " +
						" and trans_date between('"+start_dt+"') and ('"+end_dt+"') order by trans_date";
				
				System.out.println("getMerchantServiceTransactions query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					tran = new E_TRANSACTION();
					tran.setTrans_no(""+result.getObject(1));
					tran.setCard_num(""+result.getObject(2));
					tran.setMerchat_code(""+result.getObject(3));
					tran.setTrans_desc(""+result.getObject(4));
					tran.setTrans_date(""+result.getObject(5));
					tran.setTrans_amount(""+result.getObject(6));
					tran.setChannelid(""+result.getObject(7));
					
					custname = tran.getTrans_desc();
					
					if(custname.startsWith("PAYMENT TO:"));
					else if(custname.startsWith("PAYMENT:"))
					{
						afterprefix = custname.substring(custname.indexOf(":")+1);
						tran.setFirstname(afterprefix.substring(0, afterprefix.indexOf(" ")));
						tran.setLastname(afterprefix.substring(afterprefix.indexOf(" "), afterprefix.indexOf(":")));
					}
					else
					{
						System.out.println("didnt start with " + custname);
					}
					
					arr.add(tran);
				}
				
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	
	
	
	/*Method to do the select from the t_sms_receive*/
	public ArrayList getT_SMS_RECEIVE(String sms_key, String mobile, String user_code)
	{
		ArrayList arr = new ArrayList();
		T_SMS_RECEIVE sms_receive;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToTelco();
			stat = con.createStatement();
			
			String query = "select * from telcodb.dbo.T_SMS_RECEIVE where sms_key = '"+sms_key+"' order by trans_date desc";
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				sms_receive = new T_SMS_RECEIVE();
				sms_receive.setTrans_dt(""+result.getObject(1));
				sms_receive.setSms_dest(""+result.getObject(2));
				sms_receive.setSms_message(""+result.getObject(3));
				
				if(user_code.equals("000") || user_code.equalsIgnoreCase("001"))
				{
					sms_receive.setSms_originator(""+result.getObject(4));
				}
				else
				{
					if(mobile.trim().length()<=0)
						sms_receive.setSms_originator("#############");
					else
						sms_receive.setSms_originator(""+result.getObject(4));
				}
				sms_receive.setSms_status(""+result.getObject(5));
				sms_receive.setSms_key(""+result.getObject(6));
				sms_receive.setSms_id(""+result.getObject(7));
				sms_receive.setSms_duplicate(""+result.getObject(8));
				
				arr.add(sms_receive);
			}
			
		}
		catch(SQLException sq)
		{
			System.out.println("error " + sq.getMessage());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con , result);
		}
		finally
		{
			closeConnectionTelco(con, result);
		}
		return arr;
	}
	
	/*Method to do the select from the sms log*/
	public ArrayList getE_SMS_LOG(String sms_key, String mobile, String user_code)
	{
		ArrayList arr = new ArrayList();
		String start_message = "";
		String hide_message = "";
		String end_message = "";
		String sent_message = "";
		SMS_LOG sms_log;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			String query = "select UNIQUE_TRANSID,MOBILE,MESSAGE_DATE," +
					"SOURCE_CARD,TRANS_TYPE,RESPONSE_MESSAGE," +
					"(select error_desc from ecarddb.dbo.e_autoswitch_error where ecarddb.dbo.e_autoswitch_error.error_code = convert(varchar(2),ecarddb.dbo.e_smslog.response_code)),RESPONSE_TIME,ISSUER," +
					"datediff(ss,message_date,response_time) from ecarddb.dbo.e_smslog where unique_transid = '"+ sms_key +"' order by MESSAGE_DATE desc";
			result = stat.executeQuery(query);
			
			while(result.next())
			{
				sms_log = new SMS_LOG();
				
				sms_log.setUnique_transid(""+result.getObject(1));
				
				if(user_code.equals("000") || user_code.equalsIgnoreCase("001"))
				{
					sms_log.setMobile(""+result.getObject(2));
				}
				else
				{
					if(mobile.trim().length()<=0)
						sms_log.setMobile("#############");
					else
						sms_log.setMobile(""+result.getObject(2));
				}
				
				sms_log.setMessage_date(""+result.getObject(3));
				sms_log.setSource_card(""+result.getObject(4));
				sms_log.setTrans_type(""+result.getObject(5));
				
				String response_message = ""+result.getObject(6);
				
				try
				{
					start_message = response_message.substring(0,response_message.indexOf("PIN:"));
					hide_message = response_message.substring(response_message.indexOf("PIN:"),response_message.indexOf("Ref:"));
					end_message = response_message.substring(response_message.indexOf("Ref:"));
					
					response_message = start_message + "PIN: ############### " + end_message;
				}
				catch(Exception e)
				{
					//System.out.println("unable to hash pin");
				}
				sms_log.setResponse_message(response_message);
				
				
				sms_log.setResponse_code(""+result.getObject(7));
				sms_log.setResponse_time(""+result.getObject(8));
				sms_log.setIssuer(""+result.getObject(9));
				
				String s = ""+result.getObject(10);
				if(s.equals("")||s.equals("null"))
					s = "";
				
				sms_log.setResponse_time_in_secs(s);
				
				arr.add(sms_log);
			}
		}
		catch(SQLException sq)
		{
			System.out.println("error " + sq.getMessage());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con , result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Method to do the select from the sms log but returning just the sms log*/
	public SMS_LOG getE_SMS_LOG_2(String sms_key)
	{
		ArrayList arr = new ArrayList();
		String start_message = "";
		String hide_message = "";
		String end_message = "";
		String sent_message = "";
		SMS_LOG sms_log = new SMS_LOG();
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			String query = "select UNIQUE_TRANSID,MOBILE,MESSAGE_DATE," +
					"SOURCE_CARD,TRANS_TYPE,RESPONSE_MESSAGE," +
					"(select error_desc from ecarddb.dbo.e_autoswitch_error where ecarddb.dbo.e_autoswitch_error.error_code = convert(varchar(2),ecarddb.dbo.e_smslog.response_code)),RESPONSE_TIME,ISSUER," +
					"datediff(ss,message_date,response_time) from ecarddb.dbo.e_smslog where unique_transid = '"+ sms_key +"' order by MESSAGE_DATE desc";
			result = stat.executeQuery(query);
			
			while(result.next())
			{
				sms_log.setUnique_transid(""+result.getObject(1));
				sms_log.setMobile(""+result.getObject(2));
				sms_log.setMessage_date(""+result.getObject(3));
				sms_log.setSource_card(""+result.getObject(4));
				sms_log.setTrans_type(""+result.getObject(5));
				
				String response_message = ""+result.getObject(6);
				
				try
				{
					start_message = response_message.substring(0,response_message.indexOf("PIN:"));
					hide_message = response_message.substring(response_message.indexOf("PIN:"),response_message.indexOf("Ref:"));
					end_message = response_message.substring(response_message.indexOf("Ref:"));
					
					response_message = start_message + "PIN: ############### " + end_message;
				}
				catch(Exception e)
				{
					//System.out.println("unable to hash pin");
				}
				sms_log.setResponse_message(response_message);
				
				
				sms_log.setResponse_code(""+result.getObject(7));
				sms_log.setResponse_time(""+result.getObject(8));
				sms_log.setIssuer(""+result.getObject(9));
				
				String s = ""+result.getObject(10);
				if(s.equals("")||s.equals("null"))
					s = "";
				
				sms_log.setResponse_time_in_secs(s);
			}
		}
		catch(SQLException sq)
		{
			System.out.println("error " + sq.getMessage());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return sms_log;
	}
	
	
	
	/*Method to do the select from the sms log so i can resend the pin*/
	public String getE_SMS_LOGResendPIN(String sms_key)
	{
		ArrayList arr = new ArrayList();
		String response_message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			String smslogkey = "02" + sms_key;
			
			String query = "select RESPONSE_MESSAGE from ecarddb.dbo.e_smslog where unique_transid = '"+ smslogkey +"' order by MESSAGE_DATE desc";
			System.out.println("getE_SMS_LOGResendPIN query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				response_message = ""+result.getObject(1);
			}
			
			if(response_message.trim().length()<=0)
			{
				query = "select sms_message from telcodb.dbo.T_SMS_SEND where sms_key = '"+ sms_key +"'";
				System.out.println("getT_SMS_SENDResendPIN query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					response_message = ""+result.getObject(1);
				}
			}
		}
		catch(SQLException sq)
		{
			System.out.println("error " + sq.getMessage());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con , result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return response_message;
	}
	
	
	/*Method to do the select from the outgoing messageso i can resend the pin*/
	public String getOutGoingResendPIN(String message_id)
	{
		String response_message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		
	
		try
		{
			con = connectMobileDB();
			stat = con.createStatement();
			
			String query = "select response_message " +
			"from m_outgoing_messages where message_id = "+Integer.parseInt(message_id)+" and response_code = 1000 ";

			System.out.println("query " + query);
			result = stat.executeQuery(query);
			if(result.next())
			{
				response_message = ""+result.getObject(1);
			}
			
			if(response_message.trim().length() <=0)
			{
				query = "select response_message " +
				"from m_outgoing_messages where message_id = "+Integer.parseInt(message_id)+" and response_code = 0 ";

				System.out.println("query " + query);
				result = stat.executeQuery(query);
				if(result.next())
				{
					response_message = ""+result.getObject(1);
				}
				
			}
		}
		catch(SQLException sq)
		{
			System.out.println("error " + sq.getMessage());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionMobileDB(con, result);
		}
		finally
		{
			closeConnectionMobileDB(con, result);
		}
		return response_message;
	}
	
	/*Method to do the select from the request log*/
	public ArrayList getE_REQUEST_LOG(String unique_transid, String mobile, String user_code)
	{
		ArrayList arr = new ArrayList();
		REQUEST_LOG request_log;
		HashNumber hn = new HashNumber();
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			String query = "select TRANSID,CARD_NUM,TRANS_DATE,TRANS_AMOUNT,TRANS_CODE," +
			"(select merchant_name from ecarddb.dbo.e_merchant where MERCHANT_CODE = ecarddb.dbo.e_requestlog.MERCHANT_CODE)," +
			"(select error_desc from e_autoswitch_error where error_code = ecarddb.dbo.e_requestlog.response_code),RESPONSE_TIME,TRANS_DESCR,client_id," +
			"REQUEST_ID,FEE,CURRENCY,datediff(ss,response_time,trans_date)" +
			" from ecarddb.dbo.e_requestlog where transid = '"+ unique_transid +"' order by trans_DATE desc";
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				request_log = new REQUEST_LOG();
				
				request_log.setTransid(""+result.getObject(1));
				
				if(user_code.equals("000") || user_code.equalsIgnoreCase("001"))
				{
					request_log.setCard_num(hn.hashStringValue(""+result.getObject(2), 9, 4) );
				}
				else
				{
					if(mobile.trim().length()<=0)
						request_log.setCard_num("#############");
					else
						request_log.setCard_num(hn.hashStringValue(""+result.getObject(2), 9, 4) );
				}
				
				
				request_log.setTrans_date(""+result.getObject(3));
				request_log.setTrans_amount(""+result.getObject(4));
				request_log.setTrans_code(""+result.getObject(5));
				
				String s = ""+result.getObject(6);
				if(s.equals("")||s.equals("null"))
					s = "";
				request_log.setMerchant_desc(s);
				
				request_log.setResponse_code(""+result.getObject(7));
				request_log.setResponse_time(""+result.getObject(8));
				request_log.setTrans_descr(""+result.getObject(9));
				request_log.setClient_id(""+result.getObject(10));
				request_log.setRequest_id(""+result.getObject(11));
				request_log.setFee(""+result.getObject(12));
				request_log.setCurrency(""+result.getObject(13));
				
				s = ""+result.getObject(14);
				if(s.equals("")||s.equals("null"))
					s = "";
				
				request_log.setResponse_time_in_secs(s);
				
				arr.add(request_log);
			}
		}
		catch(SQLException sq)
		{
			System.out.println("error " + sq.getMessage());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Method to do the select from the telcodb send sms log*/
	public ArrayList getT_SMS_SEND(String sms_key , String mobile, String user_code)
	{
		ArrayList arr = new ArrayList();
		String start_message = "";
		String hide_message = "";
		String end_message = "";
		String sent_message = "";
		T_SMS_SEND sms_send;
		String query = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToTelco();
			stat = con.createStatement();
			
			query = "select *,datediff(ss,sms_date,sms_dlvydate) from telcodb.dbo.T_SMS_SEND where sms_key = '"+ sms_key +"'";
			//System.out.println("sms send query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				sms_send = new T_SMS_SEND();
				
				if(user_code.equals("000")|| user_code.equalsIgnoreCase("001"))
				{
					sms_send.setSms_dest(""+result.getObject(1));
				}
				else
				{
					if(mobile.trim().length()<=0)
						sms_send.setSms_dest("#############");
					else
						sms_send.setSms_dest(""+result.getObject(1));
				}
				
				String response_message = ""+result.getObject(2);
				
				try
				{
					start_message = response_message.substring(0,response_message.indexOf("PIN:"));
					hide_message = response_message.substring(response_message.indexOf("PIN:"),response_message.indexOf("Ref:"));
					end_message = response_message.substring(response_message.indexOf("Ref:"));
					
					response_message = start_message + "PIN: ############### " + end_message;
				}
				catch(Exception e)
				{
					//System.out.println("cant convert pin to hash");
				}
				sms_send.setSms_message(response_message);
				
				sms_send.setSms_originator(""+result.getObject(3));
				sms_send.setSms_date(""+result.getObject(4));
				sms_send.setSms_status(""+result.getObject(5));
				sms_send.setSms_key(""+result.getObject(6));
				sms_send.setSms_subdate(""+result.getObject(7));
				sms_send.setSms_dlvydate(""+result.getObject(8));
				sms_send.setSms_dlvystatus(""+result.getObject(9));
				sms_send.setSmsid(""+result.getObject(10));
				sms_send.setSms_id(""+result.getObject(11));
				sms_send.setClient_id(""+result.getObject(12));
				sms_send.setSend_count(""+result.getObject(13));
				sms_send.setMsg_tag(""+result.getObject(14));
				sms_send.setMsg_type(""+result.getObject(15));
				
				String s = ""+result.getObject(16);
				if(s.equals("")||s.equals("null"))
					s = "";
				
				sms_send.setResponse_time_in_secs(s);
				
				arr.add(sms_send);
				
			}
		}
		catch(SQLException sq)
		{
			System.out.println("error " + sq.getMessage());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
		}
		return arr;
	}
	
	
	/*Method to do the select from the vtu log using the trans id*/
	public ArrayList getVTUByTransID(String transid , String mobile, String user_code)
	{
		ArrayList arr = new ArrayList();
		VTU_LOG vtu_log;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			String query = "select amount, orig_msisdn, dest_msisdn, line_type, txn_date_time, response_code, response_message, mobile, response_date_time, voucher_type,status_id,id,unique_trans_id from ecarddb.dbo.vtu_log where unique_trans_id = '"+transid+"'";
			result = stat.executeQuery(query);
			while(result.next())
			{
				vtu_log = new VTU_LOG();
				vtu_log.setAmount(""+result.getObject(1));
				vtu_log.setOrig_msisdn(""+result.getObject(2));
				
				if(user_code.equals("000") || user_code.equalsIgnoreCase("001"))
				{
					vtu_log.setDest_msisdn(""+result.getObject(3));
				}
				else
				{
					if(mobile.trim().length()<=0)
						vtu_log.setDest_msisdn("#############");
					else
						vtu_log.setDest_msisdn(""+result.getObject(3));
				}
				
				vtu_log.setLine_type(""+result.getObject(4));
				vtu_log.setTxn_date_time(""+result.getObject(5));
				vtu_log.setResponse_code(""+result.getObject(6));
				vtu_log.setResponse_message(""+result.getObject(7));
				vtu_log.setMobile(""+result.getObject(8));
				vtu_log.setResponse_date(""+result.getObject(9));
				vtu_log.setVoucher_type(""+result.getObject(10));
				vtu_log.setStatus_id(""+result.getObject(11));
				vtu_log.setId(""+result.getObject(12));
				vtu_log.setUnique_trans_id(""+result.getObject(13));
				
				
				arr.add(vtu_log);
			}	
			
		}
		catch(SQLException sq)
		{
			System.out.println("error " + sq.getMessage());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	
	
	/*Method to get the channel types*/
	public ArrayList getChannel()
	{
		ArrayList arr = new ArrayList();
		Channel channel;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			String query = "select * from ecarddb..e_channel";
			result = stat.executeQuery(query);
			while(result.next())
			{
				channel = new Channel();
				
				channel.setChannel_id(""+result.getObject(1));
				channel.setChannel_desc(""+result.getObject(2));

				arr.add(channel);
			}
		}
		catch(SQLException sq)
		{
			System.out.println("error " + sq.getMessage());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	/*Method to get the channel name*/
	public String getChannelName(String channel_id)
	{
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			String query = "select channel_name from ecarddb..e_channel where channel_id = '"+channel_id+"'";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = ""+result.getObject(1);
			}
			if(message.equals(null) || message.equals("null") || message.length()<=0)
			{
				message = "NA";
			} 
		}
		catch(SQLException sq)
		{
			System.out.println("error " + sq.getMessage());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return message;
	}
	public ArrayList getBranchName()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Bank bank;
		Connection con = null;
		
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToTelco();
			stat = con.createStatement();
			
			//con1 = connectToECard();
			//stat = con1.createStatement();
		
			query = "select distinct e.sub_code ,e.sub_name from ecarddb.dbo.E_SUBISSUER as e, telcodb.dbo.T_PAYTRANS as t where t.sub_code = e.sub_code";

			/*query = "Select distinct ecarddb.dbo.E_SUBISSUER.sub_code,ecarddb.dbo.E_SUBISSUER.sub_name   " +
					"from ecarddb.dbo.E_SUBISSUER left JOIN telcodb.dbo.T_PAYTRANS " +
					"ON ecarddb.dbo.E_SUBISSUER.sub_code = telcodb.dbo.T_PAYTRANS.sub_code ";
					*/
			
			System.out.println("getting Bank Branch " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				bank = new Bank();
				bank.setBranchCode(""+result.getObject(1));
				bank.setBranchName(""+result.getObject(2));
				//bank.setBank_nm(bank_nm)
				arr.add(bank);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con,result);
			//closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionTelco(con,result);
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Method to get the trans code*/
	public ArrayList<E_TRANSCODE> getTransCode()
	{
		ArrayList<E_TRANSCODE> arr = new ArrayList<E_TRANSCODE>();
		E_TRANSCODE e_transcode;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			String query = "select * from ecarddb..E_TRANSCODE";
			result = stat.executeQuery(query);
			while(result.next())
			{
				e_transcode = new E_TRANSCODE();
				
				e_transcode.setF_code(""+result.getObject(1));
				e_transcode.setF_name(""+result.getObject(2));
				e_transcode.setF_alias(""+result.getObject(3));

				arr.add(e_transcode);
			}
		}
		catch(SQLException sq)
		{
			System.out.println("error " + sq.getMessage());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Method to get the trans code name*/
	public String getTransName(String trans_code)
	{
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			String query = "select f_name from ecarddb..E_TRANSCODE where f_code = '"+trans_code+"'";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = ""+result.getObject(1);
			}
			if(message.equals(null) || message.equals("null") || message.length()<=0)
			{
				message = "NA";
			} 
		}
		catch(SQLException sq)
		{
			System.out.println("error " + sq.getMessage());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return message;
	}
	

	
	
	/**
	 * This method is used to generate summary report
	 * */
	public ArrayList getLogSummary(String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Summary summary = new Summary();
		String str = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			//mobile channel
			query = "select count(*) from ecarddb.dbo.E_SMSLOG where message_date between('"+ start_dt +"') and ('"+ end_dt +"')";
			result = stat.executeQuery(query);
			while(result.next())
			{
				summary = new Summary();
				summary.setSummary_count(""+result.getObject(1));
				summary.setSummary_name("Mobile");
				//arr.add(summary);
			}
			
			
			query = "select count(*) from ecarddb.dbo.E_SMSLOG where message_date between('"+ start_dt +"') and ('"+ end_dt +"') and response_code = 0";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			//Summary s = (Summary)arr.get(0);
			summary.setSummary_success_status(str);
			
			
			query = "select count(*) from ecarddb.dbo.E_SMSLOG where message_date between('"+ start_dt +"') and ('"+ end_dt +"') and response_code <> 0";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			
			//summary = (Summary)arr.get(0);
			summary.setSummary_failed_status(str);
			arr.add(summary);
			
			
			//web
			query = "select count(*) from ecarddb.dbo.e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"') and substring(transid,1,2) = '01'";//01 is for web
			result = stat.executeQuery(query);
			while(result.next())
			{
				summary = new Summary();
				summary.setSummary_count(""+result.getObject(1));
				summary.setSummary_name("WEB");
				//arr.add(summary);
			}
			
			query = "select count(*) from ecarddb.dbo.e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"') and substring(transid,1,2) = '01' and response_code = '0'";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			//summary = (Summary)arr.get(1);
			summary.setSummary_success_status(str);
			
			
			query = "select count(*) from ecarddb.dbo.e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"') and substring(transid,1,2) = '01' and response_code <> '0'";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			//s = (Summary)arr.get(1);
			summary.setSummary_failed_status(str);
			arr.add(summary);
			
			
			closeConnectionECard(con, result, result);
			
			
			con = connectToETMC();
			stat = con.createStatement();
			
			//pos
			query = "select count(*) from ecarddb.dbo.e_tmcrequest where merchant_type = '6012' and trans_date between('"+start_dt+"') and ('"+end_dt+"')";
			result = stat.executeQuery(query);
			while(result.next())
			{
				summary = new Summary();
				summary.setSummary_count(""+result.getObject(1));
				summary.setSummary_name("POS");
				//arr.add(summary);
			}
			
			query = "select count(*) from ecarddb.dbo.e_tmcrequest where  merchant_type = '6012' and trans_date between('"+start_dt+"') and ('"+end_dt+"') and response_code in('00')";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			//s = (Summary)arr.get(2);
			summary.setSummary_success_status(str);
			
			query = "select count(*) from ecarddb.dbo.e_tmcrequest where merchant_type = '6012' and trans_date between('"+start_dt+"') and ('"+end_dt+"') and (response_code not in ('00') or response_code = null)";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			//s = (Summary)arr.get(2);
			summary.setSummary_failed_status(str);
			arr.add(summary);
			
			//atm
			query = "select count(*) from ecarddb.dbo.e_tmcrequest where merchant_type = '6011' and trans_date between('"+start_dt+"') and ('"+end_dt+"')";
			result = stat.executeQuery(query);
			while(result.next())
			{
				summary = new Summary();
				summary.setSummary_count(""+result.getObject(1));
				summary.setSummary_name("ATM");
				//arr.add(summary);
			}
			
			query = "select count(*) from ecarddb.dbo.e_tmcrequest where merchant_type = '6011' and trans_date between('"+start_dt+"') and ('"+end_dt+"') and response_code in('00')";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			//s = (Summary)arr.get(3);
			summary.setSummary_success_status(str);
			
			query = "select count(*) from ecarddb.dbo.e_tmcrequest where merchant_type = '6011' and trans_date between('"+start_dt+"') and ('"+end_dt+"') and (response_code not in ('00') or response_code = null)";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			//s = (Summary)arr.get(3);
			summary.setSummary_failed_status(str);
			arr.add(summary);
			
			closeConnectionETMC(con, result);
			
			
			con = connectToECard();
			stat = con.createStatement();
			//VTU
			query = "select count(*) from ecarddb.dbo.vtu_log where txn_date_time between('"+start_dt+"') and ('"+end_dt+"')";
			result = stat.executeQuery(query);
			while(result.next())
			{
				summary = new Summary();
				summary.setSummary_count(""+result.getObject(1));
				summary.setSummary_name("VTU");
			}
			
			query = "select count(*) from ecarddb.dbo.vtu_log where txn_date_time between('"+start_dt+"') and ('"+end_dt+"') and status_id in('0')";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			summary.setSummary_success_status(str);
			
			query = "select count(*) from ecarddb.dbo.vtu_log where txn_date_time between('"+start_dt+"') and ('"+end_dt+"') and (status_id not in('0') or status_id = null)";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			summary.setSummary_failed_status(str);
			arr.add(summary);
			
			closeConnectionECard(con, result, result);
			
			
			con = connectToTelco();
			stat = con.createStatement();
			//VTU II
			query = "select count(*) from telcodb..t_provider_log where trans_date between('"+start_dt+"') and ('"+end_dt+"')";
			result = stat.executeQuery(query);
			while(result.next())
			{
				summary = new Summary();
				summary.setSummary_count(""+result.getObject(1));
				summary.setSummary_name("VTU - VERSION II");
			}
			
			query = "select count(*) from telcodb..t_provider_log where trans_date between('"+start_dt+"') and ('"+end_dt+"') and response_code in('0')";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			summary.setSummary_success_status(str);
			
			query = "select count(*) from telcodb..t_provider_log where trans_date between('"+start_dt+"') and ('"+end_dt+"') and (response_code not in('0') or response_code = null)";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			summary.setSummary_failed_status(str);
			arr.add(summary);
			
			closeConnectionTelco(con, result);
			
			
			
			con = connectMobileDB();
			stat = con.createStatement();
			
			//Version II
			query = "select count(*) from m_outgoing_messages where created between('"+start_dt+"') and ('"+end_dt+"') ";
			result = stat.executeQuery(query);
			while(result.next())
			{
				summary = new Summary();
				summary.setSummary_count(""+result.getObject(1));
				summary.setSummary_name("MOBILE - VERSION II");
			}
			
			query = "select count(*) from m_outgoing_messages where response_code in (0,1000,1001,1002,1003,1004,1005) and created between('"+start_dt+"') and ('"+end_dt+"') ";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			summary.setSummary_success_status(str);
			
			query = "select count(*) from m_outgoing_messages where response_code not in (0,1000,1001,1002,1003,1004,1005) and created between('"+start_dt+"') and ('"+end_dt+"') ";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			summary.setSummary_failed_status(str);
			arr.add(summary);
			
			closeConnectionMobileDB(con, result);
			
			/*con = connectToTelco();
			stat = con.createStatement();
			//Bill Payment
			query = "select count(*) from ecarddb.dbo.vtu_log where txn_date_time between('"+start_dt+"') and ('"+end_dt+"')";
			query = "select count(*) from telcodb..t_paytrans where trans_date between('"+start_dt+"') and ('"+end_dt+"') and merchant_code in ('0690010012','2140010003','0326000002','0320010088','0582280012','0582280016')";
			result = stat.executeQuery(query);
			while(result.next())
			{
				summary = new Summary();
				summary.setSummary_count(""+result.getObject(1));
				summary.setSummary_name("Bill Payment");
			}
			
			query = "select count(*) from telcodb..t_paytrans where trans_date between('"+start_dt+"') and ('"+end_dt+"') and merchant_code in ('0690010012','2140010003','0326000002','0320010088','0582280012','0582280016')";
			
			query = "select count(*) from ecarddb.dbo.vtu_log where txn_date_time between('"+start_dt+"') and ('"+end_dt+"') and status_id in('0')";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			summary.setSummary_success_status(str);
			
			query = "select count(*) from ecarddb.dbo.vtu_log where txn_date_time between('"+start_dt+"') and ('"+end_dt+"') and (status_id not in('0') or status_id = null)";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = ""+result.getObject(1);
			}
			summary.setSummary_failed_status(str);
			arr.add(summary);
			
			closeConnectionTelco(con, result);*/
				
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
			closeConnectionETMC(con, result);
			closeConnectionMobileDB(con, result);
			closeConnectionTelco(con, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
			closeConnectionETMC(con, result);
			closeConnectionMobileDB(con, result);
			closeConnectionTelco(con, result);
		}
		return arr;
	}
	
	
	/*This method does the search on failed transactions*/
	public ArrayList getFailedSummary(String channel, String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Summary summary;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			if(channel.equals("WEB"))//web
			{
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select count(*)as total_count,response_code,(select error_desc from ecarddb.dbo.e_autoswitch_error where ecarddb.dbo.e_autoswitch_error.error_code = ecarddb.dbo.e_requestlog.response_code) as response_desc from ecarddb.dbo.e_requestlog where RESPONSE_CODE <> '0' and substring(transid,1,2) = '01' and trans_date between('"+start_dt+"') and ('"+end_dt+"') group by RESPONSE_CODE having count(*) > 0";
				result = stat.executeQuery(query);
				while(result.next())
				{
					summary = new Summary();
					summary.setSummary_name("WEB");
					summary.setSummary_count(""+result.getObject(1));
					summary.setSummary_failed_status(""+result.getObject(2));
					summary.setSummary_pending_status(""+result.getObject(3));
					arr.add(summary);
				}
				closeConnectionTelco(con, result);	
			}
			else if(channel.equals("Mobile"))//mobile
			{
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select count(*)as total_count,response_code,(select error_desc from ecarddb.dbo.e_autoswitch_error where ecarddb.dbo.e_autoswitch_error.error_code = convert(varchar(2),ecarddb.dbo.e_smslog.response_code)) as response_desc from ecarddb.dbo.e_smslog where RESPONSE_CODE <> 0 and message_date between('"+start_dt+"') and ('"+end_dt+"') group by RESPONSE_CODE having count(*) > 0";
				result = stat.executeQuery(query);
				while(result.next())
				{
					summary = new Summary();
					summary.setSummary_name("MOBILE");
					summary.setSummary_count(""+result.getObject(1));
					summary.setSummary_failed_status(""+result.getObject(2));
					summary.setSummary_pending_status(""+result.getObject(3));
					arr.add(summary);
				}
				closeConnectionTelco(con, result);	
			}
			else if(channel.equals("POS"))//pos
			{
				con = connectToETMC();
				stat = con.createStatement();
				
				query = "select count(*) as total_count,response_code,(select error_descr from ecarddb.dbo.e_isoerror where ecarddb.dbo.e_isoerror.error_code = ecarddb.dbo.e_tmcrequest.response_code) as response_desc from ecarddb.dbo.e_tmcrequest where (response_code not in ('00') or response_code = null) and merchant_type = '6012' and trans_date between('"+start_dt+"') and ('"+end_dt+"') group by RESPONSE_CODE having count(*) > 0";
				result = stat.executeQuery(query);
				while(result.next())
				{
					summary = new Summary();
					summary.setSummary_name("POS");
					summary.setSummary_count(""+result.getObject(1));
					summary.setSummary_failed_status(""+result.getObject(2));
					summary.setSummary_pending_status(""+result.getObject(3));
					arr.add(summary);
				}
				closeConnectionETMC(con, result);	
			}
			else if(channel.equals("ATM"))//atm
			{
				con = connectToETMC();
				stat = con.createStatement();
				
				query = "select count(*) as total_count,response_code,(select error_descr from ecarddb.dbo.e_isoerror where ecarddb.dbo.e_isoerror.error_code = ecarddb.dbo.e_tmcrequest.response_code) as response_desc from ecarddb.dbo.e_tmcrequest where (response_code not in ('00') or response_code = null) and merchant_type = '6011' and trans_date between('"+start_dt+"') and ('"+end_dt+"') group by RESPONSE_CODE having count(*) > 0";
				result = stat.executeQuery(query);
				while(result.next())
				{
					summary = new Summary();
					summary.setSummary_name("ATM");
					summary.setSummary_count(""+result.getObject(1));
					summary.setSummary_failed_status(""+result.getObject(2));
					summary.setSummary_pending_status(""+result.getObject(3));
					arr.add(summary);
				}
				closeConnectionETMC(con, result);	
			}
			else if(channel.equals("VTU"))//vtu
			{
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select count(*)as total_count,status_id,(select error_desc from e_autoswitch_error where e_autoswitch_error.error_code = vtu_log.status_id) as response_desc from vtu_log where status_id <> '0' and txn_date_time between('"+start_dt+"') and ('"+end_dt+"') group by status_id having count(*) > 0";
				System.out.println("query vtu log " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					summary = new Summary();
					summary.setSummary_name("VTU");
					summary.setSummary_count(""+result.getObject(1));
					summary.setSummary_failed_status(""+result.getObject(2));
					summary.setSummary_pending_status(""+result.getObject(3));
					arr.add(summary);
				}
				closeConnectionECard(con, result, result);
			}
			else if(channel.equals("VTU - VERSION II"))//vtu II
			{
				con = connectToTelco();
				stat = con.createStatement();
				
				query = "select count(*), response_code from t_provider_log where response_code not in('0') and trans_date between('"+start_dt+"') and ('"+end_dt+"') group by response_code having count(*) > 0";
				System.out.println("query vtu II log " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					summary = new Summary();
					summary.setSummary_name("VTU - VERSION II");
					summary.setSummary_count(""+result.getObject(1));
					summary.setSummary_failed_status(""+result.getObject(2));
					summary.setSummary_pending_status("NA");
					arr.add(summary);
				}
				closeConnectionTelco(con, result);
			}
			else if(channel.equals("MOBILE - VERSION II"))//version II
			{
				con = connectMobileDB();
				stat = con.createStatement();
				
				query = "select count(*)as total_count, response_code, (select ecarddb..e_autoswitch_error.error_desc from ecarddb..e_autoswitch_error where ecarddb..e_autoswitch_error.error_code =  convert(varchar(2), mobiledb..m_outgoing_messages.response_code)) from m_outgoing_messages where response_code not in (0,1000,1001,1002,1003,1004,1005) and created between('"+start_dt+"') and ('"+end_dt+"') group by response_code having count(*) > 0  ";
				System.out.println("query version II log " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					summary = new Summary();
					summary.setSummary_name("MOBILE - VERSION II");
					summary.setSummary_count(""+result.getObject(1));
					summary.setSummary_failed_status(""+result.getObject(2));
					summary.setSummary_pending_status(""+result.getObject(3));
					arr.add(summary);
				}
				closeConnectionETMC(con, result);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
			closeConnectionETMC(con, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
			closeConnectionETMC(con, result);
		}
		return arr;
	}
	
	
	/*This method gets the vtu transactions that needs to re-process*/
	public ArrayList getVTUForReprocessing(String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Summary summary;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			con = connectToTelco();
			stat = con.createStatement();
			
			query = "select response_code, count(response_code) from t_provider_log" +
					" where trans_date between ('"+start_dt+"') and ('"+end_dt+"') and attempts = 2" +
					" and initial_response_code is not null and response_code <> '0'  group by response_code";
			
			System.out.println("query getVTUForReprocessing " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				summary = new Summary();
				summary.setSummary_failed_status(""+result.getObject(1));
				summary.setSummary_count(""+result.getObject(2));
				arr.add(summary);
			}			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
		}
		return arr;
		
	}
	
	/*This method does the re-processing on failed transactions*/
	public String processFailedVTUTransaction(String start_dt, String end_dt, String response_code)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Summary summary;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int output = -1;
		String message = "";
		
		try
		{
			
			con = connectToTelcoUpdate();
			stat = con.createStatement();
			
			if(response_code.equals("TMT"))
			{
				query = "update t_provider_log set attempts = 1 where" +
				" trans_date between('"+start_dt+"') and ('"+end_dt+"') and response_code = '"+response_code+"' and attempts = 2" +
				" and initial_response_code is not null";
			}
			else if(response_code.equals("DUP"))
			{
				//nothing yet
			}
			else
			{
				query = "update t_provider_log set attempts = 1, response_code = 'REP' where " +
				" trans_date between('"+start_dt+"') and ('"+end_dt+"') and response_code = '"+response_code+"' and attempts = 2" +
				" and initial_response_code is not null";
			}
			
			
			System.out.println("process query " + query);
			output = stat.executeUpdate(query);
			if(output > 0)
			{
				con.commit();
				message = "Records processed successfullly";
			}	
			else
			{
				con.rollback();
				message = "Records not processed";
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			message = "Serevr Error " + ex.getMessage();
			try
			{
				con.rollback();
			}catch(Exception e){}
			closeConnectionTelcoUpdate(con, result);
		}
		finally
		{
			closeConnectionTelcoUpdate(con, result);
		}
		return message;
		
	}
	
	
	/*This method does the processing of payment for international merchants*/
	public String processPaymentAdvice(String merchant_code, String card_num, String amount)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Summary summary;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int output = -1;
		String message = "";
		
		try
		{
			
			con = connectToECard();
			stat = con.createStatement();
			
			query = "insert into e_transaction() values()";
			
			System.out.println("process query " + query);
			output = stat.executeUpdate(query);
			if(output > 0)
			{
				con.commit();
				message = "Records processed successfullly";
			}	
			else
			{
				con.rollback();
				message = "Records not processed";
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			message = "Serevr Error " + ex.getMessage();
			try
			{
				con.rollback();
			}catch(Exception e){}
			closeConnectionTelcoUpdate(con, result);
		}
		finally
		{
			closeConnectionTelcoUpdate(con, result);
		}
		return message;
		
	}
	
	
	/*This displays the failed transactions when a particular error code is clicked upon*/
	public ArrayList getFailedTransactions(String channel, String start_dt, String end_dt, String respond_code)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		REQUEST_LOG request_log;
		SMS_LOG sms_log;
		E_TMCREQUEST tmc_request;
		VTU_LOG vtu_log;
		M_Outgoing_Messages outgoing_messages;
		ProviderLog proLog;
		
		String start_message = "";
		String hide_message = "";
		String end_message = "";
		
		String start_pan = "";
		String end_pan = "";
		String pan = "";
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			//System.out.println("response_code " + respond_code);
			int ch = Integer.parseInt(channel);
			switch(ch)
			{
				case 1:
					
					con = connectToECard();
					stat = con.createStatement();
					
					query = "select TRANSID,CARD_NUM,TRANS_DATE,TRANS_AMOUNT,TRANS_CODE," +
					"(select merchant_name from ecarddb.dbo.e_merchant where MERCHANT_CODE = ecarddb.dbo.e_requestlog.MERCHANT_CODE)," +
					"(select error_desc from ecarddb.dbo.e_autoswitch_error where error_code = ecarddb.dbo.e_requestlog.response_code),RESPONSE_TIME,TRANS_DESCR,client_id," +
					"REQUEST_ID,FEE,CURRENCY,datediff(ss,response_time,trans_date)" +
					" from ecarddb.dbo.e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"') and substring(transid,1,2) = '01' and response_code = '"+respond_code+"' order by trans_date desc";
					
					result = stat.executeQuery(query);
					while(result.next())
					{
					
						request_log = new REQUEST_LOG();
						
						request_log.setTransid(""+result.getObject(1));
						request_log.setCard_num(""+result.getObject(2));
						request_log.setTrans_date(""+result.getObject(3));
						request_log.setTrans_amount(""+result.getObject(4));
						request_log.setTrans_code(""+result.getObject(5));
						
						String s = ""+result.getObject(6);
						if(s.equals("")||s.equals("null"))
							s = "";
						request_log.setMerchant_desc(s);
						
						request_log.setResponse_code(""+result.getObject(7));
						request_log.setResponse_time(""+result.getObject(8));
						request_log.setTrans_descr(""+result.getObject(9));
						request_log.setClient_id(""+result.getObject(10));
						request_log.setRequest_id(""+result.getObject(11));
						request_log.setFee(""+result.getObject(12));
						request_log.setCurrency(""+result.getObject(13));
						
						s = ""+result.getObject(14);
						if(s.equals("")||s.equals("null"))
							s = "";
						
						request_log.setResponse_time_in_secs(s);
						
						
						
						arr.add(request_log);
					}
					
					closeConnectionECard(con, result, result);	
					
					
					break;
				
				case 2:
					
					con = connectToECard();
					stat = con.createStatement();
					
					query = "select UNIQUE_TRANSID,MOBILE,MESSAGE_DATE," +
							"SOURCE_CARD,TRANS_TYPE,RESPONSE_MESSAGE," +
							"(select error_desc from ecarddb.dbo.e_autoswitch_error where ecarddb.dbo.e_autoswitch_error.error_code = convert(varchar(2),ecarddb.dbo.e_smslog.response_code)),RESPONSE_TIME,ISSUER," +
							"datediff(ss,message_date,response_time) from ecarddb.dbo.e_smslog where RESPONSE_CODE ="+respond_code+" and message_date between('"+start_dt+"') and ('"+end_dt+"') order by message_date desc";
					result = stat.executeQuery(query);
					
					while(result.next())
					{
						sms_log = new SMS_LOG();
						
						sms_log.setUnique_transid(""+result.getObject(1));
						sms_log.setMobile(""+result.getObject(2));
						sms_log.setMessage_date(""+result.getObject(3));
						sms_log.setSource_card(""+result.getObject(4));
						sms_log.setTrans_type(""+result.getObject(5));
						
						String response_message = ""+result.getObject(6);
						
						try
						{
							start_message = response_message.substring(0,response_message.indexOf("PIN:"));
							hide_message = response_message.substring(response_message.indexOf("PIN:"),response_message.indexOf("Ref:"));
							end_message = response_message.substring(response_message.indexOf("Ref:"));
							
							response_message = start_message + "PIN: ############### " + end_message;
						}
						catch(Exception e)
						{
							//System.out.println("cant convert pin to hash");
						}
						sms_log.setResponse_message(response_message);
						
						
						sms_log.setResponse_code(""+result.getObject(7));
						sms_log.setResponse_time(""+result.getObject(8));
						sms_log.setIssuer(""+result.getObject(9));
						
						String s = ""+result.getObject(10);
						if(s.equals("")||s.equals("null"))
							s = "";
						
						sms_log.setResponse_time_in_secs(s);
						
						arr.add(sms_log);
					}
					
					closeConnectionECard(con, result, result);
					
					break;
				
					
				case 3://POS
					
					con = connectToETMC();
					stat = con.createStatement();
					
					if(respond_code.equals("null"))
						query = "select mti,pan,(select pro_code_desc from ecarddb.dbo.e_tmccode where ecarddb.dbo.e_tmccode.pro_code =  substring(ecarddb.dbo.e_tmcrequest.pro_code,1,2)), card_acc_name, terminal_id, (select aquirer_name from ecarddb.dbo.e_tmcaquirer where ecarddb.dbo.e_tmcaquirer.aqid =  ecarddb.dbo.e_tmcrequest.aqid), trans_date, datediff(ss,trans_date,response_time), amount,(select ecarddb.dbo.e_isoerror.error_descr from ecarddb.dbo.e_isoerror where error_code = ecarddb.dbo.e_tmcrequest.response_code) from ecarddb.dbo.e_tmcrequest where trans_date between('"+start_dt+"') and ('"+end_dt+"') and merchant_type='6012' and response_code = "+respond_code+" order by trans_date desc";
					else
						query = "select mti,pan,(select pro_code_desc from ecarddb.dbo.e_tmccode where ecarddb.dbo.e_tmccode.pro_code =  substring(ecarddb.dbo.e_tmcrequest.pro_code,1,2)), card_acc_name, terminal_id, (select aquirer_name from ecarddb.dbo.e_tmcaquirer where ecarddb.dbo.e_tmcaquirer.aqid =  ecarddb.dbo.e_tmcrequest.aqid), trans_date, datediff(ss,trans_date,response_time), amount,(select ecarddb.dbo.e_isoerror.error_descr from ecarddb.dbo.e_isoerror where error_code = ecarddb.dbo.e_tmcrequest.response_code) from ecarddb.dbo.e_tmcrequest where trans_date between('"+start_dt+"') and ('"+end_dt+"') and merchant_type='6012' and response_code = '"+respond_code+"' order by trans_date desc";
					
					result = stat.executeQuery(query);
					while(result.next())
					{
						tmc_request = new E_TMCREQUEST();
						
						tmc_request.setMti(""+result.getObject(1));
					
						try
						{
							pan = ""+result.getObject(2);
							start_pan = pan.substring(0,9);
							end_pan = pan.substring(pan.length()-4);
						}
						catch(Exception e){}
						
						
						tmc_request.setPan(start_pan + "#########" + end_pan);
						
						tmc_request.setPro_code(""+result.getObject(3));
						tmc_request.setCard_acc_name(""+result.getObject(4));
						tmc_request.setTerminal_id(""+result.getObject(5));
						
						String s = ""+result.getObject(6);
						if(s.equals("")||s.equals("null"))
							s = "";
						tmc_request.setAqid(s);
						
						tmc_request.setTrans_date(""+result.getObject(7));
						
						s = ""+result.getObject(8);
						if(s.equals("")||s.equals("null"))
							s = "";
							
						tmc_request.setResponse_time_in_secs(s);
						tmc_request.setAmount(""+result.getObject(9));
						
						s = ""+result.getObject(10);
						if(s.equals("")||s.equals("null"))
							s = "";
						
						tmc_request.setResponse_code(s);
						
						
						arr.add(tmc_request);
					}
					
					closeConnectionETMC(con, result);
					
					break;
					
				case 4://ATM
					
					con = connectToETMC();
					stat = con.createStatement();
					
					//query = "select mti,pan,(select pro_code_desc from ecarddb.dbo.e_tmccode where ecarddb.dbo.e_tmccode.pro_code =  substring(ecarddb.dbo.e_tmcrequest.pro_code,1,2)), card_acc_name, terminal_id, (select aquirer_name from ecarddb.dbo.e_tmcaquirer where ecarddb.dbo.e_tmcaquirer.aqid =  ecarddb.dbo.e_tmcrequest.aqid), trans_date, datediff(ss,trans_date,response_time), amount,(select ecarddb.dbo.e_isoerror.error_descr from ecarddb.dbo.e_isoerror where error_code = ecarddb.dbo.e_tmcrequest.response_code) from ecarddb.dbo.e_tmcrequest where trans_date between('"+start_dt+"') and ('"+end_dt+"') and merchant_type='6011' and response_code = '"+respond_code+"' ";
					
					if(respond_code.equals("null"))
						query = "select mti,pan,(select pro_code_desc from ecarddb.dbo.e_tmccode where ecarddb.dbo.e_tmccode.pro_code =  substring(ecarddb.dbo.e_tmcrequest.pro_code,1,2)), card_acc_name, terminal_id, (select aquirer_name from ecarddb.dbo.e_tmcaquirer where ecarddb.dbo.e_tmcaquirer.aqid =  ecarddb.dbo.e_tmcrequest.aqid), trans_date, datediff(ss,trans_date,response_time), amount,response_code from ecarddb.dbo.e_tmcrequest where trans_date between('"+start_dt+"') and ('"+end_dt+"') and merchant_type='6011' and response_code = "+respond_code+" order by trans_date desc";
					else
						query = "select mti,pan,(select pro_code_desc from ecarddb.dbo.e_tmccode where ecarddb.dbo.e_tmccode.pro_code =  substring(ecarddb.dbo.e_tmcrequest.pro_code,1,2)), card_acc_name, terminal_id, (select aquirer_name from ecarddb.dbo.e_tmcaquirer where ecarddb.dbo.e_tmcaquirer.aqid =  ecarddb.dbo.e_tmcrequest.aqid), trans_date, datediff(ss,trans_date,response_time), amount,response_code from ecarddb.dbo.e_tmcrequest where trans_date between('"+start_dt+"') and ('"+end_dt+"') and merchant_type='6011' and response_code = '"+respond_code+"' order by trans_date desc";
					
					
					//System.out.println("query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						tmc_request = new E_TMCREQUEST();
						
						tmc_request.setMti(""+result.getObject(1));
						
						try
						{
							pan = ""+result.getObject(2);
							start_pan = pan.substring(0,9);
							end_pan = pan.substring(pan.length()-4);
						}
						catch(Exception e){}
						
						
						tmc_request.setPan(start_pan + "#########" + end_pan);
						
						tmc_request.setPro_code(""+result.getObject(3));
						tmc_request.setCard_acc_name(""+result.getObject(4));
						tmc_request.setTerminal_id(""+result.getObject(5));
						
						String s = ""+result.getObject(6);
						if(s.equals("")||s.equals("null"))
							s = "";
						tmc_request.setAqid(s);
						
						tmc_request.setTrans_date(""+result.getObject(7));
						
						s = ""+result.getObject(8);
						if(s.equals("")||s.equals("null"))
							s = "";
							
						tmc_request.setResponse_time_in_secs(s);
						tmc_request.setAmount(""+result.getObject(9));
						
						s = ""+result.getObject(10);
						if(s.equals("")||s.equals("null"))
							s = "";
						
						tmc_request.setResponse_code(s);
						
						
						arr.add(tmc_request);
					}
					
					closeConnectionETMC(con, result);
					
					break;
					
				case 5:
					
					con = connectToECard();
					stat = con.createStatement();
					
					query = "select amount, orig_msisdn, dest_msisdn, line_type, txn_date_time, response_code, response_message," +
							" mobile, response_date_time, voucher_type,status_id,id, unique_trans_id" +
							" from ecarddb.dbo.vtu_log where status_id = '"+respond_code+"' " +
							" and txn_date_time between('"+start_dt+"') and ('"+end_dt+"') order by txn_date_time desc";
					
					System.out.println("vtu failed query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						vtu_log = new VTU_LOG();
						vtu_log.setAmount(""+result.getObject(1));
						vtu_log.setOrig_msisdn(""+result.getObject(2));
						vtu_log.setDest_msisdn(""+result.getObject(3));
						vtu_log.setLine_type(""+result.getObject(4));
						vtu_log.setTxn_date_time(""+result.getObject(5));
						vtu_log.setResponse_code(""+result.getObject(6));
						vtu_log.setResponse_message(""+result.getObject(7));
						vtu_log.setMobile(""+result.getObject(8));
						vtu_log.setResponse_date(""+result.getObject(9));
						vtu_log.setVoucher_type(""+result.getObject(10));
						vtu_log.setStatus_id(""+result.getObject(11));
						vtu_log.setId(""+result.getObject(12));
						vtu_log.setUnique_trans_id(""+result.getObject(13));
						
						arr.add(vtu_log);
					}
					
					closeConnectionECard(con, result, result);
					
					break;
					
					
				case 6:
					
					con = connectMobileDB();
					stat = con.createStatement();
					
					query = "select id , message_id, appid, response_code, response_message, mobile_no, sender_id, token, created " +
					"from m_outgoing_messages where response_code =  "+Integer.parseInt(respond_code)+" and created between('"+start_dt+"') and ('"+end_dt+"') ";
		
					
					System.out.println("version II query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{						
						outgoing_messages = new M_Outgoing_Messages();
						
						outgoing_messages.setId(""+result.getObject(1));
						outgoing_messages.setMessage_id(""+result.getObject(2));
						outgoing_messages.setAppid(""+result.getObject(3));
						outgoing_messages.setResponse_code(""+result.getObject(4));
						outgoing_messages.setResponse_message(""+result.getObject(5));
						outgoing_messages.setMobile_no(""+result.getObject(6));
						outgoing_messages.setSender_id(""+result.getObject(7));
						outgoing_messages.setToken(""+result.getObject(8));
						outgoing_messages.setCreated(""+result.getObject(9));
						
						arr.add(outgoing_messages);
							
					}	
					
					closeConnectionMobileDB(con, result);
					
					break;
				
				case 7:
					
					con = connectToTelco();
					stat = con.createStatement();
					
					query = "select sequence, unique_transid, amount, source_account, dest_account, linetype, dest_balance," +
					" trans_date, response_code, response_message, source , response_date, original_amount," +
					" provider,voucher_type,original_transid,alias,attempts,initial_response_code" +
					" from t_provider_log where" +
					" trans_date between('"+start_dt+"') and ('"+end_dt+"') " +
					" and response_code =  '"+ respond_code +"'  order by trans_date desc";
					
					
					System.out.println("VTU version II query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{						
						proLog = new ProviderLog();
						proLog.setSequence(""+result.getObject(1));
						proLog.setUniqueTransid(""+result.getObject(2));
						proLog.setAmount(""+result.getObject(3));
						proLog.setSourceAccount(""+result.getObject(4));
						proLog.setDestAccount(""+result.getObject(5));
						proLog.setLineType(""+result.getObject(6));
						proLog.setDestBalance(""+result.getObject(7));
						proLog.setTransDate(""+result.getObject(8));
						proLog.setResponseCode(""+result.getObject(9));
						proLog.setResponseMessage(""+result.getObject(10));
						proLog.setSource(""+result.getObject(11));
						proLog.setResponseDate(""+result.getObject(12));
						proLog.setOriginalAmount(""+result.getObject(13));
						proLog.setProvider(""+result.getObject(14));
						proLog.setVoucherType(""+result.getObject(15));
						proLog.setOriginalTransid(""+result.getObject(16));
						proLog.setAlias(""+result.getObject(17));
						proLog.setAttempts(""+result.getObject(18));
						proLog.setInitialResponsecode(""+result.getObject(19));
						arr.add(proLog);
							
					}	
					
					closeConnectionTelco(con, result);
					
					break;
					
					
				default:
					break;
			}		
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
			closeConnectionETMC(con, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
			closeConnectionETMC(con, result);
		}
		return arr;
	}
	
	
	
	
	public ArrayList getVtuLog(String source_frm, String dest, String start_dt, String end_dt,
			String status, String table_location, String orig_msisdn, String versionType, String uniquetransid)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		VTU_LOG vtu_log;
		ProviderLog proLog;
		int counter = 0;
		Connection con = null, con1 = null;
		Statement stat = null, stat1 = null;
		ResultSet result = null, result1 = null;
		String strsource = "";
		String strdest = "";
		String strorig = "";
		String strunique = "";
		
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			con1 = connectToTelco();
			stat1 = con1.createStatement();
			
			
			String str_source_frm = source_frm.trim();
			/*if(str_source_frm.trim().length()>0)
			{
				if(str_source_frm.startsWith("234"));
				else
				{
					str_source_frm = "234" + str_source_frm.substring(1, str_source_frm.length());
				}
			}*/
			
			
			String str_dest = dest.trim();
			/*if(str_dest.trim().length()>0)
			{
				if(str_dest.startsWith("234"));
				else
				{
					str_dest = "234" + str_dest.substring(1, str_dest.length());
				}
			}*/
			
			
			if(versionType.equals("Version I"))
			{
				if(str_source_frm.trim().length() >0)
				{
					strsource = " and mobile like '"+ str_source_frm + "%' " ;
				}
				
				if(str_dest.trim().length() >0)
				{
					strdest = " and dest_msisdn like '"+ str_dest + "%' " ;
				}
				
				if(orig_msisdn.trim().length() >0)
				{
					strorig = " and orig_msisdn like '"+ orig_msisdn + "%' " ;
				}
				
				if(uniquetransid.trim().length() > 0)
				{
					strunique  = " and unique_trans_id like  '"+uniquetransid+"%' ";
				}
				
				if(table_location.equals("A"))//current table
				{
					if(status.equals("ALL"))
					{
						//query = "select amount, orig_msisdn, dest_msisdn, line_type, txn_date_time, response_code, response_message, mobile, response_date_time, voucher_type,status_id,id,unique_trans_id from ecarddb.dbo.vtu_log where txn_date_time between('"+start_dt+"') and ('"+end_dt+"') mobile like '%"+ str_source_frm + "%' and dest_msisdn like '%"+ str_dest + "%' and orig_msisdn like '"+ orig_msisdn + "%' and  order by txn_date_time desc";
						query = "select amount, orig_msisdn, dest_msisdn, line_type, txn_date_time, response_code, response_message, mobile, response_date_time, voucher_type,status_id,id,unique_trans_id from ecarddb.dbo.vtu_log where txn_date_time between('"+start_dt+"') and ('"+end_dt+"') " +  strsource + " " + strdest + " " + strorig + " " + strunique +  " order by txn_date_time desc";
					}
					else if(status.equals("SUCCESSFUL"))
					{
		
						//query = "select amount, orig_msisdn, dest_msisdn, line_type, txn_date_time, response_code, response_message, mobile, response_date_time, voucher_type,status_id,id,unique_trans_id from ecarddb.dbo.vtu_log where mobile like '%"+ str_source_frm + "%' and dest_msisdn like '%"+ str_dest + "%' and orig_msisdn like '"+ orig_msisdn + "%' and txn_date_time between('"+start_dt+"') and ('"+end_dt+"') and status_id in('0') order by txn_date_time desc";
						query = "select amount, orig_msisdn, dest_msisdn, line_type, txn_date_time, response_code, response_message, mobile, response_date_time, voucher_type,status_id,id,unique_trans_id from ecarddb.dbo.vtu_log where txn_date_time between('"+start_dt+"') and ('"+end_dt+"') " +  strsource + " " + strdest + " " + strorig + " " + strunique + "  and status_id in('0') order by txn_date_time desc";
					}
					else if(status.equals("FAILED"))
					{

						//query = "select amount, orig_msisdn, dest_msisdn, line_type, txn_date_time, response_code, response_message, mobile, response_date_time, voucher_type,status_id,id,unique_trans_id from ecarddb.dbo.vtu_log where mobile like '%"+ str_source_frm + "%' and dest_msisdn like '%"+ str_dest + "%' and orig_msisdn like '"+ orig_msisdn + "%' and txn_date_time between('"+start_dt+"') and ('"+end_dt+"') and (status_id not in('0') or status_id = null) order by txn_date_time desc";
						query = "select amount, orig_msisdn, dest_msisdn, line_type, txn_date_time, response_code, response_message, mobile, response_date_time, voucher_type,status_id,id,unique_trans_id from ecarddb.dbo.vtu_log where txn_date_time between('"+start_dt+"') and ('"+end_dt+"') " +  strsource + " " + strdest + " " + strorig + " " + strunique +  "  and (status_id not in('0') or status_id = null) order by txn_date_time desc";
					}
				}
				else//back up table
				{
					if(status.equals("ALL"))
					{
						//query = "select amount, orig_msisdn, dest_msisdn, line_type, txn_date_time, response_code, response_message, mobile, response_date_time, voucher_type,status_id,id,unique_trans_id from ecarddb.dbo.vtu_logbk where mobile like '%"+ str_source_frm + "%' and dest_msisdn like '%"+ str_dest + "%' and orig_msisdn like '"+ orig_msisdn + "%' and txn_date_time between('"+start_dt+"') and ('"+end_dt+"') order by txn_date_time desc";
						query = "select amount, orig_msisdn, dest_msisdn, line_type, txn_date_time, response_code, response_message, mobile, response_date_time, voucher_type,status_id,id,unique_trans_id from ecarddb.dbo.vtu_logbk where txn_date_time between('"+start_dt+"') and ('"+end_dt+"') " +  strsource + " " + strdest + " " + strorig + " " + strunique +  " order by txn_date_time desc";
					}
					else if(status.equals("SUCCESSFUL"))
					{
						//query = "select amount, orig_msisdn, dest_msisdn, line_type, txn_date_time, response_code, response_message, mobile, response_date_time, voucher_type,status_id,id,unique_trans_id from ecarddb.dbo.vtu_logbk where mobile like '%"+ str_source_frm + "%' and dest_msisdn like '%"+ str_dest + "%' and orig_msisdn like '"+ orig_msisdn + "%' and txn_date_time between('"+start_dt+"') and ('"+end_dt+"') and status_id in('0') order by txn_date_time desc";
						query = "select amount, orig_msisdn, dest_msisdn, line_type, txn_date_time, response_code, response_message, mobile, response_date_time, voucher_type,status_id,id,unique_trans_id from ecarddb.dbo.vtu_logbk where txn_date_time between('"+start_dt+"') and ('"+end_dt+"') " +  strsource + " " + strdest + " " + strorig + " " + strunique +  "  and status_id in('0') order by txn_date_time desc";
					}
					else if(status.equals("FAILED"))
					{
						//query = "select amount, orig_msisdn, dest_msisdn, line_type, txn_date_time, response_code, response_message, mobile, response_date_time, voucher_type,status_id,id,unique_trans_id from ecarddb.dbo.vtu_logbk where mobile like '%"+ str_source_frm + "%' and dest_msisdn like '%"+ str_dest + "%' and orig_msisdn like '"+ orig_msisdn + "%' and txn_date_time between('"+start_dt+"') and ('"+end_dt+"') and (status_id not in('0') or status_id = null) order by txn_date_time desc";
						query = "select amount, orig_msisdn, dest_msisdn, line_type, txn_date_time, response_code, response_message, mobile, response_date_time, voucher_type,status_id,id,unique_trans_id from ecarddb.dbo.vtu_logbk where txn_date_time between('"+start_dt+"') and ('"+end_dt+"') " +  strsource + " " + strdest + " " + strorig + " " + strunique +  "  and (status_id not in('0') or status_id = null) order by txn_date_time desc";
					}
				}
				
				
				
				System.out.println("vtu query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					counter++;
					vtu_log = new VTU_LOG();
					vtu_log.setCounter(""+counter);
					vtu_log.setAmount(""+result.getObject(1));
					vtu_log.setOrig_msisdn(""+result.getObject(2));
					vtu_log.setDest_msisdn(""+result.getObject(3));
					vtu_log.setLine_type(""+result.getObject(4));
					vtu_log.setTxn_date_time(""+result.getObject(5));
					vtu_log.setResponse_code(""+result.getObject(6));
					vtu_log.setResponse_message(""+result.getObject(7));
					vtu_log.setMobile(""+result.getObject(8));
					vtu_log.setResponse_date(""+result.getObject(9));
					vtu_log.setVoucher_type(""+result.getObject(10));
					vtu_log.setStatus_id(""+result.getObject(11));
					vtu_log.setId(""+result.getObject(12));
					vtu_log.setUnique_trans_id(""+result.getObject(13));

					arr.add(vtu_log);
				}
			}
			else if(versionType.equals("Version II"))
			{
				if(str_source_frm.trim().length() >0)
				{
					strsource = " and source like '"+ str_source_frm + "%' " ;
				}
				
				if(str_dest.trim().length() >0)
				{
					strdest = " and dest_account like '"+ str_dest + "%' " ;
				}
				
				if(orig_msisdn.trim().length() >0)
				{
					strorig = " and source_account like '"+ orig_msisdn + "%' " ;
				}
				
				if(uniquetransid.trim().length() > 0)
				{
					strunique  = " and unique_transid like  '"+uniquetransid+"%' ";
				}
				
				if(status.equals("ALL"))
				{
					
					query = "select sequence, unique_transid, amount, source_account, dest_account, linetype, dest_balance," +
					" trans_date, response_code, response_message, source , response_date, original_amount," +
					" provider,voucher_type,original_transid,alias,attempts,initial_response_code" +
					" from t_provider_log where" +
					" trans_date between('"+start_dt+"') and ('"+end_dt+"') " +
					" " +  strsource + " " + strdest + " " + strorig + " " + strunique +  " order by trans_date desc";
				}
				else if(status.equals("SUCCESSFUL"))
				{
					query = "select sequence, unique_transid, amount, source_account, dest_account, linetype, dest_balance," +
					" trans_date, response_code, response_message, source , response_date, original_amount," +
					" provider,voucher_type,original_transid,alias,attempts,initial_response_code" +
					" from t_provider_log where" +
					" trans_date between('"+start_dt+"') and ('"+end_dt+"') " +
					" " +  strsource + " " + strdest + " " + strorig + " " + strunique +  " and response_code in('0') order by trans_date desc";
				}
				else if(status.equals("FAILED"))
				{

					query = "select sequence, unique_transid, amount, source_account, dest_account, linetype, dest_balance," +
					" trans_date, response_code, response_message, source , response_date, original_amount," +
					" provider,voucher_type,original_transid,alias,attempts,initial_response_code" +
					" from t_provider_log where" +
					" trans_date between('"+start_dt+"') and ('"+end_dt+"') " +
					" " +  strsource + " " + strdest + " " + strorig + " " + strunique +  " and (response_code not in('0') or response_code = null)" +
					" order by trans_date desc";
					
				}
				
				System.out.println("vtuII query  " + query);
				result1 = stat1.executeQuery(query);
				while(result1.next())
				{
					proLog = new ProviderLog();
					proLog.setSequence(""+result1.getObject(1));
					proLog.setUniqueTransid(""+result1.getObject(2));
					proLog.setAmount(""+result1.getObject(3));
					proLog.setSourceAccount(""+result1.getObject(4));
					proLog.setDestAccount(""+result1.getObject(5));
					proLog.setLineType(""+result1.getObject(6));
					proLog.setDestBalance(""+result1.getObject(7));
					proLog.setTransDate(""+result1.getObject(8));
					proLog.setResponseCode(""+result1.getObject(9));
					proLog.setResponseMessage(""+result1.getObject(10));
					proLog.setSource(""+result1.getObject(11));
					proLog.setResponseDate(""+result1.getObject(12));
					proLog.setOriginalAmount(""+result1.getObject(13));
					proLog.setProvider(""+result1.getObject(14));
					proLog.setVoucherType(""+result1.getObject(15));
					proLog.setOriginalTransid(""+result1.getObject(16));
					proLog.setAlias(""+result1.getObject(17));
					proLog.setAttempts(""+result1.getObject(18));
					proLog.setInitialResponsecode(""+result1.getObject(19));
					arr.add(proLog);
				}
				
			}
			
			
			
				
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
			closeConnectionTelco(con1, result1);
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeConnectionTelco(con1, result1);
		}
		return arr;
	}
	
	
	/*Method for getting bill payment*/
	public ArrayList getPayTrans(String smart_card,String start_dt, String end_dt, String merchant_code, String table_location)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		PAYTRANS pay_trans;
		E_TRANSACTION e_trans;
		String str = "", str1 = "";
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			if(table_location.equals("A"))
			{
				con = connectToTelco();
				stat = con.createStatement();
				
				if(merchant_code.equals("ALL"))
				{
					query = "select subscriber_id, trans_date, process_status, unique_transid, STATUS_DESCRIPTION,mobile_no" +
					" from telcodb..t_paytrans where mobile_no like '"+ smart_card + "%' and" +
					" trans_date between('"+start_dt+"') and ('"+end_dt+"') and" +
					" merchant_code in ('0690010012','2140010003','0326000002','0320010088','0582280012','0582280016','2140300003') order by trans_date desc";
				}
				else
				{
					query = "select subscriber_id, trans_date, process_status, unique_transid, STATUS_DESCRIPTION,mobile_no" +
						" from telcodb..t_paytrans where mobile_no like '"+ smart_card + "%' and" +
						" trans_date between('"+start_dt+"') and ('"+end_dt+"') and" +
						" merchant_code = '"+merchant_code+"' order by trans_date desc";
				}
				
				result = stat.executeQuery(query);
				while(result.next())
				{
					counter++;
					pay_trans = new PAYTRANS();
					pay_trans.setCounter(""+counter);
					pay_trans.setSubscriber_id(""+result.getObject(1));
					pay_trans.setTrans_date(""+result.getObject(2));
					
					str = ""+result.getObject(3);
					if(str.equals("null"))
						str = "";
					else if(str.equals("0"))
						str = "Queued";
					
					else if(str.equals("1"))
						str = "Processed";
					
					pay_trans.setProcess_status(str);
					
					pay_trans.setUnique_trans_id(""+result.getObject(4));
					
					str1 = ""+result.getObject(5);
					
					pay_trans.setStatus_desc(str1);
					pay_trans.setMobile_no(""+result.getObject(6));
					
					if(str1.indexOf("SUCCESSFULLY ")>0)
					{
						pay_trans.setSuccessful_tran("Processed");
					}

					arr.add(pay_trans);
				}	
			}
			else
			{
				con = connectToECard();
				stat = con.createStatement();
				
				if(merchant_code.equals("ALL"))
				{
					query = "select unique_transid, trans_date, trans_descr" +
					" from e_transaction where trans_descr like '"+ smart_card + "%' and" +
					" trans_date between('"+start_dt+"') and ('"+end_dt+"') and" +
					" merchant_code in ('0690010012','2140010003','0326000002','0320010088','0582280012','0582280016') order by trans_date desc";
				}
				else
				{
					query = "select unique_transid, trans_date, trans_descr" +
					" from e_transaction where trans_descr like '"+ smart_card + "%' and" +
					" trans_date between('"+start_dt+"') and ('"+end_dt+"') and" +
					" merchant_code = '"+merchant_code+"' order by trans_date desc";
				}
				
				result = stat.executeQuery(query);
				while(result.next())
				{
					counter++;
					e_trans = new E_TRANSACTION();
					e_trans.setCounter(""+counter);
					e_trans.setUnique_transid(""+result.getObject(1));
					e_trans.setTrans_date(""+result.getObject(2));
					e_trans.setTrans_desc(""+result.getObject(3));
					
					arr.add(e_trans);
				}	
				
				
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	/*Method for getting bill payment for card scheme*/
	public ArrayList getPayTranScheme(String smart_card,String start_dt, String end_dt, String merchant_code, String scheme_card)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		PAYTRANS pay_trans;
		E_TRANSACTION e_trans;
		String str = "", str1 = "";
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			

			con = connectToTelco();
			stat = con.createStatement();
			
			/*if(card_num.length() > 0)
			{
				con1 = connectPocketMoniEcardDB();
				stat1 = con1.createStatement();
				
				query = "select card_num from e_cardholder where track2 = '"+card_num+"' ";
				System.out.println("query " + query);
				result = stat1.executeQuery(query);
				if(result.next())
				{
					card_num = ""+result.getObject(1);
				}
				
			}*/
			
			if(merchant_code.equals("ALL"))
			{
				query = "select subscriber_id, trans_date, process_status, unique_transid, STATUS_DESCRIPTION,mobile_no" +
				" from telcodb..t_paytrans where mobile_no like '"+ smart_card + "%' and" +
				" trans_date between('"+start_dt+"') and ('"+end_dt+"') and" +
				" and card_num like '"+scheme_card+"%'  and " +
				" merchant_code in ('0690010012','2140010003','0326000002','0320010088','0582280012','0582280016','2140300003')" +
				" order by trans_date desc";
			}
			else
			{
				query = "select subscriber_id, trans_date, process_status, unique_transid, STATUS_DESCRIPTION,mobile_no" +
					" from telcodb..t_paytrans where mobile_no like '"+ smart_card + "%' and" +
					" trans_date between('"+start_dt+"') and ('"+end_dt+"') and" +
					" and card_num like '"+scheme_card+"%'  and " +
					" merchant_code = '"+merchant_code+"' order by trans_date desc";
			}
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				pay_trans = new PAYTRANS();
				pay_trans.setCounter(""+counter);
				pay_trans.setSubscriber_id(""+result.getObject(1));
				pay_trans.setTrans_date(""+result.getObject(2));
				
				str = ""+result.getObject(3);
				if(str.equals("null"))
					str = "";
				else if(str.equals("0"))
					str = "Queued";
				
				else if(str.equals("1"))
					str = "Processed";
				
				pay_trans.setProcess_status(str);
				
				pay_trans.setUnique_trans_id(""+result.getObject(4));
				
				str1 = ""+result.getObject(5);
				
				pay_trans.setStatus_desc(str1);
				pay_trans.setMobile_no(""+result.getObject(6));
				
				if(str1.indexOf("SUCCESSFULLY ")>0)
				{
					pay_trans.setSuccessful_tran("Processed");
				}

				arr.add(pay_trans);
			}	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	public ArrayList getBillPaymentSubscriberDetails(String subscriberId, String start_dt, String end_dt, String merchantcode)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		PAYTRANS pay_trans;
		E_TRANSACTION e_trans;
		String str = "", str1 = "";
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		String apostrophe = "'";
		
		try
		{
					 
			if(merchantcode.indexOf(":")>0)
			{
				String m[] = merchantcode.split(":");
				merchantcode = "";
				for(int i=0;i<m.length;i++)
				{
					merchantcode += apostrophe + m[i] + apostrophe + ",";
				}
				
				merchantcode = merchantcode.substring(0, merchantcode.lastIndexOf(","));
			}
			else
			{
				merchantcode = apostrophe + merchantcode + apostrophe;
			}
			
			System.out.println("merchantcode " + merchantcode);
			
			con = connectToTelco();
			stat = con.createStatement();
		 
			query = "select subscriber_id, trans_date, process_status, unique_transid, STATUS_DESCRIPTION, mobile_no," +
					" Trans_Amount, merchant_id, merchant_code, trans_channel " +
					"from telcodb..t_paytrans where merchant_code in("+merchantcode+") and subscriber_id like '"+subscriberId+"%' " +
					" and trans_date between('"+start_dt+"') and ('"+end_dt+"') order by trans_date desc"; 	
			
			System.out.println("query for getBillPaymentSubscriberDetails "+query );
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				pay_trans = new PAYTRANS();
				pay_trans.setCounter(""+counter);
				pay_trans.setSubscriber_id(""+result.getObject(1));
				pay_trans.setTrans_date(""+result.getObject(2));
				pay_trans.setProcess_status(""+result.getObject(3));
				pay_trans.setUnique_trans_id(""+result.getObject(4));
				pay_trans.setStatus_desc(""+result.getObject(5));
				pay_trans.setMobile_no(""+result.getObject(6));
				pay_trans.setTrans_amount(""+result.getObject(7));
				pay_trans.setMerchant_id(""+result.getObject(8));
				pay_trans.setMerchant_code(""+result.getObject(9));
				pay_trans.setTrans_channel(""+result.getObject(10));
				
				arr.add(pay_trans);
			}	
			
		
		}catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Friendship center tranaction*/
	public ArrayList viewFriendshipCenterTranDetials(String startdt, String enddt, String merchantcode)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		PAYTRANS pay_trans;
		E_TRANSACTION e_trans;
		String str = "", str1 = "";
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		String apostrophe = "'";
		
		try
		{
					 
			if(merchantcode.indexOf(":")>0)
			{
				String m[] = merchantcode.split(":");
				merchantcode = "";
				for(int i=0;i<m.length;i++)
				{
					merchantcode += apostrophe + m[i] + apostrophe + ",";
				}
				
				merchantcode = merchantcode.substring(0, merchantcode.lastIndexOf(","));
			}
			else
			{
				merchantcode = apostrophe + merchantcode + apostrophe;
			}
			
			System.out.println("merchantcode " + merchantcode);
			
			con = connectToTelco();
			stat = con.createStatement();
		 
			query = "select subscriber_id, trans_date, process_status, unique_transid, STATUS_DESCRIPTION, mobile_no," +
					" Trans_Amount, merchant_id, merchant_code, trans_channel " +
					" from telcodb..t_paytrans where merchant_code in("+merchantcode+") " +
					" and trans_date between('"+startdt+"') and ('"+enddt+"') order by trans_date desc"; 	
			
			System.out.println("query for getBillPaymentSubscriberDetails "+query );
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				pay_trans = new PAYTRANS();
				pay_trans.setCounter(""+counter);
				pay_trans.setSubscriber_id(""+result.getObject(1));
				pay_trans.setTrans_date(""+result.getObject(2));
				pay_trans.setProcess_status(""+result.getObject(3));
				pay_trans.setUnique_trans_id(""+result.getObject(4));
				pay_trans.setStatus_desc(""+result.getObject(5));
				pay_trans.setMobile_no(""+result.getObject(6));
				pay_trans.setTrans_amount(""+result.getObject(7));
				pay_trans.setMerchant_id(""+result.getObject(8));
				pay_trans.setMerchant_code(""+result.getObject(9));
				pay_trans.setTrans_channel(""+result.getObject(10));
				
				arr.add(pay_trans);
			}	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
		}
		return arr;
}
	
	
	/*public ArrayList getBillPaymentSubscriberDetailsId(String subscriberId)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		PAYTRANS pay_trans;
		E_TRANSACTION e_trans;
		String str = "", str1 = "";
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		String apostrophe = "'";
		
		try
		{
			
			con = connectToTelco();
			stat = con.createStatement();
		 
			query = "Select subscriber_id,";
			
			

			query = "select subscriber_id, trans_date, process_status, unique_transid, STATUS_DESCRIPTION, mobile_no,Trans_Amount " +
					"from telcodb..t_paytrans where subscriber_id ='"+subscriberId+"'" +
					" order by trans_date desc"; 	
			
			
	
			System.out.println("query for getBillPaymentSubscriberDetails "+query );
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				pay_trans = new PAYTRANS();
				pay_trans.setCounter(""+counter);
				pay_trans.setSubscriber_id(""+result.getObject(1));
				pay_trans.setTrans_date(""+result.getObject(2));
				pay_trans.setProcess_status(""+result.getObject(3));
				pay_trans.setUnique_trans_id(""+result.getObject(4));
				pay_trans.setStatus_desc(""+result.getObject(5));
				pay_trans.setMobile_no(""+result.getObject(6));
				pay_trans.setTrans_amount(""+result.getObject(7));
				arr.add(pay_trans);
			}	
			
		
		}catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	 */


	public ArrayList getGTBillPaymentSubscriberSummary(String subscriberId, String start_dt, String end_dt,
			String merchantcode)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		PAYTRANS pay_trans;
		E_TRANSACTION e_trans;
		String str = "", str1 = "";
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		String apostrophe = "'";
		
		
		try
		{
					 
			if(merchantcode.indexOf(":")>0)
			{
				String m[] = merchantcode.split(":");
				merchantcode = "";
				for(int i=0;i<m.length;i++)
				{
					merchantcode += apostrophe + m[i] + apostrophe + ",";
				}
				
				merchantcode = merchantcode.substring(0, merchantcode.lastIndexOf(","));
			}
			else
			{
				merchantcode = apostrophe + merchantcode + apostrophe;
			}
			
			con = connectToTelco();
			stat = con.createStatement();
		  	
			if(subscriberId.trim().length() > 0)
			{
				query = "select distinct substring(subscriber_id,3,3), count(*), sum(trans_amount) " +
					"from t_paytrans where subscriber_id like '%"+subscriberId+"%' and" +
					" merchant_code in ("+merchantcode+") and" +
					" trans_date between('"+start_dt+"') and ('"+end_dt+"') group by substring(subscriber_id,3,3) ";
			}
			else
			{
				query = "select distinct substring(subscriber_id,3,3),  count(*),  sum(trans_amount) from t_paytrans " +
						"where trans_date between('"+start_dt+"') and ('"+end_dt+"') and " +
						"merchant_code in("+merchantcode+") group by substring(subscriber_id,3,3)";	
			}
			System.out.println("query for getBillPayment Details Gt Assure "+query  + "subscriberId  "+subscriberId);
		
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				pay_trans = new PAYTRANS();
				pay_trans.setCounter(""+counter);
				pay_trans.setSubscriber_id(""+result.getObject(1));
				pay_trans.setTrans_date(""+result.getObject(2));
				pay_trans.setTrans_amount(""+result.getObject(3));
				arr.add(pay_trans);
			}	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
		}
		return arr;
}
	
	/*public ArrayList getBillPaymentSubscriberGroupCount(String subscriberId)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		PAYTRANS pay_trans;
		E_TRANSACTION e_trans;
		String str = "", str1 = "";
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		
		try
		{
			 con = connectToTelco();
		     stat = con.createStatement();
			
					
			 query = "select subscriber_id,count(subscriber_id) Transaction_count from telcodb..t_paytrans group by subscriber_id having count(subscriber_id) > 1";
			 
			 System.out.println("query for getBillPayment Details Gt Assure for count "+query );
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				pay_trans = new PAYTRANS();
				pay_trans.setCounter(""+counter);
				pay_trans.setSubscriber_id(""+result.getObject(1));
				pay_trans.setTransaction_count(""+result.getObject(2));
				arr.add(pay_trans);
			}	
			
		
		}catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	 */
	
	
	public ArrayList getGTBillPaymentSubscriberDetails(String subscriberId, String merchantcode, String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		PAYTRANS pay_trans;
		E_TRANSACTION e_trans;
		String str = "", str1 = "";
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		String apostrophe = "'";
		
		
		try
		{
			
			if(merchantcode.indexOf(":")>0)
			{
				String m[] = merchantcode.split(":");
				merchantcode = "";
				for(int i=0;i<m.length;i++)
				{
					merchantcode += apostrophe + m[i] + apostrophe + ",";
				}
				
				merchantcode = merchantcode.substring(0, merchantcode.lastIndexOf(","));
			}
			else
			{
				merchantcode = apostrophe + merchantcode + apostrophe;
			}
			
			
			con = connectToTelco();
			stat = con.createStatement();
		 	
			query = "select subscriber_id,trans_date,process_status,unique_transid," +
					" STATUS_DESCRIPTION, mobile_no, trans_amount,Payment_type" +
					" from t_paytrans where trans_date between('"+start_dt+"') and ('"+end_dt+"') and" +
					" subscriber_id like '%"+subscriberId+"%' and merchant_code in ("+merchantcode+") ";
		  
			
			System.out.println("query for getBillPayment Details Gt Assure "+query  + "subscriberId  "+subscriberId);
		
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				pay_trans = new PAYTRANS();
				pay_trans.setCounter(""+counter);
				pay_trans.setSubscriber_id(""+result.getObject(1));
				pay_trans.setTrans_date(""+result.getObject(2));
				pay_trans.setProcess_status(""+result.getObject(3));
				pay_trans.setUnique_trans_id(""+result.getObject(4));
				pay_trans.setStatus_desc(""+result.getObject(5));
				pay_trans.setMobile_no(""+result.getObject(6));
				pay_trans.setTrans_amount(""+result.getObject(7));
				pay_trans.setPayment_type(""+result.getObject(8));
				arr.add(pay_trans);
			
			}	
			
		
		}catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
			closeConnectionECard(con, result, result);
		}
		return arr;
}

	
	/*public ArrayList getBillPaymentSubscriberDetailsGtLog(String subscriberId)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		PAYTRANS pay_trans;
		E_TRANSACTION e_trans;
		String str = "", str1 = "";
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
		
			
			con = connectToTelco();
			stat = con.createStatement();
			
			
			

			//String query = "select * from telcodb.dbo.T_SMS_RECEIVE where sms_key = '"+sms_key+"' order by trans_date desc";
  				query = " select * from telcodb..t_paytrans where subscriber_id = '"+subscriberId+"' order by trans_date desc";
		   
			
			
			System.out.println("query  Subscribersss  "+query );
			
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				pay_trans = new PAYTRANS();
				pay_trans.setCounter(""+counter);
				pay_trans.setSubscriber_id(""+result.getObject(1));
				pay_trans.setTrans_date(""+result.getObject(2));
				pay_trans.setProcess_status(""+result.getObject(3));
				pay_trans.setUnique_trans_id(""+result.getObject(4));
				pay_trans.setStatus_desc(""+result.getObject(5));
				pay_trans.setMobile_no(""+result.getObject(6));
				pay_trans.setTrans_amount(""+result.getObject(7));
				pay_trans.setPayment_type(""+result.getObject(8));
				arr.add(pay_trans);
			}	
			
		
		}catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	 */
	/*Method for getting the irreversed funds transfer*/
	public ArrayList getFailedFundsTransfer(String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		REQUEST_LOG request_log;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select TRANSID,CARD_NUM,TRANS_DATE,TRANS_AMOUNT,TRANS_CODE," +
			"MERCHANT_CODE," +
			"(select error_desc from ecarddb.dbo.e_autoswitch_error where error_code = ecarddb.dbo.e_requestlog.response_code),RESPONSE_TIME,TRANS_DESCR,client_id," +
			"REQUEST_ID,FEE,CURRENCY,datediff(ss,response_time,trans_date)" +
			" from ecarddb.dbo.e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"') and response_code = '18' order by trans_date desc";
			
			System.out.println("getFailedFundsTransfer " + query);
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				request_log = new REQUEST_LOG();
				request_log.setCounter(""+counter);
				request_log.setTransid(""+result.getObject(1));
				request_log.setCard_num(""+result.getObject(2));
				request_log.setTrans_date(""+result.getObject(3));
				request_log.setTrans_amount(""+result.getObject(4));
				request_log.setTrans_code(""+result.getObject(5));
				
				String s = ""+result.getObject(6);
				if(s.equals("")||s.equals("null"))
					s = "";
				request_log.setMerchant_desc(s);
				
				request_log.setResponse_code(""+result.getObject(7));
				request_log.setResponse_time(""+result.getObject(8));
				request_log.setTrans_descr(""+result.getObject(9));
				request_log.setClient_id(""+result.getObject(10));
				request_log.setRequest_id(""+result.getObject(11));
				request_log.setFee(""+result.getObject(12));
				request_log.setCurrency(""+result.getObject(13));
				
				s = ""+result.getObject(14);
				if(s.equals("")||s.equals("null"))
					s = "";
				
				request_log.setResponse_time_in_secs(s);
				
				arr.add(request_log);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Method for getting the failed funds transfer grouped by receiving bank*/
	public ArrayList getFailedFundsTransferGroupedByBankCode(String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		REQUEST_LOG request_log;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();

			query = "select substring(merchant_code, 1, 3), count(*)," +
					" sum(convert(decimal(18,3), trans_amount))" +
					" from e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"') and" +
					" response_code <> '0' group by  substring(merchant_code, 1, 3)";
			
			System.out.println("getFailedFundsTransferGroupedByBankCode " + query);
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				request_log = new REQUEST_LOG();
				request_log.setMerchant_code(""+result.getObject(1));
				request_log.setCounter(""+result.getObject(2));
				request_log.setTrans_amount(""+result.getObject(3));
				
				arr.add(request_log);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	/*This method is used to group failed funds transfer by error codes*/
	public ArrayList getFailedFundsTransferByBankCodeGroupByErrorCode(String merchantCode, String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		REQUEST_LOG request_log;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select response_code," +
			" (select ecarddb..e_autoswitch_error.error_desc from ecarddb..e_autoswitch_error where ecarddb..e_autoswitch_error.error_code = ecarddb..e_requestlog.response_code)," +
			" count(*)," +
			" sum(convert(decimal(18,3), trans_amount))" +
			" from e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"') " +
			" and response_code <> '0' and substring(merchant_code, 1, 3) = '"+merchantCode+"' group by response_code";
			
			System.out.println("getFailedFundsTransferByBankCodeGroupByErrorCode " + query);
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				request_log = new REQUEST_LOG();
				request_log.setResponse_code(""+result.getObject(1));
				request_log.setResponse_time(""+result.getObject(2));
				request_log.setCounter(""+result.getObject(3));
				request_log.setTrans_amount(""+result.getObject(4));
				request_log.setMerchant_code(merchantCode);
				
				arr.add(request_log);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Method for getting the failed funds transfer by a bank code*/
	public ArrayList getFailedFundsTransferByBankCodeAndErrorCode(String bank_code, String errorCode, String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		REQUEST_LOG request_log;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();

			query = "select TRANSID,CARD_NUM,TRANS_DATE,TRANS_AMOUNT,TRANS_CODE," +
			"MERCHANT_CODE," +
			"(select e_autoswitch_error.error_desc from e_autoswitch_error where e_autoswitch_error.error_code = e_requestlog.response_code)," +
			" RESPONSE_TIME,TRANS_DESCR,client_id," +
			" REQUEST_ID,FEE,CURRENCY,datediff(ss,response_time,trans_date)" +
			" from e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"') and response_code = '"+errorCode+"' " +
			" and substring(merchant_code,1,3) = '"+bank_code+"' order by trans_date desc";
			
			System.out.println("getFailedFundsTransferByBankCodeAndErrorCode " + query);
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				request_log = new REQUEST_LOG();
				request_log.setCounter(""+counter);
				request_log.setTransid(""+result.getObject(1));
				request_log.setCard_num(""+result.getObject(2));
				request_log.setTrans_date(""+result.getObject(3));
				request_log.setTrans_amount(""+result.getObject(4));
				request_log.setTrans_code(""+result.getObject(5));
				
				String s = ""+result.getObject(6);
				if(s.equals("")||s.equals("null"))
					s = "";
				request_log.setMerchant_desc(s);
				
				request_log.setResponse_code(""+result.getObject(7));
				request_log.setResponse_time(""+result.getObject(8));
				request_log.setTrans_descr(""+result.getObject(9));
				request_log.setClient_id(""+result.getObject(10));
				request_log.setRequest_id(""+result.getObject(11));
				request_log.setFee(""+result.getObject(12));
				request_log.setCurrency(""+result.getObject(13));
				
				s = ""+result.getObject(14);
				if(s.equals("")||s.equals("null"))
					s = "";
				
				request_log.setResponse_time_in_secs(s);
				
				arr.add(request_log);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	
	/*This method is used to group failed funds transfer by error codes*/
	public ArrayList getFailedFundsTransferGroupByErrorCode(String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		REQUEST_LOG request_log;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select response_code," +
			" (select ecarddb..e_autoswitch_error.error_desc from ecarddb..e_autoswitch_error where ecarddb..e_autoswitch_error.error_code = ecarddb..e_requestlog.response_code)," +
			" count(*)," +
			" sum(convert(decimal(18,3), trans_amount))" +
			" from e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"') " +
			" and response_code <> '0' group by response_code";
			
			System.out.println("getFailedFundsTransferGroupByErrorCode " + query);
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				request_log = new REQUEST_LOG();
				request_log.setResponse_code(""+result.getObject(1));
				request_log.setResponse_time(""+result.getObject(2));
				request_log.setCounter(""+result.getObject(3));
				request_log.setTrans_amount(""+result.getObject(4));
				
				arr.add(request_log);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	
	/*Method for getting the failed funds transfer by a error code*/
	public ArrayList getFailedFundsTransferByErrorCode(String errorCode, String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		REQUEST_LOG request_log;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();

			query = "select TRANSID,CARD_NUM,TRANS_DATE,TRANS_AMOUNT,TRANS_CODE," +
			"MERCHANT_CODE," +
			"(select e_autoswitch_error.error_desc from e_autoswitch_error where e_autoswitch_error.error_code = e_requestlog.response_code)," +
			" RESPONSE_TIME,TRANS_DESCR,client_id," +
			" REQUEST_ID,FEE,CURRENCY,datediff(ss,response_time,trans_date)" +
			" from e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"') and response_code = '"+errorCode+"' " +
			" order by trans_date desc";
			
			System.out.println("getFailedFundsTransferByErrorCode " + query);
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				request_log = new REQUEST_LOG();
				request_log.setCounter(""+counter);
				request_log.setTransid(""+result.getObject(1));
				request_log.setCard_num(""+result.getObject(2));
				request_log.setTrans_date(""+result.getObject(3));
				request_log.setTrans_amount(""+result.getObject(4));
				request_log.setTrans_code(""+result.getObject(5));
				
				String s = ""+result.getObject(6);
				if(s.equals("")||s.equals("null"))
					s = "";
				request_log.setMerchant_desc(s);
				
				request_log.setResponse_code(""+result.getObject(7));
				request_log.setResponse_time(""+result.getObject(8));
				request_log.setTrans_descr(""+result.getObject(9));
				request_log.setClient_id(""+result.getObject(10));
				request_log.setRequest_id(""+result.getObject(11));
				request_log.setFee(""+result.getObject(12));
				request_log.setCurrency(""+result.getObject(13));
				
				s = ""+result.getObject(14);
				if(s.equals("")||s.equals("null"))
					s = "";
				
				request_log.setResponse_time_in_secs(s);
				
				arr.add(request_log);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	
	/*Method for getting the transaction of funds transfer*/
	public ArrayList getFundsTransferTransaction(String card_num, String option, double grt_amount, 
			String start_dt, String end_dt, String transCode, String transCount, String tranSummary)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION transaction;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			if(option.equals("1"))//greater than
			{
				if(transCount.trim().length() > 0)
				{	
					query = "select distinct CARD_NUM, Count(*), sum(TRANS_AMOUNT)" +
							" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
							" and card_num like '"+card_num+"%' and trans_code ='"+transCode+"'" +
							" and trans_amount > "+grt_amount+" group by card_num having count(*) = "+transCount+"";
					System.out.println("query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						transaction = new E_TRANSACTION();
						
						transaction.setCard_num(""+result.getObject(1));
						transaction.setCard_count(""+result.getObject(2));
						transaction.setTotal_amount(""+result.getObject(3));
						arr.add(transaction);
					}
				}
				else if(tranSummary.trim().equalsIgnoreCase("G"))
				{
				
					query = "select distinct CARD_NUM, Count(*)Transaction_Count, sum(TRANS_AMOUNT) Trans_Amount" +
							" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
							" and card_num like '"+card_num+"%' and trans_code ='"+transCode+"'" +
							" and trans_amount > "+grt_amount+" group by card_num order by count(*) DESC ";
					
							System.out.println("query " + query);
							result = stat.executeQuery(query);
							while(result.next())
							{
								transaction = new E_TRANSACTION();
								transaction.setCard_num(""+result.getObject(1));
								transaction.setCard_count(""+result.getObject(2));
								transaction.setTotal_amount(""+result.getObject(3));
								arr.add(transaction);
								System.out.println("query " + query);
							}
				}
				else
				{
					query = "select TRANSID, CARD_NUM, TRANS_NO, TRANS_DATE, TRANS_DESCR," +
					"TRANS_AMOUNT,TRANS_TYPE,TRANS_CODE, MERCHANT_CODE, UNIQUE_TRANSID, " +
					"(select channel_name from e_channel where e_channel.channel_id = e_transaction.channelid), PROCESS_STATUS " +
					" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
					" and card_num like '"+card_num+"%' and trans_code = '"+transCode+"' and" +
					" trans_amount > "+grt_amount+" order by trans_date desc";
					
					System.out.println("query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						transaction = new E_TRANSACTION();
						
						transaction.setTransid(""+result.getObject(1));
						transaction.setCard_num(""+result.getObject(2));
						transaction.setTrans_no(""+result.getObject(3));
						transaction.setTrans_date(""+result.getObject(4));
						transaction.setTrans_desc(""+result.getObject(5));
						transaction.setTrans_amount(""+result.getObject(6));
						transaction.setTrans_type(""+result.getObject(7));
						transaction.setTrans_code(""+result.getObject(8));
						transaction.setMerchat_code(""+result.getObject(9));
						transaction.setUnique_transid(""+result.getObject(10));
						transaction.setChannelid(""+result.getObject(11));
						transaction.setProcess_status(""+result.getObject(12));
						
						arr.add(transaction);
					}
				}
			}
			else if(option.equals("2"))//lesser than
			{
				
				if(transCount.trim().length() > 0)
				{	
					query = "select distinct CARD_NUM, Count(*), sum(TRANS_AMOUNT)" +
							" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
							" and card_num like '"+card_num+"%' and trans_code ='"+transCode+"'" +
							" and trans_amount < "+grt_amount+" group by card_num having count(*) = "+transCount+"";
					System.out.println("query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						transaction = new E_TRANSACTION();
						
						transaction.setCard_num(""+result.getObject(1));
						transaction.setCard_count(""+result.getObject(2));
						transaction.setTotal_amount(""+result.getObject(3));
						arr.add(transaction);
					}
				}
				else if(tranSummary.trim().equalsIgnoreCase("G"))
				{
					query = "select distinct CARD_NUM, Count(*)Transaction_Count, sum(TRANS_AMOUNT) Trans_Amount" +
							" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
							" and card_num like '"+card_num+"%' and trans_code ='"+transCode+"'" +
							" and trans_amount < "+grt_amount+" group by card_num order by count(*) DESC ";
					System.out.println("query G 1 " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
							transaction = new E_TRANSACTION();
							
							transaction.setCard_num(""+result.getObject(1));
							transaction.setCard_count(""+result.getObject(2));
							transaction.setTotal_amount(""+result.getObject(3));
							arr.add(transaction);
							System.out.println("query " + query);
					}
					
				}
				else
				{
					query = "select TRANSID, CARD_NUM, TRANS_NO, TRANS_DATE, TRANS_DESCR," +
					"TRANS_AMOUNT,TRANS_TYPE,TRANS_CODE, MERCHANT_CODE, UNIQUE_TRANSID, " +
					"(select channel_name from e_channel where e_channel.channel_id = e_transaction.channelid), PROCESS_STATUS " +
					" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
					" and card_num like '"+card_num+"%' and trans_code = '"+transCode+"' and" +
					" trans_amount < "+grt_amount+" order by trans_date desc";
					
					System.out.println("query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						transaction = new E_TRANSACTION();
						
						transaction.setTransid(""+result.getObject(1));
						transaction.setCard_num(""+result.getObject(2));
						transaction.setTrans_no(""+result.getObject(3));
						transaction.setTrans_date(""+result.getObject(4));
						transaction.setTrans_desc(""+result.getObject(5));
						transaction.setTrans_amount(""+result.getObject(6));
						transaction.setTrans_type(""+result.getObject(7));
						transaction.setTrans_code(""+result.getObject(8));
						transaction.setMerchat_code(""+result.getObject(9));
						transaction.setUnique_transid(""+result.getObject(10));
						transaction.setChannelid(""+result.getObject(11));
						transaction.setProcess_status(""+result.getObject(12));
						
						arr.add(transaction);
					}
				}
			}
			else if(option.equals("3"))//exact
			{
				
				if(transCount.trim().length() > 0)
				{	
					query = "select distinct CARD_NUM, Count(*), sum(TRANS_AMOUNT)" +
							" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
							" and card_num like '"+card_num+"%' and trans_code ='"+transCode+"'" +
							" and trans_amount = "+grt_amount+" group by card_num having count(*) = "+transCount+"";
					System.out.println("query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						transaction = new E_TRANSACTION();
						
						transaction.setCard_num(""+result.getObject(1));
						transaction.setCard_count(""+result.getObject(2));
						transaction.setTotal_amount(""+result.getObject(3));
						arr.add(transaction);
					}
				}
				else if(tranSummary.trim().equalsIgnoreCase("G"))
				{
					query = "select distinct CARD_NUM, Count(*)Transaction_Count, sum(TRANS_AMOUNT) Trans_Amount" +
							" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
							" and card_num like '"+card_num+"%' and trans_code ='"+transCode+"'" +
							" and trans_amount = "+grt_amount+" group by card_num order by count(*) DESC ";
					System.out.println("query G 2" + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
				 
							transaction = new E_TRANSACTION();
							
							transaction.setCard_num(""+result.getObject(1));
							transaction.setCard_count(""+result.getObject(2));
							transaction.setTotal_amount(""+result.getObject(3));
							arr.add(transaction);
							System.out.println("query " + query);
					}
						 
					
				}
				else
				{
					query = "select TRANSID, CARD_NUM, TRANS_NO, TRANS_DATE, TRANS_DESCR," +
					"TRANS_AMOUNT,TRANS_TYPE,TRANS_CODE, MERCHANT_CODE, UNIQUE_TRANSID, " +
					"(select channel_name from e_channel where e_channel.channel_id = e_transaction.channelid), PROCESS_STATUS " +
					" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
					" and card_num like '"+card_num+"%' and trans_code = '"+transCode+"' and" +
					" trans_amount = "+grt_amount+" order by trans_date desc";
					
					System.out.println("query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						transaction = new E_TRANSACTION();
						
						transaction.setTransid(""+result.getObject(1));
						transaction.setCard_num(""+result.getObject(2));
						transaction.setTrans_no(""+result.getObject(3));
						transaction.setTrans_date(""+result.getObject(4));
						transaction.setTrans_desc(""+result.getObject(5));
						transaction.setTrans_amount(""+result.getObject(6));
						transaction.setTrans_type(""+result.getObject(7));
						transaction.setTrans_code(""+result.getObject(8));
						transaction.setMerchat_code(""+result.getObject(9));
						transaction.setUnique_transid(""+result.getObject(10));
						transaction.setChannelid(""+result.getObject(11));
						transaction.setProcess_status(""+result.getObject(12));
						
						arr.add(transaction);
					}
				}
			}
			else
			{
				
				if(transCount.trim().length() > 0)
				{	
					query = "select distinct CARD_NUM, Count(*), sum(TRANS_AMOUNT)" +
							" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
							" and card_num like '"+card_num+"%' and trans_code ='"+transCode+"' " +
							" group by card_num having count(*) = "+transCount+"";
					System.out.println("query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						transaction = new E_TRANSACTION();
						
						transaction.setCard_num(""+result.getObject(1));
						transaction.setCard_count(""+result.getObject(2));
						transaction.setTotal_amount(""+result.getObject(3));
						arr.add(transaction);
					}
				}
				else if(tranSummary.trim().equalsIgnoreCase("G"))
				{
					query = "select distinct CARD_NUM, Count(*)Transaction_Count, sum(TRANS_AMOUNT) Trans_Amount" +
							" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
							" and card_num like '"+card_num+"%' and trans_code ='"+transCode+"'" +
							" group by card_num order by count(*) DESC ";
					System.out.println("query G 2" + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
				 
							transaction = new E_TRANSACTION();
							
							transaction.setCard_num(""+result.getObject(1));
							transaction.setCard_count(""+result.getObject(2));
							transaction.setTotal_amount(""+result.getObject(3));
							arr.add(transaction);
							System.out.println("query " + query);
					}
						 
					
				}
				else
				{
					query = "select TRANSID, CARD_NUM, TRANS_NO, TRANS_DATE, TRANS_DESCR," +
					"TRANS_AMOUNT,TRANS_TYPE,TRANS_CODE, MERCHANT_CODE, UNIQUE_TRANSID, " +
					"(select channel_name from e_channel where e_channel.channel_id = e_transaction.channelid), PROCESS_STATUS " +
					" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
					" and card_num like '"+card_num+"%' and trans_code = '"+transCode+"' order by trans_date desc";
					
					System.out.println("query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						transaction = new E_TRANSACTION();
						
						transaction.setTransid(""+result.getObject(1));
						transaction.setCard_num(""+result.getObject(2));
						transaction.setTrans_no(""+result.getObject(3));
						transaction.setTrans_date(""+result.getObject(4));
						transaction.setTrans_desc(""+result.getObject(5));
						transaction.setTrans_amount(""+result.getObject(6));
						transaction.setTrans_type(""+result.getObject(7));
						transaction.setTrans_code(""+result.getObject(8));
						transaction.setMerchat_code(""+result.getObject(9));
						transaction.setUnique_transid(""+result.getObject(10));
						transaction.setChannelid(""+result.getObject(11));
						transaction.setProcess_status(""+result.getObject(12));
						
						arr.add(transaction);
					}
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Method for getting the rolled back funds transfer*/
	public ArrayList getRolledBackFundsTransfer(String unique_transid)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		REVERSAL reversal;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select UNIQUE_TRANSID, RESPONSE_CODE, ATTEMPT, REVDATE, RESPONSE_DATE," +
			"REVERSAL_TYPE" +
			" from ecarddb.dbo.E_REVERSAL_LOG where unique_transid = '"+unique_transid+"'";
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				reversal = new REVERSAL();
				
				reversal.setUnique_transid(""+result.getObject(1));
				reversal.setResponse_code(""+result.getObject(2));
				reversal.setAttempt(""+result.getObject(3));
				reversal.setRevdate(""+result.getObject(4));
				reversal.setResponse_date(""+result.getObject(5));
				reversal.setReversal_type(""+result.getObject(6));
				
				arr.add(reversal);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Method to get the etransaction data*/
	public ArrayList getE_TRANSACTION(String unique_transid , String user_code)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION transaction;
		HashNumber hn = new HashNumber();
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select TRANSID, CARD_NUM, TRANS_NO, TRANS_DATE, TRANS_DESCR," +
					"TRANS_AMOUNT,TRANS_TYPE,TRANS_CODE, MERCHANT_CODE, UNIQUE_TRANSID, " +
					"CHANNELID, PROCESS_STATUS " +
					" from ecarddb..E_TRANSACTION where unique_transid = '"+unique_transid+"' order by trans_date desc";
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				transaction = new E_TRANSACTION();
				
				transaction.setTransid(""+result.getObject(1));
				transaction.setCard_num(hn.hashStringValue(""+result.getObject(2), 9, 4) );
				transaction.setTrans_no(""+result.getObject(3));
				transaction.setTrans_date(""+result.getObject(4));
				transaction.setTrans_desc(""+result.getObject(5));
				transaction.setTrans_amount(""+result.getObject(6));
				transaction.setTrans_type(""+result.getObject(7));
				transaction.setTrans_code(""+result.getObject(8));
				transaction.setMerchat_code(""+result.getObject(9));
				transaction.setUnique_transid(""+result.getObject(10));
				transaction.setChannelid(""+result.getObject(11));
				transaction.setProcess_status(""+result.getObject(12));
				
				arr.add(transaction);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Method to get the etransaction data by merchant code*/
	public ArrayList getE_TRANSACTIONByMerchantCode(String merchant_code)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION transaction;
		HashNumber hn = new HashNumber();
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select TRANSID, CARD_NUM, TRANS_NO, TRANS_DATE, TRANS_DESCR," +
					"TRANS_AMOUNT,TRANS_TYPE,TRANS_CODE, MERCHANT_CODE, UNIQUE_TRANSID, " +
					"CHANNELID, PROCESS_STATUS " +
					" from ecarddb..E_TRANSACTION where merchant_code = '"+merchant_code+"' order by trans_date desc";
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				transaction = new E_TRANSACTION();
				
				transaction.setTransid(""+result.getObject(1));
				transaction.setCard_num(hn.hashStringValue(""+result.getObject(2), 9, 4) );
				transaction.setTrans_no(""+result.getObject(3));
				transaction.setTrans_date(""+result.getObject(4));
				transaction.setTrans_desc(""+result.getObject(5));
				transaction.setTrans_amount(""+result.getObject(6));
				transaction.setTrans_type(""+result.getObject(7));
				transaction.setTrans_code(""+result.getObject(8));
				transaction.setMerchat_code(""+result.getObject(9));
				transaction.setUnique_transid(""+result.getObject(10));
				transaction.setChannelid(""+result.getObject(11));
				transaction.setProcess_status(""+result.getObject(12));
				
				arr.add(transaction);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Method to get the e mobile subscriber data, also specifying whether its mobile banking or pocket moni*/
	public ArrayList getE_MOBILE_SUBSCRIBER(String mobile, String mobile_type, String appid)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_MOBILE_SUBSCRIBER mobile_subscriber;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			String str_mobile = mobile.trim();
			/*if(str_mobile.trim().length()>0)
			{
				if(str_mobile.startsWith("234"));
				else
				{
					str_mobile = "234" + str_mobile.substring(1, str_mobile.length());
				}
			}*/
			
			if(mobile_type.equals("Version I"))
			{
			
				con = connectToECard();	
				stat = con.createStatement();
				
				query = "select MOBILE, ONETIME_PASSWORD, ACTIVE, MODIFIED, DIFF," +
						"APPID,SYSDATE,AGENTID, ALLOW_DEBIT, INIT_USERNAME, " +
						"INIT_IP, AUTH_USERNAME,AUTH_IP,AUTH_DATE " +
						" from ecarddb..E_MOBILE_SUBSCRIBER where mobile = '"+str_mobile+"'";
				
				System.out.println("getE_MOBILE_SUBSCRIBER query "  + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					mobile_subscriber = new E_MOBILE_SUBSCRIBER();
					
					mobile_subscriber.setMobile(""+result.getObject(1));
					mobile_subscriber.setOnetime_password(""+result.getObject(2));
					mobile_subscriber.setActive(""+result.getObject(3));
					mobile_subscriber.setModified(""+result.getObject(4));
					mobile_subscriber.setDiff(""+result.getObject(5));
					mobile_subscriber.setAppid(""+result.getObject(6));
					mobile_subscriber.setSysdate(""+result.getObject(7));
					mobile_subscriber.setAgentid(""+result.getObject(8));
					mobile_subscriber.setAllow_debit(""+result.getObject(9));
					mobile_subscriber.setInit_username(""+result.getObject(10));
					mobile_subscriber.setInit_ip(""+result.getObject(11));
					mobile_subscriber.setAuth_username(""+result.getObject(12));
					mobile_subscriber.setAuth_ip(""+result.getObject(13));
					mobile_subscriber.setAuth_date(""+result.getObject(14));
					
					arr.add(mobile_subscriber);
				}
			}
			else if(mobile_type.equals("Version II"))
			{
				con = connectMobileDB();
				stat = con.createStatement();
				
				query = "select id, mobile_no, onetime_password, appid, agentid, apptype, active, created, modified " +
						"from m_mobile_subscriber where mobile_no = '"+str_mobile+"' and appid = "+Integer.parseInt(appid)+" ";
				result = stat.executeQuery(query);
				while(result.next())
				{
					mobile_subscriber = new E_MOBILE_SUBSCRIBER();
					mobile_subscriber.setId(""+result.getObject(1));
					mobile_subscriber.setMobile(""+result.getObject(2));
					mobile_subscriber.setOnetime_password(""+result.getObject(3));
					mobile_subscriber.setAppid(""+result.getObject(4));
					mobile_subscriber.setAgentid(""+result.getObject(5));
					mobile_subscriber.setApptype(""+result.getObject(6));
					mobile_subscriber.setActive(""+result.getObject(7));
					mobile_subscriber.setCreated(""+result.getObject(8));
					mobile_subscriber.setModified(""+result.getObject(9));
					
					arr.add(mobile_subscriber);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
			closeConnectionMobileDB(con, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeConnectionMobileDB(con, result);
		}
		return arr;
	}
	
	
	/*Method to get the e mobile subscriber card data*/
	public ArrayList getE_MOBILE_SUBSCRIBER_CARD(String mobile, String mobile_type)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_MOBILE_SUBSCRIBER_CARD mobile_subscriber_card;
		String card_num = "";
		String start_num = "";
		String end_num = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			String str_mobile = mobile.trim();
			
			if(mobile_type.equals("Version I"))
			{
			
				con = connectToECard();	
				stat = con.createStatement();
				
				
				
				/*if(str_mobile.trim().length()>0)
				{
					if(str_mobile.startsWith("234"));
					else
					{
						str_mobile = "234" + str_mobile.substring(1, str_mobile.length());
					}
				}*/
			
				query = "select CARD_NUM, ALIAS, MOBILE, EXPIRATION, MODIFIED,ISSUER_CODE, auth_status" +
						" from ecarddb..E_MOBILE_SUBSCRIBER_CARD where mobile = '"+str_mobile+"'";
				
				result = stat.executeQuery(query);
				while(result.next())
				{
					mobile_subscriber_card = new E_MOBILE_SUBSCRIBER_CARD();

					mobile_subscriber_card.setCard_num(""+result.getObject(1));
					mobile_subscriber_card.setAlias(""+result.getObject(2));
					mobile_subscriber_card.setMobile(""+result.getObject(3));
					mobile_subscriber_card.setExpiration(""+result.getObject(4));
					mobile_subscriber_card.setModified(""+result.getObject(5));
					mobile_subscriber_card.setIssuer_code(""+result.getObject(6));
					mobile_subscriber_card.setAuth_status(""+result.getObject(7));
					
					arr.add(mobile_subscriber_card);
				}
			}
			else if(mobile_type.equals("Version II"))
			{
				con = connectMobileDB();
				stat = con.createStatement();
			
				query = "select subscriber_id, card_number, expiration, alias, active, created, modified, auth_status" +
						" from m_mobile_subscriber_card where subscriber_id = "+Integer.parseInt(str_mobile)+" ";
				
				System.out.println("vquery " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					mobile_subscriber_card = new E_MOBILE_SUBSCRIBER_CARD();
					
					mobile_subscriber_card.setSubscriber_id(""+result.getObject(1));
					mobile_subscriber_card.setCard_num(""+result.getObject(2));
					mobile_subscriber_card.setExpiration(""+result.getObject(3));
					mobile_subscriber_card.setAlias(""+result.getObject(4));
					mobile_subscriber_card.setActive(""+result.getObject(5));
					mobile_subscriber_card.setCreated(""+result.getObject(6));
					mobile_subscriber_card.setModified(""+result.getObject(7));
					mobile_subscriber_card.setIssuer_code("N/A");
					mobile_subscriber_card.setAuth_status(""+result.getObject(8));
					
					arr.add(mobile_subscriber_card);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
			closeConnectionMobileDB(con, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeConnectionMobileDB(con, result);
		}
		return arr;
	}
	
	
	/*Method to get the e mobile subscriber card data for schemes*/
	public ArrayList getE_MOBILE_SUBSCRIBER_CARDScheme(String mobile, String mobileNo, String dbServer)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_MOBILE_SUBSCRIBER_CARD mobile_subscriber_card = null;
		String card_num = "";
		String start_num = "";
		String end_num = "";
		Connection con = null, con1 = null, con2 = null;
		Statement stat = null, stat1 = null, stat2 = null;
		ResultSet result = null, result1 = null, result2 = null;
		
		try
		{
			
			String str_mobile = mobile.trim();
			if(str_mobile==null || str_mobile == "")
			{
				str_mobile = "0";
			}

			con = connectMobileDB();
			stat = con.createStatement();
			
			con1 = connectPocketMoniEcardDB();
			stat1 = con1.createStatement();
			
			con2 = connectToTestECard();
			stat2 = con2.createStatement();
		
			query = "select subscriber_id, card_number, expiration, alias, active, created, modified, auth_status" +
					" from m_mobile_subscriber_card where subscriber_id = "+Integer.parseInt(str_mobile)+" ";
			System.out.println("q " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				mobile_subscriber_card = new E_MOBILE_SUBSCRIBER_CARD();
				
				mobile_subscriber_card.setSubscriber_id(""+result.getObject(1));
				mobile_subscriber_card.setCard_num(""+result.getObject(2));
				mobile_subscriber_card.setExpiration(""+result.getObject(3));
				mobile_subscriber_card.setAlias(""+result.getObject(4));
				mobile_subscriber_card.setActive(""+result.getObject(5));
				mobile_subscriber_card.setCreated(""+result.getObject(6));
				mobile_subscriber_card.setModified(""+result.getObject(7));
				mobile_subscriber_card.setIssuer_code("N/A");
				mobile_subscriber_card.setAuth_status(""+result.getObject(8));
				
				
				if(dbServer.equals("1"))//pocketmoni server .133
				{
					query ="select Online_Balance from  ecarddb..e_cardholder where card_num  = '"+cryptPan(mobile_subscriber_card.getCard_num(),2)+"'";
					System.out.println("online balance query from pm "  + query);
					result1 = stat1.executeQuery(query);
					while(result1.next())
					{
						mobile_subscriber_card.setOnlineBalance(""+result1.getObject(1));
						arr.add(mobile_subscriber_card);
					}
		
				}
				else if(dbServer.equals("2"))//.57
				{
					query ="select Online_Balance from  ecarddb..e_cardholder where card_num  = '"+cryptPan(mobile_subscriber_card.getCard_num(),2)+"'";
					System.out.println("online balance query from testcard  "  + query);
					result2 = stat2.executeQuery(query);
					while(result2.next())
					{
						mobile_subscriber_card.setOnlineBalance(""+result2.getObject(1));
						arr.add(mobile_subscriber_card);
					}
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionMobileDB(con, result);
			closeConnectionPocketMoniEcardDB(con1, result1);
			closeConnectionTestECard(con2, result2, result2);
		}
		finally
		{
			closeConnectionMobileDB(con, result);
			closeConnectionPocketMoniEcardDB(con1, result1);
			closeConnectionTestECard(con2, result2, result2);
		}
		return arr;
	}
	
	
	
	/*Method to get the mapped apps by mobile number*/
	public ArrayList getAppsByMobileNo(String mobile)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_MOBILE_SUBSCRIBER mobile_subscriber;
		String card_num = "";
		String start_num = "";
		String end_num = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectMobileDB();
			stat = con.createStatement();
			
			String str_mobile = mobile.trim();
			/*if(str_mobile.trim().length()>0)
			{
				if(str_mobile.startsWith("234"));
				else
				{
					str_mobile = "234" + str_mobile.substring(1, str_mobile.length());
				}
			}*/
			
			query = "select ms.appid, ma.appid from m_mobile_subscriber ms, m_mobileapp_properties ma" +
					" where ms.mobile_no = '"+str_mobile+"' and ms.appid  = ma.id";
			
			System.out.println("getAppsByMobileNo query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				mobile_subscriber = new E_MOBILE_SUBSCRIBER();
				mobile_subscriber.setAppid(""+result.getObject(1));
				mobile_subscriber.setAppnm(""+result.getObject(2));
				
				arr.add(mobile_subscriber);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionMobileDB(con, result);
		}
		finally
		{
			closeConnectionMobileDB(con, result);
		}
		return arr;
	}
	
	
	/*Method to get the etransaction summary by date range*/
	public ArrayList getE_TRANSACTIONSummaryByDate(String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION transaction;
		String split_type = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select distinct merchant_code, (select merchant_name from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code), sum(trans_amount),count(merchant_code),(select special_split from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code) from ecarddb..E_TRANSACTION " +
					"where trans_date between('"+start_dt+"') and ('"+end_dt+"') group by MERCHANT_CODE";
			//System.out.println("merchant summary " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				transaction = new E_TRANSACTION();
				split_type = "";
				
				transaction.setMerchat_code(result.getString(1));
				transaction.setMerchant_descr(result.getString(2));
				transaction.setTrans_amount(""+result.getObject(3));
				transaction.setTransaction_count(""+result.getObject(4));
				
				split_type = ""+result.getObject(5);
				
				if(split_type.equals("1"))
					transaction.setMerchant_split_type("Special Split");
				else if(split_type.equals("0"))
					transaction.setMerchant_split_type("Commission Split");
				
				if(transaction.getMerchat_code().length()<=10)
				{
					arr.add(transaction);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	

	/*Method to get the e merchant commission split type*/
	public String[] getE_MERCHANT_CATID_SPLIT_TYPE(String merchant_code)
	{
		String query = "";
		String[] str = new String[4];
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();		
			stat = con.createStatement();
			
			query = "select cat_id, special_split, merchant_code, merchant_name" +
					" from ecarddb..E_MERCHANT where merchant_code = '"+merchant_code+"'";
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = new String[4];
				str[0] = ""+result.getObject(1);
				str[1] = ""+result.getObject(2);
				str[2] = ""+result.getObject(3);
				str[3] = ""+result.getObject(4);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return str;
	}
	
	
	/*Method to get the e merchant special split*/
	public ArrayList getE_MERCHANT_SPECIAL_SPLIT(String merchant_code, String tran_count, String total_amount)
	{
		
		ArrayList arr = new ArrayList();
		String query = "";
		E_MERCHANT_SPLIT merchant_split;
		String svalue = "";
		String main_flag = "";
		double d = 0.0;
		double total_amt = 0.0;
		double db = 0.0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		ResultSet result1 = null;
		
		try
		{
			//con = connectToECard();
			con = connectToBackUpECard();
			stat = con.createStatement();
			
			query = "select split_descr, svalue, main_flag,split_card" +
					" from ecarddb..e_merchant_special_split where merchant_code = '"+merchant_code+"' order by main_flag";
			
			//System.out.println("query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				svalue = "";
				d = 0.0;

				merchant_split = new E_MERCHANT_SPLIT();
				merchant_split.setSplit_descr(""+result.getObject(1));
				
				svalue = ""+result.getObject(2);
				d = Double.parseDouble(svalue) * Double.parseDouble(tran_count);

				total_amt += d;
				main_flag = ""+result.getObject(3);
				merchant_split.setSplit_card(""+result.getObject(4));
				
				if(main_flag.equals("1") && svalue.equals("0.00"))//means i need to get the remaining balance
				{
					db = Double.parseDouble(total_amount) - total_amt;
					merchant_split.setSvalue(""+db);
				}
				else
				{
					merchant_split.setSvalue(""+d);
				}
				
				merchant_split.setMain_flag(main_flag);
				merchant_split.setTrans_count(tran_count);
				merchant_split.setEtzRatio(svalue);
				
				arr.add(merchant_split);
			}
		}
		catch(Exception ex)
		{   
			ex.printStackTrace();
			closeConnectionBackUpEcard(con, result, result1);
		}
		finally
		{
			closeConnectionBackUpEcard(con, result, result1);
		}
		return arr;
	}
	
	
	
	
	
	/*Method to get the commission split details by merchant code*/
	public ArrayList getE_MERCHANT_COMMISSION_SPLIT(String merchant_code, String cat_id, String tran_count, 
			String total_amount, String beginDate, String endDate, String channel, String transcode)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		ArrayList arr_scale = new ArrayList();
		
		E_MERCHANT_SPLIT merchant_split;
		E_CATSCALE e_catscale;
		String svalue = "";
		double d = 0.0; 
		double value = 0.0;
		String e_catscale_value = "";
		String e_catscale_type = "";
		String e_catscale_from = "";
		String e_catscale_to = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		ResultSet result1 = null;
		
		try
		{
			con = connectToBackUpECard();
			stat = con.createStatement();
			
			query = "select count(*) from ecarddb..E_CATSCALE where cat_id = '"+cat_id+"'";
			result = stat.executeQuery(query);
			if(result.next())
			{
				int count = result.getInt(1);
				if(count > 1)//multiple cat scale
				{
					query = "select scale_type, scale_value, scale_from, scale_to from ecarddb..E_CATSCALE where cat_id = '"+cat_id+"'";
					result = stat.executeQuery(query);
					while(result.next())
					{
						e_catscale = new E_CATSCALE();
						
						e_catscale.setScale_type(""+result.getObject(1));
						e_catscale.setScale_value(""+result.getObject(2));
						e_catscale.setScale_from(""+result.getObject(3));
						e_catscale.setScale_to(""+result.getObject(4));
						arr_scale.add(e_catscale);
					}
					
					double range_value = 0.0;
					double range_count = 0.0;
					
					if(arr_scale.size()>0)
					{
						query = "select count(*),trans_amount from ecarddb..e_transaction where merchant_code = '"+merchant_code+"' and channelid= '"+channel+"' and trans_code = '"+transcode+"' and trans_date between('"+beginDate+"') and ('"+endDate+"') group by trans_amount";
						result = stat.executeQuery(query);
						while(result.next())
						{
							range_value = 0.0;
							range_count = 0.0;
							
							range_count = Double.parseDouble(""+result.getObject(1));
							range_value = Double.parseDouble(""+result.getObject(2));
							
							for(int i=0;i<arr_scale.size();i++)
							{
								e_catscale = (E_CATSCALE)arr_scale.get(i);
								
								if(range_value >= Double.parseDouble(e_catscale.getScale_from()) && range_value <= Double.parseDouble(e_catscale.getScale_to()))
								{
									if(e_catscale.getScale_type().equals("0"))
									{	
										value += Double.parseDouble(e_catscale.getScale_value()) * range_count;
										break;
									}
									else if(e_catscale.getScale_type().equals("1"))
									{
										value += (Double.parseDouble(e_catscale.getScale_value()) / 100) * range_value;
										break;
									}
								}
							}
						}
						
						query = "select split_descr, ratio, split_card" +
						" from ecarddb..e_merchant_commission_split where merchant_code = '"+merchant_code+"'";
						
						result = stat.executeQuery(query);
						while(result.next())
						{
							merchant_split = new E_MERCHANT_SPLIT();
							merchant_split.setSplit_descr(""+result.getObject(1));
							
							svalue = "";
							svalue = ""+result.getObject(2);
							d = (Double.parseDouble(svalue) / 100) * value;
							
							merchant_split.setSplit_card(""+result.getObject(3));
							merchant_split.setSvalue(""+d);
							merchant_split.setTrans_count(tran_count);
							merchant_split.setCommission_value(""+value);
							merchant_split.setEtzRatio(svalue);
							
							arr.add(merchant_split);
						}

					}
				}
				else
				{
					query = "select scale_type, scale_value from ecarddb..E_CATSCALE where cat_id = '"+cat_id+"'";
					//System.out.println("ecatscale " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						e_catscale_type = ""+result.getObject(1);
						e_catscale_value = ""+result.getObject(2);
						
						if(e_catscale_type.equals("1"))//means value has to be done by ratio on the total amount
						{
							value = (Double.parseDouble(e_catscale_value) / 100) * Double.parseDouble(total_amount);
						}
						else if(e_catscale_type.equals("0"))//means value has to be done by actual values on the total count
						{
							value = Double.parseDouble(e_catscale_value) * Double.parseDouble(tran_count);
						}
						
						query = "select split_descr, ratio, split_card" +
						" from ecarddb..e_merchant_commission_split where merchant_code = '"+merchant_code+"'";
						
						//System.out.println("merchant commision " + query);
						result = stat.executeQuery(query);
						while(result.next())
						{
							merchant_split = new E_MERCHANT_SPLIT();
							merchant_split.setSplit_descr(""+result.getObject(1));
							
							svalue = "";
							svalue = ""+result.getObject(2);
							d = (Double.parseDouble(svalue) / 100) * value;
							
							merchant_split.setSplit_card(""+result.getObject(3));
							merchant_split.setSvalue(""+d);
							merchant_split.setTrans_count(tran_count);
							merchant_split.setCommission_value(""+value);
							merchant_split.setEtzRatio(svalue);
							//System.out.println("e_catscale commision value " + value + " for " + merchant_code);
							
							arr.add(merchant_split);
						}
					}
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionBackUpEcard(con, result, result1);
		}
		finally
		{
			closeConnectionBackUpEcard(con, result, result1);
		}
		return arr;
	}
	
	
	
	/*Method to get the commission split details by merchant code*/
	public ArrayList getE_MERCHANT_COMMISSION_SPLIT_Bank(String merchant_code, String cat_id, String tran_count, 
			String total_amount, String beginDate, String endDate, String channel, String transcode, String bank_code)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		ArrayList arr_scale = new ArrayList();
		
		E_MERCHANT_SPLIT merchant_split;
		E_CATSCALE e_catscale;
		String svalue = "";
		double d = 0.0; 
		double value = 0.0;
		String e_catscale_value = "";
		String e_catscale_type = "";
		String e_catscale_from = "";
		String e_catscale_to = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		ResultSet result1 = null;
		
		try
		{
			con = connectToBackUpECard();
			stat = con.createStatement();
			
			query = "select count(*) from ecarddb..E_CATSCALE where cat_id = '"+cat_id+"'";
			result = stat.executeQuery(query);
			if(result.next())
			{
				int count = result.getInt(1);
				if(count > 1)//multiple cat scale
				{
					query = "select scale_type, scale_value, scale_from, scale_to from ecarddb..E_CATSCALE where cat_id = '"+cat_id+"'";
					result = stat.executeQuery(query);
					while(result.next())
					{
						e_catscale = new E_CATSCALE();
						
						e_catscale.setScale_type(""+result.getObject(1));
						e_catscale.setScale_value(""+result.getObject(2));
						e_catscale.setScale_from(""+result.getObject(3));
						e_catscale.setScale_to(""+result.getObject(4));
						arr_scale.add(e_catscale);
					}
					
					double range_value = 0.0;
					double range_count = 0.0;
					
					if(arr_scale.size()>0)
					{
						query = "select count(*),trans_amount from ecarddb..e_transaction where merchant_code = '"+merchant_code+"' and channelid= '"+channel+"' and trans_code = '"+transcode+"' and trans_date between('"+beginDate+"') and ('"+endDate+"') and card_num like '"+ bank_code +"%' group by trans_amount";
						result = stat.executeQuery(query);
						while(result.next())
						{
							range_value = 0.0;
							range_count = 0.0;
							
							range_count = Double.parseDouble(""+result.getObject(1));
							range_value = Double.parseDouble(""+result.getObject(2));
							
							for(int i=0;i<arr_scale.size();i++)
							{
								e_catscale = (E_CATSCALE)arr_scale.get(i);
								
								if(range_value >= Double.parseDouble(e_catscale.getScale_from()) && range_value <= Double.parseDouble(e_catscale.getScale_to()))
								{
									if(e_catscale.getScale_type().equals("0"))
									{	
										value += Double.parseDouble(e_catscale.getScale_value()) * range_count;
										break;
									}
									else if(e_catscale.getScale_type().equals("1"))
									{
										value += (Double.parseDouble(e_catscale.getScale_value()) / 100) * range_value;
										break;
									}
								}
							}
						}
						
						query = "select split_descr, ratio, split_card" +
						" from ecarddb..e_merchant_commission_split where merchant_code = '"+merchant_code+"'";
						
						result = stat.executeQuery(query);
						while(result.next())
						{
							merchant_split = new E_MERCHANT_SPLIT();
							merchant_split.setSplit_descr(""+result.getObject(1));
							
							svalue = "";
							svalue = ""+result.getObject(2);
							d = (Double.parseDouble(svalue) / 100) * value;
							
							merchant_split.setSplit_card(""+result.getObject(3));
							merchant_split.setSvalue(""+d);
							merchant_split.setTrans_count(tran_count);
							merchant_split.setCommission_value(""+value);
							merchant_split.setEtzRatio(svalue);
							
							arr.add(merchant_split);
						}

					}
				}
				else
				{
					query = "select scale_type, scale_value from ecarddb..E_CATSCALE where cat_id = '"+cat_id+"'";
					//System.out.println("ecatscale " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						e_catscale_type = ""+result.getObject(1);
						e_catscale_value = ""+result.getObject(2);
						
						if(e_catscale_type.equals("1"))//means value has to be done by ratio on the total amount
						{
							value = (Double.parseDouble(e_catscale_value) / 100) * Double.parseDouble(total_amount);
						}
						else if(e_catscale_type.equals("0"))//means value has to be done by actual values on the total count
						{
							value = Double.parseDouble(e_catscale_value) * Double.parseDouble(tran_count);
						}
						
						query = "select split_descr, ratio, split_card" +
						" from ecarddb..e_merchant_commission_split where merchant_code = '"+merchant_code+"'";
						
						//System.out.println("merchant commision " + query);
						result = stat.executeQuery(query);
						while(result.next())
						{
							merchant_split = new E_MERCHANT_SPLIT();
							merchant_split.setSplit_descr(""+result.getObject(1));
							
							svalue = "";
							svalue = ""+result.getObject(2);
							d = (Double.parseDouble(svalue) / 100) * value;
							
							merchant_split.setSplit_card(""+result.getObject(3));
							merchant_split.setSvalue(""+d);
							merchant_split.setTrans_count(tran_count);
							merchant_split.setCommission_value(""+value);
							merchant_split.setEtzRatio(svalue);
							//System.out.println("e_catscale commision value " + value + " for " + merchant_code);
							
							arr.add(merchant_split);
						}
					}
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionBackUpEcard(con, result, result1);
		}
		finally
		{
			closeConnectionBackUpEcard(con, result, result1);
		}
		return arr;
	}
	
	

	
	/*Method to get the commission split details by merchant code*/
	public ArrayList getE_MERCHANT_COMMISSION_SPLIT2(String merchant_code, String cat_id, String tran_count, 
			String total_amount)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_MERCHANT_SPLIT merchant_split;
		String svalue = "";
		double d = 0.0; 
		double value = 0.0;
		String e_catscale_value = "";
		String e_catscale_type = "";
		String e_catscale_from = "";
		String e_catscale_to = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select count(*) from ecarddb..E_CATSCALE where cat_id = '"+cat_id+"'";
			result = stat.executeQuery(query);
			if(result.next())
			{
				int count = result.getInt(1);
				if(count > 1)//multiple cat scale
				{
					
				}
				else
				{
					query = "select scale_type, scale_value from ecarddb..E_CATSCALE where cat_id = '"+cat_id+"'";
					//System.out.println("ecatscale " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						e_catscale_type = ""+result.getObject(1);
						e_catscale_value = ""+result.getObject(2);
						
						if(e_catscale_type.equals("1"))//means value has to be done by ratio on the total amount
						{
							value = (Double.parseDouble(e_catscale_value) / 100) * Double.parseDouble(total_amount);
						}
						else if(e_catscale_type.equals("0"))//means value has to be done by actual values on the total count
						{
							value = Double.parseDouble(e_catscale_value) * Double.parseDouble(tran_count);
						}
						
						query = "select split_descr, ratio" +
						" from ecarddb..e_merchant_commission_split where merchant_code = '"+merchant_code+"'";
						
						//System.out.println("merchant commision " + query);
						result = stat.executeQuery(query);
						while(result.next())
						{
							merchant_split = new E_MERCHANT_SPLIT();
							merchant_split.setSplit_descr(""+result.getObject(1));
							
							svalue = "";
							svalue = ""+result.getObject(2);
							d = (Double.parseDouble(svalue) / 100) * value;
							
							merchant_split.setSvalue(""+d);
							merchant_split.setTrans_count(tran_count);
							
							arr.add(merchant_split);
						}
					}
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	
	/*Method to get the standard split details by merchant code*/
	public ArrayList getE_STANDARD_SPLIT(String channel, String transcode, String total_fee,
			String merchant_code, String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		ArrayList arr1 = new ArrayList();
		E_STANDARD_SPLIT standard_split;
		double d = 0.0;
		String part_fee = "";
		String split_type = "";
		double fee = 0.0;
		String count = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		ResultSet result1 = null;
		
		try
		{
			con = connectToBackUpECard();
			stat = con.createStatement();

			query = "select channel_id, trans_code, description ,owner_card, part_fee, split_type" +
					" from ecarddb..E_STANDARD_SPLIT where channel_id = '"+channel+"' and trans_code = '"+transcode+"' order by split_type";
			
			//System.out.println("query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				part_fee = "";
				d = 0.0;
				standard_split = new E_STANDARD_SPLIT();
				standard_split.setChannel_id(""+result.getObject(1));
				standard_split.setTrans_code(""+result.getObject(2));
				standard_split.setDescription(""+result.getObject(3));
				standard_split.setOwner_card(""+result.getObject(4));

				part_fee = ""+result.getObject(5);
				split_type = ""+result.getObject(6);
				
				if(split_type.equals("0"))//ratio
				{
					d = (Double.parseDouble(part_fee)/100) * Double.parseDouble(total_fee);
				}
				else if(split_type.equals("1"))//actual value
				{	
					
					query = "select count(*) from ecarddb..E_TRANSACTION " +
					"where merchant_code = '"+merchant_code+"' and trans_date between('"+start_dt+"') and ('"+end_dt+"') and" +
							" channelid= '"+channel+"' and trans_code = '"+transcode+"' and fee >0";
					
					System.out.println("standard query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						count = ""+result.getObject(1);
						d = (Double.parseDouble(part_fee)) * Double.parseDouble(count);
					}
				}
				
				standard_split.setPart_fee(""+d);
				standard_split.setSplit_type(split_type);
				standard_split.setEtz_ratio(part_fee);
				
				arr.add(standard_split);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionBackUpEcard(con, result, result1);
		}
		finally
		{
			closeConnectionBackUpEcard(con, result, result1);
		}
		return arr;
	}
	
	
	/*Method to get the standard split details by merchant code*/
	public ArrayList getE_STANDARD_SPLIT_Bank(String channel, String transcode, String total_fee,
			String merchant_code, String start_dt, String end_dt, String bank_code)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		ArrayList arr1 = new ArrayList();
		E_STANDARD_SPLIT standard_split;
		double d = 0.0;
		String part_fee = "";
		String split_type = "";
		double fee = 0.0;
		String count = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		ResultSet result1 = null;
		
		try
		{
			con = connectToBackUpECard();
			stat = con.createStatement();

			query = "select channel_id, trans_code, description ,owner_card, part_fee, split_type" +
					" from ecarddb..E_STANDARD_SPLIT where channel_id = '"+channel+"' and trans_code = '"+transcode+"' order by split_type";
			
			//System.out.println("query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				part_fee = "";
				d = 0.0;
				standard_split = new E_STANDARD_SPLIT();
				standard_split.setChannel_id(""+result.getObject(1));
				standard_split.setTrans_code(""+result.getObject(2));
				standard_split.setDescription(""+result.getObject(3));
				standard_split.setOwner_card(""+result.getObject(4));

				part_fee = ""+result.getObject(5);
				split_type = ""+result.getObject(6);
				
				if(split_type.equals("0"))//ratio
				{
					d = (Double.parseDouble(part_fee)/100) * Double.parseDouble(total_fee);
				}
				else if(split_type.equals("1"))//actual value
				{	
					query = "select count(*) from ecarddb..E_TRANSACTION " +
					"where merchant_code = '"+merchant_code+"' and trans_date between('"+start_dt+"') and ('"+end_dt+"') and" +
							" channelid= '"+channel+"' and trans_code = '"+transcode+"' and card_num like '"+ bank_code +"%' and fee >0";
					
					System.out.println("standard query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						count = ""+result.getObject(1);
						d = (Double.parseDouble(part_fee)) * Double.parseDouble(count);
					}
				}
				
				standard_split.setPart_fee(""+d);
				standard_split.setSplit_type(split_type);
				standard_split.setEtz_ratio(part_fee);
				
				arr.add(standard_split);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionBackUpEcard(con, result, result1);
		}
		finally
		{
			closeConnectionBackUpEcard(con, result, result1);
		}
		return arr;
	}
	
	
	
	
	
	/*Method to get the etransaction details by date range , merchant code and merchant name*/
	public ArrayList getE_TRANSACTIONDetailByDateAndMerchant(String start_dt, String end_dt, 
			String merchant_code, String channel, String transcode , boolean check_null_settle_batch)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		HashNumber hn = new HashNumber();
		
		E_TRANSACTION transaction;
		String channel_nm = "";
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			if(merchant_code.trim().length() > 0)
			{
				if(check_null_settle_batch)
				{
					query = "select card_num, trans_no, trans_descr, trans_amount, trans_date, (select channel_name from ecarddb..e_channel where channel_id = ecarddb..e_transaction.channelid),merchant_code from ecarddb..E_TRANSACTION " +
						"where trans_date between('"+start_dt+"') and ('"+end_dt+"') and merchant_code = '"+merchant_code +"' and channelid like '%"+channel+"%' and trans_code like '%"+transcode+"%' and settle_batch=null order by trans_date desc";
				}
				else
				{
					query = "select card_num, trans_no, trans_descr, trans_amount, trans_date, (select channel_name from ecarddb..e_channel where channel_id = ecarddb..e_transaction.channelid),merchant_code from ecarddb..E_TRANSACTION " +
					"where trans_date between('"+start_dt+"') and ('"+end_dt+"') and merchant_code = '"+merchant_code +"' and channelid like '%"+channel+"%' and trans_code like '%"+transcode+"%' order by trans_date desc";
				}
			}
			
			
			//System.out.println("getE_TRANSACTIONDetailByDateAndMerchant " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				transaction = new E_TRANSACTION();
				
				transaction.setCounter(""+counter);
				transaction.setCard_num(hn.hashStringValue(""+result.getObject(1), 9, 4) );
				transaction.setTrans_no(""+result.getObject(2));
				transaction.setTrans_desc(""+result.getObject(3));
				transaction.setTrans_amount(""+result.getObject(4));
				transaction.setTrans_date(""+result.getObject(5));
				
				channel_nm = ""+result.getObject(6);

				if(channel_nm.equals("null"))
					transaction.setChannelid("");
				else
					transaction.setChannelid(channel_nm);
				
				transaction.setMerchat_code(""+result.getObject(7));
				arr.add(transaction);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Method to get the etransaction details by date range , merchant code and merchant name and bank*/
	public ArrayList getE_TRANSACTIONDetailByDateAndMerchant_Bank(String start_dt, String end_dt, 
			String merchant_code, String channel, String transcode , boolean check_null_settle_batch, String bank_code)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		HashNumber hn = new HashNumber();
		
		E_TRANSACTION transaction;
		String channel_nm = "";
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			if(merchant_code.trim().length() > 0)
			{
				if(check_null_settle_batch)
				{
					query = "select card_num, trans_no, trans_descr, trans_amount, trans_date, (select channel_name from ecarddb..e_channel where channel_id = ecarddb..e_transaction.channelid),merchant_code from ecarddb..E_TRANSACTION " +
						"where trans_date between('"+start_dt+"') and ('"+end_dt+"') and merchant_code = '"+merchant_code +"' and channelid like '%"+channel+"%' and trans_code like '%"+transcode+"%' and settle_batch=null order by trans_date desc";
				}
				else
				{
					query = "select card_num, trans_no, trans_descr, trans_amount, trans_date, (select channel_name from ecarddb..e_channel where channel_id = ecarddb..e_transaction.channelid),merchant_code from ecarddb..E_TRANSACTION " +
					"where trans_date between('"+start_dt+"') and ('"+end_dt+"') and merchant_code = '"+merchant_code +"' and channelid like '%"+channel+"%' and trans_code like '%"+transcode+"%' and card_num like '"+bank_code+"%' order by trans_date desc";
				}
			}
			
			
			//System.out.println("getE_TRANSACTIONDetailByDateAndMerchant " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				transaction = new E_TRANSACTION();
				
				transaction.setCounter(""+counter);
				transaction.setCard_num(hn.hashStringValue(""+result.getObject(1), 6, 4) );
				transaction.setTrans_no(""+result.getObject(2));
				transaction.setTrans_desc(""+result.getObject(3));
				transaction.setTrans_amount(""+result.getObject(4));
				transaction.setTrans_date(""+result.getObject(5));
				
				channel_nm = ""+result.getObject(6);

				if(channel_nm.equals("null"))
					transaction.setChannelid("");
				else
					transaction.setChannelid(channel_nm);
				
				transaction.setMerchat_code(""+result.getObject(7));
				arr.add(transaction);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Method to get the like e_transaction by date , channel and trans code*/
	public ArrayList getE_TRANSACTIONByDateAndTransCodeAndChannel(String start_dt, String end_dt, 
			String channel, String transcode)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION transaction;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		ResultSet result1 = null;

		try
		{
			con = connectToBackUpECard();
			stat = con.createStatement();
			
			if(transcode.equalsIgnoreCase("P"))//if its payment
			{
				query = "select distinct merchant_code," +
						" (select merchant_name from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code)," +
						" sum(trans_amount),count(merchant_code)," +
						"(select special_split from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code)," +
						"(select cat_id from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code)," +
						"sum(fee) from ecarddb..E_TRANSACTION " +
				"where trans_date between('"+start_dt+"') and ('"+end_dt+"') and channelid= '"+channel+"' and trans_code = '"+transcode+"' and char_length(merchant_code)=10 group by MERCHANT_CODE";
			}
			else
			{
				query = "select distinct merchant_code," +
						" (select merchant_name from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code)," +
						" sum(trans_amount),count(merchant_code)," +
						"(select special_split from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code)," +
						"(select cat_id from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code)," +
						"sum(fee) from ecarddb..E_TRANSACTION " +
				"where trans_date between('"+start_dt+"') and ('"+end_dt+"') and channelid= '"+channel+"' and trans_code = '"+transcode+"' group by MERCHANT_CODE";
			}
			
			System.out.println("merchant summary " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				transaction = new E_TRANSACTION();
				
				transaction.setMerchat_code(result.getString(1));
				transaction.setMerchant_descr(result.getString(2));
				transaction.setTotal_amount(""+result.getObject(3));
				transaction.setTransaction_count(""+result.getObject(4));
				transaction.setMerchant_split_type(""+result.getObject(5));
				transaction.setMerchant_cat_id(""+result.getObject(6));
				transaction.setTotal_fee(""+result.getObject(7));
				
				arr.add(transaction);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionBackUpEcard(con, result, result1);
		}
		finally
		{
			closeConnectionBackUpEcard(con, result, result1);
		}
		return arr;
	}
	
	
	
	/*Method to get the like e_transaction by date , channel and trans code specially used while displaying commision*/
	public ArrayList getE_TRANSACTIONByDateAndTransCodeAndChannelForCommission(String start_dt, String end_dt, 
			String channel, String transcode)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION transaction;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		ResultSet result1 = null;

		try
		{
			con = connectToBackUpECard();
			stat = con.createStatement();
			
			if(transcode.equalsIgnoreCase("P"))//if its payment
			{
				query = "select distinct merchant_code," +
						" (select merchant_name from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code)," +
						" sum(trans_amount),count(merchant_code)," +
						"(select special_split from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code)," +
						"(select cat_id from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code)," +
						"sum(fee) from ecarddb..E_TRANSACTION " +
				"where trans_date between('"+start_dt+"') and ('"+end_dt+"') and channelid= '"+channel+"' and trans_code = '"+transcode+"' and char_length(merchant_code)=10 group by MERCHANT_CODE";
			}
			else
			{
				query = "select distinct merchant_code," +
						" (select merchant_name from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code)," +
						" sum(trans_amount),count(merchant_code)," +
						"(select special_split from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code)," +
						"(select cat_id from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code)," +
						"sum(fee) from ecarddb..E_TRANSACTION " +
				"where trans_date between('"+start_dt+"') and ('"+end_dt+"') and channelid= '"+channel+"' and trans_code = '"+transcode+"' group by MERCHANT_CODE";
			}
			
			System.out.println("merchant summary " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				transaction = new E_TRANSACTION();
				
				transaction.setMerchat_code(result.getString(1));
				transaction.setMerchant_descr(result.getString(2));
				transaction.setTotal_amount(""+result.getObject(3));
				transaction.setTransaction_count(""+result.getObject(4));
				transaction.setMerchant_split_type(""+result.getObject(5));
				transaction.setMerchant_cat_id(""+result.getObject(6));
				transaction.setTotal_fee(""+result.getObject(7));
				
				arr.add(transaction);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionBackUpEcard(con, result, result1);
		}
		finally
		{
			closeConnectionBackUpEcard(con, result, result1);
		}
		return arr;
	}
	
	
	
	
	/*Method to get the like e_transaction by date , channel and trans code*/
	public ArrayList getE_TRANSACTIONByDateAndBankCodeAndTransCodeAndChannel(String start_dt, String end_dt, 
			String channel, String transcode, String bank_code)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION transaction;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		ResultSet result1 = null;

		try
		{
			con = connectToBackUpECard();
			stat = con.createStatement();
			
			if(transcode.equalsIgnoreCase("P"))//if its payment
			{
				query = "select distinct merchant_code, (select merchant_name from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code), sum(trans_amount),count(merchant_code),(select special_split from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code),(select cat_id from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code),sum(fee) from ecarddb..E_TRANSACTION " +
				"where trans_date between('"+start_dt+"') and ('"+end_dt+"') and channelid= '"+channel+"' and trans_code = '"+transcode+"' and char_length(merchant_code)=10 and card_num like '"+bank_code+"%' group by MERCHANT_CODE";
			}
			else
			{
				query = "select distinct merchant_code, (select merchant_name from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code), sum(trans_amount),count(merchant_code),(select special_split from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code),(select cat_id from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code),sum(fee) from ecarddb..E_TRANSACTION " +
				"where trans_date between('"+start_dt+"') and ('"+end_dt+"') and channelid= '"+channel+"' and trans_code = '"+transcode+"' and card_num like '"+bank_code+"%' group by MERCHANT_CODE";
			}
			
			System.out.println("merchant summary " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				transaction = new E_TRANSACTION();
				
				transaction.setMerchat_code(result.getString(1));
				transaction.setMerchant_descr(result.getString(2));
				transaction.setTotal_amount(""+result.getObject(3));
				transaction.setTransaction_count(""+result.getObject(4));
				transaction.setMerchant_split_type(""+result.getObject(5));
				transaction.setMerchant_cat_id(""+result.getObject(6));
				transaction.setTotal_fee(""+result.getObject(7));
				
				arr.add(transaction);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionBackUpEcard(con, result, result1);
		}
		finally
		{
			closeConnectionBackUpEcard(con, result, result1);
		}
		return arr;
	}
	
	
	
	/*Method to get the like e_transaction by date , channel and trans code and account_id*/
	public ArrayList getE_TRANSACTIONByDateAndTransCodeAndChannelAndAccountID(String start_dt, String end_dt, 
			String channel, String transcode, String account_id)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION transaction;
		Connection con = null;
		Connection con1 = null;
		Statement stat = null;
		Statement stat1 = null;
		ResultSet result = null;
		ResultSet result1 = null;
		
		String myMerchants = "";
		String approstrophe = "'";

		try
		{
			con = connectToBackUpECard();
			stat = con.createStatement();
			
			
			con1 = connectToSupportLog();
			stat1 = con1.createStatement();
			
			query = "select merchant_code from support_merchant_mapping where username = '"+account_id+"'";
			result1 = stat1.executeQuery(query);
			while(result1.next())
			{
				myMerchants += approstrophe + ""+result1.getObject(1) + approstrophe + ",";
			}
			
			if(myMerchants.trim().length()>0)
			{
				myMerchants = myMerchants.substring(0, myMerchants.lastIndexOf(","));
			}
			else
			{
				myMerchants = "0";
			}
			
			
			if(transcode.equalsIgnoreCase("P"))//if its payment
			{
				query = "select distinct merchant_code, (select merchant_name from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code), sum(trans_amount),count(merchant_code),(select special_split from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code),(select cat_id from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code),sum(fee) from ecarddb..E_TRANSACTION " +
				"where merchant_code in ("+myMerchants+") and trans_date between('"+start_dt+"') and ('"+end_dt+"') and channelid like '"+channel+"%' and trans_code = '"+transcode+"' and char_length(merchant_code)=10 group by MERCHANT_CODE";
			}
			else
			{
				query = "select distinct merchant_code, (select merchant_name from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code), sum(trans_amount),count(merchant_code),(select special_split from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code),(select cat_id from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code),sum(fee) from ecarddb..E_TRANSACTION " +
				"where merchant_code in ("+myMerchants+") and trans_date between('"+start_dt+"') and ('"+end_dt+"') and channelid like '"+channel+"%' and trans_code = '"+transcode+"' group by MERCHANT_CODE";
			}
			
			System.out.println("merchant summary " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				transaction = new E_TRANSACTION();
				
				transaction.setMerchat_code(result.getString(1));
				transaction.setMerchant_descr(result.getString(2));
				transaction.setTotal_amount(""+result.getObject(3));
				transaction.setTransaction_count(""+result.getObject(4));
				transaction.setMerchant_split_type(""+result.getObject(5));
				transaction.setMerchant_cat_id(""+result.getObject(6));
				transaction.setTotal_fee(""+result.getObject(7));
				
				arr.add(transaction);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionBackUpEcard(con, result, result1);
			closeConnectionSupportLogDB(con1, result1);
		}
		finally
		{
			closeConnectionBackUpEcard(con, result, result1);
			closeConnectionSupportLogDB(con1, result1);
		}
		return arr;
	}
	
	
	
	
	/*Method to get the total commission / count by transcode*/
	public ArrayList getE_TRANSACTIONByDateAndTransCode_Commission(String start_dt, String end_dt, 
			 String transcode, String channel)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION transaction;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		ResultSet result1 = null;

		try
		{
			con = connectToBackUpECard();
			stat = con.createStatement();
			
			if(transcode.equalsIgnoreCase("P"))//if its payment
			{
				query = "select distinct merchant_code, (select merchant_name from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code), sum(trans_amount),count(merchant_code),(select special_split from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code),(select cat_id from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code),sum(fee) from ecarddb..E_TRANSACTION " +
				"where trans_date between('"+start_dt+"') and ('"+end_dt+"') and channelid= '"+channel+"' and trans_code = '"+transcode+"' and char_length(merchant_code)=10 group by MERCHANT_CODE";
			}
			else
			{
				query = "select distinct merchant_code, (select merchant_name from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code), sum(trans_amount),count(merchant_code),(select special_split from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code),(select cat_id from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code),sum(fee) from ecarddb..E_TRANSACTION " +
				"where trans_date between('"+start_dt+"') and ('"+end_dt+"') and channelid= '"+channel+"' and trans_code = '"+transcode+"' group by MERCHANT_CODE";
			}
			
			System.out.println("getE_TRANSACTIONByDateAndTransCode_Commission " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				transaction = new E_TRANSACTION();
				
				transaction.setMerchat_code(result.getString(1));
				transaction.setMerchant_descr(result.getString(2));
				transaction.setTotal_amount(""+result.getObject(3));
				transaction.setTransaction_count(""+result.getObject(4));
				transaction.setMerchant_split_type(""+result.getObject(5));
				transaction.setMerchant_cat_id(""+result.getObject(6));
				transaction.setTotal_fee(""+result.getObject(7));
				
				arr.add(transaction);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionBackUpEcard(con, result, result1);
		}
		finally
		{
			closeConnectionBackUpEcard(con, result, result1);
		}
		return arr;
	}
	
	
	/*Method to get the etransaction summary by date range , merchant code and merchant name*/
	public ArrayList getE_TRANSACTIONSummaryByDateAndMerchant(String start_dt, String end_dt, 
			String merchant_code, String channel, String transcode)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION transaction;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select sum(trans_amount), count(globalid) from ecarddb..E_TRANSACTION " +
					"where trans_date between('"+start_dt+"') and ('"+end_dt+"') and merchant_code= '"+merchant_code +"' and channelid like '%"+channel+"%' and trans_code like '%"+transcode+"%' and settle_batch=null";
			
			//System.out.println("getE_TRANSACTIONSummaryByDateAndMerchant " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				transaction = new E_TRANSACTION();
				
				transaction.setTrans_amount(result.getString(1));
				transaction.setTransaction_count(result.getString(2));
				
				arr.add(transaction);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Method to get the e_settlement download bk details by date range , merchant code and merchant name*/
	public ArrayList getE_SETTLEMENTDOWNLOADBKDetailByDateAndMerchant(String settle_batch, 
			String merchant_code, String channel, String transcode)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		HashNumber hn = new HashNumber();
		E_SETTLEMENTDOWNLOAD_BK e_settlement;
		String channel_nm = "";
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			
			if(merchant_code.trim().length() > 0)
			{
				query = "select card_num, trans_no, trans_descr, trans_amount, trans_date,(select channel_name from ecarddb..e_channel where channel_id = ecarddb..e_settlement_download_bk.channelid),merchant_code,settle_batch from ecarddb..E_SETTLEMENT_DOWNLOAD_BK " +
				"where settle_batch = '"+settle_batch+"' and merchant_code = '"+merchant_code +"'  and channelid like '%"+channel+"%' and trans_code like '%"+transcode+"%' order by trans_date desc";
			}
			
			//System.out.println("getE_SETTLEMENTDOWNLOADBKDetailByDateAndMerchant " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				e_settlement = new E_SETTLEMENTDOWNLOAD_BK();
				
				e_settlement.setCounter(""+counter);
				//e_settlement.setCard_num(""+result.getObject(1));
				e_settlement.setCard_num(hn.hashStringValue(""+result.getObject(1), 9, 4) );
				e_settlement.setTrans_no(""+result.getObject(2));
				e_settlement.setTrans_desc(""+result.getObject(3));
				e_settlement.setTrans_amount(""+result.getObject(4));
				e_settlement.setTrans_date(""+result.getObject(5));
				
				channel_nm = ""+result.getObject(6);
				
				if(channel_nm.equals("null"))
					e_settlement.setChannelid("");
				else
					e_settlement.setChannelid(channel_nm);
				
				e_settlement.setMerchat_code(""+result.getObject(7));
				e_settlement.setSettle_batch(""+result.getObject(8));
				
				arr.add(e_settlement);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	/*Method to get the e_settlement download bk summary by date range , merchant code and merchant name*/
	public ArrayList getE_SETTLEMENTDOWNLOADBKSummaryByDateAndMerchant(String settle_batch, 
			String merchant_code, String channel, String transcode)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_SETTLEMENTDOWNLOAD_BK e_settlement;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select sum(trans_amount), count(globalid) from ecarddb..E_SETTLEMENT_DOWNLOAD_BK where settle_batch = '"+settle_batch+"'" +
			" and merchant_code = '"+merchant_code +"' and channelid like '%"+channel+"%' and trans_code like '%"+transcode+"%' ";
	
			
			//System.out.println("getE_SETTLEMENTDOWNLOADBKSummaryByDateAndMerchant " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				e_settlement = new E_SETTLEMENTDOWNLOAD_BK();
				
				e_settlement.setTrans_amount(result.getString(1));
				e_settlement.setTransaction_count(result.getString(2));
				e_settlement.setSettle_batch(settle_batch);
				
				arr.add(e_settlement);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Method to get the e_settlement download bk*/
	public ArrayList getE_SETTLEMENTDOWNLOADBKBy058(String card_num, 
			String merchant_code, String transcode, String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_SETTLEMENTDOWNLOAD_BK e_settlement;
		E_SETTLE_BATCH e_settle;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select batch_id, batch_date, start_date, end_date, sum(trans_amount)" +
					" from E_SETTLEMENT_DOWNLOAD_BK left outer join e_settle_batch on e_settlement_download_bk.settle_batch = e_settle_batch.batch_id" +
					" where card_num not like '"+card_num+"%' and  merchant_code like '"+merchant_code+"%'" +
					" and trans_code = '"+transcode+"' and batch_date between ('"+start_dt+"') and ('"+end_dt+"')" +
					" group by settle_batch, batch_date, start_date, end_date";
			
			System.out.println("getE_SETTLEMENTDOWNLOADBKBy058 " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				e_settle = new E_SETTLE_BATCH();
				
				e_settle.setBatch_id(result.getString(1));
				e_settle.setBatch_date(result.getString(2));
				e_settle.setStart_date(result.getString(3));
				e_settle.setEnd_date(result.getString(4));
				e_settle.setTotal_amount(result.getString(5));
				
				arr.add(e_settle);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Method to get the e_settlement download bk*/
	public ArrayList getE_SETTLEMENTDOWNLOADBKBy058Details(String settle_batch, 
			String transcode, String card_num, String merchant_code)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_SETTLEMENTDOWNLOAD_BK e_settlement;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select card_num, trans_no, trans_descr, trans_date," +
					" (select channel_name from ecarddb..e_channel where channel_id = ecarddb..e_settlement_download_bk.channelid)," +
					" merchant_code,trans_amount from e_settlement_download_bk where" +
					" settle_batch = '"+settle_batch+"' and trans_code = '"+transcode+"' and " +
					" card_num not like '"+card_num+"%' and  merchant_code like '"+merchant_code+"%' ";
			
			
			System.out.println("getE_SETTLEMENTDOWNLOADBKBy058Details " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				e_settlement = new E_SETTLEMENTDOWNLOAD_BK();
				
				e_settlement.setCard_num(result.getString(1));
				e_settlement.setTrans_no(result.getString(2));
				e_settlement.setTrans_desc(result.getString(3));
				e_settlement.setTrans_date(result.getString(4));
				e_settlement.setChannelid(result.getString(5));
				e_settlement.setMerchat_code(result.getString(6));
				e_settlement.setTrans_amount(result.getString(7));
				
				
				arr.add(e_settlement);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	
	/*Method to get the merchants by merchant code*/
	public ArrayList getE_MERCHANTByCode(String merchant_code)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_MERCHANT e_merchant;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select merchant_code, merchant_acct, merchant_pin, merchant_name from ecarddb..E_MERCHANT " +
					"where merchant_name like '"+merchant_code +"%' ";
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				e_merchant = new E_MERCHANT();
				
				e_merchant.setMerchant_code(result.getString(1));
				e_merchant.setMerchant_acct(result.getString(2));
				e_merchant.setMerchant_pin(result.getString(3));
				e_merchant.setMerchant_name(result.getString(4));
				
				arr.add(e_merchant);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Method to get the merchant name by merchant code*/
	public String getE_MERCHANTNameByCode(String merchant_code)
	{
		String query = "";
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select merchant_name from ecarddb..E_MERCHANT " +
					"where merchant_code = '"+merchant_code +"' ";
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				message = result.getString(1);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return message;
	}
	
	/*Method to get the merchants by merchant code or merchant name*/
	public ArrayList getE_MERCHANTByCodeOrName(String merchant_code_name)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_MERCHANT e_merchant;
		Connection con = null;
		Connection con1 = null;
		Statement stat = null;
		Statement stat1 = null;
		ResultSet result = null;
		ResultSet result1 = null;
		
		String myMerchants = "";
		String approstrophe = "'";
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			
			con1 = connectToSupportLog();
			stat1 = con1.createStatement();
			
			query = "select merchant_code from support_merchant_mapping";
			result1 = stat1.executeQuery(query);
			while(result1.next())
			{
				myMerchants += approstrophe + ""+result1.getObject(1) + approstrophe + ",";
			}
			
			if(myMerchants.trim().length()>0)
			{
				myMerchants = myMerchants.substring(0, myMerchants.lastIndexOf(","));
			}
			else
			{
				myMerchants = "0";
			}
			
			query = "select merchant_code, merchant_acct, merchant_pin, merchant_name from ecarddb..E_MERCHANT " +
					"where (merchant_code like '"+merchant_code_name +"%' or merchant_name like '"+merchant_code_name+"%') and merchant_code not in("+myMerchants+") ";
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				e_merchant = new E_MERCHANT();
				
				e_merchant.setMerchant_code(result.getString(1));
				e_merchant.setMerchant_acct(result.getString(2));
				e_merchant.setMerchant_pin(result.getString(3));
				e_merchant.setMerchant_name(result.getString(4));
				
				arr.add(e_merchant);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
			closeConnectionSupportLogDB(con1, result1);
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeConnectionSupportLogDB(con1, result1);
		}
		return arr;
	}
	
	
	/*Method to get the e_settle batch*/
	public ArrayList getE_SETTLE_BATCH(String start_dt, String end_dt, String merchant_code,
			String channel, String transcode)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_SETTLE_BATCH e_settle;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		Statement stat1 = null;
		
		ResultSet result = null;
		ResultSet result1 = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			stat1 = con.createStatement();
			
			query = "select batch_id, batch_date, closed, start_date, end_date from ecarddb..E_SETTLE_BATCH " +
					"where start_date between('"+start_dt+"') and ('"+end_dt+"')";
			
			//System.out.println("getE_SETTLE_BATCH " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				e_settle = new E_SETTLE_BATCH();
				
				e_settle.setCounter(""+counter);
				e_settle.setBatch_id(result.getString(1));
				e_settle.setBatch_date(result.getString(2));
				e_settle.setClosed(result.getString(3));
				e_settle.setStart_date(result.getString(4));
				e_settle.setEnd_date(result.getString(5));
				
			
				query = "select sum(trans_amount), count(globalid) from ecarddb..E_SETTLEMENT_DOWNLOAD_BK where settle_batch = '"+e_settle.getBatch_id()+"'" +
				" and merchant_code = '"+merchant_code +"' and channelid like '%"+channel+"%' and trans_code like '%"+transcode+"%' ";
				
				result1 = stat1.executeQuery(query);
				while(result1.next())
				{
					e_settle.setTotal_amount(result1.getString(1));
					e_settle.setTotal_count(result1.getString(2));
				}
				
				arr.add(e_settle);
			}
			
			/*for(int i=0;i<arr.size();i++)
			{
				e_settle = (E_SETTLE_BATCH)arr.get(i);
				query = "select sum(trans_amount), count(globalid) from ecarddb..E_SETTLEMENT_DOWNLOAD_BK where settle_batch = '"+e_settle.getBatch_id()+"'" +
				" and merchant_code = '"+merchant_code +"' and channelid like '%"+channel+"%' and trans_code like '%"+transcode+"%' ";
				
				result = stat.executeQuery(query);
				while(result.next())
				{
					e_settle.setTotal_amount(result.getString(1));
					e_settle.setTotal_count(result.getString(2));
				}
				
				arr.add(e_settle);
			}*/
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	
	/*Method to get the transactions by date and calculate the fee*/
	public ArrayList getRevenueE_TRANSACTION(String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION transaction;
		String[] channel = {"Web Connect","Mobile Money"};
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			for(int i=0;i<channel.length;i++)
			{
				if(channel[i].equals("Web Connect"))
				{
					query = "select sum(trans_amount),count(merchant_code) from ecarddb..E_TRANSACTION " +
					"where trans_date between('"+start_dt+"') and ('"+end_dt+"') and channelid like '01%' and trans_code = 'P' ";
				}
				else if(channel[i].equals("Mobile Commerce"))
				{
					query = "select sum(trans_amount),count(merchant_code) from ecarddb..E_TRANSACTION " +
					"where trans_date between('"+start_dt+"') and ('"+end_dt+"') and channelid like '02%' ";
				}
				else if(channel[i].equals("Mobile Money"))
				{
					query = "select sum(trans_amount),count(merchant_code) from ecarddb..E_TRANSACTION " +
					"where trans_date between('"+start_dt+"') and ('"+end_dt+"') and channelid like '02%' ";
				}
				else if(channel[i].equals("Card Loads"))//card load
				{
					query = "select sum(trans_amount),count(merchant_code) from ecarddb..E_TRANSACTION " +
					"where trans_date between('"+start_dt+"') and ('"+end_dt+"') and channelid like '05%' and trans_code = 'D'";
				}
				else if(channel[i].equals("Card UnLoads"))//card load
				{
					query = "select sum(trans_amount),count(merchant_code) from ecarddb..E_TRANSACTION " +
					"where trans_date between('"+start_dt+"') and ('"+end_dt+"') and channelid like '05%' and trans_code = 'U'";
				}
				else if(channel[i].equals("Payoutlet Collections"))
				{
					query = "select sum(trans_amount),count(merchant_code) from ecarddb..E_TRANSACTION " +
					"where trans_date between('"+start_dt+"') and ('"+end_dt+"') and channelid like '05%' and trans_code = 'P'";
				}
				
				//System.out.println("merchant summary " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					transaction = new E_TRANSACTION();

					transaction.setMerchant_descr(channel[i]);
					transaction.setTotal_amount(""+result.getObject(1));
					transaction.setTransaction_count(""+result.getObject(2));
					arr.add(transaction);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Method to get the transactions by date and calculate the fee*/
	public ArrayList getTransactionFee(String start_dt, String end_dt,
			String channel, String transcode)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION transaction;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Method to get the transactions grouped by bank by date*/
	public ArrayList getE_TRANSACTIONGroupByBankCode(String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION transaction;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		ResultSet result1 = null;
		
		try
		{
			con = connectToBackUpECard();
			stat = con.createStatement();

			query = "select substring (card_num,1,3), count(*) as total_count, sum(trans_amount) as total_amount  from ecarddb..e_transaction  where trans_date between('"+start_dt+"') and ('"+end_dt+"') group by substring (card_num,1,3)";	
			//System.out.println("query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				transaction = new E_TRANSACTION();

				transaction.setBank_code(""+result.getObject(1));
				transaction.setTransaction_count(""+result.getObject(2));
				transaction.setTotal_amount(""+result.getObject(3));
				arr.add(transaction);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionBackUpEcard(con, result, result1);
		}
		finally
		{
			closeConnectionBackUpEcard(con, result, result1);
		}
		return arr;
	}
	
	
	/*Method to get the transactions grouped by trans code and by date*/
	public ArrayList getE_TRANSACTIONGroupByTransCode(String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION transaction;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		ResultSet result1 = null;
		
		try
		{
			con = connectToBackUpECard();
			stat = con.createStatement();

			query = "select distinct trans_code, count(*) as total_count, sum(trans_amount) as total_amount  from ecarddb..e_transaction  where trans_date between('"+start_dt+"') and ('"+end_dt+"') group by trans_code";	
			System.out.println("getE_TRANSACTIONGroupByTransCode " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				transaction = new E_TRANSACTION();

				transaction.setTrans_code(""+result.getObject(1));
				transaction.setTransaction_count(""+result.getObject(2));
				transaction.setTotal_amount(""+result.getObject(3));
				if(transaction.getTrans_code().equalsIgnoreCase("N") || transaction.getTrans_code().equalsIgnoreCase("X") || transaction.getTrans_code().equalsIgnoreCase("I") || transaction.getTrans_code().equalsIgnoreCase("C"));
				else
				{
					arr.add(transaction);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionBackUpEcard(con, result, result1);
		}
		finally
		{
			closeConnectionBackUpEcard(con, result, result1);
		}
		return arr;
	}
	
	
	/*Method to get the transactions grouped by trans code and by date and account_id*/
	public ArrayList getE_TRANSACTIONByAccountIDGroupByTransCode(String start_dt, String end_dt, String account_id)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION transaction;
		Connection con = null;
		Connection con1 = null;
		Statement stat = null;
		Statement stat1 = null;
		ResultSet result = null;
		ResultSet result1 = null;
		String myMerchants = "";
		String approstrophe = "'";
		
		try
		{
			con = connectToBackUpECard();
			stat = con.createStatement();
			
			con1 = connectToSupportLog();
			stat1 = con1.createStatement();
			
			query = "select merchant_code from support_merchant_mapping where username = '"+account_id+"'";
			result1 = stat1.executeQuery(query);
			while(result1.next())
			{
				myMerchants += approstrophe + ""+result1.getObject(1) + approstrophe + ",";
			}
			
			if(myMerchants.trim().length()>0)
			{
				myMerchants = myMerchants.substring(0, myMerchants.lastIndexOf(","));
			}
			else
			{
				myMerchants = "0";
			}

			query = "select distinct trans_code, count(*) as total_count, sum(trans_amount) as total_amount  from ecarddb..e_transaction  where merchant_code in ("+myMerchants+") and trans_date between('"+start_dt+"') and ('"+end_dt+"') group by trans_code";	
			System.out.println("getE_TRANSACTIONGroupByTransCode " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				transaction = new E_TRANSACTION();

				transaction.setTrans_code(""+result.getObject(1));
				transaction.setTransaction_count(""+result.getObject(2));
				transaction.setTotal_amount(""+result.getObject(3));
				if(transaction.getTrans_code().equalsIgnoreCase("N") || transaction.getTrans_code().equalsIgnoreCase("X") || transaction.getTrans_code().equalsIgnoreCase("I") || transaction.getTrans_code().equalsIgnoreCase("C"));
				else
				{
					arr.add(transaction);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionBackUpEcard(con, result, result1);
			closeConnectionSupportLogDB(con1, result1);
		}
		finally
		{
			closeConnectionBackUpEcard(con, result, result1);
			closeConnectionSupportLogDB(con1, result1);
		}
		return arr;
	}
	
	/*Method to get the transactions by bank code grouped by trans code and by date*/
	public ArrayList getE_TRANSACTIONByBankCodeGroupByTransCode(String bank_code,String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION transaction;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		ResultSet result1 = null;
		
		try
		{
			con = connectToBackUpECard();
			stat = con.createStatement();

			query = "select distinct trans_code, count(*) as total_count, sum(trans_amount) as total_amount  from ecarddb..e_transaction  where trans_date between('"+start_dt+"') and ('"+end_dt+"') and card_num like '"+ bank_code +"%'  group by trans_code";
			//System.out.println("getE_TRANSACTIONByBankCodeGroupByTransCode " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				transaction = new E_TRANSACTION();

				transaction.setBank_code(bank_code);
				transaction.setTrans_code(""+result.getObject(1));
				transaction.setTransaction_count(""+result.getObject(2));
				transaction.setTotal_amount(""+result.getObject(3));
				if(transaction.getTrans_code().equalsIgnoreCase("N") || transaction.getTrans_code().equalsIgnoreCase("X") || transaction.getTrans_code().equalsIgnoreCase("I") || transaction.getTrans_code().equalsIgnoreCase("C"));
				else
				{
					arr.add(transaction);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionBackUpEcard(con, result, result1);
		}
		finally
		{
			closeConnectionBackUpEcard(con, result, result1);
		}
		return arr;
	}
	
	
	/*Method to get the transactions by  trans_code grouped by channel and by date*/
	public ArrayList getE_TRANSACTIONByTransCodeGroupByChannel(String trans_code,String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION transaction;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		ResultSet result1 = null;
		
		try
		{
			con = connectToBackUpECard();
			stat = con.createStatement();
			
			if(trans_code.equalsIgnoreCase("P"))//payment
			{
				query = "select distinct channelid, count(*) as total_count, sum(trans_amount) as total_amount  from ecarddb..e_transaction  where trans_date between('"+start_dt+"') and ('"+end_dt+"') and trans_code = '"+trans_code+"' and char_length(merchant_code)=10 group by channelid";
			}
			else
			{
				query = "select distinct channelid, count(*) as total_count, sum(trans_amount) as total_amount  from ecarddb..e_transaction  where trans_date between('"+start_dt+"') and ('"+end_dt+"') and trans_code = '"+trans_code+"' group by channelid";
			}

		
			//System.out.println("getE_TRANSACTIONByTransCodeGroupByChannel " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				transaction = new E_TRANSACTION();

				transaction.setTrans_code(trans_code);
				transaction.setChannelid(""+result.getObject(1));
				transaction.setTransaction_count(""+result.getObject(2));
				transaction.setTotal_amount(""+result.getObject(3));
				arr.add(transaction);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionBackUpEcard(con, result, result1);
		}
		finally
		{
			closeConnectionBackUpEcard(con, result, result1);
		}
		return arr;
	}
	

	
	/*Method to get the transactions by bank code and transcode grouped by channel and by date*/
	public ArrayList getE_TRANSACTIONByBankCodeAndTransCodeGroupByChannel(String bank_code, String trans_code,String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION transaction;
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		ResultSet result1 = null;
		
		try
		{
			con = connectToBackUpECard();
			stat = con.createStatement();
			
			if(trans_code.equalsIgnoreCase("P"))//payment
			{
				query = "select distinct channelid, count(*) as total_count, sum(trans_amount) as total_amount  from ecarddb..e_transaction  where trans_date between('"+start_dt+"') and ('"+end_dt+"') and trans_code = '"+trans_code+"' and card_num like '"+ bank_code +"%' and char_length(merchant_code)=10 group by channelid";	
			}
			else
			{
				query = "select distinct channelid, count(*) as total_count, sum(trans_amount) as total_amount  from ecarddb..e_transaction  where trans_date between('"+start_dt+"') and ('"+end_dt+"') and trans_code = '"+trans_code+"' and card_num like '"+ bank_code +"%' group by channelid";
			}

		
			//System.out.println("getE_TRANSACTIONByTransCodeGroupByChannel " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				transaction = new E_TRANSACTION();

				transaction.setBank_code(bank_code);
				transaction.setTrans_code(trans_code);
				transaction.setChannelid(""+result.getObject(1));
				transaction.setTransaction_count(""+result.getObject(2));
				transaction.setTotal_amount(""+result.getObject(3));
				arr.add(transaction);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionBackUpEcard(con, result, result1);
		}
		finally
		{
			closeConnectionBackUpEcard(con, result, result1);
		}
		return arr;
	}
	
	
	/*Method to get the transactions by  trans_code grouped by channel and by date and account_id*/
	public ArrayList getE_TRANSACTIONByTransCodeAndAccountIDGroupByChannel(String trans_code,String start_dt, String end_dt,
			String account_id)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION transaction;
		Connection con = null;
		Connection con1 = null;
		Statement stat = null;
		Statement stat1 = null;
		ResultSet result = null;
		ResultSet result1 = null;
		
		String myMerchants = "";
		String approstrophe = "'";
		
		try
		{
			con = connectToBackUpECard();
			stat = con.createStatement();
			
			con1 = connectToSupportLog();
			stat1 = con1.createStatement();
			
			query = "select merchant_code from support_merchant_mapping where username = '"+account_id+"'";
			result1 = stat1.executeQuery(query);
			while(result1.next())
			{
				myMerchants += approstrophe + ""+result1.getObject(1) + approstrophe + ",";
			}
			
			if(myMerchants.trim().length()>0)
			{
				myMerchants = myMerchants.substring(0, myMerchants.lastIndexOf(","));
			}
			else
			{
				myMerchants = "0";
			}
			
			
			if(trans_code.equalsIgnoreCase("P"))//payment
			{
				query = "select distinct channelid, count(*) as total_count, sum(trans_amount) as total_amount  from ecarddb..e_transaction  where merchant_code in ("+myMerchants+") and trans_date between('"+start_dt+"') and ('"+end_dt+"') and trans_code = '"+trans_code+"' and char_length(merchant_code)=10 group by channelid";
			}
			else
			{
				query = "select distinct channelid, count(*) as total_count, sum(trans_amount) as total_amount  from ecarddb..e_transaction  where merchant_code in ("+myMerchants+") and trans_date between('"+start_dt+"') and ('"+end_dt+"') and trans_code = '"+trans_code+"' group by channelid";
			}
			
			//System.out.println("getE_TRANSACTIONByTransCodeGroupByChannel " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				transaction = new E_TRANSACTION();

				transaction.setTrans_code(trans_code);
				transaction.setChannelid(""+result.getObject(1));
				transaction.setTransaction_count(""+result.getObject(2));
				transaction.setTotal_amount(""+result.getObject(3));
				arr.add(transaction);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionBackUpEcard(con, result, result1);
			closeConnectionSupportLogDB(con1, result1);
		}
		finally
		{
			closeConnectionBackUpEcard(con, result, result1);
			closeConnectionSupportLogDB(con1, result1);
		}
		return arr;
	}
	
	
	
	/*Method to get the transactions by  trans_code and by date and account_id without grouping by channel id*/
	public ArrayList getE_TRANSACTIONByTransCodeAndAccountIDNoChannelGrouping(String trans_code,String start_dt, String end_dt,
			String account_id)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION transaction;
		Connection con = null;
		Connection con1 = null;
		Statement stat = null;
		Statement stat1 = null;
		ResultSet result = null;
		ResultSet result1 = null;
		
		String myMerchants = "";
		String approstrophe = "'";
		
		try
		{
			con = connectToBackUpECard();
			stat = con.createStatement();
			
			con1 = connectToSupportLog();
			stat1 = con1.createStatement();
			
			query = "select merchant_code from support_merchant_mapping where username = '"+account_id+"'";
			result1 = stat1.executeQuery(query);
			while(result1.next())
			{
				myMerchants += approstrophe + ""+result1.getObject(1) + approstrophe + ",";
			}
			
			if(myMerchants.trim().length()>0)
			{
				myMerchants = myMerchants.substring(0, myMerchants.lastIndexOf(","));
			}
			else
			{
				myMerchants = "0";
			}
			
			
			if(trans_code.equalsIgnoreCase("P"))//payment
			{
				query = "select distinct merchant_code, (select merchant_name from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code),count(merchant_code), sum(trans_amount) from ecarddb..e_transaction  where merchant_code in ("+myMerchants+") and trans_date between('"+start_dt+"') and ('"+end_dt+"') and trans_code = '"+trans_code+"' and char_length(merchant_code)=10 group by merchant_code";
			}
			else
			{
				query = "select distinct merchant_code, (select merchant_name from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code),count(merchant_code), sum(trans_amount) from ecarddb..e_transaction  where merchant_code in ("+myMerchants+") and trans_date between('"+start_dt+"') and ('"+end_dt+"') and trans_code = '"+trans_code+"' and char_length(merchant_code)=10 group by merchant_code";
			}
			
			System.out.println("getE_TRANSACTIONByTransCodeAndAccountIDNoChannelGrouping " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				transaction = new E_TRANSACTION();

				transaction.setMerchat_code(""+result.getObject(1));
				transaction.setMerchant_descr(""+result.getObject(2));
				transaction.setTransaction_count(""+result.getObject(3));
				transaction.setTotal_amount(""+result.getObject(4));
				arr.add(transaction);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionBackUpEcard(con, result, result1);
			closeConnectionSupportLogDB(con1, result1);
		}
		finally
		{
			closeConnectionBackUpEcard(con, result, result1);
			closeConnectionSupportLogDB(con1, result1);
		}
		return arr;
	}
	
	
	
	
	/*Method to get the banks*/
	public ArrayList getBanks()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Bank bank;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			query = "select * from telcodb..support_bank";	
			System.out.println("getBanks " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				bank = new Bank();

				bank.setBank_code(""+result.getObject(1));
				bank.setBank_nm(""+result.getObject(2));
				bank.setAcronym(""+result.getObject(3));
				
				arr.add(bank);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return arr;
	}
	
	
	/*Method to get the bank name by bank code*/
	public String getBankName(String bank_code)
	{
		String query = "";
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			query = "select description from telcodb..support_bank where bank_code = '"+bank_code+"'";	
			result = stat.executeQuery(query);
			while(result.next())
			{
				message = ""+result.getObject(1);
			}
			/*if(message.equals(""))
			{
				query = "select dealer_name from telcodb..T_PSM_DEALER where dealer_id = "+bank_code+"";	
				result = stat.executeQuery(query);
				while(result.next())
				{
					message = ""+result.getObject(1);
				}
			}*/
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return message;
	}
	
	
	/*Method to get the tmc nodes*/
	public ArrayList getTMCNodes()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TMCNODE enode;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToETMC();
			stat = con.createStatement();
			
			query = "select incon_name from e_tmcnode";	
			result = stat.executeQuery(query);
			while(result.next())
			{
				enode = new E_TMCNODE();
				enode.setIncon_name(""+result.getObject(1));
				arr.add(enode);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionETMC(con, result);
		}
		finally
		{
			closeConnectionETMC(con, result);
		}
		return arr;
	}
	
	
	/*Method to get the tmc event*/
	public ArrayList getE_TMCEVENT(String source, String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TMCEVENT tevent;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToETMC();
			stat = con.createStatement();
			
			
			if(source.equalsIgnoreCase("ALL"))
				query = "select source, description, event_date, event_type from ecarddb..E_TMCEVENT where event_date between '"+start_dt+"' and '"+end_dt+"' order by event_date desc";
			else
				query = "select source, description, event_date, event_type from ecarddb..E_TMCEVENT where source = '"+source+"'  and event_date between('"+start_dt+"') and ('"+end_dt+"') order by event_date desc";
			
			//System.out.println("getE_TMCEVENT " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				tevent = new E_TMCEVENT();
				counter++;
				tevent.setCounter(""+counter);
				//tevent.setEvent_id(""+result.getObject(1));
				tevent.setSource(""+result.getObject(1));
				tevent.setDescription(""+result.getObject(2));
				tevent.setEvent_date(""+result.getObject(3));
				tevent.setEvent_type(""+result.getObject(4));
				
				if(tevent.getEvent_type().equals("0"))
				{
					tevent.setColor_code("green");
					tevent.setText_color_code("white");
				}
				else if(tevent.getEvent_type().equals("1"))
				{
					tevent.setColor_code("orange");
					tevent.setText_color_code("white");
				}
				else if(tevent.getEvent_type().equals("2"))
				{
					tevent.setColor_code("red");
					tevent.setText_color_code("white");
				}
				else
				{
					tevent.setColor_code("white");
					tevent.setText_color_code("black");
				}
				
				
				arr.add(tevent);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionETMC(con, result);
		}
		finally
		{
			closeConnectionETMC(con, result);
		}
		return arr;
	}
	
	
	
	/*Method to get the tmc request for banks to check if they are up or down*/
	public ArrayList getE_TMCREQUESTFORBANK(String source)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TMCREQUEST trequest;
		E_TMCNODE tnode;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToETMC();
			stat = con.createStatement();
			
			
			if(source.equalsIgnoreCase("ALL"))
				query = "select incon_name, status, last_connected from ecarddb..E_TMCNODE order by last_connected desc";
			else
				query = "select incon_name, status, last_connected from ecarddb..E_TMCNODE where incon_name = '"+source+"' order by last_connected desc";
			
			System.out.println("getE_TMCREQUESTFORBANK " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				tnode = new E_TMCNODE();
				counter++;
				tnode.setCounter(""+counter);
				tnode.setIncon_name(""+result.getObject(1));
				tnode.setStatus(""+result.getObject(2));
				tnode.setLast_connected(""+result.getObject(3));
				
				if(tnode.getStatus().equals("1"))
				{
					tnode.setColor_code("green");
					tnode.setText_color_code("white");
				}
				else if(tnode.getStatus().equals("0"))
				{
					tnode.setColor_code("red");
					tnode.setText_color_code("white");
				}
				else
				{
					tnode.setColor_code("white");
					tnode.setText_color_code("black");
				}
				
				arr.add(tnode);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionETMC(con, result);
		}
		finally
		{
			closeConnectionETMC(con, result);
		}
		return arr;
	}
	
	/*Method to get the e_standard split formula*/
	public ArrayList getE_STANDARD_SPLIT_FORMULA()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_STANDARD_SPLIT standard_split;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		ResultSet result1 = null;
		
		try
		{
			con = connectToBackUpECard();
			stat = con.createStatement();
		
			query = "select channel_id, trans_code, description ,owner_card, part_fee, split_type" +
			" from ecarddb..E_STANDARD_SPLIT  order by split_type";
	
			//System.out.println("query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				standard_split = new E_STANDARD_SPLIT();
				standard_split.setChannel_id(""+result.getObject(1));
				standard_split.setTrans_code(""+result.getObject(2));
				standard_split.setDescription(""+result.getObject(3));
				standard_split.setOwner_card(""+result.getObject(4));
				standard_split.setPart_fee(""+result.getObject(5));
				standard_split.setSplit_type(""+result.getObject(6));
				arr.add(standard_split);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionBackUpEcard(con, result, result1);
		}
		finally
		{
			closeConnectionBackUpEcard(con, result, result1);
		}
		return arr;
	}
	
	
	
	/*Method to get the merchant split formula*/
	public ArrayList<E_MERCHANT_SPLIT> getE_MERCHANT_SPLIT_FORMULA(String merchant_code)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_MERCHANT_SPLIT merchant_split;
		String special_split = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		ResultSet result1 = null;
		
		try
		{
			con = connectToBackUpECard();
			stat = con.createStatement();
			
			query = "select special_split from ecarddb..e_merchant where merchant_code = '"+ merchant_code + "' ";
			result = stat.executeQuery(query);
			while(result.next())
			{
				special_split = "";
				special_split = ""+result.getObject(1);
			}
			if(special_split.equals("0"))//that means use the commission split
			{
				query = "select split_descr, ratio, split_card" +
				" from ecarddb..e_merchant_commission_split where merchant_code = '"+merchant_code+"'";
				
				result = stat.executeQuery(query);
				while(result.next())
				{
					merchant_split = new E_MERCHANT_SPLIT();
					merchant_split.setSplit_descr(""+result.getObject(1));
					merchant_split.setEtzRatio(""+result.getObject(2));
					merchant_split.setSplit_card(""+result.getObject(3));
					merchant_split.setSplit_type("Commission Split");
					
					arr.add(merchant_split);
				}
			}
			else if(special_split.equals("1"))//special split
			{
				query = "select split_descr, svalue, main_flag, split_card" +
				" from ecarddb..e_merchant_special_split where merchant_code = '"+merchant_code+"' order by main_flag";
				
				result = stat.executeQuery(query);
				while(result.next())
				{
					merchant_split = new E_MERCHANT_SPLIT();
					merchant_split.setSplit_descr(""+result.getObject(1));
					merchant_split.setEtzRatio(""+result.getObject(2));
					merchant_split.setMain_flag(""+result.getObject(3));
					merchant_split.setSplit_card(""+result.getObject(4));
					merchant_split.setSplit_type("Special Split");
					
					arr.add(merchant_split);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionBackUpEcard(con, result, result1);
		}
		finally
		{
			closeConnectionBackUpEcard(con, result, result1);
		}
		return arr;
	}
	
	
	
	
	/*Method to get the mobile money transactions*/
	public ArrayList getMobileMoneyTransactionsByDateAndType(String start_dt, String end_dt, String type, String card_num)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION e_tran = null;
		int counter = 0;
		String t_code = "";
		String m_code = "";
		String dt = "";
		String str_cardnum = "";
		String str_value = "";
		
		String lname = "";
		String fname = "";
		String state = "";
		String phone = "";
		
		HashNumber hn = new HashNumber();
		Connection con = null, con1 = null;
		Statement stat = null, stat1 = null;
		ResultSet result = null, result1 = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			con1 = connectToTestECard();
			stat1 = con1.createStatement();
			
			if(type.equalsIgnoreCase("P"))//airtime
			{
				query = "select trans_date, trans_amount, trans_code, channelid, card_num, merchant_code,(select ecarddb..e_mobile_subscriber_card.mobile from ecarddb..e_mobile_subscriber_card where ecarddb..e_mobile_subscriber_card.card_num = ecarddb..e_transaction.card_num),channelid,transid from ecarddb..e_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' " +
				"and substring(card_num,1,6) in ('070991','070992','070993','070994','070995','070996','070997','070998','070999','084896','084894') " +
				"and trans_code = '"+type+"' and card_num like '" + card_num +"%' ";
			}
			else if(type.equalsIgnoreCase("D"))//cash load
			{
				query = "select trans_date, trans_amount, trans_code, channelid, card_num, merchant_code,(select ecarddb..e_mobile_subscriber_card.mobile from ecarddb..e_mobile_subscriber_card where ecarddb..e_mobile_subscriber_card.card_num = ecarddb..e_transaction.merchant_code),channelid,transid from ecarddb..e_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' " +
				"and substring(merchant_code,1,6) in ('070991','070992','070993','070994','070995','070996','070997','070998','070999','084896','084894') " +
				"and trans_code = '"+type+"' and card_num like '" + card_num +"%' ";
			}
			else if(type.equalsIgnoreCase("U"))//cash unload
			{
				query = "select trans_date, trans_amount, trans_code, channelid, card_num, merchant_code,(select ecarddb..e_mobile_subscriber_card.mobile from ecarddb..e_mobile_subscriber_card where ecarddb..e_mobile_subscriber_card.card_num = ecarddb..e_transaction.card_num),channelid,transid from ecarddb..e_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' " +
				"and substring(card_num,1,6) in ('070991','070992','070993','070994','070995','070996','070997','070998','070999','084896','084894') " +
				"and trans_code = '"+type+"' and card_num like '" + card_num +"%' ";
			}
			else if(type.equalsIgnoreCase("T"))//fund transfer out
			{
				query = "select trans_date, trans_amount, trans_code, channelid, card_num, merchant_code,(select ecarddb..e_mobile_subscriber_card.mobile from ecarddb..e_mobile_subscriber_card where ecarddb..e_mobile_subscriber_card.card_num = ecarddb..e_transaction.card_num),channelid,transid from ecarddb..e_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' " +
				"and substring(card_num,1,6) in ('070991','070992','070993','070994','070995','070996','070997','070998','070999','084896','084894') " +
				"and trans_code = '"+type+"' and card_num like '" + card_num +"%' ";
			}
			else if(type.equalsIgnoreCase("TI"))//fund transfer in
			{
				query = "select trans_date, trans_amount, trans_code, channelid, card_num, merchant_code,(select ecarddb..e_mobile_subscriber_card.mobile from ecarddb..e_mobile_subscriber_card where ecarddb..e_mobile_subscriber_card.card_num = ecarddb..e_transaction.merchant_code),channelid,transid from ecarddb..e_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' " +
				"and substring(merchant_code,1,6) in ('070991','070992','070993','070994','070995','070996','070997','070998','070999','084896','084894') " +
				"and trans_code = 'T' and card_num like '" + card_num +"%' ";
			}
			System.out.println("mobile money query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				m_code = "";
				t_code = "";
				dt = "";
				lname = "";
				fname = "";
				state = "";
				phone = "";
				
				e_tran = new E_TRANSACTION();
				
				e_tran.setCounter(""+counter);
				dt = ""+result.getObject(1);
				e_tran.setTrans_date(dt.substring(0,dt.indexOf(" ")));	
				e_tran.setTran_time(dt.substring(dt.indexOf(" ")+1));
				e_tran.setTrans_amount(""+result.getObject(2));
				t_code = ""+result.getObject(3);
				e_tran.setChannelid(""+result.getObject(4));
				str_cardnum = ""+result.getObject(5);
				m_code = ""+result.getObject(6);
				e_tran.setPhone(""+result.getObject(7));
				e_tran.setChannelid(""+result.getObject(8));
				e_tran.setTransid(""+result.getObject(9));
				
				if(t_code.equals("P"))
				{
					e_tran.setTrans_code("Airtime");
					e_tran.setCard_num(str_cardnum);
					str_value = str_cardnum;
				}
				
				if(t_code.equals("D"))
				{
					e_tran.setTrans_code("Cash In");
					e_tran.setCard_num(m_code);
					str_value = m_code;
				}
				
				if(t_code.equals("U"))
				{
					e_tran.setTrans_code("Cash Out");
					e_tran.setCard_num(str_cardnum);
					str_value = str_cardnum;
				}
				
				if(t_code.equals("T") && type.equals("TI"))
				{
					e_tran.setTrans_code("Funds Transfer - In");
					e_tran.setCard_num(m_code);
					str_value = m_code;
				}
				
				if(t_code.equals("T") && type.equals("T"))
				{
					e_tran.setTrans_code("Funds Transfer - Out");
					e_tran.setCard_num(str_cardnum);
					str_value = str_cardnum;
				}
				
				if(t_code.equals("T") && type.equals("ALL"))
				{
					e_tran.setTrans_code("Funds Transfer");
					e_tran.setCard_num(str_cardnum);
					str_value = str_cardnum;
				}
				
				
				//getting the personal details
				query = "select lastname, firstname, street, state  from ecarddb..e_cardholder where card_num = '"+str_value+"'";
				result1 = stat1.executeQuery(query);
				while(result1.next())
				{
					e_tran.setLastname(""+result1.getObject(1));
					e_tran.setFirstname(""+result1.getObject(2));
					e_tran.setStreet(""+result1.getObject(3));
					e_tran.setState(""+result1.getObject(4));
				}
				
				arr.add(e_tran);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
			closeConnectionTestECard(con1, result, result1);
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeConnectionTestECard(con1, result, result1);
		}
		return arr;
	}
	
	
	/*Method to get the mobile money summary*/
	public ArrayList getMobileMoneySummaryByDateAndType(String start_dt, String end_dt, String type, String card_num)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION e_tran = null;
		int counter = 0;
		String t_code = "";
		String m_code = "";
		String lname = "";
		String fname = "";
		String state = "";
		String phone = "";
		String str_cardnum = "";
		String str_value = "";
		
		HashNumber hn = new HashNumber();
		Connection con = null, con1 = null;
		Statement stat = null, stat1 = null;
		ResultSet result = null, result1 = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			con1 = connectToTestECard();
			stat1 = con1.createStatement();
			
			if(type.equalsIgnoreCase("P"))//airtime
			{
				query = "select count(*), sum(trans_amount), card_num, (select mobile from ecarddb..e_mobile_subscriber_card where ecarddb..e_mobile_subscriber_card.card_num = ecarddb..e_transaction.card_num),merchant_code,trans_code from ecarddb..e_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' " +
				"and substring(card_num,1,6) in ('070991','070992','070993','070994','070995','070996','070997','070998','070999','084896','084894') " +
				"and trans_code = '"+type+"' and card_num like '" + card_num +"%' group by card_num, trans_code ,merchant_code";
			}
			else if(type.equalsIgnoreCase("D"))//cash load
			{
				query = "select count(*), sum(trans_amount), card_num, (select mobile from ecarddb..e_mobile_subscriber_card where ecarddb..e_mobile_subscriber_card.card_num = ecarddb..e_transaction.card_num),merchant_code,trans_code from ecarddb..e_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' " +
				"and substring(merchant_code,1,6) in ('070991','070992','070993','070994','070995','070996','070997','070998','070999','084896','084894') " +
				"and trans_code = '"+type+"' and card_num like '" + card_num +"%' group by card_num, trans_code ,merchant_code";
			}
			else if(type.equalsIgnoreCase("U"))//cash unload
			{
				query = "select count(*), sum(trans_amount), card_num, (select mobile from ecarddb..e_mobile_subscriber_card where ecarddb..e_mobile_subscriber_card.card_num = ecarddb..e_transaction.card_num),merchant_code,trans_code from ecarddb..e_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' " +
				"and substring(card_num,1,6) in ('070991','070992','070993','070994','070995','070996','070997','070998','070999','084896','084894') " +
				"and trans_code = '"+type+"' and card_num like '" + card_num +"%' group by card_num, trans_code ,merchant_code";
			}
			else if(type.equalsIgnoreCase("T"))//fund transfer out
			{
				query = "select count(*), sum(trans_amount), card_num, (select mobile from ecarddb..e_mobile_subscriber_card where ecarddb..e_mobile_subscriber_card.card_num = ecarddb..e_transaction.card_num),merchant_code,trans_code from ecarddb..e_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' " +
				"and substring(card_num,1,6) in ('070991','070992','070993','070994','070995','070996','070997','070998','070999','084896','084894') " +
				"and trans_code = '"+type+"' and card_num like '" + card_num +"%' group by card_num, trans_code ,merchant_code";
			}
			else if(type.equalsIgnoreCase("TI"))//fund transfer in
			{
				query = "select count(*), sum(trans_amount), card_num, (select mobile from ecarddb..e_mobile_subscriber_card where ecarddb..e_mobile_subscriber_card.card_num = ecarddb..e_transaction.card_num),merchant_code,trans_code from ecarddb..e_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' " +
				"and substring(merchant_code,1,6) in ('070991','070992','070993','070994','070995','070996','070997','070998','070999','084896','084894') " +
				"and trans_code = 'T' and card_num like '" + card_num +"%' group by card_num, trans_code ,merchant_code";
			}
			System.out.println("mobile money query2 " + query + " tcode " + t_code);
			result = stat.executeQuery(query);
			while(result.next())
			{			
				e_tran = new E_TRANSACTION();
				
				e_tran.setTransaction_count(""+result.getObject(1));
				e_tran.setTrans_amount(""+result.getObject(2));
				str_cardnum = ""+result.getObject(3);
				e_tran.setPhone(""+result.getObject(4));
				m_code = ""+result.getObject(5);
				t_code = ""+result.getObject(6);
				
				
				if(t_code.equals("P"))
				{
					e_tran.setTrans_code("Airtime");
					e_tran.setCard_num(str_cardnum);
					str_value = str_cardnum;
				}
				
				if(t_code.equals("D"))
				{
					e_tran.setTrans_code("Cash In");
					e_tran.setCard_num(m_code);
					str_value = m_code;
				}
			
				if(t_code.equals("U"))
				{
					e_tran.setTrans_code("Cash Out");
					e_tran.setCard_num(str_cardnum);
					str_value = str_cardnum;
				}
				
				if(t_code.equals("T") && type.equals("TI"))
				{
					e_tran.setTrans_code("Funds Transfer - In");
					e_tran.setCard_num(m_code);
					str_value = m_code;
				}
				
				if(t_code.equals("T") && type.equals("T"))
				{
					e_tran.setTrans_code("Funds Transfer - Out");
					e_tran.setCard_num(str_cardnum);
					str_value = str_cardnum;
				}
				
				if(t_code.equals("T") && type.equals("ALL"))
				{
					e_tran.setTrans_code("Funds Transfer");
					e_tran.setCard_num(str_cardnum);
					str_value = str_cardnum;
				}
				
				
				//getting the personal details
				query = "select lastname, firstname, street, state  from ecarddb..e_cardholder where card_num = '"+str_value+"'";
				result1 = stat1.executeQuery(query);
				while(result1.next())
				{
					e_tran.setLastname(""+result1.getObject(1));
					e_tran.setFirstname(""+result1.getObject(2));
					e_tran.setStreet(""+result1.getObject(3));
					e_tran.setState(""+result1.getObject(4));
				}
				
				
				arr.add(e_tran);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
			closeConnectionTestECard(con1, result, result1);
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeConnectionTestECard(con1, result, result1);
		}
		return arr;
	}
	
	
	
	/*Method to get all the mobile money card holders*/
	public ArrayList getMobileMoneyCardHolders(String card_num, String mobile)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		CardHolder cholder = null;
		int counter = 0;
		String cardnum = "";
		HashNumber hn = new HashNumber();
		Connection con = null, con1 = null;
		Statement stat = null, stat1 = null;
		ResultSet result = null, result1 = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			con1 = connectToTestECard();
			stat1 = con1.createStatement();
			
			query = "select card_num, mobile from ecarddb..E_MOBILE_SUBSCRIBER_CARD where card_num like '" + card_num +"%' and" +
			" substring(card_num,1,6) in ('070991','070992','070993','070994','070995','070996','070997','070998','070999','084896','084894')" +
			" and mobile like '" + mobile +"%' ";
		
			System.out.println("mobile money card holder query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				
				cholder = new CardHolder();
				cholder.setCounter(""+counter);
				
				cardnum = ""+result.getObject(1);
				cholder.setCard_num(cardnum);
				cholder.setPhone(""+result.getObject(2));
				
				query = "select lastname, firstname, street, state  from ecarddb..e_cardholder where card_num = '"+cardnum+"'";
				result1 = stat1.executeQuery(query);
				while(result1.next())
				{
					cholder.setLastname(""+result1.getObject(1));
					cholder.setFirstname(""+result1.getObject(2));
					cholder.setStreet(""+result1.getObject(3));
					cholder.setState(""+result1.getObject(4));
				}
				
				arr.add(cholder);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result1);
			closeConnectionTestECard(con1, result, result1);
		}
		finally
		{
			closeConnectionECard(con, result, result1);
			closeConnectionTestECard(con1, result, result1);
		}
		return arr;
	}
	
	
	/*Method to get all the mobile money card holders, this report doesn't connect to get the mobile subscriber card info*/
	public ArrayList getMobileMoneyCardHolders2(String card_num, String mobile, String lastname)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		CardHolder cholder = null;
		int counter = 0;
		HashNumber hn = new HashNumber();
		Connection con = null, con1 = null;
		Statement stat = null, stat1 = null;
		ResultSet result = null, result1 = null;
		
		try
		{
			
			con1 = connectToTestECard();
			stat1 = con1.createStatement();
			
			query = "select card_num, phone, lastname, firstname, street, state  from ecarddb..e_cardholder where card_num like '" + card_num +"%' and " +
					"phone like '" + mobile +"%' and lastname like  '"+lastname+"%' ";
			
			System.out.println("query " + query);
			result1 = stat1.executeQuery(query);
			while(result1.next())
			{
				counter++;
				
				cholder = new CardHolder();
				cholder.setCounter(""+counter);
				
				cholder.setCard_num(""+result1.getObject(1));
				cholder.setPhone(""+result1.getObject(2));
				cholder.setLastname(""+result1.getObject(3));
				cholder.setFirstname(""+result1.getObject(4));
				cholder.setStreet(""+result1.getObject(5));
				cholder.setState(""+result1.getObject(6));
				arr.add(cholder);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTestECard(con1, result, result1);
		}
		finally
		{
			closeConnectionTestECard(con1, result, result1);
		}
		return arr;
	}
	
	
	/*Method to get card holders info*/
	public ArrayList getCardHolders(String card_num)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		CardHolder cholder = null;
		int counter = 0;
		HashNumber hn = new HashNumber();
		Connection con = null, con1 = null;
		Statement stat = null, stat1 = null;
		ResultSet result = null, result1 = null;
		
		try
		{
			
			con1 = connectToTestECard();
			stat1 = con1.createStatement();
			
			
			
			query = "select card_num, phone, lastname, firstname, street, state, card_expiration, email, bound_work, bound_value," +
					" birth_date, change_pin, user_locked, user_hotlist, pin_missed, last_used, modified, created," +
					" online_balance,payfee, cashwthdw_limit, client_id  from ecarddb..e_cardholder where card_num = '" + card_num +"' ";
			
			System.out.println("query " + query);
			result1 = stat1.executeQuery(query);
			while(result1.next())
			{
				counter++;
				
				cholder = new CardHolder();
				cholder.setCounter(""+counter);
				
				cholder.setCard_num(""+result1.getObject(1));
				cholder.setPhone(""+result1.getObject(2));
				cholder.setLastname(""+result1.getObject(3));
				cholder.setFirstname(""+result1.getObject(4));
				cholder.setStreet(""+result1.getObject(5));
				cholder.setState(""+result1.getObject(6));
				cholder.setCard_expiration(""+result1.getObject(7));
				cholder.setEmail(""+result1.getObject(8));
				cholder.setBound_work(""+result1.getObject(9));
				cholder.setBound_value(""+result1.getObject(10));
				cholder.setBirth_date(""+result1.getObject(11));
				cholder.setChange_pin(""+result1.getObject(12));
				cholder.setUser_locked(""+result1.getObject(13));
				cholder.setUser_hotlist(""+result1.getObject(14));
				cholder.setPin_missed(""+result1.getObject(15));
				cholder.setLast_used(""+result1.getObject(16));
				cholder.setModified(""+result1.getObject(17));
				cholder.setCreated(""+result1.getObject(18));
				cholder.setOnline_balance(""+result1.getObject(19));
				cholder.setPayfee(""+result1.getObject(20));
				cholder.setCashwthdw_limit(""+result1.getObject(21));
				cholder.setClient_id(""+result1.getObject(22));
				arr.add(cholder);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTestECard(con1, result, result1);
		}
		finally
		{
			closeConnectionTestECard(con1, result, result1);
		}
		return arr;
	}
	public ArrayList getCardHolders_SchemeFromPocketMoniDB(String card_num,String beginDate, String endDate,
			String scheme_card, String dbServer)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		CardHolder cholder = null;
		int counter = 0;
		HashNumber hn = new HashNumber();
		Connection con = null, con1 = null;
		Statement stat = null, stat1 = null;
		ResultSet result = null, result1 = null;
		String actual_cardnum = "";
		String dt = "";
		int scheme_position = scheme_card.length();
		
		try
		{
			
			if(dbServer.equals("1"))//pocketmoni server
			{
				con = connectPocketMoniEcardDB();
				stat = con.createStatement();
			}
			else if(dbServer.equals("2"))//.57
			{
				con = connectToTestECard();
				stat = con.createStatement();
			}
			
			/*if(card_num.trim().length() > 0)//the card num here is actually the track 2
			{
				query = "select card_num from e_cardholder where track2 = '"+card_num+"' ";
				result = stat.executeQuery(query);
				if(result.next())
				{
					actual_cardnum = ""+result.getObject(1);
				}
			}*/
			
			if(card_num.trim().length() > 16)//the card num here is actually the track 2
			{
				query = "select card_num from e_cardholder where track2 = '"+card_num+"' ";
				result = stat.executeQuery(query);
				if(result.next())
				{
					actual_cardnum = ""+result.getObject(1);
				}
			}
			else
			{
				actual_cardnum = card_num;
			}
			
			
			if(card_num.trim().length() > 0)
			{				
				
				query = "select card_num, phone, lastname, firstname, street, state, card_expiration, email, bound_work, bound_value," +
						" birth_date, change_pin, user_locked, user_hotlist, pin_missed, last_used, modified, created," +
						" online_balance,payfee, cashwthdw_limit, client_id  from" +
						" ecarddb..e_cardholder where card_num = '"+actual_cardnum+"' and" +
						" substring(track2,7,"+scheme_position+") like '"+scheme_card+"%' ";
			}
			else
			{
				query = "select card_num, phone, lastname, firstname, street, state, card_expiration, email, bound_work, bound_value," +
						" birth_date, change_pin, user_locked, user_hotlist, pin_missed, last_used, modified, created," +
						" online_balance,payfee, cashwthdw_limit, client_id  from" +
						" ecarddb..e_cardholder where substring(track2,7,"+scheme_position+") like '"+scheme_card+"%' ";
			
			}
			System.out.println("getCardHolders_SchemeFromPocketMoniDB query    " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				
				cholder = new CardHolder();
				cholder.setCounter(""+counter);
				
				cholder.setCard_num(""+result.getObject(1));
				cholder.setPhone(""+result.getObject(2));
				cholder.setLastname(""+result.getObject(3));
				cholder.setFirstname(""+result.getObject(4));
				cholder.setStreet(""+result.getObject(5));
				cholder.setState(""+result.getObject(6));
				cholder.setCard_expiration(""+result.getObject(7));
				cholder.setEmail(""+result.getObject(8));
				cholder.setBound_work(""+result.getObject(9));
				cholder.setBound_value(""+result.getObject(10));
				cholder.setBirth_date(""+result.getObject(11));
				cholder.setChange_pin(""+result.getObject(12));
				cholder.setUser_locked(""+result.getObject(13));
				cholder.setUser_hotlist(""+result.getObject(14));
				cholder.setPin_missed(""+result.getObject(15));
				cholder.setLast_used(""+result.getObject(16));
				cholder.setModified(""+result.getObject(17));
				cholder.setCreated(""+result.getObject(18));
				cholder.setOnline_balance(""+result.getObject(19));
				cholder.setPayfee(""+result.getObject(20));
				cholder.setCashwthdw_limit(""+result.getObject(21));
				cholder.setClient_id(""+result.getObject(22));
				arr.add(cholder);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPocketMoniEcardDB(con, result);
			closeConnectionTestECard(con, result, result);
		}
		finally
		{
			closeConnectionPocketMoniEcardDB(con, result);
			closeConnectionTestECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Method to get card holders info from pocket moni DB*/
	public ArrayList getCardHolders_SchemeFromPocketMoniDB(String card_num, String scheme_card)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		CardHolder cholder = null;
		int counter = 0;
		HashNumber hn = new HashNumber();
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectPocketMoniEcardDB();
			stat = con.createStatement();

			query = "select card_num, phone, lastname, firstname, street, state, card_expiration, email, bound_work, bound_value," +
					" birth_date, change_pin, user_locked, user_hotlist, pin_missed, last_used, modified, created," +
					" online_balance,payfee, cashwthdw_limit, client_id  from" +
					" ecarddb..e_cardholder where card_num = '" + card_num +"' and card_num like '"+scheme_card+"%' ";
			
			System.out.println("query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				
				cholder = new CardHolder();
				cholder.setCounter(""+counter);
				
				cholder.setCard_num(""+result.getObject(1));
				cholder.setPhone(""+result.getObject(2));
				cholder.setLastname(""+result.getObject(3));
				cholder.setFirstname(""+result.getObject(4));
				cholder.setStreet(""+result.getObject(5));
				cholder.setState(""+result.getObject(6));
				cholder.setCard_expiration(""+result.getObject(7));
				cholder.setEmail(""+result.getObject(8));
				cholder.setBound_work(""+result.getObject(9));
				cholder.setBound_value(""+result.getObject(10));
				cholder.setBirth_date(""+result.getObject(11));
				cholder.setChange_pin(""+result.getObject(12));
				cholder.setUser_locked(""+result.getObject(13));
				cholder.setUser_hotlist(""+result.getObject(14));
				cholder.setPin_missed(""+result.getObject(15));
				cholder.setLast_used(""+result.getObject(16));
				cholder.setModified(""+result.getObject(17));
				cholder.setCreated(""+result.getObject(18));
				cholder.setOnline_balance(""+result.getObject(19));
				cholder.setPayfee(""+result.getObject(20));
				cholder.setCashwthdw_limit(""+result.getObject(21));
				cholder.setClient_id(""+result.getObject(22));
				arr.add(cholder);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPocketMoniEcardDB(con, result);
		}
		finally
		{
			closeConnectionPocketMoniEcardDB(con, result);
		}
		return arr;
	}
	
	
	
	/*Method to get card number when mobile or cardnumber info is supplied*/
	public String getCardNumber(String card_num_mobile)
	{
		String query = "";
		String message = "";

		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			con = connectToTestECard();
			stat = con.createStatement();
			
			query = "select card_num from ecarddb..e_cardholder where card_num = '" + card_num_mobile +"' or phone = '" + card_num_mobile +"'";
			
			System.out.println("query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				message = ""+result.getObject(1);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTestECard(con, result, result);
		}
		finally
		{
			closeConnectionTestECard(con, result, result);
		}
		return message;
	}
	
	
	/*Method to get all the mobile money card balance summary*/
	public ArrayList getMobileMoneyCardBalanceSummary(String online_dt, String end_dt, String card_num)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		CardHolder cholder = null;
		int counter = 0;
		HashNumber hn = new HashNumber();
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		
		try
		{
			con = connectToTestECard();
			stat = con.createStatement();
			
			query = "select substring(card_num,1,6), sum(online_balance) from ecarddb..e_cardholder where" +
					" substring(card_num,1,6) in ('070991','070992','070993','070994','070995','070996','070997','070998','070999','084896','084894')" +
					" group by substring(card_num,1,6)";
			
			
			System.out.println("mobile money card balance summary query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				
				cholder = new CardHolder();
				
				cholder.setCounter(""+counter);
				
				cholder.setCard_num(""+result.getObject(1));
				cholder.setOnline_balance(""+result.getObject(2));			
				
				arr.add(cholder);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTestECard(con, result,result);
		}
		finally
		{
			closeConnectionTestECard(con, result,result);
		}
		return arr;
	}
	
	/*Method to get all the mobile money card balance*/
	public ArrayList getMobileMoneyCardBalance(String online_dt, String end_dt, String card_num)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		CardHolder cholder = null;
		int counter = 0;
		HashNumber hn = new HashNumber();
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToTestECard();
			stat = con.createStatement();
			
			query = "select card_num, lastname, firstname, online_balance," +
					" online_date from ecarddb..e_cardholder where card_num like '"+card_num+"%' and" +
					" substring(card_num,1,6) in ('070991','070992','070993','070994','070995','070996','070997','070998','070999','084896','084894') ";
		
			System.out.println("mobile money card balance query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				
				cholder = new CardHolder();
				
				cholder.setCounter(""+counter);
				
				cholder.setCard_num(""+result.getObject(1));
				cholder.setLastname(""+result.getObject(2));			
				cholder.setFirstname(""+result.getObject(3));			
				cholder.setOnline_balance(""+result.getObject(4));
				cholder.setOnline_date(""+result.getObject(5));
				
				arr.add(cholder);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTestECard(con, result,result);
		}
		finally
		{
			closeConnectionTestECard(con, result,result);
		}
		return arr;
	}
	
	
	/*Method to get the new mobile money subscribers*/
	public ArrayList getMobileMoneyCardSubscribers(String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		CardHolder cholder = null;
		int counter = 0;
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{			
			con = connectToTestECard();
			stat = con.createStatement();
			
			query = "select card_num, lastname, firstname," +
					" created from ecarddb..e_cardholder where created between '"+start_dt+"' and '"+end_dt+"' and" +
					" substring(card_num,1,6) in ('070991','070992','070993','070994','070995','070996','070997','070998','070999','084896','084894') ";
		
			System.out.println("mobile money subscriber query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				
				cholder = new CardHolder();
				
				cholder.setCounter(""+counter);
				cholder.setCard_num(""+result.getObject(1));
				cholder.setLastname(""+result.getObject(2));			
				cholder.setFirstname(""+result.getObject(3));			
				cholder.setCreated(""+result.getObject(4));
				
				arr.add(cholder);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTestECard(con, result,result);
		}
		finally
		{
			closeConnectionTestECard(con, result,result);
		}
		return arr;
	}
	
	
	
	
	public String getCardHolderLastName(String card_num)
	{
		String message = "";
		String query = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToTestECard();//connecting to the test server
			stat = con.createStatement();
			
			
			query = "select lastname from ecarddb..e_cardholder where card_num = '"+card_num+"'";
			result = stat.executeQuery(query);
			while(result.next())
			{
				message = ""+result.getObject(1);
				if(message.equals("null") || message.equals(""))
					message = "NA";
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
			closeConnectionTestECard(con, result,result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeConnectionTestECard(con, result,result);
		}
		return message;
	}
	
	/*card holder firstname*/
	public String getCardHolderFirstName(String card_num)
	{
		String message = "";
		String query = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToTestECard();//connecting to the test server
			stat = con.createStatement();
			
			query = "select firstname from ecarddb..e_cardholder where card_num = '"+card_num+"'";
			result = stat.executeQuery(query);
			while(result.next())
			{
				message = ""+result.getObject(1);
				if(message.equals("null") || message.equals(""))
					message = "NA";
			}
			/*query = "select firstname from ecarddb..e_cardholder where card_num = '"+card_num+"'";
			result = stat.executeQuery(query);
			while(result.next())
			{
				message = ""+result.getObject(1);
			}
			
			
			if(message.equals("null") || message.equals(""))
			{
				con = connectToTestECard();//connecting to the test server
				query = "select firstname from ecarddb..e_cardholder where card_num = '"+card_num+"'";
				result = stat.executeQuery(query);
				while(result.next())
				{
					message = ""+result.getObject(1);
				}
			}*/
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
			closeConnectionTestECard(con, result,result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeConnectionTestECard(con, result,result);
		}
		return message;
	}
	
	
	/*card holder street*/
	public String getCardHolderStreet(String card_num)
	{
		String message = "";
		String query = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToTestECard();//connecting to the test server
			stat = con.createStatement();
			
			
			query = "select street from ecarddb..e_cardholder where card_num = '"+card_num+"'";
			result = stat.executeQuery(query);
			while(result.next())
			{
				message = ""+result.getObject(1);
				if(message.equals("null") || message.equals(""))
					message = "NA";
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
			closeConnectionTestECard(con, result,result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeConnectionTestECard(con, result,result);
		}
		return message;
	}
	
	/*card holder state*/
	public String getCardHolderState(String card_num)
	{
		String message = "";
		String query = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToTestECard();//connecting to the test server
			stat = con.createStatement();
			
			
			query = "select state from ecarddb..e_cardholder where card_num = '"+card_num+"'";
			result = stat.executeQuery(query);
			while(result.next())
			{
				message = ""+result.getObject(1);
				if(message.equals("null") || message.equals(""))
					message = "NA";
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
			closeConnectionTestECard(con, result,result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeConnectionTestECard(con, result,result);
		}
		return message;
	}
	
	
	
	/*card holder phone*/
	public String getCardHolderPhone(String card_num)
	{
		String message = "";
		String query = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToTestECard();//connecting to the test server
			stat = con.createStatement();
			
			query = "select phone from ecarddb..e_cardholder where card_num = '"+card_num+"'";
			result = stat.executeQuery(query);
			while(result.next())
			{
				message = ""+result.getObject(1);
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
			closeConnectionTestECard(con, result,result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeConnectionTestECard(con, result,result);
		}
		return message;
	}
	
	
	
	/*Method to get the switch report*/
	public ArrayList getSwitchReportByDateAndType(String start_dt, String end_dt, String type)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION etran = null;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		
		try
		{
			if(type.equalsIgnoreCase("CARD LOAD"))//card load
			{
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3) Bank, count(distinct card_num) Card_Count, count(*) Trans_Count, sum(trans_amount)" +
						" Trans_Sum from ecarddb..e_transaction where ((card_num like '______0000000000' and trans_code = 'P') Or (trans_code = 'D'))" +
						" and" +
						" char_length(merchant_code)=16 and trans_date between '"+start_dt+"' and '"+end_dt+"'" +
						" and substring(card_num,1,6) not in ('011811','011812','011813','011814','011815') group by substring(card_num,1,3)" +
						" order by trans_count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
					etran.setTransaction_count(""+result.getObject(3));
					etran.setVolume(""+result.getObject(4));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("CARD UNLOAD"))//card unload
			{
				
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(merchant_code,1,3) Bank, count(distinct card_num) Card_Count,count(*) Trans_Count, sum(trans_amount)" +
						" Trans_Sum from ecarddb..e_transaction where trans_code = 'W' and trans_date between '"+start_dt+"' and '"+end_dt+"' and" +
						" char_length(merchant_code)>10 group by substring(merchant_code,1,3) order by trans_count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
					etran.setTransaction_count(""+result.getObject(3));
					etran.setVolume(""+result.getObject(4));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("ATM WITHDRAWAL"))//ATM Withdrawal
			{
				
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(merchant_code,1,3) Bank, count(distinct card_num) Card_Count,count(unique_transid) Trans_Count," +
						" sum(trans_amount) Trans_Sum from ecarddb..e_transaction where trans_code = 'W' and" +
						" trans_date between '"+start_dt+"' and '"+end_dt+"' and char_length(merchant_code)=10 and" +
						" unique_transid like '0402%' group by substring(merchant_code,1,3) order by trans_count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
					etran.setTransaction_count(""+result.getObject(3));
					etran.setVolume(""+result.getObject(4));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("ON_US ATM WITHDRAWALS"))//ON_US ATM WITHDRAWALS
			{
				
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(merchant_code,1,3) Bank, count(distinct card_num) Card_Count,count(unique_transid) Trans_Count," +
						" sum(trans_amount) Trans_Sum from ecarddb..e_transaction where trans_code = 'W' and" +
						" trans_date between '"+start_dt+"' and '"+end_dt+"' and substring(card_num,1,3) = substring(merchant_code,1,3) and" +
						" char_length(merchant_code)=10 and unique_transid like '0402%' group by substring(merchant_code,1,3) order by trans_count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
					etran.setTransaction_count(""+result.getObject(3));
					etran.setVolume(""+result.getObject(4));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("NOT_ON_US ATM WITHDRAWALS"))//NOT_ON_US ATM WITHDRAWALS
			{
				
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(merchant_code,1,3) Bank, count( distinct card_num) Card_Count,count(*) Trans_Count," +
						" sum(trans_amount) Trans_Sum from ecarddb..e_transaction where trans_code = 'W' and" +
						" trans_date between '"+start_dt+"' and '"+end_dt+"' and" +
						" substring(card_num,1,3) <> substring(merchant_code,1,3) and char_length(merchant_code)=10 and" +
						" unique_transid like '0402%' group by substring(merchant_code,1,3) order by trans_count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
					etran.setTransaction_count(""+result.getObject(3));
					etran.setVolume(""+result.getObject(4));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("OUTLET TRANSACTIONS"))//OUTLET TRANSACTIONS
			{
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3) Bank, count(*) Trans_Count, sum(trans_amount)" +
						" Trans_Sum from ecarddb..e_transaction where char_length(card_num) > 10  and trans_code in ('P') and" +
						" char_length(merchant_code)=10 and unique_transid like '05%' and" +
						" trans_date between '"+start_dt+"' and '"+end_dt+"' and" +
						" merchant_code in (select merchant_code from payoutletdb..c_issuermerchant) group by substring(card_num,1,3) order by" +
						" trans_count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setTransaction_count(""+result.getObject(2));
					etran.setVolume(""+result.getObject(3));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("WEB TRANSACTION"))//WEB TRANSACTION
			{
				
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3) Bank, count(distinct card_num) Card_Count,count(*) Trans_Count," +
						" sum(trans_amount) Trans_Sum from ecarddb..e_transaction where char_length(card_num) > 10  and trans_code in ('P') and" +
						" char_length(merchant_code) = 10 and trans_date between '"+start_dt+"' and '"+end_dt+"' and" +
						" merchant_code not in (select merchant_code from payoutletdb..c_issuermerchant) and unique_transid like '01%' and" +
						" substring(card_num,1,6) not in ('011811','011812','011813','011814','011815') group by substring(card_num,1,3) order by" +
						" trans_count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
					etran.setTransaction_count(""+result.getObject(3));
					etran.setVolume(""+result.getObject(4));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("WEB TOPUP"))//WEB TOPUP
			{
				
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3) Bank, count(distinct card_num) Card_Count,count(*) Trans_Count, sum(trans_amount)" +
						" Trans_Sum from ecarddb..e_transaction where char_length(card_num) > 10  and" +
						" trans_code in ('P') and char_length(merchant_code) = 10 and trans_date between '"+start_dt+"' and '"+end_dt+"' and" +
						" unique_transid like '01%' and merchant_code in ('0560019910','0690019910','0690010097','0690069901') and" +
						" substring(card_num,1,6) not in ('011811','011812','011813','011814','011815') group by substring(card_num,1,3) order by" +
						" trans_count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
					etran.setTransaction_count(""+result.getObject(3));
					etran.setVolume(""+result.getObject(4));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("MOBILE TOPUP"))//MOBILE TOPUP
			{
				
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3) Bank, count(distinct card_num) Card_Count,count(*) Trans_Count, sum(trans_amount)" +
						" Trans_Sum from ecarddb..e_transaction where char_length(card_num) > 10  and trans_code in ('P') and" +
						" char_length(merchant_code) = 10 and trans_date between '"+start_dt+"' and '"+end_dt+"' and" +
						" unique_transid like '02%' and merchant_code in ('0560019910','0690019910','0690010097','0690069901') and" +
						" substring(card_num,1,6) not in ('011811','011812','011813','011814','011815') group by substring(card_num,1,3) order by" +
						" trans_count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
					etran.setTransaction_count(""+result.getObject(3));
					etran.setVolume(""+result.getObject(4));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("MTN POST PAID (MOBILE)"))//MTN POST PAID (MOBILE)
			{
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3) Bank, count( distinct card_num) Card_Count,count(*) Trans_Count, sum(trans_amount)" +
						" Trans_Sum from ecarddb..e_transaction where merchant_code = '0560019910' and" +
						" unique_transid like '02%' and trans_date between '"+start_dt+"' and '"+end_dt+"'" +
						" and trans_descr like '%MTNPOST%' group by substring(card_num,1,3)";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
					etran.setTransaction_count(""+result.getObject(3));
					etran.setVolume(""+result.getObject(4));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("MTN POST PAID (PAYOUTLET)"))//MTN POST PAID (PAYOUTLET)
			{
				
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3) Bank, count( distinct card_num) Card_Count,count(*) Trans_Count, sum(trans_amount)" +
						" Trans_Sum from ecarddb..e_transaction where merchant_code ='0112430001' and" +
						" trans_date between '"+start_dt+"' and '"+end_dt+"' and unique_transid not like '02%' and" +
						" substring(card_num,1,6) not in ('011811','011812','011813','011814','011815') group by substring(card_num,1,3) order by" +
						" trans_count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
					etran.setTransaction_count(""+result.getObject(3));
					etran.setVolume(""+result.getObject(4));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("RELOADABLE CARDS COUNT"))//RELOADABLE CARDS COUNT
			{
				
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3) Bank, count(*) Card_Count from ecarddb..e_cardholder where substring(card_num,1,6)" +
						" in (select issuer_branch from ecarddb..e_exfrontendinterface) and created between '"+start_dt+"' and '"+end_dt+"'  and" +
						" substring(card_num,1,6) not in ('011811','011812','011813','011814','011815') group by substring (card_num,1,3) order by" +
						" Card_Count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
				}
				
				closeConnectionECard(con, result, result);
				
				con = connectToTestECard();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3) Bank, count(*) Card_Count from ecarddb..e_cardholder where substring(card_num,1,6)" +
						" in (select issuer_branch from ecarddb..e_exfrontendinterface) and created between '"+start_dt+"' and '"+end_dt+"'  and" +
						" substring(card_num,1,6) not in ('011811','011812','011813','011814','011815') group by substring (card_num,1,3) order by" +
						" Card_Count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
					
					arr.add(etran);
				}
				
			}
			else if(type.equalsIgnoreCase("WEB TRANSFERS"))//WEB TRANSFERS
			{
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3) Bank, count(distinct card_num) Card_Count, count(*) Trans_Count, sum(trans_amount)" +
						" Trans_Sum from ecarddb..e_transaction where trans_code in ('T') and trans_date between '"+start_dt+"' and '"+end_dt+"' and" +
						" unique_transid not like '02%' and unique_transid not like '%CORP%'  and" +
						" substring(card_num,1,6) not in ('011811','011812','011813','011814','011815') group by substring(card_num,1,3) order by" +
						" trans_count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
					etran.setTransaction_count(""+result.getObject(3));
					etran.setVolume(""+result.getObject(4));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("WEB TRANSFERS (ON-US)"))//WEB TRANSFERS (ON-US)
			{
				
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3) Bank, count(distinct card_num) Card_Count, count(*) Trans_Count, sum(trans_amount)" +
						" Trans_Sum from ecarddb..e_transaction where trans_code in ('T') and trans_date between '"+start_dt+"' and '"+end_dt+"' and" +
						" unique_transid not like '02%' and unique_transid not like '%CORP%' and" +
						" substring(card_num,1,3) = substring(merchant_code,1,3)  and" +
						" substring(card_num,1,6) not in ('011811','011812','011813','011814','011815') group by substring(card_num,1,3) order by" +
						" trans_count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
					etran.setTransaction_count(""+result.getObject(3));
					etran.setVolume(""+result.getObject(4));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("WEB TRANSFERS (NOT ON-US)"))//WEB TRANSFERS (NOT ON-US)
			{
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3) Bank, count(distinct card_num) Card_Count, count(*) Trans_Count, sum(trans_amount)" +
						" Trans_Sum from ecarddb..e_transaction where trans_code in ('T') and trans_date between '"+start_dt+"' and '"+end_dt+"' and" +
						" unique_transid not like '02%' and unique_transid not like '%CORP%' and" +
						" substring(card_num,1,3) <> substring(merchant_code,1,3)  and" +
						" substring(card_num,1,6) not in ('011811','011812','011813','011814','011815') group by substring(card_num,1,3) order by" +
						" trans_count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
					etran.setTransaction_count(""+result.getObject(3));
					etran.setVolume(""+result.getObject(4));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("MOBILE TRANSFERS"))//MOBILE TRANSFERS
			{
				
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3) Bank,  count(distinct card_num) Card_Count, count(*) Trans_Count, sum(trans_amount)" +
						" Trans_Sum from ecarddb..e_transaction where trans_code in ('T') and trans_date between '"+start_dt+"' and '"+end_dt+"' and" +
						" unique_transid like '02%' and unique_transid not like '%CORP%'  and" +
						" substring(card_num,1,6) not in ('011811','011812','011813','011814','011815') group by substring(card_num,1,3) order by" +
						" trans_count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
					etran.setTransaction_count(""+result.getObject(3));
					etran.setVolume(""+result.getObject(4));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("MOBILE TRANSFERS (ON US)"))//MOBILE TRANSFERS (ON US)
			{
				
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3) Bank,  count(distinct card_num) Card_Count, count(*) Trans_Count, sum(trans_amount)" +
						" Trans_Sum from ecarddb..e_transaction where trans_code in ('T') and trans_date between '"+start_dt+"' and '"+end_dt+"' and" +
						" unique_transid like '02%' and unique_transid not like '%CORP%' and substring(card_num,1,3) = substring(merchant_code,1,3)  and" +
						" substring(card_num,1,6) not in ('011811','011812','011813','011814','011815') group by substring(card_num,1,3) order by" +
						" trans_count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
					etran.setTransaction_count(""+result.getObject(3));
					etran.setVolume(""+result.getObject(4));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("MOBILE TRANSFERS (NOT ON_US)"))//MOBILE TRANSFERS (NOT ON_US)
			{
				
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3) Bank,  count(distinct card_num) Card_Count, count(*) Trans_Count, sum(trans_amount)" +
						" Trans_Sum from ecarddb..e_transaction where trans_code in ('T') and trans_date between '"+start_dt+"' and '"+end_dt+"' and" +
						" unique_transid like '02%' and unique_transid not like '%CORP%' and substring(card_num,1,3) <> substring(merchant_code,1,3) and" +
						" substring(card_num,1,6) not in ('011811','011812','011813','011814','011815') group by substring(card_num,1,3)  order by" +
						" trans_count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
					etran.setTransaction_count(""+result.getObject(3));
					etran.setVolume(""+result.getObject(4));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("LSWC COLLECTION"))//LSWC COLLECTION
			{
				con = connectToPayoutlet();
				stat = con.createStatement();
				
				query  = "select substring(trans_no,1,3) Bank, count(*) Trans_Count, sum(trans_amount)" +
						" Trans_Sum from payoutletdb..c_transaction" +
						" where merchant_code in ('2320019901','0560019906','0820019906','0500029901','0440999901','0690019906','0570019907'," +
						"'0440999901','0820019906','0500029901','0820820001') and" +
						" trans_date between '"+start_dt+"' and '"+end_dt+"' group by substring(trans_no,1,3) order by trans_count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setTransaction_count(""+result.getObject(2));
					etran.setVolume(""+result.getObject(3));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("DSTV PAYMENTS"))//DSTV PAYMENTS
			{
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3) Bank, count( distinct card_num) Card_Count,count(*) Trans_Count, sum(trans_amount)" +
						" Trans_Sum from ecarddb..e_transaction where merchant_code in ('0570010001','2140010001','2140010003') and" +
						" trans_date between '"+start_dt+"' and '"+end_dt+"'  and" +
						" substring(card_num,1,6) not in ('011811','011812','011813','011814','011815') group by substring(card_num,1,3) order by" +
						" trans_count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
					etran.setTransaction_count(""+result.getObject(3));
					etran.setVolume(""+result.getObject(4));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("HITV PAYMENTS (PAYOUTLET)"))//HITV PAYMENTS (PAYOUTLET)
			{
				
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3) Bank, count( distinct card_num) Card_Count,count(*) Trans_Count, sum(trans_amount)" +
						" Trans_Sum from ecarddb..e_transaction where merchant_code ='0582280012' and" +
						" trans_date between '"+start_dt+"' and '"+end_dt+"' group by substring(card_num,1,3) order by trans_count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
					etran.setTransaction_count(""+result.getObject(3));
					etran.setVolume(""+result.getObject(4));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("HITV PAYMENTS (MOBILE)"))//HITV PAYMENTS (MOBILE)
			{
				
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3) Bank, count( distinct card_num) Card_Count,count(*) Trans_Count, sum(trans_amount)" +
						" Trans_Sum from ecarddb..e_transaction where merchant_code ='0582280016' and" +
						" trans_date between '"+start_dt+"' and '"+end_dt+"'  and" +
						" substring(card_num,1,6) not in ('011811','011812','011813','011814','011815') group by substring(card_num,1,3) order by" +
						" trans_count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
					etran.setTransaction_count(""+result.getObject(3));
					etran.setVolume(""+result.getObject(4));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("CORPORATE PAY"))//CORPORATE PAY
			{
				
				con = connectToCPay();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3), count(*) Trans_Count, sum(trans_amount)" +
						" from cpmtdb..cop_fundgate_log where  processed_date between '"+start_dt+"' and '"+end_dt+"' group by" +
						" substring(card_num,1,3) order by trans_count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();

					etran.setBank_code(""+result.getObject(1));
					etran.setTransaction_count(""+result.getObject(2));
					etran.setVolume(""+result.getObject(3));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("ASYCUDA TRANSACTION"))//ASYCUDA TRANSACTION
			{
				
				con = connectToPayoutlet();
				stat = con.createStatement();
				
				query = "select substring(merchant_code,1,3),count(*) Trans_Count,sum(trans_amount)" +
						" from payoutletdb..c_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' and" +
						" trans_descr like '%custom%' and substring(merchant_code,1,3) in ('056','232') group by substring(merchant_code,1,3) order by" +
						" trans_count desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setTransaction_count(""+result.getObject(2));
					etran.setVolume(""+result.getObject(3));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("eREMIT TRANSFER"))//eREMIT TRANSFER
			{
				
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select count(*), sum(amount) from ecarddb..imt_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"'";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setTransaction_count(""+result.getObject(1));
					etran.setVolume(""+result.getObject(2));
					
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("TOTAL MOBILE SUBSCRIBERS"))//TOTAL MOBILE SUBSCRIBERS
			{
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3) Bank, count(distinct card_num)" +
						" from e_mobile_subscriber_card group by" +
						" substring(card_num,1,3) order by count(*) desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
										
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("NEW MOBILE SUBSCRIBERS"))//NEW MOBILE SUBSCRIBERS
			{
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3) Bank, count(distinct card_num)" +
						" from ecarddb..e_mobile_subscriber_card where modified between '"+start_dt+"' and '"+end_dt+"'  group by" +
						" substring(card_num,1,3) order by count(*) desc";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
										
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("ACTIVE MOBILE SUBSCRIBERS"))//ACTIVE MOBILE SUBSCRIBERS
			{
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3) Bank, count(distinct card_num)" +
						" from e_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' " +
						" and unique_transid like '02%'  group by" +
						" substring(card_num,1,3) ";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
										
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("POS DEPLOYED"))//POS DEPLOYED
			{
				
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select count(*) from e_posterminal";

				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setTransaction_count(""+result.getObject(1));
										
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("POS PAYMENTS"))//POS PAYMENTS
			{
				
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3) Bank, count(distinct card_num) Card_Count,count(*) Trans_Count, sum(trans_amount)" +
						" Trans_Sum from e_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' and" +
						"  trans_code='p'  and channelid='03' group by substring(card_num,1,3) order by trans_count desc";
				
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
					etran.setTransaction_count(""+result.getObject(3));
					etran.setVolume(""+result.getObject(4));
										
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("POS CARDLOADS"))//POS CARDLOADS
			{
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select substring(card_num,1,3) Bank, count(distinct card_num) Card_Count,count(*) Trans_Count, sum(trans_amount)" +
						" Trans_Sum from e_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' and" +
						"  trans_code='d'  and channelid='03' group by substring(card_num,1,3)";

				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setBank_code(""+result.getObject(1));
					etran.setCard_count(""+result.getObject(2));
					etran.setTransaction_count(""+result.getObject(3));
					etran.setVolume(""+result.getObject(4));
										
					arr.add(etran);
				}
			}
			
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
			closeConnectionTestECard(con, result, result);
			closeConnectionCPay(con, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeConnectionTestECard(con, result, result);
			closeConnectionCPay(con, result);
		}
		return arr;
	}
	
	
	
	
	/*Method to get the switch report for each bank*/
	public ArrayList getSwitchReportByDateAndTypeAndBank(String start_dt, String end_dt, String type, String bank_code)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION etran = null;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		
		try
		{
			if(type.equalsIgnoreCase("REGISTERED SUBSCRIBERS"))//REGISTERED SUBSCRIBERS
			{
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select count(distinct(mobile)) from e_mobile_subscriber_card where substring(card_num,1,3) like '"+bank_code+"%' " +
						" and modified between '"+start_dt+"' and '"+end_dt+"' ";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setCard_count(""+result.getObject(1));
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("ACTIVE MOBILE SUBSCRIBERS"))//ACTIVE MOBILE SUBSCRIBERS
			{
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select count(distinct (mobile)) from e_smslog where substring(source_card, 1, 3) like '"+bank_code+"%' " +
						" and message_date between '"+start_dt+"' and '"+end_dt+"' ";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setCard_count(""+result.getObject(1));
										
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("TOTAL TRANSACTION COUNT"))//TOTAL TRANSACTION COUNT
			{
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select count(card_num)" +
						" from e_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' " +
						" and unique_transid like '02%' and substring(card_num,1,3) like '"+bank_code+"%' ";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setCard_count(""+result.getObject(1));
										
					arr.add(etran);
				}
			}
			else if(type.equalsIgnoreCase("TOTAL TRANSACTION VOLUME"))//TOTAL TRANSACTION VOLUME
			{
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select sum(trans_amount)" +
						" from e_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' " +
						" and unique_transid like '02%' and substring(card_num,1,3) like '"+bank_code+"%' ";
				
				System.out.println("general query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					etran = new E_TRANSACTION();
					etran.setCard_count(""+result.getObject(1));
										
					arr.add(etran);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Method to get the switch report for the scheme types*/
	public ArrayList getSwitchReportByDateAndTypeAndScheme(String start_dt, String end_dt, String type,
			String scheme, String dbServer )
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION etran = null;
		Connection con = null, con1 = null;
		Statement stat = null, stat1 = null;
		ResultSet result = null, result1 = null;
		int scheme_position = scheme.length();
		
		try
		{			
			etran = new E_TRANSACTION();
			
			/*created cards*/
			
			if(dbServer.equals("1"))//pocketmoni server
			{
				con = connectPocketMoniEcardDB();
				stat = con.createStatement();
			}
			else if(dbServer.equals("2"))//.57
			{
				con = connectToTestECard();
				stat = con.createStatement();
			}
			
			query = "select count(distinct card_num)" +
			" from e_cardholder where substring(track2, 7, "+scheme_position+") like '"+scheme+"%' ";
			
			
			System.out.println("cards created query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				etran.setCard_count(""+result.getObject(1));
			}
			
			/*active cards*/
			con1 = connectToECard();
			stat1 = con1.createStatement();
			
			query = "select count(distinct card_num)" +
					" from e_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' " +
					" and card_num like '"+scheme+"%' ";
			
			System.out.println("active query " + query);
			result1 = stat1.executeQuery(query);
			while(result1.next())
			{
				etran.setVolume(""+result1.getObject(1));
			}
			
			/*Inactive cards*/
			int diff = Integer.parseInt(etran.getCard_count()) - Integer.parseInt(etran.getVolume());
			etran.setSettle_batch(""+diff);
			arr.add(etran);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
			closeConnectionPocketMoniEcardDB(con1, result1);
			closeConnectionTestECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeConnectionPocketMoniEcardDB(con1, result1);
			closeConnectionTestECard(con, result, result);
		}
		return arr;
	}
	
	
	
	
	/*Method to get Status of a pin*/
	public ArrayList getPinLog(String payment_code)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		C_TRANSACTION ctran = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		
		try
		{
			con = connectToPayoutlet();
			stat = con.createStatement();
			
			query = "select payment_code, intstatus, (select merchant_name from ecarddb.dbo.e_merchant where MERCHANT_CODE = payoutletdb.dbo.c_transaction.MERCHANT_CODE) from payoutletdb..c_transaction where payment_code = '"+payment_code+"' ";
		
			System.out.println("pin log query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				
				ctran = new C_TRANSACTION();
				
				ctran.setCounter(""+counter);
				
				ctran.setPayment_code(""+result.getObject(1));
				ctran.setIntstatus(getPayoutletResponse(""+result.getObject(2)));
				ctran.setMerchant_code(""+result.getObject(3));
				
				arr.add(ctran);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPayoutlet(con, result);
		}
		finally
		{
			closeConnectionPayoutlet(con, result);
		}
		return arr;
	}
	

	
	public String getPayoutletResponse(String intstatus)
	{
		String message = "No response message";
		try
		{
			switch(Integer.parseInt(intstatus))
			{
				case 1:
					message = "Successful";
					break;

				case 2:
					message = "Transaction had already been sent";
					break;
					
				case 3:
					message = "Could have been sent successfully or not, the portal providers didn’t respond with a status";
					break;
					
				case 4:
					message = "Invalid URL Response Code";
					break;	
				
				case 5:
					message = "Socket Timeout  or Connect Exception";
					break;	
				
				case 6:
					message = "Illegal URL Address";
					break;
					
				case 7:
					message = "Could not Open Connection to URL";
					break;	
					
				default:
					
					break;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return message;
	}
	
	
	/*Method to get E_Exception*/
	public ArrayList getE_EXCEPTIONByPan(String pan)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_EXCEPTION exceptn = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select pan, reason, date from ecarddb..e_exception where pan = '"+pan+"' ";
		
			System.out.println("getE_EXCEPTION query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				
				exceptn = new E_EXCEPTION();
				
				exceptn.setCounter(""+counter);
				
				exceptn.setPan(""+result.getObject(1));
				exceptn.setReason(""+result.getObject(2));
				exceptn.setTran_date(""+result.getObject(3));
				
				arr.add(exceptn);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPayoutlet(con, result);
		}
		finally
		{
			closeConnectionPayoutlet(con, result);
		}
		return arr;
	}
	
	
	/*Method to get E_CardService*/
	public ArrayList getE_CardService(String card_num, String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_CARDSERVICE cservice = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select card_num, param0, created, modified from ecarddb..e_cardservice" +
					" where card_num like '"+card_num+"%' and created between '"+start_dt+"' and '"+end_dt+"' ";
		
			System.out.println("getE_CardService query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				
				cservice = new E_CARDSERVICE();
				
				cservice.setCounter(""+counter);
				
				cservice.setCard_num(""+result.getObject(1));
				cservice.setParam0(""+result.getObject(2));
				cservice.setCreated_dt(""+result.getObject(3));
				cservice.setModified(""+result.getObject(4));
				
				arr.add(cservice);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	
	/*Method to get Transactions based on a card number*/
	public ArrayList getCardHolderTransactions(String card_num, String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION tran = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		double d = 0.0;
		String str = "";
		
		
		try
		{
			con = connectToBackUpECard();
			stat = con.createStatement();
			
			
			query = "select trans_no, card_num, merchant_code, (select f_name from ecarddb..e_transcode where f_code = ecarddb..e_transaction.trans_code), trans_descr, (select channel_name from ecarddb..e_channel where channel_id = ecarddb..e_transaction.channelid), trans_date, " +
					"case when" +
						" card_num='"+card_num+"' " +
					"then -1*trans_amount " +
					"else" +
						" trans_amount " +
					"end" +
					" from ecarddb..e_transaction " +
					"where (card_num='"+card_num+"' or merchant_code = '"+card_num+"') " +
					"and trans_date between('"+start_dt+"') and ('"+end_dt+"') order by trans_date";
			
			
			System.out.println("getCardHolderTransactions query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				tran = new E_TRANSACTION();
				tran.setCounter(""+counter);
				tran.setTrans_no(""+result.getObject(1));
				tran.setCard_num(""+result.getObject(2));
				tran.setMerchat_code(""+result.getObject(3));
				tran.setTrans_code(""+result.getObject(4));
				tran.setTrans_desc(""+result.getObject(5));
				tran.setChannelid(""+result.getObject(6));
				tran.setTrans_date(""+result.getObject(7));
				
				d = Double.parseDouble(""+result.getObject(8));
				if(d > 0)
				{
					tran.setCreditAmt(""+d);
					tran.setDebitAmt("");
				}
				else
				{
					str = ""+d;
					tran.setDebitAmt(str.substring(str.lastIndexOf("-")+1));
					tran.setCreditAmt("");
				}
					
				arr.add(tran);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionBackUpEcard(con, result, result);
		}
		finally
		{
			closeConnectionBackUpEcard(con, result, result);
		}
		return arr;
	}
	

	/*Method to get Transactions based on a card number and scheme*/
	public ArrayList getCardHolderTransactions_scheme(String card_num, String start_dt, String end_dt, String scheme,
			String channelid, String dbServer)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION tran = null;
		int counter = 0;
		Connection con = null, con1 = null;
		Statement stat = null, stat1 = null;
		ResultSet result = null, result1 = null;
		double d = 0.0;
		String str = "";
		String actual_cardnum = "";
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			if(dbServer.equals("1"))//pocketmoni server
			{
				con1 = connectPocketMoniEcardDB();
				stat1 = con1.createStatement();
			}
			else if(dbServer.equals("2"))//.57
			{
				con1 = connectToTestECard();
				stat1 = con1.createStatement();
			}
			
			if(card_num.trim().length() > 16)//the card num here is actually the track 2
			{
				query = "select card_num from e_cardholder where track2 = '"+card_num+"' ";
				result1 = stat1.executeQuery(query);
				if(result1.next())
				{
					actual_cardnum = ""+result1.getObject(1);
				}
			}
			else
			{
				actual_cardnum = card_num;
			}
			
			
			if(card_num.trim().length() > 0)
			{	
				if(channelid.equals("00"))//ALL
				{
					query = "select trans_no,card_num, merchant_code," +
					" (select f_name from ecarddb..e_transcode where f_code = ecarddb..e_transaction.trans_code)," +
					" trans_descr, (select channel_name from ecarddb..e_channel where channel_id = ecarddb..e_transaction.channelid)," +
					" trans_date, " +
					" case when" +
						" card_num = '"+actual_cardnum+"%' " +
					" then -1*trans_amount" +
					" else " +
						"trans_amount " +
					" end " +
						" from ecarddb..e_transaction where trans_date between('"+start_dt+"') and ('"+end_dt+"') " +
					" and (card_num = '"+actual_cardnum+"' or merchant_code = '"+actual_cardnum+"' )" +
					" and (card_num like '"+scheme+"%' or merchant_code like '"+scheme+"%') " +
					" order by trans_date desc";
				}
				else
				{
					query = "select trans_no,card_num, merchant_code," +
					" (select f_name from ecarddb..e_transcode where f_code = ecarddb..e_transaction.trans_code)," +
					" trans_descr, (select channel_name from ecarddb..e_channel where channel_id = ecarddb..e_transaction.channelid)," +
					" trans_date, " +
					" case when" +
						" card_num = '"+actual_cardnum+"%' " +
					" then -1*trans_amount" +
					" else " +
						"trans_amount " +
					" end " +
						" from ecarddb..e_transaction where trans_date between('"+start_dt+"') and ('"+end_dt+"') " +
					" and (card_num = '"+actual_cardnum+"' or merchant_code = '"+actual_cardnum+"' )" +
					" and (card_num like '"+scheme+"%' or merchant_code like '"+scheme+"%') " +
					" and channelid = '"+channelid+"' order by trans_date desc";
				}
				
				/*query = "select trans_no, card_num, merchant_code," +
				" (select f_name from ecarddb..e_transcode where f_code = ecarddb..e_transaction.trans_code)," +
				" trans_descr, (select channel_name from ecarddb..e_channel where channel_id = ecarddb..e_transaction.channelid)," +
				" trans_date, trans_amount from ecarddb..e_transaction where card_num = '"+actual_cardnum+"' and card_num like '"+scheme+"%'" +
				" and channelid = '"+channelid+"' and trans_date between('"+start_dt+"') and ('"+end_dt+"') order by trans_date";*/

			}
			else
			{
				/*query = "select trans_no,card_num, merchant_code," +
				" (select f_name from ecarddb..e_transcode where f_code = ecarddb..e_transaction.trans_code)," +
				" trans_descr, (select channel_name from ecarddb..e_channel where channel_id = ecarddb..e_transaction.channelid)," +
				" trans_date, trans_amount from ecarddb..e_transaction where card_num like '"+scheme+"%' and channelid = '"+channelid+"' and " +
				" trans_date between('"+start_dt+"') and ('"+end_dt+"') order by trans_date";*/
				
				if(channelid.equals("00"))//ALL
				{
					query = "select trans_no,card_num, merchant_code," +
					" (select f_name from ecarddb..e_transcode where f_code = ecarddb..e_transaction.trans_code)," +
					" trans_descr, (select channel_name from ecarddb..e_channel where channel_id = ecarddb..e_transaction.channelid)," +
					" trans_date, " +
					" case when" +
						" card_num like '"+scheme+"%' " +
					" then -1*trans_amount" +
					" else " +
						"trans_amount " +
					" end " +
						" from ecarddb..e_transaction where trans_date between('"+start_dt+"') and ('"+end_dt+"') " +
					" and (card_num like '"+scheme+"%' or merchant_code like '"+scheme+"%') " +
					" order by trans_date desc";
				}
				else
				{
					query = "select trans_no,card_num, merchant_code," +
					" (select f_name from ecarddb..e_transcode where f_code = ecarddb..e_transaction.trans_code)," +
					" trans_descr, (select channel_name from ecarddb..e_channel where channel_id = ecarddb..e_transaction.channelid)," +
					" trans_date, " +
					" case when" +
						" card_num like '"+scheme+"%' " +
					" then -1*trans_amount" +
					" else " +
						"trans_amount " +
					" end " +
						" from ecarddb..e_transaction where trans_date between('"+start_dt+"') and ('"+end_dt+"') " +
					" and (card_num like '"+scheme+"%' or merchant_code like '"+scheme+"%') " +
					" and channelid = '"+channelid+"' order by trans_date desc";
				}
			}
			System.out.println("Card_num  "+card_num+"Card_Scheme  "+scheme);
			System.out.println("getCardHolderTransactions query " + query);
			result = stat.executeQuery(query);

			while(result.next())
			{
				counter++;
				tran = new E_TRANSACTION();
				tran.setCounter(""+counter);
				tran.setTrans_no(""+result.getObject(1));
				tran.setCard_num(""+result.getObject(2));
				tran.setMerchat_code(""+result.getObject(3));
				tran.setTrans_code(""+result.getObject(4));
				tran.setTrans_desc(""+result.getObject(5));
				tran.setChannelid(""+result.getObject(6));
				tran.setTrans_date(""+result.getObject(7));
				//tran.setTrans_amount(""+result.getObject(8));
				
				d = Double.parseDouble(""+result.getObject(8));
				if(d > 0)
				{
					tran.setCreditAmt(""+d);
					tran.setDebitAmt("");
				}
				else
				{
					str = ""+d;
					tran.setDebitAmt(str.substring(str.lastIndexOf("-")+1));
					tran.setCreditAmt("");
				}
				
				arr.add(tran);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
			closeConnectionPocketMoniEcardDB(con1, result1);
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeConnectionPocketMoniEcardDB(con1, result1);
		}
		return arr;
	}
	
	
	/*Method to get Transactions based on an international merchant_code*/
	public ArrayList getMerchantTransactions(String type, String card_num, String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION tran = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		double d = 0.0;
		String str = "";
		
		
		try
		{
			if(type.equals("0"))//LOCAL
			{
				
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select trans_no, card_num, merchant_code, trans_code, trans_descr, (select channel_name from ecarddb..e_channel where channel_id = ecarddb..e_transaction.channelid), trans_date, " +
				"case when" +
					" card_num='"+card_num+"' " +
				"then -1*trans_amount " +
				"else" +
					" trans_amount " +
				"end" +
				" from ecarddb..e_transaction " +
				"where (card_num='"+card_num+"' or merchant_code = '"+card_num+"') " +
				"and trans_date between('"+start_dt+"') and ('"+end_dt+"') order by trans_date";
				
				System.out.println("getMerchantTransactions local query " + query);
			}
			else//INTERNATIONAL
			{
				con = connectIntnlECard();
				stat = con.createStatement();
				
				query = "select trans_no, card_num, merchant_code, trans_code, trans_descr, (select channel_name from ecarddb..e_channel where channel_id = ecarddb..e_transaction.channelid), trans_date, " +
				"case when" +
					" card_num='"+card_num+"' " +
				"then -1*trans_amount " +
				"else" +
					" trans_amount " +
				"end" +
				" from ecarddb..e_transaction " +
				"where (card_num='"+card_num+"' or merchant_code = '"+card_num+"') " +
				"and trans_date between('"+start_dt+"') and ('"+end_dt+"') order by trans_date";
				
				System.out.println("getMerchantTransactions intl query " + query);
			}
			
			
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				tran = new E_TRANSACTION();
				tran.setCounter(""+counter);
				tran.setTrans_no(""+result.getObject(1));
				tran.setCard_num(""+result.getObject(2));
				tran.setMerchat_code(""+result.getObject(3));
				tran.setTrans_code(""+result.getObject(4));
				tran.setTrans_desc(""+result.getObject(5));
				tran.setChannelid(""+result.getObject(6));
				tran.setTrans_date(""+result.getObject(7));
				
				d = Double.parseDouble(""+result.getObject(8));
				if(d > 0)
				{
					tran.setCreditAmt(""+d);
					tran.setDebitAmt("");
					
				}
				else
				{
					str = ""+d;
					tran.setDebitAmt(str.substring(str.lastIndexOf("-")+1));
					tran.setCreditAmt("");
				}
					
				arr.add(tran);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionIntnlEcard(con, result);
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionIntnlEcard(con, result);
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	
	
	/*Method to get Transactions based on a merchant_code*/
	public ArrayList getOnlyMerchantTransactions(String card_num, String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION tran = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		double d = 0.0;
		String str = "";
		
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			
			query = "select trans_no, card_num, merchant_code, trans_code, trans_descr," +
					" (select merchant_name from ecarddb..e_merchant where e_merchant.merchant_code = ecarddb..e_transaction.card_num)," + 	
					" (select channel_name from ecarddb..e_channel where channel_id = ecarddb..e_transaction.channelid), trans_date, trans_amount" +
					" from ecarddb..e_transaction " +
					"where (card_num='"+card_num+"' or merchant_code = '"+card_num+"') " +
					"and trans_date between('"+start_dt+"') and ('"+end_dt+"') order by trans_date";
			
			System.out.println("getOnlyMerchantTransactions query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				tran = new E_TRANSACTION();
				tran.setCounter(""+counter);
				tran.setTrans_no(""+result.getObject(1));
				tran.setCard_num(""+result.getObject(2));
				tran.setMerchat_code(""+result.getObject(3));
				tran.setTrans_code(""+result.getObject(4));
				tran.setTrans_desc(""+result.getObject(5));
				tran.setMerchant_descr(""+result.getObject(6));
				tran.setChannelid(""+result.getObject(7));
				tran.setTrans_date(""+result.getObject(8));
				tran.setTrans_amount(""+result.getObject(9));
				arr.add(tran);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Method to get lotto transactions*/
	public ArrayList getLottoTransactions(String lotto_no, String phone_no, String terminal_id, String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		LOTTO_LOG lotto = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		double d = 0.0;
		String str = "";
		
		
		try
		{
			con = connectToETMC();
			stat = con.createStatement();
			
			query = "select created, trans_amount, phone_number, choice_number, lucky_number," +
					" winning_combinations, terminal_id, merchant_id" +
					" from ecarddb..lotto_log" +
					" where choice_number like '"+ lotto_no + "%' and phone_number like '"+ phone_no + "%' and" +
					" terminal_id like '"+terminal_id+"%' and" +
					" terminal_id not in ('08405972','08405970','08405973','08405969','08405968','08405975'," +
					" '08405974','08405983','08405984','08405971','08405994','08405993','08405991','08405990'," +
					" '08405992','08405986','08405987','08405988','08405985','08405989','08405995','08405996'," +
					" '08405997','08405998','08405999','08406000','08406004','08406002','08406001','08406003'," +
					" '08406005','08406006','08406007','08406008','08406009','08406010','08406011','08406012'," +
					" '08406013','08406014') and " +
					" created between('"+start_dt+"') and ('"+end_dt+"') order by created desc";
			
			System.out.println("getMerchantTransactions query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				lotto = new LOTTO_LOG();
				lotto.setCounter(""+counter);
				lotto.setCreated(""+result.getObject(1));
				lotto.setTrans_amount(""+result.getObject(2));
				lotto.setPhone_number(""+result.getObject(3));
				lotto.setChoice_number(""+result.getObject(4));
				lotto.setLucky_number(""+result.getObject(5));
				lotto.setWinning_combinations(""+result.getObject(6));
				lotto.setTerminal_id(""+result.getObject(7));
				lotto.setMerchant_id(""+result.getObject(8));
				//lotto.setId(""+result.getObject(9));
				arr.add(lotto);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionETMC(con, result);
		}
		finally
		{
			closeConnectionETMC(con, result);
		}
		return arr;
	}
	
	/*Method to get MTO Transfer transactions*/
	public ArrayList getMTOTransactionsGroupByChannelAndMerchantCode(String start_dt, String end_dt,
			String type, String card_num, String trans_code)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION tran = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		double d = 0.0;
		String str = "";
		
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			if(trans_code.equals("07"))//Cash PickUp
			{
				if(type.equals("0"))//successful
				{
					query = "select sum(fee), count(*), sum(trans_amount), card_num from e_transaction where trans_code = 'T' " +
					" and channelid in ('07') and substring(card_num, 1,6) like '084889%' and" +
					" card_num like '"+card_num+"%' and trans_date between('"+start_dt+"') and ('"+end_dt+"') group by card_num ";
			
					System.out.println("getMerchantTransactions query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						tran = new E_TRANSACTION();
						tran.setFee(""+result.getObject(1));
						tran.setTransaction_count(""+result.getObject(2));
						tran.setTotal_amount(""+result.getObject(3));
						tran.setCard_num(""+result.getObject(4));
						
						arr.add(tran);
					}
				}
				else
				{
					/*query = "select count(*), sum(trans_amount), card_num from e_requestlog where trans_code = 'T'" +
					" and channelid in ('07','09','11') " +
					" and substring(card_num, 1,6) like '084889%' and trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
					" and card_num like '"+card_num+"%'  group by channelid, card_num order by channelid";
			
					System.out.println("getMerchantTransactions query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						tran = new E_TRANSACTION();
						tran.setTransaction_count(""+result.getObject(1));
						tran.setTotal_amount(""+result.getObject(2));
						tran.setCard_num(""+result.getObject(3));
						tran.setChannelid("");
						
						arr.add(tran);
					}*/
				}
			}
			else if(trans_code.equals("09"))//Direct To Account
			{
				if(type.equals("0"))//successful
				{
					query = "select sum(fee), count(*), sum(trans_amount), card_num from e_transaction where trans_code = 'T' " +
					" and channelid in ('09') and substring(card_num, 1,6) like '084889%' and" +
					" card_num like '"+card_num+"%' and trans_date between('"+start_dt+"') and ('"+end_dt+"') group by card_num ";
			
					System.out.println("getMerchantTransactions query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						tran = new E_TRANSACTION();
						tran.setFee(""+result.getObject(1));
						tran.setTransaction_count(""+result.getObject(2));
						tran.setTotal_amount(""+result.getObject(3));
						tran.setCard_num(""+result.getObject(4));
						
						arr.add(tran);
					}
				}
				else
				{
					/*query = "select count(*), sum(trans_amount), card_num from e_requestlog where trans_code = 'T'" +
					" and channelid in ('07','09','11') " +
					" and substring(card_num, 1,6) like '084889%' and trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
					" and card_num like '"+card_num+"%'  group by channelid, card_num order by channelid";
			
					System.out.println("getMerchantTransactions query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						tran = new E_TRANSACTION();
						tran.setTransaction_count(""+result.getObject(1));
						tran.setTotal_amount(""+result.getObject(2));
						tran.setCard_num(""+result.getObject(3));
						tran.setChannelid("");
						
						arr.add(tran);
					}*/
				}
			}
			else if(trans_code.equals("00"))//ALL
			{
				if(type.equals("0"))//successful
				{
					query = "select sum(fee), count(*), sum(trans_amount), card_num from e_transaction where trans_code = 'T' " +
					" and channelid in ('07','09','17') and substring(card_num, 1,6) like '084889%' and" +
					" card_num like '"+card_num+"%' and trans_date between('"+start_dt+"') and ('"+end_dt+"') group by card_num ";
			
					System.out.println("getMerchantTransactions query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						tran = new E_TRANSACTION();
						tran.setFee(""+result.getObject(1));
						tran.setTransaction_count(""+result.getObject(2));
						tran.setTotal_amount(""+result.getObject(3));
						tran.setCard_num(""+result.getObject(4));
						
						arr.add(tran);
					}
				}
				else
				{
					/*query = "select count(*), sum(trans_amount), card_num from e_requestlog where trans_code = 'T'" +
					" and channelid in ('07','09','11') " +
					" and substring(card_num, 1,6) like '084889%' and trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
					" and card_num like '"+card_num+"%'  group by channelid, card_num order by channelid";
			
					System.out.println("getMerchantTransactions query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						tran = new E_TRANSACTION();
						tran.setTransaction_count(""+result.getObject(1));
						tran.setTotal_amount(""+result.getObject(2));
						tran.setCard_num(""+result.getObject(3));
						tran.setChannelid("");
						
						arr.add(tran);
					}*/
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*Method to get MTO Transfer transactions details*/
	public ArrayList getMTOTransactionsDetails(String card_num, String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION tran = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		double d = 0.0;
		String str = "";
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select trans_date, fee, trans_amount, card_num, trans_ref," +
					" substring(merchant_code, 1,3) from e_transaction where trans_code = 'T'" +
			" and channelid in ('07','09','17') and trans_date between('"+start_dt+"') and ('"+end_dt+"') and card_num = '"+card_num+"' ";
	
			System.out.println("getMerchantTransactions query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				tran = new E_TRANSACTION();
				
				
				str = ""+result.getObject(1);
				tran.setTrans_date(str.substring(0, str.indexOf(" ")));
				tran.setFee(""+result.getObject(2));
				tran.setTrans_amount(""+result.getObject(3));
				tran.setCard_num(""+result.getObject(4));
				tran.setTrans_ref(""+result.getObject(5));
				tran.setTran_time(str.substring(str.indexOf(" ")));
				tran.setMerchat_code(""+result.getObject(6));
				arr.add(tran);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	/*Method to get MTO Summary details*/
	public ArrayList getMTOTransactionSummary(String status, String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION tran = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		double d = 0.0;
		String str = "";
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			if(status.equals("0"))//successful
			{
				query = "select sum(fee), count(*), sum(trans_amount) from e_transaction where trans_code = 'T' " +
				" and channelid in ('07','09','17') and substring(card_num, 1,6) like '084889%' ";
		
				System.out.println("getMTOSummaryDetails query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					tran = new E_TRANSACTION();
					
					tran.setFee(""+result.getObject(1));
					tran.setTransaction_count(""+result.getObject(2));
					tran.setTotal_amount(""+result.getObject(3));
					
					arr.add(tran);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	public ArrayList getRequestLogData(String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		REQUEST_LOG req = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		double d = 0.0;
		String str = "";
		
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select count(*) as 'Count', substring(card_num, 1, 3) as 'Bank Code', sum(convert(decimal(18,3),trans_amount)) from ecarddb..e_requestlog where" +
					" response_code = '0' and trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
					" group by substring(card_num,1,3) order by substring(card_num,1,3)";
			
			System.out.println("getRequestLogData query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				req = new REQUEST_LOG();
				req.setTrans_code(""+result.getObject(1));
				req.setCard_num(""+result.getObject(2));
				req.setTrans_amount(""+result.getObject(3));
				
				arr.add(req);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	public ArrayList getETransactionLogData(String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION tran = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		double d = 0.0;
		String str = "";
		
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select count(*) as 'Count', substring(card_num, 1, 3) as 'Bank Code', sum(trans_amount) from ecarddb..e_transaction" +
					" where trans_date between('"+start_dt+"') and ('"+end_dt+"') group by substring(card_num,1,3) order by  substring(card_num,1,3)";
			
			System.out.println("getETransactionLogData query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				tran = new E_TRANSACTION();
				tran.setTrans_code(""+result.getObject(1));
				tran.setCard_num(""+result.getObject(2));
				tran.setTrans_amount(""+result.getObject(3));
				
				arr.add(tran);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	/*Method to delete the exception*/
	public String deleteE_EXCEPTION(String pan, String date)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "delete from ecarddb..e_exception where pan = '"+pan+"' and date = '"+date+"'  ";
		
			System.out.println("deleteE_EXCEPTION query " + query);
			output = stat.executeUpdate(query);
			if(output > 0)
			{
				message = "Records successfully deleted";
				con.commit();
			}
			else
			{
				message = "Records not deleted";
				con.rollback();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return message;
	}
	
	
	/*Method to create card service*/
	public String createE_CardService(String card_num, String mobile_no)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "insert into ecarddb..e_cardservice(card_num, service_id, param0)values('"+card_num+"', '01', '"+mobile_no+"') ";
		
			System.out.println("createdE_CardService query " + query);
			output = stat.executeUpdate(query);
			if(output > 0)
			{
				message = "Records successfully inserted";
				con.commit();
			}
			else
			{
				message = "Records not inserted";
				con.rollback();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return message;
	}
	
	
	/*Method to create e_exception data*/
	public String createE_Exception(String card_num, String reason)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select pan from e_exception where pan = '"+card_num+"' ";
			result  = stat.executeQuery(query);
			if(result.next())
			{
				message = "Records not hotlisted, data already exists";
			}
			else
			{
				query = "insert into ecarddb..e_exception(pan, reason, date)values('"+card_num+"', '"+reason+"', getDate()) ";
			
				System.out.println("createE_Exception query " + query);
				output = stat.executeUpdate(query);
				if(output > 0)
				{
					message = "Records successfully hotlisted";
					con.commit();
				}
				else
				{
					message = "Records not hotlisted";
					con.rollback();
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return message;
	}
	
	
	/*Method to resend bill payment*/
	public String resendBillPayment(String unique_transid)
	{
		String query = "";
		String message = "";
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;

		try
		{
			con = connectToTelco();
			stat = con.createStatement();
			
			query = "update t_paytrans set process_status  = null where unique_transid = '"+unique_transid+"' ";
			System.out.println("resendBillPayment query " + query);
			output = stat.executeUpdate(query);
			if(output > 0)
			{
				message = "Records successfully updated";
				con.commit();
			}
			else
			{
				message = "Records not updated";
				con.rollback();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
		}
		return message;
	}
	
	/*Method to edit bill payment*/
	public String editBillPayment(String smartcard_no, String unique_transid)
	{
		String query = "";
		String message = "";
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;

		try
		{
			con = connectToTelco();
			stat = con.createStatement();
			
			query = "update t_paytrans set mobile_no = '"+smartcard_no+"' , set process_status  = null where unique_transid = '"+unique_transid+"' ";
			System.out.println("editBillPayment query " + query);
			output = stat.executeUpdate(query);
			if(output > 0)
			{
				message = "Records successfully updated";
				con.commit();
			}
			else
			{
				message = "Records not updated";
				con.rollback();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
		}
		return message;
	}
	
	
	public ArrayList getE_SETTLEMENTDOWNLOADBKBy058DetailsGTB(String settle_batch,
			String transcode, String card_num, String merchant_code,String bankCode)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_SETTLEMENTDOWNLOAD_BK e_settlement;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;

		try
		{
			con = connectToECard();
			stat = con.createStatement();

			query = "  SELECT "
                                + "ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.TRANS_DATE,ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.MERCHANT_CODE,"
                                + " replicate('0',12 - char_length(rtrim(convert(varchar(38),"
                                + "ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.DEST_REF)))) + rtrim(convert(varchar(38),"
                                + "ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.DEST_REF)) TRANSID,"
                                + "ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.TRANS_DESCR,"
                                + "UNIQUE_TRANSID ,"
                                + "ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.TRANS_AMOUNT "
                                + "FROM ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK"
                                + "   WHERE ( ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.CARD_NUM not like '"+bankCode+"%' ) AND  "
                                + "         ( ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.MERCHANT_CODE like '"+bankCode+"%' ) AND  "
                                + "         ( ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.SETTLE_BATCH = '"+settle_batch+"' ) AND  "
                                + "         ( ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.TRANS_CODE = 'T' )   AND"
                                + "                ( ( substring(ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.MERCHANT_CODE,1,6) not in "
                                + "(select issuer_branch from e_exfrontendinterface) ) OR"
                                + "  ( substring(ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.MERCHANT_CODE,1,6) not in "
                                + "(select issuer_code from e_frontendinterface) ) ) AND"
                                + "  ( substring(ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.CARD_NUM,1,3) <>  substring(ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.MERCHANT_CODE,1,3) ) "
                                + "ORDER BY ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.TRANS_DATE";


			System.out.println("getE_SETTLEMENTDOWNLOADBKBy058Details " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				e_settlement = new E_SETTLEMENTDOWNLOAD_BK();
				//e_settlement.setCard_num(result.getString(1));
				e_settlement.setTrans_no(result.getString(5));
				e_settlement.setTrans_desc(result.getString(4));
				e_settlement.setTrans_date(result.getString(1));
				e_settlement.setChannelid(result.getString(3));
				e_settlement.setMerchat_code(result.getString(2));
				e_settlement.setTrans_amount(result.getString(6));


				arr.add(e_settlement);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}

        public ArrayList getE_SETTLEMENTDOWNLOADBKBy058GTB(String card_num,
			String merchant_code, String transcode, String start_dt, String end_dt,String bank)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_SETTLEMENTDOWNLOAD_BK e_settlement;
		E_SETTLE_BATCH e_settle;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;

		try
		{
			con = connectToECard();
			stat = con.createStatement();

			query = "SELECT ecarddb.dbo.E_SETTLE_BATCH.BATCH_DATE,ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.SETTLE_BATCH, sum(ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.TRANS_AMOUNT) "
                                + "FROM ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK    LEFT OUTER JOIN ecarddb.dbo.E_MERCHANT ON substring(ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.MERCHANT_CODE,1,3)+'TRASFER' ="
                                + " ecarddb.dbo.E_MERCHANT.MERCHANT_CODE LEFT OUTER JOIN ecarddb.dbo.E_SETTLE_BATCH ON ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.SETTLE_BATCH ="
                                + " ecarddb.dbo.E_SETTLE_BATCH.BATCH_ID  WHERE ( ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.CARD_NUM not like '"+bank+"%' ) AND"
                                + " ( ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.MERCHANT_CODE like '"+bank+"%' ) AND "
                                + " ( ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.TRANS_CODE = 'T' )   AND ( ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.TRANS_DATE BETWEEN '"+start_dt+"' "
                                + "AND '"+end_dt+"')   AND ( ( substring(ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.MERCHANT_CODE,1,6) "
                                + "not in (select issuer_branch from e_exfrontendinterface) ) OR"
                                + " ( substring(ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.MERCHANT_CODE,1,6) not in "
                                + "(select issuer_code from e_frontendinterface) ) ) AND"
                                + " ( substring(ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.CARD_NUM,1,3) <>  substring(ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.MERCHANT_CODE,1,3) ) "
                                + " GROUP BY ecarddb.dbo.E_SETTLE_BATCH.BATCH_DATE, ecarddb.dbo.E_SETTLEMENT_DOWNLOAD_BK.SETTLE_BATCH ORDER BY ecarddb.dbo.E_SETTLE_BATCH.BATCH_DATE";

			System.out.println("getE_SETTLEMENTDOWNLOADBKBy058 " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				e_settle = new E_SETTLE_BATCH();

				e_settle.setBatch_id(result.getString(2));
				e_settle.setBatch_date(result.getString(1));
				//e_settle.setStart_date(result.getString(3));
				//e_settle.setEnd_date(result.getString(4));
				e_settle.setTotal_amount(result.getString(3));

				arr.add(e_settle);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
        
        
    /*Corporate Pay Settlement*/    
    public ArrayList getAllCorporatePayTransactions(String beneficiary,
			String start_dt, String end_dt, String option, String status)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		COP_FUNDGATE_LOG cop_fundate;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		String pending = "Tra','Null','A10','A11";
		String reversed = "R','Rev','Re";
		String successful = "0','00','000";
		String failed = "Tra','Null','A10','A11','R','Rev','Re','0','00','000";
		
		String iso = "";
		
		String approstrophe = "'";

		try
		{
			con = connectToCPay();
			stat = con.createStatement();
			
			if(status.equals("SUCCESSFUL"))
			{
				iso = successful;
				iso = " AND HOST_ERROR_CODE in ('"+iso+"') ";
			}
			else if(status.equals("FAILED"))
			{
				iso = failed;
				iso = " AND HOST_ERROR_CODE not in ('"+iso+"') ";
			}
			else if(status.equals("REVERSED"))
			{
				iso = reversed;
				iso = " AND HOST_ERROR_CODE in ('"+iso+"') ";
			}
			else if(status.equals("PENDING"))
			{
				iso = pending;
				iso = " AND HOST_ERROR_CODE in ('"+iso+"') ";
			}
			
			if(option.equals("1"))//receiving
			{
				query = "SELECT COP_FUNDGATE_LOG.PAYMENT_ID as 'PAYMENT ID'," +
				" COP_MERCHANT.COMPANY_NAME as 'MERCHANT NAME'," +
				" COP_FUNDGATE_LOG.SOURCE_CARD_NUM as 'SOURCE CARD'," +
				" COP_FUNDGATE_LOG.CARD_NUM as 'HOLDING CARD'," +
				" COP_FUNDGATE_LOG.UPLOAD_DATE as 'UPLOAD DATE'," +
				" COP_FUNDGATE_LOG.PROCESSED_DATE as 'PROCESSED DATE'," +
				" COP_FUNDGATE_LOG.HOST_ERROR_CODE as 'STATUS'," +
				" COP_FUNDGATE_LOG.BATCH_ID as 'BATCH ID'," +
				" COP_FUNDGATE_LOG.REFERENCE as 'REFERENCE'," +
				" COP_FUNDGATE_LOG.ISSUER_CODE as 'BANK'," +
				" COP_FUNDGATE_LOG.ACCOUNT_NUMBER as 'ACCOUNT NUMBER'," +
				" COP_FUNDGATE_LOG.TRANS_AMOUNT as 'AMOUNT'," +
				" COP_FUNDGATE_LOG.BENEFICIARY_NAME as 'NAME'," +
				" COP_FUNDGATE_LOG.DESCRIPTION as 'NARRATION'," +
				" COP_FUNDGATE_LOG.S_BATCHID," +
				" COP_FUNDGATE_LOG.R_BATCHID," +
				" COP_FUNDGATE_LOG.PROCESS_STATUS" +
				" FROM  COP_FUNDGATE_LOG, COP_MERCHANT" +
				" WHERE COP_FUNDGATE_LOG.COMPANY_ID = COP_MERCHANT.COMPANY_ID" +
				" AND COP_FUNDGATE_LOG.ISSUER_CODE = '"+beneficiary+"' " +
				" AND SUBSTRING(COP_FUNDGATE_LOG.CARD_NUM,1,3) <> '"+beneficiary+"' " + iso + 
				" AND COP_FUNDGATE_LOG.UPLOAD_DATE BETWEEN('"+start_dt+"') AND ('"+end_dt+"') ORDER BY COP_FUNDGATE_LOG.ISSUER_CODE";
			}
			else if(option.equals("2"))//intiating
			{
				query = "SELECT COP_FUNDGATE_LOG.PAYMENT_ID as 'PAYMENT ID'," +
				" COP_MERCHANT.COMPANY_NAME as 'MERCHANT NAME'," +
				" COP_FUNDGATE_LOG.SOURCE_CARD_NUM as 'SOURCE CARD'," +
				" COP_FUNDGATE_LOG.CARD_NUM as 'HOLDING CARD'," +
				" COP_FUNDGATE_LOG.UPLOAD_DATE as 'UPLOAD DATE'," +
				" COP_FUNDGATE_LOG.PROCESSED_DATE as 'PROCESSED DATE'," +
				" COP_FUNDGATE_LOG.HOST_ERROR_CODE as 'STATUS'," +
				" COP_FUNDGATE_LOG.BATCH_ID as 'BATCH ID'," +
				" COP_FUNDGATE_LOG.REFERENCE as 'REFERENCE'," +
				" COP_FUNDGATE_LOG.ISSUER_CODE as 'BANK'," +
				" COP_FUNDGATE_LOG.ACCOUNT_NUMBER as 'ACCOUNT NUMBER'," +
				" COP_FUNDGATE_LOG.TRANS_AMOUNT as 'AMOUNT'," +
				" COP_FUNDGATE_LOG.BENEFICIARY_NAME as 'NAME'," +
				" COP_FUNDGATE_LOG.DESCRIPTION as 'NARRATION'," +
				" COP_FUNDGATE_LOG.S_BATCHID," +
				" COP_FUNDGATE_LOG.R_BATCHID," +
				" COP_FUNDGATE_LOG.PROCESS_STATUS" +
				" FROM  COP_FUNDGATE_LOG, COP_MERCHANT" +
				" WHERE COP_FUNDGATE_LOG.COMPANY_ID = COP_MERCHANT.COMPANY_ID" +
				" AND SUBSTRING(COP_FUNDGATE_LOG.CARD_NUM,1,3) = '"+beneficiary+"' " +
				" AND COP_FUNDGATE_LOG.ISSUER_CODE <> '"+beneficiary+"' " + iso +
				" AND COP_FUNDGATE_LOG.UPLOAD_DATE BETWEEN('"+start_dt+"') AND ('"+end_dt+"') ORDER BY COP_FUNDGATE_LOG.ISSUER_CODE";
			}
			

			
			System.out.println("getAllCorporatePayTransactionsToBeneficiary " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				cop_fundate = new COP_FUNDGATE_LOG();

				cop_fundate.setPAYMENT_ID(result.getString(1));
				cop_fundate.setCOMPANY_ID(result.getString(2));
				cop_fundate.setSOURCE_CARD_NUM(result.getString(3));
				cop_fundate.setCARD_NUM(result.getString(4));
				cop_fundate.setUPLOAD_DATE(result.getString(5));
				cop_fundate.setPROCESSED_DATE(result.getString(6));
				cop_fundate.setHOST_ERROR_CODE(result.getString(7));
				cop_fundate.setBATCH_ID(result.getString(8));
				cop_fundate.setREFERENCE(result.getString(9));
				cop_fundate.setISSUER_CODE(result.getString(10));
				cop_fundate.setACCOUNT_NUMBER(result.getString(11));
				cop_fundate.setTRANS_AMOUNT(result.getString(12));
				cop_fundate.setBENEFICIARY_NAME(result.getString(13));
				cop_fundate.setDESCRIPTION(result.getString(14));
				cop_fundate.setS_BATCHID(result.getString(15));
				cop_fundate.setR_BATCHID(result.getString(16));
				cop_fundate.setPROCESS_STATUS(result.getString(17));

				arr.add(cop_fundate);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionCPay(con, result);
		}
		finally
		{
			closeConnectionCPay(con, result);
		}
		return arr;
	}    
    
    
    /*Funds Transfer For Settlement*/    
    public ArrayList getAllFundsTransferTransactions_Settlement(String beneficiary,
			String start_dt, String end_dt, String option, String status)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		REQUEST_LOG request_log;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		String iso = "";
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			if(status.equals("SUCCESSFUL"))
			{
				iso = " AND response_code in ('0') ";
			}
			else
			{
				iso = " AND response_code not in ('0') ";
			}
			
			if(option.equals("1"))
			{
				query = "select TRANSID, CARD_NUM, TRANS_DATE," +
				"TRANS_AMOUNT,TRANS_CODE, MERCHANT_CODE, response_code, response_time, trans_descr " +
				" from ecarddb..e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
				" and substring(merchant_code,1,3) like '"+beneficiary+"%'" +
				" and substring(card_num,1,3) not like '"+beneficiary+"%'" + iso + 
				" and trans_code = 'T'  order by trans_date desc";
			}
			else if(option.equals("2"))//intiating
			{
				query = "select TRANSID, CARD_NUM, TRANS_DATE," +
				"TRANS_AMOUNT,TRANS_CODE, MERCHANT_CODE, response_code, response_time, trans_descr " +
				" from ecarddb..e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
				" and substring(card_num,1,3) like '"+beneficiary+"%'" +
				" and substring(merchant_code,1,3) not like '"+beneficiary+"%'" + iso + 
				" and trans_code = 'T'  order by trans_date desc";
			}
			

			
			System.out.println("getAllFundsTransferTransactions_Settlement " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				request_log = new REQUEST_LOG();

				request_log.setTransid(result.getString(1));
				request_log.setCard_num(result.getString(2));
				request_log.setTrans_date(result.getString(3));
				request_log.setTrans_amount(result.getString(4));
				request_log.setTrans_code(result.getString(5));
				request_log.setMerchant_code(result.getString(6));
				request_log.setResponse_code(result.getString(7));
				request_log.setResponse_time(result.getString(8));
				request_log.setTrans_descr(result.getString(9));
				

				arr.add(request_log);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	} 
    
    
    /*ATM/POS Settlemt Transaction*/    
    public ArrayList getATM_POS_Settlement(String pan, String terminal_id, String start_dt, String end_dt, String channel_id)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION etransaction;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;

		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			int ch = Integer.parseInt(channel_id);
			switch(ch)
			{
				case 3://POS
					
					query = "select trans_date, batch_date, settle_batch, trans_descr, trans_amount from e_transaction" +
					" a left outer join e_settle_batch b on a.settle_batch = b.batch_id" +
					" where trans_date between('"+start_dt+"') and ('"+end_dt+"') and trans_code = 'P'" +
					" and channelid = '03' and substring(serviceid,7, 15) like '"+terminal_id+"%'" +
					" and card_num = '"+pan+"'";
					
					break;
					
				case 4://ATM
					
					query = "select trans_date, batch_date, settle_batch, trans_descr, trans_amount from e_transaction" +
					" a left outer join e_settle_batch b on a.settle_batch = b.batch_id" +
					" where trans_date between('"+start_dt+"') and ('"+end_dt+"') and trans_code = 'W'" +
					" and channelid = '04' and substring(serviceid,7, 15) like '"+terminal_id+"%'" +
					" and card_num = '"+pan+"'";
					
					break;
				
				default:
					break;
			
			}

			System.out.println("getATM_POS_Settlement " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				etransaction = new E_TRANSACTION();

				etransaction.setTrans_date(result.getString(1));
				etransaction.setBatch_date(result.getString(2));
				etransaction.setSettle_batch(result.getString(3));
				etransaction.setTrans_desc(result.getString(4));
				etransaction.setTrans_amount(result.getString(5));

				arr.add(etransaction);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
    
    
    /*Chamms Settlement Transaction*/    
    public ArrayList getChammsSettlement(String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION etransaction;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;

		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select 'Agent Cash In' Nature, count(*) Trans_Count,sum(trans_amount) Credit,0 Debit from e_transaction where trans_date between '2011-05-01' and '2011-11-27' and trans_code = 'D' and channelid = '02' and merchant_code like '035834%' and card_num not like '035834%' " +
					" UNION ALL " +
					" select 'Other Cash In (Transfers)', count(*),sum(trans_amount),0 from e_transaction where trans_date between('"+start_dt+"') and ('"+end_dt+"') and ((trans_code = 'D' and channelid not in ('02','05')) OR (trans_code = 'T') ) and merchant_code like '035834%'  and card_num not like '035834%' " +
					" UNION ALL " +
					" select 'Bank Card Loads', count(*),sum(trans_amount),0 from e_transaction where trans_date between('"+start_dt+"') and ('"+end_dt+"') and trans_code = 'D' and channelid = '05' and merchant_code like '035834%'  and card_num not like '035834%' " +
					" UNION ALL " +
					" select 'Agent Cash Out', count(*),0,sum(trans_amount) from e_transaction where trans_date between('"+start_dt+"') and ('"+end_dt+"') and trans_code = 'U' and channelid = '02' and card_num like '035834%'  and merchant_code not like '035834%' "+
					" UNION ALL "+
					" select 'Bank Card Unloads', count(*),0,sum(trans_amount) from e_transaction where trans_date between('"+start_dt+"') and ('"+end_dt+"') and trans_code = 'U' and channelid = '05' and card_num like '035834%'  and merchant_code not like '035834%' "+
					" UNION ALL "+
					" select 'Third Party Payments', count(*),0,sum(trans_amount) from e_transaction where trans_date between('"+start_dt+"') and ('"+end_dt+"') and trans_code = 'P' and channelid = '02' and card_num like '035834%'  and merchant_code not like '035834%' "+
					" UNION ALL "+
					" select 'Third Party Transfers', count(*),0,sum(trans_amount) from e_transaction where trans_date between('"+start_dt+"') and ('"+end_dt+"') and ((trans_code = 'P' and channelid <> '02') OR (trans_code = 'T')) and card_num like '035834%'  and merchant_code not like '035834%'";
		
			System.out.println("getChammsSettlement " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				etransaction = new E_TRANSACTION();

				etransaction.setTrans_desc(result.getString(1));
				etransaction.setTransaction_count(result.getString(2));
				etransaction.setCreditAmt(result.getString(3));
				etransaction.setDebitAmt(result.getString(4));

				arr.add(etransaction);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
    
    
    /*This method is used to display pos merchants that we debit everyday*/    
    public ArrayList getPOSMerchantReport(String etz_merchant_code, String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION e_transaction;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		//select merchant_code , sum(trans_amount) as 'Fee', (select sum(trans_amount) from ecarddb..e_transaction where card_num = '0330011011200757' and merchant_code = merchant_code) as 'Total So Far',(select merchant_name from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code)  from ecarddb..e_transaction where trans_date between('2010-07-01 00:00') and ('2011-03-30 01:05') and card_num = '0330011011200757' group by merchant_code
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select merchant_code ,(select merchant_name from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code)," +
					" sum(fee) from e_transaction" +
					" where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
					" and merchant_code = '"+etz_merchant_code+"' group by merchant_code";
			
		
			System.out.println("getPOSMerchantReport " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				e_transaction = new E_TRANSACTION();

				e_transaction.setMerchat_code(result.getString(1));
				e_transaction.setMerchant_descr(result.getString(2));
				e_transaction.setFee(result.getString(3));
				
				arr.add(e_transaction);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	} 
    
    
    
    /*Method to get recharge pins*/
	public ArrayList getRechargePinLog(String merchant_code, String terminal_id, String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		R_pins_bought pins_bought = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		
		try
		{
			con = connectToRechargeDB();
			stat = con.createStatement();
			
			String unique_trans_id = merchant_code + terminal_id;
			
			query = "select PROVIDER_ID, PIN, PIN_VALUE, serial, batchid, pin_issued from rechargedb..r_pins_bought where pin_issued between ('"+start_dt+"') and ('"+end_dt+"')" +
					" and unique_transid like '"+unique_trans_id+"%' and reverse is null";
		
			System.out.println("getRechargePinLog query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				
				pins_bought = new R_pins_bought();
				pins_bought.setPROVIDER_ID(""+result.getObject(1));
				pins_bought.setPIN(""+result.getObject(2));
				pins_bought.setPIN_VALUE(""+result.getObject(3));
				pins_bought.setSERIAL(""+result.getObject(4));
				pins_bought.setBATCHID(""+result.getObject(5));
				pins_bought.setPIN_ISSUED(""+result.getObject(6));
				
				arr.add(pins_bought);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionRechargeDB(con, result);
		}
		finally
		{
			closeConnectionRechargeDB(con, result);
		}
		return arr;
	}
	
	
	/*Method to get pending cards deletion*/
	public ArrayList getPendingCardDeletionLog(String bank_code, String userType)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_MOBILE_SUBSCRIBER_CARD mobile_subscriber_card;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		
		try
		{
			
				
			if(userType.equals("1") || userType.equals("2"))
			{
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select card_num, alias, expiration, modified from e_mobile_subscriber_card where auth_status = '1' ";
				
				System.out.println("getPendingCardDeletionLog version I query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					mobile_subscriber_card = new E_MOBILE_SUBSCRIBER_CARD();

					mobile_subscriber_card.setCard_num(""+result.getObject(1));
					mobile_subscriber_card.setAlias(""+result.getObject(2));
					mobile_subscriber_card.setExpiration(""+result.getObject(3));
					mobile_subscriber_card.setModified(""+result.getObject(4));
					
					arr.add(mobile_subscriber_card);
					
				}
				
				closeConnectionECard(con, result, result);
				
				con = connectMobileDB();
				stat = con.createStatement();
				
				
				query = "select card_number, alias, expiration, modified from m_mobile_subscriber_card where auth_status = '1' ";
				System.out.println("getPendingCardDeletionLog version II query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					mobile_subscriber_card = new E_MOBILE_SUBSCRIBER_CARD();

					mobile_subscriber_card.setCard_num(""+result.getObject(1));
					mobile_subscriber_card.setAlias(""+result.getObject(2));
					mobile_subscriber_card.setExpiration(""+result.getObject(3));
					mobile_subscriber_card.setModified(""+result.getObject(4));
					
					arr.add(mobile_subscriber_card);
					
				}
				
			}
			else
			{
				
				con = connectToECard();
				stat = con.createStatement();
				
				query = "select card_num, alias, expiration, modified from e_mobile_subscriber_card" +
						" where auth_status = '1' and substring(card_num, 1,3) like '"+bank_code+"%' ";
				System.out.println("getPendingCardDeletionLog version I query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					mobile_subscriber_card = new E_MOBILE_SUBSCRIBER_CARD();

					mobile_subscriber_card.setCard_num(""+result.getObject(1));
					mobile_subscriber_card.setAlias(""+result.getObject(2));
					mobile_subscriber_card.setExpiration(""+result.getObject(3));
					mobile_subscriber_card.setModified(""+result.getObject(4));
					
					arr.add(mobile_subscriber_card);
					
				}
				
				closeConnectionECard(con, result, result);
				
				con = connectMobileDB();
				stat = con.createStatement();
				
				query = "select card_number, alias, expiration, modified" +
				"  from m_mobile_subscriber_card where auth_status = '1' and substring(card_number, 1,3) like '"+bank_code+"%' ";
				
				System.out.println("getPendingCardDeletionLog version II query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					mobile_subscriber_card = new E_MOBILE_SUBSCRIBER_CARD();

					mobile_subscriber_card.setCard_num(""+result.getObject(1));
					mobile_subscriber_card.setAlias(""+result.getObject(2));
					mobile_subscriber_card.setExpiration(""+result.getObject(3));
					mobile_subscriber_card.setModified(""+result.getObject(4));
					
					arr.add(mobile_subscriber_card);
					
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
			closeConnectionMobileDB(con, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeConnectionMobileDB(con, result);
		}
		return arr;
	}
	
	
	/*Method to get pocketmoni number of users*/
	public ArrayList getPocketMoniTotalNumberOfUsers()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		String[] str = new String[4];
		int counter = 0;
		Connection con = null, con1 = null;
		Statement stat = null, stat1 = null;
		ResultSet result = null, result1 = null;
		
		
		try
		{
			con = connectMobileDB();
			stat = con.createStatement();
			
			con1 = connectPocketMoniEcardDB();
			stat1 = con1.createStatement();
			
			/*Calendar cal = Calendar.getInstance();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String str_month = "";
			String str_dy = "";

			int month = cal.get(Calendar.MONTH)+1;
			int year = cal.get(Calendar.YEAR);
			
			str_month = ""+month;
			str_dy = "01";
			
			if(month < 10)
			{
				str_month = "0" + month;
			}

			String start_dt = ""+year + "-" + ""+ str_month + "-" + str_dy + " 00:00";
			System.out.println("start " + start_dt);*/
			
			query = "select count(*) from m_mobile_subscriber where appid = 2 ";//appid 2 is for pocketMoni
			System.out.println("getPocketMoniTotalNumberOfUsers query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				str[0] = ""+result.getObject(1);				
			}
			
			//connect to get the total active users, the active users are those that have an online balance of 150
			query = "select count(*) from e_cardholder where" +
					" online_balance >= 150 and (card_num like '700001%' or card_num like '700992%') ";
			result1 = stat1.executeQuery(query);
			while(result1.next())
			{
				str[1] = ""+result1.getObject(1);				
				
			}

			str[2] = ""+ (Integer.parseInt(str[0])  - Integer.parseInt(str[1]));
			str[3] = ""+ (Double.parseDouble(str[1]) / Double.parseDouble(str[0])) * 100;
			
			/*query = "select count(distinct card_num) as card_count from e_transaction where  trans_date between '"+start_dt+"' and getDate() and unique_transid like '02%' and card_num like '700%'  group by  substring(card_num,1,3)";
			System.out.println("getPocketMoniActiveUsers query " + query);
			result1 = stat1.executeQuery(query);
			while(result1.next())
			{
				str[1] = ""+result1.getObject(1);				
				
			}
			
			str[2] = ""+ (Integer.parseInt(str[0])  - Integer.parseInt(str[1]));*/
			arr.add(str);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionMobileDB(con, result);
			closeConnectionPocketMoniEcardDB(con1, result1);
		}
		finally
		{
			closeConnectionMobileDB(con, result);
			closeConnectionPocketMoniEcardDB(con1, result1);
		}
		return arr;
	}
	/*
	 * Method to get Abia Lotto Transactioin
	 */
	public ArrayList getAbiaLottoTransactions(String lotto_no, String phone_no, String terminal_id, String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		LOTTO_LOG lotto = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		double d = 0.0;
		String str = "";
	
		
		try
		{
			con = connectToETMC();
			stat = con.createStatement();
		  
			
			query = "select created, trans_amount, phone_number, choice_number, lucky_number," +
					" winning_combinations, terminal_id, merchant_id" +
					" from ecarddb..lotto_log" +
					" where choice_number like '"+ lotto_no + "%' and phone_number like '"+ phone_no + "%' and" +
					" terminal_id like '"+terminal_id+"%' and" +
					" terminal_id in ('08405972','08405970','08405973','08405969','08405968','08405975'," +
					" '08405974','08405983','08405984','08405971','08405994','08405993','08405991','08405990'," +
					" '08405992','08405986','08405987','08405988','08405985','08405989','08405995','08405996'," +
					" '08405997','08405998','08405999','08406000','08406004','08406002','08406001','08406003'," +
					" '08406005','08406006','08406007','08406008','08406009','08406010','08406011','08406012'," +
					" '08406013','08406014') and " +
					" created between('" +start_dt+"') and ('"+end_dt+"') order by created desc";
			
			
			System.out.println("getMerchantTransaction  query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				lotto = new LOTTO_LOG();
				lotto.setCounter(""+counter);
				lotto.setCreated(""+result.getObject(1));
				lotto.setTrans_amount(""+result.getObject(2));
				lotto.setPhone_number(""+result.getObject(3));
				lotto.setChoice_number(""+result.getObject(4));
				lotto.setLucky_number(""+result.getObject(5));
				lotto.setWinning_combinations(""+result.getObject(6));
				lotto.setTerminal_id(""+result.getObject(7));
				lotto.setMerchant_id(""+result.getObject(8));
				//lotto.setId(""+result.getObject(9));
				arr.add(lotto);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionETMC(con, result);
		}
		finally
		{
			closeConnectionETMC(con, result);
		}
		return arr;
	}
	
	/*
	 * 
	 * This method get Vas Transaction
	 */
	public ArrayList getVasTransactionAtmDB(String mtinumber,String beginDate,String endDate){
	{
		String query = "";
		ArrayList arr = new ArrayList();
		CardHolder cholder = null;
		E_TMCREQUEST erquest = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectPocketMoniEcardDB();
			stat = con.createStatement();
		
				query = "select pan,terminal_id,CARD_ACC_NAME,"+
						"(select aquirer_name from E_TMCAQUIRER b where a.aqid = b.aqid) as aquirer ," +
						"amount,biller_id,bill_id,response_code,advise_response " +
						"from e_tmcrequest a where mti = '"+mtinumber+"' and trans_date between('"+beginDate+"') and ('"+endDate+"')";
			
			System.out.println("query    " + query);
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				erquest  = new E_TMCREQUEST();
				erquest.setCounter(""+counter);
				erquest.setPan(""+result.getObject(1));
				erquest.setTerminal_id(""+result.getObject(2));
				erquest.setCard_acc_name(""+result.getObject(3));
				erquest.setAqid(""+result.getObject(4));
				erquest.setAmount(""+result.getObject(5));
				erquest.setBiller_id(""+result.getObject(6));
				erquest.setBill_id(""+result.getObject(7));
				erquest.setResponse_code(""+result.getObject(8));
				erquest.setAdvise_response(""+result.getObject(9));
				//erquest.setMti(""+result.getObject(10));
				//erquest.setTrans_date(""+result.getObject(10));
				
				arr.add(erquest);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPocketMoniEcardDB(con, result);
		}
		finally
		{
			closeConnectionPocketMoniEcardDB(con, result);
		}
		return arr;
		}
	}
	
	
	/*Method to get the pocketmoni summary*/
	public ArrayList getPocketMoniSummaryByDateAndType(String start_dt, String end_dt, String type, String mobile_no)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION e_tran = null;
		int counter = 0;
		String t_code = "";
		String m_code = "";
		String lname = "";
		String fname = "";
		String state = "";
		String phone = "";
		String str_cardnum = "";
		String str_value = "";
		
		HashNumber hn = new HashNumber();
		Connection con = null, con1 = null, con2 = null;
		Statement stat = null, stat1 = null, stat2 = null;
		ResultSet result = null, result1 = null;
		
		String apostrophe = "'";
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			con1 = connectMobileDB();
			stat1 = con1.createStatement();
			
			con2 = connectPocketMoniEcardDB();
			stat2 = con2.createStatement();
			
			if(mobile_no.length()>0)
			{
				query  = "select id from m_mobile_subscriber where mobile_no = '"+mobile_no+"' ";
				result = stat1.executeQuery(query);
				if(result.next())
				{
					query  = "select card_num from m_mobile_subscriber_card where subscriber_id = "+Integer.parseInt(""+result.getObject(1))+" ";
					result = stat1.executeQuery(query);
					while(result1.next())
					{						
						str_cardnum +=  apostrophe + ""+result.getObject(1) + apostrophe + ",";
					}
				}
			}
			
			
			if(str_cardnum.trim().length()>0)
			{
				str_cardnum = str_cardnum.substring(0, str_cardnum.lastIndexOf(","));
			}
			

			if(type.equalsIgnoreCase("P"))//airtime
			{
				
				if(str_cardnum.trim().length()>0)
				{
					query = "select count(*), sum(trans_amount), card_num, merchant_code, trans_code from" +
					" e_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' " +
					" and substring(card_num,1,3) in ('700') " +
					" and trans_code = '"+type+"' and card_num in ('"+str_cardnum+"') group by card_num, trans_code ,merchant_code";
				}
				else
				{
					query = "select count(*), sum(trans_amount), card_num, merchant_code, trans_code from" +
					" e_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' " +
					" and substring(card_num,1,3) in ('700') " +
					" and trans_code = '"+type+"' group by card_num, trans_code ,merchant_code";
				}
				
				
			}
			if(type.equalsIgnoreCase("BL"))//bill payment
			{
				
			}
			else if(type.equalsIgnoreCase("D"))//cash In
			{
				/*query = "select count(*), sum(trans_amount), card_num, (select mobile from ecarddb..e_mobile_subscriber_card where ecarddb..e_mobile_subscriber_card.card_num = ecarddb..e_transaction.card_num),merchant_code,trans_code from ecarddb..e_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' " +
				"and substring(merchant_code,1,6) in ('070991','070992','070993','070994','070995','070996','070997','070998','070999','084896','084894') " +
				"and trans_code = '"+type+"' and card_num like '" + card_num +"%' group by card_num, trans_code ,merchant_code";*/
			}
			else if(type.equalsIgnoreCase("U"))//cash withdrawal
			{
				/*query = "select count(*), sum(trans_amount), card_num, (select mobile from ecarddb..e_mobile_subscriber_card where ecarddb..e_mobile_subscriber_card.card_num = ecarddb..e_transaction.card_num),merchant_code,trans_code from ecarddb..e_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' " +
				"and substring(card_num,1,6) in ('070991','070992','070993','070994','070995','070996','070997','070998','070999','084896','084894') " +
				"and trans_code = '"+type+"' and card_num like '" + card_num +"%' group by card_num, trans_code ,merchant_code";*/
			}
			else if(type.equalsIgnoreCase("TI"))//fund transfer to pocket moni account
			{
				/*query = "select count(*), sum(trans_amount), card_num, (select mobile from ecarddb..e_mobile_subscriber_card where ecarddb..e_mobile_subscriber_card.card_num = ecarddb..e_transaction.card_num),merchant_code,trans_code from ecarddb..e_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' " +
				"and substring(merchant_code,1,6) in ('070991','070992','070993','070994','070995','070996','070997','070998','070999','084896','084894') " +
				"and trans_code = 'T' and card_num like '" + card_num +"%' group by card_num, trans_code ,merchant_code";*/
			}
			else if(type.equalsIgnoreCase("TI"))//fund transfer to bank account
			{
				/*query = "select count(*), sum(trans_amount), card_num, (select mobile from ecarddb..e_mobile_subscriber_card where ecarddb..e_mobile_subscriber_card.card_num = ecarddb..e_transaction.card_num),merchant_code,trans_code from ecarddb..e_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' " +
				"and substring(merchant_code,1,6) in ('070991','070992','070993','070994','070995','070996','070997','070998','070999','084896','084894') " +
				"and trans_code = 'T' and card_num like '" + card_num +"%' group by card_num, trans_code ,merchant_code";*/
			}
			else if(type.equalsIgnoreCase("TI"))//fund transfer to unregistered user
			{
				
			}
			else if(type.equalsIgnoreCase("TI"))//pocket moni card load at the bank
			{
				
			}
			else if(type.equalsIgnoreCase("TI"))//ATM cash out
			{
				
			}
			System.out.println("getPocketMoniSummaryByDateAndType " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{			
				e_tran = new E_TRANSACTION();
				
				e_tran.setTransaction_count(""+result.getObject(1));
				e_tran.setTrans_amount(""+result.getObject(2));
				str_value = ""+result.getObject(3);
				m_code = ""+result.getObject(4);
				t_code = ""+result.getObject(5);
				
				
				if(t_code.equals("P"))
				{
					e_tran.setTrans_code("Airtime");
					e_tran.setCard_num(str_value);
				}
				
				//getting the personal details
				query = "select lastname, firstname, street, state  from e_cardholder where card_num = '"+str_value+"'";
				result1 = stat2.executeQuery(query);
				while(result1.next())
				{
					e_tran.setLastname(""+result1.getObject(1));
					e_tran.setFirstname(""+result1.getObject(2));
					e_tran.setStreet(""+result1.getObject(3));
					e_tran.setState(""+result1.getObject(4));
				}
				
				/*if(t_code.equals("D"))
				{
					e_tran.setTrans_code("Cash In");
					e_tran.setCard_num(m_code);
					str_value = m_code;
				}
			
				if(t_code.equals("U"))
				{
					e_tran.setTrans_code("Cash Out");
					e_tran.setCard_num(str_cardnum);
					str_value = str_cardnum;
				}
				
				if(t_code.equals("T") && type.equals("TI"))
				{
					e_tran.setTrans_code("Funds Transfer - In");
					e_tran.setCard_num(m_code);
					str_value = m_code;
				}
				
				if(t_code.equals("T") && type.equals("T"))
				{
					e_tran.setTrans_code("Funds Transfer - Out");
					e_tran.setCard_num(str_cardnum);
					str_value = str_cardnum;
				}
				
				if(t_code.equals("T") && type.equals("ALL"))
				{
					e_tran.setTrans_code("Funds Transfer");
					e_tran.setCard_num(str_cardnum);
					str_value = str_cardnum;
				}
				
				
				
				*/
				
				arr.add(e_tran);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
			closeConnectionMobileDB(con1, result);
			closeConnectionPocketMoniEcardDB(con2, result1);
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeConnectionMobileDB(con1, result);
			closeConnectionPocketMoniEcardDB(con2, result1);
		}
		return arr;
	}
	
	
	/*This method is used to get various details from t_paytrans for phcn*/
	public ArrayList getPHCNReport(String start_dt, String end_dt, String type, String district, String merchant_code)
	{
		ArrayList arr = new ArrayList();
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		C_TRANSACTION c_tran;
		PAYTRANS tpay = null;
		String query = "";
		try
		{
			
			con = connectToTelco();
			stat = con.createStatement();
			
			if(type.equals("A"))
			{
				
				if(district.length() > 0)
				{
					query = "select trans_date,UNIQUE_TRANSID,subscriber_id, card_fullname, T_ADDRESS,TRANS_AMOUNT, issuer_code, " +
					" card_subname, sub_code, trans_note, status_description from" +
					" telcodb.dbo.T_PAYTRANS" +
					" where trans_date between('"+start_dt+"') and ('"+end_dt+"') and" +
					" merchant_code in ('"+merchant_code+"') and subscriber_id like '"+district+"%'  order by trans_date desc";
				}
				else
				{
					query = "select trans_date,UNIQUE_TRANSID,subscriber_id, card_fullname, T_ADDRESS,TRANS_AMOUNT, issuer_code, " +
					" card_subname, sub_code, trans_note, status_description from" +
					" telcodb.dbo.T_PAYTRANS" +
					" where trans_date between('"+start_dt+"') and ('"+end_dt+"') and" +
					" merchant_code in ('"+merchant_code+"')  order by trans_date desc";
				}
				
				
		
				System.out.println("getPHCNReport query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					tpay = new PAYTRANS();
					tpay.setTrans_date(""+result.getObject(1));
					tpay.setUnique_trans_id(""+result.getObject(2));
					tpay.setSubscriber_id(""+result.getObject(3));
					tpay.setCard_fullname(""+result.getObject(4));
					tpay.setT_address(""+result.getObject(5));
					tpay.setTrans_amount(""+result.getObject(6));
					tpay.setIssuer_code(""+result.getObject(7));
					tpay.setCard_subname(""+result.getObject(8));
					tpay.setSub_code(""+result.getObject(9));
					tpay.setTrans_note(""+result.getObject(10));
					tpay.setStatus_desc(""+result.getObject(11));
					arr.add(tpay);
				}
			}
			else
			{
				
			}
			
			

			
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
		}
		return arr;
	}
	
	
	/*Method to get the districts*/
	public ArrayList getDistricts()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		District district;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			query = "select * from phcn_district";	
			System.out.println("getDistricts " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				district = new District();

				district.setId(""+result.getObject(1));
				district.setCode(""+result.getObject(2));
				district.setDesc(""+result.getObject(3));
				district.setDescription(""+result.getObject(4));
				arr.add(district);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return arr;
	}
	/*public ArrayList getPHCNAdminReport(String start_dt, String end_dt, String type, String district,
			String merchant_code,String tarrif, String issue_code, String channelid,String meterno)
	{
		ArrayList arr = new ArrayList();
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		C_TRANSACTION c_tran;
		PAYTRANS tpay = null;
		String query = "";
		try
		{
			
			con = connectToTelco();
			stat = con.createStatement();
			
			System.out.println("type "+type);
			System.out.println("tarrif "+tarrif);
			
			if(type.equals("ANY") || type.equals("REAL-TIME"))
			{
				
				if(tarrif.equals("PHCN: TARRIFF"))
				{
					query = "select trans_date,UNIQUE_TRANSID,subscriber_id, card_fullname, T_ADDRESS,TRANS_AMOUNT, issuer_code, " +
					" card_subname, sub_code, trans_note, status_description, trans_channel from" +
					" telcodb.dbo.T_PAYTRANS" +
					" where trans_date between('"+start_dt+"') and ('"+end_dt+"') and" +
					" substring(subscriber_id,1,4) like '"+district+"%' and" +
					" issuer_code like '"+issue_code+"%' and merchant_code = '"+merchant_code+"' " +
					" and subscriber_id like '"+meterno+"%' and trans_channel like '"+channelid+"%'" +
					" and status_description in ('0|0|0|0') order by trans_date desc";
				}
				else if(tarrif.equals("PHCN: PREPAID"))
				{
					query = "select trans_date,UNIQUE_TRANSID,subscriber_id, card_fullname, T_ADDRESS,TRANS_AMOUNT, issuer_code, " +
					" card_subname, sub_code, trans_note, status_description, trans_channel from" +
					" telcodb.dbo.T_PAYTRANS" +
					" where trans_date between('"+start_dt+"') and ('"+end_dt+"') and" +
					" substring(subscriber_id,1,4) like '"+district+"%' and" +
					" issuer_code like '"+issue_code+"%' and merchant_code = '"+merchant_code+"'" +
					" and subscriber_id like '"+meterno+"%' and trans_channel like '"+channelid+"%'" +
					" and status_description not in('0|0|0|0') order by trans_date desc";
				}
				else if(meterno.trim().length() > 0)
				{
					query = "select trans_date,UNIQUE_TRANSID,subscriber_id, card_fullname, T_ADDRESS,TRANS_AMOUNT, issuer_code, " +
					" card_subname, sub_code, trans_note, status_description, trans_channel from" +
					" telcodb.dbo.T_PAYTRANS" +
					" where trans_date between('"+start_dt+"') and ('"+end_dt+"') and" +
					" subscriber_id like '"+meterno+"%' and" +
					" issuer_code like '"+issue_code+"%' and merchant_code = '"+merchant_code+"'" +
					" and trans_channel like '"+channelid+"%' order by trans_date desc";				
				}
				else
				{
					query = "select trans_date,UNIQUE_TRANSID,subscriber_id, card_fullname, T_ADDRESS,TRANS_AMOUNT, issuer_code, " +
					" card_subname, sub_code, trans_note, status_description, trans_channel from" +
					" telcodb.dbo.T_PAYTRANS" +
					" where trans_date between('"+start_dt+"') and ('"+end_dt+"') and" +
					" substring(subscriber_id,1,4) like '"+district+"%' and" +
					" issuer_code like '"+issue_code+"%' and merchant_code = '"+merchant_code+"'" +
					" and subscriber_id like '"+meterno+"%' and trans_channel like '"+channelid+"%' order by trans_date desc";
				}
				System.out.println("getPHCNReport query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					tpay = new PAYTRANS();
					tpay.setTrans_date(""+result.getObject(1));
					tpay.setUnique_trans_id(""+result.getObject(2));
					tpay.setSubscriber_id(""+result.getObject(3));
					tpay.setCard_fullname(""+result.getObject(4));
					tpay.setT_address(""+result.getObject(5));
					tpay.setTrans_amount(""+result.getObject(6));
					tpay.setIssuer_code(""+result.getObject(7));
					tpay.setCard_subname(""+result.getObject(8));
					tpay.setSub_code(""+result.getObject(9));
					tpay.setTrans_note(""+result.getObject(10));
					tpay.setStatus_desc(""+result.getObject(11));
					tpay.setTrans_channel(""+result.getObject(12));
					arr.add(tpay);
				}
			}
		
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
		}
		return arr;
	}*/
	
	public ArrayList getPHCNAdminReport(String start_dt, String end_dt, String type, String district,
			String merchantcode,String tarrif, String issue_code,String subcode, String channelid,String meterno)
	{
		ArrayList arr = new ArrayList();
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		C_TRANSACTION c_tran;
		PAYTRANS tpay = null;
		String query = "";
		
		String apostrophe = "'";
		
		try
		{
			
			

			if(merchantcode.indexOf(":")>0)
			{
				String m[] = merchantcode.split(":");
				merchantcode = "";
				for(int i=0;i<m.length;i++)
				{
					merchantcode += apostrophe + m[i] + apostrophe + ",";
				}
				
				merchantcode = merchantcode.substring(0, merchantcode.lastIndexOf(","));
			}
			else
			{
				merchantcode = apostrophe + merchantcode + apostrophe;
			}
			
			
			
			con = connectToTelco();
			stat = con.createStatement();
			
			System.out.println("type "+type);
			System.out.println("tarrif "+tarrif);
			System.out.println("merchantcode "+merchantcode);
			
			if(type.equals("ANY") || type.equals("REAL-TIME"))
			{
				
				if(tarrif.equals("PHCN: TARRIFF"))
				{
					query = "select trans_date,UNIQUE_TRANSID,subscriber_id, card_fullname, T_ADDRESS,TRANS_AMOUNT, issuer_code, " +
					" card_subname, sub_code, trans_note, status_description, trans_channel from" +
					" telcodb.dbo.T_PAYTRANS" +
					" where trans_date between('"+start_dt+"') and ('"+end_dt+"') and" +
					" substring(subscriber_id,1,6) like '"+district+"%' and subscriber_id like '"+meterno+"%' and " +
					" issuer_code like '"+issue_code+"%' and sub_code like '"+subcode+"%' and merchant_code in("+merchantcode+") " +
					" and trans_channel like '"+channelid+"%'  and status_description in ('0|0|0|0') order by trans_date desc";
				}
				else if(tarrif.equals("PHCN: PREPAID"))
				{
					query = "select trans_date,UNIQUE_TRANSID,subscriber_id, card_fullname, T_ADDRESS,TRANS_AMOUNT, issuer_code, " +
					" card_subname, sub_code, trans_note, status_description, trans_channel from" +
					" telcodb.dbo.T_PAYTRANS" +
					" where trans_date between('"+start_dt+"') and ('"+end_dt+"') and" +
					" subscriber_id like '"+meterno+"%' and " +
					" issuer_code like '"+issue_code+"%' and sub_code like '"+subcode+"%' and merchant_code in("+merchantcode+") " +
					" and trans_channel like '"+channelid+"%' and (status_description not in('0|0|0|0') or status_description = null )" +
					" order by trans_date desc";
				}
				else
				{
					query = "select trans_date,UNIQUE_TRANSID,subscriber_id, card_fullname, T_ADDRESS,TRANS_AMOUNT, issuer_code, " +
					" card_subname, sub_code, trans_note, status_description, trans_channel from" +
					" telcodb.dbo.T_PAYTRANS" +
					" where trans_date between('"+start_dt+"') and ('"+end_dt+"') and" +
					" subscriber_id like '"+meterno+"%' and " +
					" issuer_code like '"+issue_code+"%' and sub_code like '"+subcode+"%' and merchant_code in("+merchantcode+") " +
					" and trans_channel like '"+channelid+"%' order by trans_date desc";
				}
				System.out.println("getPHCNReport query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					tpay = new PAYTRANS();
					tpay.setTrans_date(""+result.getObject(1));
					tpay.setUnique_trans_id(""+result.getObject(2));
					tpay.setSubscriber_id(""+result.getObject(3));
					tpay.setCard_fullname(""+result.getObject(4));
					tpay.setT_address(""+result.getObject(5));
					tpay.setTrans_amount(""+result.getObject(6));
					tpay.setIssuer_code(""+result.getObject(7));
					tpay.setCard_subname(""+result.getObject(8));
					tpay.setSub_code(""+result.getObject(9));
					tpay.setTrans_note(""+result.getObject(10));
					tpay.setStatus_desc(""+result.getObject(11));
					tpay.setTrans_channel(""+result.getObject(12));
					arr.add(tpay);
				}
			}
		
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
		}
		return arr;
	}
	
	public ArrayList getPHCNPaymentByDistrictRport(String start_dt, String end_dt, String type,
			String merchant_code, String issue_code, String channelid)
	{
		ArrayList arr = new ArrayList();
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		C_TRANSACTION c_tran;
		PAYTRANS tpay = null;
		String query = "";
		try
		{
			
			con = connectToTelco();
			stat = con.createStatement();
			
			System.out.println("type "+type);
			//System.out.println("tarrif "+tarrif);

			query = "select substring(subscriber_id,1,4), count(trans_amount), sum(trans_amount), issuer_code from" +
					" telcodb.dbo.T_PAYTRANS" +
					" where trans_date between('"+start_dt+"') and ('"+end_dt+"') and" +
					" issuer_code like '"+issue_code+"%' and merchant_code = '"+merchant_code+"' " +
					" and trans_channel like '"+channelid+"%' group by substring(subscriber_id,1,4), issuer_code";
			
			
			System.out.println("getPHCNPaymentByDistrictRport query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				tpay = new PAYTRANS();
				
				tpay.setSubscriber_id(""+result.getObject(1));
				tpay.setTransaction_count(""+result.getObject(2));
				tpay.setTrans_amount(""+result.getObject(3));
				tpay.setIssuer_code(""+result.getObject(4));
				arr.add(tpay);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
		}
		return arr;
	}
    
	public ArrayList getPHCNPaymentByBankSummaryReport(String start_dt, String end_dt, String type,
			String merchant_code, String issue_code, String channelid)
	{
		ArrayList arr = new ArrayList();
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		C_TRANSACTION c_tran;
		PAYTRANS tpay = null;
		String query = "";
		try
		{
			
			con = connectToTelco();
			stat = con.createStatement();
			
			System.out.println("type "+type);
			
			query = "select count(trans_amount), sum(trans_amount), issuer_code from" +
					" telcodb.dbo.T_PAYTRANS" +
					" where trans_date between('"+start_dt+"') and ('"+end_dt+"') and" +
					" issuer_code like '"+issue_code+"%' and merchant_code = '"+merchant_code+"' " +
					" and trans_channel like '"+channelid+"%' group by issuer_code";
		
			System.out.println("getPHCNPaymentByBankRport query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				tpay = new PAYTRANS();
				tpay.setTransaction_count(""+result.getObject(1));
				tpay.setTrans_amount(""+result.getObject(2));
				tpay.setIssuer_code(""+result.getObject(3));
				arr.add(tpay);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
		}
		return arr;
	}
	public ArrayList getPHCNBranchBankSummaryReport(String start_dt, String end_dt, String type,
			String merchant_code, String issue_code, String channelid)
	{
		ArrayList arr = new ArrayList();
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		C_TRANSACTION c_tran;
		PAYTRANS tpay = null;
		String query = "";
		try
		{
			
			con = connectToTelco();
			stat = con.createStatement();
			
			System.out.println("type "+type);
			
			query = "select sub_code, sum(trans_amount), issuer_code from" +
					" telcodb.dbo.T_PAYTRANS" +
					" where trans_date between('"+start_dt+"') and ('"+end_dt+"') and" +
					" issuer_code like '"+issue_code+"%' and merchant_code = '"+merchant_code+"' " +
					" and trans_channel like '"+channelid+"%' group by sub_code,issuer_code";
		
			System.out.println("getPHCNBranchBankSummaryRepor query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				tpay = new PAYTRANS();
				tpay.setSub_code(""+result.getObject(1));
				tpay.setTrans_amount(""+result.getObject(2));
				tpay.setIssuer_code(""+result.getObject(3));
				arr.add(tpay);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
		}
		return arr;
	}
	
	
	/*Method to get the distinct merchant codes for the disco*/
	public String getDistinctPostpaidPHCNMerchantCode(String disco, String col)
	{
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		String query = "";
		
		String apostrophe = "'";
		
		try
		{

			con = connectToTelco();
			stat = con.createStatement();

			query = "select distinct " + col + " from t_phcn_districts_postpaid where disco = '"+disco+"' ";
				
			System.out.println("getDistinctPHCNMerchantCode query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				message += ""+result.getObject(1) + ":";
			}	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTelco(con, result);
		}
		finally
		{
			closeConnectionTelco(con, result);
		}
		return message;
	}
	
	
	
	/*Method to do the select from the request log by card number*/
	public ArrayList getErequestLogDetailsByCardnum(String cardnum, String start_dt, String end_dt, String status)
	{
		ArrayList arr = new ArrayList();
		REQUEST_LOG request_log;
		HashNumber hn = new HashNumber();
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		String query = "";
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			if(status.equals("ALL"))
			{
				query = "select TRANSID,CARD_NUM,TRANS_DATE,TRANS_AMOUNT,TRANS_CODE," +
				"(select merchant_name from ecarddb.dbo.e_merchant where MERCHANT_CODE = ecarddb.dbo.e_requestlog.MERCHANT_CODE)," +
				"(select error_desc from ecarddb..e_autoswitch_error where error_code = ecarddb.dbo.e_requestlog.response_code),RESPONSE_TIME,TRANS_DESCR,client_id," +
				"REQUEST_ID,FEE,CURRENCY,datediff(ss,response_time,trans_date)" +
				" from ecarddb.dbo.e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"') and card_num like '"+cardnum +"%' order by trans_date desc ";
			}
			else if(status.equals("SUCCESSFUL"))
			{
				query = "select TRANSID,CARD_NUM,TRANS_DATE,TRANS_AMOUNT,TRANS_CODE," +
				"(select merchant_name from ecarddb.dbo.e_merchant where MERCHANT_CODE = ecarddb.dbo.e_requestlog.MERCHANT_CODE)," +
				"(select error_desc from ecarddb..e_autoswitch_error where error_code = ecarddb.dbo.e_requestlog.response_code),RESPONSE_TIME,TRANS_DESCR,client_id," +
				"REQUEST_ID,FEE,CURRENCY,datediff(ss,response_time,trans_date)" +
				" from ecarddb.dbo.e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"') and card_num like '"+cardnum +"%' and response_code in('0') order by trans_date desc ";
			}
			else if(status.equals("FAILED"))
			{
				query = "select TRANSID,CARD_NUM,TRANS_DATE,TRANS_AMOUNT,TRANS_CODE," +
				"(select merchant_name from ecarddb.dbo.e_merchant where MERCHANT_CODE = ecarddb.dbo.e_requestlog.MERCHANT_CODE)," +
				"(select error_desc from ecarddb..e_autoswitch_error where error_code = ecarddb.dbo.e_requestlog.response_code),RESPONSE_TIME,TRANS_DESCR,client_id," +
				"REQUEST_ID,FEE,CURRENCY,datediff(ss,response_time,trans_date)" +
				" from ecarddb.dbo.e_requestlog where trans_date between('"+start_dt+"') and ('"+end_dt+"') and card_num like '"+cardnum +"%' and response_code not in('0') order by trans_date desc ";
			}
			else
			{
				System.out.println("issue dey ");
			}
			
			System.out.println("query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				request_log = new REQUEST_LOG();
				
				request_log.setTransid(""+result.getObject(1));
				request_log.setCard_num(""+result.getObject(2));
				request_log.setTrans_date(""+result.getObject(3));
				request_log.setTrans_amount(""+result.getObject(4));
				request_log.setTrans_code(""+result.getObject(5));
				
				String s = ""+result.getObject(6);
				if(s.equals("")||s.equals("null"))
					s = "";
				request_log.setMerchant_desc(s);
				
				request_log.setResponse_code(""+result.getObject(7));
				request_log.setResponse_time(""+result.getObject(8));
				request_log.setTrans_descr(""+result.getObject(9));
				request_log.setClient_id(""+result.getObject(10));
				request_log.setRequest_id(""+result.getObject(11));
				request_log.setFee(""+result.getObject(12));
				request_log.setCurrency(""+result.getObject(13));
				
				s = ""+result.getObject(14);
				if(s.equals("")||s.equals("null"))
					s = "";
				
				request_log.setResponse_time_in_secs(s);
				
				arr.add(request_log);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
    
	/*This method is used to get the ctransation process to monitor URLMessenger*/
	public ArrayList getCTransactionProcess(String startDt, String endDt)
	{
		ArrayList arr = new ArrayList();
		String[] str = new String[2];		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		String query = "";
		
		try
		{
			con = connectToPayoutlet();
			stat = con.createStatement();

			query = "select count(*), intstatus from payoutletdb..c_transaction" +
					" where trans_date between('"+startDt+"') and ('"+endDt+"') group by intstatus ";
			
			System.out.println("query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				str = new String[2];
				str[0] = ""+result.getObject(1);
				str[1] = ""+result.getObject(2);
				arr.add(str);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPayoutlet(con, result);
		}
		finally
		{
			closeConnectionPayoutlet(con, result);
		}
		return arr;
	}
	
	
    /*This method is used to display pos merchants that we debit everyday*/    
    /*public ArrayList getPOSMerchantReport(String etz_merchant_code, String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION e_transaction;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		//select merchant_code , sum(trans_amount) as 'Fee', (select sum(trans_amount) from ecarddb..e_transaction where card_num = '0330011011200757' and merchant_code = merchant_code) as 'Total So Far',(select merchant_name from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code)  from ecarddb..e_transaction where trans_date between('2010-07-01 00:00') and ('2011-03-30 01:05') and card_num = '0330011011200757' group by merchant_code
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select merchant_code ,(select merchant_name from ecarddb..e_merchant where ecarddb..e_merchant.merchant_code = ecarddb..e_transaction.merchant_code)," +
					" sum(fee) from e_transaction" +
					" where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
					" and merchant_code = '"+etz_merchant_code+"' group by merchant_code";
			
		
			System.out.println("getPOSMerchantReport " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				e_transaction = new E_TRANSACTION();

				e_transaction.setMerchat_code(result.getString(1));
				e_transaction.setMerchant_descr(result.getString(2));
				e_transaction.setFee(result.getString(3));
				
				arr.add(e_transaction);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	} */
            
	
	/*This method is used to get bank branch*/
	public ArrayList getBankBranch(String bankCode)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Bank bank;
		Connection con = null;
		
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select sub_name, sub_code, issuer_code from E_SUBISSUER where issuer_code = '"+bankCode+"' ";
			System.out.println(query + " query");
			result = stat.executeQuery(query);
			while(result.next())
			{
				bank = new Bank();
				bank.setBranchName(""+result.getObject(1));
				bank.setBranchCode(""+result.getObject(2));
				bank.setBank_code(""+result.getObject(3));
				arr.add(bank);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return arr;
	}
	
	
	/*This method is used to get branch name*/
	public String getBranchName(String branchCode)
	{
		String query = "";
		String message = "";
		Connection con = null;
		
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select sub_name from E_SUBISSUER where sub_code = '"+ branchCode +"' ";
			
			System.out.println("getting Bank Branch " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				message = ""+result.getObject(1);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
		}
		return message;
	}
	
    
	/**
	 * This method is used to encrypt or decrypt card numbers
	 * 
	 * */
	public static String cryptPan(String pan, int encType) 
	{
        String cryptedPan = "";
        Cryptographer crypt = new Cryptographer();
        byte[] epinblock = null;
        String mmk = "01010101010101010101010101010101";
        if (encType == 1) 
        {
            String padValue = "FFFFFF" + pan.substring(6);
            try 
            {
                epinblock = crypt.doCryto(padValue, mmk, crypt.ENCRYT);
                cryptedPan = pan.substring(0, 6) + Cryptographer.byte2hex(epinblock);
            } 
            catch (Exception e) 
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } 
        else 
        {
            try 
            {
                epinblock = crypt.doCryto(pan.substring(6), mmk, crypt.DECRYT);
                String decPan = Cryptographer.byte2hex(epinblock).substring(6);
                if (decPan.startsWith("FFFFFF")) 
                {
                    decPan = decPan.substring(6);
                }
                cryptedPan = pan.substring(0, 6) + decPan;
            } 
            catch (Exception e) 
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        //getLogger().log(Level.INFO, "Crypted Parameter : " + cryptedPan);
        return cryptedPan;
    }
	
	
	/**
	 * Connection to the ENV class
	 * @throws Exception
	 */
	private Connection connectToSupportLog() throws Exception
	{
		Connection con = null;
		con = Env.getConnectionSupportLogDB();
		return con;
	}   
	
	private void closeConnectionSupportLogDB(Connection con, ResultSet result)
	{
		if(result != null)
		{
			try
			{
				result.close();
				result = null;
			}
			catch(Exception ignore){}
		}
		if(con != null)
		{
			try
			{
				con.close();
				con = null;
			}
			catch(Exception ignore){}
		}
	}
	
	/*Connection to telco database*/
	private Connection connectToTelco() throws Exception
	{
		Connection con = null;
		con = Env.getConnectionTelcoDB();
		return con;
	}   
	
	private void closeConnectionTelco(Connection con, ResultSet result)
	{
		if(result != null)
		{
			try
			{
				result.close();
				result = null;
			}
			catch(Exception ignore){}
		}
		if(con != null)
		{
			try
			{
				con.close();
				con = null;
			}
			catch(Exception ignore){}
		}
	}
	
	/*Connection to ecard database*/
	private Connection connectToECard() throws Exception
	{
		Connection con = null;
		con = Env.getConnectionECardDB();
		return con;
	}   
	
	private void closeConnectionECard(Connection con, ResultSet result, ResultSet result1)
	{
		if(result != null)
		{
			try
			{
				result.close();
				result = null;
				
				result1.close();
				result1 = null;
			}
			catch(Exception ignore){}
		}
		if(con != null)
		{
			try
			{
				con.close();
				con = null;
			}
			catch(Exception ignore){}
		}
	}
	
	/*Connection to ectmc database*/
	private Connection connectToETMC() throws Exception
	{
		Connection con = null;
		con = Env.getConnectionETMCDB();
		return con;
	}   
	
	private void closeConnectionETMC(Connection con, ResultSet result)
	{
		if(result != null)
		{
			try
			{
				result.close();
				result = null;
			}
			catch(Exception ignore){}
		}
		if(con != null)
		{
			try
			{
				con.close();
				con = null;
			}
			catch(Exception ignore){}
		}
	}
	
	
	/*Connection to back up ecard database*/
	private Connection connectToBackUpECard() throws Exception
	{
		Connection con = null;
		con = Env.getConnectionBackUpECardDB();
		return con;
	}   
	
	private void closeConnectionBackUpEcard(Connection con, ResultSet result, ResultSet result1)
	{
		if(result != null)
		{
			try
			{
				result.close();
				result = null;
				
				result1.close();
				result1 = null;
			}
			catch(Exception ignore){}
		}
		if(con != null)
		{
			try
			{
				con.close();
				con = null;
			}
			catch(Exception ignore){}
		}
	}
	
	
	
	/*Connection to ecard test server*/
	private Connection connectToTestECard() throws Exception
	{
		Connection con = null;
		con = Env.getConnectionTestECardDB();
		return con;
	}   
	
	private void closeConnectionTestECard(Connection con, ResultSet result, ResultSet result1)
	{
		if(result != null)
		{
			try
			{
				result.close();
				result = null;
				
				result1.close();
				result1 = null;
			}
			catch(Exception ignore){}
		}
		if(con != null)
		{
			try
			{
				con.close();
				con = null;
			}
			catch(Exception ignore){}
		}
	}
	
	/*Connection to payoutlet server*/
	private Connection connectToPayoutlet() throws Exception
	{
		Connection con = null;
		con = Env.getConnectionPayoutletDB();
		return con;
	}   
	
	private void closeConnectionPayoutlet(Connection con, ResultSet result)
	{
		if(result != null)
		{
			try
			{
				result.close();
				result = null;
			}
			catch(Exception ignore){}
		}
		if(con != null)
		{
			try
			{
				con.close();
				con = null;
			}
			catch(Exception ignore){}
		}
	}
	
	
	/*Connection to cpay server*/
	private Connection connectToCPay() throws Exception
	{
		Connection con = null;
		con = Env.getConnectionCPayDB();
		return con;
	}   
	
	private void closeConnectionCPay(Connection con, ResultSet result)
	{
		if(result != null)
		{
			try
			{
				result.close();
				result = null;
			}
			catch(Exception ignore){}
		}
		if(con != null)
		{
			try
			{
				con.close();
				con = null;
			}
			catch(Exception ignore){}
		}
	}
	
	/*Connection to settle ecard server*/
	private Connection connectToSettleECard() throws Exception
	{
		Connection con = null;
		con = Env.getConnectionSettleECardDB();
		return con;
	}   
	
	private void closeConnectionSettleEcard(Connection con, ResultSet result)
	{
		if(result != null)
		{
			try
			{
				result.close();
				result = null;
			}
			catch(Exception ignore){}
		}
		if(con != null)
		{
			try
			{
				con.close();
				con = null;
			}
			catch(Exception ignore){}
		}
	}
	/*Connection to international server ecard database*/
	private Connection connectIntnlECard() throws Exception
	{
		Connection con = null;
		con = Env.getConnectionIntnlECardDB();
		return con;
	}   
	
	private void closeConnectionIntnlEcard(Connection con, ResultSet result)
	{
		if(result != null)
		{
			try
			{
				result.close();
				result = null;
			}
			catch(Exception ignore){}
		}
		if(con != null)
		{
			try
			{
				con.close();
				con = null;
			}
			catch(Exception ignore){}
		}
	}
	
	/*Connection to mobile db database*/
	private Connection connectMobileDB() throws Exception
	{
		Connection con = null;
		con = Env.getConnectionMobileDB();
		return con;
	}   
	
	private void closeConnectionMobileDB(Connection con, ResultSet result)
	{
		if(result != null)
		{
			try
			{
				result.close();
				result = null;
			}
			catch(Exception ignore){}
		}
		if(con != null)
		{
			try
			{
				con.close();
				con = null;
			}
			catch(Exception ignore){}
		}
	}
	
	
	/*Connection to recharge db server*/
	private Connection connectToRechargeDB() throws Exception
	{
		Connection con = null;
		con = Env.getConnectionRechargeDB();
		return con;
	}   
	
	private void closeConnectionRechargeDB(Connection con, ResultSet result)
	{
		if(result != null)
		{
			try
			{
				result.close();
				result = null;
			}
			catch(Exception ignore){}
		}
		if(con != null)
		{
			try
			{
				con.close();
				con = null;
			}
			catch(Exception ignore){}
		}
	}
	
	/*Connection to ecard db on 133 server*/
	private Connection connectPocketMoniEcardDB() throws Exception
	{
		Connection con = null;
		con = Env.getConnectionPocketMoniEcardDB();
		return con;
	}   
	
	private void closeConnectionPocketMoniEcardDB(Connection con, ResultSet result)
	{
		if(result != null)
		{
			try
			{
				result.close();
				result = null;
			}
			catch(Exception ignore){}
		}
		if(con != null)
		{
			try
			{
				con.close();
				con = null;
			}
			catch(Exception ignore){}
		}
	}
	
	
	/*Connection to telco database but with update permission*/
	private Connection connectToTelcoUpdate() throws Exception
	{
		Connection con = null;
		con = Env.getConnectionTelcoDBUpdate();
		return con;
	}   
	
	private void closeConnectionTelcoUpdate(Connection con, ResultSet result)
	{
		if(result != null)
		{
			try
			{
				result.close();
				result = null;
			}
			catch(Exception ignore){}
		}
		if(con != null)
		{
			try
			{
				con.close();
				con = null;
			}
			catch(Exception ignore){}
		}
	}
	
	
	
}
