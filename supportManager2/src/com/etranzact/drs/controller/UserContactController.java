/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etranzact.drs.controller;

import com.etranzact.drs.dto.ClUserprofile;
import com.etranzact.drs.dto.TransactionSearch;
import com.etranzact.drs.dto.UserSearchBean;
import com.etranzact.drs.utility.DateUtility;
import com.etranzact.drs.utility.Utility;
import com.etranzact.supportmanager.action.AuthenticationAction;
import com.etranzact.supportmanager.dto.User;
import com.etranzact.supportmanager.utility.Env;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
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
 * @author Akachukwu
 */
@Scope(ScopeType.SESSION)
@Name("usercontactcontroller")
public class UserContactController {

    @Valid
    private ClUserprofile clUser;
    @In
    FacesMessages facesMessages;
    private UserSearchBean searchBean;

    public void createUser() {
        try {
            Connection con = Env.getConnection4DRSDB();
            DateUtility dateUtil = new DateUtility();
            int output = -1;
            String message = "";
            try {
                Statement stat = null;
                stat = con.createStatement();
                String query = "INSERT INTO telcodb..cl_userprofile "
                        + "(BANK_CODE, FULLNAME, EMAIL, MOBILE, STATUS, CREATED) "
                        + "VALUES ('" + clUser.getBankCode() + "', '" + clUser.getFullname() + "', '" + clUser.getEmail() + "', "
                        + "'" + clUser.getMobile() + "', '0', getdate())";


                output = stat.executeUpdate(query);
                if (output > 0) {
                    con.commit();
                    message = "User successfully created";
                } else {
                    con.rollback();
                    message = "User creation failed";
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    con.rollback();
                    message = "Error occured while creating user";
                } catch (Exception e) {
                }

            } finally {
                try {
                    con.close();
                } catch (Exception s) {
                }
                facesMessages.add(Severity.INFO, message);
                this.clUser = null;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    public void editUser() {

        try {
            Connection con = Env.getConnection4DRSDB();
            int output = -1;
            String message = "";
            try {
                Statement stat = null;
                stat = con.createStatement();
                String query = "UPDATE telcodb..cl_userprofile SET BANK_CODE = '" + clUser.getBankCode() + "', "
                        + "FULLNAME = '" + clUser.getFullname() + "', EMAIL = '" + clUser.getEmail() + "', "
                        + "MOBILE = '" + clUser.getMobile() + "', STATUS = '" + clUser.getStatus() + "', MODIFIED = getdate() "
                        + "WHERE ID = " + clUser.getId() + "";


                output = stat.executeUpdate(query);
                if (output > 0) {
                    con.commit();
                    message = "User successfully modified";
                } else {
                    con.rollback();
                    message = "User modification failed";
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    con.rollback();
                    message = "Error occured while modifying user";
                } catch (Exception e) {
                }

            } finally {
                try {
                    con.close();
                } catch (Exception s) {
                }
                facesMessages.add(Severity.INFO, message);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }


    }

    public List<ClUserprofile> getUserList() {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
         DateUtility dateUtil = new DateUtility();
        Date startDate = this.getSearchBean().getStartDate();
        Date endDate = this.getSearchBean().getEndDate();
        String acquirer = this.getSearchBean().getAcquirer();
        String fulllname = this.getSearchBean().getFullname();
        String email = this.getSearchBean().getEmail();

        List<ClUserprofile> lists = new ArrayList<ClUserprofile>();
        try {
            con = Env.getConnection4DRSDB();
            String query = "select * from telcodb..cl_userprofile where 1=1";
            FacesContext context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
        if(!user.getUser_code().equals("000"))
        {
            query=query+" and BANK_CODE = '"+user.getUser_code()+"'";
        }


         if (acquirer != null && !acquirer.isEmpty()) {
                query = query + " and BANK_CODE = '" + acquirer + "'";
            }
            if (fulllname != null && !fulllname.isEmpty()) {
                query = query + " and FULLNAME = '" + fulllname + "'";
            }
            if (email != null && !email.isEmpty()) {
                query = query + " and EMAIL = '" + email + "'";
            }

            if (startDate != null) {
                if (endDate == null) {
                    endDate = new Date();
                }
                String startDateString = dateUtil.formatDate(startDate);
                String endDateString = dateUtil.formatDate(endDate);
                query = query + " and CREATED between '" + startDateString + "' and '" + endDateString + "'";
            }






            query = query+" order by CREATED desc";
            stat = con.createStatement();
            rs = stat.executeQuery(query);

            while (rs.next()) {
                ClUserprofile list = new ClUserprofile();
                list.setId(rs.getInt("ID"));
                list.setBankCode(rs.getString("BANK_CODE"));
                list.setFullname(rs.getString("FULLNAME"));
                list.setEmail(rs.getString("EMAIL"));
                list.setMobile(rs.getString("MOBILE"));
                list.setStatus(rs.getString("STATUS"));
                list.setCreated(rs.getDate("CREATED"));
                list.setModified(rs.getDate("MODIFIED"));
                list.setDecodeBank(new Utility().getBank(list.getBankCode(), con).getBankname());
                lists.add(list);
            }

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
            return lists;
        } else {
            return null;
        }
    }
public void destroy()
    {
    this.clUser=null;
    this.searchBean=null;
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
            String query = "SELECT EMAIL FROM telcodb..cl_userprofile where BANK_CODE in (" + inValue + ")";
            stat = con.createStatement();
            rs = stat.executeQuery(query);

            while (rs.next()) {
                lists.add(rs.getString("EMAIL"));
            }
            con.close();
            con = Env.getConnection4DRSDB();
            if(inValue!=null && inValue.equals("000"))
            {
             inValue="A";
            }
            query = "SELECT EMAIL FROM telcodb..SUPPORT_USER where USER_ID in (" + inValue + ")";
            stat = con.createStatement();

            try{
            rs = stat.executeQuery(query);

            while (rs.next()) {
                lists.add(rs.getString("EMAIL"));
            }
            }catch(Exception cv){}

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

    public void viewUserAction() {
    }
public UserSearchBean getSearchBean() {
        if (searchBean == null) {
            searchBean = new UserSearchBean();
        }
        return searchBean;
    }

    public void setSearchBean(UserSearchBean searchBean) {
        this.searchBean = searchBean;
    }
    

    public ClUserprofile getClUser() {
        if (clUser == null) {
            clUser = new ClUserprofile();
        }
        return clUser;
    }

    public void setClUser(ClUserprofile clUser) {
        this.clUser = clUser;
    }
}
