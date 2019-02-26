
package com.etz.loanCalculator.action;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;



import com.etz.loanCalculator.dto.MenuItemToRole;
import com.etz.loanCalculator.dto.User;
import com.etz.loanCalculator.model.AdminModel;
import com.etz.loanCalculator.model.AuthenticationModel;
import com.etz.loanCalculator.utility.Utils;
import com.etz.security.util.Cryptographer;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

@Scope(ScopeType.SESSION)
@Name("authenticator")
public class AuthenticationAction implements Serializable
{
	private User curUser;
	
	FacesContext context;
	
	@In
    FacesMessages facesMessages;

    @Logger
    private Log log;
    
    @Out(required = false, scope = ScopeType.SESSION)
    private User loginUser;
	
    //private List<MenuItemToRole> userMenuOptions;
    private List userMenuOptions;
	
	public User getCurUser()
	{
		if(curUser == null)
		{
			curUser = new User();
		}
		
		return curUser;
	}
	
	public void setCurUser(User curUser)
	{
		this.curUser = curUser;
	}
	
	
	public String getBundleMessage(String key)
	{
		FacesContext context = FacesContext.getCurrentInstance();

		String text = Utils.getMessageResourceString(context.getApplication()
				.getMessageBundle(), key, null, context.getViewRoot()
				.getLocale());

		return text;
	}
	

	/*ESA Validation*/
	/*public String esaValidation(String process)
	{
		String ret = "";
		try
        { 
			FacesContext context = FacesContext.getCurrentInstance();
			String username = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUsername();
			String sessionid = "";
			
			// Call Web Service Operation
			HttpServletRequest str = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

			System.out.println("about to call webservice");
			
            com.etz.sa.ws.server.SAService service = new com.etz.sa.ws.server.SAService(new URL("file:c:/tony/SA.xml"));
            com.etz.sa.ws.server.SA port = service.getSAPort();
            
           
            String appid = "xpo8200087130";
            //xpo8200087130 : xpo8200087130
            
            
            
            switch(Integer.parseInt(process))
            {
	            case 1:
	            	
	            	
	            	String tok = "";
	                String flag = "0";
	            	
	                String result = port.validateToken(username, appid, tok, flag);
	                String[] message = result.split(":");
	                System.out.println("Result 0 = "+message[0]);
	                System.out.println("Result 1 = "+message[1]);
	                if(message[0].equals("0"))//means successful
	                {
	                	System.out.println("Forwarding");
	                	ret = "/esa/esaValidation2.xhtml";
	                }
	                else
	                {
	                	if(message[0].equals("11"))
	                	{
	                		facesMessages.add(Severity.WARN, "Invalid OTP Code");
	                	}
	                	else if(message[0].equals("1"))
	                	{
	                		facesMessages.add(Severity.WARN, "Invalid application location");
	                	}
	                	else if(message[0].equals("2"))
	                	{
	                		facesMessages.add(Severity.WARN, "Application not found");
	                	}
	                	else if(message[0].equals("3"))
	                	{
	                		facesMessages.add(Severity.WARN, "Application not found");
	                	}
	                	else if(message[0].equals("4"))
	                	{
	                		facesMessages.add(Severity.WARN, "User Not Active");
	                	}
	                	else if(message[0].equals("5"))
	                	{
	                		facesMessages.add(Severity.WARN, "Unable to fetch User details");
	                	}
	                	else if(message[0].equals("6"))
	                	{
	                		facesMessages.add(Severity.WARN, "Unable to fetch User details");
	                	}
	                	else if(message[0].equals("7"))
	                	{
	                		facesMessages.add(Severity.WARN, "User/Application map Not Active");
	                	}
	                	else if(message[0].equals("8"))
	                	{
	                		facesMessages.add(Severity.WARN, "User/Application map Not Disabled");
	                	}
	                	else if(message[0].equals("9"))
	                	{
	                		facesMessages.add(Severity.WARN, "User/Application map not found");
	                	}  
	                	else if(message[0].equals("10"))
	                	{
	                		facesMessages.add(Severity.WARN, "User/Application map not found");
	                	}
	                	else if(message[0].equals("12"))
	                	{
	                		facesMessages.add(Severity.WARN, "User SYNC Required");
	                	}
	                	else if(message[0].equals("13"))
	                	{
	                		facesMessages.add(Severity.WARN, "Error Sending SMS");
	                	}
	                	
	                }
	            	
	                break;
	            	
	            case 2:
	            	
	            	tok = getCurUser().getEsa_code();
	                flag = "1";
	            	
	                result = port.validateToken(username, appid, tok, flag);
	                message = result.split(":");
	                System.out.println("Result 0 = "+message[0]);
	                System.out.println("Result 1 = "+message[1]);
	                if(message[0].equals("0"))//means successful
	                {
	                	User u = getCurUser();
	                	userProfileMenu(u.getType_id());
	                	u.setLoggedIn(true);
	                	ret = "/welcome.xhtml";
	                }
	                else
	                {
	                	if(message[0].equals("11"))
	                	{
	                		facesMessages.add(Severity.WARN, "Invalid OTP Code");
	                	}
	                	else if(message[0].equals("1"))
	                	{
	                		facesMessages.add(Severity.WARN, "Invalid application location");
	                	}
	                	else if(message[0].equals("2"))
	                	{
	                		facesMessages.add(Severity.WARN, "Application not found");
	                	}
	                	else if(message[0].equals("3"))
	                	{
	                		facesMessages.add(Severity.WARN, "Application not found");
	                	}
	                	else if(message[0].equals("4"))
	                	{
	                		facesMessages.add(Severity.WARN, "User Not Active");
	                	}
	                	else if(message[0].equals("5"))
	                	{
	                		facesMessages.add(Severity.WARN, "Unable to fetch User details");
	                	}
	                	else if(message[0].equals("6"))
	                	{
	                		facesMessages.add(Severity.WARN, "Unable to fetch User details");
	                	}
	                	else if(message[0].equals("7"))
	                	{
	                		facesMessages.add(Severity.WARN, "User/Application map Not Active");
	                	}
	                	else if(message[0].equals("8"))
	                	{
	                		facesMessages.add(Severity.WARN, "User/Application map Not Disabled");
	                	}
	                	else if(message[0].equals("9"))
	                	{
	                		facesMessages.add(Severity.WARN, "User/Application map not found");
	                	}  
	                	else if(message[0].equals("10"))
	                	{
	                		facesMessages.add(Severity.WARN, "User/Application map not found");
	                	}
	                	else if(message[0].equals("12"))
	                	{
	                		facesMessages.add(Severity.WARN, "User SYNC Required");
	                	}
	                	
	                }
	            	
	            	break;
	            		
	            default:
	            	break;
            }
 
        }catch (Exception ex) 
        {
            ex.printStackTrace();
        }
        return ret;
	}*/
	
	/**
	 * Authenticates the user based on the supplied username and password.
	 * */
	public String signIn()
	{		
		AuthenticationModel authModel = new AuthenticationModel();
		String ret = "index";
		context = FacesContext.getCurrentInstance();
		
		try
		{
			HttpServletRequest str = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String ip_address = str.getRemoteAddr();
			ip_address = InetAddress.getLocalHost().getHostAddress();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		
		User u = getCurUser();
		User user = null;
		user = authModel.login(u.getUsername(), u.getPassword());
		if(user == null)
 		{
			facesMessages.add(Severity.WARN, "user profile doesn't exist, please register");
			u.setFirst_logon(null);
		}
		else
		{
			if(user.getStatus_id().equals("2"))//not active
			{
				facesMessages.add(Severity.WARN, "user profile has been disabled, please refer to your administrator");
			}
			else
			{
				if(user.getFirst_logon().equals("1"))
				{
					u.setUser_id(user.getUser_id());
					u.setLastname(user.getLastname());
					u.setFirstname(user.getFirstname());
					u.setEmail(user.getEmail());
					u.setMobile(user.getMobile());
					u.setUsername(user.getUsername());
					u.setType_id(user.getType_id());
					u.setType_nm(user.getType_nm());
					u.setUser_code(user.getUser_code());
					u.setFirst_logon(user.getFirst_logon());
					u.setEsa_type(user.getEsa_type());
					u.setExposure(user.getExposure());
					u.setLendingHouseId(user.getLendingHouseId());
					u.setLoanLogo(user.getLoanLogo());
					if(u.getLoanLogo() == null || u.getLoanLogo().trim().length()<=0)
						u.setLoanLogo("/CreditManager/images/header_b.jpg");
					
					System.out.println("u.getT " + u.getType_nm() + " " + u.getLoanLogo());
					
					if(user.getEsa_type().equals("0"))//0 means that this user must go through ESA
					{
						//ret = "/esa/esaValidation2.xhtml";
						//ret = esaValidation("1");
					}
					else
					{
						u.setLoggedIn(true);
						
						//this method helps to get the menus of the user
						userProfileMenu(user.getType_id());
						ret = "/welcome.xhtml";
					}
				}
				else if(user.getFirst_logon().equals("0"))
				{
					u.setUser_id(user.getUser_id());
					u.setLoggedIn(false);
					u.setFirst_logon(user.getFirst_logon());
					
					ret = "/support/changeFirstPassword.xhtml";
				}
			}
		}
	
		return ret;
	}
	
	/**
	 * Signs out a user.
	 * */
	public String signOut()
	{
		String ret = "index.xhtml";
		
		context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getBundleMessage("org.jboss.seam.loginFailed"), null));
		
		org.jboss.seam.web.Session.getInstance().invalidate();
		
		return ret;
	}
	
	/*This method is used for changing passwords*/
	public void changePassword()
	{
		AuthenticationModel authModel = new AuthenticationModel();
		AdminModel adminModel = new AdminModel();
		try
		{
			context = FacesContext.getCurrentInstance();
			String user_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_id();
			
			ArrayList arr = adminModel.getUserByUserID(user_id);
			if(arr.size()>0)
			{
				User u = (User)arr.get(0);
				String message = authModel.changePassword(user_id, curUser.getOld_password(), curUser.getConfirm_password(), u.getUsername());
				facesMessages.add(Severity.WARN, message);
			}
			
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void changeFirstPassword()
	{
		AuthenticationModel authModel = new AuthenticationModel();
		AdminModel adminModel = new AdminModel();
		try
		{
			context = FacesContext.getCurrentInstance();
			String user_id = ((AuthenticationAction)context.getExternalContext().getSessionMap().get("authenticator")).getCurUser().getUser_id();
			
			ArrayList arr = adminModel.getUserByUserID(user_id);
			if(arr.size()>0)
			{
				User u = (User)arr.get(0);
				String message = authModel.changePassword(user_id, curUser.getOld_password(), curUser.getConfirm_password(), u.getUsername());
				if(message.equals("Records successfully updated"))
				{
					signIn();
				}
				facesMessages.add(Severity.WARN, message);
			}
		}
		catch(Exception ex)
		{
			System.out.println("WARNING :[ERROR] : in changeFirstPassword() ");
			ex.printStackTrace();
		}
	}
	
	//Method to get the menus of the user
	public void userProfileMenu(String userType)
	{
		AdminModel adminModel = new AdminModel();
		
		try
		{
			//menu options
			String menutemToMenu_id = "";
			MenuItemToRole mr = null;
			String ip_address = "";
			
			ArrayList arr_menuitemToMenu_id = new ArrayList();
			arr_menuitemToMenu_id = adminModel.getMappedMenuItemsToRoles(userType);
			for(int i=0;i<arr_menuitemToMenu_id.size();i++)
			{
				mr = (MenuItemToRole)arr_menuitemToMenu_id.get(i);
				
				if(i == arr_menuitemToMenu_id.size()-1)
					menutemToMenu_id += mr.getMenuItemToMenu_id();
				else
					menutemToMenu_id += mr.getMenuItemToMenu_id() + ",";	
			}

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
			System.out.println("ip_address " + ip_address);
			userMenuOptions = adminModel.getDistinctMenu(menutemToMenu_id, ip_address);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void validateSession()
    {
    	System.out.println("are u logged in ?");
    }
	
	
	public List getUserMenuOptions() {
		return userMenuOptions;
	}

	public void setUserMenuOptions(List userMenuOptions) {
		this.userMenuOptions = userMenuOptions;
	}

	
	
}
