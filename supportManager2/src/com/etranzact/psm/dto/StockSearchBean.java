/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.etranzact.psm.dto;

import java.util.Date;

/**
 *
 * @author akachukwu.ntukokwu
 */
public class StockSearchBean {
Date startDate;
Date endDate;
String dealer;
String status;
String pinId;
double pinValue;
double pimAmount;
String pinSerialNumber;
String batchNumber;
String systemBatchNumber;
Date closeDate;
String provider;
 private String level;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

 
    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getPimAmount() {
        return pimAmount;
    }

    public void setPimAmount(double pimAmount) {
        this.pimAmount = pimAmount;
    }

    public String getPinId() {
        return pinId;
    }

    public void setPinId(String pinId) {
        this.pinId = pinId;
    }

    public String getPinSerialNumber() {
        return pinSerialNumber;
    }

    public void setPinSerialNumber(String pinSerialNumber) {
        this.pinSerialNumber = pinSerialNumber;
    }

    public double getPinValue() {
        return pinValue;
    }

    public void setPinValue(double pinValue) {
        this.pinValue = pinValue;
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

    public String getSystemBatchNumber() {
        return systemBatchNumber;
    }

    public void setSystemBatchNumber(String systemBatchNumber) {
        this.systemBatchNumber = systemBatchNumber;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }




}
