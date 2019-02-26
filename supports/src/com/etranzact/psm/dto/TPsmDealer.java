/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.etranzact.psm.dto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "T_PSM_DEALER")
@NamedQueries({
    @NamedQuery(name = "TPsmDealer.findAll", query = "SELECT t FROM TPsmDealer t")})
public class TPsmDealer implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String dealerId;
    @Column(name = "dealer_name")
    private String dealerName;
    @Column(name = "dealer_address")
    private String dealerAddress;
    @Column(name = "dealer_phone_number")
    private String dealerPhoneNumber;
    @Column(name = "dealer_email")
    @Email
    private String dealerEmail;
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

    public TPsmDealer() {
    }

    public TPsmDealer(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getDealerAddress() {
        return dealerAddress;
    }

    public void setDealerAddress(String dealerAddress) {
        this.dealerAddress = dealerAddress;
    }

    public String getDealerPhoneNumber() {
        return dealerPhoneNumber;
    }

    public void setDealerPhoneNumber(String dealerPhoneNumber) {
        this.dealerPhoneNumber = dealerPhoneNumber;
    }

    public String getDealerEmail() {
        return dealerEmail;
    }

    public void setDealerEmail(String dealerEmail) {
        this.dealerEmail = dealerEmail;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dealerId != null ? dealerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TPsmDealer)) {
            return false;
        }
        TPsmDealer other = (TPsmDealer) object;
        if ((this.dealerId == null && other.dealerId != null) || (this.dealerId != null && !this.dealerId.equals(other.dealerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.etranzact.psm.dto.TPsmDealer[dealerId=" + dealerId + "]";
    }

}
