/**
 * 
 */
package com.etranzact.supportmanager.action;

import java.util.List;


import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.etranzact.drs.controller.ClaimController;
import com.etranzact.psm.controller.TPSMController;
import com.etranzact.cms.action.CardManagementAction;

/**
 * @author tony.ezeanya
 *
 */

@Name("appOptions")
@Scope(ScopeType.SESSION)
public class AppOptions 
{
	
	@In(create = true)
	ReportAction reportAction;
	
	@In(create = true)
	AdministratorAction adminAction;
	
	@In(create = true)
	ClaimController claimcontroller;
	
	@In(create = true)
	TPSMController tpsmcontroller;
	
	@In(create = true)
	MerchantReportAction merchantReportAction;
	
	@In(create = true)
	CardManagementAction cardManagementAction;

	public void resetValues()
	{
		reportAction.resetValues();
		adminAction.resetValues();
		claimcontroller.distroyAll();
		tpsmcontroller.distroy();
		merchantReportAction.resetValues();
		cardManagementAction.resetValues();
		
	}
	
}
