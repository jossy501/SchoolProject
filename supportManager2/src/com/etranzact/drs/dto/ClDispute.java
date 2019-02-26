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
@Table(name = "cl_dispute")
@NamedQueries({
    @NamedQuery(name = "ClDispute.findAll", query = "SELECT c FROM ClDispute c"),
    @NamedQuery(name = "ClDispute.findByDispute", query = "SELECT c FROM ClDispute c WHERE c.dispute = :dispute"),
    @NamedQuery(name = "ClDispute.findById", query = "SELECT c FROM ClDispute c WHERE c.id = :id")})
public class ClDispute implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "dispute")
    private String dispute;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "dispute_id")
    private Integer disputeid;

    public ClDispute() {
    }

    public ClDispute(Integer id) {
        this.id = id;
    }

    public ClDispute(Integer id, String dispute) {
        this.id = id;
        this.dispute = dispute;
    }

    public String getDispute() {
        return dispute;
    }

    public void setDispute(String dispute) {
        this.dispute = dispute;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDisputeid() {
        return disputeid;
    }

    public void setDisputeid(Integer disputeid) {
        this.disputeid = disputeid;
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
        if (!(object instanceof ClDispute)) {
            return false;
        }
        ClDispute other = (ClDispute) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "test.ClDispute[id=" + id + "]";
    }

}
