/**
 * 
 */
package com.etz.loanCalculator.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.type.SetType;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Synchronized;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;


import com.etz.loanCalculator.dto.Borrower;
import com.etz.loanCalculator.dto.LoanHouse;
import com.etz.loanCalculator.dto.Menu;
import com.etz.loanCalculator.dto.MenuItem;
import com.etz.loanCalculator.dto.User;
import com.etz.loanCalculator.model.AdminModel;
import com.etz.loanCalculator.model.ReportModel;
import com.etz.loanCalculator.utility.Utils;
import com.etz.security.util.Cryptographer;

import ecard.E_SECURE;

/**
 * @author 
 */
@Restrict("#{authenticator.curUser.loggedIn}")
@Scope(ScopeType.SESSION)
@Name("adminAction")
public class AdministratorAction 
{
	
	
	private User curUser;
	private Menu menu;
	private MenuItem menuItem;
	private LoanHouse loanHouse;
	
	private List<User> userList = new ArrayList<User>();
	private List bankList = new ArrayList();
	private List dealerList = new ArrayList();
	private List userTypeList = new ArrayList();
	
	private List<User> userBizDevList = new ArrayList<User>();
	private List userMapList = new ArrayList();
	private List userPickList = new ArrayList();
	
	//menu
	private List menuToRoleList = new ArrayList();
	private List menuList = new ArrayList();
	private List menuMapToRoleList = new ArrayList();
	
	//menuitem
	private List menuItemToMenuList = new ArrayList();
	private List menuItemList = new ArrayList();
	private List menuItemMapToMenuList = new ArrayList();
	

	private List menuItemToRoleList = new ArrayList();
	private List mappedMenuItemToMenuList = new ArrayList(); //this is used while mapping the menuitem to role
	private List menuItemMapToRoleList = new ArrayList();

	private List ipAddressList = new ArrayList();
	private List menuItemToIpAddressList = new ArrayList();
	private List mappedMenuItemipAddressList = new ArrayList();
	
	private List loanHouseList = new ArrayList();
	
	
	private String edit_id;
	private String operation_id;
	
	private String merchant_name;
	
	@RequestParameter
	private String id;//used in passing values from a jsf to a jsf
	
	@In
    FacesMessages facesMessages;
	
	@Logger
	private Log log;
	
	@In(create = true)
	FileUploadBean fileUploadBean;
	
	private Borrower borrower;

	public void createUser()
	{
		
		try
		{
			AdminModel adminModel = new AdminModel();
			ReportModel model = new ReportModel();
			Utils util = new Utils();
			
			getCurUser().setPassword(util.generateRandomNumber(8));//default password
			
			
			String email = getCurUser().getEmail().trim();
			String password = getCurUser().getPassword().trim();
			String lname = getCurUser().getLastname().trim();
			String fname = getCurUser().getFirstname().trim();
			String mobile = getCurUser().getMobile().trim();
			String type_id = getCurUser().getType_id().trim();
			String status_id = "1";
			String ip_address = "";
			//String service_id = getCurUser().getService_id();
			String esa_auth  = getCurUser().getEsa_type();
			

			try
			{
				//ip_address = InetAddress.getLocalHost().getHostAddress();
				HttpServletRequest str = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
				ip_address = str.getRemoteAddr();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			String user_code = "000";//default code
			user_code = getCurUser().getUser_code();
			
			
			if(esa_auth == null)esa_auth = "1";//no esa
			
			if(type_id.equals("SUPPORT OFFICER") || type_id.equals("ADMINISTRATOR"))
			{
				if(user_code == null)user_code = "000";
			}
			else
			{
				if(user_code == null)user_code = "001";
			}
			
			String username = getCurUser().getUsername().trim();

			FacesContext context = FacesContext.getCurrentInstance();
			String createdby_user_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_id();
			String userType = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getType_id();
			
			if(userType.equals("2"))//loan admin
			{
				user_code = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
			}
			
			
			//convert to type_id
			type_id = adminModel.getUserTypeID(type_id);
			

			String message = adminModel.createUser(email, password, lname, fname, mobile, type_id, status_id, 
					ip_address, user_code, createdby_user_id, username, esa_auth);
			if(message.equals("Records successfully inserted"))
			{
				try
				{
					Events.instance().raiseEvent("testmailer");
					message += ", Email has been sent successfully";

				}
				catch(Exception e)
				{
					e.printStackTrace();
					message += ", Email not sent " + e.getMessage();
				}
				
				getCurUser().setEmail(null);
				getCurUser().setUsername(null);
				getCurUser().setPassword(null);
				getCurUser().setLastname(null);
				getCurUser().setFirstname(null);
				getCurUser().setMobile(null);
				getCurUser().setType_id(null);
				getCurUser().setUser_code(null);
				getCurUser().setService_id(null);
				getCurUser().setEsa_type(null);
			}
			facesMessages.add(Severity.INFO, message);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void createMenu()
	{
		try
		{
			AdminModel adminModel = new AdminModel();

			FacesContext context = FacesContext.getCurrentInstance();
			String createdby_user_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_id();			
			
			String message = adminModel.createMenu(getMenu().getMenu_nm(), getMenu().getMenu_comments(), createdby_user_id);
			if(message.equals("Records successfully inserted"))
			{
				getMenu().setMenu_nm(null);
				getMenu().setMenu_comments(null);
			}
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void createMenuItem()
	{
		try
		{
			AdminModel adminModel = new AdminModel();

			FacesContext context = FacesContext.getCurrentInstance();
			String createdby_user_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_id();			
			
			
			ArrayList arr = new ArrayList();
			
			//System.out.println("menuItemToIpAddressList " + menuItemToIpAddressList.size());
			
			for(int i=0;i<menuItemToIpAddressList.size();i++)
			{
				arr.add(menuItemToIpAddressList.get(i));
			}
			
			
			if(getMenuItem().getExtraParam().equals("RESTRICTED ACCESS") && arr.size() <=0)
			{
				facesMessages.add(Severity.INFO, "No IP Address Selected");
				return;
			}
				
			
			String message = adminModel.createMenuItem(getMenuItem().getMenuitem_nm(), getMenuItem().getMenuitem_link(),
					getMenuItem().getMenuitem_comments(), getMenuItem().getExtraParam(), createdby_user_id, arr);
			if(message.equals("Records successfully inserted"))
			{
				getMenuItem().setMenuitem_nm(null);
				getMenuItem().setMenuitem_link(null);
				getMenuItem().setMenuitem_comments(null);
				getMenuItem().setExtraParam(null);
				setMenuItemToIpAddressList(null);
			}
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	public void createIPAddressRestriction()
	{
		try
		{
			AdminModel adminModel = new AdminModel();

			FacesContext context = FacesContext.getCurrentInstance();
			String createdby_user_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_id();			
			
			String message = adminModel.createIPAddressRestriction(getMenuItem().getIp_address().trim(), createdby_user_id);
			if(message.equals("Records successfully inserted"))
			{
				getMenuItem().setIp_address(null);
			}
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*This method is used to create the loan house*/
	public void createLoanHouse()
	{
		
		try
		{
			AdminModel adminModel = new AdminModel();
			FacesContext context = FacesContext.getCurrentInstance();
			String createdbyuserid = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_id();			
			
			String houseName = loanHouse.getLoanName();
			String houseCode = loanHouse.getCode();
			String exposure = loanHouse.getExposure();
			
			
			System.out.println("fileUploadBean.getByteupload() " + fileUploadBean.getByteupload());
			
			if(fileUploadBean.getByteupload() == null || fileUploadBean.getByteupload().equals("") || fileUploadBean.getByteupload().equals("null"))
			{
				facesMessages.add(Severity.INFO, "Loan House Logo is required");
				return;
			}
			
			
			String uploadedFile =  houseCode  + "_" + createdbyuserid + ".jpg";
			String filePath = "/CreditManager/loanLogo/"  + houseCode  + "_" + createdbyuserid + ".jpg";
			
			String message = adminModel.createLoanHouse(houseName, filePath, createdbyuserid, houseCode, exposure);
			if(message.equals("Records successfully inserted"))
			{
				convertToFile(fileUploadBean.getByteupload(), uploadedFile);
			}
			facesMessages.add(Severity.INFO, message);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
		public void createBorrower() {

        try 
        {
            AdminModel adminModel = new AdminModel();

            String lastname = getBorrower().getLastname();
            String firstname = getBorrower().getFirstname();
            String mobile = getBorrower().getMobile();
            String email = getBorrower().getEmail();
            Date dob = getBorrower().getDob();
            String address = getBorrower().getAddress();
            String nationalID = getBorrower().getNationalID();
            String staffID = getBorrower().getStaffID();
            String companyName = getBorrower().getCompanyName();
            String monthlyIncome = getBorrower().getMonthlyIncome();

            Double d = 0.0;
            try
            {
            	d = Double.parseDouble(monthlyIncome);
            }catch(Exception ex)
            {
            	facesMessages.add(Severity.INFO, "Only digits required for the monthly income");
            	return;
            }
            
            String message = adminModel.createBorrower(lastname,firstname,mobile,email,dob,address,nationalID,staffID,companyName,monthlyIncome);
            if (message.equals("Records successfully inserted")) {
                getBorrower().setEmail(null);
                getBorrower().setAddress(null);
                getBorrower().setCompanyName(null);
                getBorrower().setLastname(null);
                getBorrower().setFirstname(null);
                getBorrower().setMobile(null);
                getBorrower().setDob(null);
                getBorrower().setNationalID(null);
                getBorrower().setStaffID(null);
                getBorrower().setMonthlyIncome(null);
            }
            else if(message.startsWith("A borrower with"))
            {
            	setOperation_id(getBorrower().getMobile());
            	System.out.println("operationID " + getOperation_id());
            }
            
            facesMessages.add(Severity.INFO, message);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
	
	/*This method is used to get restricted ips of a menuitem*/
	public void getMenuItemIPAddress()
	{
		try
		{
			AdminModel adminModel = new AdminModel();

			//System.out.println("am here " + getId());
			String menuitem_id = getId();
			mappedMenuItemipAddressList = adminModel.getMenuItemIPAdrress(menuitem_id);
			setEdit_id(menuitem_id);
			setMenuItemToIpAddressList(null);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*This method is used to add more restricted ips to a menuitem*/
	public void addIPToMenuItem()
	{
		try
		{
			AdminModel adminModel = new AdminModel();

			FacesContext context = FacesContext.getCurrentInstance();
			String createdby_user_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_id();			
			
			ArrayList arr = new ArrayList();
			
			//System.out.println("menuItemToIpAddressList " + menuItemToIpAddressList.size());
			
			for(int i=0;i<menuItemToIpAddressList.size();i++)
			{
				arr.add(menuItemToIpAddressList.get(i));
			}
			
			//System.out.println("id " + getEdit_id());
			String menuitem_id = getEdit_id();
			
			String message = adminModel.addIPToMenuItem(menuitem_id, arr,  createdby_user_id);
			if(message.equals("Records successfully inserted"))
			{
				setMenuItemToIpAddressList(null);
				mappedMenuItemipAddressList = adminModel.getMenuItemIPAdrress(menuitem_id);
			}
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void setToEdit()
	{
		try
		{
			AdminModel adminModel = new AdminModel();
			ArrayList a  = adminModel.getUserByUserID(edit_id);
			if(a.size()>0)
			{
				User u = (User)a.get(0);
				getCurUser().setType_id(adminModel.getUserTypeName(u.getType_id().trim()));
				getBanks();
				getCurUser().setUser_code(u.getUser_code().trim());
				getCurUser().setEmail(u.getEmail().trim());
				getCurUser().setLastname(u.getLastname().trim());
				getCurUser().setFirstname(u.getFirstname().trim());
				getCurUser().setMobile(u.getMobile().trim());
				getCurUser().setUsername(u.getUsername().trim());
				getCurUser().setEsa_type(u.getEsa_type());
				getCurUser().setCardScheme(u.getCardScheme().trim());
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*set the menu to editing option*/
	public void setMenuToEdit()
	{
		try
		{
			AdminModel adminModel = new AdminModel();
			
			
			String menu_id = getEdit_id();
			//System.out.println("menu_id " + menu_id);
			
			ArrayList a  = adminModel.getMenuByMenuID(menu_id);
			if(a.size()>0)
			{
				Menu m = (Menu)a.get(0);
				getMenu().setMenu_nm(m.getMenu_nm().trim());
				getMenu().setMenu_comments(m.getMenu_comments().trim());
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*set the menuitem to editing option*/
	public void setMenuitemToEdit()
	{
		try
		{
			AdminModel adminModel = new AdminModel();
			
			
			String menuitem_id = getEdit_id();
			//System.out.println("menuitem_id " + menuitem_id);
			
			ArrayList a  = adminModel.getMenuitemByMenuID(menuitem_id);
			if(a.size()>0)
			{
				MenuItem m = (MenuItem)a.get(0);
				getMenuItem().setMenuitem_nm(m.getMenuitem_nm().trim());
				getMenuItem().setMenuitem_link(m.getMenuitem_link().trim());
				getMenuItem().setMenuitem_comments(m.getMenuitem_comments().trim());
				getMenuItem().setExtraParam(m.getExtraParam().trim());
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void updateUsers()
	{
		try
		{
			AdminModel admin = new AdminModel();
			
			String email = getCurUser().getEmail().trim();
			String lname = getCurUser().getLastname().trim();
			String fname = getCurUser().getFirstname().trim();
			String mobile = getCurUser().getMobile().trim();
			String type_id = getCurUser().getType_id().trim();
			//String service_id = getCurUser().getService_id().trim();
			String esa_auth = getCurUser().getEsa_type().trim();
			//String cardScheme = getCurUser().getCardScheme().trim();
			
			
			String user_code = "000";//default code
			user_code = getCurUser().getUser_code().trim();
			if(user_code == null)user_code = "000";
			
			String username = getCurUser().getUsername().trim();
			
			FacesContext context = FacesContext.getCurrentInstance();
			String editedby_user_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_id();
			String userType = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getType_id();
			
			if(userType.equals("2"))//loan admin
			{
				user_code = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
			}
			
			//convert to type_id
			type_id = admin.getUserTypeID(type_id);
		
		
			
			String message = admin.updateUser(edit_id, email, lname, fname, mobile, type_id, user_code,
					editedby_user_id, username, esa_auth);
			if(message.equals("Records successfully updated"))
			{
				try
				{
					Events.instance().raiseEvent("updatemailer");
					message += ", Email has been sent successfully";
				}
				catch(Exception e)
				{
					e.printStackTrace();
					message += ", Email not sent " + e.getMessage();
				}
				
				getCurUser().setEmail(null);
				getCurUser().setLastname(null);
				getCurUser().setFirstname(null);
				getCurUser().setMobile(null);
				getCurUser().setType_id(null);
				getCurUser().setUser_code(null);
				getCurUser().setUsername(null);
				getCurUser().setService_id(null);
				getCurUser().setEsa_type(null);
				//getCurUser().setCardScheme(null);
				setEdit_id(null);
			}
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*Updating the menu*/
	public void updateMenu()
	{
		try
		{
			AdminModel admin = new AdminModel();
			FacesContext context = FacesContext.getCurrentInstance();
			String editedby_user_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_id();
			
			
			String message = admin.updateMenu(edit_id, getMenu().getMenu_nm(), getMenu().getMenu_comments(), editedby_user_id);
			if(message.equals("Records successfully updated"))
			{
				getMenu().setMenu_nm(null);
				getMenu().setMenu_comments(null);
				setEdit_id(null);
			}
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*Updating the menuitem*/
	public void updateMenuitem()
	{
		try
		{
			AdminModel admin = new AdminModel();
			FacesContext context = FacesContext.getCurrentInstance();
			String editedby_user_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_id();
			
			ArrayList arr = new ArrayList();
			for(int i=0;i<menuItemToIpAddressList.size();i++)
			{
				arr.add(menuItemToIpAddressList.get(i));
			}
			
			
			if(getMenuItem().getExtraParam().equals("RESTRICTED ACCESS") && arr.size() <=0)
			{
				facesMessages.add(Severity.INFO, "No IP Address Selected");
				return;
			}
			
			String message = admin.updateMenuitem(edit_id, getMenuItem().getMenuitem_nm(), getMenuItem().getMenuitem_link(),
					getMenuItem().getMenuitem_comments(), getMenuItem().getExtraParam(),  arr, editedby_user_id);
			if(message.equals("Records successfully updated"))
			{
				getMenuItem().setMenuitem_nm(null);
				getMenuItem().setMenuitem_link(null);
				getMenuItem().setMenuitem_comments(null);
				getMenuItem().setExtraParam(null);
				setMenuItemToIpAddressList(null);
				setEdit_id(null);
			}
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	public void resetPassword()
	{
		try
		{
			AdminModel admin = new AdminModel();
			Utils util = new Utils();

			String random_password = util.generateRandomNumber(8);
			//System.out.println("random_password " + random_password);
			
			String username  = getEdit_id();
			
			String message = admin.resetPassword(operation_id, random_password, username);
			
			ArrayList arr = admin.getUserByUserID(operation_id);
			if(arr.size()>0)
			{
				User u = (User)arr.get(0);
				getCurUser().setLastname(u.getLastname());
				getCurUser().setFirstname(u.getFirstname());
				getCurUser().setUsername(u.getUsername());
				getCurUser().setPassword(random_password);
				getCurUser().setEmail(u.getEmail());
				setEdit_id(null);
				
				try
				{
					Events.instance().raiseEvent("testmailer");
					message += ", Email has been sent successfully";
				}
				catch(Exception e)
				{
					e.printStackTrace();
					message += ", Email not sent " + e.getMessage();
				}
				
				facesMessages.add(Severity.INFO, message + " , please confirm from your mailbox");
			}
			
			curUser.setEmail(null);
			curUser.setLastname(null);
			curUser.setFirstname(null);
			curUser.setUsername(null);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			facesMessages.add(Severity.INFO, "Email not sent, " + ex.getMessage());
			curUser.setEmail(null);
			curUser.setLastname(null);
			curUser.setFirstname(null);
			curUser.setUsername(null);
		}
	}
	
	public void disable_enableUser()
	{
		try
		{
			AdminModel admin = new AdminModel();
			String message = admin.enable_disableUser(operation_id, edit_id);
			setEdit_id(null);
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void resendEmail()
	{
		try
		{
			AdminModel admin = new AdminModel();
			ecard.E_SECURE secure = new E_SECURE();
			
			ArrayList arr = admin.getUserByUserID(operation_id);
			if(arr.size()>0)
			{
				User u = (User)arr.get(0);
				getCurUser().setLastname(u.getLastname());
				getCurUser().setFirstname(u.getFirstname());
				getCurUser().setUsername(u.getUsername());
				getCurUser().setPassword(secure.f_decode(u.getPassword()));
				getCurUser().setEmail(u.getEmail());
				
				Events.instance().raiseEvent("testmailer");
				facesMessages.add(Severity.INFO, "Email has been resent successfully");
			}
			
			curUser.setEmail(null);
			curUser.setLastname(null);
			curUser.setFirstname(null);
			curUser.setUsername(null);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			facesMessages.add(Severity.INFO, "Email not sent, " + ex.getMessage());
			curUser.setEmail(null);
			curUser.setLastname(null);
			curUser.setFirstname(null);
			curUser.setUsername(null);
		}
		
	}
	
	public void getBanks()
	{
		try
		{
			AdminModel adminModel = new AdminModel();
			ReportModel reportModel = new ReportModel();
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	public void getLoanHouses()
	{
		try
		{
			ReportModel reportModel = new ReportModel();
			loanHouseList = reportModel.getLoanHouses();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	
	
	public void mapAccountIDToBizDev()
	{
		try
		{
			AdminModel adminModel = new AdminModel();

			String username = getCurUser().getUsername();
			String account_id = getCurUser().getAccount_id().trim();
			
			String message = adminModel.mapAccountIDToBizDevUser(username, account_id);
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	
	//Map MenuItem to Role
	public void mapMenuToRole()
	{
		try
		{
			AdminModel adminModel = new AdminModel();
			FacesContext context = FacesContext.getCurrentInstance();
			String createdby_user_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_id();
			

			String userType_id = getCurUser().getType_id();
			ArrayList arr = new ArrayList();
			
			for(int i=0;i<menuToRoleList.size();i++)
			{
				arr.add(menuToRoleList.get(i));
			}
			
			//String message = "Records successfully inserted";
			
			String message = adminModel.mapMenuToRole(arr, userType_id, createdby_user_id);
			if(message.equals("Records successfully inserted"))
			{
				curUser = new User();
				menuMapToRoleList.clear();
			}
			
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	//Map MenuItem to Menu
	public void mapMenuItemToMenu()
	{
		try
		{
			AdminModel adminModel = new AdminModel();
			FacesContext context = FacesContext.getCurrentInstance();
			String createdby_user_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_id();
			

			String menu_id = getMenu().getMenu_id();
			ArrayList arr = new ArrayList();
			
			for(int i=0;i<menuItemToMenuList.size();i++)
			{
				arr.add(menuItemToMenuList.get(i));
			}
		
			
			String message = adminModel.mapMenuItemToMenu(arr, menu_id, createdby_user_id);
			if(message.equals("Records successfully inserted"))
			{
				menuItemMapToMenuList = adminModel.getMappedMenuItemsToMenus(menu_id);
				setMenuItemToMenuList(null);
			}
			
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	//Map MenuItem to Role
	public void mapMenuItemToRole()
	{
		try
		{
			AdminModel adminModel = new AdminModel();
			FacesContext context = FacesContext.getCurrentInstance();
			String createdby_user_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_id();
			

			String userType_id = getCurUser().getType_id();
			ArrayList arr = new ArrayList();
			
			for(int i=0;i<menuItemToRoleList.size();i++)
			{
				arr.add(menuItemToRoleList.get(i));
			}
		
			
			String message = adminModel.mapMenuItemToRole(arr, userType_id, createdby_user_id);
			if(message.equals("Records successfully inserted"))
			{
				menuItemMapToRoleList = adminModel.getMappedMenuItemsToRoles(userType_id);
				setMenuItemToRoleList(null);
			}
			
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
		
	public void loadMappedMenusToRoles()
	{
		try
		{
			AdminModel adminModel = new AdminModel();
			String userType_id = getCurUser().getType_id();
			menuMapToRoleList = adminModel.getMappedMenusToRoles(userType_id);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*Method to load the menuitems that have been mapped to menus*/
	public void loadMappedMenuItemsToMenus()
	{
		try
		{
			AdminModel adminModel = new AdminModel();
			String menu_id = getMenu().getMenu_id();
			menuItemMapToMenuList = adminModel.getMappedMenuItemsToMenus(menu_id);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*Method to load the menuitems that have been mapped to roles*/
	public void loadMappedMenuItemsToRoles()
	{
		try
		{
			AdminModel adminModel = new AdminModel();
			String userType_id = getCurUser().getType_id();
			menuItemMapToRoleList = adminModel.getMappedMenuItemsToRoles(userType_id);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	
	/*Method to delete created menu*/
	public void deleteMenu()
	{
		try
		{
			AdminModel adminModel = new AdminModel();
			String menu_id = getOperation_id().trim();
			String message  = adminModel.deleteMenu(menu_id);
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*Method to delete created menuitem*/
	public void deleteMenuItem()
	{
		try
		{
			AdminModel adminModel = new AdminModel();
			String menuitem_id = getOperation_id().trim();
			//System.out.println("menuitem_id " + menuitem_id);
			
			String message  = adminModel.deleteMenuItem(menuitem_id);
			setEdit_id(null);
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	
	/*Method to delete the mappedMenuItemToMenu*/
	public void deleteMappedMenuItemToMenu()
	{
		try
		{
			AdminModel adminModel = new AdminModel();
			String menuitemToMenuId = getOperation_id().trim();
			String menu_id = getMenu().getMenu_id();
			
			//System.out.println("menuitemToMenuId " + menuitemToMenuId);
			
			String message  = adminModel.deleteMappedMenuItemToMenu(menuitemToMenuId);
			if(message.equals("Records successfully deleted"))
			{
				menuItemMapToMenuList = adminModel.getMappedMenuItemsToMenus(menu_id);
			}
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	/*Method to delete the mappedMenuItemToRole*/
	public void deleteMappedMenuItemToRole()
	{
		try
		{
			AdminModel adminModel = new AdminModel();
			String id = getOperation_id().trim();
			String userType_id = getCurUser().getType_id();
			//System.out.println("id " + id);
			
			String message  = adminModel.deleteMappedMenuItemToRole(id);
			if(message.equals("Records successfully deleted"))
			{
				menuItemMapToRoleList = adminModel.getMappedMenuItemsToRoles(userType_id);
			}
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*Method to delete ip addresses used for restriction purpose*/
	public void deleteIPAddress()
	{
		try
		{
			AdminModel adminModel = new AdminModel();
			String id = getOperation_id().trim();
			//System.out.println("id " + id);
			
			String message  = adminModel.deleteIPAddress(id);
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*Method to delete menuitem ip addresses used for restriction purpose*/
	public void deleteMenuitemIPAddress()
	{
		try
		{
			AdminModel adminModel = new AdminModel();
			String id = getOperation_id().trim();
			String menuitem_id = getEdit_id();
			//System.out.println("id " + id);
			//System.out.println("menuitem_id " + menuitem_id);
			
			String message  = adminModel.deleteMenuitemIPAddress(id);
			if(message.equals("Records successfully deleted"))
			{
				mappedMenuItemipAddressList = adminModel.getMenuItemIPAdrress(menuitem_id);
			}
			
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	

	public String secondLevelIPValidation()
	{
		//System.out.println("all pages must enter here");
		AdminModel adminModel = new AdminModel();
		String menuitem_page = "";
		String ip_address = "";
		String navigationPage = "";
		
		try
		{
			//System.out.println("url " + menuitem_page);
			
			menuitem_page = FacesContext.getCurrentInstance().getViewRoot().getViewId();
			//HttpServletRequest str = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			//str.getRemoteHost();

			try
			{
				ip_address = InetAddress.getLocalHost().getHostAddress();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
			ArrayList b = adminModel.secondLevelIPValidation(menuitem_page, ip_address);
			//System.out.println("b " + b.size());
			if(b.size()>1);
			else
			{
				navigationPage = "/support/accessRestriction.etz";
			}
		}
		catch(Exception ex){ex.printStackTrace();}
		
		return navigationPage;
	}
	
	
	
	public void resetValues()
	{
		try
		{
			curUser = new User();
			menu = new Menu();
			menuItem = new MenuItem();
			
			edit_id = null;
			userMapList.clear();
			menuList.clear();
			menuItemList.clear();		
			
			curUser.setType_id(null);
			menu.setMenu_id(null);
			
			menuItemMapToMenuList.clear();
			menuItemMapToRoleList.clear();
			menuItemToIpAddressList.clear();
			loanHouse = new LoanHouse();
			setOperation_id(null);
			borrower = new Borrower();
		}
		catch(Exception ex)
		{
			System.out.println("Internal error occured " + ex.getMessage());
		}
	}

	public double makeDouble(String value)
	{
		double resp = 0.00;
		try
		{
			if(value == null || value.equals("") || value.equals("null"))
			{
				value= "0.00";
			}
			else
			{
				resp = Double.parseDouble(value);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return resp;
	}
	
	public String convertToFile(byte[] b ,String name)
	{
    	String message = "";
		try 
		{			
			File g = new File("");
            Properties props = new Properties();
            InputStream in = (InputStream)(new FileInputStream(new File(g.getAbsolutePath() + "\\" + "creditManager-config.properties")));
            props.load(in);
            String setup_excel_folder = props.getProperty("lendingInstLogo");
	        //String setup_excel_folder = "C:\\Tony\\tomcat-6.0.30\\webapps\\loanCalculator\\loanLogo";
	        
	        System.out.println("path to setup file " + setup_excel_folder);
	        
	        File f = new File(setup_excel_folder);
			if(f.exists())
			{
				java.io.FileOutputStream fout = new java.io.FileOutputStream(setup_excel_folder + "\\" + name);
				fout.write(b);
				fout.close();
			}
			else
			{
				f.mkdirs();
				java.io.FileOutputStream fout = new java.io.FileOutputStream(setup_excel_folder + "\\" + name);
				fout.write(b);
				fout.close();
			}
			message = "Records successfully queued";
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			message = "Server error occured";
		}
		return message;
	}
	
	public User getCurUser() 
	{
		
		if(curUser == null)
		{
			curUser = new User();
		}
		
		
		return curUser;
	}


	public void setCurUser(User curUser) {
		this.curUser = curUser;
	}
	
	
	

	public List getUserList() 
	{
		AdminModel adminModel = new AdminModel();
		
		FacesContext context = FacesContext.getCurrentInstance();
		String userType = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getType_id();
		String user_code = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_code();
		//String service_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getService_id();

		
		if(userType.equals("1"))
			userList = adminModel.getUsers();
		
		else if(userType.equals("2"))//loan admins
			userList = adminModel.getLoanUsers(user_code);
		
		return userList;
	}

	public void setUserList(List userList) {
		this.userList = userList;
	}

	public List getBankList() {
		return bankList;
	}

	public void setBankList(List bankList) {
		this.bankList = bankList;
	}

	public String getEdit_id() {
		return edit_id;
	}

	public void setEdit_id(String edit_id) {
		this.edit_id = edit_id;
	}

	public String getOperation_id() {
		return operation_id;
	}

	public void setOperation_id(String operation_id) {
		this.operation_id = operation_id;
	}

	public List getDealerList() {
		return dealerList;
	}

	public void setDealerList(List dealerList) {
		this.dealerList = dealerList;
	}

	public List getUserTypeList() 
	{
		
		AdminModel adminModel = new AdminModel();
		
		userTypeList = adminModel.getUserType();
		
		return userTypeList;
	}

	public void setUserTypeList(List userTypeList) {
		this.userTypeList = userTypeList;
	}


	public List getUserPickList()
	{
		return userPickList;
	}

	public void setUserPickList(List userPickList) {
		this.userPickList = userPickList;
	}

	public List<User> getUserBizDevList() 
	{
		AdminModel adminModel = new AdminModel();
		userBizDevList = adminModel.getUsersByType("BUSINESS DEVELOPMENT");
		
		return userBizDevList;
	}

	public void setUserBizDevList(List<User> userBizDevList) {
		this.userBizDevList = userBizDevList;
	}

	public List getMenuToRoleList() {
		return menuToRoleList;
	}

	public void setMenuToRoleList(List menuToRoleList) {
		this.menuToRoleList = menuToRoleList;
	}
	


	public List getMenuList() 
	{
		try
		{
			AdminModel adminModel = new AdminModel();
			menuList = adminModel.getMenus();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return menuList;
	}

	public void setMenuList(List menuList) {
		this.menuList = menuList;
	}

	public List getMenuMapToRoleList() 
	{
		return menuMapToRoleList;
	}

	public void setMenuMapToRoleList(List menuMapToRoleList) {
		this.menuMapToRoleList = menuMapToRoleList;
	}

	public Menu getMenu() 
	{
		if(menu == null)
		{
			menu = new Menu();
		}
		
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public MenuItem getMenuItem() 
	{
		if(menuItem == null)
		{
			menuItem = new MenuItem();
		}
		return menuItem;
	}

	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}

	public List getMenuItemList() 
	{
		try
		{
			AdminModel adminModel = new AdminModel();
			menuItemList = adminModel.getMenuItems();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return menuItemList;
	}

	public void setMenuItemList(List menuItemList) {
		this.menuItemList = menuItemList;
	}

	public List getMenuItemMapToMenuList() {
		return menuItemMapToMenuList;
	}

	public void setMenuItemMapToMenuList(List menuItemMapToMenuList) {
		this.menuItemMapToMenuList = menuItemMapToMenuList;
	}

	public List getMenuItemToMenuList() {
		return menuItemToMenuList;
	}

	public void setMenuItemToMenuList(List menuItemToMenuList) {
		this.menuItemToMenuList = menuItemToMenuList;
	}

	public List getMenuItemToRoleList() {
		return menuItemToRoleList;
	}

	public void setMenuItemToRoleList(List menuItemToRoleList) {
		this.menuItemToRoleList = menuItemToRoleList;
	}

	public List getMappedMenuItemToMenuList() 
	{
		try
		{
			AdminModel adminModel = new AdminModel();
			mappedMenuItemToMenuList = adminModel.getMappedMenuItemsToMenus2();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return mappedMenuItemToMenuList;
	}

	public void setMappedMenuItemToMenuList(List mappedMenuItemToMenuList) {
		this.mappedMenuItemToMenuList = mappedMenuItemToMenuList;
	}

	public List getMenuItemMapToRoleList() {
		return menuItemMapToRoleList;
	}

	public void setMenuItemMapToRoleList(List menuItemMapToRoleList) {
		this.menuItemMapToRoleList = menuItemMapToRoleList;
	}

	public List getIpAddressList() 
	{
		try
		{
			AdminModel adminModel = new AdminModel();
			ipAddressList = adminModel.getIPAddress();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return ipAddressList;
	}

	public void setIpAddressList(List ipAddressList) {
		this.ipAddressList = ipAddressList;
	}

	public List getMenuItemToIpAddressList() {
		return menuItemToIpAddressList;
	}

	public void setMenuItemToIpAddressList(List menuItemToIpAddressList) {
		this.menuItemToIpAddressList = menuItemToIpAddressList;
	}

	public List getMappedMenuItemipAddressList() {
		return mappedMenuItemipAddressList;
	}

	public void setMappedMenuItemipAddressList(List mappedMenuItemipAddressList) {
		this.mappedMenuItemipAddressList = mappedMenuItemipAddressList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LoanHouse getLoanHouse() 
	{
		if(loanHouse == null)
		{
			loanHouse = new LoanHouse();
		}
		
		return loanHouse;
	}

	public void setLoanHouse(LoanHouse loanHouse) {
		this.loanHouse = loanHouse;
	}

	public List getLoanHouseList() 
	{
		getLoanHouses();
		return loanHouseList;
	}

	public void setLoanHouseList(List loanHouseList) {
		this.loanHouseList = loanHouseList;
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
	
	
	
}
