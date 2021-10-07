
// Imports
package service.core;

import java.io.IOException;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import java.util.*;
import java.net.*;
import javax.jmdns.*;
import javax.xml.ws.*;



/* Broker's Job
	- Set up itself and Listener on 9000
	- Get Quotations
*/

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)


public class Broker {

	// Property Declarations
	static LinkedList<URL> services = new LinkedList<URL>();

	// Constructor(s)
	public Broker(LinkedList<URL> services_init){
		Broker.services = services_init;
	}

	public Broker(){}

	public static void main(String[] args) {

		try {

			// Broker Starts up on port 9000
			Endpoint.publish("http://0.0.0.0:9000/broker", new Broker(services));

			// Setting up JmDNS provider 
			JmDNS jmdns_service_provider = JmDNS.create(InetAddress.getLocalHost());

			// Adding the Service Listener on 9000 - which advertises to other services on the network it's whereabouts
			jmdns_service_provider.addServiceListener("_http._tcp.local.", new WSDLServiceListener());

			// Wait to delay connection after startup 
			Thread.sleep(30000);


			
		} catch (IOException | InterruptedException e) {
			
			// Error Handling	
            System.out.println(e.getMessage());

        } 
	}


	public static class WSDLServiceListener implements ServiceListener{

		@Override
		public void serviceAdded(ServiceEvent event) {

			// System.out.println(event);
			
		}

		@Override
		public void serviceRemoved(ServiceEvent event) {

			// System.out.println(event);

		}

		@Override
		public void serviceResolved(ServiceEvent event) {
			
			// Null check 
			String service_url = event.getInfo().getNiceTextString() != null ? event.getInfo().getURLs()[0] : "";

			// Variables for process
			String path = event.getInfo().getPropertyString("path") != null ? event.getInfo().getPropertyString("path") : "/quotation?wsdl";

			try {
				
				// Add path to each URL
				service_url += path;

				// Printing out the IP address of the services for error handling
				System.out.println(service_url);

				// Register Service_url to Broker Service Listener
				services.add(new URL(service_url));

			} catch (MalformedURLException e) {

				// Error Handling
				e.printStackTrace();

			}
		}

	}

	public List<Quotation> getQuotations(ClientInfo info) {

		// 	Variables for process
        List<Quotation> quotations = new LinkedList<Quotation>();
		
		/* Loop through all service urls 
			- Create request to get Quoter Service Class
			- Use Quoter Service instance method to generate Quotation from each service
			- Add quotation to quotations array to be outputted back in the client
		*/
		for (URL wsdlUrl : services) {
            QName serviceName = new QName("http://core.service/", "QuoterService");
            Service service = Service.create(wsdlUrl, serviceName);

            QName portName = new QName("http://core.service/", "QuoterPort");
            QuoterService serviceQuote = service.getPort(portName, QuoterService.class);
            quotations.add( serviceQuote.generateQuotation(info) );
        }

		return quotations;


	}
}
