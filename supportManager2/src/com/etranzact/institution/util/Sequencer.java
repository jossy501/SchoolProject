package com.etranzact.institution.util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;

import com.etranzact.supportmanager.utility.Env;

public class Sequencer {
	
	
	    public synchronized String getNext(String appId,int seqLen) 
	    {
	        Connection con = null;
	        PreparedStatement ps;
	        String code;
	        String seqPad;
	        ResultSet rs;
	        con = null;
	        ps = null;
	        Statement stmt = null;
	        rs = null;
	        String sql = "";
	        code = "";
	        seqPad = "";
	        for(int i = 0; i < seqLen; i++)
	        {
	            seqPad = seqPad + "0";
	        }
	        try{ 
	        	
	        	con = connectToECard();
	        	
		        sql = "UPDATE ecarddb..e_TMCSEQUENCER SET NEXT_SEQ = NEXT_SEQ+1 WHERE APP_ID = '"+appId+"'";
		        ps = con.prepareStatement(sql);
		        ps.executeUpdate();
		        stmt = con.createStatement();
		        rs = stmt.executeQuery("SELECT NEXT_SEQ FROM ecarddb..e_TMCSEQUENCER WHERE APP_ID = '"+appId+"'");
		        if(rs.next())
		        {
		        	if (rs.getLong(1) > 999999999999L) code = "000000000001";
		            code = (new DecimalFormat(seqPad)).format(rs.getLong(1));
		        } else
		        {
		            code = seqPad.substring(1) + "1";
		            sql = "INSERT INTO ecarddb..e_TMCSEQUENCER ( APP_ID, NEXT_SEQ) VALUES ('" + appId + "', 1)";
		            ps = con.prepareStatement(sql);
		            ps.executeUpdate();
		        }	        
	       }catch(Exception ee) 
	       {
	    	   ee.printStackTrace();    	  
	       }finally{
	        try
	        {
	            if(ps != null)
	            {
	                ps.close();
	            }
	            if(rs != null)
	            {
	                rs.close();
	            }
	            if(con != null)
	            {
	                con.close();
	            }
	        }catch(Exception _e){;}
	        // Misplaced declaration of an exception variable
	       }
	        return code;
	    }
	
	    
		/*Connection to ecard database*/
		private Connection connectToECard() throws Exception
		{
			Connection con = null;
			con = Env.getConnectionECardDB();
			return con;
		}   
		
		private void closeConnectionECard(Connection con, ResultSet result, ResultSet result1)
		{
			if(result != null)
			{
				try
				{
					result.close();
					result = null;
					
					result1.close();
					result1 = null;
				}
				catch(Exception ignore){}
			}
			if(con != null)
			{
				try
				{
					con.close();
					con = null;
				}
				catch(Exception ignore){}
			}
		}


}
