
// Imports
package service.core;

import javax.jws.WebService;
import java.util.List;

// Interface to be implememted by broker
@WebService
public interface BrokerService {
    /**
     * Method to get the list of services
     *
     * @return
     */
    public List<Quotation> getQuotations(ClientInfo info);
}
