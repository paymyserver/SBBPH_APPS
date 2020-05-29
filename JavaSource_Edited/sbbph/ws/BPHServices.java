package sbbph.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;

import org.tempuri.crsservice.JpnManager;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.2.4-b01 Generated
 * source version: 2.2
 * 
 */
@WebService(name = "BPHServices", targetNamespace = "http://ws.sbbph/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@XmlSeeAlso({ org.tempuri.crsservice.ObjectFactory.class,
		sbbph.ws.ObjectFactory.class })
public interface BPHServices {

	/**
	 * 
	 * @param arg0
	 * @return returns sbbph.ws.Users
	 */
	@WebMethod
	@WebResult(partName = "return")
	@Action(input = "http://ws.sbbph/BPHServices/getUserDataRequest", output = "http://ws.sbbph/BPHServices/getUserDataResponse")
	public Users getUserData(
			@WebParam(name = "arg0", partName = "arg0") String arg0);

	/**
	 * 
	 * @param arg3
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 */
	@WebMethod
	@Action(input = "http://ws.sbbph/BPHServices/sendEmailRequest", output = "http://ws.sbbph/BPHServices/sendEmailResponse")
	public void sendEmail(
			@WebParam(name = "arg0", partName = "arg0") String arg0,
			@WebParam(name = "arg1", partName = "arg1") String arg1,
			@WebParam(name = "arg2", partName = "arg2") String arg2,
			@WebParam(name = "arg3", partName = "arg3") String arg3);

	/**
	 * 
	 * @param arg0
	 * @return returns sbbph.ws.PesaraManager
	 */
	@WebMethod
	@WebResult(partName = "return")
	@Action(input = "http://ws.sbbph/BPHServices/semakanPesaraRequest", output = "http://ws.sbbph/BPHServices/semakanPesaraResponse")
	public PesaraManager semakanPesara(
			@WebParam(name = "arg0", partName = "arg0") String arg0);

	/**
	 * 
	 * @param arg5
	 * @param arg4
	 * @param arg3
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 */
	@WebMethod
	@Action(input = "http://ws.sbbph/BPHServices/sendEmailWithSingleAttachmentRequest", output = "http://ws.sbbph/BPHServices/sendEmailWithSingleAttachmentResponse")
	public void sendEmailWithSingleAttachment(
			@WebParam(name = "arg0", partName = "arg0") String arg0,
			@WebParam(name = "arg1", partName = "arg1") String arg1,
			@WebParam(name = "arg2", partName = "arg2") String arg2,
			@WebParam(name = "arg3", partName = "arg3") String arg3,
			@WebParam(name = "arg4", partName = "arg4") String arg4,
			@WebParam(name = "arg5", partName = "arg5") byte[] arg5);

	/**
	 * 
	 * @param arg0
	 * @return returns java.lang.String
	 */
	@WebMethod
	@WebResult(partName = "return")
	@Action(input = "http://ws.sbbph/BPHServices/getHelloWorldAsStringRequest", output = "http://ws.sbbph/BPHServices/getHelloWorldAsStringResponse")
	public String getHelloWorldAsString(
			@WebParam(name = "arg0", partName = "arg0") String arg0);

	/**
	 * 
	 * @param arg0
	 */
	@WebMethod
	@Action(input = "http://ws.sbbph/BPHServices/showJPNServicesRequest", output = "http://ws.sbbph/BPHServices/showJPNServicesResponse")
	public void showJPNServices(
			@WebParam(name = "arg0", partName = "arg0") String arg0);

	/**
	 * 
	 * @param arg0
	 * @return returns sbbph.ws.HrmisManager
	 */
	@WebMethod
	@WebResult(partName = "return")
	@Action(input = "http://ws.sbbph/BPHServices/semakanPenjawatAwamRequest", output = "http://ws.sbbph/BPHServices/semakanPenjawatAwamResponse")
	public HrmisManager semakanPenjawatAwam(
			@WebParam(name = "arg0", partName = "arg0") String arg0);

	/**
	 * 
	 * @param arg0
	 * @return returns org.tempuri.crsservice.JpnManager
	 */
	@WebMethod
	@WebResult(partName = "return")
	@Action(input = "http://ws.sbbph/BPHServices/retreiveCitizensDataTestRequest", output = "http://ws.sbbph/BPHServices/retreiveCitizensDataTestResponse")
	public JpnManager retreiveCitizensDataTest(
			@WebParam(name = "arg0", partName = "arg0") String arg0);

	/**
	 * 
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return returns org.tempuri.crsservice.JpnManager
	 */
	@WebMethod
	@WebResult(partName = "return")
	@Action(input = "http://ws.sbbph/BPHServices/retreiveCitizensDataRequest", output = "http://ws.sbbph/BPHServices/retreiveCitizensDataResponse")
	public JpnManager retreiveCitizensData(
			@WebParam(name = "arg0", partName = "arg0") String arg0,
			@WebParam(name = "arg1", partName = "arg1") String arg1,
			@WebParam(name = "arg2", partName = "arg2") String arg2);

	/**
	 * 
	 * @param arg1
	 * @param arg0
	 * @return returns sbbph.ws.GoogleManager
	 */
	@WebMethod
	@WebResult(partName = "return")
	@Action(input = "http://ws.sbbph/BPHServices/getDistanceBetweenLocationRequest", output = "http://ws.sbbph/BPHServices/getDistanceBetweenLocationResponse")
	public GoogleManager getDistanceBetweenLocation(
			@WebParam(name = "arg0", partName = "arg0") String arg0,
			@WebParam(name = "arg1", partName = "arg1") String arg1);

	/**
	 * 
	 * @param arg3
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return returns java.lang.String
	 */
	@WebMethod
	@WebResult(partName = "return")
	@Action(input = "http://ws.sbbph/BPHServices/sendEmailWithResultRequest", output = "http://ws.sbbph/BPHServices/sendEmailWithResultResponse")
	public String sendEmailWithResult(
			@WebParam(name = "arg0", partName = "arg0") String arg0,
			@WebParam(name = "arg1", partName = "arg1") String arg1,
			@WebParam(name = "arg2", partName = "arg2") String arg2,
			@WebParam(name = "arg3", partName = "arg3") String arg3);

	/**
	 * 
	 * @param arg0
	 */
	@WebMethod
	@Action(input = "http://ws.sbbph/BPHServices/testDBPersistenceRequest", output = "http://ws.sbbph/BPHServices/testDBPersistenceResponse")
	public void testDBPersistence(
			@WebParam(name = "arg0", partName = "arg0") String arg0);

}
