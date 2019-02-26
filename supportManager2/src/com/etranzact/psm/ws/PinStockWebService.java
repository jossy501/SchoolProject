package com.etranzact.psm.ws;

import com.etranzact.drs.utility.Utility;
import com.etranzact.psm.controller.TPSMController;
import com.etranzact.supportmanager.utility.Env;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

@WebService
public class PinStockWebService
{

  @Resource
  WebServiceContext wsContext;

  @WebMethod(operationName="releasePin")
  public Response releasePin(@WebParam(name="dealerID") String dealerID, @WebParam(name="pinValue") double pinValue, @WebParam(name="provider") String provider, @WebParam(name="quantity") int quantity, @WebParam(name="targetPhone") String targetPhone, @WebParam(name="requestSeessionKey") String requestSeessionKey)
  {
    if (validateClientIP()) {
      //String requestSeessionKey = new Utility().generateRandomNumber(32);
        String spinValue = (""+pinValue).substring(0,(""+pinValue).indexOf("."));
      Response res = new TPSMController().releasePin(dealerID, spinValue, provider, requestSeessionKey, quantity, targetPhone);
      res.setDealerId(dealerID);
      res.setPinDeno(pinValue);
      res.setProviderId(provider);
      res.setSessionKey(requestSeessionKey);
      return res;
    }

    Response res = new Response();
    res.setDealerId(dealerID);
    res.setMessage("Unauthorized Client Request");
    res.setPinAmount(0D);
    res.setPinCount(0);
    res.setPinDeno(pinValue);
    res.setPins(new PIN[0]);
    res.setProviderId(provider);
    res.setResposeCode("-1");
    res.setSessionKey("");
    return res;
  }

  @WebMethod(operationName="unlockPin")
  public Response unlockPin(@WebParam(name="dealerID") String dealerID, @WebParam(name="pinValue") double pinValue, @WebParam(name="provider") String provider, @WebParam(name="requestSeessionKey") String requestSeessionKey, @WebParam(name="targetPhone") String targetPhone, @WebParam(name="debited") boolean debited)
  {
      Response res = new Response();
    if (validateClientIP()) {
      res = new TPSMController().unlockPin(dealerID, pinValue, provider, requestSeessionKey, targetPhone, debited);
      res.setDealerId(dealerID);
      res.setPinDeno(pinValue);
      res.setProviderId(provider);
      res.setSessionKey(requestSeessionKey);
      return res;
    }
 else{
    res.setDealerId(dealerID);
    res.setMessage("Unauthorized Client Request");
    res.setPinAmount(0D);
    res.setPinCount(0);
    res.setPinDeno(pinValue);
    res.setPins(new PIN[0]);
    res.setProviderId(provider);
    res.setResposeCode("-1");
    res.setSessionKey(requestSeessionKey);
    return res;}
  }

  private String getClientAddress()
  {
    MessageContext msgx = this.wsContext.getMessageContext();
    HttpServletRequest exchange = (HttpServletRequest)msgx.get("javax.xml.ws.servlet.request");
    return exchange.getRemoteAddr();
  }

  private boolean validateClientIP() {
    String clientIP = getClientAddress();
    System.out.println(">>>>>>>>>> WS Request Client IP >>>>>>>>> " + clientIP);
    boolean found = false;
    if (clientIP != null) {
      String regIps = getRegisteredIPs();
      String[] regClients = regIps.split(",");
      for (int w = 0; w < regClients.length; ++w)
        if (clientIP.trim().equals(regClients[w].trim()))
          found = true;
    }
    else
    {
      found = false;
    }
    //found = true;
    return found;
  }

  private String getRegisteredIPs() {
    String ips = "";
    Connection con = null;
    try {
      con = Env.getConnection4DRSDB();
      String query = "SELECT IP FROM telcodb.dbo.T_PSM_WS_CLIENT";
      Statement stat = con.createStatement();
      ResultSet rs = stat.executeQuery(query);
      while (rs.next())
        ips = ips + "," + rs.getString("IP");
    }
    catch (Exception x)
    {
    }
    finally {
      try {
        con.close();
      }
      catch (Exception d) {
      }
    }
    System.out.println("Registered Client IP " + ips);
    return ips;
  }
}