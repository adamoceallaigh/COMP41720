package service.core;
// Imports
import java.rmi.NotBoundException;
import java.util.List;

/**
 * Interface for defining the behaviours of the broker service
 * @author Rem
 *
 */
public interface BrokerService extends java.rmi.Remote {
	public List<Quotation> getQuotations(ClientInfo info) throws java.rmi.RemoteException, NotBoundException;
	public void registerService(String name, java.rmi.Remote service) throws java.rmi.RemoteException, NotBoundException;
}
