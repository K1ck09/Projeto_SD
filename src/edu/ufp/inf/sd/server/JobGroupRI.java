package edu.ufp.inf.sd.server;

import edu.ufp.inf.sd.client.JobController;
import edu.ufp.inf.sd.client.JobControllerRI;
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
    Integer getWorkersSize()throws RemoteException;
    String getState() throws RemoteException;
    boolean attachWorker(WorkerRI worker) throws RemoteException, IOException;
    Map<Integer, WorkerRI> getJobWorkers()throws RemoteException;
    String getWorkload()throws RemoteException;

    String getBestResut() throws RemoteException;

    Integer getTotalShares() throws RemoteException;

    void uploadFile(byte[] mydata) throws RemoteException, IOException;
    byte[] downloadFileFromServer(String serverpath) throws RemoteException, IOException;
    void updateTotalShares(WorkerRI worker)throws RemoteException, IOException;
    WorkerRI getBestResult() throws RemoteException;
    void addToList(JobControllerRI jobController,String user) throws IOException;

    void removeFromList(String username)throws RemoteException;
    void updateList() throws IOException;

    void removeWorker(WorkerRI selectedWorker) throws RemoteException;

    int getIdsSize() throws RemoteException;
}
