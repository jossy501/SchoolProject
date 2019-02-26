/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etranzact.psm.controller;

import com.etranzact.psm.dto.TPsmNetworkProvider;
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
@Name("networkprovidercontroller")
public class NetworkProviderController {

    @Valid
    private TPsmNetworkProvider provider;
    @In
    FacesMessages facesMessages;

    public TPsmNetworkProvider getProvider() {
        if (provider == null) {
            provider = new TPsmNetworkProvider();
        }
        return provider;
    }

    public void setProvider(TPsmNetworkProvider provider) {
        this.provider = provider;
    }

    public void createProvider() {
        Connection con = null;
        String message = "";
        FacesContext context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();

        try {
            con = Env.getConnection4DRSDB();
            String query = "INSERT INTO telcodb.dbo.T_PSM_NETWORK_PROVIDER "
                    + "( provider_name,providerCode, pin_length, created_date, created_user,provider_filetype) VALUES ('" + this.provider.getProviderName()
                    + "','"+this.provider.getProviderCode()+"'," + this.provider.getPinLength() + ", getdate(), '" + user.getUser_id() + "','"+this.provider.getProviderFileType()+"')";

            int output = 0;
            if (!this.checkProvider(this.provider.getProviderCode().trim(), con)) {
                output = con.createStatement().executeUpdate(query);
            } else {
                message = "Error >>>>>>>>>>>> Provider Code has been used";
            }
            if (output > 0) {
                con.commit();
                message = "Provider successfully created";
            } else {
                con.rollback();
                //message = "Provider creation failed";
            }
        } catch (Exception x) {
            x.printStackTrace();
            message = "Provider creation failed >> database error " + x.getMessage();
        } finally {
            try {
                con.close();
            } catch (Exception dd) {
            }
            facesMessages.add(Severity.INFO, message);
        }


    }

    public void modifiyProvider() {
        Connection con = null;
        String message = "";
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();

            con = Env.getConnection4DRSDB();
            int output = -1;


            Statement stat = null;
            stat = con.createStatement();
            String query = "UPDATE 	telcodb.dbo.T_PSM_NETWORK_PROVIDER SET 	provider_name = '" + this.provider.getProviderName() + "',"
                    + "pin_length = " + this.provider.getPinLength() + ",modified_date = getdate() , "
                    + "modified_user = '" + user.getUser_id() + "', provider_filetype= '"+this.provider.getProviderFileType()+"' WHERE provider_id = " + this.provider.getProviderId() + "";


            output = stat.executeUpdate(query);


            if (output > 0) {
                con.commit();
                message = "Provider successfully modified";
            } else {
                con.rollback();
                //message = "Provider modification failed";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            message = "Provider modification failed >> database error " + ex.getMessage();
        } finally {
            try {
                con.close();
            } catch (Exception dd) {
            }
            facesMessages.add(Severity.INFO, message);


        }
        this.provider = null;
    }

    public List<TPsmNetworkProvider> getProviderList() {
        Connection con = null;
        String message = "";
        List<TPsmNetworkProvider> lists = new ArrayList<TPsmNetworkProvider>();
        try {
            String query = "select * from telcodb.dbo.T_PSM_NETWORK_PROVIDER where 1=1";
            con = Env.getConnection4DRSDB();
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(query);
            //System.out.println(query);
            while (rs.next()) {
                TPsmNetworkProvider list = new TPsmNetworkProvider();
                list.setProviderId(rs.getInt("provider_id"));
                list.setProviderName(rs.getString("provider_name"));
                list.setPinLength(rs.getInt("pin_length"));
                list.setCreatedDate(rs.getDate("created_date"));
                list.setCreatedUser(rs.getString("created_user"));
                list.setModifiedDate(rs.getDate("modified_date"));
                list.setModifiedUser(rs.getString("modified_user"));
                list.setProviderCode(rs.getString("providerCode"));
                list.setProviderFileType(rs.getString("provider_filetype"));
                lists.add(list);

            }

        } catch (Exception x) {
            x.printStackTrace();
            message = "Provider fatch failed >> database error";
        } finally {
            try {
                con.close();
            } catch (Exception dd) {
            }
            try{
            facesMessages.add(Severity.INFO, message);
            }catch(Exception d){}

        }
        if (lists.size() > 0) {
            return lists;
        } else {
            return null;
        }
    }

    public TPsmNetworkProvider getProvider(String providercode, Connection con) {
        //Connection con = null;
        TPsmNetworkProvider list = null;
        try {
con = Env.getConnection4DRSDB();
            String query = "select * from telcodb.dbo.T_PSM_NETWORK_PROVIDER where providerCode = '" + providercode + "'";
//con = Env.getConnectionSupportLogDB();
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(query);
            //System.out.println(query);
            rs.next();

            list = new TPsmNetworkProvider();
            try {
                list.setProviderId(rs.getInt("provider_id"));
                list.setProviderName(rs.getString("provider_name"));
                list.setPinLength(rs.getInt("pin_length"));
                list.setCreatedDate(rs.getDate("created_date"));
                list.setCreatedUser(rs.getString("created_user"));
                list.setModifiedDate(rs.getDate("modified_date"));
                list.setModifiedUser(rs.getString("modified_user"));
                list.setProviderCode(rs.getString("providerCode"));
                list.setProviderFileType(rs.getString("provider_filetype"));
            } catch (Exception se) {
            }



        } catch (Exception x) {
            x.printStackTrace();

        } finally {
            try{
            con.close();}catch(Exception sd){}
        }
        return list;

    }

    public TPsmNetworkProvider getProvider(String providerCode) {
        Connection con = null;
        TPsmNetworkProvider list = null;
        try {

            String query = "select * from telcodb.dbo.T_PSM_NETWORK_PROVIDER where providerCode = '" + providerCode + "'";
            con = Env.getConnection4DRSDB();
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(query);
            //System.out.println(query);
            rs.next();
            list = new TPsmNetworkProvider();
            list.setProviderId(rs.getInt("provider_id"));
            list.setProviderName(rs.getString("provider_name"));
            list.setPinLength(rs.getInt("pin_length"));
            list.setCreatedDate(rs.getDate("created_date"));
            list.setCreatedUser(rs.getString("created_user"));
            list.setModifiedDate(rs.getDate("modified_date"));
            list.setModifiedUser(rs.getString("modified_user"));
            list.setProviderCode(rs.getString("providerCode"));
            list.setProviderFileType(rs.getString("provider_filetype"));




        } catch (Exception x) {
            x.printStackTrace();

        } finally {
            try {
                con.close();
            } catch (Exception dd) {
            }
        }
        return list;

    }

    private boolean checkProvider(String providercode, Connection con) {
        try {
            String query = "SELECT * FROM telcodb.dbo.T_PSM_NETWORK_PROVIDER where   providerCode='" + providercode + "'";
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(query);

            if (rs.next()) {
                return true;
            }
 else
            {
                return false;
 }

        } catch (Exception x) {
        }
        return false;
    }

    public void viewProvider4Edit() {
    }

    public void distroy() {
        this.provider = null;
    }
}
