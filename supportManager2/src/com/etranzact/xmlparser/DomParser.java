
package com.etranzact.xmlparser;


import java.io.File;
import java.io.StringReader;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException; 

public class DomParser
{
	
	public DomParser(){}
	
	public String[] parseString(String data)
	{
		String ret[] = new String[4];
		try 
		{
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        InputSource is = new InputSource();
	        is.setCharacterStream(new StringReader(data));

	        
	        //<securedsession><Response>0</Response><Message>Successful</Message><CheckSum>76FC7BD63EE50DB10437236383794E6C</CheckSum><Question>What is your pet name</Question></securedsession>
	        
	        Document doc = db.parse(is);
	        NodeList nodes = doc.getElementsByTagName("securedsession");

	        // iterate the employees
	        for (int i = 0; i < nodes.getLength(); i++) 
	        {
	           Element element = (Element) nodes.item(i);

	           NodeList response = element.getElementsByTagName("Response");
	           Element line = (Element) response.item(0);
	           System.out.println("Response: " + getCharacterDataFromElement(line));
	           ret[0] = getCharacterDataFromElement(line);

	           NodeList message = element.getElementsByTagName("Message");
	           line = (Element) message.item(0);
	           System.out.println("Message: " + getCharacterDataFromElement(line));
	           ret[1] = getCharacterDataFromElement(line);
	           
	           NodeList checksum = element.getElementsByTagName("CheckSum");
	           line = (Element) checksum.item(0);
	           System.out.println("CheckSum: " + getCharacterDataFromElement(line));
	           ret[2] = getCharacterDataFromElement(line);
	           
	           NodeList question = element.getElementsByTagName("Question");
	           line = (Element) question.item(0);
	           System.out.println("Question: " + getCharacterDataFromElement(line));
	           ret[3] = getCharacterDataFromElement(line);
	        }
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
		return ret;
	}
	
	

	public static String getCharacterDataFromElement(Element e) 
	{
	    Node child = e.getFirstChild();
	    if (child instanceof CharacterData) 
	    {
	       CharacterData cd = (CharacterData) child;
	       return cd.getData();
	    }
	    return "?";
	}


}