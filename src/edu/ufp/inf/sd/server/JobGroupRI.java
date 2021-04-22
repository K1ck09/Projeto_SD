package edu.ufp.inf.sd.server;

import edu.ufp.inf.sd.client.WorkerRI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public interface JobGroupRI extends Remote {
    String getJobName()throws RemoteException;
    String getJobOwner()throws RemoteException;
    String getJobStrat()throws RemoteException;
    String getJobReward()throws RemoteException;
    State getJobState()throws RemoteException;
    Integer getWorkersSize()throws RemoteException;
    String getState() throws RemoteException;
    void attachWorker(WorkerRI worker) throws RemoteException;
    public Map<Integer, WorkerRI> getJobWorkers()throws RemoteException;

}
