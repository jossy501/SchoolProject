/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.etranzact.psm.controller;

import com.etranzact.psm.dto.TPsmUserContact;
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
@Name("tpsmusercontactcontroller")
public class UserContactController {
 @Valid
    private TPsmUserContact userContact;
    @In
    FacesMessages facesMessages;

    public TPsmUserContact getUserContact() {
        if(userContact==null)
        {
          userContact = new TPsmUserContact();
        }
        return userContact;
    }

    public void setUserContact(TPsmUserContact userContct) {
        this.userContact = userContct;
    }



    public void createUserContact() {
        Connection con=null;
        String message="";
        FacesContext context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();

        try{
            con = Env.getConnection4DRSDB();
            String query="INSERT INTO telcodb.dbo.T_PSM_USER_CONTACT (dealer_id, user_fullname, user_phone_number, user_email, "
                + "created_date, created_user) VALUES ("+this.userContact.getDealerId()+" , "
                + "'"+this.userContact.getUserFullname()+"', '"+this.userContact.getUserPhoneNumber()+"', '"+this.userContact.getUserEmail()+"',getdate() , "
                + "'"+user.getUser_code()+"') ";

            int output = con.createStatement().executeUpdate(query);
                if (output > 0) {
                    con.commit();
                    message = "User Contact successfully created";
                } else {
                    con.rollback();
                    message = "User Contact creation failed";
                }
        }
        catch(Exception x)
        {
            x.printStackTrace();
            message = "User Contact creation failed >> database error "+x.getMessage();
        }finally
        {
            try{
                con.close();
            }catch(Exception dd){}
            facesMessages.add(Severity.INFO, message);
        }

this.userContact=null;
    }
public void modifiyUserContact()
    {
    Connection con=null;
     String message = "";
    try {
        FacesContext context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();

            con = Env.getConnection4DRSDB();
            int output = -1;


                Statement stat = null;
                stat = con.createStatement();
                String query = "UPDATE 	telcodb.dbo.T_PSM_USER_CONTACT SET  dealer_id = "+this.userContact.getDealerId()+", user_fullname = '"+this.userContact.getUserFullname()+"', "
                        + "user_phone_number = '"+this.userContact.getUserPhoneNumber()+"', user_email = '"+this.userContact.getUserEmail()+"',modified_date =getdate() ,modified_user = '"+user.getUser_code()+"'  WHERE user_id = "+this.userContact.getUserId()+"";


                output = stat.executeUpdate(query);
                if (output > 0) {
                    con.commit();
                    message = "User Contact successfully modified";
                } else {
                    con.rollback();
                    message = "User Contact modification failed";
                }

        }
                catch (Exception ex) {
            ex.printStackTrace();
            message = "User Contact modification failed >> database error "+ex.getMessage();
        }
     finally
        {
            try{
                con.close();
            }catch(Exception dd){}
            facesMessages.add(Severity.INFO, message);

        }
}

 public String[] getUserEmailList(String searchKey) {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        String inValue = "";

        String[] searchKeyArray = searchKey.split("_");
        for (int t = 0; t < searchKeyArray.length; t++) {
            inValue = inValue + ",'" + searchKeyArray[t] + "'";
        }
        if (inValue.length() > 0) {
            inValue = inValue.substring(1);
        }
        List<String> lists = new ArrayList<String>();
        try {
            con = Env.getConnection4DRSDB();
            String query = "SELECT user_email FROM telcodb.dbo.T_PSM_USER_CONTACT where dealer_id in (" + inValue + ")";
            stat = con.createStatement();
            rs = stat.executeQuery(query);

            while (rs.next()) {
                lists.add(rs.getString("EMAIL"));
            }
            lists.add("akachintuko@gmail.com");

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
            con = null;
            stat = null;
            rs = null;
        }

        if (lists.size() > 0) {
            String[] rec = new String[lists.size()];
            for(int t=0;t<lists.size();t++)
            {
              rec[t]  =lists.get(t);
            }
            return rec;
        } else {
            return null;
        }
    }
    public List<TPsmUserContact> getUserContactList()
    {
        Connection con=null;
        String message="";
        List<TPsmUserContact> lists = new ArrayList<TPsmUserContact>();
        try{
            String query="SELECT * FROM telcodb.dbo.T_PSM_USER_CONTACT where 1=1";
con = Env.getConnection4DRSDB();
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(query);
            
            while (rs.next()) {
                TPsmUserContact list = new TPsmUserContact();
                list.setUserId(rs.getInt("user_id"));
                list.setDealerId(rs.getInt("dealer_id"));
                list.setUserFullname(rs.getString("user_fullname"));
                list.setUserPhoneNumber(rs.getString("user_phone_number"));
                list.setUserEmail(rs.getString("user_email"));
                list.setCreatedDate(rs.getDate("created_date"));
                list.setCreatedUser(rs.getString("created_user"));
                list.setModifiedDate(rs.getDate("modified_date"));
                list.setModifiedUser(rs.getString("modified_user"));
                list.setDealer(new DealerController().getDealer(list.getDealerId()+"", con));
                lists.add(list);
        }
            
        }
        catch(Exception x)
        {
            x.printStackTrace();
            message = "User Contact fatch failed >> database error "+x.getMessage();
        }finally
        {
            try{
                con.close();
            }catch(Exception dd){}
            facesMessages.add(Severity.INFO, message);
            //return null;
        }
        if(lists.size()>0)
            {
               return lists;
            }else
            {
                return null;
            }
    }

    public void viewUser4Edit()
    {

    }

   public void distroy() {
        this.userContact=null;
    }
}
