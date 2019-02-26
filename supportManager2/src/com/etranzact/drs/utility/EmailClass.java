/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etranzact.drs.utility;

/**
 *
 * @author Akachukwu
 */
import java.security.Security;
import java.util.Properties;
import javax.mail.Address;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailClass extends Thread {

    private static final String SMTP_HOST_NAME = "smtp.etranzact.net";//
    private static final String SMTP_PORT = "25";
    //private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    String recipients[];
    String subject;
    String message;
    String from;

    public static void main(String args[]) throws Exception {

        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        String[] email = new String[1];
        email[0] = "akachintuko@gmail.com";
        new EmailClass().sendSSLMessage(email, "hi", "TEXT", "akachintuko@gmail.com");
        System.out.println("Sucessfully Sent mail to All Users");
    }
EmailClass()
    {
    
}
    public EmailClass(String recipients[], String subject, String message, String from)
    {
      this.recipients= recipients;
      this.subject=subject;
      this.message=message;
      //from ="akachintuko@gmail.com";
      this.from=from;
    }
public void run()
    {
    try{
    sendSSLMessage(recipients, subject, message, from);
        }catch(Exception d)
    {
            d.printStackTrace();
        }
}

    private void sendSSLMessage(String recipients[], String subject,
            String message, String from) throws MessagingException {
        boolean debug = true;



        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.port", SMTP_PORT);
        //props.put("mail.smtp.socketFactory.port", SMTP_PORT);
        //props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
        //props.put("mail.smtp.socketFactory.fallback", "false");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("alert@etranzact.net", "technology123");
            }
        });

        session.setDebug(debug);
        if(from==null)
        {
            from="drs_admin@etranzact.net";
        }else if(from.isEmpty())
        {
          from="drs_admin@etranzact.net";
        }
        Message msg = new MimeMessage(session);
        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);

        
        for (int i = 0; i < recipients.length; i++) {

            try{
            Address addressTo = new InternetAddress(recipients[i]);
            //addressTo[i] = new InternetAddress(recipients[i]);
        msg.setRecipient(Message.RecipientType.TO, addressTo);
        msg.setSubject(subject);
        msg.setContent(message, "text/plain");
        Transport.send(msg);
            }catch(Exception dd){}
        }
// Setting the Subject and Content Type
        
    }
}
