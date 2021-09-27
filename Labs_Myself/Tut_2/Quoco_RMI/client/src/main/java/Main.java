// Imports and Variable Declarations

import service.core.BrokerService;
import service.core.ClientInfo;
import service.core.Quotation;
import service.core.Constants;
import service.core.ServiceRegistry;
// import broker.LocalBrokerService;
// import auldfellas.AFQService;
// import dodgydrivers.DDQService;
// import girlpower.GPQService;

public class Main {
   
    Registry registry;
	
	static {
		BrokerService local_broker_service = new LocalBrokerService();
		QuotationService afq_service = new AFQService();
		QuotationService ddq_service = new DDQService();
		QuotationService gpq_service = new GPQService();
		try {
			registry = LocateRegistry.createRegistry(1099);
			BrokerService broker_service = (BrokerService) UnicastRemoteObject.exportObject(local_broker_service,0);
			QuotationService quotation_service_afq = (QuotationService) UnicastRemoteObject.exportObject(afq_service, 0);
			QuotationService quotation_service_ddq = (QuotationService) UnicastRemoteObject.exportObject(afq_service, 0);
			QuotationService quotation_service_gpq = (QuotationService) UnicastRemoteObject.exportObject(afq_service, 0);
			
			registry.bind(Constants.BROKER_SERVICE , broker_service);
			registry.bind(Constants.AULD_FELLAS_SERVICE, quotation_service_afq);
			registry.bind(Constants.DODGY_DRIVERS_SERVICE, quotation_service_ddq);
			registry.bind(Constants.GIRL_POWER_SERVICE, quotation_service_gpq);
			

		} catch (RemoteException e | AlreadyBoundException e) {
			e.printStackTrace();
		}
	}
	
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
	public static void main(String[] args) {
		BrokerService brokerService = ServiceRegistry.lookup(Constants.BROKER_SERVICE, BrokerService.class);
 
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
	}
}
