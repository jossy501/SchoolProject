/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.etranzact.supportmanager.dto;

import java.util.Date;

/**
 *
 * @author akachukwu.ntukokwu
 */
public class FundPocketMoniLog
{
    String searchTranRef;
    Date searchDate1;
    Date searchDate2;
    String searchAction;

    String type;
    String phonenumber;
    String orderID;
    String sessionID;
    String amount;
    String dateTime;
    String Status;
    String transactionRef;
    String operatorType;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public Date getSearchDate1() {
        return searchDate1;
    }

    public void setSearchDate1(Date searchDate1) {
        this.searchDate1 = searchDate1;
    }

    public Date getSearchDate2() {
        return searchDate2;
    }

    public void setSearchDate2(Date searchDate2) {
        this.searchDate2 = searchDate2;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSearchAction() {
        return searchAction;
    }

    public void setSearchAction(String searchAction) {
        this.searchAction = searchAction;
    }

    

    public String getSearchTranRef() {
        return searchTranRef;
    }

    public void setSearchTranRef(String searchTranRef) {
        this.searchTranRef = searchTranRef;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
