/**
 * 
 */
package com.etranzact.supportmanager.utility;

/**
 * @author tony.ezeanya
 *
 */
public class HashNumber 
{

	public HashNumber()
	{
	}
	
	/*This method shows the first six characters and the last four*/
	public String hashStringValue(String value, int start, int end)
	{
		String message = "";
		
		try
		{
			message = value.substring(0,start) + "######" + value.substring(value.length()-end);
		}
		catch(Exception ex)
		{
			message = value;
			ex.printStackTrace();
		}
		return message;
	}
	
	/*This method hashes the first six characters */
	public String hashStringValue2(String value,int end)
	{
		String message = "";
		try
		{
			message = "######" + value.substring(6,value.length());
			System.out.println("message " + message);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return message;
	}
	
	/*This method hashes the specified last value characters */
	public String hashSpecifiedLastValue(String value,int end)
	{
		String message = "";
		try
		{
			message = value.substring(0, value.length() - end) + "####";
			//System.out.println("message " + message);
		}
		catch(Exception ex)
		{
			//ex.printStackTrace();
		}
		return message;
	}
	
}
