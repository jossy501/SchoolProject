/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.etranzact.etzrica.bean;


public class RAgentMaster  {
    
    
    private Integer id;
    
    private String agentFullname;
    private String agentAddress;
    private String agentCode;
    private String phoneNumber;
    

    public String getAgentAddress() {
        return agentAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAgentAddress(String agentAddress) {
        this.agentAddress = agentAddress;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getAgentFullname() {
        return agentFullname;
    }

    public void setAgentFullname(String agentFullname) {
        this.agentFullname = agentFullname;
    }

//    public List<AgentNetworkMap> getAgentNetwork() {
//        return agentNetwork;
//    }
//
//    public void setAgentNetwork(List<AgentNetworkMap> agentNetwork) {
//        this.agentNetwork = agentNetwork;
//    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    
}
