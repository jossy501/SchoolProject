/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.etranzact.etzrica.bean;

import java.util.Date;



/**
 *
 * @author Akachukwu
 */

public class RMessages {
    
    private Integer id;
    
    private String type;
    
    private String userFirstName;
    
    private String userLastName;
    
    private String countryCode;
   
    private String postalCode;
   
    private String userAddress;
    
    private String areaCode;
    private String dialingNo;
    private String region;
    private String phoneNumber;
    private String country;
    private String iDNumber;
    private String reaquestID;
    private String iPAddress;
    private String regRefNo;
    private String responseCode;
    private String responseMsg;
//    @JoinColumn(name = "R_DeviceTable_ID", referencedColumnName = "ID")
//    @ManyToOne(fetch = FetchType.EAGER)
//    private RDeviceTable rDeviceTable;

    private String province;
    private String district;
    private String identituType;
    private String job;
    private String placeOfBirth;
    private String natinality;
    private String emailAddress;
    private String postalAddress;
    private String sex;
    private String internetAcct;


    private String reqtype;
    private String city;
    private String topupValue;
    private Date createddate;

    private Date dob;

    public RMessages() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getInternetAcct() {
        return internetAcct;
    }

    public void setInternetAcct(String internetAcct) {
        this.internetAcct = internetAcct;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getiDNumber() {
        return iDNumber;
    }

    public void setiDNumber(String iDNumber) {
        this.iDNumber = iDNumber;
    }

    public String getiPAddress() {
        return iPAddress;
    }

    public void setiPAddress(String iPAddress) {
        this.iPAddress = iPAddress;
    }

    public String getIdentituType() {
        return identituType;
    }

    public void setIdentituType(String identituType) {
        this.identituType = identituType;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getNatinality() {
        return natinality;
    }

    public void setNatinality(String natinality) {
        this.natinality = natinality;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getCreateddate() {
        return createddate;
    }

    public void setCreateddate(Date createddate) {
        this.createddate = createddate;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getReqtype() {
        return reqtype;
    }

    public void setReqtype(String reqtype) {
        this.reqtype = reqtype;
    }

    public String getTopupValue() {
        return topupValue;
    }

    public void setTopupValue(String topupValue) {
        this.topupValue = topupValue;
    }

    public RMessages(Integer id) {
        this.id = id;
    }

//    public RDeviceTable getrDeviceTable() {
//        return rDeviceTable;
//    }
//
//    public void setrDeviceTable(RDeviceTable rDeviceTable) {
//        this.rDeviceTable = rDeviceTable;
//    }

    public RMessages(Integer id, String type, String reaquestID) {
        this.id = id;
        this.type = type;
        this.reaquestID = reaquestID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getDialingNo() {
        return dialingNo;
    }

    public void setDialingNo(String dialingNo) {
        this.dialingNo = dialingNo;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIDNumber() {
        return iDNumber;
    }

    public void setIDNumber(String iDNumber) {
        this.iDNumber = iDNumber;
    }

    public String getReaquestID() {
        return reaquestID;
    }

    public void setReaquestID(String reaquestID) {
        this.reaquestID = reaquestID;
    }

    public String getIPAddress() {
        return iPAddress;
    }

    public void setIPAddress(String iPAddress) {
        this.iPAddress = iPAddress;
    }

    public String getRegRefNo() {
        return regRefNo;
    }

    public void setRegRefNo(String regRefNo) {
        this.regRefNo = regRefNo;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
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
        if (!(object instanceof RMessages)) {
            return false;
        }
        RMessages other = (RMessages) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.etz.rica.controller.RMessages[id=" + id + "]";
    }

}
