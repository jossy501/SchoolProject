package serviceclient.com;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.simple.parser.ParseException;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {
	
	private ReadJson readJos;
	
	public MyResource() {
		readJos = new ReadJson();
	}

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }
    
    @Path("/data")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getJsonData() {
    	String result = "";
    	try {
    		result = readJos.getMainData();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;
    }
    
    //http://localhost:8080/serviceclient/webapi/myresource/getText
    @GET
    @Produces("text/plain")
    @Consumes("text/plain")
    @Path("getText/")
    public String getText(@QueryParam("customerIdentifier") @DefaultValue("") String customerIdentifier) {
       return  customerIdentifier ;  
       
    }

}
