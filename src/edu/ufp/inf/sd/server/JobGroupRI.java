package edu.ufp.inf.sd.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JobGroupRI extends Remote {
    String getJobName()throws RemoteException;
    String getJobOwner()throws RemoteException;
    String getJobStrat()throws RemoteException;
    String getJobReward()throws RemoteException;
    State getJobState()throws RemoteException;
    Integer getWorkersSize()throws RemoteException;
    String getState() throws RemoteException;
}
