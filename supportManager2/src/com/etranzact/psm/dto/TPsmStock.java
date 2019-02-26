/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etranzact.psm.dto;

import com.etranzact.drs.utility.Utility;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author akachukwu.ntukokwu
 */
@Entity
@Table(name = "T_PSM_STOCK")
@NamedQueries({
    @NamedQuery(name = "TPsmStock.findAll", query = "SELECT t FROM TPsmStock t")})
public class TPsmStock implements Serializable {

    private static final long serialVersionUID = 1L;
    int id;
    String PROVIDER_ID;
    String USERNAME;
    String DISCOUNT;
    String PIN;
    String PIN_VALUE;
    String PIN_STATUS;
    String PIN_USER;
    String PIN_USED;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date PIN_ISSUED;
    String BATCHID;
    String VALID_DAY;
    String SERIAL;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date PIN_EXPIRATION;
    String ORDER_NO;
    String PIN_DENO;
    String SUBATCHID;
    String trans_soc;
    String MERCHANT_CODE;
    String TARGET_PHONE;
    String SystemAsignedBatchID;
    String uploadFile;
    String CLOSED;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date MODIFIED;
    String UNIQUE_TRANSID;
    String ISSUER;
    private TPsmDealer dealer;
    private String decodeCreatedUser;
    private String formatedPinValue;
    private String formatedPinDeno;
    private TPsmNetworkProvider provider;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date upload_date;
    private String first_approval;
    private String second_approval;
    private String uploader;
    private String uploaderName;
    private String locked_Time;
    private String upload_batchId;
    private String validCount;
    private String invalidCount;

    public String getInvalidCount() {
        return invalidCount;
    }

    public void setInvalidCount(String invalidCount) {
        this.invalidCount = invalidCount;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public String getValidCount() {
        return validCount;
    }

    public void setValidCount(String validCount) {
        this.validCount = validCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSystemAsignedBatchID() {
        return SystemAsignedBatchID;
    }

    public String getFirst_approval() {
        return first_approval;
    }

    public void setFirst_approval(String first_approval) {
        this.first_approval = first_approval;
    }

    public String getLocked_Time() {
        return locked_Time;
    }

    public void setLocked_Time(String locked_Time) {
        this.locked_Time = locked_Time;
    }

    public String getSecond_approval() {
        return second_approval;
    }

    public void setSecond_approval(String second_approval) {
        this.second_approval = second_approval;
    }

    public String getUpload_batchId() {
        return upload_batchId;
    }

    public void setUpload_batchId(String upload_batchId) {
        this.upload_batchId = upload_batchId;
    }

    public Date getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(Date upload_date) {
        this.upload_date = upload_date;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public void setSystemAsignedBatchID(String SystemAsignedBatchID) {
        this.SystemAsignedBatchID = SystemAsignedBatchID;
    }

    public String getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(String uploadFile) {
        this.uploadFile = uploadFile;
    }

    public TPsmStock() {
    }

    public String getBATCHID() {
        return BATCHID;
    }

    public String getPIN_USED() {
        return PIN_USED;
    }

    public void setPIN_USED(String PIN_USED) {
        this.PIN_USED = PIN_USED;
    }

    public void setBATCHID(String BATCHID) {
        this.BATCHID = BATCHID;
    }

    public String getCLOSED() {
        return CLOSED;
    }

    public void setCLOSED(String CLOSED) {
        this.CLOSED = CLOSED;
    }

    public String getDISCOUNT() {
        return DISCOUNT;
    }

    public void setDISCOUNT(String DISCOUNT) {
        this.DISCOUNT = DISCOUNT;
    }

    public String getISSUER() {
        return ISSUER;
    }

    public void setISSUER(String ISSUER) {
        this.ISSUER = ISSUER;
    }

    public String getMERCHANT_CODE() {
        return MERCHANT_CODE;
    }

    public void setMERCHANT_CODE(String MERCHANT_CODE) {
        this.MERCHANT_CODE = MERCHANT_CODE;
    }

    public Date getMODIFIED() {
        return MODIFIED;
    }

    public void setMODIFIED(Date MODIFIED) {
        this.MODIFIED = MODIFIED;
    }

    public String getORDER_NO() {
        return ORDER_NO;
    }

    public void setORDER_NO(String ORDER_NO) {
        this.ORDER_NO = ORDER_NO;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {

        this.PIN = PIN;
    }

    public String getPIN_DENO() {
//       String PIN_DENOx = this.getPIN_VALUE();
//        System.out.println(PIN_DENOx + " XXXXXXXXXXX XXXXXXXXXXXX");
//        if (PIN_DENOx != null) {
//            PIN_DENOx = PIN_DENOx.replaceAll(".00", "").replaceAll(".0", "");
//        }
        System.out.println(PIN_DENO + " XXXXXXXXXXX XXXXXXXXXXXX 2");
        return PIN_DENO;
    }

    public void setPIN_DENO(String PIN_DENO) {
//        if (PIN_DENO != null) {
//            PIN_DENO = PIN_DENO.replaceAll(".00", "").replaceAll(".0", "");
//        }
        this.PIN_DENO = PIN_DENO;
    }

    public Date getPIN_EXPIRATION() {
        return PIN_EXPIRATION;
    }

    public void setPIN_EXPIRATION(Date PIN_EXPIRATION) {
        this.PIN_EXPIRATION = PIN_EXPIRATION;
    }

    public Date getPIN_ISSUED() {
        return PIN_ISSUED;
    }

    public void setPIN_ISSUED(Date PIN_ISSUED) {
        this.PIN_ISSUED = PIN_ISSUED;
    }

    public String getPIN_STATUS() {
        return PIN_STATUS;
    }

    public void setPIN_STATUS(String PIN_STATUS) {
        this.PIN_STATUS = PIN_STATUS;
    }

    public String getPIN_USER() {
        return PIN_USER;
    }

    public void setPIN_USER(String PIN_USER) {
        this.PIN_USER = PIN_USER;
    }

    public String getPIN_VALUE() {
        return PIN_VALUE;
    }

    public void setPIN_VALUE(String PIN_VALUE) {
        this.PIN_VALUE = PIN_VALUE;
    }

    public String getPROVIDER_ID() {
        return PROVIDER_ID;
    }

    public void setPROVIDER_ID(String PROVIDER_ID) {
        this.PROVIDER_ID = PROVIDER_ID;
    }

    public String getSERIAL() {
        return SERIAL;
    }

    public void setSERIAL(String SERIAL) {
        this.SERIAL = SERIAL;
    }

    public String getSUBATCHID() {
        return SUBATCHID;
    }

    public void setSUBATCHID(String SUBATCHID) {
        this.SUBATCHID = SUBATCHID;
    }

    public String getTARGET_PHONE() {
        return TARGET_PHONE;
    }

    public void setTARGET_PHONE(String TARGET_PHONE) {
        this.TARGET_PHONE = TARGET_PHONE;
    }

    public String getUNIQUE_TRANSID() {
        return UNIQUE_TRANSID;
    }

    public void setUNIQUE_TRANSID(String UNIQUE_TRANSID) {
        this.UNIQUE_TRANSID = UNIQUE_TRANSID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getVALID_DAY() {
        return VALID_DAY;
    }

    public void setVALID_DAY(String VALID_DAY) {
        this.VALID_DAY = VALID_DAY;
    }

    public String getTrans_soc() {
        return trans_soc;
    }

    public void setTrans_soc(String trans_soc) {
        this.trans_soc = trans_soc;
    }

    public TPsmDealer getDealer() {
        return dealer;
    }

    public void setDealer(TPsmDealer dealer) {
        this.dealer = dealer;
    }

    public String getDecodeCreatedUser() {
        return decodeCreatedUser;
    }

    public void setDecodeCreatedUser(String decodeCreatedUser) {
        this.decodeCreatedUser = decodeCreatedUser;
    }

    public String getFormatedPinDeno() {
        formatedPinDeno = new Utility().formatAmount(this.getPIN_DENO());
        return formatedPinDeno;
    }

    public void setFormatedPinDeno(String formatedPinDeno) {
        formatedPinDeno = new Utility().formatAmount(this.getPIN_DENO());
        this.formatedPinDeno = formatedPinDeno;
    }

    public String getFormatedPinValue() {
        formatedPinValue = new Utility().formatAmount(this.getPIN_VALUE());
        return formatedPinValue;
    }

    public void setFormatedPinValue(String formatedPinValue) {
        formatedPinValue = new Utility().formatAmount(this.getPIN_VALUE());
        this.formatedPinValue = formatedPinValue;
    }

    public TPsmNetworkProvider getProvider() {
        return provider;
    }

    public void setProvider(TPsmNetworkProvider provider) {
        this.provider = provider;
    }

    public static void main(String[] sd) {
        TPsmStock s = new TPsmStock();
        s.setPIN_DENO("2324.0");
        System.out.println(s.getPIN_DENO());
    }
}
