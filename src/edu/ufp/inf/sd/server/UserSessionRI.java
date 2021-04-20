package edu.ufp.inf.sd.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface UserSessionRI extends Remote {
   HashMap<String, JobGroupRI> createJob(HashMap<String,String> item) throws RemoteException;
   String getUsername() throws RemoteException;
   String getCredits() throws RemoteException;
    void setCredtis(int credits)throws RemoteException;
    HashMap<String,JobGroupRI> getJobList()throws RemoteException;
    void logout() throws RemoteException;
}
