// Imports and Variable Declarations

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.NumberFormat;

import service.core.BrokerService;
import service.core.ClientInfo;
import service.core.Quotation;
import service.core.QuotationService;
import service.core.Constants;
// import auldfellas.AFQService;
// import dodgydrivers.DDQService;
// import girlpower.GPQService;
// import broker.*;

public class Main {
   
	
	// static {
	// 	BrokerService local_broker_service = new LocalBrokerService();
	// 	QuotationService afq_service = new AFQService();
	// 	QuotationService ddq_service = new DDQService();
	// 	QuotationService gpq_service = new GPQService();
	// 	try {
	// 		BrokerService broker_service = (BrokerService) UnicastRemoteObject.exportObject(local_broker_service,0);
	// 		QuotationService quotation_service_afq = (QuotationService) UnicastRemoteObject.exportObject(afq_service, 0);
	// 		QuotationService quotation_service_ddq = (QuotationService) UnicastRemoteObject.exportObject(afq_service, 0);
	// 		QuotationService quotation_service_gpq = (QuotationService) UnicastRemoteObject.exportObject(afq_service, 0);
			
	// 		registry.bind(Constants.BROKER_SERVICE , broker_service);
	// 		registry.bind(Constants.AULD_FELLAS_SERVICE, quotation_service_afq);
	// 		registry.bind(Constants.DODGY_DRIVERS_SERVICE, quotation_service_ddq);
	// 		registry.bind(Constants.GIRL_POWER_SERVICE, quotation_service_gpq);
			

	// 	} catch (RemoteException | AlreadyBoundException e) {
	// 		e.printStackTrace();
	// 	}
	// }
	
	/**
	 * This is the starting point for the application. Here, we must
	 * get a reference to the Broker Service and then invoke the
	 * getQuotations() method on that service.
	 * 
	 * Finally, you should print out all quotations returned
	 * by the service.
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		Registry registry = null;
        registry = LocateRegistry.getRegistry("localhost", 1099);
        

		try{
			BrokerService brokerService = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);

			// Create the broker and run the test data
			for (ClientInfo info : clients) {
				displayProfile(info);
				
				// Retrieve quotations from the broker and display them...
				for(Quotation quotation : brokerService.getQuotations(info)) {
					displayQuotation(quotation);
				}
				
				// Print a couple of lines between each client
				System.out.println("\n");
			}

		} catch (NotBoundException | RemoteException e){
			System.out.println(e);
		}
 
	}


	/**
	 * Display the client info nicely.
	 * 
	 * @param info
	 */
	public static void displayProfile(ClientInfo info) {
		System.out.println("|=================================================================================================================|");
		System.out.println("|                                     |                                     |                                     |");
		System.out.println(
				"| Name: " + String.format("%1$-29s", info.name) + 
				" | Gender: " + String.format("%1$-27s", (info.gender==ClientInfo.MALE?"Male":"Female")) +
				" | Age: " + String.format("%1$-30s", info.age)+" |");
		System.out.println(
				"| License Number: " + String.format("%1$-19s", info.licenseNumber) + 
				" | No Claims: " + String.format("%1$-24s", info.noClaims+" years") +
				" | Penalty Points: " + String.format("%1$-19s", info.points)+" |");
		System.out.println("|                                     |                                     |                                     |");
		System.out.println("|=================================================================================================================|");
	}

	/**
	 * Display a quotation nicely - note that the assumption is that the quotation will follow
	 * immediately after the profile (so the top of the quotation box is missing).
	 * 
	 * @param quotation
	 */
	public static void displayQuotation(Quotation quotation) {
		System.out.println(
				"| Company: " + String.format("%1$-26s", quotation.company) + 
				" | Reference: " + String.format("%1$-24s", quotation.reference) +
				" | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.price))+" |");
		System.out.println("|=================================================================================================================|");
	}
	
	/**
	 * Test Data
	 */
	public static final ClientInfo[] clients = {
		new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 0, 5, "PQR254/1"),
		new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 0, 2, "ABC123/4"),
		new ClientInfo("Hannah Montana", ClientInfo.FEMALE, 16, 10, 0, "HMA304/9"),
		new ClientInfo("Rem Collier", ClientInfo.MALE, 44, 5, 3, "COL123/3"),
		new ClientInfo("Jim Quinn", ClientInfo.MALE, 55, 4, 7, "QUN987/4"),
		new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 5, 2, "XYZ567/9")		
	};
}
