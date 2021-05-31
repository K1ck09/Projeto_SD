package edu.ufp.inf.sd.rmi.client;

import edu.ufp.inf.sd.rmi.server.State;
import edu.ufp.inf.sd.rmi.server.User;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WorkerRI extends Remote {

    void setOperation() throws IOException;

    void updateMakeSpan(Integer makespan) throws IOException;

    Integer getBestMakespan() throws RemoteException;

    Integer getTotalShares() throws RemoteException;

    Integer getCurrentMakespan() throws RemoteException;

    Integer getId() throws RemoteException;

    State getState() throws RemoteException;

    User getOwner() throws RemoteException;

    String getJobGroupName() throws RemoteException;

    JobShopClient getClient() throws RemoteException;

    Integer getTotalRewarded() throws RemoteException;

    void setFile(String filePath) throws IOException;

    void setTotalShares(Integer totalShares) throws RemoteException;

    void setTotalRewarded(Integer totalRewarded) throws RemoteException;

    void changeState(String state) throws IOException;

    void setState(String state) throws IOException;
}
