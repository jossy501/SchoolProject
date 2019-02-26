/**
 * 
 */
package com.etz.loanCalculator.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.io.FileOutputStream;
import java.io.OutputStream;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
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

import com.etz.http.etc.Card;
import com.etz.http.etc.HttpHost;
import com.etz.http.etc.TransCode;
import com.etz.http.etc.XProcessor;
import com.etz.http.etc.XRequest;
import com.etz.http.etc.XResponse;
import com.etz.loanCalculator.dto.Borrower;
import com.etz.loanCalculator.dto.LoanHouse;
import com.etz.loanCalculator.dto.Menu;
import com.etz.loanCalculator.dto.MenuItem;
import com.etz.loanCalculator.dto.Transaction;
import com.etz.loanCalculator.dto.User;
import com.etz.loanCalculator.model.AdminModel;
import com.etz.loanCalculator.model.ReportModel;
import com.etz.loanCalculator.utility.Utils;
import com.etz.mobile.security.CardAudit;
import com.etz.security.util.Cryptographer;
import com.etz.security.util.PBEncryptor;

/**
 * @author tony.ezeanya
 *
 */
@Restrict("#{authenticator.curUser.loggedIn}")
@Scope(ScopeType.SESSION)
@Name("reportAction")
public class ReportAction implements Serializable
{
	
	FacesContext context;
	private List borrowerInfoList = new ArrayList();
	private List borrowerTranList = new ArrayList();
	private List borrowerTranBreakDownList = new ArrayList();
	private String editId;
	private String systemAdvice;
	
	@RequestParameter
	private String id;//used in passing values from a jsf to a jsf
	
	@In
    FacesMessages facesMessages;
	
	@Logger
	private Log log;
	
	@In(create = true)
	FileUploadBean fileUploadBean;
	
	private Borrower borrower; 
	private double totalMonthlyPayBack = 0.0d;
	private double totalLeftOver = 0.0d;

	
	/*This method is used to get the borrower transaction history*/
	public void getBorrowerTrasactionHistory()
	{
		
		try
		{
			ReportModel rmodel = new ReportModel();
			String borrowerMobile = borrower.getMobile();
			String borrowerNationalID = borrower.getNationalID();
			totalMonthlyPayBack = 0.0;
			
			if(borrowerMobile == null && borrowerNationalID == null)
			{
				facesMessages.add(Severity.INFO, "Please specify either the mobile number or national id");
				return;
			}
			
			borrowerInfoList = rmodel.getBorrowerInfo(borrowerMobile, borrowerNationalID);
			if(borrowerInfoList.size()>0)
			{
				Borrower b = (Borrower)borrowerInfoList.get(0);
				getBorrower().setLastname(b.getLastname());
				getBorrower().setFirstname(b.getFirstname());
				getBorrower().setNationalID(b.getNationalID());
				getBorrower().setMonthlyIncome(b.getMonthlyIncome());
				getBorrower().setCompanyName(b.getCompanyName());
				borrowerMobile = b.getMobile();
			}
			
			borrowerTranList = rmodel.getBorrowerTrasactionHistory(borrowerMobile);
			for(int i=0;i<borrowerTranList.size();i++)
			{
				Transaction t = (Transaction)borrowerTranList.get(i);
				totalMonthlyPayBack += Double.parseDouble(t.getMonthlyPayBack());				
			}
			
			
			context = FacesContext.getCurrentInstance();
			String exp = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getExposure();
			
			
			double salaryPay = Double.parseDouble(getBorrower().getMonthlyIncome());
			double exposure = Double.parseDouble(exp);//lender exposure
			totalLeftOver = (exposure/100) * salaryPay;//amount set aside for spending
			totalLeftOver = totalLeftOver - totalMonthlyPayBack;
			System.out.println("systemAdvice " + totalLeftOver);
			
			System.out.println("borrowerTranList " + borrowerTranList.size());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void getBorrowerTransactionBreakDown()
	{
		try
		{
			ReportModel rmodel = new ReportModel();
			String[] v = id.split(":");
			String borrowerId = v[0];
			String lendingHouseId = v[1];
			borrowerTranBreakDownList = rmodel.getBorrowerTransactionBreakDown(borrowerId, lendingHouseId);
			System.out.println("borrowerTranBreakDownList " + borrowerTranBreakDownList.size() + " " + borrowerTranList.size());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void doCalc()
	{
		System.out.println("in here");
		double amt = 0.0;
		int dur = 0;
		try
		{
			amt = Double.parseDouble(borrower.getAmountToBorrow());			
		}
		catch(Exception ex)
		{
			amt = 0.0;
			ex.printStackTrace();
		}
		
		try
		{
			dur = Integer.parseInt(borrower.getDuration());			
		}
		catch(Exception ex)
		{
			dur = 0;
			ex.printStackTrace();
		}
		
		try
		{
			double sysAdv = (amt / dur);
			
			BigDecimal number = new BigDecimal(sysAdv);
			number = number.setScale(2, RoundingMode.DOWN);
			setSystemAdvice("Payment for " + dur + " months is averagely "+  number +" per month");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	public void approveLoan()
	{
		
		try
		{
			ReportModel rmodel = new ReportModel();
			context = FacesContext.getCurrentInstance();
			String lendingHouseId = ((AuthenticationAction)context.getExternalContext()
					.getSessionMap().get("authenticator")).getCurUser().getLendingHouseId();
			
			String borrowerId = "";
			String borrowerMobile = borrower.getMobile();
			String borrowerNationalID = borrower.getNationalID();
			
			borrowerInfoList = rmodel.getBorrowerInfo(borrowerMobile, borrowerNationalID);
			if(borrowerInfoList.size()>0)
			{
				Borrower b = (Borrower)borrowerInfoList.get(0);
				borrowerId = b.getId();
			}
			
			String amount = borrower.getAmountToBorrow();
			String duration = borrower.getDuration();
			
			
			System.out.println("borrowerId " + borrowerId); 
			System.out.println("lendingHouseId " + lendingHouseId); 
			System.out.println("amount " + amount); 
			System.out.println("duration " + duration); 
			
			String message = rmodel.approveLoan(borrowerId, lendingHouseId, amount, duration);
			System.out.println("message " + message); 
			if(message.equals("Records inserted"))
			{
				getBorrowerTrasactionHistory();
				getBorrower().setAmountToBorrow(null);
				getBorrower().setDuration(null);
				setSystemAdvice(null);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void resetValues()
	{
		borrowerTranList.clear();
		borrowerTranList = new ArrayList();
		borrowerInfoList.clear();
		setEditId(null);
		borrower = new Borrower();
		systemAdvice = null;
	}


	public List getBorrowerTranList() {
		return borrowerTranList;
	}


	public void setBorrowerTranList(List borrowerTranList) {
		this.borrowerTranList = borrowerTranList;
	}


	public String getEditId() {
		return editId;
	}


	public void setEditId(String editId) {
		this.editId = editId;
	}

	public List getBorrowerTranBreakDownList() {
		return borrowerTranBreakDownList;
	}

	public void setBorrowerTranBreakDownList(List borrowerTranBreakDownList) {
		this.borrowerTranBreakDownList = borrowerTranBreakDownList;
	}

	public List getBorrowerInfoList() {
		return borrowerInfoList;
	}

	public void setBorrowerInfoList(List borrowerInfoList) {
		this.borrowerInfoList = borrowerInfoList;
	}

	public Borrower getBorrower() 
	{
		if (borrower == null) {
            borrower = new Borrower();
        }
		return borrower;
	}

	public void setBorrower(Borrower borrower) {
		this.borrower = borrower;
	}

	public double getTotalMonthlyPayBack() {
		return totalMonthlyPayBack;
	}

	public void setTotalMonthlyPayBack(double totalMonthlyPayBack) {
		this.totalMonthlyPayBack = totalMonthlyPayBack;
	}

	public double getTotalLeftOver() {
		return totalLeftOver;
	}

	public void setTotalLeftOver(double totalLeftOver) {
		this.totalLeftOver = totalLeftOver;
	}

	public String getSystemAdvice() {
		return systemAdvice;
	}

	public void setSystemAdvice(String systemAdvice) {
		this.systemAdvice = systemAdvice;
	}
	
	
	
}
