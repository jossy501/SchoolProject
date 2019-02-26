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
import javax.persistence.TemporalType;

/**
 *
 * @author akachukwu.ntukokwu
 */
@Entity
@Table(name = "T_PSM_NETWORK_PROVIDER")
@NamedQueries({
    @NamedQuery(name = "TPsmNetworkProvider.findAll", query = "SELECT t FROM TPsmNetworkProvider t")})
public class TPsmNetworkProvider implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Column(name = "provider_id")
    private int providerId;
    @Column(name = "provider_code")
    private String providerCode;
    @Column(name = "provider_name")
    private String providerName;
    @Column(name = "provider_filetype")
    private String providerFileType;
    @Column(name = "pin_length")
    private Integer pinLength;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "created_user")
    private String createdUser;
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    @Column(name = "modified_user")
    private String modifiedUser;

    public TPsmNetworkProvider() {
    }

    public String getProviderFileType() {
        return providerFileType;
    }

    public void setProviderFileType(String providerFileType) {
        this.providerFileType = providerFileType;
    }

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public Integer getPinLength() {
        return pinLength;
    }

    public void setPinLength(Integer pinLength) {
        this.pinLength = pinLength;
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
