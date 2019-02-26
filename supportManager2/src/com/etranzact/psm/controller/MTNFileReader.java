/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etranzact.psm.controller;

import com.etranzact.drs.utility.DateUtility;
import com.etranzact.drs.utility.EmailClass;
import com.etranzact.psm.dto.TPsmStock;
import com.etranzact.supportmanager.dto.User;
import com.etranzact.supportmanager.utility.Env;
import com.etz.http.etc.PBEncryptor;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author akachukwu.ntukokwu
 */
class MTNFileReader {

    int totalCount;
    String fileName;
    double totalPinAmount;
    java.io.File inputfullFileName;
    List<TPsmStock> list;
    String systemBatchNumber;
    String batchNumber;
    String network;
    int pinLength;
    List<ErrorBean> invalid;
    List storedPin;
    String user_code;
    String clientIP;
    String user_id;
    User user;

    public MTNFileReader(User user, String user_code, String clientIP, String user_id, int pinLength, String network, int totalCount, String fileName, java.io.File inputfullFileName, double totalPinAmount, List<TPsmStock> list, List<ErrorBean> invalid, String systemBatchNumber, String batchNumber) {
        this.totalCount = totalCount;
        this.fileName = fileName;
        this.totalPinAmount = totalPinAmount;
        this.list = list;
        this.network = network;
        this.invalid = invalid;
        this.pinLength = pinLength;
        this.inputfullFileName = inputfullFileName;
        //this.storedPin = storedPin;
        this.user_code = user_code;
        this.clientIP = clientIP;
        this.user_id = user_id;
        this.user = user;
    }

    public List<String> readFile(User user, String user_code, String clientIP, String user_id, int pinLength, String network,
            int totalCount, String fileName, java.io.File inputfullFileName, double totalPinAmount, List<TPsmStock> list, List<ErrorBean> invalid,
            String systemBatchNumber, String batchNumber) throws Exception {
        List<String> rs = new ArrayList<String>();
        List<ErrorBean> error = invalid;

        FileInputStream fstream = new FileInputStream(inputfullFileName);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
       Connection con = Env.getConnection4PSMRPDB();
        con.setAutoCommit(true);
        String strLine;
        int t = 0;
        String query = "";
        while ((strLine = br.readLine()) != null) {
            String sn = "";
            String pin = "";
            String amount = "";
            t = t + 1;
            try {
                sn = strLine.trim().substring(0, 16);
                Double.parseDouble(sn);
            } catch (Exception sd) {
                ErrorBean errorBean = new ErrorBean();
                errorBean.setErrorDesc("Invalid Colunm for Pin Serial Number " + sn);
                errorBean.setLine(t + "");
                error.add(errorBean);
            }
            try {
                pin = strLine.trim().substring(16, 28);
                Double.parseDouble(pin);
            } catch (Exception sd) {
                ErrorBean errorBean = new ErrorBean();
                errorBean.setErrorDesc("Invalid Colunm for Pin Number " + pin);
                errorBean.setLine(t + "");
                error.add(errorBean);
            }
            try {
                amount = strLine.trim().substring(28, strLine.trim().length() - 2);

                double d = Double.parseDouble(amount);
                amount = d + "";
            } catch (Exception sd) {
                ErrorBean errorBean = new ErrorBean();
                errorBean.setErrorDesc("Invalid Colunm for Pin amount");
                errorBean.setLine(t + "");
                error.add(errorBean);
            }


            {

                TPsmStock stock = new TPsmStock();
                stock.setId(t);
                stock.setBATCHID(batchNumber);
                stock.setSERIAL(sn);
               stock.setPIN_DENO(amount.replace(".00", "").replace(".0", ""));
                stock.setPIN_VALUE(amount);
                stock.setPIN(pin);
                stock.setPIN_EXPIRATION(new DateUtility().convertStringToDate(new DateUtility().getDate()));
                stock.setPROVIDER_ID(network);

                if (stock.getBATCHID() == null || stock.getBATCHID().trim().length() < 1) {
                    ErrorBean errorBean = new ErrorBean();
                    errorBean.setErrorDesc("Pin Batch Number is empty");
                    errorBean.setLine(t + "");
                    error.add(errorBean);

                } else if (stock.getSERIAL() == null || stock.getSERIAL().trim().length() < 1) {
                    ErrorBean errorBean = new ErrorBean();
                    errorBean.setErrorDesc("Pin Serial Number is empty");
                    errorBean.setLine(t + "");
                    error.add(errorBean);

                } else if (stock.getPIN_DENO() == null || stock.getPIN_DENO().trim().length() < 1) {
                    ErrorBean errorBean = new ErrorBean();
                    errorBean.setErrorDesc("Pin Deno is empty");
                    errorBean.setLine(t + "");
                    error.add(errorBean);

                } else if (stock.getPIN_VALUE() == null || stock.getPIN_VALUE().trim().length() < 1) {
                    ErrorBean errorBean = new ErrorBean();
                    errorBean.setErrorDesc("Pin Value is empty");
                    errorBean.setLine(t + "");
                    error.add(errorBean);

                } else if (stock.getPIN() == null || stock.getPIN().trim().length() < 1) {
                    ErrorBean errorBean = new ErrorBean();
                    errorBean.setErrorDesc("Pin Number is empty");
                    errorBean.setLine(t + "");
                    error.add(errorBean);

                } else if (stock.getPROVIDER_ID() == null || stock.getPROVIDER_ID().trim().length() < 1) {
                    ErrorBean errorBean = new ErrorBean();
                    errorBean.setErrorDesc("Network Provider is empty");
                    errorBean.setLine(t + "");
                    error.add(errorBean);

                } else {

                    boolean valid = false;
                    try {

                        valid = true;
                    } catch (Exception d) {
                        ErrorBean errorBean = new ErrorBean();
                        errorBean.setErrorDesc("Invalild Pin Value");
                        errorBean.setLine(t + "");
                        error.add(errorBean);

                    }
                    if (valid) {
                        if (pinLength == stock.getPIN().length()) {
                            //if (!storedPin.contains(new PBEncryptor().PBEncrypt(stock.getPIN(), stock.getSERIAL().trim())))

                            if (!new FileUploadBean().chekForDuplicateCard(con, stock.getSERIAL())) {
//                                    query = "INSERT INTO rechargedb.dbo.R_PINS (dealer_id, pin_value, pin_amount, pin_serial_number, "
//                                            + "pin_number, pin_batch_number, pin_system_batch_number, pin_sequence_number, created_date, "
//                                            + "created_user, pin_status,provider_id,request_session_key) "
//                                            + "VALUES ( '" + user.getUser_code() + "', " + stock.getPinValue() + ", " + stock.getPinAmount() + ", '" + stock.getPinSerialNumber() + "',"
//                                            + " '" + stock.getPinNumber() + "', '" + stock.getPinBatchNumber() + "', '" + stock.getPinSystemBatchNumber() + "', "
//                                            + "'" + stock.getPinId() + "',getdate(), '" + user.getUser_id() + "', 'A','" + stock.getProviderId() + "','')";

                                query = "INSERT INTO rechargedb.dbo.R_PINS_TEMP"
                                        + "(PROVIDER_ID, USERNAME, DISCOUNT, PIN, PIN_VALUE, PIN_STATUS, "
                                        + "PIN_USER, PIN_ISSUED, BATCHID, VALID_DAY, SERIAL, PIN_EXPIRATION, ORDER_NO, PIN_DENO, SUBATCHID, "
                                        + "trans_soc, issuer, TARGET_PHONE, CLOSED, MODIFIED, UNIQUE_TRANSID,"
                                        + "first_approval,second_approval,uploader,uploader_IP,locked_Time,"
                                        + "upload_batchId,SystemAsignedBatchID,uploadFile,upload_date,validCount,invalidCount) VALUES "
                                        + "('" + stock.getPROVIDER_ID() + "', null, null, '" + new PBEncryptor().PBEncrypt(stock.getPIN().trim(), stock.getSERIAL().trim()) + "', " + stock.getPIN_VALUE() + ""
                                        + ", null, null, null, null, '4', '" + stock.getSERIAL() + "', '" + new DateUtility().formatDate(stock.getPIN_EXPIRATION()) + "', ' ', "
                                        + "'" + stock.getPIN_DENO() + "', null, null,'" + user_code + "', null, '0', null, null,"
                                        + "'','','" + user_id + "','" + clientIP + "',null,"
                                        + "'" + stock.getBATCHID() + "','" + systemBatchNumber + "','" + inputfullFileName.getName() + "',getDate(),'XXXXXXXXXXCCCCC','CCCCCVVVVVVVVVV')";
                                totalCount = totalCount + 1;
                                fileName = inputfullFileName.getName();
                                totalPinAmount = totalPinAmount + Double.parseDouble(stock.getPIN_VALUE());
                                list.add(stock);
                                new TPSMController().createStock(con, query);
                                //storedPin.add(stock.getPIN());

                                rs.add(query);
                            } else {
                                ErrorBean errorBean = new ErrorBean();
                                errorBean.setErrorDesc("Duplicate reference number found");
                                errorBean.setLine(t + "");
                                error.add(errorBean);
                            }
                        } else {
                            ErrorBean errorBean = new ErrorBean();
                            errorBean.setErrorDesc("Invalid Pin Length");
                            errorBean.setLine(t + "");
                            error.add(errorBean);

                        }
                    } else {
                    }
                }
            }
        }
        //con.commit();
        con.close();
        new TPSMController().createStockError(error, batchNumber, systemBatchNumber);
        String msgs = "";
        msgs = "Dear Sir/Ma \r\nThis is to notify you of the pin upload activity on ETZ Pin Manager portal. Find below the details of the upload"
                + "\r\nBatch Total Count: " + totalCount
                + "\r\nBatch Total Amount: " + totalPinAmount
                + "\r\nUploader: " + user.getFirstname() + "  " + user.getLastname()
                + "\r\nUploader System IP Address: " + clientIP
                + "\r\nUploader Batch ID: " + batchNumber
                + "\r\nUploader File Name: " + fileName;
        String[] emails = null;
        try {
            emails = new UserContactController().getUserEmailList("000_" + user.getUser_code());
            emails[emails.length] = user.getEmail();
        } catch (Exception df) {
            emails = new String[1];
            emails[0] = user.getEmail();
        }
        //if(totalCount>0)
        {
            new Thread(new EmailClass(emails, "PIN Upload Alert", msgs, "admin@etranzact.net")).start();
        }
        in.close();



        return rs;
    }

    public static void main(String d[]) {
        try {
            FileInputStream fstream = new FileInputStream("C:\\Users\\akachukwu.ntukokwu\\Downloads\\sample%20pins\\sample pins\\mtn sample.txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                String sn = "";
                String pin = "";
                String amount = "";
                try {
                    sn = strLine.trim().substring(0, 16);
                } catch (Exception sd) {
                }
                try {
                    pin = strLine.trim().substring(16, 28);
                } catch (Exception sd) {
                }
                try {
                    amount = strLine.trim().substring(28, strLine.trim().length() - 2);
                } catch (Exception sd) {
                }


                System.out.println(sn);
                System.out.println(pin);
                System.out.println(amount);

            }
            in.close();
        } catch (Exception sd) {
        }
    }
}
