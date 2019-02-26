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
import com.etranzact.supportmanager.action.AuthenticationAction;
import com.etranzact.supportmanager.dto.User;
import com.etranzact.supportmanager.utility.Env;
import com.etz.http.etc.PBEncryptor;
import com.java.descrypt.FileDesEncryptImpl;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
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
 * @author akachukwu.ntukokwu
 */
@Scope(ScopeType.SESSION)
@Name("fileUploadBean")
public class FileUploadBean {

    private ArrayList<File> files = new ArrayList<File>();
    private int uploadsAvailable = 1;
    private List<String> insertQuery;
    private boolean autoUpload = false;
    static FacesContext context;
    private boolean useFlash = false;
    private String key;
    private List<ErrorBean> invalid;
    List<TPsmStock> list;
    String pinLength_Network;
    String fileSavedDir;
    @In
    FacesMessages facesMessages;
    int pinLength = 0;
    String network = "";
    double totalPinAmount = 0;
    int totalCount = 0;
    String clientIP;
    String fileName;
    String systemBatchNumber;
    File file = new File();
    List storedPin;
    String stBatchId = "";
    List uploadedPins;
    double amount;

    public int getSize() {
        if (getFiles().size() > 0) {
            return getFiles().size();
        } else {
            return 0;
        }
    }
    boolean rendered;

    public FileUploadBean() {
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public void paint(OutputStream stream, Object object) throws IOException {
        stream.write(getFiles().get((Integer) object).getData());
    }

    public void returns() {
        this.uploadedPins = null;
        this.invalid = null;

        this.distroy();
    }

    public void listener(UploadEvent event) throws Exception {
        UploadItem item = event.getUploadItem();
        file.setData(item.getData());
        file.setLength(item.getData().length);
        file.setName(item.getFileName());
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();
        String c = sc.getRealPath("upload");

        int tmpIndex = c.lastIndexOf(java.io.File.separator + "tmp" + java.io.File.separator);
        if (tmpIndex > 0) {
            c = c.substring(0, tmpIndex) + java.io.File.separator + "PINUploadedFile";
        }
        java.io.File f = new java.io.File(c);
        if (!f.exists()) {
            f.mkdirs();
        }

        java.io.File desfile = new java.io.File(c.toString() + "\\" + new java.io.File(file.getName()).getName());


        fileSavedDir = desfile.toString();
        file.setFileSavedDir(desfile.toString());
        OutputStream out = new FileOutputStream(desfile);
        out.write(item.getData());
        out.close();
        files.add(file);

        uploadsAvailable--;
    }

    public String clearUploadData() {
        files.clear();
        setUploadsAvailable(5);
        return null;
    }

    public long getTimeStamp() {
        return System.currentTimeMillis();
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }

    public int getUploadsAvailable() {
        return uploadsAvailable;
    }

    public void setUploadsAvailable(int uploadsAvailable) {
        this.uploadsAvailable = uploadsAvailable;
    }

    public boolean isAutoUpload() {
        return autoUpload;
    }

    public void setAutoUpload(boolean autoUpload) {
        this.autoUpload = autoUpload;
    }

    public boolean isUseFlash() {
        return useFlash;
    }

    public void setUseFlash(boolean useFlash) {
        this.useFlash = useFlash;
    }

    public List<String> getInsertQuery() {
        return insertQuery;
    }

    public void setInsertQuery(List<String> insertQuery) {
        this.insertQuery = insertQuery;
    }

    public List<TPsmStock> getUploadedPins() {
        return uploadedPins;
    }

    public void setUploadedPins(List uploadedPins) {
        this.uploadedPins = uploadedPins;
    }

    public void processPinFile() {


        String fileType = "";
        systemBatchNumber = new Utility().generateRandomNumber(25);
        String batchNumber = new Utility().generateRandomNumber(25);
        stBatchId = " batch Id: " + batchNumber;
        //storedPin = new TPSMController().getAllPins();
        context = FacesContext.getCurrentInstance();
        list = new ArrayList<TPsmStock>();
        invalid = new ArrayList<ErrorBean>();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
        clientIP = ((HttpServletRequest) context.getExternalContext().getRequest()).getRemoteAddr();
        double amount = this.getAmount();

        if (file.getFileSavedDir() != null) {

            try {
                pinLength = Integer.parseInt(pinLength_Network.split("_")[0]);
            } catch (Exception x) {
            }
            try {
                network = pinLength_Network.split("_")[1];
            } catch (Exception x) {
            }
            try {
                fileType = pinLength_Network.split("_")[2];
            } catch (Exception x) {
            }
            try {

                if (this.key != null && this.key.trim().length() < 1) {
                    this.key = null;
                }
                java.io.File inputfullFileName = new FileDesEncryptImpl().decrypt(new java.io.File(file.getFileSavedDir()), this.key);
                new Thread(new FileReader(fileType, user, clientIP, pinLength, network, totalCount,
                        fileName, inputfullFileName, totalPinAmount, list, invalid, systemBatchNumber, batchNumber, amount)).start();
                facesMessages.add(Severity.INFO, "File successfully moved to the server and been processed. A mail will be sent to you when this process is completed or you check back with this batch id: "+batchNumber);

                file.setFileSavedDir(null);
                fileName=null;
                invalid=null;

            } catch (Exception e) {
                facesMessages.add(Severity.INFO, "Error, Invalid encryption file/Key");
                e.printStackTrace();

            }

        }
    }

    private void readXls() throws Exception {

        java.io.File inputWorkbook = new FileDesEncryptImpl().decrypt(new java.io.File(file.getFileSavedDir()), this.key);
        Workbook w;

        w = Workbook.getWorkbook(inputWorkbook);
        Sheet sheet = w.getSheet(0);
        int t = 0;
        List<String> insertQueries = new ArrayList<String>();

        String query = "";
        context = FacesContext.getCurrentInstance();

        DateUtility DT = new DateUtility();
        systemBatchNumber = new Utility().generateRandomNumber(25);
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();




        try {
            pinLength = Integer.parseInt(pinLength_Network.split("_")[0]);
        } catch (Exception x) {
        }
        try {
            network = pinLength_Network.split("_")[1];
        } catch (Exception x) {
        }

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
                stock.setBATCHID(cells[0].getContents());
                stock.setSERIAL(cells[1].getContents());
                stock.setPIN_DENO(cells[2].getContents());                
                stock.setPIN_VALUE(cells[3].getContents());
                stock.setPIN(cells[4].getContents());
                stock.setPIN_EXPIRATION(DT.convertStringToDate(cells[5].getContents()));
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
                                        + "trans_soc, MERCHANT_CODE, TARGET_PHONE, CLOSED, MODIFIED, UNIQUE_TRANSID,"
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
                                storedPin.add(stock.getPIN());

                                insertQueries.add(query);
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
        setInvalid(error);
        this.invalid = error;
        this.setInsertQuery(insertQueries);
    }

    public void distroy() {
        this.fileSavedDir = null;
        this.list = null;
        this.rendered = false;
        this.list = null;
        this.invalid = null;
        this.key = null;
        stBatchId = "";
    }

    public void insertData() {
        //context = FacesContext.getCurrentInstance();
        //TPSMController SC = ((TPSMController) context.getExternalContext().getSessionMap().get("tpsmcontroller"));
        if (this.getInsertQuery() != null && this.getInsertQuery().size() > 0) {
            String msg = new TPSMController().createStock(this.getInsertQuery(), this.list.size(), this.invalid.size());
            context = FacesContext.getCurrentInstance();
            User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
            String msgs = "";
            msgs = "Dear Sir/Ma \r\nThis is to notify you of the pin upload activity on ETZ Pin Manager portal. Find below the details of the upload"
                    + "\r\nBatch Total Count: " + totalCount
                    + "\r\nBatch Total Amount: " + totalPinAmount
                    + "\r\nUploader: " + user.getFirstname() + "  " + user.getLastname()
                    + "\r\nUploader System IP Address: " + clientIP
                    + "\r\nUploader System Batch ID: " + systemBatchNumber
                    + "\r\nUploader File Name: " + fileName;

            //if(totalCount>0)
            {
                new Thread(new EmailClass(new UserContactController().getUserEmailList("000_" + user.getUser_code()), "PIN Upload Alert", msgs, "admin@etranzact.net")).start();
            }
            if (!msg.equals("Stock successfully created")) {
                stBatchId = "";
            }
            facesMessages.add(Severity.INFO, msg + stBatchId);
        }
        this.list = null;
        this.invalid = null;
        this.key = null;
        this.setUploadedPins(null);
        stBatchId = "";


    }

    public List<ErrorBean> getInvalid() {
        return invalid;
    }

    public void setInvalid(List<ErrorBean> invalid) {
        this.invalid = invalid;
    }

    public void processUpload() {
        processPinFile();


    }

    public String getPinLength() {
        return pinLength_Network;
    }

    public void setPinLength(String pinLength) {
        this.pinLength_Network = pinLength;
    }

    public boolean chekForDuplicateCard(Connection con, String sERIAL) {
        boolean result = false;
        Statement stat=null;
        ResultSet rs=null;
        try {
            String query = "select SERIAL from rechargedb.dbo.R_PINS where SERIAL = '" + sERIAL + "'";
            stat = con.createStatement();
            rs = stat.executeQuery(query);
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
            try{
            rs.close();
            stat.close();
            }catch(Exception fg){}
        }


        return result;

    }

    public static void main(String h[]) {
//        System.out.println(new E_SECURE().f_decode("6yPbrgueR8tqoBg8SpQ9sg==", "123234567"));//6yPbrgueR8tqoBg8SpQ9sg==
    }
}


/*
 *
 */
