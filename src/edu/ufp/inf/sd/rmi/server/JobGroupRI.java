package edu.ufp.inf.sd.rmi.server;
import edu.ufp.inf.sd.rmi.client.JobController;
import edu.ufp.inf.sd.rmi.client.JobControllerRI;
import edu.ufp.inf.sd.rmi.client.WorkerRI;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.Map;

public interface JobGroupRI extends Remote {
    String getJobName()throws RemoteException;
    String getJobOwner()throws RemoteException;
    String getJobStrat()throws RemoteException;
    State getJobState()throws RemoteException;
    Integer getWorkersSize()throws RemoteException;
    String getState() throws RemoteException;
    boolean attachWorker(WorkerRI worker) throws IOException;
    Map<Integer, WorkerRI> getJobWorkers()throws RemoteException;
    String getWorkload()throws RemoteException;

    String getBestResut() throws RemoteException;

    Integer getTotalShares() throws RemoteException;

    void uploadFile(byte[] mydata) throws IOException;
    byte[] downloadFileFromServer(String serverpath) throws IOException;
    void updateTotalShares(WorkerRI worker)throws IOException;
    WorkerRI getBestResult() throws RemoteException;
    void addToList(JobControllerRI jobController,String user) throws IOException;

    void removeFromList(String username)throws RemoteException;
    void updateList() throws IOException;

    void removeWorker(WorkerRI selectedWorker) throws IOException;

    int getIdsSize() throws RemoteException;

    void setState(String state) throws IOException;

    boolean removeAllWorkers() throws IOException;

    String getJobReward() throws RemoteException;
}
