/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.etranzact.drs.dto;

import java.util.Date;

/**
 *
 * @author akachukwu.ntukokwu
 */
public class UserSearchBean {
Date startDate;
  Date endDate;
    String acquirer;
    String fullname;
    String email;

    public String getAcquirer() {
        return acquirer;
    }

    public void setAcquirer(String acquirer) {
        this.acquirer = acquirer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
}
