package com.etranzact.psm.ws;

public class Response
{
  String sessionKey;
  double pinAmount;
  double pinDeno;
  String dealerId;
  String providerId;
  String message;
  String resposeCode;
  int pinCount;
  PIN[] pins;

  public String getDealerId()
  {
    return this.dealerId;
  }

  public void setDealerId(String dealerId) {
    this.dealerId = dealerId;
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public double getPinAmount() {
    return this.pinAmount;
  }

  public void setPinAmount(double pinAmount) {
    this.pinAmount = pinAmount;
  }

  public double getPinDeno() {
    return this.pinDeno;
  }

  public void setPinDeno(double pinDeno) {
    this.pinDeno = pinDeno;
  }

  public String getProviderId()
  {
    return this.providerId;
  }

  public void setProviderId(String providerId) {
    this.providerId = providerId;
  }

  public String getSessionKey() {
    return this.sessionKey;
  }

  public void setSessionKey(String sessionKey) {
    this.sessionKey = sessionKey;
  }

  public String getResposeCode() {
    return this.resposeCode;
  }

  public void setResposeCode(String resposeCode) {
    this.resposeCode = resposeCode;
  }

  public int getPinCount() {
    return this.pinCount;
  }

  public void setPinCount(int pinCount) {
    this.pinCount = pinCount;
  }

  public PIN[] getPins() {
    return this.pins;
  }

  public void setPins(PIN[] pins) {
    this.pins = pins;
  }
}