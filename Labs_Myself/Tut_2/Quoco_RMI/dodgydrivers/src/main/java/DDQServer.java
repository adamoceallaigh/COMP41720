
// Imports
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import core.*;
import dodgydrivers.DDQService;

public class DDQServer {
   
    public static void main(String[] args) {

      // Variable Declarations
     QuotationService ddqService = (QuotationService) new DDQService();

     try {

        // Connect to the RMI Registry already created by the broker
        Registry registry = null;
        registry = LocateRegistry.getRegistry("localhost", 1099);

        // Export the stub for the DodgyDrivers Quotation Service object
        QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(ddqService,0);

        // Register and Label the object with the RMI Registry 
        registry.bind(Constants.DODGY_DRIVERS_SERVICE, quotationService);
        
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
