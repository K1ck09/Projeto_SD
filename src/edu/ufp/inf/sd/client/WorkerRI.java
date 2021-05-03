package edu.ufp.inf.sd.client;

import edu.ufp.inf.sd.server.JobGroupRI;
import edu.ufp.inf.sd.server.JobThread;
import edu.ufp.inf.sd.server.State;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WorkerRI extends Remote {
    void setOperation(String filename, JobThread jobThread) throws RemoteException, IOException;

    void setOperation()throws RemoteException,IOException;

    void updateMakeSpan(int makespan) throws RemoteException, IOException;
    public int getBestMakespan()throws RemoteException;
    public int getTotalShares()throws RemoteException;
    public int getCurrentMakespan()throws RemoteException;
    void resumeWorker() throws RemoteException;
    void pauseWorker() throws RemoteException;
    void deleteWorker() throws RemoteException;
    public Integer getId() throws RemoteException;
    public State getState()throws RemoteException;
    public String getOwner()throws RemoteException;
    public String getJobGroupName()throws RemoteException;
    public JobShopClient getClient()throws RemoteException;
    public int getTotalRewarded()throws RemoteException;
    void setOperation(String filePath) throws RemoteException, IOException;
}
