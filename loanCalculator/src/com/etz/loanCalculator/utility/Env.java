
package com.etz.loanCalculator.utility;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;



/**
 * Resource object for retrieving database connections from the configurations of the web server through JNDI.
 * @author Joshua Aruno
 * */
public class Env
{
	/**
	 * Static variable for getting context configurations.
	 * Used by all methods to retrieve the environment context.
	 * */
	private static Context initCtx;
	private static Context envCtx;
	//InitialContext ic;
	//DataSource ds = null;
	//Connection con = null;
	//static Logger logger = Logger.getLogger(ConnectionPool.class);
	//public static String dbName = "java:comp/env/jdbc/FileArchiveSybaseDB";
	
	public static String supportDB = "java:comp/env/jdbc/supportSybaseDB";
	public static String telcoDB = "java:comp/env/jdbc/telcoSybaseDB";
	/**
	 * Static block for context environment connection.
	 * */
	static
	{
		try
		{
			initCtx = new InitialContext();
			envCtx = (Context)initCtx.lookup("java:comp/env");
		}
		catch(Exception ex)
		{
			System.out.println("Error initailizing the context environment...");
			ex.printStackTrace();
		}
	}
	
	//================= SUPPORT ===================//
	public static Connection getConnectionSupportLogDB()throws SQLException, ClassNotFoundException
	{
		try
		{
			DataSource ds = (DataSource)envCtx.lookup("jdbc/supportlog");
			Connection con = ds.getConnection();
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			
			//System.out.println("connected to support log db");
			
			return con;
		}
		catch(Exception ex)
		{
			System.out.println("Error retrieving the data source from the context...");
			ex.printStackTrace();
			return reconnectSupportLogDB();
		}
	}
	

	public static Connection reconnectSupportLogDB()
	{
		try
		{
			DataSource ds = (DataSource)envCtx.lookup("jdbc/supportlog");
			Connection con = ds.getConnection();
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			return con;
		}
		catch(Exception ex)
		{
			System.out.println("Error retrieving the data source from the context...");
			ex.printStackTrace();
			return null;
		}
	}
	
	
	public static Connection getConnectionTelcoDB()
	{
		try
		{
			DataSource ds = (DataSource)envCtx.lookup("jdbc/telcoDB");
			Connection con = ds.getConnection();
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			
			//System.out.println("connected to telco db");
			
			return con;
		}
		catch(Exception ex)
		{
			System.out.println("Error retrieving the data source from the context...");
			ex.printStackTrace();
			return reconnectTelcoDB();
		}
	}

	public static Connection reconnectTelcoDB()
	{
		try
		{
			DataSource ds = (DataSource)envCtx.lookup("jdbc/telcoDB");
			Connection con = ds.getConnection();
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			return con;
		}
		catch(Exception ex)
		{
			System.out.println("Error retrieving the data source from the context...");
			ex.printStackTrace();
			return null;
		}
	}

	

	/*public static Connection getConnectionSupportLogDB() throws SQLException, ClassNotFoundException 
	{
        try 
        {
            DataSource ds = (DataSource)initCtx.lookup(supportDB);
			Connection con = ds.getConnection();
			con.setAutoCommit(false);
            //System.out.println("Successfully connected to database!");
            return con;
        }
        catch (Exception ce)
        {
        	System.out.println("WARNING::Error occured trying to connect to support database " + ce.getMessage());
            ce.printStackTrace();
            return null;
        }
        
    }*/

}
