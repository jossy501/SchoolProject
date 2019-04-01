package serviceclient.com;

import java.io.FileNotFoundException;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.model.Address;
import com.model.Customer;


public class ReadJson {

	public static void main(String[] args) throws org.json.simple.parser.ParseException  {
		
		 JSONParser jsonParser = new JSONParser();
		 
		 try (FileReader reader = new FileReader("C:/Users/Owner/Desktop/db/applicationdata.json"))
	        {
			   //JSON parser object to parse read file
		       
	            //Read JSON file
	          //  Object obj = jsonParser.parse(reader);
			 
	
	            
	            JSONObject jobj = (JSONObject) jsonParser.parse(reader);
	       
	            JSONArray array = new JSONArray();
	            array.add(jobj.get("Data"));
	            for(int i=0;i<array.size();i++){
	            	JSONObject jsonobj_1 = (JSONObject)array.get(i);
	            	JSONArray jsonarr_2 = (JSONArray) jsonobj_1.get("Address");
	            	//System.out.println(jsonarr_2);
	            	geJsonDataForAddress(jsonarr_2);
	            	
	            	//JSONObject addresses = new JSONObject();
	            	//addresses.put("ID", jsonarr_2.get(i));
	            	//System.out.println(jsonarr_2.get("sdfds"));
	         
	            	//jsonarr_2.forEach( emp -> parseEmployeeObject( (JSONObject) emp ) );
	            	
	            }
	             
	            
	            
	           // JSONArray jsonarr_1 = (JSONArray) jobj.get("Data");
	  
	          // System.out.println(array);
	            
	            //Iterate over employee array
	           // jsonarr_1.forEach( emp -> parseEmployeeObject( (JSONObject) emp ) );
	 
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		
		 
	}
	
	public String getMainData()  throws org.json.simple.parser.ParseException {
		
		String resultsdata = "";
		
		JSONParser jsonParser = new JSONParser();
		 
		 try (FileReader reader = new FileReader("C:/Users/Owner/Desktop/db/applicationdata.json"))
	        {
			   //JSON parser object to parse read file
		       
	            //Read JSON file
	          //  Object obj = jsonParser.parse(reader);
			 
	
	            
	            JSONObject jobj = (JSONObject) jsonParser.parse(reader);
	       
	            JSONArray array = new JSONArray();
	            array.add(jobj.get("Data"));
	            for(int i=0;i<array.size();i++){
	            	JSONObject jsonobj_1 = (JSONObject)array.get(i);
	            	JSONArray jsonarr_2 = (JSONArray) jsonobj_1.get("Address");
	            	//System.out.println(jsonarr_2);
	            	resultsdata = geJsonDataForAddress(jsonarr_2);
	            	
	            	//JSONObject addresses = new JSONObject();
	            	//addresses.put("ID", jsonarr_2.get(i));
	            	//System.out.println(jsonarr_2.get("sdfds"));
	         
	            	//jsonarr_2.forEach( emp -> parseEmployeeObject( (JSONObject) emp ) );
	            	
	            }
	             
	            
	            
	           // JSONArray jsonarr_1 = (JSONArray) jobj.get("Data");
	  
	          // System.out.println(array);
	            
	            //Iterate over employee array
	           // jsonarr_1.forEach( emp -> parseEmployeeObject( (JSONObject) emp ) );
	 
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		
		 return resultsdata;
	}
	public static String geJsonDataForAddress(JSONArray jsonArray) {
		return jsonArray.toJSONString();
	}
	
	
	private static String parseEmployeeObject(JSONObject address)
    {
		
		Address addressPojo = new Address();
		JSONObject jssson = new JSONObject();
		
		addressPojo.setId("ID");
		addressPojo.setAddress_1("addr1");
		addressPojo.setAddress_2("addr2");
		addressPojo.setCity("cty");
		addressPojo.setState("st");
		addressPojo.setZipCode("zipCde");
		addressPojo.setPostalCode("postalCde");
		addressPojo.setCountry("country");
		
		JSONObject addresses = new JSONObject();
		addresses.put("ID", addressPojo.getId());
		addresses.put("Address 1", addressPojo.getAddress_1());
		addresses.put("Address 2", addressPojo.getAddress_2());
		addresses.put("City", addressPojo.getCity());
		addresses.put("State", addressPojo.getState());
		addresses.put("ZipCode", addressPojo.getZipCode());
		addresses.put("Postal Code", addressPojo.getPostalCode());
		addresses.put("Country", addressPojo.getCountry());
		
		 JSONArray addressList = new JSONArray();
		 addressList.add(addresses);
		
		 System.out.println(addressList);
		 return addressList.toJSONString();
	
		//employee.put("employee", customer);
        //Get ALL data
		/*JSONObject employeeObject = (JSONObject) employee.get("Data");
        System.out.println(employeeObject);*/
        
        // get all Customer
       // JSONObject customer = (JSONObject) employee.get("Customer");   
       // System.out.println(customer);
         
        //Get employee last name
       // String lastName = (String) employeeObject.get("lastName"); 
       // System.out.println(lastName);
         //
        //Get employee website name
        //String website = (String) employeeObject.get("website");   
        //System.out.println(website);*/
    }

}
