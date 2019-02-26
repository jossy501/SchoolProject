/**
 * 
 */
package com.etranzact.supportmanager.model;



import java.sql.*;
import java.util.ArrayList;

import com.etranzact.supportmanager.dto.User;
import com.etranzact.supportmanager.utility.Env;
import com.etz.security.util.Cryptographer;

import ecard.E_SECURE;

/**
 * This class handles the authentication of users
 * The password encryption mechanism has been removed
 * @author Anthony.Ezeanya
 *
 */

public class AuthenticationModel
{

	//Connection con;
	//Statement stat;
	//ResultSet result;
    static ResultSetMetaData _rsmd = null;
	
	public AuthenticationModel()
	{}
	
	/**
	 * This method handles the login process and returns a User Object
	 * @param username
	 * @param password
	 * @return
	 */
	public User login(String username,String password)
	{
		User user = null;
		//ecard.E_SECURE secure = new E_SECURE();
		//System.out.println("to encode " + secure.f_code(password));
		
		Cryptographer crpt = new Cryptographer();
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			String crypt_password = crpt.doMd5Hash(username + password);
			
			/*String query = "select user_id, email, password, lastname, firstname, type_id," +
					"(select distinct type_desc from support_type where type_id = support_user.type_id),status_id, user_code," +
					"(select distinct status_desc from support_status where status_id = support_USER.status_id),username,first_logon," +
					"(select account_id from support_account_mapping where support_account_mapping.username = support_user.username) from support_user" +
					" where support_user.username = '"+username+"' AND support_user.password = '"+secure.f_code(password)+"'";
			
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
				user.setStatus_id(""+result.getObject(8));
				user.setUser_code(""+result.getObject(9));
				user.setStatus_nm(""+result.getObject(10));
				user.setUsername(""+result.getObject(11));
				user.setFirst_logon(""+result.getObject(12));
				user.setAccount_id("");
			}*/
			String query = "select user_id, email, password, lastname, firstname, type_id," +
			"(select distinct type_desc from telcodb..support_type where type_id = support_user.type_id),status_id, user_code," +
			"(select distinct status_desc from telcodb..support_status where status_id = support_USER.status_id),username,first_logon," +
			" service_id, esa_auth, bankApp, mobile, (select card_to_credit from company where support_user.service_id = company.compCode)," +
			" (select bank from company where support_user.service_id = company.compCode), (select bankAccount from company where support_user.service_id = company.compCode), " +
			" card_scheme,cardscheme_numbers from telcodb..support_user" +
			" where telcodb..support_user.username = '"+username+"' AND telcodb..support_user.password = '"+crypt_password+"'";
	
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
				user.setStatus_id(""+result.getObject(8));
				user.setUser_code(""+result.getObject(9));
				user.setStatus_nm(""+result.getObject(10));
				user.setUsername(""+result.getObject(11));
				user.setFirst_logon(""+result.getObject(12));
				user.setService_id(""+result.getObject(13));
				user.setAccount_id("");
				user.setEsa_type(""+result.getObject(14));
				user.setBankApp(""+result.getObject(15));
				user.setMobile(""+result.getObject(16));
				user.setCardToCredit(""+result.getObject(17));
				user.setBankToCredit(""+result.getObject(18));
				user.setBankAccountToCredit(""+result.getObject(19));
				user.setCardScheme(""+result.getString(20));
				user.setCardSchemeNumbers(""+result.getObject(21));
			}
			
			
			return user;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionSupportLogDB(con, result);
			return null;
		}
		
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
	}
	
	
	public String decodePassword(String password)
	{
		String message = "";
		try
		{
			ecard.E_SECURE secure = new E_SECURE();
			
			message = secure.f_decode(password);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		return message;
	}
	
	public String changePassword(String user_id, String old_password, String password, String username)
	{
		int output = -1;
		String message = "";
		//ecard.E_SECURE secure = new E_SECURE();
		Cryptographer crpt = new Cryptographer();
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			String oldcrypt_password = crpt.doMd5Hash(username + old_password);
			String crypt_password = crpt.doMd5Hash(username + password);
			
			result = stat.executeQuery("select * from telcodb.dbo.support_user where password = '"+ oldcrypt_password +"' and user_id = "+user_id+"");
			if(result.next())
			{
				output = stat.executeUpdate("update telcodb.dbo.support_user set password = '"+ crypt_password +"',first_logon = '1' where user_id = "+user_id+"");
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
			else
			{
				message = "Old password not valid";
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			try
			{
				con.rollback();
				message = "Error occured while updating user password";
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
