/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.etranzact.drs.dto;

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
import javax.persistence.TemporalType;

/**
 *
 * @author Akachukwu
 */
@Entity
@Table(name = "e_channel")
@NamedQueries({
    @NamedQuery(name = "EChannel.findAll", query = "SELECT e FROM EChannel e"),
    @NamedQuery(name = "EChannel.findByChannelId", query = "SELECT e FROM EChannel e WHERE e.channelId = :channelId"),
    @NamedQuery(name = "EChannel.findByChannelName", query = "SELECT e FROM EChannel e WHERE e.channelName = :channelName"),
    @NamedQuery(name = "EChannel.findByCreated", query = "SELECT e FROM EChannel e WHERE e.created = :created"),
    @NamedQuery(name = "EChannel.findByModified", query = "SELECT e FROM EChannel e WHERE e.modified = :modified")})
public class EChannel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CHANNEL_ID")
    private String channelId;
    @Basic(optional = false)
    @Column(name = "CHANNEL_NAME")
    private String channelName;
    @Basic(optional = false)
    @Column(name = "CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Column(name = "MODIFIED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;

    public EChannel() {
    }

    public EChannel(String channelId) {
        this.channelId = channelId;
    }

    public EChannel(String channelId, String channelName, Date created) {
        this.channelId = channelId;
        this.channelName = channelName;
        this.created = created;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (channelId != null ? channelId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EChannel)) {
            return false;
        }
        EChannel other = (EChannel) object;
        if ((this.channelId == null && other.channelId != null) || (this.channelId != null && !this.channelId.equals(other.channelId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "test.EChannel[channelId=" + channelId + "]";
    }

}
