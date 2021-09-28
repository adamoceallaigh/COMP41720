// Imports and Variable Declarations
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;

import service.core.BrokerService;
import service.core.ClientInfo;
import service.core.Quotation;
import service.core.QuotationService;


public class LocalBrokerService implements BrokerService, Serializable{

    Registry service_registry;

    public LocalBrokerService (Registry service_registry_init) {
        this.service_registry = service_registry_init;
    }

    @Override
    public List<Quotation> getQuotations(ClientInfo info) throws RemoteException, NotBoundException {
        List<Quotation> quotations = new LinkedList<Quotation>();
		
		for (String name : service_registry.list()) {
			if (name.startsWith("qs-")) {
				QuotationService service = (QuotationService) service_registry.lookup(name);
				quotations.add(service.generateQuotation(info));
			}
		}

		return quotations;
    }
    
}
