/**
 * 
 */
package com.etz.loanCalculator.action;

import java.util.List;


import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

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
	
	public void resetValues()
	{
		adminAction.resetValues();
		reportAction.resetValues();
	}
	
}
