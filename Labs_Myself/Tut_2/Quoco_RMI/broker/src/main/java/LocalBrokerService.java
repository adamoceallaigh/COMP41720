
// Imports
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;
import core.*;


public class LocalBrokerService implements BrokerService, Serializable{

    // Variable Declarations
    Registry service_registry;

    // Constructor
    public LocalBrokerService (Registry service_registry_init) {
        this.service_registry = service_registry_init;
    }

    /**
     * Creating Function Method for Broker to use to get all services quotations for a specific client
     * @param client_info - Data to determine the proper quotation client should receive
     * @return List Of All Quotations for specific client from the different services
     */
    @Override
    public List<Quotation> getQuotations(ClientInfo info) throws RemoteException, NotBoundException {

        // Function Variable Declarations
        List<Quotation> quotations = new LinkedList<Quotation>();
		
        // Loop through services in the registery, gather quotations for each
		for (String name : service_registry.list()) {
			if (name.startsWith("qs-")) {
				QuotationService service = (QuotationService) service_registry.lookup(name);
				quotations.add(service.generateQuotation(info));
			}
		}

		return quotations;
    }
    
}
