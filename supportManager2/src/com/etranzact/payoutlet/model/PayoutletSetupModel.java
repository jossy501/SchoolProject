/**
 * 
 */
package com.etranzact.payoutlet.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.etranzact.payoutlet.dto.CAgency;
import com.etranzact.payoutlet.dto.CIssuerMerchant;
import com.etranzact.payoutlet.dto.CTransType;
import com.etranzact.supportmanager.utility.Env;

/**
 * @author tony.ezeanya
 *
 */
public class PayoutletSetupModel 
{
	
	public PayoutletSetupModel(){}
	
	//this is used to create the agency
	public String createAgency(CAgency cagency)
	{
		int output = -1;
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		try
		{
			con = connectToPayoutlet();
			stat = con.createStatement();
			
			
			String query = "select agency_id from c_agency where agency_id = '"+cagency.getAgencyId()+"'";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "This agency " + cagency.getAgencyName()  + " with this agency id " + cagency.getAgencyId() + " already exists";
				return message;
			}
			
			query = "insert into c_agency(agency_id, merchant_code, agency_name)" +
					"values('"+cagency.getAgencyId()+"', '"+cagency.getMerchantCode()+"','"+cagency.getAgencyName()+"')";
			
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
			try
			{
				con.rollback();
			}
			catch(Exception e){}
			closeConnectionPayoutlet(con, result);
		}
		finally
		{
			closeConnectionPayoutlet(con, result);
		}
		return message;
		
	}
	
	
	
	//this is used to create the trans type
	public String createTransType(CTransType ctransType)
	{
		int output = -1;
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		try
		{
			con = connectToPayoutlet();
			stat = con.createStatement();
			
			
			String query = "select trans_id from c_transtype where trans_id = '"+ctransType.getTransId()+"'";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "This trans type " + ctransType.getTypeName()  + " with this trans id " + ctransType.getTransId() + " already exists";
				return message;
			}
			
			query = "insert into c_transtype(merchant_code, trans_id, type_name, type_amount, type_symbol, type_quantity, add_fee," +
					" isMust, agency_id, alt_merchant_code)" +
					"values('"+ctransType.getMerchantCode()+"', '"+ctransType.getTransId()+"', " +
							" '"+ctransType.getTypeName()+"', "+ctransType.getTypeAmount()+", '"+ctransType.getTypeSymbol()+"'," +
							" "+ctransType.getTypeQuantity()+", "+ctransType.getAddFee()+"," +
							" '"+ctransType.getIsMust()+"', '"+ctransType.getAgencyId()+"', '"+ctransType.getAltMerchantCode()+"')";
			
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
			try
			{
				con.rollback();
			}
			catch(Exception e){}
			closeConnectionPayoutlet(con, result);
		}
		finally
		{
			closeConnectionPayoutlet(con, result);
		}
		return message;
		
	}
	
	
	
	//this is used to create the issuerMerchant
	public String createIssuerMerchant(CIssuerMerchant cissuerMerchant)
	{
		int output = -1;
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		try
		{
			con = connectToPayoutlet();
			stat = con.createStatement();
			
			
			String query = "select issuer_code from c_issuermerchant where" +
					" issuer_code = '"+cissuerMerchant.getIssuerCode()+"' and merchant_code = '"+cissuerMerchant.getMerchantCode()+"' ";
			result = stat.executeQuery(query);
			if(result.next())
			{
				message = "This issuer merchant  " + cissuerMerchant.getMerchantCode()  + " with this issuer code " +
					cissuerMerchant.getIssuerCode() + " already exists";
				return message;
			}
			
			query = "insert into c_issuermerchant(issuer_code, merchant_code, description, created, special_merchant, merchant_page," +
					" allow_multiple, disabled, online_flag)" +
					"values('"+cissuerMerchant.getIssuerCode()+"', '"+cissuerMerchant.getMerchantCode()+"', " +
							" '"+cissuerMerchant.getDescription()+"', getDate()," +
							" '"+cissuerMerchant.getSpecialMerchant()+"','"+cissuerMerchant.getMerchantPage()+"'," +
							" '"+cissuerMerchant.getAllowMulitple()+"'," +
							" '"+cissuerMerchant.getDisabled()+"', '"+cissuerMerchant.getOnlineFlag()+"')";
			
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
			try
			{	
				ex.printStackTrace();
				con.rollback();
			}
			catch(Exception e){e.printStackTrace();}
			closeConnectionPayoutlet(con, result);
		}
		finally
		{
			closeConnectionPayoutlet(con, result);
		}
		return message;
		
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

}
