/**
 * 
 */
package com.etranzact.payoutlet.action;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;

import com.etranzact.payoutlet.dto.CAgency;
import com.etranzact.payoutlet.dto.CIssuerMerchant;
import com.etranzact.payoutlet.dto.CMerchant;
import com.etranzact.payoutlet.dto.CTransType;
import com.etranzact.payoutlet.model.PayoutletSetupModel;
import com.etranzact.supportmanager.dto.User;

/**
 * @author tony.ezeanya
 *
 */
@Restrict("#{authenticator.curUser.loggedIn}")
@Scope(ScopeType.SESSION)
@Name("payoutletSetUpAction")
public class PayoutletSetUpAction 
{
	private CAgency cAgency;
	private List<CAgency> cAgencyList = new ArrayList<CAgency>();
	
	private CIssuerMerchant cIssuerMercant;
	private List<CIssuerMerchant> cIssuerMercantlist = new ArrayList<CIssuerMerchant>();
	
	private CTransType cTransType;
	private List<CTransType> cTransTypeList = new ArrayList<CTransType>();
	
	private CMerchant cMerchant;
	private List<CMerchant> cMerchantList = new ArrayList<CMerchant>();
	
	
	
	@In
    FacesMessages facesMessages;

	
	//this is used to create the agency
	public void createAgency()
	{
		try
		{
				
				PayoutletSetupModel pmodel = new PayoutletSetupModel();
				String message = pmodel.createAgency(cAgency);
				System.out.println("createAgency() Response Message  --  > "+message);
				facesMessages.add(Severity.INFO, message);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	// this is used to create the Merchant Issuer 
	public void createIssuerMerchant()
	{
		try
		{
				PayoutletSetupModel pmodel = new PayoutletSetupModel();
				System.out.println("merchant code  -  "+cIssuerMercant.getMerchantCode());
				String message = pmodel.createIssuerMerchant(cIssuerMercant);
				System.out.println("createIssuerMerchant()  Response Message  --  > "+message);	
				facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	//this is used to create Trans Type
	public void createTransType()
	{
		
		try
		{
			PayoutletSetupModel pmodel = new PayoutletSetupModel();
			System.out.println("merchant code  -  "+cTransType.getMerchantCode());
			String message = pmodel.createTransType(cTransType);
			System.out.println("createTransType()  Response Message  --  > "+message);	
			facesMessages.add(Severity.INFO, message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			
		}
	}
	
	// this is used to create Merchant
	public void createMerchant()
	{
		
		try
		{
			System.out.println("createMerchant()  Response Message  --  > Testing ");	
			
		}catch(Exception ex){ex.printStackTrace();}
	}
	
	
	public CAgency getcAgency() 
	{
		if(cAgency == null)
		{
			cAgency = new CAgency();
		}
		
		return cAgency;
	}


	public void setcAgency(CAgency cAgency) {
		this.cAgency = cAgency;
	}

	public List<CAgency> getcAgencyList() {
		return cAgencyList;
	}

	public void setcAgencyList(List<CAgency> cAgencyList) {
		this.cAgencyList = cAgencyList;
	}
	
	
	
	public CIssuerMerchant getcIssuerMercant() {
		
		if(cIssuerMercant == null)
		{
			cIssuerMercant = new CIssuerMerchant();
		}
		
		return cIssuerMercant;
	}



	public void setcIssuerMercant(CIssuerMerchant cIssuerMercant) {
		this.cIssuerMercant = cIssuerMercant;
	}



	public List<CIssuerMerchant> getcIssuerMercantlist() {
		return cIssuerMercantlist;
	}



	public void setcIssuerMercantlist(List<CIssuerMerchant> cIssuerMercantlist) {
		this.cIssuerMercantlist = cIssuerMercantlist;
	}

	public CTransType getcTransType() {
		
		if(cTransType ==  null)
		{
			cTransType = new CTransType();
		}
		
		return cTransType;
	}

	public void setcTransType(CTransType cTransType) {
		this.cTransType = cTransType;
	}

	public List<CTransType> getcTransTypeList() {
		return cTransTypeList;
	}

	public void setcTransTypeList(List<CTransType> cTransTypeList) {
		this.cTransTypeList = cTransTypeList;
	}

	public CMerchant getcMerchant() {
		
		if(cMerchant ==  null)
		{
			cMerchant = new CMerchant();
		}
		return cMerchant;
	}

	public void setcMerchant(CMerchant cMerchant) {
		this.cMerchant = cMerchant;
	}

	public List<CMerchant> getcMerchantList() {
		return cMerchantList;
	}

	public void setcMerchantList(List<CMerchant> cMerchantList) {
		this.cMerchantList = cMerchantList;
	}


	
}
