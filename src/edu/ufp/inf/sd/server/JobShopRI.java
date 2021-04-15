package edu.ufp.inf.sd.server;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface JobShopRI extends Remote {
    int runTS(String jsspInstance) throws RemoteException;
}
