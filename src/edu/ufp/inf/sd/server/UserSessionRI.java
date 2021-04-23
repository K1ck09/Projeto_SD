package edu.ufp.inf.sd.server;

import edu.ufp.inf.sd.client.WorkerRI;

import java.io.File;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public interface UserSessionRI extends Remote {
   String getUsername() throws RemoteException;
   String getCredits() throws RemoteException;
    void setCredtis(int credits)throws RemoteException;
    Map<String, JobGroupRI> getJobList()throws RemoteException;
    void logout() throws RemoteException;
    boolean isJobUnique(String jobName) throws RemoteException;
    Map<Integer, WorkerRI> getWorkersMap(String jobGroupRI) throws RemoteException ;
    Map<String, JobGroupRI> createJob(HashMap<String, String> item) throws RemoteException;
}
