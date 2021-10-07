
// Imports 
package service.core;

import java.net.*;
import javax.jmdns.*;
import com.sun.net.httpserver.*;
import java.util.concurrent.Executors;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Endpoint;


/*	
	Auldfellas Service Main Class Job 
		- Find the Host of the network
		- Start up on that host on different port 
		- Register Service Listener created by Broker
*/

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.LITERAL)

public class Quoter extends AbstractQuotationService {
	
	// All references are to be prefixed with an AF (e.g. AF001000)
	public static final String PREFIX = "AF";
	public static final String COMPANY = "Auld Fellas Ltd.";
	
	
	/**
	 * Quote generation:
	 * 30% discount for being male
	 * 2% discount per year over 60
	 * 20% discount for less than 3 penalty points
	 * 50% penalty (i.e. reduction in discount) for more than 60 penalty points 
	 */


	/** 
	 * Generates Quotation for the AuldFellas Service using the client's info
	 * @param client_info
	 * @return Quotation
	 */ 
	public Quotation generateQuotation(ClientInfo client_info) {

		// Create an initial quotation between 600 and 1200
		double price = generatePrice(600, 600);
		
		// Automatic 30% discount for being male
		int discount = (client_info.gender == ClientInfo.MALE) ? 30:0;
		
		// Automatic 2% discount per year over 60...
		discount += (client_info.age > 60) ? (2*(client_info.age-60)) : 0;
		
		// Add a points discount
		discount += getPointsDiscount(client_info.points);
		
		// Generate the quotation and send it back
		return new Quotation(COMPANY, generateReference(PREFIX), (price * (100-discount)) / 100);

	}

	/**
	 * Calculates the discount based on penalty points receieved
	 * @param penalty_points_received
	 * @return integer representing discount recieved
	*/

	private int getPointsDiscount(int penalty_points_received) {

		// 20% discount for less than 3 penalty points
		if (penalty_points_received < 3) return 20;

		// No Discount for 3 - 6 penalty points
		if (penalty_points_received <= 6) return 0;

		// 50% added on for anything over 
		return -50;
	}


	public static void main(String[] args) {

		// Function Variable Declarations
		String host = "";

		try {
			
			// Assigning host to host of the network
			host = args.length > 0 ?  args[0] : "0.0.0.0";

			// Start up Auldfellas Quotation Service
			Endpoint auldfellas_endpoint = Endpoint.create(new Quoter());
			HttpServer auldfellas_server = HttpServer.create(new InetSocketAddress(9001) , 5);
			auldfellas_server.setExecutor(Executors.newFixedThreadPool(5));
			HttpContext auldfellas_context = auldfellas_server.createContext("/quotation");
			auldfellas_endpoint.publish(auldfellas_context);
			auldfellas_server.start();

			// Attach Service to JMDNS Service Listener
			jmdnsAttach(host);
	
		} catch (Exception e) {

			// Error Handling
			e.printStackTrace();

		}
	}

	/**
	 * Attaches the service to the broker's service listener
	 * @param host
	 * @return void
	 */
	private static void jmdnsAttach(String host) {

		// Null check
		host = host != null ? host : "0.0.0.0";

		// Variables for the process
		String path = "path=/quotation?wsdl";

		try {

			// Setting up Service Provider
			JmDNS jmdns_service_provider = JmDNS.create(InetAddress.getLocalHost());

			// Register Auldfellas Service to Listener created by Broker
			ServiceInfo auldfellas_service_info = ServiceInfo.create("_http._tcp.local.", "afqs", 9001, path);
			jmdns_service_provider.registerService(auldfellas_service_info);

			// Wait to delay connection after startup 
			Thread.sleep(100000);
			
		} catch (Exception e) {

			// Error Handling
			System.out.println("Problem Advertising Service: " + e.getMessage());
			e.printStackTrace();
		}



	} 

}
