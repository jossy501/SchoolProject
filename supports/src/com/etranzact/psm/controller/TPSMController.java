 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etranzact.psm.controller;

import com.etranzact.drs.utility.DateUtility;
import com.etranzact.psm.dto.StockSearchBean;
import com.etranzact.psm.dto.TPsmStock;
import com.etranzact.psm.ws.PIN;
import com.etranzact.psm.ws.Response;
import com.etranzact.supportmanager.action.AuthenticationAction;
import com.etranzact.supportmanager.dto.User;
import com.etranzact.supportmanager.utility.Env;
//import com.etz.http.etc.HttpHost;
//import com.etz.http.etc.XProcessor;
import com.etz.http.etc.HttpHost;
import com.etz.http.etc.XProcessor;
import com.etz.http.topup.Recharge;
import com.etz.http.topup.Voucher;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.faces.context.FacesContext;
import org.hibernate.validator.Valid;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;

/**
 *
 * @author akachukwu.ntukokwu
 */
@Scope(ScopeType.SESSION)
@Name("tpsmcontroller")
public class TPSMController {

    @Valid
    private TPsmStock stock;
    private StockSearchBean searchBean;
    @In
    FacesMessages facesMessages;
    List<TPsmStock> lists;
    List<ErrorBean> errorList;
    boolean rendered;
    static String serverIP = "172.16.10.101";

    static {

        java.io.File fil = new java.io.File("config_PSM.txt");


        try {
            BufferedReader bf = new BufferedReader(new FileReader(fil));
            String msg = "";

            while ((msg = bf.readLine()) != null) {
                serverIP = msg;

            }
            if (msg == null || msg.trim().equals("")) {
                serverIP = "172.16.10.101";
            }
        } catch (Exception d) {
            d.printStackTrace();
        }

    }

    public List<ErrorBean> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<ErrorBean> errorList) {
        this.errorList = errorList;
    }

    public StockSearchBean getSearchBean() {
        if (searchBean == null) {
            searchBean = new StockSearchBean();
        }
        return searchBean;
    }

    public void setSearchBean(StockSearchBean searchBean) {
        this.searchBean = searchBean;
    }

    public TPsmStock getStock() {
        if (stock == null) {
            stock = new TPsmStock();
        }
        return stock;
    }

    public List<TPsmStock> getLists() {
        return lists;
    }

    public void setLists(List<TPsmStock> lists) {
        this.lists = lists;
    }

    public boolean getRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public void setStock(TPsmStock stock) {
        this.stock = stock;
    }

    public String createStock(List<String> queries, int valid, int invalid) {
        Connection con = null;
        String message = "";
        FacesContext context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();

        try {
            // String query = "";
            con = Env.getConnection4PSMRPDB();
            con.setAutoCommit(true);
            //System.out.println("XXXXXXXXXXXXXXXXXXXXXX "+query);
            //int output=0;            
            for (int t = 0; t < queries.size(); t++) {
                String qq = queries.get(t);
                if (qq != null) {
                    Statement st = con.createStatement();
                    String qr = qq;
                    qr = qr.replaceAll("XXXXXXXXXXCCCCC", valid + "");
                    qr = qr.replaceAll("CCCCCVVVVVVVVVV", invalid + "");
                    st.execute(qr);
                }
            }
            message = "Stock successfully created";
            // int[] res = st.executeBatch();
            // con.commit();
            //if (res != null && res.length > 0) {
            //message = "Stock successfully created";
            //}

        } catch (Exception x) {
            x.printStackTrace();
            message = "Stock creation failed >> database error ";
        } finally {
            try {
                con.close();
            } catch (Exception dd) {
            }

        }

        return message;
    }

    public String createStock(Connection con, String queries) {
        String message = "";
        Statement st=null;
        try {
            st = con.createStatement();
            st.execute(queries);

        } catch (Exception x) {
            x.printStackTrace();
        }finally
        {
            try{
            //rs.close();
            st.close();
            }catch(Exception fg){}
        }
        return message;
    }

    public String createStockError(List<ErrorBean> error,String batchid,String systemBatchid) {
        String message = "";
        Connection  con=null;
        try {
            con = Env.getConnection4PSMRPDB();
            con.setAutoCommit(true);
            for (int t = 0; t < error.size(); t++) {
                String queries = "INSERT INTO rechargedb.dbo.R_PINS_TEMP_Error (batch_id,system_batchid,line, error_desc) "
                + "values ('"+batchid+"','"+systemBatchid+"','"+error.get(t).getLine()+"','"+error.get(t).getErrorDesc()+"')";
                Statement st = con.createStatement();
                st.execute(queries);

            }
            this.updateStockTemp(con,batchid, systemBatchid, error.size());

        } catch (Exception x) {
            x.printStackTrace();
        }finally
        {
            try{
                con.close();
            }catch(Exception sd){}
        }
        return message;
    }

    private String getRowcount(Connection con,String batchid,String systemBatchid) {
        String message = "";
        try {
           //Connection  con = Env.getConnection4DRSDB();
            //con.setAutoCommit(true);
                String queries = "select count(*) from rechargedb.dbo.R_PINS_TEMP  where SystemAsignedBatchID ='"+systemBatchid+"' and upload_batchId = '"+batchid+"' ";
                Statement st = con.createStatement();
               ResultSet r = st.executeQuery(queries);
               if(r.next())
               {
                return r.getString(1);
               }

        } catch (Exception x) {
            x.printStackTrace();
        }
        return message;
    }

    public String updateStockTemp(Connection  con,String batchid,String systemBatchid,int count) {
        String message = "";
        try {
           //Connection  con = Env.getConnection4DRSDB();//where SystemAsignedBatchID ='"+systemBatchid+"' and upload_batchId = '"+batchid+"' ";
            //con.setAutoCommit(true);
                String queries = "update rechargedb.dbo.R_PINS_TEMP set invalidCount='"+count+"', validCount='"+this.getRowcount(con, batchid, systemBatchid)+"' where SystemAsignedBatchID ='"+systemBatchid+"' and upload_batchId = '"+batchid+"' ";
                Statement st = con.createStatement();
                st.execute(queries);

        } catch (Exception x) {
            x.printStackTrace();
        }
        return message;
    }

    public String deleteStockError(String batchid,String systemBatchid) {
        String message = "";
        try {
           Connection  con = Env.getConnection4PSMRPDB();
            con.setAutoCommit(true);
                String queries = "delete from rechargedb.dbo.R_PINS_TEMP_Error where system_batchid ='"+systemBatchid+"' and batch_id = '"+batchid+"' ";
                Statement st = con.createStatement();
                st.execute(queries);

        } catch (Exception x) {
            x.printStackTrace();
        }
        return message;
    }

    private String deleteStockTemp(String batchid,String systemBatchid) {
        String message = "";
        try {
           Connection  con = Env.getConnection4DRSDB();
            con.setAutoCommit(true);
                String queries = "delete from rechargedb.dbo.R_PINS_TEMP where SystemAsignedBatchID ='"+systemBatchid+"' and upload_batchId = '"+batchid+"' ";
                Statement st = con.createStatement();
                st.execute(queries);
        } catch (Exception x) {
            x.printStackTrace();
        }
        return message;
    }


    private ArrayList j(String ip, String ref, String network, String amount, String phoneNo, String dealer, int qountity) {
        HttpHost host = new HttpHost();
        ArrayList list = null;
        host.setServerAddress(ip);
        host.setSecureKey("123456");
        Recharge recharge = new Recharge();
        recharge.setReference(ref);
        recharge.setNetwork(network);
        recharge.setUnit(amount);
        recharge.setQuantity(qountity);
        recharge.setTargetPhone(phoneNo);
        recharge.setStockOwner(dealer);
        recharge.setCommand(Recharge.LOCK);
        XProcessor processor = new XProcessor();
        try {
            Recharge response = processor.process(host, recharge);
            System.out.println("Response::" + response.getResponse());
            if (response.getResponse() == 0) {
                list = (ArrayList) response.getVouchers();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return list;
    }

    public Response unlockPin(String dealerID, double pinValue, String provider, String sessionKey, String TARGET_PHONE, boolean debited) {
//        Connection con = null;
        Response msg = new Response();
        msg.setResposeCode("-1");
//        Response msg = new Response();
//        try {
//
//            con = Env.getConnection4psmDB();
//            int output = -1;
//            Statement stat = null;
//            stat = con.createStatement();
//            String query = "UPDATE rechargedb.dbo.R_PINS SET UNIQUE_TRANSID = '', TARGET_PHONE='' "
//                    + "where MERCHANT_CODE = '" + dealerID + "' and convert(numeric(10,2), PIN_DENO) = " + pinValue + ""
//                    + " and PROVIDER_ID ='" + provider + "' and UNIQUE_TRANSID='" + sessionKey + "' and TARGET_PHONE = '" + TARGET_PHONE + "'";
//
//            if (!debited) {
//                output = stat.executeUpdate(query);
//            } else {
//                String query2 = "insert  rechargedb.dbo.R_PINS_BOUGHT (PROVIDER_ID, USERNAME, DISCOUNT, PIN, PIN_VALUE, PIN_STATUS, "
//                        + "PIN_USER, PIN_ISSUED, BATCHID, VALID_DAY, SERIAL, PIN_EXPIRATION, ORDER_NO, PIN_DENO, SUBATCHID, trans_soc, "
//                        + "MERCHANT_CODE, TARGET_PHONE, CLOSED, UNIQUE_TRANSID,first_approval,second_approval,uploader,uploader_IP,locked_Time,upload_batchId,"
//                        + "SystemAsignedBatchID,uploadFile,upload_date) "
//                        + ""
//                        + "select PROVIDER_ID, USERNAME, DISCOUNT, PIN, PIN_VALUE, "
//                        + "PIN_STATUS, PIN_USER, PIN_ISSUED, BATCHID, VALID_DAY, SERIAL, PIN_EXPIRATION, ORDER_NO, PIN_DENO, SUBATCHID, "
//                        + "trans_soc, MERCHANT_CODE, TARGET_PHONE, CLOSED, UNIQUE_TRANSID,"
//                        + "first_approval,second_approval,uploader,uploader_IP,locked_Time,upload_batchId,SystemAsignedBatchID,uploadFile,upload_date"
//                        + " from rechargedb.dbo.R_PINS "
//                        + "where MERCHANT_CODE = '" + dealerID + "' and convert(numeric(10,2), PIN_DENO) = " + pinValue + ""
//                        + " and PROVIDER_ID ='" + provider + "' and UNIQUE_TRANSID='" + sessionKey + "' and TARGET_PHONE = '" + TARGET_PHONE + "'";
//                output = stat.executeUpdate(query2);
//
//                String query2_ = "SELECT pin, PIN_DENO,PIN_VALUE,SERIAL FROM rechargedb.dbo.R_PINS where UNIQUE_TRANSID  = '" + sessionKey + "' and  TARGET_PHONE ='" + TARGET_PHONE + "'";
//                ResultSet rs = stat.executeQuery(query2_);
//
//                String pinNum = null;
//                String pValue = null;
//                List<PIN> pinArray = new ArrayList<PIN>();
//                msg.setPinAmount(0);
//                while (rs.next()) {
//                    pinNum = rs.getString(1);
//                    pValue = rs.getString(2);
//                    PIN pin = new PIN();
//                    pin.setPinNumber(new E_SECURE().f_decode(pinNum, rs.getString(4)));
//                    pin.setPinDeno(pValue);
//                    pin.setPinValue(rs.getString(3));
//                    pin.setPinSerial(rs.getString(4));
//                    msg.setPinAmount(msg.getPinAmount() + rs.getDouble(3));
//                    pinArray.add(pin);
//
//                }
//                PIN[] pins = new PIN[pinArray.size()];
//                for (int t = 0; t < pinArray.size(); t++) {
//                    pins[t] = pinArray.get(t);
//                }
//                msg.setPins(pins);
//                msg.setPinCount(pinArray.size());
//
//                stat.executeUpdate("delete from rechargedb.dbo.R_PINS where MERCHANT_CODE = '" + dealerID + "' and convert(numeric(10,2), PIN_DENO) = " + pinValue + ""
//                        + " and PROVIDER_ID ='" + provider + "' and UNIQUE_TRANSID='" + sessionKey + "' and TARGET_PHONE = '" + TARGET_PHONE + "'");
//
//            }
//            if (output > 0) {
//                con.commit();
//                msg.setMessage("PIN unlock was successful");
//                msg.setResposeCode("0");
//                msg.setPinCount(output);
//            } else {
//                msg.setMessage("PIN unlock was not successful");
//                msg.setResposeCode("2");
//                msg.setPinCount(output);
//
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            msg.setResposeCode("03");
//            msg.setMessage("System error >>>>>  " + ex.getMessage());
//
//        } finally {
//            try {
//                con.close();
//            } catch (Exception dd) {
//            }
//
//        }
        try {
            HttpHost host = new HttpHost();
            host.setServerAddress(serverIP);
            host.setSecureKey("123456");
            Recharge recharge = new Recharge();
            recharge.setReference(sessionKey+"|WS");
            recharge.setNetwork(provider);
            recharge.setUnit(pinValue + "");
            recharge.setTargetPhone(TARGET_PHONE);
            recharge.setStockOwner(dealerID);
            if (debited) {
                recharge.setCommand(Recharge.SOLD);
            } else {
                recharge.setCommand(Recharge.UNLOCK);
            }
            XProcessor processor = new XProcessor();
            Recharge response = processor.process(host, recharge);
            System.out.println("Response:: " + TARGET_PHONE + " " + response.getResponse());

            PIN[] pins = null;
            ArrayList list = new ArrayList();
            System.out.println(response.getResponse());
            if (response.getResponse() == 0) {
                list = (ArrayList) response.getVouchers();
                pins = new PIN[list.size()];
                for (int t = 0; t < list.size(); t++) {
                    Voucher v = (Voucher) list.get(t);
                    PIN pin = new PIN();
                    pin.setPinDeno(v.getPinvalue() + "");
                    pin.setPinNumber(v.getPin());
                    pin.setPinSerial(v.getSerial());
                    pin.setPinValue(v.getPinvalue() + "");
                    pins[t] = pin;
                }
            }



            msg.setPins(null);
            msg.setPins(pins);
            msg.setPinCount(list.size());
            if (list.size() < 1) {
                msg.setMessage("PIN request successful Available Pin Not equal to the quantity requested");
                msg.setResposeCode("-1");
            } else {
                msg.setMessage("PIN request successful");
                msg.setResposeCode("0");
            }




        } catch (Exception d) {
            d.printStackTrace();
        }

        return msg;
    }

    public void approve() {
        Connection con = null;
        String message = "";
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();

            con = Env.getConnection4PSMRPDB();
            int output = -1;
            String pin = "''";
            String provider = "''";
            String batchid = "''";
            String serial = "''";
            String merchantCode = "''";
            
            Statement stat = null;
            stat = con.createStatement();
            String query = "UPDATE rechargedb.dbo.R_PINS_TEMP SET first_approval = '" + user.getUser_id() + "' where upload_batchId= '"+this.searchBean.getBatchNumber()+"' and issuer = '" + user.getUser_code() + "' ";

            if (this.searchBean.getLevel() != null && this.searchBean.getLevel().equals("2")) {
                query = "UPDATE rechargedb.dbo.R_PINS_TEMP SET second_approval = '" + user.getUser_id() + "' where upload_batchId= '"+this.searchBean.getBatchNumber()+"' and issuer = '" + user.getUser_code() + "' ";

            }
            System.out.println(query);
            output = stat.executeUpdate(query);

            if (output > 0) {
                con.commit();
                if (this.searchBean.getLevel() != null && this.searchBean.getLevel().equals("2"))
                {
                movedApprovedStocks(this.searchBean.getBatchNumber());
                }
                message = "Stock successfully approved";
            } else {
                con.rollback();
                message = "Stock approved failed";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            message = "Stock approved failed >> database error " + ex.getMessage();
        } finally {
            try {
                con.close();
            } catch (Exception dd) {
            }

            facesMessages.add(Severity.INFO, message);
            this.stock = null;
            this.lists = null;
            this.searchBean = null;
            this.distroy();
        }
    }

    private  void movedApprovedStocks(String batchID)
    {
       Connection con = null;
        String message = "";
        try {
             con = Env.getConnection4PSMRPDB();
            int output = -1;
            Statement stat = null;
            stat = con.createStatement();
            String query = "insert into rechargedb.dbo.R_PINS "
                    + "(PROVIDER_ID,USERNAME,DISCOUNT,PIN,PIN_VALUE,PIN_STATUS,	PIN_USER,PIN_ISSUED,BATCHID,VALID_DAY,SERIAL,"
                    + "PIN_EXPIRATION,	ORDER_NO,PIN_DENO,	SUBATCHID,	trans_soc,	MERCHANT_CODE,	TARGET_PHONE,"
                    + "CLOSED,	MODIFIED,UNIQUE_TRANSID,first_approval,	second_approval,uploader,uploader_IP,locked_Time,upload_batchId,"
                    + "SystemAsignedBatchID,	uploadFile,upload_date,invalidCount,validCount,issuer)  "
                    + "select PROVIDER_ID,USERNAME,DISCOUNT,PIN,PIN_VALUE,PIN_STATUS,	PIN_USER,PIN_ISSUED,BATCHID,VALID_DAY,SERIAL,	"
                    + "PIN_EXPIRATION,	ORDER_NO,PIN_DENO,SUBATCHID,trans_soc,MERCHANT_CODE,TARGET_PHONE,	CLOSED,	MODIFIED,"
                    + "	UNIQUE_TRANSID,	first_approval,	second_approval,uploader,uploader_IP,locked_Time,upload_batchId,SystemAsignedBatchID,	"
                    + "uploadFile,upload_date,invalidCount,validCount,issuer from rechargedb.dbo.R_PINS_TEMP where upload_batchId='"+batchID+"'";

          String query2=  "delete from rechargedb.dbo.R_PINS_TEMP where upload_batchId = '"+batchID+"'";
          String query3=  "delete from rechargedb.dbo.R_PINS_TEMP_ERROR where batch_Id = '"+batchID+"'";

            System.out.println(query);
            output = stat.executeUpdate(query);
            stat.executeUpdate(query2);
            stat.executeUpdate(query3);
            if (output > 0) {
                con.commit();               
            } else {
                con.rollback();               
            }
        } catch (Exception ex) {
            ex.printStackTrace();            
        } finally {
            try {
                con.close();
            } catch (Exception dd) {
            }
        } 
    }


    public void reject() {
        Connection con = null;
        String message = "";
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();

            con = Env.getConnection4PSMRPDB();
            int output = -1;
            String pin = "''";
            String provider = "''";
            String batchid = "''";
            String serial = "''";
            String merchantCode = "''";
            
            Statement stat = null;
            stat = con.createStatement();
//            String query = "UPDATE rechargedb.dbo.R_PINS SET first_approval = '"+user.getUser_id()+"' where PIN in ("+pin+") and PROVIDER_ID in ("+provider+") "
//                    + "and upload_BATCHID in ("+batchid+") and SERIAL in ("+serial+") and MERCHANT_CODE in ("+merchantCode+") ";
//
//            if(this.searchBean.getLevel()!=null && this.searchBean.getLevel().equals("2"))
//            {
//            query = "UPDATE rechargedb.dbo.R_PINS SET second_approval = '"+user.getUser_id()+"' where PIN in ("+pin+") and PROVIDER_ID in ("+provider+") "
//                    + "and upload_BATCHID in ("+batchid+") and SERIAL in ("+serial+") and MERCHANT_CODE in ("+merchantCode+") ";
//
//            }
            String query = "delete from rechargedb.dbo.R_PINS_TEMP where upload_batchId ='"+this.searchBean.getBatchNumber()+"' ";
            String query2 = "delete from rechargedb.dbo.R_PINS_TEMP_ERROR where batch_id ='"+this.searchBean.getBatchNumber()+"' ";
            System.out.println(query);
            output = stat.executeUpdate(query);
            stat.executeUpdate(query2);
            if (output > 0) {
                con.commit();
                message = "Stock successfully rejected";
            } else {
                con.rollback();
                message = "Stock rejection failed";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            message = "Stock rejection failed >> database error ";
        } finally {
            try {
                con.close();
            } catch (Exception dd) {
            }

            facesMessages.add(Severity.INFO, message);
            this.stock = null;
            this.searchBean = null;
            this.lists = null;

            this.distroy();
        }
    }

    public Response releasePin(String dealerID, String pindeno, String provider, String sessionKey, int quantity, String TARGET_PHONE) {
        //Connection con = null;
        Response msg = new Response();
        msg.setResposeCode("-1");
        try {

//            con = Env.getConnection4psmDB();
//            int output = -1;
//            Statement stat = null;
//            stat = con.createStatement();
//            String query = "set rowcount " + quantity + " UPDATE rechargedb.dbo.R_PINS SET UNIQUE_TRANSID  ='" + sessionKey + "', locked_Time= getDate(), TARGET_PHONE ='" + TARGET_PHONE + "' "
//                    + "where  MERCHANT_CODE  = '" + dealerID + "' and first_approval != '' and second_approval != ''  and convert(numeric(10,2), PIN_DENO)  = " + pindeno + " and "
//                    + "( UNIQUE_TRANSID = '' or UNIQUE_TRANSID = null) and (TARGET_PHONE='' or TARGET_PHONE=null) "
//                    + "  and PROVIDER_ID ='" + provider + "' set rowcount " + 0 + "";
//            output = stat.executeUpdate(query);
            //if (output > 0
            //{
//                con.commit();
//                String query2 = "SELECT pin, PIN_DENO,PIN_VALUE FROM rechargedb.dbo.R_PINS where UNIQUE_TRANSID  = '" + sessionKey + "' and  TARGET_PHONE ='" + TARGET_PHONE + "'";
//                stat = con.createStatement();
//                ResultSet rs = stat.executeQuery(query2);
//
//                String pinNum = null;
//                String pValue = null;
//                List<PIN> pinArray = new ArrayList<PIN>();
//                msg.setPinAmount(0);
//                while (rs.next()) {
//                    pinNum = rs.getString(1);
//                    pValue = rs.getString(2);
//                    PIN pin = new PIN();
//                    pin.setPinNumber(pinNum);
//                    pin.setPinDeno(pValue);
//                    pin.setPinValue(rs.getString(3));
//                    msg.setPinAmount(msg.getPinAmount() + rs.getDouble(3));
//                    pinArray.add(pin);

            //}

            System.out.println(sessionKey);
            System.out.println(provider);
            System.out.println(pindeno);
            System.out.println(quantity);
            System.out.println(TARGET_PHONE);
            System.out.println(dealerID);
            HttpHost host = new HttpHost();
            host.setServerAddress(serverIP);
            host.setSecureKey("123456");
            Recharge recharge = new Recharge();
            recharge.setReference(sessionKey+"|WS");
            recharge.setNetwork(provider);
            recharge.setUnit(pindeno + "");
            recharge.setQuantity(quantity);
            recharge.setTargetPhone(TARGET_PHONE);
            recharge.setStockOwner(dealerID);
            recharge.setCommand(Recharge.LOCK);
            XProcessor processor = new XProcessor();
            Recharge response = processor.process(host, recharge);
            PIN[] pins = null;
            ArrayList list = new ArrayList();
            System.out.println(response.getResponse());
            if (response.getResponse() == 0) {
                list = (ArrayList) response.getVouchers();
                pins = new PIN[list.size()];
                for (int t = 0; t < list.size(); t++) {
                    Voucher v = (Voucher) list.get(t);
                    PIN pin = new PIN();
                    pin.setPinDeno(v.getPinvalue() + "");
                    pin.setPinNumber(v.getPin());
                    pin.setPinSerial(v.getSerial());
                    pin.setPinValue(v.getPinvalue() + "");
                    pins[t] = pin;
                }
            }



            msg.setPins(null);
            msg.setPins(pins);
            msg.setPinCount(list.size());
            if (quantity != list.size()) {
                msg.setMessage("PIN request successful Available Pin Not equal to the quantity requested");
                msg.setResposeCode("1");
            } else {
                msg.setMessage("PIN request successful");
                msg.setResposeCode("0");
            }





        } catch (Exception ex) {
            ex.printStackTrace();
            msg.setResposeCode("3");
            msg.setMessage("System error >>>>>  ");

        } finally {
        }
        return msg;
    }

    public void modifiyStock() {
        Connection con = null;
        String message = "";
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();

            con = Env.getConnection4PSMRPDB();
            int output = -1;


            Statement stat = null;
            stat = con.createStatement();
            String query = "";


            output = stat.executeUpdate(query);
            if (output > 0) {
                con.commit();
                message = "Stock successfully modified";
            } else {
                con.rollback();
                message = "Stock modification failed";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            message = "Stock modification failed >> database error ";
        } finally {
            try {
                con.close();
            } catch (Exception dd) {
            }

            facesMessages.add(Severity.INFO, message);
        }
    }

    public List<TPsmStock> getPINList() {
        return this.getLists();
    }

    public void listPins() {
        Connection con = null;
        String message = "";
        DateUtility dateUtil = new DateUtility();
        List lists = new ArrayList<TPsmStock>();
        try {
            String query = "set rowcount 1000 SELECT * FROM rechargedb.dbo.R_PINS where 1=1";
            Date startDate = this.searchBean.getStartDate();
            Date endDate = this.searchBean.getEndDate();
            String dealer = this.searchBean.getDealer();
            String status = this.searchBean.getStatus();
            String pinId = this.searchBean.getPinId();
            double pinValue = this.searchBean.getPinValue();
            double pimAmount = this.searchBean.getPimAmount();
            String pinSerialNumber = this.searchBean.getPinSerialNumber();
            String batchNumber = this.searchBean.getBatchNumber();
            String systemBatchNumber = this.searchBean.getSystemBatchNumber();
            Date closeDate = this.searchBean.getCloseDate();
            String providerid = this.searchBean.getProvider();

            if (startDate != null) {
                if (endDate == null) {
                    endDate = new Date();

                }
                String startDateString = dateUtil.formatDate(startDate);
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime(endDate);
                gc.add(gc.DATE, 1);
                endDate = gc.getTime();
                String endDateString = dateUtil.formatDate(endDate);
                query = query + " and upload_date  between '" + startDateString + "' and '" + endDateString + "'";
            }
            if (dealer != null && !dealer.isEmpty()) {
                query = query + " and issuer ='" + dealer + "'";
            }

            if (pinValue != 0) {
                query = query + " and convert(numeric(10,2), PIN_DENO) = '" + pinValue + "'";
            }
            if (pimAmount != 0) {
                query = query + " and PIN_VALUE = " + pimAmount + "";
            }
            if (pinSerialNumber != null && !pinSerialNumber.isEmpty()) {
                query = query + " and SERIAL ='" + pinSerialNumber + "'";
            }
            if (batchNumber != null && !batchNumber.isEmpty()) {
                query = query + " and upload_BATCHID ='" + batchNumber + "'";
            }

            if (providerid != null) {
                query = query + " and PROVIDER_ID ='" + providerid + "'";
            }
            FacesContext context = FacesContext.getCurrentInstance();
            User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();


            if (user.getUser_code() != null && !user.getUser_code().isEmpty() && !user.getUser_code().equals("000")) {
                query = query + " and issuer = '" + user.getUser_code() + "'";
            }


            query = query + " order by upload_date desc set rowcount 0";

            con = Env.getConnection4PSMRPDB();
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(query);

            while (rs.next()) {
                TPsmStock list = new TPsmStock();
                list.setPROVIDER_ID(rs.getString("PROVIDER_ID"));
                list.setUSERNAME(rs.getString("USERNAME"));
                list.setDISCOUNT(rs.getString("DISCOUNT"));
                list.setPIN(rs.getString("PIN"));
                list.setPIN_VALUE(rs.getString("PIN_VALUE"));
                list.setPIN_STATUS(rs.getString("PIN_STATUS"));
                list.setPIN_USER(rs.getString("PIN_USER"));
                list.setBATCHID(rs.getString("upload_BATCHID"));
                list.setPIN_ISSUED(rs.getDate("PIN_ISSUED"));
                list.setVALID_DAY(rs.getString("VALID_DAY"));
                list.setSERIAL(rs.getString("SERIAL"));
                list.setPIN_EXPIRATION(rs.getDate("PIN_EXPIRATION"));
                list.setORDER_NO(rs.getString("ORDER_NO"));
                list.setPIN_DENO(rs.getString("PIN_DENO"));
                list.setSUBATCHID(rs.getString("SUBATCHID"));
                list.setMERCHANT_CODE(rs.getString("MERCHANT_CODE"));
                list.setTARGET_PHONE(rs.getString("TARGET_PHONE"));
                list.setCLOSED(rs.getString("CLOSED"));
                list.setMODIFIED(rs.getDate("MODIFIED"));
                list.setUNIQUE_TRANSID(rs.getString("UNIQUE_TRANSID"));
                try {
                    list.setISSUER(rs.getString("ISSUER"));
                } catch (Exception xs) {
                }
                list.setFirst_approval(rs.getString("first_approval"));
                list.setSecond_approval(rs.getString("second_approval"));
                list.setUploader(rs.getString("uploader"));
                list.setLocked_Time(rs.getString("locked_Time"));
                list.setUpload_batchId(rs.getString("upload_batchId"));
                list.setSystemAsignedBatchID(rs.getString("SystemAsignedBatchID"));
                list.setUploadFile(rs.getString("uploadFile"));
                list.setUpload_date(rs.getDate("upload_date"));



                list.setDealer(new DealerController().getDealer(list.getISSUER(), con));
                list.setProvider(new NetworkProviderController().getProvider(list.getPROVIDER_ID(), con));
                lists.add(list);
            }

        } catch (Exception x) {
            x.printStackTrace();
            message = "Stock fatch failed >> database error ";
        } finally {
            try {
                con.close();
            } catch (Exception dd) {
            }
            facesMessages.add(Severity.INFO, message);
            // return null;
        }
        this.setLists(lists);
    }

    public void listPinsApproval() {
        Connection con = null;
        String message = "";
        DateUtility dateUtil = new DateUtility();
        List lists = new ArrayList<TPsmStock>();
        String batchNumber = this.searchBean.getBatchNumber();
        try {
            String query = "set rowcount 0 SELECT * FROM rechargedb.dbo.R_PINS_TEMP where 1=1";
            Date startDate = this.searchBean.getStartDate();
            Date endDate = this.searchBean.getEndDate();
            String dealer = this.searchBean.getDealer();
            String status = this.searchBean.getStatus();
            String pinId = this.searchBean.getPinId();
            double pinValue = this.searchBean.getPinValue();
            double pimAmount = this.searchBean.getPimAmount();
            String pinSerialNumber = this.searchBean.getPinSerialNumber();
            
            String systemBatchNumber = this.searchBean.getSystemBatchNumber();
            Date closeDate = this.searchBean.getCloseDate();
            String providerid = this.searchBean.getProvider();

            if (startDate != null) {
                if (endDate == null) {
                    endDate = new Date();
                }
                String startDateString = dateUtil.formatDate(startDate);
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime(endDate);
                gc.add(gc.DATE, 1);
                endDate = gc.getTime();
                String endDateString = dateUtil.formatDate(endDate);
                query = query + " and upload_date  between '" + startDateString + "' and '" + endDateString + "'";
            }
            if (dealer != null && !dealer.isEmpty()) {
                query = query + " and issuer ='" + dealer + "'";
            }

            if (pinValue != 0) {
                query = query + " and convert(numeric(10,2), PIN_DENO) = '" + pinValue + "'";
            }
            if (pimAmount != 0) {
                query = query + " and PIN_VALUE = " + pimAmount + "";
            }
            if (pinSerialNumber != null && !pinSerialNumber.isEmpty()) {
                query = query + " and SERIAL ='" + pinSerialNumber + "'";
            }
            //if (batchNumber != null && !batchNumber.isEmpty())
            {
                query = query + " and upload_BATCHID ='" + batchNumber + "'";
            }

            if (providerid != null) {
                query = query + " and PROVIDER_ID ='" + providerid + "'";
            }
            FacesContext context = FacesContext.getCurrentInstance();
            User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();


            if (user.getUser_code() != null && !user.getUser_code().isEmpty() && !user.getUser_code().equals("000")) {
                query = query + " and issuer = '" + user.getUser_code() + "'";
            }
            if (this.searchBean.getLevel() == null) {

                query = query + " and first_approval = ''";
                query = query + " and uploader != '" + user.getUser_id() + "'";
            } else {
                if (this.searchBean.getLevel().equals("1")) {
                    query = query + " and first_approval = ''";
                } else if (this.searchBean.getLevel().equals("2")) {
                    query = query + " and first_approval != ''";
                    query = query + " and second_approval = ''";
                    query = query + " and first_approval != '" + user.getUser_id() + "'";
                    query = query + " and uploader != '" + user.getUser_id() + "'";
                } else {
                    query = "";
                }
            }


            query = query + " order by upload_date desc set rowcount 0";

            con = Env.getConnection4PSMRPDB();
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(query);
            this.stock = null;
            while (rs.next()) {
                TPsmStock list = new TPsmStock();
                list.setPROVIDER_ID(rs.getString("PROVIDER_ID"));
                list.setUSERNAME(rs.getString("USERNAME"));
                list.setDISCOUNT(rs.getString("DISCOUNT"));
                list.setPIN(rs.getString("PIN"));
                list.setPIN_VALUE(rs.getString("PIN_VALUE"));
                list.setPIN_STATUS(rs.getString("PIN_STATUS"));
                list.setPIN_USER(rs.getString("PIN_USER"));
                list.setBATCHID(rs.getString("upload_BATCHID"));
                list.setPIN_ISSUED(rs.getDate("PIN_ISSUED"));
                list.setVALID_DAY(rs.getString("VALID_DAY"));
                list.setSERIAL(rs.getString("SERIAL"));
                list.setPIN_EXPIRATION(rs.getDate("PIN_EXPIRATION"));
                list.setORDER_NO(rs.getString("ORDER_NO"));
                list.setPIN_DENO(rs.getString("PIN_DENO"));
                list.setSUBATCHID(rs.getString("SUBATCHID"));
                list.setMERCHANT_CODE(rs.getString("MERCHANT_CODE"));
                list.setTARGET_PHONE(rs.getString("TARGET_PHONE"));
                list.setCLOSED(rs.getString("CLOSED"));
                list.setMODIFIED(rs.getDate("MODIFIED"));
                list.setUNIQUE_TRANSID(rs.getString("UNIQUE_TRANSID"));
                try {
                    list.setISSUER(rs.getString("ISSUER"));
                } catch (Exception xs) {
                }
                list.setFirst_approval(rs.getString("first_approval"));
                list.setSecond_approval(rs.getString("second_approval"));
                list.setUploader(rs.getString("uploader"));
                list.setLocked_Time(rs.getString("locked_Time"));
                list.setUpload_batchId(rs.getString("upload_batchId"));
                list.setSystemAsignedBatchID(rs.getString("SystemAsignedBatchID"));
                list.setUploadFile(rs.getString("uploadFile"));
                list.setUpload_date(rs.getDate("upload_date"));
                list.setInvalidCount(rs.getString("invalidCount"));
                list.setValidCount(rs.getString("validCount"));
                list.setDealer(new DealerController().getDealer(list.getISSUER(), con));
                list.setProvider(new NetworkProviderController().getProvider(list.getPROVIDER_ID(), con));
                lists.add(list);
                this.stock = list;
            }

        } catch (Exception x) {
            x.printStackTrace();
            message = "Stock fatch failed >> database error ";
        } finally {
            try {
                con.close();
            } catch (Exception dd) {
            }
            facesMessages.add(Severity.INFO, message);
            // return null;
        }
        listPinsApprovalError(batchNumber);
        this.setLists(lists);
    }


    public void listPinsApprovalError(String batchId) {
        Connection con = null;        
        List<ErrorBean> lists = new ArrayList<ErrorBean>();
        try {
            String query = "set rowcount 0 SELECT * FROM rechargedb.dbo.R_PINS_TEMP_ERROR where batch_id ='"+batchId+"'";
           

            con = Env.getConnection4PSMRPDB();
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()) {
                ErrorBean list = new ErrorBean();
                list.setBatchId(rs.getString("batch_id"));
                list.setSysBatchId(rs.getString("system_batchid"));
                list.setLine(rs.getString("line"));
                list.setErrorDesc(rs.getString("error_desc"));
                lists.add(list);
            }

        } catch (Exception x) {
            x.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception dd) {
            }
           
        }
        this.setErrorList(lists);
    }

    public void listPinsSold() {
        Connection con = null;
        String message = "";
        DateUtility dateUtil = new DateUtility();
        List lists = new ArrayList<TPsmStock>();
        try {
            String query = "set rowcount 1000 SELECT * FROM rechargedb.dbo.r_pins_BOUGHT where 1=1";
            Date startDate = this.searchBean.getStartDate();
            Date endDate = this.searchBean.getEndDate();
            String dealer = this.searchBean.getDealer();
            String status = this.searchBean.getStatus();
            String pinId = this.searchBean.getPinId();
            double pinValue = this.searchBean.getPinValue();
            double pimAmount = this.searchBean.getPimAmount();
            String pinSerialNumber = this.searchBean.getPinSerialNumber();
            String batchNumber = this.searchBean.getBatchNumber();
            String systemBatchNumber = this.searchBean.getSystemBatchNumber();
            Date closeDate = this.searchBean.getCloseDate();
            String providerid = this.searchBean.getProvider();

            if (startDate != null) {
                if (endDate == null) {
                    endDate = new Date();
                }
                String startDateString = dateUtil.formatDate(startDate);
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime(endDate);
                gc.add(gc.DATE, 1);
                endDate = gc.getTime();
                String endDateString = dateUtil.formatDate(endDate);
                query = query + " and upload_date  between '" + startDateString + "' and '" + endDateString + "'";
            }
            if (dealer != null && !dealer.isEmpty()) {
                query = query + " and issuer ='" + dealer + "'";
            }

            if (pinValue != 0) {
                query = query + " and convert(numeric(10,2), PIN_DENO) = '" + pinValue + "'";
            }
            if (pimAmount != 0) {
                query = query + " and PIN_VALUE = " + pimAmount + "";
            }
            if (pinSerialNumber != null && !pinSerialNumber.isEmpty()) {
                query = query + " and SERIAL ='" + pinSerialNumber + "'";
            }
            try {
                if (batchNumber != null && !batchNumber.isEmpty()) {
                    query = query + " and upload_BATCHID ='" + batchNumber + "'";
                }
            } catch (Exception se) {
            }

            if (providerid != null) {
                query = query + " and PROVIDER_ID ='" + providerid + "'";
            }
            FacesContext context = FacesContext.getCurrentInstance();
            User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();


            if (user.getUser_code() != null && !user.getUser_code().isEmpty() && !user.getUser_code().equals("000")) {
                query = query + " and issuer = '" + user.getUser_code() + "'";
            }


            query = query + " order by upload_date desc set rowcount 0";

            con = Env.getConnection4PSMRPDB();
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(query);

            while (rs.next()) {
                TPsmStock list = new TPsmStock();
                list.setPROVIDER_ID(rs.getString("PROVIDER_ID"));
                list.setUSERNAME(rs.getString("USERNAME"));
                list.setDISCOUNT(rs.getString("DISCOUNT"));
                list.setPIN(rs.getString("PIN"));
                list.setPIN_VALUE(rs.getString("PIN_VALUE"));
                list.setPIN_STATUS(rs.getString("PIN_STATUS"));
                list.setPIN_USER(rs.getString("PIN_USER"));
                try {
                    list.setBATCHID(rs.getString("upload_BATCHID"));
                } catch (Exception sd) {
                }
                list.setPIN_ISSUED(rs.getDate("PIN_ISSUED"));
                list.setVALID_DAY(rs.getString("VALID_DAY"));
                list.setSERIAL(rs.getString("SERIAL"));
                list.setPIN_EXPIRATION(rs.getDate("PIN_EXPIRATION"));
                list.setORDER_NO(rs.getString("ORDER_NO"));
                list.setPIN_DENO(rs.getString("PIN_DENO"));
                list.setSUBATCHID(rs.getString("SUBATCHID"));
                list.setMERCHANT_CODE(rs.getString("MERCHANT_CODE"));
                list.setTARGET_PHONE(rs.getString("TARGET_PHONE"));
                list.setCLOSED(rs.getString("CLOSED"));
                try {
                    list.setISSUER(rs.getString("ISSUER"));
                } catch (Exception xs) {
                }
                list.setFirst_approval(rs.getString("first_approval"));
                list.setSecond_approval(rs.getString("second_approval"));
                list.setUploader(rs.getString("uploader"));
                list.setLocked_Time(rs.getString("locked_Time"));
                list.setUpload_batchId(rs.getString("upload_batchId"));
                list.setSystemAsignedBatchID(rs.getString("SystemAsignedBatchID"));
                list.setUploadFile(rs.getString("uploadFile"));
                list.setUpload_date(rs.getDate("upload_date"));
                list.setDealer(new DealerController().getDealer(list.getISSUER(), con));
                list.setProvider(new NetworkProviderController().getProvider(list.getPROVIDER_ID(), con));
                lists.add(list);
            }

        } catch (Exception x) {
            x.printStackTrace();
            message = "Stock fatch failed >> database error ";
        } finally {
            try {
                con.close();
            } catch (Exception dd) {
            }
            facesMessages.add(Severity.INFO, message);
            // return null;
        }
        this.setLists(lists);
    }

    public List getAllPins() {
        Connection con = null;
        String message = "";
        String query = "set rowcount 100 SELECT pin from rechargedb.dbo.R_PINS";
        List list = new ArrayList();
        try {

            con = Env.getConnection4PSMRPDB();
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(query);

            while (rs.next()) {
                list.add(rs.getString("pin"));
            }

        } catch (Exception x) {
            x.printStackTrace();
            message = "Stock fatch failed >> database error " + x.getMessage();
        } finally {
            try {
                con.close();
            } catch (Exception dd) {
            }

        }
        if (list.size() > 0) {
            setRendered(true);
            return list;
        } else {
            setRendered(false);
            return null;

        }
    }

    public void setSelectedPinStock() {
    }

    public void setSelectedPinStock(TPsmStock stock) {
        this.stock = stock;
    }

    public void distroy() {
        FacesContext context = FacesContext.getCurrentInstance();
        FileUploadBean uc = ((FileUploadBean) context.getExternalContext().getSessionMap().get("fileUploadBean"));
        lists = null;
        if (uc != null) {
            uc.distroy();
            uc = null;
        }
        NetworkProviderController net = ((NetworkProviderController) context.getExternalContext().getSessionMap().get("networkprovidercontroller"));
        lists = null;
        if (net != null) {
            net.distroy();
            net = null;
        }

        UserContactController userc = ((UserContactController) context.getExternalContext().getSessionMap().get("tpsmusercontactcontroller"));
        lists = null;
        if (userc != null) {
            userc.distroy();
            userc = null;
        }

        this.searchBean = null;
        DealerController dc = ((DealerController) context.getExternalContext().getSessionMap().get("dealercontroller"));
        lists = null;
        if (dc != null) {
            dc.distroy();
            dc = null;
        }
    }

    public static void main(String[] s) {
        TPSMController c = new TPSMController();
        HttpHost host = new HttpHost();
        host.setServerAddress("172.16.10.101");
        host.setSecureKey("123456");
        Recharge recharge = new Recharge();
        recharge.setReference("8888998655");
        recharge.setNetwork("MTN");
        String deno = "100.0";
        deno = deno.substring(0, deno.indexOf("."));
        System.out.println(deno);
        recharge.setUnit(deno);
        recharge.setQuantity(1);
        recharge.setTargetPhone("23480675643213");
        recharge.setStockOwner("000");
        recharge.setCommand(Recharge.LOCK);
        XProcessor processor = new XProcessor();

        try {
            Recharge response = processor.process(host, recharge);
            System.out.println("Response::" + response.getResponse());
            if (response.getResponse() == 0) {
                ArrayList list = (ArrayList) response.getVouchers();
                for (int t = 0; t < list.size(); t++) {
                    System.out.println(((Voucher) list.get(t)).getPin());
                    System.out.println("XXX" + ((Voucher) list.get(t)).getSerial());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
