package service.core;


import java.rmi.AccessException;
// Imports and Variable Declarations
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class ServiceRegistryInit implements ServiceRegistry {

    Registry registry;
    
    public ServiceRegistryInit(Registry registry_init) {
        this.registry = registry_init;
    }

    @Override
    public void bind(String name, Remote service) throws AlreadyBoundException, AccessException, RemoteException {
        registry.bind(name, service);
        
    }

    @Override
    public void unbind(String name) throws RemoteException, NotBoundException {
        registry.unbind(name);
    }

    @Override
    public Remote lookup(String name) throws RemoteException, NotBoundException {
        return registry.lookup(name);
    }

    @Override
    public String[] list() throws RemoteException {
        return registry.list();
    }

    
    
}
