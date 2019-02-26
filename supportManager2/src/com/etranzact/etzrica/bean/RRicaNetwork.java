/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.etranzact.etzrica.bean;


public class RRicaNetwork {
    
    private Integer id;
    private String networkFullname;
    private String networkCode;
    private String networkUsername;
    private String networkPassword;
//    @OneToMany(mappedBy = "network", fetch = FetchType.EAGER)
//    @Fetch(value=FetchMode.SUBSELECT)
//    private List<AgentNetworkMap> agentNetwork;

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

    public String getNetworkCode() {
        return networkCode;
    }

    public void setNetworkCode(String networkCode) {
        this.networkCode = networkCode;
    }

    public String getNetworkFullname() {
        return networkFullname;
    }

    public void setNetworkFullname(String networkFullname) {
        this.networkFullname = networkFullname;
    }

    public String getNetworkPassword() {
        return networkPassword;
    }

    public void setNetworkPassword(String networkPassword) {
        this.networkPassword = networkPassword;
    }

    public String getNetworkUsername() {
        return networkUsername;
    }

    public void setNetworkUsername(String networkUsername) {
        this.networkUsername = networkUsername;
    }


    
}
