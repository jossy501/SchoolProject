
package com.etz.sa.ws.server;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2-hudson-752-
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "SAService", targetNamespace = "http://server.ws.sa.etz.com/", wsdlLocation = "http://10.0.0.73/SA/SA?wsdl")
public class SAService
    extends Service
{

    private final static URL SASERVICE_WSDL_LOCATION;
    private final static WebServiceException SASERVICE_EXCEPTION;
    private final static QName SASERVICE_QNAME = new QName("http://server.ws.sa.etz.com/", "SAService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://10.0.0.73/SA/SA?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SASERVICE_WSDL_LOCATION = url;
        SASERVICE_EXCEPTION = e;
    }

    public SAService() {
        super(__getWsdlLocation(), SASERVICE_QNAME);
    }

    public SAService(WebServiceFeature... features) {
        super(__getWsdlLocation(), SASERVICE_QNAME, features);
    }

    public SAService(URL wsdlLocation) {
        super(wsdlLocation, SASERVICE_QNAME);
    }

    public SAService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SASERVICE_QNAME, features);
    }

    public SAService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SAService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns SA
     */
    @WebEndpoint(name = "SAPort")
    public SA getSAPort() {
        return super.getPort(new QName("http://server.ws.sa.etz.com/", "SAPort"), SA.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SA
     */
    @WebEndpoint(name = "SAPort")
    public SA getSAPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://server.ws.sa.etz.com/", "SAPort"), SA.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SASERVICE_EXCEPTION!= null) {
            throw SASERVICE_EXCEPTION;
        }
        return SASERVICE_WSDL_LOCATION;
    }

}
