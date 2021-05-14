package edu.ufp.inf.sd.rmq.client;

import edu.ufp.inf.sd.rmq.server.State;
import edu.ufp.inf.sd.rmq.server.User;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WorkerRI extends Remote {

    void setOperation() throws IOException;

    void updateMakeSpan(int makespan) throws IOException;

    int getBestMakespan() throws RemoteException;

    int getTotalShares() throws RemoteException;

    int getCurrentMakespan() throws RemoteException;

    Integer getId() throws RemoteException;

    State getState() throws RemoteException;

    User getOwner() throws RemoteException;

    String getJobGroupName() throws RemoteException;

    JobShopClient getClient() throws RemoteException;

    int getTotalRewarded() throws RemoteException;

    void setFile(String filePath) throws IOException;

    void setTotalShares(int totalShares) throws RemoteException;

    void setTotalRewarded(int totalRewarded) throws RemoteException;

    void changeState(String state) throws IOException;

    void setState(String state) throws IOException;
}
