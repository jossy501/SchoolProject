/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.etranzact.psm.dto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.validator.Email;

/**
 *
 * @author akachukwu.ntukokwu
 */
@Entity
@Table(name = "T_PSM_USER_CONTACT")
@NamedQueries({
    @NamedQuery(name = "TPsmUserContact.findAll", query = "SELECT t FROM TPsmUserContact t")})
public class TPsmUserContact implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;
    @Column(name = "dealer_id")
    private Integer dealerId;
    @Column(name = "user_fullname")
    private String userFullname;
    @Column(name = "user_phone_number")
    private String userPhoneNumber;
    @Column(name = "user_email")
    @Email
    private String userEmail;
    @Column(name = "created_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "created_user")
    private String createdUser;
    @Column(name = "modified_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date modifiedDate;
    @Column(name = "modified_user")
    private String modifiedUser;
    private TPsmDealer dealer;

    public TPsmUserContact() {
    }

    public TPsmDealer getDealer() {
        return dealer;
    }

    public void setDealer(TPsmDealer dealer) {
        this.dealer = dealer;
    }



    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public String getUserFullname() {
        return userFullname;
    }

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedUser() {
        return modifiedUser;
    }

    public void setModifiedUser(String modifiedUser) {
        this.modifiedUser = modifiedUser;
    }

   

}
