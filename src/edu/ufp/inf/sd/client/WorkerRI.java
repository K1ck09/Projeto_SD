package edu.ufp.inf.sd.client;

import edu.ufp.inf.sd.server.State;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WorkerRI extends Remote {
    void setOperation(Operations operation) throws RemoteException;

    void resumeWorker() throws RemoteException;

    void pauseWorker() throws RemoteException;

    void deleteWorker() throws RemoteException;

    Integer getId() throws RemoteException;

    State getState() throws RemoteException;

    String getOwner() throws RemoteException;

    String getJobGroupName() throws RemoteException;

    JobShopClient getClient() throws RemoteException;

    int getTotalRewarded() throws RemoteException;
}
