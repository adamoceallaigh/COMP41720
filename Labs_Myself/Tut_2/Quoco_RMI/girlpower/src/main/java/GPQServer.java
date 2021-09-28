
// Imports
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import girlpower.GPQService;
import service.core.QuotationService;
import service.core.Constants;


public class GPQServer {
    public static void main(String[] args) {

         // Variable Declarations
        QuotationService gpqService = new GPQService();
   
        try {

           // Connect to the RMI Registry already created by the broker
           Registry registry = null;
           registry = LocateRegistry.getRegistry("localhost", 1099);
           
           // Export the stub for the Girlpower Quotation Service object
           QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(gpqService,0);
   
           // Register and Label the object with the RMI Registry 
           registry.bind(Constants.GIRL_POWER_SERVICE, quotationService);
           
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
