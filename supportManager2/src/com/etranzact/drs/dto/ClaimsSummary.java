/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.etranzact.drs.dto;

/**
 *
 * @author Akachukwu
 */
public class ClaimsSummary {

    private String fromBank;
    private String noOfClaim;
    private String noOfUnresolvedClaims;

    public String getNoOfClaim() {
        return noOfClaim;
    }

    public void setNoOfClaim(String NoOfClaim) {
        this.noOfClaim = NoOfClaim;
    }

    public String getNoOfUnresolvedClaims() {
        return noOfUnresolvedClaims;
    }

    public void setNoOfUnresolvedClaims(String NoOfUnresolvedClaims) {
        this.noOfUnresolvedClaims = NoOfUnresolvedClaims;
    }

    public String getFromBank() {
        return fromBank;
    }

    public void setFromBank(String fromBank) {
        this.fromBank = fromBank;
    }


}
