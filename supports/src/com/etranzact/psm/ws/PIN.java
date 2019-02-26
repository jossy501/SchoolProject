package com.etranzact.psm.ws;

public class PIN
{
  String pinNumber;
  String pinDeno;
  String pinValue;
  String pinSerial;

  public String getPinNumber()
  {
    return this.pinNumber;
  }

  public void setPinNumber(String pinNumber) {
    this.pinNumber = pinNumber;
  }

  public String getPinSerial() {
    return this.pinSerial;
  }

  public void setPinSerial(String pinSerial) {
    this.pinSerial = pinSerial;
  }

  public String getPinDeno() {
    return this.pinDeno;
  }

  public String getPinValue() {
    return this.pinValue;
  }

  public void setPinValue(String pinValue) {
    this.pinValue = pinValue;
  }

  public void setPinDeno(String pinDeno) {
    this.pinDeno = pinDeno;
  }
}