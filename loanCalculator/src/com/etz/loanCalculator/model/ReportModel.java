/**
 * 
 */
package com.etz.loanCalculator.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.etz.loanCalculator.dto.Borrower;
import com.etz.loanCalculator.dto.LoanHouse;
import com.etz.loanCalculator.dto.Transaction;
import com.etz.loanCalculator.utility.Env;
import com.etz.security.util.Cryptographer;


/**
 * @author tony.ezeanya
 *
 */
public class ReportModel 
{

	public ReportModel(){}
	
	
	
	public String approveLoan(String borrowerId, String lendingHouseId, String amountBorrowed, String duration)
	{
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		int output = -1;
		String message = "";
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();

			
			String query = "insert into telcodb..loan_master(borrowerId, LendingHouseId, AmountBorrowed, DateBorrowed, status, duration) values " +
					"("+borrowerId+", "+lendingHouseId+", "+Double.parseDouble(amountBorrowed)+", getDate(), '1', '"+ duration +"')";
			output = stat.executeUpdate(query);
			if(output > 0)
			{
				con.commit();
				message = "Records inserted";
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
			closeConnectionSupportLogDB(con, result);
		}
		
		finally
		{
			closeConnectionSupportLogDB(con, result);
		}
		
		return message;
	}
	
	public ArrayList getLoanHouses()
	{
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		ArrayList arr = new ArrayList();
		LoanHouse loanHouse = null;
		String query  = "";
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			query = "select id, code, name, houseLogo from telcodb..loan_house";
			result = stat.executeQuery(query);
			while(result.next())
			{
				loanHouse = new LoanHouse();
				loanHouse.setId(""+result.getObject(1));
				loanHouse.setCode(result.getString(2));
				loanHouse.setLoanName(result.getString(3));
				loanHouse.setLoanHouseLogo(result.getString(4));
				
				arr.add(loanHouse);
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
	
	
	
	public ArrayList getBorrowerTrasactionHistory(String borrowerId)
	{
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		ArrayList arr = new ArrayList();
		Transaction transaction = null;
		String query  = "";
		String d = "";
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();
			
			query = "select lendingHouseId, (select name from loan_house where id = loan_master.lendingHouseId), " +
					" AmountBorrowed, (select sum(amountpaid) from loan_transaction where masterId = loan_master.id) as 'Amount Paid'," +
					" DateBorrowed, borrowerid, duration from loan_master" +
					" where borrowerId = (select id from loan_borrower where mobile = '"+borrowerId+"') group by lendingHouseId";
			
			System.out.println("query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				transaction = new Transaction();
				transaction.setLendingHouseId(""+result.getObject(1));
				transaction.setLendingHouseName(result.getString(2));
				transaction.setTotalAmountBorrowed(""+result.getObject(3));
				transaction.setTotalAmountPaid(""+result.getObject(4));
				
				d = ""+result.getObject(5);
				transaction.setDateBorrowed(d.substring(0, d.lastIndexOf(" ")));
				
				transaction.setBorrowerId(""+result.getObject(6));
				transaction.setDuration(""+result.getObject(7));
				
				double amt = Double.parseDouble(transaction.getTotalAmountBorrowed())/Double.parseDouble(transaction.getDuration());
				
				transaction.setMonthlyPayBack(""+amt);
				
				arr.add(transaction);
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
	
	
	public ArrayList getBorrowerTransactionBreakDown(String borrowerId, String lendingHouseId)
	{
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		ArrayList arr = new ArrayList();
		Transaction transaction = null;
		String query  = "";
		String d = "";
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();

			query = "select amountpaid, datepaid from loan_transaction where" +
					" masterId in (select id from loan_master where borrowerId = "+borrowerId+" and lendingHouseId = "+ lendingHouseId +")";
	
			System.out.println("query " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				transaction = new Transaction();
				transaction.setAmountPaid(""+result.getObject(1));
				
				d = ""+result.getObject(2);
				transaction.setDatePaid(d.substring(0, d.lastIndexOf(" ")));

				arr.add(transaction);
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
	
	
	public ArrayList getBorrowerInfo(String borrowerMobile, String nationalID)
	{
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		ArrayList arr = new ArrayList();
		Borrower borrower = null;
		String query  = "";
		String d = "";
		try
		{
			con = connectToSupportLog();
			stat = con.createStatement();

			query = "select lastname, firstname, mobile, email, dob," +
					" address, nationalID, staffID, companyName, monthlyIncome, id from telcodb..loan_borrower where" +
					" mobile like '"+borrowerMobile+"%' and nationalID like '"+ nationalID +"%' ";
	
			System.out.println("query " + query);
			result = stat.executeQuery(query);
			if(result.next())
			{
				borrower = new Borrower();
				borrower.setLastname(result.getString(1));
				borrower.setFirstname(result.getString(2));
				borrower.setMobile(result.getString(3));
				borrower.setEmail(result.getString(4));
				borrower.setDob(result.getDate(5));
				borrower.setAddress(result.getString(6));
				borrower.setNationalID(result.getString(7));
				borrower.setStaffID(result.getString(8));
				borrower.setCompanyName(result.getString(9));
				borrower.setMonthlyIncome(result.getString(10));
				borrower.setId(""+result.getObject(11));
				
				arr.add(borrower);
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
