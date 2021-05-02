package edu.ufp.inf.sd.server;

import edu.ufp.inf.sd.client.WorkerImpl;
import edu.ufp.inf.sd.client.WorkerRI;

import java.io.IOException;
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
    void attachWorker(WorkerRI worker) throws RemoteException, IOException;
    public Map<Integer, WorkerRI> getJobWorkers()throws RemoteException;
    String getWorkload()throws RemoteException;
    String getSharesPerWorker() throws RemoteException;

    void uploadFile(byte[] mydata) throws RemoteException, IOException;
    byte[] downloadFileFromServer(String serverpath) throws RemoteException, IOException;

    void updateTotalShares(int totalShares, WorkerRI worker)throws RemoteException;
}
