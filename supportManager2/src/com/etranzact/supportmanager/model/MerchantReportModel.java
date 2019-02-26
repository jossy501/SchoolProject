package com.etranzact.supportmanager.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.etranzact.supportmanager.dto.E_FEE_DETAIL_BK;
import com.etranzact.supportmanager.dto.E_SETTLEMENTDOWNLOAD_BK;
import com.etranzact.supportmanager.dto.E_TRANSACTION;
import com.etranzact.supportmanager.dto.REQUEST_LOG;
import com.etranzact.supportmanager.utility.Env;
import com.etranzact.supportmanager.utility.HashNumber;

public class MerchantReportModel {
	
	public MerchantReportModel(){}
	
	/*Method to get the bank commission summary by date range , merchant code and merchant name*/
	public ArrayList getBankCommissionSummaryByDateAndMerchant(String start_dt, String end_dt,
			String merchant_code, String channel, String bankcode)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_FEE_DETAIL_BK feedetail;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int counter = 0;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
						
			String mcode = merchant_code.substring(merchant_code.length() - 4);
			
			System.out.println("mno " + mcode);
			
			if(channel.equals("Branch"))//PER BRANCH ALONE
			{
				query = "SELECT SUBSTRING(E_FEE_DETAIL_BK.SERVICEID,4,3) branch_code," +
						" E_SUBISSUER.SUB_NAME branch_name, sum(trans_amount)  prod_amt" +
						" FROM E_FEE_DETAIL_BK LEFT OUTER JOIN" +
						" E_CHANNEL ON E_FEE_DETAIL_BK.CHANNELID = E_CHANNEL.CHANNEL_ID LEFT OUTER JOIN" +
						" E_SUBISSUER ON SUBSTRING(E_FEE_DETAIL_BK.SERVICEID,1,3) = E_SUBISSUER.ISSUER_CODE AND" +
						" SUBSTRING(E_FEE_DETAIL_BK.SERVICEID,4,3) = E_SUBISSUER.SUB_CODE LEFT OUTER JOIN" +
						" E_SETTLE_BATCH ON E_FEE_DETAIL_BK.SETTLE_BATCH = E_SETTLE_BATCH.BATCH_ID WHERE" +
						" ( E_FEE_DETAIL_BK.SETTLE_BATCH IN" +
						" (SELECT BATCH_ID FROM E_SETTLE_BATCH WHERE BATCH_DATE BETWEEN('"+start_dt+"') AND ('"+end_dt+"') )) AND" +
						" ( E_FEE_DETAIL_BK.MERCHANT_CODE  LIKE '"+bankcode+"___9999')" +
						" GROUP BY SUBSTRING(E_FEE_DETAIL_BK.SERVICEID,4,3), E_SUBISSUER.SUB_NAME" +
						" order by SUBSTRING(E_FEE_DETAIL_BK.SERVICEID,4,3) ";
				
			}
			else if(channel.equals("Channel"))//PER CHANNEL
			{
				query = "SELECT E_FEE_DETAIL_BK.CHANNELID prod_id, E_CHANNEL.CHANNEL_NAME prod_name," +
						" sum(trans_amount)  prod_amt FROM" +
						" E_FEE_DETAIL_BK LEFT OUTER JOIN" +
						" E_CHANNEL ON E_FEE_DETAIL_BK.CHANNELID = E_CHANNEL.CHANNEL_ID LEFT OUTER JOIN" +
						" E_SUBISSUER ON SUBSTRING(E_FEE_DETAIL_BK.SERVICEID,1,3) = E_SUBISSUER.ISSUER_CODE AND" +
						" SUBSTRING(E_FEE_DETAIL_BK.SERVICEID,4,3) = E_SUBISSUER.SUB_CODE LEFT OUTER JOIN" +
						" E_SETTLE_BATCH ON E_FEE_DETAIL_BK.SETTLE_BATCH = E_SETTLE_BATCH.BATCH_ID WHERE" +
						" ( E_FEE_DETAIL_BK.SETTLE_BATCH in (select batch_id from e_settle_batch where" +
						" batch_date between('"+start_dt+"') AND ('"+end_dt+"') ) ) AND" +
						" ( E_FEE_DETAIL_BK.MERCHANT_CODE  LIKE '"+bankcode+"___9999') GROUP BY" +
						" E_FEE_DETAIL_BK.CHANNELID, E_CHANNEL.CHANNEL_NAME order by E_FEE_DETAIL_BK.CHANNELID " ;
			}
			else if(channel.equals("Branch_Channel"))//PER BRANCH AND CHANNEL
			{
				query = "SELECT SUBSTRING(E_FEE_DETAIL_BK.SERVICEID,4,3) branch_code," +
						" E_SUBISSUER.SUB_NAME branch_name," +
						" E_FEE_DETAIL_BK.CHANNELID prod_id, E_CHANNEL.CHANNEL_NAME prod_name," +
						" sum(trans_amount)  prod_amt FROM" +
						" E_FEE_DETAIL_BK LEFT OUTER JOIN" +
						" E_CHANNEL ON E_FEE_DETAIL_BK.CHANNELID = E_CHANNEL.CHANNEL_ID LEFT OUTER JOIN" +
						" E_SUBISSUER ON SUBSTRING(E_FEE_DETAIL_BK.SERVICEID,1,3) = E_SUBISSUER.ISSUER_CODE AND" +
						" SUBSTRING(E_FEE_DETAIL_BK.SERVICEID,4,3) = E_SUBISSUER.SUB_CODE LEFT OUTER JOIN" +
						" E_SETTLE_BATCH ON E_FEE_DETAIL_BK.SETTLE_BATCH = E_SETTLE_BATCH.BATCH_ID WHERE" +
						" ( E_FEE_DETAIL_BK.SETTLE_BATCH in (select batch_id from e_settle_batch where" +
						" batch_date between('"+start_dt+"') AND ('"+end_dt+"') ) ) AND" +
						" ( E_FEE_DETAIL_BK.MERCHANT_CODE  LIKE '"+bankcode+"___9999') GROUP BY" +
						" E_FEE_DETAIL_BK.CHANNELID, E_CHANNEL.CHANNEL_NAME," +
						" SUBSTRING(E_FEE_DETAIL_BK.SERVICEID,4,3), E_SUBISSUER.SUB_NAME" +
						" order by E_FEE_DETAIL_BK.CHANNELID, SUBSTRING(E_FEE_DETAIL_BK.SERVICEID,4,3)  " ;
			}


			/*if(channel.equals("01P"))//web payments
			{
				mcode = bankcode + "WEB" + mcode;
				query = "select a.merchant_code,count(distinct b.external_transid) as total_count," +
				" sum(b.trans_amount) as total_amount from e_settlement_download_bk a left outer join e_fee_detail_bk b" +
				" on a.unique_transid = b.external_transid and b.merchant_code = '"+mcode+"'" +
				" where a.trans_code ='P' and a.channelid like '01%' and" +
				" (a.card_num like '" + bankcode + "%' or a.merchant_code like '"+ bankcode +"%') and" +
				" a.trans_date between ('"+start_dt+"') and ('"+end_dt+"') group by a.merchant_code";
			}
			else if(channel.equals("02P"))//mobile payments
			{
				mcode = bankcode + "MOB" + mcode;
				query = "select a.merchant_code,count(distinct b.external_transid) as total_count," +
				" sum(b.trans_amount) as total_amount from e_settlement_download_bk a left outer join e_fee_detail_bk b" +
				" on a.unique_transid = b.external_transid and b.merchant_code = '"+mcode+"'" +
				" where a.trans_code ='P' and a.channelid like '02%' and" +
				" (a.card_num like '" + bankcode + "%' or a.merchant_code like '"+ bankcode +"%') and" +
				" a.trans_date between ('"+start_dt+"') and ('"+end_dt+"') group by a.merchant_code";
			}
			else if(channel.equals("02T"))//mobile transfers
			{
				mcode = bankcode + "MOB" + mcode;
				query = "select substring(a.merchant_code,1,3),count(distinct b.external_transid) as total_count," +
				" sum(b.trans_amount) as total_amount from e_settlement_download_bk a left outer join e_fee_detail_bk b" +
				" on a.unique_transid = b.external_transid and b.merchant_code = '"+mcode+"'" +
				" where a.trans_code ='T' and a.channelid like '02%' and (a.card_num like '" + bankcode + "%')" +
				" and a.trans_date between ('"+start_dt+"') and ('"+end_dt+"') group by substring(a.merchant_code,1,3)";
				
			}
			else if(channel.equals("03"))//pos
			{
				mcode = bankcode + "POS" + mcode;
				query = "select a.merchant_code,count(distinct b.external_transid) as total_count," +
				" sum(b.trans_amount) as total_amount from e_settlement_download_bk a left outer join e_fee_detail_bk b" +
				" on a.unique_transid = b.external_transid and b.merchant_code = '"+mcode+"'" +
				" where a.trans_code ='P' and a.channelid like '"+channel+"%' and" +
				" (a.card_num like '" + bankcode + "%' or a.merchant_code like '"+ bankcode +"%') and" +
				" a.trans_date between ('"+start_dt+"') and ('"+end_dt+"') group by a.merchant_code";
			}
			else if(channel.equals("04"))//atm
			{
				mcode = bankcode + "ATM" + mcode;
				query = "select a.merchant_code,count(distinct b.external_transid) as total_count," +
				" sum(b.trans_amount) as total_amount from e_settlement_download_bk a left outer join e_fee_detail_bk b" +
				" on a.unique_transid = b.external_transid and b.merchant_code = '"+mcode+"'" +
				" where a.trans_code ='W' and a.channelid like '"+channel+"%' and" +
				" (a.card_num like '" + bankcode + "%' or a.merchant_code like '"+ bankcode +"%') and" +
				" a.trans_date between ('"+start_dt+"') and ('"+end_dt+"') group by a.merchant_code";
			}
			else if(channel.equals("05"))//payoutlet
			{
				mcode = bankcode + "OUT" + mcode;
				query = "select a.merchant_code,count(distinct b.external_transid) as total_count," +
				" sum(b.trans_amount) as total_amount from e_settlement_download_bk a left outer join e_fee_detail_bk b" +
				" on a.unique_transid = b.external_transid and b.merchant_code = '"+mcode+"'" +
				" where a.trans_code ='P' and a.channelid like '"+channel+"%' and" +
				" (a.card_num like '" + bankcode + "%' or a.merchant_code like '"+ bankcode +"%') and" +
				" a.trans_date between ('"+start_dt+"') and ('"+end_dt+"') group by a.merchant_code";
			}
			else if(channel.equals("06"))//eRemit
			{
				mcode = bankcode + "RET" + mcode;
				query = "select a.merchant_code,count(distinct b.external_transid) as total_count," +
				" sum(b.trans_amount) as total_amount from e_settlement_download_bk a left outer join e_fee_detail_bk b" +
				" on a.unique_transid = b.external_transid and b.merchant_code = '"+mcode+"'" +
				" where a.trans_code ='T' and a.channelid like '"+channel+"%' and" +
				" (a.card_num like '" + bankcode + "%' or a.merchant_code like '"+ bankcode +"%') and" +
				" a.trans_date between ('"+start_dt+"') and ('"+end_dt+"') group by a.merchant_code";
			}*/
			
			System.out.println("merchant commission query >>>" + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				feedetail = new E_FEE_DETAIL_BK();
				
				feedetail.setBank_code(result.getString(1));
				feedetail.setMerchant_code(result.getString(2));
				feedetail.setTotal_amount(result.getString(3));
				
				arr.add(feedetail);
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
	
	
	/*Method to get the merchant commission summary by date range , merchant code, channel and bankcode*/
	public ArrayList getMerchantCommissionSummary(String start_dt, String end_dt,
			String merchant_code, String channel, String bankcode)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_FEE_DETAIL_BK feedetail;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int counter = 0;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
						
			String mcode = merchant_code.substring(merchant_code.length() - 4);
			System.out.println("mno " + mcode);

			if(channel.equals("01P"))//web payments
			{
				mcode = bankcode + "WEB" + mcode;
				query = "select a.merchant_code,count(distinct b.external_transid) as total_count," +
				" sum(b.trans_amount) as total_amount from e_settlement_download_bk a left outer join e_fee_detail_bk b" +
				" on a.unique_transid = b.external_transid and b.merchant_code = '"+mcode+"'" +
				" where a.trans_code ='P' and a.channelid like '01%' and" +
				" (a.card_num like '" + bankcode + "%' or a.merchant_code like '"+ bankcode +"%') and" +
				" a.trans_date between ('"+start_dt+"') and ('"+end_dt+"') group by a.merchant_code";
			}
			else if(channel.equals("02P"))//mobile payments
			{
				mcode = bankcode + "MOB" + mcode;
				query = "select a.merchant_code,count(distinct b.external_transid) as total_count," +
				" sum(b.trans_amount) as total_amount from e_settlement_download_bk a left outer join e_fee_detail_bk b" +
				" on a.unique_transid = b.external_transid and b.merchant_code = '"+mcode+"'" +
				" where a.trans_code ='P' and a.channelid like '02%' and" +
				" (a.card_num like '" + bankcode + "%' or a.merchant_code like '"+ bankcode +"%') and" +
				" a.trans_date between ('"+start_dt+"') and ('"+end_dt+"') group by a.merchant_code";
			}
			else if(channel.equals("02T"))//mobile transfers
			{
				mcode = bankcode + "MOB" + mcode;
				query = "select substring(a.merchant_code,1,3),count(distinct b.external_transid) as total_count," +
				" sum(b.trans_amount) as total_amount from e_settlement_download_bk a left outer join e_fee_detail_bk b" +
				" on a.unique_transid = b.external_transid and b.merchant_code = '"+mcode+"'" +
				" where a.trans_code ='T' and a.channelid like '02%' and (a.card_num like '" + bankcode + "%')" +
				" and a.trans_date between ('"+start_dt+"') and ('"+end_dt+"') group by substring(a.merchant_code,1,3)";
				
			}
			else if(channel.equals("03"))//pos
			{
				mcode = bankcode + "POS" + mcode;
				query = "select a.merchant_code,count(distinct b.external_transid) as total_count," +
				" sum(b.trans_amount) as total_amount from e_settlement_download_bk a left outer join e_fee_detail_bk b" +
				" on a.unique_transid = b.external_transid and b.merchant_code = '"+mcode+"'" +
				" where a.trans_code ='P' and a.channelid like '"+channel+"%' and" +
				" (a.card_num like '" + bankcode + "%' or a.merchant_code like '"+ bankcode +"%') and" +
				" a.trans_date between ('"+start_dt+"') and ('"+end_dt+"') group by a.merchant_code";
			}
			else if(channel.equals("04"))//atm
			{
				mcode = bankcode + "ATM" + mcode;
				query = "select a.merchant_code,count(distinct b.external_transid) as total_count," +
				" sum(b.trans_amount) as total_amount from e_settlement_download_bk a left outer join e_fee_detail_bk b" +
				" on a.unique_transid = b.external_transid and b.merchant_code = '"+mcode+"'" +
				" where a.trans_code ='W' and a.channelid like '"+channel+"%' and" +
				" (a.card_num like '" + bankcode + "%' or a.merchant_code like '"+ bankcode +"%') and" +
				" a.trans_date between ('"+start_dt+"') and ('"+end_dt+"') group by a.merchant_code";
			}
			else if(channel.equals("05"))//payoutlet
			{
				mcode = bankcode + "OUT" + mcode;
				query = "select a.merchant_code,count(distinct b.external_transid) as total_count," +
				" sum(b.trans_amount) as total_amount from e_settlement_download_bk a left outer join e_fee_detail_bk b" +
				" on a.unique_transid = b.external_transid and b.merchant_code = '"+mcode+"'" +
				" where a.trans_code ='P' and a.channelid like '"+channel+"%' and" +
				" (a.card_num like '" + bankcode + "%' or a.merchant_code like '"+ bankcode +"%') and" +
				" a.trans_date between ('"+start_dt+"') and ('"+end_dt+"') group by a.merchant_code";
			}
			else if(channel.equals("06"))//eRemit
			{
				mcode = bankcode + "RET" + mcode;
				query = "select a.merchant_code,count(distinct b.external_transid) as total_count," +
				" sum(b.trans_amount) as total_amount from e_settlement_download_bk a left outer join e_fee_detail_bk b" +
				" on a.unique_transid = b.external_transid and b.merchant_code = '"+mcode+"'" +
				" where a.trans_code ='T' and a.channelid like '"+channel+"%' and" +
				" (a.card_num like '" + bankcode + "%' or a.merchant_code like '"+ bankcode +"%') and" +
				" a.trans_date between ('"+start_dt+"') and ('"+end_dt+"') group by a.merchant_code";
			}
			
			System.out.println("merchant commission query >>>" + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				feedetail = new E_FEE_DETAIL_BK();
				
				feedetail.setMerchant_code(result.getString(1));
				feedetail.setTotal_count(result.getString(2));
				feedetail.setTotal_amount(result.getString(3));
				
				arr.add(feedetail);
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
	
	
	/*Method to get the bank commission summary by date range , merchant code and merchant name*/
	public ArrayList getBranchComissionSummary(String start_dt, String end_dt, String merchant_code, String channel)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		E_FEE_DETAIL_BK feedetail;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int counter = 0;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
						
			query ="select card_num, count(*) as total_count, sum(trans_amount) as total_amount from e_fee_detail_bk " +
					"where trans_code ='P' and channelid like '%"+channel+"%' and trans_date between('"+start_dt+"') and ('"+end_dt+"') and merchant_code= '"+merchant_code +"' group by card_num"; 
			
			result = stat.executeQuery(query);
			while(result.next())
			{
				counter++;
				feedetail = new E_FEE_DETAIL_BK();
				
				feedetail.setCounter(""+counter);
				feedetail.setBranch_code(result.getString(1));
				feedetail.setTotal_count(result.getString(2));
				feedetail.setTotal_amount(result.getString(3));
				
				arr.add(feedetail);
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
	
	public ArrayList getMerchantSettlementSummary(String settle_batch, String merchant_code, String main_merchant_code, String start_dt, String end_dt) {
		
		ArrayList arr = new ArrayList();
		E_SETTLEMENTDOWNLOAD_BK settle_download;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int counter = 0;
		String filter = "";
		
		try
		{
			con = connectToBakUpECard();
			stat = con.createStatement();
			
			if(settle_batch!=null || !settle_batch.equals("")) {				
				filter = filter + "AND SETTLE_BATCH LIKE '%"+settle_batch.trim()+"'";
			}
			if(merchant_code!=null || !merchant_code.equals("")) {
				filter = filter + "AND MERCHANT_CODE LIKE '%"+merchant_code.trim()+"' ";
				
			}
			String query = "SELECT 'O' BANK_TYPE,'OTHER BANKS - '+e_issuer.issuer_name BANK,E_SETTLEMENT_DOWNLOAD_BK.MERCHANT_CODE, "+     
                           " E_SETTLEMENT_DOWNLOAD_BK.SETTLE_BATCH, e_settle_batch.batch_date ,  E_SETTLEMENT_DOWNLOAD_BK.TRANS_CODE , "+
                           " sum(E_SETTLEMENT_DOWNLOAD_BK.TRANS_AMOUNT) tot_amount  FROM E_SETTLEMENT_DOWNLOAD_BK left outer "+
                           " join e_settle_batch on E_SETTLEMENT_DOWNLOAD_BK.settle_batch = e_settle_batch.batch_id left outer join "+ 
                           " e_issuer on substring(E_SETTLEMENT_DOWNLOAD_BK.merchant_code,1,3) = e_issuer.issuer_code   "+
                           " WHERE E_SETTLEMENT_DOWNLOAD_BK.TRANS_CODE in ('P','C')  AND  E_SETTLEMENT_DOWNLOAD_BK.TRANS_DATE "+
                           " between('"+start_dt+"') and ('"+end_dt+"') AND   E_SETTLEMENT_DOWNLOAD_BK.MERCHANT_CODE IN "+
                           " (SELECT SUB_MERCHANT_CODE FROM E_MERCHANTCODE_MAP  WHERE MAIN_MERCHANT_CODE = '"+main_merchant_code+"') AND  "+
                           " substring(E_SETTLEMENT_DOWNLOAD_BK.CARD_NUM,1,3) <> substring(E_SETTLEMENT_DOWNLOAD_BK.MERCHANT_CODE,1,3) "+
                           "  "+filter+"  GROUP BY e_issuer.issuer_name, E_SETTLEMENT_DOWNLOAD_BK.MERCHANT_CODE, E_SETTLEMENT_DOWNLOAD_BK.SETTLE_BATCH , "+
                           " e_settle_batch.batch_date,  E_SETTLEMENT_DOWNLOAD_BK.TRANS_CODE  "+
                           " UNION ALL  "+
                           " SELECT 'M' BANK_TYPE,e_issuer.issuer_name,E_SETTLEMENT_DOWNLOAD_BK.MERCHANT_CODE,    E_SETTLEMENT_DOWNLOAD_BK.SETTLE_BATCH,"+
                           " e_settle_batch.batch_date ,  E_SETTLEMENT_DOWNLOAD_BK.TRANS_CODE,  sum(E_SETTLEMENT_DOWNLOAD_BK.TRANS_AMOUNT) tot_amount  "+
                           " FROM E_SETTLEMENT_DOWNLOAD_BK left outer join e_settle_batch on "+
                           " E_SETTLEMENT_DOWNLOAD_BK.settle_batch = e_settle_batch.batch_id left outer join e_issuer on "+
                           " substring(E_SETTLEMENT_DOWNLOAD_BK.merchant_code,1,3) = e_issuer.issuer_code "+
                           " WHERE E_SETTLEMENT_DOWNLOAD_BK.TRANS_CODE in ('P','C') AND  E_SETTLEMENT_DOWNLOAD_BK.TRANS_DATE "+
                           " between('"+start_dt+"') and ('"+end_dt+"') AND  E_SETTLEMENT_DOWNLOAD_BK.MERCHANT_CODE "+
                           " IN (SELECT SUB_MERCHANT_CODE FROM E_MERCHANTCODE_MAP  WHERE MAIN_MERCHANT_CODE = '"+main_merchant_code+"') "+
                           " AND  substring(E_SETTLEMENT_DOWNLOAD_BK.CARD_NUM,1,3) = substring(E_SETTLEMENT_DOWNLOAD_BK.MERCHANT_CODE,1,3) "+
                           "  "+filter+" GROUP BY e_issuer.issuer_name,E_SETTLEMENT_DOWNLOAD_BK.MERCHANT_CODE,   E_SETTLEMENT_DOWNLOAD_BK.SETTLE_BATCH,"+
                           " e_settle_batch.batch_date,  E_SETTLEMENT_DOWNLOAD_BK.TRANS_CODE  order by settle_batch , merchant_code";
			
			System.out.println("getMerchantSettlementSummary >>>" + query);
			result = stat.executeQuery(query);
			while(result.next()) {
				settle_download = new E_SETTLEMENTDOWNLOAD_BK();
				counter++;
				settle_download.setCounter(""+counter);
				settle_download.setCard_num(""+result.getObject("BANK"));
				settle_download.setMerchat_code(""+result.getObject("MERCHANT_CODE"));
				settle_download.setSettle_batch(""+result.getObject("SETTLE_BATCH"));
				settle_download.setTrans_date(""+result.getObject("batch_date"));
				settle_download.setTrans_amount(""+result.getObject("tot_amount"));
				settle_download.setTrans_code(""+result.getObject("TRANS_CODE"));
				settle_download.setGflag(""+result.getObject("BANK_TYPE"));
				arr.add(settle_download);
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
	
	/*Method to do drilling down of merchant transactions */
	public ArrayList getBankMerchantComDetail(String start_dt, String end_dt, String merchant_code, String channel_id, String card_num)
	{
		ArrayList arr = new ArrayList();
		E_FEE_DETAIL_BK feedetail;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int counter = 0;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			String query = "select trans_date, card_num, trans_descr, substring(serviceid,4,3) as branch_code, trans_amount from e_fee_detail_bk " +
							" where trans_code ='P' and channelid like '%"+channel_id+"%' and trans_date between('"+start_dt+"') and ('"+end_dt+"') "+
							" and merchant_code= '"+merchant_code+"' and card_num ='"+card_num+"'";
			
			System.out.println("bank comission details" + query);

			result = stat.executeQuery(query);
			while(result.next())
			{
				feedetail = new E_FEE_DETAIL_BK();
				counter++;
				feedetail.setCounter(""+counter);
				feedetail.setTrans_date(""+result.getObject(1));
				feedetail.setMerchant_code(""+result.getObject(2));
				feedetail.setTrans_desc(""+result.getObject(3));
				feedetail.setBranch_code(""+result.getObject(4));
				feedetail.setTrans_amount(""+result.getObject(5));
				arr.add(feedetail);
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
	
	/*Method to do drilling down of merchant settlement detail transactions */
	public ArrayList getMerchantSettlementDetail(String merchant_code, String s_batchid, String trans_code, String bank_t)
	{
		ArrayList arr = new ArrayList();
		E_SETTLEMENTDOWNLOAD_BK settle;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int counter = 0;
		String listQuery = "";
		HashNumber hash=new HashNumber();
		
		try
		{
			con = connectToBakUpECard();
			stat = con.createStatement();
			
			if(trans_code.equalsIgnoreCase("P") && bank_t.equalsIgnoreCase("O")) {
				listQuery = " SELECT E_SETTLEMENT_DOWNLOAD_BK.trans_date, E_SETTLEMENT_DOWNLOAD_BK.card_num, "+
						" replicate('0',12 - char_length(rtrim(convert(varchar(38),E_SETTLEMENT_DOWNLOAD_BK.TRANSID)))) "+
						 " + rtrim(convert(varchar(38),E_SETTLEMENT_DOWNLOAD_BK.TRANSID)),  E_SETTLEMENT_DOWNLOAD_BK.transid, "+
						 " E_SETTLEMENT_DOWNLOAD_BK.trans_descr, E_SETTLEMENT_DOWNLOAD_BK.trans_amount from E_SETTLEMENT_DOWNLOAD_BK "+
						 " where E_SETTLEMENT_DOWNLOAD_BK.merchant_code = '"+merchant_code.trim()+"'  and E_SETTLEMENT_DOWNLOAD_BK.settle_batch =  '"+s_batchid.trim()+"' "+
						 " and substring(E_SETTLEMENT_DOWNLOAD_BK.card_num,1,3) <>  substring(E_SETTLEMENT_DOWNLOAD_BK.merchant_code,1,3) ORDER BY TRANS_DATE";
			}
			if(trans_code.equalsIgnoreCase("P") && bank_t.equalsIgnoreCase("M")) {
				listQuery = " SELECT E_SETTLEMENT_DOWNLOAD_BK.trans_date, E_SETTLEMENT_DOWNLOAD_BK.card_num, "+
							" replicate('0',12 - char_length(rtrim(convert(varchar(38),E_SETTLEMENT_DOWNLOAD_BK.TRANSID)))) "+
							 " + rtrim(convert(varchar(38),E_SETTLEMENT_DOWNLOAD_BK.TRANSID)),  E_SETTLEMENT_DOWNLOAD_BK.transid, "+
							 " E_SETTLEMENT_DOWNLOAD_BK.trans_descr, E_SETTLEMENT_DOWNLOAD_BK.trans_amount from E_SETTLEMENT_DOWNLOAD_BK "+
							 " where E_SETTLEMENT_DOWNLOAD_BK.merchant_code = '"+merchant_code.trim()+"'  and E_SETTLEMENT_DOWNLOAD_BK.settle_batch =  '"+s_batchid.trim()+"' "+
							 " and substring(E_SETTLEMENT_DOWNLOAD_BK.card_num,1,3) = substring(E_SETTLEMENT_DOWNLOAD_BK.merchant_code,1,3) ORDER BY TRANS_DATE";
			}
			if(trans_code.equalsIgnoreCase("C") && bank_t.equalsIgnoreCase("O")) {
				listQuery = " SELECT E_SETTLEMENT_DOWNLOAD_BK.trans_date, E_FEE_DETAIL_BK.card_num, "+
							" replicate('0',12 - char_length(rtrim(convert(varchar(38),E_FEE_DETAIL_BK.TRANSID)))) "+
							 " + rtrim(convert(varchar(38),E_FEE_DETAIL_BK.TRANSID)),  E_FEE_DETAIL_BK.transid, "+
							 " E_SETTLEMENT_DOWNLOAD_BK.trans_descr, E_FEE_DETAIL_BK.trans_amount from E_FEE_DETAIL_BK left outer join	"+		  
							 " E_SETTLEMENT_DOWNLOAD_BK on E_FEE_DETAIL_BK.external_transid = E_SETTLEMENT_DOWNLOAD_BK.unique_transid "+
							 " and E_FEE_DETAIL_BK.settle_batch = E_SETTLEMENT_DOWNLOAD_BK.settle_batch 	"+		  
							 " where E_FEE_DETAIL_BK.merchant_code = '"+merchant_code.trim()+"'  and E_FEE_DETAIL_BK.settle_batch =  '"+s_batchid.trim()+"' "+
							 " and substring(E_FEE_DETAIL_BK.card_num,1,3) <>  substring(E_FEE_DETAIL_BK.merchant_code,1,3) ORDER BY TRANS_DATE";
			}
			if(trans_code.equalsIgnoreCase("C") && bank_t.equalsIgnoreCase("M")) {
				listQuery = " SELECT E_SETTLEMENT_DOWNLOAD_BK.trans_date, E_FEE_DETAIL_BK.card_num, "+
							" replicate('0',12 - char_length(rtrim(convert(varchar(38),E_FEE_DETAIL_BK.TRANSID)))) "+
							 " + rtrim(convert(varchar(38),E_FEE_DETAIL_BK.TRANSID)),  E_FEE_DETAIL_BK.transid, "+
							 " E_SETTLEMENT_DOWNLOAD_BK.trans_descr, E_FEE_DETAIL_BK.trans_amount from E_FEE_DETAIL_BK left outer join	"+		  
							 " E_SETTLEMENT_DOWNLOAD_BK on E_FEE_DETAIL_BK.external_transid = E_SETTLEMENT_DOWNLOAD_BK.unique_transid "+
							 " and E_FEE_DETAIL_BK.settle_batch = E_SETTLEMENT_DOWNLOAD_BK.settle_batch 	"+		  
							 " where E_FEE_DETAIL_BK.merchant_code = '"+merchant_code.trim()+"'  and E_FEE_DETAIL_BK.settle_batch =  '"+s_batchid.trim()+"' "+
							 " and substring(E_FEE_DETAIL_BK.card_num,1,3) =  substring(E_FEE_DETAIL_BK.merchant_code,1,3) ORDER BY TRANS_DATE";
			}		 
			
			result = stat.executeQuery(listQuery);
			while(result.next()) {
				settle = new E_SETTLEMENTDOWNLOAD_BK();
				counter++;
				settle.setCounter(""+counter);
				settle.setTrans_date(""+result.getObject("TRANS_DATE"));
				String cardnum = ""+result.getObject("CARD_NUM");
				String hash_cardnum = hash.hashStringValue(cardnum, 4, 6);
				settle.setCard_num(hash_cardnum);
				settle.setTrans_desc(""+result.getObject("TRANS_DESCR"));
				settle.setTrans_amount(""+result.getObject("TRANS_AMOUNT"));
				arr.add(settle);
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
	
	/*Method to get the merchant name*/
	public String getMerchantName(String merchant_code)
	{
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			String query = "select merchant_name from ecarddb..e_merchant where merchant_code = '"+merchant_code+"'";
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
	
	/*Method to get the merchant name*/
	public String getBranchName(String issuer_code, String sub_code)
	{
		String message = "";
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			con = connectToECard();
			stat = con.createStatement();
			
			String query = "select sub_name from ecarddb..e_subissuer where issuer_code = '"+issuer_code+"' and sub_code='"+sub_code+"'";
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
	
	
	/*Connection to ecard database*/
	private Connection connectToECard() throws Exception
	{
		Connection con = null;
		con = Env.getConnectionECardDB();
		return con;
	}   
	
	/*Connection to ecard database*/
	private Connection connectToBakUpECard() throws Exception
	{
		Connection con = null;
		con = Env.getConnectionBackUpECardDB();
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
	

}
