
// Imports 
package service.core;

import java.net.*;
import javax.jmdns.*;
import javax.jws.*;
import com.sun.net.httpserver.*;
import java.util.concurrent.Executors;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Endpoint;

/*	
	Dodgy Drivers Service Main Class Job 
		- Find the Host of the network
		- Start up on that host on different port 
		- Register Service Listener created by Broker
*/

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.LITERAL)

public class Quoter extends AbstractQuotationService {
	
	// All references are to be prefixed with an DD (e.g. DD001000)
	public static final String PREFIX = "DD";
	public static final String COMPANY = "Dodgy Drivers Corp.";
	
	/**
	 * Quote generation:
	 * 30% discount for being male
	 * 2% discount per year over 60
	 * 20% discount for less than 3 penalty points
	 * 50% penalty (i.e. reduction in discount) for more than 60 penalty points 
	 */


	/** 
	 * Generates Quotation for the Dodgy Drivers Service using the client's info
	 * @param client_info
	 * @return Quotation
	 */ 
	@WebMethod
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

			// Start up Dodgy Drivers Quotation Service
			Endpoint dodgy_drivers_endpoint = Endpoint.create(new Quoter());
			HttpServer dodgy_drivers_server = HttpServer.create(new InetSocketAddress(9003) , 5);
			dodgy_drivers_server.setExecutor(Executors.newFixedThreadPool(5));
			HttpContext dodgy_drivers_context = dodgy_drivers_server.createContext("/quotation");
			dodgy_drivers_endpoint.publish(dodgy_drivers_context);
			dodgy_drivers_server.start();
			// Endpoint.publish("http://"+host+":9003/quotation", new Quoter());

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

			// Register Dodgy Drivers Service to Listener created by Broker
			ServiceInfo dodgy_drivers_service_info = ServiceInfo.create("_http._tcp.local.", "ddqs", 9003, path);
			jmdns_service_provider.registerService(dodgy_drivers_service_info);

			// Wait to delay connection after startup 
			Thread.sleep(100000);
			
		} catch (Exception e) {

			// Error Handling
			System.out.println("Problem Advertising Service: " + e.getMessage());
			e.printStackTrace();
		}



	} 

}
