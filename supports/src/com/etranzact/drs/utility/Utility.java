/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etranzact.drs.utility;

import com.etranzact.drs.dto.CStates;
import com.etranzact.drs.dto.ClBanks;
import com.etranzact.drs.dto.ClDispute;
import com.etranzact.drs.dto.ClMessages;
import com.etranzact.drs.dto.EChannel;
import com.etranzact.supportmanager.action.AuthenticationAction;
import com.etranzact.supportmanager.dto.User;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import com.etranzact.supportmanager.utility.Env;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import javax.faces.context.FacesContext;
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
@Name("utility")
public class Utility {

    @In
    FacesMessages facesMessages;

    public List<ClBanks> getBankList() {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        List<ClBanks> banks = new ArrayList<ClBanks>();
        FacesContext context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();

        try {
            con = Env.getConnectionTelcoDB();
            //String query = "Select * from cl_banks";
            String query = "Select * from telcodb..cl_banks";
            stat = con.createStatement();
            rs = stat.executeQuery(query);

            while (rs.next()) {
                ClBanks bank = new ClBanks();
                bank.setBankcode(rs.getString("bankcode"));
                bank.setBankname(rs.getString("bankname"));
                bank.setEmailAddress(rs.getString("emailAddress"));
                bank.setId(rs.getInt("bankcode"));
                if (!user.getUser_code().equals(bank.getBankcode())) {
                    banks.add(bank);
                }
            }
            ClBanks bank = new ClBanks();
            bank.setBankcode("000");
            bank.setBankname("eTranzact");
            bank.setEmailAddress("");
            bank.setId(0);
            //if (!user.getUser_code().equals("000"))
            {
                banks.add(bank);
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

        if (banks.size() > 0) {
            return banks;
        } else {
            return null;
        }
    }


    public String getBankListString() {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        String rtv="''";
        try {
            con = Env.getConnectionTelcoDB();
            String query = "Select * from telcodb..cl_banks";//  '','',''
            stat = con.createStatement();
            rs = stat.executeQuery(query);

            while (rs.next()) {
                ClBanks bank = new ClBanks();
                bank.setBankcode(rs.getString("bankcode"));
                bank.setBankname(rs.getString("bankname"));
                bank.setEmailAddress(rs.getString("emailAddress"));
                bank.setId(rs.getInt("bankcode"));
                if (!bank.getBankcode().equals("000")) {
                   rtv=rtv+",'"+bank.getBankcode()+"'";
                }
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


            return rtv;

    }

    public String getSeq() {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        java.text.DecimalFormat df = new DecimalFormat("000000000000");

        int x = -1;
        try {
            con = Env.getConnection4DRSDB();
            con.setAutoCommit(true);
            //String query = "Select * from cl_banks where bankcode = '" + bankcode + "'";
            String query = "Select seq from telcodb..claim_Sequencer";
            stat = con.createStatement();
            rs = stat.executeQuery(query);
            while (rs.next()) {
                x = rs.getInt(1);
            }
            if (x != -1) {
                int r = x + 1;


                query = "update telcodb..claim_Sequencer set seq = " + (r);
                stat = con.createStatement();
                int c = stat.executeUpdate(query);
                if (c > 0) {
                    x = r;
                }
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

        return df.format(x);
    }

    public ClBanks getBank(String bankcode) {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        System.out.println("Bank Code: " + bankcode);
        if (bankcode == null) {
            bankcode = "";
        }
        try {
            con = Env.getConnectionTelcoDB();
            //String query = "Select * from cl_banks where bankcode = '" + bankcode + "'";
            String query = "Select * from telcodb..cl_banks where bankcode = '" + bankcode + "'";
            stat = con.createStatement();
            rs = stat.executeQuery(query);
            ClBanks bank = null;
            while (rs.next()) {
                bank = new ClBanks();
                bank.setBankcode(rs.getString("bankcode"));
                bank.setBankname(rs.getString("bankname"));
                bank.setEmailAddress(rs.getString("emailAddress"));
                bank.setId(rs.getInt("bankcode"));
            }
            if ((bank == null || bank.getBankcode() == null) && !bankcode.equals("000")) {
                //facesMessages.add(Severity.INFO, "Acquirer with code " + bankcode + " not found. Default to eTranzact");
                bank = new ClBanks();
                bank.setBankcode("000");
                bank.setBankname("eTranzact");
                bank.setEmailAddress("");
                bank.setId(0);

            }
            if (bankcode.equals("000")) {
                bank = new ClBanks();
                bank.setBankcode("000");
                bank.setBankname("eTranzact");
                bank.setEmailAddress("");
                bank.setId(0);
            }


            return bank;


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


    }

    public ClBanks getBank(String bankcode, Connection con) {
        Statement stat = null;
        ResultSet rs = null;

        System.out.println("Bank Code: " + bankcode);
        if (bankcode == null) {
            bankcode = "";
        }
        try {
            //String query = "Select * from cl_banks where bankcode = '" + bankcode + "'";
            String query = "Select * from telcodb..cl_banks where bankcode = '" + bankcode + "'";
            stat = con.createStatement();
            rs = stat.executeQuery(query);
            ClBanks bank = null;
            while (rs.next()) {
                bank = new ClBanks();
                bank.setBankcode(rs.getString("bankcode"));
                bank.setBankname(rs.getString("bankname"));
                bank.setEmailAddress(rs.getString("emailAddress"));
                bank.setId(rs.getInt("bankcode"));
            }
            if ((bank == null || bank.getBankcode() == null) && !bankcode.equals("000")) {
                //facesMessages.add(Severity.INFO, "Acquirer with code " + bankcode + " not found. Default to eTranzact");
                bank = new ClBanks();
                bank.setBankcode("000");
                bank.setBankname("eTranzact");
                bank.setEmailAddress("");
                bank.setId(0);

            }
            if (bankcode.equals("000")) {
                bank = new ClBanks();
                bank.setBankcode("000");
                bank.setBankname("eTranzact");
                bank.setEmailAddress("");
                bank.setId(0);
            }
            return bank;


        } catch (Exception se) {
            se.printStackTrace();
            return null;
        } finally {
            try {
                rs.close();
                stat.close();

            } catch (Exception se) {
            }
            stat = null;
            rs = null;
        }


    }

    public ClMessages getMessage(String claimID, Connection con) {
        Statement stat = null;
        ResultSet rs = null;
        try {
            //String query = "SELECT * FROM cl_messages where claimid = '"+claimID+"'";
            String query = "SELECT * FROM telcodb..cl_messages where claimid = '" + claimID + "'";
            stat = con.createStatement();
            rs = stat.executeQuery(query);
            ClMessages rec = null;
            while (rs.next()) {
                rec = new ClMessages();
                rec.setSubject(rs.getString("claimID"));
                rec.setDateCreated(rs.getDate("dateCreated"));
                rec.setId(rs.getInt("message"));
                rec.setId(rs.getInt("ID"));
            }
            return rec;


        } catch (Exception se) {
            se.printStackTrace();
            return null;
        } finally {
            try {
                rs.close();
                stat.close();

            } catch (Exception se) {
            }
            stat = null;
            rs = null;
        }


    }

    public ClMessages getMessage(String claimID) {
        Statement stat = null;
        Connection con = null;

        ResultSet rs = null;
        try {
            con = Env.getConnection4DRSDB();
            //String query = "SELECT * FROM cl_messages where claimid = '"+claimID
            String query = "SELECT * FROM telcodb..cl_messages where claimid = '" + claimID + "'";
            stat = con.createStatement();
            rs = stat.executeQuery(query);
            ClMessages rec = null;
            while (rs.next()) {
                rec = new ClMessages();
                rec.setSubject(rs.getString("claimID"));
                rec.setDateCreated(rs.getDate("dateCreated"));
                rec.setMessage(rs.getString("message"));
                rec.setId(rs.getInt("ID"));
            }
            return rec;


        } catch (Exception se) {
            se.printStackTrace();
            ClMessages rec = new ClMessages();
            rec.setMessage("");
            return rec;
        } finally {
            try {
                rs.close();
                stat.close();
                con.close();

            } catch (Exception se) {
            }
            stat = null;
            rs = null;
            con = null;
        }


    }

    public String formatAmount(String amount) {

        try {
            double myAmount = Double.parseDouble(amount);
            NumberFormat formatter = NumberFormat.getIntegerInstance(Locale.ENGLISH);
            formatter.setMinimumFractionDigits(2);
            formatter.setMaximumFractionDigits(2);
            formatter.setGroupingUsed(true);
            String formated = formatter.format(myAmount);
            return formated;
        } catch (Exception dd) {
            return amount;
        }
    }

    public List<CStates> getStateList() {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        List<CStates> states = new ArrayList<CStates>();
        try {
            con = Env.getConnectionToClaimsDB();
            //String query = "Select * from c_states";
            String query = "Select * from telcodb..c_states";
            stat = con.createStatement();
            rs = stat.executeQuery(query);

            while (rs.next()) {
                CStates state = new CStates();
                state.setSybIdentityCol(rs.getInt("SYB_IDENTITY_COL"));
                state.setStateId(rs.getString("STATE_ID"));
                state.setStateName(rs.getString("STATE_NAME"));
                states.add(state);
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

        if (states.size() > 0) {
            return states;
        } else {
            return null;
        }
    }

    public List<EChannel> geteChannelList() {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        List<EChannel> list = new ArrayList<EChannel>();
        try {
            con = Env.getConnectionECardDB();
            //String query = "SELECT * FROM ecarddb..e_channel";
            String query = "SELECT * FROM e_channel";
            stat = con.createStatement();
            rs = stat.executeQuery(query);

            while (rs.next()) {
                EChannel bean = new EChannel();

                bean.setChannelId(rs.getString("CHANNEL_ID"));
                bean.setChannelName(rs.getString("CHANNEL_NAME"));
                int channelCode = -1;
                channelCode = Integer.parseInt(bean.getChannelId());
                if (channelCode == 1 || channelCode == 2 || channelCode == 3 || channelCode == 4) {
                    list.add(bean);
                }
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

        if (list.size() > 0) {
            return list;
        } else {
            return null;
        }
    }

    public EChannel geteChannelList(String channel) {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = Env.getConnectionECardDB();
            //String query = "SELECT CHANNEL_NAME FROM ecarddb..e_channel where CHANNEL_ID = '"+channel+"'";
            String query = "SELECT CHANNEL_NAME FROM e_channel where CHANNEL_ID = '" + channel + "'";
            stat = con.createStatement();
            rs = stat.executeQuery(query);
            EChannel bean = null;
            while (rs.next()) {
                bean = new EChannel();
                bean.setChannelName(rs.getString("CHANNEL_NAME"));
            }
            return bean;


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


    }

    public List<ClDispute> getClDisputeList() {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        List<ClDispute> disputes = new ArrayList<ClDispute>();
        try {
            con = Env.getConnectionTelcoDB();
            //String query = "Select * from cl_dispute";
            String query = "Select * from telcodb..cl_dispute";
            stat = con.createStatement();
            rs = stat.executeQuery(query);

            while (rs.next()) {
                ClDispute dispute = new ClDispute();
                dispute.setDispute(rs.getString("dispute"));
                dispute.setId(rs.getInt("DISPUTE_ID"));
                dispute.setDisputeid(rs.getInt("DISPUTE_ID"));
                disputes.add(dispute);
            }
            ClDispute dispute = new ClDispute();
            dispute.setDispute("Others");
            dispute.setId(1111111111);
            dispute.setDisputeid(1111111111);
            disputes.add(dispute);

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

        if (disputes.size() > 0) {
            return disputes;
        } else {
            return null;
        }
    }

    public ClDispute getClDispute(int disputeid) {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = Env.getConnectionTelcoDB();
            //String query = "Select * from cl_dispute where id ='" + disputeid + "'";
            String query = "Select * from telcodb..cl_dispute where DISPUTE_ID = '" + disputeid + "'";
            stat = con.createStatement();
            rs = stat.executeQuery(query);
            ClDispute dispute = null;
            while (rs.next()) {
                dispute = new ClDispute();
                dispute.setDispute(rs.getString("dispute"));
                dispute.setId(rs.getInt("DISPUTE_ID"));
                dispute.setDisputeid(rs.getInt("DISPUTE_ID"));
            }
            if (disputeid == 1111111111) {

                dispute = new ClDispute();
                dispute.setDispute("Others");
                dispute.setId(disputeid);
                dispute.setDisputeid(disputeid);
            }
            if(dispute==null)
            {
               dispute = new ClDispute();
                dispute.setDispute("Others");
                dispute.setId(disputeid);
                dispute.setDisputeid(disputeid);
            }
            return dispute;


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


    }

    public String generateRandomNumber(int size) {
        String value = "";
        for (int t = 0; t < size; t++) {
            value = value + new Random().nextInt(9);
        }
        return value;
    }

    public static void main(String f[]) {
        Utility u = new Utility();
        com.etz.mobile.security.CardAudit cardAudit = new com.etz.mobile.security.CardAudit();//502192    445732586518
        //String PAN = cardAudit.deduceCardFromTrack2("5021920848960047033");//5021920848960047033
        String PAN = cardAudit.deduceTrack2FromCard("502192","0848960047030005");//5021920848960047033
        System.out.println(PAN);
    }

    public String decodeUser(Connection con, String closedUser) {
        if (closedUser == null) {
            return "";
        } else if (closedUser.equals("000") || closedUser.isEmpty()) {
            return "System";
        } else {
            Statement stat = null;
            ResultSet rs = null;
            String ss = "";

            try {
                if (con == null) {
                    con = Env.getConnection4DRSDB();
                }
                //String query = "SELECT * FROM cl_messages where claimid = '"+claimID+"'";
                String query = "SELECT username,LASTNAME,FIRSTNAME FROM telcodb..SUPPORT_USER where USER_ID = " + closedUser + "";
                stat = con.createStatement();
                rs = stat.executeQuery(query);
                ClMessages rec = null;
                while (rs.next()) {
                    ss = rs.getString(1) + ": " + rs.getString(3) + " " + rs.getString(2);

                }



            } catch (Exception se) {
                se.printStackTrace();
                return null;
            } finally {
                try {
                    rs.close();
                    stat.close();

                } catch (Exception se) {
                }
                stat = null;
                rs = null;
            }
            return ss;
        }
    }
}
