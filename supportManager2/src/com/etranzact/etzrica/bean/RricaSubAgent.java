/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etranzact.etzrica.bean;

import java.util.Date;



public class RricaSubAgent {

    
    private Integer id;
    private String mobileNumber;
    private String mobileNumberNetwork;
    private String agentName;
    private String authority;
    private String registeredBy;
    private Date createdDate;
    private String topupValue;
    String agent;
    String network;
    String email;
    private AgentNetworkMap agentNetwork;

    public String getAgent() {
        return agent;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    

    

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public AgentNetworkMap getAgentNetwork() {
        return agentNetwork;
    }

    public void setAgentNetwork(AgentNetworkMap agentNetwork) {
        this.agentNetwork = agentNetwork;
    }

    

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(String registeredBy) {
        this.registeredBy = registeredBy;
    }

    public String getTopupValue() {
        return topupValue;
    }

    public void setTopupValue(String topupValue) {
        this.topupValue = topupValue;
    }

	public String getMobileNumberNetwork() {
		return mobileNumberNetwork;
	}

	public void setMobileNumberNetwork(String mobileNumberNetwork) {
		this.mobileNumberNetwork = mobileNumberNetwork;
	}
}
