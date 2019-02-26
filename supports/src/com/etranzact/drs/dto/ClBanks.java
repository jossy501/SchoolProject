/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.etranzact.drs.dto;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Akachukwu
 */
@Entity
@Table(name = "cl_banks")
@NamedQueries({
    @NamedQuery(name = "ClBanks.findAll", query = "SELECT c FROM ClBanks c"),
    @NamedQuery(name = "ClBanks.findByBankname", query = "SELECT c FROM ClBanks c WHERE c.bankname = :bankname"),
    @NamedQuery(name = "ClBanks.findByBankcode", query = "SELECT c FROM ClBanks c WHERE c.bankcode = :bankcode"),
    @NamedQuery(name = "ClBanks.findByEmailAddress", query = "SELECT c FROM ClBanks c WHERE c.emailAddress = :emailAddress"),
    @NamedQuery(name = "ClBanks.findById", query = "SELECT c FROM ClBanks c WHERE c.id = :id")})
public class ClBanks implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "bankname")
    private String bankname;
    @Basic(optional = false)
    @Column(name = "bankcode")
    private String bankcode;
    @Column(name = "emailAddress")
    private String emailAddress;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;

    public ClBanks() {
    }

    public ClBanks(Integer id) {
        this.id = id;
    }

    public ClBanks(Integer id, String bankname, String bankcode) {
        this.id = id;
        this.bankname = bankname;
        this.bankcode = bankcode;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        if (!(object instanceof ClBanks)) {
            return false;
        }
        ClBanks other = (ClBanks) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "test.ClBanks[id=" + id + "]";
    }

}
