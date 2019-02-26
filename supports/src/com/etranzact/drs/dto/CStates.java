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
@Table(name = "c_states")
@NamedQueries({
    @NamedQuery(name = "CStates.findAll", query = "SELECT c FROM CStates c"),
    @NamedQuery(name = "CStates.findBySybIdentityCol", query = "SELECT c FROM CStates c WHERE c.sybIdentityCol = :sybIdentityCol"),
    @NamedQuery(name = "CStates.findByStateId", query = "SELECT c FROM CStates c WHERE c.stateId = :stateId"),
    @NamedQuery(name = "CStates.findByStateName", query = "SELECT c FROM CStates c WHERE c.stateName = :stateName")})
public class CStates implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "SYB_IDENTITY_COL")
    private Integer sybIdentityCol;
    @Basic(optional = false)
    @Column(name = "STATE_ID")
    private String stateId;
    @Basic(optional = false)
    @Column(name = "STATE_NAME")
    private String stateName;

    public CStates() {
    }

    public CStates(Integer sybIdentityCol) {
        this.sybIdentityCol = sybIdentityCol;
    }

    public CStates(Integer sybIdentityCol, String stateId, String stateName) {
        this.sybIdentityCol = sybIdentityCol;
        this.stateId = stateId;
        this.stateName = stateName;
    }

    public Integer getSybIdentityCol() {
        return sybIdentityCol;
    }

    public void setSybIdentityCol(Integer sybIdentityCol) {
        this.sybIdentityCol = sybIdentityCol;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sybIdentityCol != null ? sybIdentityCol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CStates)) {
            return false;
        }
        CStates other = (CStates) object;
        if ((this.sybIdentityCol == null && other.sybIdentityCol != null) || (this.sybIdentityCol != null && !this.sybIdentityCol.equals(other.sybIdentityCol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "test.CStates[sybIdentityCol=" + sybIdentityCol + "]";
    }

}
