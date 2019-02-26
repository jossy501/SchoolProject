/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etranzact.psm.controller;

import com.etranzact.drs.utility.DateUtility;
import com.etranzact.drs.utility.EmailClass;
import com.etranzact.drs.utility.Utility;
import com.etranzact.psm.dto.TPsmNetworkProvider;
import com.etranzact.psm.dto.TPsmStock;
import com.etranzact.supportmanager.dto.User;
import com.etranzact.supportmanager.utility.Env;
import com.etz.http.etc.PBEncryptor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 *
 * @author akachukwu.ntukokwu
 */
public class FileReader extends Thread {

    String fileType;
    User user;
    String clientIP;
    int pinLength;
    String network;
    int totalCount;
    String fileName;
    java.io.File inputfullFileName;
    double totalPinAmount;
    List<TPsmStock> list;
    List<ErrorBean> invalid;
    String systemBatchNumber;
    String batchNumber;
    double amount;

    @Override
    public void run() {
        try {
            this.reader(fileType, user, clientIP, pinLength, network, totalCount, fileName, inputfullFileName, totalPinAmount, list, invalid, systemBatchNumber, batchNumber, amount);
        } catch (Exception sd) {
            sd.printStackTrace();
        }
    }

    public FileReader(String fileType, User user, String clientIP, int pinLength, String network,
            int totalCount, String fileName, java.io.File inputfullFileName, double totalPinAmount,
            List<TPsmStock> list, List<ErrorBean> invalid, String systemBatchNumber, String batchNumber, double amount) {
        this.fileType = fileType;
        this.user = user;
        this.clientIP = clientIP;
        this.pinLength = pinLength;
        this.network = network;
        this.totalCount = totalCount;
        this.fileName = fileName;
        this.inputfullFileName = inputfullFileName;
        this.totalPinAmount = totalPinAmount;
        this.list = list;
        this.invalid = invalid;
        this.systemBatchNumber = systemBatchNumber;
        this.batchNumber = batchNumber;
        this.amount = amount;
    }

    public void reader(String fileType, User user, String clientIP, int pinLength, String network,
            int totalCount, String fileName, java.io.File inputfullFileName, double totalPinAmount,
            List<TPsmStock> list, List<ErrorBean> invalid, String systemBatchNumber, String batchNumber, double amount) {
        try {
            if (fileType.equals("000001"))//MTN
            {

                List<String> insertQueries = new ArrayList<String>();
                insertQueries = new MTNFileReader(user, user.getUser_code(), clientIP, user.getUser_id(), pinLength, network, totalCount, fileName, inputfullFileName, totalPinAmount, list, invalid, systemBatchNumber, batchNumber).readFile(user, user.getUser_code(), clientIP, user.getUser_id(), pinLength, network, totalCount, fileName, inputfullFileName, totalPinAmount, list, invalid, systemBatchNumber, batchNumber);



            } else if (fileType.equals("000002"))//Airtel
            {
                List<String> insertQueries = new ArrayList<String>();
                insertQueries = new AirtelFileReader(user, user.getUser_code(), clientIP, user.getUser_id(), pinLength, network, totalCount, fileName, inputfullFileName, totalPinAmount, list, invalid, systemBatchNumber, batchNumber).readFile(user, user.getUser_code(), clientIP, user.getUser_id(), pinLength, network, totalCount, fileName, inputfullFileName, totalPinAmount, list, invalid, systemBatchNumber, batchNumber);

            } else if (fileType.equals("000003"))//Etisalat 
            {
                List<String> insertQueries = new ArrayList<String>();
                insertQueries = new EtisalatReader(user, user.getUser_code(), clientIP, user.getUser_id(), pinLength, network, totalCount, fileName, inputfullFileName, totalPinAmount, list, invalid, systemBatchNumber, batchNumber).readFile(user, user.getUser_code(), clientIP, user.getUser_id(), pinLength, network, totalCount, fileName, inputfullFileName, totalPinAmount, list, invalid, systemBatchNumber, batchNumber);

            } else if (fileType.equals("000004"))//Glo
            {
                List<String> insertQueries = new ArrayList<String>();
                insertQueries = new GloReader(user, user.getUser_code(), clientIP, user.getUser_id(), pinLength, network, totalCount, fileName, inputfullFileName, totalPinAmount, list, invalid, systemBatchNumber, batchNumber).readFile(user, user.getUser_code(), clientIP, user.getUser_id(), pinLength, network, totalCount, fileName, inputfullFileName, totalPinAmount, list, invalid, systemBatchNumber, batchNumber);

            } else if (fileType.equals("000005"))//Starcoms
            {
                if (amount > 0) {
                    List<String> insertQueries = new ArrayList<String>();
                    insertQueries = new StarcomsReader(user, user.getUser_code(), clientIP, user.getUser_id(), pinLength, network, totalCount, fileName, inputfullFileName, totalPinAmount, list, invalid, systemBatchNumber, batchNumber).readFile(user, amount, user.getUser_code(), clientIP, user.getUser_id(), pinLength, network, totalCount, fileName, inputfullFileName, totalPinAmount, list, invalid, systemBatchNumber, batchNumber);

                } else {
                    //facesMessages.add(Severity.INFO, "Error! Amount must be greater than 0");
                }
            } else if (fileType.equals("000006"))//Visaphone
            {
                if (amount > 0) {
                    List<String> insertQueries = new ArrayList<String>();
                    insertQueries = new VisaphoneReader(user, user.getUser_code(), clientIP, user.getUser_id(), pinLength, network, totalCount, fileName, inputfullFileName, totalPinAmount, list, invalid, systemBatchNumber, batchNumber).readFile(amount, user.getUser_code(), clientIP, user.getUser_id(), pinLength, network, totalCount, fileName, inputfullFileName, totalPinAmount, list, invalid, systemBatchNumber, batchNumber);

                } else {
                    //facesMessages.add(Severity.INFO, "Error! Amount must be greater than 0");
                }
            } else {

                readXls(inputfullFileName, pinLength, network, totalCount, fileName, totalPinAmount, list, invalid, user, clientIP,batchNumber);
            }



        } catch (Exception e) {
            //facesMessages.add(Severity.INFO, "Error, Invalid encryption file/Key");
            e.printStackTrace();

        }
    }

    private void readXls(java.io.File inputfullFileName, int pinLength, String network, int totalCount, String fileName,
            double totalPinAmount, List<TPsmStock> list, List<ErrorBean> invalid, User user, String clientIP,String batchID) throws Exception {

        java.io.File inputWorkbook = inputfullFileName;
        Workbook w;

        w = Workbook.getWorkbook(inputWorkbook);
        Sheet sheet = w.getSheet(0);
        int t = 0;
        List<String> insertQueries = new ArrayList<String>();

        String query = "";


        DateUtility DT = new DateUtility();
        String systemBatchNumber = new Utility().generateRandomNumber(25);





        List<TPsmNetworkProvider> provider = new NetworkProviderController().getProviderList();
        List<ErrorBean> error = new ArrayList<ErrorBean>();
        Connection con = Env.getConnection4PSMRPDB();
        con.setAutoCommit(true);

        for (int j = 0; j < sheet.getRows(); j++) {
            if (j > 0) {
                t = t + 1;
                Cell[] cells = sheet.getRow(j);
                TPsmStock stock = new TPsmStock();
                stock.setId(t);
                stock.setBATCHID(batchID);
                stock.setSERIAL(cells[0].getContents());
                stock.setPIN_DENO(cells[1].getContents().replace(".00", "").replace(".0", ""));
                stock.setPIN_VALUE(cells[2].getContents());
                stock.setPIN(cells[3].getContents());
                stock.setPIN_EXPIRATION(DT.convertStringToDate(cells[4].getContents()));
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

                            if (!this.chekForDuplicateCard(con, stock.getSERIAL())) {
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
                                        + ", null, null, null, null, '4', '" + stock.getSERIAL() + "', '" + DT.formatDate(stock.getPIN_EXPIRATION()) + "', ' ', "
                                        + "'" + stock.getPIN_DENO() + "', null, null,'" + user.getUser_code() + "', null, '0', null, null,"
                                        + "'','','" + user.getUser_id() + "','" + clientIP + "',null,"
                                        + "'" + stock.getBATCHID() + "','" + systemBatchNumber + "','" + inputWorkbook.getName() + "',getDate(),'XXXXXXXXXXCCCCC','CCCCCVVVVVVVVVV')";
                                totalCount = totalCount + 1;
                                fileName = inputWorkbook.getName();
                                totalPinAmount = totalPinAmount + Integer.parseInt(stock.getPIN_VALUE());
                                list.add(stock);
                                insertQueries.add(query);
                                new TPSMController().createStock(con, query);
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
        //in.close();

    }

    public boolean chekForDuplicateCard(Connection con, String sERIAL) {
        boolean result = false;
        try {
            String query = "select SERIAL from rechargedb.dbo.R_PINS where SERIAL = '" + sERIAL + "'";
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(query);
            //System.out.println(query);
            if (rs.next()) {
                result = true;
            }
            query = "select SERIAL from rechargedb.dbo.R_PINS_BOUGHT where SERIAL = '" + sERIAL + "'";
            stat = con.createStatement();
            rs = stat.executeQuery(query);
            //System.out.println(query);
            if (rs.next()) {
                result = true;
            }

            query = "select SERIAL from rechargedb.dbo.R_PINS_TEMP where SERIAL = '" + sERIAL + "'";
            stat = con.createStatement();
            rs = stat.executeQuery(query);
            //System.out.println(query);
            if (rs.next()) {
                result = true;
            }

        } catch (Exception x) {
            result = true;
            x.printStackTrace();

        } finally {
        }


        return result;

    }
}
