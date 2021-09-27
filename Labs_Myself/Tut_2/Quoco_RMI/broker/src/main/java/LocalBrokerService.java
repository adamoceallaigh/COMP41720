// Imports and Variable Declarations
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;

import service.core.BrokerService;
import service.core.ClientInfo;
import service.core.Quotation;
import service.core.QuotationService;
import service.core.ServiceRegistry;


public class LocalBrokerService implements BrokerService{

    ServiceRegistry service_registry;

    public LocalBrokerService (ServiceRegistry service_registry_init) {
        this.service_registry = service_registry_init;
    }

    @Override
    public List<Quotation> getQuotations(ClientInfo info) throws RemoteException {
        List<Quotation> quotations = new LinkedList<Quotation>();
		
		for (String name : service_registry.list()) {
			if (name.startsWith("qs-")) {
				QuotationService service = service_registry.lookup(name, QuotationService.class);
				quotations.add(service.generateQuotation(info));
			}
		}

		return quotations;
    }
    
}
