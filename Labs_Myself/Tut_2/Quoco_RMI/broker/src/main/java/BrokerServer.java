
// Imports and Variable Declarations
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

import service.core.BrokerService;
import service.core.Constants;
import service.core.QuotationService;

public class BrokerServer {
   
    public static void main(String[] args) {

     try {
        
        // Connect to the RMI Registry - creating the registry will be the 
        // responsibility of the broker.
        Registry registry = null;
        registry = LocateRegistry.createRegistry(1099);
      

        // Create the Remote Object
        LocalBrokerService broker_service = new LocalBrokerService(registry);

        BrokerService quotation_service_broker = (BrokerService) UnicastRemoteObject.exportObject(broker_service,0);

        // Register the object with the RMI Registry
        registry.bind(Constants.BROKER_SERVICE, quotation_service_broker);
        
        System.out.println("STOPPING SERVER SHUTDOWN");

        // Putting thread to sleep in between connections
        while (true) {Thread.sleep(1000); }

     } catch (Exception e) {
        System.out.println("Trouble: " + e);
     }
    }

}
