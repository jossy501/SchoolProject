package com.etranzact.drs.dto;

import com.etranzact.drs.controller.ClaimController;
import com.etranzact.drs.utility.DateUtility;
import com.etranzact.drs.utility.Utility;
import com.etranzact.supportmanager.dto.User;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.swing.text.Utilities;
import org.hibernate.validator.Past;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 *
 * @author Akachukwu
 */
@Entity
@Table(name = "claim")
//@Scope(ScopeType.SESSION)
//@Name("claim")
@NamedQueries({
    @NamedQuery(name = "Claim.findAll", query = "SELECT c FROM Claim c"),
    @NamedQuery(name = "Claim.findById", query = "SELECT c FROM Claim c WHERE c.id = :id"),
    @NamedQuery(name = "Claim.findByAcquirer", query = "SELECT c FROM Claim c WHERE c.acquirer = :acquirer"),
    @NamedQuery(name = "Claim.findByChannelType", query = "SELECT c FROM Claim c WHERE c.channelType = :channelType"),
    @NamedQuery(name = "Claim.findByCardNo", query = "SELECT c FROM Claim c WHERE c.cardNo = :cardNo"),
    @NamedQuery(name = "Claim.findByTransactionAmount", query = "SELECT c FROM Claim c WHERE c.transactionAmount = :transactionAmount"),
    @NamedQuery(name = "Claim.findByDateOfTransaction", query = "SELECT c FROM Claim c WHERE c.dateOfTransaction = :dateOfTransaction"),
    @NamedQuery(name = "Claim.findByClaimType", query = "SELECT c FROM Claim c WHERE c.claimType = :claimType"),
    @NamedQuery(name = "Claim.findByCommentJournal", query = "SELECT c FROM Claim c WHERE c.commentJournal = :commentJournal"),
    @NamedQuery(name = "Claim.findByCreatedDate", query = "SELECT c FROM Claim c WHERE c.createdDate = :createdDate"),
    @NamedQuery(name = "Claim.findByClaimStatus", query = "SELECT c FROM Claim c WHERE c.claimStatus = :claimStatus"),
    @NamedQuery(name = "Claim.findByReasonForModify", query = "SELECT c FROM Claim c WHERE c.reasonForModify = :reasonForModify"),
    @NamedQuery(name = "Claim.findByClosedDate", query = "SELECT c FROM Claim c WHERE c.closedDate = :closedDate"),
    @NamedQuery(name = "Claim.findByClosedUser", query = "SELECT c FROM Claim c WHERE c.closedUser = :closedUser"),
    @NamedQuery(name = "Claim.findByCreatedUser", query = "SELECT c FROM Claim c WHERE c.createdUser = :createdUser"),
    @NamedQuery(name = "Claim.findByTransactionStan", query = "SELECT c FROM Claim c WHERE c.transactionStan = :transactionStan"),
    @NamedQuery(name = "Claim.findByTerminalId", query = "SELECT c FROM Claim c WHERE c.terminalId = :terminalId"),
    @NamedQuery(name = "Claim.findByComplainingBank", query = "SELECT c FROM Claim c WHERE c.complainingBank = :complainingBank"),
    @NamedQuery(name = "Claim.findByRealCreatedDate", query = "SELECT c FROM Claim c WHERE c.realCreatedDate = :realCreatedDate"),
    @NamedQuery(name = "Claim.findByReversalTransactionId", query = "SELECT c FROM Claim c WHERE c.reversalTransactionId = :reversalTransactionId")})
public class Claim implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "acquirer")
    private String acquirer;
    @Column(name = "channel_type")
    private String channelType;
    @Column(name = "card_no")
    private String cardNo;
    @Column(name = "transaction_amount")
    private double transactionAmount;
    @Column(name = "date_of_transaction")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Past
    private Date dateOfTransaction;
    private String type;
    private String dateOfTransaction_2;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateOfTransaction2;
    @Column(name = "claim_type")
    private int claimType;
    String decodeClosedUser;
    @Column(name = "comment_journal")
    private String comment_Journal;
    @Column(name = "created_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "claim_status")
    private String claimStatus;
    @Column(name = "reason_for_modify")
    private String reasonForModify;
    @Column(name = "closed_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date closedDate;
    private String responseCode;
    @Column(name = "closed_user")
    private String closedUser;
    @Column(name = "created_user")
    private String createdUser;
    @Column(name = "transaction_stan")
    private String transactionStan;
    @Column(name = "terminal_id")
    private String terminalId;
    @Column(name = "complaining_bank")
    private String complainingBank;
    @Column(name = "real_created_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date realCreatedDate;
    private String realCreatedDate_2;
    @Column(name = "reversal_transaction_id")
    private String reversalTransactionId;
    private ClDispute clDispute;
    private ClBanks clBanks;
    private EChannel channel;
    private String tranNo;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dueDate;
    private String dueDate_2;
    private String accountNo;
    private String modifiyBy;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date modifyDate;
    private String linkage;
    private String declineReason;
    private ClMessages message;
    private String subject;
    private String sysID;
    String messageBody;
    String prevMessageBody;
    private String statusDescription;
    private TransactionSearch transactionSearch;
    private String formatAmount;
    private String decodeSentBank;
    private String decodeClaimType;
    private String linked;
    private String reOpneningReason;
    private boolean reOpenable;
    private String attachFileURL;
    private String downloadStatus;
    private String oldStateValue;

    public Claim() {
    }

    public Claim(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDownloadStatus() {
         if(downloadStatus==null || downloadStatus.trim().isEmpty() || downloadStatus.trim().toUpperCase().equals("NULL"))
        {
        downloadStatus=null;
        }
        return downloadStatus;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public void setDownloadStatus(String downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public String getDecodeClosedUser() {
        return decodeClosedUser;
    }

    public void setDecodeClosedUser(String decodeClosedUser) {
        this.decodeClosedUser = decodeClosedUser;
    }

    public String getOldStateValue() {
        oldStateValue = this.decodeStatus(oldStateValue);
         if(oldStateValue==null || oldStateValue.trim().isEmpty() || oldStateValue.trim().toUpperCase().equals("NULL"))
        {
        oldStateValue=null;
        }
        return oldStateValue;
    }

    

    public String getDateOfTransaction_2() {
        dateOfTransaction_2 = formatDate(dateOfTransaction);
        return dateOfTransaction_2;
    }

    public void setDateOfTransaction_2(String dateOfTransaction_2) {
        dateOfTransaction_2 =formatDate(dateOfTransaction);
        this.dateOfTransaction_2 = dateOfTransaction_2;
    }

    public String getDueDate_2() {
       dueDate_2= formatDate(dueDate);
        return dueDate_2;
    }

    public void setDueDate_2(String dueDate_2) {
        dueDate_2= formatDate(dueDate);
        this.dueDate_2 = dueDate_2;
    }

    public String getRealCreatedDate_2() {
        realCreatedDate_2= formatDate(realCreatedDate);
        return realCreatedDate_2;
    }

    public void setRealCreatedDate_2(String realCreatedDate_2) {
        realCreatedDate_2= formatDate(realCreatedDate);
        this.realCreatedDate_2 = realCreatedDate_2;
    }

    public void setOldStateValue(String oldStateValue) {
        this.oldStateValue = oldStateValue;
    }

    public String getSysID() {
        return sysID;
    }

    public void setSysID(String sysID) {
        this.sysID = sysID;
    }

   

    public String getStatusDescription() {
        statusDescription = decodeStatus(claimStatus);

        return statusDescription;
    }

    public Date getDateOfTransaction2() {
        return dateOfTransaction2;
    }

    public void setDateOfTransaction2(Date dateOfTransaction2) {
        this.dateOfTransaction2 = dateOfTransaction2;
    }

    

    public void setStatusDescription(String statusDescription) {
        statusDescription = statusDescription;
    }

    public Integer getId() {
        return id;
    }

    

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAcquirer() {
        return acquirer;
    }

    public void setAcquirer(String acquirer) {
        this.acquirer = acquirer;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Date getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(Date dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public int getClaimType() {
        return claimType;
    }

    public void setClaimType(int claimType) {
        this.claimType = claimType;
    }

    public String getComment_Journal() {
        return comment_Journal;
    }

    public void setComment_Journal(String comment_Journal) {
        this.comment_Journal = comment_Journal;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    public String getReasonForModify() {
        return reasonForModify;
    }

    public void setReasonForModify(String reasonForModify) {
        this.reasonForModify = reasonForModify;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public String getClosedUser() {
        return closedUser;
    }

    public void setClosedUser(String closedUser) {
        this.closedUser = closedUser;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getTransactionStan() {
        return transactionStan;
    }

    public void setTransactionStan(String transactionStan) {
        this.transactionStan = transactionStan;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getComplainingBank() {
        return complainingBank;
    }

    public void setComplainingBank(String complainingBank) {
        this.complainingBank = complainingBank;
    }

    public Date getRealCreatedDate() {
        return realCreatedDate;
    }

    public void setRealCreatedDate(Date realCreatedDate) {
        this.realCreatedDate = realCreatedDate;
    }

    public String getReversalTransactionId() {
        return reversalTransactionId;
    }

    public void setReversalTransactionId(String reversalTransactionId) {
        this.reversalTransactionId = reversalTransactionId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAttachFileURL() {
         if(attachFileURL==null || attachFileURL.trim().isEmpty() || attachFileURL.trim().toUpperCase().equals("NULL"))
        {
        attachFileURL=null;
        }
        return attachFileURL;
    }

    public void setAttachFileURL(String attachFileURL) {
        this.attachFileURL = attachFileURL;
    }

    public Date getDueDate() {
    //dueDate = new DateUtility().calculateDueDate(createdDate);
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getTranNo() {
        return tranNo;
    }

    public void setTranNo(String tranNo) {
        this.tranNo = tranNo;
    }

    public String getLinked() {
        return linked;
    }

    public void setLinked(String linked) {
        this.linked = linked;
    }

    public EChannel getChannel() {
        Utility ut = new Utility();
        channel = ut.geteChannelList(this.channelType);
        setChannel(channel);
        ut = null;
        return channel;
    }

    public void setChannel(EChannel channel) {
        this.channel = channel;
    }

    public ClBanks getClBank() {
        Utility ut = new Utility();
        clBanks = ut.getBank(this.acquirer);
        ut = null;
        return clBanks;
    }

//    public void setClBanks(ClBanks clBanks) {
//        this.clBanks = clBanks;
//    }
    public ClDispute getClDispute() {
        Utility ut = new Utility();
        clDispute = ut.getClDispute(this.claimType);
        ut = null;
        return clDispute;
    }

    public String getLinkage() {
        if(linkage==null || linkage.trim().isEmpty() || linkage.trim().toUpperCase().equals("NULL"))
        {
        linkage=null;
        }
        return linkage;
    }

    public void setLinkage(String linkage) {
        this.linkage = linkage;
    }

    public String getModifiyBy() {
        return modifiyBy;
    }

    public void setModifiyBy(String modifiyBy) {
        this.modifiyBy = modifiyBy;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getDeclineReason() {
        return declineReason;
    }

    public void setDeclineReason(String declineReason) {
        this.declineReason = declineReason;
    }

    public ClMessages getMessage() {

        return message;
    }
//
//    public void setMessage(ClMessages message) {
//        this.message = message;
//    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPrevMessageBody() {
        Utility ut = new Utility();
        message = ut.getMessage(this.id.toString());
        ut = null;
        if(message!=null)
        {
         return message.getMessage();
        }
 else
        {
        return null;
                }
    }

    public String getDecodeClaimType() {
        return decodeClaimType;
    }

    public void setDecodeClaimType(String decodeClaimType) {
        this.decodeClaimType = decodeClaimType;
    }

    public void setPrevMessageBody(String prevMessageBody) {
        this.prevMessageBody = prevMessageBody;
    }

    public boolean isReOpenable() {
        return reOpenable;
    }

    public void setReOpenable(boolean reOpenable) {
        this.reOpenable = reOpenable;
    }

    public String getReOpneningReason() {
         if(reOpneningReason==null || reOpneningReason.trim().isEmpty() || reOpneningReason.trim().toUpperCase().equals("NULL"))
        {
        reOpneningReason=null;
        }
        return reOpneningReason;
    }

    public void setReOpneningReason(String reOpneningReason) {
        this.reOpneningReason = reOpneningReason;
    }
    

//    public void setClDispute(ClDispute clDispute) {
//        this.clDispute = clDispute;
//    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Claim)) {
            return false;
        }
        Claim other = (Claim) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    private String decodeStatus(String claimStatus) {
        try {
            if (claimStatus.equals("O")) {
                claimStatus = "Open";
            } else if (claimStatus.equals("D")) {
                claimStatus = "Declined";
            } else if (claimStatus.equals("A")) {
                claimStatus = "Accepted";
            } else if (claimStatus.equals("X")) {
                claimStatus = "Cancelled For Modification";
            }
            else if (claimStatus.equals("RO")) {
                claimStatus = "Re-Opened";
            }


            return claimStatus.toUpperCase();
        } catch (Exception s) {
            return "";
        }
    }
     public String getFormatAmount() {
        formatAmount=new Utility().formatAmount(transactionAmount+"");
        return formatAmount;
    }

    public void setFormatAmount(String formatAmount) {
        this.formatAmount = formatAmount;
    }
    public String getDecodeSentBank() {
        try{
        decodeSentBank =new Utility().getBank(complainingBank).getBankname();
        }
        catch(Exception x){}
        return decodeSentBank;
    }

    public void setDecodeSentBank(String decodeSentBank) {
        this.decodeSentBank = decodeSentBank;
    }


    private String formatDate (Date d)
    {
        try{
        return DateFormat.getDateTimeInstance().format(d);
        }
        catch(Exception dx){
            return null;
            }
        }
    }
//    public void createClaim() {
//        new ClaimController().createClaim(this);
//
//    }
//    public void distroy() {
//
//        this.distroy();
//
//    }

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package com.etranzact.drs.dto;
//
//import com.etranzact.drs.controller.ClaimController;
//import java.io.Serializable;
//import javax.persistence.Basic;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.NamedQueries;
//import javax.persistence.NamedQuery;
//import javax.persistence.Table;
//import org.jboss.seam.ScopeType;
//import org.jboss.seam.annotations.Name;
//import org.jboss.seam.annotations.Scope;
//import java.util.*;
//
///**
// *
// * @author Akachukwu
// */
////@Entity
////@Table(name = "claim")
//@Scope(ScopeType.SESSION)
//@Name("claim")
//
//public class Claim implements Serializable {
//    private static final long serialVersionUID = 1L;
//
//    private Integer id;
//    private String acquirer;
//    private String channelType;
//    private String cardNo;
//    private Long transactionAmount;
//    private Date dateOfTransaction;
//    private String claimType;
//    private String comment_Journal;
//    private Date createdDate;
//    private String claimStatus;
//    private String reasonForModify;
//    private Date closedDate;
//    private String closedUser;
//    private String createdUser;
//    private String transactionStan;
//    private String terminalId;
//    private String complainingBank;
//    private String realCreatedDate;
//    private String reversalTransactionId;
//
//    public Claim() {
//    }
//
//    public Claim(Integer id) {
//        this.id = id;
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public String getAcquirer() {
//        return acquirer;
//    }
//
//    public void setAcquirer(String acquirer) {
//        this.acquirer = acquirer;
//    }
//
//    public String getChannelType() {
//        return channelType;
//    }
//
//    public void setChannelType(String channelType) {
//        this.channelType = channelType;
//    }
//
//    public String getCardNo() {
//        return cardNo;
//    }
//
//    public void setCardNo(String cardNo) {
//        this.cardNo = cardNo;
//    }
//
//    public Long getTransactionAmount() {
//        return transactionAmount;
//    }
//
//    public void setTransactionAmount(Long transactionAmount) {
//        this.transactionAmount = transactionAmount;
//    }
//
//    public Date getDateOfTransaction() {
//        return dateOfTransaction;
//    }
//
//    public void setDateOfTransaction(Date dateOfTransaction) {
//        this.dateOfTransaction = dateOfTransaction;
//    }
//
//    public String getClaimType() {
//        return claimType;
//    }
//
//    public void setClaimType(String claimType) {
//        this.claimType = claimType;
//    }
//
//    public String getCommentJournal() {
//        return comment_Journal;
//    }
//
//    public void setCommentJournal(String comment_Journal) {
//        this.comment_Journal = comment_Journal;
//    }
//
//    public Date getCreatedDate() {
//        return createdDate;
//    }
//
//    public void setCreatedDate(Date createdDate) {
//        this.createdDate = createdDate;
//    }
//
//    public String getClaimStatus() {
//        return claimStatus;
//    }
//
//    public void setClaimStatus(String claimStatus) {
//        this.claimStatus = claimStatus;
//    }
//
//    public String getReasonForModify() {
//        return reasonForModify;
//    }
//
//    public void setReasonForModify(String reasonForModify) {
//        this.reasonForModify = reasonForModify;
//    }
//
//    public Date getClosedDate() {
//        return closedDate;
//    }
//
//    public void setClosedDate(Date closedDate) {
//        this.closedDate = closedDate;
//    }
//
//    public String getClosedUser() {
//        return closedUser;
//    }
//
//    public void setClosedUser(String closedUser) {
//        this.closedUser = closedUser;
//    }
//
//    public String getCreatedUser() {
//        return createdUser;
//    }
//
//    public void setCreatedUser(String createdUser) {
//        this.createdUser = createdUser;
//    }
//
//    public String getComplainingBank() {
//        return complainingBank;
//    }
//
//    public void setComplainingBank(String complainingBank) {
//        this.complainingBank = complainingBank;
//    }
//
//    public String getRealCreatedDate() {
//        return realCreatedDate;
//    }
//
//    public void setRealCreatedDate(String realCreatedDate) {
//        this.realCreatedDate = realCreatedDate;
//    }
//
//    public String getReversalTransactionId() {
//        return reversalTransactionId;
//    }
//
//    public void setReversalTransactionId(String reversalTransactionId) {
//        this.reversalTransactionId = reversalTransactionId;
//    }
//
//    public String getTerminalId() {
//        return terminalId;
//    }
//
//    public void setTerminalId(String terminalId) {
//        this.terminalId = terminalId;
//    }
//
//    public String getTransactionStan() {
//        return transactionStan;
//    }
//
//    public void setTransactionStan(String transactionStan) {
//        this.transactionStan = transactionStan;
//    }
//
//
//public void createClaim()
//    {
//    new ClaimController().createClaim(this);
//}
//
//}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
