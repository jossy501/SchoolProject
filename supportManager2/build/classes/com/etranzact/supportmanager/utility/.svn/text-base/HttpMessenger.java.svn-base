package com.etranzact.supportmanager.utility;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
import java.io.*;
import java.net.*;

public class HttpMessenger {
    private String remoteServer = "";
    private String remoteQuery = "";
    private String webApp = "";
    private HttpURLConnection connection = null;
    private URL urlObject = null;
    private BufferedReader in = null;
    private String url = "";

    public void setServer(String server)
    {
        this.remoteServer = server;
    }
    public String getServer()
    {
        return this.remoteServer;
    }
    public void setWebApp(String wapp)
    {
        this.webApp = wapp;
    }
    public String getWebApp()
    {
        return this.webApp;
    }

    public String getQueryString()
    {
        return this.remoteQuery;
    }
    public void setQueryString(String query)
    {
        this.remoteQuery = query;
    }
    public String submit() throws Exception 
    {
       String resp="";
       try{
               url = getServer()+"/"+getWebApp()+"/"+getQueryString();
               //System.out.println("HttpMessenger::submit() - "+url);
               urlObject = new URL(url);
               // Actually make a connection
               connection = (HttpURLConnection)urlObject.openConnection();
               connection.setDoInput(true);

               connection.connect();
               int responseCode = connection.getResponseCode();
               System.out.println("HttpMessenger::proceed() - responseCode: "+responseCode);
               if(responseCode == 200) {
                   System.out.println("HttpMessenger::proceed() - Connected to Server: "+getServer());
                   in = new BufferedReader(
                   new InputStreamReader(connection.getInputStream()));
                   StringBuffer buildStr = new StringBuffer();
                   while((resp = in.readLine())!= null){
                       buildStr.append(resp);
                   }
                   System.out.println("HttpMessenger::proceed() - response: "+buildStr.toString());
                   resp = buildStr.toString();
                   //if (resp.toLowerCase().indexOf("success") > -1) submitted = 0;
               }else{
                   System.out.println("HttpMessenger::proceed() - Connection Failed: " +getServer());
                   resp="";
               }
       }catch(Exception eee){
         System.out.println("HttpMessenger::proceed()::"+eee);
         throw eee;
       }
       return resp;

    }
}
