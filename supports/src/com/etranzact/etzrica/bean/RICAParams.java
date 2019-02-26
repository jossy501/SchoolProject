/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etranzact.etzrica.bean;


import java.util.Date;

/**
 *
 * @author akachukwu.ntukokwu
 */

public class RICAParams  {
 
    private Integer id;
    String userfname;
    String userlname;
    RRicaNetwork network;
    String city;
    String reason;
    String countrycode;
    String postalcode;
    String useraddress;
    String areacode;
    String dialingno;
    String region;
    String simidentifier;
    String sender;
    String idtype;
    String country;
    String idnumber;
    String type;
    String decodeType;
    String agentname;
    RAgentMaster agent;
    String authority;
    String sim;//1=Phone,2=SIM,3=REF
    String last4SIM;
    String suburb;
    String oldIdType;
    String oldCountry;
    String oldIdNumber;
    String agentNetwork;
    String responseCode;
    String responseMessage;
    Date registedDate;
    Date registedDate2;
    Date registedDate1;
    String requestID;
    String messageType;

    public String getDecodeType() {
        return decodeType;
    }

    public void setDecodeType(String decodeType) {
        this.decodeType = decodeType;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

   
    public Integer getId() {
        return id;
    }

    public RAgentMaster getAgent() {
        return agent;
    }

    public void setAgent(RAgentMaster agent) {
        this.agent = agent;
    }

    public RRicaNetwork getNetwork() {
        return network;
    }

    public void setNetwork(RRicaNetwork network) {
        this.network = network;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getRegistedDate() {
        return registedDate;
    }

    public void setRegistedDate(Date registedDate) {
        this.registedDate = registedDate;
    }

    public Date getRegistedDate1() {
        return registedDate1;
    }

    public void setRegistedDate1(Date registedDate1) {
        this.registedDate1 = registedDate1;
    }

    public Date getRegistedDate2() {
        return registedDate2;
    }

    public void setRegistedDate2(Date registedDate2) {
        this.registedDate2 = registedDate2;
    }

   

    public String getAreacode() {
        return areacode;
    }

    public String getAgentNetwork() {
        return agentNetwork;
    }

    public void setAgentNetwork(String agentNetwork) {
        this.agentNetwork = agentNetwork;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getAgentname() {
        return agentname;
    }

    public String getReason() {
        return reason;
    }

    public String getLast4SIM() {
        return last4SIM;
    }

    public void setLast4SIM(String last4SIM) {
        this.last4SIM = last4SIM;
    }

    public String getOldCountry() {
        return oldCountry;
    }

    public void setOldCountry(String oldCountry) {
        this.oldCountry = oldCountry;
    }

    public String getOldIdNumber() {
        return oldIdNumber;
    }

    public void setOldIdNumber(String oldIdNumber) {
        this.oldIdNumber = oldIdNumber;
    }

    public String getOldIdType() {
        return oldIdType;
    }

    public void setOldIdType(String oldIdType) {
        this.oldIdType = oldIdType;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setAgentname(String agentname) {
        this.agentname = agentname;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getDialingno() {
        return dialingno;
    }

    public void setDialingno(String dialingno) {
        this.dialingno = dialingno;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getIdtype() {
        return idtype;
    }

    public void setIdtype(String idtype) {
        this.idtype = idtype;
    }

 

    public String getSimidentifier() {
        return simidentifier;
    }

    public void setSimidentifier(String simidentifier) {
        this.simidentifier = simidentifier;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSim() {
        return sim;
    }

    public void setSim(String sim) {
        this.sim = sim;
    }

    public String getUseraddress() {
        return useraddress;
    }

    public void setUseraddress(String useraddress) {
        this.useraddress = useraddress;
    }

    public String getUserfname() {
        return userfname;
    }

    public void setUserfname(String userfname) {
        this.userfname = userfname;
    }

    public String getUserlname() {
        return userlname;
    }

    public void setUserlname(String userlname) {
        this.userlname = userlname;
    }
}
