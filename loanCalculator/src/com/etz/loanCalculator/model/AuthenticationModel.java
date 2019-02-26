/**
 * 
 */
package com.etz.loanCalculator.model;



import java.sql.*;
import java.util.ArrayList;

import com.etz.loanCalculator.dto.User;
import com.etz.loanCalculator.utility.Env;
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
		
		Cryptographer crpt = new Cryptographer();
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			String crypt_password = crpt.doMd5Hash(username + password);
			
			String query = "select user_id, email, password, lastname, firstname, type_id," +
			"(select distinct type_desc from telcodb..loan_type where type_id = loan_user.type_id),status_id, user_code," +
			"(select distinct status_desc from telcodb..loan_status where status_id = loan_user.status_id),username,first_logon," +
			" esa_auth, bankApp, mobile, (select houseLogo from telcodb..loan_house where code = loan_user.user_code)," +
			" (select exposure from telcodb..loan_house where code = loan_user.user_code)," +
			" (select id from telcodb..loan_house where code =  loan_user.user_code) " +
			" from telcodb..loan_user" +
			" where telcodb..loan_user.username = '"+username+"' AND telcodb..loan_user.password = '"+crypt_password+"'";
	
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
				user.setEsa_type(""+result.getObject(13));
				user.setBankApp(""+result.getObject(14));
				user.setMobile(""+result.getObject(15));
				user.setLoanLogo(result.getString(16));
				user.setExposure(result.getString(17));
				user.setLendingHouseId(""+result.getObject(18));
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
			
			result = stat.executeQuery("select * from telcodb.dbo.loan_user where password = '"+ oldcrypt_password +"' and user_id = "+user_id+"");
			if(result.next())
			{
				output = stat.executeUpdate("update telcodb.dbo.loan_user set password = '"+ crypt_password +"',first_logon = '1' where user_id = "+user_id+"");
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
