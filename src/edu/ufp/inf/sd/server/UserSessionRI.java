package edu.ufp.inf.sd.server;

import edu.ufp.inf.sd.client.WorkerRI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public interface UserSessionRI extends Remote {
   HashMap<String, JobGroupRI> createJob(HashMap<String,String> item) throws RemoteException;
   String getUsername() throws RemoteException;
   String getCredits() throws RemoteException;
    void setCredtis(int credits)throws RemoteException;
    HashMap<String,JobGroupRI> getJobList()throws RemoteException;
    void logout() throws RemoteException;
    boolean isJobUnique(String jobName) throws RemoteException;
    Map<String, WorkerRI> getWorkersMap(JobGroupRI jobGroupRI) throws RemoteException ;
}
