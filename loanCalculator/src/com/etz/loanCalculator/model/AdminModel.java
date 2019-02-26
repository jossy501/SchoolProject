package com.etz.loanCalculator.model;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;


import com.etz.loanCalculator.dto.IPAddress;
import com.etz.loanCalculator.dto.Menu;
import com.etz.loanCalculator.dto.MenuItem;
import com.etz.loanCalculator.dto.MenuItemToIPAddress;
import com.etz.loanCalculator.dto.MenuItemToMenu;
import com.etz.loanCalculator.dto.MenuItemToRole;
import com.etz.loanCalculator.dto.MenuToRole;
import com.etz.loanCalculator.dto.ProfileMenuOptions;
import com.etz.loanCalculator.dto.User;
import com.etz.loanCalculator.dto.UserType;
import com.etz.loanCalculator.utility.DateUtil;
import com.etz.loanCalculator.utility.Env;
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
				
				query = "select * from telcodb..loan_menuitemToIPAddress where ipaddress_id = "+Integer.parseInt(""+arr.get(i))+" and menuitem_id = "+Integer.parseInt(menuitem_id)+" ";
				result = stat.executeQuery(query);
				if(result.next())
				{
					message = "An ip address in the selected list has already been mapped to this menuitem";
					return message;
				}
				
				query = "insert into telcodb..loan_menuitemToIPAddress(menuitem_id, ipaddress_id, createdBy, created_dt)values" +
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
			String createdbyuser_id, String username, String esa_auth)
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
			
			String query = "select username from telcodb..loan_user where username = '"+username+"'";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "This username " + username  + " already exists";
				return message;
			}
			
			query = "insert into telcodb.dbo.loan_user(email, password, lastname, firstname, mobile, type_id, status_id," +
					" ip_address, create_date, user_code, createdby,username, first_logon, esa_auth)" +
					"values('"+email+"', '"+crypt_password+"','"+lname+"','"+fname+"', '"+mobile+"', "+type_id+"," +
							""+status_id+",'"+ip_address+"', getDate(), '"+user_code+"', "+createdbyuser_id+", " +
							" '"+username+"','0', '"+esa_auth+"')";
			
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

			String query = "select menu_id from telcodb..loan_menu where menu_nm = '"+menu_nm+"' and menu_comments = '"+menu_comments+"' ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "This menu " + menu_nm  + " already exists";
				return message;
			}
			
			query = "insert into telcodb..loan_menu(menu_nm, menu_comments, createdBy, created_dt)" +
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

			String query = "select menuitem_id from telcodb..loan_menuitem where menuitem_nm = '"+menuitem_nm+"' and menuitem_link = '"+menuitem_link+"' ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "This menuitem name " + menuitem_nm  + " already exists";
				return message;
			}
			
			query = "insert into telcodb..loan_menuitem(menuitem_nm, menuitem_link, menuitem_comments, extraParam," +
					" createdBy, created_dt)" +
					" values('"+menuitem_nm+"', '"+menuitem_link+"','"+menuitem_comments+"', '"+extraParam+"', "+createdbyuser_id+", getDate())";
			
			//System.out.println("query " + query);
			
			output = stat.executeUpdate(query);
			
			if(output > 0)
			{
				if(extraParam.equals("RESTRICTED ACCESS"))
				{
					query = "select menuitem_id from telcodb..loan_menuitem where menuitem_nm = '"+menuitem_nm+"'";
					result = stat.executeQuery(query);
					if(result.next())
					{
						int menuitem_id = Integer.parseInt(""+result.getObject(1));
						//System.out.println("menuitem_id " + menuitem_id);
						
						for(int i=0;i<arr.size();i++)
						{
							query = "insert into telcodb..loan_menuitemToIPAddress(menuitem_id, ipaddress_id, createdBy, created_dt)values" +
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

			String query = "select id from telcodb..loan_ipaddress where ip_address = '"+ip_address+"'";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "This ip_address " + ip_address  + " already exists";
				return message;
			}
			
			query = "insert into telcodb..loan_ipaddress(ip_address, createdBy, created_dt)" +
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
	
	
	/*Method to create the loan house*/
	public String createLoanHouse(String loanHouse, String imagePath,
			String createdbyuser_id, String code, String exposure)
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

			String query = "select id from telcodb..loan_house where code = '"+code+"'";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "This loan house " + loanHouse  + " already exists";
				return message;
			}
			
			query = "insert into telcodb..loan_house(name, houseLogo, createdBy, created_dt, code, exposure)" +
					" values('"+loanHouse+"', '"+ imagePath +"' , "+Integer.parseInt(createdbyuser_id)+",  getDate(), '"+code+"', '"+exposure+"')";
			
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
				message = "Error occured while creating Loan House";
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
	
	public String createBorrower(String lastname,String firstname,String mobile,String email, Date dob, 
			String address,String nationalID,String staffID,String companyName,String monthlyIncome)
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

	   
		   query = "select id from telcodb..loan_borrower where mobile = '"+mobile+"'";
		   result = stat.executeQuery(query);
		   if(result.next())
		   {
			   message = "A borrower with this mobile number " + mobile  + " already exists.";
			   return message;
		   }
		   
		   query = "select id from telcodb..loan_borrower where nationalID = '"+nationalID+"'";
		   result = stat.executeQuery(query);
		   if(result.next())
		   {
			   message = "A borrower with this national id number " + nationalID  + " already exists.";
			   return message;
		   }
		   
		   query = "select id from telcodb..loan_borrower where companyName = '"+companyName+"' and staffID = '"+staffID+"' ";
		   result = stat.executeQuery(query);
		   if(result.next())
		   {
			   message = "A borrower with this staff id number " + staffID  + " in this same company already exists.";
			   return message;
		   }
	   
		   query = "insert into telcodb.dbo.loan_borrower(lastname,firstname,mobile,email,dob,address,nationalID,staffID,companyName,monthlyIncome)" +
		   	"values('"+lastname+"', '"+firstname+"','"+mobile+"','"+email+"', '"+new DateUtil().formatDate(dob) +"', '"+address+"'," +
		   	"'"+nationalID+"','"+staffID+"','"+companyName+"', "+Double.parseDouble(monthlyIncome)+")";

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

			String query = "select menuitem_id from telcodb..loan_menuitemToMenu where menu_id = "+menu_id+" ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "Record not deleted, A menuitem is already mapped to this menu.";
				return message;
			}
			
			query = "delete from telcodb..loan_menu where menu_id = "+menu_id+" ";
			
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

			String query = "select menuitem_id from telcodb..loan_menuitemToMenu where menuitem_id = "+menuitem_id+" ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "Record not deleted, This menuitem is already mapped to a menu.";
				return message;
			}
			
			query = "delete from telcodb..loan_menuitem where menuitem_id = "+menuitem_id+" ";
			
			//System.out.println("query " + query);
			
			output = stat.executeUpdate(query);
			
			if(output > 0)
			{
				con.commit();
				
				query = "delete from telcodb..loan_menuitemToIPAddress where menuitem_id = "+menuitem_id+" ";
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

			String query = "select id from telcodb..loan_menuitemToRole where menuitemToMenu_id = "+menuitemToMenuId+" ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "Record not deleted, This menuitem is already mapped to a role.";
				return message;
			}
			
			query = "delete from telcodb..loan_menuitemToMenu where id = "+menuitemToMenuId+" ";
			
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
			
			String query = "delete from telcodb..loan_menuitemToRole where id = "+id+" ";
			
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
			
			
			String query = "select id from telcodb..loan_menuitemToIPAddress where ipaddress_id = "+id+" ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "Record not deleted, This ip address is already mapped to a menuitem.";
				return message;
			}
			
			query = "delete from telcodb..loan_ipaddress where id = "+id+" ";
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
			
			String query = "delete from telcodb..loan_menuitemToIPAddress where id = "+id+" ";
			
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
			
			String query = "update telcodb.dbo.loan_user set password = '"+crypt_password+"' , first_logon = '0' where user_id = "+user_id+"";
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
			
			
			String query = "select user_id, email, password, lastname, firstname, type_id," +
			" (select distinct type_desc from telcodb..loan_TYPE where type_id = telcodb..loan_USER.type_id),mobile,  status_id," +
			" user_code, (select distinct status_desc from telcodb..loan_status where status_id = telcodb..loan_USER.status_id)," +
			" username,first_logon, (select name from telcodb..loan_house where code = telcodb..loan_user.user_code)" +
			" from telcodb..loan_user";
			
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
					" (select distinct type_desc from telcodb..loan_TYPE where type_id = telcodb..loan_USER.type_id),mobile,  status_id," +
					" user_code, (select distinct status_desc from telcodb..loan_status where status_id = telcodb..loan_USER.status_id)," +
					" username,first_logon" +
					" from telcodb..loan_user where type_id = (select telcodb..loan_type.type_id from telcodb..loan_type where telcodb..loan_type.type_desc = '"+userType_desc+"') ";
			
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
	public ArrayList getLoanUsers(String bank_code)
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
			
			String query = "select user_id, email, password, lastname, firstname, type_id, (select distinct type_desc from telcodb..loan_TYPE" +
					" where type_id = telcodb..loan_USER.type_id),mobile,  status_id, user_code," +
					" (select distinct status_desc from telcodb..loan_status where status_id = telcodb..loan_USER.status_id), username," +
					" first_logon " +
					" from telcodb..loan_user where user_code = '"+bank_code+"' ";
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
			
			String query = "select * from telcodb.dbo.loan_type";
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
			
			String query = "select type_desc from loan_type where type_id = "+userType_id+" ";
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
			
			String query = "select type_id from telcodb.dbo.loan_type where type_desc = '"+userType_nm+"' ";
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
						" (select distinct type_desc from telcodb..loan_TYPE where type_id = telcodb..loan_USER.type_id)," +
						" status_id,username from telcodb..loan_user";
			}
			else
			{
				query = "select user_id, email, password, lastname, firstname, type_id, mobile," +
						" (select distinct type_desc from telcodb..loan_TYPE where type_id = telcodb..loan_USER.type_id)," +
						" status_id,username from telcodb..loan_user where email = '"+email+"'";
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
					"(select distinct type_desc from telcodb..loan_TYPE where type_id = telcodb..loan_USER.type_id),status_id," +
					" user_code,username, esa_auth, card_scheme from telcodb..loan_user where user_id = "+user_id+"";
			
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
				user.setEsa_type(""+result.getObject(12));
				String cardscheme = ""+result.getObject(13);
				if(cardscheme == null || cardscheme.equals("null"))
				{
					cardscheme = "";
				}
				user.setCardScheme(cardscheme);
				
				
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
			
			query = "select menu_id, menu_nm, createdBy, created_dt, menu_comments from telcodb..loan_menu where menu_id = "+menu_id+"  ";
			
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
					" extraParam, createdBy, created_dt from telcodb..loan_menuitem where menuitem_id =  "+menuitem_id+" ";
			
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
	 * Method for updating users
	 * */
	public String updateUser(String user_id,String email, String lname, String fname, 
			String mobile, String type_id, String user_code, String editedby_user_id,
			String username, String esa_auth)
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
			
			String query = "select username from telcodb..loan_user where username = '"+username+"' and user_id <> "+user_id+" ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "This username " + username  + " already exists";
				return message;
			}
			
			output = stat.executeUpdate("update telcodb.dbo.loan_user set email= '"+email+"' , lastname = '"+lname+"'," +
					" firstname = '"+fname+"', mobile = '"+mobile+"', type_id = "+type_id+", user_code = '"+user_code+"'," +
					" modified_date = getDate(), username = '"+username+"',  esa_auth = '"+esa_auth+"' " +
					" where user_id = "+user_id+"");
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
			System.out.println("query updateUser()  :: :: : : ::  "+output);
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
			
			String query = "select menu_nm from telcodb..loan_menu where menu_nm = '"+menu_nm+"' and menu_comments = '"+menu_comments+"' and menu_id <> "+menu_id+" ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "This menu " + menu_nm  + " already exists";
				return message;
			}
			
			output = stat.executeUpdate("update telcodb.dbo.loan_menu set menu_nm= '"+menu_nm+"' , menu_comments = '"+menu_comments+"'," +
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
			
			String query = "select menuitem_id from telcodb..loan_menuitem where menuitem_nm = '"+menuitem_nm+"'" +
					" and menuitem_link = '"+menuitem_link+"' and menuitem_id <> "+menuitem_id+" ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "This menuitem name " + menuitem_nm  + " already exists";
				return message;
			}
			
			
			output = stat.executeUpdate("update telcodb.dbo.loan_menuitem set menuitem_nm= '"+menuitem_nm+"' , menuitem_link = '"+menuitem_link+"'," +
					" menuitem_comments = '"+menuitem_comments+"', extraParam = '"+extraParam+"', " +
					" createdBy =  "+editedby_user_id+" " +
					"where menuitem_id = "+menuitem_id+"");
			if(output > 0)
			{
				if(extraParam.equals("RESTRICTED ACCESS"))
				{
					for(int i=0;i<arr.size();i++)
					{
						
						query = "select * from telcodb..loan_menuitemToIPAddress where ipaddress_id = "+Integer.parseInt(""+arr.get(i))+"" +
								" and menuitem_id = "+Integer.parseInt(menuitem_id)+" ";
						result = stat.executeQuery(query);
						if(result.next())
						{
							message = "An ip address in the selected list has already been mapped to this menuitem";
							return message;
						}
						
						query = "insert into telcodb..loan_menuitemToIPAddress(menuitem_id, ipaddress_id, createdBy, created_dt)values" +
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
					query = "select * from telcodb..loan_menuitemToIPAddress where menuitem_id = "+Integer.parseInt(menuitem_id)+" ";
					result = stat.executeQuery(query);
					if(result.next())
					{
						query = "delete from telcodb..loan_menuitemToIPAddress where menuitem_id = "+Integer.parseInt(menuitem_id)+" ";
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
			
			output = stat.executeUpdate("update telcodb.dbo.loan_user set status_id= "+status_id+" where user_id = "+user_id+"");
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
			
			
			query = "select * from telcodb..loan_account_mapping where account_id = '"+account_id+"' ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "This account " + account_id  + " has already been mapped to a user";
				return message;
			}
			
			query = "select * from telcodb..loan_account_mapping where username = '"+username+"' ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "This username " + username  + " has already been mapped to an account";
				return message;
			}
			
			
			query = "insert into telcodb..loan_account_mapping(username, account_id, crt_dt)values" +
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

				query = "select * from loan_merchant_mapping where merchant_code = '"+merchant_code+"' ";
				result = stat.executeQuery(query);
				if(result.next())
				{
					message = "This merchant " + arr.get(i)  + " has already been mapped to an account";
					return message;
				}
				
				query = "insert into loan_merchant_mapping(username, merchant_code, merchant_name)values('"+account_id+"', '"+merchant_code+"', '"+merchant_name+"')";
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
				query = "select * from telcodb..loan_menuToRole where menu_id = "+Integer.parseInt(""+menuList.get(i))+" and userType_id = "+Integer.parseInt(userType_id)+" ";
				result = stat.executeQuery(query);
				if(result.next())
				{
					message = "A menu in the selected list has already been mapped to this role";
					return message;
				}
				
				query = "insert into telcodb..loan_menuToRole(menu_id, userType_id, createdBy, created_dt)values" +
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
				query = "select * from telcodb..loan_menuitemToMenu where menuitem_id = "+Integer.parseInt(""+menuItemList.get(i))+" and menu_id = "+Integer.parseInt(menu_id)+" ";
				result = stat.executeQuery(query);
				if(result.next())
				{
					message = "A menulist in the selected list has already been mapped to this menu";
					return message;
				}
				
				query = "insert into telcodb..loan_menuitemToMenu(menuitem_id, menu_id, createdBy, created_dt)values" +
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
				query = "select * from telcodb..loan_menuitemToRole where menuitemToMenu_id = "+Integer.parseInt(""+menuItemList.get(i))+" and userType_id = "+Integer.parseInt(userType_id)+" ";
				result = stat.executeQuery(query);
				if(result.next())
				{
					message = "A menulist in the selected list has already been mapped to this role";
					return message;
				}
				
				query = "insert into telcodb..loan_menuitemToRole(menuitemToMenu_id, userType_id, createdBy, created_dt)values" +
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
			
			
			
			query = "select id, menu_id, userType_id, (select menu_nm from loan_menu where loan_menu.menu_id = loan_menuToRole.menu_id)," +
					" (select type_desc from loan_type where loan_type.type_id = loan_menuToRole.userType_id)," +
					" createdBy, created_dt from telcodb..loan_menuToRole where loan_menuToRole.userType_id = "+Integer.parseInt(userType_id)+" ";
		
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
			
			
			
			query = "select id, menuitem_id, menu_id, (select menuitem_nm from loan_menuitem where loan_menuitem.menuitem_id = loan_menuitemToMenu.menuitem_id)," +
					" (select menu_nm from loan_menu where loan_menu.menu_id = loan_menuitemToMenu.menu_id)," +
					" createdBy, created_dt from telcodb..loan_menuitemToMenu where loan_menuitemToMenu.menu_id = "+Integer.parseInt(menu_id)+" ";
		
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
			
			
			
			query = "select id, menuitem_id, menu_id, (select menuitem_nm from loan_menuitem where loan_menuitem.menuitem_id = loan_menuitemToMenu.menuitem_id)," +
					" (select menu_nm from loan_menu where loan_menu.menu_id = loan_menuitemToMenu.menu_id)," +
					" createdBy, created_dt from telcodb..loan_menuitemToMenu";
		
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
					" (select loan_menuitem.menuItem_nm from loan_menuitem where menuitem_id = (select loan_menuitemToMenu.menuItem_id from loan_menuitemToMenu where loan_menuitemToMenu.id = loan_menuitemToRole.menuitemToMenu_id))," +
					" (select loan_menu.menu_nm from loan_menu where menu_id = (select loan_menuitemToMenu.menu_id from loan_menuitemToMenu where loan_menuitemToMenu.id = loan_menuitemToRole.menuitemToMenu_id))," +
					" (select type_desc from loan_type where loan_type.type_id = loan_menuitemToRole.userType_id)," +
					" createdBy, created_dt from telcodb..loan_menuitemToRole where loan_menuitemToRole.userType_id = "+Integer.parseInt(userType_id)+" ";
		
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
			
			query = "select distinct menu_id, (select telcodb..loan_menu.menu_nm from telcodb..loan_menu where telcodb..loan_menu.menu_id = telcodb..loan_menuitemToMenu.menu_id) from loan_menuitemToMenu where id in ("+menutemToMenu_id+") order by menu_id asc";
			
			System.out.println("getDistinctMenu " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				menu_id = ""+result.getObject(1);
				menu_label = ""+result.getObject(2);
				
				ArrayList a = new ArrayList();
				query = "select menuitem_id,(select telcodb..loan_menuitem.menuitem_nm from telcodb..loan_menuitem where telcodb..loan_menuitem.menuitem_id = telcodb..loan_menuitemToMenu.menuitem_id),(select telcodb..loan_menuitem.menuitem_link from telcodb..loan_menuitem where telcodb..loan_menuitem.menuitem_id = telcodb..loan_menuitemToMenu.menuitem_id),(select telcodb..loan_menuitem.extraParam from telcodb..loan_menuitem where telcodb..loan_menuitem.menuitem_id = telcodb..loan_menuitemToMenu.menuitem_id) from loan_menuitemToMenu where menu_id =  "+ Integer.parseInt(menu_id) +" and id in ("+menutemToMenu_id+") ";
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
			
			query = "select menu_id, menu_nm, createdBy, created_dt, menu_comments from telcodb..loan_menu";
		
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
			
			query = "select menuitem_id, menuitem_nm, menuitem_link, menuitem_comments, extraParam, createdBy, created_dt from telcodb..loan_menuitem";
		
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
			
			query = "select id,(select menuitem_nm from loan_menuitem where menuitem_id = loan_menuitemToIPAddress.menuitem_id), (select ip_address from loan_ipaddress where id = loan_menuitemToIPAddress.ipaddress_id), created_dt from telcodb..loan_menuitemToIPAddress where menuitem_id = "+menuitem_id+" ";
		
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
			
			query = "select id, ip_address, created_dt from telcodb..loan_ipaddress";
		
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
			query = "select (select ip_address from loan_ipaddress where id = loan_menuitemToIPAddress.ipaddress_id) from loan_menuitemToIPAddress where menuitem_id = "+menuitem_id+" ";
		
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
}

