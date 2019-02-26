/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.etranzact.drs.dto;

import com.etranzact.drs.utility.Utility;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.validator.Email;

/**
 *
 * @author Akachukwu
 */
@Entity
@Table(name = "cl_userprofile")
@NamedQueries({
    @NamedQuery(name = "ClUserprofile.findAll", query = "SELECT c FROM ClUserprofile c"),
    @NamedQuery(name = "ClUserprofile.findByUserId", query = "SELECT c FROM ClUserprofile c WHERE c.userId = :userId"),
    @NamedQuery(name = "ClUserprofile.findByBankCode", query = "SELECT c FROM ClUserprofile c WHERE c.bankCode = :bankCode"),
    @NamedQuery(name = "ClUserprofile.findByFullname", query = "SELECT c FROM ClUserprofile c WHERE c.fullname = :fullname"),
    @NamedQuery(name = "ClUserprofile.findByEmail", query = "SELECT c FROM ClUserprofile c WHERE c.email = :email"),
    @NamedQuery(name = "ClUserprofile.findByMobile", query = "SELECT c FROM ClUserprofile c WHERE c.mobile = :mobile"),
    @NamedQuery(name = "ClUserprofile.findByStatus", query = "SELECT c FROM ClUserprofile c WHERE c.status = :status"),
    @NamedQuery(name = "ClUserprofile.findByCreated", query = "SELECT c FROM ClUserprofile c WHERE c.created = :created"),
    @NamedQuery(name = "ClUserprofile.findByModified", query = "SELECT c FROM ClUserprofile c WHERE c.modified = :modified"),
    @NamedQuery(name = "ClUserprofile.findById", query = "SELECT c FROM ClUserprofile c WHERE c.id = :id")})
public class ClUserprofile implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "BANK_CODE")
    private String bankCode;
    @Basic(optional = false)
    @Column(name = "FULLNAME")
    private String fullname;
    @Basic(optional = false)
    @Column(name = "EMAIL")
    @Email
    private String email;
    @Basic(optional = false)
    @Column(name = "MOBILE")
    private String mobile;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Column(name = "MODIFIED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    String decodeBank;
    String decodeStatus;

    public ClUserprofile() {
    }

    public ClUserprofile(Integer id) {
        this.id = id;
    }

    public ClUserprofile(Integer id, int userId, String bankCode, String fullname, String email, String mobile, String status) {
        this.id = id;
        this.bankCode = bankCode;
        this.fullname = fullname;
        this.email = email;
        this.mobile = mobile;
        this.status = status;
    }

 

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDecodeBank() {
        //decodeStatus=new Utility().getBank(bankCode).getBankname();
        return decodeBank;
    }

    public void setDecodeBank(String decodeBank) {
        this.decodeBank = decodeBank;
    }

    public String getDecodeStatus() {
        if(status!=null && status.equals("0") || status.equals("O"))
        {
        decodeStatus="Active".toUpperCase();
        } else if(status!=null && status.equals("1"))
        {
        decodeStatus="Inactive".toUpperCase();
        }
        return decodeStatus;
    }

    public void setDecodeStatus(String decodeDtatus) {

        this.decodeStatus = decodeDtatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClUserprofile)) {
            return false;
        }
        ClUserprofile other = (ClUserprofile) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "test.ClUserprofile[id=" + id + "]";
    }

}
