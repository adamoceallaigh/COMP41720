
// Imports
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

public class AFQServer {
   
    public static void main(String[] args) {

      // Variable Declarations
     QuotationService afqService = (QuotationService) new AFQService();
     String host = "localhost";

     try {
        
        // Connect to the RMI Registry already created by the broker
        Registry registry = null;

        // Check if argument passed in, if not use localhost for registry
        if(args.length != 0 ) host = args[0]; 
        registry = LocateRegistry.getRegistry(host, 1099); 

        // Export the stub for the Auldfellas Quotation Service object
        QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(afqService,0);

        // Ask the broker to register and label the AFService with registry
        BrokerService broker_service = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);
        broker_service.registerService(Constants.AULD_FELLAS_SERVICE, quotationService);

        
        // Signalling that server is operational
        System.out.println("Stopping Server Shutdown");

        // Putting thread to sleep in between connections
        while (true) {Thread.sleep(1000); }

     } catch (Exception e) {
      
         // Error Handling
        System.out.println("Trouble: " + e);

     }
    }

}
