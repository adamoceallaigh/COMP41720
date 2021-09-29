
// Imports 
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import core.*;

public class BrokerServer {
   
    public static void main(String[] args) {

     try {
        
        // Creating the RMI registry
        Registry registry = null;
        registry = LocateRegistry.createRegistry(1099);
      
         // Instanting new BrokerService , passing in registery just created
        LocalBrokerService broker_service = new LocalBrokerService(registry);
        
        // Export the stub for the Broker Service object
        BrokerService quotation_service_broker = (BrokerService) UnicastRemoteObject.exportObject(broker_service,0);

        // Register and Label the object with the RMI Registry 
        registry.bind(Constants.BROKER_SERVICE, quotation_service_broker);
        
        // Signalling that broker server is operational
        System.out.println("Stopping Server Shutdown");

        // Putting thread to sleep in between connections
        while (true) {Thread.sleep(1000); }

      } catch (Exception e) {
      
         // Error Handling
        System.out.println("Trouble: " + e);

     }
    }

}
