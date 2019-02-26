/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.etranzact.drs.dto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Akachukwu
 */
@Entity
@Table(name = "cl_claims")
@NamedQueries({
    @NamedQuery(name = "ClClaims.findAll", query = "SELECT c FROM ClClaims c"),
    @NamedQuery(name = "ClClaims.findByClaimID", query = "SELECT c FROM ClClaims c WHERE c.claimID = :claimID"),
    @NamedQuery(name = "ClClaims.findByBankname", query = "SELECT c FROM ClClaims c WHERE c.bankname = :bankname"),
    @NamedQuery(name = "ClClaims.findByTransactionType", query = "SELECT c FROM ClClaims c WHERE c.transactionType = :transactionType"),
    @NamedQuery(name = "ClClaims.findByCardNumber", query = "SELECT c FROM ClClaims c WHERE c.cardNumber = :cardNumber"),
    @NamedQuery(name = "ClClaims.findByStan", query = "SELECT c FROM ClClaims c WHERE c.stan = :stan"),
    @NamedQuery(name = "ClClaims.findByTerminalId", query = "SELECT c FROM ClClaims c WHERE c.terminalId = :terminalId"),
    @NamedQuery(name = "ClClaims.findByTransNumber", query = "SELECT c FROM ClClaims c WHERE c.transNumber = :transNumber"),
    @NamedQuery(name = "ClClaims.findByAmount", query = "SELECT c FROM ClClaims c WHERE c.amount = :amount"),
    @NamedQuery(name = "ClClaims.findByTransDate", query = "SELECT c FROM ClClaims c WHERE c.transDate = :transDate"),
    @NamedQuery(name = "ClClaims.findByDateCreated", query = "SELECT c FROM ClClaims c WHERE c.dateCreated = :dateCreated"),
    @NamedQuery(name = "ClClaims.findByExpiryDate", query = "SELECT c FROM ClClaims c WHERE c.expiryDate = :expiryDate"),
    @NamedQuery(name = "ClClaims.findByAcctNumber", query = "SELECT c FROM ClClaims c WHERE c.acctNumber = :acctNumber"),
    @NamedQuery(name = "ClClaims.findByClaim", query = "SELECT c FROM ClClaims c WHERE c.claim = :claim"),
    @NamedQuery(name = "ClClaims.findByComplainingBank", query = "SELECT c FROM ClClaims c WHERE c.complainingBank = :complainingBank"),
    @NamedQuery(name = "ClClaims.findByStatus", query = "SELECT c FROM ClClaims c WHERE c.status = :status"),
    @NamedQuery(name = "ClClaims.findByReqReopening", query = "SELECT c FROM ClClaims c WHERE c.reqReopening = :reqReopening"),
    @NamedQuery(name = "ClClaims.findByStatusFlag", query = "SELECT c FROM ClClaims c WHERE c.statusFlag = :statusFlag"),
    @NamedQuery(name = "ClClaims.findByDateStatusModified", query = "SELECT c FROM ClClaims c WHERE c.dateStatusModified = :dateStatusModified"),
    @NamedQuery(name = "ClClaims.findByDateReqReopening", query = "SELECT c FROM ClClaims c WHERE c.dateReqReopening = :dateReqReopening"),
    @NamedQuery(name = "ClClaims.findByDisputeOfficer", query = "SELECT c FROM ClClaims c WHERE c.disputeOfficer = :disputeOfficer"),
    @NamedQuery(name = "ClClaims.findByUniqueTransid", query = "SELECT c FROM ClClaims c WHERE c.uniqueTransid = :uniqueTransid"),
    @NamedQuery(name = "ClClaims.findByExternalTransid", query = "SELECT c FROM ClClaims c WHERE c.externalTransid = :externalTransid"),
    @NamedQuery(name = "ClClaims.findByGlobalid", query = "SELECT c FROM ClClaims c WHERE c.globalid = :globalid"),
    @NamedQuery(name = "ClClaims.findByMerchantCode", query = "SELECT c FROM ClClaims c WHERE c.merchantCode = :merchantCode"),
    @NamedQuery(name = "ClClaims.findByDownload", query = "SELECT c FROM ClClaims c WHERE c.download = :download"),
    @NamedQuery(name = "ClClaims.findByInitialCreatedDate", query = "SELECT c FROM ClClaims c WHERE c.initialCreatedDate = :initialCreatedDate"),
    @NamedQuery(name = "ClClaims.findById", query = "SELECT c FROM ClClaims c WHERE c.id = :id")})
public class ClClaims implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "claimID")
    private String claimID;
    @Basic(optional = false)
    @Column(name = "bankname")
    private String bankname;
    @Basic(optional = false)
    @Column(name = "transactionType")
    private String transactionType;
    @Basic(optional = false)
    @Column(name = "cardNumber")
    private String cardNumber;
    @Basic(optional = false)
    @Column(name = "stan")
    private String stan;
    @Basic(optional = false)
    @Column(name = "terminalId")
    private String terminalId;
    @Basic(optional = false)
    @Column(name = "transNumber")
    private String transNumber;
    @Basic(optional = false)
    @Column(name = "amount")
    private String amount;
    @Basic(optional = false)
    @Column(name = "transDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transDate;
    @Basic(optional = false)
    @Column(name = "dateCreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Basic(optional = false)
    @Column(name = "expiryDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;
    @Basic(optional = false)
    @Column(name = "acctNumber")
    private String acctNumber;
    @Basic(optional = false)
    @Column(name = "claim")
    private String claim;
    @Basic(optional = false)
    @Column(name = "complainingBank")
    private String complainingBank;
    @Basic(optional = false)
    @Lob
    @Column(name = "comment")
    private String comment;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @Column(name = "reqReopening")
    private String reqReopening;
    @Basic(optional = false)
    @Lob
    @Column(name = "whyStatusModified")
    private String whyStatusModified;
    @Lob
    @Column(name = "whyReqReopening")
    private String whyReqReopening;
    @Column(name = "statusFlag")
    private String statusFlag;
    @Column(name = "dateStatusModified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateStatusModified;
    @Column(name = "dateReqReopening")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateReqReopening;
    @Basic(optional = false)
    @Column(name = "disputeOfficer")
    private String disputeOfficer;
    @Column(name = "unique_transid")
    private String uniqueTransid;
    @Column(name = "external_transid")
    private String externalTransid;
    @Column(name = "globalid")
    private String globalid;
    @Column(name = "merchant_code")
    private String merchantCode;
    @Column(name = "download")
    private String download;
    @Column(name = "initialCreatedDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date initialCreatedDate;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;

    public ClClaims() {
    }

    public ClClaims(Integer id) {
        this.id = id;
    }

    public ClClaims(Integer id, String claimID, String bankname, String transactionType, String cardNumber, String stan, String terminalId, String transNumber, String amount, Date transDate, Date dateCreated, Date expiryDate, String acctNumber, String claim, String complainingBank, String comment, String status, String reqReopening, String whyStatusModified, String disputeOfficer) {
        this.id = id;
        this.claimID = claimID;
        this.bankname = bankname;
        this.transactionType = transactionType;
        this.cardNumber = cardNumber;
        this.stan = stan;
        this.terminalId = terminalId;
        this.transNumber = transNumber;
        this.amount = amount;
        this.transDate = transDate;
        this.dateCreated = dateCreated;
        this.expiryDate = expiryDate;
        this.acctNumber = acctNumber;
        this.claim = claim;
        this.complainingBank = complainingBank;
        this.comment = comment;
        this.status = status;
        this.reqReopening = reqReopening;
        this.whyStatusModified = whyStatusModified;
        this.disputeOfficer = disputeOfficer;
    }

    public String getClaimID() {
        return claimID;
    }

    public void setClaimID(String claimID) {
        this.claimID = claimID;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTransNumber() {
        return transNumber;
    }

    public void setTransNumber(String transNumber) {
        this.transNumber = transNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getAcctNumber() {
        return acctNumber;
    }

    public void setAcctNumber(String acctNumber) {
        this.acctNumber = acctNumber;
    }

    public String getClaim() {
        return claim;
    }

    public void setClaim(String claim) {
        this.claim = claim;
    }

    public String getComplainingBank() {
        return complainingBank;
    }

    public void setComplainingBank(String complainingBank) {
        this.complainingBank = complainingBank;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReqReopening() {
        return reqReopening;
    }

    public void setReqReopening(String reqReopening) {
        this.reqReopening = reqReopening;
    }

    public String getWhyStatusModified() {
        return whyStatusModified;
    }

    public void setWhyStatusModified(String whyStatusModified) {
        this.whyStatusModified = whyStatusModified;
    }

    public String getWhyReqReopening() {
        return whyReqReopening;
    }

    public void setWhyReqReopening(String whyReqReopening) {
        this.whyReqReopening = whyReqReopening;
    }

    public String getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(String statusFlag) {
        this.statusFlag = statusFlag;
    }

    public Date getDateStatusModified() {
        return dateStatusModified;
    }

    public void setDateStatusModified(Date dateStatusModified) {
        this.dateStatusModified = dateStatusModified;
    }

    public Date getDateReqReopening() {
        return dateReqReopening;
    }

    public void setDateReqReopening(Date dateReqReopening) {
        this.dateReqReopening = dateReqReopening;
    }

    public String getDisputeOfficer() {
        return disputeOfficer;
    }

    public void setDisputeOfficer(String disputeOfficer) {
        this.disputeOfficer = disputeOfficer;
    }

    public String getUniqueTransid() {
        return uniqueTransid;
    }

    public void setUniqueTransid(String uniqueTransid) {
        this.uniqueTransid = uniqueTransid;
    }

    public String getExternalTransid() {
        return externalTransid;
    }

    public void setExternalTransid(String externalTransid) {
        this.externalTransid = externalTransid;
    }

    public String getGlobalid() {
        return globalid;
    }

    public void setGlobalid(String globalid) {
        this.globalid = globalid;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public Date getInitialCreatedDate() {
        return initialCreatedDate;
    }

    public void setInitialCreatedDate(Date initialCreatedDate) {
        this.initialCreatedDate = initialCreatedDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClClaims)) {
            return false;
        }
        ClClaims other = (ClClaims) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "test.ClClaims[id=" + id + "]";
    }

}
