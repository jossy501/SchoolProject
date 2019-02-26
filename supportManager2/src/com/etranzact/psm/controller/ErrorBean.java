/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.etranzact.psm.controller;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author akachukwu.ntukokwu
 */
public class ErrorBean {
    String line;
    String errorDesc;
    private String batchId;
    private String sysBatchId;

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getSysBatchId() {
        return sysBatchId;
    }

    public void setSysBatchId(String sysBatchId) {
        this.sysBatchId = sysBatchId;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }


   


}
