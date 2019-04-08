
package com.etranzact.supportmanager.action;

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


import com.etranzact.drs.controller.ClaimController;
import com.etranzact.supportmanager.dto.MenuItem;
import com.etranzact.supportmanager.dto.MenuItemToRole;
import com.etranzact.supportmanager.dto.ProfileMenuOptions;
import com.etranzact.supportmanager.dto.User;
import com.etranzact.supportmanager.model.AdminModel;
import com.etranzact.supportmanager.model.AuthenticationModel;
import com.etranzact.supportmanager.utility.MailExample;
import com.etranzact.supportmanager.utility.Utils;
import com.etranzact.xmlparser.DomParser;
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
            com.etranzact.supportmanager.client.SecuredsessionService service = new com.etranzact.supportmanager.client.SecuredsessionService();
            com.etranzact.supportmanager.client.Securedsession port = service.getSecuredsessionPort();
            HttpServletRequest str = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
            //System.out.println("str.getSession().getId() " + str.getSession().getId());
            
            String appid = "Xportal123";
            sessionid  = str.getSession().getId();
            String pan = "Xportal123_"+username;

            Cryptographer crypt =  new Cryptographer();
            pan = crypt.doMd5Hash(pan);
            
            
            switch(Integer.parseInt(process))
            {
	            case 1:
	            	
	            	String checksum = crypt.doMd5Hash(appid + sessionid + pan);
	                String result = port.invokesession(appid, sessionid, pan, checksum);
	                //System.out.println("Result = "+result);
	                
	                DomParser dom = new DomParser();
	                String[] message = dom.parseString(result);
	                //System.out.println("message[0] " + message[0]);
	                if(message[0].equals("0"))//means successful
	                {
	                	String mychecksum = crypt.doMd5Hash(appid + sessionid + pan + message[3] + message[0]);
	                	//System.out.println("mychecksum1 " + mychecksum + " " + message[2]);
	                	 
	                	//comparing the 2checksums
	                	if(mychecksum.equals(message[2]))
	                	{
	                		getCurUser().setEsa_code(null);
	                		getCurUser().setEsa_code_email(null);
	                		getCurUser().setEsa_type(message[3]);
		                	ret = "/esa/esaValidation.xhtml";
	                	}
	                	else
	                	{
		                	facesMessages.add(Severity.WARN, "Invalid checksum");
	                	}
	                }
	                else
	                {
	                	if(message[0].equals("-3"))
	                	{
	                		facesMessages.add(Severity.WARN, "Application not found. Application setup is required");
	                	}
	                	else if(message[0].equals("1"))
	                	{
	                		facesMessages.add(Severity.WARN, "System could not send esa code to the destination address");
	                	}
	                	else if(message[0].equals("3"))
	                	{
	                		facesMessages.add(Severity.WARN, "Invalid esa code supplied");
	                	}
	                	else if(message[0].equals("2"))
	                	{
	                		facesMessages.add(Severity.WARN, "Wrong security answer supplied");
	                	}
	                	else if(message[0].equals("-2"))
	                	{
	                		facesMessages.add(Severity.WARN, "Invalid checksum");
	                	}
	                	else if(message[0].equals("0"))
	                	{
	                		facesMessages.add(Severity.WARN, "Successful");
	                	}
	                	else if(message[0].equals("-4"))
	                	{
	                		facesMessages.add(Severity.WARN, "No Esa record found. Esa enrollment is required");
	                	}
	                	else if(message[0].equals("5"))
	                	{
	                		facesMessages.add(Severity.WARN, "Sorry! ESA Code session has expired");
	                	}
	                	else if(message[0].equals("-1"))
	                	{
	                		facesMessages.add(Severity.WARN, "Unknown error");
	                	}
	                	else if(message[0].equals("-5"))
	                	{
	                		facesMessages.add(Severity.WARN, "Invalid session ID. session id should and must be 30");
	                	}                	
	                	
	                }
	            	
	            	break;
	            	
	            case 2:
	            	
	            	String esacode = getCurUser().getEsa_code();
	            	String esaquestion = "";
	            	String esaType = "";
	            	String esaAnswer = getCurUser().getEsa_answer();
	            	if(esaAnswer.trim().length()>0)
	            	{
	            		esaType = "2";
	            		esacode = getCurUser().getEsa_code_email();
	            	}
	            	else
	            	{
	            		esaType = "1";
	            	}

	            	
	            	checksum = crypt.doMd5Hash(appid + sessionid + pan + esacode + esaType + esaAnswer);
	            	
	            	System.out.println("appid = "+appid);
	            	System.out.println("sessionid = "+sessionid);
	            	System.out.println("checksum = "+checksum);
	            	System.out.println("esacode = "+esacode);
	            	System.out.println("Integer.parseInt(esaType) = "+Integer.parseInt(esaType));
	            	System.out.println("esaAnswer = "+esaAnswer);
	            	
	                result = port.validatesession(appid, sessionid, pan, checksum, esacode, Integer.parseInt(esaType), esaAnswer);
	                //System.out.println("Result 2 = "+result);
	                
	                dom = new DomParser();
	                message = dom.parseString(result);
	                //System.out.println("message 2[0] " + message[0]);
	                if(message[0].equals("0"))//means successful
	                {
	                	String mychecksum = crypt.doMd5Hash(appid + sessionid + pan + esaquestion + message[0]);
	                	
	                	//System.out.println("mychecksum2 " + mychecksum + " " + message[2]);
	                	
	                	//comparing the 2checksums
	                	if(mychecksum.equals(message[2]))
	                	{
	                		User u = getCurUser();
		                	u.setLoggedIn(true);
		                	ret = "/welcome.xhtml";
	                	}
	                	else
	                	{
		                	facesMessages.add(Severity.WARN, "Invalid checksum");
	                	}
	                }
	                else
	                {                	
	                	if(message[0].equals("-3"))
	                	{
	                		facesMessages.add(Severity.WARN, "Application not found. Application setup is required");
	                	}
	                	else if(message[0].equals("1"))
	                	{
	                		facesMessages.add(Severity.WARN, "System could not send esa code to the destination address");
	                	}
	                	else if(message[0].equals("3"))
	                	{
	                		facesMessages.add(Severity.WARN, "Invalid esa code supplied");
	                	}
	                	else if(message[0].equals("2"))
	                	{
	                		facesMessages.add(Severity.WARN, "Wrong security answer supplied");
	                	}
	                	else if(message[0].equals("-2"))
	                	{
	                		facesMessages.add(Severity.WARN, "Invalid checksum");
	                	}
	                	else if(message[0].equals("0"))
	                	{
	                		facesMessages.add(Severity.WARN, "Successful");
	                	}
	                	else if(message[0].equals("-4"))
	                	{
	                		facesMessages.add(Severity.WARN, "No Esa record found. Esa enrollment is required");
	                	}
	                	else if(message[0].equals("5"))
	                	{
	                		facesMessages.add(Severity.WARN, "Sorry! ESA Code session has expired");
	                	}
	                	else if(message[0].equals("-1"))
	                	{
	                		facesMessages.add(Severity.WARN, "Unknown error");
	                	}
	                	else if(message[0].equals("-5"))
	                	{
	                		facesMessages.add(Severity.WARN, "Invalid session ID. session id should and must be 30");
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
	}
	*/
	
	
	
	/**
	 * Authenticates the user based on the supplied username and password.
	 * */
	public String signIn()
	{
		//FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		
		AuthenticationModel authModel = new AuthenticationModel();
		
		String ret = "index";
		
		context = FacesContext.getCurrentInstance();
		
		try
		{
			//
			HttpServletRequest str = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String ip_address = str.getRemoteAddr();
			//System.out.println("ip_address " + ip_address);
			ip_address = InetAddress.getLocalHost().getHostAddress();
			//System.out.println("ip_address " + ip_address);
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
					u.setUsername(user.getUsername());
					u.setType_id(user.getType_id());
					u.setType_nm(user.getType_nm());
					u.setUser_code(user.getUser_code());
					u.setFirst_logon(user.getFirst_logon());
					u.setAccount_id(user.getService_id());
					u.setService_id(user.getService_id());

					System.out.println("u.getT " + u.getType_nm());
					
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
		
		
		
        /*AuthenticationAction ua =((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator"));
        ClaimController cC =((ClaimController) context.getExternalContext().getSessionMap().get("claimcontroller"));
        cC.distroyAll();
        ua.curUser=null;
        ua.loginUser=null;
        ua.context=null;
        ua=null;
        context=null;*/
		
		
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
