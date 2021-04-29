package edu.ufp.inf.sd.server;

import edu.ufp.inf.sd.client.WorkerRI;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface JobGroupRI extends Remote {
    String getJobName()throws RemoteException;
    String getJobOwner()throws RemoteException;
    String getJobStrat()throws RemoteException;
    String getJobReward()throws RemoteException;
    State getJobState()throws RemoteException;
    Map<Integer, WorkerRI> getJobWorkers()throws RemoteException;
    String getWorkload()throws RemoteException;
    String getSharesPerWorker() throws RemoteException;
    Integer getWorkersSize()throws RemoteException;

    byte[] downloadFileFromServer(String serverpath) throws RemoteException;

    void uploadFile(byte[] mydata) throws RemoteException, IOException;

    void attachWorker(WorkerRI worker) throws RemoteException;
    String getState() throws RemoteException;
    void setState(State state)throws RemoteException;
    void detachWorker(WorkerRI worker) throws RemoteException;
}
