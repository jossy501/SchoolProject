
package com.etz.webserviceClient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cardHolder complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cardHolder">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cardExpiration" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cardNum" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="city" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="country" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="created" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="firstName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lastName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="legalIdNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="legalTypeId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="onlineBalance" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="onlineDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="phone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="photoCountryId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="photoIdNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="photoTypeId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="street" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zip" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cardHolder", propOrder = {
    "cardExpiration",
    "cardNum",
    "city",
    "country",
    "created",
    "firstName",
    "lastName",
    "legalIdNumber",
    "legalTypeId",
    "onlineBalance",
    "onlineDate",
    "phone",
    "photoCountryId",
    "photoIdNumber",
    "photoTypeId",
    "state",
    "street",
    "zip"
})
public class CardHolder {

    protected String cardExpiration;
    protected String cardNum;
    protected String city;
    protected String country;
    protected String created;
    protected String firstName;
    protected String lastName;
    protected String legalIdNumber;
    protected String legalTypeId;
    protected String onlineBalance;
    protected String onlineDate;
    protected String phone;
    protected String photoCountryId;
    protected String photoIdNumber;
    protected String photoTypeId;
    protected String state;
    protected String street;
    protected String zip;

    /**
     * Gets the value of the cardExpiration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardExpiration() {
        return cardExpiration;
    }

    /**
     * Sets the value of the cardExpiration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardExpiration(String value) {
        this.cardExpiration = value;
    }

    /**
     * Gets the value of the cardNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardNum() {
        return cardNum;
    }

    /**
     * Sets the value of the cardNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardNum(String value) {
        this.cardNum = value;
    }

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountry(String value) {
        this.country = value;
    }

    /**
     * Gets the value of the created property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreated() {
        return created;
    }

    /**
     * Sets the value of the created property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreated(String value) {
        this.created = value;
    }

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the legalIdNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLegalIdNumber() {
        return legalIdNumber;
    }

    /**
     * Sets the value of the legalIdNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLegalIdNumber(String value) {
        this.legalIdNumber = value;
    }

    /**
     * Gets the value of the legalTypeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLegalTypeId() {
        return legalTypeId;
    }

    /**
     * Sets the value of the legalTypeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLegalTypeId(String value) {
        this.legalTypeId = value;
    }

    /**
     * Gets the value of the onlineBalance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOnlineBalance() {
        return onlineBalance;
    }

    /**
     * Sets the value of the onlineBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOnlineBalance(String value) {
        this.onlineBalance = value;
    }

    /**
     * Gets the value of the onlineDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOnlineDate() {
        return onlineDate;
    }

    /**
     * Sets the value of the onlineDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOnlineDate(String value) {
        this.onlineDate = value;
    }

    /**
     * Gets the value of the phone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the value of the phone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhone(String value) {
        this.phone = value;
    }

    /**
     * Gets the value of the photoCountryId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhotoCountryId() {
        return photoCountryId;
    }

    /**
     * Sets the value of the photoCountryId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhotoCountryId(String value) {
        this.photoCountryId = value;
    }

    /**
     * Gets the value of the photoIdNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhotoIdNumber() {
        return photoIdNumber;
    }

    /**
     * Sets the value of the photoIdNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhotoIdNumber(String value) {
        this.photoIdNumber = value;
    }

    /**
     * Gets the value of the photoTypeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhotoTypeId() {
        return photoTypeId;
    }

    /**
     * Sets the value of the photoTypeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhotoTypeId(String value) {
        this.photoTypeId = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * Gets the value of the street property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the value of the street property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStreet(String value) {
        this.street = value;
    }

    /**
     * Gets the value of the zip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZip() {
        return zip;
    }

    /**
     * Sets the value of the zip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZip(String value) {
        this.zip = value;
    }

}
