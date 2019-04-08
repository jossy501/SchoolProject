package com.etranzact.supportmanager.model;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.etranzact.psm.dto.TPsmDealer;
import com.etranzact.supportmanager.dto.Car_Inventory;
import com.etranzact.supportmanager.dto.E_MERCHANT;
import com.etranzact.supportmanager.dto.E_MOBILE_SUBSCRIBER_CARD;
import com.etranzact.supportmanager.dto.IPAddress;
import com.etranzact.supportmanager.dto.MappedMerchants;
import com.etranzact.supportmanager.dto.Menu;
import com.etranzact.supportmanager.dto.MenuItem;
import com.etranzact.supportmanager.dto.MenuItemToIPAddress;
import com.etranzact.supportmanager.dto.MenuItemToMenu;
import com.etranzact.supportmanager.dto.MenuItemToRole;
import com.etranzact.supportmanager.dto.MenuToRole;
import com.etranzact.supportmanager.dto.ProfileMenuOptions;
import com.etranzact.supportmanager.dto.R_pins_bought;
import com.etranzact.supportmanager.dto.Summary;
import com.etranzact.supportmanager.dto.User;
import com.etranzact.supportmanager.dto.UserType;
import com.etranzact.supportmanager.utility.Env;
import com.etz.security.util.Cryptographer;


/**
 *@author Ezeanya Anthony
 * */

public class AdminModel
{
	
	public AdminModel(){}
	
	
	public String addIPToMenuItem(String menuitem_id, ArrayList arr, String createdbyuser_id)
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

			String query = "";
			
			for(int i=0;i<arr.size();i++)
			{
				
				query = "select * from telcodb..support_menuitemToIPAddress where ipaddress_id = "+Integer.parseInt(""+arr.get(i))+" and menuitem_id = "+Integer.parseInt(menuitem_id)+" ";
				result = stat.executeQuery(query);
				if(result.next())
				{
					message = "An ip address in the selected list has already been mapped to this menuitem";
					return message;
				}
				
				query = "insert into telcodb..support_menuitemToIPAddress(menuitem_id, ipaddress_id, createdBy, created_dt)values" +
				"("+ menuitem_id +","+Integer.parseInt(""+arr.get(i))+", "+Integer.parseInt(createdbyuser_id)+", getDate())";
				
				output = stat.executeUpdate(query);
				if(output <= 0)
				{
					con.rollback();
					message = "Records not inserted";
					return message;
				}
			}
			
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
				message = "Error occured while adding ipaddress to menuitem";
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
	
	

	public String createUser(String email, String password,String lname, String fname, 
			String mobile, String type_id,String status_id ,String ip_address, String user_code,
			String createdbyuser_id, String username, String service_id, String esa_auth)
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
			
			Cryptographer crpt = new Cryptographer();
			String crypt_password = crpt.doMd5Hash(username+password);
			System.out.println("crypt_password " + crypt_password);
			
			String query = "select username from telcodb..support_user where username = '"+username+"'";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "This username " + username  + " already exists";
				return message;
			}
			
			query = "insert into telcodb.dbo.support_user(email, password, lastname, firstname, mobile, type_id, status_id," +
					" ip_address, create_date, user_code, createdby,username, first_logon, service_id, esa_auth)" +
					"values('"+email+"', '"+crypt_password+"','"+lname+"','"+fname+"', '"+mobile+"', "+type_id+"," +
							""+status_id+",'"+ip_address+"', getDate(), '"+user_code+"', "+createdbyuser_id+", " +
							" '"+username+"','0','"+service_id+"', '"+esa_auth+"')";
			
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
				message = "Error occured while creating user";
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
	
	
	public String createMenu(String menu_nm, String menu_comments, String createdbyuser_id)
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

			String query = "select menu_id from telcodb..support_menu where menu_nm = '"+menu_nm+"' and menu_comments = '"+menu_comments+"' ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "This menu " + menu_nm  + " already exists";
				return message;
			}
			
			query = "insert into telcodb..support_menu(menu_nm, menu_comments, createdBy, created_dt)" +
					" values('"+menu_nm+"', '"+menu_comments+"', "+createdbyuser_id+", getDate())";
			
			////System.out.println("query " + query);
			
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
				message = "Error occured while creating menu";
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
	
	
	public String createMenuItem(String menuitem_nm, String menuitem_link, String menuitem_comments,
			String extraParam, String createdbyuser_id, ArrayList arr)
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

			String query = "select menuitem_id from telcodb..support_menuitem where menuitem_nm = '"+menuitem_nm+"' and menuitem_link = '"+menuitem_link+"' ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "This menuitem name " + menuitem_nm  + " already exists";
				return message;
			}
			
			query = "insert into telcodb..support_menuitem(menuitem_nm, menuitem_link, menuitem_comments, extraParam," +
					" createdBy, created_dt)" +
					" values('"+menuitem_nm+"', '"+menuitem_link+"','"+menuitem_comments+"', '"+extraParam+"', "+createdbyuser_id+", getDate())";
			
			//System.out.println("query " + query);
			
			output = stat.executeUpdate(query);
			
			if(output > 0)
			{
				if(extraParam.equals("RESTRICTED ACCESS"))
				{
					query = "select menuitem_id from telcodb..support_menuitem where menuitem_nm = '"+menuitem_nm+"'";
					result = stat.executeQuery(query);
					if(result.next())
					{
						int menuitem_id = Integer.parseInt(""+result.getObject(1));
						//System.out.println("menuitem_id " + menuitem_id);
						
						for(int i=0;i<arr.size();i++)
						{
							query = "insert into telcodb..support_menuitemToIPAddress(menuitem_id, ipaddress_id, createdBy, created_dt)values" +
							"("+ menuitem_id +","+Integer.parseInt(""+arr.get(i))+", "+Integer.parseInt(createdbyuser_id)+", getDate())";
							
							output = stat.executeUpdate(query);
							if(output <= 0)
							{
								con.rollback();
								message = "Records not inserted";
								return message;
							}
						}
						
						if(output > 0)
						{
							con.commit();
							message = "Records successfully inserted";
						}
					}
					else
					{
						con.rollback();
						message = "Records not inserted";
					}
				}
				else
				{
					con.commit();
					message = "Records successfully inserted";
				}
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
				message = "Error occured while creating menuitem";
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
	public String createCarInventory(String all_makes, String all_model,String car_year, String engine, String transmission, String car_color,
			String fuelType, String mileage, String vin, String stock_number, String targetPrice)
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

	
			String query = "insert into telcodb.dbo.cars_inventory(all_makes, all_model, car_year,engine,transmission,car_color,fuel_type,mileage,vin,stock_number,target_price,created_date)" +
					" values('"+all_makes+"','"+all_model+"','"+car_year+"','"+engine+"','"+transmission+"','"+car_color+"','"+fuelType+"','"+mileage+"','"+vin+"','"+stock_number+"','"+targetPrice+"',getDate())";
			
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
				message = "Error occured while creating car inventory";
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
	public String createIPAddressRestriction(String ip_address, String createdbyuser_id)
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

			String query = "select id from telcodb..support_ipaddress where ip_address = '"+ip_address+"'";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "This ip_address " + ip_address  + " already exists";
				return message;
			}
			
			query = "insert into telcodb..support_ipaddress(ip_address, createdBy, created_dt)" +
					" values('"+ip_address+"', "+createdbyuser_id+", getDate())";
			
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
				message = "Error occured while creating menuitem";
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
	
	
	/*This method is used to delete the menu created*/
	public String deleteMenu(String menu_id)
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

			String query = "select menuitem_id from telcodb..support_menuitemToMenu where menu_id = "+menu_id+" ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "Record not deleted, A menuitem is already mapped to this menu.";
				return message;
			}
			
			query = "delete from telcodb..support_menu where menu_id = "+menu_id+" ";
			
			//System.out.println("query " + query);
			
			output = stat.executeUpdate(query);
			
			if(output > 0)
			{
				con.commit();
				message = "Records successfully deleted";
			}
			else
			{
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
				message = "Error occured while deleting menu";
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
	
	/*This method is used to delete the menuitem created*/
	public String deleteMenuItem(String menuitem_id)
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

			String query = "select menuitem_id from telcodb..support_menuitemToMenu where menuitem_id = "+menuitem_id+" ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "Record not deleted, This menuitem is already mapped to a menu.";
				return message;
			}
			
			query = "delete from telcodb..support_menuitem where menuitem_id = "+menuitem_id+" ";
			
			//System.out.println("query " + query);
			
			output = stat.executeUpdate(query);
			
			if(output > 0)
			{
				con.commit();
				
				query = "delete from telcodb..support_menuitemToIPAddress where menuitem_id = "+menuitem_id+" ";
				//System.out.println("query " + query);
				output = stat.executeUpdate(query);
				if(output > 0)
				{
					con.commit();
				}
				
				message = "Records successfully deleted";
			}
			else
			{
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
	
	/*This method is used to delete the menuitem created*/
	public String deleteCarInventory(String inventoryId)
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
			
			String query = "delete from telcodb.dbo.cars_inventory where inventory_id = "+inventoryId+" ";

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
	
	
	
	/*This method is used to delete the mapped menuitem to menu created*/
	public String deleteMappedMenuItemToMenu(String menuitemToMenuId)
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

			String query = "select id from telcodb..support_menuitemToRole where menuitemToMenu_id = "+menuitemToMenuId+" ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "Record not deleted, This menuitem is already mapped to a role.";
				return message;
			}
			
			query = "delete from telcodb..support_menuitemToMenu where id = "+menuitemToMenuId+" ";
			
			//System.out.println("query " + query);
			
			output = stat.executeUpdate(query);
			
			if(output > 0)
			{
				con.commit();
				message = "Records successfully deleted";
			}
			else
			{
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
	
	/*This method is used to delete the mapped menuitem to role created*/
	public String deleteMappedMenuItemToRole(String id)
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
			
			String query = "delete from telcodb..support_menuitemToRole where id = "+id+" ";
			
			//System.out.println("query " + query);
			
			output = stat.executeUpdate(query);
			
			if(output > 0)
			{
				con.commit();
				message = "Records successfully deleted";
			}
			else
			{
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
	
	
	/*This method is used to delete the ip address*/
	public String deleteIPAddress(String id)
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
			
			
			String query = "select id from telcodb..support_menuitemToIPAddress where ipaddress_id = "+id+" ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "Record not deleted, This ip address is already mapped to a menuitem.";
				return message;
			}
			
			query = "delete from telcodb..support_ipaddress where id = "+id+" ";
			output = stat.executeUpdate(query);
			
			if(output > 0)
			{
				con.commit();
				message = "Records successfully deleted";
			}
			else
			{
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
	
	
	/*This method is used to delete the menuitem ip address*/
	public String deleteMenuitemIPAddress(String id)
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
			
			String query = "delete from telcodb..support_menuitemToIPAddress where id = "+id+" ";
			
			//System.out.println("query " + query);
			
			output = stat.executeUpdate(query);
			
			if(output > 0)
			{
				con.commit();
				message = "Records successfully deleted";
			}
			else
			{
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
	
	
	/*This method is used to delete the mapped account id to username*/
	public String deleteMappedAccountID(String id)
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
			
			String query = "delete from support_account_mapping where mapping_id = "+id+" ";
			
			output = stat.executeUpdate(query);
			
			if(output > 0)
			{
				con.commit();
				message = "Records successfully deleted";
			}
			else
			{
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
	
	
	/*This method is used to delete the mapped merchant*/
	public String deleteMappedMerchant(String id)
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
			
			String query = "delete from support_merchant_mapping where merchant_code = '"+id+"' ";
			
			output = stat.executeUpdate(query);
			
			if(output > 0)
			{
				con.commit();
				message = "Records successfully deleted";
			}
			else
			{
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
				message = "Error occured while removing a merchant";
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
	
	
	/*This method is used to delete the subscriber info*/
	public String deleteSubscriber(String id, String mobile, String mobile_type)
	{
		int output = -1;
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			if(mobile_type.equals("Version I"))
			{
				con = connectToECard();
				stat = con.createStatement();
				
				String query = "delete from e_mobile_subscriber_card where mobile = '"+mobile+"' ";
				output = stat.executeUpdate(query);
				if(output > 0)
				{
					output = -1;
					query = "delete from e_mobile_subscriber where mobile = '"+mobile+"' ";
					output = stat.executeUpdate(query);
					if(output > 0)
					{
						con.commit();
						message = "Records successfully deleted";
					}
					else
					{
						con.rollback();
						message = "Records not deleted";
					}
				}
				else
				{
					con.rollback();
					message = "Records not deleted";
				}
			}
			else if(mobile_type.equals("Version II"))
			{
				con = connectMobileDB();
				stat = con.createStatement();
				
				String query = "delete from m_mobile_subscriber_card where subscriber_id = "+Integer.parseInt(id)+" ";
				output = stat.executeUpdate(query);
				if(output > 0)
				{
					output = -1;
					query = "delete from m_mobile_subscriber where id = "+Integer.parseInt(id)+" ";
					output = stat.executeUpdate(query);
					if(output > 0)
					{
						con.commit();
						message = "Records successfully deleted";
					}
					else
					{
						con.rollback();
						message = "Records not deleted";
					}
				}
				else
				{
					con.rollback();
					message = "Records not deleted";
				}
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			try
			{
				con.rollback();
				message = "Error occured while removing a subscriber";
			}
			catch(Exception e){}
			closeConnectionECard(con, result, result);
			closeConnectionMobileDB(con, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeConnectionMobileDB(con, result);
		}
		return message;
	}
	
	
	
	/*This method is used to delete the subscriber card info*/
	public String deleteSubscriberCard(String card_num, String auth_status, String auth_by, String auth_byip)
	{
		int output = -1;
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			String query = "select card_num from e_mobile_subscriber_card where card_num = '"+card_num+"' ";
			result = stat.executeQuery(query);
			if(result.next())
			{

				query = "update e_mobile_subscriber_card set auth_status = '"+auth_status+"' , auth_by = '"+auth_by+"', auth_byip = '"+auth_byip+"' " +
				" where card_num = '"+card_num+"' ";
				output = stat.executeUpdate(query);
				if(output > 0)
				{
					 output = -1;
					 query = "select card_num, expiration, alias, modified," +
					 	" auth_status, request_by, request_byip, auth_by, auth_byip, mobile from e_mobile_subscriber_card where" +
						" card_num = '"+card_num+"' ";
					 
					 System.out.println("query " + query);
					 
					 result = stat.executeQuery(query);
					 if(result.next())
					 {
						 String cn = ""+result.getObject(1);
						 String ex = ""+result.getObject(2);
						 String al = ""+result.getObject(3);
						 String md = ""+result.getObject(4);
						 String au = ""+result.getObject(5);
						 String re = ""+result.getObject(6);
						 String reip = ""+result.getObject(7);
						 String authby = ""+result.getObject(8);
						 String authbyip = ""+result.getObject(9);
						 String mobile = ""+result.getObject(10);
						 
						 
						 query = "insert into e_mobile_subscriber_cardaudit" +
						 		"(card_num, expiration, alias, modified, auth_status, request_by," +
						 		" request_byip, auth_by, auth_byip, mobile)" +
						 		" values ('"+cn+"', '"+ex+"','"+al+"', '"+md+"', '"+au+"','"+re+"'," +
						 		" '"+reip+"','"+authby+"','"+authbyip+"', '"+mobile+"') ";
						 
						 output = stat.executeUpdate(query);
						 if(output > 0)
						 {
							output = -1;

							if(!al.equalsIgnoreCase("Default"))//default card
							{
								query = "delete from e_mobile_subscriber_card where card_number = '"+card_num+"' ";
								output = stat.executeUpdate(query);
								if(output > 0)
								{
									con.commit();
									message = "Records successfully deleted";
								}
								else
								{
									con.rollback();
									message = "Records not deleted";
								}
							}
							else
							{
								query = "select mobile, onetime_password, active, modified," +
							 	" diff, appid, sysdate, agentid, allow_debit, init_username," +
							 	" init_ip, auth_username, auth_ip, auth_date from e_mobile_subscriber where" +
								" mobile = '"+mobile+"' ";
							 
								 System.out.println("query " + query);
								 
								 result = stat.executeQuery(query);
								 if(result.next())
								 {
									 String mno = ""+result.getObject(1);
									 String on = ""+result.getObject(2);
									 String ac = ""+result.getObject(3);
									 String mo = ""+result.getObject(4);
									 String dif = ""+result.getObject(5);
									 String ap = ""+result.getObject(6);
									 String sys = ""+result.getObject(7);
									 String ag = ""+result.getObject(8);
									 String ald = ""+result.getObject(9);
									 String inu = ""+result.getObject(10);
									 String inip = ""+result.getObject(11);
									 String aun = ""+result.getObject(12);
									 String aui = ""+result.getObject(13);
									 String aud = ""+result.getObject(14);
									
									 							 
									 
									 query = "insert into e_mobile_subscriber_audit" +
								 		"(mobile_no, onetime_password, active, modified," +
								 		" diff, appid, sysdate, agentid, allow_debit, init_username," +
								 		" init_ip, auth_username, auth_ip, auth_date)" +
								 		" values ('"+mno+"', '"+on+"','"+ac+"', '"+mo+"', '"+dif+"','"+ap+"'," +
								 		" '"+sys+"','"+ag+"','"+ald+"','"+inu+"','"+inip+"','"+aun+"','"+aui+"','"+aud+"') ";
									 
									 output = stat.executeUpdate(query);
									 if(output > 0)
									 {
										output = -1;
										query = "delete from e_mobile_subscriber_card where card_num = '"+card_num+"' ";
										output = stat.executeUpdate(query);
										if(output > 0)
										{
											output = -1;
											query = "delete from e_mobile_subscriber_audit where mobile_no = '"+mno+"' ";
											output = stat.executeUpdate(query);
											if(output > 0)
											{
												con.commit();
												message = "Records successfully deleted";
											}
											else
											{
												con.rollback();
												message = "Records not deleted";
											}
										}
										else
										{
											con.rollback();
											message = "Records not deleted";
										}
									 }
									 else
									 {
										con.rollback();
										message = "Records not deleted";
									 }
								 }
								 else
								 {
									con.rollback();
									message = "Records not deleted";
								 }
							}
						 }
						 else
						 {
							con.rollback();
							message = "Records not deleted";
						 }
					}
					else
					{
						con.rollback();
						message = "Records not deleted";
					}
				}
				else
				{
					con.rollback();
					message = "Records not deleted";
				}
			}
			else
			{
				con = connectMobileDB();
				stat = con.createStatement();
				
				query = "select card_number from m_mobile_subscriber_card where card_number = '"+card_num+"' ";
				result = stat.executeQuery(query);
				if(result.next())
				{
					
					query = "update m_mobile_subscriber_card set auth_status = '"+auth_status+"', auth_by = '"+auth_by+"', auth_byip = '"+auth_byip+"' " +
					" where card_number = '"+card_num+"' ";
					output = stat.executeUpdate(query);
					if(output > 0)
					{
						 output = -1;
						 query = "select subscriber_id, card_number, expiration, alias, active, created, modified," +
						 	" auth_status, request_by, request_byip, auth_by, auth_byip from m_mobile_subscriber_card where" +
							" card_number = '"+card_num+"' ";
						 
						 System.out.println("query " + query);
						 
						 result = stat.executeQuery(query);
						 if(result.next())
						 {
							 String sb = ""+result.getObject(1);
							 String cn = ""+result.getObject(2);
							 String ex = ""+result.getObject(3);
							 String al = ""+result.getObject(4);
							 String ac = ""+result.getInt(5);
							 String cr = ""+result.getObject(6);
							 String md = ""+result.getObject(7);
							 String au = ""+result.getObject(8);
							 String re = ""+result.getObject(9);
							 String reip = ""+result.getObject(10);
							 String authby = ""+result.getObject(11);
							 String authbyip = ""+result.getObject(12);
							 
							 System.out.println("ac " + ac);
							 
							 query = "insert into m_mobile_subscriber_cardaudit" +
							 		"(subscriber_id, card_number, expiration, alias, active, created, modified, auth_status," +
							 		" request_by, request_byip, auth_by, auth_byip)" +
							 		" values ("+Integer.parseInt(sb)+", '"+cn+"', '"+ex+"','"+al+"', "+Integer.parseInt(ac)+", '"+cr+"', '"+md+"', '"+au+"'," +
							 		" '"+re+"','"+reip+"','"+authby+"','"+authbyip+"') ";
							 
							 output = stat.executeUpdate(query);
							 if(output > 0)
							 {
								output = -1;
								
								if(!al.equalsIgnoreCase("Default"))//default card
								{
									query = "delete from m_mobile_subscriber_card where card_number = '"+card_num+"' ";
									output = stat.executeUpdate(query);
									if(output > 0)
									{
										con.commit();
										message = "Records successfully deleted";
									}
									else
									{
										con.rollback();
										message = "Records not deleted";
									}
								}
								else
								{
									query = "select mobile_no, onetime_password, appid, agentid," +
								 	" apptype, active, created, modified from m_mobile_subscriber where" +
									" id = "+Integer.parseInt(sb)+" ";
								 
									 System.out.println("query " + query);
									 
									 result = stat.executeQuery(query);
									 if(result.next())
									 {
										 String mno = ""+result.getObject(1);
										 String on = ""+result.getObject(2);
										 String ap = ""+result.getObject(3);
										 String ag = ""+result.getObject(4);
										 int apt = result.getInt(5);
										 int sac = result.getInt(6);
										 String scr = ""+result.getObject(7);
										 String mo = ""+result.getObject(8);
										
										 System.out.println(sac + " sac");
										 
										 query = "insert into m_mobile_subscriber_audit" +
										 		"(mobile_no, onetime_password, appid, agentid, apptype, active," +
										 		" created, modified, auth_status, request_by, request_byip, auth_by, auth_byip)" +
										 		" values ('"+mno+"', '"+on+"',"+Integer.parseInt(ap)+", '"+ag+"'," +
										 		" "+ apt +", "+sac+" ," +
										 		" '"+scr+"','"+mo+"', '"+au+"', '"+re+"', '"+reip+"', '"+authby+"', '"+authbyip+"') ";
									
										 System.out.println(query + " insert query");
										 
										 output = stat.executeUpdate(query);
										 if(output > 0)
										 {
											output = -1;
											query = "delete from m_mobile_subscriber_card where card_number = '"+card_num+"' ";
											output = stat.executeUpdate(query);
											if(output > 0)
											{
												output = -1;
												query = "delete from m_mobile_subscriber where mobile_no = '"+mno+"' ";
												output = stat.executeUpdate(query);
												if(output > 0)
												{
													con.commit();
													message = "Records successfully deleted";
												}
												else
												{
													con.rollback();
													message = "Records not deleted";
												}
											}
											else
											{
												con.rollback();
												message = "Records not deleted";
											}
										 }
										 else
										 {
											con.rollback();
											message = "Records not deleted";
										 }
									 }
									 else
									 {
										con.rollback();
										message = "Records not deleted";
									 }
								}
							 }
							 else
							 {
								con.rollback();
								message = "Records not deleted";
							 }
						}
						else
						{
							con.rollback();
							message = "Records not deleted";
						}
					}
					else
					{
						con.rollback();
						message = "Records not deleted";
					}
				}
				else
				{
					con.rollback();
					message = "Records not deleted";
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			try
			{
				con.rollback();
				message = "Error occured while removing a subscriber card";
			}
			catch(Exception e){}
			closeConnectionECard(con, result, result);
			closeConnectionMobileDB(con, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeConnectionMobileDB(con, result);
		}
		return message;
	}
	
	
	
	
	public String resetPassword(String user_id, String default_password, String username)
	{
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		String message = "";
		
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			Cryptographer crpt = new Cryptographer();
			
			String crypt_password = crpt.doMd5Hash(username+default_password);
			
			String query = "update telcodb.dbo.support_user set password = '"+crypt_password+"' , first_logon = '0' where user_id = "+user_id+"";
			int output = stat.executeUpdate(query);
			if(output > 0)
			{
				con.commit();
				message = "Password successfully reset";
			}
			else
			{
				con.rollback();
				message = "Could not reset password, please contact the administrator";
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			try
			{
				con.rollback();
				message = "Error occured while resetting password";
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
	
	/*This method is used to get all users*/
	public ArrayList getUsers()
	{
		ArrayList arr = new ArrayList();
		User user  = null;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		String user_code_nm = "";
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			/*String query = "select user_id, email, password, lastname, firstname, type_id," +
					" (select distinct type_desc from telcodb..support_TYPE where type_id = telcodb..support_USER.type_id),mobile,  status_id," +
					" user_code, (select distinct status_desc from telcodb..support_status where status_id = telcodb..support_USER.status_id)," +
					" username,first_logon, (select description from telcodb..support_bank where bank_code = telcodb..support_user.user_code)," +
					" (select dealer_name from telcodb..T_PSM_DEALER where convert(varchar(25),dealer_id) = telcodb..support_user.user_code)," +
					" service_id" +
					" from telcodb..support_user";*/
			
			String query = "select user_id, email, password, lastname, firstname, type_id," +
			" (select distinct type_desc from telcodb..support_TYPE where type_id = telcodb..support_USER.type_id),mobile,  status_id," +
			" user_code, (select distinct status_desc from telcodb..support_status where status_id = telcodb..support_USER.status_id)," +
			" username,first_logon, (select description from telcodb..support_bank where bank_code = telcodb..support_user.user_code)," +
			" (select dealer_name from telcodb..T_PSM_DEALER where dealer_code = telcodb..support_user.user_code)," +
			" service_id" +
			" from telcodb..support_user";
			
			//System.out.println("query " + query);
			
			result = stat.executeQuery(query); 
			while(result.next())
			{
				user = new User();
				user.setUser_id(""+result.getObject(1));
				user.setEmail(""+result.getObject(2));
				user.setPassword(""+result.getObject(3));
				user.setLastname(""+result.getObject(4));
				user.setFirstname(""+result.getObject(5));
				user.setType_id(""+result.getObject(6));
				user.setType_nm(""+result.getObject(7));
				user.setMobile(""+result.getObject(8));
				user.setStatus_id(""+result.getObject(9));
				user.setUser_code(""+result.getObject(10));
				user.setStatus_nm(""+result.getObject(11));
				user.setUsername(""+result.getObject(12));
				user.setFirst_logon(""+result.getObject(13));
				
				user_code_nm = ""+result.getObject(14);
				user_code_nm = (user_code_nm.equals("null") ? "" : user_code_nm);
				user.setUser_code_nm(user_code_nm);
				user.setDealer_nm(""+result.getObject(15));
				user.setService_id(""+result.getObject(16));
				
				
				
				/*query = "select description from telcodb..support_bank where bank_code = '"+user.getUser_code()+"'";	
				result = stat.executeQuery(query);
				while(result.next())
				{
					message = ""+result.getObject(1);
				}
				if(message.equals(""))
				{
					query = "select dealer_name from telcodb..T_PSM_DEALER where dealer_id = "+bank_code+"";	
					result = stat.executeQuery(query);
					while(result.next())
					{
						message = ""+result.getObject(1);
					}
				}*/
				
				
				
				arr.add(user);
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
	
	/*This method is used to get all users by the type*/
	public ArrayList getUsersByType(String userType_desc)
	{
		ArrayList arr = new ArrayList();
		User user  = null;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			String query = "select user_id, email, password, lastname, firstname, type_id," +
					" (select distinct type_desc from telcodb..support_TYPE where type_id = telcodb..support_USER.type_id),mobile,  status_id," +
					" user_code, (select distinct status_desc from telcodb..support_status where status_id = telcodb..support_USER.status_id)," +
					" username,first_logon" +
					" from telcodb..support_user where type_id = (select telcodb..support_type.type_id from telcodb..support_type where telcodb..support_type.type_desc = '"+userType_desc+"') ";
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				user = new User();
				user.setUser_id(""+result.getObject(1));
				user.setEmail(""+result.getObject(2));
				user.setPassword(""+result.getObject(3));
				user.setLastname(""+result.getObject(4));
				user.setFirstname(""+result.getObject(5));
				user.setType_id(""+result.getObject(6));
				user.setType_nm(""+result.getObject(7));
				user.setMobile(""+result.getObject(8));
				user.setStatus_id(""+result.getObject(9));
				user.setUser_code(""+result.getObject(10));
				user.setStatus_nm(""+result.getObject(11));
				user.setUsername(""+result.getObject(12));
				user.setFirst_logon(""+result.getObject(13));
				
				arr.add(user);
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
	
	/*This method is used to get all users for a bank*/
	public ArrayList getBankUsers(String bank_code)
	{
		ArrayList arr = new ArrayList();
		User user  = null;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			String query = "select user_id, email, password, lastname, firstname, type_id, (select distinct type_desc from telcodb..support_TYPE" +
					" where type_id = telcodb..support_USER.type_id),mobile,  status_id, user_code," +
					" (select distinct status_desc from telcodb..support_status where status_id = telcodb..support_USER.status_id), username," +
					" first_logon,(select description from telcodb..support_bank where bank_code = telcodb..support_user.user_code)" +
					" from telcodb..support_user where user_code = '"+bank_code+"' ";
			result = stat.executeQuery(query);
			while(result.next())
			{
				user = new User();
				user.setUser_id(""+result.getObject(1));
				user.setEmail(""+result.getObject(2));
				user.setPassword(""+result.getObject(3));
				user.setLastname(""+result.getObject(4));
				user.setFirstname(""+result.getObject(5));
				user.setType_id(""+result.getObject(6));
				user.setType_nm(""+result.getObject(7));
				user.setMobile(""+result.getObject(8));
				user.setStatus_id(""+result.getObject(9));
				user.setUser_code(""+result.getObject(10));
				user.setStatus_nm(""+result.getObject(11));
				user.setUsername(""+result.getObject(12));
				user.setFirst_logon(""+result.getObject(13));
				user.setUser_code_nm(""+result.getObject(14));
				
				arr.add(user);
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
	
	
	/*This method is used to get all users for a scheme*/
	public ArrayList getSchemeUsers(String scheme_card)
	{
		ArrayList arr = new ArrayList();
		User user  = null;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			String query = "select user_id, email, password, lastname, firstname, type_id, (select distinct type_desc from telcodb..support_TYPE" +
					" where type_id = telcodb..support_USER.type_id),mobile,  status_id, user_code," +
					" (select distinct status_desc from telcodb..support_status where status_id = telcodb..support_USER.status_id), username," +
					" first_logon,(select description from telcodb..support_bank where bank_code = telcodb..support_user.user_code)" +
					" from telcodb..support_user where service_id = '"+scheme_card+"' ";
			result = stat.executeQuery(query);
			while(result.next())
			{
				user = new User();
				user.setUser_id(""+result.getObject(1));
				user.setEmail(""+result.getObject(2));
				user.setPassword(""+result.getObject(3));
				user.setLastname(""+result.getObject(4));
				user.setFirstname(""+result.getObject(5));
				user.setType_id(""+result.getObject(6));
				user.setType_nm(""+result.getObject(7));
				user.setMobile(""+result.getObject(8));
				user.setStatus_id(""+result.getObject(9));
				user.setUser_code(""+result.getObject(10));
				user.setStatus_nm(""+result.getObject(11));
				user.setUsername(""+result.getObject(12));
				user.setFirst_logon(""+result.getObject(13));
				user.setUser_code_nm(""+result.getObject(14));
				
				arr.add(user);
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
	
	
	/*Method to load the types of users*/
	public ArrayList getUserType()
	{
		ArrayList arr = new ArrayList();
		UserType user_type;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			String query = "select * from telcodb.dbo.support_type";
			result = stat.executeQuery(query);
			while(result.next())
			{
				user_type = new UserType();
				
				user_type.setType_id(""+result.getObject(1));
				user_type.setType_descr(""+result.getObject(2));

				arr.add(user_type);
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
	
	
	/*Method to get the user type name*/
	public String getUserTypeName(String userType_id)
	{
		String message = "";

		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			String query = "select type_desc from telcodb.dbo.support_type where type_id = "+userType_id+" ";
			result = stat.executeQuery(query);
			while(result.next())
			{
				message = ""+result.getObject(1);
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
		return message;
	}
	
	/*Method to get the user type id*/
	public String getUserTypeID(String userType_nm)
	{
		String message = "";

		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			String query = "select type_id from telcodb.dbo.support_type where type_desc = '"+userType_nm+"' ";
			result = stat.executeQuery(query);
			while(result.next())
			{
				message = ""+result.getObject(1);
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
		return message;
	}
	
	
	/**
	 * 
	 * Method to get users
	 */
	public ArrayList getUser(String email)
	{
		ArrayList arr = new ArrayList();
		User user;
		String empty = "";
		String query = "";
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			if(email.equals(empty))
			{
				query = "select user_id, email, password, lastname, firstname, type_id, mobile ," +
						" (select distinct type_desc from telcodb..support_TYPE where type_id = telcodb..support_USER.type_id)," +
						" status_id,username from telcodb..support_user";
			}
			else
			{
				query = "select user_id, email, password, lastname, firstname, type_id, mobile," +
						" (select distinct type_desc from telcodb..support_TYPE where type_id = telcodb..support_USER.type_id)," +
						" status_id,username from telcodb..support_user where email = '"+email+"'";
			}
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				user = new User();
				user.setUser_id(""+result.getObject(1));
				user.setEmail(""+result.getObject(2));
				user.setPassword(""+result.getObject(3));
				user.setLastname(""+result.getObject(4));
				user.setFirstname(""+result.getObject(5));
				user.setType_id(""+result.getObject(6));
				user.setMobile(""+result.getObject(7));
				user.setType_nm(""+result.getObject(8));
				user.setStatus_id(""+result.getObject(9));
				user.setUsername(""+result.getObject(10));
				
				arr.add(user);
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
	
	/**
	 * 
	 * Method to get users by user id
	 */
	public ArrayList getUserByUserID(String user_id)
	{
		ArrayList arr = new ArrayList();
		User user;
		String empty = "";
		String query = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			query = "select user_id, email, password, lastname, firstname,type_id,mobile," +
					"(select distinct type_desc from telcodb..support_TYPE where type_id = telcodb..support_USER.type_id),status_id," +
					" user_code,username,service_id, esa_auth from telcodb..support_user where user_id = "+user_id+"";
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				user = new User();
				user.setUser_id(""+result.getObject(1));
				user.setEmail(""+result.getObject(2));
				user.setPassword(""+result.getObject(3));
				user.setLastname(""+result.getObject(4));
				user.setFirstname(""+result.getObject(5));
				user.setType_id(""+result.getObject(6));
				user.setMobile(""+result.getObject(7));
				user.setType_nm(""+result.getObject(8));
				user.setStatus_id(""+result.getObject(9));
				user.setUser_code(""+result.getObject(10));
				user.setUsername(""+result.getObject(11));
				user.setService_id(""+result.getObject(12));
				user.setEsa_type(""+result.getObject(13));
				arr.add(user);
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
	
	
	
	
	
	
	
	/*Method to get PSM Dealer*/
	public ArrayList getTPsmDealer()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		TPsmDealer psm_dealer = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		
		try
		{
			con = connectTo4DRSDB();
			stat = con.createStatement();

			query = "select dealer_code, dealer_name from telcodb..T_PSM_DEALER";
		
			result = stat.executeQuery(query);
			while(result.next())
			{
				
				psm_dealer = new TPsmDealer();
				
				psm_dealer.setDealerName(""+result.getObject(1));
				psm_dealer.setDealerAddress(""+result.getObject(2));
				
				arr.add(psm_dealer);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnection4DRSDB(con, result);
		}
		finally
		{
			closeConnection4DRSDB(con, result);
		}
		return arr;
	}
	
	
	
	/**
	 * 
	 * Method to get menus by id
	 */
	public ArrayList getMenuByMenuID(String menu_id)
	{
		ArrayList arr = new ArrayList();
		Menu m;
		String empty = "";
		String query = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			query = "select menu_id, menu_nm, createdBy, created_dt, menu_comments from telcodb..support_menu where menu_id = "+menu_id+"  ";
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				m = new Menu();

				m.setMenu_id(""+result.getInt(1));
				m.setMenu_nm(result.getString(2));
				m.setCreatedBy(""+result.getObject(3));
				m.setCreated_dt(""+result.getObject(4));
				m.setMenu_comments(""+result.getObject(5));
				
				arr.add(m);
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
	
	
	
	/**
	 * 
	 * Method to get menuitems by id
	 */
	public ArrayList getMenuitemByMenuID(String menuitem_id)
	{
		ArrayList arr = new ArrayList();
		MenuItem mitem;
		String empty = "";
		String query = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			query = "select menuitem_id, menuitem_nm, menuitem_link, menuitem_comments," +
					" extraParam, createdBy, created_dt from telcodb..support_menuitem where menuitem_id =  "+menuitem_id+" ";
			
			//System.out.println("getMenuItems " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				mitem = new MenuItem();
				
				mitem.setMenuitem_id(""+result.getObject(1));
				mitem.setMenuitem_nm(""+result.getObject(2));
				mitem.setMenuitem_link(""+result.getObject(3));
				mitem.setMenuitem_comments(""+result.getObject(4));
				mitem.setExtraParam(""+result.getObject(5));
				mitem.setCreatedBy(""+result.getObject(6));
				mitem.setCreated_dt(""+result.getObject(7));
				
				arr.add(mitem);
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
	
	
	/**
	 * 
	 * Method to get menuitems by id
	 */
	public ArrayList getCarInventoryByInventoryID(String inventoryId)
	{
		ArrayList arr = new ArrayList();
		Car_Inventory carInventory;
		String empty = "";
		String query = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			query = "select inventory_id,all_makes, all_model, car_year, engine," +
					" transmission, car_color, fuel_type,mileage,vin,stock_number,target_price from telcodb.dbo.cars_inventory where inventory_id =  '"+inventoryId+"' ";
			
			//System.out.println("getMenuItems " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				carInventory = new Car_Inventory();
				carInventory.setInventory_id(""+result.getObject(1));
				carInventory.setAll_makes(""+result.getObject(2));
				carInventory.setAll_model(""+result.getObject(3));
				carInventory.setCar_year(""+result.getObject(4));
				carInventory.setEngine(""+result.getObject(5));
				carInventory.setTransmission(""+result.getObject(6));
				carInventory.setCar_color(""+result.getObject(7));
				carInventory.setFuel_type(""+result.getObject(8));
				carInventory.setMileage(""+result.getObject(9));
				carInventory.setVin(""+result.getObject(10));
				carInventory.setStock_number(""+result.getObject(11));
				carInventory.setTarget_price(""+result.getObject(12));
				
				arr.add(carInventory);
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
	
	
	
	/**
	 * 
	 * Method to get merchants mapped to a bizdev user
	 */
	public ArrayList getMerchantsOfABizDev(String accountid)
	{
		ArrayList arr = new ArrayList();
		E_MERCHANT emerchant;
		String query = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int counter = 0;
		
		String mcode = "";
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			query = "select username, merchant_code, merchant_name" +
					" from support_merchant_mapping where username =  '"+accountid+"' ";
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				emerchant = new E_MERCHANT();
				
				emerchant.setCounter(""+counter);
				emerchant.setAccount_id(""+result.getObject(1));
				emerchant.setMerchant_code(""+result.getObject(2));
				emerchant.setMerchant_name(""+result.getObject(3));
				arr.add(emerchant);
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
	
	
	
	
	/**
	 * 
	 * Method for updating users
	 * */
	public String updateUser(String user_id,String email, String lname, String fname, 
			String mobile, String type_id, String user_code, String editedby_user_id,
			String username, String service_id, String esa_auth)
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
			
			String query = "select username from telcodb..support_user where username = '"+username+"' and user_id <> "+user_id+" ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "This username " + username  + " already exists";
				return message;
			}
			
			output = stat.executeUpdate("update telcodb.dbo.support_user set email= '"+email+"' , lastname = '"+lname+"'," +
					" firstname = '"+fname+"', mobile = '"+mobile+"', type_id = "+type_id+", user_code = '"+user_code+"'," +
					" modified_date = getDate(), username = '"+username+"', service_id = '"+service_id+"', esa_auth = '"+esa_auth+"' " +
					"where user_id = "+user_id+"");
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
				message = "Error occured while updating user";
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
	
	
	
	/**
	 * Method for updating menus
	 * */
	public String updateMenu(String menu_id,String menu_nm, String menu_comments,String editedby_user_id)
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
			
			String query = "select menu_nm from telcodb..support_menu where menu_nm = '"+menu_nm+"' and menu_comments = '"+menu_comments+"' and menu_id <> "+menu_id+" ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "This menu " + menu_nm  + " already exists";
				return message;
			}
			
			output = stat.executeUpdate("update telcodb.dbo.support_menu set menu_nm= '"+menu_nm+"' , menu_comments = '"+menu_comments+"'," +
					" createdBy =  "+editedby_user_id+" " +
					"where menu_id = "+menu_id+"");
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
				message = "Error occured while updating menu";
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
	
	
	/**
	 * Method for updating menuitems
	 * */
	public String updateMenuitem(String menuitem_id,String menuitem_nm, String menuitem_link, String menuitem_comments,
			String extraParam, ArrayList arr, String editedby_user_id)
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
			
			String query = "select menuitem_id from telcodb..support_menuitem where menuitem_nm = '"+menuitem_nm+"'" +
					" and menuitem_link = '"+menuitem_link+"' and menuitem_id <> "+menuitem_id+" ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "This menuitem name " + menuitem_nm  + " already exists";
				return message;
			}
			
			
			output = stat.executeUpdate("update telcodb.dbo.support_menuitem set menuitem_nm= '"+menuitem_nm+"' , menuitem_link = '"+menuitem_link+"'," +
					" menuitem_comments = '"+menuitem_comments+"', extraParam = '"+extraParam+"', " +
					" createdBy =  "+editedby_user_id+" " +
					"where menuitem_id = "+menuitem_id+"");
			if(output > 0)
			{
				if(extraParam.equals("RESTRICTED ACCESS"))
				{
					for(int i=0;i<arr.size();i++)
					{
						
						query = "select * from telcodb..support_menuitemToIPAddress where ipaddress_id = "+Integer.parseInt(""+arr.get(i))+"" +
								" and menuitem_id = "+Integer.parseInt(menuitem_id)+" ";
						result = stat.executeQuery(query);
						if(result.next())
						{
							message = "An ip address in the selected list has already been mapped to this menuitem";
							return message;
						}
						
						query = "insert into telcodb..support_menuitemToIPAddress(menuitem_id, ipaddress_id, createdBy, created_dt)values" +
						"("+ Integer.parseInt(menuitem_id) +","+Integer.parseInt(""+arr.get(i))+", "+Integer.parseInt(editedby_user_id)+", getDate())";
						
						output = stat.executeUpdate(query);
						if(output <= 0)
						{
							con.rollback();
							message = "Records not updated";
							return message;
						}
					}
					
					if(output > 0)
					{
						con.commit();
						message = "Records successfully updated";
					}
				}
				else
				{
					query = "select * from telcodb..support_menuitemToIPAddress where menuitem_id = "+Integer.parseInt(menuitem_id)+" ";
					result = stat.executeQuery(query);
					if(result.next())
					{
						query = "delete from telcodb..support_menuitemToIPAddress where menuitem_id = "+Integer.parseInt(menuitem_id)+" ";
						output = stat.executeUpdate(query);
						if(output > 0)
						{
							con.commit();
							message = "Records successfully updated";
						}
					}
					else
					{
						con.commit();
						message = "Records successfully updated";
					}
				}
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
				message = "Error occured while updating menu";
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
	
	
	/**
	 * 
	 * Method for update car Inventory
	 * */
	

	
	public String updateCarInventory(String inventoryId,String allMakes, String allModel, String carYear, String engine, String transmission,
			String carColor, String fuelType, String mileage , String vin, String stockNumber, String targetPrice)
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
			
			output = stat.executeUpdate("update telcodb.dbo.cars_inventory set all_makes= '"+allMakes+"', all_model = '"+allModel+"', car_year ='"+carYear+"'"
					+ ", engine = '"+engine+"', transmission = '"+transmission+"', car_color = '"+carColor+"', fuel_type = '"+fuelType+"',"
							+ "mileage = '"+mileage+"', vin='"+vin+"', stock_number = '"+stockNumber+"' , target_price = '"+targetPrice+"' where inventory_id = '"+inventoryId+"'");
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
				message = "Error occured while updating user";
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

	
	public ArrayList getSearchCarInventory(String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Car_Inventory carInventory = null;
		String str = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			//mobile channel
			query = "select inventory_id, all_makes, all_model,car_year,engine,transmission ,"
					+ "car_color, fuel_type, created_date, mileage, vin,stock_number,"
					+ "target_price from telcodb.dbo.cars_inventory where created_date between('"+ start_dt +"') and ('"+ end_dt +"')";
			result = stat.executeQuery(query);
			while(result.next()){
				carInventory = new Car_Inventory();
				carInventory.setInventory_id(""+result.getObject(1));
				carInventory.setAll_makes(""+result.getObject(2));
				carInventory.setAll_model(""+result.getObject(3));
				carInventory.setCar_year(""+result.getObject(4));
				carInventory.setEngine(""+result.getObject(5));
				carInventory.setTransmission(""+result.getObject(6));
				carInventory.setCar_color(""+result.getObject(7));
				carInventory.setFuel_type(""+result.getObject(8));
				carInventory.setCreated_date(""+result.getObject(9));
				carInventory.setMileage(""+result.getObject(10));
				carInventory.setVin(""+result.getObject(11));
				carInventory.setStock_number(""+result.getObject(12));
				carInventory.setTarget_price(""+result.getObject(13));
				arr.add(carInventory);
			}
		}catch(Exception ex){
				closeConnectionSupportLogDB(con, result);
			}
			finally
			{
				closeConnectionSupportLogDB(con, result);
			}
		
		return arr;
		
		}

	
	/**
	 * 
	 * Method for disabling and enabling users
	 * */
	public String enable_disableUser(String status_id, String user_id)
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
			
			output = stat.executeUpdate("update telcodb.dbo.support_user set status_id= "+status_id+" where user_id = "+user_id+"");
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
				message = "Error occured while updating user";
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

	
	
	/**
	 * 
	 * Method for mapping account id to bizdev users
	 * */
	public String mapAccountIDToBizDevUser(String username, String account_id)
	{
		int output = -1;
		String message = "";
		String query = "";
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			
			query = "select * from telcodb..support_account_mapping where account_id = '"+account_id+"' ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "This account " + account_id  + " has already been mapped to a user";
				return message;
			}
			
			query = "select * from telcodb..support_account_mapping where username = '"+username+"' ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "This username " + username  + " has already been mapped to an account";
				return message;
			}
			
			
			query = "insert into telcodb..support_account_mapping(username, account_id, crt_dt)values" +
					"('"+username+"','"+account_id+"', getDate())";
			
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
				message = "Error occured while mapping accounts";
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
	
	
	/**
	 * 
	 * Method for mapping merchants to bizdev users
	 * */
	public String mapMerchantsToBizDevUser(ArrayList arr, String account_id)
	{
		int output = -1;
		String message = "";
		String query = "";
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		String data = "";
		String merchant_code = "";
		String merchant_name = "";
	
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			
			for(int i=0;i<arr.size();i++)
			{
				data = ""+arr.get(i);
				merchant_code = data.substring(0, data.indexOf(":"));
				merchant_name = data.substring(data.indexOf(":")+1);

				query = "select * from support_merchant_mapping where merchant_code = '"+merchant_code+"' ";
				result = stat.executeQuery(query);
				if(result.next())
				{
					message = "This merchant " + arr.get(i)  + " has already been mapped to an account";
					return message;
				}
				
				query = "insert into support_merchant_mapping(username, merchant_code, merchant_name)values('"+account_id+"', '"+merchant_code+"', '"+merchant_name+"')";
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
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			try
			{
				con.rollback();
				message = "Error occured while mapping accounts";
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
	
	/*Map menus to roles*/
	public String mapMenuToRole(ArrayList menuList, String userType_id, String createdBy)
	{
		int output = -1;
		String message = "";
		String query = "";
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			
			for(int i=0;i<menuList.size();i++)
			{
				query = "select * from telcodb..support_menuToRole where menu_id = "+Integer.parseInt(""+menuList.get(i))+" and userType_id = "+Integer.parseInt(userType_id)+" ";
				result = stat.executeQuery(query);
				if(result.next())
				{
					message = "A menu in the selected list has already been mapped to this role";
					return message;
				}
				
				query = "insert into telcodb..support_menuToRole(menu_id, userType_id, createdBy, created_dt)values" +
				"("+Integer.parseInt(""+menuList.get(i))+","+Integer.parseInt(userType_id)+", "+Integer.parseInt(createdBy)+", getDate())";
				output = stat.executeUpdate(query);
				if(output <= 0)
				{
					con.rollback();
					message = "Records not inserted";
					return message;
				}
			}

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
				message = "Error occured while mapping accounts";
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
	
	
	/*Map menuitems to menus*/
	public String mapMenuItemToMenu(ArrayList menuItemList, String menu_id, String createdBy)
	{
		int output = -1;
		String message = "";
		String query = "";
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			
			for(int i=0;i<menuItemList.size();i++)
			{
				query = "select * from telcodb..support_menuitemToMenu where menuitem_id = "+Integer.parseInt(""+menuItemList.get(i))+" and menu_id = "+Integer.parseInt(menu_id)+" ";
				result = stat.executeQuery(query);
				if(result.next())
				{
					message = "A menulist in the selected list has already been mapped to this menu";
					return message;
				}
				
				query = "insert into telcodb..support_menuitemToMenu(menuitem_id, menu_id, createdBy, created_dt)values" +
				"("+Integer.parseInt(""+menuItemList.get(i))+","+Integer.parseInt(menu_id)+", "+Integer.parseInt(createdBy)+", getDate())";
				output = stat.executeUpdate(query);
				if(output <= 0)
				{
					con.rollback();
					message = "Records not inserted";
					return message;
				}
			}

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
				message = "Error occured while mapping accounts";
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
	
	
	/*Map menuitems to role*/
	public String mapMenuItemToRole(ArrayList menuItemList, String userType_id, String createdBy)
	{
		int output = -1;
		String message = "";
		String query = "";
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			
			for(int i=0;i<menuItemList.size();i++)
			{
				query = "select * from telcodb..support_menuitemToRole where menuitemToMenu_id = "+Integer.parseInt(""+menuItemList.get(i))+" and userType_id = "+Integer.parseInt(userType_id)+" ";
				result = stat.executeQuery(query);
				if(result.next())
				{
					message = "A menulist in the selected list has already been mapped to this role";
					return message;
				}
				
				query = "insert into telcodb..support_menuitemToRole(menuitemToMenu_id, userType_id, createdBy, created_dt)values" +
				"("+Integer.parseInt(""+menuItemList.get(i))+","+Integer.parseInt(userType_id)+", "+Integer.parseInt(createdBy)+", getDate())";
				output = stat.executeUpdate(query);
				if(output <= 0)
				{
					con.rollback();
					message = "Records not inserted";
					return message;
				}
			}

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
				message = "Error occured while mapping accounts";
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
	
	/*public String mapMerchantsToBizDevUser(String username, String merchant_code, String merchant_name)
	{
		int output = -1;
		String message = "";
		String query = "";
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			
			query = "select * from telcodb..support_merchant_mapping where username = '"+username+"' and merchant_code = '"+merchant_code+"' ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "This merchant " + merchant_name  + " has already been mapped to this user " + username;
				return message;
			}
			
			
			query = "insert into telcodb..support_merchant_mapping(username, merchant_code, merchant_name, crt_dt)values" +
					"('"+username+"','"+merchant_code+"', '"+merchant_name+"', getDate())";
			
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
				message = "Error occured while mapping merchants";
			}
			catch(Exception e){}
			closeConnectionSupportLogDB(con, result);
		}
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		return message;
	}*/
	
	
	/*Method to get the mapped account_ids to bizdev*/
	public ArrayList getMappedMerchantsToBizDev()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		MappedMerchants mm = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			query = "select mapping_id, username, account_id from telcodb..support_account_mapping";
		
			//System.out.println("getMappedMerchantsToBizDev " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				
				mm = new MappedMerchants();
				
				mm.setCounter(""+counter);
				mm.setMapping_id(""+result.getObject(1));
				mm.setUsername(""+result.getObject(2));
				mm.setAccount_id(""+result.getObject(3));
				
				arr.add(mm);
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
	
	
	/*Method to get the mapped menus to roles*/
	public ArrayList getMappedMenusToRoles(String userType_id)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		MenuToRole mr = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			
			
			query = "select id, menu_id, userType_id, (select menu_nm from support_menu where support_menu.menu_id = support_menuToRole.menu_id)," +
					" (select type_desc from support_type where support_type.type_id = support_menuToRole.userType_id)," +
					" createdBy, created_dt from telcodb..support_menuToRole where support_menuToRole.userType_id = "+Integer.parseInt(userType_id)+" ";
		
			//System.out.println("getMappedMenusToRoles " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				
				mr = new MenuToRole();
				
				mr.setCounter(""+counter);
				mr.setId(""+result.getObject(1));
				mr.setMenu_id(""+result.getObject(2));
				mr.setUserType_id(""+result.getObject(3));
				mr.setMenu_nm(""+result.getObject(4));
				mr.setUserType_nm(""+result.getObject(5));
				mr.setCreatedBy(""+result.getObject(6));
				mr.setCreated_dt(""+result.getObject(7));

				arr.add(mr);
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
	
	
	/*Method to get the mapped menuitems to menus*/
	public ArrayList getMappedMenuItemsToMenus(String menu_id)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		MenuItemToMenu mr = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			
			
			query = "select id, menuitem_id, menu_id, (select menuitem_nm from support_menuitem where support_menuitem.menuitem_id = support_menuitemToMenu.menuitem_id)," +
					" (select menu_nm from support_menu where support_menu.menu_id = support_menuitemToMenu.menu_id)," +
					" createdBy, created_dt from telcodb..support_menuitemToMenu where support_menuitemToMenu.menu_id = "+Integer.parseInt(menu_id)+" ";
		
			//System.out.println("getMappedMenuItemsToMenus " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				
				mr = new MenuItemToMenu();
				
				mr.setCounter(""+counter);
				mr.setId(""+result.getObject(1));
				mr.setMenuitem_id(""+result.getObject(2));
				mr.setMenu_id(""+result.getObject(3));
				mr.setMenuitem_nm(""+result.getObject(4));
				mr.setMenu_nm(""+result.getObject(5));
				mr.setCreatedBy(""+result.getObject(6));
				mr.setCreated_dt(""+result.getObject(7));

				arr.add(mr);
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
	
	/*Method to get the mapped menuitems to menus, used while mapping to the role*/
	public ArrayList getMappedMenuItemsToMenus2()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		MenuItemToMenu mr = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			
			
			query = "select id, menuitem_id, menu_id, (select menuitem_nm from support_menuitem where support_menuitem.menuitem_id = support_menuitemToMenu.menuitem_id)," +
					" (select menu_nm from support_menu where support_menu.menu_id = support_menuitemToMenu.menu_id)," +
					" createdBy, created_dt from telcodb..support_menuitemToMenu";
		
			//System.out.println("getMappedMenuItemsToMenus " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				
				mr = new MenuItemToMenu();
				
				mr.setCounter(""+counter);
				mr.setId(""+result.getObject(1));
				mr.setMenuitem_id(""+result.getObject(2));
				mr.setMenu_id(""+result.getObject(3));
				mr.setMenuitem_nm(""+result.getObject(4));
				mr.setMenu_nm(""+result.getObject(5));
				mr.setCreatedBy(""+result.getObject(6));
				mr.setCreated_dt(""+result.getObject(7));

				arr.add(mr);
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
	
	
	/*Method to get the mapped menuitems to roles*/
	public ArrayList getMappedMenuItemsToRoles(String userType_id)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		MenuItemToRole mr = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			
			
			query = "select id, menuitemToMenu_id, userType_id," +
					" (select support_menuitem.menuItem_nm from support_menuitem where menuitem_id = (select support_menuitemToMenu.menuItem_id from support_menuitemToMenu where support_menuitemToMenu.id = support_menuitemToRole.menuitemToMenu_id))," +
					" (select support_menu.menu_nm from support_menu where menu_id = (select support_menuitemToMenu.menu_id from support_menuitemToMenu where support_menuitemToMenu.id = support_menuitemToRole.menuitemToMenu_id))," +
					" (select type_desc from support_type where support_type.type_id = support_menuitemToRole.userType_id)," +
					" createdBy, created_dt from telcodb..support_menuitemToRole where support_menuitemToRole.userType_id = "+Integer.parseInt(userType_id)+" ";
		
			//System.out.println("getMappedMenuItemsToMenus " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				
				mr = new MenuItemToRole();
				
				mr.setCounter(""+counter);
				mr.setId(""+result.getObject(1));
				mr.setMenuItemToMenu_id(""+result.getObject(2));
				mr.setUserType_id(""+result.getObject(3));
				mr.setMenuItem_nm(""+result.getObject(4));
				mr.setMenu_nm(""+result.getObject(5));
				mr.setUserType_nm(""+result.getObject(6));
				mr.setCreatedBy(""+result.getObject(7));
				mr.setCreated_dt(""+result.getObject(8));

				arr.add(mr);
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
	
	
	/*Method to get the distinct menu*/
	public ArrayList getDistinctMenu(String menutemToMenu_id, String ip_address)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		int counter = 0;
		Connection con = null;
		Connection con1 = null;
		Statement stat = null;
		Statement stat1 = null;
		ResultSet result = null;
		ResultSet result1 = null;
		boolean b = false;
		String extraParam = "";
		String menuitem_link = "";
		
		try
		{
			con = connectToSupportLog();
			con1 = con;
			
			stat = con.createStatement();
			stat1 = con1.createStatement();
			
			String menu_label = "";
			String menu_id = "";
			
			query = "select distinct menu_id, (select telcodb..support_menu.menu_nm from telcodb..support_menu where telcodb..support_menu.menu_id = telcodb..support_menuitemToMenu.menu_id) from support_menuitemToMenu where id in ("+menutemToMenu_id+") order by menu_id asc";
			
			System.out.println("getDistinctMenu " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				menu_id = ""+result.getObject(1);
				menu_label = ""+result.getObject(2);
				
				ArrayList a = new ArrayList();
				query = "select menuitem_id,(select telcodb..support_menuitem.menuitem_nm from telcodb..support_menuitem where telcodb..support_menuitem.menuitem_id = telcodb..support_menuitemToMenu.menuitem_id),(select telcodb..support_menuitem.menuitem_link from telcodb..support_menuitem where telcodb..support_menuitem.menuitem_id = telcodb..support_menuitemToMenu.menuitem_id),(select telcodb..support_menuitem.extraParam from telcodb..support_menuitem where telcodb..support_menuitem.menuitem_id = telcodb..support_menuitemToMenu.menuitem_id) from support_menuitemToMenu where menu_id =  "+ Integer.parseInt(menu_id) +" and id in ("+menutemToMenu_id+") ";
				System.out.println("inner query " + query);
				result1 = stat1.executeQuery(query);
				while(result1.next())
				{
					MenuItem m = new MenuItem();
					b = false;
					extraParam = "";
					menuitem_link = "";
					
					m.setMenuitem_id(""+result1.getObject(1));
					m.setMenuitem_nm(""+result1.getObject(2));
					//m.setMenuitem_link(""+result1.getObject(3));
					menuitem_link = ""+result1.getObject(3);
					extraParam = ""+result1.getObject(4);
					
					if(extraParam.equals("RESTRICTED ACCESS"))
					{
						b = getOnlyIPAddress(""+result1.getObject(1)).contains(ip_address);
						//System.out.println("b " + b);
						if(b)
						{
							m.setMenuitem_link(menuitem_link);
						}
						else
						{
							m.setMenuitem_link("/support/accessRestriction.xhtml");
						}
					}
					else
					{
						m.setMenuitem_link(menuitem_link);
					}
					m.setExtraParam(extraParam);
					a.add(m);
				}
				
				ProfileMenuOptions pr = new ProfileMenuOptions(menu_label, a);
				arr.add(pr);
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
	
	
	/*Method to get the menus*/
	public ArrayList getMenus()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Menu m = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			query = "select menu_id, menu_nm, createdBy, created_dt, menu_comments from telcodb..support_menu";
		
			//System.out.println("getMenus " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				
				m = new Menu();
				
				m.setCounter(""+counter);
				m.setMenu_id(""+result.getInt(1));
				m.setMenu_nm(result.getString(2));
				m.setCreatedBy(""+result.getObject(3));
				m.setCreated_dt(""+result.getObject(4));
				m.setMenu_comments(""+result.getObject(5));
				
				arr.add(m);
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
	
	
	/*Method to get the menuitems*/
	public ArrayList getMenuItems()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		MenuItem mitem = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			query = "select menuitem_id, menuitem_nm, menuitem_link, menuitem_comments, extraParam, createdBy, created_dt from telcodb..support_menuitem";
		
			//System.out.println("getMenuItems " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				
				mitem = new MenuItem();
				
				mitem.setCounter(""+counter);
				mitem.setMenuitem_id(""+result.getObject(1));
				mitem.setMenuitem_nm(""+result.getObject(2));
				mitem.setMenuitem_link(""+result.getObject(3));
				mitem.setMenuitem_comments(""+result.getObject(4));
				mitem.setExtraParam(""+result.getObject(5));
				mitem.setCreatedBy(""+result.getObject(6));
				mitem.setCreated_dt(""+result.getObject(7));
				
				arr.add(mitem);
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
	
	
	/*Method to get the menuitems*/
	public ArrayList getCarInventoryMenuItems()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Car_Inventory cInventory = null;
		MenuItem mitem = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;

		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			query = "select inventory_id, all_makes, all_model, car_year, engine, transmission, car_color,fuel_type,car_price, created_date, car_image_location, "
					+ "mileage, vin, stock_number, target_price from telcodb.dbo.cars_inventory";
		
			//System.out.println("getMenuItems " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				
				cInventory = new Car_Inventory();
				
				cInventory.setCounter(""+counter);
				cInventory.setInventory_id(""+result.getObject(1));
				cInventory.setAll_makes(""+result.getObject(2));
				cInventory.setAll_model(""+result.getObject(3));
				cInventory.setCar_year(""+result.getObject(4));
				cInventory.setEngine(""+result.getObject(5));
				cInventory.setTransmission(""+result.getObject(6));
				cInventory.setCar_color(""+result.getObject(7));
				cInventory.setFuel_type(""+result.getObject(8));
				cInventory.setCar_price(""+result.getObject(9));
				cInventory.setCreated_date(""+result.getObject(10));
				cInventory.setImage_location(""+result.getObject(11));
				cInventory.setMileage(""+result.getObject(12));
				cInventory.setVin(""+result.getObject(13));
				cInventory.setStock_number(""+result.getObject(14));
				cInventory.setTarget_price(""+result.getObject(15));
				arr.add(cInventory);
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
	
	
	/*Method to get the menuitems when i pass the menuitem*/
	public ArrayList getMenuItemIPAdrress(String menuitem_id)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		MenuItemToIPAddress mitem = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			query = "select id,(select menuitem_nm from support_menuitem where menuitem_id = support_menuitemToIPAddress.menuitem_id), (select ip_address from support_ipaddress where id = support_menuitemToIPAddress.ipaddress_id), created_dt from telcodb..support_menuitemToIPAddress where menuitem_id = "+menuitem_id+" ";
		
			//System.out.println("getMenuItemIPAdrress " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				
				mitem = new MenuItemToIPAddress();
				
				mitem.setCounter(""+counter);
				mitem.setId(""+result.getObject(1));
				mitem.setMenuitem_nm(""+result.getObject(2));
				mitem.setIpaddress_nm(""+result.getObject(3));
				mitem.setCreated_dt(""+result.getObject(4));
				
				arr.add(mitem);
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
	
	
	/*Method to get the ip_addresses*/
	public ArrayList getIPAddress()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		IPAddress ip = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			query = "select id, ip_address, created_dt from telcodb..support_ipaddress";
		
			//System.out.println("getIPAddress " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				
				ip = new IPAddress();
				
				ip.setCounter(""+counter);
				ip.setId(""+result.getObject(1));
				ip.setIp_address(""+result.getObject(2));
				ip.setCreated_dt(""+result.getObject(3));
				
				
				arr.add(ip);
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
	
	
	/*Method to get ip_address only by menuitem_id*/
	public ArrayList getOnlyIPAddress(String menuitem_id)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			//query = "select ip_address from telcodb..support_ipaddress";
			query = "select (select ip_address from support_ipaddress where id = support_menuitemToIPAddress.ipaddress_id) from support_menuitemToIPAddress where menuitem_id = "+menuitem_id+" ";
		
			//System.out.println("getOnlyIPAddress " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				arr.add(""+result.getObject(1));
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
	

	
	/*Method to get ip_address only*/
	public ArrayList secondLevelIPValidation(String menuitem_nm, String ip_address)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			

			/*query = "select extraParam from telcodb..support_menuitem" +
					" where menuitem_id = (select menuitem_id from support_menuitem where menuitem_link = '"+menuitem_nm+"')";
			result = stat.executeQuery(query);
			while(result.next())
			{
				arr.add(""+result.getObject(1));
			}
			
			query = "select id from telcodb..support_menuitemToIPAddress" +
					" where menuitem_id = (select menuitem_id from support_menuitem where menuitem_link = '"+menuitem_nm+"')" +
					" and ipaddress_id = (select id from support_ipaddress where ip_address = '"+ip_address+"')";
		
			System.out.println("secondLevelIPValidation " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				arr.add(""+result.getObject(1));
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
		return arr;
	}
	
	
	
	/*this is used to set a card for deleting*/
	public String setMappedCardForDeleting(String mobile_type, String auth_status, String request_by,
			String request_byip, String card_num)
	{
		int output = -1;
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			if(mobile_type.equals("Version I"))
			{
				con = connectToECard();
				stat = con.createStatement();
				
				String query = "update e_mobile_subscriber_card set auth_status = '"+auth_status+"' , request_by = '"+request_by+"'," +
						" request_byip = '"+request_byip+"' where card_num = '"+card_num+"' ";
				output = stat.executeUpdate(query);
				if(output > 0)
				{
					con.commit();
					message = "Records queued for deletion, awaiting authorisation";
				}
				else
				{
					con.rollback();
					message = "Records not queued";
				}
			}
			else if(mobile_type.equals("Version II"))
			{
				con = connectMobileDB();
				stat = con.createStatement();
				
				String query = "update m_mobile_subscriber_card set auth_status = '"+auth_status+"' , request_by = '"+request_by+"'," +
						" request_byip = '"+request_byip+"'  where card_number = '"+card_num+"' ";
				output = stat.executeUpdate(query);
				if(output > 0)
				{
					con.commit();
					message = "Records queued for deletion, awaiting authorisation";
				}
				else
				{
					con.rollback();
					message = "Records not queued";
				}
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			try
			{
				con.rollback();
				message = "Error occured while removing a subscriber card";
			}
			catch(Exception e){}
			closeConnectionECard(con, result, result);
			closeConnectionMobileDB(con, result);
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeConnectionMobileDB(con, result);
		}
		return message;
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
	private Connection connectTo4DRSDB() throws Exception
	{
		Connection con = null;
		con = Env.getConnection4DRSDB();
		return con;
	}   
	
	private void closeConnection4DRSDB(Connection con, ResultSet result)
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
	
	/*Connection to t_psm server*/
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
	

}

