package service;
// Imports
import service.core.ClientInfo;
import service.core.Quotation;
import java.util.List;


public interface LocalBrokerService{

    /**
     * Creating Function Method for Broker to use to get all services quotations for a specific client
     * @return List Of All Quotations for specific client from the different services
     */
    List<Quotation> getQuotations(ClientInfo info);
}
