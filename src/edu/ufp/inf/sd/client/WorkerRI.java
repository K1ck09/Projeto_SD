package edu.ufp.inf.sd.client;

import edu.ufp.inf.sd.server.JobGroupRI;
import edu.ufp.inf.sd.server.JobThread;
import edu.ufp.inf.sd.server.State;
import edu.ufp.inf.sd.server.User;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WorkerRI extends Remote {
    void setOperation(String filename, JobThread jobThread) throws RemoteException, IOException;

    void setOperation() throws RemoteException, IOException;

    void updateMakeSpan(int makespan) throws RemoteException, IOException;

    int getBestMakespan() throws RemoteException;

    int getTotalShares() throws RemoteException;

    int getCurrentMakespan() throws RemoteException;

    void resumeWorker() throws RemoteException;

    void pauseWorker() throws RemoteException;

    void deleteWorker() throws RemoteException;

    Integer getId() throws RemoteException;

    State getState() throws RemoteException;

    User getOwner() throws RemoteException;

    String getJobGroupName() throws RemoteException;

    JobShopClient getClient() throws RemoteException;

    int getTotalRewarded() throws RemoteException;

    void setOperation(String filePath) throws RemoteException, IOException;

    void updateWorkerController() throws RemoteException, IOException;

    void setTotalShares(int totalShares) throws RemoteException;

    void setTotalRewarded(int totalRewarded) throws RemoteException;

    void setState(String paused) throws IOException;
}
