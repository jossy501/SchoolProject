/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.etranzact.psm.dto;

/**
 *
 * @author akachukwu.ntukokwu
 */
public class ErrorBean {

    private String batchId;
    private String sysBatchId;
    private String line;
    private String error;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getSysBatchId() {
        return sysBatchId;
    }

    public void setSysBatchId(String sysBatchId) {
        this.sysBatchId = sysBatchId;
    }

    


}
