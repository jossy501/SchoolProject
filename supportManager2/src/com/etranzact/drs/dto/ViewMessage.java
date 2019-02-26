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
public class ViewMessage {

    int id;
    String claimID;
    String sysID;
    String subject;
    String sendingBank;
    String receiving;
    Date sentDate;
    Claim claim;

    public Claim getClaim() {
        return claim;
    }

    public String getSysID() {
        return sysID;
    }

    public void setSysID(String sysID) {
        this.sysID = sysID;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    
    public String getClaimID() {
        return claimID;
    }

    public void setClaimID(String claimID) {
        this.claimID = claimID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReceiving() {
        return receiving;
    }

    public void setReceiving(String receiving) {
        this.receiving = receiving;
    }

    public String getSendingBank() {
        return sendingBank;
    }

    public void setSendingBank(String sendingBank) {
        this.sendingBank = sendingBank;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


}
