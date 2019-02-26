/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etranzact.psm.controller;

import com.etranzact.psm.dto.TPsmDealer;
import com.etranzact.supportmanager.action.AuthenticationAction;
import com.etranzact.supportmanager.dto.User;
import com.etranzact.supportmanager.utility.Env;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import org.hibernate.validator.Valid;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;

/**
 *
 * @author akachukwu.ntukokwu
 */
@Scope(ScopeType.SESSION)
@Name("dealercontroller")
public class DealerController {

    @Valid
    private TPsmDealer dealer;
    @In
    FacesMessages facesMessages;

    public TPsmDealer getDealer() {
        if (dealer == null) {
            dealer = new TPsmDealer();
        }
        return dealer;
    }

    public void setDealer(TPsmDealer dealer) {
        this.dealer = dealer;
    }

    public TPsmDealer getDealer(String dealerCode, Connection con) {
        Statement stat = null;
        ResultSet rs = null;
        try {
            //String query = "Select * from cl_banks where bankcode = '" + bankcode + "'";
            con = Env.getConnection4DRSDB();
            String query = "Select * from telcodb.dbo.T_PSM_DEALER where dealer_code = '" + dealerCode + "'";
            stat = con.createStatement();
            rs = stat.executeQuery(query);
            TPsmDealer list = new TPsmDealer();
            try {
                rs.next();

                list.setDealerId(rs.getString("dealer_Code"));
                list.setDealerName(rs.getString("dealer_name"));
                list.setDealerAddress(rs.getString("dealer_address"));
                list.setDealerPhoneNumber(rs.getString("dealer_phone_number"));
                list.setDealerEmail(rs.getString("dealer_email"));
                list.setCreatedDate(rs.getDate("created_date"));
                list.setCreatedUser(rs.getString("created_user"));
                list.setModifiedDate(rs.getDate("modified_date"));
                list.setModifiedUser(rs.getString("modified_user"));
            } catch (Exception se) {
            }

            return list;


        } catch (Exception se) {
            se.printStackTrace();
            return null;
        } finally {
            try {
                rs.close();
                stat.close();
                con.close();

            } catch (Exception se) {
            }
            stat = null;
            rs = null;
        }


    }

    public TPsmDealer getDealer(String dealerCode) {
        Statement stat = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = Env.getConnection4DRSDB();
            //String query = "Select * from cl_banks where bankcode = '" + bankcode + "'";
            String query = "Select * from telcodb.dbo.T_PSM_DEALER where dealer_code = '" + dealerCode + "'";
            stat = con.createStatement();
            rs = stat.executeQuery(query);

            rs.next();
            TPsmDealer list = new TPsmDealer();
            list.setDealerId(rs.getString("dealer_Code"));
            list.setDealerName(rs.getString("dealer_name"));
            list.setDealerAddress(rs.getString("dealer_address"));
            list.setDealerPhoneNumber(rs.getString("dealer_phone_number"));
            list.setDealerEmail(rs.getString("dealer_email"));
            list.setCreatedDate(rs.getDate("created_date"));
            list.setCreatedUser(rs.getString("created_user"));
            list.setModifiedDate(rs.getDate("modified_date"));
            list.setModifiedUser(rs.getString("modified_user"));

            return list;


        } catch (Exception se) {
            se.printStackTrace();
            return null;
        } finally {
            try {
                rs.close();
                stat.close();
                con.close();

            } catch (Exception se) {
            }
            stat = null;
            rs = null;
        }


    }

    public void createDealer() {
        Connection con = null;
        String message = "";
        FacesContext context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();

        try {
            con = Env.getConnection4DRSDB();
            String query = "INSERT INTO telcodb.dbo.T_PSM_DEALER(dealer_name, dealer_address, dealer_phone_number, dealer_email, "
                    + "created_date, created_user,dealer_code) VALUES  "
                    + "('" + this.dealer.getDealerName() + "', '" + this.dealer.getDealerAddress() + "', '" + this.dealer.getDealerPhoneNumber() + "', "
                    + "'" + this.dealer.getDealerEmail() + "',getdate() , '" + user.getUser_code() + "','" + this.dealer.getDealerId() + "')";
            int output = 0;
            if (!checkDearName(this.dealer.getDealerId().trim(),con)) {
                output = con.createStatement().executeUpdate(query);
            } else {
                message = "Error >>>>> Dealer name has been used.";
            }
            if (output > 0) {
                con.commit();
                message = "Dealer successfully created";
            } else {
                con.rollback();
                //message = "Dealer creation failed";
            }
        } catch (Exception x) {
            x.printStackTrace();
            message = "Dealer creation failed >> database error " + x.getMessage();
        } finally {
            try {
                con.close();
            } catch (Exception dd) {
            }
            facesMessages.add(Severity.INFO, message);
        }
        this.dealer=null;

    }

    public void modifiyDealer() {
        Connection con = null;
        String message = "";
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();

            con = Env.getConnection4DRSDB();
            int output = -1;


            Statement stat = null;
            stat = con.createStatement();
            String query = "UPDATE 	telcodb.dbo.T_PSM_DEALER SET dealer_address = '" + this.dealer.getDealerAddress() + "', dealer_phone_number = '" + this.dealer.getDealerPhoneNumber() + "',"
                    + "dealer_email = '" + this.dealer.getDealerEmail() + "', modified_date = getdate(),modified_user = '" + user.getUser_code() + "' WHERE dealer_code='" + this.dealer.getDealerId() + "'";


            output = stat.executeUpdate(query);
            if (output > 0) {
                con.commit();
                message = "Dealer successfully modified";
            } else {
                con.rollback();
                message = "Dealer modification failed";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            message = "Dealer modification failed >> database error " + ex.getMessage();
        } finally {
            try {
                con.close();
            } catch (Exception dd) {
            }
            facesMessages.add(Severity.INFO, message);

        }
    }

    public List<TPsmDealer> getDealerList() {
        Connection con = null;
        String message = "";
        List<TPsmDealer> lists = new ArrayList<TPsmDealer>();
        try {
            con = Env.getConnection4DRSDB();
            String query = "SELECT * FROM telcodb.dbo.T_PSM_DEALER where 1=1 ";

            FacesContext context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
        if(!user.getUser_code().equals("000"))
        {
         query = query+" and dealer_code= '"+user.getUser_code()+"'";
        }
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(query);

            while (rs.next()) {
                TPsmDealer list = new TPsmDealer();
                list.setDealerId(rs.getString("dealer_Code"));
                list.setDealerName(rs.getString("dealer_name"));
                list.setDealerAddress(rs.getString("dealer_address"));
                list.setDealerPhoneNumber(rs.getString("dealer_phone_number"));
                list.setDealerEmail(rs.getString("dealer_email"));
                list.setCreatedDate(rs.getDate("created_date"));
                list.setCreatedUser(rs.getString("created_user"));
                list.setModifiedDate(rs.getDate("modified_date"));
                list.setModifiedUser(rs.getString("modified_user"));
                lists.add(list);
            }

        } catch (Exception x) {
            x.printStackTrace();
            message = "Dealer fatch failed >> database error " + x.getMessage();
        } finally {
            try {
                con.close();
            } catch (Exception dd) {
            }
            facesMessages.add(Severity.INFO, message);
            // return null;
        }
        if (lists.size() > 0) {
            return lists;
        } else {
            return null;
        }
    }

    public void viewDealer4Modify() {
    }

    private boolean checkDearName(String dealerName, Connection con) {
        try {
            String query = "SELECT * FROM telcodb.dbo.T_PSM_DEALER where  dealer_Code='" + dealerName + "'";
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(query);

            if (rs.next()) {
                return true;
            }

        } catch (Exception x) {
           
        }
        return false;
    }

   public  void distroy() {
        this.dealer=null;
    }
}
