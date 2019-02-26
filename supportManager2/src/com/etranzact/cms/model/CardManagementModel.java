/**
 * 
 */
package com.etranzact.cms.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.etranzact.psm.dto.TPsmDealer;
import com.etranzact.supportmanager.dto.Bank;
import com.etranzact.supportmanager.dto.COP_FUNDGATE_LOG;
import com.etranzact.supportmanager.dto.C_TRANSACTION;
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

import com.etranzact.supportmanager.model.ReportModel;
import com.etranzact.supportmanager.utility.Env;
import com.etranzact.supportmanager.utility.HashNumber;
import com.etz.security.util.Cryptographer;
import com.etranzact.cms.dto.CMSSERVER;
import com.etranzact.cms.dto.CardControl;
import com.etranzact.cms.dto.CardHolder;
import com.etranzact.cms.dto.CardType;
import com.etranzact.cms.dto.FundGateInfo;
import com.etranzact.cms.dto.E_TRANSACTION;
import com.etranzact.cms.dto.SchemeRegistration;
import com.etranzact.institution.dto.Course;
import com.etranzact.institution.dto.Payment;
import com.etranzact.institution.util.Sequencer;




/**
 * @author tony.ezeanya
 *
 */
public class CardManagementModel 
{

	public CardManagementModel()
	{}
	
	/*
	 * 
	 * This method is to authorised Hotlisted Card
	 */
	public String Authorized_Hotlisted_Card(String cardNum,String initatorUsername,String initiatorIp,String authStatus)
	{
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int output = -1 ;
		int output1 = -1 ;
		int output2 = -1 ;
		String message = "";
		String query = "";
		String query1 = "";
		String query2 = "";
		String query3 = "";
		String query4 = "";
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
	
			query = "select * from e_update_authlog where Card_num = '"+cardNum+"' AND UPD_COLUMN_NAME = 'USER_HOTLIST' AND CURRENT_STATUS = '1' ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "exists";
			}
			else
			{
				
			query1 = "update e_cardholder set User_hotlist = '1' where card_num = '"+cardNum+"' ";
		    
			
			query2 = "insert into e_update_authlog(CARD_NUM, TABLE_NAME,TABLE_KEY,UPD_COLUMN_NAME,INITIATOR_USERNAME, INITIATOR_IP,AUTH_STATUS,AUTH_DATE," +
					"CREATED,TABLE_CACHE_NAME,AUTHORISER_USERNAME,AUTHORISER_IP,PREV_STATUS,CURRENT_STATUS)" +
					"values('"+cardNum+"', 'E_CARDHOLDER', 'CARD_NUM','USER_HOTLIST','"+initatorUsername+"','"+initiatorIp+"','"+authStatus+"',getDate()," +
					" getDate(),'eCardDBCache','"+initatorUsername+"','"+initiatorIp+"','0','1')";
			
			query3 = "update E_CARD_PENDING_AUTH set hotlist_status = '0', user_hotlist = '1' where card_num = '"+cardNum+"' ";
			
			//query4 = "update e_cardholder set User_hotlist = '1' where card_num = '"+cardNum+"' ";
			//query4 = "delete from E_CARD_PENDING_AUTH where card_num = '"+cardNum+"'";
			
			output = stat.executeUpdate(query1);
			output1 = stat.executeUpdate(query2);
			output2 = stat.executeUpdate(query3);
			System.out.println("query and output   "+output);
			
           if(output > 0)
           {
        	  
        	   message = "SUCCESS";
        	   con.commit();
           }
           else
           {
        	   message = "Failed";
        	   con.rollback();
           }
           System.out.println("query update CardHolder   "+output);
			
		 }
		 
	
		}catch(Exception ex)
		{
			closeConnectionECard(con, result, result);
			
		}
		finally
		{
			closeConnectionECard(con, result, result);
			
		}
		
		return message;
	}

	public String Hotlisted_Card(String cardNum,String initatorUsername,String initiatorIp,String authStatus)
	{
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int output = -1 ;
		int output1 = -1 ;
		int output2 = -1 ;
		String message = "";
		String query = "";
		String query1 = "";
		String query2 = "";
		String query3 = "";
		String query4 = "";
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
	
		/*	query = "select * from e_update_authlog where Card_num = '"+cardNum+"' AND UPD_COLUMN_NAME = 'USER_HOTLIST' AND CURRENT_STATUS = '1' ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "exists";
			}
			else
			{*/
				
			query1 = "update e_cardholder set User_hotlist = '1' where card_num = '"+cardNum+"' ";
		    
			
			query2 = "insert into e_update_authlog(CARD_NUM, TABLE_NAME,TABLE_KEY,UPD_COLUMN_NAME,INITIATOR_USERNAME, INITIATOR_IP,AUTH_STATUS,AUTH_DATE," +
					"CREATED,TABLE_CACHE_NAME,AUTHORISER_USERNAME,AUTHORISER_IP,PREV_STATUS,CURRENT_STATUS)" +
					"values('"+cardNum+"', 'E_CARDHOLDER', 'CARD_NUM','USER_HOTLIST','"+initatorUsername+"','"+initiatorIp+"','"+authStatus+"',getDate()," +
					" getDate(),'eCardDBCache','"+initatorUsername+"','"+initiatorIp+"','0','1')";
			
			query3 = "update E_CARD_PENDING_AUTH set hotlist_status = '0', user_hotlist = '1' where card_num = '"+cardNum+"' ";
			
					
			output = stat.executeUpdate(query1);
			
			System.out.println("query and output   "+output);
           if(output > 0)
           {
        	  
        	   message = "SUCCESS";
        	   output1 = stat.executeUpdate(query2);
   			   output2 = stat.executeUpdate(query3);
        	   con.commit();
           }
           else
           {
        	   message = "Failed";
        	   con.rollback();
           }
           System.out.println("query update CardHolder   "+output);
			
		// }
		 
	
		}catch(Exception ex)
		{
			closeConnectionECard(con, result, result);
			
		}
		finally
		{
			closeConnectionECard(con, result, result);
			
		}
		
		return message;
	}
	
	
	/*
	 * 
	 * This method is to authorised DeHotlisted Card
	 */
	public String getAuthorized_DeHotlisted_Card(String dbServer, String cardNum,String initatorUsername,String initiatorIp,String authStatus)
	{
		
		Connection con = null;
		Statement stat = null;
		Connection con1 = null;
		Statement stat1 = null;
		ResultSet result = null;
		int output = -1 ;
		int output1 = -1 ;
		int output2 = -1 ;
		String message = "";
		String query = "";
		String query1 = "";
		String query2 = "";
		String query3 = "";
		
		try
		{
			

			con1 = connectToPMEcardholder();   
			stat1 = con1.createStatement();
			
			
			if(dbServer.equals("1"))  // .57
			{
				
				con = connectToTestECard();   
				stat = con.createStatement();
			}
			else if(dbServer.equals("2"))    // .133
			{
				con = connectToPMEcardholder();
				stat = con.createStatement();
				
			}
			else if(dbServer.equals("3"))  //  .35
			{
				con = connectToECard();
				stat = con.createStatement();	
			}
		
				
			query = "update e_cardholder set User_hotlist = '0',user_locked = '0' where card_num = '"+cardNum+"' ";
		    
			
			query1 = "insert into e_update_authlog(CARD_NUM, TABLE_NAME,TABLE_KEY,UPD_COLUMN_NAME,INITIATOR_USERNAME, INITIATOR_IP,AUTH_STATUS,AUTH_DATE," +
					"CREATED,TABLE_CACHE_NAME,AUTHORISER_USERNAME,AUTHORISER_IP,PREV_STATUS,CURRENT_STATUS)" +
					"values('"+cardNum+"', 'E_CARDHOLDER', 'CARD_NUM','USER_HOTLIST/USER_LOCKED','"+initatorUsername+"','"+initiatorIp+"','"+authStatus+"',getDate()," +
					" getDate(),'eCardDBCache','"+initatorUsername+"','"+initiatorIp+"','1','0')";
			
			query2 = "update E_CARD_PENDING_INFO set dehotlist ='0' where card_num = '"+cardNum+"' ";
			
			System.out.println("query    "+query);	
			System.out.println("query1    "+query1);
			System.out.println("query2    "+query2);
			
			output = stat.executeUpdate(query);
			System.out.println("query and output   "+output);	
           if(output > 0)
           {
        	  
        	    message = "Record has been successfully authorised ";
        		output1 = stat1.executeUpdate(query1);
    			output2 = stat1.executeUpdate(query2);
        	    con.commit();
           }
           else
           {
        	   message = "Unable to authorised";
        	   con.rollback();
           }
           System.out.println("query update CardHolder   "+output);
			
		// }
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTestECard(con, result, result);
			//closeConnectionPocketMoniEcardDB(con, result);
			closeConnectionECard(con, result, result);
			closeconnectToPMEcardholder(con, result);
			
		}
		finally
		{
			closeConnectionTestECard(con, result, result);
			//closeConnectionPocketMoniEcardDB(con, result);
			closeConnectionECard(con, result, result);;
			closeconnectToPMEcardholder(con, result);
			
			
		}
		
		return message;
	}
	
	
	public String getAuthorizedPinRegeneration(String dbServer, String cardNum,String initatorUsername,String initiatorIp,String authStatus)
	{
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int output = -1 ;
		int output1 = -1 ;
		int output2 = -1 ;
		String message = "";
		String query = "";
		String query1 = "";
		String query2 = "";
		String query3 = "";
		
		try
		{
			if(dbServer.equals("1"))  // .57
			{
				
				con = connectToTestECard();   
				stat = con.createStatement();
			}
			else if(dbServer.equals("2"))    // .133
			{
				con = connectPocketMoniEcardDB();
				stat = con.createStatement();
				
			}
			else if(dbServer.equals("3"))  //  .35
			{
				con = connectToECard();
				stat = con.createStatement();	
			}
		
				
			query = "update e_cardholder set Change_Pin = '0'  where card_num = '"+cardNum+"' ";
		    
			
			query1 = "insert into e_update_authlog(CARD_NUM, TABLE_NAME,TABLE_KEY,UPD_COLUMN_NAME,INITIATOR_USERNAME, INITIATOR_IP,AUTH_STATUS,AUTH_DATE," +
					"CREATED,TABLE_CACHE_NAME,AUTHORISER_USERNAME,AUTHORISER_IP,PREV_STATUS,CURRENT_STATUS)" +
					"values('"+cardNum+"', 'E_CARDHOLDER', 'CARD_NUM','CHANGE_PIN','"+initatorUsername+"','"+initiatorIp+"','"+authStatus+"',getDate()," +
					" getDate(),'eCardDBCache','"+initatorUsername+"','"+initiatorIp+"','1','0')";
			
			query2 = "update E_CARD_PENDING_INFO set pin_reset ='0' where card_num = '"+cardNum+"' ";
			
			output = stat.executeUpdate(query);
			System.out.println("query and output   "+output);	
           if(output > 0)
           {
        	  
        	    message = "Record has been successfully authorised ";
        		output1 = stat.executeUpdate(query1);
    			output2 = stat.executeUpdate(query2);
        	    con.commit();
           }
           else
           {
        	   message = "Unable to authorised";
        	   con.rollback();
           }
           System.out.println("query update CardHolder   "+output);
			
		
			
		}catch(Exception ex)
		{
			closeConnectionTestECard(con, result, result);
			closeConnectionPocketMoniEcardDB(con, result);
			closeConnectionECard(con, result, result);;
			
		}
		finally
		{
			closeConnectionTestECard(con, result, result);
			closeConnectionPocketMoniEcardDB(con, result);
			closeConnectionECard(con, result, result);;
			
			
		}
		
		return message;
	}
	
	/*
	 * Method to Authorized reset Bound work
	 */
	public String getAuthorized_Reset_Bound_work(String cardNum)
	{
		
		Connection con = null;
		Statement stat = null;
		
		Connection con1 = null;
		Statement stat1 = null;
		ResultSet result = null;
		int output = -1 ;
		int output1 = -1 ;
		String query = "";
		String message = "";

		
		try
		{
		
			con = connectToPMEcardholder();
			stat = con.createStatement();
	
			
			query = "Select * from E_CARDTRANS where card_num = '"+cardNum+"' ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				query = "delete from E_CARDTRANS where card_num = '"+cardNum+"'";
				output = stat.executeUpdate(query);
			
	          	if(output > 0)
	            {
	        	   message = "Record has been successfully authorized  ";
	        	   output = stat.executeUpdate("update e_card_pending_info set reset_bound_work = '0' where card_num = '"+cardNum+"'");
	      	       con.commit();
	            }
	            else
	            {
	        	   message = " Unable to authorized !";
	        	   con.rollback();
	            }
				
			}
			else
			{
				
				message = "Record has been successfully authorized ";
				
				output = stat.executeUpdate("update e_card_pending_info  set reset_bound_work = '0' where card_num = '"+cardNum+"'");
				 con.commit();
				
			}
			

		}catch(Exception ex)
		{
			closeConnectionECard(con, result, result);
			closeconnectToPMEcardholder(con, result);
			/*closeConnectionSupportLogDB(con1, result);
			*/
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeconnectToPMEcardholder(con, result);
	/*		closeConnectionSupportLogDB(con1, result);*/
			
		}
		
		return message;
	}
	
	
	

	/*
	 * Method to Authorized reset Bound work
	 */
	public String getAuthorizedResetBoundlimit(String cardNum,String dbServer,String initatorUsername,String initiatorIp, String authStatus,String CurrentBoundLimit,String previousBoudLimit)
	{
		
		Connection con = null;
		Statement stat = null;
		
		Connection con1 = null;
		Statement stat1 = null;
		ResultSet result = null;
		int output = -1 ;
		int output1 = -1 ;
		int output2 = -1 ;
		String query = "";
		String query1 = "";
		String query2 = "";
		String message = "";

		
		try
		{
			con1 = connectToPMEcardholder();
			stat1 = con1.createStatement();
		
			
			if(dbServer.equals("1"))  // .57
			{
				
				con = connectToTestECard();   
				stat = con.createStatement();
			}
			else if(dbServer.equals("2"))    // .133
			{
				 con = connectToPMEcardholder();
				stat = con.createStatement();
				
			}
			else if(dbServer.equals("3"))  //  .35
			{
				con = connectToECard();
				stat = con.createStatement();	
			}
		
			
			
			
			query = "update e_cardholder set bound_value = "+CurrentBoundLimit+"  where card_num = '"+cardNum+"' ";
		    
			
			query1 = "insert into e_update_authlog(CARD_NUM, TABLE_NAME,TABLE_KEY,UPD_COLUMN_NAME,INITIATOR_USERNAME, INITIATOR_IP,AUTH_STATUS,AUTH_DATE," +
					"CREATED,TABLE_CACHE_NAME,AUTHORISER_USERNAME,AUTHORISER_IP,PREV_STATUS,CURRENT_STATUS)" +
					"values('"+cardNum+"', 'E_CARDHOLDER', 'CARD_NUM','BOUND_VALUE','"+initatorUsername+"','"+initiatorIp+"','"+authStatus+"',getDate()," +
					" getDate(),'eCardDBCache','"+initatorUsername+"','"+initiatorIp+"','"+previousBoudLimit+"','"+CurrentBoundLimit+"')";
			
			query2 = "update E_CARD_PENDING_INFO set reset_bound_limit ='0' where card_num = '"+cardNum+"' ";
			
			output = stat.executeUpdate(query);
			System.out.println("query and output   "+output);	
           if(output > 0)
           {
        	  
        	   message = "Record has been successfully authorised ";
        		output1 = stat1.executeUpdate(query1);
    			output2 = stat1.executeUpdate(query2);
        	   con.commit();
           }
           else
           {
        	   message = "Unable to authorised";
        	   con.rollback();
           }
           System.out.println("query update CardHolder   "+output);
			
		// }
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTestECard(con, result, result);
			//closeConnectionPocketMoniEcardDB(con, result);
			closeConnectionECard(con, result, result);
			closeconnectToPMEcardholder(con, result);
			
		}
		finally
		{
			closeConnectionTestECard(con, result, result);
			//closeConnectionPocketMoniEcardDB(con, result);
			closeConnectionECard(con, result, result);
			closeconnectToPMEcardholder(con1, result);
			
			
		}
		
		return message;
	}
	
	/*
	 * Method to Authorized reset Bound work
	 */
	public String unAuthorized_Reset_Bound_work(String cardNum)
	{
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int output = -1 ;
		String message = "";
		String query = "";
	
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "update E_CARD_PENDING_AUTH set Reset_Bound_work = null  where card_num = '"+cardNum+"' ";
		
			System.out.println("query and output   "+output);
			
			output = stat.executeUpdate(query);
           if(output > 0)
           {
        	  
        	   message = "SUCCESS";
        	   con.commit();
           }
           else
           {
        	   message = "Fail";
        	   con.rollback();
           }
           
           System.out.println("query update E_CARD_PENDING_AUTH   "+output);

		}catch(Exception ex)
		{
			closeConnectionECard(con, result, result);
			
		}
		finally
		{
			closeConnectionECard(con, result, result);
			
		}
		
		return message;
	}
	
	/*
	 * Method to UNAuthorized reset Bound Limit
	 */
	public String unAuthorized_Reset_Bound_Limit(String cardNum)
	{
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int output = -1 ;
		String message = "";
		String query = "";
	
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "update E_CARD_PENDING_AUTH set Reset_Bound_limit = null  where card_num = '"+cardNum+"' ";
		
			System.out.println("query and output   "+output);
			
			output = stat.executeUpdate(query);
           if(output > 0)
           {
        	  
        	   message = "SUCCESS";
        	   con.commit();
           }
           else
           {
        	   message = "Fail";
        	   con.rollback();
           }
           
           System.out.println("query update E_CARD_PENDING_AUTH   "+output);

		}catch(Exception ex)
		{
			closeConnectionECard(con, result, result);
			
		}
		finally
		{
			closeConnectionECard(con, result, result);
			
		}
		
		return message;
	}
	
	public String unAuthorized_DEHOTLIST_Card(String dbServer,String cardNum)
	{
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int output = -1 ;
		String message = "";
		String query = "";
	
		try
		{
			if(dbServer.equals("0"))  // .57
			{
				
				con = connectToTestECard();   
				stat = con.createStatement();
			}
			else if(dbServer.equals("1"))    // .133
			{
				con = connectPocketMoniEcardDB();
				stat = con.createStatement();
				
			}
			else if(dbServer.equals("2"))  //  .35
			{
				con = connectToECard();
				stat = con.createStatement();	
			}
		
			query = "update E_CARD_PENDING_AUTH set HOTLIST_STATUS = '0', BLOCK_STATUS = '0',  where card_num = '"+cardNum+"' ";
		
			System.out.println("query and output   "+output);
			
		   output = stat.executeUpdate(query);
           if(output > 0)
           {
        	  
        	   message = "SUCCESS";
        	   con.commit();
           }
           else
           {
        	   message = "Fail";
        	   con.rollback();
           }
           
           System.out.println("query update E_CARD_PENDING_AUTH   "+output);

		}catch(Exception ex)
		{
			closeConnectionECard(con, result, result);
			
		}
		finally
		{
			closeConnectionECard(con, result, result);
			
		}
		
		return message;
	}
	
	
	public String unAuthorized_UNBLOCK_CARD(String cardNum)
	{
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int output = -1 ;
		String message = "";
		String query = "";
	
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "update E_CARD_PENDING_AUTH set BLOCK_STATUS = '1'  where card_num = '"+cardNum+"' ";
		
			System.out.println("query and output   "+output);
			
			output = stat.executeUpdate(query);
           if(output > 0)
           {
        	  
        	   message = "SUCCESS";
        	   con.commit();
           }
           else
           {
        	   message = "Fail";
        	   con.rollback();
           }
           
           System.out.println("query update E_CARD_PENDING_AUTH   "+output);

		}catch(Exception ex)
		{
			closeConnectionECard(con, result, result);
			
		}
		finally
		{
			closeConnectionECard(con, result, result);
			
		}
		
		return message;
	}
	
	
	
	
	/*
	 * Method to UNAuthorized reset Bound Limit
	 */
	public String unAuthorized_Extend_CardExpiration(String cardNum)
	{
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int output = -1 ;
		String message = "";
		String query = "";
	
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "update E_CARD_PENDING_AUTH set Extend_Expiration = null  where card_num = '"+cardNum+"' ";
		
			System.out.println("query and output   "+output);
			
			output = stat.executeUpdate(query);
           if(output > 0)
           {
        	  
        	   message = "SUCCESS";
        	   con.commit();
           }
           else
           {
        	   message = "Fail";
        	   con.rollback();
           }
           
           System.out.println("query update E_CARD_PENDING_AUTH   "+output);

		}catch(Exception ex)
		{
			closeConnectionECard(con, result, result);
			
		}
		finally
		{
			closeConnectionECard(con, result, result);
			
		}
		
		return message;
	}
	
	
	
	/*
	 * Method to Authorized reset Bound work
	 */
	public String UNAuthorized_Reset_Bound_work(String cardNum,String initatorUsername,String initiatorIp,String authStatus,String boundWork,String prevBoundWork)
	{
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int output = -1 ;
		int output1 = -1 ;
		int output2 = -1 ;
		String message = "";
		String query = "";
		String query1 = "";
		String query2 = "";
		String query3 = "";
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			

			
			query1 = "update e_cardholder set Bound_work = "+boundWork+" where card_num = '"+cardNum+"' ";
		   
			query2 = "insert into e_update_authlog(CARD_NUM, TABLE_NAME,TABLE_KEY,UPD_COLUMN_NAME,INITIATOR_USERNAME, INITIATOR_IP,AUTH_STATUS,AUTH_DATE," +
					"CREATED,TABLE_CACHE_NAME,AUTHORISER_USERNAME,AUTHORISER_IP,PREV_STATUS,CURRENT_STATUS)" +
					"values('"+cardNum+"', 'E_CARDHOLDER', 'CARD_NUM','BOUND_WORK','"+initatorUsername+"','"+initiatorIp+"','"+authStatus+"',getDate()," +
					" getDate(),'eCardDBCache','"+initatorUsername+"','"+initiatorIp+"','"+prevBoundWork+"','"+boundWork+"')";
			
			query3 = "update E_CARD_PENDING_AUTH set Reset_Bound_work = null where card_num = '"+cardNum+"' ";
		
			output = stat.executeUpdate(query1);
		
			System.out.println("query and output   "+output);
			
           if(output > 0)
           {
        	  
        	   message = "SUCCESS";
        	   output1 = stat.executeUpdate(query2);
        	   output2 = stat.executeUpdate(query3);
        	   con.commit();
           }
           else
           {
        	   message = "Fail";
        	   con.rollback();
           }
           System.out.println("query update CardHolder   "+output);
			
		 
			
		}catch(Exception ex)
		{
			closeConnectionECard(con, result, result);
			
		}
		finally
		{
			closeConnectionECard(con, result, result);
			
		}
		
		return message;
	}
	
	
	/*
	 * Method to Authorized card Expiration
	 */
	public String Authorized_Extend_CardExpiration(String dbServer,String cardNum,String initatorUsername,String initiatorIp,String authStatus,String cardExtended,String prevExpire)
	{
		
		Connection con = null;
		Statement stat = null;
		Connection con1 = null;
		Statement stat1 = null;
		ResultSet result = null;
		int output = -1 ;
		int output1 = -1 ;
		int output2 = -1 ;
		String message = "";
		String query = "";
		String query1 = "";
		String query2 = "";
		String query3 = "";
		
		try
		{
				con1 = connectToPMEcardholder();
				stat1 = con1.createStatement();
				
				
			
				if(dbServer.equals("1"))  // .57
				{
					con = connectToTestECard();   
					stat = con.createStatement();
				}
				else if(dbServer.equals("2"))    // .133
				{
					con = connectToPMEcardholder();
					stat = con.createStatement();
				
				}
				else if(dbServer.equals("3"))  //  .35
				{
					con = connectToECard();
					stat = con.createStatement();	
				}
		

			
			query = "update e_cardholder set Card_Expiration = '"+cardExtended+"' where card_num = '"+cardNum+"' ";
		   
			query1 = "insert into e_update_authlog(CARD_NUM, TABLE_NAME,TABLE_KEY,UPD_COLUMN_NAME,INITIATOR_USERNAME, INITIATOR_IP,AUTH_STATUS,AUTH_DATE," +
					"CREATED,TABLE_CACHE_NAME,AUTHORISER_USERNAME,AUTHORISER_IP,PREV_STATUS,CURRENT_STATUS)" +
					"values('"+cardNum+"', 'E_CARDHOLDER', 'CARD_NUM','CARD_EXPIRATION','"+initatorUsername+"','"+initiatorIp+"','"+authStatus+"',getDate()," +
					" getDate(),'eCardDBCache','"+initatorUsername+"','"+initiatorIp+"','"+prevExpire+"','"+cardExtended+"')";
			
			query2 = "update E_CARD_PENDING_INFO set Extend_Expiration ='0' where card_num = '"+cardNum+"' ";
		
			output = stat.executeUpdate(query);
		
			System.out.println("query and output   "+output);
			
           if(output > 0)
           {
        	  
        	   message = "SUCCESS";
        	   output1 = stat1.executeUpdate(query1);
        	   output2 = stat1.executeUpdate(query2);
        	   con.commit();
           }
           else
           {
        	   message = "Fail";
        	   con.rollback();
           }
           System.out.println("query update CardHolder   "+output);
			
		 
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTestECard(con, result, result);
			//closeConnectionPocketMoniEcardDB(con, result);
			closeConnectionECard(con, result, result);
			closeconnectToPMEcardholder(con, result);
			
		}
		finally
		{
			closeConnectionTestECard(con, result, result);
			//closeConnectionPocketMoniEcardDB(con, result);
			closeConnectionECard(con, result, result);
			closeconnectToPMEcardholder(con, result);
			
		}
		
		return message;
	}
	
	
	
	
	
	/*
	 * Method to Authorized reset Bound Limit
	 */
	public String Authorized_Reset_Bound_Limit(String cardNum,String initatorUsername,String initiatorIp,String authStatus,String boundLimit,String prevBoundLimit)
	{
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int output = -1 ;
		int output1 = -1 ;
		int output2 = -1 ;
		String message = "";
		String query = "";
		String query1 = "";
		String query2 = "";
		String query3 = "";
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query1 = "update e_cardholder set Bound_value = "+boundLimit+" where card_num = '"+cardNum+"' ";
		   
			query2 = "insert into e_update_authlog(CARD_NUM, TABLE_NAME,TABLE_KEY,UPD_COLUMN_NAME,INITIATOR_USERNAME, INITIATOR_IP,AUTH_STATUS,AUTH_DATE," +
					"CREATED,TABLE_CACHE_NAME,AUTHORISER_USERNAME,AUTHORISER_IP,PREV_STATUS,CURRENT_STATUS)" +
					"values('"+cardNum+"', 'E_CARDHOLDER', 'CARD_NUM','BOUND_VALUE','"+initatorUsername+"','"+initiatorIp+"','"+authStatus+"',getDate()," +
					" getDate(),'eCardDBCache','"+initatorUsername+"','"+initiatorIp+"','"+prevBoundLimit+"','"+boundLimit+"')";
			
			query3 = "update E_CARD_PENDING_AUTH set Reset_Bound_limit = null where card_num = '"+cardNum+"' ";
		
			output = stat.executeUpdate(query1);
		
			System.out.println("query and output   "+output);
			
           if(output > 0)
           {
        	  
        	   message = "SUCCESS";
        	   output1 = stat.executeUpdate(query2);
        	   output2 = stat.executeUpdate(query3);
        	   con.commit();
           }
           else
           {
        	   message = "Fail";
        	   con.rollback();
           }
           System.out.println("query update CardHolder   "+output);
			
		 
			
		}catch(Exception ex)
		{
			closeConnectionECard(con, result, result);
			
		}
		finally
		{
			closeConnectionECard(con, result, result);
			
		}
		
		return message;
	}
	
	
	
	/*
	 * 
	 * This is to method is to blocked/hotlisted a card 
	 */
	public String Block_Hotlist_Card(String dbServer,String cardNum,String initatorUsername,String initiatorIp,String authStatus)
	{
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int output = -1 ;
		int output1 = -1 ;
		int output2 = -1 ;
		String message = "";
		String query = "";
		String query1 = "";
		String query2 = ""; 
		String query3 = "";
		
		try
		{
			
			
			if(dbServer.equals("1"))  // .57
			{
				
				con = connectToTestECard();   
				stat = con.createStatement();
				
		
			}
			else if(dbServer.equals("2"))    // .133
			{
				//con = connectPocketMoniEcardDB();
				con = connectToPMEcardholder();
				stat = con.createStatement();
			
				
			}
			else if(dbServer.equals("3"))  //  .130
			{
				con = connectToECard();
				stat = con.createStatement();	
			
				
			}
	
		
			
			query ="select * from e_cardholder where card_num = '"+cardNum+"' and user_locked = '1' and user_hotlist = '1' ";
			
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "exists";
			}
			else
			{
				
				query1 = "update e_cardholder set User_locked = '1' , user_hotlist = '1' where card_num = '"+cardNum+"' ";
			    
				
				query2 = "insert into e_update_authlog(CARD_NUM, TABLE_NAME,TABLE_KEY,UPD_COLUMN_NAME,INITIATOR_USERNAME, INITIATOR_IP,AUTH_STATUS,AUTH_DATE," +
						"CREATED,TABLE_CACHE_NAME,AUTHORISER_USERNAME,AUTHORISER_IP,PREV_STATUS,CURRENT_STATUS)" +
						"values('"+cardNum+"', 'E_CARDHOLDER', 'CARD_NUM','USER_LOCKED / USER_HOTLIST','"+initatorUsername+"','"+initiatorIp+"','"+authStatus+"',getDate()," +
						" getDate(),'eCardDBCache','"+initatorUsername+"','"+initiatorIp+"','0','1')";
				
				/*query3 = "update E_CARD_PENDING_AUTH set block_status = '1',hotlist_status ='1' where card_num = '"+cardNum+"' ";*/
				
				output = stat.executeUpdate(query1);
				
				System.out.println("query and output   "+output);
			
		          if(output > 0)
		          {
		       	  
		       	   message = "SUCCESS";
		       	   output1 = stat.executeUpdate(query2);
		       	  /* output2 = stat.executeUpdate(query3);*/
		       	   con.commit();
		          }
		          else
		          {
		       	   message = "Fail";
		       	   con.rollback();
		          }
		          System.out.println("query update CardHolder   "+output);
		   }
          
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
			closeconnectToPMEcardholder(con, result);
			//closeConnectionPocketMoniEcardDB(con, result);
			closeConnectionTestECard(con, result, result);
			
			
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeconnectToPMEcardholder(con, result);
			//closeConnectionPocketMoniEcardDB(con, result);
			closeConnectionTestECard(con, result, result);
			
		}
		
		return message;
	}
	
	
	/*
	 * 
	 * This is to authorized block Card
	 */
	public String Authorized_UNBlOCK_CARD(String cardNum,String initatorUsername,String initiatorIp,String authStatus)
	{
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int output = -1 ;
		int output1 = -1 ;
		int output2 = -1 ;
		String message = "";
		String query = "";
		String query1 = "";
		String query2 = ""; 
		String query3 = "";
		
		try
		{
					con = connectToECard();
					stat = con.createStatement();
			
	
					query = "update e_cardholder set USER_LOCKED = '0' where card_num = '"+cardNum+"' ";
					
					query2 = "insert into e_update_authlog(CARD_NUM, TABLE_NAME,TABLE_KEY,UPD_COLUMN_NAME,INITIATOR_USERNAME, INITIATOR_IP,AUTH_STATUS,AUTH_DATE," +
							"CREATED,TABLE_CACHE_NAME,AUTHORISER_USERNAME,AUTHORISER_IP,PREV_STATUS,CURRENT_STATUS)" +
							"values('"+cardNum+"', 'E_CARDHOLDER', 'CARD_NUM','USER_LOCKED','"+initatorUsername+"','"+initiatorIp+"','"+authStatus+"',getDate()," +
							" getDate(),'eCardDBCache','"+initatorUsername+"','"+initiatorIp+"','1','0')";
					
				
					query3 = "update E_CARD_PENDING_AUTH set block_status = '1', user_locked='0' where card_num = '"+cardNum+"' ";
				
			
					output = stat.executeUpdate(query);
					System.out.print("update query "+query1+" output value -- 3 "+output1);
			          if(output > 0)
			          {
					       	   message = "SUCCESS";
					        	output1 = stat.executeUpdate(query2);
								output2 = stat.executeUpdate(query3);
					       	   con.commit();
					      
			          }
			          else
			          {
					       	   message = "Fail";
					       	   con.rollback();
			          }

			//}
		}
		catch(Exception ex)
		{
			closeConnectionECard(con, result, result);
			
		}
		finally
		{
			closeConnectionECard(con, result, result);
			
		}
		
		return message;
	}
		/*
	 * 
	 * This method is to get card holder information
	 */
	public ArrayList getCardHolderEnquires(String dbServer,String card_num,String cardschemenumber,String phone,String startDat,String endDat)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION tran = null;
		CardHolder holder = null;
		Connection con = null;
		Statement stat = null;
		
		Connection con1 = null;
		Statement stat1 = null;
		ResultSet result = null;
		String apostrophe = "'";
		String message = "";
	
		String mobile = "";
		double d = 0.0;
	
		
		try
		{
			
			if(cardschemenumber.indexOf(":")>0)
			{
				String m[] = cardschemenumber.split(":");
				cardschemenumber = "";
				for(int i=0;i<m.length;i++)
				{
					cardschemenumber += apostrophe + m[i] + apostrophe + ",";
				}
				
				cardschemenumber = cardschemenumber.substring(0, cardschemenumber.lastIndexOf(","));
			}
			else
			{
				cardschemenumber = apostrophe + cardschemenumber + apostrophe;
			}
			
		
				if(dbServer.equals("1"))  // .57
				{
					
					con = connectToTestECard();   
					stat = con.createStatement();
					
				}
				else if(dbServer.equals("2"))    // .133
				{
					con = connectPocketMoniEcardDB();
					stat = con.createStatement();
				
					
				}
				else if(dbServer.equals("3"))  //  .130
				{
					con = connectToECard();
					stat = con.createStatement();	
				}
				
				
				if(card_num.length() > 0)
				{
					query = "select card_num,Lastname,Firstname,issuer_code,Email,Phone,Street,card_expiration,bound_work," +
				    		"bound_value,Birth_Date,Change_Pin,User_Hotlist,User_Locked,Pin_Missed,Last_Used,Modified,Created," +
				    		"Online_Balance,PayFee,CASHWTHDW_LIMIT,Client_ID from E_CARDHOLDER  where card_num = '"+card_num+"'" +
				    		"and substring(card_num,1,6) in("+cardschemenumber+") ";
					
					System.out.println("Cardnum query -- > "+query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						
							holder = new CardHolder();
							holder.setCard_num(""+result.getObject(1));
							holder.setLastname(""+result.getObject(2));
							holder.setFirstname(""+result.getObject(3));
							holder.setIssuer_code(""+result.getObject(4));
							holder.setEmail(""+result.getObject(5));
							holder.setPhone(""+result.getObject(6));
							holder.setStreet(""+result.getObject(7));
							holder.setCard_expiration(""+result.getObject(8));
							holder.setBound_work(""+result.getObject(9));
							holder.setBound_value(""+result.getObject(10));
							holder.setBirth_date(""+result.getObject(11));
							holder.setChange_pin(""+result.getObject(12));
							holder.setUser_hotlist(""+result.getObject(13));
							holder.setUser_locked(""+result.getObject(14));
							holder.setPin_missed(""+result.getObject(15));
							holder.setLast_used(""+result.getObject(16));
							holder.setModified(""+result.getObject(17));
							holder.setCreated(""+result.getObject(18));
							holder.setHotlistStatus("0");
							holder.setBlockStatus("0");
							holder.setOnline_balance(""+result.getObject(19));
							holder.setPayfee(""+result.getObject(20));
							holder.setCashwthdw_limit(""+result.getObject(21));
							holder.setClient_id(""+result.getObject(22));
							arr.add(holder);
							
					     }
					
				}
				else if(phone.length() > 0)
				{
					
					query = "select card_num from E_CARDHOLDER where phone = '"+phone+"' ";
					result = stat.executeQuery(query);
					if(result.next())
					{
						card_num = ""+result.getObject(1);
						
						query = "select card_num,Lastname,Firstname,issuer_code,Email,Phone,Street,card_expiration,bound_work," +
					    		"bound_value,Birth_Date,Change_Pin,User_Hotlist,User_Locked,Pin_Missed,Last_Used,Modified,Created," +
					    		"Online_Balance,PayFee,CASHWTHDW_LIMIT,Client_ID from E_CARDHOLDER  where card_num = '"+card_num+"'" +
					    		"and substring(card_num,1,6) in("+cardschemenumber+") ";
						
						System.out.println("mobile number query -- > "+query);
						result = stat.executeQuery(query);
						while(result.next())
						{
							
								holder = new CardHolder();
								holder.setCard_num(""+result.getObject(1));
								holder.setLastname(""+result.getObject(2));
								holder.setFirstname(""+result.getObject(3));
								holder.setIssuer_code(""+result.getObject(4));
								holder.setEmail(""+result.getObject(5));
								holder.setPhone(""+result.getObject(6));
								holder.setStreet(""+result.getObject(7));
								holder.setCard_expiration(""+result.getObject(8));
								holder.setBound_work(""+result.getObject(9));
								holder.setBound_value(""+result.getObject(10));
								holder.setBirth_date(""+result.getObject(11));
								holder.setChange_pin(""+result.getObject(12));
								holder.setUser_hotlist(""+result.getObject(13));
								holder.setUser_locked(""+result.getObject(14));
								holder.setPin_missed(""+result.getObject(15));
								holder.setLast_used(""+result.getObject(16));
								holder.setModified(""+result.getObject(17));
								holder.setCreated(""+result.getObject(18));
								holder.setHotlistStatus("0");
								holder.setBlockStatus("0");
								holder.setOnline_balance(""+result.getObject(19));
								holder.setPayfee(""+result.getObject(20));
								holder.setCashwthdw_limit(""+result.getObject(21));
								holder.setClient_id(""+result.getObject(22));
								arr.add(holder);
								
						     }
						}
					}
					else
					{
						message = "Unknown";
					}
					
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPocketMoniEcardDB(con, result);
			closeConnectionTestECard(con, result, result);
			closeConnectionECard(con, result, result);
	
		}
		finally
		{
			closeConnectionPocketMoniEcardDB(con, result);
			closeConnectionTestECard(con, result, result);
			closeConnectionECard(con, result, result);
	
			
		}
		return arr;
		
	}
	
	
	public ArrayList getCardHolderEnquiresTrack2(String dbServer,String track2)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION tran = null;
		CardHolder holder = null;
		Connection con = null;
		Statement stat = null;
		
		Connection con1 = null;
		Statement stat1 = null;
		ResultSet result = null;
		String apostrophe = "'";
	
		String mobile = "";
		double d = 0.0;
	
		
		try
		{
			
				
				if(dbServer.equals("1"))  // .57
				{
					
					con = connectToTestECard();   
					stat = con.createStatement();
					
			
				}
				else if(dbServer.equals("2"))    // .133
				{
					con = connectPocketMoniEcardDB();
					stat = con.createStatement();
				
					
				}
				else if(dbServer.equals("3"))  //  .130
				{
					con = connectToECard();
					stat = con.createStatement();	
				}
				
				
		
					query = "select card_num,Lastname,Firstname,issuer_code,Email,Phone,Street,card_expiration,bound_work," +
				    		"bound_value,Birth_Date,Change_Pin,User_Hotlist,User_Locked,Pin_Missed,Last_Used,Modified,Created," +
				    		"Online_Balance,PayFee,CASHWTHDW_LIMIT,Client_ID from E_CARDHOLDER  where track2 = '"+track2+"' ";
		
				
						
				System.out.println("getCardHolderEnquires scheme Option   " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					
						holder = new CardHolder();
						holder.setCard_num(""+result.getObject(1));
						holder.setLastname(""+result.getObject(2));
						holder.setFirstname(""+result.getObject(3));
						holder.setIssuer_code(""+result.getObject(4));
						holder.setEmail(""+result.getObject(5));
						holder.setPhone(""+result.getObject(6));
						holder.setStreet(""+result.getObject(7));
						holder.setCard_expiration(""+result.getObject(8));
						holder.setBound_work(""+result.getObject(9));
						holder.setBound_value(""+result.getObject(10));
						holder.setBirth_date(""+result.getObject(11));
						holder.setChange_pin(""+result.getObject(12));
						holder.setUser_hotlist(""+result.getObject(13));
						holder.setUser_locked(""+result.getObject(14));
						holder.setPin_missed(""+result.getObject(15));
						holder.setLast_used(""+result.getObject(16));
						holder.setModified(""+result.getObject(17));
						holder.setCreated(""+result.getObject(18));
						holder.setHotlistStatus("0");
						holder.setBlockStatus("0");
						holder.setOnline_balance(""+result.getObject(19));
						holder.setPayfee(""+result.getObject(20));
						holder.setCashwthdw_limit(""+result.getObject(21));
						holder.setClient_id(""+result.getObject(22));
						arr.add(holder);
				}
						
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPocketMoniEcardDB(con, result);
			closeConnectionTestECard(con, result, result);
			closeConnectionECard(con, result, result);
	
		}
		finally
		{
			closeConnectionPocketMoniEcardDB(con, result);
			closeConnectionTestECard(con, result, result);
			closeConnectionECard(con, result, result);
	
			
		}
		return arr;
		
	}
	
	
	
	/*
	 * 
	 * This method is to get card holder information
	 */
	public ArrayList getCardHolderEnquires(String dbServer,String card_num)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION tran = null;
		CardHolder holder = null;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		String apostrophe = "'";
	
		double d = 0.0;
	
		
		try
		{
				
				if(dbServer.equals("1"))  // .57
				{
					
					con = connectToTestECard();   
					stat = con.createStatement();
					
			
				}
				else if(dbServer.equals("2"))    // .133
				{
					con = connectPocketMoniEcardDB();
					stat = con.createStatement();
				
					
				}
				else if(dbServer.equals("3"))  //  .130
				{
					con = connectToECard();
					stat = con.createStatement();	
				
					
				}
				
				
				System.out.println("Database Server    --   "+dbServer);
				
				query = "select card_num,Lastname,Firstname,issuer_code,Email,Phone,Street,card_expiration,bound_work," +
				    		"bound_value,Birth_Date,Change_Pin,User_Hotlist,User_Locked,Pin_Missed,Last_Used,Modified,Created," +
				    		"Online_Balance,PayFee,CASHWTHDW_LIMIT,Client_ID from E_CARDHOLDER  where card_num ='"+card_num+"' ";
				    	
			
				System.out.println("getCardHolderEnquires scheme Option   " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
					
						holder = new CardHolder();
						holder.setCard_num(""+result.getObject(1));
						holder.setLastname(""+result.getObject(2));
						holder.setFirstname(""+result.getObject(3));
						holder.setIssuer_code(""+result.getObject(4));
						holder.setEmail(""+result.getObject(5));
						holder.setPhone(""+result.getObject(6));
						holder.setStreet(""+result.getObject(7));
						holder.setCard_expiration(""+result.getObject(8));
						holder.setBound_work(""+result.getObject(9));
						holder.setBound_value(""+result.getObject(10));
						holder.setBirth_date(""+result.getObject(11));
						holder.setChange_pin(""+result.getObject(12));
						holder.setUser_hotlist(""+result.getObject(13));
						holder.setUser_locked(""+result.getObject(14));
						holder.setPin_missed(""+result.getObject(15));
						holder.setLast_used(""+result.getObject(16));
						holder.setModified(""+result.getObject(17));
						holder.setCreated(""+result.getObject(18));
						holder.setOnline_balance(""+result.getObject(19));
						holder.setPayfee(""+result.getObject(20));
						holder.setCashwthdw_limit(""+result.getObject(21));
						holder.setClient_id(""+result.getObject(22));
						arr.add(holder);
				}
						
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPocketMoniEcardDB(con, result);
			closeConnectionTestECard(con, result, result);
			closeConnectionECard(con, result, result);
	
		}
		finally
		{
			closeConnectionPocketMoniEcardDB(con, result);
			closeConnectionTestECard(con, result, result);
			closeConnectionECard(con, result, result);
	
			
		}
		return arr;
		
	}
	
	

	/*This method is to view Hotlised and blocked Card */

	public ArrayList getHotlisted_Blocked_Card(String dbServer, String cardScheme,String hotlistOption , String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION tran = null;
		CardHolder holder = null;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		String apostrophe = "'";
	
		double d = 0.0;
	
		
		try
		{
				
				if(dbServer.equals("1")) // pocketmoni server 
				{
					con = connectPocketMoniEcardDB();
					stat = con.createStatement();
					System.out.println("Current   Server 1 ");
					
				}
				else if(dbServer.equals("2"))  //  .57
				{
					con = connectToTestECard();
					stat = con.createStatement();
					
					System.out.println("Current   Server 2 ");
					
				}
				else
				{
					con = connectToECard();
					stat = con.createStatement();
					
					System.out.println("Current   Server 3 ");
					
				}
			
				if(cardScheme.equalsIgnoreCase("ANY")) //  search base on  all card scheme 
				{
					
					
					if(hotlistOption.equalsIgnoreCase("ALL"))
					{
						
						query = "select card_num,Lastname,Firstname,issuer_code,Email,Phone,Street,card_expiration,bound_work," +
			    				"bound_value,Birth_Date,Change_Pin,User_Hotlist,User_Locked,Pin_Missed,Last_Used,Modified,Created," +
			    				"Online_Balance,PayFee,CASHWTHDW_LIMIT,Client_ID from E_CARDHOLDER  where User_Hotlist = '1' " +
			    				"OR User_Locked = '1'  and created between('"+start_dt+"') and ('"+end_dt+"') ";				
					}
					else if(hotlistOption.equalsIgnoreCase("Hotlisted"))       
					{
						query = "select card_num,Lastname,Firstname,issuer_code,Email,Phone,Street,card_expiration,bound_work," +
			    				"bound_value,Birth_Date,Change_Pin,User_Hotlist,User_Locked,Pin_Missed,Last_Used,Modified,Created," +
			    				"Online_Balance,PayFee,CASHWTHDW_LIMIT,Client_ID from E_CARDHOLDER  where User_Hotlist = '1' ";
			    			
					
					}
					else if(hotlistOption.equalsIgnoreCase("Blocked"))
					{
						query = "select card_num,Lastname,Firstname,issuer_code,Email,Phone,Street,card_expiration,bound_work," +
			    				"bound_value,Birth_Date,Change_Pin,User_Hotlist,User_Locked,Pin_Missed,Last_Used,Modified,Created," +
			    				"Online_Balance,PayFee,CASHWTHDW_LIMIT,Client_ID from E_CARDHOLDER  where User_Locked = '1' ";
			    				

					}
					
					System.out.println("getHotlisted_Blocked_Card scheme Option   " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
					
						holder = new CardHolder();
						holder.setCard_num(""+result.getObject(1));
						holder.setLastname(""+result.getObject(2));
						holder.setFirstname(""+result.getObject(3));
						holder.setIssuer_code(""+result.getObject(4));
						holder.setEmail(""+result.getObject(5));
						holder.setPhone(""+result.getObject(6));
						holder.setStreet(""+result.getObject(7));
						holder.setCard_expiration(""+result.getObject(8));
						holder.setBound_work(""+result.getObject(9));
						holder.setBound_value(""+result.getObject(10));
						holder.setBirth_date(""+result.getObject(11));
						holder.setChange_pin(""+result.getObject(12));
						holder.setUser_hotlist(""+result.getObject(13));
						holder.setUser_locked(""+result.getObject(14));
						holder.setPin_missed(""+result.getObject(15));
						holder.setLast_used(""+result.getObject(16));
						holder.setModified(""+result.getObject(17));
						holder.setCreated(""+result.getObject(18));
						holder.setOnline_balance(""+result.getObject(19));
						holder.setPayfee(""+result.getObject(20));
						holder.setCashwthdw_limit(""+result.getObject(21));
						holder.setClient_id(""+result.getObject(22));
						arr.add(holder);
					}
					
					
				}
				else
				{
				
					//Search base on a specific card scheme
					if(hotlistOption.equalsIgnoreCase("ALL"))
					{
						
						query = "select card_num,Lastname,Firstname,issuer_code,Email,Phone,Street,card_expiration,bound_work," +
			    				"bound_value,Birth_Date,Change_Pin,User_Hotlist,User_Locked,Pin_Missed,Last_Used,Modified,Created," +
			    				"Online_Balance,PayFee,CASHWTHDW_LIMIT,Client_ID from E_CARDHOLDER  where User_Hotlist = '1' " +
			    				"and card_num like '"+cardScheme+"%' OR  User_Locked = '1'  and created between('"+start_dt+"') and ('"+end_dt+"') ";				
					}
					else if(hotlistOption.equalsIgnoreCase("Hotlisted"))       
					{
						query = "select card_num,Lastname,Firstname,issuer_code,Email,Phone,Street,card_expiration,bound_work," +
			    				"bound_value,Birth_Date,Change_Pin,User_Hotlist,User_Locked,Pin_Missed,Last_Used,Modified,Created," +
			    				"Online_Balance,PayFee,CASHWTHDW_LIMIT,Client_ID from E_CARDHOLDER  where card_num like '"+cardScheme+"%' " +
			    				"and User_Hotlist = '1' ";
			    				
					
					}
					else if(hotlistOption.equalsIgnoreCase("Blocked"))
					{
						query = "select card_num,Lastname,Firstname,issuer_code,Email,Phone,Street,card_expiration,bound_work," +
			    				"bound_value,Birth_Date,Change_Pin,User_Hotlist,User_Locked,Pin_Missed,Last_Used,Modified,Created," +
			    				"Online_Balance,PayFee,CASHWTHDW_LIMIT,Client_ID from E_CARDHOLDER  where card_num like '"+cardScheme+"%' " +
			    				"and User_Locked = '1' ";
					}
					
					System.out.println("getHotlisted_Blocked_Card scheme Option   " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
					
						holder = new CardHolder();
						holder.setCard_num(""+result.getObject(1));
						holder.setLastname(""+result.getObject(2));
						holder.setFirstname(""+result.getObject(3));
						holder.setIssuer_code(""+result.getObject(4));
						holder.setEmail(""+result.getObject(5));
						holder.setPhone(""+result.getObject(6));
						holder.setStreet(""+result.getObject(7));
						holder.setCard_expiration(""+result.getObject(8));
						holder.setBound_work(""+result.getObject(9));
						holder.setBound_value(""+result.getObject(10));
						holder.setBirth_date(""+result.getObject(11));
						holder.setChange_pin(""+result.getObject(12));
						holder.setUser_hotlist(""+result.getObject(13));
						holder.setUser_locked(""+result.getObject(14));
						holder.setPin_missed(""+result.getObject(15));
						holder.setLast_used(""+result.getObject(16));
						holder.setModified(""+result.getObject(17));
						holder.setCreated(""+result.getObject(18));
						holder.setOnline_balance(""+result.getObject(19));
						holder.setPayfee(""+result.getObject(20));
						holder.setCashwthdw_limit(""+result.getObject(21));
						holder.setClient_id(""+result.getObject(22));
						arr.add(holder);
					}
					
					
						
					
								
						
					}
				
			
					
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPocketMoniEcardDB(con, result);
			closeConnectionTestECard(con, result, result);
			closeConnectionECard(con, result, result);
			
		}
		finally
		{
			closeConnectionPocketMoniEcardDB(con, result);
			closeConnectionTestECard(con, result, result);
			closeConnectionECard(con, result, result);
			
		}
		return arr;
		
	}
	
	/*Method to get card scheme status summary  report */
	public ArrayList getCardStatusReportSummary(String start_dt, String end_dt,String scheme, String dbServer )
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
	
			
			if(scheme.equalsIgnoreCase("any"))
			{
				
				
				query = "select count(distinct card_num) from e_cardholder ";
						
						
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
								" from e_transaction where trans_date between '"+start_dt+"' and '"+end_dt+"' ";
								
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
			else
			{
				
				   // search for a specific card scheme
				
					query = "select count(distinct card_num)" +
					" from e_cardholder where card_num like '"+scheme+"%' ";
					
					
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
	
	
	
	
	/*Method for getting the transaction of funds transfer*/
	public ArrayList getFundsTransferTransaction(String card_num, String option, double grt_amount, 
			String start_dt, String end_dt, String transCode, String transCount, String tranSummary,String scheme)
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

		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			con1 = connectToChams();
			stat1 = con1.createStatement();
		
			transaction = new E_TRANSACTION();
			
			System.out.println("Options Type   "+option);
			if(option.equals("1"))//greater than
			{
					if(transCount.trim().length() > 0)
					{	
						
						query = "select Phone, card_num from e_cardholder where phone = '"+card_num+"' ";
						result1 = stat1.executeQuery(query);
						System.out.print("query transCount   "+query);
						if(result1.next())
						{
							transaction.setPhone(""+result1.getObject(1));
							transaction.setCard_num(""+result1.getObject(2));
							
							
						}
						
						
						
						query = "select distinct CARD_NUM, Count(*), sum(TRANS_AMOUNT)" +
								" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
								" and card_num like '"+transaction.getCard_num()+"%' and substring(card_num,1,3) like '"+scheme+"%'" +
								" and trans_code ='"+transCode+"'" +
								" and trans_amount > "+grt_amount+" group by card_num having count(*) = "+transCount+"";
						
						System.out.println("query  check out " + query);
						
						result = stat.executeQuery(query);
						while(result.next())
						{
							
							transaction.setCard_num(""+result.getObject(1));
							transaction.setCard_count(""+result.getObject(2));
							transaction.setTotal_amount(""+result.getObject(3));
							arr.add(transaction);
						}
					}
					else if(tranSummary.trim().equalsIgnoreCase("G"))
					{
					
						query = "select Phone, card_num from e_cardholder where phone = '"+card_num+"' ";
						result1 = stat1.executeQuery(query);
						if(result1.next())
						{
							transaction.setPhone(""+result1.getObject(1));
							transaction.setCard_num(""+result1.getObject(2));
							
						}
						
						
						query = "select distinct CARD_NUM, Count(*)Transaction_Count, sum(TRANS_AMOUNT) Trans_Amount" +
								" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
								" and card_num like '"+transaction.getCard_num()+"%' and substring(card_num,1,3) like '"+scheme+"%'" +
								" and trans_code ='"+transCode+"'" +
								" and trans_amount > "+grt_amount+" group by card_num order by count(*) DESC ";
						
								System.out.println("query " + query);
								result = stat.executeQuery(query);
								while(result.next())
								{
									//transaction = new E_TRANSACTION();
									transaction.setCard_num(""+result.getObject(1));
									transaction.setCard_count(""+result.getObject(2));
									transaction.setTotal_amount(""+result.getObject(3));
									arr.add(transaction);
									System.out.println("query " + query);
								}
					}
				else
				{
					
					query = "Select Phone,card_num where created between('"+start_dt+"') and ('"+end_dt+"') ";
					result1 = stat1.executeQuery(query);
					System.out.println("query default --- > "+query);
					if(result1.next())
					{
						transaction.setPhone(""+result1.getObject(1));
						transaction.setCard_num(""+result1.getObject(2));
					}
					
				   query = " select TRANSID, CARD_NUM, TRANS_NO, TRANS_DATE, TRANS_DESCR," +
					" TRANS_AMOUNT,TRANS_TYPE,TRANS_CODE, MERCHANT_CODE, UNIQUE_TRANSID, " +
					" (select merchant_name from e_merchant where e_merchant.merchant_code = e_transaction.merchant_code), "+
					" (select channel_name from e_channel where e_channel.channel_id = e_transaction.channelid), PROCESS_STATUS " +
					" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
					" and substring(card_num,1,3) like '"+scheme+"%' and trans_code = '"+transCode+"' and" +
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
						transaction.setMerchant_descr(""+result.getObject(11));
						transaction.setChannelid(""+result.getObject(12));
						transaction.setProcess_status(""+result.getObject(13));
						arr.add(transaction);
					}
					
					
				}
			}
			else if(option.equals("2"))//lesser than
			{
				
				if(transCount.trim().length() > 0)
				{	
					
					query = "select Phone, card_num from e_cardholder where phone = '"+card_num+"' ";
					result1 = stat1.executeQuery(query);
					System.out.print("query transCount   "+query);
					if(result1.next())
					{
						transaction.setPhone(""+result1.getObject(1));
						transaction.setCard_num(""+result1.getObject(2));	
						
					}
					query = "select distinct CARD_NUM, Count(*), sum(TRANS_AMOUNT)" +
							" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
							" and card_num like '"+transaction.getCard_num()+"%' and substring(card_num,1,3) like '"+scheme+"%'" +
							" and trans_code ='"+transCode+"'" +
							" and trans_amount < "+grt_amount+" group by card_num having count(*) = "+transCount+" ";
					
					System.out.println("query " + query);
					
					result = stat.executeQuery(query);
					while(result.next())
					{
						//transaction = new E_TRANSACTION();
						 
						transaction.setCard_num(""+result.getObject(1));
						transaction.setCard_count(""+result.getObject(2));
						transaction.setTotal_amount(""+result.getObject(3));
						arr.add(transaction);
					}
				}
				else if(tranSummary.trim().equalsIgnoreCase("G"))
				{
					
					query = "select Phone, card_num from e_cardholder where phone = '"+card_num+"' ";
					result1 = stat1.executeQuery(query);
					System.out.print("query transCount   "+query);
					if(result1.next())
					{
						transaction.setPhone(""+result1.getObject(1));
						transaction.setCard_num(""+result1.getObject(2));
						
						
					}

					query = "select distinct CARD_NUM, Count(*)Transaction_Count, sum(TRANS_AMOUNT) Trans_Amount" +
							" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
							" and card_num like '"+transaction.getCard_num()+"%' and substring(card_num,1,3) like '"+scheme+"%'" +
							" trans_code ='"+transCode+"'" +
							" and trans_amount < "+grt_amount+" group by card_num order by count(*) DESC ";
					System.out.println("query G 1 " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
							//transaction = new E_TRANSACTION();
							
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
							"(select PHONE from e_cardholder where e_cardholder.card_num = e_transaction.card_num) , " +
							"(select merchant_name from e_merchant where e_merchant.merchant_code = e_transaction.merchant_code), "+
							"(select channel_name from e_channel where e_channel.channel_id = e_transaction.channelid), PROCESS_STATUS " +
							" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
							" and substring(card_num,1,3) like '"+scheme+"%' " +
							" and trans_code = '"+transCode+"' and" +
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
								transaction.setPhone(""+result.getObject(11));
								transaction.setMerchant_descr(""+result.getObject(12));
								transaction.setChannelid(""+result.getObject(13));
								transaction.setProcess_status(""+result.getObject(14));
								
								arr.add(transaction);
							}
				}
			}
			else if(option.equals("3"))//exact
			{
				
						if(transCount.trim().length() > 0)
						{	
							
							query = "select Phone, card_num from e_cardholder where phone = '"+card_num+"' ";
							result1 = stat1.executeQuery(query);
							System.out.print("query transCount   "+query);
							if(result1.next())
							{
								transaction.setPhone(""+result1.getObject(1));
								transaction.setCard_num(""+result1.getObject(2));
								
								
							}
							
							query = "select distinct CARD_NUM, Count(*), sum(TRANS_AMOUNT)" +
									" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
									" and card_num like '"+transaction.getCard_num()+"%' and substring(card_num,1,3) like '"+scheme+"%' " +
									" and trans_code ='"+transCode+"'" +
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
							
							
							query = "select Phone, card_num from e_cardholder where phone = '"+card_num+"' ";
							result1 = stat1.executeQuery(query);
							System.out.print("query transCount   "+query);
							if(result1.next())
							{
								transaction.setPhone(""+result1.getObject(1));
								transaction.setCard_num(""+result1.getObject(2));
								
								
							}
							
							query = "select distinct CARD_NUM, Count(*)Transaction_Count, sum(TRANS_AMOUNT) Trans_Amount" +
									" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
									" and card_num like '"+transaction.getCard_num()+"%' and substring(card_num,1,3) like '"+scheme+"%' " +
									" and trans_code ='"+transCode+"'" +
									" and trans_amount = "+grt_amount+" group by card_num order by count(*) DESC ";
							
							System.out.println("query G 2" + query);
							result = stat1.executeQuery(query);
							while(result.next())
							{
						 
									//transaction = new E_TRANSACTION();
									
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
							"(select PHONE from e_cardholder where e_cardholder.card_num = e_transaction.card_num) , " +
							"(select merchant_name from e_merchant where e_merchant.merchant_code = e_transaction.merchant_code), "+
							"(select channel_name from e_channel where e_channel.channel_id = e_transaction.channelid), PROCESS_STATUS " +
							" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
							" and substring(card_num,1,3) like '"+scheme+"%' " +
							" and trans_code = '"+transCode+"' and" +
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
								transaction.setPhone(""+result.getObject(11));
								transaction.setMerchant_descr(""+result.getObject(12));
								transaction.setChannelid(""+result.getObject(13));
								transaction.setProcess_status(""+result.getObject(14));
								
								arr.add(transaction);
							}
				}
			}
			else
			{
				
				if(transCount.trim().length() > 0)
				{	
					
					query = "select Phone, card_num from e_cardholder where phone = '"+card_num+"' ";
					result1 = stat1.executeQuery(query);
					System.out.print("query transCount   "+query);
					if(result1.next())
					{
						transaction.setPhone(""+result1.getObject(1));
						transaction.setCard_num(""+result1.getObject(2));
						
						
					}
					
					query = "select distinct CARD_NUM, Count(*), sum(TRANS_AMOUNT)" +
							" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
							" and card_num like '"+transaction.getCard_num()+"%' and substring(card_num,1,3) like '"+scheme+"%' " +
							" and trans_code ='"+transCode+"' " +
							" group by card_num having count(*) = "+transCount+"";
					
					System.out.println("query " + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
						//transaction = new E_TRANSACTION();
						
						transaction.setCard_num(""+result.getObject(1));
						transaction.setCard_count(""+result.getObject(2));
						transaction.setTotal_amount(""+result.getObject(3));
						arr.add(transaction);
					}
				}
				else if(tranSummary.trim().equalsIgnoreCase("G"))
				{
					
					query = "select Phone, card_num from e_cardholder where phone = '"+card_num+"' ";
					
					result1 = stat1.executeQuery(query);
					System.out.print("query transCount   "+query);
					if(result1.next())
					{
						transaction.setPhone(""+result1.getObject(1));
						transaction.setCard_num(""+result1.getObject(2));
						
						
					}
					
					query = "select distinct CARD_NUM, Count(*)Transaction_Count, sum(TRANS_AMOUNT) Trans_Amount" +
							" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
							" and card_num like '"+transaction.getCard_num()+"%' and substring(card_num,1,3) like '"+scheme+"%'" +
							" and trans_code ='"+transCode+"'" +
							" group by card_num order by count(*) DESC ";
					System.out.println("query G 2" + query);
					result = stat.executeQuery(query);
					while(result.next())
					{
				 
		
							
							transaction.setCard_num(""+result.getObject(1));
							transaction.setCard_count(""+result.getObject(2));
							transaction.setTotal_amount(""+result.getObject(3));
							arr.add(transaction);
							System.out.println("query " + query);
					}
						 
					
				}
				else
				{
					
					
					query = "Select Phone ,card_num from E_CardHolder where created between('"+start_dt+"') and ('"+end_dt+"') ";

					result1 = stat1.executeQuery(query);
					System.out.println("query default me --- > "+query);
					if(result1.next())
					{
						transaction.setPhone(""+result1.getObject(1));
						transaction.setCard_num(""+result1.getObject(2));
					}
					query = "select TRANSID, CARD_NUM, TRANS_NO, TRANS_DATE, TRANS_DESCR," +
							"TRANS_AMOUNT,TRANS_TYPE,TRANS_CODE, MERCHANT_CODE, UNIQUE_TRANSID, " +
							"(select merchant_name from e_merchant where e_merchant.merchant_code = e_transaction.merchant_code), "+
							"(select channel_name from e_channel where e_channel.channel_id = e_transaction.channelid), PROCESS_STATUS " +
							" from ecarddb..E_TRANSACTION where trans_date between('"+start_dt+"') and ('"+end_dt+"')" +
							" and substring(card_num,1,3) like '"+scheme+"%' and trans_code = '"+transCode+"' order by trans_date desc ";
			
					
							System.out.println("query last option ::::: " + query);
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
							
								String merchantName = ""+result.getObject(11);
								if(merchantName==null || merchantName.equals("null"))
								{
									transaction.setMerchant_descr("");
								}else
								{
									transaction.setMerchant_descr(merchantName);
								}
								String channel = ""+result.getObject(12);
								if(channel == null || channel.equals("null"))
								{
									transaction.setChannelid("");
								}
								else
								{
									transaction.setChannelid(channel);
								}
								
								
								transaction.setProcess_status(""+result.getObject(13));
								System.out.println("At the point of addd....."+transaction.getPhone());
								arr.add(transaction);
							}
							
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
			closeConnectionChams(con1, result, result1);
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeConnectionChams(con1, result, result1);
		}
		return arr;
	}
	
	
	
	
	public ArrayList pendingStatus(String card_num)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION tran = null;
		CardHolder holder = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		double d = 0.0;
		String str = "";
		
		String apostrophe = "'";
		
		
		try
		{
			
			con = connectToECard();
			stat = con.createStatement();
		 
			
			query =  	"select card_num,Lastname,Firstname,issuer_code,Email,Phone,Street,card_expiration,bound_work," +
	    				"bound_value,Birth_Date,Change_Pin,User_Locked,User_Hotlist,Pin_Missed,Last_Used,Modified,Created," +
	    				"Online_Balance,PayFee,CASHWTHDW_LIMIT,Client_ID,Hotlist_status,Block_status from E_CARD_PENDING_AUTH  where card_num = '"+card_num+"'";
	    		
	    				System.out.println("getCardHolderEnquires   " + query);
			
			result = stat.executeQuery(query);
			while(result.next())
			{
			
				holder = new CardHolder();
				holder.setCard_num(""+result.getObject(1));
				holder.setLastname(""+result.getObject(2));
				holder.setFirstname(""+result.getObject(3));
				holder.setIssuer_code(""+result.getObject(4));
				holder.setEmail(""+result.getObject(5));
				holder.setPhone(""+result.getObject(6));
				holder.setStreet(""+result.getObject(7));
				holder.setCard_expiration(""+result.getObject(8));
				holder.setBound_work(""+result.getObject(9));
				holder.setBound_value(""+result.getObject(10));
				holder.setBirth_date(""+result.getObject(11));
				holder.setChange_pin(""+result.getObject(12));
				holder.setUser_locked(""+result.getObject(13));
				holder.setUser_hotlist(""+result.getObject(14));
				holder.setPin_missed(""+result.getObject(15));
				holder.setLast_used(""+result.getObject(16));
				holder.setModified(""+result.getObject(17));
				holder.setCreated(""+result.getObject(18));
				holder.setOnline_balance(""+result.getObject(19));
				holder.setPayfee(""+result.getObject(20));
				holder.setCashwthdw_limit(""+result.getObject(21));
				holder.setClient_id(""+result.getObject(22));
				arr.add(holder);
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
	public ArrayList getCardHolderTransactions(String card_num, String mobile,String start_dt, String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION tran = null;
		int counter = 0;
		Connection con = null;
		Connection con1 = null;
		Statement stat = null;
		Statement stat1 = null;
		ResultSet result = null;
		ResultSet result1 = null;
		double d = 0.0;   
		String str = "";
		String apostrophe = "'";
		String query1  = "";
		String cardnum = "";
		
		try
		{
			
		
			con = connectToECard();
			stat = con.createStatement();
			
			con1 = connectPocketMoniEcardDB();
			stat1 = con1.createStatement();
			
	
				if(mobile.trim().length() > 0)
				{
					query = "select card_num from e_Cardholder where phone = '"+mobile+"' ";
					System.out.println("query ---  > "+query);
					result1 = stat1.executeQuery(query);
					if(result1.next())
					{
						card_num = result1.getString(1);				
					}
				}
			

				if(card_num.trim().length() > 0)
				{
				
					query = "select trans_no,card_num, merchant_code, (select f_name from e_transcode where f_code = e_transaction.trans_code), trans_descr, (select channel_name from e_channel where channel_id = e_transaction.channelid), trans_date, " +
							"case when" +
							" card_num like '"+card_num+"%' " +
							"then -1*trans_amount " +
							"else" + 
							" trans_amount " +
							"end" +
							" from e_transaction " +
							"where (card_num ='"+card_num+"' or merchant_code ='"+card_num+"') " +
							"and trans_date between('"+start_dt+"') and ('"+end_dt+"') order by trans_date " ;	  
					
					
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
				
				
	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
			closeConnectionPocketMoniEcardDB(con1, result);
			
		}
		finally
		{
			closeConnectionECard(con, result, result);
			closeConnectionPocketMoniEcardDB(con1, result);
		}
		return arr;
	}
	
	public ArrayList getCardHolderDetails(String cardNum)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION tran = null;
		CardHolder holder = null;
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
		

			System.out.println("getCardHolderEnquires   " + query);
			
			result = stat.executeQuery(query);
			while(result.next())
			{
			
				holder = new CardHolder();
				holder.setCard_num(""+result.getObject(1));
				holder.setLastname(""+result.getObject(2));
				holder.setFirstname(""+result.getObject(3));
				holder.setIssuer_code(""+result.getObject(4));
				holder.setEmail(""+result.getObject(5));
				holder.setPhone(""+result.getObject(6));
				holder.setStreet(""+result.getObject(7));
				holder.setCard_expiration(""+result.getObject(8));
				holder.setBound_work(""+result.getObject(9));
				holder.setBound_value(""+result.getObject(10));
				holder.setBirth_date(""+result.getObject(11));
				holder.setChange_pin(""+result.getObject(12));
				holder.setUser_locked(""+result.getObject(13));
				holder.setUser_hotlist(""+result.getObject(14));
				holder.setPin_missed(""+result.getObject(15));
				holder.setLast_used(""+result.getObject(16));
				holder.setModified(""+result.getObject(17));
				holder.setCreated(""+result.getObject(18));
				holder.setOnline_balance(""+result.getObject(19));
				holder.setPayfee(""+result.getObject(20));
				holder.setCashwthdw_limit(""+result.getObject(21));
				holder.setClient_id(""+result.getObject(22));
				arr.add(holder);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionCPMT(con, result, result);
			//closeConnectionBackUpEcard(con, result, result);
		}
		finally
		{
			closeConnectionBackUpEcard(con, result, result);
			closeConnectionCPMT(con, result, result);
		}
		return arr;
		
	}
	
	public String getCardNumbers(String card_num_mobile)
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
			
			query = "select card_num from e_cardholder where card_num = '" + card_num_mobile +"'";
			
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
	
	/*
	 * 
	 * This method is to get all Card Scheme
	 */
	public ArrayList getCardTransactionReport(String cardNum,String startDt,String endDt)
	{		
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION transaction = null;
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		double d = 0.0;
		String str = "";
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			if(cardNum.trim().length() > 0)
			{
				
				query = "select distinct card_num , count(*) trans_count, sum(trans_amount) trans_amount from  e_transaction" +
						" where card_num like '"+cardNum+"%' group by card_num ";	
			}
			else
			{
				query = "select distinct card_num , count(*) trans_count, sum(trans_amount) trans_amount from  e_transaction" +
						"  where trans_date between('"+startDt+"') and ('"+endDt+"') and card_num like '"+cardNum+"%' " +
						"group by card_num ";	
			}
			
			System.out.println("getAllCardScheme query " + query);
			
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
	
	public ArrayList getPendingAuthorization()
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
				
				query = " select * from e_cardholder where USER_HOTLIST = '1' and card_num ='0569911010140029' ";
			/*	query = "select card_num, phone, lastname, firstname, street, state, card_expiration, email, bound_work, bound_value," +
						" birth_date, change_pin, user_locked, user_hotlist, pin_missed, last_used, modified, created," +
						" online_balance,payfee, cashwthdw_limit, client_id,issuer_code  from e_cardholder where card_num = '" + card_num +"' ";*/
				
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
					cholder.setIssuer_code(""+result1.getObject(23));
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
	
	/*
	 * 
	 * This is method is to get Card Transaction summary break down
	 */
	public ArrayList getCardBreakDownTrasactionSumary(String cardNum)
	{		
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION transaction = null;
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		double d = 0.0;
		String str = "";
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
		    query = "select  card_num, count(*) trans_count, sum(trans_amount) trans_amount from  e_transaction " +
		    		"where card_num like '"+cardNum+"%' group by card_num,trans_amount ";
			
			System.out.println("getAllCardScheme query " + query);
			
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
	

	
	/*Method to create e_exception data*/
	public String createE_Exception(String card_num, String reason)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		int output1 = -1;
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
				query = "insert into e_exception(pan, reason, date)values('"+card_num+"', '"+reason+"', getDate()) ";
				
				//String query1 = "update e_cardholder set User_hotlist = '1' where card_num = '"+card_num+"' ";
				//output1 = stat.executeUpdate("update e_cardholder set User_hotlist = '1' where card_num = '"+card_num+"' ");
				
				System.out.println("createE_Exception query " + query);
				output = stat.executeUpdate(query);
				//output1 = stat.executeUpdate(query1);
				//output = stat.executeUpdate(query);
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

	/*
	 * 
	 * This method is to get all Card Scheme
	 */
	public ArrayList getAllCardScheme()
	{		
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION tran = null;
		CardHolder holder = null;
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
		
			query = "select Issuer_branch from  e_exfrontendinterface";
			System.out.println("getAllCardScheme query " + query);
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				holder = new CardHolder();
				holder.setCardScheme(""+result.getObject(1));
				arr.add(holder);
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
	
	/*Method to create pending hotlist for authorisation */
	public String createPending_Hotlist(String card_num,String hotlistStatus)
	{
		String query = "";
		String query1 = "";
		String query2 = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		int output1 = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select  * from E_CARD_PENDING_AUTH where card_num = '"+card_num+"' ";
			result  = stat.executeQuery(query);
			if(result.next())
			{
				
				query = "update E_CARD_PENDING_AUTH set Hotlist_Status = '"+hotlistStatus+"' where card_num = '"+card_num+"' ";	
				
				output1 = stat.executeUpdate(query);
				System.out.println("hottlist values "+output1);
				if(output1 > 0)
				{
					message = "Record Pending for Authorization";
				}
			}
			else
			{
				
			    
				query1 = "INSERT INTO E_CARD_PENDING_AUTH (CARD_NUM, CARD_TYPE, CARD_PIN, CARD_ACCOUNT,CARD_EXPIRATION,FIRSTNAME,LASTNAME,COMPANY,STREET,CITY,STATE,ZIP,COUNTRY," +
						"PHONE,FAX,EMAIL,MINBAL_ACTIVE,MINBAL_VALUE,BOUND_FREQ,BOUND_VALUE,BOUND_WORK,BIRTH_DATE,MARITAL_STATUS,NATIONALITY,DEP_CHILDREN,DEP_HIGHINST,RESIDENCE_TYPE," +
						"ACCT_PURPOSE,PRIM_INDUSTRY,PRIM_JOBFUNCTION,EMP_STATUS,ANNUAL_INCOME,INTERNET_ACCESS,ISSUER_CODE,SUB_CODE,CHANGE_PIN,HINTQUESTION,HINTANSWER,USER_LOCKED," +
						"USER_HOTLIST,PIN_MISSED,LAST_USED,MODIFIED,CREATED,ONLINE_DATE,ONLINE_BALANCE,OFFLINE_DATE,OFFLINE_BALANCE,SOCIAL_SECURITYNO,REGCODE,DEFAULT_PIN,MAX_LOADBALANCE," +
						"PAYFEE,TRACK2,CONTROL_CODE,BASE_CURRENCY,CARD_ALIAS,SMS_OPTION,CASHWTHDW_LASTDATE,CASHWTHDW_LIMIT,CASH_UTILISED,CASHWTHDW_VELOCITY,CREATED_BY,CLIENT_ID," +
						"ESA_STATUS,PIN_STATUS,mail_sent,VCHECK,CONTROL_ID,POS_LIMIT,POS_VELOCITY,CHIP_ENABLED,CARD_SERIAL,BATCH_ID,POS_UTILISED,POS_LASTDATE,CVV," +
						"ICVV,AID,AGREGATOR_ID,SERVICE_CODE)" +
						"SELECT CARD_NUM, CARD_TYPE, CARD_PIN, CARD_ACCOUNT,CARD_EXPIRATION,FIRSTNAME,LASTNAME,COMPANY,STREET,CITY,STATE,ZIP,COUNTRY," +
						"PHONE,FAX,EMAIL,MINBAL_ACTIVE,MINBAL_VALUE,BOUND_FREQ,BOUND_VALUE,BOUND_WORK,BIRTH_DATE,MARITAL_STATUS,NATIONALITY,DEP_CHILDREN,DEP_HIGHINST,RESIDENCE_TYPE," +
						"ACCT_PURPOSE,PRIM_INDUSTRY,PRIM_JOBFUNCTION,EMP_STATUS,ANNUAL_INCOME,INTERNET_ACCESS,ISSUER_CODE,SUB_CODE,CHANGE_PIN,HINTQUESTION,HINTANSWER,USER_LOCKED," +
						"USER_HOTLIST,PIN_MISSED,LAST_USED,MODIFIED,CREATED,ONLINE_DATE,ONLINE_BALANCE,OFFLINE_DATE,OFFLINE_BALANCE,SOCIAL_SECURITYNO,REGCODE,DEFAULT_PIN,MAX_LOADBALANCE," +
						"PAYFEE,TRACK2,CONTROL_CODE,BASE_CURRENCY,CARD_ALIAS,SMS_OPTION,CASHWTHDW_LASTDATE,CASHWTHDW_LIMIT,CASH_UTILISED,CASHWTHDW_VELOCITY,CREATED_BY,CLIENT_ID," +
						"ESA_STATUS,PIN_STATUS,mail_sent,VCHECK,CONTROL_ID,POS_LIMIT,POS_VELOCITY,CHIP_ENABLED,CARD_SERIAL,BATCH_ID,POS_UTILISED,POS_LASTDATE,CVV," +
						"ICVV,AID,AGREGATOR_ID,SERVICE_CODE FROM E_CARDHOLDER where card_num = '"+card_num+"'";
				
			 
				
				
				query2 = "update E_CARD_PENDING_AUTH set HOTLIST_STATUS = '"+hotlistStatus+"' where card_num = '"+card_num+"' ";	

				System.out.println("createHotlist  query1 " + query1);
				System.out.println("createHotlist  query2 " + query2);
				output = stat.executeUpdate(query1);
				output1 = stat.executeUpdate(query2);
			
				if(output > 0) 
				{
					message = " Record successfully Pending for Authorization ";
					con.commit();
				}
				else
				{
					message = "Record not Pending Yet";
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
	
	
	/*Method  for pending De-hotlisted and Unblocked Card before  authorisation */
	public String getDeHotlist_UnBlock_Pending(String dbServer, String card_num,String initiator)
	{
		String query = "";
		String query1 = "";
		String query2 = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		int output1 = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		Connection con1 = null;
		Statement stat1 = null;
		ResultSet result1 = null;
		
		
		try
		{
			


			if(dbServer.equals("1"))  // .57
			{
				
				con = connectToTestECard();   
				stat = con.createStatement();
				
		
			}
			else if(dbServer.equals("2"))    // .133
			{
				con = connectPocketMoniEcardDB();
				stat = con.createStatement();
			
				
			}
			else if(dbServer.equals("3"))  //  .130
			{
				con = connectToECard();
				stat = con.createStatement();	
			
				
			}
	
			con1 = connectToSupportLog();
			stat1 = con1.createStatement();
			
			
			query = "select  * from E_CARD_PENDING_AUTH where card_num = '"+card_num+"' and block_status = '0' and hotlist_status = '0' ";
			result1  = stat1.executeQuery(query);
			if(result1.next())
			{
				
				message = "Record Already Queue for  Authorization";

			}
			else
			{
				query = "select  * from telcodb.dbo.E_CARD_PENDING_AUTH where card_num = '"+card_num+"' and block_status is null and hotlist_status is null ";
				result1  = stat1.executeQuery(query);
				if(result1.next())
				{
					query = "update telcodb.dbo.E_CARD_PENDING_AUTH set block_status = '0', hotlist_status = '0' , USER_INITIATOR ='"+initiator+"',Date_Initiated= getDate()  where card_num = '"+card_num+"'  ";	
					output = stat1.executeUpdate(query);
					if(output > 0)
					{
						message =  " Record Successfully Awaiting Authorization ";
						 
						/*message = "De-Hotlist Request has been successfully logged.";
						message +="\n";
						message +=" The De-Hotlist  will be effected when this request is authorized";*/
						
					}	
				}
				else
				{
					query1 = "INSERT INTO telcodb.dbo.E_CARD_PENDING_AUTH (CARD_NUM, CARD_TYPE, CARD_PIN, CARD_ACCOUNT,CARD_EXPIRATION,FIRSTNAME,LASTNAME,COMPANY,STREET,CITY,STATE,ZIP,COUNTRY," +
							"PHONE,FAX,EMAIL,MINBAL_ACTIVE,MINBAL_VALUE,BOUND_FREQ,BOUND_VALUE,BOUND_WORK,BIRTH_DATE,MARITAL_STATUS,NATIONALITY,DEP_CHILDREN,DEP_HIGHINST,RESIDENCE_TYPE," +
							"ACCT_PURPOSE,PRIM_INDUSTRY,PRIM_JOBFUNCTION,EMP_STATUS,ANNUAL_INCOME,INTERNET_ACCESS,ISSUER_CODE,SUB_CODE,CHANGE_PIN,HINTQUESTION,HINTANSWER,USER_LOCKED," +
							"USER_HOTLIST,PIN_MISSED,LAST_USED,MODIFIED,CREATED,ONLINE_DATE,ONLINE_BALANCE,OFFLINE_DATE,OFFLINE_BALANCE,SOCIAL_SECURITYNO,REGCODE,DEFAULT_PIN,MAX_LOADBALANCE," +
							"PAYFEE,TRACK2,CONTROL_CODE,BASE_CURRENCY,CARD_ALIAS,SMS_OPTION,CASHWTHDW_LASTDATE,CASHWTHDW_LIMIT,CASH_UTILISED,CASHWTHDW_VELOCITY,CREATED_BY,CLIENT_ID," +
							"ESA_STATUS,PIN_STATUS,mail_sent,VCHECK,CONTROL_ID,POS_LIMIT,POS_VELOCITY,CHIP_ENABLED,CARD_SERIAL,BATCH_ID,POS_UTILISED,POS_LASTDATE,CVV," +
							"ICVV,AID,AGREGATOR_ID,SERVICE_CODE)" +
							"SELECT CARD_NUM, CARD_TYPE, CARD_PIN, CARD_ACCOUNT,CARD_EXPIRATION,FIRSTNAME,LASTNAME,COMPANY,STREET,CITY,STATE,ZIP,COUNTRY," +
							"PHONE,FAX,EMAIL,MINBAL_ACTIVE,MINBAL_VALUE,BOUND_FREQ,BOUND_VALUE,BOUND_WORK,BIRTH_DATE,MARITAL_STATUS,NATIONALITY,DEP_CHILDREN,DEP_HIGHINST,RESIDENCE_TYPE," +
							"ACCT_PURPOSE,PRIM_INDUSTRY,PRIM_JOBFUNCTION,EMP_STATUS,ANNUAL_INCOME,INTERNET_ACCESS,ISSUER_CODE,SUB_CODE,CHANGE_PIN,HINTQUESTION,HINTANSWER,USER_LOCKED," +
							"USER_HOTLIST,PIN_MISSED,LAST_USED,MODIFIED,CREATED,ONLINE_DATE,ONLINE_BALANCE,OFFLINE_DATE,OFFLINE_BALANCE,SOCIAL_SECURITYNO,REGCODE,DEFAULT_PIN,MAX_LOADBALANCE," +
							"PAYFEE,TRACK2,CONTROL_CODE,BASE_CURRENCY,CARD_ALIAS,SMS_OPTION,CASHWTHDW_LASTDATE,CASHWTHDW_LIMIT,CASH_UTILISED,CASHWTHDW_VELOCITY,CREATED_BY,CLIENT_ID," +
							"ESA_STATUS,PIN_STATUS,mail_sent,VCHECK,CONTROL_ID,POS_LIMIT,POS_VELOCITY,CHIP_ENABLED,CARD_SERIAL,BATCH_ID,POS_UTILISED,POS_LASTDATE,CVV," +
							"ICVV,AID,AGREGATOR_ID,SERVICE_CODE FROM E_CARDHOLDER where card_num = '"+card_num+"'";
					
				 
					
					
					query2 = "update telcodb.dbo.E_CARD_PENDING_AUTH set HOTLIST_STATUS = '0',BLOCK_STATUS = '0'  where card_num = '"+card_num+"' ";	

					System.out.println("createHotlist  query2 " + query2);
					output = stat.executeUpdate(query1);
					output1 = stat1.executeUpdate(query2);
				
					if(output > 0) 
					{
						message = "Record Successfully Awaiting Authorization";
						con.commit();
					}
					else
					{
						message = "Record Unable to Queue for Authorization ";
						con.rollback();
					}
					
				}
			    
				
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionTestECard(con, result, result);
			closeConnectionPocketMoniEcardDB(con, result);
			closeConnectionECard(con, result, result);
			closeConnectionSupportLogDB(con1, result1);
		}
		finally
		{
			closeConnectionTestECard(con, result, result);
			closeConnectionPocketMoniEcardDB(con, result);
			closeConnectionECard(con, result, result);
			closeConnectionSupportLogDB(con1, result1);
		}
		return message;
	}
	
	
	/*Method to log PIN Re-generatiion Details  */
	public String logPinRegenerationDetails(String card_num,String mobile,String status,String createdBy,String clientipAddress,String authorised_Status,String authoriser,String clientIp,String adminEmail,String sendPinOption)
	{
		String query = "";
		String query1 = "";
		String query2 = "";
		String message = "";
		String empty = "''";
		ArrayList arr = new ArrayList();
		int output = -1;
		int output1 = -1;
		Connection con = null;
		Statement stat = null;
		Connection con1 = null;
		Statement stat1 = null;
		ResultSet result = null;
		ResultSet result1 = null;
		
		try
		{
			
			
			con = connectToPMEcardholder();
			stat = con.createStatement();	
						
			query = "INSERT INTO E_MOBILEPIN(CARD_NUM,MOBILE,STATUS,CREATED,CREATED_BY,CLIENT_IP,AUTHORIZED,AUTHORIZER,AUTHORIZER_IP,AUTHORIZATION_DATE,ADMIN_MAIL,SEND_MAIL)" +
					"values('"+card_num+"','"+mobile+"','"+status+"',getDate(),'"+createdBy+"','"+clientipAddress+"','"+authorised_Status+"','"+authoriser+"','"+clientipAddress+"',getDate(),'"+adminEmail+"','"+sendPinOption+"')";
					System.out.println("query "+query);
						
			query1 = "update E_CARD_PENDING_INFO set pin_reset ='0' where card_num = '"+card_num+"' ";
						
			output = stat.executeUpdate(query);
			System.out.println("output "+output);
			if(output > 0) 
			{
				message = "Record has been successfully authorised";
				output1 = stat.executeUpdate(query1);
				con.commit();
			}
			else
			{
				message = "Unable to authorised Pin Re-Generation ";
				con.rollback();
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeconnectToPMEcardholder(con, result);
			
		}
		finally
		{
			closeconnectToPMEcardholder(con, result);
	
		}
		return message;
	}
	
	
	
	/*Method to log PIN Re-generatiion Details  
	public String logPinRegenerationDetails(String card_num,String mobile,String status,String createdBy,String clientipAddress,String authorised_Status,String authoriser,String clientIp)
	{
		String query = "";
		String query1 = "";
		String query2 = "";
		String message = "";
		String empty = "''";
		ArrayList arr = new ArrayList();
		int output = -1;
		int output1 = -1;
		Connection con = null;
		Statement stat = null;
		Connection con1 = null;
		Statement stat1 = null;
		ResultSet result = null;
		ResultSet result1 = null;
		
		try
		{
			
			
			con = connectToPMEcardholder();
			stat = con.createStatement();	
			
				query = "select  * from E_CMS_MOBILEPIN where card_num = '"+card_num+"' ";
				
				result = stat.executeQuery(query);
				if(result.next())
				{	
					query = "delete from E_CMS_MOBILEPIN where card_num = '"+card_num+"'";
					output = stat.executeUpdate(query);
					con.commit();
					if(output > 0)
					{
						
						System.out.println("DID IT GOT HERE    O  OO OOO O O O O  "+stat);
						
						query = "INSERT INTO E_CMS_MOBILEPIN(CARD_NUM,MOBILE,STATUS,CREATED,CREATED_BY,CLIENT_IP,AUTHORIZED,AUTHORIZER,AUTHORIZER_IP,AUTHORIZER_DATE)" +
								"values('"+card_num+"','"+mobile+"','"+status+"',getDate(),'"+createdBy+"','"+clientipAddress+"','"+authorised_Status+"','"+authoriser+"','"+clientipAddress+"',getDate())";
						System.out.println("query "+query);
						
						query1 = "update E_CARD_PENDING_INFO set pin_reset ='0' where card_num = '"+card_num+"' ";
						
					  	output = stat.executeUpdate(query);
					  	System.out.println("output "+output);
						if(output > 0) 
						{
							message = "Record has been successfully authorised";
							output1 = stat.executeUpdate(query1);
							con.commit();
						}
						else
						{
							message = "Unable to authorised Pin Re-Generation ";
							con.rollback();
						}
						
					}
					
					
				}
				else
				{
					
					query = "INSERT INTO E_CMS_MOBILEPIN(CARD_NUM,MOBILE,STATUS,CREATED,CREATED_BY,CLIENT_IP,AUTHORIZED,AUTHORIZER,AUTHORIZER_IP,AUTHORIZER_DATE)" +
							"values('"+card_num+"','"+mobile+"','"+status+"',getDate(),'"+createdBy+"','"+clientipAddress+"','"+authorised_Status+"','"+authoriser+"','"+clientipAddress+"',getDate())";
					System.out.println("query "+query);
					
					query1 = "update E_CARD_PENDING_INFO set pin_reset ='0' where card_num = '"+card_num+"' ";
				  	output = stat.executeUpdate(query);
				  	System.out.println("output "+output);
					if(output > 0) 
					{
						message = "Record has been successfully authorised";
						output1 = stat.executeUpdate(query1);
						con.commit();
					}
					else
					{
						message = "Unable to authorised Pin Re-Generation ";
						con.rollback();
					}
					
					
				}
				
				
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeconnectToPMEcardholder(con, result);
			
		}
		finally
		{
			closeconnectToPMEcardholder(con, result);
	
		}
		return message;
	}
	*/
	
	
	/*Method to log ResetBoud work details  */
	public String logResetBoudWorkDetails(String card_num,String fistname,String lastname,String email,
			String phone,String street,String cardExp,String onlinebalance,String cashwithdraw,
			String boundwork,String boundlimit,String userhotlist,String userblock,String pendboundWork,String initiator)
	{
		String query = "";
		String query1 = "";
		String query2 = "";
		String message = "";
		String empty = "''";
		ArrayList arr = new ArrayList();
		int output = -1;
		int output1 = -1;
		Connection con = null;
		Statement stat = null;
		Connection con1 = null;
		Statement stat1 = null;
		ResultSet result = null;
		ResultSet result1 = null;
		
		try
		{
			
	
			con = connectToPMEcardholder();
			stat = con.createStatement();	
		
		
			query = "select  * from E_CARD_PENDING_INFO where card_num = '"+card_num+"' and reset_bound_work ='"+pendboundWork+"' ";	
			result  = stat.executeQuery(query);
			if(result.next())
			{
				
				message = " Record Already Queue for  Authorization ";

			}
			else
			{
				
				query = "select  * from E_CARD_PENDING_INFO where card_num = '"+card_num+"' and (reset_bound_work ='0' OR reset_bound_work is null) ";
				result  = stat.executeQuery(query);
				if(result.next())
				{
					query = "update E_CARD_PENDING_INFO set Reset_Bound_Work = '"+pendboundWork+"', Card_Expiration = '"+cardExp+"', USER_INITIATOR ='"+initiator+"',Date_Initiated= getDate() where card_num ='"+card_num+"' ";	
					output = stat.executeUpdate(query);
					if(output > 0)
					{
						
						message = "Bound-work Reset Request has been successfully logged.";
						message +="\n";
						message +=" The Bound-work Reset will be effected when this request is authorized";
						con.commit();
					}
					
				}
				else
				{
					query = "INSERT INTO E_CARD_PENDING_INFO(CARD_NUM,FIRSTNAME,LASTNAME,EMAIL,PHONE,STREET,CARD_EXPIRATION,ONLINE_BALANCE,CASHWTHDW_LIMIT,BOUND_VALUE,BOUND_WORK,USER_HOTLIST,USER_LOCKED,RESET_BOUND_WORK,USER_INITIATOR,DATE_INITIATED)" +
							"values('"+card_num+"','"+fistname+"','"+lastname+"','"+email+"','"+phone+"','"+street+"','"+cardExp+"',"+onlinebalance+","+cashwithdraw+","+boundwork+","+boundlimit+",'"+userhotlist+"','"+userblock+"','"+pendboundWork+"','"+initiator+"',getDate())";
					
					output = stat.executeUpdate(query);
					if(output > 0) 
					{
						
						message = "Bound-work Reset Request has been successfully logged.";
						message +="\n";
						message +=" The Bound-work Reset will be effected when this request is authorized";
						con.commit();
					}
					else
					{
						message = "Bound-work Reset Request has Not been logged";
						con.rollback();
					}
				
				}
				
				
			}	
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeconnectToPMEcardholder(con, result);
		}
		finally
		{
			closeconnectToPMEcardholder(con, result);
	
		}
		return message;
	}
	
	
	/*Method to log ResetBoud limit details  */
	public String logResetBoudLimitDetails(String dbserver,String card_num,String fistname,String lastname,String email,
			String phone,String street,String cardExp,String onlinebalance,String cashwithdraw,
			String boundwork,String boundlimit,String userhotlist,String userblock,String pendingboundlimit,String initiator,String currentresetLimt)
	{
		String query = "";
		String query1 = "";
		String query2 = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		int output1 = -1;
		Connection con = null;
		Statement stat = null;
		Connection con1 = null;
		Statement stat1 = null;
		ResultSet result = null;
		ResultSet result1 = null;
		
		
		try
		{
			
	

		
			con = connectToPMEcardholder();
			stat = con.createStatement();	
		
			query = "select  * from E_CARD_PENDING_INFO where card_num = '"+card_num+"' and reset_bound_Limit = '"+pendingboundlimit+"' ";	
			result  = stat.executeQuery(query);
			if(result.next())
			{
				
				message = " Record Already Queue for  Authorization ";

			}
			else
			{
				query = "select  * from E_CARD_PENDING_INFO where card_num = '"+card_num+"' and (reset_bound_Limit = '0' OR reset_bound_Limit is null) ";
				result  = stat.executeQuery(query);
				if(result.next())
				{
					query = "update E_CARD_PENDING_INFO set Reset_Bound_Limit = '"+pendingboundlimit+"', CARD_EXPIRATION = '"+cardExp+"', BOUND_VALUE = "+boundlimit+" , CURRENT_VALUE='"+currentresetLimt+"', USER_INITIATOR ='"+initiator+"',Date_Initiated= getDate()  where card_num = '"+card_num+"'  ";	
					output = stat.executeUpdate(query);
					if(output > 0)
					{
						
						message = "Bound-Limit Reset Request has been successfully logged.";
						message +="\n";
						message +=" The Bound-Limit Reset will be effected when this request is authorized";
						con.commit();
					}
					
				}
				else
				{
					
					query = "INSERT INTO E_CARD_PENDING_INFO(CARD_NUM,FIRSTNAME,LASTNAME,EMAIL,PHONE,STREET,CARD_EXPIRATION,ONLINE_BALANCE,CASHWTHDW_LIMIT,BOUND_VALUE,BOUND_WORK,USER_HOTLIST,USER_LOCKED,RESET_BOUND_LIMIT,USER_INITIATOR,DATE_INITIATED,DB_SERVER,CURRENT_VALUE)" +
							"values('"+card_num+"','"+fistname+"','"+lastname+"','"+email+"','"+phone+"','"+street+"','"+cardExp+"',"+onlinebalance+","+cashwithdraw+","+boundlimit+","+boundwork+",'"+userhotlist+"','"+userblock+"','"+pendingboundlimit+"','"+initiator+"',getDate(),'"+dbserver+"','"+currentresetLimt+"')";
				
					output = stat.executeUpdate(query);
			
					if(output > 0) 
					{
						
						message = "Bound-Limit Reset Request has been successfully logged.";
						message +="\n";
						message +=" The Bound-Limit Reset will be effected when this request is authorized";
						con.commit();
					}
					else
					{
						message = "Bound-Limit Reset Request has Not been logged";
						con.rollback();
					}
				
				}
				
				
			}	
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeconnectToPMEcardholder(con, result);
			
		}
		finally
		{
			closeconnectToPMEcardholder(con, result);
			
		}
		return message;
	}
	
	
	/* Method to log Reset Pin details  */
	public String logResetPinDetails(String card_num,String fistname,String lastname,String email,
			String phone,String street,String cardExp,String onlinebalance,String cashwithdraw,
			String boundwork,String boundlimit,String userhotlist,String userblock,String pinReset,String initiator)
	{
		String query = "";
		String query1 = "";
		String query2 = "";
		String message = "";
		String empty = "''";
		ArrayList arr = new ArrayList();
		int output = -1;
		int output1 = -1;
		Connection con = null;
		Statement stat = null;
		Connection con1 = null;
		Statement stat1 = null;
		ResultSet result = null;
		ResultSet result1 = null;
		
		try
		{
			
			con = connectToPMEcardholder();
			stat = con.createStatement();	
		
		
			query = "select  * from E_CARD_PENDING_INFO where card_num = '"+card_num+"' and pin_reset ='"+pinReset+"' ";	
			result  = stat.executeQuery(query);
			System.out.println("query 1"+query);
			if(result.next())
			{
				
				message = " Record Already Queue for  Authorization ";

			}
			else
			{
				
				query = "select  * from E_CARD_PENDING_INFO where card_num = '"+card_num+"' and (pin_reset ='0' OR pin_reset is null) ";
				result  = stat.executeQuery(query);
				System.out.println("query 2"+query);
				if(result.next())
				{
					query = "update E_CARD_PENDING_INFO set pin_reset = '"+pinReset+"', Card_Expiration = '"+cardExp+"' , USER_INITIATOR ='"+initiator+"',Date_Initiated= getDate() where card_num ='"+card_num+"' ";	
					output = stat.executeUpdate(query);
					System.out.println("query 3"+query);
					if(output > 0)
					{
						
						message = "Pin Re-Generation Request has been successfully logged.";
						message +="\n";
						message +=" The Pin Re-Generation will be effected when this request is authorized";
						con.commit();
					}
					
				}
				else
				{
					query = "INSERT INTO E_CARD_PENDING_INFO(CARD_NUM,FIRSTNAME,LASTNAME,EMAIL,PHONE,STREET,CARD_EXPIRATION,ONLINE_BALANCE,CASHWTHDW_LIMIT,BOUND_VALUE,BOUND_WORK,USER_HOTLIST,USER_LOCKED,PIN_RESET,USER_INITIATOR,DATE_INITIATED)" +
							"values('"+card_num+"','"+fistname+"','"+lastname+"','"+email+"','"+phone+"','"+street+"','"+cardExp+"',"+onlinebalance+","+cashwithdraw+","+boundwork+","+boundlimit+",'"+userhotlist+"','"+userblock+"','"+pinReset+"','"+initiator+"',getDate())";
					System.out.println("query 4"+query);
					output = stat.executeUpdate(query);
					if(output > 0) 
					{
						
						message = "Pin Re-Generation Request has been successfully logged.";
						message +="\n";
						message +=" The Pin Re-Generation will be effected when this request is authorized";
						con.commit();
					}
					else
					{
						message = "Pin Re-Generation Request has Not been logged";
						con.rollback();
					}
				
				}
				
				
			}	
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeconnectToPMEcardholder(con, result);
		}
		finally
		{
			closeconnectToPMEcardholder(con, result);
	
		}
		return message;
	}
	
	
	
	/*Method to log ResetBoud limit details  */
	public String LogExtendCard_ExpirationDetails(String dbServer,String card_num,String fistname,String lastname,String email,
			String phone,String street,String cardExp,String onlinebalance,String cashwithdraw,
			String boundwork,String boundlimit,String userhotlist,String userblock,String extendExpPending,String initiator)
	{
		String query = "";
		String query1 = "";
		String query2 = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		int output1 = -1;
		Connection con = null;
		Statement stat = null;
		Connection con1 = null;
		Statement stat1 = null;
		ResultSet result = null;
		ResultSet result1 = null;
		
		try
		{
			
	

			
			con = connectToPMEcardholder();
			stat = con.createStatement();	
		
			query = "select * from E_CARD_PENDING_INFO where card_num = '"+card_num+"' and extend_expiration = '"+extendExpPending+"' ";	
			result  = stat.executeQuery(query);
			if(result.next())
			{
				
				message = " Record Already Queue for  Authorization ";

			}
			else
			{
				query = "select  * from E_CARD_PENDING_INFO where card_num = '"+card_num+"' and (extend_expiration ='0' OR extend_expiration is null) ";
				result  = stat.executeQuery(query);
				if(result.next())
				{
					query = "update E_CARD_PENDING_INFO set extend_expiration ='"+extendExpPending+"', Card_Expiration = '"+cardExp+"', USER_INITIATOR ='"+initiator+"',Date_Initiated= getDate()  where card_num ='"+card_num+"' ";	
					output = stat.executeUpdate(query);
					if(output > 0)
					{
						
						message = "Extend Card Expiration Request has been successfully logged.";
						message +="\n";
						message +=" The Extend Card Expiration Request will be effected when this request is authorized";
						con.commit();
					}
					
				}
				else
				{
					
					query = "INSERT INTO E_CARD_PENDING_INFO(CARD_NUM,FIRSTNAME,LASTNAME,EMAIL,PHONE,STREET,CARD_EXPIRATION,ONLINE_BALANCE,CASHWTHDW_LIMIT,BOUND_VALUE,BOUND_WORK,USER_HOTLIST,USER_LOCKED,EXTEND_EXPIRATION,USER_INITIATOR,DATE_INITIATED,DB_SERVER)" +
							"values('"+card_num+"','"+fistname+"','"+lastname+"','"+email+"','"+phone+"','"+street+"','"+cardExp+"',"+onlinebalance+","+cashwithdraw+","+boundwork+","+boundlimit+",'"+userhotlist+"','"+userblock+"','"+extendExpPending+"','"+initiator+"',getDate(),'"+dbServer+"')";
				
					output = stat.executeUpdate(query);
					if(output > 0) 
					{
						
						message = "Extend Card Expiration Request has been successfully logged.";
						message +="\n";
						message +=" The Extend Card Expiration Request will be effected when this request is authorized";
						con.commit();
					}
					else
					{
						message = "Extend Card Expiration Request has Not been logged";
						con.rollback();
					}
				
				}
				
				
			}	
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeconnectToPMEcardholder(con, result);
		}
		finally
		{
			closeconnectToPMEcardholder(con, result);
		}
		return message;
	}
	
	
	
	/*Method to log De-hotlist  details  */
	public String logDeHotlistedDetails(String dbServer,String card_num,String fistname,String lastname,String email,
			String phone,String street,String cardExp,String onlinebalance,String cashwithdraw,
			String boundwork,String boundlimit,String userhotlist,String userblock,String deholistPending,String initiator)
	{
		String query = "";
		String query1 = "";
		String query2 = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		int output1 = -1;
		Connection con = null;
		Statement stat = null;
		Connection con1 = null;
		Statement stat1 = null;
		ResultSet result = null;
		ResultSet result1 = null;
		
		
		try
		{
			
	

			
			con = connectToPMEcardholder();
			stat = con.createStatement();	
		
			query = "select * from E_CARD_PENDING_INFO where card_num = '"+card_num+"' and dehotlist ='"+deholistPending+"' ";	
			result  = stat.executeQuery(query);
			if(result.next())
			{
				
				message = " Record Already Queue for  Authorization ";

			}
			else
			{
				query = "select  * from E_CARD_PENDING_INFO where card_num ='"+card_num+"' and (dehotlist = '0' OR dehotlist is null) ";
				result  = stat.executeQuery(query);
				if(result.next())
				{
					query = "update E_CARD_PENDING_INFO set dehotlist ='"+deholistPending+"', Card_Expiration ='"+cardExp+"', USER_INITIATOR ='"+initiator+"',Date_Initiated= getDate()  where card_num ='"+card_num+"' ";	
					output = stat.executeUpdate(query);
					if(output > 0)
					{
						
						message = "De-Hotlist Request has been successfully logged.";
						message +="\n";
						message +=" De-Hotlist Request will be effected when this request is authorized";
						con.commit();
					
					}
					
				}
				else
				{
					
					query = "INSERT INTO E_CARD_PENDING_INFO(CARD_NUM,FIRSTNAME,LASTNAME,EMAIL,PHONE,STREET,CARD_EXPIRATION,ONLINE_BALANCE,CASHWTHDW_LIMIT,BOUND_VALUE,BOUND_WORK,USER_HOTLIST,USER_LOCKED,DEHOTLIST,USER_INITIATOR,DATE_INITIATED,DB_SERVER)" +
							"values('"+card_num+"','"+fistname+"','"+lastname+"','"+email+"','"+phone+"','"+street+"','"+cardExp+"',"+onlinebalance+","+cashwithdraw+","+boundwork+","+boundlimit+",'"+userhotlist+"','"+userblock+"','"+deholistPending+"','"+initiator+"',getDate(),'"+dbServer+"')";
				
					output = stat.executeUpdate(query);
			
					if(output > 0) 
					{
						
						message = "De-Hotlist Request has been successfully logged.";
						message +="\n";
						message +=" De-Hotlist Request will be effected when this request is authorized";
						con.commit();
					}
					else
					{
						message = "De-Hotlist Request has Not been logged";
						con.rollback();
					}
				
				}
				
				
			}	
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeconnectToPMEcardholder(con, result);
		}
		finally
		{
			closeconnectToPMEcardholder(con, result);
		}
		return message;
		
	}
	
	
	
	
	
	
	/*Method to create pending Reset Bound Limit */
	public String createPending_Reset_Bound_Limit(String card_num,String boundLimit,String initiator)
	{
		String query = "";
		String query1 = "";
		String query2 = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		int output1 = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select  * from E_CARD_PENDING_AUTH where card_num = '"+card_num+"' ";
			result  = stat.executeQuery(query);
			if(result.next())
			{
			
				
				
				query = "update E_CARD_PENDING_AUTH set Reset_Bound_Limit = "+boundLimit+", USER_INITIATOR ='"+initiator+"',Date_Initiated = getDate()  where card_num = '"+card_num+"'  ";	
				
				output1 = stat.executeUpdate(query);
				System.out.println("Bound limit values "+output1);
				if(output1 > 0)
				{
					//message = "Record Awaiting Authorization";
					
					message = "Bound-Limit Reset Request has been successfully logged.";
					message +="\n";
					message +=" The Bound-Limit Reset will be effected when this request is authorized";
				}
			}
			else
			{
				
			    
				query1 = "INSERT INTO E_CARD_PENDING_AUTH (CARD_NUM, CARD_TYPE, CARD_PIN, CARD_ACCOUNT,CARD_EXPIRATION,FIRSTNAME,LASTNAME,COMPANY,STREET,CITY,STATE,ZIP,COUNTRY," +
						"PHONE,FAX,EMAIL,MINBAL_ACTIVE,MINBAL_VALUE,BOUND_FREQ,BOUND_VALUE,BOUND_WORK,BIRTH_DATE,MARITAL_STATUS,NATIONALITY,DEP_CHILDREN,DEP_HIGHINST,RESIDENCE_TYPE," +
						"ACCT_PURPOSE,PRIM_INDUSTRY,PRIM_JOBFUNCTION,EMP_STATUS,ANNUAL_INCOME,INTERNET_ACCESS,ISSUER_CODE,SUB_CODE,CHANGE_PIN,HINTQUESTION,HINTANSWER,USER_LOCKED," +
						"USER_HOTLIST,PIN_MISSED,LAST_USED,MODIFIED,CREATED,ONLINE_DATE,ONLINE_BALANCE,OFFLINE_DATE,OFFLINE_BALANCE,SOCIAL_SECURITYNO,REGCODE,DEFAULT_PIN,MAX_LOADBALANCE," +
						"PAYFEE,TRACK2,CONTROL_CODE,BASE_CURRENCY,CARD_ALIAS,SMS_OPTION,CASHWTHDW_LASTDATE,CASHWTHDW_LIMIT,CASH_UTILISED,CASHWTHDW_VELOCITY,CREATED_BY,CLIENT_ID," +
						"ESA_STATUS,PIN_STATUS,mail_sent,VCHECK,CONTROL_ID,POS_LIMIT,POS_VELOCITY,CHIP_ENABLED,CARD_SERIAL,BATCH_ID,POS_UTILISED,POS_LASTDATE,CVV," +
						"ICVV,AID,AGREGATOR_ID,SERVICE_CODE)" +
						"SELECT CARD_NUM, CARD_TYPE, CARD_PIN, CARD_ACCOUNT,CARD_EXPIRATION,FIRSTNAME,LASTNAME,COMPANY,STREET,CITY,STATE,ZIP,COUNTRY," +
						"PHONE,FAX,EMAIL,MINBAL_ACTIVE,MINBAL_VALUE,BOUND_FREQ,BOUND_VALUE,BOUND_WORK,BIRTH_DATE,MARITAL_STATUS,NATIONALITY,DEP_CHILDREN,DEP_HIGHINST,RESIDENCE_TYPE," +
						"ACCT_PURPOSE,PRIM_INDUSTRY,PRIM_JOBFUNCTION,EMP_STATUS,ANNUAL_INCOME,INTERNET_ACCESS,ISSUER_CODE,SUB_CODE,CHANGE_PIN,HINTQUESTION,HINTANSWER,USER_LOCKED," +
						"USER_HOTLIST,PIN_MISSED,LAST_USED,MODIFIED,CREATED,ONLINE_DATE,ONLINE_BALANCE,OFFLINE_DATE,OFFLINE_BALANCE,SOCIAL_SECURITYNO,REGCODE,DEFAULT_PIN,MAX_LOADBALANCE," +
						"PAYFEE,TRACK2,CONTROL_CODE,BASE_CURRENCY,CARD_ALIAS,SMS_OPTION,CASHWTHDW_LASTDATE,CASHWTHDW_LIMIT,CASH_UTILISED,CASHWTHDW_VELOCITY,CREATED_BY,CLIENT_ID," +
						"ESA_STATUS,PIN_STATUS,mail_sent,VCHECK,CONTROL_ID,POS_LIMIT,POS_VELOCITY,CHIP_ENABLED,CARD_SERIAL,BATCH_ID,POS_UTILISED,POS_LASTDATE,CVV," +
						"ICVV,AID,AGREGATOR_ID,SERVICE_CODE FROM E_CARDHOLDER where card_num = '"+card_num+"'";
				
			 
				
				
				query2 = "update E_CARD_PENDING_AUTH set Reset_Bound_Limit = "+boundLimit+", USER_INITIATOR='"+initiator+"',Date_Initiated = getDate() where card_num = '"+card_num+"' ";	

				System.out.println("Rest Bound Limit  query1 " + query1);
				System.out.println("Rest Bound Limit  query2 " + query2);
				output = stat.executeUpdate(query1);
				output1 = stat.executeUpdate(query2);
			
				if(output > 0) 
				{
					
					//message = "Bound-work Reset Request has been successfully logged. /n The Bound-work Reset will be effected when this request is authorized";
					//message = " Record Successfully Awaiting Authorization ";
					
					message = "Bound-Limit Reset Request has been successfully logged.";
					message +="\n";
					message +=" The Bound-Limit Reset will be effected when this request is authorized";
					con.commit();
				}
				else
				{
					message = "Bound-Limit Reset Request has Not been logged";
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
	
	
	
	
	/*
	 * 
	 */
	public String createPending_BlockCard(String card_num,String blockPending)
	{
		String query = "";
		String query1 = "";
		//String query2 = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		int output1 = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select  * from E_CARD_PENDING_AUTH where card_num = '"+card_num+"'   ";
			result  = stat.executeQuery(query);
			if(result.next())
			{
				query = "update E_CARD_PENDING_AUTH set Block_Status = '"+blockPending+"' where card_num = '"+card_num+"' ";	
			
				output1 = stat.executeUpdate(query);
				System.out.println("output values "+output1);
				if(output1 > 0)
				{
					message = "Records Pending for Authorization";
				}
				
			}
			else
			{
				query = "INSERT INTO E_CARD_PENDING_AUTH (CARD_NUM, CARD_TYPE, CARD_PIN, CARD_ACCOUNT,CARD_EXPIRATION,FIRSTNAME,LASTNAME,COMPANY,STREET,CITY,STATE,ZIP,COUNTRY," +
						"PHONE,FAX,EMAIL,MINBAL_ACTIVE,MINBAL_VALUE,BOUND_FREQ,BOUND_VALUE,BOUND_WORK,BIRTH_DATE,MARITAL_STATUS,NATIONALITY,DEP_CHILDREN,DEP_HIGHINST,RESIDENCE_TYPE," +
						"ACCT_PURPOSE,PRIM_INDUSTRY,PRIM_JOBFUNCTION,EMP_STATUS,ANNUAL_INCOME,INTERNET_ACCESS,ISSUER_CODE,SUB_CODE,CHANGE_PIN,HINTQUESTION,HINTANSWER,USER_LOCKED," +
						"USER_HOTLIST,PIN_MISSED,LAST_USED,MODIFIED,CREATED,ONLINE_DATE,ONLINE_BALANCE,OFFLINE_DATE,OFFLINE_BALANCE,SOCIAL_SECURITYNO,REGCODE,DEFAULT_PIN,MAX_LOADBALANCE," +
						"PAYFEE,TRACK2,CONTROL_CODE,BASE_CURRENCY,CARD_ALIAS,SMS_OPTION,CASHWTHDW_LASTDATE,CASHWTHDW_LIMIT,CASH_UTILISED,CASHWTHDW_VELOCITY,CREATED_BY,CLIENT_ID," +
						"ESA_STATUS,PIN_STATUS,mail_sent,VCHECK,CONTROL_ID,POS_LIMIT,POS_VELOCITY,CHIP_ENABLED,CARD_SERIAL,BATCH_ID,POS_UTILISED,POS_LASTDATE,CVV," +
						"ICVV,AID,AGREGATOR_ID,SERVICE_CODE)" +
						"SELECT CARD_NUM, CARD_TYPE, CARD_PIN, CARD_ACCOUNT,CARD_EXPIRATION,FIRSTNAME,LASTNAME,COMPANY,STREET,CITY,STATE,ZIP,COUNTRY," +
						"PHONE,FAX,EMAIL,MINBAL_ACTIVE,MINBAL_VALUE,BOUND_FREQ,BOUND_VALUE,BOUND_WORK,BIRTH_DATE,MARITAL_STATUS,NATIONALITY,DEP_CHILDREN,DEP_HIGHINST,RESIDENCE_TYPE," +
						"ACCT_PURPOSE,PRIM_INDUSTRY,PRIM_JOBFUNCTION,EMP_STATUS,ANNUAL_INCOME,INTERNET_ACCESS,ISSUER_CODE,SUB_CODE,CHANGE_PIN,HINTQUESTION,HINTANSWER,USER_LOCKED," +
						"USER_HOTLIST,PIN_MISSED,LAST_USED,MODIFIED,CREATED,ONLINE_DATE,ONLINE_BALANCE,OFFLINE_DATE,OFFLINE_BALANCE,SOCIAL_SECURITYNO,REGCODE,DEFAULT_PIN,MAX_LOADBALANCE," +
						"PAYFEE,TRACK2,CONTROL_CODE,BASE_CURRENCY,CARD_ALIAS,SMS_OPTION,CASHWTHDW_LASTDATE,CASHWTHDW_LIMIT,CASH_UTILISED,CASHWTHDW_VELOCITY,CREATED_BY,CLIENT_ID," +
						"ESA_STATUS,PIN_STATUS,mail_sent,VCHECK,CONTROL_ID,POS_LIMIT,POS_VELOCITY,CHIP_ENABLED,CARD_SERIAL,BATCH_ID,POS_UTILISED,POS_LASTDATE,CVV," +
						"ICVV,AID,AGREGATOR_ID,SERVICE_CODE FROM E_CARDHOLDER where card_num = '"+card_num+"'";
						
				query1 = "update E_CARD_PENDING_AUTH set Block_Status = '"+blockPending+"',user_locked ='"+blockPending+"' where card_num = '"+card_num+"' ";
				
				

				System.out.println("createHotlist  query1 " + query);
				//System.out.println("createHotlist  query2 " + query2);
				output = stat.executeUpdate(query);
				output1 = stat.executeUpdate(query1);
				
				if(output > 0) 
				{
					message = " Records successfully Pending for Authorization ";
					con.commit();
				}
				else
				{
					message = "Record not Pending Yet";
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
	
	 
	public String createPending_UNBlockCard(String card_num,String unBlockStatus)
	{
		String query = "";
		String query1 = "";
		//String query2 = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		int output1 = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			query = "select  * from E_CARD_PENDING_AUTH where card_num = '"+card_num+"'   ";
			result  = stat.executeQuery(query);
			if(result.next())
			{
				query = "update E_CARD_PENDING_AUTH set Block_Status = '"+unBlockStatus+"' where card_num = '"+card_num+"' ";	
			
				output1 = stat.executeUpdate(query);
				System.out.println("output values "+output1);
				if(output1 > 0)
				{
					message = "Record Pending for Authorization";
				}
				
			}
			else
			{
				query = "INSERT INTO E_CARD_PENDING_AUTH (CARD_NUM, CARD_TYPE, CARD_PIN, CARD_ACCOUNT,CARD_EXPIRATION,FIRSTNAME,LASTNAME,COMPANY,STREET,CITY,STATE,ZIP,COUNTRY," +
						"PHONE,FAX,EMAIL,MINBAL_ACTIVE,MINBAL_VALUE,BOUND_FREQ,BOUND_VALUE,BOUND_WORK,BIRTH_DATE,MARITAL_STATUS,NATIONALITY,DEP_CHILDREN,DEP_HIGHINST,RESIDENCE_TYPE," +
						"ACCT_PURPOSE,PRIM_INDUSTRY,PRIM_JOBFUNCTION,EMP_STATUS,ANNUAL_INCOME,INTERNET_ACCESS,ISSUER_CODE,SUB_CODE,CHANGE_PIN,HINTQUESTION,HINTANSWER,USER_LOCKED," +
						"USER_HOTLIST,PIN_MISSED,LAST_USED,MODIFIED,CREATED,ONLINE_DATE,ONLINE_BALANCE,OFFLINE_DATE,OFFLINE_BALANCE,SOCIAL_SECURITYNO,REGCODE,DEFAULT_PIN,MAX_LOADBALANCE," +
						"PAYFEE,TRACK2,CONTROL_CODE,BASE_CURRENCY,CARD_ALIAS,SMS_OPTION,CASHWTHDW_LASTDATE,CASHWTHDW_LIMIT,CASH_UTILISED,CASHWTHDW_VELOCITY,CREATED_BY,CLIENT_ID," +
						"ESA_STATUS,PIN_STATUS,mail_sent,VCHECK,CONTROL_ID,POS_LIMIT,POS_VELOCITY,CHIP_ENABLED,CARD_SERIAL,BATCH_ID,POS_UTILISED,POS_LASTDATE,CVV," +
						"ICVV,AID,AGREGATOR_ID,SERVICE_CODE)" +
						"SELECT CARD_NUM, CARD_TYPE, CARD_PIN, CARD_ACCOUNT,CARD_EXPIRATION,FIRSTNAME,LASTNAME,COMPANY,STREET,CITY,STATE,ZIP,COUNTRY," +
						"PHONE,FAX,EMAIL,MINBAL_ACTIVE,MINBAL_VALUE,BOUND_FREQ,BOUND_VALUE,BOUND_WORK,BIRTH_DATE,MARITAL_STATUS,NATIONALITY,DEP_CHILDREN,DEP_HIGHINST,RESIDENCE_TYPE," +
						"ACCT_PURPOSE,PRIM_INDUSTRY,PRIM_JOBFUNCTION,EMP_STATUS,ANNUAL_INCOME,INTERNET_ACCESS,ISSUER_CODE,SUB_CODE,CHANGE_PIN,HINTQUESTION,HINTANSWER,USER_LOCKED," +
						"USER_HOTLIST,PIN_MISSED,LAST_USED,MODIFIED,CREATED,ONLINE_DATE,ONLINE_BALANCE,OFFLINE_DATE,OFFLINE_BALANCE,SOCIAL_SECURITYNO,REGCODE,DEFAULT_PIN,MAX_LOADBALANCE," +
						"PAYFEE,TRACK2,CONTROL_CODE,BASE_CURRENCY,CARD_ALIAS,SMS_OPTION,CASHWTHDW_LASTDATE,CASHWTHDW_LIMIT,CASH_UTILISED,CASHWTHDW_VELOCITY,CREATED_BY,CLIENT_ID," +
						"ESA_STATUS,PIN_STATUS,mail_sent,VCHECK,CONTROL_ID,POS_LIMIT,POS_VELOCITY,CHIP_ENABLED,CARD_SERIAL,BATCH_ID,POS_UTILISED,POS_LASTDATE,CVV," +
						"ICVV,AID,AGREGATOR_ID,SERVICE_CODE FROM E_CARDHOLDER where card_num = '"+card_num+"'";
						
				query1 = "update E_CARD_PENDING_AUTH set Block_Status = '"+unBlockStatus+"' where card_num = '"+card_num+"' ";
				
				

				System.out.println("createHotlist  query1 " + query);
				//System.out.println("createHotlist  query2 " + query2);
				output = stat.executeUpdate(query);
				output1 = stat.executeUpdate(query1);
				
				if(output > 0) 
				{
					message = " Record Pending for Authorization ";
					con.commit();
				}
				else
				{
					message = "Record not Pending Yet";
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
	
	
	/*
	 * This method is to get Card Type Details
	 */
	public ArrayList getCardType()
	{		
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION tran = null;
		CardType cardtype = null;
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
		
			query = "select Card_Type , Card_Name  from E_CARDTYPE ";
			
			System.out.println("getCardType " + query);
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				cardtype = new CardType();
				cardtype.setCardType(""+result.getObject(1));
				cardtype.setCardName(""+result.getObject(2));
				arr.add(cardtype);
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
	
	/*
	 * This methos is to get Card control details
	 */
	public ArrayList getCardControl()
	{		
		String query = "";
		ArrayList arr = new ArrayList();
		E_TRANSACTION tran = null;
		CardControl control = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		double d = 0.0;
		String str = "";
		
		try
		{
			con = connectPocketMoniEcardDB();
			stat = con.createStatement();
		
			query = "select * from E_CARDCONTROL";
			
			System.out.println("getCard Control " + query);
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				control = new CardControl();
				control.setCardControlId(""+result.getObject(1));
				control.setControlName(""+result.getObject(2));
				arr.add(control);
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
			
			query = "select card_num, phone, lastname, firstname, street, state  from e_cardholder where card_num like '" + card_num +"%' and " +
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
	
	
	/*Method to update card holder details
	 * 
	 */
	public ArrayList getCardHolders_Update_Details(String card_num, String cardScheme,String Start_dt,String end_dt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		CardHolder cholder = null;
		int counter = 0;
		HashNumber hn = new HashNumber();
		Connection con = null, con1 = null;
		Statement stat = null, stat1 = null;
		ResultSet result = null, result1 = null;
		
		String apostrophe = "'";
		try
		{
			
			
			if(cardScheme.indexOf(":")>0)
			{
				String m[] = cardScheme.split(":");
				cardScheme = "";
				for(int i=0;i<m.length;i++)
				{
					cardScheme += apostrophe + m[i] + apostrophe + ",";
				}
				
				cardScheme = cardScheme.substring(0, cardScheme.lastIndexOf(","));
			}
			else
			{
				cardScheme = apostrophe + cardScheme + apostrophe;
			}
			
			
			con = connectToECard();
			stat = con.createStatement();
			
			con1 = connectPocketMoniEcardDB();
			stat1 = con1.createStatement();
			
			if(card_num.trim().length() > 0)
			{
				
				query = "select card_num, phone, lastname, firstname, street, state, card_expiration, email, bound_work, bound_value," +
						" birth_date, change_pin, user_locked, user_hotlist, pin_missed, last_used, modified, created," +
						" online_balance,payfee, cashwthdw_limit, client_id,issuer_code,card_type,MinBal_value,Control_Id" +
						" from e_cardholder where card_num like '"+card_num+"%' and substring(card_num,1,6) in("+cardScheme+") " ;
				
			}
			else
			{
				query = "select card_num, phone, lastname, firstname, street, state, card_expiration, email, bound_work, bound_value," +
						" birth_date, change_pin, user_locked, user_hotlist, pin_missed, last_used, modified, created," +
						" online_balance,payfee, cashwthdw_limit, client_id,issuer_code,card_type,MinBal_value,Control_Id" +
						" from e_cardholder where card_num like '"+card_num+"%' and substring(card_num,1,6) in("+cardScheme+")" +
						" and Created between ('"+Start_dt+"') and ('"+end_dt+"') ";
				
			}
	
			System.out.println("query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
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
				cholder.setIssuer_code(""+result.getObject(23));
				cholder.setCardType(""+result.getObject(24));
				cholder.setMinBalance(""+result.getObject(25));
				cholder.setControlId(""+result.getObject(26));
				
				arr.add(cholder);
			}
			
			
			
			
			/*
			if(dbServer.equals("1"))
			{
				
				query = "select card_num, phone, lastname, firstname, street, state, card_expiration, email, bound_work, bound_value," +
						" birth_date, change_pin, user_locked, user_hotlist, pin_missed, last_used, modified, created," +
						" online_balance,payfee, cashwthdw_limit, client_id,issuer_code,card_type,MinBal_value,Control_Id" +
						" from e_cardholder where card_num like '"+card_num+"%' and substring(card_num,1,6) in("+cardScheme+")" +
						" and Created between ('"+Start_dt+"') and ('"+end_dt+"') ";
		
				
				System.out.println("query " + query);
				result = stat.executeQuery(query);
				while(result.next())
				{
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
					cholder.setIssuer_code(""+result.getObject(23));
					cholder.setCardType(""+result.getObject(24));
					cholder.setMinBalance(""+result.getObject(25));
					cholder.setControlId(""+result.getObject(26));
					
					arr.add(cholder);
				}
				
			}
			else if(dbServer.equals("2"))
			{
				query = "select card_num, phone, lastname, firstname, street, state, card_expiration, email, bound_work, bound_value," +
						" birth_date, change_pin, user_locked, user_hotlist, pin_missed, last_used, modified, created," +
						" online_balance,payfee, cashwthdw_limit, client_id,issuer_code,card_type,MinBal_value,Control_Id" +
						" from e_cardholder where card_num like '"+card_num+"%' and substring(card_num,1,6) in("+cardScheme+")" +
						" and Created between ('"+Start_dt+"') and ('"+end_dt+"') ";
		
				
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
					cholder.setIssuer_code(""+result1.getObject(23));
					cholder.setCardType(""+result1.getObject(24));
					cholder.setMinBalance(""+result1.getObject(25));
					cholder.setControlId(""+result1.getObject(26));
					
					arr.add(cholder);
				
				}
			
			}*/
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result, result);
			closeConnectionPocketMoniEcardDB(con1, result);
		}
		finally
		{
			closeConnectionECard(con1, result, result);
			closeConnectionPocketMoniEcardDB(con1, result);
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
					" online_balance,payfee, cashwthdw_limit, client_id,issuer_code,card_type,MinBal_value,Control_Id" +
					" from e_cardholder where card_num = '" +card_num +"' ";
	
			
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
				cholder.setIssuer_code(""+result1.getObject(23));
				cholder.setCardType(""+result1.getObject(24));
				cholder.setMinBalance(""+result1.getObject(25));
				cholder.setControlId(""+result1.getObject(26));
				
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
	
	
	/*Method to view pending bound work details */
	public ArrayList getBoundWorkPending()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		CardHolder cholder = null;
		int counter = 0;
		HashNumber hn = new HashNumber();
		Connection con = null;
		Statement stat = null;
		ResultSet result = null ;
		
		try
		{

			con = connectToPMEcardholder();
			stat = con.createStatement();


			query = " select card_num, phone, lastname, firstname, street,card_expiration, email, bound_work, bound_value," +
					" user_locked, user_hotlist, online_balance, cashwthdw_limit,User_Initiator," +
					" Date_Initiated,Reset_Bound_Work,Reset_Bound_Limit from E_CARD_PENDING_INFO where reset_bound_work = 'Queue' ";
					
					
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
				cholder.setCard_expiration(""+result.getObject(6));
				cholder.setEmail(""+result.getObject(7));
				cholder.setBound_work(""+result.getObject(8));
				cholder.setBound_value(""+result.getObject(9));
				cholder.setUser_locked(""+result.getObject(10));
				cholder.setUser_hotlist(""+result.getObject(11));
				cholder.setOnline_balance(""+result.getObject(12));
				cholder.setCashwthdw_limit(""+result.getObject(13));
				cholder.setInitiator(""+result.getObject(14));
				cholder.setDateInitiated(""+result.getObject(15));
				cholder.setResetBound(""+result.getObject(16));
				cholder.setResetLimit(""+result.getObject(17));

				arr.add(cholder);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeconnectToPMEcardholder(con, result);
		}
		finally
		{
			closeconnectToPMEcardholder(con, result);
	
		}
		return arr;
	}
	
	
	/*Method to List cms database server  */
	public ArrayList getCMSSERVER()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		CMSSERVER servers  = null;
		int counter = 0;
		HashNumber hn = new HashNumber();
		Connection con = null;
		Statement stat = null;
		ResultSet result = null ;
		
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();

			query = " select server_name,server_ip,server_status from cms_server ";
					
			System.out.println("query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{		
				servers = new CMSSERVER();
				servers.setServeName(""+result.getObject(1));
				servers.setServerIP(""+result.getObject(2));
				servers.setStatus(""+result.getObject(3));
				arr.add(servers);
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
	
	/*Method to pend reset bound work*/
	public ArrayList getReset_Bound_Limit_Pending()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		CardHolder cholder = null;
		int counter = 0;
		HashNumber hn = new HashNumber();
		Connection con = null;
		Statement stat = null;
		ResultSet result = null ;
		
		try
		{
			
			con = connectToPMEcardholder();
			stat = con.createStatement();
			
			

			query = " select card_num, phone, lastname, firstname, street,card_expiration, email, bound_work, bound_value," +
					" user_locked, user_hotlist, online_balance, cashwthdw_limit,User_Initiator," +
					" Date_Initiated,Reset_Bound_Work,Reset_Bound_Limit,DB_SERVER,Current_Value from E_CARD_PENDING_INFO where reset_bound_limit = 'Queue' ";
					
					
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
				cholder.setCard_expiration(""+result.getObject(6));
				cholder.setEmail(""+result.getObject(7));
				cholder.setBound_work(""+result.getObject(8));
				cholder.setBound_value(""+result.getObject(9));
				cholder.setUser_locked(""+result.getObject(10));
				cholder.setUser_hotlist(""+result.getObject(11));
				cholder.setOnline_balance(""+result.getObject(12));
				cholder.setCashwthdw_limit(""+result.getObject(13));
				cholder.setInitiator(""+result.getObject(14));
				cholder.setDateInitiated(""+result.getObject(15));
				cholder.setResetBound(""+result.getObject(16));
				cholder.setResetLimit(""+result.getObject(17));
				cholder.setServerStatus(""+result.getObject(18));
				cholder.setCurrentStatus(""+result.getObject(19));

				arr.add(cholder);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeconnectToPMEcardholder(con, result);
		}
		finally
		{
	
			closeconnectToPMEcardholder(con, result);
		}
		return arr;
	}
	
	
	/*Method to pend Extended card Expiration*/
	public ArrayList getReset_Extend_CardExpiration()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		CardHolder cholder = null;
		int counter = 0;
		HashNumber hn = new HashNumber();
		Connection con = null;
		Statement stat = null;
		ResultSet result = null ;
		
		try
		{
			
			con = connectToPMEcardholder();
			stat = con.createStatement();
			
			query = " select card_num, phone, lastname, firstname, street,card_expiration, email, bound_work, bound_value," +
					" user_locked, user_hotlist, online_balance, cashwthdw_limit,User_Initiator," +
					" Date_Initiated,Reset_Bound_Work,Reset_Bound_Limit,db_server from E_CARD_PENDING_INFO where extend_expiration = 'Queue' ";
					
					
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
				cholder.setCard_expiration(""+result.getObject(6));
				cholder.setEmail(""+result.getObject(7));
				cholder.setBound_work(""+result.getObject(8));
				cholder.setBound_value(""+result.getObject(9));
				cholder.setUser_locked(""+result.getObject(10));
				cholder.setUser_hotlist(""+result.getObject(11));
				cholder.setOnline_balance(""+result.getObject(12));
				cholder.setCashwthdw_limit(""+result.getObject(13));
				cholder.setInitiator(""+result.getObject(14));
				cholder.setDateInitiated(""+result.getObject(15));
				cholder.setResetBound(""+result.getObject(16));
				cholder.setResetLimit(""+result.getObject(17));
				cholder.setServerStatus(""+result.getObject(18));

				arr.add(cholder);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeconnectToPMEcardholder(con, result);
		}
		finally
		{
	
			closeconnectToPMEcardholder(con, result);
		}
		return arr;
	}
	
	
	/*Method to get already pending card details before authorization*/
	public ArrayList getDehotlistedDetails()
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
			
			con = connectToPMEcardholder();
			stat = con.createStatement();
			
			query = " select card_num, phone, lastname, firstname, street,card_expiration, email, bound_work, bound_value," +
					" user_locked, user_hotlist, online_balance, cashwthdw_limit,User_Initiator," +
					" Date_Initiated,Reset_Bound_Work,Reset_Bound_Limit,db_server from E_CARD_PENDING_INFO where dehotlist = 'Queue' ";
					
					
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
				cholder.setCard_expiration(""+result.getObject(6));
				cholder.setEmail(""+result.getObject(7));
				cholder.setBound_work(""+result.getObject(8));
				cholder.setBound_value(""+result.getObject(9));
				cholder.setUser_locked(""+result.getObject(10));
				cholder.setUser_hotlist(""+result.getObject(11));
				cholder.setOnline_balance(""+result.getObject(12));
				cholder.setCashwthdw_limit(""+result.getObject(13));
				cholder.setInitiator(""+result.getObject(14));
				cholder.setDateInitiated(""+result.getObject(15));
				cholder.setResetBound(""+result.getObject(16));
				cholder.setResetLimit(""+result.getObject(17));
				cholder.setServerStatus(""+result.getObject(18));

				arr.add(cholder);
			}
		}
		catch(Exception ex)
		{
			closeconnectToPMEcardholder(con, result);
		}
		finally
		{
			closeconnectToPMEcardholder(con, result);
		}
		return arr;
	}
	
	
	/*Method to get already pending Pin details */
	public ArrayList getPendingPinReGenerationDetails()
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
			
			con = connectToPMEcardholder();
			stat = con.createStatement();
			
			query = " select card_num, phone, lastname, firstname, street,card_expiration, email, bound_work, bound_value," +
					" user_locked, user_hotlist, online_balance, cashwthdw_limit,User_Initiator," +
					" Date_Initiated,Reset_Bound_Work,Reset_Bound_Limit,db_server from E_CARD_PENDING_INFO where pin_reset = 'Queue' ";
					
					
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
				cholder.setCard_expiration(""+result.getObject(6));
				cholder.setEmail(""+result.getObject(7));
				cholder.setBound_work(""+result.getObject(8));
				cholder.setBound_value(""+result.getObject(9));
				cholder.setUser_locked(""+result.getObject(10));
				cholder.setUser_hotlist(""+result.getObject(11));
				cholder.setOnline_balance(""+result.getObject(12));
				cholder.setCashwthdw_limit(""+result.getObject(13));
				cholder.setInitiator(""+result.getObject(14));
				cholder.setDateInitiated(""+result.getObject(15));
				cholder.setResetBound(""+result.getObject(16));
				cholder.setResetLimit(""+result.getObject(17));
				cholder.setServerStatus(""+result.getObject(18));

				arr.add(cholder);
			}
		}
		catch(Exception ex)
		{
			closeconnectToPMEcardholder(con, result);
		}
		finally
		{
			closeconnectToPMEcardholder(con, result);
		}
		return arr;
	}
	
	
	/*Method to get card holders info*/
	public ArrayList getCardHolderDetails_Block_Pending(String blockStatus)
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
			
			con = connectToECard();
			stat = con.createStatement();
			
	
			query = "select card_num, phone, lastname, firstname, street, state, card_expiration, email, bound_work, bound_value," +
					" birth_date, change_pin, user_locked, user_hotlist, pin_missed, last_used, modified, created," +
					" online_balance,payfee, cashwthdw_limit, client_id,issuer_code,card_type,MinBal_value,Control_Id" +
					" from E_CARD_PENDING_AUTH where Block_Status = '"+blockStatus+"' ";
			
			System.out.println("query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
			
				cholder = new CardHolder();
		
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
				cholder.setIssuer_code(""+result.getObject(23));
				cholder.setCardType(""+result.getObject(24));
				cholder.setMinBalance(""+result.getObject(25));
				cholder.setControlId(""+result.getObject(26));
				
				arr.add(cholder);
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
	
	/*
	/*
	 * 
	 * This method  is to update CardHolder Information
	 */
	public ArrayList UpdateCardHolder(String dbServer,String cardNum,String cardTyp,String controlId,double minBal,double boundwork,String firstName,String lastName,String mobile, String Street, String email)
	{
		
		Connection con = null;
		Statement stat = null;
		ResultSet result1 = null;
		int output = -1 ;
		String message = "";
		String query = "";
		CardHolder cholder = null;
		ArrayList arr = new ArrayList();
		
		try
		{
			if(dbServer.equals("1"))  // .57
			{
				
				con = connectToTestECard();   
				stat = con.createStatement();
		
			}
			else if(dbServer.equals("2"))    // .133
			{
				con = connectToPMEcardholder();
				stat = con.createStatement();
		
				
			}
			else if(dbServer.equals("3"))  //  .130
			{
				con = connectToECard();
				stat = con.createStatement();	
			
				
			}
	
			output = stat.executeUpdate("update e_cardholder set Card_Type = '"+cardTyp+"',Control_Id = '"+controlId+"',MINBAL_VALUE="+minBal+",BOUND_WORK="+boundwork+", firstname= '"+firstName+"' , lastname = '"+lastName+"'," +
										" Phone = '"+mobile+"', street = '"+Street+"', email ='"+email+"' where card_num = '"+cardNum+"' ");
			
			query = "select card_num, phone, lastname, firstname, street, state, card_expiration, email, bound_work, bound_value," +
					" birth_date, change_pin, user_locked, user_hotlist, pin_missed, last_used, modified, created," +
					" online_balance,payfee, cashwthdw_limit, client_id,issuer_code,MINBAL_VALUE from e_cardholder where card_num = '"+cardNum+"' ";
           
	         result1 = stat.executeQuery(query);
			if(output > 0 )
               {
				message = "SUCCESS";
            	   con.commit();  
            	   while(result1.next())
            	   {
            		    cholder = new CardHolder();
      				    cholder.setModified(message);
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
	   	   				cholder.setIssuer_code(""+result1.getObject(23));
	   	   				cholder.setMinBalance(""+result1.getObject(24));
	   	   			  	arr.add(cholder);   
            	   }
               }
               else
               {
            	   message = "Fail";
            	   con.rollback();
               }
     
     
       	
		}catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionECard(con, result1, result1);
			closeconnectToPMEcardholder(con, result1);
			//closeConnectionPocketMoniEcardDB(con, result1);
			closeConnectionTestECard(con, result1, result1);
			
		}
		finally
		{
			closeConnectionECard(con, result1, result1);
			closeconnectToPMEcardholder(con, result1);
			//closeConnectionPocketMoniEcardDB(con, result1);
			closeConnectionTestECard(con, result1, result1);
			
			
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
						" e_cardholder where card_num = '"+actual_cardnum+"' and" +
						" substring(track2,7,"+scheme_position+") like '"+scheme_card+"%' ";
			}
			else
			{
				query = "select card_num, phone, lastname, firstname, street, state, card_expiration, email, bound_work, bound_value," +
						" birth_date, change_pin, user_locked, user_hotlist, pin_missed, last_used, modified, created," +
						" online_balance,payfee, cashwthdw_limit, client_id  from" +
						" e_cardholder where substring(track2,7,"+scheme_position+") like '"+scheme_card+"%' ";
			
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
					" e_cardholder where card_num = '" + card_num +"' and card_num like '"+scheme_card+"%' ";
			
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
			
			query = "select card_num from e_cardholder where card_num = '" + card_num_mobile +"' or phone = '" + card_num_mobile +"'";
			
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
			
			query = "select substring(card_num,1,6), sum(online_balance) from e_cardholder where" +
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
					" online_date from e_cardholder where card_num like '"+card_num+"%' and" +
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
					" created from e_cardholder where created between '"+start_dt+"' and '"+end_dt+"' and" +
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
			
			
			query = "select lastname from e_cardholder where card_num = '"+card_num+"'";
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
			
			query = "select firstname from e_cardholder where card_num = '"+card_num+"'";
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
			
			
			query = "select street from e_cardholder where card_num = '"+card_num+"'";
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
			
			
			query = "select state from e_cardholder where card_num = '"+card_num+"'";
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
			
			query = "select phone from e_cardholder where card_num = '"+card_num+"'";
			result = stat.executeQuery(query);
			while(result.next())
			{
				message = ""+result.getObject(1);
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			//closeConnectionECard(con, result, result);
			closeConnectionTestECard(con, result,result);
		}
		finally
		{
			//closeConnectionECard(con, result, result);
			closeConnectionTestECard(con, result,result);
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
			
			query = "insert into e_cardservice(card_num, service_id, param0)values('"+card_num+"', '01', '"+mobile_no+"') ";
		
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
	
	
	/*Method to create scheme*/
	public String createE_Scheme(String schemeId,String schemeName, String settleBank,String schemeNarration,String createDate)
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
			
			query = "select * from e_cardscheme where SCHEME_ID ="+schemeId+" ";
			result = stat.executeQuery(query);
			
			System.out.println("query existed"+query);
			if(result.next())
			{
				System.out.println("query existed 2"+query);
					message = "EXISTED";		
				
			}
			else
			{
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			
					query = "insert into e_cardscheme(SCHEME_ID, SCHEME_OWNER, SETTLEMENT_BANK,NARRATION,CREATED)values("+schemeId+", '"+schemeName+"', '"+settleBank+"','"+schemeNarration+"','"+createDate+"') ";
				
					System.out.println("createE_Scheme query " + query);
					output = stat.executeUpdate(query);
					if(output > 0)
					{
						message = "SUCCESS";
						con.commit();
					}
					else
					{
						message = "FAILED";
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
	
	/*Method to delete scheme*/
	public String deleteScheme(String schemeI)
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
			
		
			query = " delete from e_cardscheme where SCHEME_ID = "+schemeI+" ";
			
			System.out.println("createE_Scheme query " + query);
			output = stat.executeUpdate(query);
			if(output > 0)
			{
				message = "SUCCESS";
				con.commit();
			}
			else
			{
				message = "FAILED";
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
	/*Method to get the new mobile money subscribers*/
	public ArrayList getScheme_Registration()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		CardHolder cholder = null;
		SchemeRegistration reg = null;
		int counter = 0;
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		
		try
		{			
			con = connectToECard();
			stat = con.createStatement();
			
			query = " select SCHEME_ID,SCHEME_OWNER,SETTLEMENT_BANK,NARRATION FROM e_cardscheme "; 
					
			System.out.println("scheme query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
	
				reg = new SchemeRegistration();		
				reg.setSchemeId(""+result.getObject(1));
				reg.setSchemeName(""+result.getObject(2));
				reg.setSettlementBank(""+result.getObject(3));
				reg.setSchemeNarration(""+result.getObject(4));
				
				arr.add(reg);
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
	
	/*Method to get the new mobile money subscribers*/
	public ArrayList getSchemeReport(String schemeId, String StartDt,String endDt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		CardHolder cholder = null;
		SchemeRegistration reg = null;
		int counter = 0;
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		
		try
		{			
			con = connectToECard();
			stat = con.createStatement();
			
			query = " select SCHEME_ID,SCHEME_OWNER,SETTLEMENT_BANK,NARRATION FROM e_cardscheme where convert(varchar,SCHEME_ID) like '"+schemeId+"%' " +
					"and Created between '"+StartDt+"' and '"+endDt+"' "; 
					
			System.out.println("scheme query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
	
				reg = new SchemeRegistration();		
				reg.setSchemeId(""+result.getObject(1));
				reg.setSchemeName(""+result.getObject(2));
				reg.setSettlementBank(""+result.getObject(3));
				reg.setSchemeNarration(""+result.getObject(4));
				
				arr.add(reg);
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
	
	   
	/*Method to setup Fungate */
	public String createFungateSetup(String terminalId,String iPaddress,String merchantName,String masterKey,String cardnum,String expiration,String currency,String accountType,String createdDat,String allowCredit,String chargeCardId,String vtuCom)
	{
		
		String quer = "";
		String query = "";
		String query1 = "";
		String query2 = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToPMT();
			stat = con.createStatement();
			
			quer = "Select * from COP_FUNDGATE_INFO where TERMINAL_ID = '"+terminalId+"' ";
			result = stat.executeQuery(quer);
			if(result.next())
			{
				message = "Exists";
			}
			else
			{
				query = "insert into COP_FUNDGATE_INFO(TERMINAL_ID, REMOTE_IP,MERCHANT_NAME,MASTER_KEY,CARD_NUM,EXPIRATION,CURRENCY,ACCT_TYPE,CREATION_DATE,allowcredit,CHARGE_CAT_ID,VTU_COM)values('"+terminalId+"', '"+iPaddress+"', '"+merchantName+"','"+masterKey+"','"+cardnum+"','"+expiration+"','"+currency+"','C','"+createdDat+"','"+allowCredit+"','"+chargeCardId+"','"+vtuCom+"') ";
				query1 = "insert into COP_TERMINAL_CARDINFO(TERMINAL_ID,CARD_NUMBER,DATECREATED)values('"+terminalId+"','"+cardnum+"','"+createdDat+"') ";
				query2 = "insert into COP_FUNDGATE_IPMAP(TERMINAL_ID,IP_ADDRESS)values('"+terminalId+"','"+iPaddress+"') ";
					
				System.out.println("createFungateSetup query " + query);
				output = stat.executeUpdate(query);
				if(output > 0)
				{
					output = stat.executeUpdate(query1);
					output = stat.executeUpdate(query2);
					message = "SUCCESS";
					con.commit();
				}
				else
				{
					message = "FAILED";
					con.rollback();
				}
						
			}
		
		
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result, result);
			
		}
		return message;
	}
	
	
	/*Method to update Fungate */
	public String updateFungateSetup(String TerminalId ,String merchantName,String cardnum,String expiration,String currency,String createdDat,String allowCredit,String chargeCardId,String vtuCom)
	{
		
		int output = -1;
		String quer = "";
		String query = "";
		String query1 = "";
		String query2 = "";
		String message = "";
		ArrayList arr = new ArrayList();
	
		int output1 = -1;
		int output2 = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
				con = connectToPMT();
				stat = con.createStatement();
			
		
				query  ="update COP_FUNDGATE_INFO set MERCHANT_NAME = '"+merchantName+"',CARD_NUM = '"+cardnum+"', EXPIRATION ='"+expiration+"', CURRENCY = '"+currency+"', CREATION_DATE = '"+createdDat+"', allowcredit ='"+allowCredit+"', CHARGE_CAT_ID = '"+chargeCardId+"',VTU_COM ='"+vtuCom+"' where TERMINAL_ID = '"+TerminalId+"' ";
				query1 ="update COP_TERMINAL_CARDINFO set CARD_NUMBER = '"+cardnum+"', DATECREATED ='"+createdDat+"' where TERMINAL_ID = '"+TerminalId+"'  ";
				/*query2 ="update COP_FUNDGATE_AUTH Set OPERATION = '0' where TERMINAL_ID = '"+TerminalId+"' ";*/
				
				query2 ="delete from  COP_FUNDGATE_AUTH  where TERMINAL_ID = '"+TerminalId+"' ";
			
				/*//output = stat.executeUpdate("update COP_FUNDGATE_INFO set MERCHANT_NAME = '"+merchantName+"',CARD_NUM = '"+cardnum+"', EXPIRATION ='"+expiration+"'," +
											" CURRENCY = '"+currency+"', CREATION_DATE = '"+createdDat+"', allowcredit ='"+allowCredit+"', CHARGE_CAT_ID = '"+chargeCardId+"',VTU_COM ='"+vtuCom+"'" +
											" where TERMINAL_ID = '"+TerminalId+"'");*/
			
				System.out.print("update query  "+query);
				int updat = stat.executeUpdate(query);
				System.out.print("output   "+output);
				if(updat > 0)
				{
					
					message = "SUCCESS";
					stat.executeUpdate(query1);
					stat.executeUpdate(query2);
					con.commit();
				}
				else
				{
					message = "FAILED";
					con.rollback();
				}
						
			
		
		
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result, result);
			
		}
		return message;
	}
	
	
	
	/*Method to log fundgate details  */
	public String createFungateLogDetails(String terminalId,String iPaddress,String merchantName,String cardnum,String expiration,String currency,String accountType,String createdDat,String allowCredit,String chargeCardId,String vtuCom,String initiator,String operation)
	{
		
		String quer = "";
		String query = "";
		String query1 = "";
		String query2 = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToPMT();
			stat = con.createStatement();
			
			quer = "Select * from COP_FUNDGATE_AUTH where TERMINAL_ID = '"+terminalId+"' ";
			result = stat.executeQuery(quer);
			if(result.next())
			{
			
				query = "update COP_FUNDGATE_AUTH set CREATION_DATE = '"+createdDat+"', INITIATOR = '"+initiator+"', AUTHORIZER = '"+initiator+"' ,OPERATION = '"+operation+"' ";
				output = stat.executeUpdate(query);
				if(output > 0)
				{
					message = "SUCCESS";
					con.commit();
				}
				else
				{
					message = "FAILED";
					con.rollback();
				}
			}
			else
			{
				query = "insert into COP_FUNDGATE_AUTH(TERMINAL_ID, AUTHORIZER_IP,MERCHANT_NAME,CARD_NUM,EXPIRATION,CURRENCY,ACCT_TYPE,CREATION_DATE,allowcredit,CHARGE_CAT_ID,VTU_COM,INITIATOR,AUTHORIZER,OPERATION)values" +
						"('"+terminalId+"', '"+iPaddress+"', '"+merchantName+"','"+cardnum+"','"+expiration+"','"+currency+"','C','"+createdDat+"','"+allowCredit+"','"+chargeCardId+"','"+vtuCom+"','"+initiator+"','"+initiator+"','"+operation+"') ";
					
				System.out.println("createFungateLogDetails query " + query);
				output = stat.executeUpdate(query);
				if(output > 0)
				{
					message = "SUCCESS";
					con.commit();
				}
				else
				{
					message = "FAILED";
					con.rollback();
				}
						
			}
		
		
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result, result);
			
		}
		return message;
	}
	/*Method to get fundGate records */
	public ArrayList getFungateRecords()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		FundGateInfo fundgate = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		
		try
		{			
			con = connectToPMT();
			stat = con.createStatement();
			
			query = "SELECT TERMINAL_ID,MERCHANT_NAME,MASTER_KEY,CARD_NUM,EXPIRATION,CURRENCY,ACCT_TYPE,CREATION_DATE,allowcredit,CHARGE_CAT_ID,VTU_COM FROM COP_FUNDGATE_INFO ";
		
			System.out.println("getFungateRecords " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				fundgate = new FundGateInfo();
				fundgate.setTerminalId(""+result.getObject(1));
				fundgate.setMerchantName(""+result.getObject(2));
				fundgate.setMasterKey(""+result.getObject(3));
				fundgate.setCardnum(""+result.getObject(4));
				fundgate.setExpiration(""+result.getObject(5));
				fundgate.setCurrency(""+result.getObject(6));
				fundgate.setAccountType(""+result.getObject(7));
				fundgate.setCreateDat(""+result.getObject(8));
				fundgate.setAllowCredit(""+result.getObject(9));
				fundgate.setChargeCardId(""+result.getObject(10));
				fundgate.setVtuCom(""+result.getObject(11));
				
				arr.add(fundgate);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result, result);
		
		}
		finally
		{
			closeConnectionPMT(con, result, result);
		
		}
		return arr;
	}
	
	/*Method to get fundGate records */
	public ArrayList getFungateRecords(String terminalId)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		FundGateInfo fundgate = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		
		try
		{			
			con = connectToPMT();
			stat = con.createStatement();
			
			query = "SELECT TERMINAL_ID,MERCHANT_NAME,MASTER_KEY,CARD_NUM,EXPIRATION,CURRENCY,ACCT_TYPE,CREATION_DATE,allowcredit,CHARGE_CAT_ID,VTU_COM FROM COP_FUNDGATE_INFO where TERMINAL_ID = '"+terminalId+"' ";
		
			System.out.println("Parameterized getFungateRecords  " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				fundgate = new FundGateInfo();
				fundgate.setTerminalId(""+result.getObject(1));
				fundgate.setMerchantName(""+result.getObject(2));
				fundgate.setMasterKey(""+result.getObject(3));
				fundgate.setCardnum(""+result.getObject(4));
				fundgate.setExpiration(""+result.getObject(5));
				fundgate.setCurrency(""+result.getObject(6));
				fundgate.setAccountType(""+result.getObject(7));
				fundgate.setCreateDat(""+result.getObject(8));
				fundgate.setAllowCredit(""+result.getObject(9));
				fundgate.setChargeCardId(""+result.getObject(10));
				fundgate.setVtuCom(""+result.getObject(11));
				arr.add(fundgate);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result, result);
		
		}
		finally
		{
			closeConnectionPMT(con, result, result);
		
		}
		return arr;
	}
	
	
	
	/*Method to get the new mobile money subscribers*/
	public ArrayList getFundgateReport(String terminalId, String StartDt,String endDt)
	{
		String query = "";
		ArrayList arr = new ArrayList();
	
		E_TRANSACTION trans = null;
		SchemeRegistration reg = null;
		int counter = 0;
		
		Connection con = null;
		Connection con1 = null;
		Statement stat = null;
		Statement stat1 = null;
		ResultSet result = null;
		ResultSet result1 = null;
	
		
		try
		{			
		
			con = connectToPMT();
			stat = con.createStatement();
			
			con1 = connectToCPMT();
			stat1 = con1.createStatement();
					
			query = "select  Terminal_id,Trans_descr,Trans_date,Unique_Transid,Response_code,Trans_amount from Pmtdb.dbo.cop_transactions" +
					" where Terminal_id like '"+terminalId+"%' and Trans_date between '"+StartDt+"' and '"+endDt+"'  order by Trans_Date desc ";
			
			
		
			System.out.println("scheme query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
	
				trans = new E_TRANSACTION();
				trans.setTerminalId(""+result.getObject(1));
				trans.setTrans_desc(""+result.getObject(2));
				trans.setTrans_date(""+result.getObject(3));
				trans.setUnique_transid(""+result.getObject(4));
				trans.setResponseCode(""+result.getObject(5));
				trans.setTrans_amount(""+result.getObject(6));
					
				query = "SELECT CODE_NARATION from Cpmtdb.dbo.TMC_ERROR_CODES where TMC_CODE = '"+trans.getResponseCode()+"' ";
				result1 = stat1.executeQuery(query);
				if(result1.next())
				{
					trans.setErrorDesc(""+result1.getObject(1));
					arr.add(trans);
					
				}				
				
				
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result, result);
			
		}
		finally
		{	
			closeConnectionPMT(con, result, result);
		}
		return arr;
	}
	
	/*Method to get  qeued fundgate  details before authorization*/
	public ArrayList getFundGateAuthorizedDetails()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		CardHolder cholder = null;
		FundGateInfo info;
		int counter = 0;
		HashNumber hn = new HashNumber();
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		try
		{
			
			con = connectToPMT();
			stat = con.createStatement();
			
			query = " SELECT TERMINAL_ID,MERCHANT_NAME,CARD_NUM,EXPIRATION,CURRENCY,ACCT_TYPE,CREATION_DATE,allowcredit,CHARGE_CAT_ID,VTU_COM " +
					" FROM COP_FUNDGATE_AUTH where OPERATION = 'update'" ;

					
			//System.out.println("query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;	
				info = new FundGateInfo();
				info.setTerminalId(""+result.getObject(1));
				info.setMerchantName(""+result.getObject(2));
				info.setCardnum(""+result.getObject(3));
				info.setExpiration(""+result.getObject(4));
				info.setCurrency(""+result.getObject(5));
				info.setAccountType(""+result.getObject(6));
				info.setCreateDat(""+result.getObject(7));
				info.setAllowCredit(""+result.getObject(8));
				info.setChargeCardId(""+result.getObject(9));
				info.setVtuCom(""+result.getObject(10));
				
				arr.add(info);
			}
		}
		catch(Exception ex)
		{
			closeConnectionPMT(con, result, result);
		}
		finally
		{
			closeConnectionPMT(con, result, result);;
		}
		return arr;
	}
	
	
	
	/*
	 * Method to block a card
	 */
	public String blockCard(String cardNum)
	{
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int output = -1 ;
		String message = "";
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			output = stat.executeUpdate("update e_cardholder set user_locked = '1' where card_num = '"+cardNum+"' ");
           if(output > 0 )
           {
        	   
        	   message = "Card Succesfully Blocked";
        	   con.commit();
           }
           else
           {
        	   message = " Card not Blocked ";
        	   con.rollback();
           }
           System.out.println("query blockCard   "+output);
			
		}catch(Exception ex)
		{
			closeConnectionECard(con, result, result);
			
		}
		finally
		{
			closeConnectionECard(con, result, result);
			
		}
		
		return message;
	}
	
	/*This method get the card initiator name */
	
	public String getCardHolderIniatiorByName(String cardNum)
	{
		
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int output = -1 ;
		String message = "";
		String query = "";
		
		try
		{
			con = connectToPMEcardholder();
			stat = con.createStatement();
			
			query = "Select USER_INITIATOR from ecarddb.dbo.E_CARD_PENDING_INFO where CARD_NUM = '"+cardNum+"' ";
			
           result = stat.executeQuery(query);
           while(result.next())
           {
        	   message = ""+result.getObject(1);
        	   
           }
			
		}catch(Exception ex)
		{
			closeconnectToPMEcardholder(con, result);
			
		}
		finally
		{
			closeconnectToPMEcardholder(con, result);
			
		}
		
		return message;
	}
	
	
	
	/*Method to delete fundgateRecords*/
	public String deleteFundGateRecords(String terminalId)
	{
		String query = "";
		String query1 = "";
		String query2 = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		

		try
		{
			con = connectToPMT();
			stat = con.createStatement();

			query = " delete from COP_FUNDGATE_INFO where TERMINAL_ID = '"+terminalId+"' ";
			query1 =" delete from COP_FUNDGATE_IPMAP where TERMINAL_ID = '"+terminalId+"' ";
			query2 =" delete from COP_TERMINAL_CARDINFO where TERMINAL_ID = '"+terminalId+"' ";
			
			System.out.println("deleteFundGateRecords query " + query);
			output = stat.executeUpdate(query);
			if(output > 0)
			{
				output = stat.executeUpdate(query1);
				output = stat.executeUpdate(query2);
				message = "SUCCESS";
				con.commit();
			}
			else
			{
				message = "FAILED";
				con.rollback();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result, result);
		
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
	
	/*Connection to charms database*/
	private Connection connectToChams() throws Exception
	{
		Connection con = null;
		con = Env.getConnectionChamsDB();
		return con;
	}   
	
	private void closeConnectionChams(Connection con, ResultSet result, ResultSet result1)
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
	
	/*Connection to PMT database*/
	private Connection connectToPMT() throws Exception
	{
		Connection con = null;
		con = Env.getConnectionPMTDB();
		return con;
	}   
	
	private void closeConnectionPMT(Connection con, ResultSet result, ResultSet result1)
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
	
	
	/*Connection to CPMT database*/
	private Connection connectToCPMT() throws Exception
	{
		Connection con = null;
		con = Env.getConnectionCPMTDB();
		return con;
	}   
	
	private void closeConnectionCPMT(Connection con, ResultSet result, ResultSet result1)
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
	
	
	/*Connection to PMECARDHOLDER database*/
	private Connection connectToPMEcardholder() throws Exception
	{
		Connection con = null;
		con = Env.getConnectionPMEcardHolderSybaseDB();
		return con;
	}   
	
	private void closeconnectToPMEcardholder(Connection con, ResultSet result)
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
