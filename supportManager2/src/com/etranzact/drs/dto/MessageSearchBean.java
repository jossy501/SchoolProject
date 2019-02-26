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
public class MessageSearchBean {

    Date startDate;
    Date endDate;
    String claimId;
    String subject;
    String sendingBank;
    String receivingBank;

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getReceivingBank() {
        return receivingBank;
    }

    public void setReceivingBank(String receivingBank) {
        this.receivingBank = receivingBank;
    }

    public String getSendingBank() {
        return sendingBank;
    }

    public void setSendingBank(String sendingBank) {
        this.sendingBank = sendingBank;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


}
