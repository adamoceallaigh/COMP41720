package service.core;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
// Imports and Variable Declarations
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceRegistry {
	
	void bind(String name, Remote Remote) throws java.rmi.AlreadyBoundException, AccessException, RemoteException;
	
	void unbind(String name) throws java.rmi.RemoteException, NotBoundException;

	Remote lookup(String name) throws java.rmi.RemoteException, NotBoundException;

	String[] list() throws java.rmi.RemoteException;
    
}
