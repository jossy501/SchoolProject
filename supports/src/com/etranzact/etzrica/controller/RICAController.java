/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etranzact.etzrica.controller;

import com.etranzact.drs.utility.DateUtility;
import com.etranzact.drs.utility.Utility;
import com.etranzact.etzrica.bean.AgentNetworkMap;
import com.etranzact.etzrica.bean.RAgentMaster;
import com.etranzact.etzrica.bean.RICAParams;
import com.etranzact.etzrica.bean.RRicaNetwork;
import com.etranzact.etzrica.bean.RricaSubAgent;
import com.etranzact.supportmanager.action.AuthenticationAction;
import com.etranzact.supportmanager.dto.User;
import com.etranzact.supportmanager.model.AdminModel;
import com.etranzact.supportmanager.utility.Env;
import com.etz.security.util.Cryptographer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
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
@Name("ricaetzController")
public class RICAController {

    RAgentMaster agentMaster;
    List<RAgentMaster> agentMasters;
    AgentNetworkMap agentNetworkMap;
    RricaSubAgent rricaSubAgent;
    List<RricaSubAgent> rricaSubAgents;
    RRicaNetwork rRicaNetwork;
    List<AgentNetworkMap> agentNetworkMaps;
    List<RRicaNetwork> rRicaNetworks;
    RICAParams param;
    List<RICAParams> params;
    @In
    FacesMessages facesMessages;

    public List<RAgentMaster> getAgentMasters() 
    {
    	listAgent();
        return agentMasters;
    }

    public List<RricaSubAgent> getRricaSubAgents() {
        listAgentSub();
        return rricaSubAgents;
    }

    public void setRricaSubAgents(List<RricaSubAgent> rricaSubAgents) {
        this.rricaSubAgents = rricaSubAgents;
    }

    public RricaSubAgent getRricaSubAgent() {
        if(rricaSubAgent==null)
        {
           rricaSubAgent = new RricaSubAgent();
        }
        return rricaSubAgent;
    }

    public void setRricaSubAgent(RricaSubAgent rricaSubAgent) {
        this.rricaSubAgent = rricaSubAgent;
    }

    public RICAParams getParam() {
        if (param == null) {
            param = new RICAParams();
        }
        return param;
    }

    public void setParam(RICAParams param) {
        this.param = param;
    }

    public List<RICAParams> getParams() {
        return params;
    }

    public void setParams(List<RICAParams> params) {
        this.params = params;
    }

    public void setAgentMasters(List<RAgentMaster> agentMasters) {
        this.agentMasters = agentMasters;
    }

    public List<AgentNetworkMap> getAgentNetworkMaps() {
        listNetworkAgentMap();
        return agentNetworkMaps;
    }

    public void setAgentNetworkMaps(List<AgentNetworkMap> agentNetworkMaps) {
        this.agentNetworkMaps = agentNetworkMaps;
    }

    public List<RRicaNetwork> getrRicaNetworks() {
    	listNetwork();
        return rRicaNetworks;
    }

    public void setrRicaNetworks(List<RRicaNetwork> rRicaNetworks) {
        this.rRicaNetworks = rRicaNetworks;
    }

    public AgentNetworkMap getAgentNetworkMap() {

        if (agentNetworkMap == null) {
            agentNetworkMap = new AgentNetworkMap();
        }
        return agentNetworkMap;
    }

    public void setAgentNetworkMap(AgentNetworkMap agentNetworkMap) {
        this.agentNetworkMap = agentNetworkMap;
    }

    public RAgentMaster getAgentMaster() {
        if (agentMaster == null) {
            agentMaster = new RAgentMaster();
        }
        return agentMaster;
    }

    public void setAgentMaster(RAgentMaster agentsMasters) {
        this.agentMaster = agentsMasters;
    }

    public RRicaNetwork getrRicaNetwork() {
        if (rRicaNetwork == null) {
            rRicaNetwork = new RRicaNetwork();
        }
        return rRicaNetwork;
    }

    public void setrRicaNetwork(RRicaNetwork rRicaNetwork) {
        this.rRicaNetwork = rRicaNetwork;
    }

    public void createAgentMaster() {
        //save(this.agentMaster);
    }

    public void modifyAgentMaster() {
        modify(this.agentMaster);
    }

    private void save(Object obj) {
        try {
            EntityManager em = new Util().getEntitymanager();
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
            em.close();
        } catch (Exception xc) {
            xc.printStackTrace();
        }
    }

    public void listAgentMaster() {
        this.agentMasters = (List) this.selectObject("RAgentMaster");
    }

    private void modify(Object obj) {
        try {
            EntityManager em = new Util().getEntitymanager();
            em.getTransaction().begin();
            em.merge(obj);
            em.getTransaction().commit();
            em.close();
        } catch (Exception xc) {
            xc.printStackTrace();
        }
    }

    public void listRICAMessages() {
        listRICAParams();
    }

    public List selectObject(String objectName) {
        EntityManager em = new Util().getEntitymanager();
        em.getTransaction().begin();
        List r = em.createQuery("SELECT r FROM " + objectName + " r").getResultList();
        em.flush();
        em.close();
        return r;

    }

    public void listRICAParams() {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        FacesContext context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
        DateUtility dateUtil = new DateUtility();
        String searchype = null;


        List<RICAParams> lists = new ArrayList<RICAParams>();
        try {
            con = Env.getConnection4ricaDB();
            String query = "Select * from r_RICAParams where 1=1";

            query = query + " order by registedDate desc";
            System.out.println(query);
            stat = con.createStatement();
            rs = stat.executeQuery(query);
            while (rs.next()) {
                RICAParams list = new RICAParams();
                list.setId(rs.getInt("id"));
                list.setUserfname(rs.getString("userfname"));
                list.setUserlname(rs.getString("userlname"));
                list.setSimidentifier(rs.getString("simidentifier"));
                list.setRegistedDate(rs.getDate("registedDate"));
                list.setResponseMessage(rs.getString("responseMessage"));
                list.setResponseCode(rs.getString("responseCode"));
                list.setAgentname(rs.getString("sender"));
                list.setAgentNetwork(rs.getString("network"));
                list.setType(rs.getString("type"));
                list.setRequestID(rs.getString("RequestID"));
                list.setMessageType(rs.getString("MessageType"));
                //list.setNetwork(rs.getString("network"));
                //list.setAgent(rs.getDate("agent"));

                lists.add(list);
            }

        } catch (Exception se) {
            se.printStackTrace();
            //return null;
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
            this.setParams(lists);
        } else {
            this.setParams(null);
        }
    }


    public void getMasterByID(String masterID) {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        FacesContext context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
        DateUtility dateUtil = new DateUtility();
        String searchype = null;


        List<RICAParams> lists = new ArrayList<RICAParams>();
        try {
            con = Env.getConnection4ricaDB();
            String query = "Select * from r_RICAParams where 1=1";

            query = query + " order by registedDate desc";
            System.out.println(query);
            stat = con.createStatement();
            rs = stat.executeQuery(query);
            while (rs.next()) {
                RICAParams list = new RICAParams();
                list.setId(rs.getInt("id"));
                list.setUserfname(rs.getString("userfname"));
                list.setUserlname(rs.getString("userlname"));
                list.setSimidentifier(rs.getString("simidentifier"));
                //list.setNetwork(rs.getString("network"));
                //list.setAgent(rs.getDate("agent"));

                lists.add(list);
            }

        } catch (Exception se) {
            se.printStackTrace();
            //return null;
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
            this.setParams(lists);
        } else {
            this.setParams(null);
        }
    }



    public void createAgent() {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        FacesContext context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
        DateUtility dateUtil = new DateUtility();
        String searchype = null;


        List<RICAParams> lists = new ArrayList<RICAParams>();
        try {
            con = Env.getConnection4ricaDB();
            String query = "INSERT INTO r_agent_master(Agent_address, agent_code, Agent_fullname, phone_number)"
                    + " VALUES "
                    + "('"+this.getAgentMaster().getAgentAddress()+"', '"+this.getAgentMaster().getAgentCode()+"', '"+this.getAgentMaster().getAgentFullname()+"', '"+this.getAgentMaster().getPhoneNumber()+"')";
             System.out.println(query);
            stat = con.createStatement();
            stat.executeUpdate(query);
            facesMessages.add(Severity.INFO, "Agent successfully created");

        } catch (Exception se) {
            se.printStackTrace();
            //return null;
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

        listAgent();
    }
public void createAgentNetwork() {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        FacesContext context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
        DateUtility dateUtil = new DateUtility();
        String searchype = null;


        List<RICAParams> lists = new ArrayList<RICAParams>();
        try {
            con = Env.getConnection4ricaDB();
            String query = "INSERT INTO r_agent_network_map"
                    + "(Group_name, password, topup_value, user_name, R_agent_master, Rica_network) "
                    + "VALUES ('"+this.getAgentNetworkMap().getGroupName()+"', '"+this.getAgentNetworkMap().getPassword()+"', '', "
                    + "'"+this.getAgentNetworkMap().getUserName()+"', '"+this.getAgentNetworkMap().getRagent()+"', '"+this.getAgentNetworkMap().getRnetwork()+"')";
             System.out.println("createAgentNetwork " + query);
            stat = con.createStatement();
            stat.executeUpdate(query);
            facesMessages.add(Severity.INFO, "Agent Network Map successfully created");

        } catch (Exception se) {
            se.printStackTrace();
            //return null;
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

        //listAgent();
    }

    public void createSubAgent() {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        FacesContext context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
        DateUtility dateUtil = new DateUtility();
        String searchype = null;
        String crypt_password = "";
        String ricaOfficerTypeId = "10000034";//this is the type id of the rica officer
        String defaultUserCode = "001";

        List<RICAParams> lists = new ArrayList<RICAParams>();
        try 
        {
             con = Env.getConnection4ricaDB();
            String query = "INSERT INTO r_ricasubagent"
                    + "(agent_name, authority, created_date, Mobile_number, registered_by, topup_value,agent,email) "
                    + "VALUES ('"+this.getRricaSubAgent().getAgentName()+"', '"+this.getRricaSubAgent().getAuthority()+"',getdate(), "
                    + "'"+this.getRricaSubAgent().getMobileNumber()+"', '"+user.getUser_id()+"', '0', "+
                    "'"+this.getRricaSubAgent().getAgent()+"','"+this.getRricaSubAgent().getEmail()+"')";
            System.out.println(query);
            stat = con.createStatement();
            stat.executeUpdate(query);
            facesMessages.add(Severity.INFO, "RICA Officer successfully created");

            /*
             * (String email, String password,String lname, String fname,
			   String mobile, String type_id,String status_id ,String ip_address, String user_code,
			   String createdbyuser_id, String username, String service_id, String esa_auth)
             */
            
            Cryptographer crpt = new Cryptographer();
			crypt_password = crpt.doMd5Hash(this.getRricaSubAgent().getMobileNumber() + new Utility().generateRandomNumber(8));
			
            new AdminModel().createUser(this.getRricaSubAgent().getEmail(),
            		crypt_password,
                    this.getRricaSubAgent().getAgentName(), this.getRricaSubAgent().getAgentName(),this.getRricaSubAgent().getMobileNumber(),
                    ricaOfficerTypeId, "", "", defaultUserCode,
                    user.getUser_id(), this.getRricaSubAgent().getMobileNumber(), "RUSER"+this.getRricaSubAgent().getMobileNumber(),"1");

        } catch (Exception se) {
            se.printStackTrace();
            //return null;
        } finally {
            try {
                /*rs.close();*/
                stat.close();
                con.close();
            } catch (Exception se) {
            }
            con = null;
            stat = null;
            rs = null;
        }

        //listAgent();
    }

    public void getNetworkByID(String networkID) {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        FacesContext context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
        DateUtility dateUtil = new DateUtility();
        String searchype = null;


        List<RICAParams> lists = new ArrayList<RICAParams>();
        try {
            con = Env.getConnection4ricaDB();
            String query = "Select * from r_RICAParams where 1=1";

            query = query + " order by registedDate desc";
            System.out.println(query);
            stat = con.createStatement();
            rs = stat.executeQuery(query);
            while (rs.next()) {
                RICAParams list = new RICAParams();
                list.setId(rs.getInt("id"));
                list.setUserfname(rs.getString("userfname"));
                list.setUserlname(rs.getString("userlname"));
                list.setSimidentifier(rs.getString("simidentifier"));
                //list.setNetwork(rs.getString("network"));
                //list.setAgent(rs.getDate("agent"));

                lists.add(list);
            }

        } catch (Exception se) {
            se.printStackTrace();
            //return null;
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
            params = lists;
        } else {
        	params = null;
        }
    }

    public void listAgent() {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        FacesContext context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
        DateUtility dateUtil = new DateUtility();
        String searchype = null;


        List<RAgentMaster> lists = new ArrayList<RAgentMaster>();
        try {
            con = Env.getConnection4ricaDB();
            String query = "Select * from r_agent_master where 1=1";

            //query = query + " order by registedDate desc";
            System.out.println(query);
            stat = con.createStatement();
            rs = stat.executeQuery(query);//INSERT INTO ecarddb.dbo.r_agent_master(ID, Agent_address, agent_code, Agent_fullname, phone_number) VALUES (1, 'Lagos', '343', 'ETZ', '08060738434');

            while (rs.next()) {
                RAgentMaster list = new RAgentMaster();
                list.setId(rs.getInt("id"));
                list.setAgentAddress(rs.getString("Agent_address"));
                list.setAgentCode(rs.getString("agent_code"));
                list.setAgentFullname(rs.getString("Agent_fullname"));
                list.setPhoneNumber(rs.getString("phone_number"));
                //list.setAgentFullname(rs.getString("simidentifier"));
                //list.setNetwork(rs.getString("network"));
                //list.setAgent(rs.getDate("agent"));

                lists.add(list);
            }

        } catch (Exception se) {
            se.printStackTrace();
            //return null;
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
            this.setAgentMasters(lists);
        } else {
            this.setAgentMasters(null);
        }
    }
    
    public void listNetwork() {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        FacesContext context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
        DateUtility dateUtil = new DateUtility();
        String searchype = null;


        List<RRicaNetwork> lists = new ArrayList<RRicaNetwork>();
        try {
            con = Env.getConnection4ricaDB();
            String query = "Select * from r_rica_network where 1=1";

            //query = query + " order by registedDate desc";
            System.out.println(query);
            stat = con.createStatement();
            rs = stat.executeQuery(query);//INSERT INTO ecarddb.dbo.r_agent_master(ID, Agent_address, agent_code, Agent_fullname, phone_number) VALUES (1, 'Lagos', '343', 'ETZ', '08060738434');

            while (rs.next()) {
            	RRicaNetwork list = new RRicaNetwork();
                list.setId(rs.getInt("id"));
                list.setNetworkCode(rs.getString("network_code"));
                list.setNetworkFullname(rs.getString("network_fullname"));
                list.setNetworkPassword(rs.getString("network_password"));
                list.setNetworkUsername(rs.getString("network_username"));
                //list.setAgentFullname(rs.getString("simidentifier"));
                //list.setNetwork(rs.getString("network"));
                //list.setAgent(rs.getDate("agent"));

                lists.add(list);
            }

        } catch (Exception se) {
            se.printStackTrace();
            //return null;
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
            this.setrRicaNetworks(lists);
        } else {
            this.setrRicaNetworks(null);
        }
    }


    public void listNetworkAgentMap() {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        FacesContext context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
        DateUtility dateUtil = new DateUtility();
        String searchype = null;

//INSERT INTO ecarddb.dbo.r_agent_network_map(ID, Group_name, password, topup_value, user_name, R_agent_master, Rica_network) VALUES (5000002, 'support', 'password', ' ', 'Ajegunle', '343', 'MTN');

        List<AgentNetworkMap> lists = new ArrayList<AgentNetworkMap>();
        try {
            con = Env.getConnection4ricaDB();
            String query = "Select * from r_agent_network_map where 1=1";

            //query = query + " order by registedDate desc";
            System.out.println(query);
            stat = con.createStatement();
            rs = stat.executeQuery(query);//INSERT INTO ecarddb.dbo.r_agent_master(ID, Agent_address, agent_code, Agent_fullname, phone_number) VALUES (1, 'Lagos', '343', 'ETZ', '08060738434');

            while (rs.next()) {
            	AgentNetworkMap list = new AgentNetworkMap();
                list.setId(rs.getInt("id"));
                list.setGroupName(rs.getString("Group_name"));
                list.setPassword(rs.getString("password"));
                list.setRagent(rs.getString("R_agent_master"));
                list.setRnetwork(rs.getString("Rica_network"));
                list.setTopupValue(rs.getString("topup_value"));
                list.setUserName(rs.getString("user_name"));
                //list.setAgent(rs.getDate("agent"));

                lists.add(list);
            }

        } catch (Exception se) {
            se.printStackTrace();
            //return null;
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
            this.setAgentNetworkMaps(lists);
        } else {
            this.setAgentNetworkMaps(null);
        }
    }


     public void listAgentSub() {
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        FacesContext context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
        DateUtility dateUtil = new DateUtility();
        String searchype = null;

//INSERT INTO ecarddb.dbo.r_ricasubagent(ID, , , , , , , , ) VALUES (5000003, 'Stephen Ndlovu', '1', '2012-06-25 20:28:37.013', '27832125401', '0833006055', '10', 3, '27832125401::MTN');

        List<RricaSubAgent> lists = new ArrayList<RricaSubAgent>();
        try {
            con = Env.getConnection4ricaDB();
            String query = "Select * from r_ricasubagent where 1=1";

            query = query + " order by created_date desc";
            System.out.println(query);
            stat = con.createStatement();
            rs = stat.executeQuery(query);//INSERT INTO ecarddb.dbo.r_agent_master(ID, Agent_address, agent_code, Agent_fullname, phone_number) VALUES (1, 'Lagos', '343', 'ETZ', '08060738434');

            while (rs.next()) {
            	RricaSubAgent list = new RricaSubAgent();
                list.setId(rs.getInt("id"));
                list.setAgentName(rs.getString("agent_name"));
                list.setAgentNetwork(null);
                list.setAuthority(rs.getString("authority"));
                list.setCreatedDate(rs.getDate("created_date"));
                list.setTopupValue(rs.getString("topup_value"));
                list.setMobileNumber(rs.getString("Mobile_number"));
                //list.setMobileNumberNetwork(rs.getString("MobileNumber_Network"));
                list.setRegisteredBy(rs.getString("registered_by"));
                list.setTopupValue(rs.getString("topup_value"));

                //list.setAgent(rs.getDate("agent"));

                lists.add(list);
            }

        } catch (Exception se) {
            se.printStackTrace();
            //return null;
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
            this.setRricaSubAgents(lists);
        } else {
            this.setRricaSubAgents(null);
        }
    }


     public void ricaUser()
    {
         String  resp = "";
         FacesContext context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
         try{
        String url="http://172.16.10.101:8080/ThirdPartyAppProcessor/invoke.jsp?"
      
                + "providerid=etzrica&clientid=rica000001&xmlinfo="
                + "<Request><clientID>rica000001</clientID>"
                + "<providerID>etzrica</providerID>"
                + "<providerName>etzrica</providerName>"
                + "<accountNumber>35353636</accountNumber>"
                + "<otherInfo><ricaPara>"
                + "<userfname>"+URLEncoder.encode(this.getParam().getUserfname(),"UTF-8")+"</userfname>"
                + "<userlname>"+URLEncoder.encode(this.getParam().getUserlname(),"UTF-8")+"</userlname>"
                + "<network>"+URLEncoder.encode(this.getParam().getAgentNetwork(),"UTF-8")+"</network>"
                + "<city>"+URLEncoder.encode(this.getParam().getCity(),"UTF-8")+"</city>"
                + "<countrycode>"+URLEncoder.encode(this.getParam().getCountrycode(),"UTF-8")+"</countrycode>"
                + "<postalcode>"+URLEncoder.encode(this.getParam().getPostalcode(),"UTF-8")+"</postalcode>"
                + "<useraddress>"+URLEncoder.encode(this.getParam().getUseraddress(),"UTF-8")+"</useraddress>"
                + "<areacode>"+URLEncoder.encode(this.getParam().getAreacode(),"UTF-8")+"</areacode>"
                + "<dialingno>"+URLEncoder.encode(this.getParam().getDialingno(),"UTF-8")+"</dialingno>"
                + "<region>"+URLEncoder.encode(this.getParam().getRegion(),"UTF-8")+"</region>"
                + "<simidentifier>"+URLEncoder.encode(this.getParam().getSimidentifier(),"UTF-8")+"</simidentifier>"
                + "<sender>"+URLEncoder.encode(user.getUser_code(),"UTF-8")+"</sender>"
                + "<idtype>"+URLEncoder.encode(this.getParam().getIdtype(),"UTF-8")+"</idtype>"
                + "<authority>0</authority>"
                + "<agentname></agentname>"
                + "<country>"+URLEncoder.encode(this.getParam().getCountry(),"UTF-8")+"</country>"
                + "<idnumber>"+URLEncoder.encode(this.getParam().getIdnumber(),"UTF-8")+"</idnumber>"
                + "<sim>"+URLEncoder.encode(this.getParam().getSim(),"UTF-8")+"</sim>"
                + "<type>1</type>"
                + "<last4SIM>"+URLEncoder.encode(this.getParam().getLast4SIM(),"UTF-8")+"</last4SIM>"
                + "<suburb>"+URLEncoder.encode(this.getParam().getSuburb(),"UTF-8")+"</suburb>"
                + "</ricaPara></otherInfo></Request>";

        

              //para = URLEncoder.encode(para,"UTF-8");
              System.out.println("HttpMessenger::submit() - "+url);
              URL urlObject = new URL(url);
              System.out.println("HttpMessenger::submit() - "+url);
               // Actually make a connection
              HttpURLConnection connection = (HttpURLConnection)urlObject.openConnection();
               connection.setDoInput(true);

               connection.connect();
               int responseCode = connection.getResponseCode();
               System.out.println("HttpMessenger::proceed() - responseCode: "+responseCode);
              // if(responseCode == 200)
               {

                  BufferedReader in = new BufferedReader(
                   new InputStreamReader(connection.getInputStream()));
                   StringBuffer buildStr = new StringBuffer();
                   while((resp = in.readLine())!= null){
                       buildStr.append(resp);
                   }
                   System.out.println("HttpMessenger::proceed() - response: "+buildStr.toString());
                   resp = buildStr.toString();
                   //if (resp.toLowerCase().indexOf("success") > -1) submitted = 0;
               }
        }catch(Exception sd)
         {
            sd.printStackTrace();
            resp=sd.getMessage();
        }
        
        System.out.println(resp);
        try{
        String m = resp.substring(resp.indexOf("<otherInfo>")+11,resp.indexOf("</otherInfo>"));
        if(m.startsWith("000"))
        {
            resp="RICA for "+this.getParam().getSimidentifier()+" Successful: "+m.split("\\|")[1]+".";
        }else
        {
         resp="RICA for "+this.getParam().getSimidentifier()+" failed: "+m.split("\\|")[1]+".";
        }
        }catch(Exception sd)
        {

        }
        this.setParam(null);
        facesMessages.add(Severity.INFO, resp);
     }


     public void changeRicaUser()
    {
         String  resp = "";
         FacesContext context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
         try{
        String url="http://172.16.10.101:8080/ThirdPartyAppProcessor/invoke.jsp?"

                + "providerid=etzrica&clientid=rica000001&xmlinfo="
                + "<Request><clientID>rica000001</clientID>"
                + "<providerID>etzrica</providerID>"
                + "<providerName>etzrica</providerName>"
                + "<accountNumber>35353636</accountNumber>"
                + "<otherInfo><ricaPara>"
                + "<userfname>"+URLEncoder.encode(this.getParam().getUserfname(),"UTF-8")+"</userfname>"
                + "<userlname>"+URLEncoder.encode(this.getParam().getUserlname(),"UTF-8")+"</userlname>"
                + "<network>"+URLEncoder.encode(this.getParam().getAgentNetwork(),"UTF-8")+"</network>"
                + "<city>"+URLEncoder.encode(this.getParam().getCity(),"UTF-8")+"</city>"
                + "<countrycode>"+URLEncoder.encode(this.getParam().getCountrycode(),"UTF-8")+"</countrycode>"
                + "<postalcode>"+URLEncoder.encode(this.getParam().getPostalcode(),"UTF-8")+"</postalcode>"
                + "<useraddress>"+URLEncoder.encode(this.getParam().getUseraddress(),"UTF-8")+"</useraddress>"
                + "<areacode>"+URLEncoder.encode(this.getParam().getAreacode(),"UTF-8")+"</areacode>"
                + "<dialingno>"+URLEncoder.encode(this.getParam().getDialingno(),"UTF-8")+"</dialingno>"
                + "<region>"+URLEncoder.encode(this.getParam().getRegion(),"UTF-8")+"</region>"
                + "<simidentifier>"+URLEncoder.encode(this.getParam().getSimidentifier(),"UTF-8")+"</simidentifier>"
                + "<sender>"+URLEncoder.encode(user.getUser_code(),"UTF-8")+"</sender>"
                + "<idtype>"+URLEncoder.encode(this.getParam().getIdtype(),"UTF-8")+"</idtype>"
                + "<authority>0</authority>"
                + "<agentname></agentname>"
                + "<country>"+URLEncoder.encode(this.getParam().getCountry(),"UTF-8")+"</country>"
                + "<idnumber>"+URLEncoder.encode(this.getParam().getIdnumber(),"UTF-8")+"</idnumber>"
                + "<sim>"+URLEncoder.encode(this.getParam().getSim(),"UTF-8")+"</sim>"

                + "<oldcountry>"+URLEncoder.encode(this.getParam().getOldCountry(),"UTF-8")+"</oldcountry>"
                + "<oldidnumber>"+URLEncoder.encode(this.getParam().getOldIdNumber(),"UTF-8")+"</oldidnumber>"
                + "<oldidtype>"+URLEncoder.encode(this.getParam().getOldIdType(),"UTF-8")+"</oldidtype>"

                + "<type>4</type>"
                + "<last4SIM>"+URLEncoder.encode(this.getParam().getLast4SIM(),"UTF-8")+"</last4SIM>"
                + "<suburb>"+URLEncoder.encode(this.getParam().getSuburb(),"UTF-8")+"</suburb>"
                + "</ricaPara></otherInfo></Request>";



              //para = URLEncoder.encode(para,"UTF-8");
              System.out.println("HttpMessenger::submit() - "+url);
              URL urlObject = new URL(url);
              System.out.println("HttpMessenger::submit() - "+url);
               // Actually make a connection
              HttpURLConnection connection = (HttpURLConnection)urlObject.openConnection();
               connection.setDoInput(true);

               connection.connect();
               int responseCode = connection.getResponseCode();
               System.out.println("HttpMessenger::proceed() - responseCode: "+responseCode);
              // if(responseCode == 200)
               {

                  BufferedReader in = new BufferedReader(
                   new InputStreamReader(connection.getInputStream()));
                   StringBuffer buildStr = new StringBuffer();
                   while((resp = in.readLine())!= null){
                       buildStr.append(resp);
                   }
                   System.out.println("HttpMessenger::proceed() - response: "+buildStr.toString());
                   resp = buildStr.toString();
                   //if (resp.toLowerCase().indexOf("success") > -1) submitted = 0;
               }
        }catch(Exception sd)
         {
            sd.printStackTrace();
            resp=sd.getMessage();
        }

        System.out.println(resp);
        try{
        String m = resp.substring(resp.indexOf("<otherInfo>")+11,resp.indexOf("</otherInfo>"));
        if(m.startsWith("000"))
        {
            resp="RICA for "+this.getParam().getSimidentifier()+" Successful: "+m.split("\\|")[1]+".";
        }else
        {
         resp="RICA for "+this.getParam().getSimidentifier()+" failed: "+m.split("\\|")[1]+".";
        }
        }catch(Exception sd)
        {

        }
        this.setParam(null);
        facesMessages.add(Severity.INFO, resp);
     }


     public void submitDeRica()
    {
         String  resp = "";
         FacesContext context = FacesContext.getCurrentInstance();
        User user = ((AuthenticationAction) context.getExternalContext().getSessionMap().get("authenticator")).getCurUser();
        try{
        String url="http://172.16.10.101:8080/ThirdPartyAppProcessor/invoke.jsp?"

        
                + "providerid=etzrica&clientid=rica000001&xmlinfo="
                + "<Request><clientID>rica000001</clientID>"
                + "<providerID>etzrica</providerID>"
                + "<providerName>etzrica</providerName>"
                + "<accountNumber>35353636</accountNumber>"
                + "<otherInfo><ricaPara>"
                + "<userfname></userfname>"
                + "<userlname></userlname>"
                + "<network>"+URLEncoder.encode(this.getParam().getAgentNetwork(),"UTF-8")+"</network>"
                + "<city></city>"
                + "<countrycode>"+this.getParam().getCountrycode()+"</countrycode>"
                + "<postalcode></postalcode>"
                + "<useraddress></useraddress>"
                + "<areacode></areacode>"
                + "<dialingno></dialingno>"
                + "<region></region>"
                + "<simidentifier>"+URLEncoder.encode(this.getParam().getSimidentifier(),"UTF-8")+"</simidentifier>"
                + "<sender>"+user.getUser_code()+"</sender>"
                + "<idtype>"+URLEncoder.encode(this.getParam().getIdtype(),"UTF-8")+"</idtype>"
                + "<authority>0</authority>"
                + "<agentname></agentname>"
                + "<country>"+this.getParam().getCountry()+"</country>"
                + "<idnumber>"+URLEncoder.encode(this.getParam().getIdnumber(),"UTF-8")+"</idnumber>"
                + "<sim>"+this.getParam().getSim()+"</sim>"
                + "<type>3</type>"
                + "<reason>"+URLEncoder.encode(this.getParam().getReason(),"UTF-8")+"</reason>"
                + "<last4SIM></last4SIM>"
                + "<suburb></suburb>"
                + "</ricaPara></otherInfo></Request>";

         
         //para = URLEncoder.encode(para,"UTF-8");
              System.out.println("HttpMessenger::submit() - "+url);
              URL urlObject = new URL(url);
               // Actually make a connection
              HttpURLConnection connection = (HttpURLConnection)urlObject.openConnection();
               connection.setDoInput(true);

               connection.connect();
               int responseCode = connection.getResponseCode();
               System.out.println("HttpMessenger::proceed() - responseCode: "+responseCode);
              // if(responseCode == 200)
               {

                  BufferedReader in = new BufferedReader(
                   new InputStreamReader(connection.getInputStream()));
                   StringBuffer buildStr = new StringBuffer();
                   while((resp = in.readLine())!= null){
                       buildStr.append(resp);
                   }
                   System.out.println("HttpMessenger::proceed() - response: "+buildStr.toString());
                   resp = buildStr.toString();
                   //if (resp.toLowerCase().indexOf("success") > -1) submitted = 0;
               }
        }catch(Exception sd)
         {
            sd.printStackTrace();
            resp=sd.getMessage();
        }
        try
        {
        String m = resp.substring(resp.indexOf("<otherInfo>")+11,resp.indexOf("</otherInfo>"));
        if(m.startsWith("000"))
        {
            resp="De-RICA for "+this.getParam().getSimidentifier()+" Successful: "+m.split("\\|")[1]+".";
        }else
        {
         resp="De-RICA for "+this.getParam().getSimidentifier()+" failed: "+m.split("\\|")[1]+".";
        }
        }catch(Exception sd)
        {

        }
        this.setParam(null);
        facesMessages.add(Severity.INFO, resp);
     }
}
