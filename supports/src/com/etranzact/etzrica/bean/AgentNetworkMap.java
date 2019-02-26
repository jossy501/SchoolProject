/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.etranzact.etzrica.bean;


public class AgentNetworkMap {
   
    private Integer id;
    private String groupName;
    private String userName;
    private String password;
    
    private String topupValue;
    
    private String ragent;
    
    private String rnetwork;

    

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    

    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }

   

    public String getTopupValue() {
        return topupValue;
    }

    public void setTopupValue(String topupValue) {
        this.topupValue = topupValue;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

	public String getRagent() {
		return ragent;
	}

	public void setRagent(String ragent) {
		this.ragent = ragent;
	}

	public String getRnetwork() {
		return rnetwork;
	}

	public void setRnetwork(String rnetwork) {
		this.rnetwork = rnetwork;
	}


}
