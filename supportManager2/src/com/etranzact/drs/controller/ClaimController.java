/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etranzact.drs.controller;

import com.etranzact.drs.dto.ClMessages;
import com.etranzact.drs.dto.Claim;
import com.etranzact.drs.dto.ClaimSearchBean;
import com.etranzact.drs.dto.ClaimsSummary;
import com.etranzact.drs.dto.MessageSearchBean;
import com.etranzact.drs.dto.TransactionSearch;
import com.etranzact.drs.dto.ViewMessage;
import com.etranzact.drs.utility.DateUtility;
import com.etranzact.drs.utility.EmailClass;
import com.etranzact.drs.utility.Utility;
import com.etranzact.supportmanager.action.AuthenticationAction;
import com.etranzact.supportmanager.dto.User;
import com.etranzact.supportmanager.utility.Env;
import com.etranzact.supportmanager.utility.HashNumber;
import com.sun.facelets.FaceletContext;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.*;
import jxl.write.*;
import org.hibernate.validator.Valid;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

/**
 *
 * @author Akachukwu
 */
@Scope(ScopeType.SESSION)
@Name("claimcontroller")
public class ClaimController {

    @Valid
    private Claim claim;
    static FacesContext context;
    static int claimReopenLifeSpan = 30;
    private List<Claim> claimList;
    //static User user;
    private ClaimSearchBean searchBean;
    private MessageSearchBean messageSearchBean;
    String searchype;
    @In
    FacesMessages facesMessages;
    File file = new File();
    HashNumber hn = new HashNumber();

    public void createClaim() {
        //= claim;
        Claim claim = this.claim;
        ;
        try {
            Connection con = Env.getConnection4DRSDB();
            DateUtility dateUtil = new DateUtility();
            int output = -1;
            String message = "";

            context = FacesContext.getCurrentInstance();
            User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();

            String cUser = user.getUser_id();
            String bank = user.getUser_code();
            String dueDate = dateUtil.formatDate(dateUtil.calculateDueDate(new Date(), claim.getChannelType(), claim.getType()));
            String sysID = new Utility().getSeq();


            claim.setSysID(sysID);


            try {
                Statement stat = null;
                stat = con.createStatement();
                claim.setClaimStatus("O");
                String decline_reason="";
                String closed_user="";
                if (claim.getResponseCode() != null && ((claim.getResponseCode().trim().equals("0") || claim.getResponseCode().trim().equals("00") || claim.getResponseCode().trim().equals("000")))  ) {
                    
                } else {
                claim.setClaimStatus("D");
                decline_reason="Auto Declined because the original transaction was not successful";
                closed_user="000";
                }
                String query = "INSERT INTO telcodb..claim (acquirer, channel_type, card_no, transaction_amount, "
                        + "date_of_transaction, claim_type, comment_journal, created_date, claim_status, reason_for_modify, "
                        + "created_user,complaining_bank,real_created_date,transaction_stan,terminal_id,linkage,duedate,sysID,decline_reason,closed_user) "
                        + "VALUES ('" + claim.getAcquirer() + "','" + claim.getChannelType() + "','" + claim.getCardNo() + "'," + claim.getTransactionAmount() + ","
                        + "getDate(),'" + claim.getClaimType() + "','" + claim.getComment_Journal() + "',getdate(),'"+claim.getClaimStatus()+"','','" + cUser + "','" + bank + "','" + dateUtil.formatDate(claim.getRealCreatedDate()) + "',"
                        + "'" + claim.getTransactionStan() + "','" + claim.getTerminalId() + "','" + claim.getId() + "','" + dueDate + "','" + sysID + "','"+decline_reason+"','"+closed_user+"')";

                String queryUpdate = "UPDATE telcodb..claim SET modifiyBy='" + cUser + "', reason_for_modify  = '" + claim.getReasonForModify() + "', claim_status='X', modifyDate= getdate() WHERE id = " + claim.getId() + "  and claim_status  = 'O'";
                //output = stat.executeUpdate(queryUpdate);
                if(1==1){
                int outPutUpdate = stat.executeUpdate(queryUpdate);

                    if (outPutUpdate > 0 || claim.getId() == null) {

                        if (!this.checkDublicate(con, claim.getTransactionStan(), claim.getTerminalId(), "" + claim.getId())) {
                            if(this.isValid(claim.getRealCreatedDate()))
                            {
                            output = stat.executeUpdate(query);
                            } else
                            {
                                facesMessages.add(Severity.INFO, "Error! This transaction cannot be logged. Transaction date exceeded maximum number of days(120days)");
                            }
                            
                        } else {
                            facesMessages.add(Severity.INFO, "Error! Claim has been logged before");
                        }
                    }


                    if (output > 0) {
                        con.commit();
                        String[] usersMail = new UserContactController().getUserEmailList(claim.getAcquirer() + "_000");
                        String emailMessage = "Dear Sir/Ma, \r\n\r\nA claim has been logged on dispute resolution "
                                + "system for your attention. \r\nFind below the details of the claim. "
                                + "\r\n\r\nRegards. \r\n\r\n Logged Bank: " + claim.getClBank().getBankname() + "\r\n "
                                + "Logged User: " + user.getFirstname() + "\r\n Logged Date: " + claim.getDateOfTransaction() + "\r\n "
                                + "Card/Phone/PAN No: " + claim.getCardNo() + " \r\n Trace ID: " + claim.getTransactionStan() + " \r\n Expiry Date: " + dueDate;
                        new Thread(new EmailClass(usersMail, "DRS Notification -" + sysID, emailMessage, "")).start();
                        message = "Claim successfully logged";
                    } else {
                        con.rollback();
                        message = "Claim not logged";
                    }
                } else {
                    message = "Claim not logged. Original transaction not successful.";
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    con.rollback();
                    message = "Error occured while creating Claim";
                } catch (Exception e) {
                }

            } finally {
                try {
                    con.close();
                } catch (Exception s) {
                }
                facesMessages.add(Severity.INFO, message);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }



    }

    public void export() {
        java.io.File f_ = new ExcelWriter().write(this.claimList);
        try {
            InputStream is = new FileInputStream(f_);
            long length = f_.length();
            byte[] bytes = new byte[(int) length];
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file " + f_.getName());
            }
            is.close();

            context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.setHeader("Content-disposition",
                    "attachment; filename= " + f_.getName());
            response.setContentLength(bytes.length);
            response.setContentType("application/vnd.ms-excel");
            try {

                response.getOutputStream().write(bytes);
                response.getOutputStream().flush();
                response.getOutputStream().close();
                context.responseComplete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception dd) {
            dd.printStackTrace();
        }
    }

    public void createMessage() {
        //= claim;
        Claim claim = this.claim;
        ClMessages cl_message = claim.getMessage();
        try {
            Connection con = Env.getConnection4DRSDB();
            DateUtility dateUtil = new DateUtility();
            int output = -1;
            context = FacesContext.getCurrentInstance();
            User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
            String message = "";

            try {
                Statement stat = null;
                stat = con.createStatement();
                if (claim.getPrevMessageBody() == null || (claim.getPrevMessageBody() + "").toUpperCase().equals("NULL")) {
                    claim.setPrevMessageBody("");
                }
                String query = "INSERT INTO telcodb..cl_messages "
                        + "( claimID , subject,  dateCreated , message)"
                        + "VALUES ('" + claim.getId() + "', '" + claim.getSubject() + "', getdate(), 'Date: " + new Date() + "\r\nSender: " + user.getFirstname() + " " + user.getLastname() + "\r\nSubject: " + claim.getSubject() + "\r\nMessage Body:\r\n " + claim.getMessageBody() + "\r\n \r\n" + claim.getPrevMessageBody() + "')";

                String queryUpdate = "DELETE FROM cl_messages  WHERE claimID = '" + claim.getId() + "'";
                stat.executeUpdate(queryUpdate);
                output = stat.executeUpdate(query);
                if (output > 0) {
                    con.commit();
                    String[] usersMail = new UserContactController().getUserEmailList(claim.getAcquirer() + "_000_" + claim.getComplainingBank());
                    String emailMessage = "Dear Sir/Ma, \r\nA mail has been sent to you on dispute resolution system "
                            + "for your attention.\r\nFind below the contain of the mail \r\nRegards \r\n \r\n Date: " + new Date() + "\r\nSender: " + user.getFirstname() + " " + user.getLastname() + "\r\nSubject: " + claim.getSubject() + "\r\n" + claim.getPrevMessageBody();
                    new Thread(new EmailClass(usersMail, "DRS Mail Notification-" + claim.getSubject() + "-" + claim.getSysID(), emailMessage, "")).start();
                    message = "Mail successfully sent";
                } else {
                    con.rollback();
                    message = "Mail not sent";
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    con.rollback();
                    message = "Error occured while creating mail";
                } catch (Exception e) {
                }

            } finally {
                try {
                    con.close();
                    claim.setSubject("");
                    claim.setMessageBody("");
                } catch (Exception s) {
                }
                facesMessages.add(Severity.INFO, message);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }



    }

    private boolean checkDublicate(Connection con, String transaction_stan, String terminal_id, String claimgetId) {
        //Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        boolean re = false;

        if (claimgetId == null || claimgetId.isEmpty() || claimgetId.toUpperCase().equals("NULL")) {
            claimgetId = null;
        }
        System.out.println(claimgetId);

        try {
            System.out.println("s  XXXXXXXXXXXXXXXXXXXXXXXXXX ");
            //con = Env.getConnection4DRSDB();
            System.out.println("ss XXXXXXXXXXXXXXXXXXXXXXXXXX ");
            String query = "Select * from telcodb..claim where transaction_stan = '" + transaction_stan + "'";

            stat = con.createStatement();
            rs = stat.executeQuery(query);
            System.out.println(query);
            if (rs.next()) {
                re = true;
            }
            if (re == true) {
                if (claimgetId != null) {
                    re = false;
                }
            }


        } catch (Exception se) {
            se.printStackTrace();

        } finally {
            try {
                rs.close();
                stat.close();
                //con.close();
            } catch (Exception se) {
            }
            //con = null;
            stat = null;
            rs = null;

        }
        return re;
    }

    public void editClaim() {

        Claim claim = this.claim;
        context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
        String cUser = user.getUser_id();
        try {
            Connection con = Env.getConnection4DRSDB();
            DateUtility dateUtil = new DateUtility();
            int output = -1;
            String message = "";


            try {
                Statement stat = null;
                stat = con.createStatement();
//                String query = "INSERT INTO claim (acquirer, channel_type, card_no, transaction_amount, "
//                        + "date_of_transaction, claim_type, comment_journal, created_date, claim_status, reason_for_modify, "
//                        + "closed_date, closed_user, created_user,complaining_bank,real_created_date) "
//                        + "VALUES ('"+claim.getAcquirer()+"','"+claim.getChannelType()+"','"+claim.getCardNo()+"','"+claim.getTransactionAmount()+"',"
//                        + "'"+dateUtil.getDate(claim.getDateOfTransaction())+"','"+claim.getClaimType()+"','"+claim.getComment_Journal()+"',"
//                        + "'"+dateUtil.getDate()+"','O','','','','"+bankUser+"','"+bank+"',"+dateUtil.getDate(new Date())+")";

                String queryUpdate = "UPDATE telcodb..claim SET  acquirer  = '" + claim.getAcquirer() + "',  channel_type  = '" + claim.getChannelType() + "', "
                        + " card_no  = '" + claim.getCardNo() + "',  transaction_amount  = '" + claim.getTransactionAmount() + "', "
                        + " date_of_transaction  = '" + dateUtil.formatDate(claim.getDateOfTransaction()) + "',  claim_type  = '" + claim.getClaimType() + "', "
                        + " comment_journal  = '" + claim.getComment_Journal() + "', "
                        + " claim_status  = 'O',  reason_for_modify  = '" + claim.getReasonForModify() + "',  closed_date  = '" + claim.getClosedDate() + "', "
                        + " closed_user  = '" + claim.getClosedUser() + "',  created_user  = '" + claim.getCreatedUser() + "', "
                        + " transaction_stan  = '" + claim.getTransactionStan() + "',  terminal_id  = '" + claim.getTerminalId() + "', "
                        + " complaining_bank  = '" + claim.getComplainingBank() + "',  reversal_transaction_id  = '" + claim.getReversalTransactionId() + "' WHERE id = " + claim.getId() + "";
                output = stat.executeUpdate(queryUpdate);
                if (output > 0) {
                    con.commit();
                    this.claim = null;
                    message = "Records successfully modified";
                } else {
                    con.rollback();
                    message = "Records not modified";
                }

                facesMessages.add(Severity.INFO, message);

            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    con.rollback();
                    message = "Error occured while creating user";
                } catch (Exception e) {
                }

            } finally {
                try {
                    con.close();

                } catch (Exception s) {
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    private String getTrack2(String cNo)
    {
        String resp="";

        try{
             String  url = "http://172.16.10.35/VirtualConsole/panretriever.jsp?CARD_NUM="+cNo+"";
               System.out.println("HttpMessenger::submit() - "+url);
             URL  urlObject = new URL(url);
               // Actually make a connection
               HttpURLConnection connection = (HttpURLConnection)urlObject.openConnection();
               connection.setDoInput(true);

               connection.connect();
               int responseCode = connection.getResponseCode();
               System.out.println("HttpMessenger::proceed() - responseCode: "+responseCode);
               
               if(responseCode == 200) {
                  
                  BufferedReader in = new BufferedReader(
                   new InputStreamReader(connection.getInputStream()));
                   StringBuffer buildStr = new StringBuffer();
                   while((resp = in.readLine())!= null){
                       buildStr.append(resp);
                   }
                   System.out.println("HttpMessenger::proceed() - response: "+buildStr.toString());
                   resp = buildStr.toString();
                   //if (resp.toLowerCase().indexOf("success") > -1) submitted = 0;
               }else{
                   
                   resp="";
               }
       }catch(Exception eee){
         System.out.println("HttpMessenger::proceed()::"+eee);
        
       }

        return resp;
    }

    public List<TransactionSearch> doTransactionSearch() {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        Date date1 = claim.getDateOfTransaction();
        Date date2 = claim.getDateOfTransaction2();
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date2);
        gc.add(gc.DATE, 1);
        date2 = gc.getTime();
        String cardNo = claim.getCardNo();
        DateUtility du = new DateUtility();
        List<TransactionSearch> lists = new ArrayList<TransactionSearch>();


        try {
            //con = Env.getConnection4DSRDB();

            //String query = "SELECT * FROM ecarddb..E_TMCREQUEST where PAN = '"+cardNo+"' and trans_date ";


            String query = null;
            int channelID = Integer.parseInt(claim.getChannelType());

            if (channelID == 2) {
                try {
                    if (cardNo != null && !cardNo.replace("+", "").startsWith("234")) {
                        cardNo = "234" + cardNo.substring(1);
                    } else {
                        cardNo.trim().replace("+", "");
                    }
                } catch (Exception s) {
                }
                con = Env.getConnectionECardDB();
                query = "select card_num,trans_date,merchant_code,response_message,"
                        + "transid,trans_amount,mobile,a.RESPONSE_CODE as respCode,trans_code from ecarddb..e_requestlog a, ecarddb..e_smslog b "
                        + "where a.transid = b.unique_transid  ";
                if (cardNo != null && !cardNo.isEmpty()) {
                    query = query + "and mobile = '" + cardNo + "' ";
                }
                if (claim.getTransactionStan() != null && !claim.getTransactionStan().isEmpty()) {
                    query = query + "and transid = '" + claim.getTransactionStan() + "' ";
                }
                query = query + "and trans_date between '" + du.formatDate(date1) + "' and '" + du.formatDate(date2) + "' order by trans_date desc";
                stat = con.createStatement();

                rs = stat.executeQuery(query);
                System.out.println(query);

                while (rs.next()) {
                    TransactionSearch list = new TransactionSearch();
                    list.setStan(rs.getString("transid"));
                    list.setTerminalId("");
                    list.setTransactionAmount(rs.getString("trans_amount"));
                    list.setMerchantCode(rs.getString("merchant_code"));
                    list.setTransactionNo(rs.getString("transid"));
                    list.setTransactionDate(rs.getString("trans_date"));
                    list.setTransactionDescription(rs.getString("response_message"));
                    list.setCardNo(rs.getString("mobile"));
                    list.setAcctNo(rs.getString("mobile"));
                    list.setBank(rs.getString("MERCHANT_CODE"));
                    String tc = rs.getString("trans_code");
                    if (tc != null && tc.equals("P")) {
                    } else {
                        try {
                            list.setBank(list.getBank().substring(0, 3));
                            list.setType("T");
                        } catch (Exception s) {
                        }
                    }
                    list.setResponseCode(rs.getString(8));
                    list.setTransactionRef("");
                    lists.add(list);
                }

            } else if (channelID == 1) {

                con = Env.getConnectionECardDB();
                query = "select card_num,trans_date,merchant_code,TRANS_DESCR,"
                        + "transid,trans_amount,TRANS_DESCR,a.RESPONSE_CODE as respCode,trans_code from ecarddb..e_requestlog a where substring(transid,1,2)='01' "
                        + "";
                if (cardNo != null && !cardNo.isEmpty()) {
                    query = query + " and card_num = '" + cardNo + "' ";
                }
                if (claim.getTransactionStan() != null && !claim.getTransactionStan().isEmpty()) {
                    query = query + " and transid = '" + claim.getTransactionStan() + "' ";
                }
                query = query + " and trans_date between '" + du.formatDate(date1) + "' and '" + du.formatDate(date2) + "' order by trans_date desc";
                stat = con.createStatement();
                System.out.println(query);
                rs = stat.executeQuery(query);

                while (rs.next()) {
                    TransactionSearch list = new TransactionSearch();
                    list.setStan(rs.getString("transid"));
                    list.setTerminalId("");
                    list.setTransactionAmount(rs.getString("trans_amount"));
                    list.setMerchantCode(rs.getString("merchant_code"));
                    list.setTransactionNo(rs.getString("transid"));
                    list.setTransactionDate(rs.getString("trans_date"));
                    list.setTransactionDescription(rs.getString("TRANS_DESCR"));
                    list.setCardNo(hn.hashStringValue(rs.getString("card_num"), 4, 4));
                    list.setAcctNo(hn.hashStringValue(rs.getString("card_num"), 4, 4));
                    list.setBank(rs.getString("MERCHANT_CODE"));
                    String tc = rs.getString("trans_code");
                    if (tc != null && tc.equals("P")) {
                    } else {
                        try {
                            list.setBank(list.getBank().substring(0, 3));
                            list.setType("T");
                        } catch (Exception s) {
                        }
                    }

                    list.setResponseCode(rs.getString(8));
                    list.setTransactionRef("");
                    lists.add(list);
                }

            } else if (channelID == 4 )//ATM
                    {
                con = Env.getConnectionETMCDB();
                com.etz.mobile.security.CardAudit cardAudit = new com.etz.mobile.security.CardAudit();//502192
                String PAN = getTrack2(cardNo);
                System.out.println("PAN: " + PAN);
                if (channelID == 3 || channelID == 4) {
                    cardNo = PAN;
                    System.out.println("Deduce Track FromCard >>>>>>>>>>>>>>>>>>>>> ");
                }

                String sc_id = cardNo.substring(6, 12);

                boolean checkResult = this.checkE_EXFRONTENDINTERFACE(sc_id, con);

                System.out.println(checkResult + " CCCCCCCCCCCCCCCCCCCC ");


                query = " SELECT * FROM ecarddb..E_TMCREQUEST where 1=1 "
                        + "";
                if (cardNo != null && !cardNo.isEmpty()) {
                    query = query + " and PAN = '" + cardNo + "' ";
                }
                if (claim.getTransactionStan() != null && !claim.getTransactionStan().isEmpty()) {
                    query = query + " and REFERENCE = '" + claim.getTransactionStan() + "' ";
                }
                query = query + " and trans_date between '" + du.formatDate(date1) + "' and '" + du.formatDate(date2) + "' order by trans_date desc";
                //String query = "Select * from transaction_search where transaction_date='" + date + "' and card_no = '" + cardNo + "' and transaction_amount = " + amount + "";

                stat = con.createStatement();

                rs = stat.executeQuery(query);


                while (rs.next()) {
                    TransactionSearch list = new TransactionSearch();
                    list.setStan(rs.getString("REFERENCE"));
                    list.setTerminalId(rs.getString("terminal_id"));
                    list.setTransactionAmount(rs.getString("amount"));
                    list.setMerchantCode(rs.getString("merchant_type"));
                    list.setTransactionNo(rs.getString("trans_seq"));
                    list.setTransactionDate(rs.getString("trans_date"));
                    list.setTransactionDescription(rs.getString("card_acc_name"));
                    list.setCardNo(hn.hashStringValue(rs.getString("pan"), 4, 4));
                    list.setAcctNo(hn.hashStringValue(rs.getString("pan"), 4, 4));
                    list.setResponseCode(rs.getString("RESPONSE_CODE"));
                    list.setTransactionRef(rs.getString("REFERENCE"));
                    if (!checkResult) {
                        String bank = this.getTranBank(con, rs.getString("aqid"));
                        list.setBank(bank);

                    } else {
                        if(claim.getClaimType()==1 || claim.getClaimType()==2)
                        {
                        String bank = this.getTranBank(con, rs.getString("aqid"));
                        list.setBank(bank);
                        }else {
                        list.setBank(sc_id);
                        }
                       // list.setBank(sc_id);
                    }

                    lists.add(list);
                }

            } else
            {
                con = Env.getConnectionETMCDB();
                com.etz.mobile.security.CardAudit cardAudit = new com.etz.mobile.security.CardAudit();//502192
                String PAN = getTrack2(cardNo);
                System.out.println("PAN: " + PAN);
                if (channelID == 3 || channelID == 4) {
                    cardNo = PAN;
                    System.out.println("Deduce Track FromCard >>>>>>>>>>>>>>>>>>>>> ");
                }

                String sc_id = cardNo.substring(6, 12);

                boolean checkResult = this.checkE_EXFRONTENDINTERFACE(sc_id, con);

                System.out.println(checkResult + " CCCCCCCCCCCCCCCCCCCC ");


                query = " SELECT * FROM ecarddb..E_TMCREQUEST where 1=1 "
                        + "";
                if (cardNo != null && !cardNo.isEmpty()) {
                    query = query + " and PAN = '" + cardNo + "' ";
                }
                if (claim.getTransactionStan() != null && !claim.getTransactionStan().isEmpty()) {
                    query = query + " and REFERENCE = '" + claim.getTransactionStan() + "' ";
                }
                query = query + " and trans_date between '" + du.formatDate(date1) + "' and '" + du.formatDate(date2) + "' order by trans_date desc";
                //String query = "Select * from transaction_search where transaction_date='" + date + "' and card_no = '" + cardNo + "' and transaction_amount = " + amount + "";

                stat = con.createStatement();

                rs = stat.executeQuery(query);


                while (rs.next()) {
                    TransactionSearch list = new TransactionSearch();
                    list.setStan(rs.getString("REFERENCE"));
                    list.setTerminalId(rs.getString("terminal_id"));
                    list.setTransactionAmount(rs.getString("amount"));
                    list.setMerchantCode(rs.getString("merchant_type"));
                    list.setTransactionNo(rs.getString("trans_seq"));
                    list.setTransactionDate(rs.getString("trans_date"));
                    list.setTransactionDescription(rs.getString("card_acc_name"));
                    list.setCardNo(hn.hashStringValue(rs.getString("pan"), 4, 4));
                    list.setAcctNo(hn.hashStringValue(rs.getString("pan"), 4, 4));
                    list.setResponseCode(rs.getString("RESPONSE_CODE"));
                    list.setTransactionRef(rs.getString("REFERENCE"));
                    if (!checkResult) {
                        String bank = this.getTranBank(con, rs.getString("aqid"));
                        list.setBank(bank);
                    } else {
                        if(claim.getClaimType()==1 || claim.getClaimType()==2)
                        {
                        String bank = this.getTranBank(con, rs.getString("aqid"));
                        list.setBank(bank);
                        }else {
                        list.setBank(sc_id);
                        }
                    }

                    lists.add(list);
                }

            }

        } catch (Exception se) {
            se.printStackTrace();
            return null;
        } finally {
            try {
                rs.close();
                stat.close();
                con.close();
            } catch (Exception se) {
            }
            con = null;
            stat = null;
            rs = null;
        }


        if (lists.size() > 0) {
            return lists;
        } else {
            return null;
        }
    }

    public boolean checkE_EXFRONTENDINTERFACE(String id, Connection con) {
        //Connection con = null;
        boolean result = false;
        Statement stat = null;
        ResultSet rs = null;
        try {
            //con = Env.getConnection4DSRDB();
            String query = "Select * from ecarddb.dbo.E_EXFRONTENDINTERFACE where ISSUER_BRANCH = '" + id + "'";
            System.out.println(query);
            stat = con.createStatement();
            rs = stat.executeQuery(query);
            while (rs.next()) {
                result = true;
            }


        } catch (Exception se) {
            se.printStackTrace();

        } finally {
            try {
                rs.close();
                stat.close();
                //con.close();
            } catch (Exception se) {
            }
            //con = null;
            stat = null;
            rs = null;
        }

        return result;

    }

    public Claim getClaimList(String id, Connection con) {
        //Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            //con = Env.getConnection4DSRDB();
            String query = "Select * from telcodb..claim where id = " + id + "";
            stat = con.createStatement();
            rs = stat.executeQuery(query);
            Claim list = null;
            while (rs.next()) {
                list = new Claim();
                list.setId(rs.getInt("id"));
                list.setAcquirer(rs.getString("acquirer"));
                list.setChannelType(rs.getString("channel_type"));
                list.setCardNo(rs.getString("card_no"));
                list.setTransactionAmount(rs.getDouble("transaction_amount"));
                list.setDateOfTransaction(rs.getTimestamp("date_of_transaction"));
                list.setClaimType(rs.getInt("claim_type"));
                list.setComment_Journal(rs.getString("comment_journal"));
                list.setCreatedDate(rs.getTimestamp("created_date"));
                list.setClaimStatus(rs.getString("claim_status"));
                list.setReasonForModify(rs.getString("reason_for_modify"));
                list.setClosedDate(rs.getDate("closed_date"));
                list.setClosedUser(rs.getString("closed_user"));
                list.setCreatedUser(rs.getString("created_user"));
                list.setTransactionStan(rs.getString("transaction_stan"));
                list.setTerminalId(rs.getString("terminal_id"));
                list.setComplainingBank(rs.getString("complaining_bank"));
                list.setRealCreatedDate(rs.getTimestamp("real_created_date"));
                list.setReversalTransactionId(rs.getString("reversal_transaction_id"));
                list.setDueDate(rs.getTimestamp("dueDate"));
                try {
                    list.setSysID(rs.getString("sysID"));
                } catch (Exception d) {
                }
                list.setLinkage(rs.getString("linkage"));
                list.setDownloadStatus(rs.getString("downloadStatus"));
                if (list.getDownloadStatus() == null || list.getDownloadStatus().isEmpty()) {
                    list.setDownloadStatus(null);
                }
                list.setOldStateValue(rs.getString("oldStateValue"));
                if (list.getOldStateValue() == null || list.getOldStateValue().isEmpty()) {
                    list.setOldStateValue(null);
                }
                list.setReOpneningReason(rs.getString("reOpeningReason"));
                if (list.getReOpneningReason() == null || list.getReOpneningReason().isEmpty()) {
                    list.setReOpneningReason(null);
                }
                list.setAttachFileURL(rs.getString("attachedFileName"));
                if (list.getAttachFileURL() == null || list.getAttachFileURL().isEmpty()) {
                    list.setAttachFileURL(null);
                }
                if (list.getLinkage() != null && list.getLinkage().trim().isEmpty() || (list.getLinkage() + "").trim().toUpperCase().equals("NULL")) {
                    list.setLinkage(null);
                }
                list.setDeclineReason(rs.getString("decline_reason"));
                if (list.getDeclineReason() != null && list.getDeclineReason().trim().isEmpty()) {
                    list.setDeclineReason(null);
                }
                if (list.getClaimStatus().equals("A") || list.getClaimStatus().equals("D")) {
                    if (list.getClosedDate() != null && !(new DateUtility().checkexpiration(list.getClosedDate(), new Date(), claimReopenLifeSpan))) {
                        list.setReOpenable(true);
                    }
                }
            }

            return list;


        } catch (Exception se) {
            se.printStackTrace();
            return null;
        } finally {
            try {
                rs.close();
                stat.close();
                //con.close();
            } catch (Exception se) {
            }
            //con = null;
            stat = null;
            rs = null;
        }


    }

    public Claim getClaimList2(String id, Connection con) {
        //Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            //con = Env.getConnection4DSRDB();
            String query = "Select * from telcodb..claim where id = " + id + "";
            stat = con.createStatement();
            rs = stat.executeQuery(query);
            Claim list = null;
            while (rs.next()) {
                list = new Claim();
                list.setId(rs.getInt("id"));
                list.setAcquirer(rs.getString("acquirer"));
                list.setChannelType(rs.getString("channel_type"));
                list.setCardNo(rs.getString("card_no"));
                list.setTransactionAmount(rs.getDouble("transaction_amount"));
                list.setDateOfTransaction(rs.getTimestamp("date_of_transaction"));
                list.setClaimType(rs.getInt("claim_type"));
                list.setComment_Journal(rs.getString("comment_journal"));
                list.setCreatedDate(rs.getTimestamp("created_date"));
                list.setClaimStatus(rs.getString("claim_status"));
                list.setReasonForModify(rs.getString("reason_for_modify"));
                list.setClosedDate(rs.getDate("closed_date"));
                list.setClosedUser(rs.getString("closed_user"));
                list.setCreatedUser(rs.getString("created_user"));
                list.setTransactionStan(rs.getString("transaction_stan"));
                list.setTerminalId(rs.getString("terminal_id"));
                list.setComplainingBank(rs.getString("complaining_bank"));
                list.setRealCreatedDate(rs.getTimestamp("real_created_date"));
                list.setReversalTransactionId(rs.getString("reversal_transaction_id"));
                list.setDueDate(rs.getTimestamp("dueDate"));
                try {
                    list.setSysID(rs.getString("sysID"));
                } catch (Exception d) {
                }
                list.setLinkage(rs.getString("linkage"));
                list.setDownloadStatus(rs.getString("downloadStatus"));
                if (list.getDownloadStatus() == null || list.getDownloadStatus().isEmpty()) {
                    list.setDownloadStatus(null);
                }
                list.setOldStateValue(rs.getString("oldStateValue"));
                if (list.getOldStateValue() == null || list.getOldStateValue().isEmpty()) {
                    list.setOldStateValue(null);
                }
                list.setReOpneningReason(rs.getString("reOpeningReason"));
                if (list.getReOpneningReason() == null || list.getReOpneningReason().isEmpty()) {
                    list.setReOpneningReason(null);
                }
                list.setAttachFileURL(rs.getString("attachedFileName"));
                if (list.getAttachFileURL() == null || list.getAttachFileURL().isEmpty()) {
                    list.setAttachFileURL(null);
                }
                if (list.getLinkage() != null && list.getLinkage().trim().isEmpty() || (list.getLinkage() + "").trim().toUpperCase().equals("NULL")) {
                    list.setLinkage(null);
                }
                list.setDeclineReason(rs.getString("decline_reason"));
                if (list.getDeclineReason() != null && list.getDeclineReason().trim().isEmpty()) {
                    list.setDeclineReason(null);
                }
                if (list.getClaimStatus().equals("A") || list.getClaimStatus().equals("D")) {
                    if (list.getClosedDate() != null && !(new DateUtility().checkexpiration(list.getClosedDate(), new Date(), claimReopenLifeSpan))) {
                        list.setReOpenable(true);
                    }
                }
            }

            return list;


        } catch (Exception se) {
            se.printStackTrace();
            return null;
        } finally {
            try {
                rs.close();
                stat.close();
                //con.close();
            } catch (Exception se) {
            }
            //con = null;
            stat = null;
            rs = null;
        }


    }

    public List<ViewMessage> getMessageList() {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
        DateUtility dateUtil = new DateUtility();
        Date startDate = this.getMessageSearchBean().getStartDate();
        Date endDate = this.getMessageSearchBean().getEndDate();
        String claimId = this.getMessageSearchBean().getClaimId();
        String subject = this.getMessageSearchBean().getSubject();
        String sendingBank = this.getMessageSearchBean().getSendingBank();
        String receivingBank = this.getMessageSearchBean().getReceivingBank();

        FacesContext facesContext = FacesContext.getCurrentInstance();
        String defaultToDayDate = "NO";
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        try {
            defaultToDayDate = ((String[]) request.getParameterMap().get("defaultDate"))[0];
            if (defaultToDayDate.toUpperCase().equals("YES")) {
                startDate = dateUtil.convertStringToDate(dateUtil.getDate());
                endDate = dateUtil.getTomorrow(startDate);
            }
        } catch (Exception s) {
        }


        List<ViewMessage> lists = new ArrayList<ViewMessage>();
        try {
            con = Env.getConnection4DRSDB();
            String query = "SELECT * FROM telcodb..cl_messages a,telcodb..claim b where b.id=convert(int, claimid)";
            if (claimId != null && !claimId.isEmpty()) {
                query = query + " and sysID='" + claimId + "'";
            }
            if (subject != null && !subject.isEmpty()) {
                query = query + " and subject='" + subject + "'";
            }
            if (sendingBank != null && !sendingBank.isEmpty()) {
                query = query + " and complaining_bank='" + sendingBank + "'";
            }
            if (receivingBank != null && !receivingBank.isEmpty()) {
                query = query + " and acquirer='" + receivingBank + "'";
            }
            if (startDate != null) {
                if (endDate == null) {
                    endDate = new Date();
                }
                String startDateString = dateUtil.formatDate(startDate);
                String endDateString = dateUtil.formatDate(endDate);
                query = query + " and dateCreated between '" + startDateString + "' and '" + endDateString + "'";
            }

            String searchype = null;

            try {
                searchype = ((String[]) request.getParameterMap().get("searchype"))[0];

            } catch (Exception s) {
            }

//if (searchype != null && !searchype.isEmpty() && searchype.equals("1"))
            {
                if (!user.getUser_code().equalsIgnoreCase("000")) {
                    query = query + " and ( complaining_bank = '" + user.getUser_code() + "' or acquirer = '" + user.getUser_code() + "' )";
                }
            }
            query = query + " order by dateCreated desc";
            stat = con.createStatement();
            rs = stat.executeQuery(query);
            Utility ut = new Utility();
            while (rs.next()) {
                ViewMessage list = new ViewMessage();
                list.setId(rs.getInt("ID"));
                list.setClaimID(rs.getString("claimid"));
                list.setSysID(rs.getString("sysID"));
                list.setSentDate(rs.getDate("dateCreated"));
                list.setSubject(rs.getString("subject"));
                Claim c = this.getClaimList(list.getClaimID(), con);
                list.setClaim(c);
                try {
                    list.setReceiving(ut.getBank(c.getAcquirer()).getBankname());
                } catch (Exception s) {

                    list.setReceiving(c.getAcquirer());

                }
                try {
                    list.setSendingBank(ut.getBank(c.getComplainingBank()).getBankname());
                } catch (Exception s) {
                    list.setSendingBank(c.getComplainingBank());

                }


                lists.add(list);
            }

        } catch (Exception se) {
            se.printStackTrace();
            return null;
        } finally {
            try {
                rs.close();
                stat.close();
                con.close();
            } catch (Exception se) {
            }
            con = null;
            stat = null;
            rs = null;
        }

        if (lists.size() > 0) {
            return lists;
        } else {
            return null;
        }
    }

    public List<Claim> getClaimList() {

        return this.claimList;
    }

    public List<Claim> getClaimList2() {

        return this.claimList;
    }

    public void setClaimList(List<Claim> claimList) {
        this.claimList = claimList;
    }

    public void listClaim() {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
        DateUtility dateUtil = new DateUtility();
        Date startDate = this.getSearchBean().getStartDate();
        Date endDate = this.getSearchBean().getEndDate();
        String acquirer = this.getSearchBean().getAcquirer();
        String status = this.getSearchBean().getStatus();
        String crdNo = this.getSearchBean().getCrdNo();
        String claimId = this.getSearchBean().getClaimId();
        String sender = this.getSearchBean().getSender();
        String channelType = this.getSearchBean().getChannelType();

        FacesContext facesContext = FacesContext.getCurrentInstance();
        String defaultToDayDate = "NO";
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        try {
            defaultToDayDate = ((String[]) request.getParameterMap().get("defaultDate"))[0];
            if (defaultToDayDate.toUpperCase().equals("YES")) {
                startDate = dateUtil.convertStringToDate(dateUtil.getDate());
                endDate = dateUtil.getTomorrow(startDate);
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime(endDate);
                gc.add(gc.DATE, 1);
                endDate = gc.getTime();

            }
        } catch (Exception s) {
        }
        String searchype = null;


        try {
            searchype = ((String[]) request.getParameterMap().get("searchype"))[0];

        } catch (Exception s) {
        }
        if (searchype == null || searchype.equals("null")) {
            searchype = this.getSearchype();
        } else {
            this.setSearchype(searchype);
        }
        List<Claim> lists = new ArrayList<Claim>();
        try {
            con = Env.getConnection4DRSDB();
            String query = "Select * from telcodb..claim where 1=1";
            if (acquirer != null && !acquirer.isEmpty()) {
                query = query + " and acquirer = '" + acquirer + "'";
            }
            if (status != null && !status.isEmpty()) {
                query = query + " and claim_status = '" + status + "'";
            }
            if (crdNo != null && !crdNo.isEmpty()) {
                query = query + " and transaction_stan = '" + crdNo + "'";
            }
            if (claimId != null && !claimId.isEmpty()) {
                query = query + " and SysID = '" + claimId + "'";
            }
            if (sender != null && !sender.isEmpty()) {
                if(sender.equalsIgnoreCase("000") && user.getUser_code().equals("000"))
                {

                } else
                {
                query = query + " and complaining_bank = '" + sender + "'";
                }
            }
            if (channelType != null && !channelType.isEmpty()) {
                query = query + " and channel_type = '" + channelType + "'";
            }
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
                if (!defaultToDayDate.equals("NOO")) {
                    query = query + " and created_date between '" + startDateString + "' and '" + endDateString + "'";
                } else {
                    this.getSearchBean().setStatus("O");
                    this.getSearchBean().setStartDate(null);
                    this.getSearchBean().setEndDate(null);
                }
            }
            if (searchype != null && !searchype.isEmpty() && searchype.equals("1")) {
                query = query + " and complaining_bank = '" + user.getUser_code() + "'";
            }

            if (searchype != null && !searchype.isEmpty() && searchype.equals("2")) {
                if (user.getUser_code() != null && !user.getUser_code().isEmpty() && user.getUser_code().equals("000")) {
                    if(this.getSearchBean().getSender()!=null && this.getSearchBean().getSender().equalsIgnoreCase("000") )
                    {
                     query = query + " and acquirer not in (" + new Utility().getBankListString() + ")";
                     
                    }else if(this.getSearchBean().getSender()!=null && !this.getSearchBean().getSender().equalsIgnoreCase("000") )
                    {
                    query = query + " and acquirer = '" + user.getUser_code() + "'";
                    }
                    
                }
 else
                {
                query = query + " and acquirer = '" + user.getUser_code() + "'";
 }
                if (status == null || status.isEmpty())
                    {
                      query = query + " and claim_status = 'O'";
                    }
                //query = query + " and claim_status = 'O'";

            }
            if (searchype != null && !searchype.isEmpty() && searchype.equals("4")) {
                query = query + " and (substring(downloadStatus,1,1) != 'D' or downloadstatus = null) and claim_status ='A'";
            }

            if (user.getUser_code() != null && !user.getUser_code().isEmpty() && !user.getUser_code().equals("000")) {
                query = query + " and ( complaining_bank = '" + user.getUser_code() + "' or acquirer = '" + user.getUser_code() + "' )";
            }


            query = query + " order by created_date desc";
            System.out.println(query);
            stat = con.createStatement();
            rs = stat.executeQuery(query);
            while (rs.next()) {
                Claim list = new Claim();
                list.setId(rs.getInt("id"));
                list.setAcquirer(rs.getString("acquirer"));
                list.setChannelType(rs.getString("channel_type"));
                list.setCardNo(rs.getString("card_no"));
                list.setTransactionAmount(rs.getDouble("transaction_amount"));
                list.setDateOfTransaction(rs.getTimestamp("date_of_transaction"));
                list.setClaimType(rs.getInt("claim_type"));
                list.setComment_Journal(rs.getString("comment_journal"));
                list.setCreatedDate(rs.getDate("created_date"));
                list.setClaimStatus(rs.getString("claim_status"));
                list.setReasonForModify(rs.getString("reason_for_modify"));
                list.setClosedDate(rs.getDate("closed_date"));
                list.setClosedUser(rs.getString("closed_user"));
                list.setDecodeClosedUser(new Utility().decodeUser(con, list.getClosedUser()));
                list.setCreatedUser(rs.getString("created_user"));
                list.setTransactionStan(rs.getString("transaction_stan"));
                list.setTerminalId(rs.getString("terminal_id"));
                list.setComplainingBank(rs.getString("complaining_bank"));
                list.setRealCreatedDate(rs.getTimestamp("real_created_date"));
                list.setReversalTransactionId(rs.getString("reversal_transaction_id"));
                list.setDueDate(rs.getTimestamp("dueDate"));
                try {
                    list.setSysID(rs.getString("sysID"));
                } catch (Exception d) {
                }
                list.setLinkage(rs.getString("linkage"));
                list.setDownloadStatus(rs.getString("downloadStatus"));
                if (list.getDownloadStatus() == null || list.getDownloadStatus().isEmpty()) {
                    list.setDownloadStatus(null);
                }
                list.setReOpneningReason(rs.getString("reOpeningReason"));
                if (list.getReOpneningReason() == null || list.getReOpneningReason().isEmpty()) {
                    list.setReOpneningReason(null);
                }
                list.setAttachFileURL(rs.getString("attachedFileName"));
                if (list.getAttachFileURL() == null || list.getAttachFileURL().isEmpty()) {
                    list.setAttachFileURL(null);
                }
                if (list.getLinkage() != null && list.getLinkage().trim().isEmpty()) {
                    list.setLinkage(null);
                }
                list.setOldStateValue(rs.getString("oldStateValue"));
                if (list.getOldStateValue() == null || list.getOldStateValue().isEmpty()) {
                    list.setOldStateValue(null);
                }
                list.setDeclineReason(rs.getString("decline_reason"));
                if (list.getDeclineReason() != null && list.getDeclineReason().trim().isEmpty() || (list.getDeclineReason() + "").trim().toUpperCase().equals("NULL")) {
                    list.setDeclineReason(null);
                }
                if (list.getClaimStatus().equals("A") || list.getClaimStatus().equals("D")) {
                    if (list.getClosedDate() != null && !(new DateUtility().checkexpiration(list.getClosedDate(), new Date(), claimReopenLifeSpan))) {
                        list.setReOpenable(true);
                    }
                }
                lists.add(list);
            }

        } catch (Exception se) {
            se.printStackTrace();
            //return null;
        } finally {
            try {
                rs.close();
                stat.close();
                con.close();
            } catch (Exception se) {
            }
            con = null;
            stat = null;
            rs = null;
        }

        if (lists.size() > 0) {
            this.setClaimList(lists);
        } else {
            this.setClaimList(null);
        }
    }

    public void getClaimById() {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        //this.closeClaim();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String claimID = "";
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        try {
            claimID = ((String[]) request.getParameterMap().get("claimID"))[0];

        } catch (Exception s) {
        }
        try {
            con = Env.getConnection4DRSDB();
            String query = "Select * from telcodb..claim where id = " + claimID + "";

            stat = con.createStatement();
            rs = stat.executeQuery(query);
            Claim list = null;
            while (rs.next()) {
                list = new Claim();
                list.setId(rs.getInt("id"));
                list.setAcquirer(rs.getString("acquirer"));
                list.setChannelType(rs.getString("channel_type"));
                list.setCardNo(rs.getString("card_no"));
                list.setTransactionAmount(rs.getDouble("transaction_amount"));
                list.setDateOfTransaction(rs.getDate("date_of_transaction"));
                list.setClaimType(rs.getInt("claim_type"));
                list.setComment_Journal(rs.getString("comment_journal"));
                list.setCreatedDate(rs.getDate("created_date"));
                list.setClaimStatus(rs.getString("claim_status"));
                list.setReasonForModify(rs.getString("reason_for_modify"));
                list.setClosedDate(rs.getDate("closed_date"));
                list.setClosedUser(rs.getString("closed_user"));
                list.setCreatedUser(rs.getString("created_user"));
                list.setTransactionStan(rs.getString("transaction_stan"));
                list.setTerminalId(rs.getString("terminal_id"));
                list.setComplainingBank(rs.getString("complaining_bank"));
                list.setDecodeClosedUser(new Utility().decodeUser(con, list.getClosedUser()));
                list.setRealCreatedDate(rs.getDate("real_created_date"));
                list.setReversalTransactionId(rs.getString("reversal_transaction_id"));
                list.setDueDate(rs.getDate("dueDate"));
                try {
                    list.setSysID(rs.getString("sysID"));
                } catch (Exception d) {
                }
                list.setLinkage(rs.getString("linkage"));
                list.setDownloadStatus(rs.getString("downloadStatus"));
                if (list.getDownloadStatus() == null || list.getDownloadStatus().isEmpty()) {
                    list.setDownloadStatus(null);
                }
                list.setReOpneningReason(rs.getString("reOpeningReason"));
                if (list.getReOpneningReason() == null || list.getReOpneningReason().isEmpty()) {
                    list.setReOpneningReason(null);
                }
                list.setAttachFileURL(rs.getString("attachedFileName"));
                if (list.getAttachFileURL() == null || list.getAttachFileURL().isEmpty()) {
                    list.setAttachFileURL(null);
                }
                if (list.getLinkage() == null || list.getLinkage().trim().isEmpty()) {
                    list.setLinkage(null);
                }
                list.setOldStateValue(rs.getString("oldStateValue"));
                if (list.getOldStateValue() == null || list.getOldStateValue().isEmpty()) {
                    list.setOldStateValue(null);
                }
                list.setDeclineReason(rs.getString("decline_reason"));
                if (list.getDeclineReason() == null || list.getDeclineReason().trim().isEmpty() || (list.getLinkage() + "").trim().toUpperCase().equals("NULL")) {
                    list.setDeclineReason(null);
                }
                list.setLinked(findLinged(claimID + ""));
                if (list.getClaimStatus().equals("A") || list.getClaimStatus().equals("D")) {
                    if (list.getClosedDate() != null && !(new DateUtility().checkexpiration(list.getClosedDate(), new Date(), claimReopenLifeSpan))) {
                        list.setReOpenable(true);
                    }
                }
                setClaim(list);
            }


        } catch (Exception se) {
            se.printStackTrace();

        } finally {
            try {
                rs.close();
                stat.close();
                con.close();
            } catch (Exception se) {
            }
            con = null;
            stat = null;
            rs = null;
        }


    }

    private List<Claim> getDueClaimList() {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        List<Claim> lists = new ArrayList<Claim>();
        try {
            con = Env.getConnection4DRSDB();
            String query = "Select * from telcodb..claim where 1=1";

            stat = con.createStatement();
            rs = stat.executeQuery(query);
            while (rs.next()) {
                Claim list = new Claim();
                list.setId(rs.getInt("id"));
                list.setAcquirer(rs.getString("acquirer"));
                list.setChannelType(rs.getString("channel_type"));
                list.setCardNo(rs.getString("card_no"));
                list.setTransactionAmount(rs.getDouble("transaction_amount"));
                list.setDateOfTransaction(rs.getDate("date_of_transaction"));
                list.setClaimType(rs.getInt("claim_type"));
                list.setComment_Journal(rs.getString("comment_journal"));
                list.setCreatedDate(rs.getDate("created_date"));
                list.setClaimStatus(rs.getString("claim_status"));
                list.setReasonForModify(rs.getString("reason_for_modify"));
                list.setClosedDate(rs.getDate("closed_date"));
                list.setClosedUser(rs.getString("closed_user"));
                list.setCreatedUser(rs.getString("created_user"));
                list.setTransactionStan(rs.getString("transaction_stan"));
                list.setTerminalId(rs.getString("terminal_id"));
                list.setComplainingBank(rs.getString("complaining_bank"));
                list.setRealCreatedDate(rs.getDate("real_created_date"));
                list.setReversalTransactionId(rs.getString("reversal_transaction_id"));
                list.setDueDate(rs.getDate("dueDate"));
                list.setLinkage(rs.getString("linkage"));
                list.setDeclineReason(rs.getString("decline_reason"));
                list.setDownloadStatus(rs.getString("downloadStatus"));
                if (list.getDownloadStatus() == null || list.getDownloadStatus().isEmpty()) {
                    list.setDownloadStatus(null);
                }
                list.setReOpneningReason(rs.getString("reOpeningReason"));
                if (list.getReOpneningReason() == null || list.getReOpneningReason().isEmpty()) {
                    list.setReOpneningReason(null);
                }
                list.setAttachFileURL(rs.getString("attachedFileName"));
                if (list.getAttachFileURL() == null || list.getAttachFileURL().isEmpty()) {
                    list.setAttachFileURL(null);
                }
                list.setOldStateValue(rs.getString("oldStateValue"));
                if (list.getOldStateValue() == null || list.getOldStateValue().isEmpty()) {
                    list.setOldStateValue(null);
                }
                if (list.getDeclineReason() == null || list.getDeclineReason().trim().isEmpty()) {
                    list.setDeclineReason(null);
                }
                if (list.getClaimStatus().equals("A") || list.getClaimStatus().equals("D")) {
                    if (list.getClosedDate() != null && !(new DateUtility().checkexpiration(list.getClosedDate(), new Date(), claimReopenLifeSpan))) {
                        list.setReOpenable(true);
                    }
                }
                lists.add(list);
            }

        } catch (Exception se) {
            se.printStackTrace();
            return null;
        } finally {
            try {
                rs.close();
                stat.close();
                con.close();
            } catch (Exception se) {
            }
            con = null;
            stat = null;
            rs = null;
        }

        if (lists.size() > 0) {
            return lists;
        } else {
            return null;
        }
    }

    public void setSelectedClaim(Claim c) {
        int id = c.getId();
        //this.closeClaim();
        c.setLinked(findLinged(id + ""));
        if (c.getLinkage() == null || c.getLinkage().trim().isEmpty() || (c.getLinkage() + "").trim().toUpperCase().equals("NULL")) {
            c.setLinkage(null);
        }
        if (c.getLinked() == null || c.getLinked().trim().isEmpty() || (c.getLinked() + "").trim().toUpperCase().equals("NULL")) {
            c.setLinked(null);
        }
        if (c.getReOpneningReason() == null || c.getReOpneningReason().trim().isEmpty() || (c.getReOpneningReason() + "").trim().toUpperCase().equals("NULL")) {
            c.setReOpneningReason(null);
        }
        if (c.getAttachFileURL() == null || c.getAttachFileURL().trim().isEmpty() || (c.getAttachFileURL() + "").trim().toUpperCase().equals("NULL")) {
            c.setAttachFileURL(null);
        }
        if (c.getClaimStatus().equals("A") || c.getClaimStatus().equals("D")) {
            if (c.getClosedDate() != null && !(new DateUtility().checkexpiration(c.getClosedDate(), new Date(), claimReopenLifeSpan))) {
                c.setReOpenable(true);
            }
        }
        if (c.getOldStateValue() == null || c.getOldStateValue().trim().isEmpty() || (c.getOldStateValue() + "").trim().toUpperCase().equals("NULL")) {
            c.setOldStateValue(null);
        }
        this.claim = c;

        //this.closeClaim();
//        Connection con = null;
//        Statement stat = null;
//        ResultSet rs = null;
//        try {
//            con = Env.getConnectionSupportLogDB();
//            String query = "Select * from telcodb..claim where id = " + id + "";
//            stat = con.createStatement();
//            rs = stat.executeQuery(query);
//
//            rs.next();
//            Claim list = new Claim();
//            list.setId(rs.getInt("id"));
//            list.setAcquirer(rs.getString("acquirer"));
//            list.setChannelType(rs.getString("channel_type"));
//            list.setCardNo(rs.getString("card_no"));
//            list.setTransactionAmount(rs.getDouble("transaction_amount"));
//            list.setDateOfTransaction(rs.getDate("date_of_transaction"));
//            list.setClaimType(rs.getInt("claim_type"));
//            list.setComment_Journal(rs.getString("comment_journal"));
//            list.setCreatedDate(rs.getDate("created_date"));
//            list.setClaimStatus(rs.getString("claim_status"));
//            list.setReasonForModify(rs.getString("reason_for_modify"));
//            list.setClosedDate(rs.getDate("closed_date"));
//            list.setClosedUser(rs.getString("closed_user"));
//            list.setCreatedUser(rs.getString("created_user"));
//            list.setTransactionStan(rs.getString("transaction_stan"));
//            list.setTerminalId(rs.getString("terminal_id"));
//            list.setComplainingBank(rs.getString("complaining_bank"));
//            list.setRealCreatedDate(rs.getDate("real_created_date"));//decline_reason= '"+claim.getDeclineReason()+"',
//            list.setDeclineReason(rs.getString("decline_reason"));
//            list.setDueDate(rs.getDate("dueDate"));
//            list.setLinkage(rs.getString("linkage"));
//            list.setDeclineReason(rs.getString("decline_reason"));
//            this.claim = list;


//        } catch (Exception se) {
//            se.printStackTrace();
//
//        } finally {
//            try {
//                rs.close();
//                stat.close();
//                con.close();
//            } catch (Exception se) {
//            }
//            con = null;
//            stat = null;
//            rs = null;
//        }


    }

//    public List<TransactionSearch> doTransactionSearch2(String s) {
//        Connection con = null;
//        Statement stat = null;
//        ResultSet rs = null;
//        List<TransactionSearch> lists = new ArrayList<TransactionSearch>();
//        try {
//            con = Env.getConnection4DRSDBMSQL();
//            String query = "Select * from transaction_search";
//            stat = con.createStatement();
//            rs = stat.executeQuery(query);
//
//            while (rs.next()) {
//                TransactionSearch list = new TransactionSearch();
//                list.setStan(rs.getString("stan"));
//                list.setTerminalId(rs.getString("terminal_id"));
//                list.setTransactionAmount(rs.getString("transaction_amount"));
//                list.setTransactionAmount(rs.getString("merchant_code"));
//                list.setTransactionNo(rs.getString("transaction_no"));
//                list.setTransactionDate(rs.getString("transaction_date"));
//                list.setTransactionDescription(rs.getString("transaction_description"));
//                list.setCardNo(rs.getString("card_no"));
//                list.setAcctNo(rs.getString("account_no"));
//                lists.add(list);
//            }
//
//        } catch (Exception se) {
//            se.printStackTrace();
//            return null;
//        } finally {
//            try {
//                rs.close();
//                stat.close();
//                con.close();
//            } catch (Exception se) {
//            }
//            con = null;
//            stat = null;
//            rs = null;
//        }
//
//        if (lists.size() > 0) {
//            return lists;
//        } else {
//            return null;
//        }
//    }
    public List<ClaimsSummary> getClaimsSummary() {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        //this.closeClaim();
        context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
        Utility ut = new Utility();
        List<ClaimsSummary> lists = new ArrayList<ClaimsSummary>();
        try {
            con = Env.getConnection4DRSDB();
            String query = "SELECT complaining_bank, count(complaining_bank) FROM telcodb..claim where 1=1 ";
            if (user.getUser_code() != null && !user.getUser_code().equals("000")) {
                query = query + " and acquirer = '" + user.getUser_code() + "'";
            }
            String searchype = null;
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            try {
                searchype = ((String[]) request.getParameterMap().get("searchype"))[0];

            } catch (Exception s) {
            }
            if (searchype != null && !searchype.isEmpty() && searchype.equals("1")) {
                if (user.getUser_code() != null && !user.getUser_code().equals("000")) {
                    query = query + " and acquirer = '" + user.getUser_code() + "'";
                }
            }
            query = query + " group by complaining_bank ";

            stat = con.createStatement();
            System.out.println(query);
            rs = stat.executeQuery(query);

            while (rs.next()) {
                ClaimsSummary list = new ClaimsSummary();
                try {
                    list.setFromBank(ut.getBank(rs.getString(1), con).getBankname());
                } catch (Exception x) {
                    list.setFromBank(rs.getString(1));
                }
                list.setNoOfClaim(rs.getString(2));
                list.setNoOfUnresolvedClaims(this.getNoOfUnresolvedClaims(rs.getString(1), con));

                lists.add(list);
            }

        } catch (Exception se) {
            se.printStackTrace();
            return null;
        } finally {
            try {
                rs.close();
                stat.close();
                con.close();
            } catch (Exception se) {
            }
            con = null;
            stat = null;
            rs = null;
        }

        if (lists.size() > 0) {
            return lists;
        } else {
            return null;
        }
    }

    public String getNoOfUnresolvedClaims(String bank, Connection con) {//complaining_bank
        Statement stat = null;
        ResultSet rs = null;
        context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
        Utility ut = new Utility();
        List<ClaimsSummary> lists = new ArrayList<ClaimsSummary>();
        try {
            String query = "SELECT count(complaining_bank) FROM telcodb..claim where claim_status  = 'O' ";
            if (user.getUser_code() != null && !user.getUser_code().equals("000")) {
                query = query + " and acquirer ='" + user.getUser_code() + "'";
            }
            query = query + "  and complaining_bank = '" + bank + "' group by complaining_bank";
            System.out.println(query);
            stat = con.createStatement();
            rs = stat.executeQuery(query);
            String res = "";
            while (rs.next()) {
                res = rs.getString(1);
            }
            return res;

        } catch (Exception se) {
            se.printStackTrace();
            return null;
        } finally {
            try {
                rs.close();
                stat.close();

            } catch (Exception se) {
            }
            stat = null;
            rs = null;
        }


    }

    public ClaimSearchBean getSearchBean() {
        if (searchBean == null) {
            searchBean = new ClaimSearchBean();
        }
        return searchBean;
    }

    public void setSearchBean(ClaimSearchBean searchBean) {
        this.searchBean = searchBean;
    }

    public MessageSearchBean getMessageSearchBean() {
        if (messageSearchBean == null) {
            messageSearchBean = new MessageSearchBean();
        }
        return messageSearchBean;
    }

    public void setMessageSearchBean(MessageSearchBean messageSearchBean) {
        this.messageSearchBean = messageSearchBean;
    }

    public Claim getClaim() {
        if (claim == null) {
            claim = new Claim();
        }

        if (claim.getClaimStatus() != null && (claim.getClaimStatus().equals("A") || claim.getClaimStatus().equals("D"))) {
            if (claim.getClosedDate() != null && !(new DateUtility().checkexpiration(claim.getClosedDate(), new Date(), claimReopenLifeSpan))) {
                claim.setReOpenable(true);
            }
        }

        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public void setSelectedSearchedTransaction(TransactionSearch transactionSearch) {
        //getClaim().setTransactionSearch(transactionSearch);
        getClaim().setTerminalId(transactionSearch.getTerminalId());
        getClaim().setTransactionStan(transactionSearch.getStan());
        getClaim().setType(transactionSearch.getType());
        //getClaim().setDateOfTransaction(new DateUtility().convertStringToDateyyyymmdd(transactionSearch.getTransactionDate()));
        getClaim().setRealCreatedDate(new DateUtility().convertStringToDateyyyymmdd(transactionSearch.getTransactionDate()));
        getClaim().setTranNo(transactionSearch.getTransactionNo());
        getClaim().setAccountNo(transactionSearch.getAcctNo());
        getClaim().setResponseCode(transactionSearch.getResponseCode());
        if (getClaim().getAcquirer() == null) {
            getClaim().setAcquirer(transactionSearch.getBank());
        }
        //if(getClaim().getCardNo()==null || getClaim().getCardNo().isEmpty())
        {
            getClaim().setCardNo(transactionSearch.getCardNo());
        }
        //this.set

    }

    public List<TransactionSearch> getSearchClaimTransaction() {
        List list = null;
        try {
            list = this.doTransactionSearch();

        } catch (Exception s) {
        }
        return list;
    }

    public void distroy() {

        this.claim = null;
        this.searchBean = new ClaimSearchBean();
        java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = df.parse(df.format(new Date()));
            this.searchBean.setEndDate(d);
            this.searchBean.setStartDate(d);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.claimList = null;
        //this.closeClaim();
        context = FacesContext.getCurrentInstance();
        UserContactController uc = ((UserContactController) context.getExternalContext().getSessionMap().get("usercontactcontroller"));
        if (uc != null) {
            uc.destroy();
        }

    }

    public void distroyAll() {

        this.claim = null;
        this.searchBean = new ClaimSearchBean();
        java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = df.parse(df.format(new Date()));
            this.searchBean.setEndDate(d);
            this.searchBean.setStartDate(d);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.claimList = null;



    }

    public void preViewClaim() {
    }

    public void backClaim() {
    }

    public void nextClaim() {
    }

    public void goToClaimEdit() {
    }

    public void viewClaimAction() {
    }

    public void sendMailAction() {
    }

    public void viewMessage() {
    }

    public void viewClaimInfo() {
    }

    public void claimAcceptAction() {

        Claim claim = this.claim;


        if (1 != 1) {
        } else {
            context = FacesContext.getCurrentInstance();
            User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
            String cUser = user.getUser_id();
            claim.setClaimStatus("A");
            try {
                Connection con = Env.getConnection4DRSDB();
                DateUtility dateUtil = new DateUtility();
                int output = -1;
                String message = "";

                try {
                    Statement stat = null;
                    stat = con.createStatement();
                    String queryUpdate = "UPDATE telcodb..claim SET  acquirer  = '" + claim.getAcquirer() + "',  channel_type  = '" + claim.getChannelType() + "', "
                            + " card_no  = '" + claim.getCardNo() + "', "
                            + "  claim_type  = '" + claim.getClaimType() + "', "
                            + " comment_journal  = '" + claim.getComment_Journal() + "', "
                            + " claim_status  = '" + claim.getClaimStatus() + "',  reason_for_modify  = '" + claim.getReasonForModify() + "',  closed_date  = getdate(), "
                            + " closed_user  = '" + cUser + "',  created_user  = '" + claim.getCreatedUser() + "', "
                            + " transaction_stan  = '" + claim.getTransactionStan() + "',  terminal_id  = '" + claim.getTerminalId() + "', "
                            + " complaining_bank  = '" + claim.getComplainingBank() + "',  reversal_transaction_id  = '" + claim.getReversalTransactionId() + "' WHERE id = " + claim.getId() + "  and claim_status  = 'O'";
                    output = stat.executeUpdate(queryUpdate);
                    if (output > 0) {
                        con.commit();
                        this.claim = null;
                        message = "Claim successfully Accepted";
                    } else {
                        con.rollback();
                        message = "Claim Accepted Failed";
                    }

                    facesMessages.add(Severity.INFO, message);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    try {
                        con.rollback();
                        message = "Error occured while creating user";
                    } catch (Exception e) {
                    }

                } finally {
                    try {
                        con.close();

                    } catch (Exception s) {
                    }
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }

        }

    }

    public void claimDeclinetAction() {
        Claim claim = this.claim;

        if (claim.getDeclineReason() == null || claim.getDeclineReason().isEmpty()) {
            facesMessages.add(Severity.INFO, "Error ! Please specify Decline Reason ");
        } else {
            context = FacesContext.getCurrentInstance();
            User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
            String cUser = user.getUser_id();
            claim.setClaimStatus("D");
            try {
                Connection con = Env.getConnection4DRSDB();
                DateUtility dateUtil = new DateUtility();
                int output = -1;
                String message = "";
                String bankUser = "John";
                String bank = "John";

                String decomment="";
                    try
                    {
                    decomment = claim.getDeclineReason().replaceAll("'", "");
                    }catch(Exception sd){}

                try {
                    Statement stat = null;
                    stat = con.createStatement();
                    String attacheFileName = "";
                    try {
                        attacheFileName = new java.io.File(this.claim.getAttachFileURL()).getName();
                    } catch (Exception f) {
                        attacheFileName = this.claim.getAttachFileURL();
                    }
                    String queryUpdate = "UPDATE telcodb..claim SET  acquirer  = '" + claim.getAcquirer() + "',  channel_type  = '" + claim.getChannelType() + "', "
                            + " card_no  = '" + claim.getCardNo() + "', "
                            + " claim_type  = '" + claim.getClaimType() + "', "
                            + " comment_journal  = '" + claim.getComment_Journal() + "', "
                            + " claim_status  = '" + claim.getClaimStatus() + "',  reason_for_modify  = '" + claim.getReasonForModify() + "',  closed_date  = getdate(), "
                            + " closed_user  = '" + cUser + "',  created_user  = '" + claim.getCreatedUser() + "', "
                            + " transaction_stan  = '" + claim.getTransactionStan() + "',  terminal_id  = '" + claim.getTerminalId() + "', "
                            + " complaining_bank  = '" + claim.getComplainingBank() + "', decline_reason= '" + decomment+ "',attachedFileName  = '" + attacheFileName + "',  reversal_transaction_id  = '" + claim.getReversalTransactionId() + "' WHERE id = " + claim.getId() + "  and claim_status  = 'O'";
                    output = stat.executeUpdate(queryUpdate);
                    if (output > 0) {
                        con.commit();
                        this.claim = null;
                        message = "Claim successfully Declined";
                    } else {
                        con.rollback();
                        message = "Claim Decline Action Failed";
                    }

                    facesMessages.add(Severity.INFO, message);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    try {
                        con.rollback();
                        message = "Error occured while creating user";
                    } catch (Exception e) {
                    }

                } finally {
                    try {
                        con.close();

                    } catch (Exception s) {
                    }
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String getSearchype() {
        return searchype;
    }

    public void setSearchype(String searchype) {
        this.searchype = searchype;
    }

    private String findLinged(String id) {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = Env.getConnection4DRSDB();
            String query = "Select id from telcodb..claim where linkage = '" + id + "'";

            stat = con.createStatement();
            rs = stat.executeQuery(query);
            String res = "";
            while (rs.next()) {
                res = rs.getString(1);
            }
            return res;

        } catch (Exception se) {
            se.printStackTrace();
            return null;
        } finally {
            try {
                rs.close();
                stat.close();
                con.close();
            } catch (Exception se) {
            }
            con = null;
            stat = null;
            rs = null;
        }


    }

    public void claimreOpeningAction() {
        //= claim;
        if (claim.getReOpneningReason() == null || claim.getReOpneningReason().isEmpty()) {
            facesMessages.add(Severity.INFO, "Error ! Please specify Re-opnening Reason ");
        } else {
            Claim claim = this.claim;
            try {
                DateUtility dateUtil = new DateUtility();
                int output = -1;
                Connection con = null;
                String message = "";

                context = FacesContext.getCurrentInstance();
                User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();

                String cUser = user.getUser_id();
                String bank = user.getUser_code();

                String dueDate = dateUtil.formatDate(dateUtil.calculateDueDate(new Date(), claim.getChannelType(), claim.getType()));
                String sysID = new Utility().getSeq();
                claim.setSysID(sysID);
                try {
                    Statement stat = null;
                    con = Env.getConnection4DRSDB();
                    stat = con.createStatement();
                    String comment="";
                    try
                    {
                    comment = claim.getComment_Journal().replaceAll("'", "");
                    }catch(Exception sd){}
                    
                    String query = "INSERT INTO telcodb..claim (acquirer, channel_type, card_no, transaction_amount, "
                            + "date_of_transaction, claim_type, comment_journal, created_date, claim_status, reason_for_modify, "
                            + " closed_user, created_user,complaining_bank,real_created_date,transaction_stan,terminal_id,linkage,duedate,sysID) "
                            + "VALUES ('" + claim.getAcquirer() + "','" + claim.getChannelType() + "','" + claim.getCardNo() + "'," + claim.getTransactionAmount() + ","
                            + "'" + dateUtil.formatDate(claim.getDateOfTransaction()) + "','" + claim.getClaimType() + "','" + comment + "',getdate(),'O','','','" + claim.getCreatedUser() + "','" + claim.getComplainingBank() + "','" + dateUtil.formatDate(claim.getRealCreatedDate()) + "',"
                            + "'" + claim.getTransactionStan() + "','" + claim.getTerminalId() + "','" + claim.getId() + "','" + dateUtil.formatDate(dateUtil.calculateDueDate(new Date(), claim.getChannelType(), claim.getType())) + "','" + sysID + "')";

                    String reopcomment="";
                    try
                    {
                    reopcomment = claim.getReOpneningReason().replaceAll("'", "");
                    }catch(Exception sd){}
                    String queryUpdate = "UPDATE telcodb..claim SET modifiyBy='" + cUser + "', reOpeningReason  = '" + reopcomment + "', oldStateValue='" + claim.getClaimStatus() + "', claim_status='RO', modifyDate= getdate() WHERE id = " + claim.getId() + " and claim_status in ('A','D')";
                    //output = stat.executeUpdate(queryUpdate);


                    int outPutUpdate = stat.executeUpdate(queryUpdate);
                    if (outPutUpdate > 0 || claim.getId() == null) {
                        output = stat.executeUpdate(query);
                    }
                    if (output > 0) {
                        con.commit();
                        String[] usersMail = new UserContactController().getUserEmailList(claim.getAcquirer() + "_000");
                        String emailMessage = "Dear Sir/Ma, \r\n\r\nA claim has been re-opened on dispute resolution "
                                + "system for your attention. \r\nFind below the details of the claim. "
                                + "\r\n\r\nRegards. \r\n\r\n Logged Bank: " + claim.getClBank().getBankname() + "\r\n "
                                + "Logged User: " + user.getFirstname() + "\r\n Logged Date: " + claim.getDateOfTransaction() + "\r\n "
                                + "Card/PAN/Phone No: " + claim.getCardNo() + " \r\n STAN: " + claim.getTransactionStan() + " \r\n Expiry Date: " + dueDate;
                        new Thread(new EmailClass(usersMail, "DRS Claim Re-opening Notification -" + sysID, emailMessage, "")).start();
                        message = "Claim successfully Re-opened";
                    } else {
                        con.rollback();
                        message = "Claim not Re-opened";
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    try {
                        con.rollback();
                        message = "Error occured while Re-opening Claim";
                    } catch (Exception e) {
                    }

                } finally {
                    try {
                        con.close();
                    } catch (Exception s) {
                    }
                    facesMessages.add(Severity.INFO, message);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }

    public void fileListener(UploadEvent event) {
        try {
            UploadItem item = event.getUploadItem();
            file.setData(item.getData());
            file.setLength(item.getData().length);
            file.setName(item.getFileName());
            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext sc = (ServletContext) context.getExternalContext().getContext();
            String c = sc.getRealPath("upload");
            int tmpIndex = c.lastIndexOf(java.io.File.separator + "tmp" + java.io.File.separator);
            if (tmpIndex > 0) {
                c = c.substring(0, tmpIndex) + java.io.File.separator + "UploadedClaimFile";
            }
            java.io.File f = new java.io.File(c);
            if (!f.exists()) {
                f.mkdirs();
            }

            java.io.File desfile = new java.io.File(c.toString() + "\\" + file.getName());
            file.setFileSavedDir(desfile.toString());
            OutputStream out = new FileOutputStream(desfile);
            out.write(item.getData());
            out.close();
            java.io.File rendesfile = new java.io.File(c.toString() + "\\" + new DateUtility().dateTimeStamp() + "_" + file.getName());
            desfile.renameTo(rendesfile);
            String fileSavedDir = rendesfile.toString();
            this.claim.setAttachFileURL(fileSavedDir);

        } catch (Exception sd) {
            sd.printStackTrace();
        }

    }

    public void fileListenerMsg(UploadEvent event) {
        try {
            UploadItem item = event.getUploadItem();
            file.setData(item.getData());
            file.setLength(item.getData().length);
            file.setName(item.getFileName());
            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext sc = (ServletContext) context.getExternalContext().getContext();
            String c = sc.getRealPath("upload");
            int tmpIndex = c.lastIndexOf(java.io.File.separator + "tmp" + java.io.File.separator);
            if (tmpIndex > 0) {
                c = c.substring(0, tmpIndex) + java.io.File.separator + "UploadedClaimFileMsg";
            }
            java.io.File f = new java.io.File(c);
            if (!f.exists()) {
                f.mkdirs();
            }

            java.io.File desfile = new java.io.File(c.toString() + "\\" + file.getName());
            file.setFileSavedDir(desfile.toString());
            OutputStream out = new FileOutputStream(desfile);
            out.write(item.getData());
            out.close();
            java.io.File rendesfile = new java.io.File(c.toString() + "\\" + new DateUtility().dateTimeStamp() + "_" + file.getName());
            desfile.renameTo(rendesfile);
            String fileSavedDir = rendesfile.toString();
            this.claim.setAttachFileURL(fileSavedDir);

        } catch (Exception sd) {
            sd.printStackTrace();
        }

    }

//    public String getReasonForDecline() {
//        return reasonForDecline;
//    }
//
//    public void setReasonForDecline(String reasonForDecline) {
//        this.reasonForDecline = reasonForDecline;
//    }
    public void donwloadAttachedFile() {

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext sc = (ServletContext) context.getExternalContext().getContext();
            String c = sc.getRealPath("upload");
            int tmpIndex = c.lastIndexOf(java.io.File.separator + "tmp" + java.io.File.separator);
            if (tmpIndex > 0) {
                c = c.substring(0, tmpIndex) + java.io.File.separator + "UploadedClaimFile";
            }
            java.io.File f = new java.io.File(c);
            if (!f.exists()) {
                f.mkdirs();
            }

            f = new java.io.File(c.toString() + "\\" + new java.io.File(this.claim.getAttachFileURL()).getName());

            InputStream is = new FileInputStream(f);
            long length = f.length();
            byte[] bytes = new byte[(int) length];
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file " + f.getName());
            }
            is.close();
            FileNameMap fileNameMap = URLConnection.getFileNameMap();
            String type = fileNameMap.getContentTypeFor(f.toString());
            //System.out.println("XXXXXXXXXXXXXX  "+type);

            context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.setHeader("Content-disposition",
                    "attachment; filename= " + f.getName());
            response.setContentLength(bytes.length);
            response.setContentType(type);
            try {

                response.getOutputStream().write(bytes);
                response.getOutputStream().flush();
                response.getOutputStream().close();
                context.responseComplete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception d) {
            d.printStackTrace();
        }
    }

    public void downloadClaim() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext sc = (ServletContext) context.getExternalContext().getContext();
            String c = sc.getRealPath("upload");
            int tmpIndex = c.lastIndexOf(java.io.File.separator + "tmp" + java.io.File.separator);
            if (tmpIndex > 0) {
                c = c.substring(0, tmpIndex) + java.io.File.separator + "DRS Downloads";
            }
            java.io.File f = new java.io.File(c);
            if (!f.exists()) {
                f.mkdirs();
            }


            java.io.File destFileName = new java.io.File(f.toString() + "\\DRS Download_" + new DateUtility().dateTimeStamp() + ".xls");

            WritableWorkbook workbook = Workbook.createWorkbook(destFileName);
            WritableSheet sheet = workbook.createSheet("DRS Download " + new DateUtility().dateTimeStamp(), 0);

            WritableFont times16font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD, true);
            WritableCellFormat times16format = new WritableCellFormat(times16font);

            Label calimID = new Label(0, 3, "Claim ID", times16format);
            sheet.addCell(calimID);
            Label IssuingBank = new Label(1, 3, "Issuing Bank", times16format);
            sheet.addCell(IssuingBank);
            Label sendingBank = new Label(2, 3, "Sending Bank", times16format);
            sheet.addCell(sendingBank);
            Label channelType = new Label(3, 3, "Channel Type", times16format);
            sheet.addCell(channelType);
            Label dispute = new Label(4, 3, "Dispute", times16format);
            sheet.addCell(dispute);
            Label cardsNumber = new Label(5, 3, "Cards Number", times16format);
            sheet.addCell(cardsNumber);
            Label amount = new Label(6, 3, "Amount", times16format);
            sheet.addCell(amount);
            Label dateLogged = new Label(7, 3, "Date Logged", times16format);
            sheet.addCell(dateLogged);
            Label dueDate = new Label(8, 3, "Due Date", times16format);
            sheet.addCell(dueDate);
            Label status = new Label(9, 3, "Status", times16format);
            sheet.addCell(status);
            Label STAN = new Label(10, 3, "Global ID", times16format);
            sheet.addCell(STAN);
            Label SysID = new Label(11, 3, "SysID", times16format);
            sheet.addCell(SysID);
            Label clusedUser = new Label(12, 3, "Accept/Decline", times16format);
            sheet.addCell(clusedUser);
            String inVlaue = "";
            for (int w = 0; w < this.claimList.size(); w++) {
                Claim claim = claimList.get(w);
                try {
                    if ((claim.getClaimStatus().equals("A") || claim.getClaimStatus().equals("D")) && (claim.getDownloadStatus() == null || !claim.getDownloadStatus().substring(0, 1).equals("D"))) {
                        calimID = new Label(0, 4 + w, claim.getId() + "");
                        sheet.addCell(calimID);
                        IssuingBank = new Label(1, 4 + w, claim.getClBank().getBankname());
                        sheet.addCell(IssuingBank);
                        sendingBank = new Label(2, 4 + w, claim.getDecodeSentBank());
                        sheet.addCell(sendingBank);
                        channelType = new Label(3, 4 + w, claim.getChannel().getChannelName());
                        sheet.addCell(channelType);
                        try {
                            dispute = new Label(4, 4 + w, claim.getClDispute().getDispute());
                            sheet.addCell(dispute);
                        } catch (Exception x) {
                            dispute = new Label(4, 4 + w, "Others");
                            sheet.addCell(dispute);
                        }
                        cardsNumber = new Label(5, 4 + w, claim.getCardNo());
                        sheet.addCell(cardsNumber);
                        amount = new Label(6, 4 + w, claim.getFormatAmount());
                        sheet.addCell(amount);
                        dateLogged = new Label(7, 4 + w, claim.getCreatedDate() + "");
                        sheet.addCell(dateLogged);
                        dueDate = new Label(8, 4 + w, claim.getDueDate() + "");
                        sheet.addCell(dueDate);
                        status = new Label(9, 4 + w, claim.getStatusDescription());
                        sheet.addCell(status);
                        STAN = new Label(10, 4 + w, claim.getTransactionStan());
                        clusedUser = new Label(12, 4 + w, claim.getDecodeClosedUser());
                        sheet.addCell(clusedUser);
                        sheet.addCell(STAN);
                        try {
                            SysID = new Label(11, 4 + w, claim.getSysID());
                            sheet.addCell(SysID);
                        } catch (Exception d) {
                        }
                        inVlaue = inVlaue + "," + claim.getId() + "";
                    }
                } catch (Exception d) {
                    d.printStackTrace();
                }


            }
            workbook.write();
            workbook.close();
            User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
            Statement stat = null;
            Connection con = Env.getConnection4DRSDB();
            stat = con.createStatement();
            try {
                inVlaue = inVlaue.trim().substring(1);
            } catch (Exception x) {
            }
            String queryUpdate = "UPDATE telcodb..claim SET downloadStatus  = 'D_" + user.getUser_id() + "_" + destFileName.getName() + "' WHERE id in (" + inVlaue + ")";
            int outPutUpdate = stat.executeUpdate(queryUpdate);
            con.commit();
            con.close();
            if (outPutUpdate > 0) {

                java.io.File f_ = new java.io.File(destFileName.toString());
                InputStream is = new FileInputStream(f_);
                long length = f_.length();
                byte[] bytes = new byte[(int) length];
                int offset = 0;
                int numRead = 0;
                while (offset < bytes.length
                        && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                    offset += numRead;
                }
                if (offset < bytes.length) {
                    throw new IOException("Could not completely read file " + f_.getName());
                }
                is.close();

                context = FacesContext.getCurrentInstance();
                HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                response.setHeader("Content-disposition",
                        "attachment; filename= " + f_.getName());
                response.setContentLength(bytes.length);
                response.setContentType("application/vnd.ms-excel");
                try {

                    response.getOutputStream().write(bytes);
                    response.getOutputStream().flush();
                    response.getOutputStream().close();
                    context.responseComplete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        } catch (Exception d) {
            d.printStackTrace();
        }
        this.claimList = null;
    }


    private boolean isSpecialType(Claim claim) {
       boolean logg = false;
       if(claim.getClaimType()==5 || claim.getClaimType()==7)
       {
        logg = true;
       }
       return logg;
    }

   

    private String getTranBank(Connection con, String bank) {
        String res = "000";
        try {
            String query = "SELECT issuer_code FROM ecarddb.dbo.E_TMCAQUIRER WHERE aqid = '" + bank + "'";
            System.out.println(query);
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(query);

            if (rs.next()) {
                res = rs.getString(1);
            }


        } catch (Exception se) {
            se.printStackTrace();

        }
        return res;
    }

    private boolean isValid(Date d) {
        boolean result =true;
        long dayDeff = (((new Date().getTime()/(1000*60*60*24))-(d.getTime()/(1000*60*60*24))));
        System.out.println(dayDeff);
        if(dayDeff>120)
        {
          result=false;
        }
        return result;
    }

     public static void main(String s[]) {
        com.etz.mobile.security.CardAudit cardAudit = new com.etz.mobile.security.CardAudit();//502192   6277490691450010500  62774900691450605238
        //String PAN = cardAudit.deduceTrack2FromCard("6277490", "0691450605236212");//5021920848960091312
        //String PAN = cardAudit.deduceTrack2FromCard("502192", "7338000036179541");
         ClaimController c = new ClaimController();

        System.out.println(c.getTrack2("7338000036150630").substring(6, 12));
    }

}
