/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.etranzact.drs.dto;

import java.util.Date;

/**
 *
 * @author Akachukwu
 */
public class ClaimSearchBean {

    Date startDate;
    Date endDate;
    String acquirer;
    String status;
    String crdNo;
    String claimId;
    String sender;
    String channelType;

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

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public String getCrdNo() {
        return crdNo;
    }

    public void setCrdNo(String crdNo) {
        this.crdNo = crdNo;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    

}
