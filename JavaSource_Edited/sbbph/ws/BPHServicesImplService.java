
package sbbph.ws;

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
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */


@WebServiceClient(name = "BPHServicesImplService", targetNamespace = "http://ws.sbbph/", wsdlLocation = "http://10.29.202.55:8080/sbbph/services?wsdl")
public class BPHServicesImplService extends Service {

    private final static URL BPHSERVICESIMPLSERVICE_WSDL_LOCATION;
    private final static WebServiceException BPHSERVICESIMPLSERVICE_EXCEPTION;
    private final static QName BPHSERVICESIMPLSERVICE_QNAME = new QName("http://ws.sbbph/", "BPHServicesImplService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://10.29.202.55:8080/sbbph/services?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        BPHSERVICESIMPLSERVICE_WSDL_LOCATION = url;
        BPHSERVICESIMPLSERVICE_EXCEPTION = e;
    }

    public BPHServicesImplService() {
        super(__getWsdlLocation(), BPHSERVICESIMPLSERVICE_QNAME);
    }

    public BPHServicesImplService(WebServiceFeature... features) {
        super(__getWsdlLocation(), BPHSERVICESIMPLSERVICE_QNAME, features);
    }

    public BPHServicesImplService(URL wsdlLocation) {
        super(wsdlLocation, BPHSERVICESIMPLSERVICE_QNAME);
    }

    public BPHServicesImplService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, BPHSERVICESIMPLSERVICE_QNAME, features);
    }

    public BPHServicesImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public BPHServicesImplService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns BPHServices
     */
    @WebEndpoint(name = "BPHServicesImplPort")
    public BPHServices getBPHServicesImplPort() {
        return super.getPort(new QName("http://ws.sbbph/", "BPHServicesImplPort"), BPHServices.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns BPHServices
     */
    @WebEndpoint(name = "BPHServicesImplPort")
    public BPHServices getBPHServicesImplPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://ws.sbbph/", "BPHServicesImplPort"), BPHServices.class, features);
    }

    private static URL __getWsdlLocation() {
        if (BPHSERVICESIMPLSERVICE_EXCEPTION!= null) {
            throw BPHSERVICESIMPLSERVICE_EXCEPTION;
        }
        return BPHSERVICESIMPLSERVICE_WSDL_LOCATION;
    }

}