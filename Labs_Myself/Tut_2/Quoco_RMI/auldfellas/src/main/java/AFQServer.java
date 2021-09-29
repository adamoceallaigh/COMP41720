
// Imports
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import auldfellas.AFQService;
import core.*;

public class AFQServer {
   
    public static void main(String[] args) {

      // Variable Declarations
     QuotationService afqService = (QuotationService) new AFQService();

     try {
        
        // Connect to the RMI Registry already created by the broker
        Registry registry = null;
        registry = LocateRegistry.getRegistry("localhost", 1099); 

        // Export the stub for the Auldfellas Quotation Service object
        QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(afqService,0);

        // Register and Label the object with the RMI Registry 
        registry.bind(Constants.AULD_FELLAS_SERVICE, quotationService);
        
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
