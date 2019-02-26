package com.etranzact.supportmanager.utility;


import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.Renderer;


@Name("mailExample")
@Scope(ScopeType.SESSION)
public class MailExample
{
   
   @In(create = true)
   private Renderer renderer;

   public MailExample(){}
   
   @Observer("testmailer")
   public void sendPlain()throws Exception
   {
	    renderer.render("/support/plain.xhtml");
   		System.out.println("email sent!!!");
   }
   
   @Observer("transactionmailer")
   public void sendTransactionReportByEmail()throws Exception
   {
	    renderer.render("/cms/transactionReportByEMail.xhtml");
   		System.out.println("email sent!!!");
   }
 
   
   @Observer("updatemailer")
   public void resetPlain()throws Exception
   {
	    renderer.render("/support/updateUserMail.xhtml");
  		System.out.println("email sent for update !!!");
	   
   }
   
   @Observer("cardnumbermailer")
   public void cardNumberPlain()throws Exception
   {
	    renderer.render("/support/cardNumberMail.xhtml");
  		System.out.println("card email sent !!!");
	   
   }
   
   
   @Observer("rechargepinmailer")
   public void rechargePinPlain()throws Exception
   {
	    renderer.render("/support/rechargePinMail.xhtml");
  		System.out.println("pin email sent !!!");
	   
   }
   
   
   @Observer("paymentadvicemailer")
   public void paymentAdvicePlain()throws Exception
   {
	    renderer.render("/support/paymentAdviceMail.xhtml");
  		System.out.println("payment advice email sent !!!");
	   
   }
   
}
