
// Imports and Variable Declarations
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import auldfellas.AFQService;
import service.core.QuotationService;
import service.core.Constants;

public class AFQServer {
   
    public static void main(String[] args) {
     QuotationService afqService = new AFQService();

     try {
        
        // Connect to the RMI Registry - creating the registry will be the 
        // responsibility of the broker.
        Registry registry = null;
        registry = LocateRegistry.getRegistry("localhost", 1099); 

        // Create the Remote Object
        QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(afqService,0);

        // Register the object with the RMI Registry
        registry.bind(Constants.AULD_FELLAS_SERVICE, quotationService);
        
        System.out.println("STOPPING SERVER SHUTDOWN");

        // Putting thread to sleep in between connections
        while (true) {Thread.sleep(1000); }

     } catch (Exception e) {
        System.out.println("Trouble: " + e);
     }
    }

}
