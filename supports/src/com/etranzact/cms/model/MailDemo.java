package com.etranzact.cms.model;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailDemo 
{
	
	final String username = "jossy501@gmail.com";
	final String password = "sled164hook110";
	

	
	public MailDemo()
	{
		
	}
	public void sendMail(String firstname,String lastname,String email,String transDate,String transNo,String transDesc,String transCode,String channel,String MerchantCode,String cardnum,double debitAmit,double creditAmt,double totalAmit)	
	{
	
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		
		
		Session session = Session.getInstance(props,
				  new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				  });
		 
				try {
		 
					Message message = new MimeMessage(session);
					message.setFrom(new InternetAddress("jossy501@gmail.com"));
					message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse("joshua.aruno@etranzact.net"));
					message.setSubject("Transaction Report");
					
					message.setText("Here are ur Transaction Details \n\n" +
							"Firstname : "+firstname+"\n"
							+"Lastname  : "+lastname+"\n"
							+"Email Address :"+email+"\n"
							+"TransDate : "+transNo+"\n"
							+"TransNo : "+transDesc+"\n"
							+"transCode : "+channel+"\n"
							+"Channel : "+channel+"\n"
							+"MerchantCode : "+MerchantCode+"\n"
							+"CardNUM  : "+cardnum+"\n"
							+"DebitAmount : "+debitAmit+"\n"
							+"CreditAmount : "+creditAmt+"\n"
							+"Total Amount : "+totalAmit);
					
					Transport.send(message);
				
		 
					System.out.println("Done !!!");
		 
				}catch (MessagingException e) {
					throw new RuntimeException(e);
				}
			
		
		
	}
	 
		
	 
			
	
	
	
	
}
