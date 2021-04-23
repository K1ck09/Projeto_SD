package edu.ufp.inf.sd.client;

import edu.ufp.inf.sd.server.Operations;
import edu.ufp.inf.sd.server.State;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WorkerRI extends Remote {
    void setOperation(Operations operation) throws RemoteException;
    void resumeWorker() throws RemoteException;
    void pauseWorker() throws RemoteException;
    void deleteWorker() throws RemoteException;
    public Integer getId() throws RemoteException;
    public State getState()throws RemoteException;
}
